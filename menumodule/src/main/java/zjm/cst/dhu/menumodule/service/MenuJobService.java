package zjm.cst.dhu.menumodule.service;

import android.annotation.TargetApi;
import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.Intent;
import android.os.Message;
import android.os.Messenger;
import android.os.PersistableBundle;
import android.os.RemoteException;

import zjm.cst.dhu.menumodule.MenuContract;
import zjm.cst.dhu.menumodule.view.MenuActivity;


/**
 * Created by zjm on 2017/4/19.
 */

@TargetApi(21)
public class MenuJobService extends JobService {
    MenuContract.Presenter menuContractPresenter;

    public void setPresenter(MenuContract.Presenter presenter) {
        this.menuContractPresenter = presenter;
    }

    public MenuJobService() {

    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Messenger callback = intent.getParcelableExtra("Messenger");
        Message m = Message.obtain();
        m.what = MenuActivity.Companion.getJOB_SERVICE_CALL_BACK();
        m.obj = this;
        try {
            callback.send(m);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return START_NOT_STICKY;
    }


    @Override
    public boolean onStartJob(JobParameters params) {
        menuContractPresenter.progressListenerShow();
        PersistableBundle persistableBundle = params.getExtras();
        int type = persistableBundle.getInt("type");
        if(type==MenuActivity.Companion.getUPLOAD_SERVICE_TYPE()){
            menuContractPresenter.uploadServiceWork();
        }else if(type==MenuActivity.Companion.getDOWNLOAD_SERVICE_TYPE()){
            menuContractPresenter.downloadServiceWork();
        }
        jobFinished(params, false);
        return false;
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        return false;
    }
}
