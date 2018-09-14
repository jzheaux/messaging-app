'use strict'

angular.module('secure-messaging-app.router', ['ngRoute',
    'secure-messaging-app.message-controllers'
])

.config(['$routeProvider', function($routeProvider) {
	$routeProvider

    .when('/login', {
        templateUrl : 'assets/js/common/partials/login.tpl.html',
        controller  : 'loginController'
    })

    .when('/inbox', {
        templateUrl : 'assets/js/app/message/message-list.tpl.html',
        controller  : 'inboxMessagesController'
    })

    .when('/sent', {
        templateUrl : 'assets/js/app/message/message-list.tpl.html',
        controller  : 'sentMessagesController'
    })

    .when('/compose', {
        templateUrl : 'assets/js/app/message/message-compose.tpl.html',
        controller  : 'composeMessageController'
    })

    .when('/view', {
        templateUrl : 'assets/js/app/message/message-view.tpl.html',
        controller  : 'viewMessageController'
    })

	.otherwise({redirectTo:'/inbox'});
}])
;