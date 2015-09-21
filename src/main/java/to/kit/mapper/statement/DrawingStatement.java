package to.kit.mapper.statement;

import java.awt.Point;
import java.awt.Rectangle;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;

import to.kit.mapper.io.MapperTokenizer.LineInfo;

public abstract class DrawingStatement extends ProgramStatement {
	/** 親コンテナー. */
	private String parent;
	private String name;
	private String caption;
	private int posX;
	private int posY;
	private int width;
	private int height;
	private String color;
	private String bgColor;

	/**
	 * インスタンスを生成.
	 * @param line Line
	 */
	public DrawingStatement(final LineInfo line) {
		super(line);
	}

	/**
	 * Gets a point.
	 * @return Point
	 */
	public Point getPoint() {
		return new Point(this.posX, this.posY);
	}

	/**
	 * Gets a rectangle.
	 * @return Rectangle
	 */
	public Rectangle getRectangle() {
		return new Rectangle(this.posX, this.posY, this.width, this.height);
	}

	public int setPosAndColor(String[] params, int origin) {
		int ix = origin;
		this.posY = NumberUtils.toInt(params[ix++]);
		this.posX = NumberUtils.toInt(params[ix++]);
		this.height = NumberUtils.toInt(params[ix++]);
		this.width = NumberUtils.toInt(params[ix++]);
		if (params.length <= ix) {
			return ix;
		}
		String col = params[ix];
		if (col.contains("'") || col.contains("\"")) {
			return ix;
		}
		this.color = col;
		ix++;
		if (params.length <= ix) {
			return ix;
		}
		this.bgColor = params[ix++];
		return ix;
	}

	public String getParent() {
		return this.parent;
	}
	public void setParent(String target) {
		this.parent = target;
	}
	public String getName() {
		return this.name;
	}
	public void setName(String name) {
		if (StringUtils.isBlank(name) || ".".equals(name)) {
			this.name = UUID.randomUUID().toString();
		} else {
			this.name = name;
		}
	}
	public String getCaption() {
		return this.caption;
	}
	public void setCaption(String caption) {
		this.caption = caption;
	}
	public int getWidth() {
		return this.width;
	}
	public int getHeight() {
		return this.height;
	}
	public String getColor() {
		return this.color;
	}
	public String getBgColor() {
		return this.bgColor;
	}
}
