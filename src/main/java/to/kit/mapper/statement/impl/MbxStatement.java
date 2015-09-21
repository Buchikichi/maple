package to.kit.mapper.statement.impl;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;

import org.apache.commons.lang3.StringUtils;

import to.kit.mapper.io.MapperTokenizer.LineInfo;
import to.kit.mapper.program.ProgramUnit;
import to.kit.mapper.program.VariableManager;
import to.kit.mapper.statement.ProgramStatement;
import to.kit.mapper.window.WinDialog;

/**
 * MBX.
 * @author Hidetaka Sasai
 */
public final class MbxStatement extends ProgramStatement {
	private String buttons;
	private String type;
	private String caption;
	private String msg;
	private List<Act> actList = new ArrayList<>();

	class Act {
		private final String code;
		private String lab;

		Act(String code) {
			this.code = code;
		}
		public String getCode() {
			return this.code;
		}
		public String getLab() {
			return this.lab;
		}
		public void setLab(String lab) {
			this.lab = lab;
		}
	}

	/**
	 * インスタンスを生成.
	 * @param line Line
	 */
	public MbxStatement(final LineInfo line) {
		// @MBX[,btyp,ityp],caption message code,(lab)[,code,(lab),...,code,(lab)] .
		super(line);
		String[] params = line.toArray(new String[line.size()]);
		String[] types = params[0].split(",");
		this.buttons = types[1];
		this.type = types[2];
		this.caption = params[1];
		this.msg = params[2];
		if (params.length <= 3) {
			return;
		}
		Act act = null;
		for (String element : params[3].split(",")) {
			if (act == null) {
				act = new Act(element);
				this.actList.add(act);
			} else {
				act.setLab(element);
				act = null;
			}
		}
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
		int rc = JOptionPane.showConfirmDialog(win, message, this.caption, optionType, messageType);
		String label = null;

		for (Act act : this.actList) {
			String cd = act.getCode();

			if ("Y".equals(cd) && rc == JOptionPane.YES_OPTION) {
				label = act.getLab();
			} else if ("N".equals(cd) && rc == JOptionPane.NO_OPTION) {
				label = act.getLab();
			}
			if (StringUtils.isNotBlank(label)) {
				break;
			}
		}
		LabelStatement stmt = unit.getLabelStatement(var.getValue(label));
		if (stmt != null) {
			return stmt;
		}
		return super.execute();
	}
}
