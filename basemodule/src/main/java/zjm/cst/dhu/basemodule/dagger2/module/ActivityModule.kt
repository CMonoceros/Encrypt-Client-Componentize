package zjm.cst.dhu.basemodule.dagger2.module

import android.app.Activity

import dagger.Module
import dagger.Provides
import zjm.cst.dhu.basemodule.dagger2.scope.PerActivity

/**
 * Created by zjm on 2017/2/24.
 */

@Module
class ActivityModule(private val mActivity: Activity) {

    @Provides
    @PerActivity
    fun provideActivity(): Activity {
        return mActivity
    }
}
