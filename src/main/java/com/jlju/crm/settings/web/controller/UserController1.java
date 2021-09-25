/*
package com.jlju.crm.settings.web.controller;

import com.jlju.crm.settings.domain.User;
import com.jlju.crm.settings.service.UserService;
import com.jlju.crm.utils.MD5Util;
import com.jlju.crm.utils.PrintJson;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/settings/user")
public class UserController1 {
    @Resource
    //未来业务层开发，统一使用代理类形态接口对象
    private UserService userService;

    @RequestMapping("/login.do")
    public void login(HttpServletRequest request, HttpServletResponse response){
        System.out.println("进入验证登录操作");

        String loginAct=request.getParameter("loginAct");
        String loginPwd=request.getParameter("loginPwd");
        //将密码的明文形式转换为MD5密文形式
        loginPwd= MD5Util.getMD5(loginPwd);
        //接受浏览器端的ip地址
        String ip=request.getRemoteAddr();
        System.out.println("ip-----"+ip);

        try{
            User user=userService.login(loginAct,loginPwd,ip);

            //如果程序执行到此处，说明业务层没有为controller跑出任何异常  表示登录成功
            */
/*
                {"success":true}
            *//*

            PrintJson.printJsonFlag(response,true);

        }catch (Exception e){
            e.printStackTrace();
            //一旦程序执行了catch块的信息 说明业务层为我们验证登录失败，为controller抛出了异常 表示登录失败
             */
/*
                {"success":false,"msg":?}
            *//*

            String msg=e.getMessage();
            */
/*
                我们现在作为controller 需要为ajax请求提供多项信息
                可以有两种手段处理
                    1.将多项信息打包成为map，将map解析为json串
                    2.创建一个Vo
                        private boolean success;
                        private String msg;
                   如果对于展现的信息将来还会大量的使用 我们创建一个vo类，使用方便
                   如果对于展现的信息只有这个需求中能够使用，使用map就可以了
             *//*

            Map<String,Object> map=new HashMap<String,Object>();
            map.put("success",false);
            map.put("msg",msg);
            PrintJson.printJsonObj(response,map);

        }
    }

}
*/
