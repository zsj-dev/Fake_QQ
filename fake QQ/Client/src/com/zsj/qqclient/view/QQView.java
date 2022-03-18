package com.zsj.qqclient.view;

import com.sun.javaws.IconUtil;
import com.zsj.qqclient.service.FileClientService;
import com.zsj.qqclient.service.MessageClientService;
import com.zsj.qqclient.service.UserClientService;
import com.zsj.qqclient.utils.Utility;

import javax.swing.plaf.synth.SynthEditorPaneUI;

public class QQView {
    private boolean loop=true;
    private String key="";
    private UserClientService userClientService=new UserClientService();
    private MessageClientService messageClientService=new MessageClientService();
    private FileClientService fileClientService=new FileClientService();

    public static void main(String[] args) {
        new QQView().mainMenu();
        System.out.println("客户端退出......");
    }
    //show main menu
    private void mainMenu(){
        while(loop){
            System.out.println("=========== welcome to Fake QQ ===========");
            System.out.println("\t\t\t 1 log in");
            System.out.println("\t\t\t 9 log out");
            System.out.print("please input your choice: ");
            key= Utility.readString(1);
            switch (key){
                case "1":
                    System.out.print("input your uerId: ");
                    String uerId=Utility.readString(50);
                    System.out.print("input your password: ");
                    String pwd =Utility.readString(50);

                    if(userClientService.checkUser(uerId,pwd)){
                        System.out.print("=========== welcome (user "+uerId+") ========== ");
                        while (loop){
                            System.out.println("\n=========== secondary menu (user "+uerId+") ===========");
                            System.out.println("\t\t\t 1 display the list of online users");
                            System.out.println("\t\t\t 2 mass messaging");
                            System.out.println("\t\t\t 3 private chat message");
                            System.out.println("\t\t\t 4 send file");
                            System.out.println("\t\t\t 9 log out");
                            System.out.print("input your choice: ");
                            key=Utility.readString(1);
                            switch (key){
                                case  "1":
                                    userClientService.getOnlineFriendList();
                                    break;
                                case  "2":
                                    System.out.print("input what you want to say :");
                                    String massContent=Utility.readString(100);
                                    messageClientService.sendMessageToAll(massContent,uerId);
                                    break;
                                case  "3":
                                    System.out.print("input the user who you wanted to chat with: ");
                                    String getterId= Utility.readString(50);
                                    System.out.print("input message: ");
                                    String content=Utility.readString(100);
                                    messageClientService.sendMessageToOne(content,uerId,getterId);

                                    break;
                                case  "4":
                                    System.out.print("input who you want to send: ");
                                    getterId=Utility.readString(50);
                                    System.out.print("input the path of the file: ");
                                    String src=Utility.readString(100);
                                    System.out.print("input the path that you want to send :");
                                    String dest=Utility.readString(100);
                                    fileClientService.sendFileToOne(src,dest,uerId,getterId);

                                    break;
                                case  "9":
                                    loop=false;
                                    userClientService.logout();
                                    break;
                            }
                        }

                    }else{
                        System.out.println("========== fail to log in ===========");

                    }

                    break;
                case "9":
                    loop=false;
                    break;
            }

        }
    }
}
