package zjm.cst.dhu.library.utils.others;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zjm on 2017/5/8.
 */

public class PermissionUtil {

    public static void requestPermission(final Activity context, final List<String> permissionList) {
        Boolean state = false;
        for (int i = 0; i < permissionList.size(); i++) {
            state = ContextCompat.checkSelfPermission(context, permissionList.get(i)) != PackageManager.PERMISSION_GRANTED || state;
        }
        if (state) {
            state = false;
            for (int i = 0; i < permissionList.size(); i++) {
                state = ActivityCompat.shouldShowRequestPermissionRationale(context, Manifest.permission.WRITE_EXTERNAL_STORAGE) || state;
            }
            if (state) {
                new AlertDialog.Builder(context)
                        .setMessage("Request permission")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                ActivityCompat.requestPermissions(context, permissionList.toArray(new String[permissionList.size()]), 1);
                            }
                        })
                        .show();
            } else {
                ActivityCompat.requestPermissions(context, permissionList.toArray(new String[permissionList.size()]), 1);
            }
        }
    }
}
