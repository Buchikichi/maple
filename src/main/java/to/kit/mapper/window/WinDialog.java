package to.kit.mapper.window;

import java.awt.Component;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.SpringLayout;
import javax.swing.WindowConstants;
import javax.swing.text.JTextComponent;

import org.apache.commons.lang3.StringUtils;

import to.kit.mapper.statement.impl.LabelStatement;

public final class WinDialog extends JDialog implements Win {
	/** レイアウトマネージャー. */
	private final SpringLayout mgr = new SpringLayout();
	/** ラベル. */
	private LabelStatement labelStatement;

	/**
	 * インスタンスを生成.
	 * @param owner オーナーウィンドウ
	 * @param title タイトル
	 * @param r Rectangle
	 * @param color 色
	 */
	public WinDialog(final Window owner, final String title, final Rectangle r, final String color) {
		super(owner);
		setTitle(title);
		setLayout(this.mgr);
		int left = r.x * CHAR_WIDTH;
		int top = r.y * CHAR_HEIGHT;
		int width = left + r.width * CHAR_WIDTH;
		int height = top + r.height * CHAR_HEIGHT;
		setBounds(left, top, width, height);
		ColorUtils.setColor(this.getContentPane(), color);
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
	}

	private void addComponent(String name, Point p, Component comp) {
		int left = p.x * CHAR_WIDTH;
		int top = p.y * CHAR_HEIGHT;

		this.mgr.putConstraint(SpringLayout.WEST, comp, left, SpringLayout.WEST, this);
		this.mgr.putConstraint(SpringLayout.NORTH, comp, top, SpringLayout.NORTH, this);
		comp.setName(name);
		add(comp);
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
		comp.setName(name);
		add(name, comp);
	}

	public Component getComponent(String name) {
		Component result = null;

		for (Component comp : getContentPane().getComponents()) {
			if (name.equals(comp.getName())) {
				result = comp;
				break;
			}
		}
		return result;
	}

	public String getComponentValue(String componentName) {
		Component comp = getComponent(componentName);
		if (comp instanceof JTextComponent) {
			return ((JTextComponent) comp).getText();
		}
		return StringUtils.EMPTY;
	}

	public void addEvent(String name, final LabelStatement stmt) {
		Component comp = getComponent(name);

		if (comp instanceof JButton) {
			((JButton) comp).addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					setLabelStatement(stmt);
					setVisible(false);
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
	public void addLabel(Point p, String text, String color) {
		JLabel label = new JLabel(text);

		ColorUtils.setColor(label, color);
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
}
