package zjm.cst.dhu.loginmodule;


import zjm.cst.dhu.basemodule.model.User;
import zjm.cst.dhu.basemodule.presenter.BasePresenter;
import zjm.cst.dhu.basemodule.view.BaseView;

/**
 * Created by zjm on 2017/2/24.
 */

public interface LoginContract {
    interface View extends BaseView {
        void getLoginState(User user);

        void loginNetworkError();

        void loginPasswordError();

        void loginEmptyError();
    }

    interface Presenter extends BasePresenter<View> {
        void login(String id, String password);
    }
}
