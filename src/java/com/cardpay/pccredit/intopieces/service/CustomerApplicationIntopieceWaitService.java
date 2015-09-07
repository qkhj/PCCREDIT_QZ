package com.cardpay.pccredit.intopieces.service;

import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cardpay.pccredit.common.UploadFileTool;
import com.cardpay.pccredit.customer.service.CustomerInforService;
import com.cardpay.pccredit.intopieces.constant.ApplicationStatusEnum;
import com.cardpay.pccredit.intopieces.constant.Constant;
import com.cardpay.pccredit.intopieces.dao.CustomerApplicationInfoDao;
import com.cardpay.pccredit.intopieces.dao.CustomerApplicationIntopieceWaitDao;
import com.cardpay.pccredit.intopieces.filter.CustomerApplicationProcessFilter;
import com.cardpay.pccredit.intopieces.model.CustomerApplicationInfo;
import com.cardpay.pccredit.intopieces.model.CustomerApplicationProcess;
import com.cardpay.pccredit.intopieces.web.CustomerApplicationIntopieceWaitForm;
import com.cardpay.pccredit.system.constants.NodeAuditTypeEnum;
import com.cardpay.pccredit.system.model.NodeAudit;
import com.cardpay.pccredit.system.service.NodeAuditService;
import com.cardpay.workflow.constant.ApproveOperationTypeEnum;
import com.cardpay.workflow.service.ProcessService;
import com.wicresoft.jrad.base.auth.IUser;
import com.wicresoft.jrad.base.database.dao.common.CommonDao;
import com.wicresoft.jrad.base.database.model.QueryResult;
import com.wicresoft.jrad.base.web.security.LoginManager;
import com.wicresoft.util.spring.Beans;

/**
 * CustomerApplicationIntopieceWaitService类的描述
 * 
 * @author 王海东
 * @created on 2014-11-28
 * 
 * @version $Id:$
 */
@Service
public class CustomerApplicationIntopieceWaitService {

	@Autowired
	private CommonDao commonDao;

	@Autowired
	private CustomerApplicationIntopieceWaitDao customerApplicationIntopieceWaitDao;

	@Autowired
	private CustomerApplicationInfoDao customerApplicationInfoDao;

	@Autowired
	private NodeAuditService nodeAuditService;

	@Autowired
	private ProcessService processService;
	
	@Autowired
	private CustomerInforService customerInforService;
	
	@Autowired
	private IntoPiecesService intoPiecesService;
	

	// 查询所有的进件包括审核的及未审核的
	public QueryResult<CustomerApplicationIntopieceWaitForm> findCustomerApplicationIntopieceWaitForm(CustomerApplicationProcessFilter filter) {
		List<CustomerApplicationIntopieceWaitForm> listCAI = customerApplicationIntopieceWaitDao.findCustomerApplicationIntopieceWaitForm(filter);
		int size = customerApplicationIntopieceWaitDao.findCustomerApplicationIntopieceWaitCountForm(filter);
		QueryResult<CustomerApplicationIntopieceWaitForm> qs = new QueryResult<CustomerApplicationIntopieceWaitForm>(size, listCAI);
		return qs;

	}

	// 根据serialNumber查询CUSTOMER_APPLICATION_PROCESS表
	public CustomerApplicationIntopieceWaitForm findCustomerApplicationProcessBySerialNumber(String serialNumber) {
		return customerApplicationIntopieceWaitDao.findCustomerApplicationProcessBySerialNumber(serialNumber);
	}

