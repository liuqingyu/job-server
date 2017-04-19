(function() {
    'use strict';

    angular
        .module('jobserverApp')
        .factory('QrtzSystemOperlogSearch', QrtzSystemOperlogSearch);

    QrtzSystemOperlogSearch.$inject = ['$resource'];

    function QrtzSystemOperlogSearch($resource) {
        var resourceUrl =  'api/_search/qrtz-system-operlogs/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
