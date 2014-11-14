package zhku.jackcan.webCrawler;

public class BasicNameValuePair implements NameValuePair {
	private String name;
	private String value;

	public BasicNameValuePair(String name, String value) {
		this.name = name;
		this.value = value;
	}

	public String getName() {
		return this.name;
	}

	public String getValue() {
		return this.value;
	}

	public String toString() {
		return "post data inputParameterName = " + this.name + " , value = "
				+ this.value;
	}
}
