(function() {
    'use strict';

    angular
        .module('jobserverApp')
        .controller('QrtzSystemOperlogDialogController', QrtzSystemOperlogDialogController);

    QrtzSystemOperlogDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'QrtzSystemOperlog', 'QrtzScheduleJob'];

    function QrtzSystemOperlogDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, QrtzSystemOperlog, QrtzScheduleJob) {
        var vm = this;

        vm.qrtzSystemOperlog = entity;
        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.save = save;
        vm.qrtzschedulejobs = QrtzScheduleJob.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.qrtzSystemOperlog.id !== null) {
                QrtzSystemOperlog.update(vm.qrtzSystemOperlog, onSaveSuccess, onSaveError);
            } else {
                QrtzSystemOperlog.save(vm.qrtzSystemOperlog, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('jobserverApp:qrtzSystemOperlogUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }

        vm.datePickerOpenStatus.startTime = false;
        vm.datePickerOpenStatus.endTime = false;

        function openCalendar (date) {
            vm.datePickerOpenStatus[date] = true;
        }
    }
})();
