package zjm.cst.dhu.library.utils.others

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.DialogInterface
import android.content.pm.PackageManager
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v7.app.AlertDialog

import java.util.ArrayList

/**
 * Created by zjm on 2017/5/8.
 */

object PermissionUtil {

    fun requestPermission(context: Activity, permissionList: List<String>) {
        var state: Boolean? = false
        for (i in permissionList.indices) {
            state = ContextCompat.checkSelfPermission(context, permissionList[i]) != PackageManager.PERMISSION_GRANTED || state!!
        }
        if (state!!) {
            state = false
            for (i in permissionList.indices) {
                state = ActivityCompat.shouldShowRequestPermissionRationale(context, permissionList[i]) || state!!
            }
            if (state!!) {
                AlertDialog.Builder(context)
                        .setMessage("Request permission")
                        .setPositiveButton("Yes") { dialog, which -> ActivityCompat.requestPermissions(context, permissionList.toTypedArray(), 1) }
                        .show()
            } else {
                ActivityCompat.requestPermissions(context, permissionList.toTypedArray(), 1)
            }
        }
    }
}
