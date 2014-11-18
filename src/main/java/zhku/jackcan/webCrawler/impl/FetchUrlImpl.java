package zhku.jackcan.webCrawler.impl;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpHead;
import org.apache.http.client.methods.HttpOptions;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.methods.HttpTrace;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.ByteArrayBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.util.EntityUtils;

import sun.misc.BASE64Encoder;
import zhku.jackcan.webCrawler.BasicNameValuePair;
import zhku.jackcan.webCrawler.BinaryNameValuePair;
import zhku.jackcan.webCrawler.FetchUrl;
import zhku.jackcan.webCrawler.NameValuePair;
import zhku.jackcan.webCrawler.exception.FetchTimeoutException;

public class FetchUrlImpl implements FetchUrl {
	private static final String GET = "GET";
	private static final String POST = "POST";
	private static final String DELETE = "DELETE";
	private static final String PUT = "PUT";
	private static final String HEAD = "HEAD";
	private static final String OPTIONS = "OPTIONS";
	private static final String TRACE = "TRACE";
	private static final int CONNECT_TIMEOUT = 5000;
	private static final int SENT_TIMEOUT = 20000;
	private static final int READ_TIMEOUT = 20000;
	private static final int MAX_REDIRECT_NUM = 5;
	private static Logger logger = Logger.getLogger(FetchUrlImpl.class.getName());
	private String method = "GET";
	private String decodeCharset = null;
	private HttpClient httpclient = null;
	private Map<String, String> headers = new HashMap<String,String>();
	private HttpRequestBase http;
	private Map<String, String> addHttpHeaders = new HashMap<String,String>();
	private List<NameValuePair> postDataList = new ArrayList<NameValuePair>();
	private boolean isPost = false;
	private int httpStatusCode = 200;
	private String statusMsg = "";
	private String body = "";
	private String characterEncode = "UTF-8";
	private String header_str = "";
	private String scheme = "http";
	private BinaryData responseBinaryData=null;
	public FetchUrlImpl() {
		 //多线程支持
		 ClientConnectionManager conMgr =new ThreadSafeClientConnManager();
		 httpclient = new DefaultHttpClient(conMgr);
		 init();
	}

	@Override
	public String get(String url) throws FetchTimeoutException {
		this.method = "GET";
		return fetch(url, "GET",false,1);
	}

