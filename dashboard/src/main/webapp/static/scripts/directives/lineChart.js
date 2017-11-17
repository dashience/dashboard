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
            widgetObj: '@',
            defaultChartColor: '@',
            compareDateRange: '@',
            urlType: '@'
        },
        link: function (scope, element, attr) {
            scope.loadingLine = true;
            var lineChartDataSource = JSON.parse(scope.lineChartSource);
            var startDates1 = (moment().subtract(1, 'months').startOf('month'));
            var endDates1 = (moment().subtract(1, 'months').endOf('month'));
            var startDates2 = (moment().subtract(2, 'months').startOf('month'));
            var endDates2 = (moment().subtract(2, 'months').endOf('month'));
            var startDate1 = $filter('date')(new Date(startDates1), 'MM/dd/yyyy');
            var endDate1 = $filter('date')(new Date(endDates1), 'MM/dd/yyyy');
            var startDate2 = $filter('date')(new Date(startDates2), 'MM/dd/yyyy');
            var endDate2 = $filter('date')(new Date(endDates2), 'MM/dd/yyyy');
            var url;
            var colors = [];
            var urlPath;
            var getCompareStatus;
            var dateRangeType;
            var compareDateRangeDates = "&startDate1=" + startDate1 + "&endDate1=" + endDate1 + "&startDate2=" + startDate2 + "&endDate2=" + endDate2;
            var monthEndWithoutCompare = "&startDate=" + startDate1 + "&endDate=" + endDate1;
            var compareRange = JSON.parse(scope.compareDateRange);
            var isCompare = scope.urlType;
            var url;
            var type = {};
            var widgetType;
            var alignment;
            var backgroundColor;
            var stacked;
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
            }
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

            widgetType = getWidgetObj.chartType;
            if (widgetType == 'bar') {
                widgetType = 'column'
            }
            if (widgetType == 'horizontalBar') {
                widgetType = 'bar'
            }
            if (widgetType == 'stackedbar') {
                widgetType = 'column'
                stacked = {
                    enabled: true,
                    style: {
                        fontWeight: 'bold',
//                        color: (Highcharts.theme && Highcharts.theme.textColor) || 'gray'
                    }
                }
                type[widgetType] = {
                    stacking: 'normal'
                }
            }
            if (widgetType == 'combination') {
                widgetType = ''

            }
            if (widgetType == 'gauge') {
                widgetType = 'solidgauge'

            }
            var xData;
            var yData = [];
            var xAxis;
            var displayFormats = [];
            var yAxis = [];
            var sortFields = [];
            var ySeries = [];
            var compareFormat = [];
            var count = 0;

            var chartMaxRecord = JSON.parse(scope.widgetObj);
            var widgetColumns = JSON.parse(scope.widgetColumns);
            angular.forEach(widgetColumns, function (value, key) {
                if (value.xAxis == 1) {
                    xAxis = {fieldName: value.fieldName};
                }
                if (value.yAxis == 1) {

                    yAxis.push({fieldName: value.fieldName, displayName: value.displayName})
                }
                if (value.yAxis == 2) {
                    if (value.combinationType == 'bar') {
                        value.combinationType = 'column';
                    }

                    yAxis.push({
                        fieldName: value.fieldName,
                        displayName: value.displayName,
                        yAxis: 1,
                        displayFormat: value.displayFormat,
                        combinationType: value.combinationType})
                }

                if (value.sortOrder) {
                    sortFields.push({fieldName: value.fieldName, sortOrder: value.sortOrder, fieldType: value.fieldType});
                }
                displayFormats.push({displayName: value.displayName, displayFormat: value.displayFormat})


            })
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

                })
                return format;
            }
            $http.get(url + 'connectionUrl=' + lineChartDataSource.dataSourceId.connectionString +
                    "&dataSetId=" + lineChartDataSource.id +
                    "&accountId=" + (getWidgetObj.accountId ? (getWidgetObj.accountId.id ? getWidgetObj.accountId.id : getWidgetObj.accountId) : $stateParams.accountId) +
                    "&userId=" + (lineChartDataSource.userId ? lineChartDataSource.userId.id : null) +
                    "&driver=" + lineChartDataSource.dataSourceId.sqlDriver +
//                    "&startDate=" + $stateParams.startDate +
//                    "&endDate=" + $stateParams.endDate +
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

                var chartData = response.data;

                if (chartMaxRecord.maxRecord > 0) {
                    chartData = chartData.slice(0, chartMaxRecord.maxRecord);
                }
                var sortingObj;
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
                angular.forEach(yAxis, function (value, key) {
                    yData = chartData.map(function (a) {
                        return a[value.fieldName];
                    });
                    var yData1 = chartData.map(function (a) {
                        if (a.metrics1) {
                            return a.metrics1[value.fieldName] || "0";
                        } else {
                            return 0;
                        }
                    });
                    var yData2 = chartData.map(function (a) {
                        if (a.metrics1) {
                            return a.metrics1[value.fieldName] || "0";
                        } else {
                            return 0;
                        }
                    });
                    if (isCompare == 'compareOn') {
                        var sumaryRange1 = response.summary.dateRange1.startDate + " - " + response.summary.dateRange1.endDate;
                        var sumaryRange2 = response.summary.dateRange2.startDate + " - " + response.summary.dateRange2.endDate;
                        var joinCompare1 = value.displayName + " (" + sumaryRange1 + ")";
                        var joinCompare2 = value.displayName + " (" + sumaryRange2 + ")";
                        yData1.unshift(joinCompare1);
                        yData2.unshift(joinCompare2);
                        var y1ColumnName = yData1.splice(0, 1);
                        var data1 = [];
                        var data2 = [];
//                        var y1Values = yData1.map(Number);
                        var colors = ["#DC5C1F", "#E58C00", "#F0CC5A", "#71C7A7", "#009896", "#236570", "#9F4462", "#E15E64"]
                        for (var i = 0; i < yData1.length; i++) {
                            var j = count;
                            if (widgetType == "bar") {
                                console.log(j)
                                console.log("colors" + colors[j])
                            }
                            data1.push({y: parseFloat(yData1[i]), color: colors[j]})
                            count++;
                            console.log("for" + count);
                        }
                        count = count - 2;
                        console.log(count)
                        if (widgetType == "bar") {
                            alignment = 'right';
                            backgroundColor = '#000';
                        }
                        ySeries.push({name: y1ColumnName[0],
                            data: data1,
                            yAxis: value.yAxis,
                            type: value.combinationType,
                            dataLabels: {enabled: true,
                                align: alignment,
                                backgroundColor: backgroundColor,
                                formatter: function () {
                                    return scope.format(this)
                                }}});
                        compareFormat.push({displayName: joinCompare1, displayFormat: value.displayFormat});
                        var y2ColumnName = yData2.splice(0, 1);
//                        var y2Values = yData2.map(Number);
                        var colors1 = ["#E99871", "#EFB75E", "#F6DF97", "#ACDCC8", "#5EC0BD", "#749EA5", "#C3899C", "#EC999D"]
                        for (var i = 0; i < yData2.length; i++) {
                            var j = count;
                            if (widgetType == "bar") {
                                console.log(j)
                                console.log("colors1" + colors[j])
                            }
                            data2.push({y: parseFloat(yData2[i]), color: colors1[j]})
                            count++;
                        }
                        ySeries.push({name: y2ColumnName[0],
                            data: data2,
                            yAxis: value.yAxis,
                            type: value.combinationType,
                            dataLabels: {enabled: true,
                                align: alignment,
                                backgroundColor: backgroundColor,
                                formatter: function () {
                                    return scope.format(this)
                                }}});
                        compareFormat.push({displayName: joinCompare2, displayFormat: value.displayFormat});
                    } else {
                        yData.unshift(value.displayName);
                        var splicedValue = yData.splice(0, 1);
                        var arrayOfNumbers = yData.map(Number);

                        ySeries.push({name: splicedValue[0],
                            data: arrayOfNumbers,
                            yAxis: value.yAxis,
                            type: value.combinationType,
                            dataLabels: {enabled: true,
                                formatter: function () {
                                    return scope.format(this)
                                }}})
                    }

                })
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
                    }
                }

                if (xAxis) {
                    xData = chartData.map(function (a) {
                        return a[xAxis.fieldName];
                    });
                }
                var chart = new Highcharts.Chart({
                    chart: {
                        zoomType: 'xy',
                        renderTo: element[0],
                        type: widgetType
                    },
                    title: {
                        text: ''
                    },
                    tooltip: {
                        enabled: true,
                        formatter: function () {
                            return scope.format(this);
                        }
                    },
                    subtitle: {
                        text: ''
                    },
                    xAxis: {
                        categories: xData,
//                        crosshair: true
                        labels: {
                            style: {
                                fontSize: '13px',
                                fontFamily: 'Verdana, sans-serif'
                            },
                            align: 'left',
                            x: 5, // some padding
                            y: -50, // adjust to place over column
                            reserveSpace: false
                        },
                        tickLength: 0
                    },
                    yAxis: [{
                            lineWidth: 1,
                            gridLineWidth: 0,
                            min: 0,
                            title: false,
//                            title: {
//                                text: 'Primary Axis'
//                            },
                        }
                        , {
                            lineWidth: 1,
                            gridLineWidth: 0,
                            min: 0,
                            title: false,
//                            title: {
//                                text: 'Secondary Axis'
//                            },
                            opposite: true,
                        }],
                    stackLabels: stacked,
                    plotOptions: type,
                    exporting: {
                        buttons: {
                            contextButtons: {
                                enabled: false,
                                menuItems: null
                            }
                        },
                        enabled: false
                    },
                    credits:
                            {
                                enabled: false
                            },
                    colors: ["#DC5C1F", "#E58C00", "#F0CC5A", "#71C7A7", "#009896", "#236570", "#9F4462", "E15E64"],
//                    ],
                    series: ySeries


                });
            });
        }
    }
})