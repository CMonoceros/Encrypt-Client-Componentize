package zjm.cst.dhu.library.utils.adapter;

import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by zjm on 2017/5/7.
 */

public class RecyclerViewHolder extends RecyclerView.ViewHolder {

    private ViewDataBinding viewDataBinding;

    public RecyclerViewHolder(View itemView) {
        super(itemView);
        viewDataBinding = DataBindingUtil.bind(itemView);
    }

    public ViewDataBinding getBinding() {
        return viewDataBinding;
    }
}
