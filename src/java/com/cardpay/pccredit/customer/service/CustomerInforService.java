package com.cardpay.pccredit.customer.service;

import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.cardpay.pccredit.QZBankInterface.service.ECIFService;
import com.cardpay.pccredit.common.Dictionary;
import com.cardpay.pccredit.common.UploadFileTool;
import com.cardpay.pccredit.customer.constant.CustomerInforConstant;
import com.cardpay.pccredit.customer.constant.CustomerInforDStatusEnum;
import com.cardpay.pccredit.customer.constant.WfProcessInfoType;
import com.cardpay.pccredit.customer.dao.CustomerInforDao;
import com.cardpay.pccredit.customer.dao.comdao.CustomerInforCommDao;
import com.cardpay.pccredit.customer.filter.CustomerInforFilter;
import com.cardpay.pccredit.customer.filter.VideoAccessoriesFilter;
import com.cardpay.pccredit.customer.model.CustomerCareersInformation;
import com.cardpay.pccredit.customer.model.CustomerInfor;
import com.cardpay.pccredit.customer.model.CustomerInforWeb;
import com.cardpay.pccredit.datapri.service.DataAccessSqlService;
import com.cardpay.pccredit.intopieces.constant.Constant;
import com.cardpay.pccredit.intopieces.constant.IntoPiecesException;
import com.cardpay.pccredit.intopieces.filter.CustomerApplicationInfoFilter;
import com.cardpay.pccredit.intopieces.model.CustomerApplicationContact;
import com.cardpay.pccredit.intopieces.model.CustomerApplicationGuarantor;
import com.cardpay.pccredit.intopieces.model.CustomerApplicationInfo;
import com.cardpay.pccredit.intopieces.model.CustomerApplicationOther;
import com.cardpay.pccredit.intopieces.model.CustomerApplicationProcess;
import com.cardpay.pccredit.intopieces.model.CustomerApplicationRecom;
import com.cardpay.pccredit.intopieces.model.QzApplnDcnr;
import com.cardpay.pccredit.intopieces.model.VideoAccessories;
import com.cardpay.pccredit.ipad.model.ProductAttribute;
import com.cardpay.pccredit.system.constants.NodeAuditTypeEnum;
import com.cardpay.pccredit.system.constants.YesNoEnum;
import com.cardpay.pccredit.system.model.Dict;
import com.cardpay.pccredit.system.model.NodeAudit;
import com.cardpay.pccredit.system.model.NodeControl;
import com.cardpay.pccredit.system.model.SystemUser;
import com.cardpay.pccredit.system.service.NodeAuditService;
import com.cardpay.workflow.models.WfProcessInfo;
import com.cardpay.workflow.models.WfStatusInfo;
import com.cardpay.workflow.models.WfStatusResult;
import com.cardpay.workflow.service.ProcessService;
import com.wicresoft.jrad.base.database.dao.common.CommonDao;
import com.wicresoft.jrad.base.database.id.IDGenerator;
import com.wicresoft.jrad.base.database.model.BusinessModel;
import com.wicresoft.jrad.base.database.model.QueryResult;

/**
 * 
 * @author shaoming
 *
 */
@Service
public class CustomerInforService {
	@Autowired
	private DataAccessSqlService dataAccessSqlService;

	@Autowired
	private CommonDao commonDao;

	@Autowired
	private CustomerInforDao customerInforDao;
	
	@Autowired
	private CustomerInforCommDao customerinforcommDao;
	
	@Autowired
	private NodeAuditService nodeAuditService;
	
	@Autowired
	private ProcessService processService;
	
	@Autowired
	private ECIFService eCIFService;
	
	/**
	 * 得到该客户经理下的客户数量
	 * @param userId
	 * @return
	 */
	public int findCustomerInforCountByUserId(String userId){
		return customerInforDao.findCustomerInforCountByUserId(userId);
	}
	/**
	 * 查询name
	 * 
	 */
	
	public List<CustomerInfor> findCustomerInforByName(String userId,String name){
		return customerInforDao.findCustomerInforByName(userId,name);
	}
	
	/**
	 * 根据证件号码查询
	 * 
	 */
	public CustomerInfor findCustomerInforByCardId(String cardId){
		return customerinforcommDao.findCustomerInforByCardId(cardId);
	}
	
	/**
	 * 根据证件号码查询
	 * 
	 */
	public CustomerInfor findCustomerInforByCustomerId(String customerId){
		return customerinforcommDao.findCustomerInforByCustomerId(customerId);
	}
	
	/**
	 * 插入数据
	 * @param customerinfo
	 * @return
	 */
	public String insertCustomerInfor(CustomerInfor customerinfor) {
		String id = IDGenerator.generateID();
		customerinfor.setId(id);
		customerinfor.setCreatedTime(new Date());
		commonDao.insertObject(customerinfor);
		return customerinfor.getId();
	}
	/**
	 * 过滤查询
	 * @param filter
	 * @return
	 */
	public QueryResult<CustomerInfor> findCustomerInforByFilter(CustomerInforFilter filter) {
		/*filter.setSqlString(dataAccessSqlService.getSqlByResTbl(filter.getRequest(), ResourceTableEnum.KEHU));*/
		
		return commonDao.findObjectsByFilter(CustomerInfor.class, filter);
	}
	
	/**
	 * 过滤查询  关联ecif开户信息
	 * @param filter
	 * @return
	 */
	public QueryResult<CustomerInfor> findCustomerInforWithEcifByFilter(CustomerInforFilter filter) {
		/*filter.setSqlString(dataAccessSqlService.getSqlByResTbl(filter.getRequest(), ResourceTableEnum.KEHU));*/
		
		return eCIFService.findCustomerInforWithEcifByFilter(filter);
	}
	
	/**
	 * 过滤查询影像资料
	 * @param filter
	 * @return
	 */
	public QueryResult<VideoAccessories> findCustomerVideoAccessoriesByFilter(VideoAccessoriesFilter filter) {
		return commonDao.findObjectsByFilter(VideoAccessories.class, filter);
	}
	
	
	
	/**
	 * 按id查找相应的客户基本信息
	 * @param customerInforId
	 * @return
	 */
	public CustomerInfor findCustomerInforById(String customerInforId){
		return commonDao.findObjectById(CustomerInfor.class, customerInforId);
	}
	
	/**
	 * 按经理Id查找相应的客户基本信息
	 * @param customerInforId
	 * @return
	 */
	public  List<CustomerInfor> findCustomerByManagerId(String managerId){
		
		List<CustomerInfor> customerInfors = customerinforcommDao.findCustomerByManagerId(managerId);
           return customerInfors;
	}
	
	/**
	 * 修改客户信息
	 * @param customerInfor
	 * @return
	 */
	public int updateCustomerInfor(CustomerInfor customerInfor) {
		customerInfor.setModifiedTime(Calendar.getInstance().getTime());
		return commonDao.updateObject(customerInfor);
	}
	/**
	 * 删除所属id的客户基本信息
	 * @param customerinforId
	 * @return
	 */
	public int deleteCustomerInfor(String customerinforId){
		return commonDao.deleteObject(CustomerInfor.class, customerinforId);
	}
    //TODO 以下数条获得单一属性方法建议可重构，保留也可
	/**
	 * 获取国籍
	 * @return
	 */
	public List<Dict> findNationality(){
		List<Dict> nationalities = customerInforDao.findNationality();
		return nationalities;
	}
	/**
	 * 获取证件类型
	 * @return
	 */
	public List<Dict> findCardType(){
		List<Dict> cardtypes = customerInforDao.findCardType();
		return cardtypes;
	}
	public String findTypeNameByTypeCode(String typecode){
		return customerInforDao.findTypeNameByTypeCode(typecode);
	}
	/**
	 * 获取婚姻状况
	 * @return
	 */
	public List<Dict> findMaritalStatus(){
		List<Dict> maritalstatuses = customerInforDao.findMaritalStatus();
		return maritalstatuses;
	}
	/**
	 * 获取住宅类型
	 * @return
	 */
	public List<Dict> findResidentialPropertie(){
		List<Dict> residentialproperties = customerInforDao.findResidentialPropertie();
		return residentialproperties;
	}
	
	/**
	 * 获取客户账户信息中的在我行开户情况
	 * @return
	 */
	public List<Dict> findOaccountMybankList(){
		List<Dict> oaccountMybanks = customerinforcommDao.findOaccountMybankList();
		return oaccountMybanks;
	}
	
	/**
	 * 获取客户账户持卡情况
	 * @return
	 */
	public List<Dict> findCreditCardList(){
		List<Dict> oaccountMybanks = customerinforcommDao.findCreditCardList();
		return oaccountMybanks;
	}
	
