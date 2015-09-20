package to.kit.mapper.program;

import java.util.HashMap;

public final class ProgramPackage extends HashMap<String, ProgramUnit> {
	/**
	 * プログラムを実行する.
	 * @param runId RUN-ID
	 */
	public void execute(final String runId) {
		String nextRunId = runId;

		for(;;) {
			if (!containsKey(nextRunId)) {
				break;
			}
			nextRunId = get(nextRunId).execute();
		}
	}

	@Override
	public ProgramUnit put(String key, ProgramUnit unit) {
		return super.put(key, unit);
	}
}
