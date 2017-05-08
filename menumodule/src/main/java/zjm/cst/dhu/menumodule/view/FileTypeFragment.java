package zjm.cst.dhu.menumodule.view;

import android.content.DialogInterface;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.swipe.util.Attributes;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import zjm.cst.dhu.basemodule.MyApplication;
import zjm.cst.dhu.basemodule.dagger2.component.ApplicationComponent;
import zjm.cst.dhu.basemodule.dagger2.module.ActivityModule;
import zjm.cst.dhu.basemodule.model.EncryptRelation;
import zjm.cst.dhu.basemodule.model.EncryptType;
import zjm.cst.dhu.basemodule.model.File;
import zjm.cst.dhu.basemodule.view.BaseFragment;
import zjm.cst.dhu.menumodule.FileTypeContract;
import zjm.cst.dhu.menumodule.dagger2.component.DaggerFileTypeFragmentComponent;
import zjm.cst.dhu.menumodule.dagger2.component.FileTypeFragmentComponent;
import zjm.cst.dhu.menumodule.dagger2.module.FileTypeModule;
import zjm.cst.dhu.menumodule.R;
import zjm.cst.dhu.menumodule.databinding.ModuleMenuFileTypeBinding;
import zjm.cst.dhu.menumodule.view.adapter.FileTypeAdapter;

/**
 * Created by zjm on 2017/3/3.
 */

public class FileTypeFragment extends BaseFragment implements FileTypeContract.View {

    @Inject
    FileTypeContract.Presenter fileTypePresenter;
    private List<EncryptType> sourceEncryptTypeList;
    private File file;
    private FileTypeAdapter fileTypeAdapter;

    public RecyclerView module_menu_rcv_file_type;
    public android.support.v7.app.AlertDialog.Builder adb_menu_file_encrypt, adb_others;

    private EditText et_menu_file_encrypt_exinf;
    private View v_menu_file_encrypt;
    private ViewGroup vg_menu_file_encrypt;

    @Override
    public void updateEncryptType(List<EncryptType> list) {
        sourceEncryptTypeList.clear();
        for (int i = 0; i < list.size(); i++) {
            sourceEncryptTypeList.add(list.get(i));
        }
        fileTypeAdapter.notifyDataSetChanged();
    }

    @Override
    public void getFileTypeNetworkError() {
        Toast.makeText(getActivity(), "Network Error.Please try again later!",
                Toast.LENGTH_SHORT).show();
    }

