package zjm.cst.dhu.registermodule.view;

import android.content.DialogInterface;
import android.content.res.Resources;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import javax.inject.Inject;

import zjm.cst.dhu.basemodule.MyApplication;
import zjm.cst.dhu.basemodule.dagger2.component.ApplicationComponent;
import zjm.cst.dhu.basemodule.dagger2.module.ActivityModule;
import zjm.cst.dhu.basemodule.view.BaseActivity;
import zjm.cst.dhu.registermodule.R;
import zjm.cst.dhu.registermodule.dagger2.component.DaggerRegisterActivityComponent;
import zjm.cst.dhu.registermodule.dagger2.component.RegisterActivityComponent;
import zjm.cst.dhu.registermodule.RegisterContract;
import zjm.cst.dhu.registermodule.dagger2.module.RegisterModule;
import zjm.cst.dhu.registermodule.databinding.ModuleRegisterMainBinding;

/**
 * Created by zjm on 2017/3/1.
 */

public class RegisterActivity extends BaseActivity implements RegisterContract.View {

    @Inject
    RegisterContract.Presenter registerContractPresenter;
    EditText module_register_et_name;
    EditText module_register_et_password;
    EditText module_register_et_confirmPassword;
    EditText module_register_et_verification;
    ImageView module_register_iv_verification;
    RelativeLayout module_register_rl_ui;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dataBinding();
        setupImageView();
    }

    private void setupImageView() {
        module_register_iv_verification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerContractPresenter.generateVerification();
            }
        });
        registerContractPresenter.generateVerification();
    }

    @Override
    protected void injectDependences() {
        ApplicationComponent applicationComponent = ((MyApplication) getApplication()).getApplicationComponent();
        RegisterActivityComponent registerActivityComponent = DaggerRegisterActivityComponent.builder()
                .applicationComponent(applicationComponent)
                .activityModule(new ActivityModule(this))
                .registerModule(new RegisterModule())
                .build();
        registerActivityComponent.inject(this);

        registerContractPresenter.attachView(this);
    }

    @Override
    protected void dataBinding() {
        ModuleRegisterMainBinding binding = DataBindingUtil.setContentView(this, R.layout.module_register_main);
        module_register_et_name = binding.moduleRegisterEtName;
        module_register_et_password = binding.moduleRegisterEtPassword;
        module_register_et_confirmPassword = binding.moduleRegisterEtConfirmPassword;
        module_register_et_verification = binding.moduleRegisterEtVerification;
        module_register_rl_ui = binding.moduleRegisterRlUi;
        module_register_iv_verification = binding.moduleRegisterIvVerification;
        binding.moduleRegisterBOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerTry();
            }
        });
    }


    public void registerTry() {
        registerContractPresenter.registerTry(module_register_et_name.getText().toString() + "", module_register_et_password.getText().toString() + "",
                module_register_et_confirmPassword.getText().toString() + "", module_register_et_verification.getText().toString() + "");
    }

    @Override
    public void confirmError() {
        Toast.makeText(this, "Confirm Error.Please confirm your password!",
                Toast.LENGTH_SHORT).show();
        registerContractPresenter.generateVerification();
    }

    @Override
    public void registerSuccess(int id) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Success");
        builder.setMessage("Success register : Your id is " + id);
        builder.setPositiveButton("Back", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                finish();
            }
        });
        builder.show();
    }

    @Override
    public void registerNetworkError() {
        Toast.makeText(this, "Network Error.Please try again later!",
                Toast.LENGTH_SHORT).show();
        registerContractPresenter.generateVerification();
    }

    @Override
    public void registerEmptyError() {
        Toast.makeText(this, "Name or Password can not be empty!",
                Toast.LENGTH_SHORT).show();
        registerContractPresenter.generateVerification();
    }

    @Override
    public void registerVerificationError() {
        Toast.makeText(this, "Verification Error.Please confirm again!",
                Toast.LENGTH_SHORT).show();
        registerContractPresenter.generateVerification();
    }

    @Override
    public void setVerificationBitmap(Bitmap bitmap) {
        module_register_iv_verification.setImageBitmap(bitmap);
    }

    @Override
    public void passwordUnqualified() {
        Toast.makeText(this, "Password at least 8 and must be mixed letter and number!",
                Toast.LENGTH_SHORT).show();
        registerContractPresenter.generateVerification();
    }

    @Override
    public Resources getRes() {
        return getResources();
    }
}
