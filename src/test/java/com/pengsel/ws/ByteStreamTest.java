package com.pengsel.ws;

import java.io.ByteArrayInputStream;
import java.util.Arrays;

import static com.pengsel.ws.ts.impl.TCPDataPack.byteArrayToInt;
import static com.pengsel.ws.ts.impl.TCPDataPack.intToByteArray;

/**
 * @Author pengsel
 * @Create 2019/7/11 15:01
 */
public class ByteStreamTest {
    public static void main(String[] args) {

        byte[] bytes =  new byte[]{(byte)42};
        System.out.println(bytes.length);

        ByteArrayInputStream in = new ByteArrayInputStream(bytes);

        int result = in.read();
        System.out.println("无符号数: \t"+result);
        System.out.println("2进制bit位: \t"+Integer.toBinaryString(result));
        System.out.println("___________________");
        bytes=intToByteArray(168);
        System.out.println(byteArrayToInt(bytes));

    }
}
