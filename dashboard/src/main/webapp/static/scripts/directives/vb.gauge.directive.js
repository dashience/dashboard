app.directive('gaugeDirective', function ($http, $stateParams) {
    return{
        restrict: 'AE',
        template: '<div ng-show="loadingGauge" class="text-center" style="color: #228995;"><img src="static/img/logos/loader.gif" width="40"></div>' +
                '<div ng-hide="loadingGauge" class="panel panel-default relative pnl-aln">' +
                '<div class="m-b-10" ng-hide="hideEmptyGauge">' +
                '<span>{{gaugeTitle}}</span><br>' +
                '<span class="text-lg gauges">{{totalValue}}</span>' +
                '</div>' +
                '<div ng-show="hideEmptyGauge">{{gaugeEmptyMessage}}</div>' +
                '</div>',
        scope: {
            setGaugeChartFn: '&',
            getSelectedFilterItem: '&',
            gaugeSource: '@',
            gaugeChartId: '@',
            gaugeColumns: '@',
            loadingGauge: '&',
            widgetObj: '@',
            defaultChartColor: '@'
        },
        link: function (scope, element, attr) {
            scope.loadingGauge = true;

            if (!scope.gaugeColumns) {
                return;
            }
            var gaugeColumnsObj = JSON.parse(scope.gaugeColumns);
            var fieldName = gaugeColumnsObj[0].fieldName;



            var setData = [];
            var data = [];


            var gaugeChartDataSource = JSON.parse(scope.gaugeSource);
            if (scope.gaugeSource) {
                var url = "admin/proxy/getData?";

                var dataSourcePassword;
                if (gaugeChartDataSource.dataSourceId.password) {
                    dataSourcePassword = gaugeChartDataSource.dataSourceId.password;
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
                var dashboardFilter;
                if (getWidgetObj.filterUrlParameter) {
                    dashboardFilter = JSON.stringify(getWidgetObj.filterUrlParameter)
                } else {
                    dashboardFilter = "";
                }

                scope.refreshGaugeChart = function () {
                    $http.get(url + 'connectionUrl=' + gaugeChartDataSource.dataSourceId.connectionString +
                            "&dataSetId=" + gaugeChartDataSource.id +
                            "&accountId=" + (getWidgetObj.accountId ? (getWidgetObj.accountId.id ? getWidgetObj.accountId.id : getWidgetObj.accountId) : $stateParams.accountId) +
                            "&userId=" + (gaugeChartDataSource.userId ? gaugeChartDataSource.userId.id : null) +
                            "&driver=" + gaugeChartDataSource.dataSourceId.sqlDriver +
                            "&productSegment=" + setProductSegment +
                            "&timeSegment=" + setTimeSegment +
                            "&networkType=" + setNetworkType +
                            "&dashboardFilter=" + encodeURI(dashboardFilter) +
                            "&startDate=" + $stateParams.startDate +
                            "&endDate=" + $stateParams.endDate +
                            '&username=' + gaugeChartDataSource.dataSourceId.userName +
                            "&dataSetReportName=" + gaugeChartDataSource.reportName +
                            '&password=' + dataSourcePassword +
                            '&widgetId=' + scope.widgetId +
                            '&url=' + gaugeChartDataSource.url +
                            '&port=3306&schema=vb&query=' + encodeURI(gaugeChartDataSource.query)).success(function (response) {

                        scope.loadingGauge = false;


                        //array of object
                        var gaugeDataArray = response.data;

                        var gaugeDataObj = gaugeDataArray[0];

                        var arrayGaugeData = [];

                        //array of array
                        arrayGaugeData.push({name:fieldName, data:[parseFloat((angular.isDefined(gaugeDataObj[fieldName]) == true) ? gaugeDataObj[fieldName] : 0) || 0]});

                        console.log("arrayGaugeData--------->",arrayGaugeData);
                        console.log("gaugeDataObj--------->",gaugeDataObj);


//                    var groups = gaugeData;




// The speed gauge
                        var chart = Highcharts.chart(
                                {
                                    chart: {
                                        renderTo:element[0],
                                        type: 'solidgauge',
                                        backgroundColor: '#fff'
                                    },

                                    title: null,

                                    pane: {
                                        size: '100%',
                                        startAngle: -90,
                                        endAngle: 90,
                                        background: {
                                            backgroundColor: '#FFF',
                                            innerRadius: '90%',
                                            outerRadius: '105%',
                                            shape: 'arc',
                                            borderColor: '#fff'
                                        }
                                    },

                                    tooltip: {
                                        enabled: true
                                    },

                                    // the value axis
                                    yAxis: {
                                        min: 0,
                                        max: 100,
                                        stops: [
                                            [0.1, '#e74c3c'], // red
                                            [0.5, '#f1c40f'], // yellow
                                            [0.9, '#2ecc71'] // green
                                        ],
                                        minorTickInterval: null,
                                        tickPixelInterval: 400,
                                        tickWidth: 0,
                                        gridLineWidth: 0,
                                        gridLineColor: 'transparent',
                                        labels: {
                                            enabled: true
                                        }
                                    },

                                    series: arrayGaugeData
                                });
                    });

                }
                scope.setGaugeChartFn({scatterFn: scope.refreshGaugeChart});
                scope.refreshGaugeChart();
            }



        }
    };
});
