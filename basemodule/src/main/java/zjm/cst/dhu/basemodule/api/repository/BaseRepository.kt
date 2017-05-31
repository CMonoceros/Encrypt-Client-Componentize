package zjm.cst.dhu.basemodule.api.repository

import java.util.List;
import java.util.Map;
import zjm.cst.dhu.basemodule.model.EncryptRelation
import zjm.cst.dhu.basemodule.model.EncryptType
import zjm.cst.dhu.basemodule.model.File
import zjm.cst.dhu.basemodule.model.User
import okhttp3.RequestBody
import okhttp3.ResponseBody
import rx.Observable

/**
 * Created by zjm on 2017/2/23.
 */

interface BaseRepository {
    fun login(user: User): Observable<User>?

    fun register(user: User): Observable<User>?

    fun getFileList(id: String): Observable<List<File>>?

    fun getFileListByPaper(params: Map<String, String>): Observable<List<File>>?

    fun getEncryptType(): Observable<List<EncryptType>>?

    fun getEncryptTypeByOwner(id: String): Observable<List<EncryptType>>?

    fun setEncryptTypeRate(params: Map<String, String>): Observable<List<EncryptType>>?

    fun encryptFile(params: Map<String, String>): Observable<EncryptRelation>?

    fun uploadFile(map: Map<String, RequestBody>): Observable<File>?

    fun downloadFile(downloadId: String): Observable<ResponseBody>?
}
