(function() {
    'use strict';

    angular
        .module('jobserverApp')
        .factory('QrtzScheduleJobSearch', QrtzScheduleJobSearch);

    QrtzScheduleJobSearch.$inject = ['$resource'];

    function QrtzScheduleJobSearch($resource) {
        var resourceUrl =  'api/_search/qrtz-schedule-jobs/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
