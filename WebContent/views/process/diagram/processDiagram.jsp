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

<script type='text/javascript' src='<%=request.getContextPath() %>/dwr/interface/ActivityDwr.js'></script>
<script type='text/javascript' src='<%=request.getContextPath() %>/dwr/engine.js'></script>
<script type='text/javascript' src='<%=request.getContextPath() %>/static/scripts/activiti/ProcessDiagram.js'></script>
<style type="text/css">
body {
	font-size: 12px;
}
</style>	
<script type='text/javascript'>
//流程图显示页面
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
    	}
    });//显示流程图
});
</script>
</head>
<body>

</body>
</html>