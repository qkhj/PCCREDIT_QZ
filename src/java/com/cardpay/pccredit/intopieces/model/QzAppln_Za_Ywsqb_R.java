package com.cardpay.pccredit.intopieces.model;

import com.wicresoft.jrad.base.database.model.BusinessModel;
import com.wicresoft.jrad.base.database.model.ModelParam;

@ModelParam(table = "qz_appln_za_ywsqb_r")
public class QzAppln_Za_Ywsqb_R extends BusinessModel {

	private static final long serialVersionUID = 1L;
	
	private String customerId;
	private String applicationId;
	private String productType;
	private String zaId;
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
	public String getProductType() {
		return productType;
	}
	public void setProductType(String productType) {
		this.productType = productType;
	}
	public String getZaId() {
		return zaId;
	}
	public void setZaId(String zaId) {
		this.zaId = zaId;
	}
	
}
