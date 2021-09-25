package com.jlju.crm.workbench.web.controller;

import com.jlju.crm.settings.domain.User;
import com.jlju.crm.settings.service.UserService;
import com.jlju.crm.utils.DateTimeUtil;
import com.jlju.crm.utils.UUIDUtil;
import com.jlju.crm.vo.PaginationVO;
import com.jlju.crm.workbench.domain.Activity;
import com.jlju.crm.workbench.domain.ActivityRemark;
import com.jlju.crm.workbench.service.ActivityService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/workbench/activity")
public class ActivityController {
    //未来业务层开发，统一使用代理类形态接口对象
    @Resource
    private ActivityService as;

    @Resource
    private UserService us;

    @RequestMapping("/getUserList.do")
    @ResponseBody
    private List<User> getUserList(){
        System.out.println("取得用户信息列表");
        List<User> user=us.getUserList();

        return user;
    }

    @RequestMapping("/save.do")
    @ResponseBody
    private boolean save(HttpSession session, Activity activity){
        System.out.println("执行市场活动添加操作");
        String id= UUIDUtil.getUUID();

        // 创建时间: 当前系统时间
        String createTime= DateTimeUtil.getSysTime();
        //创建人：当前登录用户
        String createBy=((User)session.getAttribute("user")).getName();

        activity.setId(id);
        activity.setCreateBy(createBy);
        activity.setCreateTime(createTime);
        boolean flag=as.save(activity);

        return flag;
    }

    @RequestMapping("/pageList.do")
    @ResponseBody
    private PaginationVO<Activity> pageList(Activity activity, Integer pageSize, Integer pageNo){
        System.out.println("进入到查询市场活动信息列表的操作(结合条件查询,分页查询)");

        String name=activity.getName();
        String owner=activity.getOwner();
        String startDate=activity.getStartDate();
        String endDate=activity.getEndDate();

        //计算出略过的记录数
        int skipCount=(pageNo-1)*pageSize;

        Map<String,Object> map=new HashMap<String,Object>();
        map.put("name",name);
        map.put("owner",owner);
        map.put("startDate",startDate);
        map.put("endDate",endDate);

        // 以下两条信息不在domain类中,所以选择使用map进行传值(<parameterType>传值不能使用vo类,<resultType>传值可以使用vo类)
        map.put("skipCount",skipCount);
        map.put("pageSize",pageSize);
        /*
            前段要：市场活动信息列表
                    查询的总条数

                    业务层拿到了以上两项信息之后，如何做返回
                    map
                    map.put("dataList":dataList)
                    map.put("total":total)
                    转JSON

                    vo
                    PaginationVo<T>
                        private int total;
                        private List<T> dataList;

                     PaginationVO<Activity> vo=new PaginationVO<>;
                     vo.setTotal(total);
                     vo.setDataList(dataList);
                     转JSON
                    将来分页查询，每个模块都有，所以选择使用一个通用vo操作起来比较方便
         */
        PaginationVO<Activity> vo=as.pageList(map);
        return vo;
    }

    @RequestMapping("/delete.do")
    @ResponseBody
    private boolean delete(@RequestParam(value = "id") String[] ids){
        System.out.println("执行市场活动的删除操作");
        boolean flag=as.delete(ids);
        return flag;
    }

    @RequestMapping("/getUserListAndActivity.do")
    @ResponseBody
    private Map<String,Object> getUserListAndActivity(String id){
        System.out.println("进入到查询用户信息列表和根据市场活动id查询单条记录的操作");
        /**
         * 总结:
         *      controller调用service的方法,返回值是什么
         *      你得想一想前端要什么,就要从service层取什么
         *
         *  前端需要的,管业务层去要
         * uList
         * a
         *
         * 以上两项信息,复用率不高,我们选择使用map打包这两项信息即可
         * map
         */
        Map<String,Object> map=as.getUserListAndActivity(id);
        return map;
    }

    @RequestMapping("/update.do")
    @ResponseBody
    private boolean update(HttpSession session,Activity activity){
        System.out.println("执行市场活动修改操作");

        // 创建时间: 当前系统时间
        String editTime= DateTimeUtil.getSysTime();
        //创建人：当前登录用户
        String editBy=((User)session.getAttribute("user")).getName();

        activity.setCreateBy(editBy);
        activity.setCreateTime(editTime);
        boolean flag=as.update(activity);

        return flag;
    }

    @RequestMapping("/detail.do")
    private ModelAndView detail(String id){
        System.out.println("进入到跳转到详细信息页的操作");
        Activity activity=as.detail(id);

        ModelAndView mv=new ModelAndView();
        mv.addObject(activity);
        mv.setViewName("forward:/workbench/activity/detail.jsp");
        return mv;
    }

    @RequestMapping("/getRemarkListByAid.do")
    @ResponseBody
    private List<ActivityRemark> getRemarkListByAid(String activityId){

        System.out.println("根据市场活动id,取得备注信息列表");

        List<ActivityRemark> arList=as.getRemarkListByAid(activityId);
        return arList;
    }

    @RequestMapping("/deleteRemark.do")
    @ResponseBody
    private boolean deleteRemark(String id){
        System.out.println("删除备注操作");

        boolean flag=as.deleteRemark(id);
        return  flag;
    }

    @RequestMapping("/saveRemark.do")
    @ResponseBody
    private Map<String,Object> saveRemark(HttpSession session,ActivityRemark ar,String noteContent,String activityId){
        System.out.println("执行添加备注操作");

        String id=UUIDUtil.getUUID();
        String createTime = DateTimeUtil.getSysTime();
        String createBy = ((User) session.getAttribute("user")).getName();
        String editFlag = "0";

        ar.setId(id);
        //ar.setNoteContent(noteContent);
        //ar.setActivityId(activityId);
        ar.setCreateBy(createBy);
        ar.setCreateTime(createTime);
        ar.setEditFlag(editFlag);

        boolean flag=as.saveRemark(ar);

        Map<String,Object> map=new HashMap<String, Object>();
        map.put("success",flag);
        map.put("ar",ar);

        return map;
    }

    @RequestMapping("/updateRemark.do")
    @ResponseBody
    private Map<String,Object> saveRemark(HttpSession session,ActivityRemark ar){
        System.out.println("执行修改备注的操作");

        String editTime = DateTimeUtil.getSysTime();
        String editBy = ((User) session.getAttribute("user")).getName();
        String editFlag = "1";

        ar.setEditTime(editTime);
        ar.setEditBy(editBy);
        ar.setEditFlag(editFlag);

        boolean flag=as.updateRemark(ar);

        Map<String,Object> map=new HashMap<String,Object>();
        map.put("success",flag);
        map.put("ar",ar);
        return map;
    }

}
