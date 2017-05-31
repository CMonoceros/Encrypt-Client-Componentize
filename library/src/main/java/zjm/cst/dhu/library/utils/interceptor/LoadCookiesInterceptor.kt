package zjm.cst.dhu.library.utils.interceptor

import android.content.Context
import android.content.SharedPreferences

import java.io.IOException

import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import rx.Observable
import rx.functions.Action1

/**
 * Created by zjm on 3/23/2017.
 */

class LoadCookiesInterceptor(private val mContext: Context) : Interceptor {

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {

        val builder = chain.request().newBuilder()
        val sharedPreferences = mContext.getSharedPreferences("cookie", Context.MODE_PRIVATE)
        Observable.just(sharedPreferences.getString("userId", ""))
                .subscribe { cookie ->
                    if (cookie !== "") {
                        builder.addHeader("Cookie", "userId=" + cookie)
                    }
                }
        Observable.just(sharedPreferences.getString("sessionId", ""))
                .subscribe { cookie ->
                    if (cookie !== "") {
                        builder.addHeader("Cookie", "sessionId=" + cookie)
                    }
                }
        return chain.proceed(builder.build())
    }

}

