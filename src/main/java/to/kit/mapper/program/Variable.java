package to.kit.mapper.program;

public final class Variable {
	private final String name;
	private final String value;

	public Variable(String name, String value) {
		this.name = name;
		this.value = value;
	}
	public String getName() {
		return this.name;
	}
	public String getValue() {
		return this.value;
	}
	@Override
	public String toString() {
		return this.name + "=" + this.value;
	}
}
