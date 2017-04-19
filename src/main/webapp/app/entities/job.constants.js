/**
 * Created by Administrator on 2016/9/1.
 */
(function () {
    'use strict';
    angular
        .module('jobserverApp')
        .constant('JOB_GROUP', {
            "remoteBatch" : "系统任务",
            "dataBatch" : "数据任务",
            "linkBatch" : "重做任务"
        })
        .constant('IS_CONCURRENT', {
            0: "否",
            1: "是"
        })
        .constant('TRIGGER_TYPE', {
            'cron': "正常批",
            'redo': "重跑批"
        })
        .constant('BATCH_STATUS', {
            '0': "正常",
            '1': "暂停"
        })
        .constant("TRIGGER_STATUS", {
            "NONE": "无",
            "NORMAL": "正常",
            "PAUSED": "暂停",
            "COMPLETE": "完成",
            "ERROR": "错误",
            "BLOCKED": "阻塞"
        });
})();
