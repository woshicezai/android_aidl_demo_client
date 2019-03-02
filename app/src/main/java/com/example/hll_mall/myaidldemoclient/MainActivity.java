package com.example.hll_mall.myaidldemoclient;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.hll_mall.myaidldemoserver.IMyAidlInterface;


public class MainActivity extends AppCompatActivity {
    private TextView mAdd;
    private IMyAidlInterface mStub;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAdd = findViewById(R.id.txt_add);
        mAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                //由于是隐式启动Service 所以要添加对应的action，A和之前服务端的一样。
                intent.setAction("com.example.hll_mall.myService");
                //android 5.0以后直设置action不能启动相应的服务，需要设置packageName或者Component。
                intent.setPackage("com.example.hll_mall.myaidldemoserver");
                bindService(intent, serviceConnection, BIND_AUTO_CREATE);
            }
        });
    }

    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            mStub = IMyAidlInterface.Stub.asInterface(iBinder);
            if (mStub == null) {
                Log.e("MainActivity", "the mStub is null");
            } else {
                try {
                    int result = mStub.add(Integer.parseInt(mAdd.getText().toString()), 3);
                    mAdd.setText(String.valueOf(result));
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {

        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(serviceConnection);
    }
}
