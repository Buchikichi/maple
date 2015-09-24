package to.kit.mapper.window;

import java.awt.Point;
import java.awt.Rectangle;

public interface Win {
	public static final int CHAR_WIDTH = 8;
	public static final int CHAR_HEIGHT = 16;

	/**
	 * TXTを追加.
	 * @param p Point
	 * @param text テキスト
	 * @param color 色
	 */
	void addLabel(Point p, String text, String color);
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
