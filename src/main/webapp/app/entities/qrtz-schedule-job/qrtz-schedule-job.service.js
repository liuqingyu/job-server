(function() {
    'use strict';
    angular
        .module('jobserverApp')
        .factory('QrtzScheduleJob', QrtzScheduleJob);

    QrtzScheduleJob.$inject = ['$resource', 'DateUtils'];

    function QrtzScheduleJob ($resource, DateUtils) {
        var resourceUrl =  'api/qrtz-schedule-jobs/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                        data.batchTime = DateUtils.convertDateTimeFromServer(data.batchTime);
                        data.startDate = DateUtils.convertDateTimeFromServer(data.startDate);
                        data.endDate = DateUtils.convertDateTimeFromServer(data.endDate);
                        data.createTime = DateUtils.convertDateTimeFromServer(data.createTime);
                        data.updateTime = DateUtils.convertDateTimeFromServer(data.updateTime);
                    }
                    return data;
                }
            },
            'update': { method:'PUT'},
            'pause': { method:'GET' ,url:'api/qrtz-schedule-jobs/pause/:id' },
            'resume': { method:'GET', url:'api/qrtz-schedule-jobs/resume/:id'},
            'run': { method:'GET', url:'api/qrtz-schedule-jobs/run/:id'}
        });
    }
})();