	@Override
	public String post(String url) throws FetchTimeoutException {
		this.method = "POST";
		return fetch(url, "POST",false,1);
	}
	private String fetch(String url, String method,boolean isResource ,int redoTimes) throws FetchTimeoutException {
		HttpEntity entity = null;
		try {
			if (null == method)
				this.method = "GET";
			else {
				this.method = method;
			}
			int flg = url.indexOf(":");
			if ((flg != 4) && (flg != 5)) {
				logger.warning("The protocol of this url is not http or https!!! The correct url example is 'http://www.baidu.com'");
			}
			String url_head = url.split(":")[0];
			String url_body = url.split(":")[1];
			if ("http".equals(url_head.toLowerCase())) {
				this.scheme = "http";
			} else if ("https".equals(url_head.toLowerCase())) {
				this.scheme = "https";
				url = new StringBuilder().append("http:").append(url_body).toString();
			} else {
				logger.warning("The protocol of this url is not http or https!!! The correct url example is 'http://www.baidu.com'");
			}
			this.http = choiceHttpMethod(this.method, url);

			genHttpHeader(this.http, url);
			genAddHttpHeader(this.http);
			if (this.isPost) {
				genPostRequest(this.http);
			}

			if (logger.isLoggable(Level.FINE)) {
				logger.fine(new StringBuilder().append("request url = ").append(this.http.getRequestLine().getUri()).toString());
				logger.fine(new StringBuilder().append("request method = ").append(this.http.getRequestLine().getMethod()).toString());
				logger.fine(new StringBuilder().append("request http version = ").append(this.http.getRequestLine().getProtocolVersion()).toString());
				Header[] headers = this.http.getAllHeaders();
				logger.fine("request headers:");
				Header[] arr = headers;
				int len = arr.length;

				for (int i = 0; i < len; i++) {
					Header h = arr[i];
					logger.fine(new StringBuilder().append(h.getName()).append(" : ").append(h.getValue()).toString());
				}

			}
			/***重试N次***/
			HttpResponse response =null;
			if(redoTimes<0) redoTimes =1;
			int times =1;
			while(response==null||response.getStatusLine().getStatusCode()>=500){
				if(times>redoTimes){
					break;
				}
				//释放之前的链接请求
				if(this.http!=null){
					this.http.releaseConnection();
				}
				try{
					response= this.httpclient.execute(this.http);
				}catch(Exception e){
					if(times>redoTimes){
						throw new FetchTimeoutException();
					}
				}
				times++;
			}
			parseResponseStatus(response);
			Header[] responseHeaders = response.getAllHeaders();
			Header[] arr = responseHeaders;
			int len = arr.length;
			this.header_str = "";
			this.headers = new HashMap();
			for (int i = 0; i < len; i++) {
				Header h = arr[i];
				this.headers.put(h.getName(), h.getValue());
				this.header_str = new StringBuilder().append(this.header_str).append(h.getName()).append(":").append(h.getValue()).append(";").toString();
				if (logger.isLoggable(Level.FINE)) {
					logger.fine(new StringBuilder().append(h.getName()).append(":").append(h.getValue()).toString());
				}
			}
			entity = response.getEntity();
			if(isResource){
				String contentType= headers.get("Content-Type");
				String suffix=null;
				if(contentType!=null){
					contentType=contentType.split(";")[0];
					suffix = contentType.split("/")[1];
				}
				if (contentType!=null) {
					String tempName = url.substring(url.lastIndexOf('/')+1, url.length());
					if(!tempName.endsWith(suffix)){
						tempName =new StringBuffer().append(tempName.replaceAll("\\.[\\s\\S]+", "")).append(".").append(suffix).toString();
					}
					this.responseBinaryData = new BinaryData(tempName,  EntityUtils.toByteArray(entity));
					return tempName;
				} 
			}else if (decodeCharset == null) {
				this.body = EntityUtils.toString(entity, this.characterEncode);
			} else {
				this.body = new String(EntityUtils.toByteArray(entity), decodeCharset);
			}
			return this.body;
		}catch(FetchTimeoutException fetche){
			throw fetche;
		} catch (Exception e) {
			StackTraceElement[] ste = e.getStackTrace();
			String ex_str = new StringBuilder().append("").append(e).append("\n").toString();
			for (int i = 0; i < ste.length; i++) {
				ex_str = new StringBuilder().append(ex_str).append(ste[i].toString()).append("\n").toString();
			}
			logger.warning(new StringBuilder().append("fetch failure.StackTrace : {[(\n").append(ex_str).append("\n)]}").toString());
			logger.warning("请求错误，可能是超时---->");
		} finally {
			try {
				InputStream in;
				if (entity != null) {
					in = entity.getContent();
					if (in != null)
						in.close();
				}
			} catch (IllegalStateException e) {
			} catch (IOException e) {
			}
		}
		return null;
	}

	private void parseResponseStatus(HttpResponse response) {
		StatusLine line = response.getStatusLine();
		int statusCode = line.getStatusCode();
		String reasonPhrase = line.getReasonPhrase();
		this.httpStatusCode = statusCode;
		if ("OK".equals(reasonPhrase))
			this.statusMsg = "OK!";
		else
			this.statusMsg = reasonPhrase;
	}

	private void genPostRequest(HttpRequestBase httpBase) {
		if ((httpBase instanceof HttpPost)) {
			if ((null == this.characterEncode) || ("".equals(this.characterEncode)))
				this.characterEncode = "UTF-8";
			try {
				HttpPost post = (HttpPost) httpBase;

				boolean flg = false;
				if ((this.postDataList != null) && (this.postDataList.size() > 0)) {
					int len = this.postDataList.size();
					for (int i = 0; (i < len) && (!flg); i++) {
						if ((this.postDataList.get(i) instanceof BinaryNameValuePair)) {
							flg = true;
							break;
						}
					}
					if (flg) {
						MultipartEntity entity = new MultipartEntity();
						Iterator postIter = this.postDataList.iterator();
						while (postIter.hasNext()) {
							NameValuePair nvp = (NameValuePair) postIter.next();
							if ((nvp instanceof BasicNameValuePair)) {
								entity.addPart(nvp.getName(), new StringBody((String) nvp.getValue(), Charset.forName(this.characterEncode)));
							} else if ((nvp instanceof BinaryNameValuePair)) {
								BinaryData bd = ((BinaryNameValuePair) nvp).getValue();
								if (bd != null) {
									ByteArrayBody bab = new ByteArrayBody(bd.getData(), bd.getFileName());
									entity.addPart(nvp.getName(), bab);
								}
							}
						}
						post.setEntity(entity);
					} else {
						List nvps = new ArrayList();
						Iterator postIter = this.postDataList.iterator();
						while (postIter.hasNext()) {
							NameValuePair nvp = (NameValuePair) postIter.next();
							if ((nvp instanceof BasicNameValuePair)) {
								nvps.add(new org.apache.http.message.BasicNameValuePair(nvp.getName(), (String) nvp.getValue()));
							}
						}
						post.setEntity(new UrlEncodedFormEntity(nvps, this.characterEncode));
					}
				}
			} catch (Exception e) {
				StackTraceElement[] ste = e.getStackTrace();
				String ex_str = new StringBuilder().append("").append(e).append("\n").toString();
				for (int i = 0; i < ste.length; i++) {
					ex_str = new StringBuilder().append(ex_str).append(ste[i].toString()).append("\n").toString();
				}
				logger.warning(new StringBuilder().append("gen post request header failure.StackTrace : {[(\n").append(ex_str).append("\n)]}").toString());
			}
		} else {
			logger.warning("current http method not post!");
		}
	}

