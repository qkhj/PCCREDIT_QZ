package com.cardpay.workflow.service;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cardpay.pccredit.intopieces.model.CustomerApplicationProcess;
import com.cardpay.workflow.constant.ApproveOperationTypeEnum;
import com.cardpay.workflow.dao.WfStatusInfoDao;
import com.cardpay.workflow.dao.WfStatusQueueRecordDao;
import com.cardpay.workflow.dao.WfStatusResultDao;
import com.cardpay.workflow.models.WfProcessRecord;
import com.cardpay.workflow.models.WfStatusInfo;
import com.cardpay.workflow.models.WfStatusQueueRecord;
import com.cardpay.workflow.models.WfStatusResult;
import com.wicresoft.jrad.base.database.dao.common.CommonDao;

/**   
 * @Title: ProcessService.java 
 * @Description: TODO(用一句话描述该文件做什么) 
 * @author 谭文华   
 * @date 2014-11-25 下午3:24:59
*/
@Service
public class ProcessService {
	//单例获得数据库操作类
	@Autowired
	private CommonDao commonDao;
	
	//实例化WfStatusResultDao
	@Autowired
	private WfStatusResultDao wfStatusResultDao;
	
	//实例化WfStatusResultDao
	@Autowired
	private WfStatusInfoDao wfStatusInfoDao;
	
	//实例化WfStatusResultDao
	@Autowired
	private WfStatusQueueRecordDao wfStatusQueueRecordDao;
	
	/**
	 * 流程开始,根据状态流转表取得该下一状态信息，同时流程记录表中新增一条记录
	 * @param wfProcessInfoID
	 * @return WfProcessRecord的ID
	 * @throws SQLException
	 */
	public String start(String wfProcessInfoID){
		
		//新建流程记录表的第一条记录
		WfProcessRecord wfProcessRecord = new WfProcessRecord();
		wfProcessRecord.setCreateDate(new Timestamp(System.currentTimeMillis()));
		wfProcessRecord.setIsClosed("0");
		wfProcessRecord.setWfProcessInfo(wfProcessInfoID);
		commonDao.insertObject(wfProcessRecord);
		
		//根据流程记录ID查找WfStatusInfo表 找到该流程的起始状态
		WfStatusInfo wfStatusInfo = wfStatusInfoDao.getStartStatus(wfProcessInfoID);
		
		//根据上一步查找到的起始状态 新建流转表的第一条记录
		WfStatusQueueRecord wfStatusQueueRecord = new WfStatusQueueRecord();
		wfStatusQueueRecord.setCurrentStatus(wfStatusInfo.getId());
		//将流转表关联到流程表
		wfStatusQueueRecord.setCurrentProcess(wfProcessRecord.getId());
		commonDao.insertObject(wfStatusQueueRecord);
		
		//将流程表关联到流转表
		wfProcessRecord.setWfStatusQueueRecord(wfStatusQueueRecord.getId());
		commonDao.updateObject(wfProcessRecord);
		
		//返回流程表ID
		return wfProcessRecord.getId();
	}
	
	/**
	 * 流程关闭。暂不考虑子流程等。
	 * @param wfProcessRecordID
	 * @return 影响记录条数
	 * @throws SQLException
	 */
	public int close(String wfProcessRecordID) {
		WfProcessRecord wfProcessRecord = commonDao.findObjectById(WfProcessRecord.class, wfProcessRecordID);
		wfProcessRecord.setIsClosed("1");
		return commonDao.updateObject(wfProcessRecord);
	}

