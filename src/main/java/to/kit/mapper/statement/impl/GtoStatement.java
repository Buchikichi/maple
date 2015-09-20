package to.kit.mapper.statement.impl;

import org.apache.commons.lang3.StringUtils;

import to.kit.mapper.program.ProgramUnit;
import to.kit.mapper.statement.ProgramStatement;

public final class GtoStatement extends ProgramStatement {
	/** 行き先ラベル. */
	private String label;

	public GtoStatement(String... params) {
		this.label = params[0];
		if (params.length != 1) {
			System.out.println("\t" + params.length + ":" + StringUtils.join(params, "|"));
		}
	}

	@Override
	public ProgramStatement execute() {
		ProgramUnit unit = getUnit();

		return unit.getLabelStatement(this.label);
	}
}
