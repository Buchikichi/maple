package to.kit.mapper.statement;

import java.lang.reflect.InvocationTargetException;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.reflect.ConstructorUtils;

import to.kit.mapper.io.MapperTokenizer.LineInfo;
import to.kit.mapper.statement.impl.ElseStatement;
import to.kit.mapper.statement.impl.LabelStatement;
import to.kit.mapper.statement.impl.RunStatement;

public final class StatementUtils {
	private static final String STMT_PKG = RunStatement.class.getPackage().getName();

	private StatementUtils() {
		// nop
	}

	@SuppressWarnings("unchecked")
	public static ProgramStatement getStatement(final LineInfo line) {
		if (line.isLabel()) {
			return new LabelStatement(line);
		}
		String cmd = line.getCommand();
		if (";".equals(cmd)) {
			return new ElseStatement(line);
		}
		String simpleName = StringUtils.capitalize(cmd.toLowerCase());
		simpleName = simpleName.replace("+", "Plus");
		String className = STMT_PKG + '.' + simpleName + "Statement";
		Class<? extends ProgramStatement> clazz = null;

		try {
			clazz = (Class<ProgramStatement>) Class.forName(className);
		} catch (ClassNotFoundException | SecurityException | IllegalArgumentException e) {
			// nop
			e.printStackTrace();
		}
		ProgramStatement stmt = null;

		try {
			stmt = ConstructorUtils.invokeConstructor(clazz, new Object[] { line });
		} catch (NoSuchMethodException | SecurityException | InstantiationException | IllegalAccessException
				| IllegalArgumentException | InvocationTargetException e) {
			// nop
			e.printStackTrace();
		}
		return stmt;
	}
}
