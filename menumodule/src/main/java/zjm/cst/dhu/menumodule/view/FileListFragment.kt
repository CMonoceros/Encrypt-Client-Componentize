package zjm.cst.dhu.menumodule.view

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.design.widget.AppBarLayout
import android.support.design.widget.CollapsingToolbarLayout
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.StaggeredGridLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast

import com.yalantis.phoenix.PullToRefreshView

import java.util.ArrayList

import javax.inject.Inject

import zjm.cst.dhu.basemodule.MyApplication
import zjm.cst.dhu.basemodule.dagger2.component.ApplicationComponent
import zjm.cst.dhu.basemodule.dagger2.module.ActivityModule
import zjm.cst.dhu.basemodule.model.File
import zjm.cst.dhu.basemodule.model.User
import zjm.cst.dhu.basemodule.view.BaseFragment
import zjm.cst.dhu.library.utils.appbarlayout.SwipyAppBarScrollListener
import zjm.cst.dhu.menumodule.FileListContract
import zjm.cst.dhu.menumodule.dagger2.component.DaggerFileListFragmentComponent
import zjm.cst.dhu.menumodule.dagger2.component.FileListFragmentComponent
import zjm.cst.dhu.menumodule.dagger2.module.FileListModule
import zjm.cst.dhu.menumodule.R
import zjm.cst.dhu.menumodule.databinding.ModuleMenuFileListBinding
import zjm.cst.dhu.menumodule.view.adapter.FileListAdapter

/**
 * Created by zjm on 3/2/2017.
 */

class FileListFragment : BaseFragment(), FileListContract.View {

    @Inject
    lateinit var fileListPresenter: FileListContract.Presenter
    internal var module_menu_rcv_file_list: RecyclerView? = null
    internal var module_menu_ptrv_file_list: PullToRefreshView? = null
    internal var abl_ui_menu: AppBarLayout? = null

    private var user: User? = null
    private var sourceFileList: MutableList<File>? = null
    private var fileListAdapter: FileListAdapter? = null
    private val fileListRows: Int = 0
    private val fileListPaper: Int = 0

    override fun setUser(user: User?) {
        this.user = user
    }

    override fun setupView() {
        module_menu_ptrv_file_list!!.setOnRefreshListener {
            module_menu_ptrv_file_list!!.postDelayed({
                fileListPresenter!!.getMenuFileList(user!!.id!!)
                module_menu_ptrv_file_list!!.setRefreshing(false)
            }, 1000)
        }

        sourceFileList = ArrayList<File>()
        module_menu_rcv_file_list!!.layoutManager = StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL)
        fileListAdapter = FileListAdapter(activity, sourceFileList)
        fileListAdapter!!.setOnItemClickListener { position -> fileListOnClick(sourceFileList!![position]) }
        module_menu_rcv_file_list!!.adapter = fileListAdapter

        fileListPresenter!!.getMenuFileList(user!!.id!!)
    }

    private fun setupActivityView() {
        abl_ui_menu = activity.findViewById(R.id.module_menu_abl_ui) as AppBarLayout
        module_menu_rcv_file_list!!.addOnScrollListener(SwipyAppBarScrollListener(abl_ui_menu, module_menu_ptrv_file_list, module_menu_rcv_file_list))
    }

    override fun getFileListNetworkError() {
        Toast.makeText(activity, "Network Error.Please try again later!",
                Toast.LENGTH_SHORT).show()
    }

    override fun updateSourceList(list: List<File>) {
        sourceFileList!!.clear()
        for (i in list.indices) {
            sourceFileList!!.add(list[i])
        }
        fileListAdapter!!.notifyDataSetChanged()
    }

    override fun fileListOnClick(file: File) {
        val menuActivity = activity as MenuActivity
        menuActivity.fileListClick(file)
    }

    override val contentViewLayoutId: Int
        get() = R.layout.module_menu_file_list

    override fun injectDependences() {
        val applicationComponent = (activity.application as MyApplication).applicationComponent
        val fileListFragmentComponent = DaggerFileListFragmentComponent.builder()
                .applicationComponent(applicationComponent)
                .activityModule(ActivityModule(activity))
                .fileListModule(FileListModule())
                .build()
        fileListFragmentComponent.inject(this)

        fileListPresenter!!.attachView(this)
    }

    override fun dataBinding(rootView: View) {
        val binding = DataBindingUtil.bind<ModuleMenuFileListBinding>(rootView)
        module_menu_ptrv_file_list = binding.moduleMenuPtrvFileList
        module_menu_rcv_file_list = binding.moduleMenuRcvFileList
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val view = super.onCreateView(inflater, container, savedInstanceState)
        val menuActivity = activity as MenuActivity
        menuActivity.inToMenu()
        setupView()
        setupActivityView()
        return view
    }

    override fun onDestroy() {
        super.onDestroy()
        module_menu_rcv_file_list!!.clearOnScrollListeners()
        MyApplication.getmRefWatcher(activity)!!.watch(this)
    }
}
