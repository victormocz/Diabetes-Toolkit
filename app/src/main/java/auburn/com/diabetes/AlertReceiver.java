package auburn.com.diabetes;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

/**
 * Created by victormo on 8/2/2016.
 */
public class AlertReceiver extends BroadcastReceiver{
    private NotificationManager notificationManager;
    private final int nid = 100;

    @Override

    public void onReceive(Context context, Intent intent) {
        notification(context);
    }

    public void notification(Context context) {
        PendingIntent notifintent = PendingIntent.getActivity(context,0,new Intent(context,MainActivity.class),0);

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context)
                .setContentTitle("Medication Reminder").setContentText("It's time to take Medication")
                .setTicker("Diabetes Medication")
                .setSmallIcon(R.drawable.ic_medication);
        notificationBuilder.setContentIntent(notifintent);

        notificationBuilder.setDefaults(NotificationCompat.DEFAULT_SOUND);
        notificationBuilder.setAutoCancel(true);

        notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(1,notificationBuilder.build());
    }
}
