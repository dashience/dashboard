app.directive('compareRangeTable', function ($http, localStorageService, $stateParams) {
    return{
        restrict: 'A',
        scope: {
            compareDateRange: '@',
            setTableChartFn: '&',
            compareTableSource: '@',
            widgetId: '@',
            widgetColumns: '@',
            tableFooter: '@',
            widgetObj: '@'
        },
        template:
                '<div ng-show="loadingTable" class="text-center" style="color: #228995;"><img src="static/img/logos/loader.gif" width="40"></div>' +
//                '<table ng-if="ajaxLoadingCompleted" class="table table-responsive table-striped table-l2t" ng-hide="hideEmptyTable">' +
                '<div ng-if="ajaxLoadingCompleted" class="table-responsive" ng-hide="hideEmptyTable">' +
                '<div>' +
                '<table class="table table-bordered">' +
                '<thead>' +
                '<tr>' +
                '<th ng-if="column.category != \'metrics\'" rowspan="{{getHeaderSpan(column, getData)+1}}" ng-repeat-start="column in columns">' +
                '<div>{{column.fieldName}}</div>' +
                '</th>' +
                '<th ng-if="column.category == \'metrics\'" colspan="{{getHeaderSpan(column, getData)}}" rowspan="{{getRowSpan(column)}}" ng-repeat-end>' +
                '<div>' +
                '<span>{{column.fieldName}}</span>' +
                '<span class="pull-right pdfButton">' +
                '<button class="btn btn-success btn-xs" ng-click="changeExpand(column); showCombainColumn(column, $index)" ng-attr-title="{{(column.expand != true)?\'Expand\':\'Reduce\'}}">' +
                '<i ng-if="!column.expand" class="fa fa-plus"></i>' +
                '<i ng-if="column.expand" class="fa fa-minus"></i>' +
                '</button>' +
                '</span>' +
                '</div>' +
                '</th>' +
                '</tr>' +
                //Child Columns Row
                '<tr>' +
                '<th style="display: none" ng-repeat-start="dateRowColumn in columns">' +
                '<th ng-if="dateRowColumn.category == \'metrics\' && dateRowColumn.expand" ng-repeat="date in getColumnDefs.summary">' +
                '<div>{{date.startDate}} - {{date.endDate}}</div>' +
                '</th>' +
                '<th ng-if="dateRowColumn.category == \'metrics\' && dateRowColumn.expand" ng-repeat-end>' +
                '<div>' +
                '<span ng-if="!dateRowColumn.changeCalculations">Difference</span>' +
                '<span class="pdfButton" ng-if="dateRowColumn.changeCalculations">Percentage</span>' +
                //Select Calculation Dropdown
                '<span class="dropdown pdfButton">' +
                '<a href="" class="dropdown-toggle" data-toggle="dropdown" role="button" aria-haspopup="true" aria-expanded="false"><span class="caret"></span></a>' +
                '<ul class="dropdown-menu" style="min-width: 50px !important">' +
                '<li ng-click="dateRowColumn.changeCalculations = !dateRowColumn.changeCalculations">' +
                '<a ng-if="dateRowColumn.changeCalculations">Difference</a>' +
                '<a ng-if="!dateRowColumn.changeCalculations">Percentage</a>' +
                '</li>' +
                '</ul>' +
                '</span>' +
                '</div>' +
                '</th>' +
                '</tr>' +
                '</thead>' +
                //Table Body
                '<tbody>' +
                '<tr ng-repeat="data in getData">' +
                '<td class="dimension-Fixed" ng-if="column.category != \'metrics\'" ng-repeat-start="column in columns">' +
                '<div>{{data[column.fieldName]}}</div>' +
                '</td>' +
                '<td ng-if="column.category == \'metrics\'">' +
                '<div>{{data.metrics1[column.fieldName]? format(column, data.metrics1[column.fieldName]):\'-\'}}</div>' +
                '</td>' +
                '<td ng-if="column.category == \'metrics\' && column.expand == true">' +
                '<div>{{data.metrics2[column.fieldName] ? format(column, data.metrics2[column.fieldName]) : \'-\'}}</div>' +
                '</td>' +
                '<td ng-if="column.category == \'metrics\' && column.expand == true" ng-repeat-end>' +
                '<div ng-if="!column.changeCalculations">{{format(column, (data.metrics1[column.fieldName]) - (data.metrics2[column.fieldName]))}}</div>' +
                '<div ng-if="column.changeCalculations">{{(((data.metrics1[column.fieldName]) - (data.metrics2[column.fieldName]))/(data.metrics2[column.fieldName]))*100 | number: 2}}%</div>' +
                '</td>' +
                '</tr>' +
                '</tbody>' +
                '</table>' +
                '</div>' +
                '</div>' +
                '<div class="text-center" ng-show="hideEmptyTable">' +
//                '<span ng-bind-html="tableEmptyType | setTextMessageColor"></span>' +
                '<span ng-bind-html="tableEmptyType"></span>' +
                '<p>{{tableEmptyDescription | uppercase}}</p>' +
                '</div>',
//        template:'<div><table border=1>' +
//                '<tr>' +
//                '<td ng-repeat="column in getColumnDefs.columnDefs" ng-init="column.hide=true">' +
//                '{{column.fieldName}}' +
//                '<span ng-if="getHeaderSpan(column, getData)!=1">' +
//                '<a ng-if="column.hide==true" ng-click="column.hide=false"><i class="fa fa-plus"></i></a>' +
//                '<a ng-if="column.hide!=true" ng-click="column.hide=true"><i class="fa fa-minus"></i></a>' +
//                '</span>' +
//                '</td>' +
//                '</tr>' +
//                '<tr>' +
//                '<td ng-repeat="column in getColumnDefs.columnDefs" ng-init="column.hide=true">' +
//                '<table class="table table-bordered">' +
//                '<tr>' +
//                '<th class="child-table-column" ng-if="column.hide!= true && getHeaderSpan(column, getData)!=1" ng-repeat="dateRange in getColumnDefs.summary">' +
//                '{{dateRange.startDate}} - {{dateRange.endDate}}'+
//                '</th>' +
//                '<th ng-if="column.hide!= true">Total</th>'+
//                '</tr>' +
//                '<tr ng-repeat="data in getData">' +
//                '<td ng-if="column.category == \'dimension\'" col-span=2>' +
//                '<div>{{data[column.fieldName]}}</div>' +
//                '</td>' +
//                '<td ng-if="column.category == \'metric\'">' +
//                '<div>{{data.metrics1[column.fieldName]}}</div>' +
//                '</td>' +
//                '<td ng-if="column.category == \'metric\' && column.hide!= true">' +
//                '<div>{{data.metrics2[column.fieldName]}}</div>' +
//                '</td>'+
//                '<td ng-if="column.category == \'metric\' && column.hide!= true">' +
//                '<div>{{data.metrics1[column.fieldName] - data.metrics2[column.fieldName]}}</div>' +
//                '</td>'+
//                '</tr>' +
//                '</table>' +
//                '</td>' +
//                '</tr>' +
//                '</table></div>',
        link: function (scope, element, attr) {
            try {
                var columnsList = JSON.parse(scope.widgetColumns)
            } catch (exception) {

            }


            scope.format = function (column, value) {
                if (!value) {
                    return "-";
                }
                if (column.displayFormat) {
                    return dashboardFormat(column, value);
                }
                return value;
            };
            scope.columns = [];
            if (scope.widgetColumns) {
                angular.forEach(columnsList, function (value, key) {
                    scope.columns.push(value);
                });
            }
            scope.getColspan = function (list) {
                var keySets = [];
                var getKeySetLength;
                if (list) {
                    angular.forEach(list, function (val, key) {
                        keySets.push([key]);
                    });
                    getKeySetLength = keySets.length;
                    return getKeySetLength;
                }
                return 0;
            };
            scope.changeExpand = function (column) {
                if (column.expand) {
                    column.expand = false;
                } else {
                    column.expand = true;
                }
            };
            scope.getRowSpan = function (column) {
                if (column.expand) {
                    return 1;
                }
                return 2;
            };
            var getWidgetObj;
            //if (scope.widgetObj) {
            try {
                getWidgetObj = JSON.parse(scope.widgetObj);
            } catch (exception) {

            }
            var maxRecord;
            if (getWidgetObj && getWidgetObj.maxRecord) {
                maxRecord = getWidgetObj.maxRecord;
            } else {
                maxRecord = ""
            }
//            } else {
//                getWidgetObj = "";
//            }
            //var getWidgetId = getWidgetObj.id;
            //var getWidgetUrl = getWidgetObj.directUrl;
            //var getWidgetLevel = (getWidgetObj.reportLevel ? (getWidgetObj.reportLevel.reportLevel ? getWidgetObj.reportLevel.reportLevel : getWidgetObj.reportLevel) : ""); //getWidgetObj.reportLevel;
            //var getWidgetSegment = (getWidgetObj.reportSegment ? (getWidgetObj.reportSegment.reportSegment ? getWidgetObj.reportSegment.reportSegment : getWidgetObj.reportSegment) : ""); //getWidgetObj.reportSegment;
            var url;
            url = "admin/proxy/getCompareData?urlPath=";
            var compareRange;
            if (scope.compareDateRange) {
                compareRange = JSON.parse(scope.compareDateRange)
            } else {
                compareRange = "";
            }

            var compareStartDate = compareRange.startDate;
            var compareEndDate = compareRange.endDate;
            scope.loadingTable = true;
            var compareTableDataSource = JSON.parse(scope.compareTableSource);
            var widgetData = JSON.parse(scope.widgetObj);
            var dataSourcePassword;
            if (compareTableDataSource.dataSourceId.password) {
                dataSourcePassword = compareTableDataSource.dataSourceId.password;
            } else {
                dataSourcePassword = '';
            }
            var setProductSegment;
            var setTimeSegment;
            var setNetworkType;

            if (widgetData.productSegment && widgetData.productSegment.type) {
                setProductSegment = widgetData.productSegment.type;
            } else {
                setProductSegment = widgetData.productSegment;
            }

            if (widgetData.timeSegment && widgetData.timeSegment.type) {
                setTimeSegment = widgetData.timeSegment.type;
            } else {
                setTimeSegment = widgetData.timeSegment;
            }

            if (widgetData.networkType && widgetData.networkType.type) {
                setNetworkType = widgetData.networkType.type;
            } else {
                setNetworkType = widgetData.networkType;
            }
            $http.get(url + '&connectionUrl=' + compareTableDataSource.dataSourceId.connectionString +
                    "&dataSetId=" + compareTableDataSource.id +
                    "&accountId=" + (widgetData.accountId ? (widgetData.accountId.id ? widgetData.accountId.id : widgetData.accountId) : $stateParams.accountId) +
                    "&userId=" + (compareTableDataSource.userId ? compareTableDataSource.userId.id : null) +
                    "&driver=" + compareTableDataSource.dataSourceId.sqlDriver +
                    "&productSegment=" + setProductSegment +
                    "&timeSegment=" + setTimeSegment +
                    "&networkType=" + setNetworkType +
                    "&startDate1=" + $stateParams.startDate +
                    "&endDate1=" + $stateParams.endDate +
                    '&startDate2=' + compareStartDate +
                    '&endDate2=' + compareEndDate +
                    '&username=' + compareTableDataSource.dataSourceId.userName +
                    "&dataSetReportName=" + compareTableDataSource.reportName +
                    '&password=' + dataSourcePassword +
                    '&widgetId=' + scope.widgetId +
                    '&url=' + compareTableDataSource.url +
                    '&port=3306&schema=vb&query=' + encodeURI(compareTableDataSource.query)).success(function (response) {
                scope.ajaxLoadingCompleted = true;
                scope.loadingTable = false;
                scope.hideEmptyTable = false;
                if (!response.data) {
                    scope.tableEmptyType = "ERROR";
                    scope.tableEmptyDescription = "NO DATA FOUND";
                    scope.hideEmptyTable = true;
                    return;
                }

                scope.getColumnDefs = response;
                scope.getSummary = response.summary;
                scope.getData = filterMaxRecord(response.data, maxRecord);
            });

            function filterMaxRecord(data, max) {
                return max ? data.splice(0, max) : data;
            }
            scope.getHeaderSpan = function (colDefs, obj) {
                var columnData = colDefs;
                var getObjs = obj;
                var machData;
                var totalLength;
                var count;
                if (columnData.category != 'metrics') {
                    return 1
                } else if (columnData.category == 'metrics') {
                    if (columnData.expand) {
                        return 3;
                    }
                    return 1;
                }
            };
        }
    };
});