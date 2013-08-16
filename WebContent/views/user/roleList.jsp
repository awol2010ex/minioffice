<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%
	String contextPath = request.getContextPath();
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">



<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>角色列表</title>
<jsp:include  page="../../css.jsp"  flush="true" />
<script type='text/javascript' src='<%=request.getContextPath() %>/dwr/interface/RoleDwr.js?rand=<%=new java.util.Date().getTime() %>'></script>
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
	
	

	//用户列表
	g=$("#grid").ligerGrid({
		toolbar:{
			
			items:[
			   {text:"新建",click:function(){
				  if(!_win)
			      {
				   _win =$.ligerDialog.open({title:"新建用户", target: $("#target") });
			      }
				   else
				  {
				   _win.show();
				  }
				     $("#_rolename").val("");
				     $("#_id").val("");		 
			    	 $("#_delflag").val("n");
			   }}       
			]
		},
        columns: [ 
              
              { display: 'ID', name: 'id', width: "15%",isAllowHide: true },
              { display: '角色名', name: 'rolename', width: "15%",isAllowHide: true },
              { display: '删除标记', name: 'delflag', width: "15%",isAllowHide: true }
        ],
        url: "<%=contextPath%>/restful/role/list/",
        sortName: 'id',
        showTitle: false,
        dataAction:'server',
        pageSize: 5,
        height:"90%",
        enabledEdit: true,
        dateFormat:'yyyy-MM-dd hh:mm:ss',
        pageSizeOptions: [5, 10, 15],
        onDblClickRow:function(_data,_id,_row){
        	UserDwr.getUser(
       	    	 _data.userid,
       	         function(ret){
       	    		 if(!_win)
   			      {
   				   _win =$.ligerDialog.open({ title:"修改用户" ,target: $("#target") });
   			      }
   				   else
   				  {
   				   _win.show();
   				  }
       	    		$("#_rolename").val("");
				     $("#_id").val("");		 
			    	 $("#_delflag").val("");
       	    		 
			    	 
			    	 $("#_rolename").val(ret.rolename);
				     $("#_id").val(ret.id);		 
			    	 $("#_delflag").val(ret.delflag);
       	         }
       	    );
        }

    });
	
	//保存用户
	$("#buttonSave").click(function(){
		
		if($("#_id").val() ==""){
	    RoleDwr.saveRole(		 
	    	 $("#_rolename").val(),
	    	 $("#_delflag").val(),
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
		}else{
			RoleDwr.updateRole(
					$("#_id").val(),		 
			    	 $("#_rolename").val(),
			    	 $("#_delflag").val(),
			         function(ret){
			             if(ret.result){
			                alert("更新成功");
			                refresh();
			                if(_win){
			                	_win.hide();
			                }
			             }else{
			                alert("更新失败:"+ret.msg);
			             }
			         }
			    );
			
		} 
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



<div id="target" style="width:200px; margin:3px; display:none;" title="用户信息">
     <table width="100%">
        <tr><td>角色名称</td></tr><tr><td><input type='text'   id="_rolename"/></td></tr>
        
        
        <tr><td><input   type="button"  id="buttonSave"  value="保存"/></td></tr>
     </table>
     
     <input type='hidden'   id="_id"/>
      <input type='hidden'   id="_delflag"/>
</div>

</body>
</html>