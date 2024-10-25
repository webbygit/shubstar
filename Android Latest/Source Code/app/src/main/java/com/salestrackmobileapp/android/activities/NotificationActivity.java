package com.salestrackmobileapp.android.activities;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.orm.query.Select;

import com.salestrackmobileapp.android.R;
import com.salestrackmobileapp.android.adapter.NotificationAdapter;
import com.salestrackmobileapp.android.gson.AllNotification;
import com.salestrackmobileapp.android.gson.AllVariants;
import com.salestrackmobileapp.android.networkManager.NetworkManager;
import com.salestrackmobileapp.android.networkManager.ServiceHandler;
import com.salestrackmobileapp.android.utils.CommonUtils;
import com.salestrackmobileapp.android.utils.Connectivity;
import com.salestrackmobileapp.android.utils.PrefsHelper;
import com.salestrackmobileapp.android.utils.RecyclerClick;
import com.salestrackmobileapp.android.utils.SuperclassExclusionStrategy;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;


public class NotificationActivity extends BaseActivity implements RecyclerClick {


    @BindView(R.id.notification_Rv)
    RecyclerView notificationRv;

    private ProgressDialog mProgressDialog;
    List<AllNotification> listAllNotification;
    NotificationAdapter mAdapter;
    private LinearLayoutManager mLayoutManager;
    @BindView(R.id.no_item)
    TextView noItem;

    String entityId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);
        ButterKnife.bind(this);

        if (getIntent() != null) {
            if (getIntent().hasExtra("entity")) {
                entityId=   getIntent().getStringExtra("entity");


            }
        }
        mLayoutManager = new LinearLayoutManager(getApplicationContext());
        notificationRv.setLayoutManager(mLayoutManager);
        mAdapter = new NotificationAdapter(getApplicationContext(), this);

        listAllNotification=new ArrayList<>();
        if (Connectivity.isNetworkAvailable(NotificationActivity.this)) {
            getAllNotification();
        }
        else{

            listAllNotification = AllNotification.listAll(AllNotification.class);

            if (listAllNotification.size() != 0) {
                mAdapter.setAllNotification();
                notificationRv.setAdapter(mAdapter);
                mAdapter.notifyDataSetChanged();

            }

        }
    }

    @OnClick(R.id.home_icon_img)
    public void backDashboard() {
        hideDialog();
        Intent intent = new Intent(this, DashboardActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_enter, R.anim.slide_exit);
    }

    private void getAllNotification() {
        Log.e("All_notif",":::;"+true);
        mProgressDialog = new ProgressDialog(NotificationActivity.this);
        mProgressDialog.setMessage("Loading...");
        mProgressDialog.setCanceledOnTouchOutside(true);
        mProgressDialog.show();

        AllNotification.deleteAll(AllNotification.class);


        final Gson builder = new GsonBuilder().addDeserializationExclusionStrategy(new SuperclassExclusionStrategy())
                .addSerializationExclusionStrategy(new SuperclassExclusionStrategy()).create();
        ServiceHandler serviceHandler = NetworkManager.createRetrofitService(getApplicationContext(), ServiceHandler.class, sharedPreference.getStringValue(PrefsHelper.ACCESS_TOKEN), NetworkManager.BASE_URL);
        serviceHandler.getAllNotification(new Callback<Response>() {
            @Override
            public void success(Response response, Response response2) {
                String arr = CommonUtils.getServerResponse(response);
                Log.e("Notification_res","::::"+arr);
                try {
                    JSONArray jsonArr = new JSONArray(arr);
                    for (int i = 0; i < jsonArr.length(); i++) {
                        AllNotification allBusiness = builder.fromJson(jsonArr.get(i).toString(), AllNotification.class);
                        allBusiness.save();
                    }
                    listAllNotification = AllNotification.listAll(AllNotification.class);

                    if (listAllNotification.size() != 0) {
                        mAdapter.setAllNotification();
                        notificationRv.setAdapter(mAdapter);
                        mAdapter.notifyDataSetChanged();
                       hideDialog();
                    } else {
                        noItem.setVisibility(View.VISIBLE);
                        AlertDialog.Builder builder1 = new AlertDialog.Builder(getApplicationContext());
                        builder1.setMessage("No notifcation are available");
                        builder1.setCancelable(false);
                        builder1.setPositiveButton("Back", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // TODO Auto-generated method stub
                                Toast.makeText(getApplicationContext(), "Back is pressed", Toast.LENGTH_SHORT).show();
                               hideDialog();
                            }
                        });
                        builder1.setNegativeButton("Reload", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // TODO Auto-generated method stub
                                getAllNotification();
                            }
                        });
                        builder1.show();
                    }

                } catch (Exception ex) {
                    Log.e("Notif_ex","::::"+ex.toString());
                    ex.printStackTrace();
                   hideDialog();
                }
            }

            @Override
            public void failure(RetrofitError error) {
                error.printStackTrace();

                hideDialog();
                Toast.makeText(getApplicationContext(), "notifications not found", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void productClick(View v, int position) {

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
