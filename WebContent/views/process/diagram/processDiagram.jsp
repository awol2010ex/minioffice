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
<jsp:include  page="../../../css.jsp"  flush="true" />

<script type='text/javascript' src='<%=request.getContextPath() %>/dwr/interface/ActivityDwr.js?rand=<%=new java.util.Date().getTime() %>'></script>
<script type='text/javascript' src='<%=request.getContextPath() %>/dwr/interface/TaskDwr.js?rand=<%=new java.util.Date().getTime() %>'></script>
<script type='text/javascript' src='<%=request.getContextPath() %>/dwr/engine.js'></script>
<script type='text/javascript' src='<%=request.getContextPath() %>/static/scripts/activiti/ProcessDiagram.js?rand=<%=new java.util.Date().getTime() %>'></script>
<style type="text/css">
body {
	font-size: 12px;
}
</style>	
<script type='text/javascript'>
//流程图显示页面
var  activity_menu =null;
$(function() {
	
	
	
	$("body").ProcessDiagram({
    	title:"流程图",
    	processInstanceId:"<%=request.getParameter("processInstanceId") %>",
    	processDefinitionId:"<%=request.getParameter("processDefinitionId") %>",
    	offsetTop:5,
    	offsetLeft:5,
    	afterLoadDiagram:function(){
    		//自适应大小
    		var _frame=parent.$("#<%=request.getParameter("frameId")%>");
    		_frame.height(document.body.scrollHeight+"px");
    		_frame.width(document.body.scrollWidth+"px");
    		if(parent.afterLoadDiagram){
    			parent.afterLoadDiagram("<%=request.getParameter("frameId")%>");
    		}
    	},
    	contextMenuNode:function(e){//右键菜单
    		  var activityId  =$(this).data("activityId");
    	      activity_menu.show({ top: e.pageY, left: e.pageX });
    	      $(activity_menu.element).css("z-index", "90000").data("activityId",activityId);
    	      return false;
    	},
    	clickNode:function(e){//单击节点
    		 var activityId  =$(this).data("activityId");
    	     alert(activityId);
  	   }
    });//显示流程图
    
    //节点右键菜单
	activity_menu= $.ligerMenu({
		top : 100,
		left : 100,
		width : 120,
		items : [ 
		  {
			text : '驳回',
			click : function(){
				var  activityId =$(activity_menu.element).data("activityId");//环节ID
				var  taskId =null;//当前任务ID
				<%if(request.getParameter("taskId")!=null){%>
				     taskId  ="<%=request.getParameter("taskId")%>";
				<%}%>
				
				if(taskId){
					  //驳回
					  TaskDwr.rejectTask(taskId ,activityId ,function(result){
						  if(result){
					          alert("驳回成功");
					      }else{
					          alert("驳回失败");
					      }
					  });
				}
			}
		  }

		]
	});
});
</script>
</head>
<body>

</body>
</html>