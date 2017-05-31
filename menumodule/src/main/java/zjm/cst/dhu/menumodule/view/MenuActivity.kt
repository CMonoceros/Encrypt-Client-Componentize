package zjm.cst.dhu.menumodule.view

import android.annotation.TargetApi
import android.app.Activity
import android.app.FragmentManager
import android.app.FragmentTransaction
import android.app.Notification
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.ProgressDialog
import android.app.job.JobInfo
import android.app.job.JobScheduler
import android.content.ComponentName
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.databinding.DataBindingUtil
import android.graphics.Color
import android.graphics.Point
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.os.Messenger
import android.os.PersistableBundle
import android.support.design.widget.CollapsingToolbarLayout
import android.support.design.widget.NavigationView
import android.support.v4.app.NotificationCompat
import android.support.v4.view.GravityCompat
import android.support.v4.widget.DrawerLayout
import android.support.v7.widget.Toolbar
import android.view.MenuItem
import android.view.WindowManager
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast

import javax.inject.Inject

import zjm.cst.dhu.basemodule.MyApplication
import zjm.cst.dhu.basemodule.api.BaseUrl
import zjm.cst.dhu.basemodule.dagger2.component.ApplicationComponent
import zjm.cst.dhu.basemodule.dagger2.module.ActivityModule
import zjm.cst.dhu.basemodule.model.File
import zjm.cst.dhu.basemodule.model.User
import zjm.cst.dhu.basemodule.view.BaseActivity
import zjm.cst.dhu.library.utils.others.FileUtil
import zjm.cst.dhu.library.utils.network.NetworkUtil
import zjm.cst.dhu.library.utils.network.ProgressListener
import zjm.cst.dhu.menumodule.MenuContract
import zjm.cst.dhu.menumodule.R
import zjm.cst.dhu.menumodule.boardreceiver.NotificationReceiver
import zjm.cst.dhu.menumodule.dagger2.component.DaggerMenuActivityComponent
import zjm.cst.dhu.menumodule.dagger2.component.MenuActivityComponent
import zjm.cst.dhu.menumodule.dagger2.module.MenuModule
import zjm.cst.dhu.menumodule.service.MenuJobService

import zjm.cst.dhu.library.utils.others.FileUtil.getPath
import zjm.cst.dhu.menumodule.databinding.ModuleMenuMainBinding

/**
 * Created by zjm on 3/2/2017.
 */
@TargetApi(21)
class MenuActivity : BaseActivity(), MenuContract.View, ProgressListener {
    private var user: User? = null
    private var fm_menu_main: FragmentManager? = null
    private var ft_menu_main: FragmentTransaction? = null
    private var menuJobService: MenuJobService? = null
    private var jobScheduler: JobScheduler? = null
    private val menuJobServiceID = 1
    private var notificationBuilder: NotificationCompat.Builder? = null
    private var notificationManager: NotificationManager? = null

    @Inject
    lateinit var menuContractPresenter: MenuContract.Presenter
    private var moduleMenuMainBinding: ModuleMenuMainBinding? = null
    private var module_menu_dl_ui: DrawerLayout? = null
    private var module_menu_tb_title: Toolbar? = null
    private var module_menu_nv_person: NavigationView? = null
    private var module_menu_ctl: CollapsingToolbarLayout? = null
    private var module_menu_rl_main: RelativeLayout? = null
    private var module_menu_iv_toolbar: ImageView? = null
    private var pd_file_progress: ProgressDialog? = null
    private var progressListener: ProgressListener? = null

    private val mHandler = object : Handler() {
        override fun handleMessage(msg: Message) {
            when (msg.what) {
                MenuActivity.JOB_SERVICE_CALL_BACK -> {
                    menuJobService = msg.obj as MenuJobService
                    menuJobService!!.setPresenter(menuContractPresenter)
                }
                MenuActivity.JOB_SERVICE_CANCEL_CALL_BACK -> jobSchedulerCancel()
            }
        }
    }

    override fun injectDependences() {
        val applicationComponent = (application as MyApplication).applicationComponent
        val menuActivityComponent = DaggerMenuActivityComponent.builder()
                .applicationComponent(applicationComponent)
                .activityModule(ActivityModule(this))
                .menuModule(MenuModule())
                .build()
        menuActivityComponent.inject(this)

        menuContractPresenter!!.attachView(this)
    }

