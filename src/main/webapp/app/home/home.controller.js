(function() {
    'use strict';

    angular
        .module('jobserverApp')
        .controller('HomeController', HomeController);

    HomeController.$inject = ['$scope', 'Principal', 'LoginService', '$state', 'Auth'];

    function HomeController ($scope, Principal, LoginService, $state, Auth) {
        var vm = this;

        vm.account = null;
        vm.isAuthenticated = null;
        //vm.login = LoginService.open;

        vm.authenticationError = false;
        vm.login = login;
        vm.password = null;
        vm.rememberMe = true;
        vm.username = null;

        vm.register = register;
        $scope.$on('authenticationSuccess', function() {
            getAccount();
        });

        getAccount();

        function getAccount() {
            Principal.identity().then(function(account) {
                vm.account = account;
                vm.isAuthenticated = Principal.isAuthenticated;
            });
        }
        function register () {
            $state.go('register');
        }

        function login (event) {
            event.preventDefault();
            Auth.login({
                username: vm.username,
                password: vm.password,
                rememberMe: vm.rememberMe
            }).then(function () {
                vm.authenticationError = false;
                getAccount();
                $state.go('home');
            }).catch(function () {
                vm.authenticationError = true;
            });
        }
    }
})();
