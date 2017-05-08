package zjm.cst.dhu.registermodule.presenter;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import org.apache.commons.codec.binary.Hex;
import org.apache.commons.codec.digest.DigestUtils;

import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;
import zjm.cst.dhu.basemodule.model.User;
import zjm.cst.dhu.library.utils.others.VerificationUtil;
import zjm.cst.dhu.registermodule.R;
import zjm.cst.dhu.registermodule.RegisterContract;
import zjm.cst.dhu.registermodule.usecase.UserUseCase;

/**
 * Created by zjm on 2017/3/1.
 */

public class RegisterPresenter implements RegisterContract.Presenter {

    private UserUseCase mUserUseCase;
    private RegisterContract.View mView;
    private CompositeSubscription mCompositeSubscription;
    private VerificationUtil mVerificationUtil;

    public RegisterPresenter(UserUseCase userUseCase) {
        this.mUserUseCase = userUseCase;
    }

    @Override
    public void attachView(RegisterContract.View BaseView) {
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
    public void registerTry(String name, String password, String confirmPassword, String verification) {
        User user;
        if (password.length() < 8) {
            mView.passwordUnqualified();
            return;
        }
        if (password.matches("[a-zA-z]+") || password.matches("[0-9]*")) {
            mView.passwordUnqualified();
            return;
        }
        if (!password.equals(confirmPassword)) {
            mView.confirmError();
            return;
        }
        if (!verification.toLowerCase().equals(mVerificationUtil.getCode().toLowerCase())) {
            mView.registerVerificationError();
            return;
        }
        try {
            String md5Password = new String(Hex.encodeHex(DigestUtils.md5(password)));
            user = new User(name, md5Password);
        } catch (Exception e) {
            e.printStackTrace();
            mView.registerEmptyError();
            return;
        }
        mUserUseCase.setUser(user);
        Subscription subscription = mUserUseCase.register()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<User>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        mView.registerNetworkError();
                    }

                    @Override
                    public void onNext(User user) {
                        if (user == null) {
                            mView.registerNetworkError();
                        } else {
                            mView.registerSuccess(user.getId());
                        }
                    }

                });
        mCompositeSubscription.add(subscription);
    }

    @Override
    public void generateVerification() {
        Subscription subscription = Observable.create(new Observable.OnSubscribe<Bitmap>() {

            @Override
            public void call(Subscriber<? super Bitmap> subscriber) {
                BitmapFactory.Options options = new BitmapFactory.Options();
                int scale = 2;
                options.inSampleSize = scale;
                Bitmap bmp = BitmapFactory.decodeResource(mView.getRes(), R.drawable.module_register_ic_loading, options);
                subscriber.onNext(bmp);
                mVerificationUtil = VerificationUtil.getInstance();
                bmp = mVerificationUtil.createBitmap();
                subscriber.onNext(bmp);
            }
        })
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Bitmap>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(Bitmap bitmap) {
                        mView.setVerificationBitmap(bitmap);
                    }
                });
        mCompositeSubscription.add(subscription);


    }
}
