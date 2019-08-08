package com.pengsel.ws.ts.impl;

import com.pengsel.ws.ts.Message;

/**
 * @Author pengsel
 * @Create 2019/7/11 14:50
 */
public class TCPMessage implements Message {
    private int dataLen;
    private boolean isJson;
    private int msgId;
    private byte[] data;

    public int getDataLen() {
        return dataLen;
    }

    public TCPMessage(int dataLen, int msgId, byte[] data) {
        this.dataLen = dataLen;
        this.msgId = msgId;
        this.data = data;
    }

    public void setDataLen(int dataLen) {
        this.dataLen = dataLen;
    }

    public int getMsgId() {
        return msgId;
    }

    public void setMsgId(int msgId) {
        this.msgId = msgId;
    }

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }

    public boolean isJson() {
        return isJson;
    }

    public void setJson(boolean json) {
        isJson = json;
    }
}
