package zjm.cst.dhu.menumodule.view;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.yalantis.phoenix.PullToRefreshView;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import zjm.cst.dhu.basemodule.MyApplication;
import zjm.cst.dhu.basemodule.dagger2.component.ApplicationComponent;
import zjm.cst.dhu.basemodule.dagger2.module.ActivityModule;
import zjm.cst.dhu.basemodule.model.File;
import zjm.cst.dhu.basemodule.model.User;
import zjm.cst.dhu.basemodule.view.BaseFragment;
import zjm.cst.dhu.library.utils.appbarlayout.SwipyAppBarScrollListener;
import zjm.cst.dhu.menumodule.FileListContract;
import zjm.cst.dhu.menumodule.dagger2.component.DaggerFileListFragmentComponent;
import zjm.cst.dhu.menumodule.dagger2.component.FileListFragmentComponent;
import zjm.cst.dhu.menumodule.dagger2.module.FileListModule;
import zjm.cst.dhu.menumodule.R;
import zjm.cst.dhu.menumodule.databinding.ModuleMenuFileListBinding;
import zjm.cst.dhu.menumodule.view.adapter.FileListAdapter;

/**
 * Created by zjm on 3/2/2017.
 */

public class FileListFragment extends BaseFragment implements FileListContract.View {

    @Inject
    FileListContract.Presenter fileListPresenter;
    RecyclerView module_menu_rcv_file_list;
    PullToRefreshView module_menu_ptrv_file_list;
    AppBarLayout abl_ui_menu;

    private User user;
    private List<File> sourceFileList;
    private FileListAdapter fileListAdapter;
    private int fileListRows;
    private int fileListPaper;

    @Override
    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public void setupView() {
        module_menu_ptrv_file_list.setOnRefreshListener(new PullToRefreshView.OnRefreshListener() {
            @Override
            public void onRefresh() {
                module_menu_ptrv_file_list.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        fileListPresenter.getMenuFileList(user.getId());
                        module_menu_ptrv_file_list.setRefreshing(false);
                    }
                }, 1000);
            }
        });

        sourceFileList = new ArrayList<File>();
        module_menu_rcv_file_list.setLayoutManager(new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL));
        fileListAdapter = new FileListAdapter(getActivity(), sourceFileList);
        fileListAdapter.setOnItemClickListener(new FileListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                fileListOnClick(sourceFileList.get(position));
            }
        });
        module_menu_rcv_file_list.setAdapter(fileListAdapter);

        fileListPresenter.getMenuFileList(user.getId());
    }

    private void setupActivityView() {
        abl_ui_menu = (AppBarLayout) getActivity().findViewById(R.id.module_menu_abl_ui);
        module_menu_rcv_file_list.addOnScrollListener(new SwipyAppBarScrollListener(abl_ui_menu, module_menu_ptrv_file_list, module_menu_rcv_file_list));
    }

    @Override
    public void getFileListNetworkError() {
        Toast.makeText(getActivity(), "Network Error.Please try again later!",
                Toast.LENGTH_SHORT).show();
    }

    @Override
    public void updateSourceList(List<File> list) {
        sourceFileList.clear();
        for (int i = 0; i < list.size(); i++) {
            sourceFileList.add(list.get(i));
        }
        fileListAdapter.notifyDataSetChanged();
    }

    @Override
    public void fileListOnClick(File file) {
        MenuActivity menuActivity = (MenuActivity) getActivity();
        menuActivity.fileListClick(file);
    }

    @Override
    protected int getContentViewLayoutId() {
        return R.layout.module_menu_file_list;
    }

    @Override
    protected void injectDependences() {
        ApplicationComponent applicationComponent = ((MyApplication) getActivity().getApplication()).getApplicationComponent();
        FileListFragmentComponent fileListFragmentComponent = DaggerFileListFragmentComponent.builder()
                .applicationComponent(applicationComponent)
                .activityModule(new ActivityModule(getActivity()))
                .fileListModule(new FileListModule())
                .build();
        fileListFragmentComponent.inject(this);

        fileListPresenter.attachView(this);
    }

    @Override
    protected void dataBinding(View rootView) {
        ModuleMenuFileListBinding binding = DataBindingUtil.bind(rootView);
        module_menu_ptrv_file_list = binding.moduleMenuPtrvFileList;
        module_menu_rcv_file_list = binding.moduleMenuRcvFileList;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        MenuActivity menuActivity = (MenuActivity) getActivity();
        menuActivity.inToMenu();
        setupView();
        setupActivityView();
        return view;

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        module_menu_rcv_file_list.clearOnScrollListeners();
        MyApplication.getmRefWatcher(getActivity()).watch(this);
    }
}
