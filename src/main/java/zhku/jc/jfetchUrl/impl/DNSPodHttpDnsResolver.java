package zhku.jc.jfetchUrl.impl;

import org.apache.http.conn.DnsResolver;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.*;

/**
 * DNSPod 免费版 HttpDns
 * Created by liaojiacan on 2017/2/8.
 */
public class DNSPodHttpDnsResolver implements DnsResolver {

    private static  final String API ="http://119.29.29.29/d?dn=";

    @Override
    public InetAddress[] resolve(String s) throws UnknownHostException {

        HttpURLConnection conn = null;
        try{
        // 创建一个URL对象
        URL mURL = new URL(API+s);
        // 调用URL的openConnection()方法,获取HttpURLConnection对象
        conn = (HttpURLConnection) mURL.openConnection();

        conn.setRequestMethod("GET");// 设置请求方法为post
        conn.setReadTimeout(5000);// 设置读取超时为5秒
        conn.setConnectTimeout(10000);// 设置连接网络超时为10秒

        int responseCode = conn.getResponseCode();// 调用此方法就不必再使用conn.connect()方法
        if (responseCode == 200) {

            InputStream is = conn.getInputStream();
            String ipStr = getStringFromInputStream(is);
            String[] ips = ipStr.split(";");
            InetAddress[] array = new InetAddress[ips.length];
            for(int i =0 ;i<ips.length;i++){
                InetAddress inetAddress = InetAddress.getByName(ips[i]);
                array[i]= inetAddress;

            }
            return  array;

        } else {
            throw  new UnknownHostException(s);
        }

        } catch (MalformedURLException e) {
            throw  new UnknownHostException(s);
        } catch (ProtocolException e) {
            throw  new UnknownHostException(s);
        } catch (IOException e) {
            e.printStackTrace();
            throw  new UnknownHostException(s);
        } finally {
        if (conn != null) {
            conn.disconnect();// 关闭连接
        }
    }

    }

    /**
     * 根据流返回一个字符串信息         *
     * @param is
     * @return
     * @throws IOException
     */
    private static String getStringFromInputStream(InputStream is)
            throws IOException {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len = -1;

        while ((len = is.read(buffer)) != -1) {
            os.write(buffer, 0, len);
        }
        is.close();
        String state = os.toString();//
        os.close();
        return state;
    }
}
