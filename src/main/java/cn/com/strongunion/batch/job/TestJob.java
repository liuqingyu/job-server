package cn.com.strongunion.batch.job;

import cn.com.strongunion.batch.common.BaseJob;
import cn.com.strongunion.batch.security.CustomAccessDeniedHandler;
import cn.com.strongunion.batch.web.rest.errors.CustomParameterizedException;
import org.springframework.stereotype.Service;

import java.util.HashMap;

/**
 * Created by Administrator on 2016/9/6.
 */
@Service
public class TestJob extends BaseJob {
    public void run () {
        System.out.println("Hello World!");
        HashMap<String, String> hm = this.getDataMap();
        System.out.println(hm.get("name"));
        System.out.println("finished");
    }
}
