package com.example.aidlclient;

import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.administrator.aidltest.IMyAidlInterface;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "MainActivity";

    private EditText num1;
    private EditText num2;
    private EditText result;

    private Button toAdd;

    IMyAidlInterface iMyAidlInterface;

    private ServiceConnection conn = new ServiceConnection() {
        //绑定上服务的时候
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder){
            //接受到了远程的服务
            Log.d(TAG, "onServiceConnected: ");
            iMyAidlInterface = IMyAidlInterface.Stub.asInterface(iBinder);
        }

        // 当服务断开的时候调用
        @Override
        public void onServiceDisconnected(ComponentName componentName){
            Log.d(TAG, "onServiceDisconnected: ");
            //回收资源
            iMyAidlInterface = null;

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        bindServce();
    }

    private void initView() {
        num1 = (EditText) findViewById(R.id.num1);
        num2 = (EditText) findViewById(R.id.num2);
        result = (EditText) findViewById(R.id.result);

        toAdd = (Button) findViewById(R.id.toAdd);
        toAdd.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        int number1 = Integer.valueOf(num1.getText().toString());
        int number2 = Integer.valueOf(num2.getText().toString());

        try {
            //调用远程服务
            int valueRes = iMyAidlInterface.add(number1, number2);
            result.setText(valueRes + "");
        } catch (RemoteException e) {
            e.printStackTrace();
            result.setText("error");
        }
    }

    private void bindServce() {
        //获取到服务端, 5.0 之后必须显示绑定服务
        Intent intent = new Intent();
        intent.setComponent(new ComponentName("com.example.administrator.aidltest", "com.example.administrator.aidltest.RemoteService"));
        bindService(intent, conn, BIND_AUTO_CREATE);
        Log.d(TAG, "bindServce: bind on end");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(conn);
    }
}