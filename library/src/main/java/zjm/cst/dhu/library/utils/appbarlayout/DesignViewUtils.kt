package zjm.cst.dhu.library.utils.appbarlayout

import android.support.design.widget.AppBarLayout
import android.support.v7.widget.RecyclerView

/**
 * Created by zjm on 2017/1/11.
 */

object DesignViewUtils {

    /**
     * AppBarLayout 完全显示 打开状态

     * @param verticalOffset
     * *
     * @return
     */
    fun isAppBarLayoutOpen(verticalOffset: Int): Boolean {
        return verticalOffset >= 0
    }

    /**
     * AppBarLayout 关闭或折叠状态

     * @param appBarLayout
     * *
     * @param verticalOffset
     * *
     * @return
     */
    fun isAppBarLayoutClose(appBarLayout: AppBarLayout, verticalOffset: Int): Boolean {
        return appBarLayout.totalScrollRange == Math.abs(verticalOffset)
    }

    /**
     * RecyclerView 滚动到底部 最后一条完全显示

     * @param recyclerView
     * *
     * @return
     */
    fun isSlideToBottom(recyclerView: RecyclerView?): Boolean {
        if (recyclerView == null) return false
        if (recyclerView.computeVerticalScrollExtent() + recyclerView.computeVerticalScrollOffset() >= recyclerView.computeVerticalScrollRange())
            return true
        return false
    }

    /**
     * RecyclerView 滚动到顶端

     * @param recyclerView
     * *
     * @return
     */
    fun isSlideToTop(recyclerView: RecyclerView): Boolean {
        return recyclerView.computeVerticalScrollOffset() <= 0
    }
}
