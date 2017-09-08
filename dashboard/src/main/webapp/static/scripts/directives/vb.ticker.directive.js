app.directive('tickerDirective', function ($http, $stateParams) {
    return{
        restrict: 'AE',
        template: '<div ng-show="loadingTicker"><img width="40" src="static/img/logos/loader.gif"></div>' +
                '<div  ng-hide="loadingTicker" >' +
                '<div ng-hide="hideEmptyTicker" class="hpanel stats">' +
                '<div class="panel-body h-150">' +
                '<div class="stats-title pull-left">' +
                '<h4>{{tickerTitleName}}</h4>' +
                '</div>' +
                '<div class="stats-icon pull-right">' +
//                        '<i class="pe-7s-share fa-4x"></i>' +
                '</div>' +
                '<div class="m-t-xl">' +
                '<h3 class="m-b-xs text-success">{{firstLevelTicker.totalValue}}</h3>' +
                '<span class="font-bold no-margins">' +
//                            '{{firstLevelTicker.tickerTitle}}' +
                '</span>' +
                '<div class="row">' +
                '<div class="col-xs-6">' +
                '<small class="stats-label">{{secondLevelTicker.tickerTitle}}</small>' +
                '<h4>{{secondLevelTicker.totalValue}}</h4>' +
                '</div>' +
                '<div class="col-xs-6 count">' +
                '<small class="stats-label">{{thirdLevelTicker.tickerTitle}}</small>' +
                '<h4>{{thirdLevelTicker.totalValue}}</h4>' +
                '</div>' +
                '</div>' +
                '</div>' +
                '</div>' +
                '</div>' +
                '<div ng-show="hideEmptyTicker">{{tickerEmptyMessage}}</div>' +
                '</div>',
        scope: {
            setTickerFn: '&',
            tickerSource: '@',
            tickerId: '@',
            tickerColumns: '@',
            tickerTitleName: '@',
            widgetObj: '@'
        },
        link: function (scope, element, attr) {
            if (!scope.widgetObj) {
                return;
            }
            var getWidgetObj = JSON.parse(scope.widgetObj);
            scope.loadingTicker = true;
            var tickerName = [];
            angular.forEach(JSON.parse(scope.tickerColumns), function (value, key) {

                if (!value) {
                    return;
                }
                tickerName.push({fieldName: value.fieldName, displayName: value.displayName, displayFormat: value.displayFormat});
            });

            var format = function (column, value) {
                if (!value) {
                    return "-";
                }
                if (column.displayFormat) {
//                    if (isNaN(value)) {
//                        return "-";
//                    }
                    var columnValue = dashboardFormat(column, value);
                    if (columnValue == 'NaN') {
                        columnValue = "-";
                    }
                    return columnValue;
                }
                return value;
            };

            var setData = [];
            var data = [];
            var tickerDataSource = JSON.parse(scope.tickerSource);
            var url = "admin/proxy/getData?";
//            if (tickerDataSource.dataSourceId.dataSourceType == "sql") {
//                url = "admin/proxy/getJson?url=../dbApi/admin/dataSet/getData&";
//            }
            var dataSourcePassword;
            if (tickerDataSource.dataSourceId.password) {
                dataSourcePassword = tickerDataSource.dataSourceId.password;
            } else {
                dataSourcePassword = '';
            }

            var getWidgetObj = JSON.parse(scope.widgetObj);
            var setProductSegment;
            var setTimeSegment;
            var setNetworkType;

            if (getWidgetObj.productSegment && getWidgetObj.productSegment.type) {
                setProductSegment = getWidgetObj.productSegment.type;
            } else {
                setProductSegment = getWidgetObj.productSegment;
            }

            if (getWidgetObj.timeSegment && getWidgetObj.timeSegment.type) {
                setTimeSegment = getWidgetObj.timeSegment.type;
            } else {
                setTimeSegment = getWidgetObj.timeSegment;
            }

            if (getWidgetObj.networkType && getWidgetObj.networkType.type) {
                setNetworkType = getWidgetObj.networkType.type;
            } else {
                setNetworkType = getWidgetObj.networkType;
            }

            scope.refreshTicker = function () {
                $http.get(url + 'connectionUrl=' + tickerDataSource.dataSourceId.connectionString +
                        "&dataSetId=" + tickerDataSource.id +
                        "&accountId=" + $stateParams.accountId +
                        "&productSegment=" + setProductSegment +
                        "&timeSegment=" + setTimeSegment +
                        "&networkType=" + setNetworkType +
                        "&userId=" + (tickerDataSource.userId ? tickerDataSource.userId.id : null) +
                        "&driver=" + tickerDataSource.dataSourceId.sqlDriver +
                        "&dataSetReportName=" + tickerDataSource.reportName +
                        "&location=" + $stateParams.locationId +
                        "&startDate=" + $stateParams.startDate +
                        "&endDate=" + $stateParams.endDate +
                        "&productSegment=" + setProductSegment +
                        "&timeSegment=" + setTimeSegment +
                        "&networkType=" + setNetworkType +
                        '&username=' + tickerDataSource.dataSourceId.userName +
                        '&password=' + dataSourcePassword +
                        '&widgetId=' + scope.tickerId +
                        '&url=' + tickerDataSource.url +
                        '&port=3306&schema=vb&query=' + encodeURI(tickerDataSource.query)).success(function (response) {
                    scope.tickers = [];
                    scope.loadingTicker = false;
                    if (!response) {
                        scope.tickerEmptyMessage = "No Data Found";
                        scope.hideEmptyTicker = true;
                        return;
                    }
                    if (response.data.length === 0) {
                        scope.tickerEmptyMessage = "No Data Found";
                        scope.hideEmptyTicker = true;
                    } else {
                        angular.forEach(tickerName, function (value, key) {
                            var tickerData = response.data;
                            var loopCount = 0;
                            data = [value.fieldName];
                            setData = tickerData.map(function (a) {
                                data.push(loopCount);
                                loopCount++;
                                return a[value.fieldName];
                            });
                            var total = 0;
                            for (var i = 0; i < setData.length; i++) {
                                if (setData[i].toString().indexOf(',') !== -1) {
                                    setData[i] = setData[i].replace(/\,/g, '');
                                }
                                total += parseFloat(setData[i]);
                            }
                            scope.tickers.push({tickerTitle: value.displayName, totalValue: format(value, total)});
                        });
                    }
                    scope.firstLevelTicker = scope.tickers[0];
                    scope.secondLevelTicker = scope.tickers[1];
                    scope.thirdLevelTicker = scope.tickers[2];
                });
            };
            scope.setTickerFn({tickerFn: scope.refreshTicker});
            scope.refreshTicker();
        }
    };
});