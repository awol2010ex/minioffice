$.fn.ProcessDiagram=function(p){
	this.each(function()
	{
		p = p || {};//选项
		
		var g ={};//管理器
		
		g.ProcessDiagram = $(this);
		g.p=p;
        
		
		var groupBox=$("<fieldset style='padding:20px;border-color:#E2DED6;border-width:1px;border-style:Solid;'></fieldset>").appendTo(g.ProcessDiagram);
		
		groupBox.append("<legend  style='color:#333333;font-size:0.8em;font-weight:bold;'>流程图</legend>");
		
		//显示流程图
		var img = $("<img/>").appendTo(  $(groupBox) ).data({
			
			processDefinitionId: g.p.processDefinitionId//流程定义ID
			,
			processInstanceId: g.p.processInstanceId//流程实例ID
			,
			offsetTop:g.p.offsetTop//偏移
			,
			offsetLeft:g.p.offsetLeft//偏移
			,g:g
		});
        
		img.load(function(){
			    var img_offset =$(this).offset();//图片坐标
			    var me= $(this);
			    //取得历史环节
			    ActivityDwr.getHistoryActivityList(me.data("processInstanceId"),me.data("processDefinitionId"),function(result){
			    	 if(result.activities&&result.activities.length>0){

			    	    	//计算偏移量
			    	    	var minX=2147483647;
			    	    	for(var i=0,s=result.activities.length;i<s;i++){
			    	    		if(result.activities[i].x<=minX){
			    	    			minX=result.activities[i].x;
			    	    		}
			    	    	}
			    	    	var minY=2147483647;
			    	    	for(var i=0,s=result.activities.length;i<s;i++){
			    	    		if(result.activities[i].y<=minY){
			    	    			minY=result.activities[i].y;
			    	    		}
			    	    	}

			    	    	
			    	    	var maxX=-2147483647;
			    	    	for(var i=0,s=result.activities.length;i<s;i++){
			    	    		if(result.activities[i].x>=maxX){
			    	    			maxX=result.activities[i].x;
			    	    		}
			    	    	}
			    	    	var maxY=-2147483647;
			    	    	for(var i=0,s=result.activities.length;i<s;i++){
			    	    		if(result.activities[i].y>=maxY){
			    	    			maxY=result.activities[i].y;
			    	    		}
			    	    	}
			    	    	
			    	    	
			    	    	//画高亮节点
			    	        for(var i=0,s=result.activities.length;i<s;i++){
			    	            var activity=result.activities[i];
			    	            var a=$("<div></div>").data({acticityId:activity.activityId})
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
			    	                  }).attr("title",activity.properties.name);//显示环节名称
			    	        };
			    	 }
			    });
		});
		
		img.attr("src",__CONTEXT_PATH+"/restful/process/processInstance/diagram/"+p.processInstanceId+"/?rand="+new Date().getTime());
		
	});
};