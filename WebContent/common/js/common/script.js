function Open(obj){//展开或隐藏box	
	var className=$(obj).parent().find("div[name=content]").attr("class");
	if(className=="display"){//隐藏时
		$(obj).parent().find("div[name=content]").attr("class","");
		$(obj).find("img").attr("src",contextPath+"/images/min.gif");
	}
	else{//展开时
		$(obj).parent().find("div[name=content]").attr("class","display");
		$(obj).find("img").attr("src",contextPath+"/images/max.gif");
	}
}
function Open2(obj){//展开或隐藏box	
	var className=$(obj).parent().find("div[name=content]").attr("class");
	if(className=="display information"){//隐藏时
		$(obj).parent().find("div[name=content]").attr("class","information");
		$(obj).find("img").attr("src",contextPath+"/images/min.gif");
	}
	else{//展开时
		$(obj).parent().find("div[name=content]").attr("class","display information");
		$(obj).find("img").attr("src",contextPath+"/images/max.gif");
	}
}
function selectTag(id,obj){//tab页
	$(".tags li").attr("class","");
	$(obj).attr("class","active");
	$(".Content div").hide();
	$("#"+id).show();
}
function addTd(table){	//表格添加行
	var tr= document.getElementById(table).getElementsByTagName("tr");
	//更新行数
	var cnt = tr.length-2+1;
	$("#"+table+"_cnt").val(cnt);
	
	if(table=="zygys"){//主要供应商
		$("#"+table).append("<tr>" + 
								"<td><input type='text' name='name_zygys_"+cnt+"'/></td>" + 
								"<td><input type='text' name='rate_zygys_"+cnt+"'/></td>" + 
								"<td><input type='text' name='condition_zygys_"+cnt+"'/></td>" + 
							"</tr>");
	}
	if(table=="zykh"){//主要客户
		$("#"+table).append("<tr>" + 
								"<td><input type='text' name='name_zykh_"+cnt+"'/></td>" + 
								"<td><input type='text' name='rate_zykh_"+cnt+"'/></td>" + 
								"<td><input type='text' name='condition_zykh_"+cnt+"'/></td>" + 
							"</tr>");
	}
	if(table=="jkjl"){//借款记录
		$("#"+table).append("<tr>" + 
								"<td><input type='text' class='input' name='bankOrOtherType_jkjl_"+cnt+"'/></td>" + 
								"<td><input type='text' class='input' name='purpose_jkjl_"+cnt+"'/></td>" + 
								"<td><input type='text' class='input' name='totalAmount_jkjl_"+cnt+"'/></td>" + 
								"<td><input type='text' class='input' name='loanDate_jkjl_"+cnt+"' value='"+timestr+"' onmouseover='_calendar()' readonly/></td>" + 
								"<td><input type='text' class='input' name='deadline_jkjl_"+cnt+"'/></td>" + 
								"<td><input type='text' class='input' name='rates_jkjl_"+cnt+"'/></td>" + 
								"<td><input type='text' class='input' name='repayType_jkjl_"+cnt+"'/></td>" + 
								"<td><input type='text' class='input' name='remainSum_jkjl_"+cnt+"'/></td>" + 
							"</tr>"); 
	}
	if(table=="dkjl"){//贷款记录
		$("#"+table).append("<tr>" + 
								"<td><input type='text' class='input' name='loanType_"+cnt+"' /></td>	" +
								"<td><input type='text' class='input' name='amount_"+cnt+"' /></td>" +					
								"<td><input type='text' class='input' name='deadline_"+cnt+"' /></td>" +	
								"<td><input type='text' class='input' name='loanDate_"+cnt+"' value='"+timestr+"' onmouseover='_calendar()' readonly/></td>	" +
								"<td><input type='text' class='input' name='purpose_"+cnt+"' /></td>" +					
								"<td><input type='text' class='input' name='remainAmount_"+cnt+"' /></td>" +	
								"<td><input type='text' class='input' name='guntForOther_"+cnt+"' /></td>" +	
								"<td><input type='text' class='input' name='guntAmount_"+cnt+"' /></td>" +	
								"<td><input type='text' class='input' name='guntDeadline_"+cnt+"' /></td>" +
							"</tr>");      
	}
	if(table=="fc"){//房产
		$("#"+table).append("<tr>" + 
								"<td><input type='text' class='input' name='addr_"+cnt+"' /></td>" + 	
								"<td><input type='text' class='input' name='price_"+cnt+"' /></td>	" + 				
								"<td>" + 
									"<select name='usrSituation_"+cnt+"'>" + 
										"<option value='1'>自住</option>" + 
										"<option value='2'>出租</option>" + 
										"<option value='3'>商业</option>" + 
										"<option value='4'>空置</option>" + 
									"</select>" + 
								"</td>	" + 
								"<td>" + 
									"<select name='haveCopy_"+cnt+"'>" + 
										"<option value='1'>有</option>" + 
										"<option value='0'>无</option>" + 
									"</select>" + 
								"</td>	" + 	
							"</tr>");      
	}
	if(table=="jdc"){//机动车
		$("#"+table).append("<tr>" + 
								"<td><input type='text' class='input' name='cardNo_"+cnt+"' /></td>	" +
								"<td><input type='text' class='input' name='price_"+cnt+"' /></td>	" +
								"<td><input type='text' class='input' value='"+timestr+"' name='buyDate_"+cnt+"' onmouseover='_calendar()' readonly/></td>	" +			
								"<td>" +
									"<select name='haveCopy_1_"+cnt+"'>" +
										"<option value='1'>有</option>" +
										"<option value='0'>无</option>" +
									"</select>" +
								"</td>	" +
								"<td>" +
									"<select name='haveCopy_2_"+cnt+"'>" +
										"<option value='1'>有</option>" +
										"<option value='0'>无</option>" +
									"</select>" +
								"</td>	" +	
							"</tr>");      
	}
	if(table=="htqdtz"){//合同签订台账
		$("#"+table).append("<tr>" + 
								"<td><input type='text' class='input' name='slrq' onmouseover='_calendar()' readonly/></td>" + 
								"<td><input type='text' class='input' name='jkrxm'/></td>" + 
								"<td><input type='text' class='input' name='pzje'/></td>" + 
								"<td><input type='text' class='input' name='yyqdrq' onmouseover='_calendar()' readonly/></td>" + 
								"<td><input type='text' class='input' name='sjqdrq' onmouseover='_calendar()' readonly/></td>" + 
								"<td><input type='text' class='input' name='zbkhjl'/></td>" + 
								"<td><input type='text' class='input' name='jbr'/></td>" + 
								"<td><input type='text' class='input' name='bz'/></td>" + 
							"</tr>");      
	}
	if(table=="gtjkr"){//共同借款人
		$("#"+table).append("<tr>" + 
								"<td><input type='text' name='gtjkrxm'/></td>" +  
								"<td><input type='text' name='gtjkrhm'/></td>" +  
							"</tr>");      
	}
	if(table=="bzdb"){//保证担保
		$("#"+table).append("<tr>" + 
								"<td><input type='text' name='bzdbxm'/></td>" +  
								"<td><input type='text' name='bzdbhm'/></td>" +  
							"</tr>");      
	}
	if(table=="dydb"){//抵押担保
		$("#"+table).append("<tr>" + 
								"<td><input type='text' name='dyr'/></td>" +  
								"<td><input type='text' name='dywmc'/></td>" +  
								"<td><input type='text' name='sl'/></td>" +  
								"<td><input type='text' name='djhm'/></td>" +  
								"<td><input type='text' name='kdyjz'/></td>" +  
							"</tr>");      
	}
	
}

function removeTd(table){ //表格删除行 
	var tr= document.getElementById(table).getElementsByTagName("tr");
	if(tr.length>2){
		document.getElementById(table).deleteRow(tr.length-1);//删除最后一行	
		$("#"+table+"_cnt").val(tr.length-2);
	}
}