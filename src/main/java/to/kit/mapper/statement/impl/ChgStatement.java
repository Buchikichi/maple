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

	/**
	 * インスタンスを生成.
	 * @param line Line
	 */
	public ChgStatement(final LineInfo line) {
		super(line);
		for (String param : line) {
			// INPUT$
			if (!param.startsWith("<")) {
				continue;
			}
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
