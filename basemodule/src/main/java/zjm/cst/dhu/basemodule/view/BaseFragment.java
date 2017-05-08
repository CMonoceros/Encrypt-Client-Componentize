package zjm.cst.dhu.basemodule.view;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by zjm on 3/2/2017.
 */

public abstract class BaseFragment extends Fragment {

    protected abstract int getContentViewLayoutId();

    protected abstract void injectDependences();

    protected abstract void dataBinding(View view);

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(getContentViewLayoutId(), container, false);
        injectDependences();
        dataBinding(rootView);
        return rootView;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
