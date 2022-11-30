package com.ShabrinaJSleepDN.jsleep_android.request;
import com.ShabrinaJSleepDN.jsleep_android.model.Account;
import com.ShabrinaJSleepDN.jsleep_android.model.City;
import com.ShabrinaJSleepDN.jsleep_android.model.Facility;
import com.ShabrinaJSleepDN.jsleep_android.model.Renter;
import com.ShabrinaJSleepDN.jsleep_android.model.Room;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface BaseApiService {

    @POST("account/register")
    Call<Account> register (@Query("name") String name,
                            @Query("email") String email,
                            @Query("password") String password);

    @GET("account/{id}")
    Call<Account> getAccount (@Path("id") int id);

    @POST("account/login")
    Call<Account> login(@Query("email") String email,
                        @Query("password") String password);


    @POST("account/{id}/registerStore")
    Call<Renter> registerRenter(@Path("id") int id,
                                @Query("username") String username,
                                @Query("address") String address,
                                @Query("phoneNumber") String phoneNumber);
}