	// 申请进件审核
	public int updateCustomerApplicationProcess(String userId) {
		// 判断自己是否有审核任务
		int i = 0;
		if (customerApplicationIntopieceWaitDao.getCustomerApplicationInfoByUserId(userId) != 0) {
			return 0;
		}
		// 是否有审核的进件
		List<CustomerApplicationProcess> listNodeId = customerApplicationIntopieceWaitDao.findCustomerApplicationInfoAll();
		for (CustomerApplicationProcess customerApplicationProcess : listNodeId) {
			List<NodeAudit> listNode = nodeAuditService.findByNodeAuditByUserId(NodeAuditTypeEnum.Product.toString(), userId, customerApplicationProcess.getProductId());
			boolean falg = false;
			for (NodeAudit nodeAudit : listNode) {
				if (customerApplicationProcess.getProductId().equals(nodeAudit.getProductId()) && customerApplicationProcess.getNextNodeId().equals(nodeAudit.getId())) {
					customerApplicationProcess.setDelayAuditUser(userId);
					customerApplicationProcess.setAuditTime(new Date());
					i = customerApplicationIntopieceWaitDao.updateCustomerApplicationProcess(customerApplicationProcess);
					
					CustomerApplicationInfo applicationInfo = commonDao.findObjectById(CustomerApplicationInfo.class, customerApplicationProcess.getApplicationId());
					applicationInfo.setModifiedBy(userId);
					applicationInfo.setModifiedTime(new Date());
					commonDao.updateObject(applicationInfo);
					
					falg = true;
					break;
				}
			}
			if(falg){
				break;
			}
		}
		return i;
		// 循环所有的节点
	}

	public void updateCustomerApplicationProcessBySerialNumberApplicationInfo(HttpServletRequest request) throws Exception {
		CustomerApplicationInfo customerApplicationInfo = new CustomerApplicationInfo();
		CustomerApplicationProcess customerApplicationProcess = new CustomerApplicationProcess();
		IUser user = Beans.get(LoginManager.class).getLoggedInUser(request);
		String loginId = user.getId();
		String serialNumber = request.getParameter("serialNumber");
		String examineAmount = request.getParameter("examineAmount");
		String applicationStatus = request.getParameter("applicationStatus");
		String applicationId = request.getParameter("applicationId");
		String customerId = request.getParameter("customerId");
		String objection = request.getParameter("objection");
		if(objection.equals("true")){
			applicationStatus = ApproveOperationTypeEnum.OBJECTION.toString();
		}
		if(StringUtils.isNotEmpty(examineAmount)){
			examineAmount = (Double.parseDouble(examineAmount) * 100) + "";
		}
		//applicationStatus 必须是ApproveOperationTypeEnum中的通过，退回，拒绝三个类型
		String examineResutl = processService.examine(serialNumber, loginId, applicationStatus, examineAmount);
		//更新单据状态
	    if (examineResutl.equals(ApproveOperationTypeEnum.REJECTAPPROVE.toString()) ||
	    		examineResutl.equals(ApproveOperationTypeEnum.RETURNAPPROVE.toString()) ||
	    		examineResutl.equals(ApproveOperationTypeEnum.NORMALEND.toString())) {
			if(examineResutl.equals(ApproveOperationTypeEnum.REJECTAPPROVE.toString())){
				customerApplicationInfo.setStatus(Constant.REFUSE_INTOPICES);
			}
			if(examineResutl.equals(ApproveOperationTypeEnum.RETURNAPPROVE.toString())){
				customerApplicationInfo.setStatus(Constant.NOPASS_INTOPICES);
				//退回时 删除提交申请备份的信息
				CustomerApplicationInfo returnApp = commonDao.findObjectById(CustomerApplicationInfo.class, applicationId);
				customerInforService.deleteCloneSubmitAppByReturn(returnApp.getCustomerId(), applicationId);
			}
			if(examineResutl.equals(ApproveOperationTypeEnum.NORMALEND.toString())){
				customerApplicationInfo.setFinalApproval(examineAmount);
				customerApplicationInfo.setStatus(Constant.APPROVED_INTOPICES);
			}
			customerApplicationInfo.setId(applicationId);
			customerApplicationInfo.setModifiedBy(user.getId());
			customerApplicationInfo.setModifiedTime(new Date());
			commonDao.updateObject(customerApplicationInfo);
			
			customerApplicationProcess.setNextNodeId(null);
		} else {
			customerApplicationInfo.setStatus(Constant.APPROVE_INTOPICES);
			customerApplicationInfo.setId(applicationId);
			customerApplicationInfo.setModifiedBy(user.getId());
			customerApplicationInfo.setModifiedTime(new Date());
			commonDao.updateObject(customerApplicationInfo);
			
			customerApplicationProcess.setNextNodeId(examineResutl);
		}
	    if(Constant.APPROVED_INTOPICES.equalsIgnoreCase(customerApplicationInfo.getStatus())){
	    	intoPiecesService.exportData(applicationId, customerId, null);
	    }
		if (StringUtils.isNotEmpty(applicationStatus) && applicationStatus.equals(ApplicationStatusEnum.RETURNAPPROVE)) {
			String fallbackReason = request.getParameter("reason");
			customerApplicationProcess.setFallbackReason(fallbackReason);
		} else if (StringUtils.isNotEmpty(applicationStatus) && applicationStatus.equals(ApplicationStatusEnum.REJECTAPPROVE)) {
			String refusalReason = request.getParameter("reason");
			customerApplicationProcess.setRefusalReason(refusalReason);
		}
		customerApplicationProcess.setProcessOpStatus(applicationStatus);
		customerApplicationProcess.setSerialNumber(serialNumber);
		customerApplicationProcess.setExamineAmount(examineAmount);
		customerApplicationProcess.setAuditUser(loginId);
		customerApplicationProcess.setCreatedTime(new Date());
		customerApplicationProcess.setExamineAmount(examineAmount);
//		customerApplicationProcess.setDelayAuditUser(user.getId());//清空字段值 
		customerApplicationIntopieceWaitDao.updateCustomerApplicationProcessBySerialNumber(customerApplicationProcess);

	}

	
	// 查询需要团队初审的进件信息
	//全部显示
	public QueryResult<CustomerApplicationIntopieceWaitForm> recieveIntopieceWaitForm(CustomerApplicationProcessFilter filter) {
		List<CustomerApplicationIntopieceWaitForm> listCAI = customerApplicationIntopieceWaitDao.IntopieceWaitForm(filter);
		int size = customerApplicationIntopieceWaitDao.CountIntopieceWaitForm(filter);
		QueryResult<CustomerApplicationIntopieceWaitForm> qs = new QueryResult<CustomerApplicationIntopieceWaitForm>(size, listCAI);
		return qs;

	}
	
