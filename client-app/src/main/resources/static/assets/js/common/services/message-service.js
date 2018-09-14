'use strict';

angular.module('secure-messaging-app.message-service', [
	'secure-messaging-app.util',
	'ngResource'
])

.factory('messageEndpoints', ['$resource', 'BASE_API_ENDPOINT', function($resource, BASE_API_ENDPOINT) {
	var messageEndpoints =  {
		resource : $resource(BASE_API_ENDPOINT + 'messages/:id', { id : '@id' }),

		inbox : $resource(BASE_API_ENDPOINT + 'messages/inbox', {}, {
			query: {
				method : 'GET',
				isArray: true
			}
		}),

		sent : $resource(BASE_API_ENDPOINT + 'messages/sent', {}, {
			query: {
				method : 'GET',
				isArray: true
			}
		})

	};

	return messageEndpoints;

}])

.factory('messageService', ['$rootScope', '$location', 'messageEndpoints', 'commonService', 'alertService', function ($rootScope, $location, messageEndpoints, commonService, alertService) {
	var viewMessage = function(message) {
		console.log("***** View Message: " + JSON.stringify(message));
		commonService.setProperty(commonService.CURRENT_MESSAGE_KEY, message);
		$location.path('/view');
		$rootScope.$broadcast(commonService.EVENT_TYPES.CURRENT_MESSAGE_CHANGE_EVENT);
	};

	var deleteMessage = function(message, callback) {
		console.log("***** Delete Message: " + JSON.stringify(message));
		messageEndpoints.resource.delete({ id: message.id })
			.$promise
			.then(function(result) {
				console.log("***** Message Deleted");
				commonService.setProperty(commonService.CURRENT_MESSAGE_KEY, null);
				$rootScope.$broadcast(commonService.EVENT_TYPES.CURRENT_MESSAGE_CHANGE_EVENT);
				callback(message, true);
				alertService.openModal({title : "Success", message : "The message was successfully deleted."});
			})
			.catch(function(error) {
				console.log("***** Error Deleting Message: " + JSON.stringify(error.data));
				callback(message, false);
				alertService.openModal({title : "Error", message : "An error occurred while attempting to delete the message."});
			});
	};

	return {
		viewMessage: viewMessage,
		deleteMessage: deleteMessage
	}
}])
;