	/**
	 * 获取客户在我行发工资情况
	 * @return
	 */
	public List<Dict> findPayMybankList(){
		List<Dict> oaccountMybanks = customerinforcommDao.findPayMybankList();
		return oaccountMybanks;
	}
	
	/**
	 * 获取教育程度
	 * @return
	 */
	public List<Dict> findDegreeeducationList(){
		List<Dict> degreeeducationList = customerinforcommDao.findDegreeeducationList();
		return degreeeducationList;
	}
	
	/**
	 * 获取扣款方式
	 * @return
	 */
	public List<Dict> debitWayList(){
		List<Dict> debitWayList = customerinforcommDao.findDegreeeducationList();
		return debitWayList;
	}
	
	/**
	 * 获取性别
	 * @return
	 */
	public List<Dict> findSex(){
		List<Dict> sexs = customerInforDao.findSex();
		return sexs;
	}
	/**
	 * 获取职务
	 * @return
	 */
	public List<Dict> findPositio(){
		return customerInforDao.findPositio();
	}
	/**
	 * 获取职称
	 * @return
	 */
	public List<Dict> findTitle(){
		return customerInforDao.findTitle();
	}
	/**
	 * 获取单位性质
	 * @return
	 */
	public List<Dict> findUnitPropertis(){
		List<Dict> unitPropertis = customerInforDao.findUnitPropertis();
		return unitPropertis;
	}
	/**
	 * 获得行业类型
	 * @return
	 */
	public List<Dict> findIndustryType(){
		List<Dict> industryTypes = customerInforDao.findIndustryType();
		return industryTypes;
	}
	/**
	 * 获得催收方式
	 * @return
	 */
	public List<Dict> findCollectionMethod(){
		List<Dict> collectionMethods = customerInforDao.findCollectionMethod();
		return collectionMethods;
	}
	/**
	 * 获取国籍、证件类型、婚姻状况、住宅类型字段
	 * @return
	 */
	public HashMap<String,List<Dict>> findDict(){
		HashMap<String,List<Dict>> dicts = new HashMap<String,List<Dict>>();
		List<Dict> nationalities = findNationality();
		List<Dict> cardtypes = findCardType();
		List<Dict> maritalstatuses = findMaritalStatus();
		List<Dict> residentialproperties = findResidentialPropertie();
		dicts.put("nationality", nationalities);
		dicts.put("cardtype", cardtypes);
		dicts.put("maritalstatus", maritalstatuses);
		dicts.put("residentialpropertie", residentialproperties);
		dicts.put("sex", this.findSex());
		dicts.put("unitPropertis", this.findUnitPropertis());
		return dicts;
	}	
	/**
	 * 得到页面客户显示信息
	 * @param id
	 * @return
	 */
	public CustomerInforWeb findCustomerInforWebById(String id){
		return customerInforDao.findCustomerInforWebById(id);
	}
	
