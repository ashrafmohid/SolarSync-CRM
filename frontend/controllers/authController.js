angular.module('solarCRM')
    .controller('AuthController', function (ApiService, $location) {
        const vm = this;

        // Registration data
        vm.registerData = {
            username: '',
            password: ''
        };
        vm.registerMessage = '';

        // Login data
        vm.loginData = {
            username: '',
            password: ''
        };
        vm.loginMessage = '';

        // Register a new user
        vm.register = function () {
            ApiService.register(vm.registerData.username, vm.registerData.password)
                .then(function (response) {
                    vm.registerMessage = 'Registration successful!';
                })
                .catch(function (error) {
                    vm.registerMessage = 'Registration failed: ' + error.data;
                });
        };

        // Log in a user
        vm.login = function () {
            ApiService.login(vm.loginData.username, vm.loginData.password)
                .then(function (response) {
                    vm.loginMessage = 'Login successful!';
                    localStorage.setItem('token', response.data.token); // Store token
                    $location.path('/dashboard'); // Redirect to dashboard
                })
                .catch(function (error) {
                    vm.loginMessage = 'Login failed: ' + error.data;
                });
        };

        // Log out a user
        vm.logout = function () {
            localStorage.removeItem('token'); // Clear token
            vm.loginMessage = 'Logged out successfully!';
            $location.path('/login'); // Redirect to login
        };
    });