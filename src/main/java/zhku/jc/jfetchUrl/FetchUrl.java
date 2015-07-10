package zhku.jc.jfetchUrl;

import java.util.List;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.client.CookieStore;

import zhku.jc.jfetchUrl.exception.FetchTimeoutException;
import zhku.jc.jfetchUrl.impl.BinaryData;
/**
 * @author JackCan
 * 基于HttpClient 封装的请求器。
 */
public interface FetchUrl {
	 /**
	  * 资源的获取。eg. 图片,mp3等非文本资源
	  * @param url 资源的 链接 
	  * @return BinaryData 
	  * @throws FetchTimeoutException
	  */
	  public abstract BinaryData getResource(String url) throws FetchTimeoutException;
	  /**
	  * 资源的获取。eg. 图片,mp3等非文本资源
	  * @param url 资源的 链接 
	  * @param fileName 指定返回的资源名称
	  * @return BinaryData 
	  * @throws FetchTimeoutException
	  */
	  public abstract BinaryData getResource(String url,String fileName) throws FetchTimeoutException;
	  /**
	  * 资源的获取。eg. 图片,mp3等非文本资源
	  * @param url 资源的 链接 
	  * @param redoTimes 指定错误重试次数
	  * @return BinaryData 
	  * @throws FetchTimeoutException
	  */
	  public abstract BinaryData getResource(String url,int redoTimes) throws FetchTimeoutException;
	  /**
	  * 资源的获取。eg. 图片,mp3等非文本资源
	  * @param url 资源的 链接 
	  * @param fileName 指定返回的资源名称
	  * @param redoTimes 指定错误重试次数
	  * @return BinaryData 
	  * @throws FetchTimeoutException
	  */
	  public abstract BinaryData getResource(String url,String fileName,int redoTimes) throws FetchTimeoutException;
	  /**
	   * 发送GET请求
	   * @param url 请求地址
	   * @return String 返回文本内容
	   * @throws FetchTimeoutException
	   */
	  public abstract String get(String url) throws FetchTimeoutException;
	  /**
	   * 发送GET请求
	   * @param url 请求地址
	   * @param redoTimes 指定错误请求次数
	   * @return String 返回文本内容
	   * @throws FetchTimeoutException
	   */
	  public abstract String get(String url,int redoTimes) throws FetchTimeoutException;
	  /**
	   * 发送POST请求
	   * POST的表单需要先 调用 setPostData 方法设置
	   * @param url 请求地址
	   * @return String 返回文本内容
	   * @throws FetchTimeoutException
	   */
	  public abstract String post(String url) throws FetchTimeoutException;
	  /**
	   * 发送POST请求
	   * POST的表单需要先 调用 setPostData 方法设置
	   * @param url 请求地址
	   * @param redoTimes 指定错误重试次数
	   * @return String 返回文本内容
	   * @throws FetchTimeoutException
	   */
	  public abstract String post(String url,int redoTimes)throws FetchTimeoutException;
	  /**
	   * 设置文本解码编码
	   * @param charset
	   * @return FetchUrl 返回自身 可链式调用
	   */
	  public abstract FetchUrl setDecodeCharset(String charset);
	  /**
	   * 设置 表单 编码 
	   * @param charset UTF-8|GB2312
	   * @return FetchUrl 返回自身 可链式调用
	   */
	  public abstract FetchUrl setRequestCharset(String charset);
	  /**
	   * 设置请求read的超时时间
	   * @param time
	   * @return FetchUrl 返回自身 可链式调用
	   */
	  public abstract FetchUrl setReadTimeout(int time);
	  /**
	   * 设置请求 发送超时时间
	   * @param time
	   * @return FetchUrl 返回自身 可链式调用
	   */
	  public abstract FetchUrl setSendTimeout(int time);
	  /**
	   * 设置连接 超时时间
	   * @param time
	   * @return FetchUrl 返回自身 可链式调用
	   */
	  public abstract FetchUrl setConnectTimeout(int time);
	  /**
	   * 设置是否支持重定向
	   * @param allow
	   * @return FetchUrl 返回自身 可链式调用
	   */
	  public abstract FetchUrl setAllowRedirect(boolean allow);
	  /**
	   * 设置 重定向的次数
	   * @param num
	   * @return FetchUrl 返回自身 可链式调用
	   */
	  public abstract FetchUrl setRedirectNum(int num);
	  /**
	   * 设置 一个cookie 值
	   * @param key 
	   * @param value
	   * @return FetchUrl 返回自身 可链式调用
	   */
	  public abstract FetchUrl setCookie(String key, String value);
	  /**
	   * 设置Cookie 值，可以是多个cookie值得集合
	   * @param cookieString 多个cookie用 ; (有空格) 隔开
	   * @return FetchUrl 返回自身 可链式调用
	   */
	  public abstract FetchUrl setCookies(String cookieString);
	  /**
	   * 设置 Cookie 
	   * @param cookieStroe 一般使用 StringCookieStore的实现类
	   * @return FetchUrl 返回自身 可链式调用
	   */
	  public abstract FetchUrl setCookieStore(CookieStore cookieStroe);
	  /**
	   * 获取 httpclient 的 内部 cookieStore
	   * @return
	   */
	  public abstract CookieStore getCookieStore();
	  /**
	   * 将 cookieStore中的 cookie键值 格式化为字符串(用; 隔开)
	   * @return
	   */
	  public abstract String getCookies();
	  /**
	   * 设置cookie键值对 ，不支持 一个name 对应多个 value
	   * @param cookieMap
	   * @return FetchUrl 返回自身 可链式调用
	   */
	  public abstract FetchUrl setCookies(Map<String, String> cookieMap);
	  /**
	   * 设置请求 头 
	   * @param name
	   * @param header
	   * @return FetchUrl 返回自身 可链式调用
	   */
	  public abstract FetchUrl setHeader(String name, String header);
	  /**
	   * 设置代理IP
	   * @param ip
	   * @param port
	   * @return
	   */
	  public abstract FetchUrl setProxy(String ip, int port);
	  /**
	   * 需要身份验证的账号信息
	   * @param userName
	   * @param password
	   * @return
	   */
	  public abstract FetchUrl setHttpAuth(String userName, String password);
	  /**
	   * 设置 标准的表单键值对
	   * @param paramList
	   * @return  FetchUrl 返回自身 可链式调用
	   */
	  public abstract FetchUrl setPostData(List<NameValuePair> paramList);
	  /**
	   * 设置 请求体 (eg.上传文件)
	   * @param entity
	   * @return FetchUrl 返回自身 可链式调用
	   */
	  public abstract FetchUrl setPostData(HttpEntity entity);
	  /**
	   * 一个比较常用的设置表单数据的方法，
	   * 适用情况是 表单不存在key重复的情况。
	   * @param paramMap
	   * @return FetchUrl 返回自身 可链式调用
	   */
	  public abstract FetchUrl setPostData(Map<String,String> paramMap);
	  /**
	   * 设置请求数据
	   * @param paramList formBoy 请求数据表单
	   * @param paramString queryString 请求地址 参数
	   * @return FetchUrl 返回自身 可链式调用
	   */
	  public abstract FetchUrl setPostData(List<NameValuePair> paramList, String paramString);
	  /**
	   * 获取当前请求的状态码
	   * @return
	   */
	  public abstract int getHttpCode();
	  /**
	   * 获取当前请求的文本内容
	   * @return
	   */
	  public abstract String getResponseBody();
	  /**
	   * 获取文本内容的长度
	   * @return
	   */
	  public abstract int getResponseBodyLen();
	  /**
	   * 获取 响应体的 头信息
	   * @return
	   */
	  public abstract Map<String, String> getResponseHeader();
	  /***
	   * 获取 格式化了的响应体的 头文本信息
	   * @return
	   */
	  public abstract String getResponseHeaderString();
	  /**
	   * 获取 当前请求产生的 
	   * @return
	   */
	  public abstract String getResponseCookies();
	  /**
	   * 获取请求错误文本
	   * @return
	   */
	  public abstract String getErrmsg();
	  /**
	   * 获取 资源请求 完成后的资源
	   * @return
	   */
	  public abstract BinaryData getResponseBinaryData();
	  /**
	   * 重置 请求器
	   * @return FetchUrl 返回自身 可链式调用
	   */ 
	  public abstract FetchUrl reset();
}
