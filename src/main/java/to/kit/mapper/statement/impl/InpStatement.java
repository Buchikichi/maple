package to.kit.mapper.statement.impl;

import java.awt.Component;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.swing.JTextField;

import to.kit.mapper.program.ProgramUnit;
import to.kit.mapper.program.VariableManager;
import to.kit.mapper.statement.ProgramStatement;
import to.kit.mapper.window.WinDialog;

/**
 * INP(Accept Input)
 * @author Hidetaka Sasai
 */
public final class InpStatement extends ProgramStatement {
	private List<String> list = new ArrayList<>();

	/**
	 * インスタンスを生成.
	 * @param params パラメーター
	 */
	public InpStatement(String... params) {
		// @INP[,vfocus,,vinput vwh,(lab),...vw] .
		// @INP[,vwh,fxmt?] .
		// @INP[,vwh,...,vwh] .
		// @INP .
		for (String param : params) {
			if (!param.startsWith("<") && !param.startsWith("(")) {
				continue;
			}
			this.list.add(param);
		}
//		System.out.println("\t" + StringUtils.join(this.list, "|"));
	}

	private WinDialog findWin() {
		WinDialog win = null;
		ProgramUnit unit = getUnit();

		for (String param : this.list) {
			win = (WinDialog) unit.findWin(param);
			if (win != null) {
				break;
			}
		}
		return win;
	}

	private ProgramStatement executeDialog() {
		VariableManager var = VariableManager.getInstance();
		ProgramUnit unit = getUnit();
		WinDialog dialog = findWin();
		String targetName = null;

		for (String param : this.list) {
			if (param.startsWith("(")) {
				String label = var.getValue(param);
				LabelStatement labelStatement = unit.getLabelStatement(label);

				dialog.addEvent(targetName, labelStatement);
			}
			targetName = param;
		}
		if (dialog != null) {
			dialog.setModal(true);
			dialog.setVisible(true);
			Map<String, Component> compMap = dialog.getCompMap();
			for (Map.Entry<String, Component> entry : compMap.entrySet()) {
				String name = entry.getKey();
				Component comp = entry.getValue();

				if (comp instanceof JTextField) {
					JTextField edt = (JTextField) comp;

					var.put(name, edt.getText());
				}
			}
			ProgramStatement stmt = dialog.getLabelStatement();

			if (stmt != null) {
				return stmt;
			}
		}
		return super.execute();
	}

	private ProgramStatement executeInput() {
		VariableManager var = VariableManager.getInstance();

		for (String param : this.list) {
			var.queue(param);
		}
		return super.execute();
	}

	/**
	 * Waitするか調べる.<br/>
	 * ラベルを持っている場合、Wait
	 * @return Waitする場合はtrue、そうでない場合はfalse
	 */
	private boolean isWait() {
		boolean isWait = false;

		for (String param : this.list) {
			if (param.startsWith("(")) {
				isWait = true;
				break;
			}
		}
		return isWait;
	}

	@Override
	public ProgramStatement execute() {
		ProgramStatement stmt;
		if (isWait()) {
			stmt = executeDialog();
		} else {
			stmt = executeInput();
		}
		return stmt;
	}
}
