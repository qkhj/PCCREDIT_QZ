package com.cardpay.pccredit.intopieces.service;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.cardpay.pccredit.QZBankInterface.model.Circle;
import com.cardpay.pccredit.QZBankInterface.model.ECIF;
import com.cardpay.pccredit.QZBankInterface.service.CircleService;
import com.cardpay.pccredit.QZBankInterface.service.ECIFService;
import com.cardpay.pccredit.QZBankInterface.web.IESBForECIFReturnMap;
import com.cardpay.pccredit.common.UploadFileTool;
import com.cardpay.pccredit.customer.model.CustomerCareersInformation;
import com.cardpay.pccredit.customer.model.CustomerInfor;
import com.cardpay.pccredit.customer.service.CustomerInforService;
import com.cardpay.pccredit.divisional.constant.DivisionalProgressEnum;
import com.cardpay.pccredit.divisional.constant.DivisionalTypeEnum;
import com.cardpay.pccredit.divisional.service.DivisionalService;
import com.cardpay.pccredit.intopieces.constant.ApplicationStatusEnum;
import com.cardpay.pccredit.intopieces.constant.Constant;
import com.cardpay.pccredit.intopieces.dao.CustomerApplicationIntopieceWaitDao;
import com.cardpay.pccredit.intopieces.dao.IntoPiecesDao;
import com.cardpay.pccredit.intopieces.dao.comdao.IntoPiecesComdao;
import com.cardpay.pccredit.intopieces.filter.IntoPiecesFilter;
import com.cardpay.pccredit.intopieces.filter.MakeCardFilter;
import com.cardpay.pccredit.intopieces.model.ApplicationDataImport;
import com.cardpay.pccredit.intopieces.model.BasicCustomerInformationS;
import com.cardpay.pccredit.intopieces.model.CustomerAccountData;
import com.cardpay.pccredit.intopieces.model.CustomerApplicationCom;
import com.cardpay.pccredit.intopieces.model.CustomerApplicationContact;
import com.cardpay.pccredit.intopieces.model.CustomerApplicationGuarantor;
import com.cardpay.pccredit.intopieces.model.CustomerApplicationInfo;
import com.cardpay.pccredit.intopieces.model.CustomerApplicationOther;
import com.cardpay.pccredit.intopieces.model.CustomerApplicationProcess;
import com.cardpay.pccredit.intopieces.model.CustomerApplicationRecom;
import com.cardpay.pccredit.intopieces.model.CustomerCareersInformationS;
import com.cardpay.pccredit.intopieces.model.IntoPieces;
import com.cardpay.pccredit.intopieces.model.MakeCard;
import com.cardpay.pccredit.intopieces.model.QzApplnAttachmentList;
import com.cardpay.pccredit.intopieces.model.QzApplnDbrxx;
import com.cardpay.pccredit.intopieces.model.QzApplnDcnr;
import com.cardpay.pccredit.intopieces.model.QzApplnHtqdtz;
import com.cardpay.pccredit.intopieces.model.QzApplnJyd;
import com.cardpay.pccredit.intopieces.model.QzApplnJydBzdb;
import com.cardpay.pccredit.intopieces.model.QzApplnJydDydb;
import com.cardpay.pccredit.intopieces.model.QzApplnJydGtjkr;
import com.cardpay.pccredit.intopieces.model.QzApplnNbscyjb;
import com.cardpay.pccredit.intopieces.model.QzApplnProcessResult;
import com.cardpay.pccredit.intopieces.model.QzApplnSdhjy;
import com.cardpay.pccredit.intopieces.model.QzApplnSxjc;
import com.cardpay.pccredit.intopieces.model.QzApplnYwsqb;
import com.cardpay.pccredit.intopieces.model.QzAppln_Za_Ywsqb_R;
import com.cardpay.pccredit.intopieces.model.VideoAccessories;
import com.cardpay.pccredit.intopieces.web.ApproveHistoryForm;
import com.cardpay.pccredit.intopieces.web.QzApplnSxjcForm;
import com.cardpay.pccredit.intopieces.web.QzDcnrUploadForm;
import com.cardpay.pccredit.product.model.AddressAccessories;
import com.cardpay.pccredit.system.model.NodeAudit;
import com.cardpay.pccredit.system.model.NodeControl;
import com.cardpay.workflow.dao.WfStatusResultDao;
import com.cardpay.workflow.models.WfProcessRecord;
import com.cardpay.workflow.models.WfStatusQueueRecord;
import com.wicresoft.jrad.base.auth.IUser;
import com.wicresoft.jrad.base.constant.JRadConstants;
import com.wicresoft.jrad.base.database.dao.common.CommonDao;
import com.wicresoft.jrad.base.database.id.IDGenerator;
import com.wicresoft.jrad.base.database.model.BusinessModel;
import com.wicresoft.jrad.base.database.model.QueryResult;
import com.wicresoft.jrad.base.web.security.LoginManager;
import com.wicresoft.util.spring.Beans;

@Service
public class IntoPiecesService {

	// TODO 路径使用相对路径，首先获得应用所在路径，之后建立上传文件目录，图片类型使用IMG，文件使用DOC

	@Autowired
	private CommonDao commonDao;
	
	@Autowired
	private IntoPiecesDao intoPiecesDao;

	@Autowired
	private IntoPiecesComdao intoPiecesComdao;
	
	@Autowired
	private CustomerInforService customerInforService;
	
	@Autowired
	private DivisionalService divisionalService;
	@Autowired
	private CustomerApplicationProcessService customerApplicationProcessService;

	@Autowired
	private CustomerApplicationIntopieceWaitService customerApplicationIntopieceWaitService;
	
	@Autowired
	private CustomerInforService customerInforservice;
	
	@Autowired
	private CustomerApplicationIntopieceWaitDao customerApplicationIntopieceWaitDao;
	
	@Autowired
	private WfStatusResultDao wfStatusResultDao;
	
	@Autowired
	private CircleService circleService;
	@Autowired
	private ECIFService eCIFService;
	
	@Autowired
	private YwsqbService ywsqbService;
	@Autowired
	private DbrxxService dbrxxService;
	@Autowired
	private AttachmentListService attachmentListService;
	@Autowired
	private NbscyjbService nbscyjbService;
	@Autowired
	private ZA_YWSQB_R_Service za_ywsqb_r_service;
	/* 查询进价信息 */
	/*
	 * TODO 1.添加注释 2.SQL写进DAO层
	 */
	public QueryResult<IntoPieces> findintoPiecesByFilter(
			IntoPiecesFilter filter) {
		QueryResult<IntoPieces> queryResult = intoPiecesComdao.findintoPiecesByFilterWF(filter);
		List<IntoPieces> intoPieces = queryResult.getItems();
		for(IntoPieces pieces : intoPieces){
			if(pieces.getStatus().equals(Constant.SAVE_INTOPICES)){
				pieces.setNodeName("未提交申请");
			} else if(pieces.getStatus().equals(Constant.APPROVE_INTOPICES)||pieces.getStatus().equals(Constant.TRTURN_INTOPICES)){
				String nodeName = intoPiecesComdao.findAprroveProgress(pieces.getId());
				if(StringUtils.isNotEmpty(nodeName)){
					pieces.setNodeName(nodeName);
				} else {
					pieces.setNodeName("不在审批中");
				}
			} else if(pieces.getStatus().equals(Constant.RETURN_INTOPICES)||pieces.getStatus().equals(Constant.TRTURN_INTOPICES)){
				pieces.setNodeName("退回");
			} else {
				pieces.setNodeName("审批结束");
			}
		}
		return queryResult;
	}
	
	//中心岗查询所有进件
	public QueryResult<IntoPieces> findintoPiecesAllByFilter(IntoPiecesFilter filter) {
		QueryResult<IntoPieces> queryResult = intoPiecesComdao.findintoPiecesAllByFilter(filter);
		List<IntoPieces> intoPieces = queryResult.getItems();
		for(IntoPieces pieces : intoPieces){
			if(pieces.getStatus().equals(Constant.SAVE_INTOPICES)){
				pieces.setNodeName("未提交申请");
			} else if(pieces.getStatus().equals(Constant.APPROVE_INTOPICES)||pieces.getStatus().equals(Constant.TRTURN_INTOPICES)){
				String nodeName = intoPiecesComdao.findAprroveProgress(pieces.getId());
				if(StringUtils.isNotEmpty(nodeName)){
					pieces.setNodeName(nodeName);
				} else {
					pieces.setNodeName("不在审批中");
				}
			} else if(pieces.getStatus().equals(Constant.RETURN_INTOPICES)){
				pieces.setNodeName("退回");
			} else {
				pieces.setNodeName("审批结束");
			}
		}
		return queryResult;
	}
	
	/*
	 * 查询已经申请通过进件
	 */
	public QueryResult<IntoPieces> findintoApplayPiecesByFilter(
			IntoPiecesFilter filter) {
		return intoPiecesComdao.findintoApplayPiecesByFilter(filter);
	}
	
	/*
	 * 判断经理是否有权操作，否则移交
	 */
	public int checkValue(String userId,String valueType) {
		return intoPiecesDao.checkValue(userId,valueType);
	}
	
	/*
	 * 查询已经申请通过进件
	 */
	public QueryResult<MakeCard> findCardByFilter(
			MakeCardFilter filter) {
		return intoPiecesComdao.findCardByFilter(filter);
	}

	/* 保存客户所有资料(主表及其其他的表) */
	/*
	 * TODO 1.文件上传使用另外的工具类进行操作
	 * 2.文件上传文件与服务器端文件名相同使用删除极为不正确，应每次对文件进行重命名，使用UUID作为文件名
	 * 3.文件上传设置类型后缀枚举类型，对不在枚举中的文件类型不进行上传(不仅仅是前端检查)
	 * 4.使用stringbuffer作为文件缓存，不要使用类似byte[8192]这种 5.注释应标明输入输出，异常抛出类型
	 */
	public void saveAllInfo(List<BusinessModel> list,
			HttpServletRequest request, String userId, String status,
			Map<String, Object> paramMap){
		if (list != null && list.size() > 0) {
			for (BusinessModel obj : list) {
				obj.setCreatedTime(new Date());
				obj.setCreatedBy(userId);
				if (!(obj instanceof AddressAccessories)) {
					if(obj instanceof CustomerInfor){
						CustomerInfor customerInfor = (CustomerInfor)obj;
						/* 判断是否需要移交进件 */
						if ((Boolean) paramMap.get("checkFlag")&&(Boolean)paramMap.get("flag")) {
							divisionalService.insertDivisionalCustomer(
									customerInfor.getId(),
									DivisionalTypeEnum.initiative,
									DivisionalProgressEnum.charge);
						}
					}
					commonDao.insertObject(obj);
				} else if (obj instanceof AddressAccessories) {
					AddressAccessories v = (AddressAccessories) obj;
					String fileName = UploadFileTool.uploadFileBySpring(
							request, v);
					if (StringUtils.trimToNull(fileName) != null) {
						v.setProductAccessoriesUrl(Constant.FILE_PATH
								+ fileName);
					}
					commonDao.insertObject(v);
				}
			}
		}
		/* 生成快照 */
		if (StringUtils.trimToNull(status) != null
				&& Constant.APPROVE_INTOPICES.equalsIgnoreCase(status)
				&& (Boolean)paramMap.get("flag")) {
			this.cloneData(paramMap.get("customerId").toString(),
					paramMap.get("applicationId").toString(), paramMap.get("productId").toString());
			paramMap.put("flag", false);
		}
	}

	/* 更新客户所有信息 */
	public void updateAllInfo(List<BusinessModel> list,
			HttpServletRequest request, String userId, String status,Map<String, Object> paramMap){
		if (list != null && list.size() > 0) {
			for (BusinessModel obj : list) {
				obj.setModifiedTime(new Date());
				obj.setModifiedBy(userId);
				if (!(obj instanceof AddressAccessories)) {
					if(obj instanceof CustomerInfor){
						CustomerInfor customerInfor = (CustomerInfor)obj;
						/* 判断是否需要移交进件 */
						if ((Boolean) paramMap.get("checkFlag")&&(Boolean)paramMap.get("flag")) {
							divisionalService.insertDivisionalCustomer(
									customerInfor.getId(),
									DivisionalTypeEnum.initiative,
									DivisionalProgressEnum.charge);
						}
					}
					commonDao.updateObject(obj);
				} else {
					AddressAccessories v = (AddressAccessories) obj;
					String fileName = UploadFileTool.uploadFileBySpring(
							request, v);
					if (StringUtils.trimToNull(fileName) != null) {
						v.setProductAccessoriesUrl(Constant.FILE_PATH
								+ fileName);
					}
					commonDao.updateObject(v);
				}
			}
		}
		/*生成快照*/
		if (StringUtils.trimToNull(status) != null
				&& Constant.APPROVE_INTOPICES.equalsIgnoreCase(status)&&(Boolean)paramMap.get("flag")) {
			this.cloneData(paramMap.get("customerId").toString(),
					paramMap.get("applicationId").toString(), paramMap.get("productId").toString());
			paramMap.put("flag", false);
		}
	}
	
	
	/* 申请件待审核中维护进件
	 * 注：基本资料 和 客户职业信息资料 更新快照表中数据
	 * 更新客户所有信息 
	 * 
	 * author：王海东
	 * */
	public void updateAllInfoWait(List<BusinessModel> list,
			HttpServletRequest request, String userId, String status,Map<String, Object> paramMap){
		if (list != null && list.size() > 0) {
			for (BusinessModel obj : list) {
				obj.setModifiedTime(new Date());
				obj.setModifiedBy(userId);
				if (!(obj instanceof AddressAccessories)) {
					if(obj instanceof BasicCustomerInformationS){
						BasicCustomerInformationS customerInfor = (BasicCustomerInformationS)obj;
					    this.updateBasicCustomerInformationS(customerInfor);
					}
					if(obj instanceof CustomerCareersInformationS){
						CustomerCareersInformationS customerCareersInformationS = (CustomerCareersInformationS)obj;
					    this.updateCustomerCareersInformationS(customerCareersInformationS);
					}
					commonDao.updateObject(obj);
				} else {
					AddressAccessories v = (AddressAccessories) obj;
					String fileName = UploadFileTool.uploadFileBySpring(
							request, v);
					if (StringUtils.trimToNull(fileName) != null) {
						v.setProductAccessoriesUrl(Constant.FILE_PATH
								+ fileName);
					}
					commonDao.updateObject(v);
				}
			}
		}
	}
	/*
	 * 更新客户基本信息快照表BASIC_CUSTOMER_INFORMATION_S
	 * 
	 */
	public int updateBasicCustomerInformationS(BasicCustomerInformationS basicCustomerInformationS){
		return commonDao.updateObject(basicCustomerInformationS);
	}
	/*
	 * 更新客户职业信息快照表CUSTOMER_CAREERS_INFORMATION_S
	 */
	public int updateCustomerCareersInformationS(CustomerCareersInformationS customerCareersInformationS) {
		return commonDao.updateObject(customerCareersInformationS);
		
	}
	private void cloneData(String customerId,String applicationId,String productId){
		customerInforService.insertCustomerInfoBySubmitApp(
				customerId, applicationId,productId);
	}

