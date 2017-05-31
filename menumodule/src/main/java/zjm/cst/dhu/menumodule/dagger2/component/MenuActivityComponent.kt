package zjm.cst.dhu.menumodule.dagger2.component

import dagger.Component
import zjm.cst.dhu.basemodule.dagger2.component.ApplicationComponent
import zjm.cst.dhu.basemodule.dagger2.module.ActivityModule
import zjm.cst.dhu.basemodule.dagger2.scope.PerActivity
import zjm.cst.dhu.menumodule.view.MenuActivity
import zjm.cst.dhu.menumodule.dagger2.module.MenuModule

/**
 * Created by zjm on 2/9/2017.
 */

@PerActivity
@Component(dependencies = arrayOf(ApplicationComponent::class), modules = arrayOf(ActivityModule::class, MenuModule::class))
interface MenuActivityComponent {
    fun inject(menuActivity: MenuActivity)
}
