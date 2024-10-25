package com.salestrackmobileapp.android.activities;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.view.ContextThemeWrapper;
import android.util.Base64;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.orm.query.Select;

import com.salestrackmobileapp.android.R;
import com.salestrackmobileapp.android.custome_views.Custome_EditText;
import com.salestrackmobileapp.android.custome_views.Custome_TextView;
import com.salestrackmobileapp.android.gson.UserInfoChange;
import com.salestrackmobileapp.android.gson.UserInfoProfile;
import com.salestrackmobileapp.android.networkManager.NetworkManager;
import com.salestrackmobileapp.android.networkManager.ServiceHandler;
import com.salestrackmobileapp.android.utils.CommonUtils;
import com.salestrackmobileapp.android.utils.Connectivity;
import com.salestrackmobileapp.android.utils.PrefsHelper;
import com.salestrackmobileapp.android.utils.SuperclassExclusionStrategy;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class ProfileActivity extends BaseActivity {

    @BindView(R.id.action_bar_title)
    TextView actionBarTitle;
    @BindView(R.id.firstname_tv)
    Custome_TextView userTitleTV;
    @BindView(R.id.email_tv)
    Custome_TextView emailTV;
    @BindView(R.id.phone_tv)
    Custome_TextView phoneTV;
    @BindView(R.id.address_tv)
    Custome_TextView addressTV;
    @BindView(R.id.save_change_btn)
    Button saveChangeBtn;
    @BindView(R.id.pick_image_img)
    ImageView pickImage;

    private static int RESULT_LOAD_IMAGE = 1;

    @BindView(R.id.firstname_et)
    Custome_EditText firstNameET;
    @BindView(R.id.lastname_et)
    Custome_EditText lastNameET;
    @BindView(R.id.email_et)
    Custome_EditText emailET;
    @BindView(R.id.phone_et)
    Custome_EditText phoneET;
    @BindView(R.id.address_et)
    Custome_EditText addressET;
    @BindView(R.id.setting_dots_img)
    ImageView threeDotsImg;

    UserInfoChange userChangeFeilds;
    de.hdodenhof.circleimageview.CircleImageView imageView;

    Bitmap selectedImage;
    String encodedImage;

    private NetworkChangeReceiver receiver;
    private boolean isConnected = false;
    IntentFilter filter;
    boolean isProfile = false;

    static int clickSaveBtn = 0;
    private ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        imageView = (de.hdodenhof.circleimageview.CircleImageView) findViewById(R.id.imgProfilePicture);
        ButterKnife.bind(this);
        Typeface custom_font = Typeface.createFromAsset(getAssets(), "Avenir-Next-LT-Pro_5196.ttf");
        actionBarTitle.setText("PROFILE");
        actionBarTitle.setTypeface(custom_font);

        filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        receiver = new NetworkChangeReceiver();
        registerReceiver(receiver, filter);
        if (Connectivity.isNetworkAvailable(ProfileActivity.this)) {
            if (!isUserProfile) {
                getUserInfo();
            }
        } else {
            UserInfoProfile userInfoProfile = Select.from(UserInfoProfile.class).first();
            if (userInfoProfile.getFirstName() != null) {
                firstNameET.setText(userInfoProfile.getFirstName());
            } else {
                firstNameET.setText(" ");
            }

            if (userInfoProfile.getLastName() != null) {
                lastNameET.setText(userInfoProfile.getLastName());
            } else {
                lastNameET.setText(" ");
            }

            if (userInfoProfile.getEmailID() != null) {
                emailET.setText(userInfoProfile.getEmailID() + " ");
                emailET.setFocusable(false);
            }
            if (userInfoProfile.getMobilePhoneNumber() != null) {
                phoneET.setText(userInfoProfile.getMobilePhoneNumber() + " ");
            }
            if (userInfoProfile.getAddress2() != null) {
                addressET.setText(userInfoProfile.getAddress1() + " ");
            } else {
                addressET.setText(userInfoProfile.getAddress1() + " ");
            }

            if (userInfoProfile.getImageUrl().equals("")) {
                imageView.setImageResource(R.drawable.user_pic);
            } else {
                Picasso.with(getApplicationContext()).load(userInfoProfile.getImageUrl()).placeholder(getApplicationContext().getResources().getDrawable(R.drawable.user_pic)).error(R.drawable.user_pic).into(imageView);
            }
        }


    }

    @OnClick(R.id.pick_image_img)
    public void pickImageFromMedia() {
        Intent photoPickerIntent = new Intent(Intent.ACTION_GET_CONTENT);
        photoPickerIntent.setType("image/*");
        startActivityForResult(photoPickerIntent, RESULT_LOAD_IMAGE);
    }

    @OnClick(R.id.setting_dots_img)
    public void showMenu() {
        final PopupMenu menu = new PopupMenu(getApplicationContext(), threeDotsImg);
        menu.inflate(R.menu.profile_menu);

        menu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                hideDialog();
                if (menu.getMenu().getItem(1).getItemId() == R.id.logout_menu) {
                    Toast.makeText(getApplicationContext(), "logout is clicked", Toast.LENGTH_SHORT).show();
                    PrefsHelper.clearPreference(getApplicationContext());
                    Intent intent = new Intent(getApplicationContext(), SplashActivity.class);
                    startActivity(intent);
                    finish();
                } else if (menu.getMenu().getItem(0).getItemId() == R.id.edit_profile) {
                    Toast.makeText(getApplicationContext(), "change password is clicked", Toast.LENGTH_SHORT).show();
                }
                return false;
            }
        });

        menu.show();
    }

    @OnClick(R.id.save_change_btn)
    public void saveChanges() {

        if (Connectivity.isConnected(getApplicationContext())) {
            saveDetailApiCall();

        } else {
           /* clickSaveBtn = 1;*/
            Toast.makeText(this, "Please check your internet connection", Toast.LENGTH_SHORT).show();
            registerReceiver(receiver, filter);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            try {
                final Uri imageUri = data.getData();
                final InputStream imageStream = getContentResolver().openInputStream(imageUri);

                selectedImage = BitmapFactory.decodeStream(imageStream);
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                selectedImage.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                byte[] b = baos.toByteArray();
                encodedImage = Base64.encodeToString(b, Base64.NO_WRAP);
                imageView.setImageBitmap(selectedImage);

            } catch (FileNotFoundException e) {
                e.printStackTrace();
                Toast.makeText(getApplicationContext(), "Something went wrong", Toast.LENGTH_SHORT).show();
            }

        } else {
            Toast.makeText(getApplicationContext(), "You haven't picked Image", Toast.LENGTH_SHORT).show();
        }
    }

    @OnClick(R.id.home_icon_img)
    public void homeIconClick() {
        hideDialog();
        Intent intent = new Intent(getApplicationContext(), DashboardActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
    }

    boolean isUserProfile = false;

    private void getUserInfo() {
        isUserProfile = true;
        mProgressDialog = new ProgressDialog(ProfileActivity.this);
        mProgressDialog.setMessage("Loading...");
        mProgressDialog.setCanceledOnTouchOutside(true);
        mProgressDialog.show();
        UserInfoProfile.deleteAll(UserInfoProfile.class);
        final Gson builder = new GsonBuilder().addDeserializationExclusionStrategy(new SuperclassExclusionStrategy())
                .addSerializationExclusionStrategy(new SuperclassExclusionStrategy()).create();
        ServiceHandler serviceHandler = NetworkManager.createRetrofitService(getApplicationContext(), ServiceHandler.class, sharedPreference.getStringValue(PrefsHelper.ACCESS_TOKEN), NetworkManager.BASE_URL);
        serviceHandler.getUserInfo(new Callback<Response>() {
            @Override
            public void success(Response response, Response response2) {
                String arr = CommonUtils.getServerResponse(response);

                try {

                    UserInfoProfile userInfoProfile = builder.fromJson(arr, UserInfoProfile.class);
                    userInfoProfile.save();


                    if (userInfoProfile.getFirstName() != null) {
                        firstNameET.setText(userInfoProfile.getFirstName());
                    } else {
                        firstNameET.setText(" ");
                    }

                    if (userInfoProfile.getLastName() != null) {
                        lastNameET.setText(userInfoProfile.getLastName());
                    } else {
                        lastNameET.setText(" ");
                    }

                    if (userInfoProfile.getEmailID() != null) {
                        emailET.setText(userInfoProfile.getEmailID() + " ");
                        emailET.setFocusable(false);

                    }
                    if (userInfoProfile.getMobilePhoneNumber() != null) {
                        phoneET.setText(userInfoProfile.getMobilePhoneNumber() + " ");
                    }
                    if (userInfoProfile.getAddress2() != null) {
                        addressET.setText(userInfoProfile.getAddress1() + " ");
                    } else {
                        addressET.setText(userInfoProfile.getAddress1() + " ");
                    }

                    if (userInfoProfile.getImageUrl().equals("")) {
                        imageView.setImageResource(R.drawable.user_pic);
                    } else {
                        Picasso.with(getApplicationContext()).load(userInfoProfile.getImageUrl()).placeholder(getApplicationContext().getResources().getDrawable(R.drawable.user_pic)).error(R.drawable.user_pic).into(imageView);
                    }
                    hideDialog();

                } catch (Exception ex) {
                    hideDialog();


                    ex.printStackTrace();
                }
            }

            @Override
            public void failure(RetrofitError error) {


                error.printStackTrace();
                hideDialog();
            }
        });
    }
//changeInfo

    private void saveDetailApiCall() {
        showDialog();
        final Gson builder = new GsonBuilder().addDeserializationExclusionStrategy(new SuperclassExclusionStrategy())
                .addSerializationExclusionStrategy(new SuperclassExclusionStrategy()).create();
        ServiceHandler serviceHandler = NetworkManager.createRetrofitService(getApplicationContext(), ServiceHandler.class, sharedPreference.getStringValue(PrefsHelper.ACCESS_TOKEN), NetworkManager.BASE_URL);
        try {

            UserInfoProfile userInfoPro = Select.from(UserInfoProfile.class).first();
            if (userInfoPro != null) {

                JSONObject object = new JSONObject();
                object.put("UserProfileID", userInfoPro.getUserProfileID());
                object.put("FirstName", firstNameET.getText());
                object.put("LastName", lastNameET.getText());
                object.put("EmailID", emailET.getText());
                object.put("Address1", addressET.getText() + "");
                object.put("Address2", "");
                object.put("City", userInfoPro.getCity());
                object.put("State", userInfoPro.getState());
                object.put("Zip", userInfoPro.getZip());
                object.put("Country", userInfoPro.getCountry());
                object.put("MobileNo",phoneET.getText());
                object.put("Image", encodedImage);
                @SuppressLint("RestrictedApi") final AlertDialog.Builder builderdialog = new AlertDialog.Builder(new ContextThemeWrapper(this, R.style.myDialog));
                Log.e("JSON","::::::"+object.toString());

                userChangeFeilds = builder.fromJson(object.toString(), UserInfoChange.class);

                serviceHandler.changeInfo(userChangeFeilds, new Callback<Response>() {
                    @Override
                    public void success(Response response, Response response2) {
                        String serverResponse = CommonUtils.getServerResponse(response);
                        try {
                            JSONObject jsonObject = new JSONObject(serverResponse);
                            Log.e("message","::::"+jsonObject.getString("Message"));
                            if (jsonObject.getString("Message").equalsIgnoreCase("Success")) {
                                hideDialog();

                                builderdialog.setMessage("Profile is updated  successfully!. ")
                                        .setCancelable(false)
                                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {
                                                Intent intent = new Intent(getApplicationContext(), DashboardActivity.class);
                                                intent.addCategory(Intent.CATEGORY_HOME);
                                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                                startActivity(intent);
                                                overridePendingTransition(R.anim.slide_out, R.anim.slide_in);
                                                finish();
                                            }
                                        });
                                AlertDialog alert = builderdialog.create();
                                alert.show();

                            } else {
                                Toast.makeText(getBaseContext(), "Unsuccessful! Please check your feilds", Toast.LENGTH_SHORT).show();
                            }

                        } catch (Exception ex) {
                            ex.printStackTrace();
                            Toast.makeText(getBaseContext(), "Unsuccessful! Please check your feilds", Toast.LENGTH_SHORT).show();
                        }
                        hideDialog();
                    }

                    @Override
                    public void failure(RetrofitError error) {
//                        Intent intent = new Intent(getApplicationContext(), DashboardActivity.class);
//                        startActivity(intent);
//                        overridePendingTransition(R.anim.slide_out, R.anim.slide_in);
//                        // error.printStackTrace();
                        String responseError = CommonUtils.getServerResponse(error.getResponse());
                        try {
                            JSONObject jsonErrorObj = new JSONObject(responseError);
                            error.printStackTrace();
                            hideDialog();
                            Toast.makeText(getApplicationContext(), jsonErrorObj.getString("Message") + "", Toast.LENGTH_SHORT).show();

                        } catch (JSONException e) {
                            e.printStackTrace();
                            hideDialog();
                        }
                        hideDialog();

                    }
                });

            } else {
                hideDialog();

                Toast.makeText(this, "User detail not found", Toast.LENGTH_SHORT).show();

            }

        } catch (Exception ex) {
            ex.printStackTrace();
            hideDialog();
            Toast.makeText(getApplicationContext(), "please check your internet connection", Toast.LENGTH_SHORT).show();
        }


    }


    public class NetworkChangeReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(final Context context, final Intent intent) {
            Log.v("Goals getting", "Receieved notification about network status");
            if (Connectivity.isNetworkAvailable(context)){
                if (!isConnected) {
                    Log.v("is network Available", "Now you are connected to Internet!");
                    isConnected = true;
                    sharedPreference.saveBooleanValue("IsProfile", true);
                    //Toast.makeText(context, "Internet Connected", Toast.LENGTH_SHORT).show();
                    if (!isUserProfile) {
                        getUserInfo();
                    }
                             /*   if(clickSaveBtn==1) {
                                    saveDetailApiCall();
                                }*/
                    unregisterReceiver(receiver);
                } else {

                    Log.d("Internet not Connected", "??????");
                }
            }
            else {
                Log.v("not connected", "You are not connected to Internet!");
                UserInfoProfile userInfoProfile = Select.from(UserInfoProfile.class).first();
                if (userInfoProfile.getFirstName() != null) {
                    firstNameET.setText(userInfoProfile.getFirstName());
                } else {
                    firstNameET.setText(" ");
                }

                if (userInfoProfile.getLastName() != null) {
                    lastNameET.setText(userInfoProfile.getLastName());
                } else {
                    lastNameET.setText(" ");
                }

                if (userInfoProfile.getEmailID() != null) {
                    emailET.setText(userInfoProfile.getEmailID() + " ");
                }
                if (userInfoProfile.getMobilePhoneNumber() != null) {
                    phoneET.setText(userInfoProfile.getMobilePhoneNumber() + " ");
                }
                if (userInfoProfile.getAddress2() != null) {
                    addressET.setText(userInfoProfile.getAddress1() + " ");
                } else {
                    addressET.setText(userInfoProfile.getAddress1() + " ");
                }

                if (userInfoProfile.getImageUrl().equals("")) {
                    imageView.setImageResource(R.drawable.user_pic);
                } else {
                    Picasso.with(getApplicationContext()).load(userInfoProfile.getImageUrl()).placeholder(getApplicationContext().getResources().getDrawable(R.drawable.user_pic)).error(R.drawable.user_pic).into(imageView);
                }

                isConnected = false;
            }

        }


        private boolean isNetworkAvailable(Context context) {
            ConnectivityManager connectivity = (ConnectivityManager)
                    context.getSystemService(Context.CONNECTIVITY_SERVICE);
            if (connectivity != null) {
                NetworkInfo[] info = connectivity.getAllNetworkInfo();
                if (info != null) {
                    for (int i = 0; i < info.length; i++) {
                        if (info[i].getState() == NetworkInfo.State.CONNECTED) {
                            if (!isConnected) {
                                Log.v("is network Available", "Now you are connected to Internet!");
                                isConnected = true;
                                sharedPreference.saveBooleanValue("IsProfile", true);
                                //Toast.makeText(context, "Internet Connected", Toast.LENGTH_SHORT).show();
                                if (!isUserProfile) {
                                    getUserInfo();
                                }
                             /*   if(clickSaveBtn==1) {
                                    saveDetailApiCall();
                                }*/
                                unregisterReceiver(receiver);
                            } else {

                                Log.d("Internet not Connected", "??????");
                            }
                            return true;
                        }
                    }
                }
            } else {

            }



            Log.v("not connected", "You are not connected to Internet!");
            UserInfoProfile userInfoProfile = Select.from(UserInfoProfile.class).first();
            if (userInfoProfile.getFirstName() != null) {
                firstNameET.setText(userInfoProfile.getFirstName());
            } else {
                firstNameET.setText(" ");
            }

            if (userInfoProfile.getLastName() != null) {
                lastNameET.setText(userInfoProfile.getLastName());
            } else {
                lastNameET.setText(" ");
            }

            if (userInfoProfile.getEmailID() != null) {
                emailET.setText(userInfoProfile.getEmailID() + " ");
            }
            if (userInfoProfile.getMobilePhoneNumber() != null) {
                phoneET.setText(userInfoProfile.getMobilePhoneNumber() + " ");
            }
            if (userInfoProfile.getAddress2() != null) {
                addressET.setText(userInfoProfile.getAddress1() + " ");
            } else {
                addressET.setText(userInfoProfile.getAddress1() + " ");
            }

            if (userInfoProfile.getImageUrl().equals("")) {
                imageView.setImageResource(R.drawable.user_pic);
            } else {
                Picasso.with(getApplicationContext()).load(userInfoProfile.getImageUrl()).placeholder(getApplicationContext().getResources().getDrawable(R.drawable.user_pic)).error(R.drawable.user_pic).into(imageView);
            }
            //  Toast.makeText(context, "not Connected", Toast.LENGTH_SHORT).show();
            isConnected = false;
            return false;
        }
    }

    public void showDialog() {

        if (mProgressDialog != null && !mProgressDialog.isShowing()) {
            mProgressDialog.show();


        }
    }

    public void hideDialog() {


        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();

        }
    }
}