	/* 客户证件ID模糊匹配 */
	/*
	 * TODO 1.注释标明输入输出，异常抛出类型 2.SQL写进DAO层
	 */
	public void selectLikeByCardId(HttpServletResponse response,
			String tempParam) throws Exception {
		intoPiecesComdao.selectLikeByCardId(response, tempParam);
	}
	
	/*
	 * TODO 1.注释标明输入输出，异常抛出类型 2.SQL写进DAO层
	 */
	public void delete(String name,String value) throws Exception {
		if(Constant.CONTACTID.equals(name)){
			commonDao.deleteObject(CustomerApplicationContact.class, value);
		}else if(Constant.GUARANTORID.equals(name)){
			commonDao.deleteObject(CustomerApplicationGuarantor.class, value);
		}else if(Constant.RECOMMENDID.equals(name)){
			commonDao.deleteObject(CustomerApplicationRecom.class, value);
		}else{
			throw new Exception("该方法只用来删除联系人，担保人，推荐人");
		}
	}
	
	/*
	 * 导出文本格式的文件并且上传到ftp服务器上
	 */
	public void exportData(String applicationId, String customerId,HttpServletResponse response) throws Exception {
		StringBuffer content = new StringBuffer();
		StringBuffer sb = new StringBuffer();
		List<CustomerApplicationContact> customerApplicationContactList = null;
		List<CustomerApplicationGuarantor> customerApplicationGuarantorList = null;
		List<CustomerApplicationRecom> customerApplicationRecomList = null;
		CustomerInfor customerInfor = this.findCustomerInforById(customerId);
		customerInfor = customerInfor==null?new CustomerInfor():customerInfor;
		CustomerCareersInformation customerCareersInformation = this.findCustomerCareersInformationByCustomerId(customerId);
		customerCareersInformation = customerCareersInformation==null?new CustomerCareersInformation():customerCareersInformation;
		CustomerApplicationInfo customerApplicationInfo = this.findCustomerApplicationInfoByApplicationId(applicationId);
		customerApplicationInfo = customerApplicationInfo==null?new CustomerApplicationInfo():customerApplicationInfo;
		List<CustomerApplicationContact> customerApplicationContactListDB = this.findCustomerApplicationContactsByApplicationId(applicationId);
		customerApplicationContactListDB = customerApplicationContactListDB==null?new ArrayList<CustomerApplicationContact>():customerApplicationContactListDB;
		List<CustomerApplicationGuarantor> customerApplicationGuarantorListDB = this.findCustomerApplicationGuarantorsByApplicationId(applicationId);
		customerApplicationGuarantorListDB = customerApplicationGuarantorListDB==null?new ArrayList<CustomerApplicationGuarantor>():customerApplicationGuarantorListDB;
		List<CustomerApplicationRecom> customerApplicationRecomListDB = this.findCustomerApplicationRecomsByApplicationId(applicationId);
		customerApplicationRecomListDB = customerApplicationRecomListDB==null?new ArrayList<CustomerApplicationRecom>():customerApplicationRecomListDB;
		CustomerApplicationOther customerApplicationOther = this.findCustomerApplicationOtherByApplicationId(applicationId);
		customerApplicationOther = customerApplicationOther==null?new CustomerApplicationOther():customerApplicationOther;
		CustomerAccountData customerAccountData = this.findCustomerAccountDataByApplicationId(applicationId);
		customerAccountData = customerAccountData==null?new CustomerAccountData():customerAccountData;
		CustomerApplicationCom customerApplicationCom = this.findCustomerApplicationComByApplicationId(applicationId);
		customerApplicationCom = customerApplicationCom==null?new CustomerApplicationCom():customerApplicationCom;
		List<ApplicationDataImport> applicationDataImportList = this.findApplicationDataImport();
		if(customerApplicationContactListDB!=null){
			customerApplicationContactList = customerApplicationContactListDB;
			if(customerApplicationContactList.size()!=2){
				for(int i=customerApplicationContactList.size();i<2;i++){
					customerApplicationContactList.add(new CustomerApplicationContact());
				}
			}
		}
		if(customerApplicationGuarantorListDB!=null){
			customerApplicationGuarantorList = customerApplicationGuarantorListDB;
			if(customerApplicationGuarantorList.size()!=2){
				for(int i=customerApplicationGuarantorList.size();i<2;i++){
					customerApplicationGuarantorList.add(new CustomerApplicationGuarantor());
				}
			}
		}
		if(customerApplicationRecomListDB!=null){
			customerApplicationRecomList = customerApplicationRecomListDB;
			if(customerApplicationRecomList.size()!=2){
				for(int i=customerApplicationRecomList.size();i<2;i++){
					customerApplicationRecomList.add(new CustomerApplicationRecom());
				}
			}
		}
		for(int i=0;i<applicationDataImportList.size();i++){
			ApplicationDataImport applicationDataImport = applicationDataImportList.get(i);
			int id = Integer.valueOf(applicationDataImport.getId());
			int length = Integer.valueOf(applicationDataImport.getFieldLength());
			switch(id-1){
			    case 0:content = UploadFileTool.getContent(content,sb.toString(),length);break;
			    case 1:content = UploadFileTool.getContent(content,sb.toString(),length);break;
			    case 2:content = UploadFileTool.getContent(content,sb.toString(),length);break;
			    case 3:content = UploadFileTool.getContent(content,sb.toString(),length);break;
			    case 4:content = UploadFileTool.getContent(content,sb.toString(),length);break;
			    case 5:content = UploadFileTool.getContent(content,sb.toString(),length);break;
			    case 6:content = UploadFileTool.getContent(content,sb.toString(),length);break;
			    case 7:content = UploadFileTool.getContent(content,sb.toString(),length);break;
			    case 8:content = UploadFileTool.getContent(content,sb.toString(),length);break;
			    case 9:content = UploadFileTool.getContent(content,sb.toString(),length);break;
			    case 10:content = UploadFileTool.getContent(content,customerInfor.getCardType(),length);break;
			    case 11:content = UploadFileTool.getContent(content,customerInfor.getCardId(),length);break;
			    case 12:content = UploadFileTool.getContent(content,sb.toString(),length);break;
			    case 13:content = UploadFileTool.getContent(content,customerInfor.getChineseName(),length);break;
			    case 14:content = UploadFileTool.getContent(content,customerInfor.getPinyinenglishName(),length);break;
			    case 15:content = UploadFileTool.getContent(content,customerInfor.getSex(),length);break;
			    case 16:content = UploadFileTool.getContent(content,customerInfor.getBirthday(),length);break;
			    case 17:content = UploadFileTool.getContent(content,customerInfor.getNationality(),length);break;
			    case 18:content = UploadFileTool.getContent(content,customerInfor.getMaritalStatus(),length);break;
			    case 19:content = UploadFileTool.getContent(content,customerInfor.getDegreeEducation(),length);break;
			    case 20:content = UploadFileTool.getContent(content,customerCareersInformation.getTitle(),length);break;
			    case 21:content = UploadFileTool.getContent(content,customerInfor.getHomePhone(),length);break;
			    case 22:content = UploadFileTool.getContent(content,customerInfor.getTelephone(),length);break;
			    case 23:content = UploadFileTool.getContent(content,customerInfor.getMail(),length);break;
			    case 24:content = UploadFileTool.getContent(content,sb.toString(),length);break;
			    case 25:content = UploadFileTool.getContent(content,customerInfor.getResidentialAddress(),length);break;
			    case 26:content = UploadFileTool.getContent(content,sb.toString(),length);break;
			    case 27:content = UploadFileTool.getContent(content,sb.toString(),length);break;
			    case 28:content = UploadFileTool.getContent(content,sb.toString(),length);break;
			    case 29:content = UploadFileTool.getContent(content,sb.toString(),length);break;
			    case 30:content = UploadFileTool.getContent(content,customerInfor.getResidentialPropertie(),length);break;
			    case 31:content = UploadFileTool.getContent(content,sb.toString(),length);break;
			    case 32:content = UploadFileTool.getContent(content,sb.toString(),length);break;
			    case 33:content = UploadFileTool.getContent(content,sb.toString(),length);break;
			    case 34:content = UploadFileTool.getContent(content,sb.toString(),length);break;
			    case 35:content = UploadFileTool.getContent(content,sb.toString(),length);break;
			    case 36:content = UploadFileTool.getContent(content,sb.toString(),length);break;
			    case 37:content = UploadFileTool.getContent(content,sb.toString(),length);break;
			    case 38:content = UploadFileTool.getContent(content,sb.toString(),length);break;
			    case 39:content = UploadFileTool.getContent(content,sb.toString(),length);break;
			    case 40:content = UploadFileTool.getContent(content,sb.toString(),length);break;
			    case 41:content = UploadFileTool.getContent(content,sb.toString(),length);break;
			    case 42:content = UploadFileTool.getContent(content,sb.toString(),length);break;
			    case 43:content = UploadFileTool.getContent(content,sb.toString(),length);break;
			    case 44:content = UploadFileTool.getContent(content,sb.toString(),length);break;
			    case 45:content = UploadFileTool.getContent(content,sb.toString(),length);break;
			    case 46:content = UploadFileTool.getContent(content,sb.toString(),length);break;
			    case 47:content = UploadFileTool.getContent(content,sb.toString(),length);break;
			    case 48:content = UploadFileTool.getContent(content,sb.toString(),length);break;
			    case 49:content = UploadFileTool.getContent(content,sb.toString(),length);break;
			    case 50:content = UploadFileTool.getContent(content,sb.toString(),length);break;
			    case 51:content = UploadFileTool.getContent(content,sb.toString(),length);break;
			    case 52:content = UploadFileTool.getContent(content,sb.toString(),length);break;
			    case 53:content = UploadFileTool.getContent(content,sb.toString(),length);break;
			    case 54:content = UploadFileTool.getContent(content,customerCareersInformation.getPositio(),length);break;
			    case 55:content = UploadFileTool.getContent(content,sb.toString(),length);break;
			    case 56:content = UploadFileTool.getContent(content,sb.toString(),length);break;
			    case 57:content = UploadFileTool.getContent(content,sb.toString(),length);break;
			    case 58:content = UploadFileTool.getContent(content,sb.toString(),length);break;
			    case 59:content = UploadFileTool.getContent(content,sb.toString(),length);break;
			    case 60:content = UploadFileTool.getContent(content,sb.toString(),length);break;
			    case 61:content = UploadFileTool.getContent(content,sb.toString(),length);break;
			    case 62:content = UploadFileTool.getContent(content,sb.toString(),length);break;
			    case 63:content = UploadFileTool.getContent(content,sb.toString(),length);break;
			    case 64:content = UploadFileTool.getContent(content,sb.toString(),length);break;
			    case 65:content = UploadFileTool.getContent(content,sb.toString(),length);break;
			    case 66:content = UploadFileTool.getContent(content,sb.toString(),length);break;
			    case 67:content = UploadFileTool.getContent(content,sb.toString(),length);break;
			    case 68:content = UploadFileTool.getContent(content,sb.toString(),length);break;
			    case 69:content = UploadFileTool.getContent(content,sb.toString(),length);break;
			    case 70:content = UploadFileTool.getContent(content,sb.toString(),length);break;
			    case 71:content = UploadFileTool.getContent(content,sb.toString(),length);break;
			    case 72:content = UploadFileTool.getContent(content,customerApplicationContactList.get(0).getContactName(),length);break;
			    case 73:content = UploadFileTool.getContent(content,customerApplicationContactList.get(0).getContactPhone(),length);break;
			    case 74:content = UploadFileTool.getContent(content,customerApplicationContactList.get(0).getRelationshipWithApplicant(),length);break;
			    case 75:content = UploadFileTool.getContent(content,customerApplicationContactList.get(0).getUnitName(),length);break;
			    case 76:content = UploadFileTool.getContent(content,customerApplicationContactList.get(0).getCellPhone(),length);break;
			    case 77:content = UploadFileTool.getContent(content,customerApplicationContactList.get(1).getContactName(),length);break;
			    case 78:content = UploadFileTool.getContent(content,customerApplicationContactList.get(1).getContactPhone(),length);break;
			    case 79:content = UploadFileTool.getContent(content,customerApplicationContactList.get(1).getRelationshipWithApplicant(),length);break;
			    case 80:content = UploadFileTool.getContent(content,customerApplicationContactList.get(1).getUnitName(),length);break;
			    case 81:content = UploadFileTool.getContent(content,customerApplicationContactList.get(1).getCellPhone(),length);break;
			    case 82:content = UploadFileTool.getContent(content,sb.toString(),length);break;
			    case 83:content = UploadFileTool.getContent(content,sb.toString(),length);break;
			    case 84:content = UploadFileTool.getContent(content,sb.toString(),length);break;
			    case 85:content = UploadFileTool.getContent(content,sb.toString(),length);break;
			    case 86:content = UploadFileTool.getContent(content,sb.toString(),length);break;
			    case 87:content = UploadFileTool.getContent(content,sb.toString(),length);break;
			    case 88:content = UploadFileTool.getContent(content,sb.toString(),length);break;
			    case 89:content = UploadFileTool.getContent(content,sb.toString(),length);break;
			    case 90:content = UploadFileTool.getContent(content,sb.toString(),length);break;
			    case 91:content = UploadFileTool.getContent(content,sb.toString(),length);break;
			    case 92:content = UploadFileTool.getContent(content,sb.toString(),length);break;
			    case 93:content = UploadFileTool.getContent(content,sb.toString(),length);break;
			    case 94:content = UploadFileTool.getContent(content,sb.toString(),length);break;
			    case 95:content = UploadFileTool.getContent(content,sb.toString(),length);break;
			    case 96:content = UploadFileTool.getContent(content,sb.toString(),length);break;
			    case 97:content = UploadFileTool.getContent(content,sb.toString(),length);break;
			    case 98:content = UploadFileTool.getContent(content,sb.toString(),length);break;
			    case 99:content = UploadFileTool.getContent(content,sb.toString(),length);break;
			    case 100:content = UploadFileTool.getContent(content,sb.toString(),length);break;
			    case 101:content = UploadFileTool.getContent(content,sb.toString(),length);break;
			    case 102:content = UploadFileTool.getContent(content,sb.toString(),length);break;
			    case 103:content = UploadFileTool.getContent(content,sb.toString(),length);break;
			    case 104:content = UploadFileTool.getContent(content,sb.toString(),length);break;
			    case 105:content = UploadFileTool.getContent(content,sb.toString(),length);break;
			    case 106:content = UploadFileTool.getContent(content,sb.toString(),length);break;
			    case 107:content = UploadFileTool.getContent(content,sb.toString(),length);break;
			    case 108:content = UploadFileTool.getContent(content,sb.toString(),length);break;
			    case 109:content = UploadFileTool.getContent(content,sb.toString(),length);break;
			    case 110:content = UploadFileTool.getContent(content,sb.toString(),length);break;
			    case 111:content = UploadFileTool.getContent(content,sb.toString(),length);break;
			    case 112:content = UploadFileTool.getContent(content,sb.toString(),length);break;
			    case 113:content = UploadFileTool.getContent(content,sb.toString(),length);break;
			    case 114:content = UploadFileTool.getContent(content,sb.toString(),length);break;
			    case 115:content = UploadFileTool.getContent(content,sb.toString(),length);break;
			    case 116:content = UploadFileTool.getContent(content,sb.toString(),length);break;
			    case 117:content = UploadFileTool.getContent(content,sb.toString(),length);break;
			    case 118:content = UploadFileTool.getContent(content,sb.toString(),length);break;
			    case 119:content = UploadFileTool.getContent(content,sb.toString(),length);break;
			    case 120:content = UploadFileTool.getContent(content,sb.toString(),length);break;
			    case 121:content = UploadFileTool.getContent(content,sb.toString(),length);break;
			    case 122:content = UploadFileTool.getContent(content,sb.toString(),length);break;
			    case 123:content = UploadFileTool.getContent(content,customerApplicationGuarantorList.get(0).getDocumentNumber(),length);break;
			    case 124:content = UploadFileTool.getContent(content,customerApplicationGuarantorList.get(0).getGuarantorMortgagorPledge(),length);break;
			    case 125:content = UploadFileTool.getContent(content,sb.toString(),length);break;
			    case 126:content = UploadFileTool.getContent(content,customerApplicationGuarantorList.get(0).getSex(),length);break;
			    case 127:content = UploadFileTool.getContent(content,sb.toString(),length);break;
			    case 128:content = UploadFileTool.getContent(content,sb.toString(),length);break;
			    case 129:content = UploadFileTool.getContent(content,sb.toString(),length);break;
			    case 130:content = UploadFileTool.getContent(content,sb.toString(),length);break;
			    case 131:content = UploadFileTool.getContent(content,customerApplicationGuarantorList.get(0).getContactPhone(),length);break;
			    case 132:content = UploadFileTool.getContent(content,customerApplicationGuarantorList.get(0).getCellPhone(),length);break;
			    case 133:content = UploadFileTool.getContent(content,sb.toString(),length);break;
			    case 134:content = UploadFileTool.getContent(content,sb.toString(),length);break;
			    case 135:content = UploadFileTool.getContent(content,sb.toString(),length);break;
			    case 136:content = UploadFileTool.getContent(content,sb.toString(),length);break;
			    case 137:content = UploadFileTool.getContent(content,sb.toString(),length);break;
			    case 138:content = UploadFileTool.getContent(content,sb.toString(),length);break;
			    case 139:content = UploadFileTool.getContent(content,sb.toString(),length);break;
			    case 140:content = UploadFileTool.getContent(content,customerApplicationGuarantorList.get(0).getUnitName(),length);break;
			    case 141:content = UploadFileTool.getContent(content,customerApplicationGuarantorList.get(0).getDepartment(),length);break;
			    case 142:content = UploadFileTool.getContent(content,sb.toString(),length);break;
			    case 143:content = UploadFileTool.getContent(content,sb.toString(),length);break;
			    case 144:content = UploadFileTool.getContent(content,sb.toString(),length);break;
			    case 145:content = UploadFileTool.getContent(content,sb.toString(),length);break;
			    case 146:content = UploadFileTool.getContent(content,sb.toString(),length);break;
			    case 147:content = UploadFileTool.getContent(content,sb.toString(),length);break;
			    case 148:content = UploadFileTool.getContent(content,sb.toString(),length);break;
			    case 149:content = UploadFileTool.getContent(content,sb.toString(),length);break;
			    case 150:content = UploadFileTool.getContent(content,sb.toString(),length);break;
			    case 151:content = UploadFileTool.getContent(content,sb.toString(),length);break;
			    case 152:content = UploadFileTool.getContent(content,sb.toString(),length);break;
			    case 153:content = UploadFileTool.getContent(content,sb.toString(),length);break;
			    case 154:content = UploadFileTool.getContent(content,sb.toString(),length);break;
			    case 155:content = UploadFileTool.getContent(content,sb.toString(),length);break;
			    case 156:content = UploadFileTool.getContent(content,sb.toString(),length);break;
			    case 157:content = UploadFileTool.getContent(content,sb.toString(),length);break;
			    case 158:content = UploadFileTool.getContent(content,sb.toString(),length);break;
			    case 159:content = UploadFileTool.getContent(content,sb.toString(),length);break;
			    case 160:content = UploadFileTool.getContent(content,sb.toString(),length);break;
			    case 161:content = UploadFileTool.getContent(content,sb.toString(),length);break;
			    case 162:content = UploadFileTool.getContent(content,sb.toString(),length);break;
			    case 163:content = UploadFileTool.getContent(content,sb.toString(),length);break;
			    case 164:content = UploadFileTool.getContent(content,sb.toString(),length);break;
			    case 165:content = UploadFileTool.getContent(content,sb.toString(),length);break;
			    case 166:content = UploadFileTool.getContent(content,sb.toString(),length);break;
			    case 167:content = UploadFileTool.getContent(content,customerApplicationRecomList.get(0).getName(),length);break;
			    case 168:content = UploadFileTool.getContent(content,sb.toString(),length);break;
			    case 169:content = UploadFileTool.getContent(content,sb.toString(),length);break;
			    case 170:content = UploadFileTool.getContent(content,sb.toString(),length);break;
			    case 171:content = UploadFileTool.getContent(content,sb.toString(),length);break;
			    case 172:content = UploadFileTool.getContent(content,sb.toString(),length);break;
			    case 173:content = UploadFileTool.getContent(content,customerApplicationInfo.getBillingDate(),length);break;
			    case 174:content = UploadFileTool.getContent(content,customerApplicationOther.getPaperBillingShippingAddress(),length);break;
			    case 175:content = UploadFileTool.getContent(content,sb.toString(),length);break;
			    case 176:content = UploadFileTool.getContent(content,sb.toString(),length);break;
			    case 177:content = UploadFileTool.getContent(content,sb.toString(),length);break;
			    case 178:content = UploadFileTool.getContent(content,customerApplicationOther.getConsumptionUsePassword(),length);break;
			    case 179:content = UploadFileTool.getContent(content,customerApplicationOther.getSmsOpeningTrading(),length);break;
			    case 180:content = UploadFileTool.getContent(content,sb.toString(),length);break;
			    case 181:content = UploadFileTool.getContent(content,sb.toString(),length);break;
			    case 182:content = UploadFileTool.getContent(content,customerApplicationOther.getBillingMethod(),length);break;
			    case 183:content = UploadFileTool.getContent(content,sb.toString(),length);break;
			    case 184:content = UploadFileTool.getContent(content,sb.toString(),length);break;
			    case 185:content = UploadFileTool.getContent(content,sb.toString(),length);break;
			    case 186:content = UploadFileTool.getContent(content,sb.toString(),length);break;
			    case 187:content = UploadFileTool.getContent(content,sb.toString(),length);break;
			    case 188:content = UploadFileTool.getContent(content,sb.toString(),length);break;
			    case 189:content = UploadFileTool.getContent(content,sb.toString(),length);break;
			    case 190:content = UploadFileTool.getContent(content,sb.toString(),length);break;
			    case 191:content = UploadFileTool.getContent(content,sb.toString(),length);break;
			    case 192:content = UploadFileTool.getContent(content,sb.toString(),length);break;
			    case 193:content = UploadFileTool.getContent(content,sb.toString(),length);break;
			    case 194:content = UploadFileTool.getContent(content,sb.toString(),length);break;
			    case 195:content = UploadFileTool.getContent(content,sb.toString(),length);break;
			    case 196:content = UploadFileTool.getContent(content,sb.toString(),length);break;
			    case 197:content = UploadFileTool.getContent(content,sb.toString(),length);break;
			    case 198:content = UploadFileTool.getContent(content,sb.toString(),length);break;
			    case 199:content = UploadFileTool.getContent(content,sb.toString(),length);break;
			    case 200:content = UploadFileTool.getContent(content,sb.toString(),length);break;
			    case 201:content = UploadFileTool.getContent(content,sb.toString(),length);break;
			    case 202:content = UploadFileTool.getContent(content,sb.toString(),length);break;
			    case 203:content = UploadFileTool.getContent(content,sb.toString(),length);break;
			    case 204:content = UploadFileTool.getContent(content,sb.toString(),length);break;
			    case 205:content = UploadFileTool.getContent(content,sb.toString(),length);break;
			    case 206:content = UploadFileTool.getContent(content,sb.toString(),length);break;
			    case 207:content = UploadFileTool.getContent(content,sb.toString(),length);break;
			    case 208:content = UploadFileTool.getContent(content,sb.toString(),length);break;
			    case 209:content = UploadFileTool.getContent(content,sb.toString(),length);break;
			    case 210:content = UploadFileTool.getContent(content,sb.toString(),length);break;
			    case 211:content = UploadFileTool.getContent(content,sb.toString(),length);break;
			    case 212:content = UploadFileTool.getContent(content,sb.toString(),length);break;
			    case 213:content = UploadFileTool.getContent(content,sb.toString(),length);break;
			    case 214:content = UploadFileTool.getContent(content,sb.toString(),length);break;
			    case 215:content = UploadFileTool.getContent(content,sb.toString(),length);break;
			    case 216:content = UploadFileTool.getContent(content,sb.toString(),length);break;
			    case 217:content = UploadFileTool.getContent(content,sb.toString(),length);break;
			    case 218:content = UploadFileTool.getContent(content,sb.toString(),length);break;
			    case 219:content = UploadFileTool.getContent(content,sb.toString(),length);break;
			    case 220:content = UploadFileTool.getContent(content,sb.toString(),length);break;
			    case 221:content = UploadFileTool.getContent(content,sb.toString(),length);break;
			    case 222:content = UploadFileTool.getContent(content,sb.toString(),length);break;
			    case 223:content = UploadFileTool.getContent(content,sb.toString(),length);break;
			    case 224:content = UploadFileTool.getContent(content,sb.toString(),length);break;
			    case 225:content = UploadFileTool.getContent(content,sb.toString(),length);break;
			    case 226:content = UploadFileTool.getContent(content,sb.toString(),length);break;
			    case 227:content = UploadFileTool.getContent(content,sb.toString(),length);break;
			    case 228:content = UploadFileTool.getContent(content,sb.toString(),length);break;
			    case 229:content = UploadFileTool.getContent(content,sb.toString(),length);break;
			    case 230:content = UploadFileTool.getContent(content,sb.toString(),length);break;
			    case 231:content = UploadFileTool.getContent(content,sb.toString(),length);break;
			    case 232:content = UploadFileTool.getContent(content,sb.toString(),length);break;
			    case 233:content = UploadFileTool.getContent(content,sb.toString(),length);break;
			    case 234:content = UploadFileTool.getContent(content,sb.toString(),length);break;
			    case 235:content = UploadFileTool.getContent(content,sb.toString(),length);break;
			    case 236:content = UploadFileTool.getContent(content,sb.toString(),length);break;
			    case 237:content = UploadFileTool.getContent(content,sb.toString(),length);break;
			    case 238:content = UploadFileTool.getContent(content,sb.toString(),length);break;
			    case 239:content = UploadFileTool.getContent(content,sb.toString(),length);break;
			    case 240:content = UploadFileTool.getContent(content,sb.toString(),length);break;
			    case 241:content = UploadFileTool.getContent(content,sb.toString(),length);break;
			    case 242:content = UploadFileTool.getContent(content,sb.toString(),length);break;
			    case 243:content = UploadFileTool.getContent(content,sb.toString(),length);break;
			    case 244:content = UploadFileTool.getContent(content,sb.toString(),length);break;
			    case 245:content = UploadFileTool.getContent(content,sb.toString(),length);break;
			    case 246:content = UploadFileTool.getContent(content,sb.toString(),length);break;
			    case 247:content = UploadFileTool.getContent(content,sb.toString(),length);break;
			    case 248:content = UploadFileTool.getContent(content,sb.toString(),length);break;
			    case 249:content = UploadFileTool.getContent(content,sb.toString(),length);break;
			    case 250:content = UploadFileTool.getContent(content,sb.toString(),length);break;
			    case 251:content = UploadFileTool.getContent(content,sb.toString(),length);break;
			    case 252:content = UploadFileTool.getContent(content,sb.toString(),length);break;
			    case 253:content = UploadFileTool.getContent(content,sb.toString(),length);break;
			    case 254:content = UploadFileTool.getContent(content,sb.toString(),length);break;
			    case 255:content = UploadFileTool.getContent(content,sb.toString(),length);break;
			    case 256:content = UploadFileTool.getContent(content,sb.toString(),length);break;
			    case 257:content = UploadFileTool.getContent(content,sb.toString(),length);break;
			    case 258:content = UploadFileTool.getContent(content,sb.toString(),length);break;
			    case 259:content = UploadFileTool.getContent(content,sb.toString(),length);break;
			    case 260:content = UploadFileTool.getContent(content,sb.toString(),length);break;
			    case 261:content = UploadFileTool.getContent(content,sb.toString(),length);break;
			    case 262:content = UploadFileTool.getContent(content,sb.toString(),length);break;
			    case 263:content = UploadFileTool.getContent(content,sb.toString(),length);break;
			    case 264:content = UploadFileTool.getContent(content,sb.toString(),length);break;
			    case 265:content = UploadFileTool.getContent(content,sb.toString(),length);break;
			    case 266:content = UploadFileTool.getContent(content,sb.toString(),length);break;
			    case 267:content = UploadFileTool.getContent(content,sb.toString(),length);break;
			    case 268:content = UploadFileTool.getContent(content,customerApplicationInfo.getCashAdvanceProportion(),length);break;
			    case 269:content = UploadFileTool.getContent(content,sb.toString(),length);break;
			    case 270:content = UploadFileTool.getContent(content,sb.toString(),length);break;
			    case 271:content = UploadFileTool.getContent(content,sb.toString(),length);break;
			    case 272:content = UploadFileTool.getContent(content,sb.toString(),length);break;
			    case 273:content = UploadFileTool.getContent(content,sb.toString(),length);break;
			    case 274:content = UploadFileTool.getContent(content,sb.toString(),length);break;
			    case 275:content = UploadFileTool.getContent(content,sb.toString(),length);break;
			    case 276:content = UploadFileTool.getContent(content,sb.toString(),length);break;
			    case 277:content = UploadFileTool.getContent(content,sb.toString(),length);break;
			    case 278:content = UploadFileTool.getContent(content,sb.toString(),length);break;
			    case 279:content = UploadFileTool.getContent(content,sb.toString(),length);break;
			    case 280:content = UploadFileTool.getContent(content,sb.toString(),length);break;
			    case 281:content = UploadFileTool.getContent(content,sb.toString(),length);break;
			    case 282:content = UploadFileTool.getContent(content,sb.toString(),length);break;
			    case 283:content = UploadFileTool.getContent(content,sb.toString(),length);break;
			    case 284:content = UploadFileTool.getContent(content,sb.toString(),length);break;
			    case 285:content = UploadFileTool.getContent(content,sb.toString(),length);break;
			    case 286:content = UploadFileTool.getContent(content,sb.toString(),length);break;
			    case 287:content = UploadFileTool.getContent(content,sb.toString(),length);break;
			    case 288:content = UploadFileTool.getContent(content,sb.toString(),length);break;
			    case 289:content = UploadFileTool.getContent(content,sb.toString(),length);break;
			    case 290:content = UploadFileTool.getContent(content,sb.toString(),length);break;
			    case 291:content = UploadFileTool.getContent(content,sb.toString(),length);break;
			    case 292:content = UploadFileTool.getContent(content,sb.toString(),length);break;
			    case 293:content = UploadFileTool.getContent(content,sb.toString(),length);break;
			    case 294:content = UploadFileTool.getContent(content,sb.toString(),length);break;
			    case 295:content = UploadFileTool.getContent(content,sb.toString(),length);break;
			    case 296:content = UploadFileTool.getContent(content,sb.toString(),length);break;
			    case 297:content = UploadFileTool.getContent(content,sb.toString(),length);break;
			    case 298:content = UploadFileTool.getContent(content,sb.toString(),length);break;
			    case 299:content = UploadFileTool.getContent(content,sb.toString(),length);break;
			    case 300:content = UploadFileTool.getContent(content,sb.toString(),length);break;
			    case 301:content = UploadFileTool.getContent(content,sb.toString(),length);break;
			    case 302:content = UploadFileTool.getContent(content,sb.toString(),length);break;
			    case 303:content = UploadFileTool.getContent(content,sb.toString(),length);break;
			    case 304:content = UploadFileTool.getContent(content,sb.toString(),length);break;
			    case 305:content = UploadFileTool.getContent(content,sb.toString(),length);break;
			    case 306:content = UploadFileTool.getContent(content,sb.toString(),length);break;
			    case 307:content = UploadFileTool.getContent(content,sb.toString(),length);break;
			    case 308:content = UploadFileTool.getContent(content,sb.toString(),length);break;
			    case 309:content = UploadFileTool.getContent(content,sb.toString(),length);break;
			    case 310:content = UploadFileTool.getContent(content,sb.toString(),length);break;
			    case 311:content = UploadFileTool.getContent(content,sb.toString(),length);break;
			    case 312:content = UploadFileTool.getContent(content,sb.toString(),length);break;
			    case 313:content = UploadFileTool.getContent(content,sb.toString(),length);break;
			    case 314:content = UploadFileTool.getContent(content,sb.toString(),length);break;
			    case 315:content = UploadFileTool.getContent(content,sb.toString(),length);break;
			    case 316:content = UploadFileTool.getContent(content,sb.toString(),length);break;
			    case 317:content = UploadFileTool.getContent(content,sb.toString(),length);break;
			    case 318:content = UploadFileTool.getContent(content,sb.toString(),length);break;
			    case 319:content = UploadFileTool.getContent(content,sb.toString(),length);break;
			    case 320:content = UploadFileTool.getContent(content,sb.toString(),length);break;
			    case 321:content = UploadFileTool.getContent(content,sb.toString(),length);break;
			    case 322:content = UploadFileTool.getContent(content,sb.toString(),length);break;
			    case 323:content = UploadFileTool.getContent(content,sb.toString(),length);break;
			    case 324:content = UploadFileTool.getContent(content,sb.toString(),length);break;
			    case 325:content = UploadFileTool.getContent(content,sb.toString(),length);break;
			    case 326:content = UploadFileTool.getContent(content,sb.toString(),length);break;
			    case 327:content = UploadFileTool.getContent(content,sb.toString(),length);break;
			    case 328:content = UploadFileTool.getContent(content,sb.toString(),length);break;
			    case 329:content = UploadFileTool.getContent(content,sb.toString(),length);break;
			    case 330:content = UploadFileTool.getContent(content,sb.toString(),length);break;
			    case 331:content = UploadFileTool.getContent(content,sb.toString(),length);break;
			    case 332:content = UploadFileTool.getContent(content,sb.toString(),length);break;
			    case 333:content = UploadFileTool.getContent(content,sb.toString(),length);break;
			    case 334:content = UploadFileTool.getContent(content,sb.toString(),length);break;
			    case 335:content = UploadFileTool.getContent(content,sb.toString(),length);break;
			    case 336:content = UploadFileTool.getContent(content,sb.toString(),length);break;
			    case 337:content = UploadFileTool.getContent(content,sb.toString(),length);break;
			    case 338:content = UploadFileTool.getContent(content,sb.toString(),length);break;
			    case 339:content = UploadFileTool.getContent(content,sb.toString(),length);break;
			    case 340:content = UploadFileTool.getContent(content,sb.toString(),length);break;
			    case 341:content = UploadFileTool.getContent(content,sb.toString(),length);break;
			    case 342:content = UploadFileTool.getContent(content,sb.toString(),length);break;
			    case 343:content = UploadFileTool.getContent(content,sb.toString(),length);break;
			    case 344:content = UploadFileTool.getContent(content,sb.toString(),length);break;
			    case 345:content = UploadFileTool.getContent(content,sb.toString(),length);break;
			    case 346:content = UploadFileTool.getContent(content,sb.toString(),length);break;
			    default:break;
			}
		}
		String fileName = customerInfor.getChineseName()+"("+customerInfor.getCardId()+").txt";
		/*生成的接口数据上传到ftp文件上*/
		UploadFileTool.uploadFileToFtp(Constant.FTPIP, Integer.valueOf(Constant.FTPPORT), Constant.FTPUSERNAME, Constant.FTPPASSWORD, Constant.FTPPATH, fileName, content.toString());
		/*如果要下载接口数据,将下面的exportTextFile方法打开*/
		/*if(response!=null){
			UploadFileTool.exportTextFile(response, content.toString(), fileName);
		}*/
		/*上传成功后讲进件信息主表的状态置为已上传状态*/
		if(customerApplicationInfo!=null&&StringUtils.trimToNull(customerApplicationInfo.getId())!=null){
			/*更新上传状态为防止影响到别的属性，这里new一个新实例*/
			CustomerApplicationInfo temp = new CustomerApplicationInfo();
			temp.setId(customerApplicationInfo.getId());
			temp.setUploadStatus(Constant.UPLOAD_INTOPICES);
			commonDao.updateObject(temp);
		}
		
	}

