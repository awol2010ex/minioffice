<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<%
	String contextPath = request.getContextPath();
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">



<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8"></meta>
<title></title>
<script type='text/javascript' src='<%=request.getContextPath() %>/dwr/interface/TBizMenuService.js'></script>
<script type='text/javascript' src='<%=request.getContextPath() %>/dwr/engine.js'></script>	
<jsp:include  page="../../css.jsp"  flush="true" />
<script type='text/javascript'>
//菜单管理页面
	var tree = null;

	var curr_node_id=null;
	
	$(function() {
		$("body").ligerLayout( {
			leftWidth : 300
		});
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
								curr_node_id=node.data.id;

								
								TBizMenuService.getMenu(node.data.id,function(result){
                                  if(result){
									 $("#id")[0].value=result.id;
					                 $("#menuName")[0].value=result.menuName;
					                 $("#menuUrl")[0].value=result.menuUrl;
					                 $("#menuIndex")[0].value=result.menuIndex;
					                 if(result.parentNode)
					                 {
					                   $("#pid")[0].value=result.parentNode.id;
					                 }
					                 else
					                 {
					                   $("#pid")[0].value="";
					                 }
                                  }
								});
							} 

						});

		$("#toptoolbar").ligerToolBar( {
			items : [

			{
				text : '增加',
				click : function(){

                    if(curr_node_id){
                    	$("#pid")[0].value=curr_node_id;
                    }
                     $("#id")[0].value="";
	                 $("#menuName")[0].value="";
	                 $("#menuUrl")[0].value="";
	                 $("#menuIndex")[0].value="";
				}
			},

			{
				text : '删除',
				click : function(){

				 TBizMenuService.removeMenu(
						 curr_node_id,
		                 function(result){
		                     if(result){
		                        alert("删除成功");
		                        refreshTree();
		                     }else{
		                        alert("删除失败");
		                     }
		                 }
		            );
				}
			}

			]

		});


		$("#Button1").click(function(){
            TBizMenuService.saveMenu(
            	 $("#id")[0].value,
                 $("#menuName")[0].value,
                 parseInt($("#menuIndex")[0].value),
                 $("#menuUrl")[0].value,
                 $("#pid")[0].value,
                 function(result){
                     if(result){
                        alert("保存成功");
                        refreshTree();
                     }else{
                        alert("保存失败");
                     }
                 }
            );
            
	    });

        $("#Button2").click(function(){
            $("#form1").reset();
	    });


        function refreshTree(){
            tree.clear();
            tree.loadData(null,'<%=contextPath%>/restful/menu/list/',null);
        }
	});
</script>
</head>
<body>
<div position="left">

<div id="toptoolbar"></div> 


<ul id="tree1" style="height: 100%">
</ul>

</div>

<div position="center">
<form id="form1" style="width: 100%">
<table cellpadding="0" cellspacing="0" class="l-table-edit">

	<tr>

		<td align="right" class="l-table-edit-td" width="10%">编码:</td>

		<td align="left" class="l-table-edit-td" width="90%"><input
			name="id" type="text" id="id" ltype="text" style="width: 400px" readOnly="true"/></td>

		<td align="left"></td>

	</tr>
	<tr>

		<td align="right" class="l-table-edit-td" width="10%">菜单名称:</td>

		<td align="left" class="l-table-edit-td" width="90%"><input
			name="menuName" type="text" id="menuName" ltype="text"
			style="width: 400px" /></td>

		<td align="left"></td>

	</tr>
	<tr>

		<td align="right" class="l-table-edit-td" width="10%">菜单顺序:</td>

		<td align="left" class="l-table-edit-td" width="90%"><input
			name="menuIndex" type="text" id="menuIndex" ltype="text"
			style="width: 400px" /></td>

		<td align="left"></td>

	</tr>
	<tr>

		<td align="right" class="l-table-edit-td" width="10%">菜单链接:</td>

		<td align="left" class="l-table-edit-td" width="90%"><input
			name="menuUrl" type="text" id="menuUrl" ltype="text"
			style="width: 400px" /></td>

		<td align="left"></td>

	</tr>
	<tr>

		<td align="right" class="l-table-edit-td" width="10%">父节点编码:</td>

		<td align="left" class="l-table-edit-td" width="90%"><input
			name="pid" type="text" id="pid" ltype="text"
			style="width: 400px"  readOnly="true"/></td>

		<td align="left"></td>

	</tr>
	
	<tr>

		<td align="right" class="l-table-edit-td" width="10%"><input
			type="button" value="提交" id="Button1"
			class="l-button l-button-submit" /></td>

		<td align="left" class="l-table-edit-td" width="90%"><input
			type="reset" value="重置" class="l-button l-button-reset" /></td>

		<td align="left"></td>

	</tr>

</table>



</form>

</div>

</body>
</html>