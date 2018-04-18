app.controller("DataSourceController", ['$scope', '$stateParams', '$http', '$rootScope', '$cookies', '$translate', '$window', function ($scope, $stateParams, $http, $rootScope, $cookies, $translate, $window) {
//    $scope.dataSourceTypes = [{type: "sql", name: "SQL"}, {type: "csv", name: "CSV"}];
        $scope.authenticateFlag = false;
        $scope.oAuthData = {};
        $scope.oAuthData.accountId = $stateParams.accountId;
        console.log("stateParams----->", $scope.oAuthData.accountId);
        $scope.oAuthData.userId = $cookies.getObject("userId");
        $scope.oAuthData.domainName = location.host;
        console.log("cookies-------------->",document.cookie);
        $http.get('static/datas/dataSources/dataSource.json').success(function (response) {
            $scope.dataSourceTypes = response.dataSource;
        });
        function getItems() {
            $http.get('admin/ui/dataSource').success(function (response) {
                $scope.dataSources = response;
                console.log("dataSources----------->", $scope.dataSources);
            });
        }
        getItems();


        $scope.agencyLanguage = $stateParams.lan;//$cookies.getObject("agencyLanguage");

        var lan = $scope.agencyLanguage;
        changeLanguage(lan);

        var lan = $scope.agencyLanguage;
        changeLanguage(lan);

        function changeLanguage(key) {
            $translate.use(key);
        }


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
//        $scope.authenticateStatus = function (name)
//        {
//            if (name.length != 0)
//            {
//                $scope.authenticateFlag = false;
//            } else {
//                $scope.authenticateFlag = true;
//            }
//        };
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
                $http.get("admin/ui/oauthCode/" + accessToken + '/' + data.dataSourceType).success(function (response) {
                    $scope.oauthToken = response.access_token;
                    $("#fbOauthToken").val(response.access_token);
                    alert(response.access_token);
                    $scope.saveDataSource(data);
                }).error(function (response) {
                    console.log("error");
                });
            }, 5000);
        };
        //get facebook datasets using datasource
        $scope.getfacebookData = function ()
        {
            $http.get("admin/ui/facebookDataSets").success(function (response) {
            }).error(function (response) {
            });
        };
        $scope.saveDataSource = function (dataSource) {
            console.log("Save DataSource");
            console.log("dataSource-------------", dataSource);
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
                code: dataSource.code ? dataSource.code : '',
                oauthStatus: dataSource.oauthStatus
            };
            if (data.name && data.dataSourceType) {
                $http({method: dataSource.id ? 'PUT' : 'POST', url: 'admin/ui/dataSource', data: data}).success(function (response) {
                    $scope.oAuthData.dataSource = response;
                    console.log("response--------->", response);
                    if (!response.oauthStatus) {
                        $scope.authenticateFlag = true;
                    } else {
                        $scope.authenticateFlag = false;
                    }
                    getItems();
                });
            }
            console.log("$scope.sourceFileName---------->", $scope.sourceFileName);
            console.log("$scope.dataSource---------->", $scope.dataSource);
            dataSource = "";
//            $scope.dataSource = "";
            $scope.sourceFileName = "";
        };
        $scope.selectedRow = null;
        $scope.highlightDataSource = function (index) {
            $scope.selectedRow = index;
        };
        $scope.editDataSource = function (dataSource) {
            console.log("dataSource--------->", dataSource);
            if (dataSource.dataSourceType === "csv" || dataSource.dataSourceType === "xls") {
                $scope.editDataSourceType = true;
                $scope.authenticateFlag = false;
            } else {
                $scope.editDataSourceType = false;
                if (dataSource.oauthStatus) {
                    console.log("dataSource.oauthStatus1------>", dataSource.oauthStatus);
                    $scope.authenticateFlag = false;
                } else {
                    console.log("dataSource.oauthStatus2------>", dataSource.oauthStatus);
                    $scope.authenticateFlag = true;
                }
            }
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
                userId: dataSource.userId.id,
                oauthStatus: dataSource.oauthStatus
            };
            $scope.dataSource = data;
            $scope.selectSourceType(dataSource);
            $scope.oAuthData.dataSource = dataSource;
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
            $scope.clearDataSource(dataSource);
            $scope.selectedRow = null;
        };
        $scope.showXLSFileUpload = false;
        $scope.showCSVFileUpload = false;
        $scope.selectSourceType = function (source) {
            var dataSourceType = source.dataSourceType;
            if (dataSourceType == 'xls') {
                $scope.showXLSFileUpload = true;
                $scope.showCSVFileUpload = false;
                $scope.authenticateFlag = false;
            } else if (dataSourceType == 'csv') {
                $scope.showCSVFileUpload = true;
                $scope.showXLSFileUpload = false;
                $scope.authenticateFlag = false;
            } else {
                $scope.showXLSFileUpload = false;
                $scope.showCSVFileUpload = false;
                $scope.oAuthData.status = false;
                $scope.oAuthData.source = source.dataSourceType;
            }
        }
        $scope.uploadFile = function (dataSource) {
            console.log($scope.editDataSourceType);
            if ($scope.editDataSourceType === true) {
                $scope.saveDataSource(dataSource);
            } else {
                var file = $scope.myFile;
                console.log("scope file name-->");
                $scope.sourceFileName = file.name;
                var uploadUrl = "admin/ui/fileUpload";
                uploadFileToUrl(file, uploadUrl, dataSource);
                $scope.myFile = "";
            }

        };
        function uploadFileToUrl(file, uploadUrl, dataSource) {
            var fd = new FormData();
            fd.append('file', file);
            $http.post(uploadUrl, fd, {transformRequest: angular.identity, headers: {'Content-Type': undefined}}).success(function (response) {
                dataSource.connectionString = response.filename;
                $scope.saveDataSource(dataSource);
            });
        }
        $scope.onSubmit = function () {
            login("admin/social/signin?apiKey=" + $scope.oAuthData.clientId + "&apiSecret=" + $scope.oAuthData.clientSecret + "&apiSource=" + $scope.oAuthData.source + "&accountId=" + $scope.oAuthData.accountId + "&dataSourceId=" + $scope.oAuthData.dataSource.id + "&domainName=" + $scope.oAuthData.domainName);
            //        $window.open("admin/social/signin?apiKey=" + $scope.oAuthData.clientId + "&apiSecret=" + $scope.oAuthData.clientSecret + "&apiSource=" + $scope.oAuthData.source);
        };
        function login(url) {
            var win = $window.open(url);

            var pollTimer = $window.setInterval(function () {
                try {
//                console.log(win.document.URL);
                    if (win.document.URL.indexOf("success") != -1) {
                        $window.clearInterval(pollTimer);
                        var url = win.document.URL;
                        win.top.close();
                        $scope.success();
                    }
                } catch (e) {
                }
            }, 500);
        }
        ;
        $scope.success = function () {
            // console.log("called-------->", $scope.saveButtonStatus1);
//            $scope.dataSources[$scope.oAuthData.index].oauthStatus = true;
            $scope.oAuthData.dataSource.oauthStatus = true;
            $http({method: "PUT", url: "admin/social/oauthStatus", data: $scope.oAuthData.dataSource}).success(function (response) {
                console.log("response-------->", response);
                getItems();
            });
            $scope.authenticateFlag = false;
            dataSource = "";
            $scope.dataSource = "";
            $scope.sourceFileName = "";
            $scope.selectedRow = null;
            $scope.$apply();
        };
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
