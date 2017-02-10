app.controller("NewOrEditReportController", function ($scope, $http, $stateParams, $filter) {

    $scope.reportId = $stateParams.reportId;
    $scope.startDate = $stateParams.startDate;
    $scope.endDate = $stateParams.endDate;

    $scope.reportWidgets = [];
    $scope.selectDurations = [
        {duration: "None"},
        {duration: "Last Week"},
        {duration: "Last Three Months"},
        {duration: "Last Six Months"},
        {duration: "Last Six Months"}
    ]; // Month Durations-Popup
    $scope.selectAggregations = [
        {name: 'None', value: ""},
        {name: 'Sum', value: "sum"},
        {name: 'CTR', value: "ctr"},
        {name: 'CPC', value: "cpc"},
        {name: 'CPA', value: "cpa"},
        {name: 'Avg', value: "avg"},
        {name: 'Count', value: "count"},
        {name: 'Min', value: "min"},
        {name: 'Max', value: "max"}
    ];   //Aggregation Type-Popup
    $scope.selectGroupPriorities = [
        {num: 'None', value: ""},
        {num: 1, value: 1},
        {num: 2, value: 2}
    ];
    $scope.selectDateDurations = [
        {duration: "None"},
        {duration: "Last Week"},
        {duration: "Last Three Months"},
        {duration: "Last Six Months"},
        {duration: "Last Six Months"}
    ]; // Month Durations-Popup
    $scope.selectXAxis = [
        {label: 'None', value: ""},
        {label: "X-1", value: 1}
    ];
    $scope.selectYAxis = [
        {label: 'None', value: ""},
        {label: "Y-1", value: 1},
        {label: "Y-2", value: 2}
    ];
    $scope.alignments = [
        {name: '', displayName: 'None'},
        {name: "left", displayName: "Left"},
        {name: "right", displayName: "Right"},
        {name: "center", displayName: "Center"}
    ];
    $scope.sorting = [
        {name: 'None', value: ''},
        {name: 'asc', value: 1},
        {name: 'dec', value: 0}
    ];
    $scope.tableWrapText = [
        {name: 'None', value: ''},
        {name: 'Yes', value: "yes"},
        {name: 'No', value: "no"}
    ];

    $http.get("admin/ui/report/" + $stateParams.reportId + "?locationId=" + $stateParams.locationId).success(function (response) {
        $scope.reports = response;
        $scope.reportTitle = response.reportTitle;
        $scope.description = response.description;
        $scope.reportTitle = response.reportTitle;
        $scope.uploadLogo = response.logo;
        angular.forEach($scope.report, function (value, key) {
            $scope.logo = window.atob(value.logo);
        })
    });

    $http.get('admin/ui/reportWidget/' + $stateParams.reportId + "?locationId=" + $stateParams.locationId).success(function (response) {
        $scope.reportWidgets = response;
    })

    $scope.uploadLogo = "static/img/logos/digital1.png";       //Logo Upload
    $scope.imageUpload = function (event) {
        var files = event.target.files;
        for (var i = 0; i < files.length; i++) {
            var file = files[i];
            var reader = new FileReader();
            reader.onload = $scope.imageIsLoaded;
            reader.readAsDataURL(file);
        }
    };

    $scope.imageIsLoaded = function (e) {
        $scope.$apply(function () {
            $scope.uploadLogo = e.target.result;
        });
    };

    $scope.saveReportData = function () {
        var data = {
            id: $stateParams.reportId,
            reportTitle: $scope.reportTitle,
            description: $scope.description,
            logo: $scope.uploadLogo   //window.btoa($scope.uploadLogo)
        };
        console.log(data.logo);
        $http({method: $stateParams.reportId ? 'PUT' : 'POST', url: 'admin/ui/report', data: data});
    };

    $scope.addReportWidget = function (newWidget) {                                     //Add new Report Widget
        var data = {
            width: newWidget, 'minHeight': 25, columns: []
        };
        $http({method: 'POST', url: 'admin/ui/reportWidget/' + $stateParams.reportId, data: data}).success(function (response) {
            $scope.reportWidgets.push({id: response.id, width: newWidget, 'minHeight': 25, columns: []});
        });
    };

    $scope.deleteReportWidget = function (reportWidget, index) {                            //Delete Widget
        $http({method: 'DELETE', url: 'admin/ui/reportWidget/' + reportWidget.id}).success(function (response) {
            $scope.reportWidgets.splice(index, 1);
        });
    };
    
    $scope.deleteColumn = function (widgetColumns, index) {        //Delete Columns - Popup
        widgetColumns.splice(index, 1);
    };
    
    $scope.dynamicLoadingUrl = function (reportWidget) {                                //Dynamic Url from columns Type data - Popup
        if (reportWidget.columns) {
            reportWidget.columns = reportWidget.columns;
            if (reportWidget.directUrl) {
                $http.get(reportWidget.directUrl + "?fieldsOnly=true").success(function (response) {
                    $scope.collectionFields = [];
                    $scope.collectionFields = response.columnDefs;
                });
            }
        } else {
            if (reportWidget.directUrl) {
                $http.get(reportWidget.directUrl + "?fieldsOnly=true").success(function (response) {
                    $scope.collectionFields = [];
                    reportWidget.columns = response.columnDefs;
                    $scope.collectionFields = response.columnDefs;
                });
            }
        }
    };

    $scope.editReportWidget = function (reportWidget) {                                     //Edit widget
        $scope.dynamicLoadingUrl(reportWidget);
        $scope.selectedRow = reportWidget.chartType;
        reportWidget.previewUrl = reportWidget.directUrl;
        reportWidget.previewType = reportWidget.chartType;
        reportWidget.previewTitle = reportWidget.widgetTitle;
        $scope.editChartType = reportWidget.chartType;
        $scope.selectProductName(reportWidget.productName, reportWidget);
    };
    $scope.selectProductName = function (productName, reportWidget) {
        if (productName === null) {
            return;
        }
        console.log(productName);
        $http.get("admin/user/datasets").success(function (response) {                      //User Based Products and Urls
            $scope.userProducts = [];
            angular.forEach(response, function (value, key) {
                $scope.userProducts.push(key);
            });
            $scope.productFields = response[productName];
        });
    };
    $scope.selectProductName();
    $http.get('static/datas/imageUrl.json').success(function (response) {                   //Popup- Select Chart-Type Json
        $scope.chartTypes = response;
    });
    $scope.reportPreviewChart = function (chartType, reportWidget, index) {                 //Selected Chart type - Bind chart-type to showPreview()
        $scope.selectedRow = chartType.type;
        reportWidget.widgetChartType = chartType.type;
        $scope.editChartType = chartType.type;
        $scope.setPreviewChartType = chartType.type;
        $scope.setPreviewColumn = reportWidget;
    };
    $scope.changeUrl = function (displayName, reportWidget) {                                       //Search dynamic Url  
        angular.forEach($scope.productFields,function(value, key){
            if(value.productDisplayName == displayName){
               $scope.requiredUrl = value.url;
            }
        });
        //var searchUrl = $filter('filter')($scope.productFields, {productDisplayName: url})[0];
        reportWidget.previewUrl = $scope.requiredUrl;
        reportWidget.columns = [];
        $http.get($scope.requiredUrl + "?fieldsOnly=true").success(function (response) {
            $scope.collectionFields = [];
            angular.forEach(response.columnDefs, function (value, key) {
                reportWidget.columns.push({fieldName: value.fieldName, displayName: value.displayName,
                    agregationFunction: value.agregationFunction, displayFormat: value.displayFormat,
                    groupPriority: value.groupPriority, sortOrder: value.sortOrder, sortPriority: value.sortPriority});
            })
            $scope.previewFields = response.columnDefs;
            angular.forEach(response, function (value, key) {
                angular.forEach(value, function (value, key) {
                    $scope.collectionFields.push(value);
                });
            });
        });
    };

    $scope.showPreview = function (reportWidget) {                                    //Show Preview Chart - Pop
        console.log($scope.editChartType)
        console.log(reportWidget.previewUrl)
        console.log($scope.setPreviewColumn)
        console.log(reportWidget)
        $scope.previewChartType = $scope.editChartType ? $scope.editChartType : reportWidget.chartType;
        $scope.previewColumn = $scope.setPreviewColumn ? $scope.setPreviewColumn : reportWidget;
        $scope.previewChartUrl = reportWidget.previewUrl;
    };

    $scope.saveReport = function (reportWidget) {
        console.log(reportWidget.id)
        console.log($scope.editChartTypes)
        reportWidget.directUrl = reportWidget.previewUrl ? reportWidget.previewUrl : reportWidget.directUrl;
        var data = {
            id: reportWidget.id,
            chartType: $scope.editChartType ? $scope.editChartType : reportWidget.chartType,
            directUrl: reportWidget.directUrl,
            widgetTitle: reportWidget.previewTitle,
            reportColumns: reportWidget.columns,
            productName: reportWidget.productName,
            productDisplayName: reportWidget.productDisplayName
        };
        reportWidget.chartType = "";
        $http({method: reportWidget.id ? 'PUT' : 'POST', url: 'admin/ui/reportWidget/' + $stateParams.reportId, data: data}).success(function (response) {
            reportWidget.chartType = data.chartType;
        });
        reportWidget.widgetTitle = reportWidget.previewTitle ? reportWidget.previewTitle : reportWidget.widgetTitle;
        reportWidget.reportColumns = reportWidget.columns;
    };

    $scope.onDropComplete = function (index, reportWidget, evt) {
        if (reportWidget !== "" && reportWidget !== null) {
            var otherObj = $scope.reportWidgets[index];
            var otherIndex = $scope.reportWidgets.indexOf(reportWidget);
            $scope.reportWidgets[index] = reportWidget;
            $scope.reportWidgets[otherIndex] = otherObj;
            var widgetOrder = $scope.reportWidgets.map(function (value, key) {
                if (!value) {
                    return;
                }
                return value.id;
            }).join(',');
            if (widgetOrder) {
                $http({method: 'GET', url: 'admin/ui/dbReportUpdateOrder/' + $stateParams.reportId + "?widgetOrder=" + widgetOrder});
            }
        };
    };
});
