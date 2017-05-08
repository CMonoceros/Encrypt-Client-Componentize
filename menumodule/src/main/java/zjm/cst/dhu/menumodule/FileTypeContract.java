package zjm.cst.dhu.menumodule;

import java.util.List;

import zjm.cst.dhu.basemodule.model.EncryptRelation;
import zjm.cst.dhu.basemodule.model.EncryptType;
import zjm.cst.dhu.basemodule.model.File;
import zjm.cst.dhu.basemodule.presenter.BasePresenter;
import zjm.cst.dhu.basemodule.view.BaseView;

/**
 * Created by zjm on 2017/3/3.
 */

public interface FileTypeContract {
    interface View extends BaseView {
        void updateEncryptType(List<EncryptType> list);

        void getFileTypeNetworkError();

        void typeDetailClick(EncryptType encryptType);

        void setFile(File file);

        void setDesKey(EncryptRelation encryptRelation);

        void encryptBaseTypeNetworkError();

        void encryptBaseTypeEncryptSuccess();

        void decryptFileExistError();

        void decryptBaseTypeDecryptSuccess();

        void decryptBaseTypeDecryptError();

        void decryptBaseTypeDecryptFailed();

        void confirmDesKey(EncryptRelation encryptRelation);
    }

    interface Presenter extends BasePresenter<View> {
        void getEncryptType();

        void encryptFile(EncryptRelation encryptRelation);

        void encryptBaseType(EncryptRelation encryptRelation, String desKey, String desLayer);

        void decryptFile(EncryptRelation encryptRelation, File file);

        void decryptBaseType(File file, String desKey);
    }
}
