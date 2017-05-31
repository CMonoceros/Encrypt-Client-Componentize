package zjm.cst.dhu.basemodule.dagger2.component

import android.app.Application

import dagger.Component
import zjm.cst.dhu.basemodule.MyApplication
import zjm.cst.dhu.basemodule.api.repository.BaseRepository
import zjm.cst.dhu.basemodule.dagger2.module.ApplicationModule
import zjm.cst.dhu.basemodule.dagger2.module.NetworkModule
import zjm.cst.dhu.basemodule.dagger2.scope.PerApplication

/**
 * Created by zjm on 2017/2/23.
 */

@PerApplication
@Component(modules = arrayOf(ApplicationModule::class, NetworkModule::class))
interface ApplicationComponent {

    fun application(): Application

    fun myApplication(): MyApplication

    fun baseApi(): BaseRepository
}
