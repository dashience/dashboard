app.controller("DataSourceController", ['$scope', '$stateParams', '$http', '$rootScope', function ($scope, $stateParams, $http, $rootScope) {
//    $scope.dataSourceTypes = [{type: "sql", name: "SQL"}, {type: "csv", name: "CSV"}];
        $scope.authenticateFlag = true;
        $scope.dataSourceTypes = [
            {
                type: "sql",
                name: "SQL"
            },
            {
                type: "csv",
                name: "CSV"
            },
            {type: "https", name: "HTTPS"},
            {
                type: "xls",
                name: "XLS"
            },
            {
                type: "facebook",
                name: "Facebook",
//                url: 'https://www.facebook.com/v2.8/dialog/oauth?client_id=1631503257146893&&display=popup&response_type=code&redirect_uri=http://localhost:9090/VizBoard/fbPost.html'
            },
            {
                type: 'instagram',
                name: 'Instagram',
//                url: 'https://www.instagram.com/oauth/authorize/?client_id=3e39cb1cc6be4a60873487a1ce90a451&redirect_uri=http://localhost:9090/VizBoard/fbPost.html&response_type=token&scope=public_content'
            },
            {
                type: 'adwords',
                name: 'Google AdWords'
            },
            {
                type: 'analytics',
                name: 'Google Analytics'
            
            },
            {
                type: 'pinterest',
                name: 'Pinterest'
            },
            {
                type: 'linkedin',
                name: 'LinkedIn'
            },
//            {
//                type: 'twitter',
//                name: 'Twitter'
//            },
             {
                type: 'bing',
                name: 'Bing'
            }
        ];
        function getItems() {
            $http.get('admin/ui/dataSource').success(function (response) {
                $scope.dataSources = response;
            });
        }
        getItems();
//        $scope.csvFileUpload = function (event) {
//            var files = event.target.files;
//            angular.forEach(files, function (value, key) {
//                $scope.sourceFileName = value.name;
//            });
//
//            if (files.length) {
//                var r = new FileReader();
//                r.onload = function (e) {
//                    var contents = e.target.result;
//                    $scope.$apply(function () {
//                        $scope.fileReader = contents;
//                    });
//                };
//                r.readAsText(files[0]);
//            }
//        };
        var sourceData;
        $scope.selectFileUploadName = function (event) {
            var files = event.target.files;
            angular.forEach(files, function (value, key) {
                $scope.sourceFileName = value.name;
            });
//            if (files.length) {
//                var r = new FileReader();
//                r.onload = function (e) {
//                    var contents = e.target.result;
//                    $scope.$apply(function () {
//                        $scope.fileReader = contents;
//                    });
//                };
//                r.readAsText(files[0]);
//            }
        };
        //for authentication button flag enable status
        $scope.authenticateStatus = function (name)
        {
            console.log(name.length);
            if (name.length != 0)
            {
                $scope.authenticateFlag = false;
            } else {
                $scope.authenticateFlag = true;
            }
        };
        $scope.getDataSource = function (data) {
            console.log(data);
            $("#dataSourceType").val(data.dataSourceType);
            localStorage.setItem("dataSourceType", $("#dataSourceType").val());
            $scope.dataSourceUrl = getDataSourceUrl(data.dataSourceType);
            console.log(data);
            window.open($scope.dataSourceUrl, data.dataSourceType, "myWindow", 'width=800,height=600');
            console.log($scope.dataSourceUrl);
            function getDataSourceUrl(dataSourceType)
            {
                console.log(dataSourceType);
                var url;
                for (var i = 0; i < $scope.dataSourceTypes.length; i++)
                {
                    if ($scope.dataSourceTypes[i].type === data.dataSourceType)
                    {
                        url = $scope.dataSourceTypes[i].url;
                        console.log(url);
                        return url;
                    }
                }
            }
            setTimeout(function () {
                var accessToken = $('#fbAccessToken').val();
                console.log(accessToken);
                $http.get("admin/ui/oauthCode/" + accessToken + '/' + data.dataSourceType).success(function (response) {
                    console.log("success");
                    console.log(response);
                    $scope.oauthToken = response.access_token;
                    $("#fbOauthToken").val(response.access_token);
                    alert(response.access_token);
                    $scope.saveDataSource(data);
                    console.log($scope.oauthToken);
                }).error(function (response) {
                    console.log("error");
                    console.log(response);
                });
            }, 5000);
        };
        //get facebook datasets using datasource
        $scope.getfacebookData = function ()
        {
            $http.get("admin/ui/facebookDataSets").success(function (response) {
                console.log("success");
                console.log(response);
            }).error(function (response) {
                console.log("error");
                console.log(response);
            });
        };
        $scope.saveDataSource = function (dataSource) {
            console.log(dataSource)
            dataSource.code = $('#fbOauthToken').val();
            dataSource.accessToken = $('#fbAccessToken').val();
            var data = {
                id: dataSource.id,
                connectionString: dataSource.connectionString,
                sqlDriver: dataSource.sqlDriver,
                name: dataSource.name,
                password: dataSource.password,
                userName: dataSource.userName,
                dataSourceType: dataSource.dataSourceType,
                sourceFile: dataSource.sourceFile,
                //sourceFileName: $scope.sourceFileName,
                accessToken: dataSource.accessToken ? dataSource.accessToken : '',
                agencyId: dataSource.agencyId,
                userId: dataSource.userId,
                code: dataSource.code ? dataSource.code : ''
            };
            console.log(data);
            $http({method: dataSource.id ? 'PUT' : 'POST', url: 'admin/ui/dataSource', data: data}).success(function (response) {
                getItems();
            });
            $scope.dataSource = "";
            $scope.sourceFileName = "";
            $scope.selectedRow = null;
        };
        $scope.selectedRow = null;
        $scope.highlightDataSource = function (index) {
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
            $scope.selectSourceType(dataSource)
        };
        $scope.clearDataSource = function (dataSource) {
            $scope.dataSource = "";
            $scope.sourceFileName = "";
            $scope.selectedRow = null;
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
        $scope.showXLSFileUpload = false;
        $scope.showCSVFileUpload = false;
        $scope.selectSourceType = function (source) {
            var dataSourceType = source.dataSourceType;
            if (dataSourceType == 'xls') {
                $scope.showXLSFileUpload = true;
                $scope.showCSVFileUpload = false;
            } else if (dataSourceType == 'csv') {
                $scope.showCSVFileUpload = true;
                $scope.showXLSFileUpload = false;
            } else {
                $scope.showXLSFileUpload = false;
                $scope.showCSVFileUpload = false;
            }
        }
        $scope.uploadFile = function (dataSource) {
            var file = $scope.myFile;
            $scope.sourceFileName = file.name;
            var uploadUrl = "admin/ui/fileUpload";
            uploadFileToUrl(file, uploadUrl, dataSource);
            $scope.myFile = "";
        };
        function uploadFileToUrl(file, uploadUrl, dataSource) {
            var fd = new FormData();
            fd.append('file', file);
            $http.post(uploadUrl, fd, {transformRequest: angular.identity, headers: {'Content-Type': undefined}}).success(function (response) {
                console.log(response.filename)
                dataSource.connectionString = response.filename;
                $scope.saveDataSource(dataSource);
            })
        }
    }]);
app.directive('fileModel', ['$parse', function ($parse) {
        return {
            restrict: 'A',
            link: function (scope, element, attrs) {
                var model = $parse(attrs.fileModel);
                var modelSetter = model.assign;
                element.bind('change', function () {
                    scope.$apply(function () {
                        modelSetter(scope, element[0].files[0]);
                    });
                });
            }
        };
    }]);
//app.service('fileUpload', ['$http', function ($http) {
//        this.uploadFileToUrl = function (file, uploadUrl) {
//            var fd = new FormData();
//            fd.append('file', file);
//            $http.post(uploadUrl, fd, {
//                transformRequest: angular.identity,
//                headers: {'Content-Type': undefined}
//            })
//                    .success(function () {
//                    })
//                    .error(function () {
//                    });
//        };
//    }]);
