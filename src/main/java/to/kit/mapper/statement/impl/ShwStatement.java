package to.kit.mapper.statement.impl;

import java.util.ArrayList;
import java.util.List;

import to.kit.mapper.io.MapperTokenizer.LineInfo;
import to.kit.mapper.statement.ProgramStatement;
import to.kit.mapper.window.Win;
import to.kit.mapper.window.WinDialog;
import to.kit.mapper.window.WinManager;

/**
 * SHW.(Show Control)
 * @author Hidetaka Sasai
 */
public final class ShwStatement extends ProgramStatement {
	private List<String> list = new ArrayList<>();

	/**
	 * インスタンスを生成.
	 * @param line Line
	 */
	public ShwStatement(final LineInfo line) {
		// @SHW[,vwh,...,vwh] .
		super(line);
		for (String target : getFirstElements()) {
			this.list.add(target);
		}
	}

	@Override
	public ProgramStatement execute() {
		WinManager winManager = this.unit.getWinManager();

		for (String target : this.list) {
			Win win = winManager.get(target);
			if (win instanceof WinDialog) {
				((WinDialog) win).setVisible(true);
			}
		}
		return super.execute();
	}
}
