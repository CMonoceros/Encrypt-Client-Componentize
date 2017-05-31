package zjm.cst.dhu.basemodule.dagger2.scope

import java.lang.annotation.Retention
import java.lang.annotation.RetentionPolicy

import javax.inject.Scope

/**
 * Created by zjm on 2017/2/23.
 */
@Scope
@Retention(RetentionPolicy.RUNTIME)
annotation class PerApplication
