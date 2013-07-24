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
<script type='text/javascript' src='<%=request.getContextPath() %>/dwr/interface/TaskDwr.js?rand=<%=new java.util.Date().getTime() %>'></script>
<script type='text/javascript' src='<%=request.getContextPath() %>/dwr/interface/ActivityDwr.js?rand=<%=new java.util.Date().getTime() %>'></script>
<script type='text/javascript' src='<%=request.getContextPath() %>/dwr/interface/ProcessDwr.js?rand=<%=new java.util.Date().getTime() %>'></script>
<script type='text/javascript' src='<%=request.getContextPath() %>/dwr/engine.js'></script>

<jsp:include  page="../../css.jsp"  flush="true" />


<script type='text/javascript' src='<%=request.getContextPath() %>/static/scripts/activiti/ProcessDiagram.js?rand=<%=new java.util.Date().getTime() %>'></script>


<style type="text/css">
body {
	font-size: 12px;
}
</style>	
<script type='text/javascript'>
var g_assignee=null;//分派任务列表

var g_candidate=null;//候选任务列表

var g_history=null;//已归档任务列表

var formDataMap={};//表单映射

var formTableMap={};//表单TABLE缓存


//我的任务列表
$(function() {
	$("body").ligerLayout( {});
	
	

	//被分派任务
	g_assignee=$("#grid_assignee").ligerGrid({
        columns: [ 
              
              { display: '任务创建时间', name: 'createTime', width: "15%",isAllowHide: true ,type:'date'},
              { display: '任务名称', name: 'name', width: "15%",isAllowHide: true },
              { display: '流程定义名称', name: 'processDefinitionName', width: "15%",isAllowHide: true },
              { display: '流程定义ID', name: 'processDefinitionId', width: "15%",isAllowHide: true },
              { display: '流程实例ID', name: 'processInstanceId', width: "15%",isAllowHide: true },
              { display: '操作', name: 'id', width: "30%",isAllowHide: true,
             	 render :function(row,i){
             		   return   "<span><button  onclick=\"commitTask('"+row.id+"')\"  >审批</button>&nbsp;<button  onclick=\"viewProcessList('"+row.processInstanceId+"')\"  >查看审批记录</button></span>"
             		 
             	 }
               
               }
        ],
        url: "<%=contextPath%>/restful/task/mytask/assignee/list/",
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
        	onShowDetail: showTaskDetail
        	
        }

    });
	//候选任务
	g_candidate=$("#grid_candidate").ligerGrid({
        columns: [ 
              
              { display: '任务创建时间', name: 'createTime', width: "15%",isAllowHide: true ,type:'date'},
              { display: '任务名称', name: 'name', width: "15%",isAllowHide: true },
              { display: '流程定义名称', name: 'processDefinitionName', width: "15%",isAllowHide: true },
              { display: '流程定义ID', name: 'processDefinitionId', width: "15%",isAllowHide: true },
              { display: '流程实例ID', name: 'processInstanceId', width: "15%",isAllowHide: true },
              { display: '操作', name: 'id', width: "20%",isAllowHide: true,
             	 render :function(row,i){
             		   return   "<span><button  onclick=\"commitTask('"+row.id+"')\"  >审批</button>&nbsp;<button  onclick=\"viewProcessList('"+row.processInstanceId+"')\"  >查看审批记录</button></span>"
             		 
             	 }
               
               }
        ],
        url: "<%=contextPath%>/restful/task/mytask/candidate/list/",
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
        	onShowDetail: showTaskDetail
        	
        }

    });
	 
	 
	//已归档任务
	g_history=$("#grid_history").ligerGrid({
        columns: [ 
              { display: '序号', name: 'id', width: "15%",isAllowHide: true },
              
              { display: '开始时间', name: 'startTime', width: "15%",isAllowHide: true,type:'date' },
              { display: '完成时间', name: 'endTime', width: "15%",isAllowHide: true ,type:'date'},
              
              { display: '任务名称', name: 'name', width: "15%",isAllowHide: true },
              { display: '流程定义名称', name: 'processDefinitionName', width: "15%",isAllowHide: true },
              { display: '流程定义ID', name: 'processDefinitionId', width: "15%",isAllowHide: true },
              { display: '流程实例ID', name: 'processInstanceId', width: "15%",isAllowHide: true },
              { display: '操作', name: 'id', width: "30%",isAllowHide: true,
              	 render :function(row,i){
              		   return   "<span><button  onclick=\"viewProcessList('"+row.processInstanceId+"')\"  >查看审批记录</button></span>"
              		 
              	 }
                
                }
        ],
        url: "<%=contextPath%>/restful/task/mytask/history/list/",
        sortName: 'id',
        showTitle: false,
        dataAction:'server',
        pageSize: 5,
        height:"90%",
        enabledEdit: false,
        dateFormat:'yyyy-MM-dd hh:mm:ss',
        pageSizeOptions: [5, 10, 15],
        detail: { 
        	//显示明细
        	onShowDetail: showTaskDetail
        	
        }

    });
	
	
	 $("#navtab1").ligerTab(); //标签页
	 
	
});

