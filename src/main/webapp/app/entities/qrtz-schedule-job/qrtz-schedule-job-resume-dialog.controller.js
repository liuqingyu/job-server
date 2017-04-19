(function() {
    'use strict';

    angular
        .module('jobserverApp')
        .controller('QrtzScheduleJobResumeController',QrtzScheduleJobResumeController);

    QrtzScheduleJobResumeController.$inject = ['$uibModalInstance', 'entity', 'QrtzScheduleJob'];

    function QrtzScheduleJobResumeController($uibModalInstance, entity, QrtzScheduleJob) {
        var vm = this;

        vm.qrtzScheduleJob = entity;
        vm.clear = clear;
        vm.confirmResume = confirmResume;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmResume (id) {
            QrtzScheduleJob.resume({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
