var validator = $($formName).validate({
	rules:
    { 
		je:{required:true,number:true},
		ll:{required:true,number:true,min:1,max:100},
     },
messages:
    {
		je:{required:"金额不能为空",number:"金额只能为数字"},
		ll:{required:"年利率不能为空",number:"年利率只能为数字",min:"年利率不能小于1%",max:"年利率不能大于100%"},
   },
	errorPlacement : function(error, element) {
		element.after(error);
		if(layout){
			layout.resizeLayout();
		}
	}
});