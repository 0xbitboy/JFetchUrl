package zhku.jackcan.webCrawler.impl;

import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.cookie.BasicClientCookie;


/** 
 * 更加 字符串构造 CookieStore 对象 ，多个 Cookie值用 >@<分开
 * @ClassName: StringCookieStore
 * @Description: TODO(convert String to {@link Cookie}}) 
 *  
 */ 
public class StringCookieStore extends BasicCookieStore{
	
	private static final long serialVersionUID = -9019044889357776457L;
	public StringCookieStore(final String cookie){
		if(!cookie.contains(">@<")) throw new RuntimeException("parameter must split by >@<");
		this.addCookie(cookie);
	}
	public synchronized StringCookieStore addCookie(final String cookie){
		if(cookie != null){
			if(cookie.length()>5){
				String cookieArr[] = cookie.split(">@<");
				BasicClientCookie basicClientCookie;
				for(String arr : cookieArr){
					int nIndex = arr.indexOf("name:")+6;
					int vIndex = arr.indexOf("value:")+7;
					int dIndex = arr.indexOf("domain:")+8;
					int pIndex = arr.indexOf("path:")+6;
					basicClientCookie = new BasicClientCookie(arr.substring(nIndex, arr.indexOf(']', nIndex)),arr.substring(vIndex, arr.indexOf(']', vIndex)));
					basicClientCookie.setDomain(arr.substring(dIndex, arr.indexOf(']', dIndex)));
					basicClientCookie.setPath(arr.substring(pIndex, arr.indexOf(']', pIndex)));
					addCookie(basicClientCookie);
				}
			}
		}
		return this;
	}
	
}
