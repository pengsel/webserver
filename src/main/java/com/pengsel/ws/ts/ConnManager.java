package com.pengsel.ws.ts;

/**
 * @Author pengsel
 * @Create 2019/7/11 11:10
 */
public interface ConnManager {

    /**
     * 增加连接
     */
    void add(Conn conn);

    /**
     * 删除连接
     */
    void remove(Conn conn);

    /**
     * 根据连接ID获取连接
     * @param connId 连接ID
     * @return
     */
    Conn get(int connId);

    /**
     * 当前管理的连接总量
     * @return size
     */
    int size();

    /**
     * 清理所有连接
     */
    void clear();

}
