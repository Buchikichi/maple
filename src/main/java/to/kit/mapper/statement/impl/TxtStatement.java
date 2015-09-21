package to.kit.mapper.statement.impl;

import java.util.ArrayList;
import java.util.List;

import to.kit.mapper.io.MapperTokenizer.LineInfo;
import to.kit.mapper.program.ProgramUnit;
import to.kit.mapper.program.VariableManager;
import to.kit.mapper.statement.DrawingStatement;
import to.kit.mapper.statement.ProgramStatement;
import to.kit.mapper.window.Win;

/**
 * TXT(Define Text Box).
 * @author Hidetaka Sasai
 */
public final class TxtStatement extends DrawingStatement {
	/** c,d,r. */
	private List<String> cdr = new ArrayList<>();
	/** line number. */
	private String lineNumber;
	/** Number of lines. */
	private String lines;

	/**
	 * インスタンス生成.
	 * @param params パラメーター
	 */
	public TxtStatement(final LineInfo line) {
		super(line);
		// Format1: @TXT[,c,d,r,l,q,vwh,vert,hort,vsiz,hsiz,fc/bc,o] [vth] .
		// Format2: @TXT[,"text",vwh,vert,hort,vsiz,hsiz,fc/bc,o] [vth] .
		String[] params = line.toArray(new String[line.size()]);
		String text = params[1];
		boolean isFormat2 = text.startsWith("\"");

		if (!isFormat2) {
if (text.startsWith("-")) return; // TODO 後でなおす
			this.cdr.add(text);
			this.cdr.add(params[1]);
			this.cdr.add(params[2]);
			this.lineNumber = params[3];
			this.lines = params[4];
			setParent(params[5]);
			int ix = setPosAndColor(params, 6);
			if (params.length <= ix) {
				return;
			}
			setCaption(params[ix++]); // 12
			setName(params[ix]); // 13
			return;
		}
		//  14:[TXT,|"本番系に接続しています"|,<WIN000>,001,001,001,126,BLA/GRE,C|.]
		setCaption(text);
		String[] elements = params[2].substring(1).split(",");
		setParent(elements[0]);
		setPosAndColor(elements, 1);
		if (params.length <= 3) {
			return;
		}
		setName(params[3]);
	}

	@Override
	public ProgramStatement execute() {
		VariableManager var = VariableManager.getInstance();
		ProgramUnit unit = getUnit();
		Win win = unit.getWin(getParent());
		String caption = var.getValue(getCaption());

		if (win == null) {
			win = unit.getLatestWin();
		}
		win.addLabel(getPoint(), caption);
		return super.execute();
	}
}
