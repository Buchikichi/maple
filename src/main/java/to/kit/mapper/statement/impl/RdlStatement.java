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
	 * @param line Line
	 */
	public RdlStatement(final LineInfo line) {
		super(line);
		// @RDL,c,d,r,l[,lab] cc vdata .
		// l 行番号
		// lab ラベル
		// cc 取得位置
		// vdata 変数
		int c = 1;

		for (String elem : getFirstElements()) {
			if (this.cdr.size() < 3 && 0 < c) {
				if (this.cdr.isEmpty()) {
					c = NumberUtils.toInt(elem);
				}
				this.cdr.add(elem);
				continue;
			}
			if (this.lineNumber == null) {
				this.lineNumber = elem;
			} else {
				this.label = elem;
			}
		}
		for (String elem : line.get(1).split(",")) {
			this.ccList.add(elem);
		}
		for (String elem : line.get(2).split(",")) {
			this.vdataList.add(elem);
		}
		LOG.debug("[{}]", StringUtils.join(line, '|'));
		String msg = String.format(" %s_%s.%s.%s_%s]", StringUtils.join(this.cdr, '|'), this.lineNumber, this.label,
				StringUtils.join(this.ccList, '|'), StringUtils.join(this.vdataList, '|'));
		LOG.debug(msg);
	}

	@Override
	public ProgramStatement execute() {
		VariableManager mgr = VariableManager.getInstance();
		int currentLine = NumberUtils.toInt(mgr.getValue(this.lineNumber));

		if (this.startLine == -1) {
			this.startLine = currentLine;
		}
		if (this.label != null && 10 < currentLine - this.startLine) {
			// TODO このロジックは嘘なので削除する
			return this.unit.getLabelStatement(this.label);
		}
		return super.execute();
	}
}
