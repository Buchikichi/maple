package to.kit.mapper.statement.impl;

import org.apache.commons.lang3.math.NumberUtils;

import to.kit.mapper.program.ProgramUnit;
import to.kit.mapper.program.VariableManager;
import to.kit.mapper.statement.DrawingStatement;
import to.kit.mapper.statement.ProgramStatement;
import to.kit.mapper.window.Win;

public final class EdtStatement extends DrawingStatement {
	/**
	 * インスタンスを生成.
	 * @param params パラメーター
	 */
	public EdtStatement(String... params) {
		String caption = params[0];
		int val = NumberUtils.toInt(caption);

		setCaption(caption);
		if (val < 0) {
			return;
		}
		setParent(params[1]);
		int ix = setPosAndColor(params, 2);
		if (params.length <= ix) {
			return;
		}
		setName(params[ix]);
	}

	@Override
	public ProgramStatement execute() {
		VariableManager var = VariableManager.getInstance();
		ProgramUnit unit = getUnit();
		Win win = unit.getWin(getParent());
		String name = var.getPureName(getName());
		String value = var.getValue(getCaption());

		win.addEdit(name, getRectangle(), value);
		return super.execute();
	}
}
