package com.jlju.crm.workbench.web.controller;

import com.jlju.crm.settings.domain.User;
import com.jlju.crm.settings.service.UserService;
import com.jlju.crm.utils.DateTimeUtil;
import com.jlju.crm.utils.UUIDUtil;
import com.jlju.crm.workbench.domain.Activity;
import com.jlju.crm.workbench.domain.Clue;
import com.jlju.crm.workbench.domain.Tran;
import com.jlju.crm.workbench.service.ActivityService;
import com.jlju.crm.workbench.service.ClueService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/workbench/clue")
public class ClueController {
    //未来业务层开发，统一使用代理类形态接口对象

    @Resource
    private UserService us;
    @Resource
    private ClueService cs;

    @Resource
    private ActivityService as;

    @RequestMapping("/getUserList.do")
    @ResponseBody
    private List<User> getUserList() {
        System.out.println("进入到线索控制器");

        System.out.println("取得用户信息列表");

        List<User> uList = us.getUserList();

        return uList;
    }

    @RequestMapping("/save.do")
    @ResponseBody
    private boolean save(HttpSession session, Clue clue) {
        System.out.println("进入到线索控制器");
        System.out.println("执行线索添加操作");

        String id = UUIDUtil.getUUID();

        // 创建时间: 当前系统时间
        String createTime = DateTimeUtil.getSysTime();
        //创建人：当前登录用户
        String createBy = ((User) session.getAttribute("user")).getName();

        clue.setId(id);
        clue.setCreateBy(createBy);
        clue.setCreateTime(createTime);

        boolean flag = cs.save(clue);

        return flag;
    }

    @RequestMapping("/detail.do")
    private ModelAndView detail(String id) {
        System.out.println("进入到线索控制器");

        System.out.println("跳转到线索的详细信息页");

        Clue clue = cs.detail(id);

        ModelAndView mv = new ModelAndView();

        mv.addObject("c", clue);

        mv.setViewName("forward:/workbench/clue/detail.jsp");

        return mv;

    }

    @RequestMapping("/getActivityListByClueId.do")
    @ResponseBody
    private List<Activity> getActivityListByClueId(String clueId) {
        System.out.println("进入到线索控制器");

        System.out.println("根据线索id查询关联的市场活动列表");

        List<Activity> aList = as.getActivityListByClueId(clueId);

        return aList;
    }

    @RequestMapping("/unbund")
    @ResponseBody
    private boolean unbund(String id) {
        System.out.println("进入到线索控制器");

        System.out.println("执行解除关联操作");

        boolean flag = cs.unbund(id);

        return flag;

    }

    @RequestMapping("/getActivityListByNameAndNotByClueId.do")
    @ResponseBody
    private List<Activity> getActivityListByNameAndNotByClueId(String aname, String clueId) {

        System.out.println("进入到线索控制器");

        System.out.println("查询市场活动列表(根据名称模糊查+排除掉已经关联指定线索的列表)");

        Map<String, String> map = new HashMap<String, String>();
        map.put("aname", aname);
        map.put("clueId", clueId);

        List<Activity> aList = as.getActivityListByNameAndNotByClueId(map);

        return aList;
    }

    @RequestMapping("/bund.do")
    @ResponseBody
    private boolean bund(HttpServletRequest req, HttpServletResponse resp) {
        System.out.println("进入到线索控制器");
        System.out.println("执行关联市场活动的操作");

        String cid = req.getParameter("cid");
        String[] aids = req.getParameterValues("aid");

        boolean flag = cs.bund(cid, aids);
        return flag;
    }

    @RequestMapping("/getActivityListByName.do")
    @ResponseBody
    private List<Activity> getActivityListByName(String aname) {

        System.out.println("进入到线索控制器");

        System.out.println("查询市场活动列表(根据名称模糊查询)");

        List<Activity> aList = as.getActivityListByName(aname);

        return aList;
    }

    @RequestMapping("/convert.do")
    private void convert(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        System.out.println("进入到线索控制器");

        System.out.println("执行线索转换操作");

        // 接收是否需要创建交易的标记
        String flag = req.getParameter("flag");
        String clueId = req.getParameter("clueId");
        //创建人：当前登录用户
        String createBy = ((User) req.getSession().getAttribute("user")).getName();

        Tran t = null;

        //是否需要创建交易的标记flag
        if ("a".equals(flag)) {
            // 接收交易表单中的参数
            String money = req.getParameter("money");
            String name = req.getParameter("name");
            String expectedDate = req.getParameter("expectedDate");
            String stage = req.getParameter("stage");
            String activityId = req.getParameter("activityId");

            String id = UUIDUtil.getUUID();
            // 创建时间: 当前系统时间
            String createTime = DateTimeUtil.getSysTime();


            t = new Tran();

            t.setId(id);
            t.setMoney(money);
            t.setName(name);
            t.setExpectedDate(expectedDate);
            t.setStage(stage);
            t.setActivityId(activityId);
            t.setCreateTime(createTime);
            t.setCreateBy(createBy);
        }

        /**
         * 为业务层传递的参数
         * 1.必须传递的参数clueId 有了这个clueId我们才知道要转换哪条记录
         * 2.必须传递的参数t 因为在线索转换的过程中 有可能临时创建一笔交易（业务层接受的t也有可能是null）
         */

        boolean flag1 = cs.convert(clueId, t, createBy);

        if (flag1) {
            resp.sendRedirect(req.getContextPath() + "/workbench/clue/index.jsp");
        }

    }

    @RequestMapping("/pageList.do")
    @ResponseBody
    public Map<String,Object> pageList(Clue clue,Integer pageNo,Integer pageSize){
        System.out.println("=================================");
        String fullname = clue.getFullname();
        String company = clue.getCompany();
        String phone = clue.getPhone();
        String source = clue.getSource();
        String owner = clue.getOwner();
        String mphone = clue.getMphone();
        String state = clue.getState();

        int skipCount = (pageNo-1) * pageSize;
        Map<String,Object> map = new HashMap<>();
        map.put("skipCount",skipCount);
        map.put("pageSize",pageSize);
        map.put("fullname",fullname);
        map.put("company",company);
        map.put("phone",phone);
        map.put("source",source);
        map.put("owner",owner);
        map.put("mphone",mphone);
        map.put("state",state);
        Map<String,Object> cMap = cs.pageList(map);
        return cMap;
    }
}
