package to.kit.mapper.io;

import java.io.IOException;
import java.io.Reader;
import java.io.StreamTokenizer;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import to.kit.mapper.statement.OP;

/**
 * Tokenizer of MAPPER.
 * @author Hidetaka Sasai
 */
public final class MapperTokenizer extends StreamTokenizer {
	/** Logger. */
	private static final Logger LOG = LoggerFactory.getLogger(MapperTokenizer.class);
	/** single quote. */
	private static final char QUOTE_S = '\'';
	/** double quote. */
	private static final char QUOTE_D = '"';

	/**
	 * インスタンスを生成.
	 * @param r Reader
	 */
	public MapperTokenizer(Reader r) {
		super(r);
		resetSyntax();
		wordChars('#', '%');
		wordChars('(', '~');
		wordChars(128 + 32, 255);
		whitespaceChars(0, ' ');
		quoteChar(QUOTE_D);
		quoteChar(QUOTE_S);
		eolIsSignificant(true);
	}

	private List<LineInfo> load() throws IOException {
		List<LineInfo> list = new ArrayList<>();
		LineInfo line = null;

		for (int type; (type = nextToken()) != TT_EOF;) {
			switch (type) {
			case QUOTE_S:
				if (line != null) {
					line.add(QUOTE_S + this.sval + QUOTE_S);
				}
				break;
			case QUOTE_D:
				if (line != null) {
					line.add(QUOTE_D + this.sval + QUOTE_D);
				}
				break;
			case StreamTokenizer.TT_WORD:
				if (line == null) {
					line = new LineInfo(lineno());
					list.add(line);
				}
				boolean isLabel = this.sval.matches("@[0-9]+[:].+");
				if (isLabel) {
					for (String element : this.sval.split(":")) {
						line.add(element);
					}
				} else {
					line.add(this.sval);
				}
				break;
			case StreamTokenizer.TT_EOL:
				line = null;
				break;
			default:
				LOG.error("type:{}", String.valueOf((char) type));
				throw new UnsupportedOperationException();
			}
		}
		return list;
	}

	private List<LineInfo> splitLine(LineInfo line) {
		List<LineInfo> list = new ArrayList<>();
		List<Integer> pointList = new ArrayList<>();
		int ix = 0;

		for (String elem : line) {
			if (!elem.matches("[,]+")) {
				String word = elem.split(",")[0];
				boolean isCommand = word.matches("[A-Z]{2,4}|;") && OP.get(word) == null;

				if (isCommand) {
					pointList.add(Integer.valueOf(ix));
				}
			}
			ix++;
		}
		if (!pointList.isEmpty()) {
			pointList.add(Integer.valueOf(ix));
			ix = 0;
			for (Integer point : pointList) {
				int pt = point.intValue();
				LineInfo newLine = new LineInfo(line.getNum());

				newLine.addAll(line.subList(ix, pt));
				list.add(newLine);
				ix = pt;
			}
		}
		if (list.isEmpty()) {
			// 結局もとのまま
			list.add(line);
		}
		return list;
	}

	/**
	 * 分割済みの全ての行を取得.
	 * @return 行情報一覧
	 * @throws IOException 入出力例外
	 */
	public List<LineInfo> getList() throws IOException {
		List<LineInfo> list = new ArrayList<>();

		for (LineInfo line : load()) {
			String cmd = line.peek();
			boolean isDiscarded = cmd.startsWith(".") || cmd.startsWith("*") || cmd.startsWith("@.");

			if (isDiscarded) {
				continue;
			}
			// コマンド分割
			for (LineInfo newLine : splitLine(line)) {
				if (newLine.isEmpty() || "@".equals(newLine.peek())) {
					continue;
				}
				list.add(newLine);
			}
		}
		return list;
	}

	/**
	 * 行情報.
	 * @author Hidetaka Sasai
	 */
	public class LineInfo extends LinkedList<String> {
		/** 行番号. */
		private final int num;

		/**
		 * インスタンスを生成.
		 * @param lineno 行番号
		 */
		LineInfo(int lineno) {
			this.num = lineno;
		}
		/**
		 * コマンドを取得.
		 * @return コマンド
		 */
		public String getCommand() {
			String peek = peek();
			boolean isLabel = peek.matches("@[0-9]+[:]*");

			if (isLabel) {
				return "label";
			}
			int ix = peek.indexOf(',');
			int beginIndex = peek.startsWith("@") ? 1 : 0;

			if (ix != -1) {
				peek = peek.substring(beginIndex, ix);
			} else {
				peek = peek.substring(beginIndex);
			}
			if (";".equals(peek)) {
				return "else";
			}
			peek = peek.replace("+", "Plus");
			boolean isLiteral = !peek.matches("[A-Z]+");
			if (isLiteral) {
				return "sc";
			}
			return peek;
		}
		/**
		 * 行番号を取得.
		 * @return 行番号
		 */
		public int getNum() {
			return this.num;
		}
		@Override
		public String toString() {
			return String.format("%3d:[%s]", Integer.valueOf(this.num), StringUtils.join(this, '|'));
		}
	}
}
