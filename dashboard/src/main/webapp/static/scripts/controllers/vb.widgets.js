app.controller('WidgetController', function ($scope, $http, $stateParams, $timeout, $filter, localStorageService, $state, $window) {

    $scope.widgets = []
    $scope.dragEnabled = true;
    $scope.toggleDragging = function () {
        $scope.dragEnabled = !$scope.dragEnabled;
    };

    $http.get("admin/report/reportWidget").success(function (response) {
        $scope.reportWidgets = response;
    });

    $http.get('admin/report/getReport').success(function (response) {
        $scope.reportList = response;
    });

    $scope.tags = [];
    $http.get('admin/tag').success(function (response) {
        $scope.tags = response;
    });

    $scope.permission = localStorageService.get("permission");
    $scope.accountID = $stateParams.accountId;
    $scope.accountName = $stateParams.accountName;
    $scope.productID = $stateParams.productId;
    $scope.widgetTabId = $stateParams.tabId;
    $scope.widgetStartDate = $stateParams.startDate;
    $scope.widgetEndDate = $stateParams.endDate;

    if ($scope.permission.createReport === true) {
        $scope.showCreateReport = true;
    } else {
        $scope.showCreateReport = false;
    }

    $scope.downloadPdf = function () {
        var url = "admin/proxy/download/" + $stateParams.tabId + "?accountId=" + $stateParams.accountId + "&productId=" + $stateParams.productId + "&startDate=" + $stateParams.startDate + "&endDate=" + $stateParams.endDate + "&exportType=pdf";
        $window.open(url);
    };

    $scope.downloadPpt = function () {
        var url = "admin/proxy/download/" + $stateParams.tabId + "?accountId=" + $stateParams.accountId + "&productId=" + $stateParams.productId + "&startDate=" + $stateParams.startDate + "&endDate=" + $stateParams.endDate + "&exportType=ppt";
        $window.open(url);
    };

    function getWidgetItem() {      //Default Loading Items
        if (!$stateParams.tabId) {
            $stateParams.tabId = 0;
        }
        $http.get("admin/ui/dbWidget/" + $stateParams.tabId).success(function (response) {
            var widgetItems = [];
            console.log(response);
            widgetItems = response;
            $http.get("admin/tag/getAllFav/").success(function (favResponse) {
                widgetItems.forEach(function (value, key) {
                    favWidget = $.grep(favResponse, function (b) {
                        return b.id === value.id;
                    });
                    if (favWidget.length > 0) {
                        value.isFav = true;
                    } else {
                        value.isFav = false;
                    }
                });
            })
//            widgetItems.forEach(function (value, key) {
//                $http.get("admin/tag/widgetTag/" + value.id).success(function (response) {
//                    console.log(response)
//                    if (response.length == 0) {
//                        var tagsList = $scope.tags[0]
//                        console.log($scope.tags[0])
//                        tagsList.status = 'InActive';
//                        value.tags = tagsList;
//                        console.log(value)
//                    }
//                    response.forEach(function (val, k) {
//                        if (value.id == val.widgetId.id) {
//                            val.tagId.status = val.status ? val.status : 'InActive';
//                            value.tags = val.tagId;
//                        }
//                    });
//                });
//            });
            $scope.widgets = widgetItems;
        });
    }
    getWidgetItem();

    $scope.addWidget = function (newWidget) {       //Add Widget
//        var data = {
//            width: newWidget, 'minHeight': 25, columns: [], chartType: ""
//        };
//        $http({method: 'POST', url: 'admin/ui/dbWidget/' + $stateParams.tabId, data: data}).success(function (response) {
        $state.go("index.editWidget", {
            productId: $stateParams.productId,
            accountId: $stateParams.accountId,
            accountName: $stateParams.accountName,
            tabId: $stateParams.tabId,
            widgetId: 0, //response.id,
            startDate: $stateParams.startDate,
            endDate: $stateParams.endDate
        });
//        });
    };
    $scope.removeBackDrop = function () {
        $('body').removeClass().removeAttr('style');
        $('.modal-backdrop').remove();
        $state.go('index.dataSource', {accountId: $stateParams.accountId, accountName: $stateParams.accountName, startDate: $stateParams.startDate, endDate: $stateParams.endDate})
    };

    $scope.deleteWidget = function (widget, index) {                            //Delete Widget
        $http({method: 'DELETE', url: 'admin/ui/dbWidget/' + widget.id}).success(function (response) {
            $scope.widgets.splice(index, 1);
        });
    };

    $scope.widgetDuplicate = function (widgetData) {
        console.log(widgetData);
        console.log(widgetData.widgetId + " : " + widgetData.tabId);
        $http.get("admin/ui/dbWidgetDuplicate/" + widgetData.widgetId + "/" + widgetData.tabId).success(function (response) {
            console.log(response);
            $http.get("admin/ui/dbDuplicateTag/" + response.id).success(function (dataTag) {
                response["tags"] = dataTag[0];
                $scope.widgets.push(response);
                console.log($scope.widgets);
            });
        });
    }

    $scope.pageRefresh = function () {          //Page Refresh
        getWidgetItem();
    };
    $scope.moveWidget = function (list, from, to) {
        list.splice(to, 0, list.splice(from, 1)[0]);
        return list;
    };

    $scope.addWidgetToReport = function (widget) {
        var data = {};
        data.widgetId = widget.id;
        data.reportId = widget.reportWidget.id;
        $http({method: widget.id ? 'PUT' : 'POST', url: 'admin/report/reportWidget', data: data}).success(function (response) {
        });
        $scope.reportLogo = "";
        $scope.reportDescription = "";
        $scope.reportWidgetTitle = "";
        $scope.showReportWidgetName = false;
        $scope.showReportEmptyMessage = false;
    };

    $scope.clearReport = function () {
        $scope.reportLogo = "";
        $scope.reportDescription = "";
        $scope.reportWidgetTitle = "";
        $scope.showReportWidgetName = false;
    };
    $scope.favourites = false;

    $scope.toggleFavourite = function (widget) {
        var isFav = widget.isFav;
        if (isFav) {
            $http({method: 'POST', url: "admin/tag/removeFav/" + widget.id});
            widget.isFav = false;
        } else {
            $http({method: 'POST', url: "admin/tag/setFav/" + widget.id});
            widget.isFav = true;
        }
        console.log(widget)
    }

//    $scope.favouritesNew = function (favourites, widget) {
//        alert(widget.id);
//        console.log(widget.id);
//        console.log(favourites);
//        console.log(widget);
//        console.log(widget.tags.status);
//        console.log(widget.tags.tagName);
//
//        if (favourites === true) {
//            alert("active")
//            widget.tags.status = "Active";
//        } else {
//            alert("inactive")
//            widget.tags.status = "InActive";
//        }
//        var tagData = {
//            tagName: widget.tags.tagName,
//            widgetId: widget.id,
//            status: widget.tags.status
//        }
//        $http({method: 'POST', url: "admin/tag/selectedTag", data: tagData});
//    };


    $scope.goReport = function () {
        $state.go('index.report.reports', {accountId: $stateParams.accountId, accountName: $stateParams.accountName, startDate: $stateParams.startDate, endDate: $stateParams.endDate});
    };

    $scope.onDropComplete = function (index, widget, evt) {

        if (widget !== "" && widget !== null) {
            var otherObj = $scope.widgets[index];
            var otherIndex = $scope.widgets.indexOf(widget);
//            $scope.widgets.move(otherIndex, index);
            $scope.widgets = $scope.moveWidget($scope.widgets, otherIndex, index);
//            $scope.widgets[index] = widget;
//            $scope.widgets[otherIndex] = otherObj;
            var widgetOrder = $scope.widgets.map(function (value, key) {
                if (value) {
                    return value.id;
                }
            }).join(',');
            var data = {widgetOrder: widgetOrder};
            if (widgetOrder) {
                $http({method: 'GET', url: 'admin/ui/dbWidgetUpdateOrder/' + $stateParams.tabId + "?widgetOrder=" + widgetOrder});
            }
        }
    };

    function splitCamelCase(s) {
        return s.split(/(?=[A-Z])/).join(' ');
    }

    function makeUnselectable(node) {
        if (node.nodeType == 1) {
            node.setAttribute("unselectable", "on");
        }
        var child = node.firstChild;
        while (child) {
            makeUnselectable(child);
            child = child.nextSibling;
        }
    }

    $scope.showReportWidgetName = false;
    $scope.selectReport = function (reportWidget) {
        $scope.showReportWidgetName = false;
        $scope.reportWidgetTitle = []
        $scope.reportLogo = reportWidget.logo;
        $scope.reportDescription = reportWidget.description;
        $http.get("admin/report/reportWidget/" + reportWidget.id + "?locationId=" + $stateParams.accountId).success(function (response) {
            if (response.length > 0) {
                $scope.showReportWidgetName = true;
                $scope.reportWidgetTitle = response;
                $scope.showReportEmptyMessage = false;
            } else {
                $scope.showReportEmptyMessage = true;
                $scope.reportEmptyMessage = "No Data Found"
            }
        });
    };

    $scope.setLineChartFn = function (lineFn) {
        $scope.directiveLineFn = lineFn;
    };
    $scope.setAreaChartFn = function (areaFn) {
        $scope.directiveAreaFn = areaFn;
    };
    $scope.setBarChartFn = function (barFn) {
        $scope.directiveBarFn = barFn;
    };
    $scope.setPieChartFn = function (pieFn) {
        $scope.directivePieFn = pieFn;
    };
    $scope.setStackedBarChartFn = function (stackedBarChartFn) {
        $scope.directiveStackedBarChartFn = stackedBarChartFn;
    };
    $scope.setTableChartFn = function (tableFn) {
        $scope.directiveTableFn = tableFn;
    };
    $scope.setTickerFn = function (tickerFn) {
        $scope.directiveTickerFn = tickerFn;
    };
    $scope.expandWidget = function (widget) {
        var expandchart = widget.chartType;
        widget.chartType = null;
        if (widget.width == 3) {
            widget.width = widget.width + 1;
        } else if (widget.width == 4) {
            widget.width = widget.width + 2;
        } else if (widget.width == 6) {
            widget.width = widget.width + 2;
        } else {
            widget.width = 12;
        }
        $timeout(function () {
            widget.chartType = expandchart;
            var data = {
                id: widget.id,
                chartType: widget.chartType,
                widgetTitle: widget.widgetTitle,
                widgetColumns: widget.columns,
                dataSourceId: widget.dataSourceId.id,
                dataSetId: widget.dataSetId.id,
                tableFooter: widget.tableFooter,
                zeroSuppression: widget.zeroSuppression,
                maxRecord: widget.maxRecord,
                dateDuration: widget.dateDuration,
                content: widget.content,
                width: widget.width,
                jsonData:widget.jsonData,
                queryFilter:widget.queryFilter
            };

            $http({method: widget.id ? 'PUT' : 'POST', url: 'admin/ui/dbWidget/' + $stateParams.tabId, data: data}).success(function (response) {
            });
        }, 50);

    };
    $scope.reduceWidget = function (widget) {
        var expandchart = widget.chartType;
        widget.chartType = null;

        if (widget.width == 12) {
            widget.width = widget.width - 4;
        } else if (widget.width == 8) {
            widget.width = widget.width - 2;
        } else if (widget.width == 6) {
            widget.width = widget.width - 2;
        } else {
            widget.width = 3;
        }

        $timeout(function () {
            widget.chartType = expandchart;
            var data = {
                id: widget.id,
                chartType: widget.chartType,
                widgetTitle: widget.widgetTitle,
                widgetColumns: widget.columns,
                dataSourceId: widget.dataSourceId.id,
                dataSetId: widget.dataSetId.id,
                tableFooter: widget.tableFooter,
                zeroSuppression: widget.zeroSuppression,
                maxRecord: widget.maxRecord,
                dateDuration: widget.dateDuration,
                content: widget.content,
                width: widget.width,
                jsonData:widget.jsonData,
                queryFilter:widget.queryFilter
            };

            $http({method: widget.id ? 'PUT' : 'POST', url: 'admin/ui/dbWidget/' + $stateParams.tabId, data: data}).success(function (response) {
            });
        }, 50);

    }
});


