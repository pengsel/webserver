package com.pengsel.ws.ts;

import java.util.concurrent.Executor;

/**
 * @Author pengsel
 * @Create 2019/7/11 11:06
 */
public interface Server {
    /**
     * 启动服务器
     */
    void start();

    /**
     * 停止服务器
     */
    void stop();

    /**
     * 服务
     */
    void serve();

    /**
     * 增加路由
     */
    void addRouter(int msgId,Router router);

    /**
     * 获取连接管理器
     * @return 连接管理器
     */
    ConnManager getConnManager();

    Executor getThreadPoolExecutor();
}
