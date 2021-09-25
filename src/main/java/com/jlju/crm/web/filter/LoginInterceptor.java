package com.jlju.crm.web.filter;


import com.jlju.crm.settings.domain.User;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;


public class LoginInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        System.out.println("进入到验证有没有登录过的拦截器");
        String path = request.getServletPath();

        // 不应该被拦截的资源,自动放行请求
        if ("/settings/user/login.do".equals(path)) {
            return true;
        } else {
            HttpSession session = request.getSession();
            User user = (User) session.getAttribute("user");
            // 如果user不为null,说明登录过
            if (user != null) {
                System.out.println("mvc通过");
                return true;
                // 没有登录过
            } else {
                // 重定向到登录页
                System.out.println("mvc不通过");
                response.sendRedirect(request.getContextPath() + "/login.jsp");
                return false;
            }
        }
    }
}