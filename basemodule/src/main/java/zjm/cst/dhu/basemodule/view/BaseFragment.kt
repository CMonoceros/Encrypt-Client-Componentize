package zjm.cst.dhu.basemodule.view

import android.app.Fragment
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

/**
 * Created by zjm on 3/2/2017.
 */

abstract class BaseFragment : Fragment() {

    protected abstract val contentViewLayoutId: Int

    protected abstract fun injectDependences()

    protected abstract fun dataBinding(view: View)

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val rootView = inflater!!.inflate(contentViewLayoutId, container, false)
        injectDependences()
        dataBinding(rootView)
        return rootView
    }

    override fun onDestroy() {
        super.onDestroy()
    }
}
