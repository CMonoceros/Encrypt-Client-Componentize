package zjm.cst.dhu.loginmodule.presenter;


import org.apache.commons.codec.binary.Hex;
import org.apache.commons.codec.digest.DigestUtils;

import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;
import zjm.cst.dhu.basemodule.model.User;
import zjm.cst.dhu.loginmodule.LoginContract;
import zjm.cst.dhu.loginmodule.usecase.UserUseCase;

/**
 * Created by zjm on 2017/2/24.
 */

public class LoginPresenter implements LoginContract.Presenter {
    private UserUseCase mUserUseCase;
    private LoginContract.View mView;
    private CompositeSubscription mCompositeSubscription;

    public LoginPresenter(UserUseCase userUseCase) {
        this.mUserUseCase = userUseCase;
    }

    @Override
    public void attachView(LoginContract.View BaseView) {
        mView = BaseView;
        mCompositeSubscription = new CompositeSubscription();
    }

    @Override
    public void detachView() {
        if (mCompositeSubscription != null && mCompositeSubscription.isUnsubscribed()) {
            mCompositeSubscription.unsubscribe();
        }
    }

    @Override
    public void login(String id, String password) {
        User user;
        String md5Password = new String(Hex.encodeHex(DigestUtils.md5(password)));
        try {
            user = new User(Integer.parseInt(id), md5Password);
        } catch (Exception e) {
            e.printStackTrace();
            mView.loginEmptyError();
            return;
        }
        mUserUseCase.setUser(user);
        Subscription subscription = mUserUseCase.login()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<User>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        mView.loginNetworkError();
                    }

                    @Override
                    public void onNext(User user) {
                        mView.getLoginState(user);
                    }

                });
        mCompositeSubscription.add(subscription);

    }
}
