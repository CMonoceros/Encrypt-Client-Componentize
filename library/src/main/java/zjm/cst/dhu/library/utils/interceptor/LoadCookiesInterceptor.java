package zjm.cst.dhu.library.utils.interceptor;

import android.content.Context;
import android.content.SharedPreferences;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import rx.Observable;
import rx.functions.Action1;

/**
 * Created by zjm on 3/23/2017.
 */

public class LoadCookiesInterceptor implements Interceptor {
    private Context mContext;

    public LoadCookiesInterceptor(Context context) {
        mContext = context;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {

        final Request.Builder builder = chain.request().newBuilder();
        SharedPreferences sharedPreferences = mContext.getSharedPreferences("cookie", Context.MODE_PRIVATE);
        Observable.just(sharedPreferences.getString("userId", ""))
                .subscribe(new Action1<String>() {
                    @Override
                    public void call(String cookie) {
                        if(cookie!=""){
                            builder.addHeader("Cookie", "userId="+cookie);
                        }
                    }
                });
        Observable.just(sharedPreferences.getString("sessionId", ""))
                .subscribe(new Action1<String>() {
                    @Override
                    public void call(String cookie) {
                        if(cookie!=""){
                            builder.addHeader("Cookie", "sessionId="+cookie);
                        }
                    }
                });
        return chain.proceed(builder.build());
    }

}

