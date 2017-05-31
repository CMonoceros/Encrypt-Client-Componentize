package zjm.cst.dhu.basemodule.model

import com.google.gson.annotations.Expose

import java.io.Serializable

/**
 * Created by zjm on 2017/3/3.
 */

data class EncryptType(
        @Expose var id: Int,
        @Expose var name: String,
        @Expose var description: String
) : Serializable
