package com.core.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.Serializable;

/**
 * 
 * @author Bonjour
 *
 */
public class SerializeUtil {
	
	/**
	 * 檢查物件是否可以被序列化
	 * 
	 * @param o : 要檢查的物件
	 * @return
	 */
    public static boolean isSearializable(final Object o){
        if(o==null) return true;
        
        final boolean retVal;

        if(implementsInterface(o)){
            retVal = attemptToSerialize(o);
        }else{
            retVal = false;
        }

        return (retVal);
    }

    private static boolean implementsInterface(final Object o){
        final boolean retVal;

        retVal = ((o instanceof Serializable) || (o instanceof Externalizable));

        return (retVal);
    }

    private static boolean attemptToSerialize(final Object o){
        final OutputStream sink;
        ObjectOutputStream stream;

        stream = null;

        try{
            sink   = new ByteArrayOutputStream();
            stream = new ObjectOutputStream(sink);
            stream.writeObject(o);
        }catch(final IOException ex){
        	ex.printStackTrace();
            return (false);
        }finally{
            if(stream != null){
                try{
                    stream.close();
                }catch(final IOException ex){
                    ex.printStackTrace();
                }
            }
        }

        return (true);
    }
    
    
    
    /**
     * 序列化物件轉byte[]
     * 
     * @return
     * @throws Exception 
     */
    public static byte[] serialize(Object obj) throws Exception {
    	ByteArrayOutputStream baos = new ByteArrayOutputStream();
    	ObjectOutputStream os = new ObjectOutputStream(baos);
    	os.writeObject(obj);
    	return baos.toByteArray();
    }
    
    
    /**
     * byte[]轉回序列化物件
     * 
     * @return 
     * @return
     * @throws Exception 
     */
    @SuppressWarnings("unchecked")
	public static <T> T deserialize(byte[] bytes) throws Exception {
    	ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
    	ObjectInputStream os = new ObjectInputStream(bais);
    	return (T) os.readObject();
    }
    
    
    @SuppressWarnings("unchecked")
	public static <T> T cloneObject(Object obj) throws Exception {
    	if(obj==null) return (T)obj;
    	return (T) deserialize(serialize(obj));
    }
    
}
