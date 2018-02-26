app.directive('funnelDirective', function ($http, $stateParams, $filter) {
    return{
        restrict: 'AE',
        template: '<div ng-show="loadingFunnel" class="text-center"><img src="static/img/logos/loader.gif" width="40"></div>' +
                '<div ng-show="hideEmptyFunnel" class="text-center">{{funnelEmptyMessage}}</div>',
        scope: {
            setFunnelFn: '&',
            funnelSource: '@',
            funnelId: '@',
            funnelColumns: '@',
            funnelTitleName: '@',
            widgetObj: '@',
            defaultChartColor: '@'
        },
        link: function (scope, element, attr) {
            scope.loadingFunnel = true;
            var funnelName = [];
            if (!scope.funnelColumns) {
                return;
            }
            angular.forEach(JSON.parse(scope.funnelColumns), function (value, key) {
                if (!value) {
                    return;
                }
                funnelName.push({fieldName: value.fieldName, displayName: value.displayName, displayFormat: value.displayFormat});
            });
            var format = function (column, value) {
                if (!value) {
                    var temp = 0;
                    return temp;
                }
                if (column.displayFormat) {
                    if (isNaN(value)) {
                        var temp = 0;
                        return temp;
                    }
                    if (column.displayFormat.indexOf("%") > -1) {
                        return d3.format(column.displayFormat)(value / 100);
                    } else if (column.displayFormat === 'H:M:S') {
                        return formatBySecond(parseInt(value));
                    }
                    return d3.format(column.displayFormat)(value);
                }

                return value;
            };
            var setData = [];
            var data = [];
            var funnelDataSource = JSON.parse(scope.funnelSource);
            if (funnelDataSource) {
                var url = "admin/proxy/getData?";
//                if (funnelDataSource.dataSourceId.dataSourceType == "sql") {
//                    url = "admin/proxy/getJson?url=../dbApi/admin/dataSet/getData&";
//                }

                var dataSourcePassword;
                if (funnelDataSource.dataSourceId.password) {
                    dataSourcePassword = funnelDataSource.dataSourceId.password;
                } else {
                    dataSourcePassword = '';
                }
                var getWidgetObj = JSON.parse(scope.widgetObj);

                //var defaultColors = ['#59B7DE', '#D7EA2B', '#FF3300', '#E7A13D', '#3F7577', '#7BAE16'];
                var defaultColors = scope.defaultChartColor ? JSON.parse(scope.defaultChartColor) : "";
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

                scope.refreshFunnel = function () {
                    $http.get(url + 'connectionUrl=' + funnelDataSource.dataSourceId.connectionString +
                            "&dataSetId=" + funnelDataSource.id +
                            "&accountId=" + (getWidgetObj.accountId ? (getWidgetObj.accountId.id ? getWidgetObj.accountId.id : getWidgetObj.accountId) : $stateParams.accountId) +
                            "&userId=" + (funnelDataSource.userId ? funnelDataSource.userId.id : null) +
                            "&driver=" + funnelDataSource.dataSourceId.sqlDriver +
                            "&dataSetReportName=" + funnelDataSource.reportName +
                            "&startDate=" + $stateParams.startDate +
                            "&endDate=" + $stateParams.endDate +
                            "&productSegment=" + setProductSegment +
                            "&timeSegment=" + setTimeSegment +
                            "&networkType=" + setNetworkType +
                            '&username=' + funnelDataSource.dataSourceId.userName +
                            '&password=' + dataSourcePassword +
                            '&widgetId=' + scope.funnelId +
                            '&url=' + funnelDataSource.url +
                            '&port=3306&schema=vb&query=' + encodeURI(funnelDataSource.query)).success(function (response) {
                        scope.funnels = [];
                        scope.loadingFunnel = false;
                        if (!response) {
                            scope.scatterEmptyMessage = "No Data Found";
                            scope.hideEmptyScatter = true;
                            return;
                        }
                        if (response.data.length === 0) {
                            scope.funnelEmptyMessage = "No Data Found";
                            scope.hideEmptyFunnel = true;
                        } else {
                            if (!response) {
                                return;
                            }
                            angular.forEach(funnelName, function (value, key) {
                                var funnelData = response.data;
                                var loopCount = 0;
                                data = [value.fieldName];
                                setData = funnelData.map(function (a) {
                                    data.push(loopCount);
                                    loopCount++;
                                    return a[value.fieldName];
                                });
                                var total = 0;
                                for (var i = 0; i < setData.length; i++) {
                                    total += parseFloat(setData[i]);
                                }
                                scope.funnels.push([value.displayName, parseFloat((angular.isDefined(total) == true) ? total : 0) || 0]);
                            });
                        }
                        console.log("scope.funnels----------------->", scope.funnels);
//                        var data = scope.funnels;
//                        scope.funnelCharts = [];
//                        scope.funnelFiltered = $filter('orderBy')(scope.funnels, 'totalValue');
//                        scope.fName = [];
//                        scope.fValue = [];
//                        scope.dValue = [];
//                        angular.forEach(scope.funnels, function (value, key) {
//                            var funnelFieldName = value.funnelTitle;
//                            var funnelValue = value.totalValue;
//                            var dataValue = value.dataValue;
//                            scope.fName.push(funnelFieldName);
//                            scope.fValue.push(funnelValue);
//                            scope.dValue.push(dataValue);
//                        });
//                        var funnelData = filterFunnelByValue(scope.fName, scope.fValue, scope.dValue);
//                        scope.funnelCharts = funnelData;
//                        function filterFunnelByValue(name, value, dataValue) {
//                            var len = name.length;
//                            var temp, temp1 = 0, temp2 = 0;
//                            for (var i = 0; i < len; i++) {
//                                for (var j = i + 1; j < len; j++) {
//                                    if (dataValue[i] < dataValue[j]) {
//                                        temp = value[i];
//                                        value[i] = value[j];
//                                        value[j] = temp;
//                                        temp1 = name[i];
//                                        name[i] = name[j];
//                                        name[j] = temp1;
//                                        temp2 = dataValue[i];
//                                        dataValue[i] = dataValue[j];
//                                        dataValue[j] = temp2;
//                                    }
//                                }
//                            }
//                            return funnelArrayObjects(name, value);
//                        }
//                            console.log("funnelArrayObjects------------>",scope.funnelCharts);
//                        function funnelArrayObjects(name, value) {
//                            var funnelObject = [];
//                            var funnelColor = chartColors ? chartColors : defaultColors;
//                            var len = name.length;
//                            for (var i = 0; i < len; i++) {
//                                funnelObject.push([name[i], value[i], funnelColor[i]]);
//                            }
//                            return funnelObject;
//                        }

                        /*Filter*/
                        var chart = Highcharts.chart(
                                {
                                    chart: {
                                        renderTo:element[0],
                                        type: 'funnel'
                                    },
                                    title: {
                                        text: ''
                                    },
                                    plotOptions: {
                                        series: {
                                            dataLabels: {
                                                enabled: true,
                                                format: '<b>{point.name}</b> ({point.y:,.0f})',
                                                color: (Highcharts.theme && Highcharts.theme.contrastTextColor) || 'black',
                                                softConnector: true
                                            },
                                            center: ['40%', '50%'],
                                            neckWidth: '30%',
                                            neckHeight: '25%',
                                            width: '80%'
                                        }
                                    },
                                    legend: {
                                        enabled: false
                                    },
                                    series: [{
                                            name: '',
                                            data: scope.funnels
                                        }]
                                });
                    });
                };
                scope.setFunnelFn({funnelFn: scope.refreshFunnel});
                scope.refreshFunnel();
            }
        }
    };
});