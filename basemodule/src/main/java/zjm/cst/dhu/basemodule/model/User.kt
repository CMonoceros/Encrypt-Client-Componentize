package zjm.cst.dhu.basemodule.model

import com.google.gson.annotations.Expose

import java.io.Serializable

/**
 * Created by zjm on 2017/2/24.
 */

data class User(@Expose var id: Int? = null,
                @Expose var name: String? = null,
                @Expose var password: String? = null,
                @Expose var registerTime: String? = null,
                @Expose var sex: Byte? = null,
                @Expose var tel: Int? = null,
                @Expose var qq: Int? = null,
                @Expose var email: String? = null,
                @Expose var permission: Int? = null
) : Serializable
