package to.kit.mapper.program;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import to.kit.mapper.io.MapperTokenizer;
import to.kit.mapper.io.MapperTokenizer.LineInfo;
import to.kit.mapper.statement.StatementUtils;

/**
 * プログラムパッケージ.
 * @author Hidetaka Sasai
 */
@Component
@PropertySource("classpath:mapper.properties")
public final class ProgramPackage extends HashMap<String, ProgramUnit> {
	/** Logger. */
	private static final Logger LOG = LoggerFactory.getLogger(ProgramPackage.class);
	/** RUN-ID. */
	private static final String KEY_RUN_ID = "run-id";
	/** プロパティ. */
	@Autowired
	private Environment env;

	private String format(final String filename) throws IOException {
		StringBuilder buff = new StringBuilder();

		try (InputStream stream = ProgramPackage.class.getResourceAsStream(filename);
				Reader in = new InputStreamReader(stream, this.env.getProperty("charset"))
				;BufferedReader reader =new BufferedReader(in)) {
			String line;
			int lf = 1;

			while ((line = reader.readLine()) != null) {
				line = line.replaceAll("@[\\s]+", "@");
				line = StringUtils.strip(line);
				boolean isDiscarded = line.startsWith(".") || line.startsWith("*") || line.startsWith("@.");
				boolean isContinues = line.endsWith("\\");

				if (!isDiscarded) {
					if (isContinues) {
						line = StringUtils.chop(line);
						lf++;
					}
					buff.append(line);
				}
				if (!isContinues) {
					buff.append(StringUtils.repeat('\n', lf));
					lf = 1;
				}
			}
		}
		String result = buff.toString();
		result = result.replace("'/'", "\\n");
		return result;
	}

	/**
	 * プログラムをロード.
	 * @param runId run-id
	 * @return プログラム
	 * @throws IOException 入出力例外
	 */
	private ProgramUnit load(final String runId) throws IOException {
		String filename = "/" + this.env.getProperty(runId);
		List<LineInfo> list;

		if (containsKey(runId)) {
			return get(runId);
		}
		LOG.debug("Load:" + filename);
		String buff = format(filename);

		try (Reader input = new StringReader(buff)) {
			MapperTokenizer tokenizer = new MapperTokenizer(input);
			list = tokenizer.getList();
		}
//System.out.println(StringUtils.join(list, "\n"));
		ProgramUnit unit = new ProgramUnit(filename);
		for (LineInfo line : list) {
			unit.addStatement(StatementUtils.getStatement(line));
		}
		put(runId, unit);
		return unit;
	}

	/**
	 * プログラムを実行.
	 * @throws IOException 入出力例外
	 */
	public void execute() throws IOException {
		String nextRunId = this.env.getProperty(KEY_RUN_ID);

		for (;;) {
			ProgramUnit unit = load(nextRunId);

			if (unit == null) {
				break;
			}
			LOG.info(nextRunId + "@" + unit.getSrc());
			nextRunId = unit.execute();
			if (nextRunId == null) {
				break;
			}
		}
	}
}
