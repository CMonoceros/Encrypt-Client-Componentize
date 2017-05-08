package zjm.cst.dhu.menumodule.boardreceiver;

import android.annotation.TargetApi;
import android.app.job.JobScheduler;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;

import zjm.cst.dhu.menumodule.view.MenuActivity;


/**
 * Created by zjm on 2017/4/20.
 */

@TargetApi(21)
public class NotificationReceiver extends BroadcastReceiver {

    private static JobScheduler jobScheduler;
    private static Handler handler;

    public NotificationReceiver() {
        super();
    }

    public void setJobScheduler(JobScheduler Scheduler) {
        jobScheduler = Scheduler;
    }

    public void setHandler(Handler h) {
        handler = h;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        int id = intent.getIntExtra("jobServiceID", 0);
        Messenger callback = intent.getParcelableExtra("Messenger");
        Message m = Message.obtain();
        m.what = MenuActivity.JOB_SERVICE_CANCEL_CALL_BACK;
        m.obj = this;
        jobScheduler.cancel(id);
        try {
            callback.send(m);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }
}
