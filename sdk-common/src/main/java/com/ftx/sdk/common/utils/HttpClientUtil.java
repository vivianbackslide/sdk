package com.ftx.sdk.common.utils;

import com.google.common.base.Predicate;
import com.google.common.base.Strings;
import com.google.common.collect.Maps;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.entity.BufferedHttpEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.xml.sax.InputSource;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.net.*;
import java.security.*;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.util.*;

/**
 * Http客户端工具类<br/>
 * 这是内部调用类，请不要在外部调用。
 * @author miklchen
 *
 */
public class HttpClientUtil {
    private static final Log logger = LogFactory.getLog(HttpClientUtil.class);
    public static final String SunX509 = "SunX509";
    public static final String JKS = "JKS";
    public static final String PKCS12 = "PKCS12";
    public static final String TLS = "TLS";
    
    
    /**
	 * 向指定URL发送GET方法的请求
	 * 
	 * @param url
	 *            发送请求的URL
	 * @param url
	 *            请求参数，请求参数应该是 name1=value1&name2=value2 的形式。
	 * @return URL 所代表远程资源的响应结果
	 */
	public static String sendGet(String url) {
		String result = "";
		BufferedReader in = null;
		try {
			URL realUrl = new URL(url);
			// 打开和URL之间的连接
			URLConnection connection = realUrl.openConnection();
			connection.setConnectTimeout(5000);//连接超时时间为5s
			// 设置通用的请求属性
			connection.setRequestProperty("accept", "*/*");
			connection.setRequestProperty("connection", "Keep-Alive");
			connection.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
			// 建立实际的连接
			connection.connect();
			// 获取所有响应头字段
			/*Map<String, List<String>> map = connection.getHeaderFields();
			// 遍历所有的响应头字段
			for (String key : map.keySet()) {
				System.out.println(key + "--->" + map.get(key));
			}*/
			// 定义 BufferedReader输入流来读取URL的响应
			in = new BufferedReader(new InputStreamReader(connection.getInputStream(), "utf-8"));
			String line;
			while ((line = in.readLine()) != null) {
				result += line;
			}
		} catch (Exception e) {
			logger.error("请求{URL:" + url + "}出现异常", e);
		}
		// 使用finally块来关闭输入流
		finally {
			try {
				if (in != null) {
					in.close();
				}
			} catch (Exception e2) {
				logger.error("关闭流出现异常", e2);
			}
		}
		return result;
	}


    /**
     * 创建时间： 2013-9-16 下午06:47:41
     * 创建人：wuhaifei
     * 参数： 
     * 返回值： String
     * 方法描述 : GET请求
     */
    public static String get(String url, List<NameValuePair> nvps) {
		StringBuilder responseString = new StringBuilder();
    	try {
    		BufferedReader br = null;
    		DefaultHttpClient httpclient = new DefaultHttpClient();
    		String connectUrl = buildGetUrl(url, nvps);
    		long timestamp = System.currentTimeMillis();
    		logger.info("准备Get连接 id: " + timestamp + " | url ：" + connectUrl);
    		HttpGet httpGet = new HttpGet(connectUrl);
            HttpResponse response = httpclient.execute(httpGet);
            HttpEntity entity = response.getEntity();
            int statusCode = response.getStatusLine().getStatusCode();
            if (entity != null && statusCode == 200) {
            	br = new BufferedReader(new InputStreamReader(entity.getContent(), HTTP.UTF_8));
            	String length = null;
            	while((length = br.readLine()) != null){
            		responseString.append(length);
            	}
            } else {
            	logger.error("httpclient get connect error! status code: " + statusCode + " | url:" + connectUrl);
            }
            if(br != null) {
            	br.close();
            }
            if(entity != null) {
            	entity.consumeContent();
            }
            logger.info("本次Get请求返回 id:" + timestamp + " | response:" + responseString.toString());
    	} catch (Exception e) {
			logger.error("请求{URL:" + url + "}出现异常", e);
        }
		
        return responseString.toString();
	}
    