app.directive('dateRangePicker', function () {
    return{
        restrict: 'A',
//        template: '<input type="text" name="daterange" value="01/01/2015 1:30 PM - 01/01/2015 2:00 PM" />',
//        template: '<div id="reportrange" class="pull-right" style="background: #fff; cursor: pointer; padding: 5px 10px; border: 1px solid #ccc; width: 100%">' +
//                '<i class="glyphicon glyphicon-calendar fa fa-calendar"></i>&nbsp;' +
//                '<span></span> <b class="caret"></b>' + '</div>',
        link: function (scope, element, attr) {
            $(function () {
                $('input[name="daterange"]').daterangepicker({
                    // timePicker: true,
                    //timePickerIncrement: 30,
                    locale: {
                        format: 'MM/DD/YYYY'
                    }
                });
            });
        }
    };
});

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
                '<div ng-click="initData(col)" class="">{{col.displayName}}' +
//                '<div ng-click="initData(col)" class="text-{{col.alignment}}">{{col.displayName}}' +
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
                return "Total :"
            }
            scope.initTotalPrint = function () {
                scope.totalShown = 0;
                return "";
            }
            scope.hideParent = function (grouping, hideStatus) {
                if (!grouping)
                    return;
                angular.forEach(grouping.data, function (value, key) {
                    if (hideStatus == false) {
                        value.$hideRows = hideStatus;
                        scope.hideParent(value, hideStatus);
                        scope.hideParent(value.data, false)
                    }
                    // scope.hideChild(value.data, false)
                    //value.data.$hideRows = true;
                });
            };

            scope.hideChild = function (item, hideStatus) {
                // console.log(item);
                if (!item)
                    return;
                angular.forEach(item, function (value, key) {
                    value.$hideRows = hideStatus;

                    if (hideStatus == false) {
                        scope.hideChild(value, hideStatus);
                    }
                    //value.data.$hideRows = true;
                });
            };

            scope.hideAll = function (grouping, hideStatus) {
                if (!grouping)
                    return;
                angular.forEach(grouping.data, function (value, key) {
                    value.$hideRows = hideStatus;
//                    if (value.data)
//                        return;
                    if (hideStatus == false) {
                        scope.hideAll(value, hideStatus);
                        scope.hideAll(value.data, false)
                    }
                });
            };
            scope.doSomething = function (ev) {
                var element = ev.srcElement ? ev.srcElement : ev.target;
            };

            //scope.currentPage = 1;
            //scope.pageSize = 10;
            // console.log
            scope.columns = [];
            angular.forEach(JSON.parse(scope.widgetColumns), function (value, key) {
                scope.columns.push(value);
            });

            scope.isZeroRow = function (row, col) {
                var widgetData = JSON.parse(scope.widgetObj);
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
            }

            scope.format = function (column, value) {
                if (!value) {
                    return "-";
                }
                if (column.displayFormat) {
                    if (Number.isNaN(value)) {
                        return "-";
                    }
                    if (column.displayFormat.indexOf("%") > -1) {
                        return d3.format(column.displayFormat)(value / 100);
                    } else if (column.displayFormat == 'H:M:S') {
                        return formatBySecond(parseInt(value))
                    } else {
                        return d3.format(column.displayFormat)(value);
                    }
                }
                return value;
            };

            scope.getStars = function (rating) {
//                console.log(rating);
                // Get the value
                var val = parseFloat(rating);
                // Turn value into number/100
                var size = val / 5 * 100;
//                console.log(size);
                return size + '%';
            }

            function sortByDay(list, sortFields) {
                var returnSortDay;
                var dateOrders = ["Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"];
                angular.forEach(sortFields, function (value, key) {
                    returnSortDay = orderByFilter(list, function (item) {
                        if (value.sortOrder === 'asc') {
                            return dateOrders.indexOf(item[value.fieldName]);
                        } else if (value.sortOrder === 'desc') {
                            return dateOrders.indexOf(item[value.fieldName] * -1);
                        }
                    });
                });
                return returnSortDay;
            }

            function formatBySecond(second) {
                var minutes = "0" + Math.floor(second / 60);
                var seconds = "0" + (second - minutes * 60);
                var hours = "0" + Math.floor(minutes / 60);
                return hours.substr(-2) + " : " + minutes.substr(-2) + " : " + seconds.substr(-2);
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
            var tableDataSource = JSON.parse(scope.dynamicTableSource)
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
            }
            var url = "admin/proxy/getData?";
            if (tableDataSource.dataSourceId.dataSourceType == "sql") {
                url = "admin/proxy/getJson?url=../dbApi/admin/dataSet/getData&";
            }

            var dataSourcePassword;
            if (tableDataSource.dataSourceId.password) {
                dataSourcePassword = tableDataSource.dataSourceId.password;
            } else {
                dataSourcePassword = '';
            }

            scope.refreshTable = function () {
                console.log(tableDataSource.id);
                console.log(scope.widgetId);
                console.log(tableDataSource);

                scope.connectionTestUrl = url + 'connectionUrl=' + tableDataSource.dataSourceId.connectionString +
                        "&dataSetId=" + tableDataSource.id +
                        "&accountId=" + $stateParams.accountId +
                        "&driver=" + tableDataSource.dataSourceId.sqlDriver +
                        "&location=" + $stateParams.locationId +
                        "&startDate=" + $stateParams.startDate +
                        "&endDate=" + $stateParams.endDate +
                        '&username=' + tableDataSource.dataSourceId.userName +
                        '&password=' + dataSourcePassword +
                        '&dataSetReportName=' + tableDataSource.reportName +
                        '&widgetId=' + scope.widgetId +
                        '&port=3306&schema=vb&query=' + encodeURI(tableDataSource.query);
                console.log("***************************************");
                console.log(scope.connectionTestUrl);


                $http.get(url + 'connectionUrl=' + tableDataSource.dataSourceId.connectionString +
                        "&dataSetId=" + tableDataSource.id +
                        "&accountId=" + $stateParams.accountId +
                        "&driver=" + tableDataSource.dataSourceId.sqlDriver +
                        "&location=" + $stateParams.locationId +
                        "&startDate=" + $stateParams.startDate +
                        "&endDate=" + $stateParams.endDate +
                        '&username=' + tableDataSource.dataSourceId.userName +
                        "&dataSetReportName=" + tableDataSource.reportName +
                        '&password=' + dataSourcePassword +
                        '&dataSetReportName=' + tableDataSource.reportName +
                        '&widgetId=' + scope.widgetId +
                        '&url=' + tableDataSource.url +
                        '&port=3306&schema=vb&query=' + encodeURI(tableDataSource.query)).success(function (response) {
                    scope.ajaxLoadingCompleted = true;
                    scope.loadingTable = false;
                    console.log(response);

                    if (!response.data) {
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
                        var widgetData = JSON.parse(scope.widgetObj);
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
            }
            scope.setTableChartFn({tableFn: scope.refreshTable});
            scope.refreshTable();
            scope.initData = function (col) {
                angular.forEach(scope.columns, function (value, key) {
                    if (value.fieldName != col.fieldName) {
                        value.sortOrder = "";
                    }
                })
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
                        console.log(responseData)
                    } else {
                        responseData = sortByDay(responseData, sortFields)
                    }
                });

                var widgetData = JSON.parse(scope.widgetObj);
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
//                    scope.groupingData = $sce.trustAsHtml(dataToPush);
                }
            }

            scope.sortColumn = scope.columns;
            scope.objectHeader = [];
            scope.reverse = false;
            scope.toggleSort = function (index) {
                if (scope.sortColumn === scope.objectHeader[index]) {
                    scope.reverse = !scope.reverse;
                }
                scope.sortColumn = scope.objectHeader[index];
            };

