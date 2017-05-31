package zjm.cst.dhu.basemodule.presenter

import zjm.cst.dhu.basemodule.view.BaseView

/**
 * Created by zjm on 2017/2/24.
 */

interface BasePresenter<T : BaseView> {
    fun attachView(BaseView: T)

    fun detachView()
}
