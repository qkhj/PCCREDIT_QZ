package com.cardpay.pccredit.report.model;

import com.wicresoft.jrad.base.database.model.BusinessModel;

/**
 * @author chinh
 *
 * 2015-8-1上午17:05:27
 */
public class AccLoanOverdueInfo extends BusinessModel{
	
	private static final long serialVersionUID = -7513388150092036959L;
	private String rowIndex;//序号
	private String managerId;//客户经理号
	private String orgName;//所属机构
	private String billNo;//借据号
	private String acctName;//账号名称
	private Double realityIrY;//利率
	private String contStartDate;//贷款日期
	private String contEndDate;//到期日期
	private Double contAmt;//授信金额
	private Double loanAmt;//贷款金额
	private Double intAccum;//欠息总额
	private String qixiDate;//起息日期
	private String accStatus;//贷款状态
	private Double overdueMoney;//累计逾期金额
	private String overdue;//逾期期数
	private String clientName;//客户名称
	private Double loanBalance;//贷款余额
	
	public String getRowIndex() {
		return rowIndex;
	}
	public void setRowIndex(String rowIndex) {
		this.rowIndex = rowIndex;
	}
	public String getManagerId() {
		return managerId;
	}
	public void setManagerId(String managerId) {
		this.managerId = managerId;
	}
	public String getOrgName() {
		return orgName;
	}
	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}
	public String getBillNo() {
		return billNo;
	}
	public void setBillNo(String billNo) {
		this.billNo = billNo;
	}
	public String getContStartDate() {
		return contStartDate;
	}
	public void setContStartDate(String contStartDate) {
		this.contStartDate = contStartDate;
	}
	public String getContEndDate() {
		return contEndDate;
	}
	public void setContEndDate(String contEndDate) {
		this.contEndDate = contEndDate;
	}
	public String getQixiDate() {
		return qixiDate;
	}
	public void setQixiDate(String qixiDate) {
		this.qixiDate = qixiDate;
	}
	public String getAccStatus() {
		return accStatus;
	}
	public void setAccStatus(String accStatus) {
		this.accStatus = accStatus;
	}
	public String getClientName() {
		return clientName;
	}
	public void setClientName(String clientName) {
		this.clientName = clientName;
	}
	public String getOverdue() {
		return overdue;
	}
	public void setOverdue(String overdue) {
		this.overdue = overdue;
	}
	public Double getRealityIrY() {
		return realityIrY;
	}
	public void setRealityIrY(Double realityIrY) {
		this.realityIrY = realityIrY;
	}
	public Double getContAmt() {
		return contAmt;
	}
	public void setContAmt(Double contAmt) {
		this.contAmt = contAmt;
	}
	public Double getLoanAmt() {
		return loanAmt;
	}
	public void setLoanAmt(Double loanAmt) {
		this.loanAmt = loanAmt;
	}
	public Double getIntAccum() {
		return intAccum;
	}
	public void setIntAccum(Double intAccum) {
		this.intAccum = intAccum;
	}
	public Double getOverdueMoney() {
		return overdueMoney;
	}
	public void setOverdueMoney(Double overdueMoney) {
		this.overdueMoney = overdueMoney;
	}
	public String getAcctName() {
		return acctName;
	}
	public void setAcctName(String acctName) {
		this.acctName = acctName;
	}
	public Double getLoanBalance() {
		return loanBalance;
	}
	public void setLoanBalance(Double loanBalance) {
		this.loanBalance = loanBalance;
	}
	
}
