package com.salestracker.sales.activities;

import android.content.ClipData;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.salestracker.sales.R;
import com.salestracker.sales.application.AppController;
import com.salestracker.sales.uploadservice.AbstractUploadServiceReceiver;
import com.salestracker.sales.uploadservice.CustomFileUtils;
import com.salestracker.sales.uploadservice.UploadRequest;
import com.salestracker.sales.uploadservice.UploadService;
import com.salestracker.sales.utils.PublicMethods;
import com.salestracker.sales.widgets.DynamicImageView;

import de.hdodenhof.circleimageview.CircleImageView;
import jp.wasabeef.blurry.Blurry;
import jp.wasabeef.blurry.internal.BlurFactor;

public class ProfileActivity extends AppCompatActivity {

    private static final int REQUEST_CODE_CHOOSE_PHOTO = 100;
    private static final int REQUEST_CODE_CAPTURE_PHOTO = 101;

    private CircleImageView civProfilePic;
    private DynamicImageView bgProfilePic;

    private BlurFactor blurFactor = new BlurFactor();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        blurFactor.radius = 25;
        blurFactor.sampling = 4;

        civProfilePic = (CircleImageView) findViewById(R.id.civProfilePic);
        bgProfilePic = (DynamicImageView) findViewById(R.id.bgProfilePic);

