package com.jlju.crm.web.listener;

import com.jlju.crm.settings.domain.DicValue;
import com.jlju.crm.settings.service.DicService;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.util.*;

public class SysInitListener implements ServletContextListener {
    /**
     * 该方法是用来监听上下文域对象的方法，当服务器启动，上下文域对象创建
     * 对象创建完毕后，马上执行该方法
     *      servletContextEvent 该参数能够取得监听的对象
     *        监听的是什么对象，就可以通过该参数取得什么对象
     *        例如我们现在监听的是上下文域对象，通过该参数就可以取得上下文域对象
     */

    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {

        System.out.println("服务器缓存处理数据字典开始");
        //System.out.println("上下文域对象创建了");
        ServletContext application=servletContextEvent.getServletContext();
        //取数据字典
        //application.setAttribute();
        DicService ds=WebApplicationContextUtils.getWebApplicationContext(servletContextEvent.getServletContext()).getBean(DicService.class);

        /**
         * 应该管业务层要什么
         *  7个List
         *
         *  可以打包称为一个map
         *  业务层应该是这样来保存数据的:
         *      map.put("appellationList",dvList1);
         *      map.put("clueStateList",dvList2);
         *      map.put("stageList",dvList3);
         */

        Map<String, List<DicValue>> map=ds.getAll();

        // 将map解析为上下文域对象中保存的键值对
        Set<String> set=map.keySet();
        for (String key : set) {
            application.setAttribute(key,map.get(key));
        }
        System.out.println("服务器缓存处理数据字典结束");


        //-------------------------------------------------

        //数据字典处理完毕后 处理stage2Possibility.properties文件
        /**
         *  处理stage2Possibility.properties文件步骤
         *      解析该文件，将该属性文件中的键值对关系处理成为java中键值对关系(map)
         *
         *      Map<String(阶段stage),String(可能性possibility)> pMap = ....
         *      pMap.put("01资质审查",10);
         *      pMap.put("02需求分析",25);
         *      .....
         *      pMap.put("07成交",100);
         *
         *      pMap保存值之后,放在服务器缓存中
         *      application.setAttribute("pMap",pMap);
         */

        //解析stage2Possibility.properties文件
        Map<String,String> pMap=new HashMap<String,String>();
        ResourceBundle rb=ResourceBundle.getBundle("Stage2Possibility");



        //迭代器比foreach快

        Enumeration<String> e=rb.getKeys();
        while (e.hasMoreElements()){
            //阶段
            String key=e.nextElement();
            //可能性
            String value=rb.getString(key);
            pMap.put(key,value);

        }

        //将pMap保存到服务器缓存中
        application.setAttribute("pMap",pMap);

    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {
        System.out.println("上下文域对象销毁了");
    }
}
