package com.cardpay.pccredit.intopieces.web;

import java.util.ArrayList;
import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cardpay.pccredit.QZBankInterface.model.Circle;
import com.cardpay.pccredit.QZBankInterface.model.ECIF;
import com.cardpay.pccredit.QZBankInterface.service.CircleService;
import com.cardpay.pccredit.QZBankInterface.service.ECIFService;
import com.cardpay.pccredit.QZBankInterface.web.IESBForCircleForm;
import com.cardpay.pccredit.QZBankInterface.web.IESBForECIFReturnMap;
import com.cardpay.pccredit.customer.constant.CustomerInforConstant;
import com.cardpay.pccredit.customer.constant.WfProcessInfoType;
import com.cardpay.pccredit.customer.dao.CustomerInforDao;
import com.cardpay.pccredit.customer.dao.comdao.CustomerInforCommDao;
import com.cardpay.pccredit.customer.filter.CustomerInforFilter;
import com.cardpay.pccredit.customer.model.CustomerInfor;
import com.cardpay.pccredit.customer.service.CustomerInforService;
import com.cardpay.pccredit.datapri.constant.DataPriConstants;
import com.cardpay.pccredit.datapri.service.DataAccessSqlService;
import com.cardpay.pccredit.intopieces.constant.Constant;
import com.cardpay.pccredit.intopieces.filter.CustomerApplicationProcessFilter;
import com.cardpay.pccredit.intopieces.model.CustomerApplicationInfo;
import com.cardpay.pccredit.intopieces.model.CustomerApplicationProcess;
import com.cardpay.pccredit.intopieces.model.QzApplnAttachmentList;
import com.cardpay.pccredit.intopieces.model.QzApplnDbrxx;
import com.cardpay.pccredit.intopieces.model.QzApplnDbrxxDkjl;
import com.cardpay.pccredit.intopieces.model.QzApplnDbrxxFc;
import com.cardpay.pccredit.intopieces.model.QzApplnDbrxxJdc;
import com.cardpay.pccredit.intopieces.model.QzApplnJyxx;
import com.cardpay.pccredit.intopieces.model.QzApplnJydBzdb;
import com.cardpay.pccredit.intopieces.model.QzApplnJydDydb;
import com.cardpay.pccredit.intopieces.model.QzApplnJydGtjkr;
import com.cardpay.pccredit.intopieces.model.QzApplnNbscyjb;
import com.cardpay.pccredit.intopieces.model.QzApplnYwsqb;
import com.cardpay.pccredit.intopieces.model.QzApplnYwsqbJkjl;
import com.cardpay.pccredit.intopieces.model.QzApplnYwsqbZygys;
import com.cardpay.pccredit.intopieces.model.QzApplnYwsqbZykh;
import com.cardpay.pccredit.intopieces.model.QzApplnZa;
import com.cardpay.pccredit.intopieces.model.QzApplnZaReturnMap;
import com.cardpay.pccredit.intopieces.model.QzAppln_Za_Ywsqb_R;
import com.cardpay.pccredit.intopieces.service.AttachmentListService;
import com.cardpay.pccredit.intopieces.model.QzApplnJyd;
import com.cardpay.pccredit.intopieces.service.CustomerApplicationIntopieceWaitService;
import com.cardpay.pccredit.intopieces.service.DbrxxService;
import com.cardpay.pccredit.intopieces.service.IntoPiecesService;
import com.cardpay.pccredit.intopieces.service.JyxxService;
import com.cardpay.pccredit.intopieces.service.NbscyjbService;
import com.cardpay.pccredit.intopieces.service.YwsqbService;
import com.cardpay.pccredit.intopieces.service.ZAService;
import com.cardpay.pccredit.intopieces.service.ZA_YWSQB_R_Service;
import com.cardpay.pccredit.product.filter.ProductFilter;
import com.cardpay.pccredit.product.model.ProductAttribute;
import com.cardpay.pccredit.product.service.ProductService;
import com.cardpay.pccredit.system.constants.NodeAuditTypeEnum;
import com.cardpay.pccredit.system.constants.YesNoEnum;
import com.cardpay.pccredit.system.model.NodeAudit;
import com.cardpay.pccredit.system.model.NodeControl;
import com.cardpay.pccredit.system.service.NodeAuditService;
import com.cardpay.workflow.models.WfProcessInfo;
import com.cardpay.workflow.models.WfStatusInfo;
import com.cardpay.workflow.models.WfStatusResult;
import com.cardpay.workflow.service.ProcessService;
import com.wicresoft.jrad.base.auth.IUser;
import com.wicresoft.jrad.base.auth.JRadModule;
import com.wicresoft.jrad.base.constant.JRadConstants;
import com.wicresoft.jrad.base.database.dao.common.CommonDao;
import com.wicresoft.jrad.base.database.id.IDGenerator;
import com.wicresoft.jrad.base.database.model.QueryResult;
import com.wicresoft.jrad.base.web.JRadModelAndView;
import com.wicresoft.jrad.base.web.controller.BaseController;
import com.wicresoft.jrad.base.web.result.JRadPagedQueryResult;
import com.wicresoft.jrad.base.web.result.JRadReturnMap;
import com.wicresoft.jrad.base.web.security.LoginManager;
import com.wicresoft.jrad.base.web.utility.WebRequestHelper;
import com.wicresoft.jrad.modules.privilege.model.User;
import com.wicresoft.util.date.DateHelper;
import com.wicresoft.util.spring.Beans;
import com.wicresoft.util.spring.mvc.mv.AbstractModelAndView;
import com.wicresoft.util.web.RequestHelper;

@Controller
@RequestMapping("/intopieces/intopiecesapprove/*")
@JRadModule("intopieces.intopiecesapprove")
public class IntoPiecesApproveControl extends BaseController {

	@Autowired
	private IntoPiecesService intoPiecesService;

	@Autowired
	private CustomerInforService customerInforService;

	@Autowired
	private ProductService productService;
	
	
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
	private CustomerApplicationIntopieceWaitService customerApplicationIntopieceWaitService;
	
	@Autowired
	private CircleService circleService;
	
	@Autowired
	private ECIFService eCIFService;
	
	@Autowired
	private YwsqbService ywsqbService;
	
	@Autowired
	private JyxxService jyxxService;
	
	@Autowired
	private DbrxxService dbrxxService;
	
	@Autowired
	private AttachmentListService attachmentListService;
	
	@Autowired
	private NbscyjbService nbscyjbService;
	
	@Autowired
	private ZAService zaService;
	
	@Autowired
	private ZA_YWSQB_R_Service za_ywsqb_r_service;
	
	public static final Logger logger = Logger.getLogger(IntoPiecesApproveControl.class);
	
	/**
	 * 申请页面
	 * 
	 * @param filter
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "approve.page", method = { RequestMethod.GET })
	public AbstractModelAndView browse(@ModelAttribute CustomerInforFilter filter,HttpServletRequest request) {
        filter.setRequest(request);
        IUser user = Beans.get(LoginManager.class).getLoggedInUser(request);
		filter.setUserId(user.getId());
		QueryResult<CustomerInfor> result = customerInforService.findCustomerInforWithEcifByFilter(filter);
		for(int i=0;i<result.getItems().size();i++){
				CustomerApplicationInfo info = customerInforService.ifProcess(result.getItems().get(i).getId());
				//目前存在申请件
				if(info!=null){
					if(info.getStatus().equals(Constant.APPROVED_INTOPICES)){
						result.getItems().get(i).setProcessId(Constant.APP_STATE_4);
					}else{
						if(!info.getStatus().equals(Constant.RETURN_INTOPICES)){
							result.getItems().get(i).setProcessId(Constant.APP_STATE_1);
						}else{
							result.getItems().get(i).setProcessId(Constant.APP_STATE_3);
						}
					}
				}else{
					result.getItems().get(i).setProcessId(Constant.APP_STATE_2);
				}
			}
		JRadPagedQueryResult<CustomerInfor> pagedResult = new JRadPagedQueryResult<CustomerInfor>(filter, result);
		JRadModelAndView mv = new JRadModelAndView("/intopieces/intopieces_approve",
                                                    request);
		mv.addObject(PAGED_RESULT, pagedResult);

		return mv;
	}
	
	/**
	 * 添加申请进件信息页面
	 * 
	 * @param request
	 * @return
	*/
	@ResponseBody
	@RequestMapping(value = "changewh.page")
	public AbstractModelAndView changewh(HttpServletRequest request) {
		JRadModelAndView mv = new JRadModelAndView("/customer/customerInforUpdate/qz_customerinfor_base", request);
		String customerInforId = RequestHelper.getStringValue(request, ID);
		if (StringUtils.isNotEmpty(customerInforId)) {
			CustomerInfor customerInfor = customerInforService.findCustomerInforById(customerInforId);
			mv.addObject("customerInfor", customerInfor);
			mv.addObject("customerId", customerInfor.getId());
		}
		return mv;
		
	}
	
