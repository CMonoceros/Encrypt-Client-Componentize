package zjm.cst.dhu.basemodule.api;

import android.os.Environment;

/**
 * Created by zjm on 2017/2/23.
 */

public interface BaseUrl {
    String BASEHTTP = "http://";
    String BASEWEBSOCKET = "ws://";
    String BASEPORT = ":80/Final/App/";
    String BASEIP = "115.159.73.148";
    String DOWNLOADPATH = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Encrypt/";
    String EXTRAMENUUSER = "extra_menu_user";
    String MENUMODULENAME = "zjm.cst.dhu.menumodule.view.MenuActivity";
}
