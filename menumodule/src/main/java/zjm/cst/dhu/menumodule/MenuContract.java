package zjm.cst.dhu.menumodule;


import zjm.cst.dhu.basemodule.model.File;
import zjm.cst.dhu.basemodule.presenter.BasePresenter;
import zjm.cst.dhu.basemodule.view.BaseView;
import zjm.cst.dhu.library.utils.network.ProgressListener;

/**
 * Created by zjm on 3/3/2017.
 */

public interface MenuContract {
    interface View extends BaseView {
        void fileListClick(File file);

        void chooseFileError();

        void uploadNetworkError();

        void uploadSuccess();

        void uploadFailed();

        void uploadFileStartService(int owner, java.io.File file);

        void downloadFileStartService(File file);

        void uploadFileNeedWifi();

        void downloadFileNeedWifi();

        void downloadFileNetworkError();

        void downloadFileSuccess(String dir);

        void progressListenerShow();

        void notificationDismiss();

        void notificationShow();

        void jobSchedulerCancel();

        void inToMenu();

        void outToMenu();
    }

    interface Presenter extends BasePresenter<View> {

        void setupUploadService(int owner, java.io.File file, ProgressListener progressListener);

        void setupDownloadService(File file, ProgressListener progressListener);

        void uploadServiceWork();

        void downloadServiceWork();

        void progressListenerShow();
    }
}