	/**
	 * 执行申请
	 * @param customerInforFilter
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "save_apply.json")
	public JRadReturnMap save_apply(@ModelAttribute CustomerInforFilter customerInforFilter, HttpServletRequest request) {
		JRadReturnMap returnMap = new JRadReturnMap();
		if (returnMap.isSuccess()) {
			try {
				String customerId = request.getParameter("id");
				//检查相关的表是否填写
				//添加产品类型appId
				QzAppln_Za_Ywsqb_R qzappln_za_ywsqb_r = za_ywsqb_r_service.findByCustomerId(customerId);
				if(qzappln_za_ywsqb_r==null){
					returnMap.put(JRadConstants.MESSAGE, "请先\"选择产品\"");
					returnMap.put(JRadConstants.SUCCESS, false);
					return returnMap;
				}
				
				//添加业务申请表appId
				QzApplnYwsqb qzApplnYwsqb = ywsqbService.findYwsqb(customerId, null);
				if(qzApplnYwsqb==null){
					returnMap.put(JRadConstants.MESSAGE, "请先填写\"业务申请表\"");
					returnMap.put(JRadConstants.SUCCESS, false);
					return returnMap;
				}
				
				//添加担保人appId
				/*List<QzApplnDbrxx> dbrxx_ls = dbrxxService.findDbrxx(customerId, null);
				if(dbrxx_ls == null || dbrxx_ls.size() == 0){
					returnMap.put(JRadConstants.MESSAGE, "请先填写\"担保人信息表\"");
					returnMap.put(JRadConstants.SUCCESS, false);
					return returnMap;
				}*/
				//添加附件appId
				QzApplnAttachmentList qzApplnAttachmentList = attachmentListService.findAttachmentList(customerId, null);
				if(qzApplnAttachmentList == null){
					returnMap.put(JRadConstants.MESSAGE, "请先填写\"待决策资料清单\"");
					returnMap.put(JRadConstants.SUCCESS, false);
					return returnMap;
				}
				
				//添加内部审查appId
				QzApplnNbscyjb qzApplnNbscyjb = nbscyjbService.findNbscyjb(customerId, null);
				if(qzApplnNbscyjb == null){
					returnMap.put(JRadConstants.MESSAGE, "请先填写\"内部审查意见表\"");
					returnMap.put(JRadConstants.SUCCESS, false);
					return returnMap;
				}
				
				QzApplnJyd jyd = intoPiecesService.getSdhjydForm(customerId);
				if(jyd==null){
					returnMap.put(JRadConstants.MESSAGE, "请先填写\"审贷会决议单\"");
					returnMap.put(JRadConstants.SUCCESS, false);
					return returnMap;
				}
				
				Circle circle = circleService.findCircle(customerId,null);
				if(circle == null){
					returnMap.put(JRadConstants.MESSAGE, "请先填写\"贷款信息\"");
					returnMap.put(JRadConstants.SUCCESS, false);
					return returnMap;
				}
				
				//设置流程开始
				saveApply(customerId);
				
