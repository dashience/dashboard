app.directive('lineChartDirective', function ($http, $filter, $stateParams, orderByFilter) {
    return{
        restrict: 'A',
        template: '<div ng-show="loadingLine" class="text-center"><img src="static/img/logos/loader.gif" width="40"></div>' +
                '<div ng-show="hideEmptyLine" class="text-center">{{lineEmptyMessage}}</div>',
        scope: {
            setLineChartFn: '&',
            lineChartSource: '@',
            widgetId: '@',
            widgetColumns: '@',
            lineChartId: '@',
            widgetObj: '@',
            defaultChartColor: '@',
            compareDateRange: '@',
            urlType: '@'
        },
        link: function (scope, element, attr) {
            var labels = {format: {}};
            scope.loadingLine = true;
            var yAxis = [];
            var columns = [];
            var xAxis;
            var ySeriesOrder = 1;
            var sortField = "";
            var sortOrder = 0;
            var sortDataType = "number";
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
            var startDates1 = (moment().subtract(1, 'months').startOf('month'));
            var endDates1 = (moment().subtract(1, 'months').endOf('month'));
            var startDates2 = (moment().subtract(2, 'months').startOf('month'));
            var endDates2 = (moment().subtract(2, 'months').endOf('month'));
            var startDate1 = $filter('date')(new Date(startDates1), 'MM/dd/yyyy');
            var endDate1 = $filter('date')(new Date(endDates1), 'MM/dd/yyyy');
            var startDate2 = $filter('date')(new Date(startDates2), 'MM/dd/yyyy');
            var endDate2 = $filter('date')(new Date(endDates2), 'MM/dd/yyyy');
            var url;
            var urlPath;
            var getCompareStatus;
            var dateRangeType;
            var compareDateRangeDates = "&startDate1=" + startDate1 + "&endDate1=" + endDate1 + "&startDate2=" + startDate2 + "&endDate2=" + endDate2;
            var monthEndWithoutCompare = "&startDate=" + startDate1 + "&endDate=" + endDate1;
            var compareRange = JSON.parse(scope.compareDateRange);
            var isCompare = scope.urlType;
            var url;
            if (isCompare == 'compareOn') {
                var compareStartDate = compareRange.startDate;
                var compareEndDate = compareRange.endDate;
                dateRangeType = '&startDate1=' + $stateParams.startDate +
                        "&endDate1=" + $stateParams.endDate +
                        "&startDate2=" + compareStartDate +
                        "&endDate2=" + compareEndDate;
                url = "admin/proxy/getCompareData?";
            } else {
                dateRangeType = '&startDate=' + $stateParams.startDate + "&endDate=" + $stateParams.endDate;
                url = "admin/proxy/getData?";
            }
            var lineChartDataSource = JSON.parse(scope.lineChartSource);
            if (scope.lineChartSource) {

//                var url = "admin/proxy/getData?";
//                if (lineChartDataSource.dataSourceId.dataSourceType == "sql") {
//                    url = "admin/proxy/getJson?url=../dbApi/admin/dataSet/getData&";
//                }
                var dataSourcePassword;
                if (lineChartDataSource.dataSourceId.password) {
                    dataSourcePassword = lineChartDataSource.dataSourceId.password;
                } else {
                    dataSourcePassword = '';
                }
                var getWidgetObj = JSON.parse(scope.widgetObj);
                var defaultColors = scope.defaultChartColor ? JSON.parse(scope.defaultChartColor) : "";
                //var defaultColors = ['#59B7DE', '#D7EA2B', '#FF3300', '#E7A13D', '#3F7577', '#7BAE16'];

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
                scope.refreshLineChart = function () {
                    $http.get(url + 'connectionUrl=' + lineChartDataSource.dataSourceId.connectionString +
                            "&dataSetId=" + lineChartDataSource.id +
                            "&accountId=" + (getWidgetObj.accountId ? (getWidgetObj.accountId.id ? getWidgetObj.accountId.id : getWidgetObj.accountId) : $stateParams.accountId) +
                            "&userId=" + (lineChartDataSource.userId ? lineChartDataSource.userId.id : null) +
                            "&driver=" + lineChartDataSource.dataSourceId.sqlDriver +
//                            "&startDate=" + $stateParams.startDate +
//                            "&endDate=" + $stateParams.endDate +
                            dateRangeType +
                            "&productSegment=" + setProductSegment +
                            "&timeSegment=" + setTimeSegment +
                            "&networkType=" + setNetworkType +
                            '&username=' + lineChartDataSource.dataSourceId.userName +
                            '&password=' + dataSourcePassword +
                            "&dataSetReportName=" + lineChartDataSource.reportName +
                            '&widgetId=' + scope.widgetId +
                            '&url=' + lineChartDataSource.url +
                            '&port=3306&schema=vb&query=' + encodeURI(lineChartDataSource.query)).success(function (response) {
                        scope.loadingLine = false;
                        if (!response.data) {
                            scope.lineEmptyMessage = "No Data Found";
                            scope.hideEmptyLine = true;
                            return;
                        }
                        if (response.data.length === 0) {
                            scope.lineEmptyMessage = "No Data Found";
                            scope.hideEmptyLine = true;
                        } else {
                            var loopCount = 0;
                            var sortingObj;
                            var gridData = JSON.parse(scope.widgetObj);
                            var chartMaxRecord = JSON.parse(scope.widgetObj);
                            var chartData = response.data;
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
                            }
                            if (chartMaxRecord.maxRecord > 0) {
                                chartData = chartData.slice(0, chartMaxRecord.maxRecord);
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
                                var ySeriesData = chartData.map(function (a) {
                                    return a[value.fieldName] || "0";
                                });
                                if (isCompare == 'compareOn') {
                                var ySeriesData1 = chartData.map(function (a) {
                                    if (Object.keys(a.metrics1).length !== 0) {
                                        return a.metrics1[value.fieldName] || "0";
                                    } else if (Object.keys(a.dimensions).length !== 0) {
                                        return a.dimensions[value.fieldName] || "0";
                                    } else {
                                        return a[value.fieldName] || "0";
                                    }
                                });
                                var ySeriesData2 = chartData.map(function (a) {
                                    if (Object.keys(a.metrics2).length !== 0) {
                                        return a.metrics2[value.fieldName] || "0";
                                    } else if (Object.keys(a.dimensions).length !== 0) {
                                        return a.dimensions[value.fieldName] || "0";
                                    } else {
                                        return a[value.fieldName] || "0";
                                    }
                                });
                                    var sumaryRange1 = response.summary.dateRange1.startDate + " - " + response.summary.dateRange1.endDate;
                                    var sumaryRange2 = response.summary.dateRange2.startDate + " - " + response.summary.dateRange2.endDate;
                                    var joinCompare1 = value.displayName + " (" + sumaryRange1 + ")";
                                    var joinCompare2 = value.displayName + " (" + sumaryRange2 + ")";
                                    ySeriesData1.unshift(joinCompare1);
                                    ySeriesData2.unshift(joinCompare2);
                                    columns.push(ySeriesData1);
                                    columns.push(ySeriesData2);
//                            labels["format"][joinCompare1] = function (value) {
//                                return value;
//                            };
//                            labels["format"][joinCompare2] = function (value) {
//                                return value;
//                            };
                                    var displayName = value.displayName;
                                    if (value.displayFormat) {
                                        var format = value.displayFormat;
                                        if (value.displayFormat && value.displayFormat != 'H:M:S') {
                                            labels["format"][joinCompare1] = function (value) {
                                                if (format.indexOf("%") > -1) {
                                                    return d3.format(format)(value / 100);
                                                }
                                                return d3.format(format)(value);
                                            };
                                            labels["format"][joinCompare2] = function (value) {
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
                                        labels["format"][joinCompare1] = function (value) {
                                            return value;
                                        };
                                        labels["format"][joinCompare2] = function (value) {
                                            return value;
                                        };
                                        labels["format"][displayName] = function (value) {
                                            return value;
                                        };
                                    }
                                } else {
                                    ySeriesData.unshift(value.displayName);
                                    columns.push(ySeriesData);
                                }
                                //yaxis mapping data
//                            angular.forEach(yAxis, function (value, key) {
//                                ySeriesData = chartData.map(function (a) {
//                                    return a[value.fieldName] || "0";
//                                });
//                                ySeriesData.unshift(value.displayName);
//                                columns.push(ySeriesData);
                            });


                            angular.forEach(combinationTypes, function (value, key) {
                                chartCombinationtypes[[value.fieldName]] = value.combinationType;
                            });

                            var data = {
                                x: xAxis.fieldName,
                                columns: columns,
                                labels: labels,
                                type: 'area',
                                axes: axes,
                                types: chartCombinationtypes
                            };
                            console.log("data");
                            console.log(data);

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
                };
                scope.setLineChartFn({lineFn: scope.refreshLineChart});
                scope.refreshLineChart();
            }
        }
    };
});
