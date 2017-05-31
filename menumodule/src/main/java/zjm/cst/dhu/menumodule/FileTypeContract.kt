package zjm.cst.dhu.menumodule

import zjm.cst.dhu.basemodule.model.EncryptRelation
import zjm.cst.dhu.basemodule.model.EncryptType
import zjm.cst.dhu.basemodule.model.File
import zjm.cst.dhu.basemodule.presenter.BasePresenter
import zjm.cst.dhu.basemodule.view.BaseView

/**
 * Created by zjm on 2017/3/3.
 */

interface FileTypeContract {
    interface View : BaseView {
        fun updateEncryptType(list: List<EncryptType>)

        fun getFileTypeNetworkError()

        fun setEncryptTypeRateNetworkError()

        fun typeDetailClick(encryptType: EncryptType)

        fun setFile(file: File)

        fun setDesKey(encryptRelation: EncryptRelation)

        fun encryptBaseTypeNetworkError()

        fun encryptBaseTypeEncryptSuccess()

        fun decryptFileExistError()

        fun decryptBaseTypeDecryptSuccess()

        fun decryptBaseTypeDecryptError()

        fun decryptBaseTypeDecryptFailed()

        fun confirmDesKey(encryptRelation: EncryptRelation)
    }

    interface Presenter : BasePresenter<View> {
        fun getEncryptType()

        fun getEncryptTypeByOwner(id: Int)

        fun setEncryptTypeRate(userId: Int, typeId: Int, rate: Float)

        fun encryptFile(encryptRelation: EncryptRelation)

        fun encryptBaseType(encryptRelation: EncryptRelation, desKey: String, desLayer: String)

        fun decryptFile(encryptRelation: EncryptRelation, file: File?)

        fun decryptBaseType(file: File?, desKey: String)
    }
}
