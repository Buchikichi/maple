package to.kit.mapper.io;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;
import java.nio.charset.Charset;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import to.kit.mapper.io.MapperTokenizer.LineInfo;
import to.kit.mapper.program.ProgramPackage;

/**
 * プログラムをロードする.
 * @author Hidetaka Sasai
 */
@Component
public final class Loader  {
	private String format(final String filename, final Charset charset) throws IOException {
		StringBuilder buff = new StringBuilder();

		try (InputStream stream = ProgramPackage.class.getResourceAsStream(filename);
				Reader in = new InputStreamReader(stream, charset);
				BufferedReader reader = new BufferedReader(in)) {
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
	 * プログラムをロードする.
	 * @param filename ファイル名
	 * @param charset Charset
	 * @return プログラム
	 * @throws IOException 入出力例外
	 */
	public List<LineInfo> load(final String filename, final Charset charset) throws IOException {
		String buff = format(filename, charset);
		List<LineInfo> list;

		try (Reader input = new StringReader(buff)) {
			MapperTokenizer tokenizer = new MapperTokenizer(input);
			list = tokenizer.getList();
		}
		return list;
	}
}
