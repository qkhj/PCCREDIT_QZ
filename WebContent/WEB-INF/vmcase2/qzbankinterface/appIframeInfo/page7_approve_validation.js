var validator = $($formName).validate({
	rules:
    { 
		checkSug:{required:true},
     },
messages:
    {
		checkSug:{required:"内部审查建议不能为空"},
   },
	errorPlacement : function(error, element) {
		element.after(error);
		if(layout){
			layout.resizeLayout();
		}
	}
});