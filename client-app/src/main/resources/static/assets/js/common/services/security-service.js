'use strict';

angular.module('secure-messaging-app.security-service', [
	'secure-messaging-app.util',
	'ngResource'
])

.factory('securityEndpoints', ['$resource', 'BASE_API_ENDPOINT', function($resource, BASE_API_ENDPOINT) {
	var securityEndpoints =  {
		currentPrincipal : $resource(BASE_API_ENDPOINT + 'principal', {}, {
			query: {
				method : 'GET',
				isArray: false
			}
		}),

		logout : $resource(BASE_API_ENDPOINT + 'logout')
	};

	return securityEndpoints;

}])

.factory('securityService', ['$rootScope', '$location', 'securityEndpoints', 'commonService', 'alertService', function ($rootScope, $location, securityEndpoints, commonService, alertService) {
	var currentPrincipal = function() {
		console.log("***** Current Principal");
		securityEndpoints.currentPrincipal.query()
			.$promise
			.then(function(result) {
				commonService.setProperty(commonService.CURRENT_PRINCIPAL_KEY, result);
				$rootScope.$broadcast(commonService.EVENT_TYPES.CURRENT_PRINCIPAL_CHANGE_EVENT);
			})
			.catch(function(error) {
				console.log("***** Error Current Principal: " + JSON.stringify(error.data));
				alertService.openModal({title : "Error", message : "An error occurred while attempting to retrieve the current principal."});
			});
	};

	var logout = function() {
		console.log("***** Logout");
		securityEndpoints.logout.save()
			.$promise
			.then(function(result) {
				console.log("***** Logout Success");
				commonService.setProperty(commonService.CURRENT_PRINCIPAL_KEY, null);
				$rootScope.$broadcast(commonService.EVENT_TYPES.CURRENT_PRINCIPAL_CHANGE_EVENT);
				window.location.href = "/";
			})
			.catch(function(error) {
				console.log("***** Logout Failed: " + JSON.stringify(error.data));
				alertService.openModal({title : "Error", message : "An error occurred while attempting to logout."});
			});
	};

	return {
		currentPrincipal: currentPrincipal,
		logout: logout
	}
}])
;