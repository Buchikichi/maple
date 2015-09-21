package to.kit.mapper.statement.impl;

import java.awt.Component;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.swing.JTextField;

import org.apache.commons.lang3.StringUtils;

import to.kit.mapper.io.MapperTokenizer.LineInfo;
import to.kit.mapper.program.ProgramUnit;
import to.kit.mapper.program.VariableManager;
import to.kit.mapper.statement.ProgramStatement;
import to.kit.mapper.window.WinDialog;

/**
 * INP.(Accept Input)
 * @author Hidetaka Sasai
 */
public final class InpStatement extends ProgramStatement {
	private String vfocus;
	private String vinput;
	private List<WaitInfo> waitList = new ArrayList<>();
	private List<String> list = new ArrayList<>();

	class WaitInfo {
		private final String vwh;
		private String lab;
		WaitInfo(String vwh) {
			this.vwh = vwh;
		}
		public String getLab() {
			return this.lab;
		}
		public void setLab(String lab) {
			this.lab = lab;
		}
		public String getVwh() {
			return this.vwh;
		}
	}

	/**
	 * インスタンスを生成.
	 * @param params パラメーター
	 */
	public InpStatement(final LineInfo line) {
		super(line);
		// @INP[,vfocus,,vinput vwh,(lab),...vwh,(lab)] .
		// @INP[,vwh,fxmt?] .
		// @INP[,vwh,...,vwh] .
		// @INP .
		boolean isFormat1 = line.size() == 3;
		String[] params = line.toArray(new String[line.size()]);

		if (isFormat1) {
			WaitInfo info = null;
			String[] vf = params[0].split(",");

			this.vfocus = vf[1];
			this.vinput = vf[3];
			for (String element : params[1].split(",")) {
				if (info == null) {
					info = new WaitInfo(element);
					this.waitList.add(info);
				} else {
					String label = StringUtils.strip(element, "()");
					info.setLab(label);
					info = null;
				}
			}
			return;
		}
		for (String element : params[0].split(",")) {
			if (!element.startsWith("<")) {
				continue;
			}
			this.list.add(element);
		}
//		System.out.println("\t" + StringUtils.join(this.list, "|"));
	}

	private ProgramStatement executeDialog() {
		VariableManager var = VariableManager.getInstance();
		ProgramUnit unit = getUnit();
		WinDialog dialog = (WinDialog) unit.findWin(this.vinput);

		for (WaitInfo info : this.waitList) {
			String label = info.getLab();
			LabelStatement labelStatement = unit.getLabelStatement(label);

			dialog.addEvent(info.getVwh(), labelStatement);
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

	@Override
	public ProgramStatement execute() {
		ProgramStatement stmt;
		if (this.waitList.isEmpty()) {
			stmt = executeInput();
		} else {
			stmt = executeDialog();
		}
		return stmt;
	}
}
