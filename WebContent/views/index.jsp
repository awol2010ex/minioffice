<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%
	String contextPath = request.getContextPath();

    String error=(String)request.getAttribute("error");
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">



<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title></title>

<style type="text/css">
body {
	font-size: 12px;
}

.l-table-edit {
	
}

.l-table-edit-td {
	padding: 4px;
}

.l-button-submit,.l-button-test {
	width: 80px;
	float: left;
	margin-left: 10px;
	padding-bottom: 2px;
}

.l-verify-tip {
	left: 230px;
	top: 120px;
}
</style>

<script type='text/javascript'>
//初始页面
function init(){
	
	<%if(error!=null&&!"".equals(error)){%>
	   alert("<%=error%>");
	<%}%>
}

//登陆
function login(form){
	 if   (event.keyCode   ==   13)   { 
	       document.getElementById("form1").submit();
	 }
}

</script>
</head>
<body onload="init()">
<form name="form1" method="post" id="form1" action="<%=contextPath %>/restful/user/login/">
<center>
<table cellpadding="0" cellspacing="0" class="l-table-edit">

	<tr>

		<td align="right" class="l-table-edit-td" width="30%">用户名:</td>

		<td class="l-table-edit-td"><input name="j_username"
			type="text" id="j_username" ltype="text"   onkeypress= "login()" /></td>


	</tr>
	<tr>

		<td align="right" class="l-table-edit-td" >密码:</td>

		<td  class="l-table-edit-td"><input name="j_password"
			 id="j_password" ltype="password"  type="password" onkeypress= "login()"/></td>


	</tr>
</table>
</center>
</form>
</body>
</html>