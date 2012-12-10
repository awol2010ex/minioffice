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
<jsp:include  page="../../css.jsp"  flush="true" />
<script type='text/javascript' src='<%=request.getContextPath() %>/dwr/interface/ProcessDwr.js?rand=<%=new java.util.Date().getTime() %>'></script>
<script type='text/javascript' src='<%=request.getContextPath() %>/dwr/engine.js'></script>	
<style type="text/css">
body {
	font-size: 12px;
}
</style>	
<script type='text/javascript'>
var g=null;
//已部署流程定义列表
$(function() {
	$("body").ligerLayout( {});
	var g=$("#grid").ligerGrid({
        columns: [ 
              { display: '序号', name: 'id', width: "20%",isAllowHide: true },
              { display: 'key', name: 'key', width: "20%",isAllowHide: true },
              { display: '版本', name: 'version', width: "20%",isAllowHide: true },
              { display: '名称', name: 'name', width: "20%",isAllowHide: true }
        ],
        url: "<%=contextPath%>/restful/process/processDef/list/",
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
                var img = $("<img/>").appendTo(  $(td) );
                
                img.attr("src" ,"<%=contextPath%>/restful/process/processDef/diagram/"+row.deploymentId+"/?rand="+new Date().getTime());//显示流程图
                
                var tr =  $("<tr/>").appendTo(  $(table) );
                var td =  $("<td align='center'/>").appendTo(  $(tr) );
                var button = $("<button>发起流程</button>").appendTo(  $(td) );
                button.data("row",row);
                button.click(function(){
                	var _row =$(this).data("row"); 
                	//发起流程
                	ProcessDwr.initProcess(_row.key,function(result){
                		 if(result){
                            alert("发起成功");
                         }else{
                            alert("发起失败");
                         }
                	});
                });
        	}
        	
        }

    });
});
</script>
</head>
<body>

<div position="center">
  <div id="grid" style="width:99% ;height:90%;"></div>
</div>


</body>
</html>