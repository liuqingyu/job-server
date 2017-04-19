/**
 * Created by Administrator on 2016/9/5.
 */
(function() {
    'use strict';

    angular
        .module("jobserverApp")
        .factory("CommonService",CommonService);

    CommonService.$inject = ['$q','JOB_GROUP','IS_CONCURRENT','TRIGGER_TYPE','BATCH_STATUS', 'TRIGGER_STATUS'];

    function CommonService($q,JOB_GROUP,IS_CONCURRENT,TRIGGER_TYPE,BATCH_STATUS,TRIGGER_STATUS) {
        var service = {
            JOB_GROUP: JOB_GROUP,
            IS_CONCURRENT: IS_CONCURRENT,
            BATCH_STATUS: BATCH_STATUS,
            TRIGGER_TYPE: TRIGGER_TYPE,
            TRIGGER_STATUS: TRIGGER_STATUS
        };

        return service;
    }
})();
