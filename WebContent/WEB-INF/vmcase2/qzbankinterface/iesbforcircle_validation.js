var validator = $($formName).validate({
	rules:
    { 
		acctNo1:{required:true},
		acctNo2:{required:true},
		contractNo:{required:true},
		cardNo:{required:true},
		
		contractAmt:{required:true},
		startDate:{required:true},
		endDate:{required:true},
		//exchangeRate:{required:true},
		manaOrg:{required:true},
		registeredId:{required:true},
		registOrgNo:{required:true},
		incomeOrgNo:{required:true},
		registeredDate:{required:true},
		term:{required:true,number:true},
		actIntRate:{required:true},
		overdueIntRate:{required:true,number:true,min:1,max:100},
		penaltyIntRate:{required:true,number:true,min:1,max:100},
		repayDate:{required:true},
		
		//aClientNo:{required:true},
		clientName:{required:true},
		globalId:{required:true},
		issDate:{required:true},
		globalEffDate:{required:true},
		address:{required:true},
		birthDate:{required:true},
		signDate:{required:true},
		//expiryDate:{required:true},
		//loanCardNo:{required:true},
		mobile:{required:true},
		higherOrgNo:{required:true},
		acctExec:{required:true},
		openAcctDate:{required:true},
		
		feeAmount:{required:true,number:true},
		feeAcctNo:{required:true}
     },
messages:
    {
		acctNo1:{required:"收息收款账号不能为空"},
		acctNo2:{required:"放款账号不能为空"},
		contractNo:{required:"合同号不能为空"},
		cardNo:{required:"卡号不能为空"},
	
		contractAmt:{required:"合同金额不能为空"},
		startDate:{required:"贷款起始日期不能为空"},
		endDate:{required:"贷款结束日期不能为空"},
		//exchangeRate:{required:"外币时需要换算不能为空"},
		manaOrg:{required:"管理机构不能为空"},
		registeredId:{required:"登记人不能为空"},
		registOrgNo:{required:"登记机构不能为空"},
		incomeOrgNo:{required:"入账机构码不能为空"},
		registeredDate:{required:"登记日期不能为空"},
		term:{required:"期限不能为空",number:"还款期限只能为数字"},
		actIntRate:{required:"执行利率不能为空"},
		overdueIntRate:{required:"逾期利率不能为空",number:"逾期利率只能为数字",min:"逾期利率不能小于1%",max:"逾期利率不能大于100%"},
		penaltyIntRate:{required:"违约利率不能为空",number:"违约利率只能为数字",min:"违约利率不能小于1%",max:"违约利率不能大于100%"},
		repayDate:{required:"还款日期不能为空"},
		
		//aClientNo:{required:"客户号不能为空"},
		clientName:{required:"客户名称不能为空"},
		globalId:{required:"证件号码不能为空"},
		issDate:{required:"签发日期不能为空"},
		globalEffDate:{required:"证件有效日期不能为空"},
		address:{required:"地址不能为空"},
		birthDate:{required:"出生日期不能为空"},
		signDate:{required:"签约日期不能为空"},
		//expiryDate:{required:"信用到期日期不能为空"},
		//loanCardNo:{required:"贷款卡号不能为空"},
		mobile:{required:"手机号码不能为空"},
		higherOrgNo:{required:"上级机构不能为空"},
		acctExec:{required:"客户经理不能为空"},
		openAcctDate:{required:"开户日期不能为空"},
		
		feeAmount:{required:"费用金额不能为空",number:"费用金额只能为数字"},
		feeAcctNo:{required:"费用账号不能为空"}
   },
	errorPlacement : function(error, element) {
		element.after(error);
		if(layout){
			layout.resizeLayout();
		}
	}
});