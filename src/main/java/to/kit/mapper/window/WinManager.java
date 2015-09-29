package to.kit.mapper.window;

import java.util.HashMap;

/**
 * Winマネージャー.
 * @author Hidetaka Sasai
 */
public final class WinManager extends HashMap<String, Win> {
	private Win latest;

	public WinDialog getLatest() {
		return (WinDialog) this.latest;
	}

	public WinDialog scan(String name) {
		WinDialog result = null;

		for (Entry<String, Win> entry : entrySet()) {
			WinDialog dialog = (WinDialog) entry.getValue();

			if (dialog.getComponent(name) != null) {
				result = dialog;
				break;
			}
		}
		return result;
	}

	public WinDialog find(String... targets) {
		WinDialog win = null;

		for (String target : targets) {
			win = (WinDialog) get(target);
			if (win != null) {
				break;
			}
			win = scan(target);
			if (win != null) {
				break;
			}
		}
		if (win == null) {
			win = getLatest();
		}
		return win;
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
