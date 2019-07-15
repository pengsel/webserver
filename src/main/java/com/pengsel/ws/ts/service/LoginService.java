package com.pengsel.ws.ts.service;

import com.pengsel.ws.util.annotation.Autowired;
import com.pengsel.ws.util.annotation.RequestMapping;
import com.pengsel.ws.util.annotation.Service;

/**
 * @Author pengsel
 * @Create 2019/7/15 17:02
 */
@Service(name = "loginService")
@RequestMapping(path = "/login")
public class LoginService {

    @Autowired(instance = "helloService")
    private HelloService helloService;

    @RequestMapping(path = "/login")
    public void login(){
        System.out.println("Login......");
        helloService.time();
    }
}
