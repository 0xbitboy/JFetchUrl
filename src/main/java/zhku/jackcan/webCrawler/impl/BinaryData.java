package zhku.jackcan.webCrawler.impl;
public class BinaryData
{
  private String fileName;
  private byte[] data;

  public BinaryData(String fileName, byte[] data)
  {
    this.fileName = fileName;
    this.data = data;
  }
  public String getFileName() {
    return this.fileName;
  }

  public byte[] getData() {
    return this.data;
  }

  public String toString()
  {
    return "fileName = " + this.fileName;
  }
}