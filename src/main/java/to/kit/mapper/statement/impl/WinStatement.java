package to.kit.mapper.statement.impl;

import java.awt.Frame;

import to.kit.mapper.io.MapperTokenizer.LineInfo;
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
	/**
	 * インスタンスを生成.
	 * @param line Line
	 */
	public WinStatement(final LineInfo line) {
		// @WIN[,vwh,vert,hort,vsiz,hsiz,fc/bc,o] caption [vwh] .
		super(line);
		String[] params = line.toArray(new String[line.size()]);
		String[] elements = params[0].split(",");

		setParent(elements[1]);
		setPosAndColor(elements, 2);
		setCaption(params[1]);
		if (params.length <= 2) {
			return;
		}
		setName(params[2]);
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