    override fun dataBinding() {
        moduleMenuMainBinding = DataBindingUtil.setContentView<ModuleMenuMainBinding>(this, R.layout.module_menu_main)
        module_menu_dl_ui = moduleMenuMainBinding!!.moduleMenuDlUi
        module_menu_tb_title = moduleMenuMainBinding!!.moduleMenuTbTitle
        module_menu_nv_person = moduleMenuMainBinding!!.moduleMenuNvPerson
        module_menu_ctl = moduleMenuMainBinding!!.moduleMenuCtl
        module_menu_rl_main = moduleMenuMainBinding!!.moduleMenuRlMain
        module_menu_iv_toolbar = moduleMenuMainBinding!!.moduleMenuIvToolbar

        val point = Point()
        val windowManager = windowManager
        windowManager.defaultDisplay.getSize(point)
        val scrWidth = point.x
        val scrHeight = point.y / 3
        moduleMenuMainBinding!!.context = this
        moduleMenuMainBinding!!.drawableId = R.drawable.module_menu_background_toolbar
        moduleMenuMainBinding!!.srcHeight = scrHeight
        moduleMenuMainBinding!!.srcWidth = scrWidth
        moduleMenuMainBinding!!.user = user
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        jobScheduler = getSystemService(Context.JOB_SCHEDULER_SERVICE) as JobScheduler
        menuJobService = MenuJobService()
        user = intent.getSerializableExtra(BaseUrl.EXTRAMENUUSER) as? User
        if (user == null) {
            user = User(id = 36)
            user!!.name = "zjm"
        }
        dataBinding()
        setupView()
        setupFragment()
        setupService()
        setupNotification()
        setupBroadcastReceiver()
    }

    @TargetApi(21)
    override fun onDestroy() {
        super.onDestroy()
        jobScheduler!!.cancelAll()
    }

    private fun setupBroadcastReceiver() {
        val notificationReceiver = NotificationReceiver()
        notificationReceiver.setJobScheduler(jobScheduler)
    }

    private fun setupService() {
        val startServiceIntent = Intent(this@MenuActivity, MenuJobService::class.java)
        startServiceIntent.putExtra("Messenger", Messenger(mHandler))
        startService(startServiceIntent)
    }

    private fun setupView() {
        setSupportActionBar(module_menu_tb_title)

        module_menu_ctl!!.setExpandedTitleColor(Color.WHITE)//设置还没收缩时状态下字体颜色
        module_menu_ctl!!.setCollapsedTitleTextColor(Color.WHITE)//设置收缩后Toolbar上字体的颜色


        val nv_menu_header = module_menu_nv_person!!.inflateHeaderView(R.layout.module_menu_nv_header) as RelativeLayout
        val tv_nv_menu_id = nv_menu_header.findViewById(R.id.module_menu_tv_nv_id) as TextView
        val tv_nv_menu_name = nv_menu_header.findViewById(R.id.module_menu_tv_nv_name) as TextView
        tv_nv_menu_id.text = user!!.id!!.toString() + ""
        tv_nv_menu_name.text = user!!.name

        module_menu_nv_person!!.setBackgroundColor(Color.WHITE)
        module_menu_nv_person!!.setNavigationItemSelectedListener(object : NavigationView.OnNavigationItemSelectedListener {
            private var mPreMenuItem: MenuItem? = null

            override fun onNavigationItemSelected(item: MenuItem): Boolean {
                if (mPreMenuItem != null) {
                    mPreMenuItem!!.isCheckable = false
                }
                item.isChecked = true
                module_menu_dl_ui!!.closeDrawers()
                mPreMenuItem = item
                navigationViewClick(item)
                return true
            }
        })

        pd_file_progress = ProgressDialog(this)
        pd_file_progress!!.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL)
        pd_file_progress!!.isIndeterminate = false
        pd_file_progress!!.progress = 0
        pd_file_progress!!.setButton(DialogInterface.BUTTON_POSITIVE, "切入后台") { dialog, which -> pd_file_progress!!.dismiss() }
        pd_file_progress!!.setCancelable(false)
        pd_file_progress!!.setCanceledOnTouchOutside(false)

