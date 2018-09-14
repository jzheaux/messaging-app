'use strict'

angular.module('secure-messaging-app.message-controllers', [
	'secure-messaging-app.message-service',
	'secure-messaging-app.user-service',
	'secure-messaging-app.router',
	'secure-messaging-app.util',
	'ngAnimate',
	'underscore',
	'ui.bootstrap'
])

.controller('inboxMessagesController', ['$scope', '$rootScope', '$location', 'messageEndpoints', 'messageService', 'commonService', 'alertService', function ($scope, $rootScope, $location, messageEndpoints, messageService, commonService, alertService) {

	$scope.messageListType = "Inbox";

	$scope.viewMessage = function(message) {
		messageService.viewMessage(message);
	};

	$scope.deleteMessage = function(message) {
		messageService.deleteMessage(message, function(message, deleted) {
			if (deleted) {
				$scope.messages = _.reject($scope.messages, function(msg) {
					return msg.id == message.id
				});
			}
		});
	};

	$scope.getMessages = function() {
		messageEndpoints.inbox.query()
			.$promise
			.then(function(result) {
				$scope.messages = result;
			})
			.catch(function(error) {
				// console.log("***** Error retrieving inbox messages: " + JSON.stringify(error.data));
				// alertService.openModal({title : "Error", message : "An error occurred while retrieving inbox messages."});
			});
	};

	var init = function() {
		$scope.getMessages();
	};

	init();
}])

.controller('sentMessagesController', ['$scope', '$rootScope', '$location', 'messageEndpoints', 'messageService', 'commonService', 'alertService', function ($scope, $rootScope, $location, messageEndpoints, messageService, commonService, alertService) {

	$scope.messageListType = "Sent";

	$scope.viewMessage = function(message) {
		messageService.viewMessage(message);
	};

	$scope.deleteMessage = function(message) {
		messageService.deleteMessage(message, function(message, deleted) {
			if (deleted) {
				$scope.messages = _.reject($scope.messages, function(msg) {
					return msg.id == message.id
				});
			}
		});
	};

	$scope.getMessages = function() {
		messageEndpoints.sent.query()
			.$promise
			.then(function(result) {
				$scope.messages = result;
			})
			.catch(function(error) {
				// console.log("***** Error retrieving sent messages: " + JSON.stringify(error.data));
				// alertService.openModal({title : "Error", message : "An error occurred while retrieving sent messages."});
			});
	};

	var init = function() {
		$scope.getMessages();
	};

	init();
}])

.controller('viewMessageController', ['$scope', 'commonService', function ($scope, commonService) {

	$scope.$on(commonService.EVENT_TYPES.CURRENT_MESSAGE_CHANGE_EVENT, function() {
		console.log("***** " + commonService.EVENT_TYPES.CURRENT_MESSAGE_CHANGE_EVENT);
		init();
	});

	var init = function() {
		$scope.currentMessage = commonService.getProperty(commonService.CURRENT_MESSAGE_KEY);
		$scope.lastView = $scope.currentActiveLink
	};

	init();
}])

.controller('composeMessageController', ['$scope', '$rootScope', '$location', 'userEndpoint', 'messageEndpoints', 'messageService', 'commonService', 'alertService', function ($scope, $rootScope, $location, userEndpoint, messageEndpoints, messageService, commonService, alertService) {

	$scope.getAllUsers = function() {
		userEndpoint.query()
			.$promise
			.then(function(result) {
				$scope.users = result;
			})
			.catch(function(error) {
				// console.log("***** Error retrieving the list of users: " + JSON.stringify(error.data));
				// alertService.openModal({title : "Error", message : "An error occurred while retrieving the list of users."});
			});
	};

	$scope.sendMessage = function() {
		console.log("***** Send Message: " + JSON.stringify($scope.newMessage));
		messageEndpoints.resource.save({}, $scope.newMessage)
			.$promise
			.then(function(result) {
				console.log("***** Message Sent");
				alertService.openModal({title : "Success", message : "The message was successfully sent."});
				$location.path('/sent');
			})
			.catch(function(error) {
				console.log("***** Error Sending Message: " + JSON.stringify(error.data));
				alertService.openModal({title : "Error", message : "An error occurred while attempting to send the message."});
			});

	};

	var init = function() {
		$scope.newMessage = {};
		$scope.getAllUsers();
	};

	init();
}]);
