package zhku.jc.jfetchUrl;

import java.io.IOException;

import zhku.jc.jfetchUrl.impl.BinaryData;

public class BinaryNameValuePair
  implements NameValuePair
{
  private String name;
  private BinaryData data;
  @SuppressWarnings("unused")
  private static final int MAX_SEND_DATA = 10485760;

  public BinaryNameValuePair(String inputParameterName, String fileName, byte[] data)
    throws IOException
  {
    this.name = inputParameterName;
    if (data.length > 10485760) {
      throw new IOException("file is too large");
    }
    this.data = new BinaryData(fileName, data);
  }

  public String getName()
  {
    return this.name;
  }

  public BinaryData getValue()
  {
    return this.data;
  }

  public String toStirng() {
    String str = "post data inputParameterName = " + this.name;
    if (this.data != null) {
      str = str + " , fileName = " + this.data.getFileName();
    }
    return str;
  }
}