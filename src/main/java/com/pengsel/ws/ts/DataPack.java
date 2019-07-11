package com.pengsel.ws.ts;

/**
 * @Author pengsel
 * @Create 2019/7/11 11:24
 */
public interface DataPack {

    int getHeadLen();

    byte[] pack(Message msg);

    Message unpack(byte[] bytes);
}
