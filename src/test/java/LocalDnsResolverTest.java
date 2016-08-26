import junit.framework.TestCase;
import org.apache.http.conn.DnsResolver;
import zhku.jc.jfetchUrl.FetchUrl;
import zhku.jc.jfetchUrl.FetchUrlBuilder;
import zhku.jc.jfetchUrl.impl.LocalCacheDnsResolver;

import java.util.concurrent.TimeUnit;

/**
 * Created by liaojiacan on 2016/8/26.
 */
public class LocalDnsResolverTest  extends TestCase{

    public void testDdns()throws Exception {

        FetchUrl fetchUrl = new FetchUrlBuilder(new LocalCacheDnsResolver(TimeUnit.MINUTES.toMillis(30))).build();
        fetchUrl.get("http://baidu.com");
        System.out.println(fetchUrl.getResponseBody());
    }

}
