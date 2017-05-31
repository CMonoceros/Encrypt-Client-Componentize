package zjm.cst.dhu.basemodule.dagger2.module

import android.app.Application

import dagger.Module
import dagger.Provides
import zjm.cst.dhu.basemodule.MyApplication
import zjm.cst.dhu.basemodule.dagger2.scope.PerApplication

/**
 * Created by zjm on 2017/2/23.
 */
@Module
class ApplicationModule(private val myApplication: MyApplication) {

    @Provides
    @PerApplication
    fun provideApplication(): Application {
        return myApplication
    }

    @Provides
    @PerApplication
    fun provideMyApplication(): MyApplication {
        return myApplication
    }
}
