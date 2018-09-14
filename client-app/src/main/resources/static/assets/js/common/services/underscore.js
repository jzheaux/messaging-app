angular.module('underscore', [])

.factory('_', ['$window', function() {
  return $window._;
}])
;