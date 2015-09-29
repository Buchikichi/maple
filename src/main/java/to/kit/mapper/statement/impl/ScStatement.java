package to.kit.mapper.statement.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import to.kit.mapper.io.MapperTokenizer.LineInfo;
import to.kit.mapper.statement.ProgramStatement;

/**
 * SC.
 * @author Hidetaka Sasai
 */
public final class ScStatement extends ProgramStatement {
	/** Logger. */
	private static final Logger LOG = LoggerFactory.getLogger(ScStatement.class);

	private String msg;

	/**
	 * インスタンスを生成.
	 * @param line Line
	 */
	public ScStatement(final LineInfo line) {
		super(line);
		StringBuilder buff = new StringBuilder();

		for (String str : line) {
			buff.append(str);
		}
		this.msg = buff.toString();
	}

	@Override
	public ProgramStatement execute() {
		LOG.info("{}", this.msg);
		return super.execute();
	}
}
