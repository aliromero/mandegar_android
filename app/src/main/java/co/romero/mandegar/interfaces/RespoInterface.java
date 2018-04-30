package co.romero.mandegar.interfaces;

import co.romero.mandegar.request.SendMessageRequest;
import co.romero.mandegar.response.Respo;
import co.romero.mandegar.request.CustomerCheckCodeRequest;
import co.romero.mandegar.request.CustomerEmailRequest;
import co.romero.mandegar.request.CustomerLoginOrRegisterRequest;
import co.romero.mandegar.request.CustomerNameRequest;
import co.romero.mandegar.request.CustomerPassRequest;
import co.romero.mandegar.request.CustomerRequest;

import co.romero.mandegar.request.CustomerResetEmailRequest;
import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;

public interface RespoInterface {

    @POST("checkMobile")
    Call<Respo> checkMobile(@Body CustomerRequest request);

    @POST("checkEmail")
    Call<Respo> checkEmail(@Body CustomerEmailRequest request);


    @POST("checkPassword/{id}")
    Call<Respo> checkPassword(@Body CustomerPassRequest request,@Path("id") String id);

    @POST("checkName/{id}")
    Call<Respo> checkName(@Body CustomerNameRequest request, @Path("id") String id);


    @Multipart
    @POST("checkPic/{id}")
    Call<Respo> checkPic(@Part MultipartBody.Part pic, @Path("id") String id);


    @POST("resetPass")
    Call<Respo> resetEmail(@Body CustomerResetEmailRequest request);


    @POST("getGroups")
    Call<Respo> getGroups();


    @POST("sendMessage")
    Call<Respo> sendMessage(@Body SendMessageRequest request);



    @GET("json")
    Call<Respo> checkCountry();


    @POST("customerLoginOrRegister")
    Call<Respo> customerLoginOrRegister(@Body CustomerLoginOrRegisterRequest request);

    @POST("checkLoginCode")
    Call<Respo> checkLoginCode(@Body CustomerCheckCodeRequest request);

}
