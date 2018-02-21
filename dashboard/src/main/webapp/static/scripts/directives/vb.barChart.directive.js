app.directive('barChartDirective', function ($http, $stateParams, $filter, orderByFilter) {
    return{
        restrict: 'A',
        template: '<div ng-show="loadingBar" class="text-center"><img src="static/img/logos/loader.gif" width="40"></div>' +
                '<div ng-show="hideEmptyBar" class="text-center">{{barEmptyMessage}}</div>',
        scope: {
            setBarChartFn: '&',
            barChartSource: '@',
            widgetId: '@',
            barChartId: '@',
            widgetColumns: '@',
            widgetObj: '@',
            defaultChartColor: '@',
            isHorizontalBar: '@',
            compareDateRange: '@',
            urlType: '@'
        },
        link: function (scope, element, attr) {
            var labels = {format: {}};
            scope.loadingBar = true;
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
            var graphMargin = 40;
            var graphHeight = 400;
            var barThickness = 25;
            var textColor = '#989797';
            var dataLabelFontSize = 10;
            var axisLabelFontSize = 12;
            var displayFormats = [];
            var compareFormat = [];
            if (!scope.widgetColumns) {
                return;
            }
            var colorsHeirarchy = {
                1: ['#009b96'],
                2: ['#71c7a7', '#e58c00'],
                3: ['#dc5c1f', '#f0cc5a', '#009b96'],
                4: ['#dc5c1f', '#71c7a7', '#f0cc5a', '#236570'],
                5: ['#e58c00', '#71c7a7', '#f0cc5a', '#156572', '#e15e64'],
                6: ['#dc5c1f', '#f0cc5a', '#009b96', '#e58c00', '#236570', '#e15e64'],
                7: ['#dc5c1f', '#f0cc5a', '#71c7a7', '#e58c00', '#009b96', '#e15e64', '#9f4462'],
                8: ['#dc5c1f', '#f0cc5a', '#71c7a7', '#e58c00', '#009b96', '#e15e64', '#236570', '#9f4462']
            };

            var colorsHeirarchyShades = {
                1: ['#088478'],
                2: ['#62b592', '#d37607'],
                3: ['#c44412', '#e0ad3d', '#088478'],
                4: ['#c44412', '#62b592', '#e0ad3d', '#10545b'],
                5: ['#d37607', '#62b592', '#e0ad3d', '#10545b', '#c4515f'],
                6: ['#c44412', '#e0ad3d', '#088478', '#d37607', '#10545b', '#c4515f'],
                7: ['#c44412', '#e0ad3d', '#62b592', '#d37607', '#088478', '#c4515f', '#822e4e'],
                8: ['#c44412', '#e0ad3d', '#62b592', '#d37607', '#088478', '#c4515f', '#10545b', '#822e4e']
            };
            var showLegend;
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
                    console.log(value.fieldName + " --- " + value.combinationType);
                    combinationTypes.push({fieldName: value.fieldName, combinationType: value.combinationType});
                }
                displayFormats.push({displayName: value.displayName, displayFormat: value.displayFormat});
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
//                alert(maxValue.maxRecord)
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
            var barChartDataSource = JSON.parse(scope.barChartSource);
            if (scope.barChartSource) {

                var getWidgetObj = JSON.parse(scope.widgetObj);
//                var url = "admin/proxy/getData?";
//                if (barChartDataSource.dataSourceId.dataSourceType == "sql") {
//                    url = "admin/proxy/getJson?url=../dbApi/admin/dataSet/getData&";
//                }
                var dataSourcePassword;
                if (barChartDataSource.dataSourceId.password) {
                    dataSourcePassword = barChartDataSource.dataSourceId.password;
                } else {
                    dataSourcePassword = '';
                }
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
                scope.format = function (val) {
                    var format;
                    angular.forEach(displayFormats, function (value, key) {
                        var displayName = value.displayName;
                        var dispFormat = value.displayFormat;
                        if (val.series.name === displayName) {
                            if (value.displayFormat) {
                                if (value.displayFormat && value.displayFormat != 'H:M:S') {
                                    if (dispFormat.indexOf("%") > -1) {
                                        format = d3.format(dispFormat)(val.y / 100);
                                    } else {
                                        format = d3.format(dispFormat)(val.y);
                                    }
                                } else {
                                    format = formatBySecond(parseInt(val.y))
                                }
//                                   
                            } else {
                                format = val.y;
                            }

                        }

                    });
                    return format;
                };

                scope.refreshBarChart = function () {
                    $http.get(url + 'connectionUrl=' + barChartDataSource.dataSourceId.connectionString +
                            "&dataSetId=" + barChartDataSource.id +
                            "&accountId=" + (getWidgetObj.accountId ? (getWidgetObj.accountId.id ? getWidgetObj.accountId.id : getWidgetObj.accountId) : $stateParams.accountId) +
                            "&userId=" + (barChartDataSource.userId ? barChartDataSource.userId.id : null) +
                            "&dataSetReportName=" + barChartDataSource.reportName +
                            "&driver=" + barChartDataSource.dataSourceId.sqlDriver +
                            "&location=" + $stateParams.locationId +
//                            "&startDate=" + $stateParams.startDate +
//                            "&endDate=" + $stateParams.endDate +
                            dateRangeType +
                            "&productSegment=" + setProductSegment +
                            "&timeSegment=" + setTimeSegment +
                            "&networkType=" + setNetworkType +
                            '&username=' + barChartDataSource.dataSourceId.userName +
                            '&password=' + dataSourcePassword +
                            '&widgetId=' + scope.widgetId +
                            '&url=' + barChartDataSource.url +
                            '&port=3306&schema=vb&query=' + encodeURI(barChartDataSource.query)).success(function (response) {
                        scope.loadingBar = false;
                        if (!response) {
                            scope.barEmptyMessage = "No Data Found";
                            scope.hideEmptyBar = true;
                            return;
                        }
                        if (response.data.length === 0) {
                            scope.barEmptyMessage = "No Data Found";
                            scope.hideEmptyBar = true;
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
//                            xTicks = [xAxis.fieldName];
//                            xData = chartData.map(function (a) {
//                                xTicks.push(loopCount);
//                                loopCount++;
//                                return a[xAxis.fieldName];
//                            });
                            xData = chartData.map(function (a) {
                                return a[xAxis.fieldName];
                            });
                            console.log("xaxis-------------->",xAxis);
//                            columns.push(xTicks);
                            angular.forEach(yAxis, function (value, key) {
                                var ySeriesData = chartData.map(function (a) {
//                                    return a[value.fieldName] || "0";
                                    return parseFloat((angular.isDefined(a[value.fieldName]) == true) ? a[value.fieldName] : 0) || 0;
                                });
                                var ySeriesData1 = chartData.map(function (a) {
                                    if (a.metrics1) {
//                                        return a.metrics1[value.fieldName] || "0";
                                        return parseFloat((angular.isDefined(a[value.fieldName]) == true) ? a[value.fieldName] : 0) || 0;

                                    } else {
                                        return 0;
                                    }
                                });
                                var ySeriesData2 = chartData.map(function (a) {
                                    if (a.metrics2) {
//                                        return a.metrics2[value.fieldName] || "0";
                                        return parseFloat((angular.isDefined(a.metrics2[value.fieldName]) == true) ? a.metrics2[value.fieldName] : 0) || 0;

                                    } else {
                                        return 0;
                                    }
                                });
                                if (isCompare == 'compareOn') {
                                    var sumaryRange1 = response.summary.dateRange1.startDate + " - " + response.summary.dateRange1.endDate;
                                    var sumaryRange2 = response.summary.dateRange2.startDate + " - " + response.summary.dateRange2.endDate;
                                    var joinCompare1 = value.displayName + " (" + sumaryRange1 + ")";
                                    var joinCompare2 = value.displayName + " (" + sumaryRange2 + ")";
//                                    ySeriesData1.unshift(joinCompare1);
//                                    ySeriesData2.unshift(joinCompare2);
//                                    columns.push(ySeriesData1);
//                                    columns.push(ySeriesData2);
                                    if (joinCompare1) {
                                        var tempArray1 = {"name": joinCompare1, "data": ySeriesData1};
                                        compareFormat.push({displayName: joinCompare1, displayFormat: value.displayFormat});
                                    }
                                    if (joinCompare2) {
                                        var tempArray2 = {"name": joinCompare2, "data": ySeriesData2};
                                        compareFormat.push({displayName: joinCompare2, displayFormat: value.displayFormat});
                                    }
                                    columns.push(tempArray1);
                                    columns.push(tempArray2);
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
//                                    ySeriesData.unshift(value.displayName);
                                    var tempArray1 = {"name": value.displayName, "data": ySeriesData};
                                    columns.push(tempArray1);
                                }
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
                            var gridLine = false;
                            if (gridData.isGridLine == 'Yes') {
                                gridLine = true;
                            } else {
                                gridLine = false;
                            }
                            try {
                                var isRotate = scope.isHorizontalBar == undefined ? false : true;

                            } catch (exception) {
                                console.log("Exception-------->",exception);
                            }
                            
                            if (isCompare == "compareOn") {
                                scope.format = function (val) {
                                    var format;
                                    angular.forEach(compareFormat, function (value, key) {
                                        var dispFormat = value.displayFormat;
                                        if (val.series.name === value.displayName) {
                                            if (value.displayFormat) {
                                                if (value.displayFormat && value.displayFormat != 'H:M:S') {
                                                    if (dispFormat.indexOf("%") > -1) {
                                                        format = d3.format(dispFormat)(val.y / 100);

                                                    } else {
                                                        format = d3.format(dispFormat)(val.y);
                                                    }

                                                } else {
                                                    format = formatBySecond(parseInt(val.y))
                                                }
//                                   
                                            } else {
                                                format = val.y;
                                            }

                                        }
                                    });
                                    return format;

                                };
                            }
                            if (columns.length > 1) {
                                showLegend = true;
                            } else {
                                showLegend = false;
                            }
                            var labelOffset = (columns.length / 2) + .3;
                            var len = columns[0].data.length;
                            if (len > Object.keys(colorsHeirarchy).length) {
                                len = Object.keys(colorsHeirarchy).length;
                            }
                            var formattedData = [];
                            columns.forEach(function (dataGroup, groupIndex) {
                                var index = groupIndex;
                                var formattedDataArray = [];
                                for (i = 0; i < dataGroup.data.length; i++) {
                                    if (columns.length > 1) {
                                        len = columns.length;
                                    } else {
                                        index = i
                                    }
                                    if (index >= Object.keys(colorsHeirarchy).length) {
                                        index = index % 8;
                                    }
                                    var formattedDataPoint = {
                                        y: dataGroup.data[i],
                                        color: colorsHeirarchy[len][index],
                                        dataLabels: {backgroundColor: colorsHeirarchyShades[len][index]}
                                    };
                                    formattedDataArray.push(formattedDataPoint);
                                }
//
                                formattedData.push({
                                    name: dataGroup.name,
                                    data: formattedDataArray
                                });
                            });

                            // SETUP FOR GRAY BACKGROUND BEHIND BARS
                            var plotLines = [];
                            xData.forEach(function (category, index) {
                                var newPlotLine = {
                                    width: barThickness * columns.length,
                                    color: '#f4f4f4',
                                    value: index
                                };
                                plotLines.push(newPlotLine);
                            });
                            
                            var chart = new Highcharts.Chart({
                                chart: {
                                    renderTo: element[0],
                                    type: isRotate ? 'bar': 'column'
                                },
                                title: {
                                    text: ''
                                },
                                xAxis: {
                                    categories: xData,
                                    title: {
                                        text: null
                                    }
                                },
                                yAxis: {
                                    min: 0,
                                    title: {
                                        text: '',
                                        align: 'high'
                                    },
                                    labels: {
                                        overflow: 'justify'
                                    }
                                },
                                tooltip: {
                                    valueSuffix: ' millions',
                                    useHTML: true,
                                    formatter: function () {
                                        var symbol = '●';
                                        return this.x + '<br/>' + '<span style="color:' + this.series.color + '">' + symbol + '</span>' + ' ' + this.series.name + ': ' + scope.format(this)

                                    },
                                    style: {
                                        zIndex: 100
                                    }
                                },
                                plotOptions: {
                                    bar: {
                                        dataLabels: {
                                            enabled: true,
                                            formatter: function () {
                                                return scope.format(this)
                                            }
                                        }
                                    }
                                },
                                legend: {
                                    layout: 'horizontal',
                                    align: 'center',
                                    backgroundColor: ((Highcharts.theme && Highcharts.theme.legendBackgroundColor) || '#FFFFFF'),
                                    shadow: true
                                },
                                credits: {
                                    enabled: false
                                },
                                series: formattedData
                            });
                        }
                    });
                };
                scope.setBarChartFn({barFn: scope.refreshBarChart});
                scope.refreshBarChart();
            }
        }
    };
});