package zjm.cst.dhu.library.utils.interceptor

import android.content.Context
import android.content.SharedPreferences

import java.io.IOException

import okhttp3.Interceptor
import okhttp3.Response
import rx.Observable
import rx.functions.Action1
import rx.functions.Func1

/**
 * Created by zjm on 3/23/2017.
 */

class SaveCookiesInterceptor(private val mContext: Context) : Interceptor {

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val sharedPreferences = mContext.getSharedPreferences("cookie", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        val originalResponse = chain.proceed(chain.request())
        //这里获取请求返回的cookie
        if (!originalResponse.headers("Set-Cookie").isEmpty()) {
            val cookieBuffer = StringBuffer()
            Observable.from(originalResponse.headers("Set-Cookie"))
                    .map { s ->
                        val cookieArray = s.split(";".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                        cookieArray[0]
                    }
                    .subscribe { cookie ->
                        val res = cookie.split("=".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                        editor.putString(res[0], res[1] + ";")
                    }
            editor.commit()
        }

        return originalResponse
    }


}
