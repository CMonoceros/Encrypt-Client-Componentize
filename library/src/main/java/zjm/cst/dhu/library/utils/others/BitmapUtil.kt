package zjm.cst.dhu.library.utils.others

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Point
import android.graphics.drawable.BitmapDrawable
import android.view.View
import android.view.WindowManager

/**
 * Created by zjm on 2017/5/7.
 */

object BitmapUtil {

    fun adjustBackgroundByWindow(context: Context, drawableId: Int): BitmapDrawable {
        val point = Point()
        val windowManager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        windowManager.defaultDisplay.getSize(point)
        val scrWidth = point.x
        val scrHeight = point.y

        val options = BitmapFactory.Options()
        options.inJustDecodeBounds = true
        BitmapFactory.decodeResource(context.resources, drawableId, options)
        val picWidth = options.outWidth
        val picHeight = options.outHeight

        val dx = picWidth / scrWidth
        val dy = picHeight / scrHeight
        var scale = 1
        if (dx >= dy && dy >= 1) {
            scale = dx
        }
        if (dy >= dx && dx >= 1) {
            scale = dy
        }

        options.inSampleSize = scale
        options.inJustDecodeBounds = false
        val bmp = BitmapFactory.decodeResource(context.resources, drawableId, options)
        val bitmapDrawable = BitmapDrawable(context.resources, bmp)
        return bitmapDrawable
    }

    fun adjustBackground(context: Context, drawableId: Int, srcWidth: Int, srcHeight: Int): BitmapDrawable {

        val options = BitmapFactory.Options()
        options.inJustDecodeBounds = true
        BitmapFactory.decodeResource(context.resources, drawableId, options)
        val picWidth = options.outWidth
        val picHeight = options.outHeight

        val dx = picWidth / srcWidth
        val dy = picHeight / srcHeight
        var scale = 1
        if (dx >= dy && dy >= 1) {
            scale = dx
        }
        if (dy >= dx && dx >= 1) {
            scale = dy
        }

        options.inSampleSize = scale
        options.inJustDecodeBounds = false
        val bmp = BitmapFactory.decodeResource(context.resources, drawableId, options)
        val bitmapDrawable = BitmapDrawable(context.resources, bmp)
        return bitmapDrawable
    }
}
