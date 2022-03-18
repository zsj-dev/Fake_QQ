package com.zsj.qqclient.service;

import com.zsj.qqcommon.Message;
import com.zsj.qqcommon.MessageType;

import java.io.*;

public class FileClientService {
    public void sendFileToOne(String src,String dest,String senderId,String getterId){
        Message message=new Message();
        message.setMesType(MessageType.MESSAGE_FILE_MES);
        message.setSender(senderId);
        message.setGetter(getterId);
        message.setDest(dest);
        message.setSrc(src);
        FileInputStream fileInputStream=null;
        byte[] fileBytes=new byte[(int)new File(src).length()];
        try {
            fileInputStream=new FileInputStream(src);
            fileInputStream.read(fileBytes);
            message.setFileBytes(fileBytes);

        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            if(fileInputStream!=null){
                try {
                    fileInputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        System.out.println("\n"+senderId+" send file ("+src+") to "+getterId+" to ("+dest+")");
        try {
            ObjectOutputStream oos = new ObjectOutputStream(ManageClientConnectServerThread
                    .getClientConnectServerThread(senderId).getSocket().getOutputStream());
            oos.writeObject(message);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
