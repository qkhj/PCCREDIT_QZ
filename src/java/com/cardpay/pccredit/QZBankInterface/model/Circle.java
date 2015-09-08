package com.cardpay.pccredit.QZBankInterface.model;

import java.util.Date;

import com.wicresoft.jrad.base.database.id.IDType;
import com.wicresoft.jrad.base.database.model.BusinessModel;
import com.wicresoft.jrad.base.database.model.ModelParam;

/**
 * 
 * @author shaoming
 *
 */
@ModelParam(table = "qz_iesb_for_circle",generator=IDType.assigned)
public class Circle extends BusinessModel{
	private static final long serialVersionUID = 1L;
	private String contractNo;
	private String cardNo;
	private String clientNo;
	private String guaranteeMode;
	private String guaranteeModeDetail;
	private String ccy1;
	private String contractAmt;
	private Date startDate;
	private Date endDate;
	private String exchangeRate;
	private String manaOrg;
	private String registeredId;
	private String registOrgNo;
	private String incomeOrgNo;
	private Date registeredDate;
	private String term;
	private String termType;
	private String deferFlag;
	private String actIntRate;
	private String overdueIntRate;
	private String penaltyIntRate;
	private String repayType;
	private String repayDate;
	private String fiveLevelType;
	private String speLoanType;
	private String limitUseedType;
	private String drUsage;
	private String flag;
	private String loanKind;
	
	private String loanKind_1;
	private String loanKind_2;
	private String loanKind_3;
	private String loanKind_4;
	
	private String agriLoanKind;
	
	private String agriLoanKind_1;
	private String agriLoanKind_2;
	private String agriLoanKind_3;
	private String agriLoanKind_4;
	private String agriLoanKind_5;
	
	private String personLoanKind;
	private String adjustType;
	private String newPrdType;
	private String newPrdLoan;
	private String loanDirection;
	
	private String loanDirection_1;
	private String loanDirection_2;
	private String loanDirection_3;
	private String loanDirection_4;
	
	private String loanBelong1;
	
	private String loanBelong1_1;
	private String loanBelong1_2;
	private String loanBelong1_3;
	private String loanBelong1_4;
	private String loanBelong1_5;
	
	private String loanBelong2;
	private String loanBelong3;
	private String loanBelong4;
	private String bussType;
	private String chiCustManager;
	private String aClientNo;
	private String clientName;
	private String sex;
	private String clientType;
	private String globalType;
	private String globalId;
	private String longGlobalType;
	private Date issDate;
	private Date globalEffDate;
	private String agriFlag;
	private String countryCode;
	private String nationalityCode;
	private String regPermResidence;
	
	private String regPermResidence_1;
	private String regPermResidence_2;
	private String regPermResidence_3;
	
	private String address;
	private Date birthDate;
	private String education;
	private String degree;
	private Date signDate;
	private String holdCardMsg;
	private String passportFlag;
	private String creditLevel;
	private Date expiryDate;
	private String relClientFlag;
	private String ownBranchRelation;
	private String post;
	private String loanCardFlag;
	private String loanCardNo;
	private String mobile;
	private String higherOrgNo;
	private String acctExec;
	private Date openAcctDate;
	private String feeCcy;
	private String feeAmount;
	private String feeAcctNo;
//	private String acctChrt;
//	private String ownBranchFlag;
//	private String acctNo;
//	private String acctName;
//	private String openAcctBranchId;
//	private String openAcctBranchName;
//	private String payAmt;
//	private String glCode;
//	private String ccy3;
//	private String cInterbankId;
	private String userId;
	
	private String retMsg;
	private String retCode;
	private String retContno;
	private String loanStatus;
	
	private String acctNo1;//收息收款账号
	private String acctNo2;//放款账号
	
	private String customerId;
	private String applicationId;
	
