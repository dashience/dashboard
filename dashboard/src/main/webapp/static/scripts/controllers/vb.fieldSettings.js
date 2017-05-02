app.controller('FieldSettingsController', function ($scope, $http, $stateParams, $filter, $timeout) {
    $scope.fieldSettings = [];

    $http.get('admin/fieldSettings').success(function (response) {
        $scope.fieldSettings = response;
    })
    $scope.getFields = function () {
        $http.get('admin/fieldSettings').success(function (response) {
            $scope.fieldSettings = response;

        })
    }
    $scope.addFieldSettings = function () {
        $scope.fieldSettings.push({isEdit: true});
    }
    $scope.saveField = function (fields) {
        var data = {
            id: fields.id,
            fieldName: fields.fieldName,
            displayFormat: fields.displayFormat,
            dataFormat: fields.dataFormat,
            dataType: fields.dataType,
            displayName: fields.displayName,
            agregationFunction: fields.agregationFunction
        }
        $http({method: fields.id ? "PUT" : "POST", url: 'admin/fieldSettings', data: data}).success(function (response) {
            $scope.getFields();
        });
        $scope.field = ""
    }
    $scope.deleteField = function (fields) {
        $http({method: "DELETE", url: 'admin/fieldSettings/' + fields.id}).success(function (response) {
            $scope.getFields();
        });
    }
    $scope.updateField = function (fields) {
        var data = {
            id: fields.id,
            fieldName: fields.fieldName,
            displayFormat: fields.displayFormat,
            dataFormat: fields.dataFormat,
            dataType: fields.dataType,
            displayName: fields.displayName,
            agregationFunction: fields.agregationFunction
        }
        $scope.field = data;
    }
    $scope.deleteFieldSettings = function (index) {
        $scope.fieldSettings.splice(index, 1);
    }
});