        findViewById(R.id.ivEditProfilePic).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectActionToTakePicture();
            }
        });

        findViewById(R.id.ivEditProfile).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ProfileActivity.this, ProfileEditActivity.class));
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void selectActionToTakePicture() {

        AlertDialog.Builder builder = new AlertDialog.Builder(ProfileActivity.this) ;
        builder.setTitle("Upload Photo").setItems(new CharSequence[]{"Choose a photo", "Take a photo"},
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        if (which == 0) {
                            selectImageToUpload();
                            dialog.dismiss();
                        } else if (which == 1) {
                            captureImageToUpdate();
                            dialog.dismiss();

                        }

                    }
                });
        AlertDialog alert = builder.create();
        alert.show();

    }

    private CustomFileUtils currentFileUnderUpload;

    private void selectImageToUpload() {

        if (currentFileUnderUpload != null && currentFileUnderUpload.fileStatusEnum == CustomFileUtils.FileStatus.UPLOADING) {
            UploadService.stopCurrentUpload(); // this will also throw java.net.ProtocolException
        } else {
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            String[] mime = {"image/*"};
            intent.setType("*/*");
            intent.putExtra(Intent.EXTRA_MIME_TYPES, mime);
            intent.addCategory(Intent.CATEGORY_OPENABLE);
            intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
            }
            try {
                startActivityForResult(Intent.createChooser(intent, "Choose Image"), REQUEST_CODE_CHOOSE_PHOTO);
            } catch (android.content.ActivityNotFoundException ex) {
                ex.printStackTrace();
            }
        }
    }

    private void captureImageToUpdate() {
        Intent intentPicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intentPicture, REQUEST_CODE_CAPTURE_PHOTO);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE_CHOOSE_PHOTO || requestCode == REQUEST_CODE_CAPTURE_PHOTO) {
            if (resultCode == RESULT_OK) {
                if (data == null)
                    return;
                Uri uri = data.getData();
                if (uri == null && data.getExtras() != null && data.getExtras().get("data") != null && data.getExtras().get("data") instanceof Uri)
                    uri = (Uri) data.getExtras().get("data");
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2 && uri == null) {
                    ClipData clipData = data.getClipData();
                    if (clipData == null)
                        return;
                    int count = clipData.getItemCount();
                    for (int i = 0; i < count; ++i) {
                        uri = clipData.getItemAt(i).getUri();
                        handleFile(uri);
                    }
                } else if (uri != null) {
                    handleFile(uri);
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void handleFile(Uri uri) {
        findViewById(R.id.ivEditProfilePic).setVisibility(View.GONE);
        findViewById(R.id.pbUpdateProfile).setVisibility(View.VISIBLE);
        CustomFileUtils customFileUtils = new CustomFileUtils(this, uri);

        currentFileUnderUpload = customFileUtils;

        upload(this, customFileUtils.path, customFileUtils.size, customFileUtils.name, customFileUtils.uri, customFileUtils.md5_checksum);
    }

    private final AbstractUploadServiceReceiver uploadReceiver =
            new AbstractUploadServiceReceiver() {
                @Override
                public void onProgress(String md5_checksum, int progress) {
                    fileUploadInProgress(md5_checksum, progress);
                }

                @Override
                public void onError(String md5_checksum, Exception exception) {
                    exception.printStackTrace();
                    fileUploadError(md5_checksum);
                }

                @Override
                public void onCompleted(String md5_checksum,
                                        int serverResponseCode,
                                        String serverResponseMessage) {
                    if (serverResponseCode >= 200 && serverResponseCode <= 299) {
                        fileUploadCompleted(md5_checksum, serverResponseMessage);
                    } else {
                        // TODO handle server errors, display appropriate mesaages to user.
                        // example 413 - Request entity too large
                        fileUploadError(md5_checksum);
                    }
                }
            };

    private void fileUploadInProgress(String md5_checksum, int progress) {
        try {
            if (md5_checksum.equalsIgnoreCase(currentFileUnderUpload.md5_checksum))
                currentFileUnderUpload.fileStatusEnum = CustomFileUtils.FileStatus.UPLOADING;
        } catch (Exception npe) {
            npe.printStackTrace();
        }
    }

    private void fileUploadError(String md5_checksum) {
        if (ProfileActivity.this != null) {
            findViewById(R.id.ivEditProfilePic).setVisibility(View.VISIBLE);
            findViewById(R.id.pbUpdateProfile).setVisibility(View.GONE);
        }
        try {
            if (md5_checksum.equalsIgnoreCase(currentFileUnderUpload.md5_checksum)) {
                currentFileUnderUpload.fileStatusEnum = CustomFileUtils.FileStatus.RETRY;
                PublicMethods.showToast("Profile picture update failed");
            }
        } catch (Exception npe) {
            npe.printStackTrace();
        }
        currentFileUnderUpload = null;
    }

    private void fileUploadCompleted(String md5_checksum, String response) {
        if (ProfileActivity.this != null) {
            findViewById(R.id.ivEditProfilePic).setVisibility(View.VISIBLE);
            findViewById(R.id.pbUpdateProfile).setVisibility(View.GONE);
        }
        try {
            if (md5_checksum.equalsIgnoreCase(currentFileUnderUpload.md5_checksum)) {
                PublicMethods.showToast("Profile picture updated");
//                EventBus.getDefault().post(new UserDataUpdated());

                if (ProfileActivity.this != null){
                    ImageLoader imageLoader = AppController.getInstance().getImageLoader();

                    imageLoader.get("", new ImageLoader.ImageListener() {

                        public void onErrorResponse(VolleyError error) {
                            civProfilePic.setImageResource(R.drawable.user_default);

                            bgProfilePic.setImageResource(R.drawable.user_default);

                            new Blurry.ImageComposer(ProfileActivity.this, bgProfilePic, blurFactor, false).into(bgProfilePic);
                        }

                        public void onResponse(ImageLoader.ImageContainer response, boolean arg1) {
                            if (ProfileActivity.this == null)
                                return;
                            if (response.getBitmap() != null) {

                                civProfilePic.setImageBitmap(response.getBitmap());

                                bgProfilePic.setImageBitmap(response.getBitmap());

                                new Blurry.ImageComposer(ProfileActivity.this, bgProfilePic, blurFactor, false).into(bgProfilePic);

                            }
                        }
                    });
                }
            }
        } catch (Exception npe) {
            npe.printStackTrace();
        }

        currentFileUnderUpload = null;
    }

    public void upload(final Context context, String path, long size, String name, Uri uri, String md5_checksum) {
        final UploadRequest request = new UploadRequest(context,
                md5_checksum, // here md5_checksum works as an uploadID, which will be used to identify files.
                AppController.BASE_URL + "/apis/users/");

        request.addFileToUpload(path,
                "profile.profile_picture",
                name,
                context.getContentResolver().getType(uri));

        request.setMethod("PATCH");
        //request.addParameter("attachment_name", "profile.profile_picture");
        /*request.addHeader("Authorization", "Token " + preferenceUtils.getGreymeterAuthToken());*/

        request.setNotificationConfig(android.R.drawable.ic_menu_upload_you_tube,
                "DP upload",
                "Uploading " + name,
                "Uploaded " + name,
                "Failed " + name,
                true);

        try {
            UploadService.startUpload(request);
        } catch (Exception exc) {
            Log.e("AndroidUploadService", exc.getLocalizedMessage(), exc);
        }
    }
}
