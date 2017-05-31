package zjm.cst.dhu.library.utils.adapter

import android.databinding.DataBindingUtil
import android.databinding.ViewDataBinding
import android.support.v7.widget.RecyclerView
import android.view.View

/**
 * Created by zjm on 2017/5/7.
 */

class RecyclerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    val binding: ViewDataBinding

    init {
        binding = DataBindingUtil.bind<ViewDataBinding>(itemView)
    }
}
