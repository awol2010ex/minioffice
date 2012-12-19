<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%
	String contextPath = request.getContextPath();
    
    String processInstanceId =request.getParameter("processInstanceId");//流程实例ID
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">



<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title></title>

<jsp:include  page="../../css.jsp"  flush="true" />




<style type="text/css">
body {
	font-size: 12px;
}
</style>	
<script type='text/javascript'>
var g_history=null;//分派任务列表



//当前流程的任务列表
$(function() {
	$("body").ligerLayout( {});
	
	

	 
	//已归档任务
	g_history=$("#grid_history").ligerGrid({
        columns: [ 
              { display: '序号', name: 'id', width: "15%",isAllowHide: true },
              { display: '分派人', name: 'assignee', width: "15%",isAllowHide: true },
              { display: '开始时间', name: 'startTime', width: "15%",isAllowHide: true,type:'date' },
              { display: '完成时间', name: 'endTime', width: "15%",isAllowHide: true ,type:'date'},
              
              { display: '任务名称', name: 'name', width: "15%",isAllowHide: true },
              { display: '结束原因', name: 'deleteReason', width: "15%",isAllowHide: true }
              
              
        ],
        url: "<%=contextPath%>/restful/task/process/history/list/?processInstanceId=<%=processInstanceId%>",
        sortName: 'id',
        showTitle: false,
        dataAction:'server',
        pageSize: 5,
        height:"90%",
        enabledEdit: false,
        dateFormat:'yyyy-MM-dd hh:mm:ss',
        pageSizeOptions: [5, 10, 15]
    });
	
	 
	
});

</script>
</head>
<body>

<div position="center">
            <div id="grid_history" style="width:99% ;height:90%;"></div>
</div>


</body>
</html>