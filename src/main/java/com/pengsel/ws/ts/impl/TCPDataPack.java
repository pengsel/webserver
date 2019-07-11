package com.pengsel.ws.ts.impl;

import com.pengsel.ws.ts.DataPack;
import com.pengsel.ws.ts.Message;

import java.io.ByteArrayInputStream;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @Author pengsel
 * @Create 2019/7/11 14:08
 */
public class TCPDataPack implements DataPack {
    public int getHeadLen() {
        return 8;
    }

    public byte[] pack(Message msg) {
        byte[] bytes=new byte[4+4+msg.getDataLen()];
        for (int i=0;i<bytes.length;i++){
            if (i<4){
                bytes[i]=intToByteArray(msg.getDataLen())[i];
            }else if (i<8){
                bytes[i]=intToByteArray(msg.getMsgId())[i-4];
            }else {
                bytes[i]=msg.getData()[i-8];
            }
        }
        return bytes;
    }

    public Message unpack(byte[] bytes) {
        byte[] dataLenBytes=new byte[4];
        byte[] msgIdBytes=new byte[4];
        for (int i=0;i<bytes.length;i++){
            if (i<4){
                dataLenBytes[i]=bytes[i];
            }else {
                msgIdBytes[i-4]=bytes[i];
            }
        }
        int dataLen=byteArrayToInt(dataLenBytes);
        int msgId=byteArrayToInt(msgIdBytes);
        Message msg=new TCPMessage(dataLen,msgId,null);
        return msg;
    }

    /**
     * int到byte[]
     * @param i
     * @return
     */
    public static byte[] intToByteArray(int i) {
        byte[] result = new byte[4];
        //由高位到低位
        result[0] = (byte)((i >> 24) & 0xFF);
        result[1] = (byte)((i >> 16) & 0xFF);
        result[2] = (byte)((i >> 8) & 0xFF);
        result[3] = (byte)(i & 0xFF);
        return result;
    }

    /**
     * byte[]转int
     * @param bytes
     * @return
     */
    public static int byteArrayToInt(byte[] bytes) {
        int value= 0;
        //由高位到低位
        for (int i = 0; i < 4; i++) {
            int shift= (4 - 1 - i) * 8;
            //往高位游
            value +=(bytes[i] & 0x000000FF) << shift;
        }
        return value;
    }
}
