package com.salestrackmobileapp.android.networkManager;


import com.salestrackmobileapp.android.gson.CheckedDataHit;
import com.salestrackmobileapp.android.gson.LatLng;
import com.salestrackmobileapp.android.gson.SaveOrder;
import com.salestrackmobileapp.android.gson.UserInfoChange;
import com.salestrackmobileapp.android.gson.UserLogin;

import java.util.List;

import retrofit.Callback;
import retrofit.client.Response;
import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.Query;

/**
 * Created by kanchan on 3/23/2017.
 */

public interface ServiceHandler {

   /* @FormUrlEncoded
    @POST("api/Account/Login")
    void login(@Field("Username") String username,
               @Field("Password") String password,
               @Field("DeviceId") String deviceId,
               @Field("DeviceType") String deviceType,
               @Field("Longitude") Double longitute,
               @Field("Latitude") Double latitude,
               Callback<Response> response);*/

    //@Headers("Content-type: application/json")
    @POST("/Account/Login")
    void login(@Body UserLogin object,
               Callback<Response> response);

    @POST("/Attendance/SaveAttendanceStart")
    void startAttendence(Callback<Response> response);


    @POST("/Attendance/SaveAttendanceEnd")
    void stopAttendence(Callback<Response> response);

    @GET("/Account/ForgetPassword")
    void forgetPassword(@Query("EmailID") String dateSt,
                        Callback<Response> response);

    @POST("/UserProfile/Edit")
    void changeInfo(@Body UserInfoChange object,
                    Callback<Response> response);
    @GET("/UserDevice/VerifyFCMRegID")
    void checkSession(@Query("DeviceID") String device_id, @Query("FCMRegID") String reg_id,
                      Callback<Response> response);

    @POST("/Order/SaveOrder")
    void placeOrder(@Body List<SaveOrder> object,
                    Callback<Response> response);

    ///UserProfile/Get
    @GET("/UserProfile/Get")
    void getUserInfo(Callback<Response> response);


    @GET("/Business/GetAll")
    void getAllBusinessArray(Callback<Response> response);

    @GET("/Deal/GetAllActive")
    void getAllDealsArray(Callback<Response> response);

    @GET("/ProductCategory/GetAll")
    void getAllProductCategory(Callback<Response> response);

    @GET("/Brand/GetAll")
    void getAllBrands(Callback<Response> response);

    @GET("/Order/GetAll")
    void getAllPreOrder(Callback<Response> response);

    @GET("/Products/GetAll")
    void getAllProduct(Callback<Response> response);

    @GET("/DailyGoals/GetByDate")
    void getGoalsAccDate(@Query("Date") String dateSt,
                         Callback<Response> response);

    @GET("/Order/GetAll")//Status
    void getOrderHistory(@Query("Status") String status, Callback<Response> response);

    @GET("/Order/GetOrderItems")
    void getOrderItems(@Query("OrderId") int orderID, Callback<Response> response);

    @POST("/Track/SaveTrack")
    void trackLatLong(@Body LatLng object,
                      Callback<Response> response);

    //https://salestrackapi.azurewebsites.net/api/Order/GetByBussinesswithDate?id=1&SalesPersonID=1&StartDate=19-06-2017&EndDate=12%2F2%2F17
    /*@Field("Username") String username,
               @Field("Password") String password,
               @Field("DeviceId") String deviceId,
               @Field("DeviceType") String deviceType,
               @Field("Longitude") Double longitute,
               @Field("Latitude") Double latitude,*/
    @GET("/Order/GetByBussinesswithDate")
    void getBusinessAccDate(@Query("id") String businessId,
                            @Query("SalesPersonID") String salesId,
                            @Query("StartDate") String startDate,
                            @Query("EndDate") String endDate, @Query("status") String status,
                            Callback<Response> response);

    @POST("/CheckIn/SaveCheckIn")
    void checkedInSave(@Body CheckedDataHit object,
                       Callback<Response> response);

    @GET("/UnitOfMeasurement/GetAll")
    void getAllMeasurement(Callback<Response> response);

    @GET("/Dashboard/GetSalesPersonDashboard")
    void getDashboardDetail(Callback<Response> response);


    @GET("/Order/GetOrderItems")
    void getOrderItem(@Query("OrderId") int orderId,
                      Callback<Response> response);

    @GET("/Notification/GetDeviceNotifications")
    void getAllNotification(Callback<Response> response);

    @GET("/Variant/GetByProductID")
    void getVariantsbyId(@Query("id") long productId,
                         Callback<Response> response);
    @GET("/Variant/Get")
    void getAllVariants(Callback<Response> response);
    @GET("/Products/GetProductPriceList")
    void getProductPriceList(@Query("ProductID") int productId,
                             Callback<Response> response);

    @GET("/Order/DeleteItem")
    void getOrderDeleteItem(@Query("ID") int orderID,
                            Callback<Response> response);
}
