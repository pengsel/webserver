package com.pengsel.ws.ts;

/**
 * @Author pengsel
 * @Create 2019/7/11 11:27
 */
public interface Message {

    int getDataLen();

    int getMsgId();

    byte[] getData();

    boolean isJson();

    void setJson(boolean isJson);

    void setMsgId(int msgId);

    void setData(byte[] bytes);

    void setDataLen(int dataLen);
}
