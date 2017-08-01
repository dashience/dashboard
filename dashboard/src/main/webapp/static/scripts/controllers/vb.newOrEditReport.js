app.controller("NewOrEditReportController", function ($scope, $http, $stateParams, $filter, $window, localStorageService, $timeout) {
    $scope.permission = localStorageService.get("permission");
    $scope.accountId = $stateParams.accountId;
    $scope.accountName = $stateParams.accountName;
    $scope.reportId = $stateParams.reportId;
    $scope.startDate = $stateParams.startDate;
    $scope.endDate = $stateParams.endDate;

    $scope.reportWidgets = [];

    $http.get("admin/report/" + $stateParams.reportId).success(function (response) {
        $scope.reports = response;
        if (!response) {
            $scope.editReport = true;
        }
        $scope.reportTitle = response.reportTitle;
        $scope.description = response.description;
        $scope.reportTitle = response.reportTitle;
        $scope.uploadLogo = response.logo;
        angular.forEach($scope.report, function (value, key) {
            $scope.logo = window.atob(value.logo);
        })
    });

    $http.get('admin/report/reportWidget/' + $stateParams.reportId).success(function (response) {
        var widgetItems = response;
        $http.get("admin/ui/getChartColorByUserId").success(function (response) {
            $scope.userChartColors = response;
            var widgetColors;
            if (response.optionValue) {
                widgetColors = response.optionValue.split(',');
            }
            widgetItems.forEach(function (value, key) {
                value.widgetId.chartColors = widgetColors;
            });
            $scope.reportWidgets = widgetItems;
        }).error(function () {
            $scope.reportWidgets = widgetItems;
        });
    });

    $scope.downloadReportPdf = function (report) {
        var url = "admin/proxy/downloadReport/" + $stateParams.reportId + "?dealerId=" + $stateParams.accountId + "&location=" + $stateParams.accountId + "&accountId=" + $stateParams.accountId + "&startDate=" + $stateParams.startDate + "&endDate=" + $stateParams.endDate + "&exportType=pdf";
        $window.open(url);
    }

    $scope.uploadLogo = "static/img/logos/deeta-logo.png";       //Logo Upload
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
        if (0 == $stateParams.reportId) {
            $scope.selectReportId = "";
        } else {
            $scope.selectReportId = $stateParams.reportId;
        }
        var data = {
            id: $scope.selectReportId,
            reportTitle: $scope.reportTitle,
            description: $scope.description,
            logo: $scope.uploadLogo   //window.btoa($scope.uploadLogo)
        };
        $http({method: $scope.selectReportId ? 'PUT' : 'POST', url: 'admin/report/report', data: data}).success(function () {
            $stateParams.reportId = $scope.reports[$scope.reports.length - 1].id;
        });
    };

    $scope.expandWidget = function (widget) {
        var expandchart = widget.chartType;
        widget.chartType = null;
        if (widget.width == 3) {
            widget.width = widget.width + 1;
        } else if (widget.width == 4) {
            widget.width = widget.width + 2;
        } else if (widget.width == 6) {
            widget.width = widget.width + 2;
        } else {
            widget.width = 12;
        }
        saveWidgetSize(widget, expandchart)
    };

    $scope.reduceWidget = function (widget) {
        var expandchart = widget.chartType;
        widget.chartType = null;

        if (widget.width == 12) {
            widget.width = widget.width - 4;
        } else if (widget.width == 8) {
            widget.width = widget.width - 2;
        } else if (widget.width == 6) {
            widget.width = widget.width - 2;
        } else {
            widget.width = 3;
        }
        saveWidgetSize(widget, expandchart)
    };

    function saveWidgetSize(widget, expandchart) {
        $timeout(function () {
            widget.chartType = expandchart;
            var data = {
                id: widget.id,
                chartType: widget.chartType,
                widgetTitle: widget.widgetTitle,
                widgetColumns: widget.columns,
                dataSourceId: widget.dataSourceId.id,
                dataSetId: widget.dataSetId.id,
                tableFooter: widget.tableFooter,
                zeroSuppression: widget.zeroSuppression,
                maxRecord: widget.maxRecord,
                dateDuration: widget.dateDuration,
                content: widget.content,
                width: widget.width,
                jsonData: widget.jsonData,
                queryFilter: widget.queryFilter
            };
            $http({method: widget.id ? 'PUT' : 'POST', url: 'admin/ui/dbWidget/' + widget.tabId.id, data: data}).success(function (response) {
            });
        }, 50);
    }

    $scope.moveWidget = function (list, from, to) {
        list.splice(to, 0, list.splice(from, 1)[0]);
        return list;
    };
    $scope.onDropComplete = function (index, reportWidget, evt) {
        if (reportWidget !== "" && reportWidget !== null) {
            var otherObj = $scope.reportWidgets[index];
            var otherIndex = $scope.reportWidgets.indexOf(reportWidget);
//            $scope.reportWidgets = $scope.moveWidget($scope.reportWidgets, otherIndex, index);
            $scope.reportWidgets[index] = reportWidget;
            $scope.reportWidgets[otherIndex] = otherObj;
            var widgetOrder = $scope.reportWidgets.map(function (value, key) {
                if (!value) {
                    return;
                }
                return value.id;
            }).join(',');
            if (widgetOrder) {
                $http({method: 'GET', url: 'admin/report/dbReportUpdateOrder/' + $stateParams.reportId + "?widgetOrder=" + widgetOrder});
            }
        }
        ;
    };

    $scope.deleteReportWidget = function (reportWidget, index) {                            //Delete Widget
        $http({method: 'DELETE', url: 'admin/report/reportWidget/' + reportWidget.id}).success(function (response) {
            $scope.reportWidgets.splice(index, 1);
        });
    };
});