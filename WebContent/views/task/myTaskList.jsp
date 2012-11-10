<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%
	String contextPath = request.getContextPath();
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">



<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title></title>
<script type='text/javascript' src='<%=request.getContextPath() %>/dwr/interface/TaskDwr.js'></script>
<script type='text/javascript' src='<%=request.getContextPath() %>/dwr/engine.js'></script>
<jsp:include  page="../../css.jsp"  flush="true" />
<style type="text/css">
body {
	font-size: 12px;
}
</style>	
<script type='text/javascript'>
var g_assignee=null;//分派任务列表

var g_history=null;//已归档任务列表


//我的任务列表
$(function() {
	$("body").ligerLayout( {});
	
	

	//被分派任务
	g_assignee=$("#grid_assignee").ligerGrid({
        columns: [ 
              { display: 'id', name: 'id', width: "15%",isAllowHide: true },
              { display: '任务创建时间', name: 'createTime', width: "15%",isAllowHide: true ,type:'date'},
              { display: '处理时间', name: 'dueDate', width: "15%",isAllowHide: true,type:'date' },
              { display: '任务名称', name: 'name', width: "15%",isAllowHide: true },
              { display: '流程定义名称', name: 'processDefinitionName', width: "15%",isAllowHide: true }
        ],
        url: "<%=contextPath%>/restful/task/mytask/assignee/list/",
        sortName: 'id',
        showTitle: false,
        dataAction:'server',
        pageSize: 5,
        height:"90%",
        enabledEdit: true,
        dateFormat:'yyyy-MM-dd hh:mm:ss',
        pageSizeOptions: [5, 10, 15],
        detail: { 
        	//显示明细
        	onShowDetail: function(row, detailPanel,callback){
               
                
                
                var table =  $("<table width='100%'/>").appendTo(  $(detailPanel) );
                var tr =  $("<tr/>").appendTo(  $(table) );
                var td =  $("<td align='center'/>").appendTo(  $(tr) );
                var img = $("<img src='<%=contextPath%>/static/skins/Aqua/images/common/loading.gif'/>").appendTo(  $(td) );
                
                img.attr("src" ,"<%=contextPath%>/restful/process/processInstance/diagram/"+row.processInstanceId+"/?rand="+new Date().getTime());//显示流程图
                
                var tr =  $("<tr/>").appendTo(  $(table) );
                var td =  $("<td align='center'/>").appendTo(  $(tr) );
                var button = $("<button>审批任务</button>").appendTo(  $(td) );
                button.data("row",row);
                button.click(function(){
                	var _row =$(this).data("row"); 
                	//审批任务流程
                	TaskDwr.commitTask(_row.id ,function(result){
               		   if(result){
                         alert("审批成功");
                       }else{
                         alert("审批失败");
                       }
                	});
                });
                
                
        	}
        	
        }

    });
	 
	 
	 
	//已归档任务
	g_history=$("#grid_history").ligerGrid({
        columns: [ 
              { display: '序号', name: 'id', width: "15%",isAllowHide: true },
              
              { display: '开始时间', name: 'startTime', width: "15%",isAllowHide: true,type:'date' },
              { display: '完成时间', name: 'endTime', width: "15%",isAllowHide: true ,type:'date'},
              
              { display: '任务名称', name: 'name', width: "15%",isAllowHide: true },
              { display: '流程定义名称', name: 'processDefinitionName', width: "15%",isAllowHide: true }
        ],
        url: "<%=contextPath%>/restful/task/mytask/history/list/",
        sortName: 'id',
        showTitle: false,
        dataAction:'server',
        pageSize: 5,
        height:"90%",
        enabledEdit: true,
        dateFormat:'yyyy-MM-dd hh:mm:ss',
        pageSizeOptions: [5, 10, 15],
        detail: { 
        	//显示明细
        	onShowDetail: function(row, detailPanel,callback){
               
                
                
                var table =  $("<table width='100%'/>").appendTo(  $(detailPanel) );
                var tr =  $("<tr/>").appendTo(  $(table) );
                var td =  $("<td align='center'/>").appendTo(  $(tr) );
                var img = $("<img src='<%=contextPath%>/static/skins/Aqua/images/common/loading.gif'/>").appendTo(  $(td) );
                
                img.attr("src" ,"<%=contextPath%>/restful/process/processInstance/diagram/"+row.processInstanceId+"/?rand="+new Date().getTime());//显示流程图
                
                
                
        	}
        	
        }

    });
	
	
	 $("#navtab1").ligerTab(); //标签页
});
</script>
</head>
<body>

<div position="center">
   <div id="navtab1" style="width: 100%;overflow:hidden; border:1px solid #A3C0E8; ">
        <div  title="分派任务" showClose="false">
            <!-- 被分派任务 -->
            <div id="grid_assignee" style="width:99% ;height:90%;"></div>
        </div>
        <div  title="已归档任务" showClose="false">
            <!-- 已归档任务 -->
            <div id="grid_history" style="width:99% ;height:90%;"></div>
        </div>
   </div>
</div>


</body>
</html>