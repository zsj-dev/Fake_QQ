package com.zsj.qqcserver.service;

import com.zsj.qqcommon.Message;
import com.zsj.qqcommon.MessageType;
import com.zsj.qqcommon.User;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ConcurrentHashMap;

public class QQServer {
    private ServerSocket ss=null;
    private static ConcurrentHashMap<String,User> validUsers =new ConcurrentHashMap<>();
    static {
        validUsers.put("100",new User("100","123456"));
        validUsers.put("200",new User("200","123456"));
        validUsers.put("300",new User("300","123456"));
        validUsers.put("400",new User("400","123456"));
    }
    private boolean checkUser(String userId,String password){
        User user =validUsers.get(userId);
        if(user==null){
            return false;
        }
        if(!(user.getPassword().equals(password))){
            return false;
        }
        return true;
    }

    public QQServer(){
        try {
            System.out.println("the server is listing on port 9999");
            ss=new ServerSocket(9999);
            while (true){
                Socket socket=ss.accept();
                ObjectInputStream ois = new ObjectInputStream((socket.getInputStream()));

                ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
                User u=(User) ois.readObject();
                Message message=new Message();
                //check
                if(checkUser(u.getUserId(),u.getPassword())){
                    message.setMesType(MessageType.MESSAGE_LOGIN_SUCCEED);
                    oos.writeObject(message);

                    ServerConnectClientThread serverConnectClientThread =
                            new ServerConnectClientThread(socket, u.getUserId());
                    serverConnectClientThread.start();

                    ManageClientThreads.addThread(u.getUserId(),serverConnectClientThread);

                }else{
                    System.out.println("user "+u.getUserId()+" fail to log in");
                    message.setMesType(MessageType.MESSAGE_LOGIN_FAIL);
                    oos.writeObject(message);
                    socket.close();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            try {
                ss.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

    }
}
