package com.zsj.qqcserver.service;

import java.util.HashMap;
import java.util.Iterator;

public class ManageClientThreads {
    private static HashMap<String,ServerConnectClientThread> hm=new HashMap<>();
    public static void addThread(String userId, ServerConnectClientThread serverConnectClientThread){
        hm.put(userId,serverConnectClientThread);
    }
    public static ServerConnectClientThread getServerConnectClientThread(String userId){
        return hm.get(userId);
    }
    public static String getOnlineUsers(){
        Iterator<String> iterator = hm.keySet().iterator();
        String onlineUserList ="";
        while (iterator.hasNext()){
            onlineUserList+= iterator.next().toString()+" ";
        }
        return onlineUserList;
    }

    public static void removeTread(String usrId){
        hm.remove(usrId);

    }

    public static HashMap<String, ServerConnectClientThread> getHm() {
        return hm;
    }
}
