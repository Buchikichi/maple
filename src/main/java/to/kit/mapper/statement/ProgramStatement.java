package to.kit.mapper.statement;

import to.kit.mapper.program.ProgramUnit;

/**
 * ステートメントの基底クラス.
 * @author Hidetaka Sasai
 */
public abstract class ProgramStatement {
	/** ユニット. */
	private ProgramUnit unit;
	/** 次のステートメント. */
	private ProgramStatement next;

	/**
	 * ステートメントを実行する.
	 * @return 次のステートメント
	 */
	public ProgramStatement execute() {
		return this.next;
	}

	public ProgramUnit getUnit() {
		return this.unit;
	}
	public void setUnit(ProgramUnit unit) {
		this.unit = unit;
	}
	public ProgramStatement getNext() {
		return this.next;
	}
	public void setNext(ProgramStatement next) {
		this.next = next;
	}
}
