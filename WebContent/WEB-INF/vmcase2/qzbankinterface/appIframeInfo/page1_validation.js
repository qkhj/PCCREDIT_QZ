var validator = $($formName).validate({
	rules:
    { 
		applyTime:{required:true},
		sex:{required:true},
		phone_1:{required:true,number:true},
		
		applyAmount:{required:true,number:true},
		applyDeadline:{required:true},
		applyUse:{required:true},
		applyEndTime:{required:true},
		monthRepay:{required:true,number:true},
		
		signDate:{required:true},
		
		commet:{required:true},
		
		managerTime:{required:true},
		
		yearIncome:{number:true},
		profit:{number:true},
		totalAssets:{number:true},
		inMoney:{number:true},
		outMoney:{number:true},
		otherOut:{number:true},
		monthOtherIncome:{number:true},
		
		bussStartYear:{number:true},
		bussEmployeeNum:{number:true},
		
		maritalGlobalId:{idcard:true},
		familyNum:{number:true}
     },
messages:
    {
		applyTime:{required:"申请日期不能为空"},
		sex:{required:"性别不能为空"},
		phone_1:{required:"移动电话1不能为空",number:"移动电话1只能为数字"},
		
		applyAmount:{required:"贷款金额不能为空",number:"贷款金额只能为数字"},
		applyDeadline:{required:"贷款期限不能为空"},
		applyUse:{required:"贷款用途不能为空"},
		applyEndTime:{required:"申请到期日不能为空"},
		monthRepay:{required:"月还款能力不能为空",number:"月还款能力只能为数字"},
		
		signDate:{required:"签名日期不能为空"},
		
		commet:{required:"评论不能为空"},
		
		managerTime:{required:"日期不能为空"},
		
		yearIncome:{number:"年营业额只能为数字"},
		profit:{number:"利润只能为数字"},
		totalAssets:{number:"总资产只能为数字"},
		inMoney:{number:"应收账款只能为数字"},
		outMoney:{number:"应付账款只能为数字"},
		otherOut:{number:"其他负债只能为数字"},
		monthOtherIncome:{number:"每月其他收入金额只能为数字"},
		
		bussStartYear:{number:"开业时间只能为具体年份"},
		bussEmployeeNum:{number:"雇员人数只能为数字"},
		
		//maritalGlobalId:{idcard:""},
		familyNum:{number:"家庭人数只能为数字"}
   },
	errorPlacement : function(error, element) {
		element.after(error);
		if(layout){
			layout.resizeLayout();
		}
	}
});