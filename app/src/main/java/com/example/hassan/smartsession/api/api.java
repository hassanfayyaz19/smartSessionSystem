package com.example.hassan.smartsession.api;

import com.example.hassan.smartsession.model.ViewDataResponse;
import com.example.hassan.smartsession.model.attendenceResponse;
import com.example.hassan.smartsession.model.loginResponse;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface api {


    @GET("login.php")
    Call<loginResponse> PerformLogin(
            @Query("roll_no")
                    String roll,

            @Query("password")
                    String password
    );


    @FormUrlEncoded
    @POST("insertAttendence.php")
    Call<attendenceResponse> createuser(
            @Field("name") String name,
            @Field("roll_no") String roll_no,
            @Field("subject") String subject,
            @Field("department") String department,
            @Field("semester") String semester,
            @Field("status") String status,
            @Field("mac") String mac
    );


    @GET("view.php")
    Call<ViewDataResponse> getView(
            @Query("roll_no") String rollNo
          /*  @Query("_sort") String sort,
            @Query("_order") String order*/
    );

}
