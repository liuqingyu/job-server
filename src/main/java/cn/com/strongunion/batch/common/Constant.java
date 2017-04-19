package cn.com.strongunion.batch.common;

public class Constant {

    //日期格式
    public static final String YYYYMMDD ="yyyy-MM-dd";
    //scheduleJob在dataMap中的Key
    public static final String SCHEDULE_JOB = "scheduleJob";

    public static final String EXEC_TYPE_PAUSE="pause";

    public static final String EXEC_TYPE_RESUEM="resume";

    /** 任务调度的参数key */
    public static final String JOB_PARAM_KEY    = "jobParam";
    public static final String JOB_MAP_KEY = "dataMap";

    /**设置执行计划的类型**/
    public static final String TRIGGER_TYPE_CRON="cron";
    public static final String TRIGGER_TYPE_REDO="redo";

    public static final String JOB_STATUS_NOMAL="0";
    public static final String JOB_STATUS_PAUSE="1";

}
