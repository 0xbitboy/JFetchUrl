package zhku.jackcan.webCrawler;

import zhku.jackcan.webCrawler.impl.FetchUrlImpl;

public class FetchUrlFactory {
	public static FetchUrl getFetchurl() {
		return new FetchUrlImpl();
	}
}
