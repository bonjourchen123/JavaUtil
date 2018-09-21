package com.core.util;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.StringReader;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.input.SAXBuilder;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.HierarchicalStreamDriver;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;
import com.thoughtworks.xstream.io.naming.NoNameCoder;
import com.thoughtworks.xstream.io.xml.CompactWriter;
import com.thoughtworks.xstream.io.xml.Xpp3DomDriver;


/**
 * 
 * @author Bonjour
 *
 */
public class XmlUtil {
	
	/**
	 * 將XML轉換成對應的BEAN
	 * 
	 * @param xml : 要轉換的XML
	 * @param obj : 要轉換BEAN的Class
	 * @return 轉換完的結果
	 */
	@SuppressWarnings("unchecked")
	public static <T> T parseXmlToBean(String xml,Class<T> obj) {
		
		T result = null;
		try {
			//將取回的XML 轉換成BEAN
			JAXBContext jaxbContext = JAXBContext.newInstance(obj);
			Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
			StringReader reader = new StringReader(xml);
			result = (T)jaxbUnmarshaller.unmarshal(reader);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
        return result;
    }

	/**
     *                 
	 * 將 Bean 轉成 xml 
	 * p.s bean 需設定 annotation 
	 * class 設定 @XStreamAlias("xx")
	 * field 設定 @XStreamAlias("xx")
     *                 
	 * @param beanObject : 要轉換的 Bean
	 * @param filterTitle : 是否移除 xml title
     * @param newLine : 是否換行
	 * @param beanObject
	 * @param filterTitle
	 * @return xml string
	 */
	public static String parseBeanToXml(Object beanObject, boolean filterTitle, boolean newLine) {
		String result = null;
		if(beanObject!=null) {
    		try {
    		    HierarchicalStreamDriver domDriver  = null;
                NoNameCoder noNameCoder = new NoNameCoder();    		    
    		    if(newLine) {
    		        domDriver = new Xpp3DomDriver(noNameCoder);
    		    }else {
    		        domDriver = new Xpp3DomDriver(noNameCoder) {
                        public HierarchicalStreamWriter createWriter(Writer out) {
                            return new CompactWriter(out){
                            };                                   
                        } 
                    };
    		    }
    		    
    		    XStream xstream = new XStream(domDriver);       		    
    			xstream.autodetectAnnotations(true);
    			result = xstream.toXML(beanObject).replace("__", "_");
    			if(!filterTitle) {
    				result += "<?xml version=\"1.0\" encoding=\"UTF-8\" ?>" + result;
    			}
    		} catch (Exception e) {
    			e.printStackTrace();
    		}
		}
        return result;
    }
	
	public static String parseBeanToXml(Object beanObject, boolean filterTitle) {
	   return parseBeanToXml(beanObject, filterTitle, false);
	}
	
    /**
     * 將XML字串轉成Map.
     * 
     * @param inputString
     * @param encoding
     * @return
     * @throws Exception
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
	public static Map parseXml(String inputString, String encoding) throws Exception {
        Map map = new LinkedHashMap();

        SAXBuilder builder = new SAXBuilder();
        InputStream is = new ByteArrayInputStream(inputString.getBytes(encoding));
        Document doc = builder.build(is);

        map = parseElement(doc.getRootElement());
        map.put("rootName", doc.getRootElement().getName());

        return map;
    }
    
    @SuppressWarnings({ "rawtypes", "unchecked" })
    private static Map parseElement(Element element) {
        Map map = new LinkedHashMap();
        List list = element.getChildren();
        Iterator iterator = list.iterator();
        while (iterator.hasNext()) {
            Element childElement = (Element) iterator.next();
            if (childElement.getChildren().isEmpty()) {
                map.put(childElement.getName(), childElement.getText());
            } else {
                List arrayList = null;
                if (map.get(childElement.getName()) == null) {
                    arrayList = new ArrayList();
                } else {
                    arrayList = (ArrayList) map.get(childElement.getName());
                }
                arrayList.add(parseElement(childElement));
                map.put(childElement.getName(), arrayList);
            }
        }
        return map;
    }
}