	/* 批量导入客户数据 excel2003或者excel2007格式*/
	public void importBatchCustomerInfoByExcel(MultipartFile file,String userId) throws IOException,IntoPiecesException, ParseException {
		String fileName = file.getOriginalFilename();
		List<BusinessModel> customerInforList = new ArrayList<BusinessModel>();
		List<BusinessModel> customerCareersInformationList = new ArrayList<BusinessModel>();
		List<BusinessModel> customerApplicationInfoList = new ArrayList<BusinessModel>();
		List<BusinessModel> customerApplicationGuarantorList = new ArrayList<BusinessModel>();
		List<BusinessModel> customerApplicationContactList = new ArrayList<BusinessModel>();
		List<BusinessModel> customerApplicationRecomList = new ArrayList<BusinessModel>();
		List<BusinessModel> customerApplicationOtherList = new ArrayList<BusinessModel>();
		HashMap<String,List<Dict>> dicts  = this.findDict();
		List<ProductAttribute> productAttributeList = commonDao.queryBySql(ProductAttribute.class, "select * from PRODUCT_ATTRIBUTE", null);
		if(fileName.endsWith(CustomerInforConstant.EXCEL_2003)){
			HSSFWorkbook hssfWorkbook = new HSSFWorkbook(file.getInputStream());
			/*循环工作表Sheet*/
			for (int numSheet = 0; numSheet < hssfWorkbook.getNumberOfSheets(); numSheet++) {
				HSSFSheet hssfSheet = hssfWorkbook.getSheetAt(numSheet);
				if (hssfSheet == null) {
					continue;
				}
				switch(numSheet){
					case 0:this.getExcel2003Content(hssfSheet,numSheet,customerInforList);break;
					case 1:this.getExcel2003Content(hssfSheet,numSheet,customerCareersInformationList);break;
					case 2:this.getExcel2003Content(hssfSheet,numSheet,customerApplicationInfoList);break;
					case 3:this.getExcel2003Content(hssfSheet,numSheet,customerApplicationGuarantorList);break;
					case 4:this.getExcel2003Content(hssfSheet,numSheet,customerApplicationContactList);break;
					case 5:this.getExcel2003Content(hssfSheet,numSheet,customerApplicationRecomList);break;
					case 6:this.getExcel2003Content(hssfSheet,numSheet,customerApplicationOtherList);break;
					default:break;
				}
			}
		}else if(fileName.endsWith(CustomerInforConstant.EXCEL_2007)){
			XSSFWorkbook xssfWorkbook = new XSSFWorkbook(file.getInputStream());
			/*循环工作表Sheet*/
			for (int numSheet = 0; numSheet < xssfWorkbook.getNumberOfSheets(); numSheet++) {
				XSSFSheet xssfSheet = xssfWorkbook.getSheetAt(numSheet);
				if (xssfSheet == null) {
					continue;
				}
				switch(numSheet){
				case 0:this.getExcel2007Content(xssfSheet,numSheet,customerInforList);break;
				case 1:this.getExcel2007Content(xssfSheet,numSheet,customerCareersInformationList);break;
				case 2:this.getExcel2007Content(xssfSheet,numSheet,customerApplicationInfoList);break;
				case 3:this.getExcel2007Content(xssfSheet,numSheet,customerApplicationGuarantorList);break;
				case 4:this.getExcel2007Content(xssfSheet,numSheet,customerApplicationContactList);break;
				case 5:this.getExcel2007Content(xssfSheet,numSheet,customerApplicationRecomList);break;
				case 6:this.getExcel2007Content(xssfSheet,numSheet,customerApplicationOtherList);break;
				default:break;
			   }
			}
		}else{
			throw new IntoPiecesException(CustomerInforConstant.EXCEL_FORMAT_ERROR);
		}
		
		/*如果excel中有重复的数据抛出提示信息*/ 
		for(int i=0;i<customerInforList.size();i++){
			CustomerInfor customerInforOuter = (CustomerInfor)customerInforList.get(i);
			if(StringUtils.trimToNull(customerInforOuter.getCode())==null){
				throw new IntoPiecesException("客户基本信息中编号不能为空");
			}
			if(StringUtils.trimToNull(customerInforOuter.getChineseName())==null){
				throw new IntoPiecesException("客户基本信息中姓名不能为空");
			}
			if(StringUtils.trimToNull(customerInforOuter.getCardType())==null){
				throw new IntoPiecesException("客户基本信息中证件类型不能为空");
			}
			if(StringUtils.trimToNull(customerInforOuter.getCardId())==null){
				throw new IntoPiecesException("客户基本信息中证件号码不能为空");
			}
			String outerString = StringUtils.trim(customerInforOuter.getCardType())+StringUtils.trim(customerInforOuter.getCardId());
			for(int j=i+1;j<customerInforList.size();j++){
				CustomerInfor customerInforInner = (CustomerInfor)customerInforList.get(j);
				String innerString = StringUtils.trim(customerInforInner.getCardType())+StringUtils.trim(customerInforInner.getCardId());
				if(outerString.equalsIgnoreCase(innerString)){
					throw new IntoPiecesException("客户基本信息中："+customerInforInner.getChineseName()+"的证件类型和证件号码与别人重复!");
				}
			}
		}
		
		/*验证申请表编号是否为空*/
		for(int i=0;i<customerInforList.size();i++){
			CustomerInfor customerInfor = (CustomerInfor)customerInforList.get(i);
			if(StringUtils.trimToNull(customerInfor.getCode())==null){
				throw new IntoPiecesException("客户申请信息中编号不能为空!");
			}
			CustomerApplicationInfo customerApplicationInfo = (CustomerApplicationInfo)customerApplicationInfoList.get(i);
			customerApplicationInfo.setCustomerId(customerInfor.getId());
		}
		
		/*剔除职业表中客户编号跟客户基本信息表中编号不匹配的数据*/
		for(int i=0;i<customerCareersInformationList.size();i++){
			int count = 0;
			String customerId = null;
			CustomerCareersInformation customerCareersInformation = (CustomerCareersInformation)customerCareersInformationList.get(i);
			if(StringUtils.trimToNull(customerCareersInformation.getCustomerCode())==null){
				throw new IntoPiecesException("客户职业资料中客户编号不能为空 !");
			}
			for(int j=0;j<customerInforList.size();j++){
				CustomerInfor customerInfor =(CustomerInfor)customerInforList.get(j);
				if(!customerCareersInformation.getCustomerCode().equalsIgnoreCase(customerInfor.getCode())){
					count++;
				}else{
					customerId = customerInfor.getId();
			    	break;
			    }
			}
			if(count==customerInforList.size()){
				throw new IntoPiecesException("客户职业信息表中客户编号为"+customerCareersInformation.getCustomerCode()+"与客户信息表的编号匹配不上请检查!");
			}else{
				customerCareersInformation.setCustomerId(customerId);
				customerCareersInformation.setCustomerCode(null);
			}
		}
		/*剔除担保人,联系人,推荐人,其他资料中进件编号跟申请表信息不相符合的数据*/
		for(int i=0;i<customerApplicationGuarantorList.size();i++){
			int count = 0;
			String applicationId = null;
			CustomerApplicationGuarantor customerApplicationGuarantor = (CustomerApplicationGuarantor)customerApplicationGuarantorList.get(i);
			if(StringUtils.trimToNull(customerApplicationGuarantor.getMainApplicationFormCode())==null){
				throw new IntoPiecesException("担保人资料申请表编号不能为空 !");
			}
			for(int j=0;j<customerApplicationInfoList.size();j++){
				CustomerApplicationInfo customerApplicationInfo = (CustomerApplicationInfo)customerApplicationInfoList.get(j);
			    if(!customerApplicationGuarantor.getMainApplicationFormCode().equalsIgnoreCase(customerApplicationInfo.getCode())){
			    	count++;
			    }else{
			    	applicationId = customerApplicationInfo.getId();
			    	break;
			    }
			}
			if(count==customerApplicationInfoList.size()){
				throw new IntoPiecesException("担保人信息表中申请件编号为:"+customerApplicationGuarantor.getMainApplicationFormCode()+"与申请信息表中的编号匹配不上请检查!");
			}else{
				customerApplicationGuarantor.setMainApplicationFormId(applicationId);
				customerApplicationGuarantor.setMainApplicationFormCode(null);
			}
		}
		
		/*联系人*/
		for(int i=0;i<customerApplicationContactList.size();i++){
			int count = 0;
			String applicationId = null;
			CustomerApplicationContact customerApplicationContact = (CustomerApplicationContact)customerApplicationContactList.get(i);
			if(StringUtils.trimToNull(customerApplicationContact.getMainApplicationFormCode())==null){
				throw new IntoPiecesException("联系人资料申请表编号不能为空 !");
			}
			for(int j=0;j<customerApplicationInfoList.size();j++){
				CustomerApplicationInfo customerApplicationInfo = (CustomerApplicationInfo)customerApplicationInfoList.get(j);
			    if(!customerApplicationContact.getMainApplicationFormCode().equalsIgnoreCase(customerApplicationInfo.getCode())){
			    	count++;
			    }else{
			    	applicationId = customerApplicationInfo.getId();
			    	break;
			    }
			}
			if(count==customerApplicationInfoList.size()){
				throw new IntoPiecesException("联系人信息表中申请件编号为:"+customerApplicationContact.getMainApplicationFormCode()+"与申请信息表中的编号匹配不上请检查!");
			}else{
				customerApplicationContact.setMainApplicationFormId(applicationId);
				customerApplicationContact.setMainApplicationFormCode(null);
			}
		}
		
		/*推荐人*/
		for(int i=0;i<customerApplicationRecomList.size();i++){
			int count = 0;
			String applicationId = null;
			CustomerApplicationRecom customerApplicationRecom = (CustomerApplicationRecom)customerApplicationRecomList.get(i);
			if(StringUtils.trimToNull(customerApplicationRecom.getMainApplicationFormCode())==null){
				throw new IntoPiecesException("推荐人资料申请表编号不能为空 !");
			}
			for(int j=0;j<customerApplicationInfoList.size();j++){
				CustomerApplicationInfo customerApplicationInfo = (CustomerApplicationInfo)customerApplicationInfoList.get(j);
			    if(!customerApplicationRecom.getMainApplicationFormCode().equalsIgnoreCase(customerApplicationInfo.getCode())){
			    	count++;
			    }else{
			    	applicationId = customerApplicationInfo.getId();
			    	break;
			    }
			}
			if(count==customerApplicationInfoList.size()){
				throw new IntoPiecesException("推荐人信息表中申请件编号为:"+customerApplicationRecom.getMainApplicationFormCode()+"与申请信息表中的编号匹配不上请检查!");
			}else{
				customerApplicationRecom.setMainApplicationFormId(applicationId);
				customerApplicationRecom.setMainApplicationFormCode(null);
			}
		}
		
		/*其他*/
		for(int i=0;i<customerApplicationOtherList.size();i++){
			int count = 0;
			String applicationId = null;
			CustomerApplicationOther customerApplicationOther = (CustomerApplicationOther)customerApplicationOtherList.get(i);
			if(StringUtils.trimToNull(customerApplicationOther.getMainApplicationFormCode())==null){
				throw new IntoPiecesException("其他资料申请表编号不能为空 !");
			}
			for(int j=0;j<customerApplicationInfoList.size();j++){
				CustomerApplicationInfo customerApplicationInfo = (CustomerApplicationInfo)customerApplicationInfoList.get(j);
			    if(!customerApplicationOther.getMainApplicationFormCode().equalsIgnoreCase(customerApplicationInfo.getCode())){
			    	count++;
			    }else{
			    	applicationId = customerApplicationInfo.getId();
			    	break;
			    }
			}
			if(count==customerApplicationInfoList.size()){
				throw new IntoPiecesException("其他信息表中申请件编号为:"+customerApplicationOther.getMainApplicationFormCode()+"与申请信息表中的编号匹配不上请检查!");
			}else{
				customerApplicationOther.setMainApplicationFormId(applicationId);
				customerApplicationOther.setMainApplicationFormCode(null);
			}
		}
		
		/*检查重复上传的数据(与数据库对比)*/
		List<String> allCustorInfo = this.checkCustomerInfo();
		for(int i=0;i<customerInforList.size();i++){
			boolean isRemove = false;
			CustomerInfor obj = (CustomerInfor)customerInforList.get(i);
			if(allCustorInfo!=null&&!allCustorInfo.isEmpty()){
				for(String str :allCustorInfo){
					if(str.equalsIgnoreCase(StringUtils.trim(obj.getCardType())+StringUtils.trim(obj.getCardId()))){
						isRemove = true;
						break;
					}
				}
			}
			if(isRemove){
				throw new IntoPiecesException("系统检测到"+obj.getChineseName()+"的证件类型和证件号码已经在系统中存在,请勿重复上传!");
			}
		}
		
		for(BusinessModel obj : customerInforList){
			CustomerInfor customerInfor = (CustomerInfor)obj;
			customerInfor.setCreatedBy(userId);
			customerInfor.setCreatedTime(new Date());
			if(customerInfor!=null&&StringUtils.trimToNull(customerInfor.getBirthday())!=null){
				customerInfor.setBirthday(customerInfor.getBirthday().replaceAll("/", "-"));
			}
			for(String key:dicts.keySet()){
				List<Dict> list = dicts.get(key);
				for(Dict dict:list){
					if(dict.getTypeName().equalsIgnoreCase(customerInfor.getNationality())){
						customerInfor.setNationality(dict.getTypeCode());
						break;
					}else if(dict.getTypeName().equalsIgnoreCase(customerInfor.getCardType())){
						customerInfor.setCardType(dict.getTypeCode());
						break;
					}else if(dict.getTypeName().equalsIgnoreCase(customerInfor.getResidentialPropertie())){
						customerInfor.setResidentialPropertie(dict.getTypeCode());
						break;
					}else if(dict.getTypeName().equalsIgnoreCase(customerInfor.getMaritalStatus())){
						customerInfor.setMaritalStatus(dict.getTypeCode());
						break;
					}else if(dict.getTypeName().equalsIgnoreCase(customerInfor.getSex())){
						customerInfor.setSex(dict.getTypeCode());
						break;
					}
				}
			}
			
			for(String key:Dictionary.degreeeducationList.keySet()){
				if(Dictionary.degreeeducationList.get(key).equalsIgnoreCase(customerInfor.getDegreeEducation())){
					customerInfor.setDegreeEducation(key);
					break;
				}
			}
			/*验证身份证号码是否正确的代码
			if(CustomerInforConstant.ID_CODE.equals(customerInfor.getCardType())){
				String errorMessage = IdCardValidate.IDCardValidate(customerInfor.getCardId());
				if(StringUtils.trimToNull(errorMessage)!=null){
					throw new IntoPiecesException(customerInfor.getChineseName()+":"+errorMessage);
				}
			}*/
			/*根据客户经理编号查找客户经理id*/
			List<SystemUser> userList = commonDao.queryBySql(SystemUser.class, "select * from sys_user s where s.external_id='"+customerInfor.getUserId()+"'", null);
			if(userList!=null&&!userList.isEmpty()){
				customerInfor.setUserId(userList.get(0).getId());
			}else{
				customerInfor.setUserId(null);
			}
			commonDao.insertObject(obj);
		}
		
		for(BusinessModel obj : customerCareersInformationList){
			CustomerCareersInformation customerCareersInformation = (CustomerCareersInformation)obj;
			customerCareersInformation.setCreatedBy(userId);
			customerCareersInformation.setCreatedTime(new Date());
			/*单位性质*/
			for(String key:Dictionary.unitPropertisList.keySet()){
				if(Dictionary.unitPropertisList.get(key).equalsIgnoreCase(customerCareersInformation.getUnitNature())){
					customerCareersInformation.setUnitNature(key);
					break;
				}
			}
			/*行业类别*/
			for(String key:Dictionary.industryTypeList.keySet()){
				if(Dictionary.industryTypeList.get(key).equalsIgnoreCase(customerCareersInformation.getIndustryType())){
					customerCareersInformation.setIndustryType(key);
					break;
				}
			}
			/*职务*/
			for(String key:Dictionary.positioList.keySet()){
				if(Dictionary.positioList.get(key).equalsIgnoreCase(customerCareersInformation.getPositio())){
					customerCareersInformation.setPositio(key);
					break;
				}
			}
			/*职称*/
			for(String key:Dictionary.titleList.keySet()){
				if(Dictionary.titleList.get(key).equalsIgnoreCase(customerCareersInformation.getTitle())){
					customerCareersInformation.setTitle(key);
					break;
				}
			}
			commonDao.insertObject(obj);
		}
		
		for(BusinessModel obj : customerApplicationInfoList){
			CustomerApplicationInfo customerApplicationInfo = (CustomerApplicationInfo)obj;
			customerApplicationInfo.setCreatedBy(userId);
			customerApplicationInfo.setCreatedTime(new Date());
			for(String key:Dictionary.debitWayList.keySet()){
				if(Dictionary.debitWayList.get(key).equalsIgnoreCase(customerApplicationInfo.getDebitWay())){
					customerApplicationInfo.setDebitWay(key);
				}
			}
			if(productAttributeList!=null&&!productAttributeList.isEmpty()){
				int i =0;
				for(ProductAttribute productAttribute:productAttributeList){
					if(productAttribute.getProductName().equalsIgnoreCase(customerApplicationInfo.getProductId())){
						customerApplicationInfo.setProductId(productAttribute.getId());
						break;
					}else{
						i++;
					}
				}
				if(i==productAttributeList.size()){
					throw new IntoPiecesException(customerApplicationInfo.getProductId()+"不是合法的产品,请检查!");
				}
			}
			commonDao.insertObject(obj);
		}
		
		for(BusinessModel obj : customerApplicationGuarantorList){
			CustomerApplicationGuarantor customerApplicationGuarantor = (CustomerApplicationGuarantor)obj;
			customerApplicationGuarantor.setCreatedBy(userId);
			customerApplicationGuarantor.setCreatedTime(new Date());
			for(String key:dicts.keySet()){
				List<Dict> list = dicts.get(key);
				for(Dict dict:list){
					if(dict.getTypeName().equalsIgnoreCase(customerApplicationGuarantor.getSex())){
						customerApplicationGuarantor.setSex(dict.getTypeCode());
						break;
					}
				}
			}
			commonDao.insertObject(obj);
		}
		
		for(BusinessModel obj : customerApplicationContactList){
			CustomerApplicationContact customerApplicationContact = (CustomerApplicationContact)obj;
			customerApplicationContact.setCreatedBy(userId);
			customerApplicationContact.setCreatedTime(new Date());
			for(String key:dicts.keySet()){
				List<Dict> list = dicts.get(key);
				for(Dict dict:list){
					if(dict.getTypeName().equalsIgnoreCase(customerApplicationContact.getSex())){
						customerApplicationContact.setSex(dict.getTypeCode());
						break;
					}
				}
			}
			commonDao.insertObject(obj);
		}
		
		for(BusinessModel obj : customerApplicationRecomList){
			CustomerApplicationRecom customerApplicationRecom = (CustomerApplicationRecom)obj;
			customerApplicationRecom.setCreatedBy(userId);
			customerApplicationRecom.setCreatedTime(new Date());
			commonDao.insertObject(obj);
		}
		
		for(BusinessModel obj : customerApplicationOtherList){
			CustomerApplicationOther customerApplicationOther = (CustomerApplicationOther)obj;
			customerApplicationOther.setCreatedBy(userId);
			customerApplicationOther.setCreatedTime(new Date());
			if("使用密码".equalsIgnoreCase(customerApplicationOther.getConsumptionUsePassword())){
				customerApplicationOther.setConsumptionUsePassword("1");
			}else{
				customerApplicationOther.setConsumptionUsePassword("0");
			}
			if("开通".equalsIgnoreCase(customerApplicationOther.getSmsOpeningTrading())){
				customerApplicationOther.setSmsOpeningTrading("1");
			}else{
				customerApplicationOther.setSmsOpeningTrading("0");
			}
			
			if("纸质账单".equalsIgnoreCase(customerApplicationOther.getBillingMethod())){
				customerApplicationOther.setBillingMethod("0");
			}else if("电子账单".equalsIgnoreCase(customerApplicationOther.getBillingMethod())){
				customerApplicationOther.setBillingMethod("1");
			}
			if("到网点领取".equalsIgnoreCase(customerApplicationOther.getCollarCardMode())){
				customerApplicationOther.setCollarCardMode("0");
			}else if("普通邮寄".equalsIgnoreCase(customerApplicationOther.getCollarCardMode())){
				customerApplicationOther.setCollarCardMode("1");
			}else if("快递".equalsIgnoreCase(customerApplicationOther.getCollarCardMode())){
				customerApplicationOther.setCollarCardMode("2");
			}
			if("是".equalsIgnoreCase(customerApplicationOther.getUseSecondCard())){
				customerApplicationOther.setUseSecondCard("1");
			}else{
				customerApplicationOther.setUseSecondCard("0");
			}
			commonDao.insertObject(obj);
		}
	}
	
