'use strict';

angular.module('secure-messaging-app', [
  	'secure-messaging-app.router',
	'secure-messaging-app.security-service',
	'secure-messaging-app.util',
  	'ui.bootstrap',
  	'underscore'
])

.factory('responseInterceptor', ['$q', '$rootScope', '$location', function($q, $rootScope, $location) {
	var responseError = function(response) {
		if (response.status >= 400) {
			var errorMessage = "An error occurred: [Status " + response.status + "] " + response.statusText;
            console.log("***** " + errorMessage);
			return $q.reject(response);
		}
		return response;
	};

	return {
		responseError: responseError
	}
}])

.config(['$httpProvider', function($httpProvider) {
	$httpProvider.defaults.headers.common['X-Requested-With'] = 'XMLHttpRequest';
	$httpProvider.interceptors.push('responseInterceptor');
}])

.constant('BASE_API_ENDPOINT', '')

.controller('appController', ['$scope', '$location', 'commonService', 'securityService', function ($scope, $location, commonService, securityService) {
	$scope.updateCurrentActiveLink = function (clickedLink) {
		$scope.currentActiveLink = clickedLink;
	};

	$scope.isCurrentActiveLink = function (link) {
		return $scope.currentActiveLink === link;
	};

	$scope.$on(commonService.EVENT_TYPES.CURRENT_PRINCIPAL_CHANGE_EVENT, function() {
		console.log("***** " + commonService.EVENT_TYPES.CURRENT_PRINCIPAL_CHANGE_EVENT);
		$scope.currentPrincipal = commonService.getProperty(commonService.CURRENT_PRINCIPAL_KEY);
	});

	$scope.getCurrentPrincipal = function() {
		securityService.currentPrincipal();
	};

	$scope.logout = function() {
		securityService.logout();
	};

	$scope.goTo = function(view) {
		$location.path(view);
	};

	var init = function() {
		$scope.getCurrentPrincipal();
	};

	init();

}])

.directive('headerDirective', function() {
	return {
		templateUrl : 'assets/js/common/directives/header/header.tpl.html'
  	};
})
;