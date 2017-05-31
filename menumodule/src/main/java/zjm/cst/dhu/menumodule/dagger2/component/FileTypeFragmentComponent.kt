package zjm.cst.dhu.menumodule.dagger2.component

import dagger.Component
import zjm.cst.dhu.basemodule.dagger2.component.ApplicationComponent
import zjm.cst.dhu.basemodule.dagger2.module.ActivityModule
import zjm.cst.dhu.basemodule.dagger2.scope.PerActivity
import zjm.cst.dhu.menumodule.dagger2.module.FileTypeModule
import zjm.cst.dhu.menumodule.view.FileTypeFragment

/**
 * Created by zjm on 2017/3/3.
 */
@PerActivity
@Component(dependencies = arrayOf(ApplicationComponent::class), modules = arrayOf(ActivityModule::class, FileTypeModule::class))
interface FileTypeFragmentComponent {
    fun inject(fileTypeFragment: FileTypeFragment)
}