	/* 根据客户id查询客户职业资料 */
	public CustomerCareersInformation findCustomerCareersInformationByCustomerId(
			String customerId) {
		return intoPiecesComdao
				.findCustomerCareersInformationByCustomerId(customerId);
	}

	/* 根据id查询联系人资料 */
	public List<CustomerApplicationContact> findCustomerApplicationContactsByApplicationId(
			String applicationId) {
		return intoPiecesComdao
				.findCustomerApplicationContactsByApplicationId(applicationId);
	}

	/* 根据id查询推荐人资料 */
	public List<CustomerApplicationRecom> findCustomerApplicationRecomsByApplicationId(
			String applicationId) {
		return intoPiecesComdao
				.findCustomerApplicationRecomsByApplicationId(applicationId);
	}

	/* 根据id查询担保人资料 */
	public List<CustomerApplicationGuarantor> findCustomerApplicationGuarantorsByApplicationId(
			String applicationId) {
		return intoPiecesComdao
				.findCustomerApplicationGuarantorsByApplicationId(applicationId);
	}

	/* 根据id查询申请的主表信息 */
	public CustomerApplicationInfo findCustomerApplicationInfoByApplicationId(
			String applicationId) {
		return intoPiecesComdao
				.findCustomerApplicationInfoByApplicationId(applicationId);
	}

