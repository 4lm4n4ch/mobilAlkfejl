package hu.Mobilalkfejl;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.util.Log;

import androidx.core.app.NotificationCompat;

public class NotificationHandler {
    private static final String CHANNEL_ID= "allas_notification_channel";
    private static final int NOTIFICATION_ID= 0;
    private NotificationManager mManager;
        private Context mcontext;

    public NotificationHandler(Context mcontext) {
        this.mcontext = mcontext;
        this.mManager= (NotificationManager) mcontext.getSystemService(mcontext.NOTIFICATION_SERVICE);

        createChannel();

    }
    private void createChannel(){
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            Log.d("NotificationHandler", "A készülék verziója nem támogatja a NotificationChannel-t.");
            return;
        }

        NotificationChannel channel = new NotificationChannel(CHANNEL_ID,"AllasPortal Notification",NotificationManager.IMPORTANCE_DEFAULT);
    channel.enableLights(true);
    channel.enableVibration(true);
    channel.setLightColor(Color.BLUE);
    channel.setDescription("Ertesites az Állásportáltól");
    this.mManager.createNotificationChannel(channel);
    }


    public void send(String message) {

        Intent intent = new Intent(mcontext, KezdolapActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

        // Létrehozunk egy PendingIntent-et, amely az Intent-et tartalmazza.
        PendingIntent pendingIntent = PendingIntent.getActivity(mcontext, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

        System.out.println("belep asendbe");
        NotificationCompat.Builder builder = new NotificationCompat.Builder(mcontext, CHANNEL_ID)
                .setSmallIcon(R.drawable.app_image_noti)
                .setContentTitle("Álláskereső Portál")
                .setContentText(message)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent)
                .setChannelId(CHANNEL_ID)
                .setPriority(NotificationCompat.PRIORITY_HIGH);
// Hozzáadjuk a PendingIntent-et a Builder-hez

        this.mManager.notify(NOTIFICATION_ID, builder.build());
        Log.d("NotificationHandler", "Értesítés elküldve.");
    }



}
