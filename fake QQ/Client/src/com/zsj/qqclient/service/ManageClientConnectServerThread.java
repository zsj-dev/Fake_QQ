package com.zsj.qqclient.service;

import java.util.HashMap;

public class ManageClientConnectServerThread {
    //key is userId ,value is threads
    private static HashMap<String,ClientConnectServerThread> hm=new HashMap<>();

    public static void addThread(String userId,ClientConnectServerThread clientConnectServerThread){
        hm.put(userId,clientConnectServerThread);

    }
    public static ClientConnectServerThread getClientConnectServerThread(String userId){
        return hm.get(userId);
    }
}
