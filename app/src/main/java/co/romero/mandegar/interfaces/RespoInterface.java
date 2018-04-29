package co.romero.mandegar.interfaces;

import co.romero.mandegar.model.Respo;
import co.romero.mandegar.request.CustomerCheckCodeRequest;
import co.romero.mandegar.request.CustomerEmailRequest;
import co.romero.mandegar.request.CustomerLoginOrRegisterRequest;
import co.romero.mandegar.request.CustomerPassRequest;
import co.romero.mandegar.request.CustomerRequest;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface RespoInterface {

    @POST("checkMobile")
    Call<Respo> checkMobile(@Body CustomerRequest request);

    @POST("checkEmail")
    Call<Respo> checkEmail(@Body CustomerEmailRequest request);


    @POST("checkPassword")
    Call<Respo> checkPassword(@Body CustomerPassRequest request);



    @GET("json")
    Call<Respo> checkCountry();


    @POST("customerLoginOrRegister")
    Call<Respo> customerLoginOrRegister(@Body CustomerLoginOrRegisterRequest request);

    @POST("checkLoginCode")
    Call<Respo> checkLoginCode(@Body CustomerCheckCodeRequest request);

}
