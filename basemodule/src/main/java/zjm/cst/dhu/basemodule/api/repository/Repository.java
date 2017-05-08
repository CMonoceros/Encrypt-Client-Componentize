package zjm.cst.dhu.basemodule.api.repository;

import java.util.List;
import java.util.Map;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Retrofit;
import rx.Observable;
import zjm.cst.dhu.basemodule.api.ApiService;
import zjm.cst.dhu.basemodule.model.EncryptRelation;
import zjm.cst.dhu.basemodule.model.EncryptType;
import zjm.cst.dhu.basemodule.model.File;
import zjm.cst.dhu.basemodule.model.User;

/**
 * Created by zjm on 2017/2/23.
 */

public class Repository implements BaseRepository {

    private ApiService mApiService;

    public Repository(Retrofit retrofit) {
        mApiService = retrofit.create(ApiService.class);
    }


    @Override
    public Observable<User> login(User user) {
        return mApiService.login(user);
    }

    @Override
    public Observable<User> register(User user) {
        return mApiService.register(user);
    }

    @Override
    public Observable<List<File>> getFileList(String id) {
        return mApiService.getFileList(id);
    }

    @Override
    public Observable<List<File>> getFileListByPaper(Map<String, String> params) {
        return mApiService.getFileListByPaper(params);
    }

    @Override
    public Observable<List<EncryptType>> getEncryptType() {
        return mApiService.getEncryptType();
    }

    @Override
    public Observable<EncryptRelation> encryptFile(Map<String, String> params) {
        return mApiService.encryptFile(params);
    }

    @Override
    public Observable<File> uploadFile(Map<String,RequestBody> map) {
        return mApiService.uploadFile(map);
    }

    @Override
    public Observable<ResponseBody> downloadFile(String downloadId) {
        return mApiService.downloadFile(downloadId);
    }
}
