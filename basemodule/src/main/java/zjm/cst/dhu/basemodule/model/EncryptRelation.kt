package zjm.cst.dhu.basemodule.model

import com.google.gson.annotations.Expose

import java.io.Serializable

/**
 * Created by zjm on 3/23/2017.
 */

data class EncryptRelation(
        @Expose var id: Int?=null,
        @Expose var fileId: Int?=null,
        @Expose var typeId: Int?=null
) : Serializable
