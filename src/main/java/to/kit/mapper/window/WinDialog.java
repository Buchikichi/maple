package to.kit.mapper.window;

import static to.kit.mapper.window.WinPanel.CHAR_HEIGHT;
import static to.kit.mapper.window.WinPanel.CHAR_WIDTH;

import java.awt.Component;
import java.awt.Frame;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.SpringLayout;
import javax.swing.WindowConstants;

import org.apache.commons.lang3.StringUtils;

import to.kit.mapper.statement.impl.LabelStatement;

public final class WinDialog extends JDialog implements Win {
	/** レイアウトマネージャー. */
	private final SpringLayout mgr = new SpringLayout();
	/** コンポーネントマップ. */
	private final Map<String, Component> compMap = new HashMap<>();
	/** ラベル. */
	private LabelStatement labelStatement;

	/**
	 * インスタンスを生成.
	 * @param owner オーナーウィンドウ
	 * @param title タイトル
	 * @param r Rectangle
	 */
	public WinDialog(Frame owner, String title, Rectangle r) {
		super(owner);
		setTitle(title);
		setLayout(this.mgr);
		int left = r.x * CHAR_WIDTH;
		int top = r.y * CHAR_HEIGHT;
		int width = left + r.width * CHAR_WIDTH;
		int height = top + r.height * CHAR_HEIGHT;
		setBounds(left, top, width, height);
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
	}

	private void addComponent(String name, Component comp) {
		add(comp);
		if (StringUtils.isNotBlank(name)) {
			this.compMap.put(name, comp);
		}
	}

	private void addComponent(String name, Point p, Component comp) {
		int left = p.x * CHAR_WIDTH;
		int top = p.y * CHAR_HEIGHT;

		this.mgr.putConstraint(SpringLayout.WEST, comp, left, SpringLayout.WEST, this);
		this.mgr.putConstraint(SpringLayout.NORTH, comp, top, SpringLayout.NORTH, this);
		addComponent(name, comp);
	}

	private void addComponent(String name, Rectangle rect, Component comp) {
		int left = rect.x * CHAR_WIDTH;
		int top = rect.y * CHAR_HEIGHT;
		int width = left + rect.width * CHAR_WIDTH;
		int height = top + rect.height * CHAR_HEIGHT;

		this.mgr.putConstraint(SpringLayout.WEST, comp, left, SpringLayout.WEST, this);
		this.mgr.putConstraint(SpringLayout.NORTH, comp, top, SpringLayout.NORTH, this);
		this.mgr.putConstraint(SpringLayout.EAST, comp, width, SpringLayout.WEST, this);
		this.mgr.putConstraint(SpringLayout.SOUTH, comp, height, SpringLayout.NORTH, this);
		addComponent(name, comp);
		revalidate();
	}

	public void addEvent(String name, final LabelStatement stmt) {
		Component comp = this.compMap.get(name);

		if (comp instanceof JButton) {
			((JButton) comp).addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					setLabelStatement(stmt);
					dispose();
				}
			});
		}
	}

	protected void setLabelStatement(LabelStatement stmt) {
		this.labelStatement = stmt;
	}
	public LabelStatement getLabelStatement() {
		return this.labelStatement;
	}

	@Override
	public void dispose() {
		getOwner().setEnabled(true);
		super.dispose();
	}
	@Override
	public void addLabel(Point p, String text) {
		JLabel label = new JLabel(text);

		addComponent(null, p, label);
	}
	@Override
	public void addButton(String name, Rectangle rect, String text) {
		JButton button = new JButton(text);

		addComponent(name, rect, button);
	}
	@Override
	public void addEdit(String name, Rectangle rect, String text) {
		JTextField edit = new JTextField(text, rect.width);

		addComponent(name, new Point(rect.x, rect.y), edit);
	}

	public Map<String, Component> getCompMap() {
		return this.compMap;
	}
}
