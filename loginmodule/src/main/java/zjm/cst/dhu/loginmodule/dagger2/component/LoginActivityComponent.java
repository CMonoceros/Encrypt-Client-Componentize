package zjm.cst.dhu.loginmodule.dagger2.component;

import dagger.Component;
import zjm.cst.dhu.basemodule.dagger2.component.ApplicationComponent;
import zjm.cst.dhu.basemodule.dagger2.module.ActivityModule;
import zjm.cst.dhu.basemodule.dagger2.scope.PerActivity;
import zjm.cst.dhu.loginmodule.dagger2.module.LoginModule;
import zjm.cst.dhu.loginmodule.view.LoginActivity;

/**
 * Created by zjm on 2017/2/24.
 */

@PerActivity
@Component(dependencies = ApplicationComponent.class, modules = {ActivityModule.class, LoginModule.class})
public interface LoginActivityComponent {
    void inject(LoginActivity loginActivity);
}
