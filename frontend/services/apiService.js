// services/apiService.js
angular.module('solarCRM')
    .service('ApiService', function ($http) {
        const baseUrl = 'http://localhost:8080/api/auth';

        this.register = function (username, password) {
            return $http.post(`${baseUrl}/register`, null, {
                params: { username, password }
            });
        };

        this.login = function (username, password) {
            return $http.post(`${baseUrl}/login`, null, {
                params: { username, password }
            });
        };
    });