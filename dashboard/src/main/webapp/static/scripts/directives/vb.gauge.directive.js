app.directive('gaugeDirective', function ($http, $stateParams) {
    return{
        restrict: 'AE',
        template: '<div ng-show="loadingGauge" class="text-center" style="color: #228995;"><img src="static/img/logos/loader.gif"></div>' +
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
            gaugeChartSource: '@',
            gaugeChartId: '@',
            widgetColumns: '@',
            loadingGauge: '&',
            widgetObj: '@',
            defaultChartColor: '@'
        },
        link: function (scope, element, attr) {

//            console.log(scope.setGaugeChartFn);
//            console.log(scope.getSelectedFilterItem);
//            console.log(scope.gaugeChartSource);
//            console.log(scope.widgetColumns);
//            console.log(scope.widgetObj);
//            var gaugeName;


            scope.loadingGauge = true;

//            console.log(scope.widgetColumns);
            var gaugeColumnsObj = JSON.parse(scope.widgetColumns);
            var fieldName = gaugeColumnsObj[0].fieldName;
            console.log(fieldName);



            var setData = [];
            var data = [];


            var gaugeChartDataSource = JSON.parse(scope.gaugeChartSource);
            if (scope.gaugeChartSource) {
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
                    dashboardFilter = ""
                }



//             scope.refreshGaugeChart = function () {
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
                    console.log(gaugeDataArray);

                    var gaugeDataObj = gaugeDataArray[0];
                    console.log(gaugeDataObj);

                    var arrayGaugeData = [];

                    //array of array
                    arrayGaugeData.push(fieldName, gaugeDataObj[fieldName]);

                    console.log(gaugeDataObj[fieldName]);

                    console.log(arrayGaugeData);


//                    var groups = gaugeData;
//                    console.log(groups)

                    var chart = c3.generate({
                        bindto: element[0],
//                        groups: groups,
                        data: {
                            columns: [arrayGaugeData],
                            type: 'gauge',
//                            groups: [groups],
                        },
                        color: {
                            pattern: chartColors ? chartColors : defaultColors
                        },
                    });


                });


            }



        }
    };
});
