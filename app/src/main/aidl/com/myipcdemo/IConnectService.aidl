// IConnectService.aidl
package com.myipcdemo;

interface IConnectService {
   oneway void connect();
   void disConnect();
   boolean isConnect();
}
