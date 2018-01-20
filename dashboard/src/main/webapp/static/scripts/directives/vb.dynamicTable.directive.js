app.directive('dynamicTable', function ($http, $filter, $stateParams, orderByFilter) {
    return{
        restrict: 'A',
        scope: {
            setTableChartFn: '&',
            dynamicTableSource: '@',
            widgetId: '@',
            widgetColumns: '@',
            tableFooter: '@',
            widgetObj: '@',
            pdfFunction: '&'
        },
        template: '<div ng-show="loadingTable" class="text-center" style="color: #228995;"><img src="static/img/logos/loader.gif" width="40"></div>' +
//                Start Table
                '<table ng-if="ajaxLoadingCompleted" class="table table-striped" ng-hide="hideEmptyTable">' +
//                        Start Table Header
                '<thead>' +
                '<tr>' +
                '<th ng-if="groupingName">' + //Group Header Name with Icon
                '<i style="cursor: pointer" ng-click="groupingData.$hideRows = !groupingData.$hideRows; hideAll(groupingData, groupingData.$hideRows, true); selected_Row = !selected_Row" class="fa" ng-class="{\'fa-plus-circle\': !selected_Row, \'fa-minus-circle\': selected_Row}"></i>' +
                ' Group' +
                '</th>' +
                '<th ng-repeat="col in columns" ng-if="col.columnHide == null">' + //Display Fields Header Names and Sorting Icons
//                '<div ng-click="initData(col)" class="">{{col.displayName}}' +
                '<div ng-click="initData(col)" class="text-{{col.alignment}}">{{col.displayName}}' +
                '<i ng-if="col.sortOrder==\'asc\'" class="fa fa-sort-asc"></i>' +
                '<i ng-if="col.sortOrder==\'desc\'" class="fa fa-sort-desc"></i>' +
                '</div>' +
                '</th>' +
                '</tr>' +
                '<tr>' +
                '<th ng-repeat="col in columns">' + //Search Box
                '<div ng-if="col.search == true">' +
                '<input type="text" placeholder="Search..." class="form-control" ng-model="search.col[col.fieldName]" ng-change="bindSearch(search, col)" ng-mousedown="$event.stopPropagation()">' +
                '<div>' +
                '</th>' +
                '</tr>' +
                '</thead>' +
//                          End Of Table Header
//                          Start Table Body
                '<tbody ng-repeat="grouping in groupingData.data | filter : searchData">' +
                '<tr ng-if="!isZeroRow(grouping, columns)" ng-class-odd="\'odd\'" ng-class-even="\'even\'">' + //First Level Grouping Name and Data
                '<td ng-if="groupingName">' +
                '<i style="cursor: pointer" class="fa" ng-click="grouping.$hideRows = !grouping.$hideRows; hideParent(grouping, grouping.$hideRows); hideChild(grouping.data, false)" ng-class="{\'fa-plus-circle\': !grouping.$hideRows, \'fa-minus-circle\': grouping.$hideRows}"></i>' +
                ' <span ng-bind-html="grouping._key"></span>' +
                '</td>' +
                '<td ng-repeat="col in columns" style="width: {{col.width}}%" ng-if="col.columnHide == null">' +
                '<div class="text-{{col.alignment}}">' +
                '<span ng-if="col.displayFormat != \'starRating\'" ng-bind-html="format(col, grouping[col.fieldName])"></span>' +
                '<span ng-if="col.displayFormat == \'starRating\'" class="stars alignright">' +
                '<span ng-style="{\'width\': getStars(grouping[col.fieldName])}"></span>' +
                '</div>' +
                '</td>' +
                '</tr>' +
                '<tr ng-if="!isZeroRow(item, columns)" ng-show="grouping.$hideRows" ng-repeat-start="item in grouping.data">' + //Second Level Grouping and Data
                '<td class="right-group">' +
                '<i ng-if="item._groupField && item.data.length != 1" style="cursor: pointer" class="fa" ng-click="item.$hideRows = !item.$hideRows; hideChild(item, item.$hideRows)" ng-class="{\'fa-plus-circle\': !item.$hideRows, \'fa-minus-circle\': item.$hideRows}"></i>' +
                ' <span ng-bind-html="item._key"></span>' +
                '</td>' +
                '<td ng-repeat="col in columns" ng-if="col.columnHide == null">' +
                '<div class="text-{{col.alignment}}"><span ng-bind-html="format(col, item[col.fieldName])"></span></div>' +
                '</td>' +
                '</tr>' +
                '<tr ng-show="item.$hideRows" ng-if="!isZeroRow(childItem, columns) && item.data.length != 1" ng-repeat="childItem in item.data" ng-repeat-end>' + //Third Level Grouping and Data
                '<td>' + '</td>' +
                '<td ng-repeat="col in columns" style="width: {{col.width}}%" ng-if="col.columnHide == null">' +
                '<div class="text-{{col.alignment}}">' +
                '<span ng-bind-html="format(col, childItem[col.fieldName])"></span>' +
                '</div>' +
                '</td>' +
                '</tr>' +
                '</tbody>' +
//                    End Of table Body
//                    Start Of Table Footer
                '<tfoot ng-if="displayFooter == \'true\'">' +
                '<tr> {{initTotalPrint()}}' +
                '<td ng-if="groupingName">{{ showTotal() }}</td>' +
                '<td ng-repeat="col in columns" ng-if="col.columnHide == null">' +
                '<div ng-if="totalShown == 1" class="text-{{col.alignment}}">' +
                '<span ng-bind-html="format(col, groupingData[col.fieldName])"></span>' +
                '</div>' +
                '<div ng-if="totalShown != 1" class="text-{{col.alignment}}">{{showTotal()}}</div>' +
                '</td>' +
                '</tr>' +
                '</tfoot>' +
//                    End Of table Footer
                '</table>' +
                '<div class="text-center" ng-show="hideEmptyTable">{{tableEmptyMessage}}</div>', //+
        link: function (scope, element, attr) {
            if (!scope.widgetObj) {
                return;
            }
            var widgetData = JSON.parse(scope.widgetObj);
            scope.bindSearch = function (search) {
                scope.searchData = search.col;
            };
            scope.pdfFunction({test: "Test"});
            scope.totalShown = 0;
            scope.displayFooter = scope.tableFooter;
            scope.loadingTable = true;
            scope.clickRow = function () {
                scope.grouping.$hideRows = false;
            };
            scope.showTotal = function () {
                scope.totalShown = 1;
                return "Total :";
            };
            scope.initTotalPrint = function () {
                scope.totalShown = 0;
                return "";
            };
            scope.hideParent = function (grouping, hideStatus) {
                if (!grouping)
                    return;
                angular.forEach(grouping.data, function (value, key) {
                    if (hideStatus == false) {
                        value.$hideRows = hideStatus;
                        scope.hideParent(value, hideStatus);
                        scope.hideParent(value.data, false)
                    }
                    ;
                });
            };

            scope.hideChild = function (item, hideStatus) {
                if (!item)
                    return;
                angular.forEach(item, function (value, key) {
                    value.$hideRows = hideStatus;

                    if (hideStatus == false) {
                        scope.hideChild(value, hideStatus);
                    }
                });
            };

            scope.hideAll = function (grouping, hideStatus) {
                if (!grouping)
                    return;
                angular.forEach(grouping.data, function (value, key) {
                    value.$hideRows = hideStatus;
                    if (hideStatus == false) {
                        scope.hideAll(value, hideStatus);
                        scope.hideAll(value.data, false);
                    }
                });
            };
            scope.doSomething = function (ev) {
                var element = ev.srcElement ? ev.srcElement : ev.target;
            };
            scope.columns = [];
            angular.forEach(JSON.parse(scope.widgetColumns), function (value, key) {
                scope.columns.push(value);
            });

            scope.isZeroRow = function (row, col) {
                if (!widgetData.zeroSuppression || widgetData.zeroSuppression == false) {
                    return false;
                }
                var zeroRow = true;
                angular.forEach(col, function (value, key) {
                    var fieldName = value.fieldName;
                    var fieldValue = Number(row[fieldName]);
                    if (!isNaN(fieldValue) && fieldValue != 0) {
                        zeroRow = false;
                        return zeroRow;
                    }
                });
                return zeroRow;
            };

            scope.format = function (column, value) {
                if (!value) {
                    return "-";
                }
                if (column.displayFormat) {
//                    if (isNaN(value)) {
//                        return "aa-";
//                    }
                    var columnValue = dashboardFormat(column, value);
                    if (columnValue == 'NaN') {
                        columnValue = "-";
                    }
                    return columnValue;
                }
                return value;
            };

            scope.getStars = function (rating) {
                var val = parseFloat(rating);
                var size = val / 5 * 100;
                return size + '%';
            };

            function sortByDay(list, sortFields) {
                var returnSortDay;
                var dateOrders = ["Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"];
                angular.forEach(sortFields, function (value, key) {
                    returnSortDay = orderByFilter(list, function (item) {
                        if (value.sortOrder === 'asc') {
                            return dateOrders.indexOf(item[value.fieldName]);
                        } else if (value.sortOrder === 'desc') {
                            return dateOrders.indexOf(item[value.fieldName]) * -1;
                        }
                    });
                });
                return returnSortDay;
            }

            var groupByFields = []; // ['device', 'campaignName'];
            var aggreagtionList = [];
            var sortFields = [];
            for (var i = 0; i < scope.columns.length; i++) {
                if (scope.columns[i].groupPriority) {
                    groupByFields.push(scope.columns[i].fieldName);
                }
                if (scope.columns[i].agregationFunction) {
                    aggreagtionList.push({fieldname: scope.columns[i].fieldName, aggregationType: scope.columns[i].agregationFunction});
                }
                if (scope.columns[i].sortOrder) {
                    sortFields.push({fieldName: scope.columns[i].fieldName, sortOrder: scope.columns[i].sortOrder, fieldType: scope.columns[i].fieldType});
                }
            }
            var fullAggreagtionList = aggreagtionList;
            var tableDataSource = JSON.parse(scope.dynamicTableSource);
            var data = {
                url: '../dbApi/admin/dataSet/getData',
                connectionUrl: tableDataSource.dataSourceId.connectionString,
                startDate: $stateParams.startDate,
                endDate: $stateParams.endDate,
                username: tableDataSource.dataSourceId.userName,
                password: tableDataSource.dataSourceId.password,
                query: "select * from data ",
                port: 3306,
                schema: 'vb'
            };
            var url = "admin/proxy/getData?";
//            if (tableDataSource.dataSourceId.dataSourceType == "sql") {
//                url = "admin/proxy/getJson?url=../dbApi/admin/dataSet/getData&";
//            }

            var dataSourcePassword;
            if (tableDataSource.dataSourceId.password) {
                dataSourcePassword = tableDataSource.dataSourceId.password;
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

            scope.refreshTable = function () {
                $http.get(url + 'connectionUrl=' + tableDataSource.dataSourceId.connectionString +
                        "&dataSetId=" + tableDataSource.id +
                        "&accountId=" + (widgetData.accountId ? (widgetData.accountId.id ? widgetData.accountId.id : widgetData.accountId) : $stateParams.accountId) +
                        "&userId=" + (tableDataSource.userId ? tableDataSource.userId.id : null) +
                        "&driver=" + tableDataSource.dataSourceId.sqlDriver +
                        "&productSegment=" + setProductSegment +
                        "&timeSegment=" + setTimeSegment +
                        "&networkType=" + setNetworkType +
                        "&startDate=" + $stateParams.startDate +
                        "&endDate=" + $stateParams.endDate +
                        '&username=' + tableDataSource.dataSourceId.userName +
                        "&dataSetReportName=" + tableDataSource.reportName +
                        '&password=' + dataSourcePassword +
                        '&widgetId=' + scope.widgetId +
                        '&url=' + tableDataSource.url +
                        '&port=3306&schema=vb&query=' + encodeURI(tableDataSource.query)).success(function (response) {
                    scope.ajaxLoadingCompleted = true;
                    scope.loadingTable = false;
                    if (!response) {
                        scope.tableEmptyMessage = "No Data Found";
                        scope.hideEmptyTable = true;
                        return;
                    }
                    var pdfData = {};
                    if (response.data.length === 0) {
                        scope.tableEmptyMessage = "No Data Found";
                        scope.hideEmptyTable = true;
                        pdfData[scope.widgetId] = "No Data Found";
                    } else {
                        var responseData = response.data;
                        scope.orignalData = response.data;
                        pdfData[scope.widgetId] = scope.orignalData;
                        angular.forEach(sortFields, function (value, key) {
                            if (value.fieldType != 'day') {
                                responseData = scope.orderData(responseData, sortFields);

                            } else {
                                responseData = sortByDay(responseData, sortFields)//   
                            }
                        });
                        if (widgetData.maxRecord > 0) {
                            responseData = responseData.slice(0, widgetData.maxRecord);
                        }

                        if (groupByFields && groupByFields.length > 0) {
                            scope.groupingName = groupByFields;
                            groupedData = scope.group(responseData, groupByFields, aggreagtionList);
                            var dataToPush = {};
                            dataToPush = angular.extend(dataToPush, aggregate(responseData, fullAggreagtionList));
                            dataToPush.data = groupedData;
                            scope.groupingData = dataToPush;
                        } else {
                            var dataToPush = {};
                            dataToPush = angular.extend(dataToPush, aggregate(responseData, fullAggreagtionList));
                            dataToPush.data = responseData;
                            scope.groupingData = dataToPush;
                        }
                    }
                });
            };
            scope.setTableChartFn({tableFn: scope.refreshTable});
            scope.refreshTable();
            scope.initData = function (col) {
                angular.forEach(scope.columns, function (value, key) {
                    if (value.fieldName != col.fieldName) {
                        value.sortOrder = "";
                    }
                });
                if (col.sortOrder == "asc") {
                    col.sortOrder = "desc";
                } else {
                    col.sortOrder = "asc";
                }
                var sortFields = [];
                sortFields.push({fieldName: col.fieldName, sortOrder: col.sortOrder, fieldType: col.fieldType});
                var responseData = scope.orignalData;

                angular.forEach(sortFields, function (value, key) {
                    if (value.fieldType != 'day') {
                        responseData = scope.orderData(responseData, sortFields);
                    } else {
                        responseData = sortByDay(responseData, sortFields)
                    }
                });

                if (widgetData.maxRecord > 0) {
                    responseData = responseData.slice(0, widgetData.maxRecord);
                }

                if (groupByFields && groupByFields.length > 0) {
                    scope.groupingName = groupByFields;
                    groupedData = scope.group(responseData, groupByFields, aggreagtionList);
                    var dataToPush = {};
                    dataToPush = angular.extend(dataToPush, aggregate(responseData, fullAggreagtionList));
                    dataToPush.data = groupedData;
                    scope.groupingData = dataToPush;
                } else {
                    var dataToPush = {};
                    dataToPush = angular.extend(dataToPush, aggregate(responseData, fullAggreagtionList));
                    dataToPush.data = responseData;
                    scope.groupingData = dataToPush;
                }
            };

            scope.sortColumn = scope.columns;
            scope.objectHeader = [];
            scope.reverse = false;
            scope.toggleSort = function (index) {
                if (scope.sortColumn === scope.objectHeader[index]) {
                    scope.reverse = !scope.reverse;
                }
                scope.sortColumn = scope.objectHeader[index];
            };

            scope.pageChangeHandler = function (num) {
            };

            scope.sum = function (list, fieldname) {
                var sum = 0;
                for (var i in list)
                {
                    if (isNaN(list[i][fieldname])) {

                    } else {

                        sum = sum + Number(list[i][fieldname]);
                    }
                }
                return sum;
            };

            scope.calculatedMetric = function (list, name, field1, field2) {
                var value1 = scope.sum(list, field1);
                var value2 = scope.sum(list, field2);
                var returnValue = "0";
                if (value1 && value2) {
                    returnValue = value1 / value2;
                }
                if (name == "ctr") {
                }
                return returnValue;
            };

            listOfCalculatedFunction = [
                {name: 'ctr', field1: 'clicks', field2: 'impressions'},
                {name: 'cpa', field1: 'cost', field2: 'conversions'},
                {name: 'cpas', field1: 'spend', field2: 'conversions'},
                {name: 'cpc', field1: 'spend', field2: 'clicks'},
                {name: 'cpcs', field1: 'spend', field2: 'clicks'},
                {name: 'cpr', field1: 'spend', field2: 'actions_post_reaction'},
                {name: 'ctl', field1: 'spend', field2: 'actions_like'},
                {name: 'cplc', field1: 'spend', field2: 'actions_link_click'},
                {name: 'cpcomment', field1: 'spend', field2: 'actions_comment'},
                {name: 'cposte', field1: 'spend', field2: 'actions_post_engagement'},
                {name: 'cpagee', field1: 'spend', field2: 'actions_page_engagement'},
                {name: 'cpp', field1: 'spend', field2: 'actions_post'}
            ];

            function aggregate(list, aggreationList) {
                var returnValue = {};
                angular.forEach(aggreationList, function (value, key) {
                    if (value.aggregationType == "sum") {
                        returnValue[value.fieldname] = scope.sum(list, value.fieldname);
                    }
                    if (value.aggregationType == "avg") {
                        returnValue[value.fieldname] = scope.sum(list, value.fieldname) / list.length;
                    }
                    if (value.aggregationType == "count") {
                        returnValue[value.fieldname] = list.length;
                    }
                    if (value.aggregationType == "min") {
                        returnValue[value.fieldname] = Math.min.apply(Math, list.map(function (currentValue) {
                            return Number(currentValue[value.fieldname]);
                        }));
                    }
                    if (value.aggregationType == "max") {
                        returnValue[value.fieldname] = Math.max.apply(Math, list.map(function (currentValue) {
                            return Number(currentValue[value.fieldname]);
                        }));
                    }
                    angular.forEach(listOfCalculatedFunction, function (calculatedFn, key) {
                        if (calculatedFn.name == value.aggregationType) {
                            returnValue[value.fieldname] = scope.calculatedMetric(list, calculatedFn.name, calculatedFn.field1, calculatedFn.field2);
                        }
                    });
                    if (isNaN(returnValue[value.fieldname])) {
                        returnValue[value.fieldname] = "-";
                    }
                });
                return returnValue;
            }

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
            scope.group = function (list, fieldnames, aggreationList) {
                var currentFields = fieldnames;
                if (fieldnames.length == 0)
                    return list;
                var actualList = list;
                var data = [];
                var groupingField = currentFields[0];
                var currentListGrouped = $filter('groupBy')(actualList, groupingField);
                var currentFields = currentFields.splice(1);
                angular.forEach(currentListGrouped, function (value1, key1) {
                    var dataToPush = {};
                    dataToPush._key = key1;
                    dataToPush[groupingField] = key1;
                    dataToPush._groupField = groupingField;
                    dataToPush = angular.extend(dataToPush, aggregate(value1, fullAggreagtionList));
                    dataToPush.data = scope.group(value1, currentFields, aggreationList);
                    data.push(dataToPush);
                });
                return data;
            };
        }
    };
});
