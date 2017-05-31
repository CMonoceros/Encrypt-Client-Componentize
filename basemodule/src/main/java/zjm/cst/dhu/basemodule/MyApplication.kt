package zjm.cst.dhu.basemodule

import android.app.Application
import android.content.Context
import android.os.Build
import android.os.StrictMode

import com.squareup.leakcanary.LeakCanary
import com.squareup.leakcanary.RefWatcher
import zjm.cst.dhu.basemodule.dagger2.component.ApplicationComponent

import zjm.cst.dhu.basemodule.dagger2.module.ApplicationModule
import zjm.cst.dhu.basemodule.dagger2.module.NetworkModule
import zjm.cst.dhu.basemodule.dagger2.component.DaggerApplicationComponent
import zjm.cst.dhu.library.utils.others.CrashHandler

/**
 * Created by zjm on 2017/2/23.
 */

class MyApplication : Application() {
    var applicationComponent: ApplicationComponent? = null
        private set
    private var mRefWatcher: RefWatcher? = null

    override fun onCreate() {
        super.onCreate()
        context = applicationContext
        setStrictMode()
        setCrashHandler()
        initStetho()
        initLeakCanary()
        setupInjector()
    }

    /**

     */
    private fun setStrictMode() {
        if (BuildConfig.DEBUG && Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
            StrictMode.setThreadPolicy(StrictMode.ThreadPolicy.Builder().detectAll().penaltyLog().build())
            StrictMode.setVmPolicy(StrictMode.VmPolicy.Builder().detectAll().penaltyLog().build())
        }
    }

    /**

     */
    private fun setCrashHandler() {
        val crashHandler = CrashHandler.getInstance(this)
        Thread.setDefaultUncaughtExceptionHandler(crashHandler)
    }

    /**

     */
    private fun initStetho() {
        //Stetho.initializeWithDefaults(this);
    }

    /**

     */
    private fun initLeakCanary() {
        mRefWatcher = LeakCanary.install(this)
    }

    private fun setupInjector() {
        applicationComponent = DaggerApplicationComponent.builder().applicationModule(ApplicationModule(this))
                .networkModule(NetworkModule(this)).build()
    }

    companion object {

        var context: Context? = null
            private set

        fun getmRefWatcher(context: Context): RefWatcher? {
            val myApplication = context.applicationContext as MyApplication
            return myApplication.mRefWatcher
        }
    }
}
