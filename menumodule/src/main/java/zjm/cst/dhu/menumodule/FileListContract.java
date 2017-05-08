package zjm.cst.dhu.menumodule;

import java.util.List;

import zjm.cst.dhu.basemodule.model.File;
import zjm.cst.dhu.basemodule.model.User;
import zjm.cst.dhu.basemodule.presenter.BasePresenter;
import zjm.cst.dhu.basemodule.view.BaseView;


/**
 * Created by zjm on 3/2/2017.
 */

public interface FileListContract {
    interface View extends BaseView {

        void setUser(User user);

        void setupView();

        void getFileListNetworkError();

        void updateSourceList(List<File> list);

        void fileListOnClick(File file);

    }

    interface Presenter extends BasePresenter<View> {
        void getMenuFileList(int id);

        void getMenuFileListByPaper(int id, int rows, int paper);

    }
}
