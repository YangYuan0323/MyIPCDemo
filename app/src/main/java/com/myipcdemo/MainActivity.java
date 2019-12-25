package com.myipcdemo;

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

import com.myipcdemo.entity.Message;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "MainActivity";
    private IConnectService connectServiceProxy;
    private IMessageService messageServiceProxy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        initData();
    }

    private void initView() {
        Button connect = findViewById(R.id.btn_connect);
        Button disConnect = findViewById(R.id.btn_disConnect);
        Button isConnect = findViewById(R.id.btn_isConnect);
        Button register = findViewById(R.id.btn_register);
        Button unregister = findViewById(R.id.btn_unregister);
        Button sendMessage = findViewById(R.id.btn_send_message);
        connect.setOnClickListener(this);
        disConnect.setOnClickListener(this);
        isConnect.setOnClickListener(this);
        register.setOnClickListener(this);
        unregister.setOnClickListener(this);
        sendMessage.setOnClickListener(this);

    }

    private void initData() {
        Intent intent=new Intent(MainActivity.this,RemoteService.class);
        bindService(intent,serviceConnection,Context.BIND_AUTO_CREATE);
    }

    private ServiceConnection serviceConnection = new ServiceConnection(){
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            try {
                IServiceManager serviceManagerProxy = IServiceManager.Stub.asInterface(service);
                connectServiceProxy = IConnectService.Stub.asInterface(serviceManagerProxy.getService(IConnectService.class.getSimpleName()));
                messageServiceProxy = IMessageService.Stub.asInterface(serviceManagerProxy.getService(IMessageService.class.getSimpleName()));
            } catch (RemoteException e) {
                e.printStackTrace();
            }

        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };

    private MessageReceiveListener listener = new MessageReceiveListener.Stub() {
        @Override
        public void onReceiveMessage(Message message) throws RemoteException {
            Log.i(TAG,"MessageReceiveListener -->message:"+message);
        }
    };


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_connect:
                try {
                    connectServiceProxy.connect();
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
                break;
            case R.id.btn_disConnect:
                try {
                    connectServiceProxy.disConnect();
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
                break;
            case R.id.btn_isConnect:
                try {
                    boolean isConnect = connectServiceProxy.isConnect();
                    Log.i(TAG,"MainActivity -->isConnect:"+isConnect);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
                break;
            case R.id.btn_register:
                try {
                    messageServiceProxy.registerMessageReceiveListener(listener);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
                break;
            case R.id.btn_unregister:
                try {
                    messageServiceProxy.unregisterMessageReceiveListener(listener);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
                break;
            case R.id.btn_send_message:
                try {
                    Message message = new Message();
                    message.setContent("send message from MainActivity");
                    messageServiceProxy.sendMessage(message);
                    Log.i(TAG,"MainActivity -->message.getSendSuccess:"+message.getSendSuccess());
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(serviceConnection);
    }
}
