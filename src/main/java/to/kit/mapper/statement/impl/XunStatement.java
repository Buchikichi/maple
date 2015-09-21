package to.kit.mapper.statement.impl;

import to.kit.mapper.io.MapperTokenizer.LineInfo;
import to.kit.mapper.statement.ProgramStatement;

/**
 * Exit MAPPER.
 * @author Hidetaka Sasai
 */
public final class XunStatement extends ProgramStatement {
	/**
	 * インスタンスを生成.
	 * @param line Line
	 */
	public XunStatement(final LineInfo line) {
		super(line);
	}
}
