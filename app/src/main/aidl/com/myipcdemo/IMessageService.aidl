// IMessageService.aidl
package com.myipcdemo;
import com.myipcdemo.entity.Message;
import com.myipcdemo.MessageReceiveListener;

// Declare any non-default types here with import statements

interface IMessageService {
   void sendMessage(inout Message message);
   void registerMessageReceiveListener(MessageReceiveListener messageReceiveListener);
   void unregisterMessageReceiveListener(MessageReceiveListener messageReceiveListener);
}