    @Override
    public void typeDetailClick(EncryptType encryptType) {
        adb_others.setTitle(encryptType.getName());
        adb_others.setMessage(encryptType.getDescription());
        adb_others.setNegativeButton("关闭", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        adb_others.show();
    }

    public void setFile(File file) {
        this.file = file;
    }


    @Override
    public void setDesKey(final EncryptRelation encryptRelation) {
        setupEncryptExinfDialog();
        adb_menu_file_encrypt.setTitle("输入DES密钥！");
        adb_menu_file_encrypt.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String i = et_menu_file_encrypt_exinf.getText().toString();
                if (i.length() < 8) {
                    Toast.makeText(getActivity(), "DES Key at least 8 bit!",
                            Toast.LENGTH_SHORT).show();
                } else {
                    dialog.dismiss();
                    fileTypePresenter.encryptBaseType(encryptRelation, i, "1");
                }
            }
        });
        adb_menu_file_encrypt.show();
    }

    @Override
    public void encryptBaseTypeNetworkError() {
        Toast.makeText(getActivity(), "Network Error.Please try again later!",
                Toast.LENGTH_SHORT).show();
    }

    @Override
    public void encryptBaseTypeEncryptSuccess() {
        Toast.makeText(getActivity(), "Encrypt File Success!",
                Toast.LENGTH_SHORT).show();
    }

    @Override
    public void decryptFileExistError() {
        Toast.makeText(getActivity(), "File doesn't exist!Please download first!",
                Toast.LENGTH_SHORT).show();
    }

    @Override
    public void decryptBaseTypeDecryptSuccess() {
        Toast.makeText(getActivity(), "Decrypt File Success!",
                Toast.LENGTH_SHORT).show();
    }

    @Override
    public void decryptBaseTypeDecryptError() {
        Toast.makeText(getActivity(), "Decrypt Error.Please try again later!",
                Toast.LENGTH_SHORT).show();
    }

    @Override
    public void decryptBaseTypeDecryptFailed() {
        Toast.makeText(getActivity(), "Decrypt Failed.Please confirm your key!",
                Toast.LENGTH_SHORT).show();
    }

    @Override
    public void confirmDesKey(EncryptRelation encryptRelation) {
        setupEncryptExinfDialog();
        adb_menu_file_encrypt.setTitle("输入DES密钥！");
        adb_menu_file_encrypt.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String i = et_menu_file_encrypt_exinf.getText().toString();
                if (i.length() < 8) {
                    Toast.makeText(getActivity(), "DES Key at least 8 bit!",
                            Toast.LENGTH_SHORT).show();
                } else {
                    dialog.dismiss();
                    fileTypePresenter.decryptBaseType(file, i);
                }
            }
        });
        adb_menu_file_encrypt.show();
    }

    @Override
    protected int getContentViewLayoutId() {
        return R.layout.module_menu_file_type;
    }

    @Override
    protected void injectDependences() {
        ApplicationComponent applicationComponent = ((MyApplication) getActivity().getApplication()).getApplicationComponent();
        FileTypeFragmentComponent fileTypeFragmentComponent = DaggerFileTypeFragmentComponent.builder()
                .applicationComponent(applicationComponent)
                .activityModule(new ActivityModule(getActivity()))
                .fileTypeModule(new FileTypeModule())
                .build();
        fileTypeFragmentComponent.inject(this);

        fileTypePresenter.attachView(this);
    }

    @Override
    protected void dataBinding(View view) {
        ModuleMenuFileTypeBinding binding = DataBindingUtil.bind(view);
        module_menu_rcv_file_type = binding.moduleMenuRcvFileType;
    }


    private void setupView() {
        adb_others = new android.support.v7.app.AlertDialog.Builder(getActivity());

        sourceEncryptTypeList = new ArrayList<EncryptType>();
        fileTypeAdapter = new FileTypeAdapter(getActivity(), sourceEncryptTypeList);
        fileTypeAdapter.setMode(Attributes.Mode.Single);
        fileTypeAdapter.setDownloadClickListener(new FileTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                MenuActivity menuActivity = (MenuActivity) getActivity();
                menuActivity.downloadFileStartService(file);
            }
        });
        fileTypeAdapter.setEncryptClickListener(new FileTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                EncryptRelation encryptRelation = new EncryptRelation();
                encryptRelation.setFileId(file.getId());
                encryptRelation.setTypeId(sourceEncryptTypeList.get(position).getId());
                fileTypePresenter.encryptFile(encryptRelation);
            }
        });
        fileTypeAdapter.setClickListener(new FileTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                typeDetailClick(sourceEncryptTypeList.get(position));
            }
        });
        fileTypeAdapter.setDecryptClickListener(new FileTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                EncryptRelation encryptRelation = new EncryptRelation();
                encryptRelation.setFileId(file.getId());
                encryptRelation.setTypeId(sourceEncryptTypeList.get(position).getId());
                fileTypePresenter.decryptFile(encryptRelation, file);
            }
        });

        module_menu_rcv_file_type.setLayoutManager(new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL));
        module_menu_rcv_file_type.setAdapter(fileTypeAdapter);

        fileTypePresenter.getEncryptType();
    }

    private void setupEncryptExinfDialog() {
        LayoutInflater inflater = getActivity().getLayoutInflater();
        vg_menu_file_encrypt = (ViewGroup) getActivity().findViewById(R.id.module_menu_rl_file_encrypt_exinf);
        v_menu_file_encrypt = inflater.inflate(R.layout.module_menu_file_encrypt_exinf, vg_menu_file_encrypt);
        et_menu_file_encrypt_exinf = (EditText) v_menu_file_encrypt.findViewById(R.id.module_menu_et_file_encrypt_exinf);
        adb_menu_file_encrypt = new android.support.v7.app.AlertDialog.Builder(getActivity());
        adb_menu_file_encrypt.setView(v_menu_file_encrypt);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        MenuActivity menuActivity = (MenuActivity) getActivity();
        menuActivity.outToMenu();
        setupView();
        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        MyApplication.getmRefWatcher(getActivity()).watch(this);
    }

}
