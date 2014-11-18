package zhku.jackcan.webCrawler;

import java.util.List;
import java.util.Map;

import zhku.jackcan.webCrawler.exception.FetchTimeoutException;
import zhku.jackcan.webCrawler.impl.BinaryData;

public interface FetchUrl {
	  public abstract BinaryData getResource(String url) throws FetchTimeoutException;
	  public abstract BinaryData getResource(String url,String fileName) throws FetchTimeoutException;
	  public abstract BinaryData getResource(String url,int redoTimes) throws FetchTimeoutException;
	  public abstract BinaryData getResource(String url,String fileName,int redoTimes) throws FetchTimeoutException;
	  public abstract String get(String url) throws FetchTimeoutException;
	  public abstract String get(String url,int redoTimes) throws FetchTimeoutException;
	  public abstract String post(String url) throws FetchTimeoutException;
	  public abstract String post(String url,int redoTimes)throws FetchTimeoutException;
	  public abstract FetchUrl setDecodeCharset(String charset);
	  public abstract FetchUrl setRequestCharset(String charset);
	  public abstract FetchUrl setReadTimeout(int paramInt);
	  public abstract FetchUrl setSendTimeout(int paramInt);
	  public abstract FetchUrl setConnectTimeout(int paramInt);
	  public abstract FetchUrl setAllowRedirect(boolean paramBoolean);
	  public abstract FetchUrl setRedirectNum(int paramInt);
	  public abstract FetchUrl setCookie(String key, String value);
	  public abstract FetchUrl setCookies(String cookieString);
	  public abstract FetchUrl setCookies(Map<String, String> cookieMap);
	  public abstract FetchUrl setHeader(String name, String header);
	  public abstract FetchUrl setHttpAuth(String userName, String password);
	  public abstract FetchUrl setPostData(List<NameValuePair> paramList);
	  public abstract FetchUrl setPostData(Map<String,String> paramMap);
	  public abstract FetchUrl setPostData(List<NameValuePair> paramList, String paramString);
	  public abstract int getHttpCode();
	  public abstract String getResponseBody();
	  public abstract int getResponseBodyLen();
	  public abstract Map<String, String> getResponseHeader();
	  public abstract String getResponseHeaderString();
	  public abstract String getResponseCookies();
	  public abstract String getErrmsg();
	  public abstract BinaryData getResponseBinaryData();
	  public abstract FetchUrl reset();
}
