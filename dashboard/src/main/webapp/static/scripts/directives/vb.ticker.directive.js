app.directive('tickerDirective', function ($http, $stateParams, $filter) {
    return{
        restrict: 'AE',
        template:
                '<div ng-show="loadingTicker"><img width="40" src="static/img/logos/loader.gif"></div>' +
                '<div  ng-hide="loadingTicker" >' +
                '<div ng-hide="hideEmptyTicker" class="hpanel stats">' +
                '<div class="panel-body h-150">' +
                '<div class="stats-title pull-left">' +
                '<h4>{{tickerTitleName}}</h4>' +
                '</div>' +
                '<div class="stats-icon pull-right">' +
//                        '<i class="pe-7s-share fa-4x"></i>' +
                '</div>' +
                //First Level
                '<div class="m-t-xl">' +
                '<h3 class="m-b-xs text-success" style="color:{{colorName}};">{{firstLevelTicker.totalValue?format(formatColumn, firstLevelTicker.totalValue):format(formatColumn, firstLevelTickerValue)}}' +
                //Percentage
                '<span ng-click="changeComparisonType(\'firstLevel\')" ' +
                'style="cursor:pointer" ng-if="percent(formatColumn, firstLevelTickerValue,firstLevelTickerValue1) && showDifference != true" ' +
                'ng-class="{\'arrow-up\':(percent(formatColumn, firstLevelTickerValue,firstLevelTickerValue1) > 0), \'arrow-down\':(percent(formatColumn, firstLevelTickerValue,firstLevelTickerValue1) < 0)}" ng-hide="hideEmptyTickerSecondLevel">' +
                '<i class="fa" ng-class="{\'fa-arrow-up\':percent(formatColumn, firstLevelTickerValue,firstLevelTickerValue1) > 0, \'fa-arrow-down\':(percent(formatColumn, firstLevelTickerValue,firstLevelTickerValue1) < 0)}"></i>' +
                '<span ng-class="{\'leads-up\':percent(formatColumn, firstLevelTickerValue,firstLevelTickerValue1) > 0, \'leads-down\':(percent(formatColumn, firstLevelTickerValue,firstLevelTickerValue1) < 0)}" ng-hide="showDifference">' +
                '{{percent(formatColumn, firstLevelTickerValue,firstLevelTickerValue1)}}' + "%" +
                '</span>' +
                '</span>' +
                '<span class="empty-ticker arrow-up" ng-click="changeComparisonType(\'firstLevel\')" ng-hide="hideEmptyTickerSecondLevel" style="cursor:pointer" ng-if="!(percent(formatColumn, firstLevelTickerValue,firstLevelTickerValue1)) && showDifference == true">0 </span>' +
                //Diff
                '<span ng-click="changeComparisonType(\'firstLevel\')" ' +
                'style="cursor:pointer" ng-if="sub(formatColumn, firstLevelTickerValue,firstLevelTickerValue1) != 0 && showDifference == true && firstLevelTickerValue != 0 && firstLevelTickerValue1 != 0"' + 'ng-class="{\'arrow-up\':(sub(formatColumn, firstLevelTickerValue,firstLevelTickerValue1) > 0), \'arrow-down\':(sub(formatColumn, firstLevelTickerValue,firstLevelTickerValue1) < 0)}" ng-hide="hideEmptyTickerSecondLevel">' +
                ' <i class="fa" ng-class="{\'fa-arrow-up\':sub(formatColumn, firstLevelTickerValue,firstLevelTickerValue1) > 0, \'fa-arrow-down\':(sub(formatColumn, firstLevelTickerValue,firstLevelTickerValue1) < 0)}"></i>' +
                '<span class="leads" ng-show="showDifference">' + '{{differ(formatColumn, firstLevelTickerValue,firstLevelTickerValue1)}}' +
                '<span class="leads-name" ng-if="sub(formatColumn, firstLevelTickerValue,firstLevelTickerValue1)"> {{secondFormatName}}</span>' +
                '</span>' +
                '</span>' +
                '<div class="icon pull-right" ng-hide="hideEmptyTicker"><i class="{{selectedChartIcon}}" aria-hidden="true"></i></div>' +
                '</h3>' +
//                '<span class="font-bold no-margins">' +
//                            '{{firstLevelTicker.tickerTitle}}' +
//                '</span>' +
                //Second Level
                '<div class="row">' +
                '<div class="col-xs-6">' +
                '<small class="stats-label">{{secondLevelTicker.tickerTitle?secondLevelTicker.tickerTitle:secondLevelTickerTitle}}</small>' +
                '<h4>{{secondLevelTicker.totalValue?format(formatColumnSecond, secondLevelTicker.totalValue):format(formatColumnSecond, secondLevelTickerValue)}}' +
                //Percentage
                '<span ng-click="changeComparisonType(\'secondLevel\')" ' +
                'style="cursor:pointer" ng-if="percent(formatColumnSecond, secondLevelTickerValue,secondLevelTickerValue1) && showSecondDifference != true" ' +
                'ng-class="{\'arrow-up\':(percent(formatColumnSecond, secondLevelTickerValue,secondLevelTickerValue1) > 0), \'arrow-down\':(percent(formatColumnSecond, secondLevelTickerValue,secondLevelTickerValue1) < 0)}" ng-hide="hideEmptyTickerSecondLevel">' +
                '<i class="fa" ng-class="{\'fa-arrow-up\':percent(formatColumnSecond, secondLevelTickerValue,secondLevelTickerValue1) > 0, \'fa-arrow-down\':(percent(formatColumnSecond, secondLevelTickerValue,secondLevelTickerValue1) < 0)}"></i>' +
                '<span ng-class="{\'leads-up\':percent(formatColumnSecond, secondLevelTickerValue,secondLevelTickerValue1) > 0, \'leads-down\':(percent(formatColumnSecond, secondLevelTickerValue,secondLevelTickerValue1) < 0)}" ng-hide="showSecondDifference">' +
                '{{percent(formatColumnSecond, secondLevelTickerValue,secondLevelTickerValue1)}}' + "%" +
                '</span>' +
                '</span>' +
//                '<span class="empty-ticker arrow-up" ng-click="changeComparisonType(\'secondLevel\')" ng-hide="hideEmptyTickerSecondLevel" style="cursor:pointer" ng-if="!(percent(formatColumn, firstLevelTicker,secondLevelTicker)) && showSecondDifference == true">0 </span>' +



                //Diff
                '<span ng-click="changeComparisonType(\'secondLevel\')" ' +
                'style="cursor:pointer" ng-if="sub(formatColumnSecond, secondLevelTickerValue,secondLevelTickerValue1) != 0 && showSecondDifference == true && firstLevelTicker != 0 && secondLevelTicker != 0"' + 'ng-class="{\'arrow-up\':(sub(formatColumnSecond, secondLevelTickerValue,secondLevelTickerValue1) > 0), \'arrow-down\':(sub(formatColumnSecond, secondLevelTickerValue,secondLevelTickerValue1) < 0)}" ng-hide="hideEmptyTickerSecondLevel">' +
                ' <i class="fa" ng-class="{\'fa-arrow-up\':sub(formatColumnSecond, secondLevelTickerValue,secondLevelTickerValue1) > 0, \'fa-arrow-down\':(sub(formatColumnSecond, secondLevelTickerValue,secondLevelTickerValue1) < 0)}"></i>' +
                '<span class="leads" ng-show="showSecondDifference">' + '{{differ(formatColumnSecond, secondLevelTickerValue,secondLevelTickerValue1)}}' +
                '<span class="leads-name" ng-if="sub(formatColumnSecond, secondLevelTickerValue,secondLevelTickerValue1)"> {{secondFormatNameSecond}}</span>' +
                '</span>' +
                '</span>' +
                '</h4>' +
                '</div>' +
                //Third Level
                '<div class="col-xs-6 count">' +
                '<small class="stats-label">{{thirdLevelTicker.tickerTitle?thirdLevelTicker.tickerTitle:thirdLevelTickerTitle}}</small>' +
                '<h4>{{thirdLevelTicker.totalValue?format(formatColumnThird, thirdLevelTicker.totalValue):format(formatColumnThird, thirdLevelTickerValue)}}' +
                //Percentage
                '<span ng-click="changeComparisonType(\'thirdLevel\')" ' +
                'style="cursor:pointer" ng-if="percent(formatColumnThird, thirdLevelTickerValue,thirdLevelTickerValue1) && showThirdDifference != true" ' +
                'ng-class="{\'arrow-up\':(percent(formatColumnThird, thirdLevelTickerValue,thirdLevelTickerValue1) > 0), \'arrow-down\':(percent(formatColumnThird, thirdLevelTickerValue,thirdLevelTickerValue1) < 0)}" ng-hide="hideEmptyTickerSecondLevel">' +
                '<i class="fa" ng-class="{\'fa-arrow-up\':percent(formatColumn, thirdLevelTickerValue,thirdLevelTickerValue1) > 0, \'fa-arrow-down\':(percent(formatColumnThird, thirdLevelTickerValue,thirdLevelTickerValue1) < 0)}"></i>' +
                '<span ng-class="{\'leads-up\':percent(formatColumnThird, thirdLevelTickerValue,thirdLevelTickerValue1) > 0, \'leads-down\':(percent(formatColumnThird, thirdLevelTickerValue,thirdLevelTickerValue1) < 0)}" ng-hide="showThirdDifference">' +
                '{{percent(formatColumnThird, thirdLevelTickerValue,thirdLevelTickerValue1)}}' + "%" +
                '</span>' +
                '</span>' +
                '<span class="empty-ticker arrow-up" ng-click="changeComparisonType(\'thirdLevel\')" ng-hide="hideEmptyTickerSecondLevel" style="cursor:pointer" ng-if="!(percent(formatColumn, thirdLevelTickerValue,thirdLevelTickerValue1)) && showThirdDifference == true">0 </span>' +
                //Diff
                '<span ng-click="changeComparisonType(\'thirdLevelLevel\')" ' +
                'style="cursor:pointer" ng-if="sub(formatColumnThird, thirdLevelTickerValue,thirdLevelTickerValue1) != 0 && showThirdDifference == true && thirdLevelTickerValue != 0 && thirdLevelTickerValue1 != 0"' + 'ng-class="{\'arrow-up\':(sub(formatColumn, thirdLevelTickerValue,thirdLevelTickerValue1) > 0), \'arrow-down\':(sub(formatColumn, thirdLevelTickerValue,thirdLevelTickerValue1) < 0)}" ng-hide="hideEmptyTickerSecondLevel">' +
                '<i class="fa" ng-class="{\'fa-arrow-up\':sub(formatColumnThird, thirdLevelTickerValue,thirdLevelTickerValue1) > 0, \'fa-arrow-down\':(sub(formatColumnThird, thirdLevelTickerValue,thirdLevelTickerValue1) < 0)}"></i>' +
                '<span class="leads" ng-show="showThirdDifference">' + '{{differ(formatColumnThird, thirdLevelTickerValue,thirdLevelTickerValue1)}}' +
                '<span class="leads-name" ng-if="sub(formatColumnThird, thirdLevelTickerValue,thirdLevelTickerValue1)"> {{secondFormatNameThird}}</span>' +
                '</span>' +
                '</span>' +
                '</h4>' +
                '</div>' +
                '</div>' +
                '</div>' +
                '</div>' +
                '</div>' +
                '</div>' +
                '<div class="tickerMessage" ng-show="hideEmptyTicker">' +
                '<div class="panel-body h-150">' +
                '<div class="stats-title pull-left">' +
                '<h4>{{tickerTitleName}}</h4>' +
                '</div>' +
                '<div class="stats-icon pull-right">' +
                '</div>' +
                '<div class="m-t-xl" style="text-align:center">{{tickerEmptyMessage}}' + '</div>' +
                '</div>' +
                '</div>',
        scope: {
            setTickerFn: '&',
            tickerSource: '@',
            tickerId: '@',
            tickerColumns: '@',
            tickerTitleName: '@',
            widgetObj: '@',
            defaultChartColor: '@',
            compareDateRange: '@',
            urlType: '@'
        },
        link: function (scope, element, attr) {
            if (!scope.widgetObj) {
                return;
            }
            var getWidgetObj = JSON.parse(scope.widgetObj);
            scope.loadingTicker = true;
            var tickerName = [];
            angular.forEach(JSON.parse(scope.tickerColumns), function (value, key) {
                if (!value) {
                    return;
                }
                tickerName.push({fieldName: value.fieldName, displayName: value.displayName, displayFormat: value.displayFormat, agregationFunction: value.agregationFunction, icon: value.icon});
            });
            scope.format = function (column, value) {
                if (!value) {
                    return "";
                }
                if (column && column.displayFormat) {
                    var columnValue = dashboardFormat(column, value);
                    if (columnValue == 'NaN') {
                        columnValue = "";
                    }
                    return columnValue;
                }
                return value;
            };
            scope.differ = function (column, firstLevel, secondLevel) {
                if (!column) {
                    column = "";
                }
                if (!firstLevel) {
                    firstLevel = 0;
                }
                if (!secondLevel) {
                    secondLevel = 0;
                }
                var firstLevelTicker = firstLevel;
                var secondLevelTicker = secondLevel;
                if (column.fieldType === "date" || column.fieldType === "string") {
                    return "-";
                }
                var sub;
                if ((firstLevelTicker || firstLevelTicker == 0) && (secondLevelTicker || secondLevelTicker == 0)) {
                    sub = firstLevelTicker - secondLevelTicker;
                    sub = sub + "";
                    var tmpFormat = {};
                    if (sub.substr(0, 1) == "-") {
                        sub = sub.replace("-", "");
                        sub = parseFloat(sub);
                        if (isNumber(sub)) {
                            if (column.displayFormat == ',.2%') {
                                tmpFormat.fieldType = 'number';
                                tmpFormat.displayFormat = ',.2f';
                            }
                            return "-" + scope.format(tmpFormat, sub);
                        } else {
                            return "-";
                        }
                    } else {
                        sub = parseFloat(sub);
                        if (isNumber(sub)) {
                            if (column.displayFormat == ',.2%') {
                                tmpFormat.fieldType = 'number';
                                tmpFormat.displayFormat = ',.2f';
                            }
                            return  scope.format(tmpFormat, sub);
                        } else {
                            return "-";
                        }
                    }
                }
                if (firstLevelTicker && (!secondLevelTicker && secondLevelTicker != 0)) {
                    return scope.format(column, firstLevelTicker);
                }
                if ((!firstLevelTicker && firstLevelTicker != 0) && secondLevelTicker) {
                    if (scope.format(column, secondLevelTicker) != "-") {
                        return "-" + scope.format(column, secondLevelTicker);
                    }
                    return  scope.format(column, secondLevelTicker);
                }
                if (!firstLevelTicker && !secondLevelTicker) {
                    return "";
                }
            };

            scope.sub = function (column, firstLevel, secondLevel) {
                if (!column) {
                    column = "";
                }
                if (!firstLevel) {
                    firstLevel = 0;
                }
                if (!secondLevel) {
                    secondLevel = 0;
                }
                var firstLevelTicker = firstLevel;
                var secondLevelTicker = secondLevel;
                if (column.fieldType === "date" || column.fieldType === "string") {
                    return;
                }
                var sub;
                if ((firstLevelTicker || firstLevelTicker == 0) && (secondLevelTicker || secondLevelTicker == 0)) {
                    sub = firstLevelTicker - secondLevelTicker;
                    return sub;
                }
                if (firstLevelTicker && (!secondLevelTicker && secondLevelTicker != 0)) {
                    return firstLevelTicker;
                }
                if ((!firstLevelTicker && firstLevelTicker != 0) && secondLevelTicker) {
                    return secondLevelTicker;
                }
                if (!firstLevelTicker && !secondLevelTicker) {
                    return;
                }
            };

            scope.percent = function (column, firstLevel, secondLevel) {
                if (!column) {
                    column = "";
                }

                if (column.fieldType === "date" || column.fieldType === "string") {
                    return 0;
                }
                if (firstLevel && secondLevel) {
                    var percent = ((firstLevel - secondLevel) / secondLevel) * 100;
                    if (percent.toFixed(2) != 0.00) {
                        return percent.toFixed(2);
                    } else {
                        return;
                    }
                } else {
                    return;
                }
            };

            scope.changeComparisonType = function (type) {
                if (type == "firstLevel") {
                    if (scope.showDifference) {
                        scope.showDifference = false;
                    } else {
                        scope.showDifference = true;
                    }
                } else if (type == "secondLevel") {
                    if (scope.showSecondDifference) {
                        scope.showSecondDifference = false;
                    } else {
                        scope.showSecondDifference = true;
                    }
                } else {
                    if (scope.showThirdDifference) {
                        scope.showThirdDifference = false;
                    } else {
                        scope.showThirdDifference = true;
                    }
                }
            };
            scope.selectedChartIcon = getWidgetObj.icon;
            var setData = [];
            var data = [];
            var tickerDataSource = JSON.parse(scope.tickerSource);
            var getCompareStatus = getWidgetObj.compareTabletype;
            var isCompare = getCompareStatus ? getCompareStatus : scope.urlType;
            console.log("url type----------------------->", scope.urlType);
            var url;

            function calTotal(val, displayFormat) {
                if (!val) {
                    return;
                }
                var total = 0;
                angular.forEach(val, function (value, key) {
                    if (value) {
                        if (value.toString().indexOf(',') !== -1) {
                            value = value.replace(/\,/g, '');
                        }
                        total += parseFloat(value);
                    }
                });
                if (displayFormat == ',.2%') {
                    total = total / val.length;
                }
                return total;
            }
            var dateRangeType;
            if (isCompare == 'compareOn') {
                var compareRange = JSON.parse(scope.compareDateRange);
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

            var startDates1 = (moment().subtract(1, 'months').startOf('month'));//$stateParams.startDate//new Date(moment().subtract(2, 'month').startOf('month')).toLocaleDateString('en-US');
            var endDates1 = (moment().subtract(1, 'months').endOf('month'));//$stateParams.endDate//new Date(moment().subtract(2, 'month').endOf('month')).toLocaleDateString('en-US');
            var startDates2 = (moment().subtract(2, 'months').startOf('month'));//$stateParams.startDate//new Date(moment().subtract(1, 'month').startOf('month')).toLocaleDateString('en-US');
            var endDates2 = (moment().subtract(2, 'months').endOf('month'));//$stateParams.endDate//new Date(moment().subtract(1, 'month').endOf('month')).toLocaleDateString('en-US');
            var startDate1 = $filter('date')(new Date(startDates1), 'MM/dd/yyyy');
            var endDate1 = $filter('date')(new Date(endDates1), 'MM/dd/yyyy');
            var startDate2 = $filter('date')(new Date(startDates2), 'MM/dd/yyyy');
            var endDate2 = $filter('date')(new Date(endDates2), 'MM/dd/yyyy');

            var compareDateRangeDates = "&startDate1=" + startDate1 + "&endDate1=" + endDate1 + "&startDate2=" + startDate2 + "&endDate2=" + endDate2;

            var dataSourcePassword;
            if (tickerDataSource.dataSourceId.password) {
                dataSourcePassword = tickerDataSource.dataSourceId.password;
            } else {
                dataSourcePassword = '';
            }


            // var defaultColors = scope.defaultChartColor ? JSON.parse(scope.defaultChartColor) : "";
//                var defaultColors = ['#59B7DE', '#D7EA2B', '#FF3300', '#E7A13D', '#3F7577', '#7BAE16'];
            var widgetChartColors;
            if (getWidgetObj.chartColorOption) {
                widgetChartColors = getWidgetObj.chartColorOption.split(',');
            }
            var setWidgetChartColors = getWidgetObj.chartColors ? getWidgetObj.chartColors : "";
            var chartColors = widgetChartColors ? widgetChartColors : setWidgetChartColors;
            scope.colorName = chartColors[0];

            // var getWidgetObj = JSON.parse(scope.widgetObj);
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
            var listOfCalculatedFunction = [
                {name: 'ctr', field1: ['clicks'], field2: ['impressions']},
                {name: 'cpa', field1: ['cost'], field2: ['conversions']},
                {name: 'cpas', field1: ['spend'], field2: ['conversions']},
                {name: 'cpc', field1: ['spend', 'cost'], field2: ['clicks']},
                {name: 'cpcs', field1: ['spend'], field2: ['clicks']},
                {name: 'cpr', field1: ['spend'], field2: ['actions_post_reaction']},
                {name: 'ctl', field1: ['spend'], field2: ['actions_like']},
                {name: 'cplc', field1: ['spend'], field2: ['actions_link_click']},
                {name: 'cpcomment', field1: ['spend'], field2: ['actions_comment']},
                {name: 'cposte', field1: ['spend'], field2: ['actions_post_engagement']},
                {name: 'cpagee', field1: ['spend'], field2: ['actions_page_engagement']},
                {name: 'cpp', field1: ['spend'], field2: ['actions_post']}
            ];
            scope.refreshTicker = function () {
                $http.get(url + 'connectionUrl=' + tickerDataSource.dataSourceId.connectionString +
                        "&dataSetId=" + tickerDataSource.id +
                        "&accountId=" + $stateParams.accountId +
                        "&productSegment=" + setProductSegment +
                        "&timeSegment=" + setTimeSegment +
                        "&networkType=" + setNetworkType +
                        "&userId=" + (tickerDataSource.userId ? tickerDataSource.userId.id : null) +
                        "&driver=" + tickerDataSource.dataSourceId.sqlDriver +
                        "&dataSetReportName=" + tickerDataSource.reportName +
                        "&location=" + $stateParams.locationId +
//                        "&startDate=" + $stateParams.startDate +
//                        "&endDate=" + $stateParams.endDate +
                        dateRangeType +
                        // "&productSegment=" + setProductSegment +
                        // "&timeSegment=" + setTimeSegment +
                        // "&networkType=" + setNetworkType +
                        '&username=' + tickerDataSource.dataSourceId.userName +
                        '&password=' + dataSourcePassword +
                        '&widgetId=' + scope.tickerId +
                        '&url=' + tickerDataSource.url +
                        '&port=3306&schema=vb&query=' + encodeURI(tickerDataSource.query)).success(function (response) {
                    scope.tickers = [];
                    scope.tickerItems = [];
                    scope.loadingTicker = false;
                    var tickerData = response.data;
                    if (!response) {
                        scope.tickerEmptyMessage = "No Data Found";
                        scope.hideEmptyTicker = true;
                        return;
                    }
                    if (response.data == null || response.data.length === 0) {
                        scope.tickerEmptyMessage = "No Data Found";
                        scope.hideEmptyTicker = true;
                    } else {
                        var returnDimensionData = {};
                        var returnMetricsData1 = {};
                        var returnMetricsData2 = {};
                        angular.forEach(tickerName, function (value, key) {
                            var field1 = null;
                            var field2 = null;
                            angular.forEach(listOfCalculatedFunction, function (calculatedFn, key) {
                                if (value.agregationFunction == calculatedFn.name) {
                                    angular.forEach(calculatedFn.field1, function (fieldValue1) {
                                        angular.forEach(calculatedFn.field2, function (fieldValue2) {
                                            angular.forEach(tickerData, function (val) {
                                                if (val[fieldValue1]) {
                                                    field1 = fieldValue1;
                                                }
                                                if (val[fieldValue2]) {
                                                    field2 = fieldValue2;
                                                }
                                            });
                                        });
                                    });
                                    returnDimensionData[field1] = [];
                                    returnMetricsData1[field1] = [];
                                    returnMetricsData2[field1] = [];
                                    returnDimensionData[field2] = [];
                                    returnMetricsData1[field2] = [];
                                    returnMetricsData2[field2] = [];
                                }
                            });
                            var field = value.fieldName;
                            returnDimensionData[field] = [];
                            returnMetricsData1[field] = [];
                            returnMetricsData2[field] = [];
                            angular.forEach(tickerData, function (val) {
                                if (val && val[field]) {
                                    returnDimensionData[field].push(val[field]);
                                    if (field1 && field2) {
                                        returnDimensionData[field1].push(val[field1]);
                                        returnDimensionData[field2].push(val[field2]);
                                    }
                                }
                                if (val.metrics1) {
                                    returnMetricsData1[field].push(val.metrics1[field]);
                                    if (field1 && field2) {
                                        returnMetricsData1[field1].push(val.metrics1[field1]);
                                        returnMetricsData1[field2].push(val.metrics1[field2]);
                                    }
                                }
                                if (val.metrics2) {
                                    returnMetricsData2[field].push(val.metrics2[field]);
                                    if (field1 && field2) {
                                        returnMetricsData2[field1].push(val.metrics2[field1]);
                                        returnMetricsData2[field2].push(val.metrics2[field2]);
                                    }
                                }
                            });
                            var timeFormat = "";
                            if (value.displayFormat) {
                                if (value.displayFormat == "M:S") {
                                    timeFormat = "min";
                                }
                            }
                            if (isCompare == 'compareOn') {
                                scope.tickers.push({tickerTitle: value.displayName,
                                    dimensionData: field1 && field2 ? calTotal(returnDimensionData[field1], value.displayFormat) / calTotal(returnDimensionData[field2], value.displayFormat) : calTotal(returnDimensionData[field], value.displayFormat),
                                    metricsData1: field1 && field2 ? calTotal(returnMetricsData1[field1], value.displayFormat) / calTotal(returnMetricsData1[field2], value.displayFormat) : calTotal(returnMetricsData1[field], value.displayFormat),
                                    metricsData2: field1 && field2 ? calTotal(returnMetricsData2[field1], value.displayFormat) / calTotal(returnMetricsData2[field2], value.displayFormat) : calTotal(returnMetricsData2[field], value.displayFormat),
                                    column: value,
                                    valueFormat1: timeFormat,
                                    valueFormat2: timeFormat
                                });
                            } else {
                                scope.tickers.push({tickerTitle: value.displayName,
                                    totalValue: field1 ? calTotal(returnDimensionData[field1], value.displayFormat) / calTotal(returnDimensionData[field2], value.displayFormat) : calTotal(returnDimensionData[field], value.displayFormat),
                                    column: value, valueFormat: timeFormat, });
                            }
                        });
                        if (isCompare == 'compareOn') {
                            scope.firstLevelTickerValue = scope.tickers[0].metricsData1 ? scope.tickers[0].metricsData1 : scope.tickers[0].dimensionData;
                            scope.firstLevelTickerValue1 = scope.tickers[0].metricsData2;

                            scope.secondLevelTickerTitle = scope.tickers[1] ? scope.tickers[1].tickerTitle : '';
                            scope.secondLevelTickerValue = scope.tickers[1] ? scope.tickers[1].metricsData1 : '';
                            scope.secondLevelTickerValue1 = scope.tickers[1] ? scope.tickers[1].metricsData2 : '';

                            scope.thirdLevelTickerTitle = scope.tickers[2] ? scope.tickers[2].tickerTitle : '';
                            scope.thirdLevelTickerValue = scope.tickers[2] ? scope.tickers[2].metricsData1 : '';
                            scope.thirdLevelTickerValue1 = scope.tickers[2] ? scope.tickers[2].metricsData2 : '';

                            scope.formatColumn = scope.tickers[0] ? scope.tickers[0].column : '';
                            scope.firstFormatName = scope.tickers[0] ? scope.tickers[0].valueFormat1 : "";
                            scope.secondFormatName = scope.tickers[0] ? scope.tickers[0].valueFormat2 : "";
                            scope.formatColumnSecond = scope.tickers[1] ? scope.tickers[1].column : '';

                            scope.firstFormatNameSecond = scope.tickers[1] ? scope.tickers[1].valueFormat1 : "";
                            scope.secondFormatNameSecond = scope.tickers[1] ? scope.tickers[1].valueFormat2 : "";
                            scope.formatColumnThird = scope.tickers[2] ? scope.tickers[2].column : '';
                            scope.firstFormatNameThird = scope.tickers[2] ? scope.tickers[2].valueFormat1 : "";
                            scope.secondFormatNameThird = scope.tickers[2] ? scope.tickers[2].valueFormat2 : "";
                            if (!scope.secondFormatName) {
                                scope.secondFormatName = scope.tickers[0].metricsData2 ? scope.tickers[0].valueFormat2 : "";
                            }
                            if (scope.secondLevelTicker) {
                                scope.hideEmptyTickerSecondLevel = false;
                            }
                            if (!scope.secondFormatNameSecond) {
                                scope.secondFormatNameSecond = scope.tickers[1] ? scope.tickers[1].valueFormat2 : "";
                            }
                            if (scope.secondLevelTickerValue1) {
                                scope.hideEmptyTickerSecondLevel = false;
                            }
                            if (!scope.secondFormatNameThird) {
                                scope.secondFormatNameThird = scope.tickers[2] ? scope.tickers[2].valueFormat2 : "";
                            }
                            if (scope.thirdLevelTickerValue1) {
                                scope.hideEmptyTickerSecondLevel = false;
                            }
                        } else {
                            scope.showDifference = false;
                            scope.firstLevelTicker = scope.tickers[0]//.totalValue;
                            scope.secondLevelTicker = scope.tickers[1];
                            scope.thirdLevelTicker = scope.tickers[2];
                            scope.formatColumn = scope.tickers[0] ? scope.tickers[0].column : '';
                            scope.formatColumnSecond = scope.tickers[1] ? scope.tickers[1].column : '';
                            scope.formatColumnThird = scope.tickers[2] ? scope.tickers[2].column : '';
                            scope.firstFormatName = scope.tickers[0].totalValue ? scope.tickers[0].valueFormat : "";
                            if (!scope.secondLevelTicker) {
                                scope.secondFormatName = "";
                                //scope.hideEmptyTickerSecondLevel = true;
                            }
                        }
                    }
                });
            }
            scope.setTickerFn({tickerFn: scope.refreshTicker});
            scope.refreshTicker();
        }
    }

});






//                        angular.forEach(tickerName, function (value, key) {
//                            var tickerData = response.data;
//                            var loopCount = 0;
//                            data = [value.fieldName];
//                            setData = tickerData.map(function (a) {
//                                data.push(loopCount);
//                                loopCount++;
//                                return a[value.fieldName];
//                            });
//                            var total = 0;
//                            for (var i = 0; i < setData.length; i++) {
//                                if (setData[i].toString().indexOf(',') !== -1) {
//                                    setData[i] = setData[i].replace(/\,/g, '');
//                                }
//                                total += parseFloat(setData[i]);
//                            }
//                            scope.tickers.push({tickerTitle: value.displayName, totalValue: format(value, total)});
//                        });
//                    }
//                    scope.firstLevelTicker = scope.tickers[0];
//                    scope.secondLevelTicker = scope.tickers[1];
//                    scope.thirdLevelTicker = scope.tickers[2];
//                });
//            };
//            scope.setTickerFn({tickerFn: scope.refreshTicker});
//            scope.refreshTicker();
//        }
//    };
//});



//app.directive('tickerDirective', function ($http, $stateParams, $filter) {
//    return{
//        restrict: 'AE',
//        template: '<div ng-show="loadingTicker" class="text-center" style="color: #228995;"><img src="static/img/logos/loader.gif"></div>' +
//                '<div ng-hide="loadingTicker" class="">' +
//                '<div class="inner" ng-hide="hideEmptyTicker">' +
//                '<div class="col-md-9 ticker-value">{{format(formatColumn, firstLevelTicker)}}<span class="ticker-time">{{firstFormatName}}</span></div>' +
//                '</div>' +
//                '<div class="inner text-center text-empty" ng-show="hideEmptyTicker">' +
//                '<span ng-bind-html="tickerEmptyType | setTextMessageColor"></span>' +
//                '<p>{{tickerEmptyDescription | uppercase}}</p>' +
//                '</div>' +
//                '<div class="icon" ng-hide="hideEmptyTicker"><i class="{{selectedChartIcon}}" aria-hidden="true"></i></div>' +
//                '<div class="small-box-footer">' +
//                '<span class="pull-left col-md-7 row ticker-title">{{tickerTitle}}</span>' +
//                //Percentage
//                '<div class="col-md-5 pull-right">' +
//                '<span ng-click="changeComparisonType()" ' +
//                'style="cursor:pointer" ng-if="percent(formatColumn, firstLevelTicker,secondLevelTicker) && showDifference == true" ' +
//                'ng-class="{\'arrow-up\':(percent(formatColumn, firstLevelTicker,secondLevelTicker) > 0), \'arrow-down\':(percent(formatColumn, firstLevelTicker,secondLevelTicker) < 0)}" ng-hide="hideEmptyTickerSecondLevel">' +
//                '<i class="fa" ng-class="{\'fa-arrow-up\':percent(formatColumn, firstLevelTicker,secondLevelTicker) > 0, \'fa-arrow-down\':(percent(formatColumn, firstLevelTicker,secondLevelTicker) < 0)}"></i>' +
//                '<span ng-class="{\'leads-up\':percent(formatColumn, firstLevelTicker,secondLevelTicker) > 0, \'leads-down\':(percent(formatColumn, firstLevelTicker,secondLevelTicker) < 0)}" ng-show="showDifference">' +
//                '{{percent(formatColumn, firstLevelTicker,secondLevelTicker)}}' + "%" +
//                '</span>' +
//                '</span>' +
//                '<span class="empty-ticker arrow-up" ng-click="changeComparisonType()" ng-hide="hideEmptyTickerSecondLevel" style="cursor:pointer" ng-if="!(percent(formatColumn, firstLevelTicker,secondLevelTicker)) && showDifference == true">0 </span>' +
//                //Diff
//                '<span ng-click="changeComparisonType()" ' +
//                'style="cursor:pointer" ng-if="sub(formatColumn, firstLevelTicker,secondLevelTicker) != 0 && showDifference != true && firstLevelTicker != 0 && secondLevelTicker != 0"' + 'ng-class="{\'arrow-up\':(sub(formatColumn, firstLevelTicker,secondLevelTicker) > 0), \'arrow-down\':(sub(formatColumn, firstLevelTicker,secondLevelTicker) < 0)}" ng-hide="hideEmptyTickerSecondLevel">' +
//                '<i class="fa" ng-class="{\'fa-arrow-up\':sub(formatColumn, firstLevelTicker,secondLevelTicker) > 0, \'fa-arrow-down\':(sub(formatColumn, firstLevelTicker,secondLevelTicker) < 0)}"></i>' +
//                '<span class="leads" ng-hide="showDifference">' + '{{differ(formatColumn, firstLevelTicker,secondLevelTicker)}}' +
//                '<span class="leads-name" ng-if="sub(formatColumn, firstLevelTicker,secondLevelTicker)"> {{secondFormatName}}</span>' +
//                '</span>' +
//                '</span>' +
//                '<span class="empty-ticker arrow-up" ng-click="changeComparisonType()"  ng-hide="hideEmptyTickerSecondLevel" style="cursor:pointer" ng-if="(((sub(formatColumn, firstLevelTicker,secondLevelTicker) != 0) && (firstLevelTicker == 0  ||  secondLevelTicker == 0))  ||  (sub(formatColumn, firstLevelTicker,secondLevelTicker) == 0)) && showDifference != true">0 </span>' + '</div>' +
//                '</div>' +
//                '</div>',
//        scope: {
//            tickerUrl: '@',
//            tickerId: '@',
//            tickerColumns: '@',
//            tickerDateDuration: '@',
//            tickerFrequencyDuration: '@',
//            tickerCustomRange: '@',
//            tickerObj: '@',
//            monthEndReport: '@',
//            compareDateRange: '@',
//            urlType: '@',
//            forceClose: '&'
//        },
//        link: function (scope, element, attr) {
//            scope.loadingTicker = true;
//            scope.showDifference = true;
//            var getWidgetObj = JSON.parse(scope.tickerObj);
//            var tickerName = [];
//            angular.forEach(JSON.parse(scope.tickerColumns), function (value, key) {
//                scope.tickerTitle = value.displayName;
//                tickerName.push({fieldName: value.fieldName, displayName: value.displayName, displayFormat: value.displayFormat});
//            });
//
//            scope.format = function (column, value) {
//                if (!value) {
//                    return "-";
//                }
//                if (column.displayFormat) {
//                    return dashboardFormat(column, value);
//                }
//                return value;
//            };
//
//            scope.differ = function (column, firstLevel, secondLevel) {
//                if (!column) {
//                    column = "";
//                }
//                if (!firstLevel) {
//                    firstLevel = 0;
//                }
//                if (!secondLevel) {
//                    secondLevel = 0;
//                }
//                var firstLevelTicker = firstLevel;
//                var secondLevelTicker = secondLevel;
//                if (column.fieldType === "date" || column.fieldType === "string") {
//                    return "-";
//                }
//                var sub;
//                if ((firstLevelTicker || firstLevelTicker == 0) && (secondLevelTicker || secondLevelTicker == 0)) {
//                    sub = firstLevelTicker - secondLevelTicker;
//                    sub = sub + "";
//                    if (sub.substr(0, 1) == "-") {
//                        sub = sub.replace("-", "");
//                        sub = parseFloat(sub);
//                        if (isNumber(sub)) {
//                            return "-" + scope.format(column, sub);
//                        } else {
//                            return "-";
//                        }
//                    } else {
//                        sub = parseFloat(sub);
//                        if (isNumber(sub)) {
//                            return  scope.format(column, sub);
//                        } else {
//                            return "-";
//                        }
//                    }
//                }
//                if (firstLevelTicker && (!secondLevelTicker && secondLevelTicker != 0)) {
//                    return scope.format(column, firstLevelTicker);
//                }
//                if ((!firstLevelTicker && firstLevelTicker != 0) && secondLevelTicker) {
//                    if (scope.format(column, secondLevelTicker) != "-") {
//                        return "-" + scope.format(column, secondLevelTicker);
//                    }
//                    return  scope.format(column, secondLevelTicker);
//                }
//                if (!firstLevelTicker && !secondLevelTicker) {
//                    return "-";
//                }
//            };
//
//            scope.sub = function (column, firstLevel, secondLevel) {
//                if (!column) {
//                    column = "";
//                }
//                if (!firstLevel) {
//                    firstLevel = 0;
//                }
//                if (!secondLevel) {
//                    secondLevel = 0;
//                }
//                var firstLevelTicker = firstLevel;
//                var secondLevelTicker = secondLevel;
//                if (column.fieldType === "date" || column.fieldType === "string") {
//                    return;
//                }
//                var sub;
//                if ((firstLevelTicker || firstLevelTicker == 0) && (secondLevelTicker || secondLevelTicker == 0)) {
//                    sub = firstLevelTicker - secondLevelTicker;
//                    return sub;
//                }
//                if (firstLevelTicker && (!secondLevelTicker && secondLevelTicker != 0)) {
//                    return firstLevelTicker;
//                }
//                if ((!firstLevelTicker && firstLevelTicker != 0) && secondLevelTicker) {
//                    return secondLevelTicker;
//                }
//                if (!firstLevelTicker && !secondLevelTicker) {
//                    return;
//                }
//            };
//
//            scope.percent = function (column, firstLevel, secondLevel) {
//                if (!column) {
//                    column = "";
//                }
//
//                if (column.fieldType === "date" || column.fieldType === "string") {
//                    return 0;
//                }
//
////                if (column && column.displayFormat) {
////                    if (column.displayFormat === "H:M:S" || column.displayFormat === "M:S") {
////                        return;
////                    }
////                }
//
//                if (firstLevel && secondLevel) {
//                    var percent = ((firstLevel - secondLevel) / secondLevel) * 100;
//                    if (percent.toFixed(2) != 0.00) {
//                        return percent.toFixed(2);
//                    } else {
//                        return;
//                    }
//                } else {
//                    return;
//                }
//            };
//
//            scope.changeComparisonType = function () {
//                if (scope.showDifference) {
//                    scope.showDifference = false;
//                } else {
//                    scope.showDifference = true;
//                }
//            };
//
//            var setData = [];
//            var data = [];
//
//            scope.tickerTitle = getWidgetObj.widgetTitle;
//            scope.selectedChartIcon = getWidgetObj.chartIcon;
//            var getWidgetId = getWidgetObj.id;
//            var getWidgetUrl = getWidgetObj.directUrl;
//            var getWidgetLevel = (getWidgetObj.reportLevel ? (getWidgetObj.reportLevel.reportLevel ? getWidgetObj.reportLevel.reportLevel : getWidgetObj.reportLevel) : ""); //getWidgetObj.reportLevel;
//            var getWidgetSegment = (getWidgetObj.reportSegment ? (getWidgetObj.reportSegment.reportSegment ? getWidgetObj.reportSegment.reportSegment : getWidgetObj.reportSegment) : ""); //getWidgetObj.reportSegment;
//            var getProductName = getWidgetObj.productName;
//            var monthEndReport = scope.monthEndReport;
//            var getCompareStatus = getWidgetObj.compareTableype;
//            var isCompare = getCompareStatus ? getCompareStatus : scope.urlType;
//            var url;
//            var urlPath;
//
//            function calTotal(val) {
//                if (!val) {
//                    return;
//                }
//                var total = 0;
//                for (var i = 0; i < val.length; i++) {
//                    if (val[i]) {
//                        if (val[i].toString().indexOf(',') !== -1) {
//                            val[i] = val[i].replace(/\,/g, '');
//                        }
//                        total += parseFloat(val[i]);
//                    }
//                }
//                return total;
//            }
//            var dateRangeType;
//            if (isCompare == 'compareOn') {
//                var compareRange = JSON.parse(scope.compareDateRange);
//                var compareStartDate = compareRange.startDate;
//                var compareEndDate = compareRange.endDate;
//                dateRangeType = '&startDate1=' + $stateParams.startDate +
//                        "&endDate1=" + $stateParams.endDate +
//                        "&startDate2=" + compareStartDate +
//                        "&endDate2=" + compareEndDate;
//            } else {
//                dateRangeType = '&startDate=' + $stateParams.startDate + "&endDate=" + $stateParams.endDate;
//            }
//
//            var startDates1 = (moment().subtract(1, 'months').startOf('month'));//$stateParams.startDate//new Date(moment().subtract(2, 'month').startOf('month')).toLocaleDateString('en-US');
//            var endDates1 = (moment().subtract(1, 'months').endOf('month'));//$stateParams.endDate//new Date(moment().subtract(2, 'month').endOf('month')).toLocaleDateString('en-US');
//            var startDates2 = (moment().subtract(2, 'months').startOf('month'));//$stateParams.startDate//new Date(moment().subtract(1, 'month').startOf('month')).toLocaleDateString('en-US');
//            var endDates2 = (moment().subtract(2, 'months').endOf('month'));//$stateParams.endDate//new Date(moment().subtract(1, 'month').endOf('month')).toLocaleDateString('en-US');
//            var startDate1 = $filter('date')(new Date(startDates1), 'MM/dd/yyyy');
//            var endDate1 = $filter('date')(new Date(endDates1), 'MM/dd/yyyy');
//            var startDate2 = $filter('date')(new Date(startDates2), 'MM/dd/yyyy');
//            var endDate2 = $filter('date')(new Date(endDates2), 'MM/dd/yyyy');
//
//            var compareDateRangeDates = "&startDate1=" + startDate1 + "&endDate1=" + endDate1 + "&startDate2=" + startDate2 + "&endDate2=" + endDate2;
//
//            if (getProductName == 'Overall') {
//                if (isCompare != 'compareOn') {
//                    urlPath = "admin/mapdata/" + getWidgetUrl + "/json?";
//                } else {
//                    if (scope.monthEndReport == 'MonthEndReport') {
//                        if (getWidgetLevel == 'PERFORMANCE') {
//                            getWidgetUrl = "overallCmp/performance";
//                        }
//                        if (getWidgetLevel == 'BYPRODUCT') {
//                            getWidgetUrl = 'overallCmp/byProduct';
//                        }
//                        urlPath = "admin/mapdata/" + getWidgetUrl + "/json?" + compareDateRangeDates;
//                    } else {
//                        if (getWidgetLevel == 'PERFORMANCE') {
//                            getWidgetUrl = "overallCmp/performance";
//                        }
//                        if (getWidgetLevel == 'BYPRODUCT') {
//                            getWidgetUrl = 'overallCmp/byProduct';
//                        }
//                        urlPath = "admin/mapdata/" + getWidgetUrl + "/json?" + dateRangeType;
//                    }
//                }
//            } else {
//                if (isCompare != 'compareOn') {
//                    urlPath = "admin/mapdata/getMapData?urlPath=" + getWidgetUrl;
//                } else {
//                    if (scope.monthEndReport == 'MonthEndReport') {
//                        urlPath = "admin/mapdata/compareMapData?urlPath=" + getWidgetUrl + compareDateRangeDates;
//                    } else {
//                        urlPath = "admin/mapdata/compareMapData?urlPath=" + getWidgetUrl + dateRangeType;
//                    }
//                }
//            }
//
//            if (getWidgetLevel == 'COMPAREBYPRODUCT') {
//                url = urlPath +
//                        "&widgetId=" + getWidgetId +
//                        "&productName=" + getProductName +
//                        "&level=" + getWidgetLevel +
//                        "&segment=" + (getWidgetSegment ? getWidgetSegment : "") + dateRangeType +
//                        "&dealerIds=" + $stateParams.dealerId +
//                        "&date = " + new Date();
//            } else {
//                url = urlPath +
//                        "&widgetId=" + getWidgetId +
//                        "&productName=" + getProductName +
//                        "&level=" + getWidgetLevel +
//                        "&segment=" + (getWidgetSegment ? getWidgetSegment : "") +
//                        "&startDate=" + $stateParams.startDate +
//                        "&endDate=" + $stateParams.endDate +
//                        "&dealerIds=" + $stateParams.dealerId +
//                        "&date = " + new Date();
//            }
//
//            $http.get(url).success(function (response) {
//                scope.tickers = [];
//                scope.loadingTicker = false;
//                var errors = response.errors;
//                if (response && response.meta) {
//                    scope.forceClose({meta: response.meta});
//                } else {
//                    scope.forceClose({meta: false});
//                }
//                if (!response.data) {
//                    scope.tickerEmptyType = errors ? (errors[0] ? errors[0].type : "ERROR") : "ERROR";
//                    scope.tickerEmptyDescription = errors ? (errors[0] ? errors[0].description : "NO DATA FOUND") : "NO DATA FOUND";
//                    scope.hideEmptyTicker = true;
//                    scope.hideEmptyTickerSecondLevel = true;
//                    return;
//                }
//                if (response.data === null || response.data.length === 0) {
//                    scope.tickerEmptyType = errors ? (errors[0] ? errors[0].type : "ERROR") : "ERROR";
//                    scope.tickerEmptyDescription = errors ? (errors[0] ? errors[0].description : "NO DATA FOUND") : "NO DATA FOUND";
//                    scope.hideEmptyTicker = true;
//                    scope.hideEmptyTickerSecondLevel = true;
//                } else {
//                    if (!response) {
//                        return;
//                    }
//                    var tickerData = response.data;
//                    if (isCompare == 'compareOn') {
//                        var returnDimensionData = [];
//                        var returnMetricsData1 = [];
//                        var returnMetricsData2 = [];
//                        angular.forEach(tickerName, function (value, key) {
//                            angular.forEach(tickerData, function (val) {
//                                if (val && val[value.fieldName]) {
//                                    returnDimensionData.push(val[value.fieldName]);
//                                }
//                                if (val.metrics1) {
//                                    returnMetricsData1.push(val.metrics1[value.fieldName]);
//                                }
//                                if (val.metrics2) {
//                                    returnMetricsData2.push(val.metrics2[value.fieldName]);
//                                }
//                            });
//                            var timeFormat = "";
//                            if (value.displayFormat) {
//                                if (value.displayFormat == "M:S") {
//                                    timeFormat = "min";
//                                }
//                            }
//                            scope.tickers.push({tickerTitle: value.displayName,
//                                dimensionData: calTotal(returnDimensionData),
//                                metricsData1: calTotal(returnMetricsData1),
//                                metricsData2: calTotal(returnMetricsData2),
//                                column: value,
//                                valueFormat1: timeFormat,
//                                valueFormat2: timeFormat
//                            });
//                        });
//                        scope.firstLevelTicker = scope.tickers[0].metricsData1 ? scope.tickers[0].metricsData1 : scope.tickers[0].dimensionData;
//                        scope.secondLevelTicker = scope.tickers[0].metricsData2;
//                        scope.formatColumn = scope.tickers[0].column;
//                        scope.firstFormatName = scope.tickers[0].metricsData1 ? scope.tickers[0].valueFormat1 : "";
//                        scope.secondFormatName = scope.tickers[0].metricsData1 ? scope.tickers[0].valueFormat2 : "";
//                        if (!scope.secondFormatName) {
//                            scope.secondFormatName = scope.tickers[0].metricsData2 ? scope.tickers[0].valueFormat2 : "";
//                        }
//                        if (scope.secondLevelTicker) {
//                            scope.hideEmptyTickerSecondLevel = false;
//                        }
//                    } else {
//                        angular.forEach(tickerName, function (value, key) {
//                            var loopCount = 0;
//                            data = [value.fieldName];
//                            setData = tickerData.map(function (a) {
//                                data.push(loopCount);
//                                loopCount++;
//                                if (!a[value.fieldName]) {
//                                    return "";
//                                }
//                                return a[value.fieldName];
//                            });
//                            var timeFormat = "";
//                            if (value.displayFormat) {
//                                if (value.displayFormat == "M:S") {
//                                    timeFormat = "min";
//                                }
//                            }
//                            scope.tickers.push({tickerTitle: value.displayName, totalValue: calTotal(setData), column: value, valueFormat: timeFormat});
//                        });
//                        scope.firstLevelTicker = scope.tickers[0].totalValue;
//                        scope.secondLevelTicker = scope.tickers[1];
//                        scope.formatColumn = scope.tickers[0].column;
//                        scope.firstFormatName = scope.tickers[0].totalValue ? scope.tickers[0].valueFormat : "";
//                        if (!scope.secondLevelTicker) {
//                            scope.secondFormatName = "";
//                            scope.hideEmptyTickerSecondLevel = true;
//                        }
//                    }
//                }
//            });
//        }
//    };
//});
