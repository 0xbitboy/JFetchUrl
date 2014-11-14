package zhku.jackcan.webCrawler;

import java.util.List;
import java.util.Map;

import zhku.jackcan.webCrawler.exception.FetchTimeoutException;
import zhku.jackcan.webCrawler.impl.BinaryData;

public interface FetchUrl {
	  public abstract byte[] getResource(String url);
	  public abstract byte[] getResource(String url,int redoTimes) throws FetchTimeoutException;
	  public abstract String get(String url);
	  public abstract String get(String url,int redoTimes) throws FetchTimeoutException;
	  public abstract String post(String url);
	  public abstract String post(String url,int redoTimes)throws FetchTimeoutException;
	  public abstract String fetch(String url, String method);
	  public abstract void setDecodeCharset(String charset);
	  public abstract void setRequestCharset(String charset);
	  public abstract void setReadTimeout(int paramInt);
	  public abstract void setSendTimeout(int paramInt);
	  public abstract void setConnectTimeout(int paramInt);
	  public abstract void setAllowRedirect(boolean paramBoolean);
	  public abstract void setRedirectNum(int paramInt);
	  public abstract void setCookie(String key, String value);
	  public abstract void setCookies(String cookieString);
	  public abstract void setCookies(Map<String, String> cookieMap);
	  public abstract void setHeader(String name, String header);
	  public abstract void setHttpAuth(String userName, String password);
	  public abstract void setPostData(List<NameValuePair> paramList);
	  public abstract void setPostData(Map<String,String> paramMap);
	  public abstract void setPostData(List<NameValuePair> paramList, String paramString);
	  public abstract int getHttpCode();
	  public abstract String getResponseBody();
	  public abstract int getResponseBodyLen();
	  public abstract Map<String, String> getResponseHeader();
	  public abstract String getResponseHeaderString();
	  public abstract String getResponseCookies();
	  public abstract String getErrmsg();
	  public abstract BinaryData getResponseBinaryData();
	  public abstract void reset();
}
