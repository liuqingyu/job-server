(function() {
    'use strict';
    angular
        .module('jobserverApp')
        .factory('QrtzSystemOperlog', QrtzSystemOperlog);

    QrtzSystemOperlog.$inject = ['$resource', 'DateUtils'];

    function QrtzSystemOperlog ($resource, DateUtils) {
        var resourceUrl =  'api/qrtz-system-operlogs/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                        data.startTime = DateUtils.convertDateTimeFromServer(data.startTime);
                        data.endTime = DateUtils.convertDateTimeFromServer(data.endTime);
                    }
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();
