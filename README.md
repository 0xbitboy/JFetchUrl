

# JFetchUrl

这是一个基于Httpclient 的Http请求处理工具。

## 0.0.2 _新功能_

1. 支持对没有Content-type的资源的下载。
2. 添加FetchUrlBuilder 来创建FetchUrl实例。
3. 添加LocalCacheDnsResolver的实现，支持DNS缓存。

## 0.0.3 _新功能_
1. DNSPod 免费版的HttpDnsResolver的实现


## 使用示例

* GET

    ```
    
     FetchUrl fetchUrl = new FetchUrlBuilder().build();
     fetchUrl.get("http://qq.com");
     System.out.println(fetchUrl.getResponseBody());
     
    ```
* POST

    ```
     FetchUrl fetchUrl = new FetchUrlBuilder().build();     
     Map<String,String> param = new HashMap<String, String>();
     param.put("username","liaojiacan");
     param.put("passowrd","abc");
     fetchUrl.setPostData(param);
     fetchUrl.post("http://qq.com");
     System.out.println(fetchUrl.getResponseBody());
      
    ```
* 使用自定义DnsResolver
    
    ```
     FetchUrl fetchUrl = new FetchUrlBuilder(new DNSPodHttpDnsResolver()).build();
     fetchUrl.get("http://gitbub.com");
     System.out.println(fetchUrl.getResponseBody());

    ```
* 设置Header

    ```
    FetchUrl fetchUrl = new FetchUrlBuilder().build();
    fetchUrl.setHeader("Content-Type","application/json");
    fetchUrl.get("http://gitbub.com");
    System.out.println(fetchUrl.getResponseBody());
    
    ```