	/*读取excel2003*/
	private void getExcel2003Content(HSSFSheet hssfSheet, int i,List<BusinessModel> list) {
		if (i == 0) {
			/* 第一行是表头, 所以从第二行开始遍历 */
			for (int rowNum = 1; rowNum <= hssfSheet.getLastRowNum(); rowNum++) {
				HSSFRow hssfRow = hssfSheet.getRow(rowNum);
				if (hssfRow == null) {
					continue;
				}
				CustomerInfor customerInfor = new CustomerInfor();
				customerInfor.setId(IDGenerator.generateID());
                for(int cellNum=0;cellNum<=18;cellNum++){
                	switch(cellNum){
                	   case 0:customerInfor.setCode(this.getExcel2003Value(hssfRow.getCell(0))); break;
                	   case 1:customerInfor.setChineseName(this.getExcel2003Value(hssfRow.getCell(1))); break;
                	   case 2:customerInfor.setNationality(this.getExcel2003Value(hssfRow.getCell(2))); break;
                	   case 3:customerInfor.setSex(this.getExcel2003Value(hssfRow.getCell(3))); break;
                	   case 4:customerInfor.setPinyinenglishName(this.getExcel2003Value(hssfRow.getCell(4))); break;
                	   case 5:customerInfor.setBirthday(this.getExcel2003Value(hssfRow.getCell(5))); break;
                	   case 6:customerInfor.setCardType(this.getExcel2003Value(hssfRow.getCell(6))); break;
                	   case 7:customerInfor.setCardId(this.getExcel2003Value(hssfRow.getCell(7))); break;
                	   case 8:customerInfor.setResidentialAddress(this.getExcel2003Value(hssfRow.getCell(8))); break;
                	   case 9:customerInfor.setZipCode(this.getExcel2003Value(hssfRow.getCell(9))); break;
                	   case 10:customerInfor.setHomePhone(this.getExcel2003Value(hssfRow.getCell(10))); break;
                	   case 11:customerInfor.setTelephone(this.getExcel2003Value(hssfRow.getCell(11))); break;
                	   case 12:customerInfor.setMail(this.getExcel2003Value(hssfRow.getCell(12))); break;
                	   case 13:customerInfor.setResidentialPropertie(this.getExcel2003Value(hssfRow.getCell(13))); break;
                	   case 14:customerInfor.setMaritalStatus(this.getExcel2003Value(hssfRow.getCell(14))); break;
                	   case 15:customerInfor.setDegreeEducation(this.getExcel2003Value(hssfRow.getCell(15))); break;
                	   case 16:customerInfor.setHouseholdAddress(this.getExcel2003Value(hssfRow.getCell(16))); break;
                	   case 17:customerInfor.setZipCodeAddress(this.getExcel2003Value(hssfRow.getCell(17))); break;
                	   case 18:customerInfor.setUserId(this.getExcel2003Value(hssfRow.getCell(18))); break;
                	   default:break;
                	}
                }
                list.add(customerInfor);
			}
		}else if(i == 1){
			/* 第一行是表头, 所以从第二行开始遍历 */
			for (int rowNum = 1; rowNum <= hssfSheet.getLastRowNum(); rowNum++) {
				HSSFRow hssfRow = hssfSheet.getRow(rowNum);
				if (hssfRow == null) {
					continue;
				}
				CustomerCareersInformation customerCareersInformation = new CustomerCareersInformation();
                for(int cellNum=0;cellNum<=11;cellNum++){
                	switch(cellNum){
                	   case 0:customerCareersInformation.setCustomerCode(this.getExcel2003Value(hssfRow.getCell(0))); break;
                	   case 1:customerCareersInformation.setNameUnit(this.getExcel2003Value(hssfRow.getCell(1))); break;
                	   case 2:customerCareersInformation.setDepartmentName(this.getExcel2003Value(hssfRow.getCell(2))); break;
                	   case 3:customerCareersInformation.setUnitAddress(this.getExcel2003Value(hssfRow.getCell(3))); break;
                	   case 4:customerCareersInformation.setZipCode(this.getExcel2003Value(hssfRow.getCell(4))); break;
                	   case 5:customerCareersInformation.setUnitPhone(this.getExcel2003Value(hssfRow.getCell(5))); break;
                	   case 6:customerCareersInformation.setUnitNature(this.getExcel2003Value(hssfRow.getCell(6))); break;
                	   case 7:customerCareersInformation.setIndustryType(this.getExcel2003Value(hssfRow.getCell(7))); break;
                	   case 8:customerCareersInformation.setPositio(this.getExcel2003Value(hssfRow.getCell(8))); break;
                	   case 9:customerCareersInformation.setTitle(this.getExcel2003Value(hssfRow.getCell(9))); break;
                	   case 10:customerCareersInformation.setAnnualIncome(this.getExcel2003Value(hssfRow.getCell(10))); break;
                	   case 11:customerCareersInformation.setYearWorkUnit(this.getExcel2003Value(hssfRow.getCell(11))); break;
                	   default:break;
                	}
                }
                list.add(customerCareersInformation);
			}
		}else if(i==2){
			/* 第一行是表头, 所以从第二行开始遍历 */
			for (int rowNum = 1; rowNum <= hssfSheet.getLastRowNum(); rowNum++) {
				HSSFRow hssfRow = hssfSheet.getRow(rowNum);
				if (hssfRow == null) {
					continue;
				}
				CustomerApplicationInfo customerApplicationInfo = new CustomerApplicationInfo();
				customerApplicationInfo.setId(IDGenerator.generateID());
				customerApplicationInfo.setStatus(Constant.SAVE_INTOPICES);
                for(int cellNum=0;cellNum<=5;cellNum++){
                	switch(cellNum){
                	   case 0:customerApplicationInfo.setCode(this.getExcel2003Value(hssfRow.getCell(0))); break;
                	   case 1:customerApplicationInfo.setProductId(this.getExcel2003Value(hssfRow.getCell(1))); break;
                	   case 2:customerApplicationInfo.setApplyQuota(this.getExcel2003Value(hssfRow.getCell(2))); break;
                	   case 3:customerApplicationInfo.setDebitWay(this.getExcel2003Value(hssfRow.getCell(3))); break;
                	   case 4:customerApplicationInfo.setAutomaticRepaymentAgreement(this.getExcel2003Value(hssfRow.getCell(4))); break;
                	   case 5:customerApplicationInfo.setRepaymentCardNumber(this.getExcel2003Value(hssfRow.getCell(5))); break;
                	   default:break;
                	}
                }
                list.add(customerApplicationInfo);
			}
		}else if(i==3){
			/* 第一行是表头, 所以从第二行开始遍历 */
			for (int rowNum = 1; rowNum <= hssfSheet.getLastRowNum(); rowNum++) {
				HSSFRow hssfRow = hssfSheet.getRow(rowNum);
				if (hssfRow == null) {
					continue;
				}
				CustomerApplicationGuarantor customerApplicationGuarantor = new CustomerApplicationGuarantor();
                for(int cellNum=0;cellNum<=8;cellNum++){
                	switch(cellNum){
                	   case 0:customerApplicationGuarantor.setMainApplicationFormCode(this.getExcel2003Value(hssfRow.getCell(0))); break;
                	   case 1:customerApplicationGuarantor.setGuarantorMortgagorPledge(this.getExcel2003Value(hssfRow.getCell(1))); break;
                	   case 2:customerApplicationGuarantor.setSex(this.getExcel2003Value(hssfRow.getCell(2))); break;
                	   case 3:customerApplicationGuarantor.setRelationshipWithApplicant(this.getExcel2003Value(hssfRow.getCell(3))); break;
                	   case 4:customerApplicationGuarantor.setUnitName(this.getExcel2003Value(hssfRow.getCell(4))); break;
                	   case 5:customerApplicationGuarantor.setDepartment(this.getExcel2003Value(hssfRow.getCell(5))); break;
                	   case 6:customerApplicationGuarantor.setContactPhone(this.getExcel2003Value(hssfRow.getCell(6))); break;
                	   case 7:customerApplicationGuarantor.setCellPhone(this.getExcel2003Value(hssfRow.getCell(7))); break;
                	   case 8:customerApplicationGuarantor.setDocumentNumber(this.getExcel2003Value(hssfRow.getCell(8))); break;
                	   default:break;
                	}
                }
                list.add(customerApplicationGuarantor);
			}
		}else if(i==4){
			/* 第一行是表头, 所以从第二行开始遍历 */
			for (int rowNum = 1; rowNum <= hssfSheet.getLastRowNum(); rowNum++) {
				HSSFRow hssfRow = hssfSheet.getRow(rowNum);
				if (hssfRow == null) {
					continue;
				}
				CustomerApplicationContact customerApplicationContact = new CustomerApplicationContact();
                for(int cellNum=0;cellNum<=7;cellNum++){
                	switch(cellNum){
                	   case 0:customerApplicationContact.setMainApplicationFormCode(this.getExcel2003Value(hssfRow.getCell(0))); break;
                	   case 1:customerApplicationContact.setContactName(this.getExcel2003Value(hssfRow.getCell(1))); break;
                	   case 2:customerApplicationContact.setSex(this.getExcel2003Value(hssfRow.getCell(2))); break;
                	   case 3:customerApplicationContact.setRelationshipWithApplicant(this.getExcel2003Value(hssfRow.getCell(3))); break;
                	   case 4:customerApplicationContact.setUnitName(this.getExcel2003Value(hssfRow.getCell(4))); break;
                	   case 5:customerApplicationContact.setDepartment(this.getExcel2003Value(hssfRow.getCell(5))); break;
                	   case 6:customerApplicationContact.setContactPhone(this.getExcel2003Value(hssfRow.getCell(6))); break;
                	   case 7:customerApplicationContact.setCellPhone(this.getExcel2003Value(hssfRow.getCell(7))); break;
                	   default:break;
                	}
                }
                list.add(customerApplicationContact);
			}
		}else if(i==5){
			/* 第一行是表头, 所以从第二行开始遍历 */
			for (int rowNum = 1; rowNum <= hssfSheet.getLastRowNum(); rowNum++) {
				HSSFRow hssfRow = hssfSheet.getRow(rowNum);
				if (hssfRow == null) {
					continue;
				}
				CustomerApplicationRecom customerApplicationRecom = new CustomerApplicationRecom();
                for(int cellNum=0;cellNum<=4;cellNum++){
                	switch(cellNum){
                	   case 0:customerApplicationRecom.setMainApplicationFormCode(this.getExcel2003Value(hssfRow.getCell(0))); break;
                	   case 1:customerApplicationRecom.setName(this.getExcel2003Value(hssfRow.getCell(1))); break;
                	   case 2:customerApplicationRecom.setOutlet(this.getExcel2003Value(hssfRow.getCell(2))); break;
                	   case 3:customerApplicationRecom.setContactPhone(this.getExcel2003Value(hssfRow.getCell(3))); break;
                	   case 4:customerApplicationRecom.setRecommendedIdentityCardNumb(this.getExcel2003Value(hssfRow.getCell(4))); break;
                	   default:break;
                	}
                }
                list.add(customerApplicationRecom);
			}
		}else if(i==6){
			/* 第一行是表头, 所以从第二行开始遍历 */
			for (int rowNum = 1; rowNum <= hssfSheet.getLastRowNum(); rowNum++) {
				HSSFRow hssfRow = hssfSheet.getRow(rowNum);
				if (hssfRow == null) {
					continue;
				}
				CustomerApplicationOther customerApplicationOther = new CustomerApplicationOther();
                for(int cellNum=0;cellNum<=5;cellNum++){
                	switch(cellNum){
                	   case 0:customerApplicationOther.setMainApplicationFormCode(this.getExcel2003Value(hssfRow.getCell(0))); break;
                	   case 1:customerApplicationOther.setBillingMethod(this.getExcel2003Value(hssfRow.getCell(1))); break;
                	   case 2:customerApplicationOther.setPaperBillingShippingAddress(this.getExcel2003Value(hssfRow.getCell(2))); break;
                	   case 3:customerApplicationOther.setCollarCardMode(this.getExcel2003Value(hssfRow.getCell(3))); break;
                	   case 4:customerApplicationOther.setConsumptionUsePassword(this.getExcel2003Value(hssfRow.getCell(4))); break;
                	   case 5:customerApplicationOther.setSmsOpeningTrading(this.getExcel2003Value(hssfRow.getCell(5))); break;
                	   default:break;
                	}
                }
                list.add(customerApplicationOther);
			}
		}
	}
	
