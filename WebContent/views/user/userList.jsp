<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%
	String contextPath = request.getContextPath();
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">



<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>用户列表</title>
<jsp:include  page="../../css.jsp"  flush="true" />
<script type='text/javascript' src='<%=request.getContextPath() %>/dwr/interface/UserDwr.js?rand=<%=new java.util.Date().getTime() %>'></script>
<script type='text/javascript' src='<%=request.getContextPath() %>/dwr/engine.js'></script>

<style type="text/css">
body {
	font-size: 12px;
}
</style>	
<script type='text/javascript'>
var g=null;//分派任务列表

var _win;

//我的任务列表
$(function() {
	$("body").ligerLayout( {});
	
	

	//被分派任务
	g=$("#grid").ligerGrid({
		toolbar:{
			
			items:[
			   {text:"新建",click:function(){
				  if(!_win)
			      {
				   _win =$.ligerDialog.open({ target: $("#target") });
			      }
				   else
				  {
				   _win.show();
				  }
				     $("#_loginid").val("");		 
			    	 $("#_username").val("");
			    	 $("#_password").val("");
			         $("#_email").val("");
			   }}       
			]
		},
        columns: [ 
              
              { display: 'ID', name: 'id', width: "15%",isAllowHide: true },
              { display: '登陆名', name: 'loginid', width: "15%",isAllowHide: true },
              { display: '名称', name: 'alias', width: "15%",isAllowHide: true },
              { display: 'Email', name: 'email', width: "15%",isAllowHide: true }
        ],
        url: "<%=contextPath%>/restful/user/list/",
        sortName: 'id',
        showTitle: false,
        dataAction:'server',
        pageSize: 5,
        height:"90%",
        enabledEdit: true,
        dateFormat:'yyyy-MM-dd hh:mm:ss',
        pageSizeOptions: [5, 10, 15]

    });
	
	//保存用户
	$("#buttonSave").click(function(){
	    UserDwr.saveUser(
	    	 $("#_loginid").val(),		 
	    	 $("#_username").val(),
	    	 $("#_password").val(),
	         $("#_email").val(),
	         function(ret){
	             if(ret.result){
	                alert("保存成功");
	                refresh();
	                if(_win){
	                	_win.hide();
	                }
	             }else{
	                alert("保存失败:"+ret.msg);
	             }
	         }
	    );
	    
	});
});


function refresh(){
	$("#grid").ligerGetGridManager().loadData();
}
  
</script>
</head>
<body>

<div position="center">
            <div id="grid" style="width:99% ;height:90%;"></div>
        
</div>



<div id="target" style="width:200px; margin:3px; display:none;">
     <table width="100%">
        <tr><td>登陆名</td></tr><tr><td><input type='text'   id="_loginid"/></td></tr>
        <tr><td>用户名称</td></tr><tr><td><input type='text'   id="_username"/></td></tr>
        <tr><td>密码</td></tr><tr><td><input type='password' id="_password"/></td></tr>
        <tr><td>邮箱</td></tr><tr><td><input type='text'  id="_email" /></td></tr>
        
        
        <tr><td><input   type="button"  id="buttonSave"  value="保存"/></td></tr>
     </table>
</div>

</body>
</html>