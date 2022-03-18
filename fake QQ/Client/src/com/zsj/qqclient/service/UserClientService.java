package com.zsj.qqclient.service;

import com.zsj.qqcommon.Message;
import com.zsj.qqcommon.MessageType;
import com.zsj.qqcommon.User;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

public class UserClientService {
    private User u=new User();
    private Socket socket;

    public boolean checkUser(String userId, String password)  {
        boolean b=false;
        //create a user
        u.setUserId(userId);
        u.setPassword(password);
        //connect to the server send user
        try {
            socket=new Socket(InetAddress.getLocalHost(),9999);
            ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
            oos.writeObject(u);//send user

            ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
            Message ms=(Message) ois.readObject();
            if(ms.getMesType().equals(MessageType.MESSAGE_LOGIN_SUCCEED)){

                ClientConnectServerThread clientConnectServerThread =
                        new ClientConnectServerThread(socket);
                clientConnectServerThread.start();
                ManageClientConnectServerThread.addThread(userId,clientConnectServerThread);
                b=true;
            }else {
                socket.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return b;
    }

    public void getOnlineFriendList(){
        Message message = new Message();
        message.setMesType(MessageType.MESSAGE_GET_ONLINE_FRIEND);
        message.setSender(u.getUserId());
        try {
            ObjectOutputStream oos =
                    new ObjectOutputStream(ManageClientConnectServerThread
                            .getClientConnectServerThread(u.getUserId()).getSocket().getOutputStream());

            oos.writeObject(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void logout(){
        Message message=new Message();
        message.setMesType(MessageType.MESSAGE_CLIENT_EXIT);
        message.setSender(u.getUserId());
        try {
            ObjectOutputStream oos =
                    new ObjectOutputStream(ManageClientConnectServerThread
                            .getClientConnectServerThread(u.getUserId()).getSocket().getOutputStream());
            oos.writeObject(message);
            System.out.println(u.getUserId()+" exit system");
            System.exit(0);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
