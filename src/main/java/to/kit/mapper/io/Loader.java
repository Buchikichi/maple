package to.kit.mapper.io;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import to.kit.mapper.program.ProgramPackage;
import to.kit.mapper.program.ProgramUnit;
import to.kit.mapper.statement.ProgramStatement;
import to.kit.mapper.statement.StatementUtils;
import to.kit.mapper.statement.impl.LabelStatement;

/**
 * プログラムをロードする.
 * @author Hidetaka Sasai
 */
public final class Loader {
	/** Logger. */
	private static final Logger LOG = LoggerFactory.getLogger(Loader.class);
	/** RUN-ID. */
	private static final String SIG_RUN_ID = "*RUN-ID:";

	private ProgramUnit analyze(File file, List<String> lineList) {
		ProgramUnit unit = null;

		for (String str : lineList) {
			if (str.startsWith(SIG_RUN_ID)) {
				String runId = str.substring(SIG_RUN_ID.length());

				if (StringUtils.isBlank(runId)) {
					break;
				}
				unit = new ProgramUnit(file, runId);
				continue;
			}
			if (str.startsWith(".") || str.startsWith("@.") || str.startsWith("*")) {
				continue;
			}
			if (!str.startsWith("@")) {
				// ただの表示コマンド(?)
//				System.out.println("???" + str);
				continue;
			}
			if (unit == null) {
				break;
			}
			String line = str.substring(1).trim();
			if (line.matches("^[0-9]+[:\\s].*$")) {
				int ix = line.indexOf(':');

				if (ix == -1) {
					ix = line.indexOf('\t');
					if (ix == -1) {
						ix = line.indexOf(' ');
					}
				}
				String label = line.substring(0, ix);

				unit.addStatement(new LabelStatement(label));
				line = line.substring(ix + 1).trim();
			}
			if (StringUtils.isBlank(line) || line.startsWith(".")) {
				continue;
			}
			Collection<ProgramStatement> stmtList = StatementUtils.getStatementList(line);

			for (ProgramStatement stmt : stmtList) {
				unit.addStatement(stmt);
			}
		}
		return unit;
	}

	private List<String> load(File file) throws IOException {
		List<StringBuilder> buffList = new ArrayList<>();

		try (InputStream stream = new FileInputStream(file);
				Reader input = new InputStreamReader(stream, "MS932")) {
			StringBuilder buff = null;
			for (Object obj : IOUtils.readLines(input)) {
				String line = String.valueOf(obj).trim();
				boolean isContinues = line.endsWith("\\");

				if (StringUtils.isBlank(line)) {
					continue;
				}
				line = line.replaceAll("[\\s]+,", "");
				if (isContinues) {
					int endIndex = line.length() - 1;

					line = line.substring(0, endIndex);
				}
				if (buff == null) {
					buff = new StringBuilder(line);
					buffList.add(buff);
				} else {
					buff.append(line);
				}
				if (!isContinues) {
					buff = null;
				}
			}
		}
		// refill
		List<String> list = new ArrayList<>();
		for (StringBuilder buff : buffList) {
			list.add(buff.toString());
		}
		return list;
	}

	public ProgramPackage load(final String name) throws IOException {
		ProgramPackage pkg = new ProgramPackage();
		List<URL> urlList = Collections.list(Loader.class.getClassLoader().getResources(name));

		for (URL url : urlList) {
			try {
				for (File file : new File(url.toURI()).listFiles()) {
					if (!file.isFile()) {
						continue;
					}
					LOG.debug("\"{}\"", file.getName());
					List<String> lineList = load(file);
					ProgramUnit unit = analyze(file, lineList);

					if (unit == null || StringUtils.isBlank(unit.getRunId()) || pkg.containsKey(unit.getRunId())) {
						//System.out.println("*BAD runId");
						continue;
					}
					pkg.put(unit.getRunId(), unit);
				}
			} catch (URISyntaxException e) {
				// nop
			}
		}
		return pkg;
	}
}
