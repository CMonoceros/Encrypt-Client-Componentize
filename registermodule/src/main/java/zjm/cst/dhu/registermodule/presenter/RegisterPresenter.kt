package zjm.cst.dhu.registermodule.presenter

import android.graphics.Bitmap
import android.graphics.BitmapFactory

import org.apache.commons.codec.binary.Hex
import org.apache.commons.codec.digest.DigestUtils

import rx.Observable
import rx.Observer
import rx.Subscriber
import rx.Subscription
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import rx.subscriptions.CompositeSubscription
import zjm.cst.dhu.basemodule.model.User
import zjm.cst.dhu.library.utils.others.VerificationUtil
import zjm.cst.dhu.registermodule.R
import zjm.cst.dhu.registermodule.RegisterContract
import zjm.cst.dhu.registermodule.usecase.UserUseCase

/**
 * Created by zjm on 2017/3/1.
 */

class RegisterPresenter(private val mUserUseCase: UserUseCase) : RegisterContract.Presenter {
    private var mView: RegisterContract.View? = null
    private var mCompositeSubscription: CompositeSubscription? = null
    private var mVerificationUtil: VerificationUtil? = null

    override fun attachView(BaseView: RegisterContract.View) {
        mView = BaseView
        mCompositeSubscription = CompositeSubscription()
    }

    override fun detachView() {
        if (mCompositeSubscription != null && mCompositeSubscription!!.isUnsubscribed) {
            mCompositeSubscription!!.unsubscribe()
        }
    }

    override fun registerTry(name: String, password: String, confirmPassword: String, verification: String) {
        val user: User
        if (password.length < 8) {
            mView!!.passwordUnqualified()
            return
        }
        if (password.matches("[a-zA-z]+".toRegex()) || password.matches("[0-9]*".toRegex())) {
            mView!!.passwordUnqualified()
            return
        }
        if (password != confirmPassword) {
            mView!!.confirmError()
            return
        }
        if (verification.toLowerCase() != mVerificationUtil!!.code!!.toLowerCase()) {
            mView!!.registerVerificationError()
            return
        }
        try {
            val md5Password = String(Hex.encodeHex(DigestUtils.md5(password)))
            user = User(name=name, password = md5Password)
        } catch (e: Exception) {
            e.printStackTrace()
            mView!!.registerEmptyError()
            return
        }

        mUserUseCase.setUser(user)
        val subscription = mUserUseCase.register()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Observer<User> {
                    override fun onCompleted() {

                    }

                    override fun onError(e: Throwable) {
                        e.printStackTrace()
                        mView!!.registerNetworkError()
                    }

                    override fun onNext(user: User?) {
                        if (user == null) {
                            mView!!.registerNetworkError()
                        } else {
                            mView!!.registerSuccess(user.id!!)
                        }
                    }

                })
        mCompositeSubscription!!.add(subscription)
    }

    override fun generateVerification() {
        val subscription = Observable.create(Observable.OnSubscribe<Bitmap> { subscriber ->
            val options = BitmapFactory.Options()
            val scale = 2
            options.inSampleSize = scale
            var bmp = BitmapFactory.decodeResource(mView!!.res, R.drawable.module_register_ic_loading, options)
            subscriber.onNext(bmp)
            mVerificationUtil = VerificationUtil.instance
            bmp = mVerificationUtil!!.createBitmap()
            subscriber.onNext(bmp)
        })
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Observer<Bitmap> {
                    override fun onCompleted() {

                    }

                    override fun onError(e: Throwable) {

                    }

                    override fun onNext(bitmap: Bitmap) {
                        mView!!.setVerificationBitmap(bitmap)
                    }
                })
        mCompositeSubscription!!.add(subscription)


    }
}
