var validator = $($formName).validate({
	rules:
    { 
		originator:{required:true},
		initiator:{required:true},
		initDate:{required:true},
		name:{required:true},
		address:{required:true},
		code:{required:true},
		sug:{required:true}
     },
messages:
    {
		originator:{required:"发起方不能为空"},
		initiator:{required:"发起人不能为空"},
		initDate:{required:"发起时间不能为空"},
		name:{required:"专案名称不能为空"},
		address:{required:"专案地址不能为空"},
		code:{required:"专案编号不能为空"},
		sug:{required:"评审意见不能为空"}
   },
	errorPlacement : function(error, element) {
		element.after(error);
		if(layout){
			layout.resizeLayout();
		}
	}
});