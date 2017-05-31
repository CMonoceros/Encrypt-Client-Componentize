package zjm.cst.dhu.loginmodule.presenter


import org.apache.commons.codec.binary.Hex
import org.apache.commons.codec.digest.DigestUtils

import rx.Observer
import rx.Subscription
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import rx.subscriptions.CompositeSubscription
import zjm.cst.dhu.basemodule.model.User
import zjm.cst.dhu.loginmodule.LoginContract
import zjm.cst.dhu.loginmodule.usecase.UserUseCase

/**
 * Created by zjm on 2017/2/24.
 */

class LoginPresenter(private val mUserUseCase: UserUseCase) : LoginContract.Presenter {
    private var mView: LoginContract.View? = null
    private var mCompositeSubscription: CompositeSubscription? = null

    override fun attachView(BaseView: LoginContract.View) {
        mView = BaseView
        mCompositeSubscription = CompositeSubscription()
    }

    override fun detachView() {
        if (mCompositeSubscription != null && mCompositeSubscription!!.isUnsubscribed) {
            mCompositeSubscription!!.unsubscribe()
        }
    }

    override fun login(id: String, password: String) {
        val user: User
        val md5Password = String(Hex.encodeHex(DigestUtils.md5(password)))
        try {
            user = User(id = Integer.parseInt(id), password = md5Password)
        } catch (e: Exception) {
            e.printStackTrace()
            mView!!.loginEmptyError()
            return
        }

        mUserUseCase.setUser(user)
        val subscription = mUserUseCase.login()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Observer<User> {
                    override fun onCompleted() {

                    }

                    override fun onError(e: Throwable) {
                        e.printStackTrace()
                        mView!!.loginNetworkError()
                    }

                    override fun onNext(user: User) {
                        mView!!.getLoginState(user)
                    }

                })
        mCompositeSubscription!!.add(subscription)

    }
}
