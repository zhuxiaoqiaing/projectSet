package com.cacheserverdeploy.deploy;
import java.util.ArrayList;
import java.util.List;

import com.huawei.core.graphSearch;
import com.huawei.core.huaweiDriver;
import com.huawei.test.graphInfo;
public class Deploy
{
    public static String[] deployServer(String[] graphContent)
    {
    	huaweiDriver huawei=new huaweiDriver();
    	String[]results=huawei.driver(graphContent);
        return results;
    }
}
