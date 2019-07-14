package com.pengsel.ws.ts;

import org.apache.log4j.Logger;

/**
 * @Author pengsel
 * @Create 2019/7/11 12:08
 */
public class TestLog {

    private static Logger logger= Logger.getLogger(TestLog.class);

    public static void main(String[] args) {

        // 记录debug级别的信息
        logger.debug("This is debug message.");
        // 记录info级别的信息
        logger.info("This is info message.");
        // 记录error级别的信息
        logger.error("This is error message.");
    }
}
