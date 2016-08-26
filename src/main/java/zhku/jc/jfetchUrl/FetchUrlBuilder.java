package zhku.jc.jfetchUrl;

import org.apache.http.conn.DnsResolver;
import zhku.jc.jfetchUrl.impl.FetchUrlImpl;

/**
 * Created by liaojiacan on 2016/8/26.
 */
public class FetchUrlBuilder {


    private DnsResolver dnsResolver;

    public FetchUrlBuilder(){

    }

    public FetchUrlBuilder( DnsResolver dnsResolver){
        this.dnsResolver=dnsResolver;
    }

    public FetchUrl build(){
        if(dnsResolver!=null){
            return new FetchUrlImpl(getDnsResolver());
        }

        return new  FetchUrlImpl();
    }

    public DnsResolver getDnsResolver() {
        return dnsResolver;
    }

    public FetchUrlBuilder setDnsResolver(DnsResolver dnsResolver) {
        this.dnsResolver = dnsResolver;
        return this;
    }


}
