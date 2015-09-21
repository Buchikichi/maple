package to.kit.mapper.statement.impl;

import to.kit.mapper.io.MapperTokenizer.LineInfo;
import to.kit.mapper.statement.ProgramStatement;

/**
 * "."(NOP).
 * @author Hidetaka Sasai
 */
public final class Nop extends ProgramStatement {
	/**
	 * インスタンスを生成.
	 * @param line Line
	 */
	public Nop(final LineInfo line) {
		super(line);
	}
}
