package com.cardpay.pccredit.manager.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.cardpay.pccredit.manager.filter.AccountManagerParameterFilter;
import com.cardpay.pccredit.manager.model.AccountManagerParameter;
import com.cardpay.pccredit.manager.web.AccountManagerParameterForm;
import com.wicresoft.util.annotation.Mapper;


/**
 * AccountManagerParameterDao类的描述
 *
 * @author 王海东
 * @created on 2014-11-7
 * 
 * @version $Id:$
 */
@Mapper
public interface AccountManagerParameterDao {

	/*
	 * 根据filter查询所有客户经理信息
	 */
	public List<AccountManagerParameterForm> findAccountManagerParametersByFilter(AccountManagerParameterFilter filter);
	
	/*
	 * 查询所有客户经理信息
	 */
	public List<AccountManagerParameterForm> findAccountManagerParametersAll();
	
	/*
	 * 根据filter查询所有客户经理信息数量
	 */
	public int findAccountManagerParametersCountByFilter(AccountManagerParameterFilter filter);

	public AccountManagerParameterForm findAccountManagerParameterByUserId(@Param("userId") String userId);
	
	public String findRoleNameByUserId(@Param("userId") String userId);
}