	private void genAddHttpHeader(HttpRequestBase http) {
		Iterator iter = this.addHttpHeaders.keySet().iterator();
		while (iter.hasNext()) {
			String name = (String) iter.next();
			String value = (String) this.addHttpHeaders.get(name);
			http.setHeader(name, value);
		}
	}

	private HttpRequestBase choiceHttpMethod(String method, String url) {
		String httpMethod = method.toUpperCase();
		if ("GET".equals(httpMethod))
			return new HttpGet(url);
		if ("POST".equals(httpMethod))
			return new HttpPost(url);
		if ("DELETE".equals(httpMethod))
			return new HttpDelete(url);
		if ("PUT".equals(httpMethod))
			return new HttpPut(url);
		if ("HEAD".equals(httpMethod))
			return new HttpHead(url);
		if ("OPTIONS".equals(httpMethod))
			return new HttpOptions(url);
		if ("TRACE".equals(httpMethod)) {
			return new HttpTrace(url);
		}
		return new HttpGet(url);
	}

	private void genHttpHeader(HttpRequestBase http, String url) {
		// 模拟google爬虫
		//http.setHeader("User-Agent", "Mozilla/5.0 (compatible; Googlebot/2.1; +http://www.google.com/bot.html)");
		//猎豹浏览器
		http.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.3; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/34.0.1847.137 Safari/537.36 LBBROWSER");
	}

	@Override
	public FetchUrl setReadTimeout(int timeout) {
		if ((timeout > 20000) || (timeout < 1))
			timeout = 20000;
		this.httpclient.getParams().setParameter("http.socket.timeout", Integer.valueOf(timeout));
		return this;
	}

	@Override
	public FetchUrl setSendTimeout(int timeout) {
		if ((timeout > 20000) || (timeout < 1))
			timeout = 20000;
		this.httpclient.getParams().setParameter("http.socket.timeout", Integer.valueOf(timeout));
		return this;
	}

	@Override
	public FetchUrl setConnectTimeout(int timeout) {
		if ((timeout > 5000) || (timeout < 1))
			timeout = 5000;
		this.httpclient.getParams().setParameter("http.connection.timeout", Integer.valueOf(timeout));
		return this;
	}

	@Override
	public FetchUrl setAllowRedirect(boolean allow) {
		this.httpclient.getParams().setParameter("http.protocol.handle-redirects", Boolean.valueOf(allow));
		return this;

	}

	@Override
	public FetchUrl setRedirectNum(int num) {
		if ((num < 0) || (num > 5)) {
			num = 5;
		}
		this.httpclient.getParams().setParameter("http.protocol.max-redirects", Integer.valueOf(num));
		return this;
	}

	@Override
	public FetchUrl setCookie(String name, String value) {
		this.addHttpHeaders.put("Cookie", new StringBuilder().append(name).append("=").append(value).append(";").toString());
		return this;
	}

	@Override
	public FetchUrl setCookies(Map<String, String> maps) {
		Iterator iter = maps.keySet().iterator();
		StringBuilder cookieResult = new StringBuilder();
		while (iter.hasNext()) {
			String cookieName = (String) iter.next();
			String cookieValue = (String) maps.get(cookieName);
			cookieResult.append(cookieName).append("=").append(cookieValue).append(";");
		}

		if (cookieResult.length() > 0) {
			cookieResult.deleteCharAt(cookieResult.length() - 1);
		}
		this.addHttpHeaders.put("Cookie", cookieResult.toString());
		return this;
	}

	@Override
	public FetchUrl setHeader(String name, String value) {
		String trimName = name.trim();
		this.addHttpHeaders.put(trimName, value);
		return this;
	}

