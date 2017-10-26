app.directive('pieChartDirective', function ($http, $stateParams, $filter, orderByFilter) {
    return{
        restrict: 'AC',
        template: '<div ng-show="loadingPie" class="text-center"><img src="static/img/logos/loader.gif" width="40"></div>' +
                '<div ng-show="hideEmptyPie" class="text-center">{{pieEmptyMessage}}</div>',
        scope: {
            setPieChartFn: '&',
            pieChartSource: '@',
            widgetId: '@',
            widgetColumns: '@',
            pieChartId: '@',
            loadingPie: '&',
            widgetObj: '@',
            defaultChartColor: '@'
        },
        link: function (scope, element, attr) {
            var labels = {format: {}};
            scope.loadingPie = true;
            var yAxis = [];
            var columns = [];
            var xAxis;
            var ySeriesOrder = 1;
            var sortField = "";
            var sortOrder = 0;
            var displayDataFormat = {};
            var axes = {};
            var startDate = "";
            var endDate = "";
            var sortFields = [];
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
                    xAxis = {fieldName: value.fieldName, displayName: value.displayName, displayFormat: value.displayFormat};
                }
                if (value.yAxis) {
                    yAxis.push({fieldName: value.fieldName, displayName: value.displayName, displayFormat: value.displayFormat});
                    axes[value.displayName] = 'y' + (value.yAxis > 1 ? 2 : '');
                }
                if (value.yAxis > 1) {
                    y2 = {show: true, label: ''};
                }
                if (value.sortOrder) {
                    sortFields.push({fieldName: value.fieldName, sortOrder: value.sortOrder, fieldType: value.fieldType});
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
            var pieChartDataSource = JSON.parse(scope.pieChartSource);
            if (scope.pieChartSource) {
                var url = "admin/proxy/getData?";
//                if (pieChartDataSource.dataSourceId.dataSourceType == "sql") {
//                    url = "admin/proxy/getJson?url=../dbApi/admin/dataSet/getData&";
//                }

                var dataSourcePassword;
                if (pieChartDataSource.dataSourceId.password) {
                    dataSourcePassword = pieChartDataSource.dataSourceId.password;
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

                var setWidgetAccountId;
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

                scope.refreshPieChart = function () {
                    $http.get(url + 'connectionUrl=' + pieChartDataSource.dataSourceId.connectionString +
                            "&dataSetId=" + pieChartDataSource.id +
                            "&accountId=" + (getWidgetObj.accountId ? (getWidgetObj.accountId.id ? getWidgetObj.accountId.id : getWidgetObj.accountId) : $stateParams.accountId) +
                            "&userId=" + (pieChartDataSource.userId ? pieChartDataSource.userId.id : null) +
                            "&dataSetReportName=" + pieChartDataSource.reportName +
                            "&driver=" + pieChartDataSource.dataSourceId.sqlDriver +
                            "&startDate=" + $stateParams.startDate +
                            "&endDate=" + $stateParams.endDate +
                            "&productSegment=" + setProductSegment +
                            "&timeSegment=" + setTimeSegment +
                            "&networkType=" + setNetworkType +
                            '&username=' + pieChartDataSource.dataSourceId.userName +
                            '&password=' + dataSourcePassword +
                            '&widgetId=' + scope.widgetId +
                            '&url=' + pieChartDataSource.url +
                            '&port=3306&schema=vb&query=' + encodeURI(pieChartDataSource.query)).success(function (response) {
                        scope.loadingPie = false;
                        if (!response) {
                            scope.pieEmptyMessage = "No Data Found";
                            scope.hideEmptyPie = true;
                            return;
                        }
                        if (response.data == null  || response.data.length === 0) {
                            scope.pieEmptyMessage = "No Data Found";
                            scope.hideEmptyPie = true;
                        } else {
                            var loopCount = 0;
                            var sortingObj;
                            var chartMaxRecord = JSON.parse(scope.widgetObj);
                            var chartData = response.data;
                            if (sortFields.length > 0) {
                                angular.forEach(sortFields, function (value, key) {
                                    if (value.fieldType != 'day') {
                                        sortingObj = scope.orderData(chartData, sortFields);
                                        if (chartMaxRecord.maxRecord) {
                                            chartData = maximumRecord(chartMaxRecord, sortingObj);
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
                                            chartData = maximumRecord(chartMaxRecord, sortingObj);
                                        } else {
                                            chartData = sortingObj;
                                        }
                                    }
                                });
                            }
                            if (xAxis) {
                                xTicks = [xAxis.fieldName];
                                xData = chartData.map(function (a) {
                                    xTicks.push(loopCount);
                                    loopCount++;
                                    return a[xAxis.fieldName];
                                });
                                columns.push(xTicks);
                            }
                            angular.forEach(yAxis, function (value, key) {
                                ySeriesData = chartData.map(function (a) {
                                    return a[value.fieldName] || "0";
                                });
                                ySeriesData.unshift(value.displayName);
                                columns.push(ySeriesData);
                            });
                            var data = {};
                            var legends = [];
                            var yAxisField = yAxis[0];
                            chartData.forEach(function (e) {
                                legends.push(e[xAxis.fieldName]);
                                data[e[xAxis.fieldName]] = data[e[xAxis.fieldName]] ? data[e[xAxis.fieldName]] : 0 + e[yAxisField.fieldName] ? e[yAxisField.fieldName] : 0;
                            });
                            var chart = c3.generate({
                                padding: {
                                    top: 10,
                                    right: 50,
                                    bottom: 10,
                                    left: 50
                                },
                                bindto: element[0],
                                data: {
                                    json: [data],
                                    keys: {
                                        value: xData
                                    },
                                    type: 'pie'
                                },
                                pie: {
                                    label: {
                                        format: function (value, ratio, id) {
                                            var percentage = d3.format("%.2f")(ratio);
                                            var columnValue = dashboardFormat(yAxisField, value);
                                            if (columnValue == 'NaN') {
                                                columnValue = "-";
                                            }
                                            return  percentage + ", \n" + columnValue;
                                        }
                                    }
                                },
                                color: {
                                    pattern: chartColors ? chartColors : defaultColors
                                },
                                tooltip: {
                                    show: true,
                                    format: {
                                        value: function (value, ratio, id) {
                                            var percentage = d3.format("%.2f")(ratio);
                                            var columnValue = dashboardFormat(yAxisField, value);
                                            if (columnValue == 'NaN') {
                                                columnValue = "-";
                                            }
                                            return  percentage + ", \n" + columnValue;
                                        }
                                    }
                                },
                                axis: {
                                    x: {
                                        tick: {
                                            format: function (x) {
                                                return xData[x];
                                            }
                                        }
                                    }
                                },
                                grid: {
                                    x: {
                                        show: false
                                    },
                                    y: {
                                        show: false
                                    }
                                }
                            });
                        }
                    });
                };
                scope.setPieChartFn({pieFn: scope.refreshPieChart});
                scope.refreshPieChart();
            }
        }
    };
});