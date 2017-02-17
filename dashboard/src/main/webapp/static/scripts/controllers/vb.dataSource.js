app.controller("DataSourceController", function ($scope, $stateParams, $http) {

    $scope.dataSourceTypes = [{type: "sql", name: "SQL"}, {type: "csv", name: "CSV"}, {type: "https", name: "HTTPS"}, {type: "xls", name: "XLS"}]

    function getItems() {
        $http.get('admin/ui/dataSource').success(function (response) {
            $scope.dataSources = response;
        });
    }
    getItems();
//    $scope.addDataSource = function () {
//        $scope.dataSources.unshift({isEdit: true});
//    };

    $scope.saveDataSource = function (dataSource) {
        var data = {
            id: dataSource.id,
            connectionString: dataSource.connectionString,
            sqlDriver: dataSource.sqlDriver,
            name: dataSource.name,
            password: dataSource.password,
            userName: dataSource.userName,
            dataSourceType: dataSource.dataSourceType,
            sourceFile: dataSource.sourceFile
        };
        $http({method: dataSource.id ? 'PUT' : 'POST', url: 'admin/ui/dataSource', data: data}).success(function (response) {
            getItems()
        });
        $scope.dataSource = "";
    };

    $scope.editDataSource = function (dataSource) {
        var data = {
            id: dataSource.id,
            name: dataSource.name,
            connectionString: dataSource.connectionString,
            sqlDriver: dataSource.sqlDriver,
            userName: dataSource.userName,
            password: dataSource.password,
            dataSourceType: dataSource.dataSourceType,
            sourceFile: dataSource.sourceFile
        }
        $scope.dataSource = data;
    }

    $scope.clearDataSource = function (dataSource) {
        $scope.dataSource = "";
    }

    $scope.deleteDataSource = function (dataSource, index) {
        if (dataSource.id) {
            $http({method: 'DELETE', url: 'admin/ui/dataSource/' + dataSource.id}).success(function () {
                $scope.dataSources.splice(index, 1);
            })
        } else {
            $scope.dataSources.splice(index, 1);
        }
    };
});