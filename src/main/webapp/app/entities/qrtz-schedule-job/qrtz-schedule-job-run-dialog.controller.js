(function() {
    'use strict';

    angular
        .module('jobserverApp')
        .controller('QrtzScheduleJobRunController',QrtzScheduleJobRunController);

    QrtzScheduleJobRunController.$inject = ['$uibModalInstance', 'entity', 'QrtzScheduleJob'];

    function QrtzScheduleJobRunController($uibModalInstance, entity, QrtzScheduleJob) {
        var vm = this;

        vm.qrtzScheduleJob = entity;
        vm.clear = clear;
        vm.confirmRun = confirmRun;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmRun (id) {
            QrtzScheduleJob.run({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
