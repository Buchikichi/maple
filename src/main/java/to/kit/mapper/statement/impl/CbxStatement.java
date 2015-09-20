package to.kit.mapper.statement.impl;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import to.kit.mapper.statement.ProgramStatement;

public class CbxStatement extends ProgramStatement {
	/** Logger. */
	private static final Logger LOG = LoggerFactory.getLogger(CbxStatement.class);

	public CbxStatement(String... params) {
		LOG.debug("\t" + params.length + ":" + StringUtils.join(params, "|"));

	}
}
