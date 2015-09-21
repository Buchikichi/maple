package to.kit.mapper.statement.impl;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;

import to.kit.mapper.io.MapperTokenizer.LineInfo;
import to.kit.mapper.program.VariableManager;
import to.kit.mapper.statement.ProgramStatement;

/**
 * INC.
 * @author Hidetaka Sasai
 */
public final class IncStatement extends ProgramStatement {
	/** 変数名. */
	private String name;
	/** 値. */
	private String value;

	/**
	 * インスタンスを生成.
	 * @param line Line
	 */
	public IncStatement(final LineInfo line) {
		super(line);
		// @INC[,n] v[,v,...,v] .
		String[] params = line.toArray(new String[line.size()]);
		if (params.length == 1) {
			this.name = params[0];
		} else {
			this.name = params[1];
			this.value = params[0];
		}
	}

	@Override
	public ProgramStatement execute() {
		VariableManager mgr = VariableManager.getInstance();
		int intValue = NumberUtils.toInt(mgr.getValue(this.name));
		String newValue;

		if (StringUtils.isBlank(this.value)) {
			newValue = String.valueOf(++intValue); 
		} else {
			int augend = NumberUtils.toInt(mgr.getValue(this.value));
			newValue = String.valueOf(intValue + augend); 
		}
		mgr.put(this.name, newValue);
		return super.execute();
	}
}
