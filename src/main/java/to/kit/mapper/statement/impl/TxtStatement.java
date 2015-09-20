package to.kit.mapper.statement.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.math.NumberUtils;

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
	public TxtStatement(String... params) {
		// Format1: @TXT[,c,d,r,l,q,vwh,vert,hort,vsiz,hsiz,fc/bc,o] [vth] .
		// Format2: @TXT[,"text",vwh,vert,hort,vsiz,hsiz,fc/bc,o] [vth] .
//		System.out.println("\t" + params.length + ":" + StringUtils.join(params, "|"));
		String text = params[0];

		if (!text.startsWith("\"")) {
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
		setCaption(params[0]);
		setParent(params[1]);
		if (params.length <= 2) {
			return;
		}
		int ix = setPosAndColor(params, 2);
		if (params.length <= ix) {
			return;
		}
		String str = params[ix];
		if (params.length == ix) {
			setName(str);
			return;
		}
//		setColor(str);
		String un = params[7];
		if (".".equals(un)) {
			return;
		}
//		setBgColor(un);
		if (params.length < 9) {
			return;
		}
		setName(params[8]);
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