        progressListener = this
    }

    override fun onProgress(progress: Long, total: Long, done: Boolean) {
        pd_file_progress!!.max = total.toInt()
        pd_file_progress!!.progress = progress.toInt()
        notificationBuilder!!.setProgress(total.toInt(), progress.toInt(), false)
        notificationShow()
    }

    private fun setupFragment() {
        moduleMenuMainBinding!!.isMenu = true
        fm_menu_main = fragmentManager
        ft_menu_main = fm_menu_main!!.beginTransaction()
        val fileListFragment = FileListFragment()
        fileListFragment.setUser(user)
        ft_menu_main!!.add(R.id.module_menu_rl_main, fileListFragment)
        ft_menu_main!!.commit()
    }

    private fun setupNotification() {
        notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationBuilder = NotificationCompat.Builder(this)
        notificationBuilder!!.priority = Notification.PRIORITY_MIN
        notificationBuilder!!.setOngoing(true)
    }

    private fun navigationViewClick(item: MenuItem) {
        val id = item.itemId
        if (id == R.id.nV_Menu_Item_Home) {
            ft_menu_main = fm_menu_main!!.beginTransaction()
            moduleMenuMainBinding!!.isMenu = true
            val fileListFragment = FileListFragment()
            fileListFragment.setUser(user)
            ft_menu_main!!.replace(R.id.module_menu_rl_main, fileListFragment)
            ft_menu_main!!.addToBackStack(null)
            ft_menu_main!!.commit()
        }
        if (id == R.id.nV_Menu_Item_Upload_File) {
            item.isChecked = false
            val chooseFile = Intent(Intent.ACTION_GET_CONTENT)
            chooseFile.type = "*/*"//设置类型，我这里是任意类型，任意后缀的可以这样写。
            chooseFile.addCategory(Intent.CATEGORY_OPENABLE)
            startActivityForResult(chooseFile, EXTRA_CHOOSE_FILE)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            EXTRA_CHOOSE_FILE -> if (resultCode == Activity.RESULT_OK) {
                val filePath = getPath(this, data.data)
                val file = java.io.File(filePath!!)
                if (!file.exists()) {
                    chooseFileError()
                }
                uploadFileStartService(user!!.id!!, file)
            } else {
                chooseFileError()
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            module_menu_dl_ui!!.openDrawer(GravityCompat.START)
            return super.onOptionsItemSelected(item)
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        fragmentManager.popBackStack()
    }


    override fun fileListClick(file: File) {
        moduleMenuMainBinding!!.file = file
        ft_menu_main = fm_menu_main!!.beginTransaction()
        val fileTypeFragment = FileTypeFragment()
        fileTypeFragment.setFile(file)
        ft_menu_main!!.replace(R.id.module_menu_rl_main, fileTypeFragment)
        ft_menu_main!!.addToBackStack(null)
        ft_menu_main!!.commit()
    }

    override fun chooseFileError() {
        Toast.makeText(this, "Choose File Error.Please try again later!",
                Toast.LENGTH_SHORT).show()
    }

    override fun uploadNetworkError() {
        pd_file_progress!!.dismiss()
        notificationBuilder!!.setContentTitle("Network Error")
        notificationBuilder!!.setTicker("Network Error")
        notificationDismiss()
        Toast.makeText(this, "Network Error.Please try again later!",
                Toast.LENGTH_SHORT).show()
    }

    override fun uploadSuccess() {
        pd_file_progress!!.dismiss()
        notificationBuilder!!.setContentTitle("Success upload")
        notificationBuilder!!.setTicker("Success upload")
        notificationDismiss()
        Toast.makeText(this, "Upload success!Please refresh list!",
                Toast.LENGTH_SHORT).show()
    }

    override fun uploadFailed() {
        pd_file_progress!!.dismiss()
        notificationBuilder!!.setContentTitle("Failed upload")
        notificationBuilder!!.setTicker("Failed upload")
        notificationDismiss()
        Toast.makeText(this, "Upload failed.Please try again later!",
                Toast.LENGTH_SHORT).show()
    }


    @TargetApi(21)
    override fun uploadFileStartService(owner: Int, file: java.io.File) {
        menuContractPresenter!!.setupUploadService(owner, file, progressListener)
        notificationBuilder!!.setContentTitle("Uploading " + file.name)
        notificationBuilder!!.setSmallIcon(R.drawable.module_menu_ic_file_upload_black_48dp)
        val componentName = ComponentName(this@MenuActivity, MenuJobService::class.java)
        val builder = JobInfo.Builder(menuJobServiceID, componentName)
        val persistableBundle = PersistableBundle()
        persistableBundle.putInt("type", UPLOAD_SERVICE_TYPE)
        builder.setExtras(persistableBundle)
        val size = FileUtil.getFileOrFilesSize(file, FileUtil.SIZETYPE_MB)
        if (size > 1.0) {
            builder.setRequiredNetworkType(JobInfo.NETWORK_TYPE_UNMETERED)
        }
        builder.setMinimumLatency(100)
        if (NetworkUtil.isMobileConnected(this)) {
            uploadFileNeedWifi()
        }
        jobScheduler!!.schedule(builder.build())
    }

    @TargetApi(21)
    override fun downloadFileStartService(file: File) {
        menuContractPresenter!!.setupDownloadService(file, progressListener)
        notificationBuilder!!.setContentTitle("Downloading " + file.name)
        notificationBuilder!!.setSmallIcon(R.drawable.module_menu_ic_file_download_black_48dp)
        val componentName = ComponentName(this@MenuActivity, MenuJobService::class.java)
        val builder = JobInfo.Builder(menuJobServiceID, componentName)
        val persistableBundle = PersistableBundle()
        persistableBundle.putInt("type", DOWNLOAD_SERVICE_TYPE)
        builder.setExtras(persistableBundle)
        val size = file.size
        if (!size.endsWith("KB")) {
            builder.setRequiredNetworkType(JobInfo.NETWORK_TYPE_UNMETERED)
        }
        builder.setMinimumLatency(100)
        if (NetworkUtil.isMobileConnected(this)) {
            downloadFileNeedWifi()
        }
        jobScheduler!!.schedule(builder.build())
    }

    override fun uploadFileNeedWifi() {
        Toast.makeText(this, "File is too big!Need Wifi to upload!",
                Toast.LENGTH_SHORT).show()
        notificationBuilder!!.setContentIntent(setupPendingIntent())
        notificationBuilder!!.setContentTitle("Waiting to connect WIFI!Click to cancel!")
        notificationBuilder!!.setSmallIcon(R.drawable.module_menu_ic_file_upload_black_48dp)
        notificationShow()
    }

    override fun downloadFileNeedWifi() {
        Toast.makeText(this, "File is too big!Need Wifi to download!",
                Toast.LENGTH_SHORT).show()
        notificationBuilder!!.setContentIntent(setupPendingIntent())
        notificationBuilder!!.setSmallIcon(R.drawable.module_menu_ic_file_download_black_48dp)
        notificationBuilder!!.setContentTitle("Waiting to connect WIFI!Click to cancel!")
        notificationShow()
    }

    private fun setupPendingIntent(): PendingIntent {
        val clickIntent = Intent(this@MenuActivity, NotificationReceiver::class.java)
        clickIntent.putExtra("jobServiceID", menuJobServiceID)
        clickIntent.putExtra("Messenger", Messenger(mHandler))
        clickIntent.action = "dhu.cst.zjm.start_job"
        val pendingIntent = PendingIntent.getBroadcast(this, 1, clickIntent, PendingIntent.FLAG_UPDATE_CURRENT)
        return pendingIntent
    }

    override fun downloadFileNetworkError() {
        pd_file_progress!!.dismiss()
        notificationBuilder!!.setContentTitle("Network Error")
        notificationBuilder!!.setTicker("Network Error")
        notificationDismiss()
        Toast.makeText(this, "Network Error.Please try again later!",
                Toast.LENGTH_SHORT).show()
    }

    override fun downloadFileSuccess(dir: String) {
        pd_file_progress!!.dismiss()
        notificationBuilder!!.setContentTitle("Success download")
        notificationBuilder!!.setTicker("Success download")
        notificationDismiss()
        Toast.makeText(this, "Download success on " + dir,
                Toast.LENGTH_SHORT).show()
    }

    override fun progressListenerShow() {
        pd_file_progress!!.show()
    }

    override fun notificationDismiss() {
        notificationBuilder!!.setProgress(0, 0, false)
        notificationBuilder!!.setOngoing(false)
        notificationShow()
    }

    override fun notificationShow() {
        val notification = notificationBuilder!!.build()
        notification.flags = Notification.FLAG_ONGOING_EVENT
        notificationManager!!.notify(NOTIFICATION_UPLOAD_OR_DOWNLOAD, notification)
    }

    override fun jobSchedulerCancel() {
        notificationBuilder!!.setContentTitle("Cancel")
        notificationDismiss()
        Toast.makeText(this, "Cancel Job!",
                Toast.LENGTH_SHORT).show()
    }

    override fun inToMenu() {
        moduleMenuMainBinding!!.isMenu = true
    }

    override fun outToMenu() {
        moduleMenuMainBinding!!.isMenu = false
    }

    companion object {

        val EXTRA_CHOOSE_FILE = 1

        val JOB_SERVICE_CALL_BACK = 1
        val JOB_SERVICE_CANCEL_CALL_BACK = 2

        val NOTIFICATION_UPLOAD_OR_DOWNLOAD = 1

        val UPLOAD_SERVICE_TYPE = 1
        val DOWNLOAD_SERVICE_TYPE = 2
    }
}
