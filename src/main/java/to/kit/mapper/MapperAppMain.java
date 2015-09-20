package to.kit.mapper;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import to.kit.mapper.io.Loader;
import to.kit.mapper.program.ProgramPackage;

public final class MapperAppMain {
	/** Logger. */
	private static final Logger LOG = LoggerFactory.getLogger(MapperAppMain.class);
	public static final int SCREEN_WIDTH = 1024;
	public static final int SCREEN_HEIGHT = 768;

	private void execute(String[] args) throws IOException {
		Loader loader = new Loader();
		ProgramPackage pkg = loader.load(".");
		String runId = args[0];

		pkg.execute(runId);
		LOG.debug("done.");
		System.exit(0);
	}

	/**
	 * Main.
	 * @param args arguments
	 * @throws Exception 例外
	 */
	public static void main(String[] args) throws Exception {
		if (args.length < 1) {
			return;
		}
		MapperAppMain app = new MapperAppMain();

		app.execute(args);
	}
}
