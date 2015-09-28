package to.kit.mapper.statement.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.math.NumberUtils;

import to.kit.mapper.io.MapperTokenizer.LineInfo;
import to.kit.mapper.program.VariableManager;
import to.kit.mapper.statement.ProgramStatement;

/**
 * DEC.
 * @author Hidetaka Sasai
 */
public final class DecStatement extends ProgramStatement {
	/** 減量. */
	private int n = 1;
	/** 変数. */
	private List<String> variableList = new ArrayList<>();

	/**
	 * インスタンスを生成.
	 * @param line Line
	 */
	public DecStatement(final LineInfo line) {
		// @DEC[,n] v[,v,...,v] .
		super(line);
		String[] params = getFirstElements();
		if (0 < params.length) {
			this.n = NumberUtils.toInt(params[0]);
		}
		for (String element : line.get(1).split(",")) {
			this.variableList.add(element);
		}
	}

	@Override
	public ProgramStatement execute() {
		VariableManager mgr = VariableManager.getInstance();

		for (String name : this.variableList) {
			int value = NumberUtils.toInt(mgr.getValue(name));
			value -= this.n;

			mgr.put(name, String.valueOf(value));
		}
		return super.execute();
	}
}
