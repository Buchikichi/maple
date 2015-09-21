package to.kit.mapper.statement.impl;

import to.kit.mapper.io.MapperTokenizer.LineInfo;
import to.kit.mapper.program.ProgramUnit;
import to.kit.mapper.statement.ProgramStatement;

/**
 * GTO.
 * @author Hidetaka Sasai
 */
public final class GtoStatement extends ProgramStatement {
	/** 行き先ラベル. */
	private String label;

	/**
	 * インスタンスを生成.
	 * @param line Line
	 */
	public GtoStatement(final LineInfo line) {
		super(line);
		this.label = line.get(1);
	}

	@Override
	public ProgramStatement execute() {
		ProgramUnit unit = getUnit();

		return unit.getLabelStatement(this.label);
	}
}
