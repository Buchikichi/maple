package to.kit.mapper.statement.impl;

import java.util.ArrayList;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import to.kit.mapper.io.MapperTokenizer.LineInfo;
import to.kit.mapper.program.ProgramUnit;
import to.kit.mapper.program.VariableManager;
import to.kit.mapper.statement.ProgramStatement;

/**
 * RDL(Read Line).
 * @author Hidetaka Sasai
 */
public class RdlStatement extends ProgramStatement {
	/** Logger. */
	private static final Logger LOG = LoggerFactory.getLogger(RdlStatement.class);
	/** c,d,r. */
	private Deque<String> cdr = new LinkedList<>();
	/** 行番号. */
	private String lineNumber;
	/** ラベル. */
	private String label;
	/** CC. */
	private List<String> ccList = new ArrayList<>();
	/** VDATA. */
	private List<String> vdataList = new ArrayList<>();
	/** 開始位置. */
	private int startLine = -1;

	/**
	 * インスタンスを生成.
	 * @param params パラメーター
	 */
	public RdlStatement(final LineInfo line) {
		super(line);
		// @RDL,c,d,r,l[,lab] cc vdata .
		// l 行番号
		// lab ラベル
		// cc 取得位置
		// vdata 変数
		for (String param : line) {
			if (!param.startsWith("-") && (param.contains("-") || param.contains("'"))) {
				this.ccList.add(param);
			} else if (!this.ccList.isEmpty() && param.contains("<")) {
				this.vdataList.add(param);
			} else if (param.matches("0[0-9]+")) {
				this.label = param;
			} else {
				this.cdr.add(param);
			}
			if (this.lineNumber == null && !this.ccList.isEmpty()) {
				this.lineNumber = this.cdr.pollLast();
			}
		}
		LOG.debug("[{}]", StringUtils.join(line, '|'));
		String msg = String.format(" %s_%s.%s.%s_%s]", StringUtils.join(this.cdr, '|'), this.lineNumber, this.label,
				StringUtils.join(this.ccList, '|'), StringUtils.join(this.vdataList, '|'));
		LOG.debug(msg);
	}

	@Override
	public ProgramStatement execute() {
		ProgramUnit unit = getUnit();
		VariableManager mgr = VariableManager.getInstance();
		int currentLine = NumberUtils.toInt(mgr.getValue(this.lineNumber));

		if (this.startLine == -1) {
			this.startLine = currentLine;
		}
		if (this.label != null && 10 < currentLine - this.startLine) {
			// TODO このロジックは嘘なので削除する
			return unit.getLabelStatement(this.label);
		}
		return super.execute();
	}
}