	/*读取excel2007*/
	private void getExcel2007Content(XSSFSheet xssfSheet, int i,List<BusinessModel> list) {
		if (i == 0) {
			/* 第一行是表头, 所以从第二行开始遍历 */
			for (int rowNum = 1; rowNum <= xssfSheet.getLastRowNum(); rowNum++) {
				XSSFRow hssfRow = xssfSheet.getRow(rowNum);
				if (hssfRow == null) {
					continue;
				}
				CustomerInfor customerInfor = new CustomerInfor();
				customerInfor.setId(IDGenerator.generateID());
                for(int cellNum=0;cellNum<=18;cellNum++){
                	switch(cellNum){
                	   case 0:customerInfor.setCode(this.getExcel2007Value(hssfRow.getCell(0))); break;
                	   case 1:customerInfor.setChineseName(this.getExcel2007Value(hssfRow.getCell(1))); break;
                	   case 2:customerInfor.setNationality(this.getExcel2007Value(hssfRow.getCell(2))); break;
                	   case 3:customerInfor.setSex(this.getExcel2007Value(hssfRow.getCell(3))); break;
                	   case 4:customerInfor.setPinyinenglishName(this.getExcel2007Value(hssfRow.getCell(4))); break;
                	   case 5:customerInfor.setBirthday(this.getExcel2007Value(hssfRow.getCell(5))); break;
                	   case 6:customerInfor.setCardType(this.getExcel2007Value(hssfRow.getCell(6))); break;
                	   case 7:customerInfor.setCardId(this.getExcel2007Value(hssfRow.getCell(7))); break;
                	   case 8:customerInfor.setResidentialAddress(this.getExcel2007Value(hssfRow.getCell(8))); break;
                	   case 9:customerInfor.setZipCode(this.getExcel2007Value(hssfRow.getCell(9))); break;
                	   case 10:customerInfor.setHomePhone(this.getExcel2007Value(hssfRow.getCell(10))); break;
                	   case 11:customerInfor.setTelephone(this.getExcel2007Value(hssfRow.getCell(11))); break;
                	   case 12:customerInfor.setMail(this.getExcel2007Value(hssfRow.getCell(12))); break;
                	   case 13:customerInfor.setResidentialPropertie(this.getExcel2007Value(hssfRow.getCell(13))); break;
                	   case 14:customerInfor.setMaritalStatus(this.getExcel2007Value(hssfRow.getCell(14))); break;
                	   case 15:customerInfor.setDegreeEducation(this.getExcel2007Value(hssfRow.getCell(15))); break;
                	   case 16:customerInfor.setHouseholdAddress(this.getExcel2007Value(hssfRow.getCell(16))); break;
                	   case 17:customerInfor.setZipCodeAddress(this.getExcel2007Value(hssfRow.getCell(17))); break;
                	   case 18:customerInfor.setUserId(this.getExcel2007Value(hssfRow.getCell(18))); break;
                	   default:break;
                	}
                }
                list.add(customerInfor);
			}
		}else if(i == 1){
			/* 第一行是表头, 所以从第二行开始遍历 */
			for (int rowNum = 1; rowNum <= xssfSheet.getLastRowNum(); rowNum++) {
				XSSFRow hssfRow = xssfSheet.getRow(rowNum);
				if (hssfRow == null) {
					continue;
				}
				CustomerCareersInformation customerCareersInformation = new CustomerCareersInformation();
                for(int cellNum=0;cellNum<=11;cellNum++){
                	switch(cellNum){
                	   case 0:customerCareersInformation.setCustomerCode(this.getExcel2007Value(hssfRow.getCell(0))); break;
                	   case 1:customerCareersInformation.setNameUnit(this.getExcel2007Value(hssfRow.getCell(1))); break;
                	   case 2:customerCareersInformation.setDepartmentName(this.getExcel2007Value(hssfRow.getCell(2))); break;
                	   case 3:customerCareersInformation.setUnitAddress(this.getExcel2007Value(hssfRow.getCell(3))); break;
                	   case 4:customerCareersInformation.setZipCode(this.getExcel2007Value(hssfRow.getCell(4))); break;
                	   case 5:customerCareersInformation.setUnitPhone(this.getExcel2007Value(hssfRow.getCell(5))); break;
                	   case 6:customerCareersInformation.setUnitNature(this.getExcel2007Value(hssfRow.getCell(6))); break;
                	   case 7:customerCareersInformation.setIndustryType(this.getExcel2007Value(hssfRow.getCell(7))); break;
                	   case 8:customerCareersInformation.setPositio(this.getExcel2007Value(hssfRow.getCell(8))); break;
                	   case 9:customerCareersInformation.setTitle(this.getExcel2007Value(hssfRow.getCell(9))); break;
                	   case 10:customerCareersInformation.setAnnualIncome(this.getExcel2007Value(hssfRow.getCell(10))); break;
                	   case 11:customerCareersInformation.setYearWorkUnit(this.getExcel2007Value(hssfRow.getCell(11))); break;
                	   default:break;
                	}
                }
                list.add(customerCareersInformation);
			}
		}else if(i==2){
			/* 第一行是表头, 所以从第二行开始遍历 */
			for (int rowNum = 1; rowNum <= xssfSheet.getLastRowNum(); rowNum++) {
				XSSFRow hssfRow = xssfSheet.getRow(rowNum);
				if (hssfRow == null) {
					continue;
				}
				CustomerApplicationInfo customerApplicationInfo = new CustomerApplicationInfo();
				customerApplicationInfo.setId(IDGenerator.generateID());
				customerApplicationInfo.setStatus(Constant.SAVE_INTOPICES);
                for(int cellNum=0;cellNum<=5;cellNum++){
                	switch(cellNum){
                	   case 0:customerApplicationInfo.setCode(this.getExcel2007Value(hssfRow.getCell(0))); break;
                	   case 1:customerApplicationInfo.setProductId(this.getExcel2007Value(hssfRow.getCell(1))); break;
                	   case 2:customerApplicationInfo.setApplyQuota(this.getExcel2007Value(hssfRow.getCell(2))); break;
                	   case 3:customerApplicationInfo.setDebitWay(this.getExcel2007Value(hssfRow.getCell(3))); break;
                	   case 4:customerApplicationInfo.setAutomaticRepaymentAgreement(this.getExcel2007Value(hssfRow.getCell(4))); break;
                	   case 5:customerApplicationInfo.setRepaymentCardNumber(this.getExcel2007Value(hssfRow.getCell(5))); break;
                	   default:break;
                	}
                }
                list.add(customerApplicationInfo);
			}
		}else if(i==3){
			/* 第一行是表头, 所以从第二行开始遍历 */
			for (int rowNum = 1; rowNum <= xssfSheet.getLastRowNum(); rowNum++) {
				XSSFRow hssfRow = xssfSheet.getRow(rowNum);
				if (hssfRow == null) {
					continue;
				}
				CustomerApplicationGuarantor customerApplicationGuarantor = new CustomerApplicationGuarantor();
                for(int cellNum=0;cellNum<=8;cellNum++){
                	switch(cellNum){
                	   case 0:customerApplicationGuarantor.setMainApplicationFormCode(this.getExcel2007Value(hssfRow.getCell(0))); break;
                	   case 1:customerApplicationGuarantor.setGuarantorMortgagorPledge(this.getExcel2007Value(hssfRow.getCell(1))); break;
                	   case 2:customerApplicationGuarantor.setSex(this.getExcel2007Value(hssfRow.getCell(2))); break;
                	   case 3:customerApplicationGuarantor.setRelationshipWithApplicant(this.getExcel2007Value(hssfRow.getCell(3))); break;
                	   case 4:customerApplicationGuarantor.setUnitName(this.getExcel2007Value(hssfRow.getCell(4))); break;
                	   case 5:customerApplicationGuarantor.setDepartment(this.getExcel2007Value(hssfRow.getCell(5))); break;
                	   case 6:customerApplicationGuarantor.setContactPhone(this.getExcel2007Value(hssfRow.getCell(6))); break;
                	   case 7:customerApplicationGuarantor.setCellPhone(this.getExcel2007Value(hssfRow.getCell(7))); break;
                	   case 8:customerApplicationGuarantor.setDocumentNumber(this.getExcel2007Value(hssfRow.getCell(8))); break;
                	   default:break;
                	}
                }
                list.add(customerApplicationGuarantor);
			}
		}else if(i==4){
			/* 第一行是表头, 所以从第二行开始遍历 */
			for (int rowNum = 1; rowNum <= xssfSheet.getLastRowNum(); rowNum++) {
				XSSFRow hssfRow = xssfSheet.getRow(rowNum);
				if (hssfRow == null) {
					continue;
				}
				CustomerApplicationContact customerApplicationContact = new CustomerApplicationContact();
                for(int cellNum=0;cellNum<=7;cellNum++){
                	switch(cellNum){
                	   case 0:customerApplicationContact.setMainApplicationFormCode(this.getExcel2007Value(hssfRow.getCell(0))); break;
                	   case 1:customerApplicationContact.setContactName(this.getExcel2007Value(hssfRow.getCell(1))); break;
                	   case 2:customerApplicationContact.setSex(this.getExcel2007Value(hssfRow.getCell(2))); break;
                	   case 3:customerApplicationContact.setRelationshipWithApplicant(this.getExcel2007Value(hssfRow.getCell(3))); break;
                	   case 4:customerApplicationContact.setUnitName(this.getExcel2007Value(hssfRow.getCell(4))); break;
                	   case 5:customerApplicationContact.setDepartment(this.getExcel2007Value(hssfRow.getCell(5))); break;
                	   case 6:customerApplicationContact.setContactPhone(this.getExcel2007Value(hssfRow.getCell(6))); break;
                	   case 7:customerApplicationContact.setCellPhone(this.getExcel2007Value(hssfRow.getCell(7))); break;
                	   default:break;
                	}
                }
                list.add(customerApplicationContact);
			}
		}else if(i==5){
			/* 第一行是表头, 所以从第二行开始遍历 */
			for (int rowNum = 1; rowNum <= xssfSheet.getLastRowNum(); rowNum++) {
				XSSFRow hssfRow = xssfSheet.getRow(rowNum);
				if (hssfRow == null) {
					continue;
				}
				CustomerApplicationRecom customerApplicationRecom = new CustomerApplicationRecom();
                for(int cellNum=0;cellNum<=4;cellNum++){
                	switch(cellNum){
                	   case 0:customerApplicationRecom.setMainApplicationFormCode(this.getExcel2007Value(hssfRow.getCell(0))); break;
                	   case 1:customerApplicationRecom.setName(this.getExcel2007Value(hssfRow.getCell(1))); break;
                	   case 2:customerApplicationRecom.setOutlet(this.getExcel2007Value(hssfRow.getCell(2))); break;
                	   case 3:customerApplicationRecom.setContactPhone(this.getExcel2007Value(hssfRow.getCell(3))); break;
                	   case 4:customerApplicationRecom.setRecommendedIdentityCardNumb(this.getExcel2007Value(hssfRow.getCell(4))); break;
                	   default:break;
                	}
                }
                list.add(customerApplicationRecom);
			}
		}else if(i==6){
			/* 第一行是表头, 所以从第二行开始遍历 */
			for (int rowNum = 1; rowNum <= xssfSheet.getLastRowNum(); rowNum++) {
				XSSFRow hssfRow = xssfSheet.getRow(rowNum);
				if (hssfRow == null) {
					continue;
				}
				CustomerApplicationOther customerApplicationOther = new CustomerApplicationOther();
                for(int cellNum=0;cellNum<=5;cellNum++){
                	switch(cellNum){
                	   case 0:customerApplicationOther.setMainApplicationFormCode(this.getExcel2007Value(hssfRow.getCell(0))); break;
                	   case 1:customerApplicationOther.setBillingMethod(this.getExcel2007Value(hssfRow.getCell(1))); break;
                	   case 2:customerApplicationOther.setPaperBillingShippingAddress(this.getExcel2007Value(hssfRow.getCell(2))); break;
                	   case 3:customerApplicationOther.setCollarCardMode(this.getExcel2007Value(hssfRow.getCell(3))); break;
                	   case 4:customerApplicationOther.setConsumptionUsePassword(this.getExcel2007Value(hssfRow.getCell(4))); break;
                	   case 5:customerApplicationOther.setSmsOpeningTrading(this.getExcel2007Value(hssfRow.getCell(5))); break;
                	   default:break;
                	}
                }
                list.add(customerApplicationOther);
			}
		}
	}
	
	
	private String getExcel2003Value(HSSFCell hssfCell) {
		if(hssfCell==null){
			return null;
		}else if (hssfCell.getCellType() == hssfCell.CELL_TYPE_BOOLEAN) {
            // 返回布尔类型的值
            return String.valueOf(hssfCell.getBooleanCellValue());
        } else if (hssfCell.getCellType() == hssfCell.CELL_TYPE_NUMERIC) {
            // 返回数值类型的值
            return String.valueOf(hssfCell.getNumericCellValue());
        } else {
            // 返回字符串类型的值
            return String.valueOf(hssfCell.getStringCellValue());
        }
    }
	
