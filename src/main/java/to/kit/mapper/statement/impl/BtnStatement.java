package to.kit.mapper.statement.impl;

import to.kit.mapper.io.MapperTokenizer.LineInfo;
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
	 * @param line Line
	 */
	public BtnStatement(final LineInfo line) {
		// Format1 @BTN[,vwh,vert,hort,vsiz,hsiz,fc/bc,o,lab] text [vbh] .
		// Format2 @BTN[,vwh,vert,hort,vsiz,hsiz,fc/bc,o,lab] c,d,r[,tf?,oname vbh] .
		//         @BTN[,vwh,vert,hort,vsiz,hsiz,fc/bc,o,lab] c,d,r,[tf?],oname [vbh] .
		//         @BTN[,vwh,vert,hort,vsiz,hsiz,fc/bc,o,lab] [c],[d],[r],[tf?],oname [vbh] .
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
		String name = var.getPureName(getName());
		String value = var.getValue(getCaption());
		Win win = getParentWindow();

		win.addButton(name, getRectangle(), value);
		return super.execute();
	}
}
