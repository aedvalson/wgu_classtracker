package com.aedvalson.classtracker;

import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.widget.Toast;

/**
 * Created by a_edv on 10/2/2016.
 */

public class AlarmHandler extends BroadcastReceiver {

    public static final String courseAlarmFile = "courseAlarms" ;
    public static final String assessmentAlarmFile = "assessmentAlarms";
    public static final String alarmFile = "alarmFile";

    public static final String nextAlarmField = "nextAlarmId";

    @Override
    public void onReceive(Context context, Intent intent) {

        String destination = intent.getStringExtra("destination");
        if (destination == null || destination.isEmpty()) {
            destination = "";
        }

        Long id = intent.getLongExtra("id", 0);
        String alarmText = intent.getStringExtra("text");
        String alarmTitle = intent.getStringExtra("title");
        int nextAlarmId = intent.getIntExtra("nextAlarmId", getAndIncrementNextAlarmId(context));

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(context)
                        .setSmallIcon(R.drawable.ic_calendar_clock)
                        .setContentTitle(alarmTitle)
                        .setContentText(alarmText);

        Intent resultIntent;
        Uri uri;

        SharedPreferences specificPrefs;

        switch(destination) {
            case "course":
                resultIntent = new Intent(context, CourseViewerActivity.class);
                uri = Uri.parse(DataProvider.COURSE_URI + "/" + id);
                resultIntent.putExtra(DataProvider.COURSE_CONTENT_TYPE, uri);
                break;
            case "assessment":
                resultIntent = new Intent(context, AssessmentViewerActivity.class);
                uri = Uri.parse(DataProvider.ASSESSMENT_URI + "/" + id);
                resultIntent.putExtra(DataProvider.ASSESSMENT_CONTENT_TYPE, uri);
                break;
            default:
                resultIntent = new Intent(context, MainActivity.class);
                break;
        }

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        stackBuilder.addParentStack(MainActivity.class);
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent =
                stackBuilder.getPendingIntent(
                        0,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );
        mBuilder.setContentIntent(resultPendingIntent)
                .setAutoCancel(true);

        NotificationManager mNotificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        mNotificationManager.notify(nextAlarmId, mBuilder.build());
    }



    public static boolean scheduleCourseAlarm(Context context, int id, long time, String Title, String Text) {
        // create the object
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        // Check shared preferences for next alarm ID
        int nextAlarmId = getNextAlarmId(context);

        // create an Intent for our alarmHandler class. onReceive will handle the actual alarm
        Intent intentAlarm = new Intent(context, AlarmHandler.class);
        intentAlarm.putExtra("text", Text);
        intentAlarm.putExtra("title", Title);
        intentAlarm.putExtra("destination", "course");
        intentAlarm.putExtra("nextAlarmId", nextAlarmId);

        //set the alarm for particular time
        alarmManager.set(AlarmManager.RTC_WAKEUP, time, PendingIntent.getBroadcast(context, 1,  intentAlarm, PendingIntent.FLAG_UPDATE_CURRENT));


        // Store mapping to alarm id in course Prefs
        SharedPreferences sp = context.getSharedPreferences(courseAlarmFile, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putInt(Long.toString(id), nextAlarmId);
        editor.commit();

        incrementNextAlarmId(context);
        return true;
    }

    public static boolean scheduleAssessmentAlarm(Context context, int id, long time, String Title, String Text) {
        // create the object
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        // Check shared preferences for next alarm ID
        int nextAlarmId = getNextAlarmId(context);

        // create an Intent for our alarmHandler class. onReceive will handle the actual alarm
        Intent intentAlarm = new Intent(context, AlarmHandler.class);
        intentAlarm.putExtra("text", Text);
        intentAlarm.putExtra("title", Title);
        intentAlarm.putExtra("destination", "assessment");
        intentAlarm.putExtra("nextAlarmId", nextAlarmId);

        //set the alarm for particular time

        alarmManager.set(AlarmManager.RTC_WAKEUP, time, PendingIntent.getBroadcast(context, 1, intentAlarm, PendingIntent.FLAG_UPDATE_CURRENT));


        // Store mapping to alarm id in course Prefs
        SharedPreferences sp = context.getSharedPreferences(courseAlarmFile, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putInt(Long.toString(id), nextAlarmId);
        editor.commit();

        incrementNextAlarmId(context);
        return true;
    }

    private static int getNextAlarmId(Context context) {
        SharedPreferences alarmPrefs;
        alarmPrefs = context.getSharedPreferences(alarmFile, Context.MODE_PRIVATE);
        int nextAlarmId = alarmPrefs.getInt(nextAlarmField, 1);
        return nextAlarmId;
    }

    private static void incrementNextAlarmId(Context context) {
        SharedPreferences alarmPrefs;
        alarmPrefs = context.getSharedPreferences(alarmFile, Context.MODE_PRIVATE);
        int nextAlarmId = alarmPrefs.getInt(nextAlarmField, 1);
        SharedPreferences.Editor alarmEditor = alarmPrefs.edit();
        alarmEditor.putInt(nextAlarmField, nextAlarmId + 1);
        alarmEditor.commit();
    }

    private static int getAndIncrementNextAlarmId(Context context) {
        int nextAlarmId = getNextAlarmId(context);
        incrementNextAlarmId(context);
        return nextAlarmId;
    }
}
