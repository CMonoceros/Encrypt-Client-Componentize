package zjm.cst.dhu.basemodule.view

import android.os.Bundle
import android.support.v7.app.AppCompatActivity


/**
 * Created by zjm on 2017/2/24.
 */

abstract class BaseActivity : AppCompatActivity() {

    protected abstract fun injectDependences()

    protected abstract fun dataBinding()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        injectDependences()
    }

    override fun onStart() {
        super.onStart()
    }

    override fun onStop() {
        super.onStop()
    }
}
