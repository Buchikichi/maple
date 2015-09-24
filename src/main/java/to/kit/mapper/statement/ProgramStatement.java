package to.kit.mapper.statement;

import java.util.Arrays;

import to.kit.mapper.io.MapperTokenizer.LineInfo;
import to.kit.mapper.program.ProgramUnit;

/**
 * ステートメントの基底クラス.
 * @author Hidetaka Sasai
 */
public abstract class ProgramStatement {
	/** 行情報. */
	private final LineInfo line;
	/** 次のステートメント. */
	private ProgramStatement next;
	/** ユニット. */
	protected ProgramUnit unit;

	/**
	 * インスタンスを生成.
	 * @param line Line
	 */
	protected ProgramStatement(final LineInfo line) {
		this.line = line;
	}

	protected String[] getFirstElements() {
		String[] elements = this.line.peek().split(",");

		return Arrays.copyOfRange(elements, 1, elements.length);
	}

	/**
	 * ステートメントを実行する.
	 * @return 次のステートメント
	 */
	public ProgramStatement execute() {
		return this.next;
	}

	public LineInfo getLine() {
		return this.line;
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
