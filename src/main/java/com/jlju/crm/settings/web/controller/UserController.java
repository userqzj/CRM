package com.jlju.crm.settings.web.controller;

import com.jlju.crm.exception.LoginException;
import com.jlju.crm.settings.domain.User;
import com.jlju.crm.settings.service.UserService;
import com.jlju.crm.utils.MD5Util;
import com.jlju.crm.utils.PrintJson;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/settings/user")
public class UserController {
    @Resource
    //未来业务层开发，统一使用代理类形态接口对象
    private UserService userService;

    @RequestMapping("/login.do")
    @ResponseBody
    public Map<String, Object> login(HttpServletRequest request, HttpServletResponse response) throws LoginException {
        System.out.println("进入验证登录操作");

        String loginAct = request.getParameter("loginAct");
        String loginPwd = request.getParameter("loginPwd");
        //将密码的明文形式转换为MD5密文形式
        loginPwd = MD5Util.getMD5(loginPwd);
        //接受浏览器端的ip地址
        String ip = request.getRemoteAddr();
        System.out.println("ip-----" + ip);

        Map<String, Object> map = new HashMap<String, Object>();
        User user=null;
        try {
            user = userService.login(loginAct, loginPwd, ip);
            //如果程序执行到此处，说明业务层没有为controller抛出任何异常  表示登录成功
            map.put("success", true);
            request.getSession().setAttribute("user", user);
        }catch (Exception e){
            map.put("success", false);
            map.put("msg",e.getMessage());
        }

        return map;


    }
}
