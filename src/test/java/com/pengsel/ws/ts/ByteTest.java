package com.pengsel.ws.ts;

import java.util.Arrays;

public class ByteTest {
    public static void main(String[] args) {
        char c='{';
        byte[] b = new byte[2];
        b[0] = (byte) ((c & 0xFF00) >> 8);
        b[1] = (byte) (c & 0xFF);
        System.out.println(Arrays.toString(b));

        int i=123;
        byte[] result = new byte[4];
        //由高位到低位
        result[0] = (byte)((i >> 24) & 0xFF);
        result[1] = (byte)((i >> 16) & 0xFF);
        result[2] = (byte)((i >> 8) & 0xFF);
        result[3] = (byte)(i & 0xFF);
        System.out.println(Arrays.toString(result));
    }
}