	/* 根据id查询申请主表信息 的其他资料信息 */
	public CustomerApplicationOther findCustomerApplicationOtherByApplicationId(
			String applicationId) {
		return intoPiecesComdao
				.findCustomerApplicationOtherByApplicationId(applicationId);
	}

	/* 根据id查询申请主表信息 的行社专栏信息 */
	public CustomerApplicationCom findCustomerApplicationComByApplicationId(
			String applicationId) {
		return intoPiecesComdao
				.findCustomerApplicationComByApplicationId(applicationId);
	}
	
	/* 根据id查询客户账户信息 */
	public CustomerAccountData findCustomerAccountDataByApplicationId(
			String applicationId) {
		return intoPiecesComdao
				.findCustomerAccountDataByApplicationId(applicationId);
	}
	

	/* 根据客户id查询客户申请主表信息 的影像资料信息 */
	public VideoAccessories findVideoAccessoriesByCustomerId(String customerId) {
		return intoPiecesComdao.findVideoAccessoriesByCustomerId(customerId);
	}

	/* 校验客户是否存在，如果存在这里需要更新客户信息 */
	public CustomerInfor findCustomerInforById(String id) {
		return intoPiecesComdao.findCustomerInforById(id);
	}

	/* 校验职业信息是否存在，如果存在这里需要更新该客户信息 */
	public CustomerCareersInformation findCustomerCareersInformationById(
			String id) {
		return intoPiecesComdao.findCustomerCareersInformationById(id);
	}

	/* 信息主表信息是否存在，如果存在则更新信息 */
	public CustomerApplicationInfo findCustomerApplicationInfoById(String id) {
		return intoPiecesComdao.findCustomerApplicationInfoById(id);
	}

	/* 查询申请的某一笔进件申请单中上传的产品的附件 */
	public List<AddressAccessories> findAddressAccessories(
			String applicationId, String productId) {
		return intoPiecesComdao
				.findAddressAccessories(applicationId, productId);
	}
	
	/* 查找接口字段配置表 */
	public List<ApplicationDataImport> findApplicationDataImport() {
		return intoPiecesComdao.findApplicationDataImport();
	}
	
	
	/* 查找接口字段配置表 */
	public void saveCard(MakeCard makeCard) {
		commonDao.insertObject(makeCard);
	}
	
