app.controller('EditWidgetController', function ($scope, $http, $stateParams, localStorageService, $timeout, $filter, $state) {
    $scope.editWidgetData = []
    $scope.permission = localStorageService.get("permission");
    $scope.accountId = $stateParams.accountId;
    $scope.accountName = $stateParams.accountName;
    $scope.productId = $stateParams.productId;
    $scope.tabId = $stateParams.tabId;
    $scope.startDate = $stateParams.startDate;
    $scope.endDate = $stateParams.endDate;

    $http.get("admin/ui/dbWidget/" + $stateParams.tabId).success(function (response) {
        $scope.widgets = response;
        if ($stateParams.widgetId) {
            $scope.editWidgetData.push($filter('filter')($scope.widgets, {id: $stateParams.widgetId})[0]);
            angular.forEach($scope.editWidgetData, function (value, key) {
                $scope.editWidget(value)
            })
        }
    });

    $scope.selectAggregations = [
        {name: 'None', value: ""},
        {name: 'Sum', value: "sum"},
        {name: 'CTR', value: "ctr"},
        {name: 'CPC', value: "cpc"},
        {name: 'CPCS', value: "cpcs"},
        {name: 'CPS', value: "cps"},
        {name: 'CPA', value: "cpa"},
        {name: 'CPAS', value: "cpas"},
        {name: 'Avg', value: "avg"},
        {name: 'Count', value: "count"},
        {name: 'Min', value: "min"},
        {name: 'Max', value: "max"},
        {name: 'CPL', value: "cpl"},
        {name: 'CPLC', value: "cplc"},
        {name: 'CPComment', value: "cpcomment"},
        {name: 'CPostE', value: "cposte"},
        {name: 'CPageE', value: "cpagee"},
        {name: 'CPP', value: "cpp"},
        {name: 'CPR', value: "cpr"}

    ];   //Aggregation Type-Popup
    $scope.selectGroupPriorities = [
        {num: 'None', value: ""},
        {num: 1, value: 1},
        {num: 2, value: 2}
    ];
    $scope.selectDateDurations = [
        {duration: "None", value: 'none'},
        {duration: "Today", value: 'today'},
        {duration: "Last N days", value: ''},
        {duration: "Last N Weeks", value: ''},
        {duration: "Last N Months", value: ''},
        {duration: "This Month", value: 'thisMonth'},
        {duration: "This Year", value: 'thisYear'},
        {duration: "Last Year", value: 'lastYear'},
        {duration: "Yesterday", value: 'yesterday'},
        {duration: "Custom", value: 'custom'}
    ]; // Month Durations-Popup
    $scope.selectXAxis = [
        {label: 'None', value: ""},
        {label: "X-1", value: 1}
    ];
    $scope.selectYAxis = [
        {label: 'None', value: ""},
        {label: "Y-1", value: 1},
        {label: "Y-2", value: 2}
    ];
    $scope.alignments = [
        {name: '', displayName: 'None'},
        {name: "left", displayName: "Left"},
        {name: "right", displayName: "Right"},
        {name: "center", displayName: "Center"}
    ];


    $scope.sorting = [
        {name: 'None', value: ''},
        {name: 'asc', value: 'asc'},
        {name: 'desc', value: 'desc'}
    ];

    //$scope.sorting = ['None', 'asc', 'desc']
//        {name: 'None', value: ''},
//        {name: 'asc', value: 'asc'},
//        {name: 'desc', value: 'desc'}
//    ];
    $scope.tableWrapText = [
        {name: 'None', value: ''},
        {name: 'Yes', value: "yes"}
    ];

    $scope.fieldTypes = ['none', 'string', 'number', 'date', 'day']
//        {id: 0, name: 'None', value: ''},
//        {id: 1, name: 'String', value: 'string'},
//        {id: 2, name: 'Number', value: 'number'},
//        {id: 3, name: 'Date', value: 'date'}
//    ];
    $scope.hideOptions = [
        {name: 'Yes', value: 1},
        {name: 'No', value: ''}
    ];
    $scope.isEditPreviewColumn = false;

    $scope.formats = [
        {name: "Currency", value: '$,.2f'},
        {name: "Integer", value: ',.0f'},
        {name: "Percentage", value: ',.2%'},
        {name: "Decimal1", value: ',.1f'},
        {name: "Decimal2", value: ',.2f'},
        {name: "None", value: ''}
    ];

    $scope.selectWidgetDuration = function (dateRangeName, widget) {
        //scheduler.dateRangeName = dateRangeName;
        console.log(dateRangeName)
        if (dateRangeName == 'Last N Days') {
            if (widget.lastNdays) {
                widget.dateRangeName = "Last " + widget.lastNdays + " Days";
            } else {
                widget.dateRangeName = "Last 0 Days";
            }
            widget.lastNweeks = "";
            widget.lastNmonths = "";
            widget.lastNyears = "";
        } else if (dateRangeName == 'Last N Weeks') {
            if (widget.lastNweeks) {
                widget.dateRangeName = "Last " + widget.lastNweeks + " Weeks";
            } else {
                widget.dateRangeName = "Last 0 Weeks";
            }
            widget.lastNdays = "";
            widget.lastNmonths = "";
            widget.lastNyears = "";
        } else if (dateRangeName == 'Last N Months') {
            if (widget.lastNmonths) {
                widget.dateRangeName = "Last " + widget.lastNmonths + " Months";
            } else {
                widget.dateRangeName = "Last 0 Months";
            }
            widget.lastNdays = "";
            widget.lastNweeks = "";
            widget.lastNyears = "";
        } else if (dateRangeName == 'Last N Years') {
            if (widget.lastNyears) {
                widget.dateRangeName = "Last " + widget.lastNyears + " Years";
            } else {
                widget.dateRangeName = "Last 0 Years";
            }
            widget.lastNdays = "";
            widget.lastNweeks = "";
            widget.lastNmonths = "";
        } else {
            widget.dateRangeName = dateRangeName;
            widget.lastNdays = "";
            widget.lastNweeks = "";
            widget.lastNmonths = "";
            widget.lastNyears = "";
        }
    }

//    $scope.dateDuration = function (widget, selectDateDuration) {
//        widget.duration = selectDateDuration.duration;
//    };

    $http.get('admin/ui/dataSource').success(function (response) {
        $scope.dataSources = response;
    });

    $scope.selectDataSource = function (dataSourceName, widget) {
        if (!dataSourceName) {
            return;
        }
        $http.get('admin/ui/dataSet').success(function (response) {
            $scope.dataSets = []
            angular.forEach(response, function (value, key) {
                if (value.dataSourceId.name == dataSourceName.name) {
                    $scope.dataSets.push(value);
                }
            });
        });
    };

    $scope.widgets = [];
    function getWidgetItem() {      //Default Loading Items
        if (!$stateParams.tabId) {
            $stateParams.tabId = 1;
        }
        $http.get("admin/ui/dbWidget/" + $stateParams.tabId).success(function (response) {
            $scope.widgets = response;
        });
    }
    getWidgetItem();
    $scope.collectionField = {};
    $scope.dispName = function (currentColumn) {
        $scope.filterName = $filter('filter')($scope.collectionFields, {fieldName: currentColumn.fieldName})[0];
        currentColumn.displayName = $scope.filterName.displayName;
    };


//    $scope.editWidgetItems = [{previewTitle: "test"}]
    $scope.editWidget = function (widget) {    //Edit widget
        $scope.editPreviewTitle = false;
        $scope.y1Column = [];
        $scope.y2Column = [];
        $scope.tickerItem = []
//        $scope.editWidgetItems.push(widget);
        $scope.tableDef(widget);
        $scope.selectedRow = widget.chartType;
        widget.previewUrl = widget.dataSetId;//widget.directUrl;
        $scope.selectDataSource(widget.dataSourceId, widget)
        widget.previewType = widget.chartType;
        $scope.editChartType = widget.chartType;
        if (widget.chartType == 'table' || widget.chartType == 'ticker') {
            $scope.previewChart(widget, widget);
        }
        angular.forEach(widget.columns, function (value, key) {
            var exists = false;
            angular.forEach($scope.formats, function (val, header) {
                if (value.displayFormat === val.value) {
                    exists = true;
                    $scope.tickerItem.push({displayName: value.displayName, fieldName: value.fieldName, displayFormat: {name: val.name, value: val.value}})
                }
            });
            if (exists == false) {
                $scope.tickerItem.push({displayName: value.displayName, fieldName: value.fieldName})
            }
        });
        angular.forEach(widget.columns, function (val, key) {
            if (val.xAxis == 1) {
                $scope.xColumn = val;
                $scope.selectPieChartXAxis = val;

                $scope.selectX1Axis(widget, val);
            }
            if (val.yAxis == 1) {
                if (val.fieldName) {
                    $scope.selectPieChartYAxis = val;
                    $scope.y1Column.push(val);
//                    $scope.selectY1Axis(widget, val);
                }
            }
            if (val.yAxis == 2) {
                if (val.fieldName) {
                    $scope.y2Column.push(val);
                    //$scope.selectY2Axis(widget, val);
                }
            }
        });
    };

    $scope.tableDef = function (widget) {      //Dynamic Url from columns Type data - Popup
        var dataSourcePassword;
        if (!widget.dataSetId) {
            return;
        }
        if (widget.dataSetId.dataSourceId.password) {
            dataSourcePassword = widget.dataSetId.dataSourceId.password;
        } else {
            dataSourcePassword = '';
        }
        if (widget.columns) {
            widget.columns = widget.columns;
            if (widget.dataSetId) {
                var url = "admin/proxy/getData?";
                if (widget.dataSetId.dataSourceId.dataSourceType == "sql") {
                    url = "admin/proxy/getJson?url=../dbApi/admin/dataSet/getData&";
                }
                if (widget.dataSetId.dataSourceId.dataSourceType == "csv") {
                    url = "admin/csv/getData?";
                }
                if (widget.dataSetId.dataSourceId.dataSourceType == "facebook") {
                    url = "admin/proxy/getData?";
                }
                $http.get(url + 'connectionUrl=' + widget.dataSetId.dataSourceId.connectionString +
                        "&dataSetId=" + widget.dataSetId.id +
                        "&accountId=" + $stateParams.accountId +
                        "&driver=" + widget.dataSetId.dataSourceId.sqlDriver +
                        "&startDate=" + $stateParams.startDate +
                        "&endDate=" + $stateParams.endDate +
                        '&username=' + widget.dataSetId.dataSourceId.userName +
                        '&password=' + dataSourcePassword +
                        '&port=3306&schema=vb&query=' + encodeURI(widget.dataSetId.query) +
                        "&fieldsOnly=true").success(function (response) {
                    $scope.collectionFields = [];
                    $scope.collectionFields = response.columnDefs;
                });
            }
        } else {
            if (widget.dataSetId) {
                var url = "admin/proxy/getData?";
                if (widget.dataSetId.dataSourceId.dataSourceType == "sql") {
                    url = "admin/proxy/getJson?url=../dbApi/admin/dataSet/getData&";
                }
                if (widget.dataSetId.dataSourceId.dataSourceType == "csv") {
                    url = "admin/csv/getData?";
                }
                if (widget.dataSetId.dataSourceId.dataSourceType == "facebook") {
                    url = "admin/proxy/getData?";
                }
                $http.get(url + 'connectionUrl=' + widget.dataSetId.dataSourceId.connectionString +
                        "&dataSetId=" + widget.dataSetId.id +
                        "&accountId=" + $stateParams.accountId +
                        "&driver=" + widget.dataSetId.dataSourceId.sqlDriver +
                        "&startDate=" + $stateParams.startDate +
                        "&endDate=" + $stateParams.endDate +
                        '&username=' + widget.dataSetId.dataSourceId.userName +
                        '&password=' + dataSourcePassword +
                        '&port=3306&schema=vb&query=' + encodeURI(widget.dataSetId.query) +
                        "&fieldsOnly=true").success(function (response) {
                    $scope.collectionFields = [];
                    widget.columns = response.columnDefs;
                    $scope.collectionFields = response.columnDefs;
                });
            }
        }
    };

    $scope.changeUrl = function (dataSet, widget) {
        if (!dataSet) {
            return;
        }
        $scope.editChartType = null;
        widget.previewUrl = dataSet;
        widget.columns = [];
        var chartType = widget

        var url = "admin/proxy/getData?";
        if (dataSet.dataSourceId.dataSourceType == "sql") {
            url = "admin/proxy/getJson?url=../dbApi/admin/dataSet/getData&";
        }
        if (dataSet.dataSourceId.dataSourceType == "csv") {
            url = "admin/csv/getData?";
        }
        if (dataSet.dataSourceId.dataSourceType == "facebook") {
            url = "admin/proxy/getData?";
        }
        var dataSourcePassword;
        if (dataSet.dataSourceId.password) {
            dataSourcePassword = dataSet.dataSourceId.password;
        } else {
            dataSourcePassword = '';
        }
        $http.get(url + 'connectionUrl=' + dataSet.dataSourceId.connectionString +
                "&dataSetId=" + dataSet.id +
                "&accountId=" + $stateParams.accountId +
                "&driver=" + dataSet.dataSourceId.sqlDriver +
                "&startDate=" + $stateParams.startDate +
                "&endDate=" + $stateParams.endDate +
                '&username=' + dataSet.dataSourceId.userName +
                '&password=' + dataSourcePassword +
                '&port=3306&schema=vb&query=' + encodeURI(dataSet.query) +
                "&fieldsOnly=true").success(function (response) {
            $scope.collectionFields = [];
            widget.columns = response.columnDefs;
//            angular.forEach(response.columnDefs, function (value, key) {
//                widget.columns.push({fieldName: value.fieldName, displayName: value.displayName, xAxis: value.xAxis, yAxis: value.yAxis,
//                    agregationFunction: value.agregationFunction, displayFormat: value.displayFormat, fieldType: value.type,
//                    groupPriority: value.groupPriority, sortOrder: value.sortOrder, sortPriority: value.sortPriority});
//
//            });
            angular.forEach(response.columnDefs, function (value, key) {
                $scope.collectionFields.push(value);
            });
            //$timeout(function () {
            $scope.previewChart(chartType, widget)
            // }, 50);
        });
    };

    $scope.pageRefresh = function () {          //Page Refresh
        getWidgetItem();
    };
    $http.get("static/datas/panelSize.json").success(function (response) {      //Default Panel in Ui
        $scope.newWidgets = response;
    });

//    $scope.addWidget = function (newWidget) {       //Add Widget
//        var data = {
//            width: newWidget, 'minHeight': 25, columns: [], chartType: ""
//        };
//        $http({method: 'POST', url: 'admin/ui/dbWidget/' + $stateParams.tabId, data: data}).success(function (response) {
//            $scope.widgets.unshift({id: response.id, width: newWidget, 'minHeight': 25, columns: [], tableFooter: 1, zeroSuppression: 1});
//            $scope.newWidgetId = response.id;
//            $scope.openPopup(response);
//        });
//    };

    $scope.deleteWidget = function (widget, index) {                            //Delete Widget
        $http({method: 'DELETE', url: 'admin/ui/dbWidget/' + widget.id}).success(function (response) {
            $scope.widgets.splice(index, 1);
        });
    };

    $http.get('static/datas/imageUrl.json').success(function (response) {       //Popup- Select Chart-Type Json
        $scope.chartTypes = response;
    });

    //DataSource
    $http.get('admin/datasources').success(function (response) {
        $scope.datasources = response;
    });

    //Data Source
    $http.get('admin/datasources').success(function (response) {
        $scope.datasources = response;
    });
    $scope.selectedDataSource = function (selectedItem) {
        $scope.selectItem = selectedItem;
        selectedItems(selectedItem);
    };

    $scope.previewChart = function (chartType, widget) {
        $scope.showPreviewItems = chartType.type ? chartType.type : chartType.chartType;
        widget.chartType = chartType.type ? chartType.type : chartType.chartType;    //Selected Chart type - Bind chart-type to showPreview()
        $scope.selectedRow = chartType.type ? chartType.type : chartType.chartType;
        $scope.editChartType = chartType.type ? chartType.type : chartType.chartType;
        $scope.previewChartUrl = widget.previewUrl;
        $scope.previewColumn = widget;
        if (chartType.type == 'text') {
            widget.dataSetId = '';
            widget.dataSourceId = '';
        }
    };

    $scope.selectPieChartX = function (widget, column) {
        if (!column) {
            return;
        }
        $scope.editChartType = null;
        var exists = false;
        angular.forEach(widget.columns, function (value, key) {
            if (column.fieldName == value.fieldName) {
                exists = true;
                value.xAxis = 1;
            } else {
                value.xAxis = null;
            }
        });
        if (exists == false) {
            column.xAxis = 1;
            widget.columns.push(column);
        }
        var chartType = widget;
        $timeout(function () {
            $scope.previewChart(chartType, widget)
        }, 50);
    };

    $scope.selectPieChartY = function (widget, column) {
        $scope.editChartType = null;
        var exists = false;
        angular.forEach(widget.columns, function (value, key) {
            if (column.fieldName == value.fieldName) {
                exists = true;
                value.yAxis = 1;
            } else {
                value.yAxis = null;
            }
        });
        if (exists == false) {
            column.yAxis = 1;
            widget.columns.push(column);
        }
        var chartType = widget;
        $timeout(function () {
            $scope.previewChart(chartType, widget)
        }, 50);
    };

    $scope.selectX1Axis = function (widget, column) {
        $scope.editChartType = null;
        var exists = false;
        angular.forEach(widget.columns, function (value, key) {
            if (column.fieldName == value.fieldName) {
                exists = true;
                value.xAxis = 1;
            }
        });
        if (exists == false) {
            column.xAxis = 1;
            widget.columns.push(column);
        }
        console.log(widget)
        var chartType = widget;
        $timeout(function () {
            $scope.previewChart(chartType, widget)
        }, 50);
    };

    $scope.selectY1Axis = function (widget, y1data) {
        $scope.editChartType = null;
        angular.forEach(y1data, function (value, key) {
            if (!value) {
                return;
            }
            var exists = false;
            angular.forEach(widget.columns, function (val, key) {
                if (val.fieldName === value.fieldName) {
                    exists = true;
                    val.yAxis = 1;
                } else {
                    if (val.fieldName == y1data.removeItem) {
                        val.yAxis = null;
                    }
                }
            });
            if (exists == false) {
                if (value.displayName) {
                    value.yAxis = 1;
                    widget.columns.push(value);
                }
            }
        });

        var chartType = widget;
        $timeout(function () {
            $scope.previewChart(chartType, widget)
        }, 50);
    };

    $scope.selectY2Axis = function (widget, y2data) {
        $scope.editChartType = null;
        angular.forEach(y2data, function (value, key) {
            if (!value) {
                return;
            }
            var exists = false;
            angular.forEach(widget.columns, function (val, key) {
                if (val.fieldName === value.fieldName) {
                    exists = true;
                    val.yAxis = 2;
                } else {
                    if (val.fieldName == y2data.removeItem) {
                        val.yAxis = null;
                    }
                }
            });
            if (exists == false) {
                if (value.displayName) {
                    value.yAxis = 2;
                    widget.columns.push(value);
                }
            }
        });
        var chartType = widget;
        $timeout(function () {
            $scope.previewChart(chartType, widget)
        }, 50);
    };
    $scope.setChartFormat = function (widget, column) {
        if (widget.chartType != 'pie') {
            if (column.yAxis == 1) {
                $scope.selectY1Axis(widget, column);
            }
            if (column.yAxis == 2) {
                $scope.selectY2Axis(widget, column);
            }
        } else if (widget.chartType == 'pie') {
            if (column.xAxis == 1) {
                $scope.selectPieChartX(widget, column);
            }
            if (column.yAxis == 1) {
                $scope.selectPieChartY(widget, column);
            }
        } else {
            return;
        }
    };

    $scope.removedByY1Column = function (widget, column, yAxisItems) {
        $scope.editChartType = null;
        if (yAxisItems.length > 0) {
            yAxisItems.removeItem = column.fieldName;
            $scope.selectY1Axis(widget, yAxisItems);
        } else {
            angular.forEach(widget.columns, function (val, key) {
                if (val.fieldName == column.fieldName) {
                    val.yAxis = null;
                }
            });
        }
        var chartType = widget;
        $timeout(function () {
            $scope.previewChart(chartType, widget)
        }, 50);
    };
    $scope.removedByY2Column = function (widget, column, yAxisItems) {
        $scope.editChartType = null;
        if (yAxisItems.length > 0) {
            yAxisItems.removeItem = column.fieldName;
            $scope.selectY2Axis(widget, yAxisItems);
        } else {
            angular.forEach(widget.columns, function (val, key) {
                if (val.fieldName == column.fieldName) {
                    val.yAxis = null;
                }
            });
        }
        var chartType = widget;
        $timeout(function () {
            $scope.previewChart(chartType, widget)
        }, 50);
    };

    $scope.ticker = function (widget, column) {
        $scope.editChartType = null;
        var newColumns = [];
        angular.forEach(column, function (value, key) {
            angular.forEach($scope.collectionFields, function (val, header) {
                if (val.fieldName === value.fieldName) {
                    val.displayFormat = value.displayFormat;
                    newColumns.push(val);
                }
            });
            widget.columns = newColumns;
        });
        var chartType = widget;
        $timeout(function () {
            $scope.previewChart(chartType, widget)
        }, 50);
    };

    $scope.setFormat = function (widget, column) {
        $scope.editChartType = null;
        angular.forEach(widget.columns, function (value, key) {
            if (column.fieldName === value.fieldName) {
                value.displayFormat = column.displayFormat;
            }
        });
        $timeout(function () {
            $scope.editChartType = "ticker";
        }, 50);
    };

    $scope.tickerFormat = function (widget, format) {
        $scope.setFormat(widget, format)
    };

    $scope.removedByTicker = function (widget, column, tickerItem) {
        $scope.editChartType = null;
        $scope.ticker(widget, tickerItem);
        var chartType = widget;
        $timeout(function () {
            $scope.editChartType = "ticker";
        }, 50);
    };

    $scope.save = function (widget) {
        try {
            $scope.customStartDate = moment($('#widgetDateRange').data('daterangepicker').startDate).format('MM/DD/YYYY') ? moment($('#widgetDateRange').data('daterangepicker').startDate).format('MM/DD/YYYY') : $stateParams.startDate;//$scope.startDate.setDate($scope.startDate.getDate() - 1);

            $scope.customEndDate = moment($('#widgetDateRange').data('daterangepicker').endDate).format('MM/DD/YYYY') ? moment($('#widgetDateRange').data('daterangepicker').endDate).format('MM/DD/YYYY') : $stateParams.endDate;
        } catch (e) {

        }
        widget.directUrl = widget.previewUrl ? widget.previewUrl : widget.directUrl;
        var widgetColumnsData = [];
        angular.forEach(widget.columns, function (value, key) {
            var hideColumn = value.columnHide;
            if (value.groupPriority > 0) {
                hideColumn = 1;
            }
            var columnData = {
                id: value.id,
                fieldName: value.fieldName,
                displayName: value.displayName,
                agregationFunction: value.agregationFunction,
                groupPriority: isNaN(value.groupPriority) ? null : value.groupPriority,
                xAxis: isNaN(value.xAxis) ? null : value.xAxis,
                yAxis: isNaN(value.yAxis) ? null : value.yAxis,
                sortOrder: value.sortOrder,
                displayFormat: value.displayFormat,
                alignment: value.alignment,
                baseFieldName: value.baseFieldName,
                fieldGenerationFields: value.fieldGenerationFields,
                fieldGenerationFunction: value.fieldGenerationFunction,
                fieldType: value.fieldType,
                functionParameters: value.functionParameters,
                remarks: value.remarks,
                sortPriority: isNaN(value.sortPriority) ? null : value.sortPriority,
                width: isNaN(value.width) ? null : value.width,
                wrapText: value.wrapText,
                xAxisLabel: value.xAxisLabel,
                yAxisLabel: value.yAxisLabel,
                columnHide: hideColumn,
                search: value.search
            };
            widgetColumnsData.push(columnData);
        });
        var dataSourceTypeId;
        var dataSetTypeId;
        if (widget.chartType != 'text') {
            dataSourceTypeId = widget.dataSourceId.id;
        } else {
            dataSourceTypeId = 0;
        }
        if (widget.chartType != 'text') {
            dataSetTypeId = widget.dataSetId.id;
        } else {
            dataSetTypeId = 0;
        }
        var data = {
            id: widget.id,
            chartType: $scope.editChartType ? $scope.editChartType : widget.chartType,
            widgetTitle: widget.widgetTitle,
            widgetColumns: widgetColumnsData,
            dataSourceId: dataSourceTypeId,
            dataSetId: dataSetTypeId,
            tableFooter: widget.tableFooter,
            zeroSuppression: widget.zeroSuppression,
            maxRecord: widget.maxRecord,
            dateDuration: widget.dateDuration,
            content: widget.content,
            width: widget.width,
            dateRangeName: widget.dateRangeName,
            lastNdays: widget.lastNdays,
            lastNweeks: widget.lastNweeks,
            lastNmonths: widget.lastNmonths,
            lastNyears: widget.lastNyears,
            customStartDate: $scope.customStartDate, //widget.customStartDate,
            customEndDate: $scope.customEndDate//widget.customEndDate
        };

        $http({method: widget.id ? 'PUT' : 'POST', url: 'admin/ui/dbWidget/' + $stateParams.tabId, data: data}).success(function (response) {
            $state.go("index.dashboard.widget", {productId: $stateParams.productId, accountId: $stateParams.accountId, accountName: $stateParams.accountName, tabId: $stateParams.tabId, startDate: $stateParams.startDate, endDate: $stateParams.endDate})
        });
    };

    $scope.closeWidget = function (widget) {
        $scope.widget = "";
        $state.go("index.dashboard.widget", {productId: $stateParams.productId, accountId: $stateParams.accountId, accountName: $stateParams.accountName, tabId: $stateParams.tabId, startDate: $stateParams.startDate, endDate: $stateParams.endDate})
    };
});

