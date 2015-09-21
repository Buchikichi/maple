package to.kit.mapper.statement.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.text.StrMatcher;
import org.apache.commons.lang3.text.StrTokenizer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import to.kit.mapper.io.MapperTokenizer.LineInfo;
import to.kit.mapper.program.Variable;
import to.kit.mapper.program.VariableManager;
import to.kit.mapper.statement.ProgramStatement;

/**
 * LDV(Load Variable)
 * @author Hidetaka Sasai
 */
public final class LdvStatement extends ProgramStatement {
	/** Logger. */
	private static final Logger LOG = LoggerFactory.getLogger(LdvStatement.class);
	private String option;
	private List<Variable> varList = new ArrayList<>();

	public String[] splitByEqual(String param) {
		StrTokenizer token = new StrTokenizer(param, "=");

		token.setQuoteMatcher(StrMatcher.singleQuoteMatcher());
		return token.getTokenArray();
	}

	/**
	 * インスタンスを生成.
	 * @param params パラメーター
	 */
	public LdvStatement(final LineInfo line) {
		// @LDV[,o] v=vld[,v=vld,...,v=vld] .
		// @LDV,o v[,v,...,v] .
		// @LDV,Q rv=iv,n[(delim),rv=iv,n(delim),...,rv=iv,n(delim)] .
		// o{C:center, P:pack, R:right-justifies, U:upper, Z:R&zero-fills}
		super(line);
		for (String param : line) {
			if (param.matches("[A-Z]")) {
				this.option = param;
				continue;
			}
			if (param.contains("=")) {
				String[] values = splitByEqual(param);

				if (values.length < 2) {
					this.varList.add(new Variable(values[0], StringUtils.EMPTY));
				} else {
					this.varList.add(new Variable(values[0], values[1]));
				}
			} else {
				this.varList.add(new Variable(param, null));
			}
		}
//		LOG.debug("opt[{}][{}]", StringUtils.join(params, '|'), StringUtils.join(this.varList, '|'));
	}

	@Override
	public ProgramStatement execute() {
		VariableManager var = VariableManager.getInstance();

		for (Variable variable : this.varList) {
			String name = var.getPureName(variable.getName());
			String value = variable.getValue();

			if (value == null) {
				value = var.getValue(name);
			} else {
				value = var.getValue(value);
			}
			// TODO optional function
			var.put(name, value);
		}
		return super.execute();
	}
}
