(function() {
    'use strict';

    angular
        .module('jobserverApp')
        .controller('QrtzSystemOperlogDeleteController',QrtzSystemOperlogDeleteController);

    QrtzSystemOperlogDeleteController.$inject = ['$uibModalInstance', 'entity', 'QrtzSystemOperlog'];

    function QrtzSystemOperlogDeleteController($uibModalInstance, entity, QrtzSystemOperlog) {
        var vm = this;

        vm.qrtzSystemOperlog = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            QrtzSystemOperlog.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
