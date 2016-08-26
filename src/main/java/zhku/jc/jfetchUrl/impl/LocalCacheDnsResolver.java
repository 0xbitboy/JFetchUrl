package zhku.jc.jfetchUrl.impl;

import org.apache.http.conn.DnsResolver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

/**
 * 本地缓存dns 解析
 * Created by liaojiacan on 2016/8/26.
 */
public class LocalCacheDnsResolver implements DnsResolver {

    private final static Map<String, ExpireInetAddresses> MAPPINGS =  new ConcurrentHashMap<String, ExpireInetAddresses>();
    private final static Map<String, Long> expires =  new ConcurrentHashMap<String, Long>();
    private long expireMillis = TimeUnit.MINUTES.toMillis(1); //默认一分钟失效

    private static Logger logger = LoggerFactory.getLogger(LocalCacheDnsResolver.class);

    public LocalCacheDnsResolver() {
    }

    public LocalCacheDnsResolver(long expireMillis) {
        this.expireMillis = expireMillis;
    }

    @Override
    public InetAddress[] resolve(String host) throws UnknownHostException {
        Date now = new Date();
        ExpireInetAddresses result;
        if(MAPPINGS.containsKey(host)){
            ExpireInetAddresses expInetAddress = MAPPINGS.get(host);
            if(expInetAddress.isExpire(now,getExpireMillis())){
                try{
                    result = addResolve(host, now.getTime());
                }catch (UnknownHostException e){
                    logger.warn("host:" + host + "，dns couldn't resolver. return a expire result.");
                    result = expInetAddress;
                }
            }else{
                result = expInetAddress;
            }
        }else{
            result =  addResolve(host,now.getTime());
        }

        if(logger.isDebugEnabled()){
            StringBuilder sb = new StringBuilder();
            sb.append("host:").append(host).append("[");
            for(InetAddress address : result.getInetAddresses()){
                sb.append(address.getHostAddress()).append(",");
            }
            sb.deleteCharAt(sb.length()-1).append("]");
            logger.debug(sb.toString());
        }
        return result.getInetAddresses();
    }

    private ExpireInetAddresses addResolve(String host,long  nowtime) throws UnknownHostException {
        InetAddress[] addresses= InetAddress.getAllByName(host);
        MAPPINGS.put(host, new ExpireInetAddresses(nowtime+getExpireMillis(),addresses));

        if(logger.isDebugEnabled()){
            StringBuilder sb = new StringBuilder();
            sb.append("add or update dns ==> host:").append(host).append("[");
            for(InetAddress address : addresses){
                sb.append(address.getHostAddress()).append(",");
            }
            sb.deleteCharAt(sb.length()-1).append("]");
            logger.debug(sb.toString());
        }

        return MAPPINGS.get(host);
    }

    public long getExpireMillis() {
        return expireMillis;
    }

    public void setExpireMillis(long expireMillis) {
        this.expireMillis = expireMillis;
    }


    class ExpireInetAddresses{
        private long expires;
        private InetAddress[] inetAddresses;

        public ExpireInetAddresses() {
        }

        public ExpireInetAddresses(long expires, InetAddress[] inetAddresses) {
            this.expires = expires;
            this.inetAddresses = inetAddresses;
        }

        public long getExpires() {
            return expires;
        }

        public void setExpires(long expires) {
            this.expires = expires;
        }

        public InetAddress[] getInetAddresses() {
            return inetAddresses;
        }

        public void setInetAddresses(InetAddress[] inetAddresses) {
            this.inetAddresses = inetAddresses;
        }

        public  boolean isExpire(Date now ,Long expireMillis){

            if(now ==null){
                now = new Date();
            }
            return  now.getTime()<expireMillis+expires;
        }


    }


}
