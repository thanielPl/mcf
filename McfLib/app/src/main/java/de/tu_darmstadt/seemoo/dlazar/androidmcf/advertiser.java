package de.tu_darmstadt.seemoo.dlazar.androidmcf;

import android.net.nsd.NsdManager;
import android.net.nsd.NsdServiceInfo;

import java.io.IOException;
import java.net.ServerSocket;

/**
 * Created by Daniel Lazar
 */
public class advertiser {
    ServerSocket mServerSocket;
    int mLocalPort;
    NsdManager.RegistrationListener mRegistrationListener;
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
}
