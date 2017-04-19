(function() {
    'use strict';

    angular
        .module('jobserverApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('qrtz-system-operlog', {
            parent: 'entity',
            url: '/qrtz-system-operlog?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'jobserverApp.qrtzSystemOperlog.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/qrtz-system-operlog/qrtz-system-operlogs.html',
                    controller: 'QrtzSystemOperlogController',
                    controllerAs: 'vm'
                }
            },
            params: {
                page: {
                    value: '1',
                    squash: true
                },
                sort: {
                    value: 'id,asc',
                    squash: true
                },
                search: null
            },
            resolve: {
                pagingParams: ['$stateParams', 'PaginationUtil', function ($stateParams, PaginationUtil) {
                    return {
                        page: PaginationUtil.parsePage($stateParams.page),
                        sort: $stateParams.sort,
                        predicate: PaginationUtil.parsePredicate($stateParams.sort),
                        ascending: PaginationUtil.parseAscending($stateParams.sort),
                        search: $stateParams.search
                    };
                }],
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('qrtzSystemOperlog');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('qrtz-system-operlog-detail', {
            parent: 'entity',
            url: '/qrtz-system-operlog/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'jobserverApp.qrtzSystemOperlog.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/qrtz-system-operlog/qrtz-system-operlog-detail.html',
                    controller: 'QrtzSystemOperlogDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('qrtzSystemOperlog');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'QrtzSystemOperlog', function($stateParams, QrtzSystemOperlog) {
                    return QrtzSystemOperlog.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'qrtz-system-operlog',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('qrtz-system-operlog-detail.edit', {
            parent: 'qrtz-system-operlog-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/qrtz-system-operlog/qrtz-system-operlog-dialog.html',
                    controller: 'QrtzSystemOperlogDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['QrtzSystemOperlog', function(QrtzSystemOperlog) {
                            return QrtzSystemOperlog.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('qrtz-system-operlog.new', {
            parent: 'qrtz-system-operlog',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/qrtz-system-operlog/qrtz-system-operlog-dialog.html',
                    controller: 'QrtzSystemOperlogDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                operParam: null,
                                operResult: null,
                                operType: null,
                                startTime: null,
                                endTime: null,
                                duration: null,
                                success: null,
                                triggerName: null,
                                userId: null,
                                userName: null,
                                beanClass: null,
                                methodName: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('qrtz-system-operlog', null, { reload: 'qrtz-system-operlog' });
                }, function() {
                    $state.go('qrtz-system-operlog');
                });
            }]
        })
        .state('qrtz-system-operlog.edit', {
            parent: 'qrtz-system-operlog',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/qrtz-system-operlog/qrtz-system-operlog-dialog.html',
                    controller: 'QrtzSystemOperlogDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['QrtzSystemOperlog', function(QrtzSystemOperlog) {
                            return QrtzSystemOperlog.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('qrtz-system-operlog', null, { reload: 'qrtz-system-operlog' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('qrtz-system-operlog.delete', {
            parent: 'qrtz-system-operlog',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/qrtz-system-operlog/qrtz-system-operlog-delete-dialog.html',
                    controller: 'QrtzSystemOperlogDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['QrtzSystemOperlog', function(QrtzSystemOperlog) {
                            return QrtzSystemOperlog.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('qrtz-system-operlog', null, { reload: 'qrtz-system-operlog' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
