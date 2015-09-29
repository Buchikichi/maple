package to.kit.mapper.window;

import java.awt.Color;
import java.awt.Container;

import javax.swing.JComponent;

import org.apache.commons.lang3.StringUtils;

/**
 * Colorユーティリティ.
 * @author Hidetaka Sasai
 */
public final class ColorUtils {
	private ColorUtils() {
		// nop
	}

	/**
	 * コンポーネントの色を設定.
	 * @param component コンポーネント
	 * @param code 色
	 */
	public static void setColor(final Container component, final String code) {
		String[] codes = StringUtils.defaultString(code).split("/");

		if (0 < codes.length) {
			Color foreground = ColorMap.get(codes[0]);

			if (foreground != null) {
				component.setForeground(foreground);
			}
		}
		if (1 < codes.length) {
			Color background = ColorMap.get(codes[1]);

			if (background != null) {
				if (component instanceof JComponent) {
					((JComponent) component).setOpaque(true);
				}
				component.setBackground(background);
			}
		}
	}

	/**
	 * カラーマップ.
	 * @author Hidetaka Sasai
	 */
	enum ColorMap {
		BLA(Color.BLACK),
		BLU(Color.BLUE),
		RED(Color.RED),
		PIN(Color.PINK),
		GRE(Color.GREEN),
		YEL(Color.YELLOW),
		WHI(Color.WHITE),
		LGR(Color.LIGHT_GRAY),
		;
		private Color color;

		/**
		 * インスタンス生成.
		 * @param color Color
		 */
		private ColorMap(Color color) {
			this.color = color;
		}
		private boolean matches(String code) {
			return code.startsWith(this.toString());
		}
		static Color get(String code) {
			Color color = null;

			if (code == null || code.isEmpty()) {
				return color;
			}
			for (ColorMap col : values()) {
				if (col.matches(code)) {
					color = col.color;
					break;
				}
			}
if (color == null) {
	color = Color.ORANGE;
}
			return color;
		}
	}
}
