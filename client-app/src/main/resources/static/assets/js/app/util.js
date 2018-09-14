'use strict'

angular.module('secure-messaging-app.util', [
    'ngAnimate',
    'ui.bootstrap'
])

.factory('commonService', function() {

    var _properties = {};

    var _eventTypes = {
        CURRENT_MESSAGE_CHANGE_EVENT : 'CURRENT_MESSAGE_CHANGE_EVENT',
        CURRENT_PRINCIPAL_CHANGE_EVENT : 'CURRENT_PRINCIPAL_CHANGE_EVENT'
    };

    return {
        CURRENT_MESSAGE_KEY: 'CURRENT_MESSAGE',

        CURRENT_PRINCIPAL_KEY : 'CURRENT_PRINCIPAL',

        EVENT_TYPES: _eventTypes,

        setProperty: function (key, value) {
            _properties[key] = value;
        },

        getProperty: function (key) {
            return _properties[key];
        }
    }
})

.controller('alertModalController', ['$scope', '$uibModalInstance', 'modalData', function($scope, $uibModalInstance, modalData) {
    $scope.alertTitle = modalData.title;
    $scope.alertMessage = modalData.message;

    $scope.ok = function() {
        $uibModalInstance.close();
    }
}])

.factory('alertService', ['$uibModal', function($uibModal) {
    function openModal(args) {
        $uibModal.open({
            animation: true,
            templateUrl: 'assets/js/common/partials/alertModal.tpl.html',
            controller: 'alertModalController',
            resolve: {
                modalData: function () {
                    var modalData = {
                        title : args.title,
                        message : args.message
                    };
                    return modalData;
                }
            }
        });
    }

    return {
        openModal : openModal
    }
}])
;