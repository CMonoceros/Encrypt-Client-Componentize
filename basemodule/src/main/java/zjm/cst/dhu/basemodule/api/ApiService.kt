package zjm.cst.dhu.basemodule.api

import java.util.List;
import java.util.Map;
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.FieldMap
import retrofit2.http.FormUrlEncoded
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.PartMap
import retrofit2.http.Streaming
import rx.Observable

import zjm.cst.dhu.basemodule.model.EncryptRelation
import zjm.cst.dhu.basemodule.model.EncryptType
import zjm.cst.dhu.basemodule.model.File
import zjm.cst.dhu.basemodule.model.User

/**
 * Created by zjm on 2017/2/23.
 */

interface ApiService {
    @POST("login.action")
    fun login(@Body user: User): Observable<User>?

    @POST("register.action")
    fun register(@Body user: User): Observable<User>?

    @FormUrlEncoded
    @POST("getFileList.action")
    fun getFileList(@Field("owner") id: String): Observable<List<File>>?

    @FormUrlEncoded
    @POST("getFileListByPaper.action")
    fun getFileListByPaper(@FieldMap params: Map<String, String>): Observable<List<File>>?

    @POST("getEncryptType.action")
    fun getEncryptType(): Observable<List<EncryptType>>?

    @FormUrlEncoded
    @POST("getEncryptTypeByOwner.action")
    fun getEncryptTypeByOwner(@Field("user_id") id: String): Observable<List<EncryptType>>?

    @FormUrlEncoded
    @POST("setEncryptTypeRate.action")
    fun setEncryptTypeRate(@FieldMap params: Map<String, String>): Observable<List<EncryptType>>?

    @FormUrlEncoded
    @POST("encryptFile.action")
    fun encryptFile(@FieldMap params: Map<String, String>): Observable<EncryptRelation>?


    @Multipart
    @POST("uploadFile.action")
    fun uploadFile(@PartMap map: Map<String, RequestBody>): Observable<File>?

    @FormUrlEncoded
    @Streaming
    @POST("downloadFile.action")
    fun downloadFile(@Field("id") downloadId: String): Observable<ResponseBody>?
}
