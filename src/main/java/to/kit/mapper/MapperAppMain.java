package to.kit.mapper;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import to.kit.mapper.program.ProgramPackage;

/**
 * Main of application.
 * @author Hidetaka Sasai
 */
@Configuration
@ComponentScan
@Component
public class MapperAppMain {
	/** Logger. */
	private static final Logger LOG = LoggerFactory.getLogger(MapperAppMain.class);
	public static final int SCREEN_WIDTH = 1024;
	public static final int SCREEN_HEIGHT = 768;

	@Autowired
	private ProgramPackage pkg;

	private void execute() throws IOException {
		this.pkg.execute();
		LOG.debug("done.");
		System.exit(0);
	}

	/**
	 * Main.
	 * @param args arguments
	 * @throws Exception 例外
	 */
	public static void main(String[] args) throws Exception {
		try (AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(MapperAppMain.class)) {
			MapperAppMain app = context.getBean(MapperAppMain.class);

			app.execute();
		}
		System.exit(0);
	}
}
