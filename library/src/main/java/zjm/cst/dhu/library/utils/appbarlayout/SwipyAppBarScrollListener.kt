package zjm.cst.dhu.library.utils.appbarlayout

import android.support.design.widget.AppBarLayout
import android.support.v4.view.ViewCompat
import android.support.v7.widget.RecyclerView
import android.view.ViewGroup

/**
 * Created by zjm on 2017/1/11.
 */

class SwipyAppBarScrollListener(private val appBarLayout: AppBarLayout?, private val refreshLayout: ViewGroup?, private val recyclerView: RecyclerView?) : RecyclerView.OnScrollListener(), AppBarLayout.OnOffsetChangedListener {
    private var isAppBarLayoutOpen = true
    private var isAppBarLayoutClose: Boolean = false

    init {
        disptachScrollRefresh()
    }


    private fun disptachScrollRefresh() {
        if (this.appBarLayout != null && this.recyclerView != null && refreshLayout != null) {
            this.appBarLayout.addOnOffsetChangedListener(this)
            this.recyclerView.addOnScrollListener(this)
        }
    }

    override fun onScrollStateChanged(recyclerView: RecyclerView?, newState: Int) {
        super.onScrollStateChanged(recyclerView, newState)
    }

    override fun onScrolled(recyclerView: RecyclerView?, dx: Int, dy: Int) {
        super.onScrolled(recyclerView, dx, dy)
        dispatchScroll()
    }

    override fun onOffsetChanged(appBarLayout: AppBarLayout, verticalOffset: Int) {
        isAppBarLayoutOpen = DesignViewUtils.isAppBarLayoutOpen(verticalOffset)
        isAppBarLayoutClose = DesignViewUtils.isAppBarLayoutClose(appBarLayout, verticalOffset)
        dispatchScroll()
    }

    private fun dispatchScroll() {
        if (this.recyclerView != null && this.appBarLayout != null && this.refreshLayout != null) {
            //不可滚动
            if (!(ViewCompat.canScrollVertically(recyclerView, -1) || ViewCompat.canScrollVertically(recyclerView, 1))) {
                refreshLayout.isEnabled = isAppBarLayoutOpen
            } else
            //可以滚动
            {
                if (isAppBarLayoutOpen || isAppBarLayoutClose) {
                    if (!ViewCompat.canScrollVertically(recyclerView, -1) && isAppBarLayoutOpen) {
                        refreshLayout.isEnabled = true
                    } else if (isAppBarLayoutClose && !ViewCompat.canScrollVertically(recyclerView, 1)) {
                        refreshLayout.isEnabled = true
                    } else {
                        refreshLayout.isEnabled = false
                    }
                } else {
                    refreshLayout.isEnabled = false
                }
            }
        }
    }
}
