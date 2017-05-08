package zjm.cst.dhu.menumodule.view.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.daimajia.swipe.adapters.RecyclerSwipeAdapter;

import java.util.List;

import zjm.cst.dhu.basemodule.model.EncryptType;
import zjm.cst.dhu.library.utils.adapter.RecyclerViewHolder;
import zjm.cst.dhu.menumodule.BR;
import zjm.cst.dhu.menumodule.R;

/**
 * Created by zjm on 2017/3/3.
 */

public class FileTypeAdapter extends RecyclerSwipeAdapter<RecyclerViewHolder> {

    private List<EncryptType> mData;
    private LayoutInflater mInflater;
    private OnItemClickListener downloadListener;
    private OnItemClickListener encryptListener;
    private OnItemClickListener detailListener;
    private OnItemClickListener decryptListener;

    public FileTypeAdapter(Context context, List<EncryptType> data) {
        mInflater = LayoutInflater.from(context);
        mData = data;
    }

    public void setDownloadClickListener(OnItemClickListener listener) {
        this.downloadListener = listener;
    }

    public void setEncryptClickListener(OnItemClickListener listener) {
        this.encryptListener = listener;
    }

    public void setDecryptClickListener(OnItemClickListener listener) {
        this.decryptListener = listener;
    }

    public void setClickListener(OnItemClickListener listener) {
        this.detailListener = listener;
    }

    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = mInflater.inflate(
                R.layout.module_menu_rv_file_type_item, parent, false);
        return new RecyclerViewHolder(v);
    }

    @Override
    public void onBindViewHolder(RecyclerViewHolder viewHolder, final int position) {
        viewHolder.getBinding().setVariable(BR.type, mData.get(position));
        viewHolder.getBinding().setVariable(BR.decryptListener, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (decryptListener != null) {
                    decryptListener.onItemClick(position);
                }
            }
        });
        viewHolder.getBinding().setVariable(BR.downloadListener, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (downloadListener != null) {
                    downloadListener.onItemClick(position);
                }
            }
        });
        viewHolder.getBinding().setVariable(BR.encryptListener, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (encryptListener != null) {
                    encryptListener.onItemClick(position);
                }
            }
        });
        viewHolder.getBinding().setVariable(BR.detailListener, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (detailListener != null) {
                    detailListener.onItemClick(position);
                }
            }
        });
    }


    @Override
    public int getItemCount() {
        return mData.size();
    }

    @Override
    public int getSwipeLayoutResourceId(int position) {
        return R.id.module_menu_sl_file_type_item;
    }

    public interface OnItemClickListener {
        void onItemClick(int position);
    }
}

