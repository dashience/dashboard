app.controller('FieldSettingsController', function ($scope, $http, $stateParams, $filter, $timeout) {
    $scope.fieldSettings = [];
    $scope.selectAggregations = [
        {name: 'None', value: ""},
        {name: 'Sum', value: "sum"},
        {name: 'CTR', value: "ctr"},
        {name: 'CPC', value: "cpc"},
        {name: 'CPCS', value: "cpcs"},
        {name: 'CPS', value: "cps"},
        {name: 'CPA', value: "cpa"},
        {name: 'CPAS', value: "cpas"},
        {name: 'Avg', value: "avg"},
        {name: 'Count', value: "count"},
        {name: 'Min', value: "min"},
        {name: 'Max', value: "max"},
        {name: 'CPL', value: "cpl"},
        {name: 'CPLC', value: "cplc"},
        {name: 'CPComment', value: "cpcomment"},
        {name: 'CPostE', value: "cposte"},
        {name: 'CPageE', value: "cpagee"},
        {name: 'CPP', value: "cpp"},
        {name: 'CPR', value: "cpr"}
    ];
    $scope.fieldTypes = [
        {name: 'None', value: ''},
        {name: 'String', value: 'string'},
        {name: 'Number', value: 'number'},
        {name: 'Date', value: 'date'},
        {name: 'Day', value: 'day'}
    ];
    $scope.formats = [
        {name: "None", value: ''},
        {name: "Currency", value: '$,.2f'},
        {name: "Integer", value: ',.0f'},
        {name: "Percentage", value: ',.2%'},
        {name: "Decimal1", value: ',.1f'},
        {name: "Decimal2", value: ',.2f'},
        {name: "Time", value: 'H:M:S'},
        {name: "Star Rating", value: 'starRating'},
        {name: "Date", value: 'MM-dd-yyyy'}
    ];
    $http.get('admin/fieldSettings').success(function (response) {
        $scope.fieldSettings = response;
    });
    $scope.getFields = function () {
        $http.get('admin/fieldSettings').success(function (response) {
            $scope.fieldSettings = response;
        });
    };
    $scope.selectDisplayFormat = function (field) {
        //field.dataFormat = field.displayFormat;
    };
    $scope.selectDataFormat = function (field) {
        field.displayFormat = field.dataFormat;
    };
    $scope.addFieldSettings = function () {
        $scope.fieldSettings.push({isEdit: true});
    };
    $scope.saveField = function (fields) {
        var data = {
            id: fields.id,
            fieldName: fields.fieldName,
            displayFormat: fields.displayFormat ? fields.displayFormat.value : null,
            dataFormat: fields.dataFormat,
            dataType: fields.dataType,
            displayName: fields.displayName,
            agregationFunction: fields.agregationFunction
        };
        $http({method: fields.id ? "PUT" : "POST", url: 'admin/fieldSettings', data: data}).success(function (response) {
            $scope.getFields();
        });
        $scope.field = "";
    };
    $scope.deleteField = function (fields) {
        if ($scope.deleteFieldOnCreate === true) {
            $scope.deleteFieldSettings($scope.deleteFieldIndex);
        } else {
            var field=$scope.deleteFieldItems;
            $http({method: "DELETE", url: 'admin/fieldSettings/' + field.id}).success(function (response) {
                $scope.getFields();
            });
        }

    };
    $scope.updateField = function (field) {
        var getDisplayFormat = field.displayFormat;

        var getDisplayObj = $.grep($scope.formats, function (val) {
            return getDisplayFormat === val.value;
        });
        angular.forEach(getDisplayObj, function (val, key) {
            // field.dataFormat = val;
            field.displayFormat = val;
        });
//        var data = {
//            id: fields.id,
//            fieldName: fields.fieldName,
//            displayFormat: fields.displayFormat,
//            dataFormat: fields.dataFormat,
//            dataType: fields.dataType,
//            displayName: fields.displayName,
//            agregationFunction: fields.agregationFunction
//        };
//        $scope.field = data;
    };
    $scope.deleteFieldSettings = function (index) {
        $scope.fieldSettings.splice(index, 1);
    };

    $scope.deleteFieldSettingsItem = function (fields, index, type) {
        console.log(fields);
        if (type === "create") {
            $scope.deleteFieldOnCreate = true;
            $scope.deleteFieldIndex = index;
            $scope.deleteFieldItems = fields;
        } else {
            $scope.deleteFieldOnCreate = false;
            $scope.deleteFieldIndex = index;
            $scope.deleteFieldItems = fields;
            
        }

    };

});