	public String getContractNo() {
		return contractNo;
	}
	public void setContractNo(String contractNo) {
		this.contractNo = contractNo;
	}
	public String getCardNo() {
		return cardNo;
	}
	public void setCardNo(String cardNo) {
		this.cardNo = cardNo;
	}
	public String getClientNo() {
		return clientNo;
	}
	public void setClientNo(String clientNo) {
		this.clientNo = clientNo;
	}
	public String getGuaranteeMode() {
		return guaranteeMode;
	}
	public void setGuaranteeMode(String guaranteeMode) {
		this.guaranteeMode = guaranteeMode;
	}
	public String getGuaranteeModeDetail() {
		return guaranteeModeDetail;
	}
	public void setGuaranteeModeDetail(String guaranteeModeDetail) {
		this.guaranteeModeDetail = guaranteeModeDetail;
	}
	public String getCcy1() {
		return ccy1;
	}
	public void setCcy1(String ccy1) {
		this.ccy1 = ccy1;
	}
	public String getContractAmt() {
		return contractAmt;
	}
	public void setContractAmt(String contractAmt) {
		this.contractAmt = contractAmt;
	}
	public Date getStartDate() {
		return startDate;
	}
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}
	public Date getEndDate() {
		return endDate;
	}
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
	public String getExchangeRate() {
		return exchangeRate;
	}
	public void setExchangeRate(String exchangeRate) {
		this.exchangeRate = exchangeRate;
	}
	public String getManaOrg() {
		return manaOrg;
	}
	public void setManaOrg(String manaOrg) {
		this.manaOrg = manaOrg;
	}
	public String getRegisteredId() {
		return registeredId;
	}
	public void setRegisteredId(String registeredId) {
		this.registeredId = registeredId;
	}
	public String getRegistOrgNo() {
		return registOrgNo;
	}
	public void setRegistOrgNo(String registOrgNo) {
		this.registOrgNo = registOrgNo;
	}
	public String getIncomeOrgNo() {
		return incomeOrgNo;
	}
	public void setIncomeOrgNo(String incomeOrgNo) {
		this.incomeOrgNo = incomeOrgNo;
	}
	public Date getRegisteredDate() {
		return registeredDate;
	}
	public void setRegisteredDate(Date registeredDate) {
		this.registeredDate = registeredDate;
	}
	public String getTerm() {
		return term;
	}
	public void setTerm(String term) {
		this.term = term;
	}
	public String getTermType() {
		return termType;
	}
	public void setTermType(String termType) {
		this.termType = termType;
	}
	public String getDeferFlag() {
		return deferFlag;
	}
	public void setDeferFlag(String deferFlag) {
		this.deferFlag = deferFlag;
	}
	public String getActIntRate() {
		return actIntRate;
	}
	public void setActIntRate(String actIntRate) {
		this.actIntRate = actIntRate;
	}
	public String getOverdueIntRate() {
		return overdueIntRate;
	}
	public void setOverdueIntRate(String overdueIntRate) {
		this.overdueIntRate = overdueIntRate;
	}
	public String getPenaltyIntRate() {
		return penaltyIntRate;
	}
	public void setPenaltyIntRate(String penaltyIntRate) {
		this.penaltyIntRate = penaltyIntRate;
	}
	public String getRepayType() {
		return repayType;
	}
	public void setRepayType(String repayType) {
		this.repayType = repayType;
	}
	public String getRepayDate() {
		return repayDate;
	}
	public void setRepayDate(String repayDate) {
		this.repayDate = repayDate;
	}
	public String getFiveLevelType() {
		return fiveLevelType;
	}
	public void setFiveLevelType(String fiveLevelType) {
		this.fiveLevelType = fiveLevelType;
	}
	public String getSpeLoanType() {
		return speLoanType;
	}
	public void setSpeLoanType(String speLoanType) {
		this.speLoanType = speLoanType;
	}
	public String getLimitUseedType() {
		return limitUseedType;
	}
	public void setLimitUseedType(String limitUseedType) {
		this.limitUseedType = limitUseedType;
	}
	public String getDrUsage() {
		return drUsage;
	}
	public void setDrUsage(String drUsage) {
		this.drUsage = drUsage;
	}
	public String getFlag() {
		return flag;
	}
	public void setFlag(String flag) {
		this.flag = flag;
	}
	public String getLoanKind() {
		return loanKind;
	}
	public void setLoanKind(String loanKind) {
		this.loanKind = loanKind;
	}
	public String getAgriLoanKind() {
		return agriLoanKind;
	}
	public void setAgriLoanKind(String agriLoanKind) {
		this.agriLoanKind = agriLoanKind;
	}
	public String getPersonLoanKind() {
		return personLoanKind;
	}
	public void setPersonLoanKind(String personLoanKind) {
		this.personLoanKind = personLoanKind;
	}
	public String getAdjustType() {
		return adjustType;
	}
	public void setAdjustType(String adjustType) {
		this.adjustType = adjustType;
	}
	public String getNewPrdType() {
		return newPrdType;
	}
	public void setNewPrdType(String newPrdType) {
		this.newPrdType = newPrdType;
	}
	public String getNewPrdLoan() {
		return newPrdLoan;
	}
	public void setNewPrdLoan(String newPrdLoan) {
		this.newPrdLoan = newPrdLoan;
	}
	public String getLoanDirection() {
		return loanDirection;
	}
	public void setLoanDirection(String loanDirection) {
		this.loanDirection = loanDirection;
	}
	public String getLoanBelong1() {
		return loanBelong1;
	}
	public void setLoanBelong1(String loanBelong1) {
		this.loanBelong1 = loanBelong1;
	}
	public String getLoanBelong2() {
		return loanBelong2;
	}
	public void setLoanBelong2(String loanBelong2) {
		this.loanBelong2 = loanBelong2;
	}
	public String getLoanBelong3() {
		return loanBelong3;
	}
	public void setLoanBelong3(String loanBelong3) {
		this.loanBelong3 = loanBelong3;
	}
	public String getLoanBelong4() {
		return loanBelong4;
	}
	public void setLoanBelong4(String loanBelong4) {
		this.loanBelong4 = loanBelong4;
	}
	public String getBussType() {
		return bussType;
	}
	public void setBussType(String bussType) {
		this.bussType = bussType;
	}
	public String getChiCustManager() {
		return chiCustManager;
	}
	public void setChiCustManager(String chiCustManager) {
		this.chiCustManager = chiCustManager;
	}
	public String getaClientNo() {
		return aClientNo;
	}
	public void setaClientNo(String aClientNo) {
		this.aClientNo = aClientNo;
	}
	public String getClientName() {
		return clientName;
	}
	public void setClientName(String clientName) {
		this.clientName = clientName;
	}
	public String getSex() {
		return sex;
	}
	public void setSex(String sex) {
		this.sex = sex;
	}
	public String getClientType() {
		return clientType;
	}
	public void setClientType(String clientType) {
		this.clientType = clientType;
	}
	public String getGlobalType() {
		return globalType;
	}
	public void setGlobalType(String globalType) {
		this.globalType = globalType;
	}
	public String getGlobalId() {
		return globalId;
	}
	public void setGlobalId(String globalId) {
		this.globalId = globalId;
	}
	public String getLongGlobalType() {
		return longGlobalType;
	}
	public void setLongGlobalType(String longGlobalType) {
		this.longGlobalType = longGlobalType;
	}
	
	public Date getIssDate() {
		return issDate;
	}
	public void setIssDate(Date issDate) {
		this.issDate = issDate;
	}
	public Date getGlobalEffDate() {
		return globalEffDate;
	}
	public void setGlobalEffDate(Date globalEffDate) {
		this.globalEffDate = globalEffDate;
	}
	public String getAgriFlag() {
		return agriFlag;
	}
	public void setAgriFlag(String agriFlag) {
		this.agriFlag = agriFlag;
	}
	public String getCountryCode() {
		return countryCode;
	}
	public void setCountryCode(String countryCode) {
		this.countryCode = countryCode;
	}
	public String getNationalityCode() {
		return nationalityCode;
	}
	public void setNationalityCode(String nationalityCode) {
		this.nationalityCode = nationalityCode;
	}
	public String getRegPermResidence() {
		return regPermResidence;
	}
	public void setRegPermResidence(String regPermResidence) {
		this.regPermResidence = regPermResidence;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public Date getBirthDate() {
		return birthDate;
	}
	public void setBirthDate(Date birthDate) {
		this.birthDate = birthDate;
	}
	public String getEducation() {
		return education;
	}
	public void setEducation(String education) {
		this.education = education;
	}
	public String getDegree() {
		return degree;
	}
	public void setDegree(String degree) {
		this.degree = degree;
	}
	public Date getSignDate() {
		return signDate;
	}
	public void setSignDate(Date signDate) {
		this.signDate = signDate;
	}
	public String getHoldCardMsg() {
		return holdCardMsg;
	}
	public void setHoldCardMsg(String holdCardMsg) {
		this.holdCardMsg = holdCardMsg;
	}
	public String getPassportFlag() {
		return passportFlag;
	}
	public void setPassportFlag(String passportFlag) {
		this.passportFlag = passportFlag;
	}
	public String getCreditLevel() {
		return creditLevel;
	}
	public void setCreditLevel(String creditLevel) {
		this.creditLevel = creditLevel;
	}
	public Date getExpiryDate() {
		return expiryDate;
	}
	public void setExpiryDate(Date expiryDate) {
		this.expiryDate = expiryDate;
	}
	public String getRelClientFlag() {
		return relClientFlag;
	}
	public void setRelClientFlag(String relClientFlag) {
		this.relClientFlag = relClientFlag;
	}
	public String getOwnBranchRelation() {
		return ownBranchRelation;
	}
	public void setOwnBranchRelation(String ownBranchRelation) {
		this.ownBranchRelation = ownBranchRelation;
	}
	public String getPost() {
		return post;
	}
	public void setPost(String post) {
		this.post = post;
	}
	public String getLoanCardFlag() {
		return loanCardFlag;
	}
	public void setLoanCardFlag(String loanCardFlag) {
		this.loanCardFlag = loanCardFlag;
	}
	public String getLoanCardNo() {
		return loanCardNo;
	}
	public void setLoanCardNo(String loanCardNo) {
		this.loanCardNo = loanCardNo;
	}
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	public String getHigherOrgNo() {
		return higherOrgNo;
	}
	public void setHigherOrgNo(String higherOrgNo) {
		this.higherOrgNo = higherOrgNo;
	}
	public String getAcctExec() {
		return acctExec;
	}
	public void setAcctExec(String acctExec) {
		this.acctExec = acctExec;
	}
	public Date getOpenAcctDate() {
		return openAcctDate;
	}
	public void setOpenAcctDate(Date openAcctDate) {
		this.openAcctDate = openAcctDate;
	}
	public String getFeeAmount() {
		return feeAmount;
	}
	public void setFeeAmount(String feeAmount) {
		this.feeAmount = feeAmount;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getLoanKind_1() {
		return loanKind_1;
	}
	public void setLoanKind_1(String loanKind_1) {
		this.loanKind_1 = loanKind_1;
	}
	public String getLoanKind_2() {
		return loanKind_2;
	}
	public void setLoanKind_2(String loanKind_2) {
		this.loanKind_2 = loanKind_2;
	}
	public String getLoanKind_3() {
		return loanKind_3;
	}
	public void setLoanKind_3(String loanKind_3) {
		this.loanKind_3 = loanKind_3;
	}
	public String getLoanKind_4() {
		return loanKind_4;
	}
	public void setLoanKind_4(String loanKind_4) {
		this.loanKind_4 = loanKind_4;
	}
	public String getAgriLoanKind_1() {
		return agriLoanKind_1;
	}
	public void setAgriLoanKind_1(String agriLoanKind_1) {
		this.agriLoanKind_1 = agriLoanKind_1;
	}
	public String getAgriLoanKind_2() {
		return agriLoanKind_2;
	}
	public void setAgriLoanKind_2(String agriLoanKind_2) {
		this.agriLoanKind_2 = agriLoanKind_2;
	}
	public String getAgriLoanKind_3() {
		return agriLoanKind_3;
	}
	public void setAgriLoanKind_3(String agriLoanKind_3) {
		this.agriLoanKind_3 = agriLoanKind_3;
	}
	public String getAgriLoanKind_4() {
		return agriLoanKind_4;
	}
	public void setAgriLoanKind_4(String agriLoanKind_4) {
		this.agriLoanKind_4 = agriLoanKind_4;
	}
	public String getAgriLoanKind_5() {
		return agriLoanKind_5;
	}
	public void setAgriLoanKind_5(String agriLoanKind_5) {
		this.agriLoanKind_5 = agriLoanKind_5;
	}
	public String getLoanDirection_1() {
		return loanDirection_1;
	}
	public void setLoanDirection_1(String loanDirection_1) {
		this.loanDirection_1 = loanDirection_1;
	}
	public String getLoanDirection_2() {
		return loanDirection_2;
	}
	public void setLoanDirection_2(String loanDirection_2) {
		this.loanDirection_2 = loanDirection_2;
	}
	public String getLoanDirection_3() {
		return loanDirection_3;
	}
	public void setLoanDirection_3(String loanDirection_3) {
		this.loanDirection_3 = loanDirection_3;
	}
	public String getLoanDirection_4() {
		return loanDirection_4;
	}
	public void setLoanDirection_4(String loanDirection_4) {
		this.loanDirection_4 = loanDirection_4;
	}
	public String getLoanBelong1_1() {
		return loanBelong1_1;
	}
	public void setLoanBelong1_1(String loanBelong1_1) {
		this.loanBelong1_1 = loanBelong1_1;
	}
	public String getLoanBelong1_2() {
		return loanBelong1_2;
	}
	public void setLoanBelong1_2(String loanBelong1_2) {
		this.loanBelong1_2 = loanBelong1_2;
	}
	public String getLoanBelong1_3() {
		return loanBelong1_3;
	}
	public void setLoanBelong1_3(String loanBelong1_3) {
		this.loanBelong1_3 = loanBelong1_3;
	}
	public String getLoanBelong1_4() {
		return loanBelong1_4;
	}
	public void setLoanBelong1_4(String loanBelong1_4) {
		this.loanBelong1_4 = loanBelong1_4;
	}
	public String getLoanBelong1_5() {
		return loanBelong1_5;
	}
	public void setLoanBelong1_5(String loanBelong1_5) {
		this.loanBelong1_5 = loanBelong1_5;
	}
	public String getRegPermResidence_1() {
		return regPermResidence_1;
	}
	public void setRegPermResidence_1(String regPermResidence_1) {
		this.regPermResidence_1 = regPermResidence_1;
	}
	public String getRegPermResidence_2() {
		return regPermResidence_2;
	}
	public void setRegPermResidence_2(String regPermResidence_2) {
		this.regPermResidence_2 = regPermResidence_2;
	}
	public String getRegPermResidence_3() {
		return regPermResidence_3;
	}
	public void setRegPermResidence_3(String regPermResidence_3) {
		this.regPermResidence_3 = regPermResidence_3;
	}
	public String getFeeCcy() {
		return feeCcy;
	}
	public void setFeeCcy(String feeCcy) {
		this.feeCcy = feeCcy;
	}
	public String getFeeAcctNo() {
		return feeAcctNo;
	}
	public void setFeeAcctNo(String feeAcctNo) {
		this.feeAcctNo = feeAcctNo;
	}
	public String getAcctNo1() {
		return acctNo1;
	}
	public void setAcctNo1(String acctNo1) {
		this.acctNo1 = acctNo1;
	}
	public String getAcctNo2() {
		return acctNo2;
	}
	public void setAcctNo2(String acctNo2) {
		this.acctNo2 = acctNo2;
	}
	public String getCustomerId() {
		return customerId;
	}
	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}
	public String getApplicationId() {
		return applicationId;
	}
	public void setApplicationId(String applicationId) {
		this.applicationId = applicationId;
	}
	public String getRetMsg() {
		return retMsg;
	}
	public void setRetMsg(String retMsg) {
		this.retMsg = retMsg;
	}
	public String getRetCode() {
		return retCode;
	}
	public void setRetCode(String retCode) {
		this.retCode = retCode;
	}
	public String getRetContno() {
		return retContno;
	}
	public void setRetContno(String retContno) {
		this.retContno = retContno;
	}
	public String getLoanStatus() {
		return loanStatus;
	}
	public void setLoanStatus(String loanStatus) {
		this.loanStatus = loanStatus;
	}
}
