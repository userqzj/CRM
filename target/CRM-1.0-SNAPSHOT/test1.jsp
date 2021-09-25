<%--
  Created by IntelliJ IDEA.
  User: Administrator
  Date: 2021-06-27
  Time: 14:34
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    String basePath = request.getScheme() + "://" +
            request.getServerName() + ":" + request.getServerPort() +
            request.getContextPath() + "/";
%>
<html>
<head>
    <base href="<%=basePath%>"/>
    <title>Title</title>
</head>
<body>

        $.ajax({
        url:"",
        data:{

        },
        type:"",
        dataType:"json",
        success:function (data) {

        }
        })

        // 创建时间: 当前系统时间
        String createTime= DateTimeUtil.getSysTime();
        //创建人：当前登录用户
        String createBy=((User)session.getAttribute("user")).getName();

        $(".time").datetimepicker({
                minView: "month",
                language:  'zh-CN',
                format: 'yyyy-mm-dd',
                autoclose: true,
                todayBtn: true,
                pickerPosition: "bottom-left"
        });
</body>
</html>