    /**
     * 创建时间： 2013-8-8 下午07:21:30
     * 创建人：wuhaifei
     * 参数： 
     * 返回值： String
     * 方法描述 : POST请求
     */
    public static String post(String url, List<NameValuePair> qparams) {
        logger.info("准备post连接 url:" + url + " | params=:" + buildHttpGetParams(qparams));
        try {
            DefaultHttpClient httpclient = new DefaultHttpClient();
            HttpPost httpost = new HttpPost(url);
            httpost.setEntity(new UrlEncodedFormEntity(qparams, HTTP.UTF_8));
            HttpResponse response = httpclient.execute(httpost);
            StatusLine statusLine = response.getStatusLine();
            if(statusLine.getStatusCode() != 200){
                return "" + statusLine.getStatusCode();
            }
            HttpEntity entity = new BufferedHttpEntity(response.getEntity());
            if (entity != null) {
                entity = new BufferedHttpEntity(entity);
                return EntityUtils.toString(entity, HTTP.UTF_8);
            }
        } catch (Exception e) {
			logger.error("请求{URL:" + url + "}出现异常", e);
        }
        return "500";
    }
    
    /**
	 * 向指定 URL 发送POST方法的请求
	 * 
	 * @param url
	 *            发送请求的 URL
	 * @param param
	 *            请求参数，请求参数应该是 name1=value1&name2=value2 的形式。
	 * @return 所代表远程资源的响应结果
	 * @throws Exception
	 */
	public static String sendPost(String url, String param) throws Exception {
		Map<String, String> header = new HashMap<String, String>();
		header.put("accept", "*/*");
		header.put("connection", "Keep-Alive");
		header.put("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
		header.put("charset", "UTF-8");
		return sendPost(url, param, header);
	}
	
	
	/**
	 * 向指定 URL 发送POST方法的请求
	 * 
	 * @param url
	 *            发送请求的 URL
	 * @param param
	 *            请求参数，请求参数应该是 name1=value1&name2=value2 的形式。
	 * @return 所代表远程资源的响应结果
	 * @param header 
	 * @return
	 * @throws Exception
	 */
	public static String sendPost(String url, String param, Map<String, String> header) throws Exception {
		PrintWriter out = null;
		BufferedReader in = null;
		String result = "";
		logger.info("准备post连接 url:" + url + " | params=:\n" + param);
		try {
			URL realUrl = new URL(url);
			// 打开和URL之间的连接
			URLConnection conn = realUrl.openConnection();
			for (Map.Entry<String, String> entry : header.entrySet()) {
				conn.setRequestProperty(entry.getKey(), entry.getValue());
			}
			// 发送POST请求必须设置如下两行
			conn.setDoOutput(true);
			conn.setDoInput(true);
			conn.setConnectTimeout(3000);
			conn.setReadTimeout(10000);
			// 获取URLConnection对象对应的输出流
			out = new PrintWriter(new OutputStreamWriter(conn.getOutputStream(), "UTF-8"));
			// 发送请求参数
			out.print(param);
			// flush输出流的缓冲
			out.flush();
			// 定义BufferedReader输入流来读取URL的响应
			in = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
			String line;
			while ((line = in.readLine()) != null) {
				result += line;
			}
		} catch (Exception e) {
			throw new Exception(e.getMessage(), e);
		}finally {// 使用finally块来关闭输出流、输入流
			try {
				if (out != null) {
					out.close();
				}
				if (in != null) {
					in.close();
				}
			} catch (IOException e) {
				throw new Exception(e.getMessage(), e);
			}
		}
		return result;
	}
	
	/**
	 * 讲传递的参数转换为map格式
	 * @param map
	 * @return
	 */
	public static String mapToParams(Map<String, Object> map) {
    	StringBuffer sb = new StringBuffer();
    	int k=0;
    	for(String key:map.keySet()){
    		if(k>0){
    			sb.append("&");
    		}
    		sb.append(key);
    		sb.append("=");
    		sb.append(map.get(key));
    		k++;
    	}
		return sb.toString();
	}
/**
 * 将map格式的转换为XML文件
 * @param params
 * @return
 */
    public static String mapToXml(Map<String, Object> params){
        StringBuilder buf = new StringBuilder();
        List<String> keys = new ArrayList<String>(params.keySet());
        Collections.sort(keys);
        buf.append("<xml>");
        for(String key : keys){
            buf.append("<").append(key).append(">");
            buf.append("<![CDATA[").append(params.get(key).toString()).append("]]>");
            buf.append("</").append(key).append(">\n");
        }
        buf.append("</xml>");
        return buf.toString();
    }
    /**
     * 转  XML to  map
     * @author  
     * @param xmlBytes
     * @param charset
     * @return
     * @throws Exception
     */
    public static Map<String, Object> xmlToMap(byte[] xmlBytes,String charset) throws Exception{
        SAXReader reader = new SAXReader(false);
        InputSource source = new InputSource(new ByteArrayInputStream(xmlBytes));
        source.setEncoding(charset);
        Document doc = reader.read(source);
        Map<String, Object> params = toMap(doc.getRootElement());
        return params;
    }
    
    /** 
     * request转字符串
     * @param request
     * @return
     *
     */
    public static String parseRequst(HttpServletRequest request){
        String body = "";
        try {
            ServletInputStream inputStream = request.getInputStream(); 
            BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
            while(true){
                String info = br.readLine();
                if(info == null){
                    break;
                }
                if(body == null || "".equals(body)){
                    body = info;
                }else{
                    body += info;
                }
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }            
        return body;
    }
    
    /**
	 * 参数按字母顺序组装
     * @author 
     * @param payParams
     * @param encoding--拼接是否转码
     * @return
     */
    public static void buildPayParams(StringBuilder sb,Map<String, Object> payParams,boolean encoding){
    	payParams = Maps.filterValues(payParams, new Predicate<Object>() {
            @Override
            public boolean apply(Object value) {
                if (null == value || Strings.isNullOrEmpty(value.toString())) {
                    return false;
                }
                return true;
            }
        });
    	List<String> keys = new ArrayList<String>(payParams.keySet());
        Collections.sort(keys);
        for(String key : keys){
            sb.append(key).append("=");
            if(encoding){
            	if(payParams.get(key) != null) {
                    sb.append(urlEncode(payParams.get(key).toString()));
                }
            }else{
                sb.append(payParams.get(key));
            }
            sb.append("&");
        }
        sb.setLength(sb.length() - 1);
    }
    /**
     * 转码
     * @param str
     * @return
     */
    public static String urlEncode(String str){
        try {
            return URLEncoder.encode(str, "UTF-8");
        } catch (Throwable e) {
            return str;
        } 
    }
    /**
     * 转MAP
     * @author  
     * @param element
     * @return
     */
    public static Map<String, Object> toMap(Element element){
        Map<String, Object> rest = new HashMap<String, Object>();
        List<Element> els = element.elements();
        for(Element el : els){
            rest.put(el.getName().toLowerCase(), el.getTextTrim());
        }
        return rest;
    }
	/**
     * get HttpURLConnection
     * @param strUrl url地址
     * @return HttpURLConnection
     * @throws IOException
     */
    public static HttpURLConnection getHttpURLConnection(String strUrl)
            throws IOException {
        URL url = new URL(strUrl);
        HttpURLConnection httpURLConnection = (HttpURLConnection) url
                .openConnection();
        return httpURLConnection;
    }

    /**
     * get HttpsURLConnection
     * @param strUrl url地址
     * @return HttpsURLConnection
     * @throws IOException
     */
    public static HttpsURLConnection getHttpsURLConnection(String strUrl)
            throws IOException {
        URL url = new URL(strUrl);
        HttpsURLConnection httpsURLConnection = (HttpsURLConnection) url
                .openConnection();
        return httpsURLConnection;
    }

    /**
     * 获取不带查询串的url
     * @param strUrl
     * @return String
     */
    public static String getURL(String strUrl) {

        if(null != strUrl) {
            int indexOf = strUrl.indexOf("?");
            if(-1 != indexOf) {
                return strUrl.substring(0, indexOf);
            }

            return strUrl;
        }

        return strUrl;

    }

    /**
     * 获取查询串
     * @param strUrl
     * @return String
     */
    public static String getQueryString(String strUrl) {

        if(null != strUrl) {
            int indexOf = strUrl.indexOf("?");
            if(-1 != indexOf) {
                return strUrl.substring(indexOf+1, strUrl.length());
            }

            return "";
        }

        return strUrl;
    }

    /**
     * 查询字符串转换成Map<br/>
     * name1=key1&name2=key2&...
     * @param queryString
     * @return
     */
    public static Map queryString2Map(String queryString) {
        if(null == queryString || "".equals(queryString)) {
            return null;
        }

        Map m = new HashMap();
        String[] strArray = queryString.split("&");
        for(int index = 0; index < strArray.length; index++) {
            String pair = strArray[index];
            HttpClientUtil.putMapByPair(pair, m);
        }

        return m;

    }

    /**
     * 把键值添加至Map<br/>
     * pair:name=value
     * @param pair name=value
     * @param m
     */
    public static void putMapByPair(String pair, Map m) {

        if(null == pair || "".equals(pair)) {
            return;
        }

        int indexOf = pair.indexOf("=");
        if(-1 != indexOf) {
            String k = pair.substring(0, indexOf);
            String v = pair.substring(indexOf+1, pair.length());
            if(null != k && !"".equals(k)) {
                m.put(k, v);
            }
        } else {
            m.put(pair, "");
        }
    }

    /**
     * BufferedReader转换成String<br/>
     * 注意:流关闭需要自行处理
     * @param reader
     * @return String
     * @throws IOException
     */
    public static String bufferedReader2String(BufferedReader reader) throws IOException {
        StringBuffer buf = new StringBuffer();
        String line = null;
        while( (line = reader.readLine()) != null) {
            buf.append(line);
            buf.append("\r\n");
        }

        return buf.toString();
    }

    /**
     * 处理输出<br/>
     * 注意:流关闭需要自行处理
     * @param out
     * @param data
     * @param len
     * @throws IOException
     */
    public static void doOutput(OutputStream out, byte[] data, int len)
            throws IOException {
        int dataLen = data.length;
        int off = 0;
        while(off < dataLen) {
            if(len >= dataLen) {
                out.write(data, off, dataLen);
            } else {
                out.write(data, off, len);
            }

            //刷新缓冲区
            out.flush();

            off += len;

            dataLen -= len;
        }

    }

    /**
     * 获取SSLContext
     * @param trustPasswd
     * @param keyPasswd
     * @return
     * @throws NoSuchAlgorithmException
     * @throws KeyStoreException
     * @throws IOException
     * @throws CertificateException
     * @throws UnrecoverableKeyException
     * @throws KeyManagementException
     */
    public static SSLContext getSSLContext(
            FileInputStream trustFileInputStream, String trustPasswd,
            FileInputStream keyFileInputStream, String keyPasswd)
            throws NoSuchAlgorithmException, KeyStoreException,
            CertificateException, IOException, UnrecoverableKeyException,
            KeyManagementException {

        // ca
        TrustManagerFactory tmf = TrustManagerFactory.getInstance(HttpClientUtil.SunX509);
        KeyStore trustKeyStore = KeyStore.getInstance(HttpClientUtil.JKS);
        trustKeyStore.load(trustFileInputStream, HttpClientUtil
                .str2CharArray(trustPasswd));
        tmf.init(trustKeyStore);

        final char[] kp = HttpClientUtil.str2CharArray(keyPasswd);
        KeyManagerFactory kmf = KeyManagerFactory.getInstance(HttpClientUtil.SunX509);
        KeyStore ks = KeyStore.getInstance(HttpClientUtil.PKCS12);
        ks.load(keyFileInputStream, kp);
        kmf.init(ks, kp);

        SecureRandom rand = new SecureRandom();
        SSLContext ctx = SSLContext.getInstance(HttpClientUtil.TLS);
        ctx.init(kmf.getKeyManagers(), tmf.getTrustManagers(), rand);

        return ctx;
    }

    /**
     * 获取CA证书信息
     * @param cafile CA证书文件
     * @return Certificate
     * @throws CertificateException
     * @throws IOException
     */
    public static Certificate getCertificate(File cafile)
            throws CertificateException, IOException {
        CertificateFactory cf = CertificateFactory.getInstance("X.509");
        FileInputStream in = new FileInputStream(cafile);
        Certificate cert = cf.generateCertificate(in);
        in.close();
        return cert;
    }

    /**
     * 字符串转换成char数组
     * @param str
     * @return char[]
     */
    public static char[] str2CharArray(String str) {
        if(null == str) {
            return null;
        }

        return str.toCharArray();
    }

    /**
     * 存储ca证书成JKS格式
     * @param cert
     * @param alias
     * @param password
     * @param out
     * @throws KeyStoreException
     * @throws NoSuchAlgorithmException
     * @throws CertificateException
     * @throws IOException
     */
    public static void storeCACert(Certificate cert, String alias,
                                   String password, OutputStream out) throws KeyStoreException,
            NoSuchAlgorithmException, CertificateException, IOException {
        KeyStore ks = KeyStore.getInstance("JKS");

        ks.load(null, null);

        ks.setCertificateEntry(alias, cert);

        // store keystore
        ks.store(out, HttpClientUtil.str2CharArray(password));

    }

    public static InputStream String2Inputstream(String str) {
        return new ByteArrayInputStream(str.getBytes());
    }
    
  	/**
  	 * 创建时间： 2013-9-16 下午06:46:06
  	 * 创建人：wuhaifei
  	 * 参数： 
  	 * 返回值： String
  	 * 方法描述 : 构造get方式url
  	 */
	public static String buildGetUrl(String url, List<NameValuePair> nvps) {
		StringBuffer connectUrl = new StringBuffer(url);
		if (url.indexOf("?") == -1) {
			if (nvps.size() != 0 && nvps.size() > 0) {
				connectUrl.append("?");
				connectUrl.append(URLEncodedUtils.format(nvps, HTTP.UTF_8));
			}
		} else {
			if (nvps.size() != 0 && nvps.size() > 0) {
				connectUrl.append("&");
				connectUrl.append(URLEncodedUtils.format(nvps, HTTP.UTF_8));
			}
		}
		
		return connectUrl.toString();
	}
	
	/**
	 * 创建时间： 2013-9-22 上午11:17:35
	 * 创建人：wuhaifei
	 * 参数： 
	 * 返回值： String
	 * 方法描述 : 构造get形式的参数,没有URL转码
	 */
	public static String buildHttpGetParams(List<NameValuePair> params) {
		String paramStr = null;
		StringBuffer paramStrBuff = new StringBuffer();
		if(params.size() > 0) {
			paramStrBuff.append("?");
			for (NameValuePair nvp : params) {
				paramStrBuff.append(nvp.toString()).append("&");
			}
			String str = paramStrBuff.toString();
			paramStr = str.substring(0, str.length() - 1);
		}
		
		return paramStr;
	}
	/**
	 * 提取post和get的参数
	 * @param request
	 * @return
	 */
	public static String ParamstoResultString(HttpServletRequest request) {
		Map<String, Object> map = ParamstoResultMap(request);
        Enumeration paramNames = request.getParameterNames();  
        while (paramNames.hasMoreElements()) {  
            String paramName = (String) paramNames.nextElement();  
  
            String[] paramValues = request.getParameterValues(paramName);  
            if (paramValues.length == 1) {  
                String paramValue = paramValues[0];  
                if (paramValue.length() != 0) {  
                    map.put(paramName, paramValue);  
                }  
            }  
        }  
        StringBuilder sb = new StringBuilder();
        for (String key : map.keySet() ) {  
            sb.append("&").append(key).append("=").append(map.get(key));
        }  
		return sb.toString();
	}
	/**
	 * 提取post和get的参数
	 * @param request
	 * @return
	 */
	public static Map<String, Object> ParamstoResultMap(HttpServletRequest request) {
		Map<String, Object> map = new HashMap<String, Object>();  
        Enumeration paramNames = request.getParameterNames();  
        while (paramNames.hasMoreElements()) {  
            String paramName = (String) paramNames.nextElement();  
            String[] paramValues = request.getParameterValues(paramName);  
            if (paramValues.length == 1) {  
                String paramValue = paramValues[0];  
                if (paramValue.length() != 0) {  
                    map.put(paramName, paramValue);  
                }  
            }  
        }  
		return map;
	}
	
	

	/**
	 * post:直接将参数（json）写入输出流
	 * @param url 地址
	 * @param json json格式的参数
	 * @return
	 * @throws Exception 
	 */
	public static String post(String url, String json) {  
        try {  
            HttpsURLConnection connection = (HttpsURLConnection) new URL(url).openConnection();    
            connection.setRequestMethod("POST");    
            connection.setDoOutput(true);    
            connection.setAllowUserInteraction(false);   
            PrintStream ps = new PrintStream(connection.getOutputStream());    
            ps.print(json); 
            ps.close();    
            BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));    
            String str;    
            StringBuffer sb = new StringBuffer();    
            while ((str = br.readLine()) != null) {    
                sb.append(str);      
            }    
            br.close();    
            return sb.toString();
        } catch (Exception e) {  
        	logger.error("请求{URL:" + url + "}出现异常", e);
        }  
        return null;
	  }

