package zjm.cst.dhu.basemodule.model

import com.google.gson.annotations.Expose

import java.io.Serializable

/**
 * Created by zjm on 3/2/2017.
 */

data class File(
        @Expose var id: Int,
        @Expose var name: String,
        @Expose var owner: Int,
        @Expose var uploadTime: String,
        @Expose var size: String,
        @Expose var path: String,
        @Expose var lastDownloadTime: String? = null,
        @Expose var lastEncryptTime: String? = null
) : Serializable
