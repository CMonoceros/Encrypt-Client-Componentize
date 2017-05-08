package zjm.cst.dhu.menumodule.view;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Messenger;
import android.os.PersistableBundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.NavigationView;
import android.support.v4.app.NotificationCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import javax.inject.Inject;

import zjm.cst.dhu.basemodule.MyApplication;
import zjm.cst.dhu.basemodule.api.BaseUrl;
import zjm.cst.dhu.basemodule.dagger2.component.ApplicationComponent;
import zjm.cst.dhu.basemodule.dagger2.module.ActivityModule;
import zjm.cst.dhu.basemodule.event.FileEvent;
import zjm.cst.dhu.basemodule.event.FragmentEvent;
import zjm.cst.dhu.basemodule.event.UserEvent;
import zjm.cst.dhu.basemodule.model.File;
import zjm.cst.dhu.basemodule.model.User;
import zjm.cst.dhu.basemodule.view.BaseActivity;
import zjm.cst.dhu.library.utils.others.FileUtil;
import zjm.cst.dhu.library.utils.network.NetworkUtil;
import zjm.cst.dhu.library.utils.network.ProgressListener;
import zjm.cst.dhu.menumodule.MenuContract;
import zjm.cst.dhu.menumodule.R;
import zjm.cst.dhu.menumodule.boardreceiver.NotificationReceiver;
import zjm.cst.dhu.menumodule.dagger2.component.DaggerMenuActivityComponent;
import zjm.cst.dhu.menumodule.dagger2.component.MenuActivityComponent;
import zjm.cst.dhu.menumodule.dagger2.module.MenuModule;
import zjm.cst.dhu.menumodule.databinding.ModuleMenuMainBinding;
import zjm.cst.dhu.menumodule.service.MenuJobService;

import static zjm.cst.dhu.library.utils.others.FileUtil.getPath;

/**
 * Created by zjm on 3/2/2017.
 */

public class MenuActivity extends BaseActivity implements MenuContract.View {
    private User user;
    private FragmentManager fm_menu_main;
    private FragmentTransaction ft_menu_main;
    private MenuJobService menuJobService;
    private JobScheduler jobScheduler;
    private int menuJobServiceID = 1;
    private NotificationCompat.Builder notificationBuilder;
    private NotificationManager notificationManager;

    public static final int EXTRA_CHOOSE_FILE = 1;

    public static final int JOB_SERVICE_CALL_BACK = 1;
    public static final int JOB_SERVICE_CANCEL_CALL_BACK = 2;

    public static final int NOTIFICATION_UPLOAD_OR_DOWNLOAD = 1;

    public static final int UPLOAD_SERVICE_TYPE = 1;
    public static final int DOWNLOAD_SERVICE_TYPE = 2;

    @Inject
    MenuContract.Presenter menuContractPresenter;
    private ModuleMenuMainBinding moduleMenuMainBinding;
    private DrawerLayout module_menu_dl_ui;
    private Toolbar module_menu_tb_title;
    private NavigationView module_menu_nv_person;
    private CollapsingToolbarLayout module_menu_ctl;
    private RelativeLayout module_menu_rl_main;
    private ImageView module_menu_iv_toolbar;
    private ProgressDialog pd_file_progress;
    private ProgressListener progressListener;

