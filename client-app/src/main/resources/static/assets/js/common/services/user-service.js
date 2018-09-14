'use strict';

angular.module('secure-messaging-app.user-service', ['ngResource'])

.factory('userEndpoint', ['$resource', 'BASE_API_ENDPOINT', function($resource, BASE_API_ENDPOINT) {
	var userEndpoint = $resource(BASE_API_ENDPOINT + 'users/:id', { id : '@id' });

	return userEndpoint;

}])
;