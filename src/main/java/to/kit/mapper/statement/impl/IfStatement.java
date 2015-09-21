package to.kit.mapper.statement.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import to.kit.mapper.io.MapperTokenizer.LineInfo;
import to.kit.mapper.program.VariableManager;
import to.kit.mapper.statement.OP;
import to.kit.mapper.statement.ProgramStatement;

public final class IfStatement extends ProgramStatement {
	/** 左辺. */
	private List<String> left = new ArrayList<>();
	/** 演算子. */
	private OP operator;
	/** 右辺. */
	private List<String> right = new ArrayList<>();

	/**
	 * インスタンスを生成.
	 * @param params パラメーター
	 */
	public IfStatement(final LineInfo line) {
		super(line);
		boolean isLeft = true;

		for (int ix = 1; ix < line.size(); ix++) {
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
				this.left.add(param);
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
		String leftValue = null;

		for (String var : this.left) {
			leftValue = StringUtils.defaultString(mgr.getValue(var));
		}
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
