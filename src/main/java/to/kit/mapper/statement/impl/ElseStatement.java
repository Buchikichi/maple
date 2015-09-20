package to.kit.mapper.statement.impl;

import to.kit.mapper.statement.ProgramStatement;

/**
 * ";"(else)の実装.<br/>
 * IF の条件が成立し、処理を実行し続けると、ここにたどり着く前提
 * @author Hidetaka Sasai.
 */
public final class ElseStatement extends ProgramStatement {
	@Override
	public ProgramStatement execute() {
		ProgramStatement stmt = getNext();
		for (;;) {
			if (stmt instanceof Nop) {
				break;
			}
			stmt = stmt.getNext();
		}
		return stmt;
	}
}
