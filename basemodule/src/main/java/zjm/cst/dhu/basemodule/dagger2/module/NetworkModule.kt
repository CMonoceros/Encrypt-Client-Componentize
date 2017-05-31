package zjm.cst.dhu.basemodule.dagger2.module

import android.text.TextUtils
import android.util.Log

import com.google.gson.Gson
import com.google.gson.GsonBuilder

import java.io.IOException
import java.util.Locale
import java.util.concurrent.TimeUnit

import dagger.Module
import dagger.Provides
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import zjm.cst.dhu.basemodule.MyApplication
import zjm.cst.dhu.basemodule.api.BaseUrl
import zjm.cst.dhu.basemodule.api.interceptor.CacheInterceptor
import zjm.cst.dhu.library.utils.interceptor.LoadCookiesInterceptor
import zjm.cst.dhu.library.utils.interceptor.SaveCookiesInterceptor
import zjm.cst.dhu.basemodule.api.repository.BaseRepository
import zjm.cst.dhu.basemodule.api.repository.Repository
import zjm.cst.dhu.basemodule.dagger2.scope.PerApplication

/**
 * Created by zjm on 2017/2/23.
 */
@Module
class NetworkModule(private val myApplication: MyApplication) {


    @Provides
    @PerApplication
    fun provideOkHttpClient(): OkHttpClient {
        //用于记录应用中的网络请求的信息的拦截器
        val mLoggingBaseInterceptor = Interceptor { chain ->
            val request = chain.request()
            val requestTime = System.nanoTime()
            Log.i("Request", String.format("Sending request %s on %s%n%s", request.url(), chain.connection(), request.headers()))
            val response = chain.proceed(request)
            val responseTime = System.nanoTime()
            Log.i("Response", String.format(Locale.getDefault(), "Received response for %s in %.1fms%n%s",
                    response.request().url(), (responseTime - requestTime) / 1e6, response.headers()))
            response
        }

        val mLoggingJsonInterceptor = HttpLoggingInterceptor(
                HttpLoggingInterceptor.Logger { message ->
                    if (TextUtils.isEmpty(message)) return@Logger
                    val s = message.substring(0, 1)
                    //如果收到响应是json才打印
                    if ("{" == s || "[" == s) {
                        Log.i("Response Json", message)
                    }
                })
        mLoggingJsonInterceptor.level = HttpLoggingInterceptor.Level.BODY

        val okHttpClient = OkHttpClient.Builder()
                .connectTimeout((15 * 1000).toLong(), TimeUnit.MILLISECONDS)
                .readTimeout((60 * 1000).toLong(), TimeUnit.MILLISECONDS)
                .writeTimeout((60 * 1000).toLong(), TimeUnit.MILLISECONDS)
                .addInterceptor(CacheInterceptor())
                .addInterceptor(LoadCookiesInterceptor(myApplication))
                .addInterceptor(SaveCookiesInterceptor(myApplication))
                .build()
        return okHttpClient
    }

    @Provides
    @PerApplication
    fun provideRetrofit(client: OkHttpClient): Retrofit {
        val gson = GsonBuilder().excludeFieldsWithoutExposeAnnotation().create()
        val gsonConverterFactory = GsonConverterFactory.create(gson)

        val retrofit = Retrofit.Builder()
                .baseUrl(BaseUrl.BASEHTTP + BaseUrl.BASEIP
                        + BaseUrl.BASEPORT)
                .addConverterFactory(gsonConverterFactory)
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .client(client)
                .build()
        return retrofit
    }

    @Provides
    @PerApplication
    fun provideBaseRepository(retrofit: Retrofit): BaseRepository {
        return Repository(retrofit)
    }

}
