// app.js
angular.module('solarCRM', ['ngRoute'])
    .config(function ($routeProvider) {
        $routeProvider
            .when('/dashboard', {
                template: '<h1>Dashboard</h1>',
                controller: 'DashboardController',
                controllerAs: 'dashboardCtrl',
                resolve: {
                    auth: function ($location, $localStorage) {
                        if (!$localStorage.token) {
                            $location.path('/login'); // Redirect to login if not authenticated
                        }
                    }
                }
            })
            .when('/login', {
                template: '<h1>Login</h1>',
                controller: 'AuthController',
                controllerAs: 'authCtrl'
            })
            .otherwise({ redirectTo: '/login' });
    });