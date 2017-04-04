package com.filetool.util;
import java.io.*;
/**
 * 完成对象复制
 * @author cristo
 *
 */
public class myUtil{   
 //序列化  
 public static <T extends Serializable> byte[] objToByte(T obj) throws IOException{  
      ByteArrayOutputStream out = new ByteArrayOutputStream();    
      ObjectOutputStream obs = new ObjectOutputStream(out);    
      obs.writeObject(obj);  
      obs.close();  
      byte[] b=out.toByteArray();  
return b;    
  }  
 //反序列化  
   public static <c extends Serializable> c byteToObj(byte[] bytes,Class c) throws Exception{  
      ByteArrayInputStream ios = new ByteArrayInputStream(bytes);    
      ObjectInputStream ois = new ObjectInputStream(ios);    
      //返回生成的新对象    
      c obj = (c) ois.readObject();    
      ois.close();    
    return obj;  
  } 
 
   }