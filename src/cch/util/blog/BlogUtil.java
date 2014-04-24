package cch.util.blog;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.http.client.ClientProtocolException;
import org.dom4j.Element;
import org.dom4j.dom.DOMDocument;

import cch.util.proxy.ProxyUtil;

public class BlogUtil {
	
	public static void main(String[] args) throws ClientProtocolException, IOException {
		Logger logger = Logger.getLogger("name");
		String url = "http://love.heima.com/book/xinlingjitang/";
		String result = ProxyUtil.go(url);
		logger.log(Level.INFO, result);
		DOMDocument dom = new DOMDocument(result);
		Element e = (Element) dom.getElementById("middle");
		
		logger.log(Level.INFO, e.getName());
		
	}
}
