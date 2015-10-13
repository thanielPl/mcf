package de.tu_darmstadt.seemoo.dlazar.androidmcf;

import android.content.Context;
import android.net.nsd.NsdManager;
import android.net.nsd.NsdServiceInfo;
import android.util.Log;

import java.io.IOException;
import java.net.ServerSocket;

/**
 * Created by Daniel Lazar
 */
public class NsdHelper {


    Context mContext;
    NsdManager mNsdManager;
    ServerSocket mServerSocket;
    int mLocalPort;
    NsdServiceInfo mService;
    NsdManager.RegistrationListener mRegistrationListener;
    NsdManager.DiscoveryListener mDiscoveryListener;

    String mServiceName;
    String mStringId;

    public void registerService(int port, String id){
        mStringId = id;
        //instantiate NsdServiceInfo which can handle the Bonjour/mDNS stuff
        //therefore no library is needed
        NsdServiceInfo serviceInfo = new NsdServiceInfo();

        //service name should be string id
        serviceInfo.setServiceName(mStringId);
        //should get the name for our service on the ios devices, followed from _tcp.
        //which is default in Multipeer Connectivity
        serviceInfo.setServiceType("_chat-files._tcp.");
        serviceInfo.setPort(port);

    }

    public void initializeServerSocket() throws IOException {
        mServerSocket = new ServerSocket(0);

        mLocalPort = mServerSocket.getLocalPort();
    }

    public void initializeRegistrationListener() {
        mRegistrationListener = new NsdManager.RegistrationListener(){

            @Override
            public void onServiceRegistered(NsdServiceInfo NsdServiceInfo){
                mServiceName = NsdServiceInfo.getServiceName();
            }

            @Override
            public void onRegistrationFailed(NsdServiceInfo serviceInfo, int errorCode){
                //TODO some output which states that the registration failed
            }

            @Override
            public void onServiceUnregistered(NsdServiceInfo arg0){
                //TODO don't know...maybe not needed for MCF
            }

            @Override
            public void onUnregistrationFailed(NsdServiceInfo serviceInfo, int errorCode){
                //TODO same as method above
            }
        };
    }

    public void initializeDiscoveryListener() {
        mDiscoveryListener = new NsdManager.DiscoveryListener() {

            @Override
            public void onStartDiscoveryFailed(String serviceType, int errorCode) {
                Log.e("NsdHelper","Discovery failed: Error code:"+errorCode);
                mNsdManager.stopServiceDiscovery(this);
            }

            @Override
            public void onStopDiscoveryFailed(String serviceType, int errorCode) {
                Log.e("NsdHelper","Discovery failed: Error code:"+errorCode);
                mNsdManager.stopServiceDiscovery(this);
            }

            @Override
            public void onDiscoveryStarted(String serviceType) {

            }

            @Override
            public void onDiscoveryStopped(String serviceType) {

            }

            @Override
            public void onServiceFound(NsdServiceInfo serviceInfo) {
                if(!serviceInfo.getServiceType().equals("_chat-files._tcp.")){
                    //log, that it is a different service type, but not the one for our chat app
                } else if(serviceInfo.getServiceName().equals("")){
                    //log, that it is our machine....
                }
                else{
                    //implement method which distributes the neccesary information to the second protocol
                };
            }

            @Override
            public void onServiceLost(NsdServiceInfo serviceInfo) {
                if (mService == serviceInfo) {
                    mService = null;
                }
            }
        };
    }

    public void discoverServices(){
        mNsdManager.discoverServices("_chat-files._tcp.", NsdManager.PROTOCOL_DNS_SD, mDiscoveryListener);
    }

    public void stopDicovery() {
        mNsdManager.stopServiceDiscovery(mDiscoveryListener);
    }

    public NsdServiceInfo getChosenServiceInfo() {
        return mService;
    }

    public void tearDown() {
        mNsdManager.unregisterService(mRegistrationListener);
    }
}
