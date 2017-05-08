package zjm.cst.dhu.menumodule.view.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import java.util.ArrayList;
import java.util.List;

import zjm.cst.dhu.basemodule.model.File;
import zjm.cst.dhu.library.utils.adapter.RecyclerViewHolder;
import zjm.cst.dhu.menumodule.BR;
import zjm.cst.dhu.menumodule.R;

/**
 * Created by zjm on 3/2/2017.
 */

public class FileListAdapter extends RecyclerView.Adapter<RecyclerViewHolder> {
    private List<File> list = new ArrayList<>();
    private LayoutInflater mInflater;
    private OnItemClickListener listener;

    public FileListAdapter(Context mContext, List<File> list) {
        mInflater = LayoutInflater.from(mContext);
        this.list = list;
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = mInflater.inflate(R.layout.module_menu_rv_file_list_item, parent,
                false);
        return new RecyclerViewHolder(v);
    }

    @Override
    public void onBindViewHolder(RecyclerViewHolder holder,final int position) {
        holder.getBinding().setVariable(BR.file,list.get(position));
        holder.getBinding().setVariable(BR.clickListener, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onItemClick(position);
                }
            }
        });
    }


    @Override
    public int getItemCount() {
        return list.size();
    }

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

}