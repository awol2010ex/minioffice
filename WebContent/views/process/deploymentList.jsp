<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%
	String contextPath = request.getContextPath();
    String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+contextPath+"/";
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
var g=null;
//部署流程模板列表
$(function() {
	$("body").ligerLayout( {topHeight:200});
	var g=$("#grid").ligerGrid({
        columns: [ 
              { display: '序号', name: 'id', width: "20%",isAllowHide: true },
              { display: '名称', name: 'name', width: "20%",isAllowHide: true },
              { display: '部署时间', name: 'deploymentTime', width: "20%",isAllowHide: true  ,type:'date'}
             
        ],
        url: "<%=contextPath%>/restful/process/deployment/list/",
        sortName: 'id',
        showTitle: false,
        dataAction:'server',
        pageSize: 5,
        height:"90%",
        enabledEdit: true,
        dateFormat:'yyyy-MM-dd hh:mm:ss',
        pageSizeOptions: [5, 10, 15]

    });
	
	
	$("#uploadify").uploadify({ 
		'uploader': '<%=contextPath%>/static/resources/uploadify.swf', 
		'script': '<%=basePath%>restful/process/processDef/deploy/', 
		'cancelImg': '<%=contextPath%>/static/resources/cancel.png', 
		'expressInstall'  : '<%=contextPath%>/static/resources/expressInstall.swf',
		'queueID': 'fileQueue', 
		'auto': false, 
		'multi': true,
		'wmode':'transparent',
		'method':'post',
		'buttonImg':"<%=contextPath%>/static/resources/add.gif"
	}); 
	
	
	$("#Button1").click(function(){
		$('#uploadify').uploadifyUpload();

	});
});
</script>
</head>
<body style="width:99%;height:95%;">
<div position="top">
         <table width="95%">
   
   <tr>
       <td colspan="2">
         <div id="fileQueue"></div> 
         <input type="file" name="uploadify" id="uploadify" /> 
       </td>
   </tr>
   <tr>
       <td colspan="2" align="left" class="l-table-edit-td" width="10%"><input
			type="button" value="上传" id="Button1"
			class="l-button l-button-submit" /></td>
   </tr>
   <tr>

		


	</tr>
</table>
</div>
<div position="center">
  <div id="grid" style="width:99% ;height:90%;"></div>
</div>


</body>
</html>