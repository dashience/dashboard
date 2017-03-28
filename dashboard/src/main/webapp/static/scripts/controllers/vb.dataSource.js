app.controller("DataSourceController", function ($scope, $stateParams, $http, $rootScope) {
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
            name: "facebook",
            url: 'https://www.facebook.com/v2.8/dialog/oauth?client_id=1631503257146893&&display=popup&response_type=code&redirect_uri=http://localhost:9090/VizBoard/fbPost.html'
        },
        {
            type: "linkedin",
            name: "linkedin",
            url: 'https://www.linkedin.com/oauth/v2/authorization?client_id=81kqaac7cnusqy&redirect_uri=http://localhost:8084/VizBoard/fbPost.html&state=123908353453&response_type=code'
        },
        {
            type: 'instagram',
            name: 'Instagram',
            url: 'https://www.instagram.com/oauth/authorize/?client_id=3e39cb1cc6be4a60873487a1ce90a451&redirect_uri=http://localhost:9090/VizBoard/fbPost.html&response_type=token&scope=public_content'
        },
        {
            type: 'adwords',
            name: 'Adwords'   
        },
        {
            type: 'analytics',
            name: 'Analytics'   
        }
    ];

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
        localStorage.setItem("dataSourceType",$("#dataSourceType").val());
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
        console.log(dataSource);

        //for accesstoken
        console.log($('#fbAccessToken').val());
        dataSource.code = $('#fbOauthToken').val();
        dataSource.accessToken = $('#fbAccessToken').val();
        console.log($('#fbOauthToken').val());


        console.log(dataSource.accessToken);
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
