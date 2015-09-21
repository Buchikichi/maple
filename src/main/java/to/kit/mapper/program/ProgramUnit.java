package to.kit.mapper.program;

import java.awt.Component;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import to.kit.mapper.statement.ProgramStatement;
import to.kit.mapper.statement.impl.LabelStatement;
import to.kit.mapper.statement.impl.RunStatement;
import to.kit.mapper.window.Win;
import to.kit.mapper.window.WinDialog;

public final class ProgramUnit {
	/** Logger. */
	private static final Logger LOG = LoggerFactory.getLogger(ProgramUnit.class);

	/** ソースファイル. */
	private final String src;
	/** ステートメント. */
	private ProgramStatement firstStatement;
	/** 最後のステートメント. */
	private ProgramStatement latestStatement;
	/** ラベル. */
	private final Map<String, LabelStatement> labelMap = new HashMap<>();
	/** WINマップ. */
	private final Map<String, Win> winMap = new TreeMap<>();

	/**
	 * インスタンス生成.
	 * @param file ソースファイル
	 */
	public ProgramUnit(String filename) {
		this.src = filename;
	}

	/**
	 * WINを追加.
	 * @param name WIN名
	 * @param win WIN
	 */
	public void addWin(String name, Win win) {
		this.winMap.put(name, win);
	}

	/**
	 * WINを取得.
	 * @param name WIN名
	 * @return WIN
	 */
	public Win getWin(String name) {
		return this.winMap.get(name);
	}

	/**
	 * WINを取得.
	 * @param name WIN名もしくはコントロール名
	 * @return WIN
	 */
	public Win findWin(String name) {
		Win win = this.winMap.get(name);

		if (win != null) {
			return win;
		}
		for (Map.Entry<String, Win> entry : this.winMap.entrySet()) {
			WinDialog wk = (WinDialog) entry.getValue();
			Map<String, Component> compMap = wk.getCompMap();

			if (compMap.containsKey(name)) {
				win = wk;
				break;
			}
		}
		return win;
	}

	public Win getLatestWin() {
		Win latest = null;

		for (Map.Entry<String, Win> entry : this.winMap.entrySet()) {
			WinDialog win = (WinDialog) entry.getValue();

			if (win.isVisible()) {
				latest = win;
			}
		}
		return latest;
	}

	/**
	 * プログラムユニットを実行する.
	 * @return 次のRUN-ID
	 */
	public String execute() {
		String nextId = null;
		ProgramStatement stmt = this.firstStatement;

		while (stmt != null) {
			LOG.debug("{}", stmt.getLine());
			ProgramStatement nextStmt = stmt.execute();
			if (stmt instanceof RunStatement) {
				nextId = ((RunStatement) stmt).getNextId();
				break;
			}
			stmt = nextStmt;
		}
		return nextId;
	}

	/**
	 * ステートメントを追加する.
	 * @param statement ステートメント
	 */
	public void addStatement(ProgramStatement statement) {
		statement.setUnit(this);
		if (this.firstStatement == null) {
			this.firstStatement = statement;
		}
		if (statement instanceof LabelStatement) {
			LabelStatement stmt = (LabelStatement) statement;
			this.labelMap.put(stmt.getLabel(), stmt);
		}
		if (this.latestStatement != null) {
			this.latestStatement.setNext(statement);
		}
		this.latestStatement = statement;
	}
	public String getSrc() {
		return this.src;
	}
	public ProgramStatement getStatement() {
		return this.firstStatement;
	}
	public LabelStatement getLabelStatement(String label) {
		return this.labelMap.get(label);
	}
}
