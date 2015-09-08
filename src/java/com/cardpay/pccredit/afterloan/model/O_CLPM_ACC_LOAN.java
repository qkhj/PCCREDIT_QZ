package com.cardpay.pccredit.afterloan.model;

import com.wicresoft.jrad.base.database.model.BusinessModel;
import com.wicresoft.jrad.base.database.model.ModelParam;

/**
 * 台账信息表
 * @author chinh
 *
 */
@ModelParam(table = "O_CLPM_ACC_LOAN")
public class O_CLPM_ACC_LOAN extends BusinessModel{
	
	private static final long serialVersionUID = 1L;
	
	private String rowIndex;
	private String seqno;
	private String accDate;
	private String acccYear;
	private String accMon;
	private String prdId;
	private String cusId;
	private String contNo;
	private String billNo;
	private Double loanAmt;
	private Double loanBalance;
	private String distrDate;
	private String endDate;
	private String oriEndDate;
	private Integer postCount;
	private Integer overdue;
	private String separateDate;
	private Double rulingIr;
	private Double irFloatRate;
	private Double irFloatPoint;
	private Double realityIrY;
	private Double compIntBalance;
	private Double innerOweInt;
	private Double outOweInt;
	private Double recIntAccum;
	private Double recvIntAccum;
	private Double normalBalance;
	private Double overdueBalance;
	private Double slackBalance;
	private Double badDbtBalance;
	private String writeoffDate;
	private String payDate;
	private String fiveClass;
	private String twelveClsFlg;
	private String managerBrId;
	private String finaBrId;
	private String accStatus;
	private String curType;
	private String overdueDate;
	private String settlDate;
	private Double overdueRateY;
	private Double defaultRateY;
	private String twelveClassTime;
	private String srcSysId;
	private String odsDateTime;
	public String getSeqno() {
		return seqno;
	}
	public void setSeqno(String seqno) {
		this.seqno = seqno;
	}
	public String getAccDate() {
		return accDate;
	}
	public void setAccDate(String accDate) {
		this.accDate = accDate;
	}
	public String getAcccYear() {
		return acccYear;
	}
	public void setAcccYear(String acccYear) {
		this.acccYear = acccYear;
	}
	public String getAccMon() {
		return accMon;
	}
	public void setAccMon(String accMon) {
		this.accMon = accMon;
	}
	public String getPrdId() {
		return prdId;
	}
	public void setPrdId(String prdId) {
		this.prdId = prdId;
	}
	public String getCusId() {
		return cusId;
	}
	public void setCusId(String cusId) {
		this.cusId = cusId;
	}
	public String getContNo() {
		return contNo;
	}
	public void setContNo(String contNo) {
		this.contNo = contNo;
	}
	public String getBillNo() {
		return billNo;
	}
	public void setBillNo(String billNo) {
		this.billNo = billNo;
	}
	public Double getLoanAmt() {
		return loanAmt;
	}
	public void setLoanAmt(Double loanAmt) {
		this.loanAmt = loanAmt;
	}
	public Double getLoanBalance() {
		return loanBalance;
	}
	public void setLoanBalance(Double loanBalance) {
		this.loanBalance = loanBalance;
	}
	public String getDistrDate() {
		return distrDate;
	}
	public void setDistrDate(String distrDate) {
		this.distrDate = distrDate;
	}
	public String getEndDate() {
		return endDate;
	}
	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}
	public String getOriEndDate() {
		return oriEndDate;
	}
	public void setOriEndDate(String oriEndDate) {
		this.oriEndDate = oriEndDate;
	}
	public Integer getPostCount() {
		return postCount;
	}
	public void setPostCount(Integer postCount) {
		this.postCount = postCount;
	}
	public Integer getOverdue() {
		return overdue;
	}
	public void setOverdue(Integer overdue) {
		this.overdue = overdue;
	}
	public String getSeparateDate() {
		return separateDate;
	}
	public void setSeparateDate(String separateDate) {
		this.separateDate = separateDate;
	}
	public Double getRulingIr() {
		return rulingIr;
	}
	public void setRulingIr(Double rulingIr) {
		this.rulingIr = rulingIr;
	}
	public Double getIrFloatRate() {
		return irFloatRate;
	}
	public void setIrFloatRate(Double irFloatRate) {
		this.irFloatRate = irFloatRate;
	}
	public Double getIrFloatPoint() {
		return irFloatPoint;
	}
	public void setIrFloatPoint(Double irFloatPoint) {
		this.irFloatPoint = irFloatPoint;
	}
	public Double getRealityIrY() {
		return realityIrY;
	}
	public void setRealityIrY(Double realityIrY) {
		this.realityIrY = realityIrY;
	}
	public Double getCompIntBalance() {
		return compIntBalance;
	}
	public void setCompIntBalance(Double compIntBalance) {
		this.compIntBalance = compIntBalance;
	}
	public Double getInnerOweInt() {
		return innerOweInt;
	}
	public void setInnerOweInt(Double innerOweInt) {
		this.innerOweInt = innerOweInt;
	}
	public Double getOutOweInt() {
		return outOweInt;
	}
	public void setOutOweInt(Double outOweInt) {
		this.outOweInt = outOweInt;
	}
	public Double getRecIntAccum() {
		return recIntAccum;
	}
	public void setRecIntAccum(Double recIntAccum) {
		this.recIntAccum = recIntAccum;
	}
	public Double getRecvIntAccum() {
		return recvIntAccum;
	}
	public void setRecvIntAccum(Double recvIntAccum) {
		this.recvIntAccum = recvIntAccum;
	}
	public Double getNormalBalance() {
		return normalBalance;
	}
	public void setNormalBalance(Double normalBalance) {
		this.normalBalance = normalBalance;
	}
	public Double getOverdueBalance() {
		return overdueBalance;
	}
	public void setOverdueBalance(Double overdueBalance) {
		this.overdueBalance = overdueBalance;
	}
	public Double getSlackBalance() {
		return slackBalance;
	}
	public void setSlackBalance(Double slackBalance) {
		this.slackBalance = slackBalance;
	}
	public Double getBadDbtBalance() {
		return badDbtBalance;
	}
	public void setBadDbtBalance(Double badDbtBalance) {
		this.badDbtBalance = badDbtBalance;
	}
	public String getWriteoffDate() {
		return writeoffDate;
	}
	public void setWriteoffDate(String writeoffDate) {
		this.writeoffDate = writeoffDate;
	}
	public String getPayDate() {
		return payDate;
	}
	public void setPayDate(String payDate) {
		this.payDate = payDate;
	}
	public String getFiveClass() {
		return fiveClass;
	}
	public void setFiveClass(String fiveClass) {
		this.fiveClass = fiveClass;
	}
	public String getTwelveClsFlg() {
		return twelveClsFlg;
	}
	public void setTwelveClsFlg(String twelveClsFlg) {
		this.twelveClsFlg = twelveClsFlg;
	}
	public String getManagerBrId() {
		return managerBrId;
	}
	public void setManagerBrId(String managerBrId) {
		this.managerBrId = managerBrId;
	}
	public String getFinaBrId() {
		return finaBrId;
	}
	public void setFinaBrId(String finaBrId) {
		this.finaBrId = finaBrId;
	}
	public String getAccStatus() {
		return accStatus;
	}
	public void setAccStatus(String accStatus) {
		this.accStatus = accStatus;
	}
	public String getCurType() {
		return curType;
	}
	public void setCurType(String curType) {
		this.curType = curType;
	}
	public String getOverdueDate() {
		return overdueDate;
	}
	public void setOverdueDate(String overdueDate) {
		this.overdueDate = overdueDate;
	}
	public String getSettlDate() {
		return settlDate;
	}
	public void setSettlDate(String settlDate) {
		this.settlDate = settlDate;
	}
	public Double getOverdueRateY() {
		return overdueRateY;
	}
	public void setOverdueRateY(Double overdueRateY) {
		this.overdueRateY = overdueRateY;
	}
	public Double getDefaultRateY() {
		return defaultRateY;
	}
	public void setDefaultRateY(Double defaultRateY) {
		this.defaultRateY = defaultRateY;
	}
	public String getTwelveClassTime() {
		return twelveClassTime;
	}
	public void setTwelveClassTime(String twelveClassTime) {
		this.twelveClassTime = twelveClassTime;
	}
	public String getSrcSysId() {
		return srcSysId;
	}
	public void setSrcSysId(String srcSysId) {
		this.srcSysId = srcSysId;
	}
	public String getOdsDateTime() {
		return odsDateTime;
	}
	public void setOdsDateTime(String odsDateTime) {
		this.odsDateTime = odsDateTime;
	}
	public String getRowIndex() {
		return rowIndex;
	}
	public void setRowIndex(String rowIndex) {
		this.rowIndex = rowIndex;
	}
	
}
