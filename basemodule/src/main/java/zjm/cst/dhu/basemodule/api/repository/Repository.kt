package zjm.cst.dhu.basemodule.api.repository

import java.util.List;
import java.util.Map;
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Retrofit
import rx.Observable
import zjm.cst.dhu.basemodule.api.ApiService
import zjm.cst.dhu.basemodule.model.EncryptRelation
import zjm.cst.dhu.basemodule.model.EncryptType
import zjm.cst.dhu.basemodule.model.File
import zjm.cst.dhu.basemodule.model.User

/**
 * Created by zjm on 2017/2/23.
 */

class Repository(retrofit: Retrofit) : BaseRepository {

    private val mApiService: ApiService

    init {
        mApiService = retrofit.create(ApiService::class.java)
    }


    override fun login(user: User): Observable<User>? {
        return mApiService.login(user)
    }

    override fun register(user: User): Observable<User>? {
        return mApiService.register(user)
    }

    override fun getFileList(id: String): Observable<List<File>>? {
        return mApiService.getFileList(id)
    }

    override fun getFileListByPaper(params: Map<String, String>): Observable<List<File>>? {
        return mApiService.getFileListByPaper(params)
    }

    override fun getEncryptType(): Observable<List<EncryptType>>? {
        return mApiService.getEncryptType()
    }

    override fun setEncryptTypeRate(params: Map<String, String>): Observable<List<EncryptType>>? {
        return mApiService.setEncryptTypeRate(params)
    }

    override fun getEncryptTypeByOwner(id: String): Observable<List<EncryptType>>? {
        return mApiService.getEncryptTypeByOwner(id)
    }

    override fun encryptFile(params: Map<String, String>): Observable<EncryptRelation>? {
        return mApiService.encryptFile(params)
    }

    override fun uploadFile(map: Map<String, RequestBody>): Observable<File>? {
        return mApiService.uploadFile(map)
    }

    override fun downloadFile(downloadId: String): Observable<ResponseBody>? {
        return mApiService.downloadFile(downloadId)
    }
}
