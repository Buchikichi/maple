package to.kit.mapper.statement.impl;

import java.util.ArrayList;
import java.util.List;

import to.kit.mapper.io.MapperTokenizer.LineInfo;
import to.kit.mapper.program.VariableManager;
import to.kit.mapper.statement.ProgramStatement;

/**
 * CHG(Change Variable).
 * @author Hidetaka Sasai
 */
public final class ChgStatement extends ProgramStatement {
	private List<String> list = new ArrayList<>();
	/** Variable. */
	private String v;
	/** Reserved word. */
	private String rw;

	/**
	 * インスタンスを生成.
	 * @param line Line
	 */
	public ChgStatement(final LineInfo line) {
		// @CHG v {exp|vld} .
		// @CHG rw v[,v,...,v] .
		super(line);
		String str = line.get(1);
		boolean isFormat2 = str.endsWith("$");

		if (!isFormat2) {
			this.v = str;
			// TODO
			return;
		}
		this.rw = str;
		for (String param : line.get(2).split(",")) {
			this.list.add(param);
		}
	}

	@Override
	public ProgramStatement execute() {
		VariableManager var = VariableManager.getInstance();

		for (String param : this.list) {
			String name = var.getPureName(param);

			var.chg(name);
		}
		return super.execute();
	}
}