	private String getExcel2007Value(XSSFCell hssfCell) {
		if(hssfCell==null){
			return null;
		}else if (hssfCell.getCellType() == hssfCell.CELL_TYPE_BOOLEAN) {
            // 返回布尔类型的值
            return String.valueOf(hssfCell.getBooleanCellValue());
        } else if (hssfCell.getCellType() == hssfCell.CELL_TYPE_NUMERIC) {
            // 返回数值类型的值
            return String.valueOf(hssfCell.getNumericCellValue());
        } else {
            // 返回字符串类型的值
            return String.valueOf(hssfCell.getStringCellValue());
        }
    }

	/**
	 * 修改客户信息移交状态
	 * @param id
	 * @param status
	 * @return
	 */
	public boolean updateCustomerInforDivisionalStatus(String id,CustomerInforDStatusEnum status){
		int i = customerInforDao.updateCustomerInforDivisionalStatus(id, status.toString());
		return i>0?true:false;
	}
	/**
	 * 得到客户信息移交状态
	 * @param id
	 * @return
	 */
	public boolean findCustomerInforDivisionalStatus(String id){
		String result = customerInforDao.getCustomerInforDivisionalStatus(id);
		if(result.equals(CustomerInforDStatusEnum.turn)){
			return false;
		}else{
			return true;
		}
	}
	
