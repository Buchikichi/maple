package to.kit.mapper.statement.impl;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;

import to.kit.mapper.io.MapperTokenizer.LineInfo;
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
		String[] firstElements = getFirstElements();
		boolean isFormat1 = 0 < firstElements.length;

		if (isFormat1) {
			int c = NumberUtils.toInt(firstElements[0]);

			String l = firstElements[1];
			String q = firstElements[2];
			setParent(firstElements[3]);
			if (firstElements.length <= 4) {
				return;
			}
			setPosAndColor(firstElements, 4);
			setName(line.get(1));
			return;
		}
		setCaption(line.get(1));
		String[] elements = line.get(2).substring(1).split(",");
		setParent(elements[0]);
		setPosAndColor(elements, 1);
		setName(line.get(3));
	}

	@Override
	public ProgramStatement execute() {
		VariableManager var = VariableManager.getInstance();
		String name = var.getPureName(getName());

		if (StringUtils.isNotBlank(name)) {
			String value = var.getValue(getCaption());
			Win win = getParentWindow();

			win.addEdit(name, getRectangle(), value);
		}
		return super.execute();
	}
}
