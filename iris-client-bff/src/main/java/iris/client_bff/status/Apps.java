package iris.client_bff.status;

import iris.client_bff.status.Apps.App;

import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import org.springframework.data.util.Streamable;

/**
 * @author Jens Kutzsche
 */
public record Apps(List<App> apps) implements Streamable<App> {

	@Override
	public Iterator<App> iterator() {
		return apps.iterator();
	}

	public Apps sort() {

		apps.sort(Comparator.comparing(App::name, String.CASE_INSENSITIVE_ORDER));

		return this;
	}

	public record App(String name) {}
}
