package to.kit.mapper.statement.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import to.kit.mapper.io.MapperTokenizer.LineInfo;
import to.kit.mapper.program.VariableManager;
import to.kit.mapper.statement.OP;
import to.kit.mapper.statement.ProgramStatement;

/**
 * IF.
 * @author Hidetaka Sasai
 */
public final class IfStatement extends ProgramStatement {
	/** 左辺. */
	private String left;
	/** 演算子. */
	private OP operator;
	/** 右辺. */
	private List<String> right = new ArrayList<>();

	/**
	 * インスタンスを生成.
	 * @param line Line
	 */
	public IfStatement(final LineInfo line) {
		super(line);
		boolean isLeft = true;

		for (int ix = 1/* form 1 */; ix < line.size(); ix++) {
			String param = line.get(ix);
			OP op = OP.get(param);
			if (op != null) {
				if (OP.NOT.equals(this.operator)) {
					this.operator = OP.NE;
				} else {
					this.operator = op;
				}
				isLeft = false;
				continue;
			}
			if (isLeft) {
				this.left = param;
			} else {
				this.right.add(param);
			}
		}
//		if (1 < this.left.size()) {
//System.out.println("[" + StringUtils.join(params, ",") + "]" + StringUtils.join(this.left, "|"));
//		}
	}

	private boolean isSatisfied() {
		VariableManager mgr = VariableManager.getInstance();
		boolean isSatisfied = false;
		String leftValue = StringUtils.defaultString(mgr.getValue(this.left));

		for (String var : this.right) {
			String rightValue = StringUtils.defaultString(mgr.getValue(var));

			rightValue = rightValue.trim();
			if (this.operator.satisfy(leftValue, rightValue)) {
				isSatisfied = true;
				break;
			}
		}
		return isSatisfied;
	}

	@Override
	public ProgramStatement execute() {
		if (!isSatisfied()) {
			ProgramStatement stmt = getNext();
			for (;;) {
				String cmd = stmt.getLine().peek();

				if (cmd.startsWith("@")) {
					break;
				}
				if (stmt instanceof ElseStatement || stmt instanceof GtoStatement) {
					stmt = stmt.getNext();
					break;
				}
				stmt = stmt.getNext();
			}
			return stmt;
		}
		return super.execute();
	}
}
