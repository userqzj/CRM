package com.jlju.crm.workbench.web.controller;

import com.jlju.crm.settings.domain.User;
import com.jlju.crm.settings.service.UserService;
import com.jlju.crm.utils.DateTimeUtil;
import com.jlju.crm.utils.UUIDUtil;
import com.jlju.crm.workbench.domain.Tran;
import com.jlju.crm.workbench.domain.TranHistory;
import com.jlju.crm.workbench.service.CustomerService;
import com.jlju.crm.workbench.service.TranService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/workbench/transaction")
public class TranController {
    @Resource
    private TranService ts;
    @Resource
    private UserService us;
    @Resource
    private CustomerService cs;

    @RequestMapping("/getChars.do")
    @ResponseBody
    private Map<String,Object> getCharts(){
        System.out.println("取得交易阶段数量统计图标的数据");

        /**
         * 业务层为我们返回
         *  total
         *  dataList
         *
         *  通过map打包以上两项进行返回
         */
        Map<String,Object> map=ts.getCharts();

        return map;

    }

    @RequestMapping("/changeStage.do")
    @ResponseBody
    private Map<String,Object> changeStage(Tran t,HttpServletRequest req){
        // 创建时间: 当前系统时间
        String editTime= DateTimeUtil.getSysTime();
        //创建人：当前登录用户
        String editBy=((User)req.getSession().getAttribute("user")).getName();

        t.setEditTime(editTime);
        t.setEditBy(editBy);

        boolean flag=ts.changeStage(t);

        Map<String,String> pMap = (Map<String, String>) req.getServletContext().getAttribute("pMap");
        String possibility = pMap.get(t.getStage());
        t.setPossibility(possibility);

        Map<String,Object> map=new HashMap<String, Object>();
        map.put("success",flag);
        map.put("t",t);

        return map;
    }

    @RequestMapping("/getHistoryListByTranId.do")
    @ResponseBody
    private List<TranHistory> getHistoryListByTranId(String tranId,HttpServletRequest req){
        System.out.println("根据交易id取得相应的历史列表");

        List<TranHistory> thList=ts.getHistoryListByTranId(tranId);

        //阶段和可能性之间的
        Map<String,String> pMap= (Map<String, String>) req.getServletContext().getAttribute("pMap");
        //将交易历史列表遍历
        for (TranHistory th : thList) {

            //根据每条交易历史，取出每一个阶段
            String stage=th.getStage();
            String possibility=pMap.get(stage);;
            th.setPossibility(possibility);

        }

        return thList;
    }

    @RequestMapping("/detail.do")
    private ModelAndView detail(String id, HttpServletRequest request){
        System.out.println("跳转到详细信息页");
        ModelAndView mv=new ModelAndView();

        Tran t=ts.detail(id);

        //处理可能性
        /*
            阶段 t
            阶段和可能性之间的对应关系 pMap
         */

        String stage=t.getStage();
        Map<String,String> pMap= (Map<String, String>) request.getServletContext().getAttribute("pMap");
        String possibility=pMap.get(stage);

        t.setPossibility(possibility);

        mv.addObject("t",t);
        //mv.addObject("possibility",possibility);
        mv.setViewName("forward:/workbench/transaction/detail.jsp");

        return mv;
    }

    @RequestMapping("/save.do")
    private ModelAndView save(@RequestParam(value = "customerName")String customerName,Tran t, HttpSession session){
        System.out.println("执行添加交易的操作");

        String id= UUIDUtil.getUUID();
        // 创建时间: 当前系统时间
        String createTime= DateTimeUtil.getSysTime();
        //创建人：当前登录用户
        String createBy=((User)session.getAttribute("user")).getName();
        t.setCreateTime(createTime);
        t.setId(id);
        t.setCreateBy(createBy);
        ModelAndView mv=new ModelAndView();

        boolean flag=ts.save(t,customerName);

        if (flag){
            //重定向
            mv.setViewName("redirect:/workbench/transaction/index.jsp");
        }

        return mv;
    }

    @RequestMapping("/add.do")
    private ModelAndView add(){

        System.out.println("进入到跳转到交易添加页的操作");
        List<User> uList=us.getUserList();

        ModelAndView mv=new ModelAndView();
        mv.addObject("uList",uList);
        mv.setViewName("forward:/workbench/transaction/save.jsp");

        return mv;

    }

    @RequestMapping("/getCustomerName.do")
    @ResponseBody
    private List<String> getCustomerName(String name){
        System.out.println("取得客户名称列表(按照客户的名称进行模糊查询)");

        List<String >slist=cs.getCustomerName(name);

        return slist;
    }
}
