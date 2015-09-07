var validator = $($formName).validate({
	rules:
    { 
		name:{required:true},
		sex:{required:true},
		birthday:{required:true},
		globalType:{required:true},
		globalId:{required:true,idcard:true},
		phone_1:{required:true},
		
		yeahIncome:{number:true},
     },
messages:
    {
		name:{required:"姓名不能为空"},
		sex:{required:"性别不能为空"},
		birthday:{required:"出生日期不能为空"},
		globalType:{required:"证件类型不能为空"},
		globalId:{required:"证件号码不能为空"},
		phone_1:{required:"手机1不能为空"},
		
		yeahIncome:{number:"个人年收入只能为数字"},
   },
	errorPlacement : function(error, element) {
		element.after(error);
		if(layout){
			layout.resizeLayout();
		}
	}
});