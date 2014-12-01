package zhku.jackcan.webCrawler;

import zhku.jackcan.webCrawler.exception.FetchTimeoutException;

public class CharsetTest {

	public static void main(String[] args) throws FetchTimeoutException {
		FetchUrl ft= FetchUrlFactory.getFetchurl();
		ft.setDecodeCharset("GBK");
		String html = ft.get("http://jw.zhku.edu.cn/jwmis/ZNPK/Private/List_JXL.aspx?w=150&id=3");
		System.out.println(html);
	}
}