	@Override
	public FetchUrl setHttpAuth(String username, String password) {
		String content = new StringBuilder().append(username).append(":").append(password).toString();
		String encodeContent = new BASE64Encoder().encode(content.getBytes());
		this.addHttpHeaders.put("Authorization", new StringBuilder().append("Basic ").append(encodeContent).toString());
		return this;
	}

	@Override
	public FetchUrl setPostData(List<NameValuePair> postDataList) {
		setPostData(postDataList, this.characterEncode);
		return this;
	}

	@Override
	public FetchUrl setPostData(List<NameValuePair> postDataList, String characterEncode) {
		this.method = "POST";
		this.postDataList = postDataList;
		this.isPost = true;
		this.characterEncode = characterEncode;
		if (logger.isLoggable(Level.FINE))
			logger.fine(new StringBuilder().append("post data:").append(postDataList).toString());
		return this;
	}

	@Override
	public int getHttpCode() {
		return this.httpStatusCode;
	}

	@Override
	public String getResponseBody() {
		return this.body;
	}

	@Override
	public int getResponseBodyLen() {
		if (this.body != null) {
			return this.body.length();
		}
		return 0;
	}

	@Override
	public Map<String, String> getResponseHeader() {
		return new HashMap<String, String>(this.headers);
	}

	@Override
	public String getResponseHeaderString() {
		return this.header_str;
	}

	@Override
	public String getResponseCookies() {
		return (String) this.headers.get("Set-Cookie");
	}

	@Override
	public String getErrmsg() {
		return this.statusMsg;
	}

	private void init() {
		this.httpclient.getParams().setParameter("http.connection.timeout", Integer.valueOf(5000));
		this.httpclient.getParams().setParameter("http.socket.timeout", Integer.valueOf(20000));
		this.httpclient.getParams().setParameter("http.protocol.handle-redirects", Boolean.valueOf(true));
		this.httpclient.getParams().setParameter("http.protocol.max-redirects", Integer.valueOf(5));
	}

	@Override
	public FetchUrl reset() {
		this.httpclient = new DefaultHttpClient();
		init();
		this.http = null;
		if (this.headers != null)
			this.headers = new HashMap<String,String>();
		if (this.addHttpHeaders != null)
			this.addHttpHeaders =new HashMap<String,String>();
		this.isPost = false;
		if (this.postDataList != null) {
			this.postDataList = new ArrayList<NameValuePair>();
		}
		this.method = "GET";
		this.httpStatusCode = 200;
		this.characterEncode = "UTF-8";
		decodeCharset = null;
		this.statusMsg = "";
		this.body = "";
		this.header_str = "";
		return this;
	}
	@Override
	public BinaryData getResponseBinaryData() {
		return this.responseBinaryData;
	}

	@Override
	public FetchUrl setCookies(String cookieString) {
		this.addHttpHeaders.put("Cookie", cookieString);
		return this;
	}
	@Override
	public FetchUrl setDecodeCharset(String charset) {
		this.decodeCharset = charset;
		return this;
	}

	@Override
	public FetchUrl setPostData(Map<String, String> paramMap) {
		this.postDataList.clear();
		Set<String> keys=paramMap.keySet();
		for(String key:keys){
			this.postDataList.add(new BasicNameValuePair(key, paramMap.get(key)));
		}
		setPostData(postDataList, this.characterEncode);
		return this;
	}

	@Override
	public String get(String url, int redoTimes) throws FetchTimeoutException {
		this.method = "GET";
		return fetch(url, method, false,redoTimes);
	}

	@Override
	public String post(String url, int redoTimes) throws FetchTimeoutException {
		this.method = "POST";
		return fetch(url, method, false ,redoTimes);
	}

	@Override
	public BinaryData getResource(String url) throws FetchTimeoutException {
		return getResource(url,1);
	}

	@Override
	public BinaryData getResource(String url, int redoTimes) throws FetchTimeoutException {
		this.method = "GET";
		fetch(url, method, true,redoTimes);
		return this.getResponseBinaryData();
	}

	@Override
	public FetchUrl setRequestCharset(String charset) {
		this.characterEncode=charset;
		return this;
	}

	public void setResponseBinaryData(BinaryData responseBinaryData) {
		this.responseBinaryData = responseBinaryData;
	}

	@Override
	public BinaryData getResource(String url, String fileName) throws FetchTimeoutException {
		return getResource(url).setFileName(fileName);
	}

	@Override
	public BinaryData getResource(String url, String fileName, int redoTimes) throws FetchTimeoutException {
		return getResource(url,redoTimes).setFileName(fileName);
	}


}
