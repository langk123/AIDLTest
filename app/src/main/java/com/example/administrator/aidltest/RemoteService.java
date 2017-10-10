package com.example.administrator.aidltest;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.v4.media.MediaMetadataCompat;
import android.util.Log;

public class RemoteService extends Service {
    private static final String TAG = "RemoteService";

    public RemoteService() {
    }

    @Override
    public IBinder onBind(Intent intent){
        return iBinder;
    }
    private IBinder iBinder = new IMyAidlInterface.Stub() {

        @Override
        public int add(int num1, int num2) throws RemoteException {
            Log.d(TAG, "add方法：收到了客户端的请求 ，输入的" +
                    "num1 ->" + num1 + "\n num2 ->" + num2);
            return num1 + num2;
        }
    };
}

