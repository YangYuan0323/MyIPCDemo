// IServiceManager.aidl
package com.myipcdemo;

// Declare any non-default types here with import statements

interface IServiceManager {
   IBinder getService(String serviceName);
}
