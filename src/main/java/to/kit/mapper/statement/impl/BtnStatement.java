package to.kit.mapper.statement.impl;

import to.kit.mapper.program.ProgramUnit;
import to.kit.mapper.program.VariableManager;
import to.kit.mapper.statement.DrawingStatement;
import to.kit.mapper.statement.ProgramStatement;
import to.kit.mapper.window.Win;

/**
 * BTN.
 * @author Hidetaka Sasai
 */
public final class BtnStatement extends DrawingStatement {
	/**
	 * インスタンスを生成.
	 * @param params パラメーター
	 */
	public BtnStatement(String... params) {
		setParent(params[0]);
		int ix = setPosAndColor(params, 1);
		setCaption(params[ix++]);
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

		win.addButton(name, getRectangle(), value);
		return super.execute();
	}
}
