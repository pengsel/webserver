package com.pengsel.ws.ts.service;

import com.pengsel.ws.util.annotation.RequestMapping;
import com.pengsel.ws.util.annotation.Service;

/**
 * @Author pengsel
 * @Create 2019/7/15 16:43
 */
@Service(name = "helloService")
@RequestMapping(path = "/hello")
public class HelloService {

    @RequestMapping(path = "/time")
    public void time(){
        System.out.println("Now :"+System.currentTimeMillis());
    }
}
