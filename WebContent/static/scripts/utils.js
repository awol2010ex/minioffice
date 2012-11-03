
var utils={
	
	
	getFormData:function(form){
		var result={};
		var inputList=document.body.getElementsByTagName("input");
		if(inputList&&inputList.length>0){
			
			for(var i=0,s=inputList.length;i<s;i++){
				
				var ip=inputList[i];
				if(ip.field_name){//IE
				   result[ip.field_name]=$.trim(ip.value);
				}
				else//firefox
				if(ip.attributes.field_name&&ip.attributes.field_name.value){
					
				   result[ip.attributes.field_name.value]=$.trim(ip.value);
				}
	
			}
		}
		return result;
	}
};