package to.kit.mapper.statement.impl;

import to.kit.mapper.io.MapperTokenizer.LineInfo;
import to.kit.mapper.statement.ProgramStatement;

/**
 * LZR.(Line Zero)
 * @author sasai
 */
public class LzrStatement extends ProgramStatement {
	private String[] cdr;
	private String vlines;

	/**
	 * インスタンスを生成.
	 * @param line Line
	 */
	public LzrStatement(final LineInfo line) {
		// @LZR,c,d,r[,lab vlines,vcpl,vhdgs,,,vdept,vuser,vrpw,vwpw,vlgn,vrtyp] .
		// vlines ... 行数
		super(line);

		this.cdr = getFirstElements();
		this.vlines = line.get(1).split(",")[0];
	}
}
