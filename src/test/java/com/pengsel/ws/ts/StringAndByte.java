package com.pengsel.ws.ts;

/**
 * @Author pengsel
 * @Create 2019/7/11 17:58
 */
public class StringAndByte {
    public static void main(String[] args)
    {
        //Original String
        String string = "hello world";

        //Convert to byte[]
        byte[] bytes = string.getBytes();

        //Convert back to String
        String s = new String(bytes);

        //Check converted string against original String
        System.out.println("Decoded String : " + s);
    }
}
