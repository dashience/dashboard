app.controller('DataSetController', function ($scope, $http) {
    function getItems() {
        $http.get('admin/ui/dataSet').success(function (response) {
            $scope.dataSets = response;
        });
    }
    getItems();
    $http.get('admin/ui/dataSource').success(function (response) {
        $scope.dataSources = response;
    });

    $scope.saveDataSet = function (dataSet) {
        $http({method: dataSet.id ? 'PUT' : 'POST', url: 'admin/ui/dataSet', data: dataSet}).success(function (response) {
            getItems();
        });
        $scope.dataSet = "";
    };

    $scope.editDataSet = function (dataSet) {
        var data = {
            id: dataSet.id,
            name: dataSet.name,
            query: dataSet.query,
            dataSourceId: dataSet.dataSourceId
        };
        $scope.dataSet = data;
    };


    $scope.resetPreview = function (dataSet) {
        $scope.previewData = null;
        console.log(dataSet);
    };
    $scope.previewDataSet = function (dataSet) {
        $scope.previewData = dataSet;
        console.log(dataSet);
    };

    $scope.clearDataSet = function (dataSet) {
        $scope.dataSet = "";
    };

    $scope.deleteDataSet = function (dataSet, index) {alert(dataSet.id)
        $http({method: 'DELETE', url: 'admin/ui/dataSet/' + dataSet.id}).success(function (response) {
            $scope.dataSets.splice(index, 1)
        });
    };

    $scope.selectedRow = null;
    $scope.setClickedRow = function (index) {
        $scope.selectedRow = index;
    }


    //$(function () {
//        var editor = ace.edit("editor");
//        editor.setTheme("ace/theme/monokai");
//        editor.getSession().setMode("ace/mode/sql");

    // });
});
app.directive('previewTable', function ($http, $filter, $stateParams) {
    return{
        restrict: 'A',
        scope: {
            path: '@'
//            dataSetId: '@'
                    // widgetColumns: '@',
                    //setTableFn: '&',
                    // tableFooter:'@'
        },
        template: '<div ng-show="loadingTable" class="text-center" style="color: #228995;"><img src="static/img/logos/loader.gif"></div>' +
                '<table ng-if="ajaxLoadingCompleted" class="table table-responsive table-bordered table-l2t">' +
                '<thead><tr>' +
                '<th class="text-capitalize info table-bg" ng-repeat="col in tableColumns">' +
                '{{col.displayName}}' +
                '</th>' +
                '</tr></thead>' +
                '<tbody ng-repeat="tableRow in tableRows">' +
                '<tr class="text-capitalize text-info info">' +
                '<td ng-repeat="col in tableColumns">' +
                '<div>{{tableRow[col.fieldName]}}</div>' +
                '</td>' +
                '</tbody>' +
                '</table>',
        link: function (scope, element, attr) {
            scope.loadingTable = true;
            var dataSourcePath = JSON.parse(scope.path)
            console.log(dataSourcePath)
            console.log(dataSourcePath.dataSourceId.userName)
            $http.get('admin/proxy/getJson?url=../dbApi/admin/dataSet/getData?connectionUrl=' + dataSourcePath.dataSourceId.connectionString + "&driver=" + dataSourcePath.dataSourceId.sqlDriver + "&location=" + $stateParams.locationId + "&startDate=" + $stateParams.startDate + "&endDate=" + $stateParams.endDate + '&username=' + dataSourcePath.dataSourceId.userName + '&password=' + dataSourcePath.dataSourceId.password + '&port=3306&schema=vb&query=' + encodeURI(dataSourcePath.query)).success(function (response) {
                scope.ajaxLoadingCompleted = true;
                scope.loadingTable = false;
                scope.tableColumns = response.columnDefs;
                scope.tableRows = response.data;
            });
        }
    };
});
