package to.kit.mapper.window;

import java.awt.Point;
import java.awt.Rectangle;

public interface Win {
	/**
	 * TXTを追加.
	 * @param p Point
	 * @param text テキスト
	 */
	void addLabel(Point p, String text);
	/**
	 * BTNを追加.
	 * @param name 名前
	 * @param rect Rectangle
	 * @param text テキスト
	 */
	void addButton(String name, Rectangle rect, String text);
	/**
	 * EDTを追加.
	 * @param name 名前
	 * @param rect Rectangle
	 * @param text テキスト
	 */
	void addEdit(String name, Rectangle rect, String text);
}
