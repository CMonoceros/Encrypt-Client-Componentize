package zjm.cst.dhu.library.utils.others;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.view.View;
import android.view.WindowManager;

/**
 * Created by zjm on 2017/5/7.
 */

public class BitmapUtil {

    public static BitmapDrawable adjustBackgroundByWindow(Context context, int drawableId) {
        Point point = new Point();
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        windowManager.getDefaultDisplay().getSize(point);
        int scrWidth = point.x;
        int scrHeight = point.y;

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(context.getResources(), drawableId, options);
        int picWidth = options.outWidth;
        int picHeight = options.outHeight;

        int dx = picWidth / scrWidth;
        int dy = picHeight / scrHeight;
        int scale = 1;
        if (dx >= dy && dy >= 1) {
            scale = dx;
        }
        if (dy >= dx && dx >= 1) {
            scale = dy;
        }

        options.inSampleSize = scale;
        options.inJustDecodeBounds = false;
        Bitmap bmp = BitmapFactory.decodeResource(context.getResources(), drawableId, options);
        BitmapDrawable bitmapDrawable = new BitmapDrawable(context.getResources(), bmp);
        return bitmapDrawable;
    }

    public static BitmapDrawable adjustBackground(Context context, int drawableId, int srcWidth, int srcHeight) {

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(context.getResources(), drawableId, options);
        int picWidth = options.outWidth;
        int picHeight = options.outHeight;

        int dx = picWidth / srcWidth;
        int dy = picHeight / srcHeight;
        int scale = 1;
        if (dx >= dy && dy >= 1) {
            scale = dx;
        }
        if (dy >= dx && dx >= 1) {
            scale = dy;
        }

        options.inSampleSize = scale;
        options.inJustDecodeBounds = false;
        Bitmap bmp = BitmapFactory.decodeResource(context.getResources(), drawableId, options);
        BitmapDrawable bitmapDrawable = new BitmapDrawable(context.getResources(), bmp);
        return bitmapDrawable;
    }
}
