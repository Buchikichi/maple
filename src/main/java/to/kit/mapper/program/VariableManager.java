package to.kit.mapper.program;

import java.util.Deque;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;

public final class VariableManager extends HashMap<String, String> {
	/** 唯一のインスタンス. */
	public static final VariableManager ME = new VariableManager();
	/** 変数キュー(INPUT$). */
	private final Deque<Variable> variableQueue = new LinkedList<>();

	private VariableManager() {
		// nop
	}

	/**
	 * インスタンスを取得.
	 * @return インスタンス
	 */
	public static VariableManager getInstance() {
		return ME;
	}

	/**
	 * 何らかの値を取得.
	 * @param something 変数かどうかわからない何か
	 * @return 値
	 */
	public String getValue(final String something) {
		String value = StringUtils.defaultString(something);
		boolean isVariable = value.startsWith("<");
		Matcher m = Pattern.compile("[<][^<>]+[>]").matcher(value);
		Map<String, String> replaceMap = new HashMap<>();

		if (isVariable) {
			value = getPureName(something);
		}
		while (m.find()) {
			String target = m.group();

			replaceMap.put(target, StringUtils.defaultString(get(target)));
		}
		for (Map.Entry<String, String> entry : replaceMap.entrySet()) {
			value = value.replace(entry.getKey(), entry.getValue());
		}
		value = value.replace("'/'", "\n");
		if (value.startsWith("'")) {
			value = StringUtils.strip(value, "'");
		} else if (value.startsWith("\"")) {
			value = StringUtils.strip(value, "\"");
		} else if (value.startsWith("(")) {
			value = StringUtils.strip(value, "(");
		}
		return value;
	}

	/**
	 * 型が付いていない変数名を取得.
	 * @param name 変数名
	 * @return 変数名
	 */
	public String getPureName(final String name) {
		String pureName = StringUtils.defaultString(name);
		int ix = pureName.lastIndexOf('>');

		if (ix == -1) {
			return pureName;
		}
		return pureName.substring(0, ix + 1);
	}

	/**
	 * 変数を追加.
	 * @param var 変数名
	 */
	public void queue(String var) {
		String value = getValue(var);

		if (value != null) {
			this.variableQueue.add(new Variable(var, value));
		}
	}

	public void chg(String name) {
		Variable var = this.variableQueue.pollFirst();

		if (var != null) {
			put(name, var.getValue());
		}
	}

	/**
	 * キューをクリア.
	 */
	public void clearQueue() {
		this.variableQueue.clear();
	}
}
