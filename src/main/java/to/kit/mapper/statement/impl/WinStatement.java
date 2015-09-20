package to.kit.mapper.statement.impl;

import java.awt.Frame;

import org.apache.commons.lang3.StringUtils;

import to.kit.mapper.program.ProgramUnit;
import to.kit.mapper.program.VariableManager;
import to.kit.mapper.statement.DrawingStatement;
import to.kit.mapper.statement.ProgramStatement;
import to.kit.mapper.window.WinDialog;

/**
 * WIN.
 * @author Hidetaka Sasai.
 */
public final class WinStatement extends DrawingStatement {
	public WinStatement(String... params) {
System.out.println(params.length + ":" + StringUtils.join(params, "|"));
		setParent(params[0]);
		int ix = setPosAndColor(params, 1);
		if (params.length <= ix) {
			return;
		}
		setCaption(params[ix++]);
		if (params.length <= ix) {
			return;
		}
		setName(params[ix]);
//		System.out.println("\t" + this.name);
	}

	@Override
	public ProgramStatement execute() {
		VariableManager var = VariableManager.getInstance();
		ProgramUnit unit = getUnit();
		String parent = getParent();
		String name = var.getPureName(getName());
		Frame owner = null;
		WinDialog panel = new WinDialog(owner, getCaption(), getRectangle());

		unit.addWin(name, panel);
		return super.execute();
	}
}