	/* 卡片下发 */
	public void organizationIssuedCard(String id,String cardOrganizationStatus,String cardCustomerStatus) {
		MakeCard makeCard = new MakeCard();
		makeCard.setId(id);
		if(StringUtils.trimToNull(cardOrganizationStatus)!=null){
			makeCard.setCardOrganizationStatus(cardOrganizationStatus);
		}
		if(StringUtils.trimToNull(cardCustomerStatus)!=null){
			makeCard.setCardCustomerStatus(cardCustomerStatus);
		}
		commonDao.updateObject(makeCard);
	}
	
	/* 查看卡片信息 */
	public MakeCard findMakeCardById(String id) {
		return commonDao.findObjectById(MakeCard.class, id);
	}
	
	/**
	 * 
	 * @param id
	 * @param dataType application 进件 amountadjustment 调额信息
	 * @return
	 */
	public List<ApproveHistoryForm> findApplicationDataImport(String id, String dataType) {
		return intoPiecesComdao.findApproveHistoryByDataId(id, dataType);
	}
	
	public boolean checkApplyQuota(float applyQuota,String userId,String productId){
		boolean flag = false;
		Float floatDb = intoPiecesComdao.checkApplyQuota(userId, productId);
		if(floatDb!=null){
			if(applyQuota<=floatDb.floatValue()){
				flag = true;
			}
		}
		return flag;
	}
	
	/*
	 * 添加三性检测，并进入下一节点行政岗-初
	 */
	public void addSxjc(QzApplnSxjcForm filter,HttpServletRequest request) throws Exception{
		QzApplnSxjc sxjc = commonDao.findObjectById(QzApplnSxjc.class, filter.getApplicationId());
		if(sxjc == null){
			sxjc = new QzApplnSxjc();
			sxjc.setApplication_id(filter.getApplicationId());
		}
		sxjc.setReality(filter.getReality());
		sxjc.setComplete(filter.getComplete());
		sxjc.setStandard(filter.getStandard());
		commonDao.insertObject(sxjc);
		CustomerApplicationProcess process =  customerApplicationProcessService.findByAppId(filter.getApplicationId());
		request.setAttribute("serialNumber", process.getSerialNumber());
		request.setAttribute("applicationId", process.getApplicationId());
		request.setAttribute("applicationStatus", ApplicationStatusEnum.APPROVE);
		request.setAttribute("objection", "false");
		//查找审批金额
		CustomerApplicationInfo appInfo = this.findCustomerApplicationInfoByApplicationId(process.getApplicationId());
		Circle circle = circleService.findCircleByAppId(process.getApplicationId());
		
		request.setAttribute("examineAmount", circle.getContractAmt());
		customerApplicationIntopieceWaitService.updateCustomerApplicationProcessBySerialNumberApplicationInfo1(request);
	}
	
	/* 初审节点退回进件 */
	/*
	 * TODO 1.添加注释 2.SQL写进DAO层
	 */
	public void checkDoNot(QzApplnSxjcForm filter) throws Exception{
		QzApplnSxjc sxjc = commonDao.findObjectById(QzApplnSxjc.class, filter.getApplicationId());
		if(sxjc==null){
			sxjc = new QzApplnSxjc();
			sxjc.setApplication_id(filter.getApplicationId());
		}
		sxjc.setReality(filter.getReality());
		sxjc.setComplete(filter.getComplete());
		sxjc.setStandard(filter.getStandard());
		commonDao.insertObject(sxjc);
		//获取进件信息
		CustomerApplicationInfo applicationInfo= commonDao.findObjectById(CustomerApplicationInfo.class, filter.getApplicationId());
		//获取客户信息
		CustomerInfor infor = commonDao.findObjectById(CustomerInfor.class, applicationInfo.getCustomerId());
//		commonDao.deleteObject(CustomerApplicationInfo.class, filter.getApplicationId());
		//更新状态为--退件到申请状态
		applicationInfo.setStatus(Constant.RETURN_INTOPICES);
		commonDao.updateObject(applicationInfo);
		//更新客户信息--退回
		infor.setProcessId(Constant.APPROVE_EDIT_1);
		commonDao.updateObject(infor);
		
		//将所有相关表记录删除appId
//		commonDao.queryBySql("update qz_appln_za_ywsqb_r set application_id=null where application_id='"+filter.getApplicationId()+"'", null);
//		commonDao.queryBySql("update qz_appln_ywsqb set application_id=null where application_id='"+filter.getApplicationId()+"'", null);
//		commonDao.queryBySql("update qz_appln_dbrxx set application_id=null where application_id='"+filter.getApplicationId()+"'", null);
//		commonDao.queryBySql("update qz_appln_attachment_list set application_id=null where application_id='"+filter.getApplicationId()+"'", null);
//		commonDao.queryBySql("update qz_appln_nbscyjb set application_id=null where application_id='"+filter.getApplicationId()+"'", null);
//		commonDao.queryBySql("update qz_iesb_for_circle set application_id=null where application_id='"+filter.getApplicationId()+"'", null);
//		commonDao.queryBySql("update qz_appln_dcnr set application_id=null where application_id='"+filter.getApplicationId()+"'", null);
//		commonDao.queryBySql("update qz_appln_jyd set application_id=null where application_id='"+filter.getApplicationId()+"'", null);
//		commonDao.queryBySql("update qz_appln_nbscyjb set application_id=null where application_id='"+filter.getApplicationId()+"'", null);
				
		
	}
	
/* 初审节点拒件 */
	/*
	 * TODO 1.添加注释 2.SQL写进DAO层
	 */
	public void reject(QzApplnSxjcForm filter, HttpServletRequest request) throws Exception{
		QzApplnSxjc sxjc = commonDao.findObjectById(QzApplnSxjc.class, filter.getApplicationId());
		if(sxjc==null){
			sxjc = new QzApplnSxjc();
			sxjc.setApplication_id(filter.getApplicationId());
		}
		sxjc.setReality(filter.getReality());
		sxjc.setComplete(filter.getComplete());
		sxjc.setStandard(filter.getStandard());
		commonDao.insertObject(sxjc);
		//获取进件信息
		CustomerApplicationInfo applicationInfo= commonDao.findObjectById(CustomerApplicationInfo.class, filter.getApplicationId());
		//获取客户信息
		CustomerInfor infor = commonDao.findObjectById(CustomerInfor.class, applicationInfo.getCustomerId());
		//更新客户信息--拒件
		infor.setProcessId(Constant.APPROVE_EDIT_2);
		commonDao.updateObject(infor);
		
		//通过申请表ID获取流程表
		CustomerApplicationProcess process =  customerApplicationProcessService.findByAppId(filter.getApplicationId());
		//插入流程log表
		insertProcessLog(filter.getApplicationId(),Constant.APPLN_TYPE_3,request,request.getParameter("jjyy"),process);
		
	}
	/**
	 * 根据进件id获取调查内容附件记录
	 */
	public List<QzDcnrUploadForm> getUploadList(String appId){
		String sql="SELECT A .id ,a.CUSTOMER_ID ,a.REPORT_ID ,a.REPORT_NAME ,";
				sql+=" a.LOAD_STATUS ,a.APPLICATION_ID ,a.hetong_id,a.user_name,a.card_id,b.id as upload_id,b.FILE_NAME ,";
				sql+="b.REMARK   FROM QZ_APPLN_DCNR A LEFT JOIN VIDEO_ACCESSORIES b on b.dcnr_id=a.id WHERE";
				sql+=" A .APPLICATION_ID = '"+appId+"'";
		return commonDao.queryBySql(QzDcnrUploadForm.class,sql, null);
	}
	
	/**
	 * 保存调查内容附件
	 * @param customerId
	 */
	public void saveDcnrByCustomerId(String appId,String dcnrId,String remark, MultipartFile file) {
		CustomerApplicationInfo infor = commonDao.findObjectById(CustomerApplicationInfo.class, appId);
		Map<String, String> map = UploadFileTool.uploadYxzlFileBySpring(file);
		String fileName = map.get("fileName");
		String url = map.get("url");
		//先删除之前记录
		List<VideoAccessories> video = commonDao.queryBySql(VideoAccessories.class,"select * from video_accessories where customer_id='"+infor.getCustomerId()+"' and dcnr_id='"+dcnrId+"'", null);
		if(video.size()>0){
			customerInforservice.deleteYxzlById(video.get(0).getId());
		}
		VideoAccessories videoAccessories = new VideoAccessories();
		videoAccessories.setId(IDGenerator.generateID());
		videoAccessories.setCustomerId(infor.getCustomerId());
		videoAccessories.setRemark(remark);
		videoAccessories.setCreatedTime(new Date());
		videoAccessories.setDcnrId(dcnrId);
		if (StringUtils.trimToNull(url) != null) {
			videoAccessories.setServerUrlPath(url);
		}
		if (StringUtils.trimToNull(fileName) != null) {
			videoAccessories.setFileName(fileName);
		}
		commonDao.insertObject(videoAccessories);
		QzApplnDcnr dcnr = commonDao.findObjectById(QzApplnDcnr.class, dcnrId);
		//设置已上传
		dcnr.setLoadStatus("1");
		dcnr.setCardId(null);
		dcnr.setUserName(null);
		dcnr.setHetongId(null);
		commonDao.updateObject(dcnr);
	}
	
	
	/**
	 * 保存决议单
	 * @param customerId
	 */
	public void saveJydByCustomerId(String appId,String remark, MultipartFile file) {
		CustomerApplicationInfo infor = commonDao.findObjectById(CustomerApplicationInfo.class, appId);
		Map<String, String> map = UploadFileTool.uploadYxzlFileBySpring(file);
		String fileName = map.get("fileName");
		String url = map.get("url");
		//先查询是否存在决议单记录
		List<QzApplnDcnr> dcnrList = commonDao.queryBySql(QzApplnDcnr.class,"select * from qz_appln_dcnr where application_id='"+appId+"' and report_id='"+Constant.jyd_id+"'", null);
		if(dcnrList.size()>0){
			//先删除影像资料表之前记录
			String dcnrId = dcnrList.get(0).getId();
			List<VideoAccessories> video = commonDao.queryBySql(VideoAccessories.class,"select * from video_accessories where customer_id='"+infor.getCustomerId()+"' and dcnr_id='"+dcnrId+"'", null);
			if(video.size()>0){
				customerInforservice.deleteYxzlById(video.get(0).getId());
			}
			//添加影像资料表
			VideoAccessories videoAccessories = new VideoAccessories();
			videoAccessories.setId(IDGenerator.generateID());
			videoAccessories.setCustomerId(infor.getCustomerId());
			videoAccessories.setRemark(remark);
			videoAccessories.setCreatedTime(new Date());
			videoAccessories.setDcnrId(dcnrId);
			if (StringUtils.trimToNull(url) != null) {
				videoAccessories.setServerUrlPath(url);
			}
			if (StringUtils.trimToNull(fileName) != null) {
				videoAccessories.setFileName(fileName);
			}
			commonDao.insertObject(videoAccessories);
		}else{
			//添加记录表
			QzApplnDcnr dcnr = new QzApplnDcnr();
			dcnr.setApplicationId(appId);
			dcnr.setCustomerId(infor.getCustomerId());
			dcnr.setReportId(Constant.jyd_id);
			dcnr.setReportName("授信决议单");
			dcnr.setLoadStatus("1");
			dcnr.setCardId(null);
			dcnr.setUserName(null);
			dcnr.setHetongId(null);
			commonDao.insertObject(dcnr);
			//添加影像资料表
			VideoAccessories videoAccessories = new VideoAccessories();
			videoAccessories.setId(IDGenerator.generateID());
			videoAccessories.setCustomerId(infor.getCustomerId());
			videoAccessories.setRemark(remark);
			videoAccessories.setCreatedTime(new Date());
			videoAccessories.setDcnrId(dcnr.getId());
			if (StringUtils.trimToNull(url) != null) {
				videoAccessories.setServerUrlPath(url);
			}
			if (StringUtils.trimToNull(fileName) != null) {
				videoAccessories.setFileName(fileName);
			}
			commonDao.insertObject(videoAccessories);
		}

	}
	
	/**
	 * 保存合同单
	 * @param customerId
	 */
	public void saveHtByCustomerId(String appId,String remark,String hetongId,String userName,String cardId, MultipartFile file) {
		CustomerApplicationInfo infor = commonDao.findObjectById(CustomerApplicationInfo.class, appId);
		Map<String, String> map = UploadFileTool.uploadYxzlFileBySpring(file);
		String fileName = map.get("fileName");
		String url = map.get("url");
		//先查询是否存在决议单记录
		List<QzApplnDcnr> dcnrList = commonDao.queryBySql(QzApplnDcnr.class,"select * from qz_appln_dcnr where application_id='"+appId+"' and report_id='"+Constant.htd_id+"'", null);
		if(dcnrList.size()>0){
			//先删除影像资料表之前记录
			String dcnrId = dcnrList.get(0).getId();
			List<VideoAccessories> video = commonDao.queryBySql(VideoAccessories.class,"select * from video_accessories where customer_id='"+infor.getCustomerId()+"' and dcnr_id='"+dcnrId+"'", null);
			if(video.size()>0){
				customerInforservice.deleteYxzlById(video.get(0).getId());
			}
			//修改调查内容记录
			QzApplnDcnr dcnr = dcnrList.get(0);
			dcnr.setCardId(cardId);
			dcnr.setUserName(userName);
			dcnr.setHetongId(hetongId);
			commonDao.updateObject(dcnr);
			//添加影像资料表
			VideoAccessories videoAccessories = new VideoAccessories();
			videoAccessories.setId(IDGenerator.generateID());
			videoAccessories.setCustomerId(infor.getCustomerId());
			videoAccessories.setRemark(remark);
			videoAccessories.setCreatedTime(new Date());
			videoAccessories.setDcnrId(dcnrId);
			if (StringUtils.trimToNull(url) != null) {
				videoAccessories.setServerUrlPath(url);
			}
			if (StringUtils.trimToNull(fileName) != null) {
				videoAccessories.setFileName(fileName);
			}
			commonDao.insertObject(videoAccessories);
		}else{
			//添加记录表
			QzApplnDcnr dcnr = new QzApplnDcnr();
			dcnr.setApplicationId(appId);
			dcnr.setCustomerId(infor.getCustomerId());
			dcnr.setReportId(Constant.htd_id);
			dcnr.setReportName("合同单");
			dcnr.setLoadStatus("1");
			dcnr.setHetongId(hetongId);
			dcnr.setUserName(userName);
			dcnr.setCardId(cardId);
			commonDao.insertObject(dcnr);
			//添加影像资料表
			VideoAccessories videoAccessories = new VideoAccessories();
			videoAccessories.setId(IDGenerator.generateID());
			videoAccessories.setCustomerId(infor.getCustomerId());
			videoAccessories.setRemark(remark);
			videoAccessories.setCreatedTime(new Date());
			videoAccessories.setDcnrId(dcnr.getId());
			if (StringUtils.trimToNull(url) != null) {
				videoAccessories.setServerUrlPath(url);
			}
			if (StringUtils.trimToNull(fileName) != null) {
				videoAccessories.setFileName(fileName);
			}
			commonDao.insertObject(videoAccessories);
		}

	}
	
