(function() {
    'use strict';

    angular
        .module('jobserverApp')
        .controller('QrtzScheduleJobPauseController',QrtzScheduleJobPauseController);

    QrtzScheduleJobPauseController.$inject = ['$uibModalInstance', 'entity', 'QrtzScheduleJob'];

    function QrtzScheduleJobPauseController($uibModalInstance, entity, QrtzScheduleJob) {
        var vm = this;

        vm.qrtzScheduleJob = entity;
        vm.clear = clear;
        vm.confirmPause = confirmPause;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmPause (id) {
            QrtzScheduleJob.pause(vm.qrtzScheduleJob,
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
