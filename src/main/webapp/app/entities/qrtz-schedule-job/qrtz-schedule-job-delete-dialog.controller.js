(function() {
    'use strict';

    angular
        .module('jobserverApp')
        .controller('QrtzScheduleJobDeleteController',QrtzScheduleJobDeleteController);

    QrtzScheduleJobDeleteController.$inject = ['$uibModalInstance', 'entity', 'QrtzScheduleJob'];

    function QrtzScheduleJobDeleteController($uibModalInstance, entity, QrtzScheduleJob) {
        var vm = this;

        vm.qrtzScheduleJob = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            QrtzScheduleJob.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
