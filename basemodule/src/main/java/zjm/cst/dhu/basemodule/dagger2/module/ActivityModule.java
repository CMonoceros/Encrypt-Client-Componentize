package zjm.cst.dhu.basemodule.dagger2.module;

import android.app.Activity;

import dagger.Module;
import dagger.Provides;
import zjm.cst.dhu.basemodule.dagger2.scope.PerActivity;

/**
 * Created by zjm on 2017/2/24.
 */

@Module
public class ActivityModule {
    private final Activity mActivity;

    public ActivityModule(Activity activity) {
        this.mActivity = activity;
    }

    @Provides
    @PerActivity
    public Activity provideActivity() {
        return mActivity;
    }
}
