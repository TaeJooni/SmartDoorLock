package com.example.smartdoorlock2;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Switch;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import app.akexorcist.bluetotohspp.library.BluetoothSPP;
import app.akexorcist.bluetotohspp.library.BluetoothState;
import app.akexorcist.bluetotohspp.library.DeviceList;

public class PrjAlarmActivity extends AppCompatActivity {

    Switch switchSound, switchVibe, switchSound2, switchVibe2, switchSound3, switchVibe3;
    Bitmap mLargeIconForNoti;
    Button button, button2, button3, buttonSave;
    private boolean check1, check2, check3, check4, check5, check6;
    private SharedPreferences appData;
    private BluetoothSPP bt;

    @RequiresApi(api=Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prjalarm);

        switchSound = findViewById(R.id.switchSound);
        switchVibe = findViewById(R.id.switchVibe);
        switchSound2 = findViewById(R.id.switchSound2);
        switchVibe2 = findViewById(R.id.switchVibe2);
        switchSound3 = findViewById(R.id.switchSound3);
        switchVibe3 = findViewById(R.id.switchVibe3);
        buttonSave = findViewById(R.id.buttonSave);

        button = findViewById(R.id.Alarm_btn);
        button2 = findViewById(R.id.button2);
        button3 = findViewById(R.id.bluebtn);

        bt = new BluetoothSPP(this); //Initializing

        if (!bt.isBluetoothAvailable()) { //블루투스 사용 불가
            Toast.makeText(getApplicationContext()
                    , "Bluetooth is not available"
                    , Toast.LENGTH_SHORT).show();
            finish();
        }

        bt.setOnDataReceivedListener(new BluetoothSPP.OnDataReceivedListener() { //데이터 수신
            public void onDataReceived(byte[] data, String message) {
                Toast.makeText(PrjAlarmActivity.this, message, Toast.LENGTH_SHORT).show();
            }
        });


        appData = getSharedPreferences("appData", MODE_PRIVATE);
        load();

        if (check1) {
            switchSound.setChecked(check1);
            switchVibe.setChecked(check2);
            switchSound2.setChecked(check3);
            switchVibe2.setChecked(check4);
            switchSound3.setChecked(check5);
            switchVibe3.setChecked(check6);
        }

        
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mLargeIconForNoti =
                        BitmapFactory.decodeResource(getResources(), R.drawable.alarm);

                PendingIntent mPendingintent = PendingIntent.getActivity(PrjAlarmActivity.this, 0,
                        new Intent(getApplicationContext(), PrjAlarmActivity.class),
                        PendingIntent.FLAG_UPDATE_CURRENT);
                Notification.Builder mBuilder = new Notification.Builder(getApplicationContext());

