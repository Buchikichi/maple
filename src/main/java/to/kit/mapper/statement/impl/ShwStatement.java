package to.kit.mapper.statement.impl;

import java.util.ArrayList;
import java.util.List;

import to.kit.mapper.io.MapperTokenizer.LineInfo;
import to.kit.mapper.program.ProgramUnit;
import to.kit.mapper.statement.ProgramStatement;
import to.kit.mapper.window.Win;
import to.kit.mapper.window.WinDialog;

public final class ShwStatement extends ProgramStatement {
	private List<String> list = new ArrayList<>();

	public ShwStatement(final LineInfo line) {
		super(line);
		for (String target : line) {
			if (target.startsWith("<")) {
				this.list.add(target);
			}
		}
	}

	@Override
	public ProgramStatement execute() {
		ProgramUnit unit = getUnit();

		for (String target : this.list) {
			Win win = unit.getWin(target);
			if (win instanceof WinDialog) {
				((WinDialog) win).setVisible(true);
			}
		}
		return super.execute();
	}
}
