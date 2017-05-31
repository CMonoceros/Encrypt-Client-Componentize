package zjm.cst.dhu.menumodule


import zjm.cst.dhu.basemodule.model.File
import zjm.cst.dhu.basemodule.presenter.BasePresenter
import zjm.cst.dhu.basemodule.view.BaseView
import zjm.cst.dhu.library.utils.network.ProgressListener

/**
 * Created by zjm on 3/3/2017.
 */

interface MenuContract {
    interface View : BaseView {
        fun fileListClick(file: File)

        fun chooseFileError()

        fun uploadNetworkError()

        fun uploadSuccess()

        fun uploadFailed()

        fun uploadFileStartService(owner: Int, file: java.io.File)

        fun downloadFileStartService(file: File)

        fun uploadFileNeedWifi()

        fun downloadFileNeedWifi()

        fun downloadFileNetworkError()

        fun downloadFileSuccess(dir: String)

        fun progressListenerShow()

        fun notificationDismiss()

        fun notificationShow()

        fun jobSchedulerCancel()

        fun inToMenu()

        fun outToMenu()
    }

    interface Presenter : BasePresenter<View> {

        fun setupUploadService(owner: Int, file: java.io.File, progressListener: ProgressListener?)

        fun setupDownloadService(file: File, progressListener: ProgressListener?)

        fun uploadServiceWork()

        fun downloadServiceWork()

        fun progressListenerShow()
    }
}