                if(switchSound.isChecked() == true && switchVibe.isChecked() == true) {
                    Toast.makeText(getApplicationContext(), "둘 중 하나만 켜주세요.", Toast.LENGTH_LONG).show();
                    switchSound.toggle();
                    switchVibe.toggle();
                    return;
                }
                else if(switchSound.isChecked() == true && switchVibe.isChecked() == false) {
                    mBuilder.setSmallIcon(R.drawable.alarm)
                            .setContentTitle("감지 알림")
                            .setContentText("화재가 감지되었습니다.")
                            .setDefaults(Notification.DEFAULT_SOUND)
                            .setLargeIcon(mLargeIconForNoti)
                            .setAutoCancel(true)
                            .setTicker("Ticker")
                            .setContentIntent(mPendingintent);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        mBuilder.setCategory(Notification.CATEGORY_MESSAGE)
                                .setPriority(Notification.PRIORITY_HIGH)
                                .setVisibility(Notification.VISIBILITY_PUBLIC);
                    }
                }
                else if(switchVibe.isChecked() == true && switchSound.isChecked() == false) {
                    mBuilder.setSmallIcon(R.drawable.alarm)
                            .setContentTitle("감지 알림")
                            .setContentText("화재가 감지되었습니다.")
                            .setDefaults(Notification.DEFAULT_VIBRATE)
                            .setLargeIcon(mLargeIconForNoti)
                            .setPriority(Notification.PRIORITY_HIGH)
                            .setAutoCancel(true)
                            .setTicker("Ticker")
                            .setContentIntent(mPendingintent);
                }
                else {
                    Toast.makeText(getApplicationContext(), "하나의 설정을 켜주세요.", Toast.LENGTH_LONG).show();
                    return;
                }

                NotificationManager mNotificationManager =
                        (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
                mNotificationManager.notify(0, mBuilder.build());
            }
        });

        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mLargeIconForNoti =
                        BitmapFactory.decodeResource(getResources(), R.drawable.alarm);

                PendingIntent mPendingintent = PendingIntent.getActivity(PrjAlarmActivity.this, 1,
                        new Intent(getApplicationContext(), PrjAlarmActivity.class),
                        PendingIntent.FLAG_UPDATE_CURRENT);
                Notification.Builder mBuilder = new Notification.Builder(getApplicationContext());

                if(switchSound2.isChecked() == true && switchVibe2.isChecked() == true) {
                    Toast.makeText(getApplicationContext(), "둘 중 하나만 켜주세요.", Toast.LENGTH_LONG).show();
                    switchSound2.toggle();
                    switchVibe2.toggle();
                    return;
                }
                else if(switchSound2.isChecked() == true && switchVibe2.isChecked() == false) {
                    mBuilder.setSmallIcon(R.drawable.alarm)
                            .setContentTitle("감지 알림")
                            .setContentText("충격이 감지되었습니다.")
                            .setDefaults(Notification.DEFAULT_SOUND)
                            .setLargeIcon(mLargeIconForNoti)
                            .setAutoCancel(true)
                            .setTicker("Ticker")
                            .setContentIntent(mPendingintent);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        mBuilder.setCategory(Notification.CATEGORY_MESSAGE)
                                .setPriority(Notification.PRIORITY_HIGH)
                                .setVisibility(Notification.VISIBILITY_PUBLIC);
                    }
                }
                else if(switchVibe2.isChecked() == true && switchSound2.isChecked() == false) {
                    mBuilder.setSmallIcon(R.drawable.alarm)
                            .setContentTitle("감지 알림")
                            .setContentText("충격이 감지되었습니다.")
                            .setDefaults(Notification.DEFAULT_VIBRATE)
                            .setLargeIcon(mLargeIconForNoti)
                            .setPriority(Notification.PRIORITY_HIGH)
                            .setAutoCancel(true)
                            .setTicker("Ticker")
                            .setContentIntent(mPendingintent);
                }
                else {
                    Toast.makeText(getApplicationContext(), "하나의 설정을 켜주세요.", Toast.LENGTH_LONG).show();
                    return;
                }

                NotificationManager mNotificationManager =
                        (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
                mNotificationManager.notify(1, mBuilder.build());
            }
        });

        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mLargeIconForNoti =
                        BitmapFactory.decodeResource(getResources(), R.drawable.alarm);

                PendingIntent mPendingintent = PendingIntent.getActivity(PrjAlarmActivity.this, 2,
                        new Intent(getApplicationContext(), PrjAlarmActivity.class),
                        PendingIntent.FLAG_UPDATE_CURRENT);
                Notification.Builder mBuilder = new Notification.Builder(getApplicationContext());

                if(switchSound3.isChecked() == true && switchVibe3.isChecked() == true) {
                    Toast.makeText(getApplicationContext(), "둘 중 하나만 켜주세요.", Toast.LENGTH_LONG).show();
                    switchSound3.toggle();
                    switchVibe3.toggle();
                    return;
                }
                else if(switchSound3.isChecked() == true && switchVibe3.isChecked() == false) {
                    mBuilder.setSmallIcon(R.drawable.alarm)
                            .setContentTitle("감지 알림")
                            .setContentText("비인가 접근이 감지되었습니다.")
                            .setDefaults(Notification.DEFAULT_SOUND)
                            .setLargeIcon(mLargeIconForNoti)
                            .setAutoCancel(true)
                            .setTicker("Ticker")
                            .setContentIntent(mPendingintent);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        mBuilder.setCategory(Notification.CATEGORY_MESSAGE)
                                .setPriority(Notification.PRIORITY_HIGH)
                                .setVisibility(Notification.VISIBILITY_PUBLIC);
                    }
                }
                else if(switchVibe3.isChecked() == true && switchSound3.isChecked() == false) {
                    mBuilder.setSmallIcon(R.drawable.alarm)
                            .setContentTitle("감지 알림")
                            .setContentText("비인가 접근이 감지되었습니다.")
                            .setDefaults(Notification.DEFAULT_VIBRATE)
                            .setLargeIcon(mLargeIconForNoti)
                            .setPriority(Notification.PRIORITY_HIGH)
                            .setAutoCancel(true)
                            .setTicker("Ticker")
                            .setContentIntent(mPendingintent);
                }
                else {
                    Toast.makeText(getApplicationContext(), "하나의 설정을 켜주세요.", Toast.LENGTH_LONG).show();
                    return;
                }

                NotificationManager mNotificationManager =
                        (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
                mNotificationManager.notify("비인가 감지", 2, mBuilder.build());
            }
        });

        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                save();
            }
        });
    }

    private void save() {
        // SharedPreferences 객체만으론 저장 불가능 Editor 사용
        SharedPreferences.Editor editor = appData.edit();

        // 저장시킬 이름, 저장시킬 값
        editor.putBoolean("Fire_DATA",  switchSound.isChecked());
        editor.putBoolean("Fire_DATA2",  switchVibe.isChecked());
        editor.putBoolean("Shock_DATA",  switchSound2.isChecked());
        editor.putBoolean("Shock_DATA2",  switchVibe2.isChecked());
        editor.putBoolean("Unknown_DATA",  switchSound3.isChecked());
        editor.putBoolean("Unknown_DATA2",  switchVibe3.isChecked());
        // 또는 commit
        editor.apply();
    }

    private void load() {
        // 저장된 이름, 기본값
        check1 = appData.getBoolean("Fire_DATA", false);
        check2 = appData.getBoolean("Fire_DATA2", false);
        check3 = appData.getBoolean("Shock_DATA", false);
        check4 = appData.getBoolean("Shock_DATA2", false);
        check5 = appData.getBoolean("Unknown_DATA", false);
        check6 = appData.getBoolean("Unknown_DATA2", false);
    }

}
