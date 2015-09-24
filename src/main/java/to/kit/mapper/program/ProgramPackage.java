package to.kit.mapper.program;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.HashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

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
	/** ApplicationContext. */
	@Autowired
	private ApplicationContext context;

	/**
	 * プログラム単位を取得.
	 * @param runId run-id
	 * @return プログラム
	 * @throws IOException 入出力例外
	 */
	private ProgramUnit getProgramUnit(final String runId) throws IOException {
		if (!this.env.containsProperty(runId)) {
			return null;
		}
		if (containsKey(runId)) {
			return get(runId);
		}
		String filename = "/" + this.env.getProperty(runId);
		Charset charset = Charset.forName(this.env.getProperty("charset"));
		ProgramUnit unit = this.context.getBean(ProgramUnit.class);

		unit.load(filename, charset);
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
			ProgramUnit unit = getProgramUnit(nextRunId);

			if (unit == null) {
				LOG.info("END:{}", nextRunId);
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
