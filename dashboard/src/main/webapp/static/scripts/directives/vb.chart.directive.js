app.directive('chartDirective', function ($http, $filter, $stateParams, orderByFilter, chartFactory, httpService) {
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
            var sortFields = [];
            var compareFormat = [];
            var displayFormats = [];
            var chartUrl, httpUrl, requestUrl;
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
                    var combinationType = value.combinationType == 'bar' ? 'column' : value.combinationType;
                    yAxis.push({fieldName: value.fieldName, displayName: value.displayName, displayFormat: value.displayFormat, combinationType: combinationType});
                }
                if (value.sortOrder) {
                    sortFields.push({fieldName: value.fieldName, sortOrder: value.sortOrder, fieldType: value.fieldType});
                }
                displayFormats.push({fieldName: value.fieldName, displayName: value.displayName, displayFormat: value.displayFormat});
            });
            console.log("combination type---------->", yAxis);
            var xData = [];
            var isCompare = scope.urlType == 'compareOn' && widgetObj.chartType != 'pie' && widgetObj.chartType != 'funnel' && widgetObj.chartType != 'gauge' ? true : false;
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
                scope.refreshLineChart = function () {
                    chartUrl = 'connectionUrl=' + lineChartDataSource.dataSourceId.connectionString +
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
                            '&port=3306&schema=vb&query=' + encodeURI(lineChartDataSource.query);
                    httpUrl = url + chartUrl;
                    requestUrl = httpService.httpProcess('GET', httpUrl);
//                    $http.get(url + ).success(function (response) {
                    requestUrl.then(function (response) {

                        console.log("response------->", response);
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
                            var chartData = response.data;
                            chartData = chartFactory.sortData(sortFields, chartData, widgetObj.maxRecord);
                            if (xAxis) {
                                xData = chartData.map(function (a) {
                                    return a[xAxis.fieldName];
                                });
                            }
                            var formattedDataTest = [];
                            var formattedDataVal = {};
                            var formattedData = [];
                            var scatter = false;
                            if (widgetObj.chartType == 'scatter') {
                                scatter = true;
                            }
                            if (widgetObj.chartType == 'pie') {
                                angular.forEach(chartData, function (value) {
                                    var arrayData = parseFloat((angular.isDefined(value[yAxis[0].fieldName]) == true) ? value[yAxis[0].fieldName] : 0) || 0;
                                    var tempArray = {"name": value[xAxis.fieldName], "y": arrayData};
                                    console.log("tempArray-------------------->", tempArray);
                                    formattedDataTest.push(tempArray);
                                });
                                formattedDataVal = {name: yAxis[0].fieldName, data: formattedDataTest};
                                formattedData.push(formattedDataVal);
                            } else if (widgetObj.chartType == 'gauge') {
                                var fieldName = widgetObj.columns[0].fieldName;
                                var displayName = widgetObj.columns[0].displayName;
                                var gaugeDataArray = response.data;
                                var gaugeDataObj = gaugeDataArray[0];
                                formattedData.push({name: displayName, data: [parseFloat((angular.isDefined(gaugeDataObj[fieldName]) == true) ? gaugeDataObj[fieldName] : 0) || 0]});
                            } else if (widgetObj.chartType == 'funnel') {
                                angular.forEach(displayFormats, function (value, key) {
                                    var funnelData = response.data;
                                    formattedDataTest = funnelData.map(function (a) {
                                        return a[value.fieldName];
                                    });
                                    var total = 0;
                                    for (var i = 0; i < formattedDataTest.length; i++) {
                                        total += parseFloat(formattedDataTest[i]);
                                    }
                                    if (value.displayFormat == ',.2%') {
                                        total = total / formattedDataTest.length;
                                    }
                                    formattedData.push([value.displayName, parseFloat((angular.isDefined(total) == true) ? total : 0) || 0]);
                                });
                            } else {
                                angular.forEach(yAxis, function (value, key) {
                                    var ySeriesData = chartData.map(function (a) {
                                        if (scatter) {
                                            return [parseFloat((angular.isDefined(a[value.fieldName]) == true) ? a[value.fieldName] : 0) || 0];
                                        }
                                        return parseFloat((angular.isDefined(a[value.fieldName]) == true) ? a[value.fieldName] : 0) || 0;
                                    });
                                    var ySeriesData1 = chartData.map(function (a) {
                                        if (a.metrics1) {
                                            if (scatter) {
                                                return [parseFloat((angular.isDefined(a.metrics1[value.fieldName]) == true) ? a.metrics1[value.fieldName] : 0) || 0];
                                            }
                                            return parseFloat((angular.isDefined(a.metrics1[value.fieldName]) == true) ? a.metrics1[value.fieldName] : 0) || 0;

                                        } else {
                                            return 0;
                                        }
                                    });
                                    var ySeriesData2 = chartData.map(function (a) {
                                        if (a.metrics2) {
                                            if (scatter) {
                                                return [parseFloat((angular.isDefined(a.metrics2[value.fieldName]) == true) ? a.metrics2[value.fieldName] : 0) || 0];
                                            }
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
                                        type: dataGroup.type,
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
                            showLegend = true;
                            console.log("formatData 1------------>", formattedData);
                            var highChartData = {formatData: isCompare ? compareFormat : displayFormats,
                                renderTo: element[0],
                                columns: columns,
                                xData: xData,
                                showLegend: showLegend,
                                formattedData: formattedData,
                                isCompare: isCompare
                            };
                            console.log("columns", columns);
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
