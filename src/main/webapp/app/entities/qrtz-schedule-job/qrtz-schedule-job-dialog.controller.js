(function() {
    'use strict';

    angular
        .module('jobserverApp')
        .controller('QrtzScheduleJobDialogController', QrtzScheduleJobDialogController);

    QrtzScheduleJobDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'QrtzScheduleJob', 'CommonService'];

    function QrtzScheduleJobDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, QrtzScheduleJob, CommonService) {
        var vm = this;

        vm.qrtzScheduleJob = entity;
        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.save = save;
        $scope.jobGroup = CommonService.JOB_GROUP;
        $scope.isConcurrent = CommonService.IS_CONCURRENT;
        $scope.triggerType = CommonService.TRIGGER_TYPE;
        $scope.batchStatus = CommonService.BATCH_STATUS;

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.qrtzScheduleJob.id !== null) {
                QrtzScheduleJob.update(vm.qrtzScheduleJob, onSaveSuccess, onSaveError);
            } else {
                QrtzScheduleJob.save(vm.qrtzScheduleJob, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('jobserverApp:qrtzScheduleJobUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }

        vm.datePickerOpenStatus.batchTime = false;
        vm.datePickerOpenStatus.startDate = false;
        vm.datePickerOpenStatus.endDate = false;
        vm.datePickerOpenStatus.createTime = false;
        vm.datePickerOpenStatus.updateTime = false;

        function openCalendar (date) {
            vm.datePickerOpenStatus[date] = true;
        }
    }
})();
