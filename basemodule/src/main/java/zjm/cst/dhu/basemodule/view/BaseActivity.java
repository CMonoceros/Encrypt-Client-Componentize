package zjm.cst.dhu.basemodule.view;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;


/**
 * Created by zjm on 2017/2/24.
 */

public abstract class BaseActivity extends AppCompatActivity {

    protected abstract void injectDependences();

    protected abstract void dataBinding();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        injectDependences();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }
}
