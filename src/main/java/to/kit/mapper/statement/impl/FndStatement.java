package to.kit.mapper.statement.impl;

import to.kit.mapper.statement.ProgramStatement;

/**
 * Find.<br/>
 * 
 * @author Hidetaka Sasai
 */
public class FndStatement extends ProgramStatement {
	/**
	 * インスタンスを生成.
	 * @param params パラメーター
	 */
	public FndStatement(String... params) {
		// @FND,c,d[,r,l,lab] o cc ltyp,p [vrpt,vlno] .
		// cc 検索位置
		// p  検索文字
	}
}
