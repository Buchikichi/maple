package to.kit.mapper.statement.impl;

import to.kit.mapper.io.MapperTokenizer.LineInfo;
import to.kit.mapper.statement.ProgramStatement;

/**
 * ラベル.
 * @author Hidetaka Sasai
 */
public final class LabelStatement extends ProgramStatement {
	/** ラベル. */
	private final String label;

	/**
	 * インスタンスを生成.
	 * @param line Line
	 */
	public LabelStatement(final LineInfo line) {
		super(line);
		this.label = line.peek().replaceAll("[@:]", "");
	}

	@Override
	public ProgramStatement execute() {
		return super.execute();
	}

	/**
	 * ラベルを取得.
	 * @return ラベル
	 */
	public String getLabel() {
		return this.label;
	}
}
