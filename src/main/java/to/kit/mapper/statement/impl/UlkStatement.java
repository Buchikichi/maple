package to.kit.mapper.statement.impl;

import to.kit.mapper.io.MapperTokenizer.LineInfo;
import to.kit.mapper.statement.ProgramStatement;

/**
 * ULK(Unlock).
 * @author Hidetaka Sasai
 */
public class UlkStatement extends ProgramStatement {
	/**
	 * インスタンスを生成.
	 * @param line Line
	 */
	public UlkStatement(final LineInfo line) {
		super(line);
	}
}