	//手动搜件
	public QueryResult<CustomerApplicationIntopieceWaitForm> recieveIntopieceWaitFormM(CustomerApplicationProcessFilter filter) {
		List<CustomerApplicationIntopieceWaitForm> listCAI = customerApplicationIntopieceWaitDao.IntopieceWaitFormM(filter);
		int size = customerApplicationIntopieceWaitDao.CountIntopieceWaitFormM(filter);
		QueryResult<CustomerApplicationIntopieceWaitForm> qs = new QueryResult<CustomerApplicationIntopieceWaitForm>(size, listCAI);
		return qs;

	}
	
	// 查询需要团队初审拒件的进件
	public QueryResult<CustomerApplicationIntopieceWaitForm> IntopieceChushenRejectForm() {
		List<CustomerApplicationIntopieceWaitForm> listCAI = customerApplicationIntopieceWaitDao.IntopieceChushenRejectForm();
		int size = customerApplicationIntopieceWaitDao.CountIntopieceChushenRejectForm();
		QueryResult<CustomerApplicationIntopieceWaitForm> qs = new QueryResult<CustomerApplicationIntopieceWaitForm>(size, listCAI);
		return qs;

	}
	// 查询需要补充上会的进件
	public QueryResult<CustomerApplicationIntopieceWaitForm> shouxinAddInforForm(CustomerApplicationProcessFilter filter) {
		List<CustomerApplicationIntopieceWaitForm> listCAI = customerApplicationIntopieceWaitDao.shouxinAddInforForm(filter);
		int size = customerApplicationIntopieceWaitDao.CountshouxinAddInforForm(filter);
		QueryResult<CustomerApplicationIntopieceWaitForm> qs = new QueryResult<CustomerApplicationIntopieceWaitForm>(size, listCAI);
		return qs;

	}
	
