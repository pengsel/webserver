package com.pengsel.ws.util;

import java.io.File;

/**
 * @Author pengsel
 * @Create 2019/7/11 13:29
 */
public class Constants {

    public static final String HOST="127.0.0.1";

    public static final String RIGISTER_CENTER_IP="127.0.0.1";

    public static final int REGISTER_CENTER_PORT=6666;


    public static final int PORT=7777;
    public static final int MAX_CONN=10;
    public static final int WORKER_POOL_SIZE=10;
    public static final int TCP_CONN_SIZE=10;

    public static final String WEB_ROOT=System.getProperty("user.dir") + File.separator  + "webroot";

    public static final String SCAN_PATH=System.getProperty("user.dir") +
            File.separator+"src"+
            File.separator+"main"+
            File.separator+"java"+
            File.separator+"com"+
            File.separator+"pengsel"+
            File.separator+"ws"+
            File.separator+"ts"+
            File.separator+"service";
}
