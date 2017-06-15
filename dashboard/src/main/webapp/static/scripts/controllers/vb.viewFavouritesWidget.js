app.controller('ViewFavouritesWidgetController', function ($http, $scope, $stateParams, $timeout, $state) {
    $scope.accountId = $stateParams.accountId;
    $scope.accountName = $stateParams.accountName;
    $scope.productId = $stateParams.productId;
    $scope.startDate = $stateParams.startDate;
    $scope.endDate = $stateParams.endDate;
    $scope.favouriteId = $stateParams.favouriteId;
    $scope.favouritesWidgets = [];
    $scope.favouriteName = $stateParams.favouriteName;
    $http.get("admin/tag/widgetTag/" + $stateParams.favouriteName).success(function (response) {
        var widgetItems = [];
        widgetItems = response;
        $http.get("admin/tag/getAllFav/").success(function (favResponse) {
            widgetItems.forEach(function (value, key) {
                favWidget = $.grep(favResponse, function (b) {
                    return b.id === value.widgetId.id;
                });
                if (favWidget.length > 0) {
                    value.widgetId.isFav = true;
                } else {
                    value.widgetId.isFav = false;
                }
            });
        });

        $scope.favouritesWidgets = widgetItems;
    });

    $scope.toggleFavourite = function (favouritesWidget, index) {
        var isFav = favouritesWidget.widgetId.isFav;
        if (isFav) {
            $http({method: 'POST', url: "admin/tag/removeFav/" + favouritesWidget.widgetId.id}).success(function (response) {
                $scope.favouritesWidgets.splice(index, 1);
            });
            favouritesWidget.widgetId.isFav = false;
        } else {
            $http({method: 'POST', url: "admin/tag/setFav/" + favouritesWidget.widgetId.id});
            favouritesWidget.widgetId.isFav = true;
        }
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
    
    $scope.deleteReportWidget = function (favouritesWidget, index) {                            //Delete Widget
        $http({method: 'DELETE', url: 'admin/ui/dbWidget/' + favouritesWidget.widgetId.id}).success(function (response) {
            $scope.favouritesWidgets.splice(index, 1);
        });
    };

    $http.get("admin/report/reportWidget").success(function (response) {
        $scope.reportWidgets = response;
    });

    $http.get('admin/report/getReport').success(function (response) {
        $scope.reportList = response;
    });
    $scope.addWidgetToReport = function (favouritesWidget) {
        var data = {};
        data.widgetId = favouritesWidget.widgetId.id;
        data.reportId = favouritesWidget.widgetId.reportWidget.id;
        $http({method: favouritesWidget.widgetId.id ? 'PUT' : 'POST', url: 'admin/report/reportWidget', data: data}).success(function (response) {
        });
        $scope.reportLogo = "";
        $scope.reportDescription = "";
        $scope.reportWidgetTitle = "";
        $scope.showReportWidgetName = false;
        $scope.showReportEmptyMessage = false;
    };

    $scope.selectReport = function (reportWidget) {
        $scope.showReportWidgetName = false;
        $scope.reportWidgetTitle = []
        $scope.reportLogo = reportWidget.logo;
        $scope.reportDescription = reportWidget.description;
        $http.get("admin/report/reportWidget/" + reportWidget.id + "?locationId=" + $stateParams.accountId).success(function (response) {
            if (response.length > 0) {
                $scope.showReportWidgetName = true;
                $scope.reportWidgetTitle = response;
                $scope.showReportEmptyMessage = false;
            } else {
                $scope.showReportEmptyMessage = true;
                $scope.reportEmptyMessage = "No Data Found"
            }
        });
    };

    $scope.goReport = function () {
        $state.go('index.report.reports', {accountId: $stateParams.accountId, accountName: $stateParams.accountName, startDate: $stateParams.startDate, endDate: $stateParams.endDate});
    };

    $scope.setLineChartFn = function (lineFn) {
        $scope.directiveLineFn = lineFn;
    };
    $scope.setAreaChartFn = function (areaFn) {
        $scope.directiveAreaFn = areaFn;
    };
    $scope.setBarChartFn = function (barFn) {
        $scope.directiveBarFn = barFn;
    };
    $scope.setPieChartFn = function (pieFn) {
        $scope.directivePieFn = pieFn;
    };
    $scope.setStackedBarChartFn = function (stackedBarChartFn) {
        $scope.directiveStackedBarChartFn = stackedBarChartFn;
    };
    $scope.setTableChartFn = function (tableFn) {
        $scope.directiveTableFn = tableFn;
    };
    $scope.setTickerFn = function (tickerFn) {
        $scope.directiveTickerFn = tickerFn;
    };
});