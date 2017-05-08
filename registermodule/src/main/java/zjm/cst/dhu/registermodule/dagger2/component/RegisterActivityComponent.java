package zjm.cst.dhu.registermodule.dagger2.component;

import dagger.Component;
import zjm.cst.dhu.basemodule.dagger2.component.ApplicationComponent;
import zjm.cst.dhu.basemodule.dagger2.module.ActivityModule;
import zjm.cst.dhu.basemodule.dagger2.scope.PerActivity;
import zjm.cst.dhu.registermodule.dagger2.module.RegisterModule;
import zjm.cst.dhu.registermodule.view.RegisterActivity;

/**
 * Created by zjm on 2017/3/1.
 */

@PerActivity
@Component(dependencies = ApplicationComponent.class, modules = {ActivityModule.class, RegisterModule.class})
public interface RegisterActivityComponent {
    void inject(RegisterActivity registerActivity);
}
