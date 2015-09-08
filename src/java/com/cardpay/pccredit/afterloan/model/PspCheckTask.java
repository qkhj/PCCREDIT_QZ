package com.cardpay.pccredit.afterloan.model;

import com.wicresoft.jrad.base.database.model.BusinessModel;
import com.wicresoft.jrad.base.database.model.ModelParam;
/**
 * 贷后检查任务表
 * @author chinh
 *
 */
@ModelParam(table = "psp_check_task")
public class PspCheckTask extends BusinessModel{
	
	private static final long serialVersionUID = 1L;
	
	private String taskId;
	private String cusId;
	private String taskType;
	private String taskCreateDate;
	private String taskRequestTime;
	private Integer qnt;
	private Double LoanTotlAmt;
	private Double loanBalance;
	private String taskHuser;
	private String taskHorg;
	private String managerId;
	private String managerBrId;
	private String checkTime;
	private String checkAddr;
	private String agreedPerson;
	private String remarks;
	private String approveStatus;
	private String industryOutlook;
	private String repayment;
	private String reciprocalType;//回访类型
	private String contactInformation;
	private String repaymentOther;//还款情况说明
	
	public String getTaskId() {
		return taskId;
	}
	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}
	public String getCusId() {
		return cusId;
	}
	public void setCusId(String cusId) {
		this.cusId = cusId;
	}
	public String getTaskType() {
		return taskType;
	}
	public void setTaskType(String taskType) {
		this.taskType = taskType;
	}
	public String getTaskCreateDate() {
		return taskCreateDate;
	}
	public void setTaskCreateDate(String taskCreateDate) {
		this.taskCreateDate = taskCreateDate;
	}
	public String getTaskRequestTime() {
		return taskRequestTime;
	}
	public void setTaskRequestTime(String taskRequestTime) {
		this.taskRequestTime = taskRequestTime;
	}
	public Integer getQnt() {
		return qnt;
	}
	public void setQnt(Integer qnt) {
		this.qnt = qnt;
	}
	public Double getLoanTotlAmt() {
		return LoanTotlAmt;
	}
	public void setLoanTotlAmt(Double loanTotlAmt) {
		LoanTotlAmt = loanTotlAmt;
	}
	public Double getLoanBalance() {
		return loanBalance;
	}
	public void setLoanBalance(Double loanBalance) {
		this.loanBalance = loanBalance;
	}
	public String getTaskHuser() {
		return taskHuser;
	}
	public void setTaskHuser(String taskHuser) {
		this.taskHuser = taskHuser;
	}
	public String getTaskHorg() {
		return taskHorg;
	}
	public void setTaskHorg(String taskHorg) {
		this.taskHorg = taskHorg;
	}
	public String getManagerId() {
		return managerId;
	}
	public void setManagerId(String managerId) {
		this.managerId = managerId;
	}
	public String getManagerBrId() {
		return managerBrId;
	}
	public void setManagerBrId(String managerBrId) {
		this.managerBrId = managerBrId;
	}
	public String getCheckTime() {
		return checkTime;
	}
	public void setCheckTime(String checkTime) {
		this.checkTime = checkTime;
	}
	public String getCheckAddr() {
		return checkAddr;
	}
	public void setCheckAddr(String checkAddr) {
		this.checkAddr = checkAddr;
	}
	public String getAgreedPerson() {
		return agreedPerson;
	}
	public void setAgreedPerson(String agreedPerson) {
		this.agreedPerson = agreedPerson;
	}
	public String getRemarks() {
		return remarks;
	}
	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}
	public String getApproveStatus() {
		return approveStatus;
	}
	public void setApproveStatus(String approveStatus) {
		this.approveStatus = approveStatus;
	}
	public String getIndustryOutlook() {
		return industryOutlook;
	}
	public void setIndustryOutlook(String industryOutlook) {
		this.industryOutlook = industryOutlook;
	}
	public String getRepayment() {
		return repayment;
	}
	public void setRepayment(String repayment) {
		this.repayment = repayment;
	}
	public String getReciprocalType() {
		return reciprocalType;
	}
	public void setReciprocalType(String reciprocalType) {
		this.reciprocalType = reciprocalType;
	}
	public String getContactInformation() {
		return contactInformation;
	}
	public void setContactInformation(String contactInformation) {
		this.contactInformation = contactInformation;
	}
	public String getRepaymentOther() {
		return repaymentOther;
	}
	public void setRepaymentOther(String repaymentOther) {
		this.repaymentOther = repaymentOther;
	}
	
	
	
}
