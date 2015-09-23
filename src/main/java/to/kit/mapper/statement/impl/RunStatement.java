package to.kit.mapper.statement.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import to.kit.mapper.io.MapperTokenizer.LineInfo;
import to.kit.mapper.program.VariableManager;
import to.kit.mapper.statement.ProgramStatement;

/**
 * RUN.
 * @author Hidetaka Sasai
 */
public final class RunStatement extends ProgramStatement {
	/** 次に実行するRUN-ID. */
	private final String nextId;
	/** 引数(INPUT$に入る). */
	private List<String> vld = new ArrayList<>();

	/**
	 * インスタンスを生成.
	 * @param line 行
	 */
	public RunStatement(final LineInfo line) {
		super(line);
		String[] params = line.get(1).split(",");
		this.nextId = params[0];
		if (params.length < 2) {
			return;
		}
		List<String> list = Arrays.asList(Arrays.copyOfRange(params, 1, params.length));
		this.vld.addAll(list);
//		System.out.println("\t" + params.length + ":" + StringUtils.join(params, "|"));
	}

	@Override
	public ProgramStatement execute() {
		VariableManager var = VariableManager.getInstance();

		var.clearQueue();
		for (String param : this.vld) {
			var.queue(param);
		}
		return super.execute();
	}

	/**
	 * @return the nextId
	 */
	public String getNextId() {
		return this.nextId;
	}
}
