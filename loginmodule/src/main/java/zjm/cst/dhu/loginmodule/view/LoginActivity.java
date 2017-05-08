package zjm.cst.dhu.loginmodule.view;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import zjm.cst.dhu.basemodule.MyApplication;
import zjm.cst.dhu.basemodule.api.BaseUrl;
import zjm.cst.dhu.basemodule.dagger2.component.ApplicationComponent;
import zjm.cst.dhu.basemodule.dagger2.module.ActivityModule;
import zjm.cst.dhu.basemodule.model.User;
import zjm.cst.dhu.basemodule.view.BaseActivity;
import zjm.cst.dhu.library.utils.others.PermissionUtil;
import zjm.cst.dhu.loginmodule.LoginContract;
import zjm.cst.dhu.loginmodule.R;
import zjm.cst.dhu.loginmodule.dagger2.component.DaggerLoginActivityComponent;
import zjm.cst.dhu.loginmodule.dagger2.component.LoginActivityComponent;
import zjm.cst.dhu.loginmodule.dagger2.module.LoginModule;
import zjm.cst.dhu.loginmodule.databinding.ModuleLoginMainBinding;

/**
 * Created by zjm on 2017/2/24.
 */

public class LoginActivity extends BaseActivity implements LoginContract.View {

    @Inject
    LoginContract.Presenter loginContractPresenter;
    public EditText module_login_et_id;
    public EditText module_login_et_password;
    public RelativeLayout module_login_rl_ui;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dataBinding();
    }


    @Override
    protected void injectDependences() {
        ApplicationComponent applicationComponent = ((MyApplication) getApplication()).getApplicationComponent();
        LoginActivityComponent loginActivityComponent = DaggerLoginActivityComponent.builder()
                .applicationComponent(applicationComponent)
                .activityModule(new ActivityModule(this))
                .loginModule(new LoginModule())
                .build();
        loginActivityComponent.inject(this);

        loginContractPresenter.attachView(this);
    }

    @Override
    protected void dataBinding() {
        ModuleLoginMainBinding binding = DataBindingUtil.setContentView(this, R.layout.module_login_main);
        module_login_et_id = binding.moduleLoginEtId;
        module_login_et_password = binding.moduleLoginEtPassword;
        module_login_rl_ui = binding.moduleLoginRlUi;
        binding.moduleLoginBLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginInternet();
            }
        });
        binding.moduleLoginBRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                register();
            }
        });
    }


    public void loginInternet() {
        loginContractPresenter.login(module_login_et_id.getText().toString() + "", module_login_et_password.getText().toString() + "");
    }

    public void register() {
        Uri uri = new Uri.Builder().scheme("RegisterModule").build();
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(intent);
        overridePendingTransition(android.support.v7.appcompat.R.anim.abc_fade_in,
                android.support.v7.appcompat.R.anim.abc_fade_out);
    }

    @Override
    public void getLoginState(User user) {
        if (user != null) {
            Uri uri = new Uri.Builder().scheme("MenuModule").build();
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            Bundle bundle = new Bundle();
            bundle.putSerializable(BaseUrl.EXTRAMENUUSER, user);
            intent.putExtras(bundle);
            startActivity(intent);
            overridePendingTransition(android.support.v7.appcompat.R.anim.abc_fade_in,
                    android.support.v7.appcompat.R.anim.abc_fade_out);
        } else {
            loginPasswordError();
        }
    }

    @Override
    public void loginNetworkError() {
        Toast.makeText(this, "Network Error.Please try again later!",
                Toast.LENGTH_SHORT).show();
    }

    @Override
    public void loginPasswordError() {
        Toast.makeText(this, "ID or Password Wrong.Please confirm!",
                Toast.LENGTH_SHORT).show();
    }

    @Override
    public void loginEmptyError() {
        Toast.makeText(this, "ID or Password can not be empty!",
                Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_DENIED) {
                    if (!ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission_group.STORAGE)) {
                        Toast.makeText(this, "Permission is denied!It will cause some error!",
                                Toast.LENGTH_SHORT).show();
                    }
                }
                break;
        }
    }
}
