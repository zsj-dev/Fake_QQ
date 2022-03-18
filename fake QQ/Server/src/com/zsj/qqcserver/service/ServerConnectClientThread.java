package com.zsj.qqcserver.service;

import com.zsj.qqcommon.Message;
import com.zsj.qqcommon.MessageType;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.HashMap;
import java.util.Iterator;

public class ServerConnectClientThread extends Thread {
    private Socket socket;
    private String userId;

    public Socket getSocket() {
        return socket;
    }

    public ServerConnectClientThread(Socket socket, String userId) {
        this.socket = socket;
        this.userId = userId;
    }

    @Override
    public void run() {
        while(true){
            try {
                System.out.println("the server is connecting with the client ("+userId+ ") and reading data...");
                ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
                Message message=(Message) ois.readObject();
                if(message.getMesType().equals(MessageType.MESSAGE_GET_ONLINE_FRIEND)){
                    System.out.println(message.getSender()+" is requiring the list of online users ");
                    String onlineUsers = ManageClientThreads.getOnlineUsers();
                    Message message2 = new Message();
                    message2.setMesType(MessageType.MESSAGE_RET_ONLINE_FRIEND);
                    message2.setContent(onlineUsers);
                    message2.setGetter(message.getSender());
                    ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
                    oos.writeObject(message2);
                }else if(message.getMesType().equals(MessageType.MESSAGE_CLIENT_EXIT)){
                    System.out.println(message.getSender()+ " exit");

                    ManageClientThreads.removeTread(message.getSender());
                    socket.close();
                    break;

                }else if(message.getMesType().equals(MessageType.MESSAGE_COMM_MES)){
                    ServerConnectClientThread serverConnectClientThread =
                            ManageClientThreads.getServerConnectClientThread(message.getGetter());
                    ObjectOutputStream oos =
                            new ObjectOutputStream(serverConnectClientThread.getSocket().getOutputStream());
                    oos.writeObject(message);

                }else if(message.getMesType().equals(MessageType.MESSAGE_TO_ALL)){
                    HashMap<String, ServerConnectClientThread> hm = ManageClientThreads.getHm();
                    Iterator<String> iterator = hm.keySet().iterator();
                    while (iterator.hasNext()){
                        String onlineUser=iterator.next().toString();
                        if(!(onlineUser.equals(message.getSender()))){
                            ObjectOutputStream oos =
                                    new ObjectOutputStream(hm.get(onlineUser).getSocket().getOutputStream());
                            oos.writeObject(message);
                        }

                    }
                }else if(message.getMesType().equals(MessageType.MESSAGE_FILE_MES)){
                    ServerConnectClientThread serverConnectClientThread = ManageClientThreads
                            .getServerConnectClientThread(message.getGetter());
                    ObjectOutputStream oos = new ObjectOutputStream(serverConnectClientThread
                            .getSocket().getOutputStream());
                    oos.writeObject(message);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