app.filter('xAxis', [function () {
        return function (chartXAxis) {
            var xAxis = ['', 'x-1']
            return xAxis[chartXAxis];
        };
    }]);
app.filter('yAxis', [function () {
        return function (chartYAxis) {
            var yAxis = ['', 'y-1', 'y-2']
            return yAxis[chartYAxis];
        };
    }]);
app.filter('hideColumn', [function () {
        return function (chartYAxis) {
            var hideColumn = ['No', 'Yes']
            return hideColumn[chartYAxis];
        };
    }]);

app.directive('widgetPreviewTable', function ($http, $stateParams, $state) {
    return{
        restrict: 'AE',
        scope: {
            previewUrls: '@',
            previewColumns: '@',
            previewWidget: '@',
            previewWidgetTable: '@',
        },
        template:                
                "<div class='panel-head'>" +
                //Panel Tools
                "<div class='row'>" +
                "<div>" +
                "<div class='pull-right list-button'>" +
                "<button class='btn btn-info' data-toggle='dropdown' aria-haspopup='true' aria-expanded='false'><i class='fa fa-list'></i> List" +
                "</button>" +
                //list columns
                "<ul class='dropdown-menu list-unstyled'>" +
                "<li ng-repeat='column in tableList'>" +
                "<button class='btn btn-link dropdown-item' ng-click='addList(column)'>" +
                "{{column.displayName}}" +
                "</button>" +
                "</li>" +
                "</ul>" +
                "</div>" +
                //Panel Title
                "<div ng-hide='editPreviewTitle'>" +
                "<a ng-click='editPreviewTitle = true'>{{previewWidgetTitle?previewWidgetTitle:'Widget Title'}}</a>" +
                "</div>" +
                "<div ng-show='editPreviewTitle'>" +
                "<div class='col-sm-6'><input class='form-control' type='text' ng-model='previewWidgetTitle'></div>" +
                "<div class='col-sm-4'><a ng-click='editPreviewTitle = false'><i class='fa fa-save'></i></a>" +
                "<a ng-click='editPreviewTitle = false'><i class='fa fa-close'></i></a>" +
                "</div>" +
                "</div>" +                
                "</div>" +
                "</div>" + //End Panel Title
                "</div>" +
                "</div>" +
                //Table
                "<div class=''>" +
                "<div class='table-responsive tbl-preview' style='height:200px; overflow: auto'>" +
                "<table class='table table-bordered table-hover' >" +
                "<thead >" +
                "<tr data-as-sortable='' data-ng-model='previewTableHeaderName' unselectable='on' class='unselectable'>" +
                "<th style='cursor: move;' data-as-sortable-item ng-repeat='collectionField in previewTableHeaderName track by $index'>" +
                "<div data-as-sortable-item-handle ng-hide='collectionField.isEdit'>" +
//                "<div class='preview-table-settings' ng-click='collectionField.isEdit=true'>" +
                "<div class='preview-table-settings' ng-hide='collectionField.isEdit'>" +
                "<a ng-click='collectionField.isEdit = true'>{{collectionField.displayName}}</a>" +
                "</div></div>" +
                //column Edit
                "<div ng-show='collectionField.isEdit'><input class='form-control-btn' ng-model='collectionField.displayName'>" +
                "<a class='btn btn-default btn-xs' ng-click='collectionField.isEdit=false'><i class='fa fa-save'></i></a>" +
                "<a class='btn btn-default btn-xs' ng-click='collectionField.isEdit=false'><i class='fa fa-close'></i></a></div>" +
                "</th>" +
                "</tr>" +
                "<tr><th ng-repeat='collectionField in previewTableHeaderName track by $index'>" +
                "<button class='btn btn-default btn-xs'" +
                "ns-popover=''" +
                "ns-popover-template='close'" +
                "ns-popover-trigger='click'" +
                "ns-popover-theme='ns-popover-tooltip-theme'" +
                "ns-popover-timeout='1000'" +
                "ns-popover-hide-on-inside-click='false'" +
                "ns-popover-hide-on-outside-click='true'" +
                "ns-popover-hide-on-button-click='true'><i class='fa fa-cog'></i></button>" +
                //DropDown List
                " <script type='text/ng-template' id='close'>" +
                "<div class='triangle'></div>" +
                "<div class='ns-popover-tooltip'>" +
                "<form class='form-inline'>" +
                "<ul class='scheduler-list-style'>" +
                "<li class='input-group col-sm-12'> <label>Aggregation function</label>" +
                "<select class='form-control' ng-model='collectionField.agregationFunction'>" +
                "<option ng-repeat='selectAggregation in selectAggregations'" +
                "value='{{selectAggregation.value}}'>" +
                "{{selectAggregation.name}}</option></select></li>" +
                "<li class='input-group col-sm-12'><label>Group priority</label><select ng-model='collectionField.groupPriority' class='form-control'>" +
                "<option ng-repeat='selectGroupPriority in selectGroupPriorities' value='{{selectGroupPriority.value}}'>" +
                "{{selectGroupPriority.num}}</option></select></li>" +
                "<li class='input-group col-sm-12'><label>Sort</label><select ng-model='collectionField.sortOrder' class='form-control'>" +
                "<option ng-repeat='sort in sorting' value='{{sort.value}}'>" +
                "{{sort.name}}</option></select></li>" +
                "<li class='input-group col-sm-12'><label>Field Type</label>" +
                "<select class='form-control' ng-model='collectionField.fieldType' ng-options='fieldType for fieldType in fieldTypes'>" +
                "</select>" +
                "</li>" +
                "<li class='input-group col-sm-12'><label>Format</label><select ng-model='collectionField.displayFormat' class='form-control'>" +
                "<option ng-repeat='format in formats' value='{{format.value}}'>" +
                "{{format.name}}</option></select></li>" +
                "<li class='input-group col-sm-12'><label>Alignment</label><select ng-model='collectionField.alignment' class='form-control'>" +
                "<option ng-repeat='alignment in alignments' value='{{alignment.name}}'>" +
                "{{alignment.name}}</option></select></li>" +
                "<li class='input-group col-sm-12'><label>ColumnHide</label><select ng-model='collectionField.columnHide' class='form-control'>" +
                "<option ng-repeat='hideOption in hideOptions' value='{{hideOption.value}}'>" +
                "{{hideOption.name}}</option></select></li>" +
                "<li><input type='checkbox' class='search-check' ng-model='collectionField.search'><label class='search-label'>Search</label></li>" +
//                        "<li><a class='btn' ng-click='deleteColumn($index)'>Delete</a></li>" +
                "</ul>" +
                "<button class='btn btn-info btn-sm' ng-click='hidePopover()'>Close</button>&nbsp;" +
                "<button class='btn btn-warning btn-sm' ng-click='deleteColumn($index); hidePopover()'>Delete</button>" +
                "</form>" +
                "</div>" +
                "</script>" +
                "</th></tr>" +
                " </thead>" +
                //colum Fields
                "<tbody>" +
                "<tr ng-repeat='tablelist in tableData'>" +
                "<td ng-repeat='collectionField in previewTableHeaderName track by $index'>" +
                "<div>{{tablelist[collectionField.fieldName]}}</div>" +
                "</td>" +
                "</tr>" +
                "</tbody>" +
                "</table> " +
                "</div>" +
                "</div>" +
                "<div class='preview-footer'>" +
//                "<div class='form-group btn-preview pull-right'>" +
                "<button data-dismiss='modal' class='btn btn-default' ng-click='closeWidget()'><span class='fa fa-times'></span> Cancel</button>" +
                "<button data-dismiss='modal' class='btn btn-info' ng-click='save()'><span class='fa fa-save'></span> Save</button>" +
//                "</div>" +
                "</div>" +
                "</div>",
        link: function (scope, attrs) {
            scope.previewTableHeaderName = JSON.parse(scope.previewColumns);
            scope.listColumns = [];
            scope.listColumns = JSON.parse(scope.previewColumns);
            scope.previewWidgetTitle = JSON.parse(scope.previewWidget).widgetTitle;
            var widget = JSON.parse(scope.previewWidget);
            scope.selectAggregations = [
                {name: 'None', value: ""},
                {name: 'Sum', value: "sum"},
                {name: 'CTR', value: "ctr"},
                {name: 'CPC', value: "cpc"},
                {name: 'CPS', value: "cps"},
                {name: 'CPA', value: "cpa"},
                {name: 'Avg', value: "avg"},
                {name: 'Count', value: "count"},
                {name: 'Min', value: "min"},
                {name: 'Max', value: "max"},
                {name: 'CPL', value: "cpl"},
                {name: 'CPLC', value: "cplc"},
                {name: 'CPComment', value: "cpcomment"},
                {name: 'CPostE', value: "cposte"},
                {name: 'CPageE', value: "cpagee"},
                {name: 'CPP', value: "cpp"},
                {name: 'CPR', value: "cpr"}
            ];
            scope.selectGroupPriorities = [
                {num: 'None', value: ""},
                {num: 1, value: 1},
                {num: 2, value: 2}
            ];
            scope.alignments = [
                {name: '', displayName: 'None'},
                {name: "left", displayName: "Left"},
                {name: "right", displayName: "Right"},
                {name: "center", displayName: "Center"}
            ];
            scope.sorting = [
                {name: 'None', value: ''},
                {name: 'asc', value: 'asc'},
                {name: 'desc', value: 'desc'}
            ];
            scope.fieldTypes = ['none', 'string', 'number', 'date']
            scope.formats = [
                {name: "Currency", value: '$,.2f'},
                {name: "Integer", value: ',.0f'},
                {name: "Percentage", value: ',.2%'},
                {name: "Decimal1", value: ',.1f'},
                {name: "Decimal2", value: ',.2f'},
                {name: "None", value: ''}
            ];
            scope.hideOptions = [
                {name: 'Yes', value: 1},
                {name: 'No', value: ''}
            ];
            scope.addList = function (list) {
                list.isEdit = true;
                scope.previewTableHeaderName.push(list);
            };
            var tableDataSource = JSON.parse(scope.previewUrls)
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
            if (tableDataSource.dataSourceId.dataSourceType == "csv") {
                url = "admin/csv/getData?";
            }
            if (tableDataSource.dataSourceId.dataSourceType == "facebook") {
                url = "admin/proxy/getData?";
            }

            var dataSourcePassword;
            if (tableDataSource.dataSourceId.password) {
                dataSourcePassword = tableDataSource.dataSourceId.password;
            } else {
                dataSourcePassword = '';
            }
            $http.get(url + 'connectionUrl=' + tableDataSource.dataSourceId.connectionString +
                    "&dataSetId=" + tableDataSource.id +
                    "&driver=" + tableDataSource.dataSourceId.sqlDriver +
                    "&accountId=" + $stateParams.accountId +
                    "&startDate=" + $stateParams.startDate +
                    "&endDate=" + $stateParams.endDate +
                    '&username=' + tableDataSource.dataSourceId.userName +
                    '&password=' + dataSourcePassword +
                    '&port=3306&schema=vb&query=' + encodeURI(tableDataSource.query)).success(function (response) {
                scope.tableData = response.data;
                scope.tableList = response.columnDefs;
                console.log(response)
            })
            scope.deleteColumn = function ($index) {
                scope.previewTableHeaderName.splice($index, 1);
            }
            scope.save = function (column) {
                try {
                    scope.customStartDate = moment($('#widgetDateRange').data('daterangepicker').startDate).format('MM/DD/YYYY') ? moment($('#widgetDateRange').data('daterangepicker').startDate).format('MM/DD/YYYY') : $stateParams.startDate;//$scope.startDate.setDate($scope.startDate.getDate() - 1);

                    scope.customEndDate = moment($('#widgetDateRange').data('daterangepicker').endDate).format('MM/DD/YYYY') ? moment($('#widgetDateRange').data('daterangepicker').endDate).format('MM/DD/YYYY') : $stateParams.endDate;
                } catch (e) {

                }
                var widgetColumnsData = [];
                angular.forEach(scope.previewTableHeaderName, function (value, key) {
                    var hideColumn = value.columnHide;
                    if (value.groupPriority > 0) {
                        hideColumn = 1;
                    }

                    var columnData = {
                        id: value.id,
                        fieldName: value.fieldName,
                        displayName: value.displayName,
                        agregationFunction: value.agregationFunction,
                        groupPriority: isNaN(value.groupPriority) ? null : value.groupPriority,
                        xAxis: isNaN(value.xAxis) ? null : value.xAxis,
                        yAxis: isNaN(value.yAxis) ? null : value.yAxis,
                        sortOrder: value.sortOrder,
                        displayFormat: value.displayFormat,
                        alignment: value.alignment,
                        baseFieldName: value.baseFieldName,
                        fieldGenerationFields: value.fieldGenerationFields,
                        fieldGenerationFunction: value.fieldGenerationFunction,
                        fieldType: value.fieldType,
                        functionParameters: value.functionParameters,
                        remarks: value.remarks,
                        sortPriority: isNaN(value.sortPriority) ? null : value.sortPriority,
                        width: isNaN(value.width) ? null : value.width,
                        wrapText: value.wrapText,
                        xAxisLabel: value.xAxisLabel,
                        yAxisLabel: value.yAxisLabel,
                        columnHide: hideColumn,
                        search: value.search
                    };
                    widgetColumnsData.push(columnData);
                });
                if (scope.previewWidgetTitle == "Widget Title") {
                    scope.previewWidgetTitle = "";
                }

                var data = {
                    id: widget.id,
                    chartType: widget.chartType,
                    widgetTitle: scope.previewWidgetTitle,
                    widgetColumns: widgetColumnsData,
                    dataSourceId: widget.dataSourceId.id,
                    dataSetId: widget.dataSetId.id,
                    tableFooter: JSON.parse(scope.previewWidgetTable).tableFooter,
                    zeroSuppression: JSON.parse(scope.previewWidgetTable).zeroSuppression,
                    maxRecord: JSON.parse(scope.previewWidgetTable).maxRecord,
                    dateDuration: widget.dateDuration,
                    content: widget.content,
                    width: widget.width
                };

                $http({method: widget.id ? 'PUT' : 'POST', url: 'admin/ui/dbWidget/' + $stateParams.tabId, data: data}).success(function (response) {
                    $state.go("index.dashboard.widget", {productId: $stateParams.productId, accountId: $stateParams.accountId, accountName: $stateParams.accountName, tabId: $stateParams.tabId, startDate: $stateParams.startDate, endDate: $stateParams.endDate})
                });
            };
            scope.closeWidget = function () {
                widget = "";
                $state.go("index.dashboard.widget", {productId: $stateParams.productId, accountId: $stateParams.accountId, accountName: $stateParams.accountName, tabId: $stateParams.tabId, startDate: $stateParams.startDate, endDate: $stateParams.endDate})
            };
        }
    };
});
app.directive('ckEditor', function () {
    return {
        require: '?ngModel',
        link: function (scope, elm, attr, ngModel) {
            var ck = CKEDITOR.replace(elm[0], {
                removeButtons: 'About'
            });

            if (!ngModel)
                return;

            ck.on('pasteState', function () {
                scope.$apply(function () {
                    ngModel.$setViewValue(ck.getData());
                });
            });

            ngModel.$render = function (value) {
                ck.setData(ngModel.$viewValue);
            };
        }
    };
});

app.directive('customWidgetDateRange', function ($stateParams) {
    return{
        restrict: 'A',
        link: function (scope, element, attr) {
            //Date range as a button
            $(element[0]).daterangepicker(
                    {
                        ranges: {
//                        'Today': [moment(), moment()],
//                        'Yesterday': [moment().subtract(1, 'days'), moment().subtract(1, 'days')],
//                        'Last 7 Days': [moment().subtract(6, 'days'), moment()],
//                        'Last 30 Days': [moment().subtract(29, 'days'), moment()],
                            'This Month': [moment().startOf('month'), moment().endOf(new Date())],
                            'Last Month': [moment().subtract(1, 'month').startOf('month'), moment().subtract(1, 'month').endOf('month')]
                        },
                        startDate: $stateParams.startDate ? $stateParams.startDate : moment().subtract(29, 'days'),
                        endDate: $stateParams.endDate ? $stateParams.endDate : moment(),
                        maxDate: new Date(),
                    },
                    function (start, end) {
                        $('#widgetDateRange span').html(start.format('MM-DD-YYYY') + ' - ' + end.format('MM-DD-YYYY'));
                    }
            );


        }
    }
})
