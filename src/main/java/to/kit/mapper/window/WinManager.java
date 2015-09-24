package to.kit.mapper.window;

import java.util.HashMap;

import org.apache.commons.lang3.StringUtils;

public final class WinManager extends HashMap<String, Win> {
	private Win latest;

	public WinDialog getLatest() {
		return (WinDialog) this.latest;
	}

	public WinDialog find(String name) {
		if (StringUtils.isBlank(name)) {
			return getLatest();
		}
		return (WinDialog) get(name);
	}

	@Override
	public Win get(Object key) {
		Win win = super.get(key);
		if (win != null) {
			this.latest = win;
		}
		return win;
	}
	@Override
	public Win put(String key, Win value) {
		this.latest = value;
		return super.put(key, value);
	}
}
