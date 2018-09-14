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

		logout : $resource(BASE_API_ENDPOINT + 'logout'),

		login : $resource(BASE_API_ENDPOINT + 'login?username=:username&password=:password', { username : '@username', password : '@password' })
	};

	return securityEndpoints;

}])

.factory('basicAuthEndpoint', function ($resource) {
	return function(authHeader) {
		return $resource('/principal', {}, {
			get: {
				method: 'GET',
				params: {},
				isArray: false,
				headers: authHeader || {}
			}
		});
	};
})

.factory('securityService', ['$rootScope', '$location', 'securityEndpoints', 'commonService', 'alertService', 'basicAuthEndpoint', 'base64', function ($rootScope, $location, securityEndpoints, commonService, alertService, basicAuthEndpoint, base64) {
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

	var login = function(auth, callback) {
		console.log("***** Login");
		var basicAuthHeader = {"Authorization" : "Basic " + base64.encode(auth.username + ":" + auth.password)};
		basicAuthEndpoint(basicAuthHeader).get(
			function(result) {
				console.log("***** Login Success");
				callback(result, true);
				commonService.setProperty(commonService.CURRENT_PRINCIPAL_KEY, result);
				$rootScope.$broadcast(commonService.EVENT_TYPES.CURRENT_PRINCIPAL_CHANGE_EVENT);
			},
			function() {
				console.log("***** Login Failed");
				callback(null, false);
				alertService.openModal({title: "Error", message: "An error occurred while attempting to login."});
			});
	};

	return {
		currentPrincipal: currentPrincipal,
		logout: logout,
		login : login
	}
}])

.factory('base64', function() {
	var keyStr = 'ABCDEFGHIJKLMNOP' +
		'QRSTUVWXYZabcdef' +
		'ghijklmnopqrstuv' +
		'wxyz0123456789+/' +
		'=';
	return {
		encode: function (input) {
			var output = "";
			var chr1, chr2, chr3 = "";
			var enc1, enc2, enc3, enc4 = "";
			var i = 0;

			do {
				chr1 = input.charCodeAt(i++);
				chr2 = input.charCodeAt(i++);
				chr3 = input.charCodeAt(i++);

				enc1 = chr1 >> 2;
				enc2 = ((chr1 & 3) << 4) | (chr2 >> 4);
				enc3 = ((chr2 & 15) << 2) | (chr3 >> 6);
				enc4 = chr3 & 63;

				if (isNaN(chr2)) {
					enc3 = enc4 = 64;
				} else if (isNaN(chr3)) {
					enc4 = 64;
				}

				output = output +
					keyStr.charAt(enc1) +
					keyStr.charAt(enc2) +
					keyStr.charAt(enc3) +
					keyStr.charAt(enc4);
				chr1 = chr2 = chr3 = "";
				enc1 = enc2 = enc3 = enc4 = "";
			} while (i < input.length);

			return output;
		},

		decode: function (input) {
			var output = "";
			var chr1, chr2, chr3 = "";
			var enc1, enc2, enc3, enc4 = "";
			var i = 0;

			// remove all characters that are not A-Z, a-z, 0-9, +, /, or =
			var base64test = /[^A-Za-z0-9\+\/\=]/g;
			if (base64test.exec(input)) {
				alert("There were invalid base64 characters in the input text.\n" +
					"Valid base64 characters are A-Z, a-z, 0-9, '+', '/',and '='\n" +
					"Expect errors in decoding.");
			}
			input = input.replace(/[^A-Za-z0-9\+\/\=]/g, "");

			do {
				enc1 = keyStr.indexOf(input.charAt(i++));
				enc2 = keyStr.indexOf(input.charAt(i++));
				enc3 = keyStr.indexOf(input.charAt(i++));
				enc4 = keyStr.indexOf(input.charAt(i++));

				chr1 = (enc1 << 2) | (enc2 >> 4);
				chr2 = ((enc2 & 15) << 4) | (enc3 >> 2);
				chr3 = ((enc3 & 3) << 6) | enc4;

				output = output + String.fromCharCode(chr1);

				if (enc3 != 64) {
					output = output + String.fromCharCode(chr2);
				}
				if (enc4 != 64) {
					output = output + String.fromCharCode(chr3);
				}

				chr1 = chr2 = chr3 = "";
				enc1 = enc2 = enc3 = enc4 = "";

			} while (i < input.length);

			return output;
		}
	};
})
;