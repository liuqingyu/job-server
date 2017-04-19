(function() {
    'use strict';

    angular
        .module('jobserverApp')
        .controller('QrtzSystemOperlogDetailController', QrtzSystemOperlogDetailController);

    QrtzSystemOperlogDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'QrtzSystemOperlog', 'QrtzScheduleJob'];

    function QrtzSystemOperlogDetailController($scope, $rootScope, $stateParams, previousState, entity, QrtzSystemOperlog, QrtzScheduleJob) {
        var vm = this;

        vm.qrtzSystemOperlog = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('jobserverApp:qrtzSystemOperlogUpdate', function(event, result) {
            vm.qrtzSystemOperlog = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
