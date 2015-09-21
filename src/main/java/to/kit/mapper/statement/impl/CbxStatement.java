package to.kit.mapper.statement.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import to.kit.mapper.io.MapperTokenizer.LineInfo;
import to.kit.mapper.statement.ProgramStatement;

public class CbxStatement extends ProgramStatement {
	/** Logger. */
	private static final Logger LOG = LoggerFactory.getLogger(CbxStatement.class);

	/**
	 * インスタンスを生成.
	 * @param line Line
	 */
	public CbxStatement(final LineInfo line) {
		super(line);
		LOG.debug("{}", line);
	}
}
