package to.kit.mapper.statement.impl;

import to.kit.mapper.io.MapperTokenizer.LineInfo;
import to.kit.mapper.program.ProgramUnit;
import to.kit.mapper.program.VariableManager;
import to.kit.mapper.statement.DrawingStatement;
import to.kit.mapper.statement.ProgramStatement;
import to.kit.mapper.window.Win;

/**
 * EDT.
 * @author Hidetaka Sasai
 */
public final class EdtStatement extends DrawingStatement {
	/**
	 * インスタンスを生成.
	 * @param line Line
	 */
	public EdtStatement(final LineInfo line) {
		// Format1 @EDT,c,d,r[,l,q,vwh,vert,hort,vsiz,hsiz,fc/bc,o] [veh] .
		// Format2 @EDT[,"text",vwh,vert,hort,vsiz,hsiz,fc/bc,o] [veh] .
		super(line);
		String[] params = line.toArray(new String[line.size()]);
		String caption = params[1];
		boolean isFormat2 = caption.startsWith("\"");

		if (!isFormat2) {
			// TODO
			return;
		}
		setCaption(caption);
		String[] elements = params[2].substring(1).split(",");
		setParent(elements[0]);
		setPosAndColor(elements, 1);
		setName(params[3]);
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
