package zjm.cst.dhu.basemodule.api

import android.os.Environment

/**
 * Created by zjm on 2017/2/23.
 */

class BaseUrl {
    companion object{
        val BASEHTTP = "http://"
        val BASEWEBSOCKET = "ws://"
        val BASEPORT = ":8080/App/"
        val BASEIP = "172.27.35.1"
        val DOWNLOADPATH = Environment.getExternalStorageDirectory().absolutePath + "/Encrypt/"
        val EXTRAMENUUSER = "extra_menu_user"
        val MENUMODULENAME = "zjm.cst.dhu.menumodule.view.MenuActivity"
    }
}
