/*
 * IdeaJdbc is a JDBC Data Access Framework. It depends on IdeaData Framework.
 *
 * copyright © www.ideamoment.com
 */
package com.ideamoment.ideajdbc.actionparser;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import com.ideamoment.ideadata.annotation.DataItemType;
import com.ideamoment.ideajdbc.action.Parameter;
import com.ideamoment.ideajdbc.exception.IdeaJdbcException;
import com.ideamoment.ideajdbc.exception.IdeaJdbcExceptionCode;

/**
 * @author Chinakite
 *
 */
public class AbstractActionParser {
	/**
	 * 解析SQL语句，构建要传给PreparedStatement的参数列表。
	 * 
	 * @param sql SQL语句
	 * @param parameters SqlQueryAction收集到的参数列表
	 * @return 参数数组
	 */
	protected JdbcSql buildJdbcSql(String sql, Map<Object, Parameter> parameters) {
		Parameter[] params = new Parameter[parameters.size()];
		Stack opStack = new Stack();
		StringBuffer paramNameBuffer = new StringBuffer();
		int t = 0;		//params的总计数器
		int c = 0;		//问号的计数器
		int lastColon = 0;		//上一次冒号的位置，用于将参数名称替换为问号
		for(int i=0; i<sql.length(); i++) {
			char ch = sql.charAt(i);
			if(opStack.empty()) {	//如果栈是空的
				if(ch == '\'') {	//遇到单引号则将单引号入栈
					opStack.push(ch);
				}else{		//如果不是单引号
					if(ch == '?') {		//如果是问号，则认为是参数占位符，需要进行处理;
						if(paramNameBuffer.length() > 0) {		//如果?处于一个参数名称中，则抛出异常。
							throw new IdeaJdbcException(IdeaJdbcExceptionCode.SQL_PARAMETER_PARSER_ERR, "The SQL is wrong with parameters.");
						}
						//获取参数放入参数列表的正确位置
						params[t] = parameters.get(c);
						t++;
						c++;
					}else if(ch == ':'){		//如果是冒号，开始识别参数名
						paramNameBuffer.append(ch);
						lastColon = i;
					}else if(ch == ' ' || ch == ')' || ch == ','){		//如果遇到空格，则需判断参数名缓冲中是否有内容，有内容则标志着成功识别一个参数名称，
						                        //需获取参数放入参数列表的正确位置，并将名称替换为问号
						if(paramNameBuffer.length() > 0) {
							String paramName = paramNameBuffer.deleteCharAt(0).toString();
							//获取参数放入参数列表的正确位置
							params[t] = parameters.get(paramName);
							paramNameBuffer = new StringBuffer();
							//将sql语句中的参数名称替换为问号
							sql = sql.substring(0, lastColon) + "?" + sql.substring(i);
							
							t++;		//将参数数组下标加1
							i = i - paramName.length() + 1;		//由于替换了参数名称，因此需要将循环的指针置于正确的位置
						}
					}else{		//不是?、:和空格
						//如果参数名缓冲中有内容，则认为是处于解析一个参数名的状态
						if(paramNameBuffer.length() > 0) {
							paramNameBuffer.append(ch);
						}
					}
				}
			}else{		//如果栈不是空的
				if(ch == '\'') {	//遇到单引号先判断单引号前是不是转义字符
					if(sql.charAt(i - 1) == '\\'){	//如果是转义字符，则入栈
						opStack.push(ch);
					}else{		//如果不是转义字符，则将栈清空，一个完整字符串解析结束
						opStack.clear();
					}
				}else{	//如果不是单引号，直接入栈
					opStack.push(ch);
				}
			}//end if opStack.empty();
		}
		
		/*
		 * 如果参数名后SQL语句就结束了，这时前面写的处理空格的逻辑不会被触发，因此要补救一下
		 */
		if(paramNameBuffer.length() > 0) {
			params[t] = parameters.get(paramNameBuffer.deleteCharAt(0).toString());
			sql = sql.substring(0, lastColon) + "?";
		}
		
		List<JdbcSqlParam> sqlParams = new ArrayList<JdbcSqlParam>(params.length);
		int i = 0;
		for(Parameter p : params) {
			JdbcSqlParam jdbcSqlParam = new JdbcSqlParam();
			jdbcSqlParam.setType(getParamType(p));
			jdbcSqlParam.setParamValue(p.getValue());
			sqlParams.add(jdbcSqlParam);
			i++;
		}
		
		return new JdbcSql(sql, sqlParams);
	}
	
	private DataItemType getParamType(Parameter p) {
		Object type = p.getType();
		if(type instanceof DataItemType) {
			return (DataItemType)type;
		}else{
			//基本类型：int boolean long float double char byte short
			//复杂类型: String Date BigDecimal
			if(type.equals(String.class)) {
				return DataItemType.STRING;
			}else if(type.equals(Integer.class) || type.equals(Integer.TYPE)) {
				return DataItemType.INTEGER;
			}else if(type.equals(Short.class) || type.equals(Short.TYPE)) {
				return DataItemType.INTEGER;
			}else if(type.equals(Long.class) || type.equals(Long.TYPE)) {
				return DataItemType.LONG;
			}else if(type.equals(Float.class) || type.equals(Float.TYPE)) {
				return DataItemType.FLOAT;
			}else if(type.equals(Double.class) || type.equals(Double.TYPE)) {
				return DataItemType.DOUBLE;
			}else if(type.equals(BigDecimal.class)) {
				return DataItemType.DECIMAL;
			}else if(type.equals(Boolean.class) || type.equals(Boolean.TYPE)) {
				return DataItemType.BOOLEAN;
			}else if(type.equals(Byte.class) || type.equals(Byte.TYPE)) {
				return DataItemType.BYTE;
			}else if(type.equals(Character.class) || type.equals(Character.TYPE)) {
				return DataItemType.CHAR;
			}else if(type.equals(Date.class) || type.equals(java.sql.Timestamp.class)) {
				return DataItemType.DATETIME;
			}else if(type.equals(java.sql.Date.class)) {
				return DataItemType.DATE;
			}else if(type.equals(java.sql.Time.class)) {
				return DataItemType.TIME;
			}else{
				return DataItemType.UNKNOWN;
			}
		}
	}
}
