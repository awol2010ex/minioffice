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
<jsp:include  page="../css.jsp"  flush="true" />
<style type="text/css">
body {
	font-size: 12px;
}
</style>

<script type='text/javascript' src='<%=request.getContextPath() %>/dwr/interface/TBizMenuService.js'></script>
<script type='text/javascript' src='<%=request.getContextPath() %>/dwr/engine.js'></script>	
<script type='text/javascript'>
//菜单树
var tree = null;
//标签页
var navtab=null;
$(function() {
	$("body").ligerLayout( {
		leftWidth : 300
	});
	
	//标签页
	$("#tab").ligerTab({changeHeightOnResize:true,dragToMove:true,dblClickToClose:true});

    navtab = $("#tab").ligerGetTabManager();
	
	//菜单树
	tree = $("#tree1")
			.ligerTree(
					{

						url : '<%=contextPath%>/restful/menu/list/',
						checkbox: false,
						nodeWidth : 200,
						textFieldName : 'menuName',
						idFieldName:"id",
						isLeaf:function(data){
							//这是BUG吧,为什么isLeaf 是 true ,hasChildren 就是true呢,看源代码
							return true;
						},
						onBeforeExpand : function(node) {

							//防止重复加载,使用isloaded 标记
                            if($("#"+node.data.id).data("isloaded")){
                               return; 
                            }
							var params = {};
							if (node.data.id) {
								params.pid = node.data.id;
							}

							
							tree
									.loadData(
											$("#"+node.data.id)[0],
											'<%=contextPath%>/restful/menu/list/',
											params);
							$("#"+node.data.id).data("isloaded",true)
						},
						onSelect :function (node){

							
							TBizMenuService.getMenu(node.data.id,function(result){
                              if(result && result.menuUrl && result.menuUrl!=''){
				                 navtab.addTabItem({tabid:result.id,text:result.menuName,url:'<%=contextPath%>'+result.menuUrl,height:"95%"});
                              }
							});
						} 

					});
});
</script>
</head>
<body>

<div position="left">

  <ul id="tree1" style="height: 100%">
  </ul>

</div>

<div position="center">
  <div id="tab"></div>
</div>


</body>
</html>