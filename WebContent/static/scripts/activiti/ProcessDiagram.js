//用于取得绝对位置的函数
function getElementLeft(element){
              var actualLeft = element.offsetLeft;
              var current = element.offsetParent;
              while (current !== null){
               actualLeft += current.offsetLeft;
               current = current.offsetParent;
              }
             return actualLeft;
}
function getElementTop(element){
             var actualTop = element.offsetTop;
             var current = element.offsetParent;
             while (current !== null){
                   actualTop += current.offsetTop;
                   current = current.offsetParent;
             }
             return actualTop;
}


$.fn.ProcessDiagram=function(p){
	this.each(function()
	{
		p = p || {};// 选项
		
		var g ={};// 管理器
		$(this).empty();
		g.ProcessDiagram = $(this);
		g.p=p;
        
		
		var groupBox=$("<fieldset style='padding:20px;border-color:#E2DED6;border-width:1px;border-style:Solid;'></fieldset>").appendTo(g.ProcessDiagram);
		
		groupBox.append("<legend  style='color:#333333;font-size:0.8em;font-weight:bold;'>"+g.p.title+"</legend>");
		
		// 显示流程图
		var img = $("<img/>").appendTo(  $(groupBox) ).data({
			
			processDefinitionId: g.p.processDefinitionId// 流程定义ID
			,
			processInstanceId: g.p.processInstanceId// 流程实例ID
			,
			offsetTop:g.p.offsetTop// 偏移
			,
			offsetLeft:g.p.offsetLeft// 偏移
		   ,p:g.p
		});
        
		img.load(function(){
	
			    var img_offset ={top:getElementTop($(this)[0]),left:getElementLeft($(this)[0])};// 图片坐标
			    
			    //设置外层大小
			    $(this).parent().parent().css({
			    	height:$(this).height()+50,
			    	width:$(this).width()+20
			    });
			    
			    var me= $(this);
			    // 取得历史环节
			    ActivityDwr.getHistoryActivityList(me.data("processInstanceId"),me.data("processDefinitionId"),function(result){
			    	
			    	// 所有环节的列表
			    	 if(result.activities&&result.activities.length>0){

			    	    	// 计算偏移量
			    	    	var minX=2147483647;
			    	    	var minY=2147483647;
			    	    	var maxX=-2147483647;
			    	    	var maxY=-2147483647;
			    	    	
			    	    	for(var i=0,s=result.activities.length;i<s;i++){
			    	    		if(result.activities[i].x<=minX){
			    	    			minX=result.activities[i].x;
			    	    		}
			    	    		if(result.activities[i].y<=minY){
			    	    			minY=result.activities[i].y;
			    	    		}
			    	    		if(result.activities[i].x>=maxX){
			    	    			maxX=result.activities[i].x;
			    	    		}
			    	    		if(result.activities[i].y>=maxY){
			    	    			maxY=result.activities[i].y;
			    	    		}
			    	    	}
			    	    	
			    	    
			    	    	
			    	    	
			    	    	// 画高亮节点
			    	        for(var i=0,s=result.activities.length;i<s;i++){
			    	            var activity=result.activities[i];
			    	        
			    	            // DIV层缓存
			    	            if(!me["activityDivMap"]){
			    	            	me["activityDivMap"]={};
			    	            }
			    	          
			    	            // 节点隐藏DIV层
			    	            var a=$("<div></div>").data({activityId:activity.id})
			    	                  .appendTo(me.parent())
			    	                  .css({

			    	                      "position":"absolute",
			    	                      "zIndex":"999",
			    	                      "height":activity.height+"px",
			    	                      "width":activity.width+"px",
			    	                      "left":(activity.x-minX+img_offset.left+parseInt(me.data("offsetLeft")))+"px",
			    	                      "top":(activity.y-minY+img_offset.top+parseInt(me.data("offsetTop")))+"px",
			    	                      "borderColor":"#6BE04E",
			    	                      "borderWidth":"0px",
			    	                      "borderStyle":"solid"
			    	                  }).attr("title",activity.properties.name);// 显示环节名称
			    	            
			    	           me.activityDivMap[activity.id]=a;// 缓存DIV层
			    	        };
			    	 }
			    	 
			    	 
			    	 // 历史经过环节列表
			    	 if(result.historyActivitys  && result.historyActivitys.length>0){
			    		 // 标注已经过环节
			    		  for(var i=0,s=result.historyActivitys.length;i<s;i++){
			    	            var ha=result.historyActivitys[i];
			    	            var a= me.activityDivMap[ha.activityId];
			    	            var flagImg =$("<img  src='"+__CONTEXT_PATH+"/static/images/flag.png'  style='width:12px;height:12px;'>").appendTo(a);
			    		  }
			    	 }
			    	 
			    	 
			    	 //执行加载后方法
			    	 var  func_afterLoadDiagram  =me.data("p").afterLoadDiagram ;
			    	 if(func_afterLoadDiagram ){
			    		 func_afterLoadDiagram();
			    	 }
			    });
		});
		
		img.attr("src",__CONTEXT_PATH+"/restful/process/processInstance/diagram/"+p.processInstanceId+"/?rand="+new Date().getTime());
		
	});
};