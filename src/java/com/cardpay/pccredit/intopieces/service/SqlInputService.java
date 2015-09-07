package com.cardpay.pccredit.intopieces.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.log4j.Logger;
import org.jfree.util.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wicresoft.jrad.base.database.dao.common.CommonDao;

@Service
public class SqlInputService {
	Logger logger  = Logger.getLogger(SqlInputService.class);
	@Autowired
	private CommonDao commonDao;
	
	/* 
	/*
	 * 执行sql语句
	 */
	public boolean changeDatabase(String sql){
		boolean flag = false;
		Connection conn = commonDao.getSqlSession().getConnection();
		Statement st = null;
		
		if(conn==null){
			logger.error("获取数据库连接失败 ！");
			return false;
		}
		try {
			st = conn.createStatement();
			st.execute(sql);
			int num = st.getUpdateCount();
			flag = true;
			logger.info("更新记录数 ： "+num);
		} catch (SQLException e) {
			
			logger.error(e.getMessage());
		}finally {
			try{
				if(st!= null ){
					st.close();
				}
				
			}catch(SQLException e1){
				Log.error(e1.getMessage());
			}
		}
		return flag;
	}
}
