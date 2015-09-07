/*
 * Copyright 2013 Wicresoft, Inc. All rights reserved.
 */

package com.cardpay.pccredit.intopieces.web;

import java.util.Date;

import com.cardpay.pccredit.intopieces.model.QzApplnJyxx;
import com.cardpay.pccredit.intopieces.model.QzApplnYwsqb;
import com.wicresoft.jrad.base.database.model.BaseModel;
import com.wicresoft.jrad.base.web.form.BaseForm;

public class QzApplnYwsqbForm extends BaseForm{

	private static final long serialVersionUID = 1L;
	private String applicationId;
	private String orgName;
	private String orgId;
	private Date applyTime;
	private String name;
	private String sex;
	private String globalId;
	private String globalType;
	private String globalTypeOther;
	private String educationLevel;
	private String educationLevelOther;
	private String maritalStatus;
	private String maritalStatusOther;
	private String maritalName;
	private String maritalGlobalType;
	private String maritalGlobalTypeOther;
	private String maritalGlobalId;
	private String maritalWorkunit;
	private String maritalPhone;
	private String familyNum;
	private String addressType;
	private String addressTypeOther;
	private String address;
	private String phone_1;
	private String phone_2;
	private String homePhone;
	private String bussName;
	private String bussScope;
	private String bussStartYear;
	private String bussAddrType;
	private String bussAddrTypeOther;
	private String bussAddr;
	private String bussPhone;
	private String bussEmployeeNum;
	private String bussType;
	private String bussTypeOther;
	private String applyAmount;
	private String applyDeadline;
	private String applyUse;
	private Date applyEndTime;
	private String monthRepay;
	private String guntThing;
	private String guntPeople;
	private String yearIncome;
	private String profitType;
	private String profit;
	private String totalAssets;
	private String inMoney;
	private String outMoney;
	private String otherOut;
	private String monthOtherIncome;
	private String otherIncomeSrc;
	private String borrowHistory;
	private String haveGunt;
	private String thing_1;
	private String thing_2;
	private String thing_3;
	private String thing_4;
	private String loanUser_1;
	private String loanUser_2;
	private String loanUser_3;
	private String loanUser_4;
	private String guntForOther;
	private String guntForOtherBankname;
	private String guntForOtherClientname;
	private String haveApplyLoan;
	private Date haveLoanTime;
	private String haveElePro;
	private String haveEleProType;
	private String haveGotLoan;
	private String haveGotLoanName;
	private String haveGotLoanRelation;
	private String sign;
	private Date signDate;
	private String infoType;
	private String infoTypeOther;
	private String commet;
	private String managerName;
	private String managerId;
	private Date managerTime;
	private String email;
	private String unit;
	private String profession;
	private String occupation;
	private String jobPost;
	private String positionTitle;
	private String workYear;
	private String unitPhone;
	private String unitAddress;
	private String companySize;
	private String unitType;
	private String bussdistrictAddress;
	private String bussdisAddDetail;
	
