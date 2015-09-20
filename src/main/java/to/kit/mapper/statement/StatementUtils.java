package to.kit.mapper.statement;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.reflect.ConstructorUtils;

import to.kit.mapper.statement.impl.ElseStatement;
import to.kit.mapper.statement.impl.Nop;

public final class StatementUtils {
	private static final String STMT_PKG = ProgramStatement.class.getPackage().getName();

	private StatementUtils() {
		// nop
	}

	private static void addStatement(Deque<ProgramStatement> statementList, Class<? extends ProgramStatement> clazz, List<String> cmdList) {
		ProgramStatement last = statementList.peekLast();
		ProgramStatement stmt = makeStatement(clazz, cmdList);

		if (last instanceof Nop && stmt instanceof ElseStatement) {
			// elseの前のnopを除去
			statementList.removeLast();
		}
		statementList.add(stmt);
		cmdList.clear();
	}

	private static ProgramStatement makeStatement(Class<? extends ProgramStatement> clazz, List<String> cmdList) {
		ProgramStatement stmt = null;

		if (clazz == Nop.class) {
			return new Nop();
		} else if (clazz == ElseStatement.class) {
			return new ElseStatement();
		}
		String[] args = cmdList.toArray(new String[cmdList.size()]);
		try {
			stmt = ConstructorUtils.invokeConstructor(clazz, new Object[] { args });
		} catch (NoSuchMethodException | SecurityException | InstantiationException | IllegalAccessException
				| IllegalArgumentException | InvocationTargetException e) {
			// nop
			e.printStackTrace();
		}
		return stmt;
	}

	@SuppressWarnings("unchecked")
	private static Class<? extends ProgramStatement> getStatement(final String cmd) {
		if (".".equals(cmd)) {
			return Nop.class;
		}
		if (";".equals(cmd)) {
			return ElseStatement.class;
		}
		Class<? extends ProgramStatement> clazz = null;
		String simpleName = StringUtils.capitalize(cmd.toLowerCase());
		simpleName = simpleName.replace("+", "Plus");
		String className = STMT_PKG + ".impl." + simpleName + "Statement";

		try {
			clazz = (Class<ProgramStatement>) Class.forName(className);
		} catch (ClassNotFoundException | SecurityException | IllegalArgumentException e) {
			// nop
		}
		return clazz;
	}

	/**
	 * コマンドを分割する.
	 * @param line 行
	 * @return コマンドとパラメーター
	 */
	private static String[] split(String line) {
		List<String> list = new ArrayList<>();
		StringBuilder buff = new StringBuilder();
		char last = 0;
		char quote = 0;

		for (char c : line.toCharArray()) {
			boolean split = false;

			if (c == '\'' || c == '"') {
				if (quote == 0) {
					quote = c;
				} else {
					quote = 0;
				}
			} else if (c == ',' && quote == 0) {
				split = true;
			} else if (c == ' ' && quote == 0) {
				if (last == ' ') {
					continue;
				}
				split = true;
			}
			last = c;
			if (split) {
				list.add(buff.toString());
				buff = new StringBuilder();
				continue;
			}
			buff.append(c);
		}
		list.add(buff.toString());
		return list.toArray(new String[list.size()]);
	}

	public static Collection<ProgramStatement> getStatementList(String line) {
		Deque<ProgramStatement> statementList = new LinkedList<>();
		List<String> cmdList = new ArrayList<>();
		Class<? extends ProgramStatement> current = null;
		boolean isEdt = line.startsWith("EDT");

		for (String cmd : split(line)) {
			boolean pass = isEdt && !"EDT".equals(cmd) && !".".equals(cmd);
			Class<? extends ProgramStatement> clazz = null;

			if (!pass && cmd.matches("[A-Z]+[+]*|[.;]")) {
				clazz = getStatement(cmd);
				if (clazz != null) {
					if (current != null) {
						addStatement(statementList, current, cmdList);
					}
					current = clazz;
					continue;
				}
			}
			cmdList.add(cmd);
		}
		if (current != null) {
			addStatement(statementList, current, cmdList);
		}
		return statementList;
	}
}