	/*
	 * 退件（退到上一节点）
	 * distancet 退回的目标节点
	 * current 当前节点表示
<<<<<<< HEAD
	 */
	public void returnAppln(String applicationId,HttpServletRequest request, int distancet, int current) throws Exception{
=======
	 * nodeId 退回的节点id
	 */
	public void returnAppln(String applicationId,HttpServletRequest request, String nodeId) throws Exception{
>>>>>>> chinhBy-master
		IUser user = Beans.get(LoginManager.class).getLoggedInUser(request);
		String loginId = user.getId();
		//通过申请表ID获取流程表
		CustomerApplicationProcess process =  customerApplicationProcessService.findByAppId(applicationId);
		//获取进件信息
		CustomerApplicationInfo applicationInfo= commonDao.findObjectById(CustomerApplicationInfo.class, applicationId);
		//插入流程log表
		insertProcessLog(applicationId,Constant.APPLN_TYPE_2,request,request.getParameter("remark"),process);
<<<<<<< HEAD
		//通过流程表的当前节点获取上一节点
		NodeControl nodeControl = customerApplicationProcessService.getLastStatus(process.getNextNodeId());
		//循环获取目标节点
		for(int i = 0; i < current-distancet-1; i++){
			nodeControl = customerApplicationProcessService.getLastStatus(nodeControl.getCurrentNode());
		}
		System.out.println("最终节点：" + nodeControl.getCurrentNode());
=======

>>>>>>> chinhBy-master
		//更新业务流程表
		process.setNextNodeId(nodeId);
		process.setAuditUser(loginId);
		process.setCreatedTime(new Date());
		process.setFallbackReason(request.getParameter("remark"));
		//process.setFallbackReason(request.getParameter("remark"));
	    customerApplicationIntopieceWaitDao.updateCustomerApplicationProcessBySerialNumber(process);
	    
	    //更新状态为--退件到申请状态
	  	applicationInfo.setStatus(Constant.TRTURN_INTOPICES);
	  	commonDao.updateObject(applicationInfo);
	    //更新流程备份表
	    
	   //查找当前所处流转状态
  		WfProcessRecord wfProcessRecord = commonDao.findObjectById(WfProcessRecord.class, process.getSerialNumber());
		
<<<<<<< HEAD
		//通过上一节点获取上一流程
		WfStatusQueueRecord befoRecord = wfStatusResultDao.getLastStatus(beforeStatus);
		//循环获取目标流程
		for(int i = 0; i < current-distancet-1; i++){
			befoRecord = wfStatusResultDao.getLastStatus(befoRecord.getBeforeStatus());
		}
		wfProcessRecord.setWfStatusQueueRecord(befoRecord.getId());
=======
  		WfStatusQueueRecord wfStatusQueueRecord = commonDao.findObjectById(WfStatusQueueRecord.class,wfProcessRecord.getWfStatusQueueRecord());
		
		String currentProcess = wfStatusQueueRecord.getCurrentProcess();
		String sql = "select t.* from wf_status_queue_record t, wf_status_info t1 " 
				+ "where  t.current_status = t1.id and t.current_process =  #{currentProcess}"
				+ " and t1.status_code = #{statusCode}";
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("currentProcess", currentProcess);
		params.put("statusCode", nodeId);
		List<WfStatusQueueRecord> list = commonDao.queryBySql(WfStatusQueueRecord.class, sql, params);
		
		wfProcessRecord.setWfStatusQueueRecord(list.get(0).getId());
>>>>>>> chinhBy-master
		commonDao.updateObject(wfProcessRecord);
	}

	
	public void insertProcessLog(String appId,String operateType,HttpServletRequest request,String remark,CustomerApplicationProcess process){
		IUser user = Beans.get(LoginManager.class).getLoggedInUser(request);
		//通过申请表ID获取流程表
		NodeAudit nodeAudit = commonDao.findObjectById(NodeAudit.class, process.getNextNodeId());
		
		//添加流程记录表
		QzApplnProcessResult processResult = new QzApplnProcessResult();
		processResult.setApplicationId(appId);
		processResult.setNodeId(nodeAudit.getId());
		processResult.setNodeName(nodeAudit.getNodeName());
		processResult.setOperateType(operateType);
		processResult.setUserId(user.getId());
		processResult.setUserName(user.getDisplayName());
		processResult.setCreatedTime(new Date());
		processResult.setRemark(remark);
		commonDao.insertObject(processResult);
	}
	
	
	/*
	 * 拒件
	 */
	public void rejectAppln(String applicationId,HttpServletRequest request) throws Exception{
		IUser user = Beans.get(LoginManager.class).getLoggedInUser(request);
		String loginId = user.getId();
		CustomerApplicationProcess process =  customerApplicationProcessService.findByAppId(applicationId);
		//查找当前所处流转状态
		WfProcessRecord wfProcessRecord = commonDao.findObjectById(WfProcessRecord.class, process.getSerialNumber());
		WfStatusQueueRecord wfStatusQueueRecord = commonDao.findObjectById(WfStatusQueueRecord.class,wfProcessRecord.getWfStatusQueueRecord());
		
		//进入下一流转之前 先更新当前流转
		wfStatusQueueRecord.setExamineUser(loginId);
		wfStatusQueueRecord.setExamineResult(ApplicationStatusEnum.REJECTAPPROVE);
		wfStatusQueueRecord.setStartExamineTime(new Date());
		commonDao.updateObject(wfStatusQueueRecord);
		
		wfProcessRecord.setIsClosed("1");
		commonDao.updateObject(wfProcessRecord);
		
		//获取进件信息（改为拒件状态）
		CustomerApplicationInfo applicationInfo= commonDao.findObjectById(CustomerApplicationInfo.class, applicationId);
		applicationInfo.setStatus(Constant.REFUSE_INTOPICES);
		applicationInfo.setModifiedBy(user.getId());
		applicationInfo.setModifiedTime(new Date());
		commonDao.updateObject(applicationInfo);
		
		//插入流程log表
		insertProcessLog(applicationId,Constant.APPLN_TYPE_3,request,request.getParameter("remark"),process);
		
	}
	
	/*
	 * 授信岗同意初审拒件（不需要插入log，更新basic表）
	 */
	public void inToReject(String applicationId,HttpServletRequest request) throws Exception{
		IUser user = Beans.get(LoginManager.class).getLoggedInUser(request);
		String loginId = user.getId();
		CustomerApplicationProcess process =  customerApplicationProcessService.findByAppId(applicationId);
		//查找当前所处流转状态
		WfProcessRecord wfProcessRecord = commonDao.findObjectById(WfProcessRecord.class, process.getSerialNumber());
		WfStatusQueueRecord wfStatusQueueRecord = commonDao.findObjectById(WfStatusQueueRecord.class,wfProcessRecord.getWfStatusQueueRecord());
		
		//进入下一流转之前 先更新当前流转
		wfStatusQueueRecord.setExamineUser(loginId);
		wfStatusQueueRecord.setExamineResult(ApplicationStatusEnum.REJECTAPPROVE);
		wfStatusQueueRecord.setStartExamineTime(new Date());
		commonDao.updateObject(wfStatusQueueRecord);
		
		wfProcessRecord.setIsClosed("1");
		commonDao.updateObject(wfProcessRecord);
		
		//获取进件信息
		CustomerApplicationInfo applicationInfo= commonDao.findObjectById(CustomerApplicationInfo.class, applicationId);
		applicationInfo.setStatus(Constant.REFUSE_INTOPICES);
		applicationInfo.setModifiedBy(user.getId());
		applicationInfo.setModifiedTime(new Date());
		commonDao.updateObject(applicationInfo);
		//获取客户信息
		CustomerInfor infor = commonDao.findObjectById(CustomerInfor.class, applicationInfo.getCustomerId());
		//更新客户信息--还原
		infor.setProcessId("");
		commonDao.updateObject(infor);
	}
	
	/*
	 * 授信岗不同意初审拒件（状态还原）
	 */
	public void outToReject(String applicationId,HttpServletRequest request) throws Exception{
		//获取进件信息
		CustomerApplicationInfo applicationInfo= commonDao.findObjectById(CustomerApplicationInfo.class, applicationId);
		//获取客户信息
		CustomerInfor infor = commonDao.findObjectById(CustomerInfor.class, applicationInfo.getCustomerId());
		//更新客户信息--还原
		infor.setProcessId("");
		commonDao.updateObject(infor);
	}
	
	/**
	 * 保存审贷会决议单form(申请前)
	 */
	public void insertSdhjydForm(QzApplnJyd qzSdhjyd, HttpServletRequest request){
		String sql="select * from qz_appln_jyd where customer_id='"+qzSdhjyd.getCustomerId()+"' and application_id='"+qzSdhjyd.getApplicationId()+"'";
		List<QzApplnJyd> qz = commonDao.queryBySql(QzApplnJyd.class,sql, null);
		if(qz.size()>0){
			commonDao.deleteObject(QzApplnJyd.class, qz.get(0).getId());
			commonDao.queryBySql("delete from qz_appln_jyd_gtjkr where jyd_id='"+qz.get(0).getId()+"'", null);
			commonDao.queryBySql("delete from qz_appln_jyd_bzdb where jyd_id='"+qz.get(0).getId()+"'", null);
			commonDao.queryBySql("delete from qz_appln_jyd_dydb where jyd_id='"+qz.get(0).getId()+"'", null);
		}
			commonDao.insertObject(qzSdhjyd);
			String jyd_id = qzSdhjyd.getId();
			String[] gtjkrxm = request.getParameterValues("gtjkrxm");
			String[] gtjkrhm = request.getParameterValues("gtjkrhm");
			String[] bzdbxm = request.getParameterValues("bzdbxm");
			String[] bzdbhm = request.getParameterValues("bzdbhm");
			String[] dyr = request.getParameterValues("dyr");
			String[] dywmc = request.getParameterValues("dywmc");
			String[] sl = request.getParameterValues("sl");
			String[] djhm = request.getParameterValues("djhm");
			String[] kdyjz = request.getParameterValues("kdyjz");
			//新增共同借款人
			if(gtjkrxm!=null){
				for(int i=0;i<gtjkrxm.length;i++){
					QzApplnJydGtjkr gtjkr = new QzApplnJydGtjkr();
					gtjkr.setName(gtjkrxm[i]);
					gtjkr.setCardId(gtjkrhm[i]);
					gtjkr.setJydId(jyd_id);
					commonDao.insertObject(gtjkr);
				}
			}
			//新增保证担保
			if(bzdbxm!=null){
				for(int i=0;i<bzdbxm.length;i++){
					QzApplnJydBzdb bzdb = new QzApplnJydBzdb();
					bzdb.setName(bzdbxm[i]);
					bzdb.setCardId(bzdbhm[i]);
					bzdb.setJydId(jyd_id);
					commonDao.insertObject(bzdb);
				}
			}
			//新增抵押担保
			if(dyr!=null){
				for(int i=0;i<dyr.length;i++){
					QzApplnJydDydb dydb = new QzApplnJydDydb();
					dydb.setUserName(dyr[i]);
					dydb.setGoodsName(dywmc[i]);
					dydb.setGoodsCount(sl[i]);
					dydb.setCard(djhm[i]);
					dydb.setGoodsValues(kdyjz[i]);
					dydb.setJydId(jyd_id);
					commonDao.insertObject(dydb);
				}
			}
	}
	
	/**
	 * 获取审贷会决议单form(申请前)
	 */
	public QzApplnJyd getSdhjydForm(String customerId){
		String sql="select * from qz_appln_jyd where customer_id='"+customerId+"' and application_id is null";
		List<QzApplnJyd> qz = commonDao.queryBySql(QzApplnJyd.class,sql, null);
		if(qz.size()>0){
			return qz.get(0);
		}
			return null;
	}
	
	/**
	 * 获取审贷会决议单form(申请后)
	 */
	public QzApplnJyd getSdhjydFormAfter(String appId){
		String sql="select * from qz_appln_jyd where application_id='"+appId+"'";
		List<QzApplnJyd> qz = commonDao.queryBySql(QzApplnJyd.class,sql, null);
		if(qz.size()>0){
			return qz.get(0);
		}
			return null;
	}
	/**
	 * 获取共同借款人list
	 */
	public List<QzApplnJydGtjkr> getJkrList(String jydId){
		String sql="select * from qz_appln_jyd_gtjkr where jyd_id='"+jydId+"'";
		List<QzApplnJydGtjkr> qz = commonDao.queryBySql(QzApplnJydGtjkr.class,sql, null);
		if(qz.size()>0){
			return qz;
		}
			return null;
	}
	/**
	 * 获取保证担保list
	 */
	public List<QzApplnJydBzdb> getBzdbList(String jydId){
		String sql="select * from qz_appln_jyd_bzdb where jyd_id='"+jydId+"'";
		List<QzApplnJydBzdb> qz = commonDao.queryBySql(QzApplnJydBzdb.class,sql, null);
		if(qz.size()>0){
			return qz;
		}
			return null;
	}
	/**
	 * 获取抵押担保list
	 */
	public List<QzApplnJydDydb> getDydbList(String jydId){
		String sql="select * from qz_appln_jyd_dydb where jyd_id='"+jydId+"'";
		List<QzApplnJydDydb> qz = commonDao.queryBySql(QzApplnJydDydb.class,sql, null);
		if(qz.size()>0){
			return qz;
		}
			return null;
	}
	
	
	/**
	 * 保存审贷会决议单form(申请后)
	 */
	public void insertSdhjydFormAfter(QzApplnJyd qzSdhjyd){
		String sql="select * from qz_appln_jyd where application_id='"+qzSdhjyd.getApplicationId()+"'";
		List<QzApplnJyd> qz = commonDao.queryBySql(QzApplnJyd.class,sql, null);
		if(qz.size()>0){
			commonDao.deleteObject(QzApplnJyd.class, qz.get(0).getId());
		}
			commonDao.insertObject(qzSdhjyd);
			
		//将决策金额和利率以及期限填入circle
		Circle circle = circleService.findCircleByAppId(qzSdhjyd.getApplicationId());
		circle.setContractAmt(qzSdhjyd.getJe());
		circle.setActIntRate(qzSdhjyd.getLl());
		circle.setTerm(qzSdhjyd.getQx());
		commonDao.updateObject(circle);
	}
	
	/**
	 * 保存审议纪要form
	 */
	public void insertSyjyForm(QzApplnSdhjy qzSyjy){
		String sql="select * from qz_appln_sdhjy where application_id='"+qzSyjy.getApplicationId()+"'";
		List<QzApplnSdhjy> qz = commonDao.queryBySql(QzApplnSdhjy.class,sql, null);
		if(qz.size()>0){
			commonDao.deleteObject(QzApplnSdhjy.class, qz.get(0).getId());
		}
			commonDao.insertObject(qzSyjy);
	}
	/**
	 * 获取审议纪要form
	 */
	public QzApplnSdhjy getSyjyForm(String appId){
		String sql="select * from qz_appln_sdhjy where application_id='"+appId+"'";
		List<QzApplnSdhjy> qz = commonDao.queryBySql(QzApplnSdhjy.class,sql, null);
		if(qz.size()>0){
			QzApplnSdhjy sdhjy = qz.get(0);
			return sdhjy;
		}else{
			QzApplnSdhjy sdhjy = new QzApplnSdhjy();
			String sql1="select * from qz_appln_ywsqb where  application_id='"+appId+"'";
			List<QzApplnYwsqb> qz1 = commonDao.queryBySql(QzApplnYwsqb.class,sql1, null);
			String sql2="select * from qz_appln_nbscyjb where  application_id='"+appId+"'";
			List<QzApplnNbscyjb> qz2= commonDao.queryBySql(QzApplnNbscyjb.class,sql2, null);
			if(qz1.size()>0){
				sdhjy.setJkrxm(qz1.get(0).getName());
				sdhjy.setSqrq(qz1.get(0).getApplyTime());
			}
			if(qz2.size()>0){
				sdhjy.setSqje(qz2.get(0).getApplyAmount());
				sdhjy.setSqqx(qz2.get(0).getApplyDeadline());
				sdhjy.setSqll(qz2.get(0).getSugRates());
				sdhjy.setDbfs(qz2.get(0).getSugGuntType());
			}
			return sdhjy;
		}
	}
	
	/**
	 * 保存台帐form
	 * @throws Exception 
	 */
	public void insertTzList(HttpServletRequest request,String appId) throws Exception{
		//先删除历史记录
		String sql="select * from qz_appln_htqdtz where application_id='"+appId+"'";
		List<QzApplnHtqdtz> qz = commonDao.queryBySql(QzApplnHtqdtz.class,sql, null);
		if(qz.size()>0){
			commonDao.queryBySql("delete from qz_appln_htqdtz where application_id='"+appId+"'", null);
		}
		String[] slrq = request.getParameterValues("slrq");
		String[] jkrxm = request.getParameterValues("jkrxm");
		String[] pzje = request.getParameterValues("pzje");
		String[] yyqdrq = request.getParameterValues("yyqdrq");
		String[] sjqdrq = request.getParameterValues("sjqdrq");
		String[] zbkhjl = request.getParameterValues("zbkhjl");
		String[] jbr = request.getParameterValues("jbr");
		String[] bz = request.getParameterValues("bz");
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		if(slrq!=null){
			for(int i=0;i<slrq.length;i++){
				QzApplnHtqdtz tz = new QzApplnHtqdtz();
				tz.setSlrq(sdf.parse(slrq[i]));
				tz.setJkrxm(jkrxm[i]);
				tz.setPzje(pzje[i]);
				tz.setYyqdrq(sdf.parse(yyqdrq[i]));
				tz.setSjqdrq(sdf.parse(sjqdrq[i]));
				tz.setZbkhjl(zbkhjl[i]);
				tz.setJbr(jbr[i]);
				tz.setBz(bz[i]);
				tz.setApplicationId(appId);
				tz.setCreatedTime(new Date());
				commonDao.insertObject(tz);
			}
		}
	}
	/**
	 * 获取台帐form
	 */
	public List<QzApplnHtqdtz> getTzList(String appId){
		String sql="select * from qz_appln_htqdtz where application_id='"+appId+"'";
		List<QzApplnHtqdtz> qz = commonDao.queryBySql(QzApplnHtqdtz.class,sql, null);
		return qz;
	}
	
	/* 授信岗补充上会操作 */
	/*
	 * TODO 1.添加注释 2.SQL写进DAO层
	 */
	public void returnToCu(HttpServletRequest request) throws Exception{
		//获取进件信息
		CustomerApplicationInfo applicationInfo= commonDao.findObjectById(CustomerApplicationInfo.class,request.getParameter("id"));
		//获取客户信息
		CustomerInfor infor = commonDao.findObjectById(CustomerInfor.class, applicationInfo.getCustomerId());
		//更新客户信息--补充上会
		infor.setProcessId(Constant.APPROVE_EDIT_3);
		commonDao.updateObject(infor);
	}
	
	/*
	 * 对客户信息表中没有appId的表添加appid
	 */
	public void addAppId(String customerId,String applicationId){
		//添加产品类型appId
		QzAppln_Za_Ywsqb_R qzappln_za_ywsqb_r = za_ywsqb_r_service.findByCustomerId(customerId);
		if(qzappln_za_ywsqb_r!=null){
			qzappln_za_ywsqb_r.setApplicationId(applicationId);
			commonDao.updateObject(qzappln_za_ywsqb_r);
		}
		
		//添加业务申请表appId
		QzApplnYwsqb qzApplnYwsqb = ywsqbService.findYwsqb(customerId, null);
		if(qzApplnYwsqb!=null){
			qzApplnYwsqb.setApplicationId(applicationId);
			commonDao.updateObject(qzApplnYwsqb);
		}
		//添加担保人appId
		List<QzApplnDbrxx> dbrxx_ls = dbrxxService.findDbrxx(customerId, null);
		for(QzApplnDbrxx obj : dbrxx_ls){
			obj.setApplicationId(applicationId);
			commonDao.updateObject(obj);
		}
		//添加附件appId
		QzApplnAttachmentList qzApplnAttachmentList = attachmentListService.findAttachmentList(customerId, null);
		if(qzApplnAttachmentList != null){
			qzApplnAttachmentList.setApplicationId(applicationId);
			commonDao.updateObject(qzApplnAttachmentList);
		}
		
		//添加内部审查appId
		QzApplnNbscyjb qzApplnNbscyjb = nbscyjbService.findNbscyjb(customerId, null);
		if(qzApplnNbscyjb != null){
			qzApplnNbscyjb.setApplicationId(applicationId);
			commonDao.updateObject(qzApplnNbscyjb);
		}
		
		//添加circle表appId
		Circle circle = circleService.findCircle(customerId,null);
		if(circle != null){
			circle.setApplicationId(applicationId);
			commonDao.updateObject(circle);
		}
		
		//添加调查内容appId
		String sql1= "select * from qz_appln_dcnr where customer_id='"+customerId+"' and application_id is null" ;
		List<QzApplnDcnr> dcnrList = commonDao.queryBySql(QzApplnDcnr.class, sql1, null);
		if(dcnrList.size()>0){
			for(int i=0;i<dcnrList.size();i++){
				QzApplnDcnr dcnr = dcnrList.get(i);
				dcnr.setApplicationId(applicationId);
				commonDao.updateObject(dcnr);
			}
		}
		//添加决议单
		String sql2= "select * from qz_appln_jyd where customer_id='"+customerId+"' and application_id is null" ;
		List<QzApplnJyd> jydList = commonDao.queryBySql(QzApplnJyd.class, sql2, null);
		if(jydList.size()>0){
			QzApplnJyd jyd = jydList.get(0);
			jyd.setApplicationId(applicationId);
			commonDao.updateObject(jyd);
		}
		
		//添加内部审查意见表
		String sql3= "select * from qz_appln_nbscyjb where customer_id='"+customerId+"' and application_id is null" ;
		List<QzApplnNbscyjb> yjbList = commonDao.queryBySql(QzApplnNbscyjb.class, sql3, null);
		if(yjbList.size()>0){
			QzApplnNbscyjb jyd = yjbList.get(0);
			jyd.setApplicationId(applicationId);
			commonDao.updateObject(jyd);
		}
	}
	
	/**
	 * 保存清单至调查内容表
	 */
	public void addAttachList(QzApplnAttachmentList qzApplnAttachmentList){
		String chkValue = qzApplnAttachmentList.getChkValue();
		//工薪类
		Map<String, String> map = new HashMap<String, String>();
		if(qzApplnAttachmentList.getBussType().equals("1")){
			if((Integer.parseInt(chkValue)&1) != 0){
				map.put("1", Constant.ATTACH_LIST1);
			}
			if((Integer.parseInt(chkValue)&2) != 0){
				map.put("2", Constant.ATTACH_LIST2);
			}
			if((Integer.parseInt(chkValue)&4) != 0){
				map.put("4", Constant.ATTACH_LIST3);
			}
			if((Integer.parseInt(chkValue)&8) != 0){
				map.put("8", Constant.ATTACH_LIST4);
			}
			if((Integer.parseInt(chkValue)&16) != 0){
				map.put("16", Constant.ATTACH_LIST5);
			}
			if((Integer.parseInt(chkValue)&32) != 0){
				map.put("32", Constant.ATTACH_LIST6);
			}
			if((Integer.parseInt(chkValue)&64) != 0){
				map.put("64", Constant.ATTACH_LIST7);
			}
			if((Integer.parseInt(chkValue)&128) != 0){
				map.put("128", Constant.ATTACH_LIST8);
			}
			if((Integer.parseInt(chkValue)&256) != 0){
				map.put("256", Constant.ATTACH_LIST9);
			}
			if((Integer.parseInt(chkValue)&512) != 0){
				map.put("512", Constant.ATTACH_LIST10);
			}
			if((Integer.parseInt(chkValue)&1024) != 0){
				map.put("1024", Constant.ATTACH_LIST11);
			}
			if((Integer.parseInt(chkValue)&2048) != 0){
				map.put("2048", Constant.ATTACH_LIST12);
			}
			if((Integer.parseInt(chkValue)&4096) != 0){
				map.put("4096", Constant.ATTACH_LIST13);
			}
			if((Integer.parseInt(chkValue)&8192) != 0){
				map.put("8192", Constant.ATTACH_LIST14);
			}
		}
		if(qzApplnAttachmentList.getBussType().equals("2")){
			if((Integer.parseInt(chkValue)&1) != 0){
				map.put("1", Constant.ATTACH_LIST15);
			}
			if((Integer.parseInt(chkValue)&2) != 0){
				map.put("2", Constant.ATTACH_LIST2);
			}
			if((Integer.parseInt(chkValue)&4) != 0){
				map.put("4", Constant.ATTACH_LIST3);
			}
			if((Integer.parseInt(chkValue)&8) != 0){
				map.put("8", Constant.ATTACH_LIST4);
			}
			if((Integer.parseInt(chkValue)&16) != 0){
				map.put("16", Constant.ATTACH_LIST5);
			}
			if((Integer.parseInt(chkValue)&32) != 0){
				map.put("32", Constant.ATTACH_LIST6);
			}
			if((Integer.parseInt(chkValue)&64) != 0){
				map.put("64", Constant.ATTACH_LIST7);
			}
			if((Integer.parseInt(chkValue)&128) != 0){
				map.put("128", Constant.ATTACH_LIST16);
			}
			if((Integer.parseInt(chkValue)&256) != 0){
				map.put("256", Constant.ATTACH_LIST17);
			}
			if((Integer.parseInt(chkValue)&512) != 0){
				map.put("512", Constant.ATTACH_LIST18);
			}
			if((Integer.parseInt(chkValue)&1024) != 0){
				map.put("1024", Constant.ATTACH_LIST19);
			}
			if((Integer.parseInt(chkValue)&2048) != 0){
				map.put("2048", Constant.ATTACH_LIST20);
			}
			if((Integer.parseInt(chkValue)&4096) != 0){
				map.put("4096", Constant.ATTACH_LIST21);
			}
			if((Integer.parseInt(chkValue)&8192) != 0){
				map.put("8192", Constant.ATTACH_LIST22);
			}
			if((Integer.parseInt(chkValue)&16384) != 0){
				map.put("16384", Constant.ATTACH_LIST23);
			}
			if((Integer.parseInt(chkValue)&32768) != 0){
				map.put("32768", Constant.ATTACH_LIST24);
			}
			if((Integer.parseInt(chkValue)&65536) != 0){
				map.put("65536", Constant.ATTACH_LIST25);
			}
			if((Integer.parseInt(chkValue)&131072) != 0){
				map.put("131072", Constant.ATTACH_LIST26);
			}
			if((Integer.parseInt(chkValue)&262144) != 0){
				map.put("262144", Constant.ATTACH_LIST27);
			}
		}
		//先删除之前调查内容
		String sql = "delete from qz_appln_dcnr where customer_id='"+qzApplnAttachmentList.getCustomerId()+"'";
		if(qzApplnAttachmentList.getApplicationId()==null){
			sql+=" and application_id is null";
		}else{
			sql+=" and application_id ='"+qzApplnAttachmentList.getApplicationId()+"'";
		}
		commonDao.queryBySql(sql, null);
		//新增调查内容
		for(Map.Entry<String, String> entry: map.entrySet()){
			QzApplnDcnr dcnr = new QzApplnDcnr();
			dcnr.setApplicationId(qzApplnAttachmentList.getApplicationId());
			dcnr.setReportId(entry.getKey());
			dcnr.setReportName(entry.getValue());
			dcnr.setCustomerId(qzApplnAttachmentList.getCustomerId());
			commonDao.insertObject(dcnr);
		}
	}
	
	public String getReturnUrl(String operate){
		if(operate==null){
			return null;
		}
		if(operate.equals(Constant.status_chushen)){
			return "/intopieces/intopiecesrecieve/chushen.page";
		}else if(operate.equals(Constant.status_xingzheng1)){
			return "/intopieces/intopiecesxingzheng1/xingzhengbegin.page";
		}else if(operate.equals(Constant.status_shouxin)){
			return "/intopieces/intopiecesshouxin/shouxin.page";
		}else if(operate.equals(Constant.status_zhongxin)){
			return "/intopieces/intopieceszhongxin/zhongxin.page";
		}else if(operate.equals(Constant.status_xinshen)){
			return "/intopieces/intopiecesxindai/xindai.page";
		}else if(operate.equals(Constant.status_buchong)){
			return "/intopieces/intopiecesapprove/add_information.page";
		}else if(operate.equals(Constant.status_cardquery)){
			return "/intopieces/intopiecesqueryAll/browseAll.page";
		}else if(operate.equals(Constant.status_query)){
			return "/intopieces/intopiecesquery/browse.page";
<<<<<<< HEAD
=======
		}else if(operate.equals(Constant.status_onelevel)){
			return "/intopieces/intopiecesonelevel/onelevel.page";
		}else if(operate.equals(Constant.status_twolevel)){
			return "/intopieces/intopiecestwolevel/twolevel.page";
>>>>>>> chinhBy-master
		}else{
			return "/intopieces/intopiecesxingzheng2/xingzhengend.page";
		}
	}
	
	public Boolean getDcnrList(String appId){
		List<QzApplnDcnr> dcnrs = commonDao.queryBySql(QzApplnDcnr.class, "select * from qz_appln_dcnr where report_id= '"+Constant.htd_id+"' and application_id='"+appId+"'", null);
		if(dcnrs.size()>0){
			return true;
		}
		return false;
	}
	
	public CustomerApplicationInfo ifReturnToApprove(String customerId){
		String sql = "select * from customer_application_info where customer_id='"+customerId+"' and (status='"+Constant.RETURN_INTOPICES+"' or status='"+Constant.SAVE_INTOPICES+"')";
		List<CustomerApplicationInfo> list = commonDao.queryBySql(CustomerApplicationInfo.class, sql, null);
		if(list.size()>0){
			return list.get(0);
		}	else{
			return null;
		}
	}
	public CustomerApplicationInfo ifReturnToApproveByappId(String application_id){
		String sql = "select * from customer_application_info where id='"+application_id+"' and status='"+Constant.RETURN_INTOPICES+"'";
		List<CustomerApplicationInfo> list = commonDao.queryBySql(CustomerApplicationInfo.class, sql, null);
		if(list.size()>0){
			return list.get(0);
		}	else{
			return null;
		}
	}
	public CustomerApplicationInfo ifReturnToApproveByappIdForNew(String application_id){
		String sql = "select * from customer_application_info where id='"+application_id+"' and status='"+Constant.SAVE_INTOPICES+"'";
		List<CustomerApplicationInfo> list = commonDao.queryBySql(CustomerApplicationInfo.class, sql, null);
		if(list.size()>0){
			return list.get(0);
		}	else{
			return null;
		}
	}
	/**
	 * 根据appId查询进件申请表信息
	 * @param appId
	 * @return
	 */
	public CustomerApplicationInfo findCusAppInforByAppId(String appId){
		String sql = "select * from customer_application_info where id='"+appId+"'";
		List<CustomerApplicationInfo> list = commonDao.queryBySql(CustomerApplicationInfo.class, sql, null);
		if(list.size()>0){
			return list.get(0);
		}	else{
			return null;
		}
	}
	
	/**
	 * 获取某客户经理进件状态数量（统计）
	 * @return
	 */
	public String getStatusCount(String status,String userId){
		String sql = "select count(*) as count from customer_application_info a,basic_customer_information b where  a.customer_id=b.id and status='"+ status+"' and b.user_id='"+userId+"'";
		List<HashMap<String, Object>> list = commonDao.queryBySql(sql, null);
		return list.get(0).get("COUNT").toString();
	}
	
	/**
	 * 获取进件节点数量（统计）
	 * @return
	 */
	public List<HashMap<String, Object>> getTipCount(){
		String sql = "SELECT count(*) as count,N.NODE_NAME as NAME	FROM	CUSTOMER_APPLICATION_INFO b,BASIC_CUSTOMER_INFORMATION U,CUSTOMER_APPLICATION_PROCESS A,";
		sql+=" NODE_AUDIT N,NODE_AUDIT_USER p WHERE U.ID = b.CUSTOMER_ID and a.APPLICATION_ID=b.ID AND N.ID=A.NEXT_NODE_ID";
		sql+=" and A.NEXT_NODE_ID=p.NODE_ID 	and b.status='audit' GROUP BY N.NODE_NAME";
		List<HashMap<String, Object>> list = commonDao.queryBySql(sql, null);
		return list;
	}
	
	public QzApplnJyd getJydByCustomerId(String customerId,String appId){
		//查询决议单
		String sql ="";
		if(appId==null){
			sql= "select * from qz_appln_jyd where customer_id='"+customerId+"' and application_id is null" ;
		}else{
			sql= "select * from qz_appln_jyd where customer_id='"+customerId+"' and application_id='"+appId+"'" ;
		}
		List<QzApplnJyd> jydList = commonDao.queryBySql(QzApplnJyd.class, sql, null);
		if(jydList.size()>0){
			return jydList.get(0);
		}else{
			return null;
		}
		
	}
	
	/* 退回进件至客户经理 */
	/*
	 * TODO 1.添加注释 2.SQL写进DAO层
	 */
<<<<<<< HEAD
	public void checkDoNotToManager(String applicationId, HttpServletRequest request, int distancet, int current) throws Exception{
//		QzApplnSxjc sxjc = commonDao.findObjectById(QzApplnSxjc.class, applicationId);
//		if(sxjc==null){
//			sxjc = new QzApplnSxjc();
//			sxjc.setApplication_id(applicationId);
//		}
//		sxjc.setReality(filter.getReality());
//		sxjc.setComplete(filter.getComplete());
//		sxjc.setStandard(filter.getStandard());
//		commonDao.insertObject(sxjc);
=======
	public void checkDoNotToManager(String applicationId, HttpServletRequest request) throws Exception{
>>>>>>> chinhBy-master
		IUser user = Beans.get(LoginManager.class).getLoggedInUser(request);
		String loginId = user.getId();
		//获取进件信息
		CustomerApplicationInfo applicationInfo= commonDao.findObjectById(CustomerApplicationInfo.class, applicationId);
		//获取进件流程信息
		CustomerApplicationProcess process =  customerApplicationProcessService.findByAppId(applicationId);
		//获取客户信息
		CustomerInfor infor = commonDao.findObjectById(CustomerInfor.class, applicationInfo.getCustomerId());
//		commonDao.deleteObject(CustomerApplicationInfo.class, filter.getApplicationId());
<<<<<<< HEAD
		//通过流程表的当前节点获取上一节点
		NodeControl nodeControl = customerApplicationProcessService.getLastStatus(process.getNextNodeId());
		//循环获取目标节点
		for(int i = 0; i < current-distancet-2; i++){
			nodeControl = customerApplicationProcessService.getLastStatus(nodeControl.getCurrentNode());
		}
		System.out.println("最终节点：" + nodeControl.getCurrentNode());
				//更新业务流程表
		process.setNextNodeId(nodeControl.getCurrentNode());
=======
		String nodeSql = "select n.id from ("
				+" select t.*,connect_by_isleaf isLeaf from node_control t"
				+" start with current_node=#{currentNode}"
				+" connect by next_node= prior current_node"
				+") m,node_audit n where m.current_node=n.id"
				+ " and m.isLeaf=1";
		Map<String, Object> nodeparams = new HashMap<String, Object>();
		nodeparams.put("currentNode", process.getNextNodeId());
		List<HashMap<String, Object>> nodeList = commonDao.queryBySql(nodeSql, nodeparams);
		//更新业务流程表
		process.setNextNodeId((String)nodeList.get(0).get("ID"));
>>>>>>> chinhBy-master
		process.setAuditUser(loginId);
		process.setCreatedTime(new Date());
		process.setFallbackReason(request.getParameter("remark"));
		//process.setFallbackReason(request.getParameter("remark"));
		customerApplicationIntopieceWaitDao.updateCustomerApplicationProcessBySerialNumber(process);
		//更新状态为--退件到申请状态
		applicationInfo.setStatus(Constant.RETURN_INTOPICES);
		commonDao.updateObject(applicationInfo);
		//更新客户信息--退回
		infor.setProcessId(Constant.APPROVE_EDIT_1);
		commonDao.updateObject(infor);
		//查找当前所处流转状态
  		WfProcessRecord wfProcessRecord = commonDao.findObjectById(WfProcessRecord.class, process.getSerialNumber());
		WfStatusQueueRecord wfStatusQueueRecord = commonDao.findObjectById(WfStatusQueueRecord.class,wfProcessRecord.getWfStatusQueueRecord());
<<<<<<< HEAD
		//查找上一节点
		String beforeStatus = wfStatusQueueRecord.getBeforeStatus();
		
		//通过上一节点获取上一流程
		WfStatusQueueRecord befoRecord = wfStatusResultDao.getLastStatus(beforeStatus);
		//循环获取目标流程
		for(int i = 0; i < current-distancet-2; i++){
			befoRecord = wfStatusResultDao.getLastStatus(befoRecord.getBeforeStatus());
		}
		wfProcessRecord.setWfStatusQueueRecord(befoRecord.getId());
		commonDao.updateObject(wfProcessRecord);
		
		//将所有相关表记录删除appId
		commonDao.queryBySql("update qz_appln_za_ywsqb_r set application_id=null where application_id='"+applicationId+"'", null);
		commonDao.queryBySql("update qz_appln_ywsqb set application_id=null where application_id='"+applicationId+"'", null);
		commonDao.queryBySql("update qz_appln_dbrxx set application_id=null where application_id='"+applicationId+"'", null);
		commonDao.queryBySql("update qz_appln_attachment_list set application_id=null where application_id='"+applicationId+"'", null);
		commonDao.queryBySql("update qz_appln_nbscyjb set application_id=null where application_id='"+applicationId+"'", null);
		commonDao.queryBySql("update qz_iesb_for_circle set application_id=null where application_id='"+applicationId+"'", null);
		commonDao.queryBySql("update qz_appln_dcnr set application_id=null where application_id='"+applicationId+"'", null);
		commonDao.queryBySql("update qz_appln_jyd set application_id=null where application_id='"+applicationId+"'", null);
		commonDao.queryBySql("update qz_appln_nbscyjb set application_id=null where application_id='"+applicationId+"'", null);
=======
		String currentProcess = wfStatusQueueRecord.getCurrentProcess();
		String sql = "select t.* from wf_status_queue_record t " 
					+ "where t.current_process =  #{currentProcess}"
					+ "and t.before_status is null";
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("currentProcess", currentProcess);
		List<WfStatusQueueRecord> list = commonDao.queryBySql(WfStatusQueueRecord.class, sql, params);

		wfProcessRecord.setWfStatusQueueRecord(list.get(0).getId());
		commonDao.updateObject(wfProcessRecord);
		
		//将所有相关表记录删除appId
//		commonDao.queryBySql("update qz_appln_za_ywsqb_r set application_id=null where application_id='"+applicationId+"'", null);
//		commonDao.queryBySql("update qz_appln_ywsqb set application_id=null where application_id='"+applicationId+"'", null);
//		commonDao.queryBySql("update qz_appln_dbrxx set application_id=null where application_id='"+applicationId+"'", null);
//		commonDao.queryBySql("update qz_appln_attachment_list set application_id=null where application_id='"+applicationId+"'", null);
//		commonDao.queryBySql("update qz_appln_nbscyjb set application_id=null where application_id='"+applicationId+"'", null);
//		commonDao.queryBySql("update qz_iesb_for_circle set application_id=null where application_id='"+applicationId+"'", null);
//		commonDao.queryBySql("update qz_appln_dcnr set application_id=null where application_id='"+applicationId+"'", null);
//		commonDao.queryBySql("update qz_appln_jyd set application_id=null where application_id='"+applicationId+"'", null);
//		commonDao.queryBySql("update qz_appln_nbscyjb set application_id=null where application_id='"+applicationId+"'", null);
>>>>>>> chinhBy-master
				
		
	}
}
