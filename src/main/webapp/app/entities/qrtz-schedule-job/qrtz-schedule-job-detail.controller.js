(function() {
    'use strict';

    angular
        .module('jobserverApp')
        .controller('QrtzScheduleJobDetailController', QrtzScheduleJobDetailController);

    QrtzScheduleJobDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'QrtzScheduleJob', 'CommonService'];

    function QrtzScheduleJobDetailController($scope, $rootScope, $stateParams, previousState, entity, QrtzScheduleJob, CommonService) {
        var vm = this;

        vm.qrtzScheduleJob = entity;
        vm.previousState = previousState.name;

        $scope.jobGroup = CommonService.JOB_GROUP;
        $scope.isConcurrent = CommonService.IS_CONCURRENT;
        $scope.triggerType = CommonService.TRIGGER_TYPE;
        $scope.batchStatus = CommonService.BATCH_STATUS;
        $scope.triggerStatus = CommonService.TRIGGER_STATUS;

        var unsubscribe = $rootScope.$on('jobserverApp:qrtzScheduleJobUpdate', function(event, result) {
            vm.qrtzScheduleJob = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
