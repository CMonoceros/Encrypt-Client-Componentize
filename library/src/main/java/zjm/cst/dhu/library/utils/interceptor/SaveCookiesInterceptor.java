package zjm.cst.dhu.library.utils.interceptor;

import android.content.Context;
import android.content.SharedPreferences;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Response;
import rx.Observable;
import rx.functions.Action1;
import rx.functions.Func1;

/**
 * Created by zjm on 3/23/2017.
 */

public class SaveCookiesInterceptor implements Interceptor {
    private Context mContext;

    public SaveCookiesInterceptor(Context context) {
        mContext = context;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        SharedPreferences sharedPreferences = mContext.getSharedPreferences("cookie", Context.MODE_PRIVATE);
        final SharedPreferences.Editor editor = sharedPreferences.edit();
        Response originalResponse = chain.proceed(chain.request());
        //这里获取请求返回的cookie
        if (!originalResponse.headers("Set-Cookie").isEmpty()) {
            final StringBuffer cookieBuffer = new StringBuffer();
            Observable.from(originalResponse.headers("Set-Cookie"))
                    .map(new Func1<String, String>() {
                        @Override
                        public String call(String s) {
                            String[] cookieArray = s.split(";");
                            return cookieArray[0];
                        }
                    })
                    .subscribe(new Action1<String>() {
                        @Override
                        public void call(String cookie) {
                            String[] res=cookie.split("=");
                            editor.putString(res[0], res[1]+";");
                        }
                    });
            editor.commit();
        }

        return originalResponse;
    }


}
