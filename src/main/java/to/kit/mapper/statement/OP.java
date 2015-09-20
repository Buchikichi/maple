package to.kit.mapper.statement;

import java.util.function.BiPredicate;

public enum OP {
	EQ("=", (l, r) -> l.equals(r)),
	NE("<>", (l, r) -> !l.equals(r)),
	LT("<", (l, r) -> l.compareTo(r) < 0),
	LE("<=", (l, r) -> l.compareTo(r) <= 0),
	GT(">", (l, r) -> r.compareTo(l) < 0),
	GE(">=", (l, r) -> r.compareTo(l) <= 0),
	NOT("!", (l, r) -> false);

	private String symbol;
	private BiPredicate<String, String> func;

	OP(String symbol, BiPredicate<String, String> func) {
		this.symbol = symbol;
		this.func = func;
	}
	public static OP get(String value) {
		OP result = null;
		for (OP op : values()) {
			if (op.equals(value)) {
				result = op;
				break;
			}
		}
		return result;
	}
	public boolean equals(String value) {
		if (toString().equals(value)) {
			return true;
		}
		return this.symbol.equals(value);
	}
	public boolean satisfy(String l, String r) {
		return this.func.test(l, r);
	}
}
