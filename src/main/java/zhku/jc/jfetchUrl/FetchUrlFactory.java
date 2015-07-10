package zhku.jc.jfetchUrl;

import zhku.jc.jfetchUrl.impl.FetchUrlImpl;

public class FetchUrlFactory {
	public static FetchUrl getFetchurl() {
		return new FetchUrlImpl();
	}
}