	/**
	 * 代理发送get
	 * @author goff.yin 
	 * @date 2017-3-10 上午10:17:58  
	 * @version 1.0.0 
	 * @param url
	 * @param param
	 * @return
	 */
	public static String sendGetByProxy(String url, String param) {
	    Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress("127.0.0.1", 1080)); 
	    String result = "";
	        BufferedReader in = null;
	        try {
	            String urlNameString = url + "?" + param;
	            URL realUrl = new URL(urlNameString);
	            // 打开和URL之间的连接
	            URLConnection connection = realUrl.openConnection(proxy);
	            // 设置通用的请求属性
	            connection.setRequestProperty("accept", "*/*");
	            connection.setRequestProperty("connection", "Keep-Alive");
	            connection.setRequestProperty("user-agent","Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
	            connection.setRequestProperty("charset", "UTF-8");
	            // 建立实际的连接
	            connection.connect();
	            // 获取所有响应头字段
	            Map<String, List<String>> map = connection.getHeaderFields();
	            // 遍历所有的响应头字段
	            /*for (String key : map.keySet()) {
	                System.out.println(key + "--->" + map.get(key));
	            }*/
	            // 定义 BufferedReader输入流来读取URL的响应
	            in = new BufferedReader(new InputStreamReader(
	                    connection.getInputStream()));
	            String line;
	            while ((line = in.readLine()) != null) {
	                result += line;
	            }
	        } catch (Exception e) {
	            System.out.println("发送GET请求出现异常！" + e);
	            e.printStackTrace();
	        }
	        // 使用finally块来关闭输入流
	        finally {
	            try {
	                if (in != null) {
	                    in.close();
	                }
	            } catch (Exception e2) {
	                e2.printStackTrace();
	            }
	        }
	        return result;
	}  
}

