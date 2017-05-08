package zjm.cst.dhu.menumodule.dagger2.component;

import dagger.Component;
import zjm.cst.dhu.basemodule.dagger2.component.ApplicationComponent;
import zjm.cst.dhu.basemodule.dagger2.module.ActivityModule;
import zjm.cst.dhu.basemodule.dagger2.scope.PerActivity;
import zjm.cst.dhu.menumodule.dagger2.module.FileListModule;
import zjm.cst.dhu.menumodule.view.FileListFragment;

/**
 * Created by zjm on 3/2/2017.
 */

@PerActivity
@Component(dependencies = ApplicationComponent.class ,modules = {ActivityModule.class, FileListModule.class})
public interface FileListFragmentComponent {
    void inject(FileListFragment fileListFragment);
}