	public String getApplicationId() {
		return applicationId;
	}
	public void setApplicationId(String applicationId) {
		this.applicationId = applicationId;
	}
	public String getOrgName() {
		return orgName;
	}
	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}
	public String getOrgId() {
		return orgId;
	}
	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}
	public Date getApplyTime() {
		return applyTime;
	}
	public void setApplyTime(Date applyTime) {
		this.applyTime = applyTime;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getSex() {
		return sex;
	}
	public void setSex(String sex) {
		this.sex = sex;
	}
	public String getGlobalId() {
		return globalId;
	}
	public void setGlobalId(String globalId) {
		this.globalId = globalId;
	}
	public String getGlobalType() {
		return globalType;
	}
	public void setGlobalType(String globalType) {
		this.globalType = globalType;
	}
	public String getGlobalTypeOther() {
		return globalTypeOther;
	}
	public void setGlobalTypeOther(String globalTypeOther) {
		this.globalTypeOther = globalTypeOther;
	}
	public String getEducationLevel() {
		return educationLevel;
	}
	public void setEducationLevel(String educationLevel) {
		this.educationLevel = educationLevel;
	}
	public String getEducationLevelOther() {
		return educationLevelOther;
	}
	public void setEducationLevelOther(String educationLevelOther) {
		this.educationLevelOther = educationLevelOther;
	}
	public String getMaritalStatus() {
		return maritalStatus;
	}
	public void setMaritalStatus(String maritalStatus) {
		this.maritalStatus = maritalStatus;
	}
	public String getMaritalStatusOther() {
		return maritalStatusOther;
	}
	public void setMaritalStatusOther(String maritalStatusOther) {
		this.maritalStatusOther = maritalStatusOther;
	}
	public String getMaritalName() {
		return maritalName;
	}
	public void setMaritalName(String maritalName) {
		this.maritalName = maritalName;
	}
	public String getMaritalGlobalType() {
		return maritalGlobalType;
	}
	public void setMaritalGlobalType(String maritalGlobalType) {
		this.maritalGlobalType = maritalGlobalType;
	}
	public String getMaritalGlobalTypeOther() {
		return maritalGlobalTypeOther;
	}
	public void setMaritalGlobalTypeOther(String maritalGlobalTypeOther) {
		this.maritalGlobalTypeOther = maritalGlobalTypeOther;
	}
	public String getMaritalGlobalId() {
		return maritalGlobalId;
	}
	public void setMaritalGlobalId(String maritalGlobalId) {
		this.maritalGlobalId = maritalGlobalId;
	}
	public String getMaritalWorkunit() {
		return maritalWorkunit;
	}
	public void setMaritalWorkunit(String maritalWorkunit) {
		this.maritalWorkunit = maritalWorkunit;
	}
	public String getMaritalPhone() {
		return maritalPhone;
	}
	public void setMaritalPhone(String maritalPhone) {
		this.maritalPhone = maritalPhone;
	}
	public String getFamilyNum() {
		return familyNum;
	}
	public void setFamilyNum(String familyNum) {
		this.familyNum = familyNum;
	}
	public String getAddressType() {
		return addressType;
	}
	public void setAddressType(String addressType) {
		this.addressType = addressType;
	}
	public String getAddressTypeOther() {
		return addressTypeOther;
	}
	public void setAddressTypeOther(String addressTypeOther) {
		this.addressTypeOther = addressTypeOther;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getPhone_1() {
		return phone_1;
	}
	public void setPhone_1(String phone_1) {
		this.phone_1 = phone_1;
	}
	public String getPhone_2() {
		return phone_2;
	}
	public void setPhone_2(String phone_2) {
		this.phone_2 = phone_2;
	}
	public String getHomePhone() {
		return homePhone;
	}
	public void setHomePhone(String homePhone) {
		this.homePhone = homePhone;
	}
	public String getBussName() {
		return bussName;
	}
	public void setBussName(String bussName) {
		this.bussName = bussName;
	}
	public String getBussScope() {
		return bussScope;
	}
	public void setBussScope(String bussScope) {
		this.bussScope = bussScope;
	}
	public String getBussStartYear() {
		return bussStartYear;
	}
	public void setBussStartYear(String bussStartYear) {
		this.bussStartYear = bussStartYear;
	}
	public String getBussAddrType() {
		return bussAddrType;
	}
	public void setBussAddrType(String bussAddrType) {
		this.bussAddrType = bussAddrType;
	}
	public String getBussAddrTypeOther() {
		return bussAddrTypeOther;
	}
	public void setBussAddrTypeOther(String bussAddrTypeOther) {
		this.bussAddrTypeOther = bussAddrTypeOther;
	}
	public String getBussAddr() {
		return bussAddr;
	}
	public void setBussAddr(String bussAddr) {
		this.bussAddr = bussAddr;
	}
	public String getBussPhone() {
		return bussPhone;
	}
	public void setBussPhone(String bussPhone) {
		this.bussPhone = bussPhone;
	}
	public String getBussEmployeeNum() {
		return bussEmployeeNum;
	}
	public void setBussEmployeeNum(String bussEmployeeNum) {
		this.bussEmployeeNum = bussEmployeeNum;
	}
	public String getBussType() {
		return bussType;
	}
	public void setBussType(String bussType) {
		this.bussType = bussType;
	}
	public String getBussTypeOther() {
		return bussTypeOther;
	}
	public void setBussTypeOther(String bussTypeOther) {
		this.bussTypeOther = bussTypeOther;
	}
	public String getApplyAmount() {
		return applyAmount;
	}
	public void setApplyAmount(String applyAmount) {
		this.applyAmount = applyAmount;
	}
	public String getApplyDeadline() {
		return applyDeadline;
	}
	public void setApplyDeadline(String applyDeadline) {
		this.applyDeadline = applyDeadline;
	}
	public String getApplyUse() {
		return applyUse;
	}
	public void setApplyUse(String applyUse) {
		this.applyUse = applyUse;
	}
	public Date getApplyEndTime() {
		return applyEndTime;
	}
	public void setApplyEndTime(Date applyEndTime) {
		this.applyEndTime = applyEndTime;
	}
	public String getMonthRepay() {
		return monthRepay;
	}
	public void setMonthRepay(String monthRepay) {
		this.monthRepay = monthRepay;
	}
	public String getGuntThing() {
		return guntThing;
	}
	public void setGuntThing(String guntThing) {
		this.guntThing = guntThing;
	}
	public String getGuntPeople() {
		return guntPeople;
	}
	public void setGuntPeople(String guntPeople) {
		this.guntPeople = guntPeople;
	}
	public String getYearIncome() {
		return yearIncome;
	}
	public void setYearIncome(String yearIncome) {
		this.yearIncome = yearIncome;
	}
	public String getProfitType() {
		return profitType;
	}
	public void setProfitType(String profitType) {
		this.profitType = profitType;
	}
	public String getProfit() {
		return profit;
	}
	public void setProfit(String profit) {
		this.profit = profit;
	}
	public String getTotalAssets() {
		return totalAssets;
	}
	public void setTotalAssets(String totalAssets) {
		this.totalAssets = totalAssets;
	}
	public String getInMoney() {
		return inMoney;
	}
	public void setInMoney(String inMoney) {
		this.inMoney = inMoney;
	}
	public String getOutMoney() {
		return outMoney;
	}
	public void setOutMoney(String outMoney) {
		this.outMoney = outMoney;
	}
	public String getOtherOut() {
		return otherOut;
	}
	public void setOtherOut(String otherOut) {
		this.otherOut = otherOut;
	}
	public String getMonthOtherIncome() {
		return monthOtherIncome;
	}
	public void setMonthOtherIncome(String monthOtherIncome) {
		this.monthOtherIncome = monthOtherIncome;
	}
	public String getOtherIncomeSrc() {
		return otherIncomeSrc;
	}
	public void setOtherIncomeSrc(String otherIncomeSrc) {
		this.otherIncomeSrc = otherIncomeSrc;
	}
	public String getBorrowHistory() {
		return borrowHistory;
	}
	public void setBorrowHistory(String borrowHistory) {
		this.borrowHistory = borrowHistory;
	}
	public String getHaveGunt() {
		return haveGunt;
	}
	public void setHaveGunt(String haveGunt) {
		this.haveGunt = haveGunt;
	}
	public String getThing_1() {
		return thing_1;
	}
	public void setThing_1(String thing_1) {
		this.thing_1 = thing_1;
	}
	public String getThing_2() {
		return thing_2;
	}
	public void setThing_2(String thing_2) {
		this.thing_2 = thing_2;
	}
	public String getThing_3() {
		return thing_3;
	}
	public void setThing_3(String thing_3) {
		this.thing_3 = thing_3;
	}
	public String getThing_4() {
		return thing_4;
	}
	public void setThing_4(String thing_4) {
		this.thing_4 = thing_4;
	}
	public String getLoanUser_1() {
		return loanUser_1;
	}
	public void setLoanUser_1(String loanUser_1) {
		this.loanUser_1 = loanUser_1;
	}
	public String getLoanUser_2() {
		return loanUser_2;
	}
	public void setLoanUser_2(String loanUser_2) {
		this.loanUser_2 = loanUser_2;
	}
	public String getLoanUser_3() {
		return loanUser_3;
	}
	public void setLoanUser_3(String loanUser_3) {
		this.loanUser_3 = loanUser_3;
	}
	public String getLoanUser_4() {
		return loanUser_4;
	}
	public void setLoanUser_4(String loanUser_4) {
		this.loanUser_4 = loanUser_4;
	}
	public String getGuntForOther() {
		return guntForOther;
	}
	public void setGuntForOther(String guntForOther) {
		this.guntForOther = guntForOther;
	}
	public String getGuntForOtherBankname() {
		return guntForOtherBankname;
	}
	public void setGuntForOtherBankname(String guntForOtherBankname) {
		this.guntForOtherBankname = guntForOtherBankname;
	}
	public String getGuntForOtherClientname() {
		return guntForOtherClientname;
	}
	public void setGuntForOtherClientname(String guntForOtherClientname) {
		this.guntForOtherClientname = guntForOtherClientname;
	}
	public String getHaveApplyLoan() {
		return haveApplyLoan;
	}
	public void setHaveApplyLoan(String haveApplyLoan) {
		this.haveApplyLoan = haveApplyLoan;
	}
	public Date getHaveLoanTime() {
		return haveLoanTime;
	}
	public void setHaveLoanTime(Date haveLoanTime) {
		this.haveLoanTime = haveLoanTime;
	}
	public String getHaveElePro() {
		return haveElePro;
	}
	public void setHaveElePro(String haveElePro) {
		this.haveElePro = haveElePro;
	}
	public String getHaveEleProType() {
		return haveEleProType;
	}
	public void setHaveEleProType(String haveEleProType) {
		this.haveEleProType = haveEleProType;
	}
	public String getHaveGotLoan() {
		return haveGotLoan;
	}
	public void setHaveGotLoan(String haveGotLoan) {
		this.haveGotLoan = haveGotLoan;
	}
	public String getHaveGotLoanName() {
		return haveGotLoanName;
	}
	public void setHaveGotLoanName(String haveGotLoanName) {
		this.haveGotLoanName = haveGotLoanName;
	}
	public String getHaveGotLoanRelation() {
		return haveGotLoanRelation;
	}
	public void setHaveGotLoanRelation(String haveGotLoanRelation) {
		this.haveGotLoanRelation = haveGotLoanRelation;
	}
	public String getSign() {
		return sign;
	}
	public void setSign(String sign) {
		this.sign = sign;
	}
	public Date getSignDate() {
		return signDate;
	}
	public void setSignDate(Date signDate) {
		this.signDate = signDate;
	}
	public String getInfoType() {
		return infoType;
	}
	public void setInfoType(String infoType) {
		this.infoType = infoType;
	}
	public String getInfoTypeOther() {
		return infoTypeOther;
	}
	public void setInfoTypeOther(String infoTypeOther) {
		this.infoTypeOther = infoTypeOther;
	}
	public String getCommet() {
		return commet;
	}
	public void setCommet(String commet) {
		this.commet = commet;
	}
	public String getManagerName() {
		return managerName;
	}
	public void setManagerName(String managerName) {
		this.managerName = managerName;
	}
	public String getManagerId() {
		return managerId;
	}
	public void setManagerId(String managerId) {
		this.managerId = managerId;
	}
	public Date getManagerTime() {
		return managerTime;
	}
	public void setManagerTime(Date managerTime) {
		this.managerTime = managerTime;
	}
	public QzApplnJyxx createModelJyxx() {
		// TODO Auto-generated method stub
		QzApplnJyxx obj = new QzApplnJyxx();
		obj.setBussName(this.getBussName());
		obj.setBussScope(this.getBussScope());
		obj.setBussStartYear(this.getBussStartYear());
		obj.setBussAddrType(this.getBussAddrType());
		obj.setBussAddr(this.getBussAddr());
		obj.setBussAddrTypeOther(this.getBussAddrTypeOther());
		obj.setBussPhone(this.getBussPhone());
		obj.setBussEmployeeNum(this.getBussEmployeeNum());
		obj.setBussType(this.getBussType());
		obj.setBussTypeOther(this.getBussTypeOther());
		return obj;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getUnit() {
		return unit;
	}
	public void setUnit(String unit) {
		this.unit = unit;
	}
	public String getProfession() {
		return profession;
	}
	public void setProfession(String profession) {
		this.profession = profession;
	}
	public String getOccupation() {
		return occupation;
	}
	public void setOccupation(String occupation) {
		this.occupation = occupation;
	}
	public String getJobPost() {
		return jobPost;
	}
	public void setJobPost(String jobPost) {
		this.jobPost = jobPost;
	}
	public String getPositionTitle() {
		return positionTitle;
	}
	public void setPositionTitle(String positionTitle) {
		this.positionTitle = positionTitle;
	}
	public String getWorkYear() {
		return workYear;
	}
	public void setWorkYear(String workYear) {
		this.workYear = workYear;
	}
	public String getUnitAddress() {
		return unitAddress;
	}
	public void setUnitAddress(String unitAddress) {
		this.unitAddress = unitAddress;
	}
	public String getUnitPhone() {
		return unitPhone;
	}
	public void setUnitPhone(String unitPhone) {
		this.unitPhone = unitPhone;
	}
	public String getCompanySize() {
		return companySize;
	}
	public void setCompanySize(String companySize) {
		this.companySize = companySize;
	}
	public String getUnitType() {
		return unitType;
	}
	public void setUnitType(String unitType) {
		this.unitType = unitType;
	}
	public String getBussdistrictAddress() {
		return bussdistrictAddress;
	}
	public void setBussdistrictAddress(String bussdistrictAddress) {
		this.bussdistrictAddress = bussdistrictAddress;
	}
	public String getBussdisAddDetail() {
		return bussdisAddDetail;
	}
	public void setBussdisAddDetail(String bussdisAddDetail) {
		this.bussdisAddDetail = bussdisAddDetail;
	}
	
}
