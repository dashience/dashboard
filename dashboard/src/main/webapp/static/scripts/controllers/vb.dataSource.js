app.controller("DataSourceController", function ($scope, $stateParams, $http) {
    $scope.dataSourceTypes = [{type: "sql", name: "SQL"}, {type: "csv", name: "CSV"}];
//    $scope.dataSourceTypes = [{type: "sql", name: "SQL"}, {type: "csv", name: "CSV"}, {type: "https", name: "HTTPS"}, {type: "xls", name: "XLS"}];
    
    function getItems() {
        $http.get('admin/ui/dataSource').success(function (response) {
            $scope.dataSources = response;
        });
    }
    getItems();

    $scope.csvFileUpload = function (event) {
        var files = event.target.files;
        angular.forEach(files, function (value, key) {
            $scope.sourceFileName = value.name;
        });

        if (files.length) {
            var r = new FileReader();
            r.onload = function (e) {
                var contents = e.target.result;
                $scope.$apply(function () {
                    $scope.fileReader = contents;
                });
            };
            r.readAsText(files[0]);
        }
    };
    var sourceData;
    $scope.xlsFileUpload = function (event) {
        var files = event.target.files;
        angular.forEach(files, function (value, key) {
            $scope.sourceFileName = value.name;
        });

        if (files.length) {
            var r = new FileReader();
            r.onload = function (e) {
                var contents = e.target.result;
                $scope.$apply(function () {
                    $scope.fileReader = contents;
                });
            };
            r.readAsText(files[0]);
        }
    };

    $scope.saveDataSource = function (dataSource) {
        var data = {
            id: dataSource.id,
            connectionString: dataSource.connectionString,
            sqlDriver: dataSource.sqlDriver,
            name: dataSource.name,
            password: dataSource.password,
            userName: dataSource.userName,
            dataSourceType: dataSource.dataSourceType,
            sourceFile: dataSource.sourceFile ? dataSource.sourceFile : $scope.fileReader,
            sourceFileName: $scope.sourceFileName,
            agencyId: dataSource.agencyId,
            userId: dataSource.userId
        };
        $http({method: dataSource.id ? 'PUT' : 'POST', url: 'admin/ui/dataSource', data: data}).success(function (response) {
            getItems();
        });
        $scope.dataSource = "";
        $scope.sourceFileName = "";
    };

    $scope.selectedRow = null;
    $scope.highlightDataSource = function(index){
        $scope.selectedRow = index;        
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
            sourceFile: dataSource.sourceFile,
            agencyId: dataSource.agencyId.id,
            userId: dataSource.userId.id
        };
        $scope.dataSource = data;
    };

    $scope.clearDataSource = function (dataSource) {
        $scope.dataSource = "";
        $scope.sourceFileName = "";
    };

    $scope.deleteDataSource = function (dataSource, index) {
        if (dataSource.id) {
            $http({method: 'DELETE', url: 'admin/ui/dataSource/' + dataSource.id}).success(function () {
                $scope.dataSources.splice(index, 1);
            });
        } else {
            $scope.dataSources.splice(index, 1);
        }
    };
});