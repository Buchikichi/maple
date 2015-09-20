package to.kit.mapper.statement.impl;

import javax.swing.JOptionPane;

import to.kit.mapper.program.ProgramUnit;
import to.kit.mapper.program.VariableManager;
import to.kit.mapper.statement.ProgramStatement;
import to.kit.mapper.window.WinDialog;

public class MbxStatement extends ProgramStatement {
	private String buttons;
	private String type;
	private String title;
	private String msg;
	private String res1;
	private String label1;
	private String res2;
	private String label2;

	public MbxStatement(String... params) {
		this.buttons = params[0];
		this.type = params[1];
		this.title = params[2];
		this.msg = params[3];
		this.res1 = params[4];
		this.label1 = params[5];
		if (params.length < 7) {
			return;
		}
		this.res2 = params[6];
		this.label2 = params[7];
	}

	@Override
	public ProgramStatement execute() {
		VariableManager var = VariableManager.getInstance();
		ProgramUnit unit = getUnit();
		WinDialog win = (WinDialog) unit.getLatestWin();
		int optionType;
		if ("YN".equals(this.buttons)) {
			optionType = JOptionPane.YES_NO_OPTION;
		} else {
			optionType = JOptionPane.DEFAULT_OPTION;
		}
		int messageType;
		if ("E".equals(this.type)) {
			messageType = JOptionPane.ERROR_MESSAGE;
		} else if ("Q".equals(this.type)) {
			messageType = JOptionPane.QUESTION_MESSAGE;
		} else {
			messageType = JOptionPane.INFORMATION_MESSAGE;
		}
		String message = var.getValue(this.msg);
		int rc = JOptionPane.showConfirmDialog(win, message, this.title, optionType, messageType);
		String label;

		if (rc == JOptionPane.NO_OPTION) {
			label = this.label2;
		} else {
			label = this.label1;
		}
		LabelStatement stmt = unit.getLabelStatement(var.getValue(label));
		if (stmt != null) {
			return stmt;
		}
		return super.execute();
	}
}
