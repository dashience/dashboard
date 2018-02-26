app.directive('lineChartDirective', function ($http, $filter, $stateParams, orderByFilter, chartFactory) {
    return{
        restrict: 'A',
        template: '<div ng-show="loadingLine" class="text-center"><img src="static/img/logos/loader.gif" width="40"></div>' +
                '<div ng-show="hideEmptyLine" class="text-center">{{lineEmptyMessage}}</div>',
        scope: {
            setLineChartFn: '&',
            widgetObj: '@',
            defaultChartColor: '@',
            compareDateRange: '@',
            urlType: '@'
        },
        link: function (scope, element, attr) {
            scope.loadingLine = true;
            var yAxis = [];
            var columns = [];
            var xAxis;
            var y2 = {show: false, label: ''};
            var axes = {};
            var sortFields = [];
            var combinationTypes = [];
            var chartCombinationtypes = [];
            var compareFormat = [];
            var displayFormats = [];
            var showLegend;
            var widgetObj = JSON.parse(scope.widgetObj);
            console.log("widgetObj---------->", widgetObj);
            if (!widgetObj.columns) {
                return;
            }
            console.log("lineChartSource---------->", scope.lineChartSource);
            angular.forEach(widgetObj.columns, function (value, key) {
                console.log("value-------------->", value);
                if (value.xAxis) {
                    xAxis = {fieldName: value.fieldName, displayName: value.displayName};
                }
                if (value.yAxis) {
                    var combinationType = value.combinationType == 'bar'? 'column' : value.combinationType;
                    yAxis.push({fieldName: value.fieldName, displayName: value.displayName, displayFormat: value.displayFormat, combinationType: combinationType});
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
                displayFormats.push({displayName: value.displayName, displayFormat: value.displayFormat});
            });
            console.log("combination type---------->", yAxis);
            var xData = [];
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
            var isCompare = scope.urlType == 'compareOn' && widgetObj.chartType != 'pie' ? true : false;
            var compareRange = JSON.parse(scope.compareDateRange);
            var url;
            var dateRangeType;
            if (isCompare) {
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
            var lineChartDataSource = widgetObj.dataSetId;
            if (lineChartDataSource) {

                var dataSourcePassword;
                if (lineChartDataSource.dataSourceId.password) {
                    dataSourcePassword = lineChartDataSource.dataSourceId.password;
                } else {
                    dataSourcePassword = '';
                }
                var widgetChartColors;
                if (widgetObj.chartColorOption) {
                    widgetChartColors = widgetObj.chartColorOption.split(',');
                }
                var setWidgetChartColors = widgetObj.chartColors ? widgetObj.chartColors : "";
                var chartColors = widgetChartColors ? widgetChartColors : setWidgetChartColors;

                var setProductSegment;
                var setTimeSegment;
                var setNetworkType;

                if (widgetObj.productSegment && widgetObj.productSegment.type) {
                    setProductSegment = widgetObj.productSegment.type;
                } else {
                    setProductSegment = widgetObj.productSegment;
                }

                if (widgetObj.timeSegment && widgetObj.timeSegment.type) {
                    setTimeSegment = widgetObj.timeSegment.type;
                } else {
                    setTimeSegment = widgetObj.timeSegment;
                }

                if (widgetObj.networkType && widgetObj.networkType.type) {
                    setNetworkType = widgetObj.networkType.type;
                } else {
                    setNetworkType = widgetObj.networkType;
                }
                var groupingNames = [];
                var groupingNames2 = [];
                scope.refreshLineChart = function () {
                    $http.get(url + 'connectionUrl=' + lineChartDataSource.dataSourceId.connectionString +
                            "&dataSetId=" + lineChartDataSource.id +
                            "&accountId=" + (widgetObj.accountId ? (widgetObj.accountId.id ? widgetObj.accountId.id : widgetObj.accountId) : $stateParams.accountId) +
                            "&userId=" + (lineChartDataSource.userId ? lineChartDataSource.userId.id : null) +
                            "&driver=" + lineChartDataSource.dataSourceId.sqlDriver +
                            dateRangeType +
                            "&productSegment=" + setProductSegment +
                            "&timeSegment=" + setTimeSegment +
                            "&networkType=" + setNetworkType +
                            '&username=' + lineChartDataSource.dataSourceId.userName +
                            '&password=' + dataSourcePassword +
                            "&dataSetReportName=" + lineChartDataSource.reportName +
                            '&widgetId=' + widgetObj.id +
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
                            var chartData = response.data;
                            if (sortFields.length > 0) {
                                angular.forEach(sortFields, function (value, key) {
                                    if (value.fieldType != 'day') {
                                        sortingObj = scope.orderData(chartData, sortFields);
                                        if (widgetObj.maxRecord) {
                                            chartData = maximumRecord(widgetObj, sortingObj)
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

                                        if (widgetObj.maxRecord) {
                                            chartData = maximumRecord(widgetObj, sortingObj)
                                        } else {
                                            chartData = sortingObj;
                                        }
                                    }
                                });
                            }
                            if (widgetObj.maxRecord > 0) {
                                chartData = chartData.slice(0, widgetObj.maxRecord);
                            }
                            console.log("chartData--------->", chartData);
                            if (xAxis) {
                                xData = chartData.map(function (a) {
                                    return a[xAxis.fieldName];
                                });
                            }

                            var formattedDataTest = [];
                            var formattedDataVal = {};
                            var formattedData = [];
                            if (widgetObj.chartType == 'pie') {
                                angular.forEach(chartData, function (value) {
                                    var arrayData = parseFloat((angular.isDefined(value[yAxis[0].fieldName]) == true) ? value[yAxis[0].fieldName] : 0) || 0;
                                    var tempArray = {"name": value[xAxis.fieldName], "y": arrayData};
                                    console.log("tempArray-------------------->", tempArray);
                                    formattedDataTest.push(tempArray);
                                });
                                formattedDataVal = {name: yAxis[0].fieldName, data: formattedDataTest};
                                formattedData.push(formattedDataVal);
                            } else {
                                angular.forEach(yAxis, function (value, key) {
                                    var ySeriesData = chartData.map(function (a) {
                                        return parseFloat((angular.isDefined(a[value.fieldName]) == true) ? a[value.fieldName] : 0) || 0;
                                    });
                                    var ySeriesData1 = chartData.map(function (a) {
                                        if (a.metrics1) {
                                            return parseFloat((angular.isDefined(a.metrics1[value.fieldName]) == true) ? a.metrics1[value.fieldName] : 0) || 0;

                                        } else {
                                            return 0;
                                        }
                                    });
                                    var ySeriesData2 = chartData.map(function (a) {
                                        if (a.metrics2) {
                                            return parseFloat((angular.isDefined(a.metrics2[value.fieldName]) == true) ? a.metrics2[value.fieldName] : 0) || 0;

                                        } else {
                                            return 0;
                                        }
                                    });
                                    if (isCompare) {
                                        var sumaryRange1 = response.summary.dateRange1.startDate + " - " + response.summary.dateRange1.endDate;
                                        var sumaryRange2 = response.summary.dateRange2.startDate + " - " + response.summary.dateRange2.endDate;
                                        var joinCompare1 = value.displayName + " (" + sumaryRange1 + ")";
                                        var joinCompare2 = value.displayName + " (" + sumaryRange2 + ")";
                                        if (joinCompare1) {
                                            var tempArray1 = {type: value.combinationType, name: joinCompare1, data: ySeriesData1, range: sumaryRange1};
                                            compareFormat.push({displayName: joinCompare1, displayFormat: value.displayFormat});
                                        }
                                        if (joinCompare2) {
                                            var tempArray2 = {type: value.combinationType, name: joinCompare2, data: ySeriesData2, range: sumaryRange2};
                                            compareFormat.push({displayName: joinCompare2, displayFormat: value.displayFormat});
                                        }
                                        columns.push(tempArray1);
                                        columns.push(tempArray2);


                                    } else {
                                        var tempArray1 = {type: value.combinationType, name: value.displayName, data: ySeriesData};
                                        columns.push(tempArray1);
                                    }
                                });
                                if (columns.length > 1) {
                                    showLegend = true;
                                } else {
                                    showLegend = false;
                                }

                                angular.forEach(combinationTypes, function (value, key) {
                                    chartCombinationtypes[[value.fieldName]] = value.combinationType;
                                });
                                var formattedData = [];
                                columns.forEach(function (dataGroup) {
                                    console.log("datagroup--------->", dataGroup);
                                    var formattedDataArray = [];
                                    dataGroup.data.forEach(function (dataPoint) {
                                        var formattedDataPoint = {
                                            y: dataPoint,
                                            marker: {
                                                symbol: 'circle',
                                                radius: 5,
                                                fillColor: 'white',
                                                enabled: true
                                            }
                                        };
                                        formattedDataArray.push(formattedDataPoint);
                                    });

                                    formattedData.push({
                                        type:dataGroup.type,
                                        name: dataGroup.name,
                                        data: formattedDataArray,
                                        stack: dataGroup.range,
                                        showInLegend: true,
                                        marker: {
                                            symbol: 'circle',
                                            fillColor: null
                                        }
                                    });
                                });
                            }
                            console.log("formatData 1------------>", formattedData);
                            var highChartData = {formatData: isCompare ? compareFormat : displayFormats,
                                renderTo: element[0],
                                columns: columns,
                                xData: xData,
                                showLegend: showLegend,
                                formattedData: formattedData,
                                isCompare: isCompare
                            };
                            console.log("widgetObj---->", widgetObj);
                            var chart = new Highcharts.Chart(chartFactory.selectChart(widgetObj.chartType, highChartData));
                        }
                    });
                };
                scope.setLineChartFn({lineFn: scope.refreshLineChart});
                scope.refreshLineChart();
            }
        }
    };
});
