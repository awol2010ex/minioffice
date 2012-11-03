<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<%
	String contextPath = request.getContextPath();
%>

<link rel="stylesheet" type="text/css"
	href="<%=contextPath%>/static/skins/ligerui-icons.css" />
<link rel="stylesheet" type="text/css"
	href="<%=contextPath%>/static/skins/Aqua/css/ligerui-all.css" />
<link rel="stylesheet" type="text/css"
	href="<%=contextPath%>/static/resources/uploadify.css" />

<script src="<%=contextPath%>/static/scripts/jquery-1.7.2.min.js"
	type="text/javascript"></script>
<script type="text/javascript" src="<%=contextPath%>/static/scripts/swfobject.js"></script> 
<script type="text/javascript" src="<%=contextPath%>/static/scripts/jquery.uploadify.v2.1.4.js"></script>

<script type='text/javascript'
	src='<%=contextPath%>/static/scripts/dateformat.js'></script>	
<script type='text/javascript'
	src='<%=contextPath%>/static/scripts/utils.js'></script>	

<script type="text/javascript" src="<%=contextPath%>/static/scripts/ligerui/core/base.js"></script>
<script type="text/javascript" src="<%=contextPath%>/static/scripts/ligerui/plugins/ligerAccordion.js"></script> 
<script type="text/javascript" src="<%=contextPath%>/static/scripts/ligerui/plugins/ligerButton.js"></script> 
<script type="text/javascript" src="<%=contextPath%>/static/scripts/ligerui/plugins/ligerCheckBox.js"></script> 
<script type="text/javascript" src="<%=contextPath%>/static/scripts/ligerui/plugins/ligerComboBox.js"></script> 
<script type="text/javascript" src="<%=contextPath%>/static/scripts/ligerui/plugins/ligerDateEditor.js"></script> 
<script type="text/javascript" src="<%=contextPath%>/static/scripts/ligerui/plugins/ligerDialog.js"></script> 
<script type="text/javascript" src="<%=contextPath%>/static/scripts/ligerui/plugins/ligerDrag.js"></script> 
<script type="text/javascript" src="<%=contextPath%>/static/scripts/ligerui/plugins/ligerEasyTab.js"></script> 
<script type="text/javascript" src="<%=contextPath%>/static/scripts/ligerui/plugins/ligerForm.js"></script> 
<script type="text/javascript" src="<%=contextPath%>/static/scripts/ligerui/plugins/ligerGrid.js"></script> 
<script type="text/javascript" src="<%=contextPath%>/static/scripts/ligerui/plugins/ligerLayout.js"></script> 
<script type="text/javascript" src="<%=contextPath%>/static/scripts/ligerui/plugins/ligerMenu.js"></script> 
<script type="text/javascript" src="<%=contextPath%>/static/scripts/ligerui/plugins/ligerMenuBar.js"></script> 
<script type="text/javascript" src="<%=contextPath%>/static/scripts/ligerui/plugins/ligerMessageBox.js"></script> 
<script type="text/javascript" src="<%=contextPath%>/static/scripts/ligerui/plugins/ligerNoSelect.js"></script> 
<script type="text/javascript" src="<%=contextPath%>/static/scripts/ligerui/plugins/ligerRadio.js"></script> 
<script type="text/javascript" src="<%=contextPath%>/static/scripts/ligerui/plugins/ligerResizable.js"></script> 
<script type="text/javascript" src="<%=contextPath%>/static/scripts/ligerui/plugins/ligerSpinner.js"></script> 
<script type="text/javascript" src="<%=contextPath%>/static/scripts/ligerui/plugins/ligerTab.js"></script> 
<script type="text/javascript" src="<%=contextPath%>/static/scripts/ligerui/plugins/ligerTextBox.js"></script> 
<script type="text/javascript" src="<%=contextPath%>/static/scripts/ligerui/plugins/ligerTip.js"></script> 
<script type="text/javascript" src="<%=contextPath%>/static/scripts/ligerui/plugins/ligerToolBar.js"></script> 
<script type="text/javascript" src="<%=contextPath%>/static/scripts/ligerui/plugins/ligerTree.js"></script> 
<script type="text/javascript" src="<%=contextPath%>/static/scripts/ligerui/plugins/ligerWindow.js"></script> 
<style>
body {
	font-size: 12px;
}

.l-table-edit {
	
}

.l-table-edit-td {
	padding: 4px;
}

.l-button-submit,.l-button-reset {
	width: 80px;
	float: left;
	margin-left: 10px;
	padding-bottom: 2px;
}

.l-verify-tip {
	left: 230px;
	top: 120px;
}

.l-tab-content{ margin:0 auto; padding:0; border:none; width:100%;height:100%;}
</style>