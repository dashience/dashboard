app.directive('areaChartDirective', function ($http, $stateParams, $filter, orderByFilter) {
    return {
        restrict: 'A',
        template: '<div ng-show="loadingArea" class="text-center"><img src="static/img/logos/loader.gif" width="40"></div>' +
                '<div ng-show="hideEmptyArea" class="text-center">{{areaEmptyMessage}}</div>',
        scope: {
            setAreaChartFn: '&',
            widgetId: '@',
            areaChartSource: '@',
            widgetColumns: '@',
            pieChartId: '@',
            widgetObj: '@',
            defaultChartColor: '@'
        },
        link: function (scope, element, attr) {
            var labels = {format: {}};
            scope.loadingArea = true;
            var yAxis = [];
            var columns = [];
            var xAxis;
            var ySeriesOrder = 1;
            var sortField = "";
            var sortOrder = 0;
            var displayDataFormat = {};
            var y2 = {show: false, label: ''};
            var axes = {};
            var startDate = "";
            var endDate = "";
            var sortFields = [];
            var combinationTypes = [];
            var chartCombinationtypes = [];
            if (!scope.widgetColumns) {
                return;
            }
            angular.forEach(JSON.parse(scope.widgetColumns), function (value, key) {
                if (!labels["format"]) {
                    labels = {format: {}};
                }
                if (value.displayFormat) {
                    var format = value.displayFormat;
                    var displayName = value.displayName;

                    if (value.displayFormat && value.displayFormat != 'H:M:S') {
                        labels["format"][displayName] = function (value) {
                            if (format.indexOf("%") > -1) {
                                return d3.format(format)(value / 100);
                            }
                            return d3.format(format)(value);
                        };
                    } else {
                        labels["format"][displayName] = function (value) {
                            return formatBySecond(parseInt(value))
                        };
                    }
                } else {
                    var displayName = value.displayName;
                    labels["format"][displayName] = function (value) {
                        return value;
                    };
                }
                if (value.sortOrder) {
                    sortField = value.fieldName;
                    sortOrder = value.sortOrder;
                }
                if (value.xAxis) {
                    xAxis = {fieldName: value.fieldName, displayName: value.displayName};
                }
                if (value.yAxis) {
                    yAxis.push({fieldName: value.fieldName, displayName: value.displayName});
                    axes[value.displayName] = 'y' + (value.yAxis > 1 ? 2 : '');
                }
                if (value.yAxis > 1) {
                    y2 = {show: true, label: ''};
                }
                if (value.sortOrder) {
                    sortFields.push({fieldName: value.fieldName, sortOrder: value.sortOrder, fieldType: value.fieldType});
                }
                if (value.combinationType) {
                    combinationTypes.push({fieldName: value.fieldName, combinationType: value.combinationType});
                }
            });
            var xData = [];
            var xTicks = [];
            scope.orderData = function (list, fieldnames) {
                if (fieldnames.length == 0) {
                    return list;
                }
                var fieldsOrder = [];
                angular.forEach(fieldnames, function (value, key) {
                    if (value.fieldType == "string") {
                        if (value.sortOrder == "asc") {
                            fieldsOrder.push(value.fieldName);
                        } else if (value.sortOrder == "desc") {
                            fieldsOrder.push("-" + value.fieldName);
                        }
                    } else if (value.fieldType == "number") {
                        if (value.sortOrder == "asc") {
                            fieldsOrder.push(function (a) {

                                var parsedValue = parseFloat(a[value.fieldName]);
                                if (isNaN(parsedValue)) {
                                    return 0;
                                }
                                return parsedValue;
                            });
                        } else if (value.sortOrder == "desc") {
                            fieldsOrder.push(function (a) {
                                var parsedValue = parseFloat(a[value.fieldName]);
                                if (isNaN(parsedValue)) {
                                    return 0;
                                }
                                return -1 * parsedValue;
                            });
                        }
                    } else if (value.fieldType == "date") {
                        if (value.sortOrder == "asc") {
                            fieldsOrder.push(function (a) {

                                var parsedDate = new Date(a[value.fieldName]);
                                var parsedValue = parsedDate.getTime() / 1000;
                                if (isNaN(parsedValue)) {
                                    return 0;
                                }
                                return parsedValue;
                            });
                        } else if (value.sortOrder == "desc") {
                            fieldsOrder.push(function (a) {
                                var parsedDate = new Date(a[value.fieldName]);
                                var parsedValue = parsedDate.getTime() / 1000;
                                if (isNaN(parsedValue)) {
                                    return 0;
                                }
                                return -1 * parsedValue;
                            });
                        }
                    } else {
                        if (value.sortOrder == "asc") {
                            fieldsOrder.push(function (a) {
                                var parsedValue = parseFloat(a[value.fieldName]);
                                if (isNaN(parsedValue)) {
                                    return a[value.fieldName];
                                }
                                return parsedValue;
                            });
                        } else if (value.sortOrder == "desc") {
                            fieldsOrder.push(function (a) {
                                return -1 * parseFloat(a[value.fieldName])
                            });
                        }
                    }
                });
                return $filter('orderBy')(list, fieldsOrder);
            };
            function maximumRecord(maxValue, list) {
                var maxData;
                if (maxValue.maxRecord > 0) {
                    maxData = list.slice(0, maxValue.maxRecord);
                }
                return maxData;
            }
            var areaChartDataSource = JSON.parse(scope.areaChartSource);
            if (scope.areaChartSource) {
                var url = "admin/proxy/getData?";
//                if (areaChartDataSource.dataSourceId.dataSourceType == "sql") {
//                    url = "admin/proxy/getJson?url=../dbApi/admin/dataSet/getData&";
//                }

                var dataSourcePassword;
                if (areaChartDataSource.dataSourceId.password) {
                    dataSourcePassword = areaChartDataSource.dataSourceId.password;
                } else {
                    dataSourcePassword = '';
                }
                var getWidgetObj = JSON.parse(scope.widgetObj);
                var defaultColors = scope.defaultChartColor ? JSON.parse(scope.defaultChartColor) : "";
//                var defaultColors = ['#59B7DE', '#D7EA2B', '#FF3300', '#E7A13D', '#3F7577', '#7BAE16'];
                var widgetChartColors;
                if (getWidgetObj.chartColorOption) {
                    widgetChartColors = getWidgetObj.chartColorOption.split(',');
                }
                var setWidgetChartColors = getWidgetObj.chartColors ? getWidgetObj.chartColors : "";
                var chartColors = widgetChartColors ? widgetChartColors : setWidgetChartColors;

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

                scope.refreshAreaChart = function () {
                    $http.get(url + 'connectionUrl=' + areaChartDataSource.dataSourceId.connectionString +
                            "&dataSetId=" + areaChartDataSource.id +
                            "&accountId=" + (getWidgetObj.accountId ? (getWidgetObj.accountId.id ? getWidgetObj.accountId.id : getWidgetObj.accountId) : $stateParams.accountId) +
                            "&userId=" + (areaChartDataSource.userId ? areaChartDataSource.userId.id : null) +
                            "&dataSetReportName=" + areaChartDataSource.reportName +
                            "&driver=" + areaChartDataSource.dataSourceId.sqlDriver +
//                            "&location=" + $stateParams.locationId +
                            "&startDate=" + $stateParams.startDate +
                            "&endDate=" + $stateParams.endDate +
                            "&productSegment=" + setProductSegment +
                            "&timeSegment=" + setTimeSegment +
                            "&networkType=" + setNetworkType +
                            '&username=' + areaChartDataSource.dataSourceId.userName +
                            '&password=' + dataSourcePassword +
                            '&widgetId=' + scope.widgetId +
                            '&url=' + areaChartDataSource.url +
                            '&port=3306&schema=vb&query=' + encodeURI(areaChartDataSource.query)).success(function (response) {
                        scope.loadingArea = false;
                        if (!response) {
                            scope.areaEmptyMessage = "No Data Found";
                            scope.hideEmptyArea = true;
                            return;
                        }
                        if (response.data.length === 0) {
                            scope.areaEmptyMessage = "No Data Found";
                            scope.hideEmptyArea = true;
                        } else {
                            var loopCount = 0;
                            var sortingObj;
                            var gridData = JSON.parse(scope.widgetObj);
                            var chartMaxRecord = JSON.parse(scope.widgetObj)
                            var chartData;// = response.data;
                            if (sortFields.length > 0) {
                                angular.forEach(sortFields, function (value, key) {
                                    if (value.fieldType != 'day') {
                                        sortingObj = scope.orderData(chartData, sortFields);
                                        if (chartMaxRecord.maxRecord) {
                                            chartData = maximumRecord(chartMaxRecord, sortingObj)
                                        } else {
                                            chartData = sortingObj;
                                        }
                                    } else {
                                        var dateOrders = ["Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"]
                                        sortingObj = orderByFilter(chartData, function (item) {
                                            if (value.sortOrder === 'asc') {
                                                return dateOrders.indexOf(item[value.fieldName]);
                                            } else if (value.sortOrder === 'desc') {
                                                return dateOrders.indexOf(item[value.fieldName]) * -1;
                                            }
                                        });
                                        if (chartMaxRecord.maxRecord) {
                                            chartData = maximumRecord(chartMaxRecord, sortingObj)
                                        } else {
                                            chartData = sortingObj;
                                        }
                                    }
                                });
                            } else {
                                var responseObject = response.data;
                                if (chartMaxRecord.maxRecord) {
                                    chartData = maximumRecord(chartMaxRecord, responseObject);
                                } else {
                                    chartData = responseObject;
                                }
                            }
                            xTicks = [xAxis.fieldName];
                            xData = chartData.map(function (a) {
                                xTicks.push(loopCount);
                                loopCount++;
                                return a[xAxis.fieldName];
                            });
                            columns.push(xTicks);
                            angular.forEach(yAxis, function (value, key) {
                                ySeriesData = chartData.map(function (a) {
                                    return a[value.fieldName] || "0";
                                });
                                ySeriesData.unshift(value.displayName);
                                columns.push(ySeriesData);
                            });
                            angular.forEach(combinationTypes, function (value, key) {
                                chartCombinationtypes[[value.fieldName]] = value.combinationType;
                            });
                            var gridLine = false;
                            if (gridData.isGridLine == 'Yes') {
                                gridLine = true;
                            } else {
                                gridLine = false;
                            }
                            var chart = c3.generate({
                                padding: {
                                    top: 10,
                                    right: 50,
                                    bottom: 10,
                                    left: 50,
                                },
                                bindto: element[0],
                                data: {
                                    x: xAxis.fieldName,
                                    columns: columns,
                                    labels: labels,
                                    type: 'area',
                                    axes: axes,
                                    types: chartCombinationtypes
                                },
                                color: {
                                    pattern: chartColors ? chartColors : defaultColors
                                },
                                tooltip: {show: false},
                                axis: {
                                    x: {
                                        tick: {
                                            format: function (x) {
                                                return xData[x];
                                            },
                                            culling: false
                                        }
                                    },
                                    y2: y2
                                },
                                grid: {
                                    x: {
                                        show: gridLine
                                    },
                                    y: {
                                        show: gridLine
                                    }
                                }
                            });
                        }
                    });
                }
                scope.setAreaChartFn({areaFn: scope.refreshAreaChart});
                scope.refreshAreaChart();
            }
        }
    };
});