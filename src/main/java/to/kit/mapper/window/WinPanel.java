package to.kit.mapper.window;

import java.awt.Component;
import java.awt.Point;
import java.awt.Rectangle;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SpringLayout;
import javax.swing.border.TitledBorder;

public final class WinPanel extends JPanel implements Win {
	private SpringLayout mgr = new SpringLayout();

	/**
	 * インスタンスを生成.
	 * @param title タイトル
	 */
	public WinPanel(String title) {
		setLayout(this.mgr);
		setBorder(new TitledBorder(title));
	}

	private void addComponent(Point p, Component comp) {
		int left = p.x * CHAR_WIDTH;
		int top = p.y * CHAR_HEIGHT;

		this.mgr.putConstraint(SpringLayout.WEST, comp, left, SpringLayout.WEST, this);
		this.mgr.putConstraint(SpringLayout.NORTH, comp, top, SpringLayout.NORTH, this);
		add(comp);
	}

	private void addComponent(Rectangle rect, Component comp) {
		int left = rect.x * CHAR_WIDTH;
		int top = rect.y * CHAR_HEIGHT;
		int width = left + rect.width * CHAR_WIDTH;
		int height = top + rect.height * CHAR_HEIGHT;

		this.mgr.putConstraint(SpringLayout.WEST, comp, left, SpringLayout.WEST, this);
		this.mgr.putConstraint(SpringLayout.NORTH, comp, top, SpringLayout.NORTH, this);
		this.mgr.putConstraint(SpringLayout.EAST, comp, width, SpringLayout.WEST, this);
		this.mgr.putConstraint(SpringLayout.SOUTH, comp, height, SpringLayout.NORTH, this);
		add(comp);
		revalidate();
	}

	@Override
	public void addLabel(Point p, String text, String color) {
		JLabel label = new JLabel(text);

		addComponent(p, label);
	}
	@Override
	public void addButton(String name, Rectangle rect, String text) {
		JButton button = new JButton(text);

		addComponent(rect, button);
	}
	@Override
	public void addEdit(String name, Rectangle rect, String text) {
		JTextField edit = new JTextField(text);
		addComponent(rect, edit);
	}
}
