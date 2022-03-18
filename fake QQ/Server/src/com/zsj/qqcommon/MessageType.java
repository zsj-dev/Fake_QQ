package com.zsj.qqcommon;

public interface MessageType {
    String MESSAGE_LOGIN_SUCCEED="1";//represent login succeed
    String MESSAGE_LOGIN_FAIL="2";//represent login fail
    String MESSAGE_COMM_MES="3";//common message
    String MESSAGE_GET_ONLINE_FRIEND="4";//require  return list of online friends
    String MESSAGE_RET_ONLINE_FRIEND="5";//return list of online friends
    String MESSAGE_CLIENT_EXIT="6";//ask for exiting
    String MESSAGE_TO_ALL = "7";//mass message
    String MESSAGE_FILE_MES ="8";//send file
}
