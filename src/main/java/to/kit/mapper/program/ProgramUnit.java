package to.kit.mapper.program;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import to.kit.mapper.io.Loader;
import to.kit.mapper.io.MapperTokenizer.LineInfo;
import to.kit.mapper.statement.ProgramStatement;
import to.kit.mapper.statement.StatementUtils;
import to.kit.mapper.statement.impl.LabelStatement;
import to.kit.mapper.statement.impl.RunStatement;
import to.kit.mapper.window.WinManager;

/**
 * プログラム単位.
 * @author Hidetaka Sasai
 */
@Component
@Scope("prototype")
public final class ProgramUnit {
	/** Logger. */
	private static final Logger LOG = LoggerFactory.getLogger(ProgramUnit.class);

	/** ソースファイル名. */
	private String src;
	/** ステートメント. */
	private ProgramStatement firstStatement;
	/** ラベル. */
	private final Map<String, LabelStatement> labelMap = new HashMap<>();
	/** WINマネージャー. */
	private WinManager winManager = new WinManager();
	/** プログラムローダー. */
	@Autowired
	private Loader loader;

	/**
	 * プログラムをロードする.
	 * @param filename ファイル名
	 * @param charset Charset
	 * @throws IOException 入出力例外
	 */
	public void load(final String filename, final Charset charset) throws IOException {
		LOG.debug("Load[{}]", filename);
		List<LineInfo> list = this.loader.load(filename, charset);

		this.src = filename;
		//LOG.debug(StringUtils.join(list, "\n"));
		ProgramStatement latestStatement = null;
		for (LineInfo line : list) {
			ProgramStatement stmt = StatementUtils.getStatement(line);

			if (stmt == null) {
				continue;
			}
			stmt.setUnit(this);
			if (this.firstStatement == null) {
				this.firstStatement = stmt;
			}
			if (stmt instanceof LabelStatement) {
				LabelStatement label = (LabelStatement) stmt;
				this.labelMap.put(label.getLabel(), label);
			}
			if (latestStatement != null) {
				latestStatement.setNext(stmt);
			}
			latestStatement = stmt;
		}
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
	 * ソースファイル名を取得.
	 * @return ソースファイル名
	 */
	public String getSrc() {
		return this.src;
	}
	/**
	 * ラベルを取得
	 * @param label ラベル
	 * @return ラベル
	 */
	public LabelStatement getLabelStatement(String label) {
		return this.labelMap.get(label);
	}
	/**
	 * WinManagerを取得.
	 * @return WinManager
	 */
	public WinManager getWinManager() {
		return this.winManager;
	}
}
