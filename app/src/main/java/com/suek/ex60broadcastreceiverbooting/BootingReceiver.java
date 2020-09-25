package com.suek.ex60broadcastreceiverbooting;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.AudioAttributes;
import android.net.Uri;
import android.os.Build;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;

public class BootingReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Toast.makeText(context, "Booting Received!!", Toast.LENGTH_SHORT).show();

        String action= intent.getAction();
        if(action.equals(Intent.ACTION_BOOT_COMPLETED)){

            //android 10버전이나 그 이상들에서는 직접 액티비티를 실행할 수 없음
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q){
                //부팅을 듣고 알림을 만들어서 notification 알림을 클릭하여 액티비티를 실행

                NotificationManager notificationManager= (NotificationManager) context.getSystemService(context.NOTIFICATION_SERVICE);

                NotificationCompat.Builder builder= null;
                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
                    NotificationChannel channel= new NotificationChannel("ch01", "Channel #1", NotificationManager.IMPORTANCE_HIGH);
                    notificationManager.createNotificationChannel(channel);

                    //사운드나 진동을 채널객체에서 설정
                    Uri soundUri= Uri.parse("android.resource://"+ context.getPackageName()+"/"+R.raw.s_sijak);
                    channel.setSound(soundUri, new AudioAttributes.Builder().build());

                    builder= new NotificationCompat.Builder(context, "ch01");

                }else {
                    builder= new NotificationCompat.Builder(context, null);
                    //사운드 작업
                    Uri soundUri= Uri.parse("android.resource://"+ context.getPackageName()+"/"+R.raw.s_sijak);
                    builder.setSound(soundUri);
                }

                //상태바에 보이는 아이콘
                builder.setSmallIcon(R.drawable.ic_stat_name);

                //확장상태바의 설정들
                builder.setContentTitle("부팅 완료!");
                builder.setContentText("Ex60의 MainActivity를 실행할 수 있습니다.");

                //확장상태바의 알림을 클릭하여 MainActivity 를 실행하도록....
                Intent i= new Intent(context, MainActivity.class);
                //바로 실행되지 않기때문에 인텐트를 보류(pending)중인 인텐트로 변경
                PendingIntent pendingIntent= PendingIntent.getActivity(context, 10, i, PendingIntent.FLAG_UPDATE_CURRENT);
                //알림에 인텐트 적용
                builder.setContentIntent(pendingIntent);

                //클릭하면 자동으로 알림없어지도록
                builder.setAutoCancel(true);

                Notification notification= builder.build();
                notificationManager.notify(0, notification);

            }else{   //10버전 이하..
                //새로운 액티비티 실행
                Intent i= new Intent(context, MainActivity.class);

                //액티비티를 실행하는 TASK 가 없었기 때문에 그냥 startActivity()하면
                //새로운 엑티비티가 실행되지 않음
                //인텐트에 설정추가
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(i);
            }


        }
    }
}