//Dir-Paginations
            scope.pageChangeHandler = function (num) {
                console.log('reports page changed to ' + num);
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
                    // returnValue = returnValue * 100;
                }
                return returnValue;
            };

            listOfCalculatedFunction = [
                {name: 'ctr', field1: 'clicks', field2: 'impressions'},
                {name: 'cpa', field1: 'cost', field2: 'conversions'},
                {name: 'cpas', field1: 'spend', field2: 'conversions'},
                {name: 'cpc', field1: 'cost', field2: 'clicks'},
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
                    if (Number.isNaN(returnValue)) {
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
                            //fieldsOrder.push(value.fieldname);
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
            }
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

app.directive('tickerDirective', function ($http, $stateParams) {
    return{
        restrict: 'AE',
        template: '<div ng-show="loadingTicker"><img width="40" src="static/img/logos/loader.gif"></div>' +
                '<div  ng-hide="loadingTicker" >' +
                '<div ng-hide="hideEmptyTicker" class="hpanel stats">' +
                '<div class="panel-body h-150">' +
                '<div class="stats-title pull-left">' +
                '<h4>{{tickerTitleName}}</h4>' +
                '</div>' +
                '<div class="stats-icon pull-right">' +
//                        '<i class="pe-7s-share fa-4x"></i>' +
                '</div>' +
                '<div class="m-t-xl">' +
                '<h3 class="m-b-xs text-success">{{firstLevelTicker.totalValue}}</h3>' +
                '<span class="font-bold no-margins">' +
//                            '{{firstLevelTicker.tickerTitle}}' +
                '</span>' +
                '<div class="row">' +
                '<div class="col-xs-6">' +
                '<small class="stats-label">{{secondLevelTicker.tickerTitle}}</small>' +
                '<h4>{{secondLevelTicker.totalValue}}</h4>' +
                '</div>' +
                '<div class="col-xs-6 count">' +
                '<small class="stats-label">{{thirdLevelTicker.tickerTitle}}</small>' +
                '<h4>{{thirdLevelTicker.totalValue}}</h4>' +
                '</div>' +
                '</div>' +
                '</div>' +
                '</div>' +
                '</div>' +
                '<div ng-show="hideEmptyTicker">{{tickerEmptyMessage}}</div>' +
                '</div>',
        scope: {
            setTickerFn: '&',
            tickerSource: '@',
            tickerId: '@',
            tickerColumns: '@',
            tickerTitleName: '@'
        },
        link: function (scope, element, attr) {
            scope.loadingTicker = true;
            var tickerName = [];
            angular.forEach(JSON.parse(scope.tickerColumns), function (value, key) {
                if (!value) {
                    return;
                }
                tickerName.push({fieldName: value.fieldName, displayName: value.displayName, displayFormat: value.displayFormat})
            });

            var format = function (column, value) {
                if (!value) {
                    return "-";
                }
                if (column.displayFormat) {
                    if (Number.isNaN(value)) {
                        return "-";
                    }
                    if (column.displayFormat.indexOf("%") > -1) {
                        return d3.format(column.displayFormat)(value / 100);
                    }
                    return d3.format(column.displayFormat)(value);
                }
                return value;
            };

            var setData = [];
            var data = [];
            var tickerDataSource = JSON.parse(scope.tickerSource);
            var url = "admin/proxy/getData?";
            if (tickerDataSource.dataSourceId.dataSourceType == "sql") {
                url = "admin/proxy/getJson?url=../dbApi/admin/dataSet/getData&";
            }
            var dataSourcePassword;
            if (tickerDataSource.dataSourceId.password) {
                dataSourcePassword = tickerDataSource.dataSourceId.password;
            } else {
                dataSourcePassword = '';
            }
            scope.refreshTicker = function () {
                console.log("ticker --- > " + scope.tickerId);
                $http.get(url + 'connectionUrl=' + tickerDataSource.dataSourceId.connectionString +
                        "&dataSetId=" + tickerDataSource.id +
                        "&accountId=" + $stateParams.accountId +
                        "&driver=" + tickerDataSource.dataSourceId.sqlDriver +
                        "&dataSetReportName=" + tickerDataSource.reportName +
                        "&location=" + $stateParams.locationId +
                        "&startDate=" + $stateParams.startDate +
                        "&endDate=" + $stateParams.endDate +
                        '&username=' + tickerDataSource.dataSourceId.userName +
                        '&password=' + dataSourcePassword +
                        '&widgetId=' + scope.tickerId +
                        '&url=' + tickerDataSource.url +
                        '&port=3306&schema=vb&query=' + encodeURI(tickerDataSource.query)).success(function (response) {
                    scope.tickers = [];
                    scope.loadingTicker = false;
                    if (response.length === 0) {
                        scope.tickerEmptyMessage = "No Data Found";
                        scope.hideEmptyTicker = true;
                    } else {
                        if (!response) {
                            return;
                        }
                        angular.forEach(tickerName, function (value, key) {
                            var tickerData = response.data;
                            var loopCount = 0;
                            data = [value.fieldName];
                            setData = tickerData.map(function (a) {
                                data.push(loopCount);
                                loopCount++;
                                return a[value.fieldName];
                            });
                            var total = 0;
                            for (var i = 0; i < setData.length; i++) {
                                total += parseFloat(setData[i]);
                            }
                            scope.tickers.push({tickerTitle: value.displayName, totalValue: format(value, total)});
                        });
                    }
                    scope.firstLevelTicker = scope.tickers[0];
                    scope.secondLevelTicker = scope.tickers[1];
                    scope.thirdLevelTicker = scope.tickers[2];
                });
            }
            scope.setTickerFn({tickerFn: scope.refreshTicker});
            scope.refreshTicker();
        }
    };
});

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
            lineChartId: '@',
            widgetObj: '@'
        },
        link: function (scope, element, attr) {
            var labels = {format: {}};
            scope.loadingLine = true;
            var yAxis = [];
            var columns = [];
            var xAxis;
            var ySeriesOrder = 1;
            var sortField = "";
            var sortOrder = 0;
            var sortDataType = "number";
            var displayDataFormat = {};
            var y2 = {show: false, label: ''};
            var axes = {};
            var startDate = "";
            var endDate = "";
            var sortFields = [];
            var combinationTypes = [];
            var chartCombinationtypes = [];
            function formatBySecond(second) {
                var minutes = "0" + Math.floor(second / 60);
                var seconds = "0" + (second - minutes * 60);
                var hours = "0" + Math.floor(minutes / 60);
                return hours.substr(-2) + " : " + minutes.substr(-2) + " : " + seconds.substr(-2);
            }

            angular.forEach(JSON.parse(scope.widgetColumns), function (value, key) {
                if (!labels["format"]) {
                    labels = {format: {}};
                }
                if (value.displayFormat) {
                    var format = value.displayFormat;
                    var displayName = value.displayName;

                    if (value.displayFormat && value.displayFormat != 'H:M:S') {
                        labels["format"][displayName] = function (value) {
                            // alert(format);
                            if (format.indexOf("%") > -1) {
                                return d3.format(format)(value / 100);
                            }
                            return d3.format(format)(value);
                        };
                    } else {
                        labels["format"][displayName] = function (value) {
                            return formatBySecond(parseInt(value))
                            console.log(formatBySecond(parseInt(value)))
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
                    combinationTypes.push({fieldName: value.fieldName, combinationType: value.combinationType});
                }
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
                            //fieldsOrder.push(value.fieldname);
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
            }
            function maximumRecord(maxValue, list) {
                var maxData;
                if (maxValue.maxRecord > 0) {
                    maxData = list.slice(0, maxValue.maxRecord);
                }
                return maxData;
            }
            var lineChartDataSource = JSON.parse(scope.lineChartSource);
            if (scope.lineChartSource) {

                var url = "admin/proxy/getData?";
                if (lineChartDataSource.dataSourceId.dataSourceType == "sql") {
                    url = "admin/proxy/getJson?url=../dbApi/admin/dataSet/getData&";
                }
                var dataSourcePassword;
                if (lineChartDataSource.dataSourceId.password) {
                    dataSourcePassword = lineChartDataSource.dataSourceId.password;
                } else {
                    dataSourcePassword = '';
                }
                scope.refreshLineChart = function () {
                    console.log("line --- > " + scope.widgetId);
                    $http.get(url + 'connectionUrl=' + lineChartDataSource.dataSourceId.connectionString +
                            "&dataSetId=" + lineChartDataSource.id +
                            "&accountId=" + $stateParams.accountId +
                            "&driver=" + lineChartDataSource.dataSourceId.sqlDriver +
                            "&location=" + $stateParams.locationId +
                            "&startDate=" + $stateParams.startDate +
                            "&endDate=" + $stateParams.endDate +
                            '&username=' + lineChartDataSource.dataSourceId.userName +
                            '&password=' + dataSourcePassword +
                            "&dataSetReportName=" + lineChartDataSource.reportName +
                            '&widgetId=' + scope.widgetId +
                            '&url=' + lineChartDataSource.url +
                            '&port=3306&schema=vb&query=' + encodeURI(lineChartDataSource.query)).success(function (response) {
                        scope.loadingLine = false;
                        if (!response.data) {
                            return;
                        }
                        if (response.data.length === 0) {
                            scope.lineEmptyMessage = "No Data Found";
                            scope.hideEmptyLine = true;
                        } else {
                            var loopCount = 0;
                            var sortingObj;
                            var gridData = JSON.parse(scope.widgetObj);
                            var chartMaxRecord = JSON.parse(scope.widgetObj)
                            var chartData = response.data;
                            if (sortFields.length > 0) {
                                angular.forEach(sortFields, function (value, key) {
                                    if (value.fieldType != 'day') {
//                                    chartData = scope.orderData(chartData, sortFields);
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
                                                return dateOrders.indexOf(item[value.fieldName] * -1);
                                            }
                                        });

                                        if (chartMaxRecord.maxRecord) {
                                            chartData = maximumRecord(chartMaxRecord, sortingObj)
                                        } else {
                                            chartData = sortingObj;
                                        }
                                    }
                                });
                                // chartData = scope.orderData(chartData, sortFields);
                            }
                            if (chartMaxRecord.maxRecord > 0) {
                                chartData = chartData.slice(0, chartMaxRecord.maxRecord);
                            }
                            xTicks = [xAxis.fieldName];
                            xData = chartData.map(function (a) {
                                xTicks.push(loopCount);
                                loopCount++;
                                return a[xAxis.fieldName];
                            });
                            columns.push(xTicks);

                            angular.forEach(yAxis, function (value, key) {
                                ySeriesData = chartData.map(function (a) {
                                    return a[value.fieldName] || "0";
                                });
                                ySeriesData.unshift(value.displayName);
                                columns.push(ySeriesData);
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


                            console.log(gridLine)
                            var chart = c3.generate({
                                bindto: element[0],
                                data: {
                                    x: xAxis.fieldName,
                                    columns: columns,
                                    labels: labels,
                                    axes: axes,
                                    types: chartCombinationtypes
                                },
                                color: {
                                    pattern: ['#62cb31', '#555555']

                                },
                                tooltip: {show: false},
                                axis: {
                                    x: {
                                        tick: {
                                            format: function (x) {
                                                return xData[x];
                                            }
                                        }
                                    },
                                    y2: y2
                                },
                                grid: {
                                    x: {
                                        show: gridLine
                                    },
                                    y: {
                                        show: gridLine
                                    }
                                }
                            });
                        }
                    });
                }
                scope.setLineChartFn({lineFn: scope.refreshLineChart});
                scope.refreshLineChart();
            }
        }
    };
});

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
            widgetObj: '@'
        },
        link: function (scope, element, attr) {
            console.log(scope.widgetObj)
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
            angular.forEach(JSON.parse(scope.widgetColumns), function (value, key) {
                if (!labels["format"]) {
                    labels = {format: {}};
                }
                if (value.displayFormat) {
                    var format = value.displayFormat;
                    var displayName = value.displayName;

                    if (value.displayFormat && value.displayFormat != 'H:M:S') {
                        labels["format"][displayName] = function (value) {
                            // alert(format);
                            if (format.indexOf("%") > -1) {
                                return d3.format(format)(value / 100);
                            }
                            return d3.format(format)(value);
                        };
                    } else {
                        labels["format"][displayName] = function (value) {
                            return formatBySecond(parseInt(value))
                            console.log(formatBySecond(parseInt(value)))
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
                    combinationTypes.push({fieldName: value.fieldName, combinationType: value.combinationType});
                }
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
                            //fieldsOrder.push(value.fieldname);
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
            }

            function maximumRecord(maxValue, list) {
                var maxData;
                if (maxValue.maxRecord > 0) {
                    maxData = list.slice(0, maxValue.maxRecord);
                }
                return maxData;
            }

            var barChartDataSource = JSON.parse(scope.barChartSource);
            if (scope.barChartSource) {

                var url = "admin/proxy/getData?";
                if (barChartDataSource.dataSourceId.dataSourceType == "sql") {
                    url = "admin/proxy/getJson?url=../dbApi/admin/dataSet/getData&";
                }
                var dataSourcePassword;
                if (barChartDataSource.dataSourceId.password) {
                    dataSourcePassword = barChartDataSource.dataSourceId.password;
                } else {
                    dataSourcePassword = '';
                }
                scope.refreshBarChart = function () {
                    console.log("bar -----> " + scope.widgetId);
                    $http.get(url + 'connectionUrl=' + barChartDataSource.dataSourceId.connectionString +
                            "&dataSetId=" + barChartDataSource.id +
                            "&accountId=" + $stateParams.accountId +
                            "&dataSetReportName=" + barChartDataSource.reportName +
                            "&driver=" + barChartDataSource.dataSourceId.sqlDriver +
                            "&location=" + $stateParams.locationId +
                            "&startDate=" + $stateParams.startDate +
                            "&endDate=" + $stateParams.endDate +
                            '&username=' + barChartDataSource.dataSourceId.userName +
                            '&password=' + dataSourcePassword +
                            '&widgetId=' + scope.widgetId +
                            '&url=' + barChartDataSource.url +
                            '&port=3306&schema=vb&query=' + encodeURI(barChartDataSource.query)).success(function (response) {
                        scope.loadingBar = false;
                        if (!response) {
                            return;
                        }
                        if (response.data.length === 0) {
                            scope.barEmptyMessage = "No Data Found";
                            scope.hideEmptyBar = true;
                        } else {
                            var loopCount = 0;
                            var sortingObj;
                            var gridData = JSON.parse(scope.widgetObj)
                            var chartMaxRecord = JSON.parse(scope.widgetObj)
                            var chartData = response.data;
                            if (sortFields.length > 0) {
                                angular.forEach(sortFields, function (value, key) {
                                    if (value.fieldType != 'day') {
//                                    chartData = scope.orderData(chartData, sortFields);
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
                                                return dateOrders.indexOf(item[value.fieldName] * -1);
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
//                        chartData = orderData(chartData, sortFields);
                            xTicks = [xAxis.fieldName];
                            xData = chartData.map(function (a) {
                                xTicks.push(loopCount);
                                loopCount++;
                                return a[xAxis.fieldName];
                            });

                            columns.push(xTicks);

                            angular.forEach(yAxis, function (value, key) {
                                ySeriesData = chartData.map(function (a) {
                                    return a[value.fieldName] || "0";
                                });
                                ySeriesData.unshift(value.displayName);
                                columns.push(ySeriesData);
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
                            var chart = c3.generate({
                                bindto: element[0],
                                data: {
                                    x: xAxis.fieldName,
                                    columns: columns,
                                    labels: labels,
                                    type: 'bar',
                                    axes: axes,
                                    types: chartCombinationtypes
                                },
                                color: {
                                    pattern: ['#62cb31', '#555555']

                                },
                                tooltip: {show: false},
                                axis: {
                                    x: {
                                        tick: {
                                            format: function (x) {
                                                return xData[x];
                                            }
                                        }
                                    },
                                    y2: y2
                                },
                                grid: {
                                    x: {
                                        show: gridLine
                                    },
                                    y: {
                                        show: gridLine
                                    }
                                }
                            });
                        }
                    });
                }
                scope.setBarChartFn({barFn: scope.refreshBarChart});
                scope.refreshBarChart();
            }
        }
    };
});
app.directive('pieChartDirective', function ($http, $stateParams, $filter, orderByFilter) {
    return{
        restrict: 'AC',
        template: '<div ng-show="loadingPie" class="text-center"><img src="static/img/logos/loader.gif" width="40"></div>' +
                '<div ng-show="hideEmptyPie" class="text-center">{{pieEmptyMessage}}</div>',
        scope: {
            setPieChartFn: '&',
            pieChartSource: '@',
            widgetId: '@',
            widgetColumns: '@',
            pieChartId: '@',
            loadingPie: '&',
            widgetObj: '@'
        },
        link: function (scope, element, attr) {
            var labels = {format: {}};
            scope.loadingPie = true;
            var yAxis = [];
            var columns = [];
            var xAxis;
            var ySeriesOrder = 1;
            var sortField = "";
            var sortOrder = 0;
            var displayDataFormat = {};
            var axes = {};
            var startDate = "";
            var endDate = "";
            var sortFields = [];
            angular.forEach(JSON.parse(scope.widgetColumns), function (value, key) {
                if (!labels["format"]) {
                    labels = {format: {}};
                }
                if (value.displayFormat) {
                    var format = value.displayFormat;
                    var displayName = value.displayName;

                    if (value.displayFormat && value.displayFormat != 'H:M:S') {
                        labels["format"][displayName] = function (value) {
                            // alert(format);
                            if (format.indexOf("%") > -1) {
                                return d3.format(format)(value / 100);
                            }
                            return d3.format(format)(value);
                        };
                    } else {
                        labels["format"][displayName] = function (value) {
                            return formatBySecond(parseInt(value))
                            console.log(formatBySecond(parseInt(value)))
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
                            //fieldsOrder.push(value.fieldname);
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
            var pieChartDataSource = JSON.parse(scope.pieChartSource);
            if (scope.pieChartSource) {
                var url = "admin/proxy/getData?";
                if (pieChartDataSource.dataSourceId.dataSourceType == "sql") {
                    url = "admin/proxy/getJson?url=../dbApi/admin/dataSet/getData&";
                }
                
                var dataSourcePassword;
                if (pieChartDataSource.dataSourceId.password) {
                    dataSourcePassword = pieChartDataSource.dataSourceId.password;
                } else {
                    dataSourcePassword = '';
                }

                scope.refreshPieChart = function () {
                    console.log("pie -----> " + scope.widgetId);
                    $http.get(url + 'connectionUrl=' + pieChartDataSource.dataSourceId.connectionString +
                            "&dataSetId=" + pieChartDataSource.id +
                            "&accountId=" + $stateParams.accountId +
                            "&dataSetReportName=" + pieChartDataSource.reportName +
                            "&driver=" + pieChartDataSource.dataSourceId.sqlDriver +
                            "&location=" + $stateParams.locationId +
                            "&startDate=" + $stateParams.startDate +
                            "&endDate=" + $stateParams.endDate +
                            '&username=' + pieChartDataSource.dataSourceId.userName +
                            '&password=' + dataSourcePassword +
                            '&widgetId=' + scope.widgetId +
                            '&url=' + pieChartDataSource.url +
                            '&port=3306&schema=vb&query=' + encodeURI(pieChartDataSource.query)).success(function (response) {
                        scope.loadingPie = false;
                        if (!response) {
                            return;
                        }
                        if (response.data.length === 0) {
                            scope.pieEmptyMessage = "No Data Found";
                            scope.hideEmptyPie = true;
                        } else {
                            var loopCount = 0;
                            var sortingObj;
                            var chartMaxRecord = JSON.parse(scope.widgetObj)
                            var chartData = response.data;
//                        var chartData = response.data;                        
                            if (sortFields.length > 0) {
                                angular.forEach(sortFields, function (value, key) {
                                    if (value.fieldType != 'day') {
//                                    chartData = scope.orderData(chartData, sortFields);
                                        sortingObj = scope.orderData(chartData, sortFields);
                                        if (chartMaxRecord.maxRecord) {
                                            chartData = maximumRecord(chartMaxRecord, sortingObj)
                                            console.log(chart)
                                        } else {
                                            chartData = sortingObj;
                                        }
                                    } else {
                                        var dateOrders = ["Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"]
                                        sortingObj = orderByFilter(chartData, function (item) {
                                            if (value.sortOrder === 'asc') {
                                                return dateOrders.indexOf(item[value.fieldName]);
                                            } else if (value.sortOrder === 'desc') {
                                                return dateOrders.indexOf(item[value.fieldName] * -1);
                                            }
                                        });
                                        if (chartMaxRecord.maxRecord) {
                                            chartData = maximumRecord(chartMaxRecord, sortingObj)
                                        } else {
                                            chartData = sortingObj;
                                        }
                                    }
                                })
                            }

//                        chartData = sortResults(chartData, sortField, sortOrder);
                            xTicks = [xAxis.fieldName];
                            xData = chartData.map(function (a) {
                                xTicks.push(loopCount);
                                loopCount++;
                                return a[xAxis.fieldName];
                            });
                            columns.push(xTicks);
                            angular.forEach(yAxis, function (value, key) {
                                ySeriesData = chartData.map(function (a) {
                                    return a[value.fieldName] || "0";
                                });
                                ySeriesData.unshift(value.displayName);
                                columns.push(ySeriesData);
                            });
                            var data = {};
                            var legends = [];
                            var yAxisField = yAxis[0];
                            chartData.forEach(function (e) {
                                legends.push(e[xAxis.fieldName]);
                                data[e[xAxis.fieldName]] = data[e[xAxis.fieldName]] ? data[e[xAxis.fieldName]] : 0 + e[yAxisField.fieldName] ? e[yAxisField.fieldName] : 0;
                            })

                            var chart = c3.generate({
                                bindto: element[0],
                                data: {
                                    json: [data],
                                    keys: {
                                        value: xData,
                                    },
                                    type: 'pie'
                                },
                                color: {
                                    pattern: ['#62cb31', '#666666', '#a5d169', '#75ccd0', '#DC143C']

                                },
                                tooltip: {show: false},
                                axis: {
                                    x: {
                                        tick: {
                                            format: function (x) {
                                                return xData[x];
                                            }
                                        }
                                    }
                                },
                                grid: {
                                    x: {
                                        show: false
                                    },
                                    y: {
                                        show: false
                                    }
                                }
                            });
                        }
                    });
                }
                scope.setPieChartFn({pieFn: scope.refreshPieChart});
                scope.refreshPieChart();
            }
        }
    };
});
app.directive('areaChartDirective', function ($http, $stateParams, $filter, orderByFilter) {
    return{
        restrict: 'A',
        template: '<div ng-show="loadingArea" class="text-center"><img src="static/img/logos/loader.gif" width="40"></div>' +
                '<div ng-show="hideEmptyArea" class="text-center">{{areaEmptyMessage}}</div>',
        scope: {
            setAreaChartFn: '&',
            widgetId: '@',
            areaChartSource: '@',
            widgetColumns: '@',
            pieChartId: '@',
            widgetObj: '@'
        },
        link: function (scope, element, attr) {
            var labels = {format: {}};
            scope.loadingArea = true;
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
            angular.forEach(JSON.parse(scope.widgetColumns), function (value, key) {
                if (!labels["format"]) {
                    labels = {format: {}};
                }
                if (value.displayFormat) {
                    var format = value.displayFormat;
                    var displayName = value.displayName;

                    if (value.displayFormat && value.displayFormat != 'H:M:S') {
                        labels["format"][displayName] = function (value) {
                            // alert(format);
                            if (format.indexOf("%") > -1) {
                                return d3.format(format)(value / 100);
                            }
                            return d3.format(format)(value);
                        };
                    } else {
                        labels["format"][displayName] = function (value) {
                            return formatBySecond(parseInt(value))
                            console.log(formatBySecond(parseInt(value)))
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
                    combinationTypes.push({fieldName: value.fieldName, combinationType: value.combinationType});
                }
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
                            //fieldsOrder.push(value.fieldname);
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
            }
            function maximumRecord(maxValue, list) {
                var maxData;
                if (maxValue.maxRecord > 0) {
                    maxData = list.slice(0, maxValue.maxRecord);
                }
                return maxData;
            }
            var areaChartDataSource = JSON.parse(scope.areaChartSource);
            if (scope.areaChartSource) {
                var url = "admin/proxy/getData?";
                if (areaChartDataSource.dataSourceId.dataSourceType == "sql") {
                    url = "admin/proxy/getJson?url=../dbApi/admin/dataSet/getData&";
                }
                
                var dataSourcePassword;
                if (areaChartDataSource.dataSourceId.password) {
                    dataSourcePassword = areaChartDataSource.dataSourceId.password;
                } else {
                    dataSourcePassword = '';
                }
                scope.refreshAreaChart = function () {
                    console.log("Area -----> " + scope.widgetId);
                    $http.get(url + 'connectionUrl=' + areaChartDataSource.dataSourceId.connectionString +
                            "&dataSetId=" + areaChartDataSource.id +
                            "&accountId=" + $stateParams.accountId +
                            "&dataSetReportName=" + areaChartDataSource.reportName +
                            "&driver=" + areaChartDataSource.dataSourceId.sqlDriver +
                            "&location=" + $stateParams.locationId +
                            "&startDate=" + $stateParams.startDate +
                            "&endDate=" + $stateParams.endDate +
                            '&username=' + areaChartDataSource.dataSourceId.userName +
                            '&password=' + dataSourcePassword +
                            '&widgetId=' + scope.widgetId +
                            '&url=' + areaChartDataSource.url +
                            '&port=3306&schema=vb&query=' + encodeURI(areaChartDataSource.query)).success(function (response) {
                        scope.loadingArea = false;
                        if (response.data.length === 0) {
                            scope.areaEmptyMessage = "No Data Found";
                            scope.hideEmptyArea = true;
                        } else {
                            var loopCount = 0;
                            var sortingObj;
                            var gridData = JSON.parse(scope.widgetObj);
                            var chartMaxRecord = JSON.parse(scope.widgetObj)
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
                                                return dateOrders.indexOf(item[value.fieldName] * -1);
                                            }
                                        });
                                        if (chartMaxRecord.maxRecord) {
                                            chartData = maximumRecord(chartMaxRecord, sortingObj)
                                        } else {
                                            chartData = sortingObj;
                                        }
                                    }
                                })
                                //chartData = scope.orderData(chartData, sortFields);
                            }

                            xTicks = [xAxis.fieldName];
                            xData = chartData.map(function (a) {
                                xTicks.push(loopCount);
                                loopCount++;
                                return a[xAxis.fieldName];
                            });
                            columns.push(xTicks);
                            angular.forEach(yAxis, function (value, key) {
                                ySeriesData = chartData.map(function (a) {
                                    return a[value.fieldName] || "0";
                                });
                                ySeriesData.unshift(value.displayName);
                                columns.push(ySeriesData);
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
                            var chart = c3.generate({
                                bindto: element[0],
                                data: {
                                    x: xAxis.fieldName,
                                    columns: columns,
                                    labels: labels,
                                    type: 'area',
                                    axes: axes,
                                    types: chartCombinationtypes
                                },
                                color: {
                                    pattern: ['#62cb31', '#555555']

                                },
                                tooltip: {show: false},
                                axis: {
                                    x: {
                                        tick: {
                                            format: function (x) {
                                                return xData[x];
                                            }
                                        }
                                    },
                                    y2: y2
                                },
                                grid: {
                                    x: {
                                        show: gridLine
                                    },
                                    y: {
                                        show: gridLine
                                    }
                                }
                            });
                        }
                    });
                }
                scope.setAreaChartFn({areaFn: scope.refreshAreaChart});
                scope.refreshAreaChart();
            }
        }
    };
});
app.directive('stackedBarChartDirective', function ($http, $stateParams, $filter, orderByFilter) {
    return{
        restrict: 'A',
        template: '<div ng-show="loadingStackedBar" class="text-center"><img src="static/img/logos/loader.gif" width="40"></div>' +
                '<div ng-show="hideEmptyStackedBar" class="text-center">{{stackedBarEmptyMessage}}</div>',
        scope: {
            setStackedBarChartFn: '&',
            widgetId: '@',
            stackedBarChartSource: '@',
            widgetColumns: '@',
            pieChartId: '@',
            widgetObj: '@'
        },
        link: function (scope, element, attr) {
            var labels = {format: {}};
            scope.loadingStackedBar = true;
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
            var groupingFields = [];
            var combinationTypes = [];
            var chartCombinationtypes = [];
            angular.forEach(JSON.parse(scope.widgetColumns), function (value, key) {
                if (!labels["format"]) {
                    labels = {format: {}};
                }
                if (value.displayFormat) {
                    var format = value.displayFormat;
                    var displayName = value.displayName;

                    if (value.displayFormat && value.displayFormat != 'H:M:S') {
                        labels["format"][displayName] = function (value) {
                            // alert(format);
                            if (format.indexOf("%") > -1) {
                                return d3.format(format)(value / 100);
                            }
                            return d3.format(format)(value);
                        };
                    } else {
                        labels["format"][displayName] = function (value) {
                            return formatBySecond(parseInt(value))
                            console.log(formatBySecond(parseInt(value)))
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
                if (value.groupField) {
                    groupingFields.push({fieldName: value.fieldName, groupField: value.groupField, fieldType: value.fieldType});
                }
                if (value.combinationType) {
                    combinationTypes.push({fieldName: value.fieldName, combinationType: value.combinationType});
                }
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
                            //fieldsOrder.push(value.fieldname);
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
            }

            function maximumRecord(maxValue, list) {
                var maxData;
                if (maxValue.maxRecord > 0) {
                    maxData = list.slice(0, maxValue.maxRecord);
                }
                return maxData;
            }

            var stackedBarChartDataSource = JSON.parse(scope.stackedBarChartSource);
            if (scope.stackedBarChartSource) {
                var url = "admin/proxy/getData?";
                if (stackedBarChartDataSource.dataSourceId.dataSourceType == "sql") {
                    url = "admin/proxy/getJson?url=../dbApi/admin/dataSet/getData&";
                }
                
                var dataSourcePassword;
                if (stackedBarChartDataSource.dataSourceId.password) {
                    dataSourcePassword = stackedBarChartDataSource.dataSourceId.password;
                } else {
                    dataSourcePassword = '';
                }
                scope.refreshStackedBarChart = function () {
                    console.log("Stacked Bar -----> " + scope.widgetId);
                    $http.get(url + 'connectionUrl=' + stackedBarChartDataSource.dataSourceId.connectionString +
                            "&dataSetId=" + stackedBarChartDataSource.id +
                            "&accountId=" + $stateParams.accountId +
                            "&dataSetReportName=" + stackedBarChartDataSource.reportName +
                            "&driver=" + stackedBarChartDataSource.dataSourceId.sqlDriver +
                            "&location=" + $stateParams.locationId +
                            "&startDate=" + $stateParams.startDate +
                            "&endDate=" + $stateParams.endDate +
                            '&username=' + stackedBarChartDataSource.dataSourceId.userName +
                            '&password=' + dataSourcePassword +
                            '&widgetId=' + scope.widgetId +
                            '&url=' + stackedBarChartDataSource.url +
                            '&port=3306&schema=vb&query=' + encodeURI(stackedBarChartDataSource.query)).success(function (response) {
                        scope.loadingStackedBar = false;
                        if (response.data.length === 0) {
                            scope.stackedBarEmptyMessage = "No Data Found";
                            scope.hideEmptyStackedBar = true;
                        } else {
                            var loopCount = 0;
                            var sortingObj;
                            var gridData = JSON.parse(scope.widgetObj);
                            var chartMaxRecord = JSON.parse(scope.widgetObj)
                            var chartData = response.data;
                            if (sortFields.length > 0) {
                                angular.forEach(sortFields, function (value, key) {
                                    if (value.fieldType != 'day') {
//                                    chartData = scope.orderData(chartData, sortFields);
                                        sortingObj = scope.orderData(chartData, sortFields);
                                        if (chartMaxRecord.maxRecord) {
                                            console.log(chartMaxRecord.maxRecord)
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
                                                return dateOrders.indexOf(item[value.fieldName] * -1);
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
//                        chartData = orderData(chartData, sortFields);
                            xTicks = [xAxis.fieldName];
                            xData = chartData.map(function (a) {
                                xTicks.push(loopCount);
                                loopCount++;
                                return a[xAxis.fieldName];
                            });
                            columns.push(xTicks);
                            angular.forEach(yAxis, function (value, key) {
                                ySeriesData = chartData.map(function (a) {
                                    return a[value.fieldName] || "0";
                                });
                                ySeriesData.unshift(value.displayName);
                                columns.push(ySeriesData);
                            });
                            var groupingNames = []
                            angular.forEach(groupingFields, function (value, key) {
                                groupingNames.push(value.fieldName)
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
                            var chart = c3.generate({
                                bindto: element[0],
                                data: {
                                    x: xAxis.fieldName,
                                    columns: columns,
                                    labels: labels,
                                    type: 'bar',
                                    groups: [groupingNames],
                                    axes: axes,
                                    types: chartCombinationtypes
                                },
                                color: {
                                    pattern: ['#555555', '#62cb31', '#75ccd0', '#666666', '#a5d169']

                                },
                                tooltip: {show: false},
                                axis: {
                                    x: {
                                        tick: {
                                            format: function (x) {
                                                return xData[x];
                                            }
                                        }
                                    },
                                    y2: y2
                                },
                                grid: {
                                    x: {
                                        show: gridLine
                                    },
                                    y: {
                                        show: gridLine
                                    }
                                }
                            });
                        }
                    });
                }
                scope.setStackedBarChartFn({stackedBarChartFn: scope.refreshStackedBarChart});
                scope.refreshStackedBarChart();

            }
        }
    };
});
app.filter('setDecimal', function () {
    return function (input, places) {
        if (isNaN(input))
            return input;
        var factor = "1" + Array(+(places > 0 && places + 1)).join("0");
        return Math.round(input * factor) / factor;
    };
})
        .filter('gridDisplayFormat', function () {
            return function (input, formatString) {
                if (formatString) {
                    if (formatString.indexOf("%") > -1) {
                        return d3.format(formatString)(input / 100);
                    }
                    return d3.format(formatString)(input);
                }
                return input;
            }
        })
        .filter('capitalize', function () {
            return function (input) {
                return (!!input) ? input.charAt(0).toUpperCase() + input.substr(1).toLowerCase() : '';
            }
        })
        .filter('xAxis', [function () {
                return function (chartXAxis) {
                    var xAxis = ['', 'x-1']
                    return xAxis[chartXAxis];
                }
            }])
        .filter('yAxis', [function () {
                return function (chartYAxis) {
                    var yAxis = ['', 'y-1', 'y-2']
                    return yAxis[chartYAxis];
                }
            }])
        .filter('hideColumn', [function () {
                return function (chartYAxis) {
                    var hideColumn = ['No', 'Yes']
                    return hideColumn[chartYAxis];
                };
            }]);
app.service('stats', function ($filter) {
    var coreAccumulate = function (aggregation, value) {
        initAggregation(aggregation);
        if (angular.isUndefined(aggregation.stats.accumulator)) {
            aggregation.stats.accumulator = [];
        }
        aggregation.stats.accumulator.push(value);
    };
    var initAggregation = function (aggregation) {
        /* To be used in conjunction with the cleanup finalizer */
        if (angular.isUndefined(aggregation.stats)) {
            aggregation.stats = {sum: 0, impressionsSum: 0, clicksSum: 0, costSum: 0, conversionsSum: 0};
        }
    };
    var initProperty = function (obj, prop) {
        /* To be used in conjunction with the cleanup finalizer */
        if (angular.isUndefined(obj[prop])) {
            obj[prop] = 0;
        }
    };
    var increment = function (obj, prop) {
        /* if the property on obj is undefined, sets to 1, otherwise increments by one */
        if (angular.isUndefined(obj[prop])) {
            obj[prop] = 1;
        } else {
            obj[prop]++;
        }
    };
    var service = {
        aggregator: {
            accumulate: {
                /* This is to be used with the uiGrid customTreeAggregationFn definition,
                 * to accumulate all of the data into an array for sorting or other operations by customTreeAggregationFinalizerFn
                 * In general this strategy is not the most efficient way to generate grouped statistics, but
                 * sometime is the only way.
                 */
                numValue: function (aggregation, fieldValue, numValue) {
                    return coreAccumulate(aggregation, numValue);
                },
                fieldValue: function (aggregation, fieldValue) {
                    return coreAccumulate(aggregation, fieldValue);
                }
            },
            ctr: function (aggregation, fieldValue, numValue, row) {

                initAggregation(aggregation);
                increment(aggregation.stats, 'count');
                aggregation.stats.clicksSum += row.entity.clicks;
                aggregation.stats.impressionsSum += row.entity.impressions;
                aggregation.value = (aggregation.stats.clicksSum * 100) / aggregation.stats.impressionsSum; //row.entity.balance;
            },
            ctrFooter: function (x, y, z) {
                // Need Sum of balance/sum of age
                var clicksColumn = $filter('filter')(this.grid.columns, {displayName: 'Clicks'})[0];
                var impressionsColumn = $filter('filter')(this.grid.columns, {displayName: 'Impressions'})[0];
                clicksColumn.updateAggregationValue();
                impressionsColumn.updateAggregationValue();
                var aggregatedClicks = clicksColumn.aggregationValue;
                var aggregatedImpressions = impressionsColumn.aggregationValue;
                if (aggregatedClicks && aggregatedImpressions) {
                    if (isNaN(aggregatedClicks)) {
                        aggregatedClicks = Number(aggregatedClicks.replace(/[^0-9.]/g, ""));
                    }
                    if (isNaN(aggregatedImpressions)) {
                        aggregatedImpressions = Number(aggregatedImpressions.replace(/[^0-9.]/g, ""));
                    }
                    return (aggregatedClicks * 100) / aggregatedImpressions;
                }
                return "";
            },
            cpc: function (aggregation, fieldValue, numValue, row) {

                initAggregation(aggregation);
                increment(aggregation.stats, 'count');
                aggregation.stats.clicksSum += row.entity.clicks;
                aggregation.stats.costSum += row.entity.cost;
                //aggregation.stats.sum += row.entity.clicks * row.entity.age;

                aggregation.value = aggregation.stats.costSum / aggregation.stats.clicksSum; //row.entity.balance;
            },
            cpcFooter: function (x, y, z) {
                // Need Sum of balance/sum of age
                var clicksColumn = $filter('filter')(this.grid.columns, {displayName: 'Clicks'})[0];
                var costColumn = $filter('filter')(this.grid.columns, {displayName: 'Cost'})[0];
                clicksColumn.updateAggregationValue();
                costColumn.updateAggregationValue();
                var aggregatedClicks = clicksColumn.aggregationValue;
                var aggregatedCost = costColumn.aggregationValue;
                if (aggregatedClicks && aggregatedCost) {
                    if (isNaN(aggregatedClicks)) {
                        aggregatedClicks = Number(aggregatedClicks.replace(/[^0-9.]/g, ""));
                    }
                    if (isNaN(aggregatedCost)) {
                        aggregatedCost = Number(aggregatedCost.replace(/[^0-9.]/g, ""));
                    }
                    return aggregatedCost / aggregatedClicks;
                }
                return "";
            },
            cpa: function (aggregation, fieldValue, numValue, row) {

                initAggregation(aggregation);
                increment(aggregation.stats, 'count');
                aggregation.stats.conversionsSum += row.entity.conversions;
                aggregation.stats.costSum += row.entity.cost;
                aggregation.value = aggregation.stats.costSum / aggregation.stats.conversionsSum; //row.entity.balance;
            },
            cpaFooter: function (x, y, z) {
                // Need Sum of balance/sum of age
                var conversionsColumn = $filter('filter')(this.grid.columns, {displayName: 'Conversions'})[0];
                var costColumn = $filter('filter')(this.grid.columns, {displayName: 'Cost'})[0];
                conversionsColumn.updateAggregationValue();
                costColumn.updateAggregationValue();
                var aggregatedConversions = conversionsColumn.aggregationValue;
                var aggregatedCost = costColumn.aggregationValue;
                if (aggregatedConversions && aggregatedCost) {
                    if (isNaN(aggregatedConversions)) {
                        aggregatedConversions = Number(aggregatedConversions.replace(/[^0-9.]/g, ""));
                    }
                    if (isNaN(aggregatedCost)) {
                        aggregatedCost = Number(aggregatedCost.replace(/[^0-9.]/g, ""));
                    }
                    return aggregatedCost / aggregatedConversions;
                }
                return "";
            }
        },
        finalizer: {
            cleanup: function (aggregation) {
                delete aggregation.stats;
                if (angular.isUndefined(aggregation.rendered)) {
                    aggregation.rendered = aggregation.value;
                }
            },
            ctr: function (aggregation) {
                //service.finalizer.variance(aggregation);
                aggregation.value = 20000;
                //aggregation.rendered = 30000;
            }
        },
    };
    return service;
});
