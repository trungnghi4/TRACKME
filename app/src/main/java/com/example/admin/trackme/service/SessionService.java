//package com.example.admin.trackme.service;
//
//import android.app.Service;
//import android.content.Intent;
//import android.os.IBinder;
//import android.support.annotation.Nullable;
//
//public class SessionService extends Service {
//    @Nullable
//    @Override
//    public IBinder onBind(Intent intent) {
//        return null;
//    }
//    @Override
//    public void onCreate() {
//        super.onCreate();
//        Toast.makeText(this, "onCreate", Toast.LENGTH_SHORT).show();
//    }
//
//    @Override
//    public void onStart(Intent intent, int startId) {
//        super.onStart(intent, startId);
//        Toast.makeText(this, "onStart", Toast.LENGTH_SHORT).show();
//    }
//
//    @Override
//    public int onStartCommand(Intent intent, int flags, int startId) {
//        String thangcoder = null;
//        if(intent!= null){
//            thangcoder  = intent.getStringExtra("Thangcoder.com");
//        }
//        Toast.makeText(this, "onStartCommand: "+thangcoder, Toast.LENGTH_SHORT).show();
//        return START_STICKY;
//    }
//
//
//    @Override
//    public void onDestroy() {
//        super.onDestroy();
//        Toast.makeText(this, "onDestroy", Toast.LENGTH_SHORT).show();
//    }
//}
