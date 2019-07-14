package com.pengsel.ws.util;

import java.io.File;

/**
 * @Author pengsel
 * @Create 2019/7/11 13:29
 */
public class Constants {

    public static final String HOST="127.0.0.1";
    public static final int PORT=7777;
    public static final int MAX_CONN=10;
    public static final int WORKER_POOL_SIZE=10;

    public static final String WEB_ROOT=System.getProperty("user.dir") + File.separator  + "webroot";
}