package co.romero.mandegar.network

import android.content.Context

import java.io.IOException
import java.util.concurrent.TimeUnit

import co.romero.mandegar.Util.Utils
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ServiceGenerator {


    fun <S> createService(serviceClass: Class<S>, context: Context, api_address: String,auth:String? = ""): S {

        val logging = HttpLoggingInterceptor()
        logging.level = HttpLoggingInterceptor.Level.BODY
        val httpClient = OkHttpClient.Builder()
        httpClient.addInterceptor(logging)
        httpClient.connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS).addInterceptor { chain ->
                    val original = chain.request()

                    val request = original.newBuilder()
                            .header("Accept", "application/json")
                            .header("Authorization", "Bearer $auth")
                            .method(original.method(), original.body())
                            .build()

                    chain.proceed(request)
                }

        val client = httpClient.build()
        val retrofit = Retrofit.Builder()
                .baseUrl(api_address)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build()


        return retrofit.create(serviceClass)
    }


}