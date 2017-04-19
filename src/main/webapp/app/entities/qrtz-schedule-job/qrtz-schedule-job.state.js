(function() {
    'use strict';

    angular
        .module('jobserverApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('qrtz-schedule-job', {
            parent: 'entity',
            url: '/qrtz-schedule-job?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'jobserverApp.qrtzScheduleJob.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/qrtz-schedule-job/qrtz-schedule-jobs.html',
                    controller: 'QrtzScheduleJobController',
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
                    $translatePartialLoader.addPart('qrtzScheduleJob');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('qrtz-schedule-job-detail', {
            parent: 'entity',
            url: '/qrtz-schedule-job/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'jobserverApp.qrtzScheduleJob.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/qrtz-schedule-job/qrtz-schedule-job-detail.html',
                    controller: 'QrtzScheduleJobDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('qrtzScheduleJob');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'QrtzScheduleJob', function($stateParams, QrtzScheduleJob) {
                    return QrtzScheduleJob.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'qrtz-schedule-job',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('qrtz-schedule-job-detail.edit', {
            parent: 'qrtz-schedule-job-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/qrtz-schedule-job/qrtz-schedule-job-dialog.html',
                    controller: 'QrtzScheduleJobDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['QrtzScheduleJob', function(QrtzScheduleJob) {
                            return QrtzScheduleJob.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('qrtz-schedule-job.new', {
            parent: 'qrtz-schedule-job',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/qrtz-schedule-job/qrtz-schedule-job-dialog.html',
                    controller: 'QrtzScheduleJobDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                jobName: null,
                                jobGroup: null,
                                triggerType: null,
                                cronExpression: null,
                                beanClass: null,
                                methodName: null,
                                parameterJson: null,
                                isConcurrent: null,
                                batchStatus: null,
                                batchTime: null,
                                description: null,
                                startDate: null,
                                endDate: null,
                                createTime: null,
                                createBy: null,
                                updateTime: null,
                                updateBy: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('qrtz-schedule-job', null, { reload: 'qrtz-schedule-job' });
                }, function() {
                    $state.go('qrtz-schedule-job');
                });
            }]
        })
        .state('qrtz-schedule-job.edit', {
            parent: 'qrtz-schedule-job',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/qrtz-schedule-job/qrtz-schedule-job-dialog.html',
                    controller: 'QrtzScheduleJobDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['QrtzScheduleJob', function(QrtzScheduleJob) {
                            return QrtzScheduleJob.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('qrtz-schedule-job', null, { reload: 'qrtz-schedule-job' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('qrtz-schedule-job.delete', {
            parent: 'qrtz-schedule-job',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/qrtz-schedule-job/qrtz-schedule-job-delete-dialog.html',
                    controller: 'QrtzScheduleJobDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['QrtzScheduleJob', function(QrtzScheduleJob) {
                            return QrtzScheduleJob.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('qrtz-schedule-job', null, { reload: 'qrtz-schedule-job' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('qrtz-schedule-job.pause', {
            parent: 'qrtz-schedule-job',
            url: '/pause/{id}',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/qrtz-schedule-job/qrtz-schedule-job-pause-dialog.html',
                    controller: 'QrtzScheduleJobPauseController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['QrtzScheduleJob', function(QrtzScheduleJob) {
                            return QrtzScheduleJob.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('qrtz-schedule-job', null, { reload: 'qrtz-schedule-job' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('qrtz-schedule-job.resume', {
            parent: 'qrtz-schedule-job',
            url: '/resume/{id}',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/qrtz-schedule-job/qrtz-schedule-job-resume-dialog.html',
                    controller: 'QrtzScheduleJobResumeController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['QrtzScheduleJob', function(QrtzScheduleJob) {
                            return QrtzScheduleJob.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('qrtz-schedule-job', null, { reload: 'qrtz-schedule-job' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('qrtz-schedule-job.run', {
            parent: 'qrtz-schedule-job',
            url: '/run/{id}',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/qrtz-schedule-job/qrtz-schedule-job-run-dialog.html',
                    controller: 'QrtzScheduleJobRunController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['QrtzScheduleJob', function(QrtzScheduleJob) {
                            return QrtzScheduleJob.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('qrtz-schedule-job', null, { reload: 'qrtz-schedule-job' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
