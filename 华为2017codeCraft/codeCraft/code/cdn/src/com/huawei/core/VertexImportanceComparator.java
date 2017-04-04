package com.huawei.core;

import java.util.Comparator;

import com.huawei.domain.VertexImportance;

/**
 * 内部比较器,完成节点重要性排序
 * @author cristo
 *
 */
public class VertexImportanceComparator implements Comparator{  	  
    public int compare(Object o1, Object o2) {
    	if(o1==null&&o2==null){
    		return 0;
    	}
    	if(o1==null){
    		return -1;
    	}
    	if(o2==null){
    		return 1;
    	}
    	VertexImportance t1=(VertexImportance) o1;  
    	VertexImportance t2=(VertexImportance) o2;
    	if(t1.allbandwith==t2.allbandwith){
    		if(t1.degree==t2.degree){
    			return 0;
    		}
    		else
    			return t1.degree>t2.degree?-1:1;
    	}
    	else{
    		return t1.allbandwith>t2.allbandwith?-1:1;
    	}
    }        
}  