	/**
	 * 审批,返回下一状态信息
	 * @param wfProcessRecordID
	 * @param exUserID
	 * @param exResult  必须是ApproveOperationTypeEnum中的类型
	 * @param exAmount
	 * @return 
	 * 	下一流程状态ID; 
	 * 	ApproveOperationTypeEnum.NORMALEND时为流程正常结束;
	 * 	ApproveOperationTypeEnum.RETURNAPPROVE时为退回;
	 * 	ApproveOperationTypeEnum.REJECTAPPROVE时为拒绝;
	 * 
	 * @throws SQLException
	 */
	public String examine(String wfProcessRecordID,String exUserID,String exResult,String exAmount){
		//查找当前所处流转状态
		WfProcessRecord wfProcessRecord = commonDao.findObjectById(WfProcessRecord.class, wfProcessRecordID);
		WfStatusQueueRecord wfStatusQueueRecord = commonDao.findObjectById(WfStatusQueueRecord.class,wfProcessRecord.getWfStatusQueueRecord());
		
		//进入下一流转之前 先更新当前流转
		wfStatusQueueRecord.setExamineUser(exUserID);
		wfStatusQueueRecord.setExamineResult(exResult);
		wfStatusQueueRecord.setExamineAmount(exAmount);
		wfStatusQueueRecord.setStartExamineTime(new Date());
		commonDao.updateObject(wfStatusQueueRecord);
		
		//退回
		if(exResult.equalsIgnoreCase(ApproveOperationTypeEnum.RETURNAPPROVE.toString())){
			wfProcessRecord.setIsClosed("1");
			commonDao.updateObject(wfProcessRecord);
			return ApproveOperationTypeEnum.RETURNAPPROVE.toString();
		} //拒绝 
		else if(exResult.equalsIgnoreCase(ApproveOperationTypeEnum.REJECTAPPROVE.toString())){
			wfProcessRecord.setIsClosed("1");
			commonDao.updateObject(wfProcessRecord);
			return ApproveOperationTypeEnum.REJECTAPPROVE.toString();
		} //通过 
		else {
			//根据当前审批结果 查找审批结果表 获取下一个审批状态
			WfStatusResult wfStatusResult = wfStatusResultDao.getNextStatus(wfStatusQueueRecord.getCurrentStatus(), exUserID, exResult);
			
			//判断下一个审批批状态是否为结束状态
			WfStatusInfo wfStatusInfo = commonDao.findObjectById(WfStatusInfo.class, wfStatusResult.getNextStatus());
			if(wfStatusInfo.getIsClosed().equals("1")){//标示下一状态为结束
				//将流程记录表标识为结束
				wfProcessRecord.setIsClosed("1");
				commonDao.updateObject(wfProcessRecord);
				return ApproveOperationTypeEnum.NORMALEND.toString();
			}
			
			//流转表新增一条记录
			String beforeStatus = wfStatusQueueRecord.getCurrentStatus();
			wfStatusQueueRecord = new WfStatusQueueRecord();
			wfStatusQueueRecord.setBeforeStatus(beforeStatus);
			wfStatusQueueRecord.setCurrentProcess(wfProcessRecordID);
			wfStatusQueueRecord.setCurrentStatus(wfStatusResult.getNextStatus());
			commonDao.insertObject(wfStatusQueueRecord);
			
			//流程表关联到新的流转
			wfProcessRecord.setWfStatusQueueRecord(wfStatusQueueRecord.getId());
			commonDao.updateObject(wfProcessRecord);
			
			String wfStatusInfoId = wfStatusResult.getNextStatus();
			WfStatusInfo nextStatusInfo = commonDao.findObjectById(WfStatusInfo.class, wfStatusInfoId);
			//返回节点的id
			return nextStatusInfo.getStatusCode();
		}
	}
	
	/**
	 * 获得某用户的审批历史
	 * @param exUserID
	 * @return
	 * @throws SQLException
	 */
	public List<WfStatusQueueRecord> getExamineHistory(String exUserID) {
		return wfStatusQueueRecordDao.getExamineHistory(exUserID);
	}
	/**
	 * 根据application_id查询customer_application_process信息
	 * 
	 */
	public CustomerApplicationProcess findProcessByAppId(String appId){
		String sql="select * from customer_application_process p where p.application_id='"+appId+"'";
		List<CustomerApplicationProcess> pl = commonDao.queryBySql(CustomerApplicationProcess.class,sql, null);
		if(pl.size()>0){
			return pl.get(0);
		}
		return null;
	}
}
