package com.myipcdemo;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteCallbackList;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;

import com.myipcdemo.entity.Message;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * @author JohnYang
 * @description:
 * @date :2019/12/24 0024 下午 2:41
 */
public class RemoteService extends Service {
    public String TAG = "RemoteService";
    private Boolean isConnect = false;
    private ScheduledExecutorService scheduledExecutorService;
    private RemoteCallbackList<MessageReceiveListener> messageReceiveListeners = new RemoteCallbackList<>();

    @Override
    public void onCreate() {
        super.onCreate();
        scheduledExecutorService = Executors.newScheduledThreadPool(1);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return serviceManager.asBinder();
    }

    private IConnectService connectService = new IConnectService.Stub() {
        @Override
        public void connect() throws RemoteException {
            try {
                isConnect = true;
                Thread.sleep(5*1000);
                Log.i(TAG,"connectService --> connect");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void disConnect() throws RemoteException {
            isConnect = false;
            Log.i(TAG,"connectService --> disConnect");
        }

        @Override
        public boolean isConnect() throws RemoteException {
            Log.i(TAG,"connectService --> isConnect");
            return isConnect;
        }
    };

    private IMessageService messageService = new IMessageService.Stub() {
        @Override
        public void sendMessage(final Message message) throws RemoteException {
            Log.i(TAG,"IMessageService -->message:"+message);
            if(isConnect){
                message.setSendSuccess(true);
            }else{
                message.setSendSuccess(false);
            }
            scheduledExecutorService.scheduleAtFixedRate(new Runnable() {
                @Override
                public void run() {
                    int size = messageReceiveListeners.beginBroadcast();
                    for (int i=0;i<size;i++){
                        message.setContent("send message from RemoteService");
                        try {
                            messageReceiveListeners.getBroadcastItem(i).onReceiveMessage(message);
                        } catch (RemoteException e) {
                            e.printStackTrace();
                        }
                    }
                    messageReceiveListeners.finishBroadcast();

                }
            },3*1000,3*1000,TimeUnit.MILLISECONDS);

        }

        @Override
        public void registerMessageReceiveListener(MessageReceiveListener messageReceiveListener) throws RemoteException {
            Log.i(TAG,"registerMessageReceiveListener");
            messageReceiveListeners.register(messageReceiveListener);
        }

        @Override
        public void unregisterMessageReceiveListener(MessageReceiveListener messageReceiveListener) throws RemoteException {
            Log.i(TAG,"unregisterMessageReceiveListener");
            messageReceiveListeners.unregister(messageReceiveListener);
        }
    };

    private IServiceManager serviceManager = new IServiceManager.Stub() {
        @Override
        public IBinder getService(String serviceName) throws RemoteException {
            if(IConnectService.class.getSimpleName().equals(serviceName)){
                return connectService.asBinder();
            }else if(IMessageService.class.getSimpleName().equals(serviceName)){
                return messageService.asBinder();
            }else{
                return null;
            }
        };
    };
}
