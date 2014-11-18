package zhku.jackcan.webCrawler;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;

import org.junit.Test;

import zhku.jackcan.webCrawler.exception.FetchTimeoutException;
import zhku.jackcan.webCrawler.impl.BinaryData;
import junit.framework.TestCase;

/**
 * Unit test for simple App.
 */
public class AppTest 
    extends TestCase
{
    @Test
    public void testGetResource(){
    	String url = "http://jw.zhku.edu.cn/jwmis/sys/ValidateCode.aspx";
    	FetchUrl fetchUrl  = FetchUrlFactory.getFetchurl();

		for(int i=0;i<20;i++)
    	try {
			BinaryData img = fetchUrl.getResource(url,new Date().getTime()+".jpg");
			System.out.println(fetchUrl.getResponseCookies());
			FileOutputStream op = new FileOutputStream("E:\\validate\\"+img.getFileName());
			op.write(img.getData());
			op.flush();
    			
		} catch (FetchTimeoutException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
    }
}
