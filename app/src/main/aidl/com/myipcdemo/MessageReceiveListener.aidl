// MessageReceiveListener.aidl
package com.myipcdemo;
import com.myipcdemo.entity.Message;

// Declare any non-default types here with import statements

interface MessageReceiveListener {
   void onReceiveMessage(in Message message);
}
