package com.zsj.qqclient.service;

import com.zsj.qqcommon.Message;
import com.zsj.qqcommon.MessageType;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;

public class ClientConnectServerThread extends Thread {
    private Socket socket;

    public ClientConnectServerThread(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        while(true){
            try {
                System.out.println("client thread is waiting read message from server");
                ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
                Message message=(Message) ois.readObject();

                if(message.getMesType().equals(MessageType.MESSAGE_RET_ONLINE_FRIEND)){
                    String[] onlineUsers =message.getContent().split(" ");
                    System.out.println("\n=========== the list of online users ===========");
                    for(int i=0;i< onlineUsers.length;i++){
                        System.out.println("User: "+onlineUsers[i]);
                    }

                }else if(message.getMesType().equals(MessageType.MESSAGE_COMM_MES)){
                    System.out.println("\n"+message.getSender()+" send to me"+
                            ": "+message.getContent());
                }else if(message.getMesType().equals(MessageType.MESSAGE_TO_ALL)){
                    System.out.println("\n"+message.getSender()+" send to everyone:"
                            +message.getContent());
                }else if(message.getMesType().equals(MessageType.MESSAGE_FILE_MES)){
                    System.out.println("\n"+message.getSender()+" send me a file to ("+message.getDest()+")");
                    FileOutputStream fileOutputStream = new FileOutputStream(message.getDest());
                    fileOutputStream.write(message.getFileBytes());
                    fileOutputStream.close();
                    System.out.println("\nsucceed to save the file");

                }
            } catch (Exception e) {
                e.printStackTrace();
            }


        }

    }

    public Socket getSocket() {
        return socket;
    }

    public void setSocket(Socket socket) {
        this.socket = socket;
    }
}
