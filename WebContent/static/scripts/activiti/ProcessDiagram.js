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
		var img = $("<img src='"+__CONTEXT_PATH+"/restful/process/processInstance/diagram/"+p.processInstanceId+"/?rand="+new Date().getTime()+"'/>").appendTo(  $(groupBox) );
        
	});
};