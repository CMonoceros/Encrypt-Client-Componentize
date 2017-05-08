package zjm.cst.dhu.basemodule.api.repository;


import java.util.List;
import java.util.Map;

import zjm.cst.dhu.basemodule.model.EncryptRelation;
import zjm.cst.dhu.basemodule.model.EncryptType;
import zjm.cst.dhu.basemodule.model.File;
import zjm.cst.dhu.basemodule.model.User;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import rx.Observable;

/**
 * Created by zjm on 2017/2/23.
 */

public interface BaseRepository {
    Observable<User> login(User user);

    Observable<User> register(User user);

    Observable<List<File>> getFileList(String id);

    Observable<List<File>> getFileListByPaper(Map<String, String> params);

    Observable<List<EncryptType>> getEncryptType();

    Observable<EncryptRelation> encryptFile(Map<String, String> params);

    Observable<File> uploadFile(Map<String, RequestBody> map);

    Observable<ResponseBody> downloadFile(String downloadId);
}
