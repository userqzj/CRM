package com.jlju.settings.test;

import com.jlju.crm.utils.DateTimeUtil;
import com.jlju.crm.utils.MD5Util;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Test1 {
    public static void main(String[] args) {

        //验证失效时间
        //失效时间
     /*   String expireTime="2022-10-10 10:10:10";
        //当前系统时间
        String currentTime= DateTimeUtil.getSysTime();
        // >0没失效 <0失效
        int count=expireTime.compareTo(currentTime);
        System.out.println(count);*/

        /*String lockState="0";
        if ("0".equals(lockState)){
            System.out.println("账号已锁定");
        }*/

        //浏览器端的ip地址
        /*String ip="192.168.1.1";
        //允许访问的ip地址群
        String allowIps="192.168.1.1,192.168.1.2";
        if (allowIps.contains(ip)){
            System.out.println("有效的ip地址，允许访问系统");
        }else {
            System.out.println("ip地址受限,请联系管理员");
        }*/

        String pwd="abc";
        pwd=MD5Util.getMD5(pwd);
        System.out.println(pwd);
        System.out.println("中文");
    }
}