				returnMap.put(RECORD_ID, customerId);
				returnMap.addGlobalMessage(CREATE_SUCCESS);
			}catch (Exception e) {
				returnMap.put(JRadConstants.MESSAGE, DataPriConstants.SYS_EXCEPTION_MSG);
				returnMap.put(JRadConstants.SUCCESS, false);
				return WebRequestHelper.processException(e);
			}
		}else{
			returnMap.setSuccess(false);
			returnMap.addGlobalError(CustomerInforConstant.CREATEERROR);
		}
		return returnMap;
	}

	/**
	 * 提交申请，开始流程
	 * @param customer_id
	 */
	public void saveApply(String customer_id){
		//先判断是否为其他岗位退件的客户，如果是，只需改变状态不需再次新增申请件
		CustomerApplicationInfo info = intoPiecesService.ifReturnToApprove(customer_id);
		String appId = "";
		if(info!=null){
			info.setStatus(Constant.APPROVE_INTOPICES);
			commonDao.updateObject(info);
			appId = info.getId();
			//新增修改不是初审退下来的进件，流程信息要改为初审，不然进件重新申请回直接到退回的岗位？
		}else{
			//设置申请
			CustomerApplicationInfo customerApplicationInfo = new CustomerApplicationInfo();
			//customerApplicationInfo.setStatus(status);
			customerApplicationInfo.setId(IDGenerator.generateID());
			customerApplicationInfo.setApplyQuota("0");//设置额度
			customerApplicationInfo.setCustomerId(customer_id);
			if(customerApplicationInfo.getApplyQuota()!=null){
				customerApplicationInfo.setApplyQuota((Integer.valueOf(customerApplicationInfo.getApplyQuota())*100)+"");
			}
			customerApplicationInfo.setCreatedTime(new Date());
			customerApplicationInfo.setStatus(Constant.APPROVE_INTOPICES);
			//查找默认产品
			ProductFilter filter = new ProductFilter();
			filter.setDefault_type(Constant.DEFAULT_TYPE);
			ProductAttribute productAttribute = productService.findProductsByFilter(filter).getItems().get(0);
			customerApplicationInfo.setProductId(productAttribute.getId());
			
			commonDao.insertObject(customerApplicationInfo);
			
			
			//添加申请件流程
			WfProcessInfo wf=new WfProcessInfo();
			wf.setProcessType(WfProcessInfoType.process_type);
			wf.setVersion("1");
			commonDao.insertObject(wf);
			List<NodeAudit> list=nodeAuditService.findByNodeTypeAndProductId(NodeAuditTypeEnum.Product.name(),productAttribute.getId());
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
						customerApplicationProcess.setApplicationId(customerApplicationInfo.getId());
						commonDao.insertObject(customerApplicationProcess);
						
						CustomerApplicationInfo applicationInfo = commonDao.findObjectById(CustomerApplicationInfo.class, customerApplicationInfo.getId());
						applicationInfo.setSerialNumber(serialNumber);
						commonDao.updateObject(applicationInfo);
					}
				}
				
				if(nodeAudit.getIsend().equals(YesNoEnum.YES.name())){
					endBool=true;
				}
			}
			//节点关系
			List<NodeControl> nodeControls = nodeAuditService.findNodeControlByNodeTypeAndProductId(NodeAuditTypeEnum.Product.name(), productAttribute.getId());
			for(NodeControl control : nodeControls){
				WfStatusResult wfStatusResult = new WfStatusResult();
				wfStatusResult.setCurrentStatus(nodeWfStatusMap.get(control.getCurrentNode()));
				wfStatusResult.setNextStatus(nodeWfStatusMap.get(control.getNextNode()));
				wfStatusResult.setExamineResult(control.getCurrentStatus());
				commonDao.insertObject(wfStatusResult);
			}
			appId = customerApplicationInfo.getId();
		}
		//对之前无appId的表添加id(尤其是调查内容记录表添加appId)
		intoPiecesService.addAppId(customer_id,appId);
	}
	
	//iframe
	@ResponseBody
	@RequestMapping(value = "iframe.page")
	public AbstractModelAndView iframe(HttpServletRequest request) {
		JRadModelAndView mv = new JRadModelAndView("/qzbankinterface/appIframeInfo/iframe", request);
		String customerInforId = RequestHelper.getStringValue(request, ID);
		if (StringUtils.isNotEmpty(customerInforId)) {
			CustomerInfor customerInfor = customerInforService.findCustomerInforById(customerInforId);
			mv.addObject("customerInfor", customerInfor);
			mv.addObject("customerId", customerInfor.getId());
		}
		return mv;
	}
	
	//page0
	@ResponseBody
	@RequestMapping(value = "page0.page")
	public AbstractModelAndView page0(HttpServletRequest request) {
		JRadModelAndView mv = new JRadModelAndView("/qzbankinterface/appIframeInfo/page0", request);
		String customerInforId = RequestHelper.getStringValue(request, ID);
		QzAppln_Za_Ywsqb_R qzappln_za_ywsqb_r = za_ywsqb_r_service.findByCustomerId(customerInforId);
		if (StringUtils.isNotEmpty(customerInforId)) {
			CustomerInfor customerInfor = customerInforService.findCustomerInforById(customerInforId);
			mv.addObject("customerInfor", customerInfor);
			mv.addObject("customerId", customerInfor.getId());
		}
		mv.addObject("qzappln_za_ywsqb_r", qzappln_za_ywsqb_r);
		//查找专案信息
		List<QzApplnZaReturnMap> za_ls = zaService.findZas();
		mv.addObject("za_ls", za_ls);
		mv.addObject("za_ls_json", JSONArray.fromObject(za_ls).toString());
		//查找已配置的专案信息
		if(qzappln_za_ywsqb_r != null && qzappln_za_ywsqb_r.getProductType() != null && qzappln_za_ywsqb_r.getProductType().equals("2")){
			QzApplnZa qzApplnZa = zaService.findZaById(qzappln_za_ywsqb_r.getZaId());
			mv.addObject("qzApplnZa", qzApplnZa);
		}
		return mv;
	}
	
	//insert_page0
	@ResponseBody
	@RequestMapping(value = "insert_page0.json")
	public JRadReturnMap insert_page0(HttpServletRequest request) {
		JRadReturnMap returnMap = new JRadReturnMap();
		if (returnMap.isSuccess()) {
			try {
				String customerId = request.getParameter("customerId");
				String productType = request.getParameter("productType");
				String zaId = request.getParameter("zaId");
				
				User user = (User) Beans.get(LoginManager.class).getLoggedInUser(request);
				QzAppln_Za_Ywsqb_R qzappln_za_ywsqb_r = za_ywsqb_r_service.findByCustomerId(customerId);
				if(qzappln_za_ywsqb_r == null){
					qzappln_za_ywsqb_r = new QzAppln_Za_Ywsqb_R();
					//未填申请时 关联客户id
					qzappln_za_ywsqb_r.setCustomerId(customerId);
					qzappln_za_ywsqb_r.setProductType(productType);
					qzappln_za_ywsqb_r.setZaId(zaId);
					ywsqbService.insert_page0(qzappln_za_ywsqb_r);
				}else{
					qzappln_za_ywsqb_r.setProductType(productType);
					qzappln_za_ywsqb_r.setZaId(zaId);
					ywsqbService.update_page0(qzappln_za_ywsqb_r);
				}
				
				returnMap.addGlobalMessage(CREATE_SUCCESS);
				returnMap.setSuccess(true);
			}catch (Exception e) {
				return WebRequestHelper.processException(e);
			}
		}else{
			returnMap.setSuccess(false);
			returnMap.addGlobalError(CustomerInforConstant.CREATEERROR);
		}
		return returnMap;
	}
		
	//page1
	@ResponseBody
	@RequestMapping(value = "page1.page")
	public AbstractModelAndView page1(HttpServletRequest request) {
		
		String customerInforId = RequestHelper.getStringValue(request, ID);
		String appId = RequestHelper.getStringValue(request, "appId");
		String type = RequestHelper.getStringValue(request, "type");
		String operate = RequestHelper.getStringValue(request, "operate");
		String ifHideUser = RequestHelper.getStringValue(request, "ifHideUser");
		//查询产品信息
		QzAppln_Za_Ywsqb_R qzappln_za_ywsqb_r = null;
		if(appId != null && !appId.equals("")){
			qzappln_za_ywsqb_r = za_ywsqb_r_service.findByAppId(appId);
		}else{
			qzappln_za_ywsqb_r = za_ywsqb_r_service.findByCustomerId(customerInforId);
		}
		//查找page1信息
		QzApplnYwsqb qzApplnYwsqb = null;
		if(appId != null && !appId.equals("")){
			qzApplnYwsqb = ywsqbService.findYwsqbByAppId(appId);
		}
		else{
			qzApplnYwsqb = ywsqbService.findYwsqb(customerInforId, null);
		}
		
		QzApplnJyxx qzApplnJyxx = jyxxService.findJyxx(customerInforId, null);
		
		JRadModelAndView mv = null;
		if(qzApplnYwsqb != null){
			//获取产品类型，是贷生活就调整至page1ForLife_change
			if(qzappln_za_ywsqb_r != null){
				if("2".equals(qzappln_za_ywsqb_r.getProductType())){
					//获取专案信息
					QzApplnZa qzApplnZa = zaService.findZaById(qzappln_za_ywsqb_r.getZaId());
					mv = new JRadModelAndView("/qzbankinterface/appIframeInfo/page1ForLife_change", request);
					mv.addObject("qzApplnZa", qzApplnZa);
				}else{
					mv = new JRadModelAndView("/qzbankinterface/appIframeInfo/page1_change", request);
				}
			}else{
				mv = new JRadModelAndView("/qzbankinterface/appIframeInfo/page1_change", request);
			}
			List<QzApplnYwsqbZygys> zygys_ls = ywsqbService.findYwsqbZygys(qzApplnYwsqb.getId());
			List<QzApplnYwsqbZykh> zykh_ls = ywsqbService.findYwsqbZykh(qzApplnYwsqb.getId());
			List<QzApplnYwsqbJkjl> jkjl_ls = ywsqbService.findYwsqbJkjl(qzApplnYwsqb.getId());
			mv.addObject("qzApplnYwsqb", qzApplnYwsqb);
			mv.addObject("zygys_ls", zygys_ls);
			mv.addObject("zykh_ls", zykh_ls);
			mv.addObject("jkjl_ls", jkjl_ls);
			mv.addObject("type", type);
		}
		else{
			//获取产品类型，是贷生活就调整至page1ForLife
			if(qzappln_za_ywsqb_r != null){
				if("2".equals(qzappln_za_ywsqb_r.getProductType())){
					//获取专案信息
					QzApplnZa qzApplnZa = zaService.findZaById(qzappln_za_ywsqb_r.getZaId());
					mv = new JRadModelAndView("/qzbankinterface/appIframeInfo/page1ForLife", request);
					mv.addObject("qzApplnZa", qzApplnZa);
				}else{
					mv = new JRadModelAndView("/qzbankinterface/appIframeInfo/page1", request);
				}
			}else{
				mv = new JRadModelAndView("/qzbankinterface/appIframeInfo/page1", request);
			}
			IUser user = Beans.get(LoginManager.class).getLoggedInUser(request);
			mv.addObject("orgName",user.getOrganization().getName());
			mv.addObject("orgId",user.getOrganization().getId());
			String externalId = user.getLogin();//工号
			mv.addObject("externalId",externalId);
			mv.addObject("userName",user.getDisplayName());
			
			if (StringUtils.isNotEmpty(customerInforId)) {
				CustomerInfor customerInfor = customerInforService.findCustomerInforById(customerInforId);
				mv.addObject("customerInfor", customerInfor);
				mv.addObject("customerId", customerInfor.getId());
			}
			//查找开户信息 自动填充
			ECIF ecif = eCIFService.findEcifByCustomerId(customerInforId);
			mv.addObject("ecif", ecif);
			mv.addObject("type", type);
		}
		mv.addObject("returnUrl",intoPiecesService.getReturnUrl(operate) );
		mv.addObject("ifHideUser", ifHideUser);
		mv.addObject("qzApplnJyxx", qzApplnJyxx);
		return mv;
	}
	
	//insert_page1
	@ResponseBody
	@RequestMapping(value = "insert_page1.json")
	public JRadReturnMap insert_page1(@ModelAttribute QzApplnYwsqbForm qzApplnYwsqbForm, HttpServletRequest request) {
		JRadReturnMap returnMap = new JRadReturnMap();
		if (returnMap.isSuccess()) {
			try {
				String customerId = request.getParameter("customerId");
				QzApplnYwsqb qzApplnYwsqb = qzApplnYwsqbForm.createModel(QzApplnYwsqb.class);
				QzApplnJyxx qzApplnJyxx = qzApplnYwsqbForm.createModelJyxx();
				ywsqbService.dealWithNullValueJyxx(qzApplnJyxx);
				User user = (User) Beans.get(LoginManager.class).getLoggedInUser(request);
				qzApplnYwsqb.setCreatedBy(user.getId());
				qzApplnYwsqb.setCreatedTime(new Date());
				//未填申请时 关联客户id
				qzApplnYwsqb.setCustomerId(customerId);
				
				qzApplnJyxx.setCreatedBy(user.getId());
				qzApplnJyxx.setCreatedTime(new Date());
				//未填申请时 关联客户id
				qzApplnJyxx.setCustomerId(customerId);
				
				ywsqbService.insert_page1(qzApplnYwsqb, qzApplnJyxx,request);
				//returnMap.put(RECORD_ID, id);
				returnMap.addGlobalMessage(CREATE_SUCCESS);
				returnMap.setSuccess(true);
			}catch (Exception e) {
				return WebRequestHelper.processException(e);
			}
		}else{
			returnMap.setSuccess(false);
			returnMap.addGlobalError(CustomerInforConstant.CREATEERROR);
		}
		return returnMap;
	}
		
	//update_page1
	@ResponseBody
	@RequestMapping(value = "update_page1.json")
	public JRadReturnMap update_page1(@ModelAttribute QzApplnYwsqbForm qzApplnYwsqbForm, HttpServletRequest request) {
		JRadReturnMap returnMap = new JRadReturnMap();
		if (returnMap.isSuccess()) {
			try {
				String ywsqbId = request.getParameter("id");
				String customerId = request.getParameter("customerId");
				QzApplnYwsqb qzApplnYwsqb = qzApplnYwsqbForm.createModel(QzApplnYwsqb.class);
				qzApplnYwsqb.setCustomerId(customerId);
				ywsqbService.dealWithNullValue(qzApplnYwsqb);
				QzApplnJyxx qzApplnJyxx = qzApplnYwsqbForm.createModelJyxx();
				ywsqbService.dealWithNullValueJyxx(qzApplnJyxx);
				User user = (User) Beans.get(LoginManager.class).getLoggedInUser(request);
				//未填申请时 关联客户id
				qzApplnYwsqb.setId(ywsqbId);
				ywsqbService.update_page1(qzApplnYwsqb,qzApplnJyxx,request);
				returnMap.addGlobalMessage(CREATE_SUCCESS);
				returnMap.setSuccess(true);
			}catch (Exception e) {
				return WebRequestHelper.processException(e);
			}
		}else{
			returnMap.setSuccess(false);
			returnMap.addGlobalError(CustomerInforConstant.CREATEERROR);
		}
		return returnMap;
	}
	
	//insert_page1ForLife
	@ResponseBody
	@RequestMapping(value = "insert_page1ForLife.json")
	public JRadReturnMap insert_page1ForLife(@ModelAttribute QzApplnYwsqbForm qzApplnYwsqbForm, HttpServletRequest request) {
		JRadReturnMap returnMap = new JRadReturnMap();
		if (returnMap.isSuccess()) {
			try {
				String customerId = request.getParameter("customerId");
				String bussAddDedail = qzApplnYwsqbForm.getBussdisAddDetail();
				String bussDistrictAdd = qzApplnYwsqbForm.getBussdistrictAddress();
				qzApplnYwsqbForm.setBussdistrictAddress(bussDistrictAdd+bussAddDedail);
				QzApplnYwsqb qzApplnYwsqb = qzApplnYwsqbForm.createModel(QzApplnYwsqb.class);
//				QzApplnJyxx qzApplnJyxx = qzApplnYwsqbForm.createModelJyxx();
//				ywsqbService.dealWithNullValueJyxx(qzApplnJyxx);
				User user = (User) Beans.get(LoginManager.class).getLoggedInUser(request);
				qzApplnYwsqb.setCreatedBy(user.getId());
				qzApplnYwsqb.setCreatedTime(new Date());
				//未填申请时 关联客户id
				qzApplnYwsqb.setCustomerId(customerId);
					
//				qzApplnJyxx.setCreatedBy(user.getId());
//				qzApplnJyxx.setCreatedTime(new Date());
				//未填申请时 关联客户id
//				qzApplnJyxx.setCustomerId(customerId);
					
				ywsqbService.insert_page1ForLife(qzApplnYwsqb,request);
				//returnMap.put(RECORD_ID, id);
				returnMap.addGlobalMessage(CREATE_SUCCESS);
				returnMap.setSuccess(true);
			}catch (Exception e) {
				return WebRequestHelper.processException(e);
			}
		}else{
			returnMap.setSuccess(false);
			returnMap.addGlobalError(CustomerInforConstant.CREATEERROR);
		}
		return returnMap;
		}
		//update_page1ForLife("贷生活")
		@ResponseBody
		@RequestMapping(value = "update_page1ForLife.json")
		public JRadReturnMap update_page1ForLife(@ModelAttribute QzApplnYwsqbForm qzApplnYwsqbForm, HttpServletRequest request) {
			JRadReturnMap returnMap = new JRadReturnMap();
			if (returnMap.isSuccess()) {
				try {
					String ywsqbId = request.getParameter("id");
					String customerId = request.getParameter("customerId");
					QzApplnYwsqb qzApplnYwsqb = qzApplnYwsqbForm.createModel(QzApplnYwsqb.class);
					qzApplnYwsqb.setCustomerId(customerId);
					//ywsqbService.dealWithNullValue(qzApplnYwsqb);
					
					//判断空值
					//证件类型
					if(!qzApplnYwsqb.getGlobalType().equals("026")){
						qzApplnYwsqb.setGlobalTypeOther("");
					}
					//教育水平
					if(!qzApplnYwsqb.getEducationLevel().equals("000")){
						qzApplnYwsqb.setEducationLevelOther("");
					}
					//婚姻状况
					if(!qzApplnYwsqb.getMaritalStatus().equals("20")){
						qzApplnYwsqb.setMaritalStatusOther("");
						qzApplnYwsqb.setMaritalName("");
						qzApplnYwsqb.setMaritalGlobalType("001");
						qzApplnYwsqb.setMaritalGlobalTypeOther("");
						qzApplnYwsqb.setMaritalGlobalId("");
						qzApplnYwsqb.setMaritalWorkunit("");
						qzApplnYwsqb.setMaritalPhone("");
					}
					//配偶证件类型
					if(qzApplnYwsqb.getMaritalGlobalType() == null || !qzApplnYwsqb.getMaritalGlobalType().equals("026")){
						qzApplnYwsqb.setMaritalGlobalTypeOther("");
					}
					//家庭住址类型
					if(!qzApplnYwsqb.getAddressType().equals("5")){
						qzApplnYwsqb.setAddressTypeOther("");
					}
					//是否曾在泉州银行申请过贷款
					if(!qzApplnYwsqb.getHaveApplyLoan().equals("1")){
						qzApplnYwsqb.setHaveLoanTime(null);
					}
					//是否曾使用泉州银行电子银行产品
					if(!qzApplnYwsqb.getHaveElePro().equals("1")){
						qzApplnYwsqb.setHaveEleProType("");
					}
					//有关联的个人或实体是否获得过泉州银行贷款
					if(!qzApplnYwsqb.getHaveGotLoan().equals("1")){
						qzApplnYwsqb.setHaveGotLoanName("");
						qzApplnYwsqb.setHaveGotLoanRelation("");
					}
					//信息渠道
					if(!qzApplnYwsqb.getInfoType().equals("8")){
						qzApplnYwsqb.setInfoTypeOther("");
					}
//					QzApplnJyxx qzApplnJyxx = qzApplnYwsqbForm.createModelJyxx();
//					ywsqbService.dealWithNullValueJyxx(qzApplnJyxx);
					User user = (User) Beans.get(LoginManager.class).getLoggedInUser(request);
					//未填申请时 关联客户id
					qzApplnYwsqb.setId(ywsqbId);
					ywsqbService.update_page1ForLife(qzApplnYwsqb,request);
					returnMap.addGlobalMessage(CREATE_SUCCESS);
					returnMap.setSuccess(true);
				}catch (Exception e) {
					return WebRequestHelper.processException(e);
				}
			}else{
				returnMap.setSuccess(false);
				returnMap.addGlobalError(CustomerInforConstant.CREATEERROR);
			}
			return returnMap;
		}
	//page4_list
	@ResponseBody
	@RequestMapping(value = "page4_list.page")
	public AbstractModelAndView page4_list(HttpServletRequest request) {
		JRadModelAndView mv = new JRadModelAndView("/qzbankinterface/appIframeInfo/page4_list", request);
		String customerInforId = RequestHelper.getStringValue(request, ID);
		String appId = RequestHelper.getStringValue(request, "appId");
		String type = RequestHelper.getStringValue(request, "type");
		String operate = RequestHelper.getStringValue(request, "operate");
		
		List<QzApplnDbrxx> dbrxx_ls = null;
		if(appId != null && !appId.equals("")){
			dbrxx_ls = dbrxxService.findDbrxxByAppId(appId);
		}else{
			dbrxx_ls = dbrxxService.findDbrxx(customerInforId, null);
		}
		
		mv.addObject("dbrxx_ls", dbrxx_ls);
		if (StringUtils.isNotEmpty(customerInforId)) {
			CustomerInfor customerInfor = customerInforService.findCustomerInforById(customerInforId);
			mv.addObject("customerInfor", customerInfor);
			mv.addObject("customerId", customerInfor.getId());
			mv.addObject("type", type);
			mv.addObject("returnUrl",intoPiecesService.getReturnUrl(operate) );
		}
		return mv;
	}
	
	//page4
	@ResponseBody
	@RequestMapping(value = "page4.page")
	public AbstractModelAndView page4(HttpServletRequest request) {
		JRadModelAndView mv = new JRadModelAndView("/qzbankinterface/appIframeInfo/page4", request);
		String customerInforId = RequestHelper.getStringValue(request, "customerId");
		
		if (StringUtils.isNotEmpty(customerInforId)) {
			CustomerInfor customerInfor = customerInforService.findCustomerInforById(customerInforId);
			mv.addObject("customerInfor", customerInfor);
			mv.addObject("customerId", customerInfor.getId());
			
		}
		return mv;
	}
		
	//insert_page4
	@ResponseBody
	@RequestMapping(value = "insert_page4.json")
	public JRadReturnMap insert_page4(@ModelAttribute QzApplnDbrxxForm qzApplnDbrxxForm, HttpServletRequest request) {
		JRadReturnMap returnMap = new JRadReturnMap();
		if (returnMap.isSuccess()) {
			try {
				String customerId = request.getParameter("customerId");
				QzApplnDbrxx qzApplnDbrxx = qzApplnDbrxxForm.createModel(QzApplnDbrxx.class);
				User user = (User) Beans.get(LoginManager.class).getLoggedInUser(request);
				qzApplnDbrxx.setCreatedBy(user.getId());
				qzApplnDbrxx.setCreatedTime(new Date());
				//未填申请时 关联客户id
				qzApplnDbrxx.setCustomerId(customerId);
				dbrxxService.insert_page4(qzApplnDbrxx, request);
				returnMap.addGlobalMessage(CREATE_SUCCESS);
				returnMap.setSuccess(true);
			}catch (Exception e) {
				return WebRequestHelper.processException(e);
			}
		}else{
			returnMap.setSuccess(false);
			returnMap.addGlobalError(CustomerInforConstant.CREATEERROR);
		}
		return returnMap;
	}
		
	//update_page4
	@ResponseBody
	@RequestMapping(value = "update_page4.page")
	public AbstractModelAndView update_page4(HttpServletRequest request) {
		JRadModelAndView mv = new JRadModelAndView("/qzbankinterface/appIframeInfo/page4_change", request);
		String id = RequestHelper.getStringValue(request, ID);
		//查找page4信息
		QzApplnDbrxx qzApplnDbrxx = dbrxxService.findDbrxxById(id);
		List<QzApplnDbrxxDkjl> dkjl_ls = dbrxxService.findDbrxxDkjl(qzApplnDbrxx.getId());
		List<QzApplnDbrxxFc> fc_ls = dbrxxService.findDbrxxFc(qzApplnDbrxx.getId());
		List<QzApplnDbrxxJdc> jdc_ls = dbrxxService.findDbrxxJdc(qzApplnDbrxx.getId());
		mv.addObject("qzApplnDbrxx", qzApplnDbrxx);
		mv.addObject("dkjl_ls", dkjl_ls);
		mv.addObject("fc_ls", fc_ls);
		mv.addObject("jdc_ls", jdc_ls);
		return mv;
	}
		
	//update_page4
	@ResponseBody
	@RequestMapping(value = "update_page4.json")
	public JRadReturnMap update_page4(@ModelAttribute QzApplnDbrxxForm qzApplnDbrxxForm, HttpServletRequest request) {
		JRadReturnMap returnMap = new JRadReturnMap();
		if (returnMap.isSuccess()) {
			try {
				String dbrxxId = request.getParameter("id");
				QzApplnDbrxx qzApplnDbrxx = qzApplnDbrxxForm.createModel(QzApplnDbrxx.class);
				dbrxxService.dealWithNullValue(qzApplnDbrxx);
				User user = (User) Beans.get(LoginManager.class).getLoggedInUser(request);
				//未填申请时 关联客户id
				qzApplnDbrxx.setId(dbrxxId);
				dbrxxService.update_page4(qzApplnDbrxx, request);
				returnMap.addGlobalMessage(CREATE_SUCCESS);
				returnMap.setSuccess(true);
			}catch (Exception e) {
				return WebRequestHelper.processException(e);
			}
		}else{
			returnMap.setSuccess(false);
			returnMap.addGlobalError(CustomerInforConstant.CREATEERROR);
		}
		return returnMap;
	}
	
	//del_page4
	@ResponseBody
	@RequestMapping(value = "del_page4.json")
	public JRadReturnMap del_page4(HttpServletRequest request) {
		JRadReturnMap returnMap = new JRadReturnMap();
		if (returnMap.isSuccess()) {
			try {
				String dbrxxId = request.getParameter("id");
				dbrxxService.deleteDbrxx(dbrxxId);
				returnMap.addGlobalMessage(CREATE_SUCCESS);
				returnMap.setSuccess(true);
			}catch (Exception e) {
				return WebRequestHelper.processException(e);
			}
		}else{
			returnMap.setSuccess(false);
			returnMap.addGlobalError(CustomerInforConstant.CREATEERROR);
		}
		return returnMap;
	}
		
	//page5
	@ResponseBody
	@RequestMapping(value = "page5.page")
	public AbstractModelAndView page5(HttpServletRequest request) {
		JRadModelAndView mv = null;
		String customerInforId = RequestHelper.getStringValue(request, ID);
		String appId = RequestHelper.getStringValue(request, "appId");
		String type = RequestHelper.getStringValue(request, "type");
		String operate = RequestHelper.getStringValue(request, "operate");
		if(appId==null){
			appId="";
		}
		//查找page5信息
		QzApplnAttachmentList qzApplnAttachmentList = null;
		if(appId != null && !appId.equals("")){
			qzApplnAttachmentList = attachmentListService.findAttachmentListByAppId(appId);
		}
		else{
			qzApplnAttachmentList = attachmentListService.findAttachmentList(customerInforId, null);
		}
		
		if(qzApplnAttachmentList == null){
			mv = new JRadModelAndView("/qzbankinterface/appIframeInfo/page5", request);
			if (StringUtils.isNotEmpty(customerInforId)) {
				CustomerInfor customerInfor = customerInforService.findCustomerInforById(customerInforId);
				mv.addObject("customerInfor", customerInfor);
				mv.addObject("appId", appId);
				mv.addObject("type", type);
				mv.addObject("customerId", customerInfor.getId());
			}
			
			IUser user = Beans.get(LoginManager.class).getLoggedInUser(request);
			String externalId = user.getLogin();//工号
			mv.addObject("externalId",externalId);
			mv.addObject("userName",user.getDisplayName());
		}
		else{
			mv = new JRadModelAndView("/qzbankinterface/appIframeInfo/page5_change", request);
			mv.addObject("qzApplnAttachmentList", qzApplnAttachmentList);
			mv.addObject("type", type);
			mv.addObject("returnUrl",intoPiecesService.getReturnUrl(operate) );
		}
		
		//查找客户信息和经营信息
		CustomerInfor customerInfo = customerInforService.findCustomerInforById(customerInforId);
		mv.addObject("customerInfo", customerInfo);
		QzApplnJyxx qzApplnJyxx = jyxxService.findJyxx(customerInforId, null);
		mv.addObject("qzApplnJyxx", qzApplnJyxx);
		return mv;
	}
	
	//insert_page5
	@ResponseBody
	@RequestMapping(value = "insert_page5.json")
	public JRadReturnMap insert_page5(@ModelAttribute QzApplnAttachmentListForm qzApplnAttachmentListForm, HttpServletRequest request) {
		JRadReturnMap returnMap = new JRadReturnMap();
		if (returnMap.isSuccess()) {
			try {
				String customerId = request.getParameter("customerId");
				QzApplnAttachmentList qzApplnAttachmentList = qzApplnAttachmentListForm.createModel(QzApplnAttachmentList.class);
				User user = (User) Beans.get(LoginManager.class).getLoggedInUser(request);
				qzApplnAttachmentList.setCreatedBy(user.getId());
				qzApplnAttachmentList.setCreatedTime(new Date());
				qzApplnAttachmentList.setDocid(DateHelper.getDateFormat(qzApplnAttachmentList.getCreatedTime(), "yyyyMMddHHmmss"));
				qzApplnAttachmentList.setUploadValue("0");
				//未填申请时 关联客户id
				qzApplnAttachmentList.setCustomerId(customerId);
				attachmentListService.insert_page5(qzApplnAttachmentList, request);
				returnMap.addGlobalMessage(CREATE_SUCCESS);
				returnMap.setSuccess(true);
			}catch (Exception e) {
				return WebRequestHelper.processException(e);
			}
		}else{
			returnMap.setSuccess(false);
			returnMap.addGlobalError(CustomerInforConstant.CREATEERROR);
		}
		return returnMap;
	}
	
	//update_page5
	@ResponseBody
	@RequestMapping(value = "update_page5.json")
	public JRadReturnMap update_page5(@ModelAttribute QzApplnAttachmentListForm qzApplnAttachmentListForm, HttpServletRequest request) {
		JRadReturnMap returnMap = new JRadReturnMap();
		if (returnMap.isSuccess()) {
			try {
				String id = request.getParameter("id");
				String customerId = request.getParameter("customerId");
				QzApplnAttachmentList qzApplnAttachmentList = qzApplnAttachmentListForm.createModel(QzApplnAttachmentList.class);
				//未填申请时 关联客户id
				qzApplnAttachmentList.setId(id);
				qzApplnAttachmentList.setCustomerId(customerId);
				attachmentListService.update_page5(qzApplnAttachmentList, request);
				returnMap.addGlobalMessage(CREATE_SUCCESS);
				returnMap.setSuccess(true);
			}catch (Exception e) {
				return WebRequestHelper.processException(e);
			}
		}else{
			returnMap.setSuccess(false);
			returnMap.addGlobalError(CustomerInforConstant.CREATEERROR);
		}
		return returnMap;
	}
		
	//page7
	@ResponseBody
	@RequestMapping(value = "page7.page")
	public AbstractModelAndView page7(HttpServletRequest request) {
		JRadModelAndView mv = null;
		String customerInforId = RequestHelper.getStringValue(request, ID);
		String appId = RequestHelper.getStringValue(request, "appId");
		String type = RequestHelper.getStringValue(request, "type");
		String operate = RequestHelper.getStringValue(request, "operate");
		if(appId==null){
			appId="";
		}
		QzApplnNbscyjb qzApplnNbscyjb = null;
		if(appId != null && !appId.equals("")){
			qzApplnNbscyjb = nbscyjbService.findNbscyjbByAppId(appId);
		}else{
			qzApplnNbscyjb = nbscyjbService.findNbscyjb(customerInforId, null);
		}
		
		if(qzApplnNbscyjb == null){
			 mv = new JRadModelAndView("/qzbankinterface/appIframeInfo/page7", request);
			
			if (StringUtils.isNotEmpty(customerInforId)) {
				CustomerInfor customerInfor = customerInforService.findCustomerInforById(customerInforId);
				mv.addObject("customerInfor", customerInfor);
				mv.addObject("customerId", customerInfor.getId());
				mv.addObject("appId", appId);
				mv.addObject("type", type);
			}
		}
		else{
			mv = new JRadModelAndView("/qzbankinterface/appIframeInfo/page7_change", request);
			mv.addObject("qzApplnNbscyjb", qzApplnNbscyjb);
			mv.addObject("type", type);
			mv.addObject("returnUrl",intoPiecesService.getReturnUrl(operate) );
		}
		
		//查找客户信息和经营信息
		CustomerInfor customerInfo = customerInforService.findCustomerInforById(customerInforId);
		mv.addObject("customerInfo", customerInfo);
		QzApplnJyxx qzApplnJyxx = jyxxService.findJyxx(customerInforId, null);
		mv.addObject("qzApplnJyxx", qzApplnJyxx);
				
		return mv;
	}
	
	//insert_page7
	@ResponseBody
	@RequestMapping(value = "insert_page7.json")
	public JRadReturnMap insert_page5(@ModelAttribute QzApplnNbscyjbForm qzApplnNbscyjbForm, HttpServletRequest request) {
		JRadReturnMap returnMap = new JRadReturnMap();
		if (returnMap.isSuccess()) {
			try {
				String customerId = request.getParameter("customerId");
				QzApplnNbscyjb qzApplnNbscyjb = qzApplnNbscyjbForm.createModel(QzApplnNbscyjb.class);
				User user = (User) Beans.get(LoginManager.class).getLoggedInUser(request);
				qzApplnNbscyjb.setCreatedBy(user.getId());
				qzApplnNbscyjb.setCreatedTime(new Date());
				String shopName = qzApplnNbscyjb.getShopName();
				//未填申请时 关联客户id
				qzApplnNbscyjb.setCustomerId(customerId);
				nbscyjbService.insert_page7(qzApplnNbscyjb, request);
				returnMap.addGlobalMessage(CREATE_SUCCESS);
				returnMap.setSuccess(true);
			}catch (Exception e) {
				return WebRequestHelper.processException(e);
			}
		}else{
			returnMap.setSuccess(false);
			returnMap.addGlobalError(CustomerInforConstant.CREATEERROR);
		}
		return returnMap;
	}
		
	//update_page7
	@ResponseBody
	@RequestMapping(value = "update_page7.json")
	public JRadReturnMap update_page7(@ModelAttribute QzApplnNbscyjbForm qzApplnNbscyjbForm, HttpServletRequest request) {
		JRadReturnMap returnMap = new JRadReturnMap();
		if (returnMap.isSuccess()) {
			try {
				String id = request.getParameter("id");
				QzApplnNbscyjb qzApplnNbscyjb = qzApplnNbscyjbForm.createModel(QzApplnNbscyjb.class);
				User user = (User) Beans.get(LoginManager.class).getLoggedInUser(request);
				qzApplnNbscyjb.setId(id);
				String shopName = qzApplnNbscyjb.getShopName();
				nbscyjbService.update_page7(qzApplnNbscyjb, request);
				returnMap.addGlobalMessage(CREATE_SUCCESS);
				returnMap.setSuccess(true);
			}catch (Exception e) {
				return WebRequestHelper.processException(e);
			}
		}else{
			returnMap.setSuccess(false);
			returnMap.addGlobalError(CustomerInforConstant.CREATEERROR);
		}
		return returnMap;
	}
		
	//page8
	@ResponseBody
	@RequestMapping(value = "page8.page")
	public AbstractModelAndView page8(HttpServletRequest request) {
		JRadModelAndView mv = new JRadModelAndView("/qzbankinterface/appIframeInfo/page8", request);
		String customerId = RequestHelper.getStringValue(request, ID);
		
		String appId = RequestHelper.getStringValue(request, "appId");
		String type = RequestHelper.getStringValue(request, "type");
		String operate = RequestHelper.getStringValue(request, "operate");
		//作为审批后修改标志
		if(appId==null){
			appId="";
		}
		
		//获取内部审查意见表信息
		QzApplnNbscyjb qzApplnNbscyjb = null;
		if(appId != null && !appId.equals("")){
			qzApplnNbscyjb = nbscyjbService.findNbscyjbByAppId(appId);
		}else{
			qzApplnNbscyjb = nbscyjbService.findNbscyjb(customerId, null);
		}
		
		QzApplnJyd qzSdhjyd = new QzApplnJyd();
		List<QzApplnJydGtjkr> gtjkrs = new ArrayList<QzApplnJydGtjkr>();
		List<QzApplnJydBzdb> bzdbs = new ArrayList<QzApplnJydBzdb>();
		List<QzApplnJydDydb> dydbs = new ArrayList<QzApplnJydDydb>();
		if (StringUtils.isNotEmpty(appId)) {
			qzSdhjyd = intoPiecesService.getSdhjydFormAfter(appId);
		}else{
			qzSdhjyd = intoPiecesService.getSdhjydForm(customerId);
		}
		if(qzSdhjyd!=null){
			//获取共同借款人list
			gtjkrs = intoPiecesService.getJkrList(qzSdhjyd.getId());
			//获取保证担保list
			bzdbs = intoPiecesService.getBzdbList(qzSdhjyd.getId());
			//获取抵押担保list
			dydbs = intoPiecesService.getDydbList(qzSdhjyd.getId());
		}
		mv.addObject("customerId", customerId);
		mv.addObject("appId", appId);
		mv.addObject("type", type);
		mv.addObject("result", qzSdhjyd);
		mv.addObject("qzApplnNbscyjb", qzApplnNbscyjb);
		//查找开户信息 自动填充
		ECIF ecif = eCIFService.findEcifByCustomerId(customerId);
		mv.addObject("ecif", ecif);
		IUser user = Beans.get(LoginManager.class).getLoggedInUser(request);
		mv.addObject("orgName",user.getOrganization().getName());
		mv.addObject("userName",user.getDisplayName());
		mv.addObject("gtjkrs", gtjkrs);
		mv.addObject("bzdbs", bzdbs);
		mv.addObject("dydbs", dydbs);
		mv.addObject("returnUrl",intoPiecesService.getReturnUrl(operate) );
		mv.addObject("operate",operate);
		return mv;
	}
	
	/**
	 * 审贷会决议单保存(申请前)
	 * @param customerinfoForm
	 * @param request
	 * @return
	 */

	@ResponseBody
	@RequestMapping(value = "page8insert.json")
	public JRadReturnMap insert(@ModelAttribute QzSdhjydForm qzSdhjydForm, HttpServletRequest request) {
		JRadReturnMap returnMap = new JRadReturnMap();
		if (returnMap.isSuccess()) {
			try {
				String customerId = RequestHelper.getStringValue(request, ID);
				String appId = RequestHelper.getStringValue(request, "appId");
				QzApplnJyd qzSdhjyd = qzSdhjydForm.createModel(QzApplnJyd.class);
				qzSdhjyd.setCustomerId(customerId);
				qzSdhjyd.setCreatedTime(new Date());
				qzSdhjyd.setApplicationId(appId);
				if(StringUtils.isNotEmpty(appId)){
					intoPiecesService.insertSdhjydFormAfter(qzSdhjyd);
				}else{
					intoPiecesService.insertSdhjydForm(qzSdhjyd,request);
				}
				returnMap.addGlobalMessage(CREATE_SUCCESS);
			}catch (Exception e) {
				returnMap.put(JRadConstants.MESSAGE, DataPriConstants.SYS_EXCEPTION_MSG);
				returnMap.put(JRadConstants.SUCCESS, false);
				return WebRequestHelper.processException(e);
			}
		}else{
			returnMap.setSuccess(false);
			returnMap.addGlobalError(CustomerInforConstant.CREATEERROR);
		}
		return returnMap;
	}
	
	/**
	 * 进入补充上会记录页面
	 * 
	 * @param filter
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "add_information.page", method = { RequestMethod.GET })
	public AbstractModelAndView reject(@ModelAttribute CustomerApplicationProcessFilter filter, HttpServletRequest request) throws SQLException {
		filter.setRequest(request);
		IUser user = Beans.get(LoginManager.class).getLoggedInUser(request);
		String loginId = user.getId();
		filter.setLoginId(loginId);
		QueryResult<CustomerApplicationIntopieceWaitForm> result = customerApplicationIntopieceWaitService.shouxinAddInforForm(filter);
		JRadPagedQueryResult<CustomerApplicationIntopieceWaitForm> pagedResult = new JRadPagedQueryResult<CustomerApplicationIntopieceWaitForm>(filter, result);

		JRadModelAndView mv = new JRadModelAndView(
				"/intopieces/intopieces_wait/intopiecesApprove_shouxin_add_infor", request);
		mv.addObject(PAGED_RESULT, pagedResult);
		return mv;
	}
	
	/**
	 * 客户经理补充上会提交
	 * @param filter
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "pass.json")
	public JRadReturnMap pass(HttpServletRequest request) throws SQLException {
		JRadReturnMap returnMap = new JRadReturnMap();
		try {
			String customerId = request.getParameter("customerId");
			//更新客户信息状态
			CustomerInfor infor = commonDao.findObjectById(CustomerInfor.class, customerId);
			infor.setProcessId("");
			commonDao.updateObject(infor);
			returnMap.addGlobalMessage(CHANGE_SUCCESS);
		} catch (Exception e) {
			returnMap.addGlobalMessage("保存失败");
			e.printStackTrace();
		}
		return returnMap;
	}
	
	//iframe_approve(申请后)
	@ResponseBody
	@RequestMapping(value = "iframe_approve.page")
	public AbstractModelAndView iframeApprove(HttpServletRequest request) {
		JRadModelAndView mv = new JRadModelAndView("/qzbankinterface/appIframeInfo/iframe_approve", request);
		String customerInforId = RequestHelper.getStringValue(request, ID);
		String appId = RequestHelper.getStringValue(request, "appId");
		if (StringUtils.isNotEmpty(customerInforId)) {
			CustomerInfor customerInfor = customerInforService.findCustomerInforById(customerInforId);
			mv.addObject("customerInfor", customerInfor);
			mv.addObject("customerId", customerInfor.getId());
			mv.addObject("appId", appId);
			mv.addObject("operate", Constant.status_buchong);
		}
		return mv;
	}
	
	//影像
	@ResponseBody
	@RequestMapping(value = "sunds_ocx.page")
	public AbstractModelAndView sunds_ocx(HttpServletRequest request) {
		JRadModelAndView mv = new JRadModelAndView("/qzbankinterface/appIframeInfo/sunds_ocx", request);
		String appId = RequestHelper.getStringValue(request, "appId");
		mv.addObject("appId", appId);
		//查找page5信息
		JSONObject jsonStr = JSONObject.fromObject(attachmentListService.findAttachmentListJsonByAppId(appId));
		mv.addObject("children", jsonStr);
		return mv;
	}
	
	@ResponseBody
	@RequestMapping(value = "sunds_ocx_display.page")
	public AbstractModelAndView sunds_ocx_display(HttpServletRequest request) {
		JRadModelAndView mv = new JRadModelAndView("/qzbankinterface/appIframeInfo/sunds_ocx_display", request);
		String appId = RequestHelper.getStringValue(request, "appId");
		mv.addObject("appId", appId);
		//查找page5信息
		JSONObject jsonStr = JSONObject.fromObject(attachmentListService.findAttachmentListJsonByAppId(appId));
		mv.addObject("children", jsonStr);
		return mv;
	}
	
	//新增/修改影像
	@ResponseBody
	@RequestMapping(value = "getPage5UploadValue.json")
	public JRadReturnMap getPage5UploadValue(HttpServletRequest request) throws SQLException {
		JRadReturnMap returnMap = new JRadReturnMap();
		try {
			String appId = request.getParameter("appId");
			String docID = request.getParameter("docID");
			
			//更新客户信息状态
			QzApplnAttachmentList attachmentList = attachmentListService.findAttachmentListByAppId(appId);
			String uploadValue = attachmentList.getUploadValue();
			attachmentList.setUploadValue(Integer.parseInt(uploadValue)+Integer.parseInt(docID.substring(14, docID.length()))+"");
			// TODO 逻辑设计思路？现在不满足要求？？？
			returnMap.put("uploadFlag", Integer.parseInt(uploadValue)&Integer.parseInt(docID.substring(14, docID.length())));
			returnMap.addGlobalMessage(CHANGE_SUCCESS);
			returnMap.put(JRadConstants.SUCCESS, true);
		} catch (Exception e) {
			returnMap.addGlobalMessage("保存失败");
			returnMap.put(JRadConstants.SUCCESS, false);
			e.printStackTrace();
		}
		return returnMap;
	}
		
	//新增/修改影像
	@ResponseBody
	@RequestMapping(value = "WDScan.page")
	public AbstractModelAndView WDScan(HttpServletRequest request) {
		JRadModelAndView mv = new JRadModelAndView("/qzbankinterface/appIframeInfo/WDScan", request);
		String appId = RequestHelper.getStringValue(request, "appId");
		mv.addObject("appId", appId);
		//查找page5信息
		JSONObject jsonStr = JSONObject.fromObject(attachmentListService.findAttachmentListJsonByAppId(appId));
		mv.addObject("children", jsonStr);
		return mv;
	}
	
	//新增/修改影像
	@ResponseBody
	@RequestMapping(value = "WDView.page")
	public AbstractModelAndView WDView(HttpServletRequest request) {
		JRadModelAndView mv = new JRadModelAndView("/qzbankinterface/appIframeInfo/WDView", request);
		String appId = RequestHelper.getStringValue(request, "appId");
		mv.addObject("appId", appId);
		//查找page5信息
		JSONObject jsonStr = JSONObject.fromObject(attachmentListService.findAttachmentListJsonByAppId(appId));
		mv.addObject("children", jsonStr);
		return mv;
	}
	
	//新增/修改影像
		@ResponseBody
		@RequestMapping(value = "WDModify.page")
		public AbstractModelAndView WDModify(HttpServletRequest request) {
			JRadModelAndView mv = new JRadModelAndView("/qzbankinterface/appIframeInfo/WDModify", request);
			String appId = RequestHelper.getStringValue(request, "appId");
			mv.addObject("appId", appId);
			//查找page5信息
			JSONObject jsonStr = JSONObject.fromObject(attachmentListService.findAttachmentListJsonByAppId(appId));
			mv.addObject("children", jsonStr);
			return mv;
		}
	
	//新增/修改影像
	@ResponseBody
	@RequestMapping(value = "insert_sunds.json")
	public JRadReturnMap insert_sunds(HttpServletRequest request) throws SQLException {
		JRadReturnMap returnMap = new JRadReturnMap();
		try {
			String appId = request.getParameter("appId");
			String docID = request.getParameter("docID");
			
			//更新客户信息状态
			QzApplnAttachmentList attachmentList = attachmentListService.findAttachmentListByAppId(appId);
			String uploadValue = attachmentList.getUploadValue();
			attachmentList.setUploadValue(Integer.parseInt(uploadValue)+Integer.parseInt(docID.substring(14, docID.length()))+"");
			commonDao.updateObject(attachmentList);
			returnMap.addGlobalMessage(CHANGE_SUCCESS);
			returnMap.put(JRadConstants.SUCCESS, true);
		} catch (Exception e) {
			returnMap.addGlobalMessage("保存失败");
			returnMap.put(JRadConstants.SUCCESS, false);
			e.printStackTrace();
		}
		return returnMap;
	}
	
	//客户号检测
	@ResponseBody
	@RequestMapping(value = "detect_clientNo.json")
	public JRadReturnMap detect_clientNo(@ModelAttribute IESBForCircleForm iesbForCircleForm, HttpServletRequest request){
		JRadReturnMap returnMap = new JRadReturnMap();
		try{
			String circleId = RequestHelper.getStringValue(request, "id");
			String customerId = RequestHelper.getStringValue(request, "customerId");
			String clientNo = iesbForCircleForm.getClientNo();
			String clientType = iesbForCircleForm.getClientType();
			String globalId = iesbForCircleForm.getGlobalId();
			String globalType = iesbForCircleForm.getGlobalType();
			
			logger.info("circleId=" + circleId + "customerId= " +customerId+ "clientType= " +clientType
					+ "globalId=" +globalId+ "globalType=" + globalType);
			Map ECIFResp = eCIFService.detectECIF(globalId, clientType,globalType);
			
			String retCode = (String)ECIFResp.get("RET_CODE");
			if("000000".equals(retCode)){
				String clientName = (String)ECIFResp.get("CLIENT_NAME");
				String golbalType = (String)ECIFResp.get("GLOBAL_TYPE");
				String golbalId = (String)ECIFResp.get("GLOBAL_ID");
				String resClientNo = (String)ECIFResp.get("CLIENT_NO");
				
				//把获取的客户号存在库中
				Circle circle = commonDao.findObjectById(Circle.class, circleId);
				if(circle != null){
					circle.setaClientNo(resClientNo);
					circleService.updateCustomerInforCircle(circle);
				}
				
				if(clientName.equals(iesbForCircleForm.getClientName()) && golbalType.equals(iesbForCircleForm.getGlobalType())
						&& golbalId.equals(iesbForCircleForm.getGlobalId())){
					returnMap.put(JRadConstants.SUCCESS, true);
					returnMap.addGlobalMessage("获取成功");
					returnMap.put("resClientNo", resClientNo);
					return returnMap;
				}
				returnMap.put(JRadConstants.SUCCESS, false);
				returnMap.put("resClientNo", resClientNo);
				returnMap.put("message", "请检查客户身份信息和名称是否一致~!");
				return returnMap;
			}
			returnMap.put("message", "客户信息不存在 ~！");
			returnMap.put(JRadConstants.SUCCESS, false);
		}catch(Exception e){
			returnMap.put(JRadConstants.SUCCESS, false);
			returnMap.put("message", "检测失败");
			e.printStackTrace();
		}
		return returnMap;
	}
}