	/**
	 * 进件申请提交时 客户维护资料做快照  7张表对应客户的数据快照
	 * @param customerId 客户id
	 * @param applicationId 进件申请id
	 * @throws SQLException 
	 */
	public void insertCustomerInfoBySubmitApp(String customerId, String applicationId,String productId){
		customerInforDao.cloneBasicCustomerInfo(customerId, applicationId);
		customerInforDao.cloneCustomerCareersInf(customerId, applicationId);
		customerInforDao.cloneCustomerMainManager(customerId, applicationId);
		customerInforDao.cloneCustomerQuestionInfo(customerId, applicationId);
		customerInforDao.cloneCustomerWorkshipInfo(customerId, applicationId);
		customerInforDao.cloneDimensionalModelCredit(customerId, applicationId);
		customerInforDao.cloneCustomerVideoAccessories(customerId, applicationId);
		//添加申请件流程
		WfProcessInfo wf=new WfProcessInfo();
		wf.setProcessType(WfProcessInfoType.process_type);
		wf.setVersion("1");
		commonDao.insertObject(wf);
		List<NodeAudit> list=nodeAuditService.findByNodeTypeAndProductId(NodeAuditTypeEnum.Product.name(),productId);
		boolean startBool=false;
		boolean endBool=false;
		//节点id和WfStatusInfo id的映射
		Map<String, String> nodeWfStatusMap = new HashMap<String, String>();
		for(NodeAudit nodeAudit:list){
			if(nodeAudit.getIsstart().equals(YesNoEnum.YES.name())){
				startBool=true;
			}
			
			if(startBool&&!endBool){
				WfStatusInfo wfStatusInfo=new WfStatusInfo();
				wfStatusInfo.setIsStart(nodeAudit.getIsstart().equals(YesNoEnum.YES.name())?"1":"0");
				wfStatusInfo.setIsClosed(nodeAudit.getIsend().equals(YesNoEnum.YES.name())?"1":"0");
				wfStatusInfo.setRelationedProcess(wf.getId());
				wfStatusInfo.setStatusName(nodeAudit.getNodeName());
				wfStatusInfo.setStatusCode(nodeAudit.getId());
				commonDao.insertObject(wfStatusInfo);
				
				nodeWfStatusMap.put(nodeAudit.getId(), wfStatusInfo.getId());
				
				if(nodeAudit.getIsstart().equals(YesNoEnum.YES.name())){
					//添加初始审核
					CustomerApplicationProcess customerApplicationProcess=new CustomerApplicationProcess();
					String serialNumber = processService.start(wf.getId());
					customerApplicationProcess.setSerialNumber(serialNumber);
					customerApplicationProcess.setNextNodeId(nodeAudit.getId()); 
					customerApplicationProcess.setApplicationId(applicationId);
					commonDao.insertObject(customerApplicationProcess);
					
					CustomerApplicationInfo applicationInfo = commonDao.findObjectById(CustomerApplicationInfo.class, applicationId);
					applicationInfo.setSerialNumber(serialNumber);
					commonDao.updateObject(applicationInfo);
				}
			}
			
			if(nodeAudit.getIsend().equals(YesNoEnum.YES.name())){
				endBool=true;
			}
		}
		//节点关系
		List<NodeControl> nodeControls = nodeAuditService.findNodeControlByNodeTypeAndProductId(NodeAuditTypeEnum.Product.name(), productId);
		for(NodeControl control : nodeControls){
			WfStatusResult wfStatusResult = new WfStatusResult();
			wfStatusResult.setCurrentStatus(nodeWfStatusMap.get(control.getCurrentNode()));
			wfStatusResult.setNextStatus(nodeWfStatusMap.get(control.getNextNode()));
			wfStatusResult.setExamineResult(control.getCurrentStatus());
			commonDao.insertObject(wfStatusResult);
		}
	}
	
