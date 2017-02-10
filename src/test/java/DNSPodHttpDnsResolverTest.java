import junit.framework.TestCase;
import zhku.jc.jfetchUrl.FetchUrl;
import zhku.jc.jfetchUrl.FetchUrlBuilder;
import zhku.jc.jfetchUrl.exception.FetchTimeoutException;
import zhku.jc.jfetchUrl.impl.DNSPodHttpDnsResolver;
import zhku.jc.jfetchUrl.impl.LocalCacheDnsResolver;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Created by liaojiacan on 2017/2/8.
 */
public class DNSPodHttpDnsResolverTest extends TestCase {

    public void testGet() throws FetchTimeoutException {

        FetchUrl fetchUrl = new FetchUrlBuilder(new DNSPodHttpDnsResolver()).build();
        fetchUrl.get("http://gitbub.com");
        System.out.println(fetchUrl.getResponseBody());
    }


}
