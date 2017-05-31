package zjm.cst.dhu.menumodule

import zjm.cst.dhu.basemodule.model.File
import zjm.cst.dhu.basemodule.model.User
import zjm.cst.dhu.basemodule.presenter.BasePresenter
import zjm.cst.dhu.basemodule.view.BaseView


/**
 * Created by zjm on 3/2/2017.
 */

interface FileListContract {
    interface View : BaseView {

        fun setUser(user: User?)

        fun setupView()

        fun getFileListNetworkError()

        fun updateSourceList(list: List<File>)

        fun fileListOnClick(file: File)

    }

    interface Presenter : BasePresenter<View> {
        fun getMenuFileList(id: Int)

        fun getMenuFileListByPaper(id: Int, rows: Int, paper: Int)

    }
}