    private final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MenuActivity.JOB_SERVICE_CALL_BACK:
                    menuJobService = (MenuJobService) msg.obj;
                    menuJobService.setPresenter(menuContractPresenter);
                    break;
                case MenuActivity.JOB_SERVICE_CANCEL_CALL_BACK:
                    jobSchedulerCancel();
                    break;
            }
        }
    };

    @Override
    protected void injectDependences() {
        ApplicationComponent applicationComponent = ((MyApplication) getApplication()).getApplicationComponent();
        MenuActivityComponent menuActivityComponent = DaggerMenuActivityComponent.builder()
                .applicationComponent(applicationComponent)
                .activityModule(new ActivityModule(this))
                .menuModule(new MenuModule())
                .build();
        menuActivityComponent.inject(this);

        menuContractPresenter.attachView(this);
    }

    @Override
    protected void dataBinding() {
        moduleMenuMainBinding = DataBindingUtil.setContentView(this, R.layout.module_menu_main);
        module_menu_dl_ui = moduleMenuMainBinding.moduleMenuDlUi;
        module_menu_tb_title = moduleMenuMainBinding.moduleMenuTbTitle;
        module_menu_nv_person = moduleMenuMainBinding.moduleMenuNvPerson;
        module_menu_ctl = moduleMenuMainBinding.moduleMenuCtl;
        module_menu_rl_main = moduleMenuMainBinding.moduleMenuRlMain;
        module_menu_iv_toolbar = moduleMenuMainBinding.moduleMenuIvToolbar;

        Point point = new Point();
        WindowManager windowManager = getWindowManager();
        windowManager.getDefaultDisplay().getSize(point);
        int scrWidth = point.x;
        int scrHeight = point.y / 3;
        moduleMenuMainBinding.setContext(this);
        moduleMenuMainBinding.setDrawableId(R.drawable.module_menu_background_toolbar);
        moduleMenuMainBinding.setSrcHeight(scrHeight);
        moduleMenuMainBinding.setSrcWidth(scrWidth);
        moduleMenuMainBinding.setUser(user);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        jobScheduler = (JobScheduler) getSystemService(Context.JOB_SCHEDULER_SERVICE);
        menuJobService = new MenuJobService();
        user = (User) getIntent().getSerializableExtra(BaseUrl.EXTRAMENUUSER);
        if (user == null) {
            user = new User(36, "null");
            user.setName("zjm");
        }
        dataBinding();
        setupView();
        setupFragment();
        setupService();
        setupNotification();
        setupBroadcastReceiver();
    }

    @TargetApi(21)
    @Override
    protected void onDestroy() {
        super.onDestroy();
        jobScheduler.cancelAll();
    }

    private void setupBroadcastReceiver() {
        NotificationReceiver notificationReceiver = new NotificationReceiver();
        notificationReceiver.setJobScheduler(jobScheduler);
    }

    private void setupService() {
        Intent startServiceIntent = new Intent(MenuActivity.this, MenuJobService.class);
        startServiceIntent.putExtra("Messenger", new Messenger(mHandler));
        startService(startServiceIntent);
    }

    private void setupView() {
        setSupportActionBar(module_menu_tb_title);

        module_menu_ctl.setExpandedTitleColor(Color.WHITE);//设置还没收缩时状态下字体颜色
        module_menu_ctl.setCollapsedTitleTextColor(Color.WHITE);//设置收缩后Toolbar上字体的颜色


        RelativeLayout nv_menu_header = (RelativeLayout) module_menu_nv_person.inflateHeaderView(R.layout.module_menu_nv_header);
        TextView tv_nv_menu_id = (TextView) nv_menu_header.findViewById(R.id.module_menu_tv_nv_id);
        TextView tv_nv_menu_name = (TextView) nv_menu_header.findViewById(R.id.module_menu_tv_nv_name);
        tv_nv_menu_id.setText(user.getId() + "");
        tv_nv_menu_name.setText(user.getName());

        module_menu_nv_person.setBackgroundColor(Color.WHITE);
        module_menu_nv_person.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            private MenuItem mPreMenuItem;

            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                if (mPreMenuItem != null) {
                    mPreMenuItem.setCheckable(false);
                }
                item.setChecked(true);
                module_menu_dl_ui.closeDrawers();
                mPreMenuItem = item;
                navigationViewClick(item);
                return true;
            }
        });

        pd_file_progress = new ProgressDialog(this);
        pd_file_progress.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        pd_file_progress.setIndeterminate(false);
        pd_file_progress.setProgress(0);
        pd_file_progress.setButton(DialogInterface.BUTTON_POSITIVE, "切入后台", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                pd_file_progress.dismiss();
            }
        });
        pd_file_progress.setCancelable(false);
        pd_file_progress.setCanceledOnTouchOutside(false);

        progressListener = new ProgressListener() {
            @Override
            public void onProgress(long progress, long total, boolean done) {
                pd_file_progress.setMax((int) total);
                pd_file_progress.setProgress((int) progress);
                notificationBuilder.setProgress((int) total, (int) progress, false);
                notificationShow();
            }
        };
    }

    private void setupFragment() {
        moduleMenuMainBinding.setIsMenu(true);
        fm_menu_main = getFragmentManager();
        ft_menu_main = fm_menu_main.beginTransaction();
        FileListFragment fileListFragment = new FileListFragment();
        fileListFragment.setUser(user);
        ft_menu_main.add(R.id.module_menu_rl_main, fileListFragment);
        ft_menu_main.commit();
    }

    private void setupNotification() {
        notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notificationBuilder = new NotificationCompat.Builder(this);
        notificationBuilder.setPriority(Notification.PRIORITY_MIN);
        notificationBuilder.setOngoing(true);
    }

    private void navigationViewClick(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.nV_Menu_Item_Home) {
            ft_menu_main = fm_menu_main.beginTransaction();
            moduleMenuMainBinding.setIsMenu(true);
            FileListFragment fileListFragment = new FileListFragment();
            fileListFragment.setUser(user);
            ft_menu_main.replace(R.id.module_menu_rl_main, fileListFragment);
            ft_menu_main.addToBackStack(null);
            ft_menu_main.commit();
        }
        if (id == R.id.nV_Menu_Item_Upload_File) {
            item.setChecked(false);
            Intent chooseFile = new Intent(Intent.ACTION_GET_CONTENT);
            chooseFile.setType("*/*");//设置类型，我这里是任意类型，任意后缀的可以这样写。
            chooseFile.addCategory(Intent.CATEGORY_OPENABLE);
            startActivityForResult(chooseFile, EXTRA_CHOOSE_FILE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case EXTRA_CHOOSE_FILE:
                if (resultCode == Activity.RESULT_OK) {
                    String filePath = getPath(this, data.getData());
                    java.io.File file = new java.io.File(filePath);
                    if (!file.exists()) {
                        chooseFileError();
                    }
                    uploadFileStartService(user.getId(), file);
                } else {
                    chooseFileError();
                }
                break;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            module_menu_dl_ui.openDrawer(GravityCompat.START);
            return super.onOptionsItemSelected(item);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        getFragmentManager().popBackStack();
    }


    @Override
    public void fileListClick(File file) {
        moduleMenuMainBinding.setFile(file);
        ft_menu_main = fm_menu_main.beginTransaction();
        FileTypeFragment fileTypeFragment = new FileTypeFragment();
        fileTypeFragment.setFile(file);
        ft_menu_main.replace(R.id.module_menu_rl_main, fileTypeFragment);
        ft_menu_main.addToBackStack(null);
        ft_menu_main.commit();
    }

    @Override
    public void chooseFileError() {
        Toast.makeText(this, "Choose File Error.Please try again later!",
                Toast.LENGTH_SHORT).show();
    }

    @Override
    public void uploadNetworkError() {
        pd_file_progress.dismiss();
        notificationBuilder.setContentTitle("Network Error");
        notificationBuilder.setTicker("Network Error");
        notificationDismiss();
        Toast.makeText(this, "Network Error.Please try again later!",
                Toast.LENGTH_SHORT).show();
    }

    @Override
    public void uploadSuccess() {
        pd_file_progress.dismiss();
        notificationBuilder.setContentTitle("Success upload");
        notificationBuilder.setTicker("Success upload");
        notificationDismiss();
        Toast.makeText(this, "Upload success!Please refresh list!",
                Toast.LENGTH_SHORT).show();
    }

    @Override
    public void uploadFailed() {
        pd_file_progress.dismiss();
        notificationBuilder.setContentTitle("Failed upload");
        notificationBuilder.setTicker("Failed upload");
        notificationDismiss();
        Toast.makeText(this, "Upload failed.Please try again later!",
                Toast.LENGTH_SHORT).show();
    }


    @TargetApi(21)
    @Override
    public void uploadFileStartService(int owner, java.io.File file) {
        menuContractPresenter.setupUploadService(owner, file, progressListener);
        notificationBuilder.setContentTitle("Uploading " + file.getName());
        notificationBuilder.setSmallIcon(R.drawable.module_menu_ic_file_upload_black_48dp);
        ComponentName componentName = new ComponentName(MenuActivity.this, MenuJobService.class);
        JobInfo.Builder builder = new JobInfo.Builder(menuJobServiceID, componentName);
        PersistableBundle persistableBundle = new PersistableBundle();
        persistableBundle.putInt("type", UPLOAD_SERVICE_TYPE);
        builder.setExtras(persistableBundle);
        double size = FileUtil.getFileOrFilesSize(file, FileUtil.SIZETYPE_MB);
        if (size > 1.0) {
            builder.setRequiredNetworkType(JobInfo.NETWORK_TYPE_UNMETERED);
        }
        builder.setMinimumLatency(100);
        if (NetworkUtil.isMobileConnected(this)) {
            uploadFileNeedWifi();
        }
        jobScheduler.schedule(builder.build());
    }

    @TargetApi(21)
    @Override
    public void downloadFileStartService(File file) {
        menuContractPresenter.setupDownloadService(file, progressListener);
        notificationBuilder.setContentTitle("Downloading " + file.getName());
        notificationBuilder.setSmallIcon(R.drawable.module_menu_ic_file_download_black_48dp);
        ComponentName componentName = new ComponentName(MenuActivity.this, MenuJobService.class);
        JobInfo.Builder builder = new JobInfo.Builder(menuJobServiceID, componentName);
        PersistableBundle persistableBundle = new PersistableBundle();
        persistableBundle.putInt("type", DOWNLOAD_SERVICE_TYPE);
        builder.setExtras(persistableBundle);
        String size = file.getSize();
        if (!size.endsWith("KB")) {
            builder.setRequiredNetworkType(JobInfo.NETWORK_TYPE_UNMETERED);
        }
        builder.setMinimumLatency(100);
        if (NetworkUtil.isMobileConnected(this)) {
            downloadFileNeedWifi();
        }
        jobScheduler.schedule(builder.build());
    }

    @Override
    public void uploadFileNeedWifi() {
        Toast.makeText(this, "File is too big!Need Wifi to upload!",
                Toast.LENGTH_SHORT).show();
        notificationBuilder.setContentIntent(setupPendingIntent());
        notificationBuilder.setContentTitle("Waiting to connect WIFI!Click to cancel!");
        notificationBuilder.setSmallIcon(R.drawable.module_menu_ic_file_upload_black_48dp);
        notificationShow();
    }

    @Override
    public void downloadFileNeedWifi() {
        Toast.makeText(this, "File is too big!Need Wifi to download!",
                Toast.LENGTH_SHORT).show();
        notificationBuilder.setContentIntent(setupPendingIntent());
        notificationBuilder.setSmallIcon(R.drawable.module_menu_ic_file_download_black_48dp);
        notificationBuilder.setContentTitle("Waiting to connect WIFI!Click to cancel!");
        notificationShow();
    }

    private PendingIntent setupPendingIntent() {
        Intent clickIntent = new Intent(MenuActivity.this, NotificationReceiver.class);
        clickIntent.putExtra("jobServiceID", menuJobServiceID);
        clickIntent.putExtra("Messenger", new Messenger(mHandler));
        clickIntent.setAction("dhu.cst.zjm.start_job");
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 1, clickIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        return pendingIntent;
    }

    @Override
    public void downloadFileNetworkError() {
        pd_file_progress.dismiss();
        notificationBuilder.setContentTitle("Network Error");
        notificationBuilder.setTicker("Network Error");
        notificationDismiss();
        Toast.makeText(this, "Network Error.Please try again later!",
                Toast.LENGTH_SHORT).show();
    }

    @Override
    public void downloadFileSuccess(String dir) {
        pd_file_progress.dismiss();
        notificationBuilder.setContentTitle("Success download");
        notificationBuilder.setTicker("Success download");
        notificationDismiss();
        Toast.makeText(this, "Download success on "+dir,
                Toast.LENGTH_SHORT).show();
    }

    @Override
    public void progressListenerShow() {
        pd_file_progress.show();
    }

    @Override
    public void notificationDismiss() {
        notificationBuilder.setProgress(0, 0, false);
        notificationBuilder.setOngoing(false);
        notificationShow();
    }

    @Override
    public void notificationShow() {
        Notification notification = notificationBuilder.build();
        notification.flags = Notification.FLAG_ONGOING_EVENT;
        notificationManager.notify(NOTIFICATION_UPLOAD_OR_DOWNLOAD, notification);
    }

    @Override
    public void jobSchedulerCancel() {
        notificationBuilder.setContentTitle("Cancel");
        notificationDismiss();
        Toast.makeText(this, "Cancel Job!",
                Toast.LENGTH_SHORT).show();
    }

    @Override
    public void inToMenu() {
        moduleMenuMainBinding.setIsMenu(true);
    }

    @Override
    public void outToMenu() {
        moduleMenuMainBinding.setIsMenu(false);
    }
}
