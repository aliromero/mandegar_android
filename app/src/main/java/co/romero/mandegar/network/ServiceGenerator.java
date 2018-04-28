package co.romero.mandegar.network;

import android.content.Context;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import co.romero.mandegar.Util.Utils;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ServiceGenerator {


    public static <S> S createService(Class<S> serviceClass, final Context context,String api_address) {

        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();


        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();


        httpClient.addInterceptor(logging);


        httpClient.connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS).addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request original = chain.request();

                Request request = original.newBuilder()
                        .header("Accept", "application/json")
                        .method(original.method(), original.body())
                        .build();

                return chain.proceed(request);
            }
        });

        OkHttpClient client = httpClient.build();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(api_address)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();


        return retrofit.create(serviceClass);
    }


}