	public void updateCustomerApplicationProcessBySerialNumberApplicationInfo1(HttpServletRequest request) throws Exception {
		CustomerApplicationInfo customerApplicationInfo = new CustomerApplicationInfo();
		CustomerApplicationProcess customerApplicationProcess = new CustomerApplicationProcess();
		IUser user = Beans.get(LoginManager.class).getLoggedInUser(request);
		String loginId = user.getId();
		String serialNumber = request.getAttribute("serialNumber").toString();
		String examineAmount = request.getAttribute("examineAmount").toString();
		String applicationStatus = request.getAttribute("applicationStatus").toString();
		String applicationId = request.getAttribute("applicationId").toString();
		String objection = request.getAttribute("objection").toString();
		if(objection.equals("true")){
			applicationStatus = ApproveOperationTypeEnum.OBJECTION.toString();
		}
		if(StringUtils.isNotEmpty(examineAmount)){
			examineAmount = (Double.parseDouble(examineAmount) * 100) + "";
		}
		//applicationStatus 必须是ApproveOperationTypeEnum中的通过，退回，拒绝三个类型
		String examineResutl = processService.examine(serialNumber, loginId, applicationStatus, examineAmount);
		//更新单据状态
	    if (examineResutl.equals(ApproveOperationTypeEnum.REJECTAPPROVE.toString()) ||
	    		examineResutl.equals(ApproveOperationTypeEnum.RETURNAPPROVE.toString()) ||
	    		examineResutl.equals(ApproveOperationTypeEnum.NORMALEND.toString())) {
			if(examineResutl.equals(ApproveOperationTypeEnum.REJECTAPPROVE.toString())){
				customerApplicationInfo.setStatus(Constant.REFUSE_INTOPICES);
			}
			if(examineResutl.equals(ApproveOperationTypeEnum.RETURNAPPROVE.toString())){
				customerApplicationInfo.setStatus(Constant.NOPASS_INTOPICES);
				//退回时 删除提交申请备份的信息
				CustomerApplicationInfo returnApp = commonDao.findObjectById(CustomerApplicationInfo.class, applicationId);
				customerInforService.deleteCloneSubmitAppByReturn(returnApp.getCustomerId(), applicationId);
			}
			if(examineResutl.equals(ApproveOperationTypeEnum.NORMALEND.toString())){
				customerApplicationInfo.setFinalApproval(examineAmount);
				customerApplicationInfo.setStatus(Constant.APPROVED_INTOPICES);
			}
			customerApplicationInfo.setId(applicationId);
			customerApplicationInfo.setModifiedBy(user.getId());
			customerApplicationInfo.setModifiedTime(new Date());
			commonDao.updateObject(customerApplicationInfo);
			
			customerApplicationProcess.setNextNodeId(null);
		} else {
			customerApplicationInfo.setStatus(Constant.APPROVE_INTOPICES);
			customerApplicationInfo.setId(applicationId);
			customerApplicationInfo.setModifiedBy(user.getId());
			customerApplicationInfo.setModifiedTime(new Date());
			commonDao.updateObject(customerApplicationInfo);
			
			customerApplicationProcess.setNextNodeId(examineResutl);
		}
		if (StringUtils.isNotEmpty(applicationStatus) && applicationStatus.equals(ApplicationStatusEnum.RETURNAPPROVE)) {
			String fallbackReason = request.getParameter("reason");
			customerApplicationProcess.setFallbackReason(fallbackReason);
		} else if (StringUtils.isNotEmpty(applicationStatus) && applicationStatus.equals(ApplicationStatusEnum.REJECTAPPROVE)) {
			String refusalReason = request.getParameter("reason");
			customerApplicationProcess.setRefusalReason(refusalReason);
		}else{
			customerApplicationProcess.setRefusalReason("");
			customerApplicationProcess.setFallbackReason("");
		}
		customerApplicationProcess.setProcessOpStatus(applicationStatus);
		customerApplicationProcess.setSerialNumber(serialNumber);
		customerApplicationProcess.setExamineAmount(examineAmount);
		customerApplicationProcess.setAuditUser(loginId);
		customerApplicationProcess.setCreatedTime(new Date());
		customerApplicationProcess.setExamineAmount(examineAmount);
//		customerApplicationProcess.setDelayAuditUser(user.getId());//清空字段值 
		customerApplicationIntopieceWaitDao.updateCustomerApplicationProcessBySerialNumber(customerApplicationProcess);

	}
	
}
