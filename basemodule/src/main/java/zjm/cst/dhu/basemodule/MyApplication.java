package zjm.cst.dhu.basemodule;

import android.app.Application;
import android.content.Context;
import android.os.Build;
import android.os.StrictMode;

import com.squareup.leakcanary.LeakCanary;
import com.squareup.leakcanary.RefWatcher;

import zjm.cst.dhu.basemodule.dagger2.component.ApplicationComponent;
import zjm.cst.dhu.basemodule.dagger2.component.DaggerApplicationComponent;
import zjm.cst.dhu.basemodule.dagger2.module.ApplicationModule;
import zjm.cst.dhu.basemodule.dagger2.module.NetworkModule;
import zjm.cst.dhu.library.utils.others.CrashHandler;

/**
 * Created by zjm on 2017/2/23.
 */

public class MyApplication extends Application {

    private static Context mContext;
    private ApplicationComponent mApplicationComponent;
    private RefWatcher mRefWatcher;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = getApplicationContext();
        setStrictMode();
        setCrashHandler();
        initStetho();
        initLeakCanary();
        setupInjector();
    }

    /**
     *
     */
    private void setStrictMode() {
        if (BuildConfig.DEBUG && Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
            StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().detectAll().penaltyLog().build());
            StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder().detectAll().penaltyLog().build());
        }
    }

    /**
     *
     */
    private void setCrashHandler() {
        CrashHandler crashHandler = CrashHandler.getInstance(this);
        Thread.setDefaultUncaughtExceptionHandler(crashHandler);
    }

    /**
     *
     */
    private void initStetho() {
        //Stetho.initializeWithDefaults(this);
    }

    /**
     *
     */
    private void initLeakCanary() {
        mRefWatcher = LeakCanary.install(this);
    }

    private void setupInjector() {
        mApplicationComponent = DaggerApplicationComponent.builder().
                applicationModule(new ApplicationModule(this))
                .networkModule(new NetworkModule(this)).build();
    }

    public ApplicationComponent getApplicationComponent() {
        return mApplicationComponent;
    }

    public static Context getContext() {
        return mContext;
    }

    public static RefWatcher getmRefWatcher(Context context){
        MyApplication myApplication=(MyApplication) context.getApplicationContext();
        return myApplication.mRefWatcher;
    }
}
