package zjm.cst.dhu.menumodule.dagger2.component;

import dagger.Component;
import zjm.cst.dhu.basemodule.dagger2.component.ApplicationComponent;
import zjm.cst.dhu.basemodule.dagger2.module.ActivityModule;
import zjm.cst.dhu.basemodule.dagger2.scope.PerActivity;
import zjm.cst.dhu.menumodule.dagger2.module.FileTypeModule;
import zjm.cst.dhu.menumodule.view.FileTypeFragment;

/**
 * Created by zjm on 2017/3/3.
 */
@PerActivity
@Component(dependencies = ApplicationComponent.class, modules = {ActivityModule.class, FileTypeModule.class})
public interface FileTypeFragmentComponent {
    void inject(FileTypeFragment fileTypeFragment);
}
