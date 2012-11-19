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
		   ,g:g
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
			    	            if(!me["activityMap"]){
			    	            	me["activityMap"]={};
			    	            }
			    	          
			    	            // 节点隐藏DIV层
			    	            var a=$("<div></div>").data({activityId:activity.id})
			    	                  .appendTo(me.parent())
			    	                  .css({

			    	                      "position":"absolute",
			    	                      "zIndex":"10",
			    	                      "height":activity.height+"px",
			    	                      "width":activity.width+"px",
			    	                      "left":(activity.x-minX+img_offset.left+parseInt(me.data("offsetLeft")))+"px",
			    	                      "top":(activity.y-minY+img_offset.top+parseInt(me.data("offsetTop")))+"px",
			    	                      "borderColor":"#6BE04E",
			    	                      "borderWidth":"0px",
			    	                      "borderStyle":"solid"
			    	                  }).attr("title",activity.properties.name);// 显示环节名称
			    	            
			    	            
			    	          //节点右键方法
						      var  func_contextMenuNode  =me.data("p").contextMenuNode ;
			    	          if(func_contextMenuNode){
			    	        	  a.bind("contextmenu",   func_contextMenuNode );
			    	          }
			    	            
			    	            
			    	           me.activityDivMap[activity.id]=a;// 缓存DIV层
			    	           me.activityMap[activity.id]=activity;// 缓存节点
			    	           
			    	           //显示路由边
			    	           if(activity.transitions && activity.transitions.length>0 ){
			    	        	     for(var k=0,m=activity.transitions.length;k<m;k++){
			    	        	    	   var  trn=activity.transitions[k]
			    	        	    	   var trn_x =(activity.x+trn.destination.x)/2;///路由边x
			    	        	    	   var trn_y =(activity.y+trn.destination.y)/2;//路由边y
			    	        	    	   
			    	        	    	   if(trn.properties.name)
			    	        	    	   var trn_a=$("<a></a>").text(trn.properties.name)// 显示路由名称
					    	                  .appendTo(me.parent())
					    	                  .css({

					    	                      "position":"absolute",
					    	                      "zIndex":"999",
					    	                      "left":(trn_x-minX+img_offset.left+parseInt(me.data("offsetLeft")))+"px",
					    	                      "top":(trn_y-minY+img_offset.top+parseInt(me.data("offsetTop")))+"px"
					    	                  });
			    	        	     }
			    	           }
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
			    	 
			    	 
			    	 //显示轨迹的按钮
			    	 $("<img></img>")
			    	 .data("activities",result.activities)//所有环节列表
			         .attr({src:__CONTEXT_PATH+"/static/images/array_path_32.png",title:"查看轨迹"})
			         .css({

			             "position":"absolute",
			             "zIndex":"2",
			             "height":"32px",
			             "width":"32px",
			             "left":(img_offset.left+parseInt(me.data("offsetLeft"))+16)+"px",
			             "top":(img_offset.top+parseInt(me.data("offsetTop"))-10)+"px",
			             "borderColor":"#6BE04E",
			             "borderWidth":"0px",
			             "borderStyle":"solid"
			         }).appendTo(me.data("g").ProcessDiagram)
			          .dblclick(
                               function(e){
                                           //球初始位置
                            	   if(!$(this).data("moveBall")){
                            		   $(this).data("moveBall",$("<img></img>")
                                       .appendTo(me.data("g").ProcessDiagram)
                                       .attr({src:__CONTEXT_PATH+"/static/images/Inspiration_Orb_Icon_005.png"})
                                       .css({

                                           "position":"absolute",
                                           "zIndex":"2",
                                           "height":"32px",
                                           "width":"32px",
                                           "left":(result.activities[0].x-minX+img_offset.left+parseInt(me.data("offsetLeft")))+"px",
 			    	                      "top":(result.activities[0].y-minY+img_offset.top+parseInt(me.data("offsetTop")))+"px",
                                           "borderColor":"#6BE04E",
                                           "borderWidth":"0px",
                                           "borderStyle":"solid"
                                       }));
                            		   
                            	   }
                            	   $(this).data("moveBall").show();//显示轨迹球
                            	 //按时间顺序移动轨迹球
                                   for(var i=0,s=result.historyActivitys.length;i<s;i++){
                                       var activity=result.historyActivitys[i];
                                       var node=me.activityMap[activity.activityId];
                                       if(i>0){
                                         $(this).data("moveBall").animate({
                                           "left":(node.x-minX+img_offset.left+parseInt(me.data("offsetLeft")))+"px",
                                           "top":(node.y-minY+img_offset.top+parseInt(me.data("offsetTop")))+"px"
                                         },1000);
                                       }
                                       else{
                                    	   $(this).data("moveBall").css({
                                               "left":(node.x-minX+img_offset.left+parseInt(me.data("offsetLeft")))+"px",
                                               "top":(node.y-minY+img_offset.top+parseInt(me.data("offsetTop")))+"px"
                                             });
                                       }
                                   }

                                   
                                 //移动到最后一个任务(如果最后一个任务已完成)
                                 if(result.historyActivitys[result.historyActivitys.length-1].endTime){
                                   $(this).data("moveBall").animate({
                                	   "left":(result.activities[result.activities.length-1].x-minX+img_offset.left+parseInt(me.data("offsetLeft")))+"px",
			    	                   "top":(result.activities[result.activities.length-1].y-minY+img_offset.top+parseInt(me.data("offsetTop")))+"px",
                                   },1000);
                                 }
                                   //隐藏轨迹球
                                   $(this).data("moveBall").hide(1000);
                               }
                     );
			         
			         ;
			    });
		});
		
		img.attr("src",__CONTEXT_PATH+"/restful/process/processInstance/diagram/"+p.processInstanceId+"/?rand="+new Date().getTime());
		
	});
};