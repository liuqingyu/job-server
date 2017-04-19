package cn.com.strongunion.batch.common;

import java.io.Serializable;
import java.util.HashMap;

/**
 * Created by Administrator on 2016/9/5.
 */
public class BaseJob implements Serializable {

    private static final long serialVersionUID = 85867598630282408L;

    /**
     * 设置作业时传入的参数，key:value;
     */
    public HashMap<String ,String> dataMap = null;

    public HashMap<String, String> getDataMap() {
        return dataMap;
    }

    public void setDataMap(HashMap<String, String> dataMap) {
        this.dataMap = dataMap;
    }

}
