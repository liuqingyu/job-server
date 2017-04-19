package cn.com.strongunion.batch.common.utils;

import cn.com.strongunion.batch.common.Constant;
import cn.com.strongunion.batch.domain.QrtzScheduleJob;
import cn.com.strongunion.batch.web.rest.errors.CustomParameterizedException;
import org.apache.commons.lang.StringUtils;
import org.apache.el.util.ReflectionUtil;
import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;

import java.lang.reflect.Method;
import java.util.HashMap;


public class TaskUtils {
    public final static Logger log = Logger.getLogger(TaskUtils.class);

    /**
     * 通过反射调用scheduleJob中定义的方法
     *
     * @param scheduleJob
     */
    @SuppressWarnings({ "rawtypes", "checked"})
    public static void invokMethod(QrtzScheduleJob scheduleJob, ApplicationContext applicationContext) {
        Object object = null;
        Class clazz = null;
        if (StringUtils.isNotBlank(scheduleJob.getBeanClass())) {
            try {
                System.out.println(scheduleJob.getBeanClass());
                clazz = ReflectionUtil.forName(scheduleJob.getBeanClass());
                object = applicationContext.getBean(clazz);
                if (object == null) {
                    object = clazz.newInstance();
                }
            } catch (Exception e) {
                throw new CustomParameterizedException("任务名称 = [" + scheduleJob.getJobName() + "] 未启动成功: " + e.getMessage() + e.getCause());
            }
        }
        Method method;
        try {
            if (StringUtils.isNotBlank(scheduleJob.getParameterJson())){
                String getParams[] = scheduleJob.getParameterJson().replaceAll("\r|\n", "").split(";");
                HashMap<String,String> parameterMap =  new HashMap<String,String>();
                String spitParamKey;
                String spitParamValue;
                for (String getParam : getParams) {
                    spitParamKey = getParam;
                    spitParamValue = getParam;
                    int idx = spitParamKey.indexOf("=");
                    spitParamKey = spitParamKey.substring(0, idx);
                    int idx1 = spitParamValue.indexOf("=");
                    spitParamValue = spitParamValue.substring(idx1 + 1, spitParamValue.length());
                    log.debug("【spitParamKey=】" + spitParamKey);
                    log.debug("【spitParamValue=】" + spitParamValue);
                    parameterMap.put(spitParamKey.trim(), spitParamValue.trim());
                }
                ReflectUtil.setFieldValue(object, Constant.JOB_MAP_KEY, parameterMap);
            }
            method = clazz.getDeclaredMethod(scheduleJob.getMethodName());
        } catch (Exception e) {
            throw new CustomParameterizedException("任务名称 = [" + scheduleJob.getJobName() + "] 未启动成功: " +  e.getMessage() + e.getCause());
        }

        if (method != null) {
            try {
                method.invoke(object);
            } catch (Exception e) {
                e.printStackTrace();
                throw new CustomParameterizedException("任务名称 = [" + scheduleJob.getJobName() + "] 未启动成功：" + e.getMessage() + e.getCause());
            }
        }
        log.info("任务名称 = [" + scheduleJob.getJobName() + "]----------启动成功");
    }
}
