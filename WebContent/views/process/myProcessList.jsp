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

<style type="text/css">
body {
	font-size: 12px;
}
</style>	
<script type='text/javascript'>
var g=null;
//我发起的流程列表
$(function() {
	$("body").ligerLayout( {});
	var g=$("#grid").ligerGrid({
        columns: [ 
              { display: '序号', name: 'id', width: "20%",isAllowHide: true },
              { display: '开始时间', name: 'startTime', width: "20%",isAllowHide: true ,type:'date'},
              { display: '结束时间', name: 'endTime', width: "20%",isAllowHide: true,type:'date' },
              { display: '流程定义序号', name: 'processDefinitionId', width: "20%",isAllowHide: true },
        ],
        url: "<%=contextPath%>/restful/process/myprocess/list/",
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
               
                
                
                
                var _iframe =$("<iframe frameborder='0'/>").attr("id",new Date().getTime()).appendTo($(detailPanel));
              //流程图链接
                var url ="<%=contextPath%>/views/process/diagram/processDiagram.jsp?processInstanceId="+row.id+"&processDefinitionId="+row.processDefinitionId+"&frameId="+_iframe.attr("id");
                //加载流程图
                _iframe.attr("src",url);
        	}
        	
        }

    });
});


//自适应高度
function afterLoadDiagram(frameId){
	    var  f= $("#"+frameId);
	    f.parent().height(f.height()+"px");
	    f.parent().height((f.height()+100)+"px");
}

</script>
</head>
<body>

<div position="center">
  <div id="grid" style="width:99% ;height:90%;"></div>
</div>


</body>
</html>