	/**
	 * 进件申请退回时 删除备份表中该单据的信息
	 * @param customerId
	 * @param applicationId
	 */
	public void deleteCloneSubmitAppByReturn(String customerId, String applicationId){
		customerInforDao.deleteCloneBasicCustomerInfo(customerId, applicationId);
		customerInforDao.deleteCloneCustomerCareersInf(customerId, applicationId);
		customerInforDao.deleteCloneCustomerMainManager(customerId, applicationId);
		customerInforDao.deleteCloneCustomerQuestionInfo(customerId, applicationId);
		customerInforDao.deleteCloneCustomerWorkshipInfo(customerId, applicationId);
		customerInforDao.deleteCloneDimensionalModelCredit(customerId, applicationId);
		customerInforDao.deleteCloneCustomerVideoAccessories(customerId, applicationId);
	}
	
	/**
	 * 生成流水
	 */
	
	/**
	 * 客户信息批量上传校验不能上传重复的客户信息,判断条件为证件类型和证件号码组合
	 */
	private List<String> checkCustomerInfo(){
		return customerinforcommDao.checkCustomerInfo();
	}

	/**
	 * 根据进件申请Id查询客户维护资料快照
	 * @param applicationId
	 */
	public CustomerInfor findCloneCustomerInforByAppId(String applicationId) {
		return customerInforDao.findCloneCustomerInforByAppId(applicationId);
	}
	
	/**
	 * 保存客户影像资料
	 * @param customerId
	 */
	public void saveYxzlByCustomerId(String customerId,String remark, MultipartFile file) {
		Map<String, String> map = UploadFileTool.uploadYxzlFileBySpring(file);
		String fileName = map.get("fileName");
		String url = map.get("url");
		VideoAccessories videoAccessories = new VideoAccessories();
		videoAccessories.setId(IDGenerator.generateID());
		videoAccessories.setCustomerId(customerId);
		videoAccessories.setRemark(remark);
		videoAccessories.setCreatedTime(new Date());
		if (StringUtils.trimToNull(url) != null) {
			videoAccessories.setServerUrlPath(url);
		}
		if (StringUtils.trimToNull(fileName) != null) {
			videoAccessories.setFileName(fileName);
		}
		commonDao.insertObject(videoAccessories);
	}
	
	/**
	 * 删除客户影像资料
	 * @param id
	 */
	public void deleteYxzlById(String id){
		VideoAccessories v = commonDao.findObjectById(VideoAccessories.class, id);
		if(v!=null){
			UploadFileTool.deleteFile(v.getServerUrlPath());
		}
		commonDao.deleteObject(VideoAccessories.class, id);
	}
	
	/**
	 * 下载客户影像资料
	 * @param id
	 * @throws Exception 
	 */
	public void downLoadYxzlById(HttpServletResponse response,String id) throws Exception{
		VideoAccessories v = commonDao.findObjectById(VideoAccessories.class, id);
		if(v!=null){
			UploadFileTool.downLoadFile(response, v.getServerUrlPath(), v.getFileName());
		}
	}
	
	
	/**
	 * 生成影像资料缩略图,返回缩略图和原始图的图片的url
	 * @param id
	 * @throws Exception 
	 */
	public Map<String,String> createThumbnail(String id) throws Exception{
		Map<String,String> map = null;
		VideoAccessories v = commonDao.findObjectById(VideoAccessories.class, id);
		if(v!=null){
			map = UploadFileTool.CreateThumbnail(Constant.FILE_PATH, v.getServerUrlPath(), v.getFileName());
		}
		return map;
	}
	
	
	/**
	 * 按申请表id查找相应的客户拍照信息
	 * @param applicationId
	 * @return CustomerInfor
	 */
	public CustomerInfor findCustomerInforsById(String applicationId){
		return customerInforDao.findCloneCustomerInforByAppId(applicationId);
	}
	
	/**
	 * 按客户id查找相应的客户置业拍照信息
	 * @param customerId
	 * @return CustomerCareersInformation
	 */
	public CustomerCareersInformation findCustomerCareersByCustomerId(String customerId, String applicationId){
		return customerInforDao.findcloneCustomerCareersByCustomerId(customerId, applicationId);
	}
	
	/**
	 * 按客户id查找其对应的影像附件资料
	 * @param customerId
	 * @return List<CustomerCareersInformation>
	 */
	public List<VideoAccessories> findCustomerVideoAccessoriesByCustomerId(String customerId){
		return customerinforcommDao.findCustomerVideoAccessoriesByCustomerId(customerId);
	}

	
	
	/**
	 * 查询客户是否已有进件流程
	 */
	public CustomerApplicationInfo ifProcess(String customerId){
		CustomerApplicationInfoFilter info = new CustomerApplicationInfoFilter();
		info.setCustomerId(customerId);
		List<CustomerApplicationInfo> listApplicationInfo = customerinforcommDao.ifProcess(customerId);
		if(listApplicationInfo.size()>0){
			return listApplicationInfo.get(0);
		}else{
			return null;
		}
	}
	/**
	 * 保存调查内容附件
	 */
	public void saveDcnr(HttpServletRequest request){
		String[] box = request.getParameterValues("checkbox");
		String customerId = request.getParameter("customerId");
		if(box!=null){
			for(int i=0;i<box.length;i++){
				if(box[i]!=null){
					System.out.println(box[i]);
					QzApplnDcnr qzApplnDcnr = new QzApplnDcnr();
					qzApplnDcnr.setCustomerId(customerId);
					qzApplnDcnr.setReportId(box[i].split("@")[0]);
					qzApplnDcnr.setReportName(box[i].split("@")[1]);
					String sql = "select * from qz_appln_dcnr where report_id='"+box[i].split("@")[0]+"' and customer_id='"+customerId+"'";
					List<QzApplnDcnr> dcnr = commonDao.queryBySql(QzApplnDcnr.class, sql, null);
					if(dcnr.size()>0){
					}else{
						commonDao.insertObject(qzApplnDcnr);
					}
				}
			}
		}
	}
	
}
