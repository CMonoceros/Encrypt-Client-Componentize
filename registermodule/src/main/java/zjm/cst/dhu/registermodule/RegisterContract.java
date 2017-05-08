package zjm.cst.dhu.registermodule;

import android.content.res.Resources;
import android.graphics.Bitmap;

import zjm.cst.dhu.basemodule.presenter.BasePresenter;
import zjm.cst.dhu.basemodule.view.BaseView;


/**
 * Created by zjm on 2017/3/1.
 */

public interface RegisterContract {
    interface View extends BaseView {
        void confirmError();

        void registerSuccess(int id);

        void registerNetworkError();

        void registerEmptyError();

        void registerVerificationError();

        void setVerificationBitmap(Bitmap bitmap);

        void passwordUnqualified();

        Resources getRes();
    }

    interface Presenter extends BasePresenter<View> {

        void generateVerification();

        void registerTry(String name, String password, String confirmPassword, String verification);
    }
}