//显示需操作任务表单
function showTaskDetail(row, detailPanel,callback){

    var table =$("<table width='100%'></table>").appendTo($(detailPanel));
    var tr = $("<tr></tr>").appendTo(table);
    var td = $("<td></td>").appendTo(tr);
    var _iframe =$("<iframe frameborder='0'/>").attr("id",new Date().getTime()).appendTo(td);
    //流程图链接
      var url ="<%=contextPath%>/views/process/diagram/processDiagram.jsp?processInstanceId="+row.processInstanceId+"&processDefinitionId="+row.processDefinitionId+"&taskId="+row.id+"&frameId="+_iframe.attr("id");
      //加载流程图
      _iframe.attr("src",url);
      
    //  
    
    
    
    //流程变量
      var tr = $("<tr></tr>").appendTo(table);
      var td_var = $("<td align='left'  style='padding:10px'></td>").appendTo(tr);
      ProcessDwr.getVariableList(row.processInstanceId,
    	     function(result){
    	           if(result && result.length>0){
    	        	   
    	        	   
    	        	   
    	        	   var var_table =$("<table width='50%'  border='1'></table>").appendTo(td_var);//表格用的table
    	        	   
    	        	   var  var_tr=$("<tr></tr>").appendTo(var_table);
    	        	   var var_td =$("<td style='padding:10px' ></td>").appendTo(var_tr);
    	        	   
    	        	   var var_div =$("<div></div>").appendTo(var_td);
    	        	   
    	        	   var_div.ligerGrid({
    	        	            columns: [ 
    	        	                  { display: '变量名', name: 'variableName', width: "50%",isAllowHide: true },
    	        	                  { display: '值', name: 'value', width: "50%",isAllowHide: true }
    	        	            ],
    	        	            data:{Rows:result , Total:result.length},
    	        	            title:'变量',
    	        	            dataAction:'local',
    	        	            pageSize: result.length,
    	        	            height:"90%",
    	        	            enabledEdit: false,
    	        	            pageSizeOptions: [5, 10, 15]
    	        	   });
    	           }
             } 	  
      );
      
    //
      
      
    //打开任务表单
      var tr = $("<tr></tr>").appendTo(table);
      var td_form = $("<td align='center'  style='padding:10px'></td>").appendTo(tr);
      
      TaskDwr.getFormData(row.id,function(result){
    	  if(result && result.length>0){
    		  
    		  formDataMap[row.id]=result; //表单数据缓存
    		  
    		  
    		    var form_table =$("<table width='50%'  border='1'></table>").appendTo(td_form);//表格用的table
    		    for(var i=0,s= result.length;i<s ;i++){
    		    	  var  form_tr=$("<tr></tr>").appendTo(form_table);
    		    	  
    		    	  var form_td1 =$("<td style='padding:10px'></td>").appendTo(form_tr).text(result[i].name+":");
    		    	  var form_td2 =$("<td style='padding:10px'></td>").appendTo(form_tr);
    		    	  $("<input type='text'    />").addClass("field_"+result[i].id).appendTo(form_td2).val(result[i].value);
    		    }
    		    
    		    formTableMap[row.id]=form_table;//表单表格缓存
    	  }
      });
      //
      
      
      
}

//自适应高度
function afterLoadDiagram(frameId){
	    var  f= $("#"+frameId);
	    f.parent().height(f.height()+"px");
	    f.parent().parent().parent().parent().parent().height((f.height()+100)+"px");
}

//审批任务
function commitTask(taskId){
	 var _formData= formDataMap[taskId];//表单数据
	 var _formTable =formTableMap[taskId];//表单表格
	 
	 var varset ={};//提交变量
	 
	 if(_formData &&_formData.length>0&& _formTable){
		 
		   for(var i=0,s= _formData.length;i<s;i++){
			   var key =_formData[i].id;
			   var var_value=_formTable.find(" .field_"+key).val();//变量值
			   varset[key]=var_value;//设置变量值
		   }
	 }
	 
	 
	 
 	//审批任务流程
 	TaskDwr.commitTask(taskId ,varset,function(ret){
		if(ret.result){
          alert("审批成功");
          refresh();
        }else{
          alert("审批失败:"+ret.msg);
        }
 	});
	 
 }
 
 //查看流程审批列表
 function viewProcessList(processInstanceId){
	 top.navtab.addTabItem({tabid:new Date().getTime(),text:'流程审批列表',url:'<%=contextPath%>/views/task/processTaskList.jsp?processInstanceId='+processInstanceId,height:"95%"});
 }
 function refresh(){
		$("#grid_candidate").ligerGetGridManager().loadData();
		$("#grid_assignee").ligerGetGridManager().loadData();
		$("#grid_history").ligerGetGridManager().loadData();
		
 }
  
</script>
</head>
<body>

<div position="center">
   <div id="navtab1" style="width: 100%;overflow:hidden; border:1px solid #A3C0E8; ">
        <div  title="分派任务" showClose="false">
            <!-- 被分派任务 -->
            <div id="grid_assignee" style="width:99% ;height:90%;"></div>
        </div>
         <div  title="候选任务" showClose="false">
            <!-- 被邀请任务 -->
            <div id="grid_candidate" style="width:99% ;height:90%;"></div>
        </div>
        <div  title="归档任务" showClose="false">
            <!-- 已归档任务 -->
            <div id="grid_history" style="width:99% ;height:90%;"></div>
        </div>
   </div>
</div>


</body>
</html>