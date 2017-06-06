app.controller('EditWidgetController', function ($scope, $http, $stateParams, localStorageService, $timeout, $filter, $state, $rootScope) {
    $scope.editWidgetData = []
    $scope.permission = localStorageService.get("permission");
    $scope.accountId = $stateParams.accountId;
    $scope.accountName = $stateParams.accountName;
    $scope.productId = $stateParams.productId;
    $scope.tabId = $stateParams.tabId;
    $scope.startDate = $stateParams.startDate;
    $scope.endDate = $stateParams.endDate;
    $scope.widgets = [];
    //Tabs
    $scope.tab = 1;
    $scope.setTab = function (newTab) {
        $scope.tab = newTab;
    };
    $scope.isSet = function (tabNum) {


        return $scope.tab === tabNum;
    };
    $scope.dispQueryBuilder = false;
    $scope.resetQueryBuilder = function () {
        $scope.dispQueryBuilder = true;

    }
    $scope.saveQuery = function () {
        if ($('.query-builder').queryBuilder('getRules') != null) {
            $scope.setTab(1);
        } else {
            return;
        }
    }
//    $scope.clickBtn = function(){

//    }
    $scope.loadingEditWidget = true;
    $http.get("admin/ui/dbWidget/" + $stateParams.tabId).success(function (response) {
        $scope.loadingEditWidget = false;
//        $scope.widgets = response;
//        if ($stateParams.widgetId) {
//            $scope.editWidgetData.push($filter('filter')($scope.widgets, {id: $stateParams.widgetId})[0]);
//            angular.forEach($scope.editWidgetData, function (value, key) {
//                $scope.editWidget(value)
//            })
//        }
        $scope.widgets = response;
        if ($stateParams.widgetId != 0) {
            $scope.editWidgetData.push($filter('filter')($scope.widgets, {id: $stateParams.widgetId})[0]);
            angular.forEach($scope.editWidgetData, function (value, key) {
                $scope.editWidget(value)
//                $scope.buildQuery = value.jsonData;
            });
        } else {
            $scope.editWidgetData.push({width: 12, columns: []})
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

    ]; //Aggregation Type-Popup
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
    ];
    $scope.alignments = [
        {value: '', name: 'None'},
        {value: "left", name: "Left"},
        {value: "right", name: "Right"},
        {value: "center", name: "Center"}
    ];
    $scope.sorting = [
        {name: 'None', value: ''},
        {name: 'asc', value: 'asc'},
        {name: 'desc', value: 'desc'}
    ];
    $scope.tableWrapText = [
        {name: 'None', value: ''},
        {name: 'Yes', value: "yes"}
    ];
    $scope.fieldTypes = [
        {name: 'None', value: ''},
        {name: 'String', value: 'string'},
        {name: 'Number', value: 'number'},
        {name: 'Date', value: 'date'},
        {name: 'Day', value: 'day'}
    ];
    $scope.hideOptions = [
        {name: 'Yes', value: 1},
        {name: 'No', value: ''}
    ];
    $scope.isEditPreviewColumn = false;
    $scope.formats = [
        {name: "None", value: ''},
        {name: "Currency", value: '$,.2f'},
        {name: "Integer", value: ',.0f'},
        {name: "Percentage", value: ',.2%'},
        {name: "Decimal1", value: ',.1f'},
        {name: "Decimal2", value: ',.2f'},
        {name: "Time", value: 'H:M:S'},
        {name: "Star Rating", value: 'starRating'}
    ];
    $scope.chartAggregation = [
        {name: "None", value: ''},
        {name: 'Min', value: "min"},
        {name: 'Max', value: "max"}
    ];
    $scope.combinationChartTypes = [
        {name: 'None', value: ""},
        {name: 'Line Chart', value: "line"},
        {name: 'Area Chart', value: "area"},
        {name: 'Bar Chart', value: "bar"}

    ];
    $scope.gridLine = [
        {name: 'Yes', value: "Yes"},
        {name: 'No', value: "No"}
    ]
    $scope.selectWidgetDuration = function (dateRangeName, widget) {
        //scheduler.dateRangeName = dateRangeName;
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

    $http.get('admin/ui/dataSource').success(function (response) {
        $scope.dataSources = response;
    });
    $scope.selectDataSource = function (dataSourceName, widget) {
        if (!dataSourceName) {
            return;
        }
        $http.get('admin/ui/dataSet').success(function (response) {
            $scope.dataSets = [];
            angular.forEach(response, function (value, key) {
                if (value.dataSourceId.name == dataSourceName.name) {
                    $scope.dataSets.push(value);
                }
            });
        });
    };
    $scope.collectionField = {};
    $scope.dispName = function (currentColumn) {
        $scope.filterName = $filter('filter')($scope.collectionFields, {fieldName: currentColumn.fieldName})[0];
        currentColumn.displayName = $scope.filterName.displayName;
    };
    $scope.tableDef = function (widget) {
        if (widget.columns) {
            if (widget.dataSetId) {
                columnHeaderDef(widget)
            }
        } else {
            if (widget.dataSetId) {
                columnHeaderDef(widget)
            }
        }
    };
    function columnHeaderDef(widget) {
        var dataSourcePassword;
        if (!widget.dataSetId) {
            return;
        }
        if (widget.dataSetId.dataSourceId.password) {
            dataSourcePassword = widget.dataSetId.dataSourceId.password;
        } else {
            dataSourcePassword = '';
        }
        var url = "admin/proxy/getData?";
        if (widget.dataSetId.dataSourceId.dataSourceType == "sql") {
            url = "admin/proxy/getJson?url=../dbApi/admin/dataSet/getData&";
        }
        $http.get(url + 'connectionUrl=' + widget.dataSetId.dataSourceId.connectionString +
                "&dataSetId=" + widget.dataSetId.id +
                "&accountId=" + $stateParams.accountId +
                "&dataSetReportName=" + widget.dataSetId.reportName +
                "&driver=" + widget.dataSetId.dataSourceId.sqlDriver +
                "&startDate=" + $stateParams.startDate +
                "&endDate=" + $stateParams.endDate +
                '&username=' + widget.dataSetId.dataSourceId.userName +
                '&password=' + dataSourcePassword +
                '&url=' + widget.dataSetId.url +
                '&port=3306&schema=vb&query=' + encodeURI(widget.dataSetId.query) +
                "&fieldsOnly=true").success(function (response) {
            $scope.collectionFields = [];
//            widget.columns = response.columnDefs;
            $scope.collectionFields = response.columnDefs;
        });
    }

    $scope.editWidget = function (widget) {
        console.log(widget);
        tagWidgetId(widget);
        $scope.editPreviewTitle = false;
        $scope.y1Column = [];
        $scope.y2Column = [];
        $scope.tickerItem = [];
        $scope.groupingFields = [];
        $scope.tableDef(widget);
        $scope.selectedRow = widget.chartType;
        widget.previewUrl = widget.dataSetId; //widget.directUrl;
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
                }
            }
            if (val.yAxis == 2) {
                if (val.fieldName) {
                    $scope.y2Column.push(val);
                }
            }
            if (val.groupField) {
                $scope.groupingFields.push(val)
            }
        });
    };
    $scope.changeUrl = function (dataSet, widget) {
        if (!dataSet) {
            return;
        }
        widget.jsonData = null;
        widget.queryFilter = null;
        $scope.editChartType = null;
        widget.previewUrl = dataSet;
        $scope.dispQueryBuilder = false
        widget.columns = [];
        var chartType = widget;
        var url = "admin/proxy/getData?";
        if (dataSet.dataSourceId.dataSourceType == "sql") {
            url = "admin/proxy/getJson?url=../dbApi/admin/dataSet/getData&";
        }
//        if (dataSet.dataSourceId.dataSourceType == "csv") {
//            url = "admin/csv/getData?";
//        }
//        if (dataSet.dataSourceId.dataSourceType == "facebook") {
//            url = "admin/proxy/getData?";
//        }
        var dataSourcePassword;
        if (dataSet.dataSourceId.password) {
            dataSourcePassword = dataSet.dataSourceId.password;
        } else {
            dataSourcePassword = '';
        }
        $http.get(url + 'connectionUrl=' + dataSet.dataSourceId.connectionString +
                "&dataSetId=" + dataSet.id +
                "&accountId=" + $stateParams.accountId +
                "&dataSetReportName=" + dataSet.reportName +
                "&driver=" + dataSet.dataSourceId.sqlDriver +
                "&startDate=" + $stateParams.startDate +
                "&endDate=" + $stateParams.endDate +
                '&username=' + dataSet.dataSourceId.userName +
                '&password=' + dataSourcePassword +
                '&url=' + dataSet.url +
                '&port=3306&schema=vb&query=' + encodeURI(dataSet.query) +
                "&fieldsOnly=true").success(function (response) {
            $scope.collectionFields = [];
            widget.columns = response.columnDefs;
            angular.forEach(response.columnDefs, function (value, key) {
                $scope.collectionFields.push(value);
            });
            //$timeout(function () {
            $scope.previewChart(chartType, widget)
            // }, 50);
        });
    };
    $http.get("static/datas/panelSize.json").success(function (response) {      //Default Panel in Ui
        $scope.newWidgets = response;
    });
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
    $scope.selectedDataSource = function (selectedItem) {
        $scope.selectItem = selectedItem;
        selectedItems(selectedItem);
    };

    $scope.previewChart = function (chartType, widget) {
        $scope.resetQueryBuilder();
        $scope.showPreviewItems = chartType.type ? chartType.type : chartType.chartType;
        widget.chartType = chartType.type ? chartType.type : chartType.chartType; //Selected Chart type - Bind chart-type to showPreview()
        $scope.selectedRow = chartType.type ? chartType.type : chartType.chartType;
        $scope.editChartType = chartType.type ? chartType.type : chartType.chartType;
        $scope.previewChartUrl = widget.previewUrl;
        $scope.previewColumn = widget;
        if (chartType.type == 'text') {
            widget.dataSetId = '';
            widget.dataSourceId = '';
        }
        if (chartType.type == 'ticker') {
            $scope.tickerItem = '';
        }
    };
    //Max Record
    $scope.reloadMaxRecord = function (widget) {
        $scope.editChartType = null;
        var chartType = widget;
        $timeout(function () {
            $scope.previewChart(chartType, widget)
        }, 50);
    };
    //Set Charts
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
            $scope.previewChart(chartType, widget);
        }, 50);
    };
    $scope.selectX1Axis = function (widget, column) {
        $scope.editChartType = null;
        var exists = false;
        angular.forEach(widget.columns, function (value, key) {
            if (column.fieldName === value.fieldName) {
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
    $scope.selectY1Axis = function (widget, y1data) {
        var groupVar = [];
        if (widget.chartType == 'stackedbar') {
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
//                        val.groupField = widget.columns.indexOf(val.fieldName) + 1;
                        val.groupField = y1data.indexOf(value) + 1;
                        console.log(val.groupField);
                    } else {
                        if (val.fieldName == y1data.removeItem) {
                            val.yAxis = null;
                            val.groupField = null;
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
        }
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
                $scope.setFormat(widget, column)
            }
            if (column.yAxis == 2) {
                $scope.setFormat(widget, column)
            }
        } else if (widget.chartType == 'pie') {
            if (column.xAxis == 1) {
                $scope.setFormat(widget, column)
            }
            if (column.yAxis == 1) {
                $scope.setFormat(widget, column)
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
        } else {b
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
        console.log(widget.columns);
        console.log(yAxisItems);
        console.log(column);
        console.log(column.fieldName);
        if (yAxisItems.length > 0) {
            yAxisItems.removeItem = column.fieldName;
            $scope.selectY2Axis(widget, yAxisItems);
        } else {
            angular.forEach(widget.columns, function (val, key) {
                if (val.fieldName == column.fieldName && val.yAxis == column.yAxis) {
                    val.yAxis = null;
                }
            });
        }
        var chartType = widget;
        $timeout(function () {
            $scope.previewChart(chartType, widget)
        }, 50);
    };
    //Ticker Format
    $scope.ticker = function (widget, column) {
        $scope.editChartType = null;
        var newColumns = [];
        if (column.length == 0) {
            widget.columns = "";
        } else {
            angular.forEach(column, function (value, key) {
                angular.forEach($scope.collectionFields, function (val, header) {
                    if (val.fieldName === value.fieldName) {
                        val.displayFormat = value.displayFormat;
                        newColumns.push(val);
                    }
                });
                widget.columns = newColumns;
            });
        }
        var chartType = widget;
        $timeout(function () {
            $scope.previewChart(chartType, widget)
        }, 50);
    };
    $scope.removedByTicker = function (widget, column, tickerItem) {
        $scope.editChartType = null;
        $scope.ticker(widget, tickerItem);
        var chartType = widget;
        $timeout(function () {
            $scope.editChartType = "ticker";
        }, 50);
    };
    $scope.setFormat = function (widget, column) {
        $scope.editChartType = null;
        angular.forEach(widget.columns, function (value, key) {
            if (column.fieldName === value.fieldName) {
                value.displayFormat = column.displayFormat;
            }
        });
        var chartType = widget;
        $timeout(function () {
            $scope.previewChart(chartType, widget)
        }, 50);
    };
    $scope.setCombinationChartType = function (widget, column) {
        $scope.editChartType = null;
        angular.forEach(widget.columns, function (value, key) {
            if (column.fieldName === value.fieldName) {
                value.combinationType = column.combinationType;
            }
        });
        var chartType = widget;
        $timeout(function () {
            $scope.previewChart(chartType, widget)
        }, 50);
    }
//    $scope.filterData = function (filterQuery) {
//        $scope.jsonRules = filterQuery[0].list;
//        $scope.sqlQuery = filterQuery[0].query.sql;
//    }

    $scope.setGridLine = function (widget) {
        $scope.editChartType = null;
        var chartType = widget;
        $timeout(function () {
            $scope.previewChart(chartType, widget)
        }, 50);
    }
    $scope.tickerFormat = function (widget, format) {
        $scope.setFormat(widget, format)
    };
    //Stacked Bar Grouping
    $scope.selectGrouping = function (widget, groupingFields) {
        $scope.editChartType = null;
        var groups = [];
        angular.forEach(groupingFields, function (value, key) {
            $scope.fieldNames = value.fieldName;
            groups.push($scope.fieldNames);
        });
        angular.forEach(groupingFields, function (value, key) {
            angular.forEach(widget.columns, function (val, header) {
                if (value.fieldName === val.fieldName) {
                    val.groupField = groups.indexOf(value.fieldName) + 1;
                }
            });
        });
        var chartType = widget;
        $timeout(function () {
            $scope.previewChart(chartType, widget)
        }, 50);
    };
    //Removed Grouping
    $scope.removedByGrouping = function (widget, column, groupList) {
        angular.forEach(widget.columns, function (value, key) {
            if (value.fieldName == column.fieldName) {
                value.groupField = "";
            }
        });
        $scope.selectGrouping(widget, groupList)
    };
    $scope.tags = [];
    $http.get('admin/tag').success(function (response) {
        $scope.tagsGroups = response;
        angular.forEach(response, function (value, key) {
            $scope.tags.push(value.tagName)
        });
    });
    function tagWidgetId(widget) {
        widget.tagName = [];
        $http.get("admin/tag/widgetTag/" + widget.id).success(function (response) {
            $scope.widgetTags = response;
            angular.forEach(response, function (value, key) {
                widget.tagName.push(value.tagId.tagName)
            });
        });
    }

    $scope.save = function (widget) {
        if (widget.chartType != 'text') {
            if ($('.query-builder').queryBuilder('getRules')) {
                $scope.jsonData = JSON.stringify($('.query-builder').queryBuilder('getRules'));
                $scope.queryFilter = $('.query-builder').queryBuilder('getSQL', false, true).sql;
            }
        }
        widget.dateRangeName = $("#dateRangeName").text().trim();
        try {
            $scope.customStartDate = widget.dateRangeName !== "Select Date Duration" && widget.dateRangeName !== "None" ? moment($('#widgetDateRange').data('daterangepicker').startDate).format('MM/DD/YYYY') : $stateParams.startDate; //$scope.startDate.setDate($scope.startDate.getDate() - 1);
            $scope.customEndDate = widget.dateRangeName !== "Select Date Duration" && widget.dateRangeName !== "None" ? moment($('#widgetDateRange').data('daterangepicker').endDate).format('MM/DD/YYYY') : $stateParams.endDate;
        } catch (e) {

        }
        console.log(widget.dateRangeName);
        console.log($scope.customStartDate);
        console.log($scope.customEndDate);
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
                fieldType: value.type ? value.type : value.fieldType,
                functionParameters: value.functionParameters,
                remarks: value.remarks,
                sortPriority: isNaN(value.sortPriority) ? null : value.sortPriority,
                width: isNaN(value.width) ? null : value.width,
                wrapText: value.wrapText,
                xAxisLabel: value.xAxisLabel,
                yAxisLabel: value.yAxisLabel,
                columnHide: hideColumn,
                search: value.search,
                groupField: value.groupField,
                combinationType: value.combinationType
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
            isGridLine: widget.isGridLine,
            customStartDate: $scope.customStartDate, //widget.customStartDate,
            customEndDate: $scope.customEndDate, //widget.customEndDate
            jsonData: $scope.jsonData ? $scope.jsonData : null,
            queryFilter: $scope.queryFilter ? $scope.queryFilter : null
        };
        $http({method: widget.id ? 'PUT' : 'POST', url: 'admin/ui/dbWidget/' + $stateParams.tabId, data: data}).success(function (response) {
            console.log(response)
            if (widget.tagName) {
                var tag = widget.tagName.map(function (value, key) {
                    if (value) {
                        return value;
                    }
                }).join(',');
                var tagData = {
                    tagName: tag,
                    widgetId: widget.id
                };
                $http({method: 'POST', url: "admin/tag/widgetTag", data: tagData});
            }

//                $scope.editWidgetData = response;
//                angular.$apply()
            $rootScope.goBack()
            //$state.go("index.dashboard.widget", {productId: $stateParams.productId, accountId: $stateParams.accountId, accountName: $stateParams.accountName, tabId: $stateParams.tabId, startDate: $stateParams.startDate, endDate: $stateParams.endDate})
        });
    }

    $scope.closeWidget = function (widget) {
        $scope.widget = "";
        $state.go("index.dashboard.widget", {productId: $stateParams.productId, accountId: $stateParams.accountId, accountName: $stateParams.accountName, tabId: $stateParams.tabId, startDate: $stateParams.startDate, endDate: $stateParams.endDate})
    };
    //Remove Tags
    $scope.removedTags = function (deletedItem) {
        var tags = $scope.widgetTags;
        tags.forEach(function (value, key) {
            if (value.tagId.tagName === deletedItem) {
                $http({method: 'DELETE', url: "admin/tag/widgetTag/" + value.id});
            }
        });
    };

    $scope.selectDateRangeName = function (widget) {
        $scope.editChart = widget;
        widget.dateRangeName = "Custom";
        widget.lastNdays = "";
        widget.lastNweeks = "";
        widget.lastNmonths = "";
        widget.lastNyears = "";
    };
});
app.filter('xAxis', [function () {
        return function (chartXAxis) {
            var xAxis = ['', 'x-1'];
            return xAxis[chartXAxis];
        };
    }]);
app.filter('yAxis', [function () {
        return function (chartYAxis) {
            var yAxis = ['', 'y-1', 'y-2'];
            return yAxis[chartYAxis];
        };
    }]);
app.filter('hideColumn', [function () {
        return function (chartYAxis) {
            var hideColumn = ['No', 'Yes'];
            return hideColumn[chartYAxis];
        };
    }]);
app.directive('widgetPreviewTable', function ($http, $stateParams, $state, orderByFilter, $rootScope) {
    return{
        restrict: 'AE',
        scope: {
            saveQueryFn: '&',
            previewUrls: '@',
            previewColumns: '@',
            previewWidget: '@',
            previewWidgetTable: '@',
            aggregationsTypes: '@',
            groupPriorities: '@',
            sortingFields: '@',
            fieldTypes: '@',
            displayFormats: '@',
            displayAlignments: '@',
            hideOptions: '@',
            currentUrl: '@',
            reloadUrl: '@',
            lastNDays: '@',
            lastNWeeks: '@',
            lastNMonths: '@',
            lastNYears: '@'
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
                "<ul class='dropdown-menu'>" +
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
                "<div class='col-sm-6' ng-click='widgetTableEdit()'><input class='form-control' type='text'  ng-model='previewWidgetTitle'></div>" +
                "<div class='col-sm-4'><a ng-click='widgetTableSave()'><i class='fa fa-save'></i></a>" +
                "<a ng-click='widgetTableSave()'><i class='fa fa-close'></i></a></div>" +
                "</div>" +
                "</div>" +
                "</div>" +
                "</div>" + //End Panel Title
                "</div>" +
                "</div>" +
                //Table
                "<div class=''>" +
                "<div class='table-responsive tbl-preview'>" +
                "<table  class='table table-bordered table-hover'>" +
                "<thead>" +
                "<tr ng-model='previewTableHeader' ui-sortable='sortableOptions'>" +
                "<th ng-repeat='collectionField in previewTableHeader'>" +
                "<div ng-hide='collectionField.isEdit'>" +
                "<div class='preview-table-settings' ng-hide='collectionField.isEdit'>" +
                "<a ng-click='collectionField.isEdit = true'>{{collectionField.displayName}}</a>" +
                "</div></div>" +
                //column Edit
                "<div ng-show='collectionField.isEdit'><input class='form-control-btn' ng-model='collectionField.displayName'>" +
                "<a class='btn btn-default btn-xs' ng-click='collectionField.isEdit=false'><i class='fa fa-save'></i></a>" +
                "<a class='btn btn-default btn-xs' ng-click='collectionField.isEdit=false'><i class='fa fa-close'></i></a></div>" +
//                "</th>" +
//                "</tr>" +
//                "<tr><th ng-repeat='collectionField in previewTableHeader'>" +
                "<button class='settings btn btn-default btn-xs'" +
                "ns-popover=''" +
                "ns-popover-template='close'" +
                "ns-popover-trigger='click'" +
                "ns-popover-theme='ns-popover-tooltip-theme'" +
                "ns-popover-timeout='1000'" +
                "ns-popover-hide-on-inside-click='false'" +
                "ns-popover-hide-on-outside-click='true'" +
                "ns-popover-hide-on-button-click='true'><i class='fa fa-cog'></i></button>" +
                //Start DropDown List
                " <script type='text/ng-template' id='close'>" +
                "<div class='triangle'></div>" +
                "<div class='ns-popover-tooltip'>" +
                "<form class='form-inline'>" +
                "<ul>" +
                //Aggregation Function
                "<li class='input-group col-sm-12'>" +
                "<label>Aggregation function</label>" +
                "<select class='form-control' ng-model='collectionField.agregationFunction'>" +
                "<option ng-repeat='selectAggregation in tableAggregations' value='{{selectAggregation.value}}'>{{selectAggregation.name}}</option>" +
                "</select>" +
                "</li>" +
                //Group Priority
                "<li class='input-group col-sm-12'>" +
                "<label>Group priority</label>" +
                "<select ng-model='collectionField.groupPriority' class='form-control'>" +
                "<option ng-repeat='selectGroupPriority in tableGrouping' value='{{selectGroupPriority.value}}'>{{selectGroupPriority.num}}</option>" +
                "</select>" +
                "</li>" +
                //Sorting
                "<li class='input-group col-sm-12'>" +
                "<label>Sort</label>" +
                "<select ng-model='collectionField.sortOrder' class='form-control'>" +
                "<option ng-repeat='sort in tableSorting' value='{{sort.value}}'>{{sort.name}}</option>" +
                "</select>" +
                "</li>" +
                //Field Type
                "<li class='input-group col-sm-12'><label>Field Type</label>" +
                "<select ng-model='collectionField.fieldType' class='form-control'>" +
                "<option ng-repeat='fieldType in tableFieldTypes' value='{{fieldType.value}}'>{{fieldType.name}}</option>" +
                "</select>" +
                "</li>" +
                //Display Format
                "<li class='input-group col-sm-12'>" +
                "<label>Format</label>" +
                "<select ng-model='collectionField.displayFormat' class='form-control'>" +
                "<option ng-repeat='format in tableDisplayFormats' value='{{format.value}}'>{{format.name}}</option>" +
                "</select>" +
                "</li>" +
                //Alignment
                "<li class='input-group col-sm-12'>" +
                "<label>Alignment</label>" +
                "<select ng-model='collectionField.alignment' class='form-control'>" +
                "<option ng-repeat='alignment in tableAlignments' value='{{alignment.value}}'>{{alignment.name}}</option>" +
                "</select>" +
                "</li>" +
                //ColumnHide
                "<li class='input-group col-sm-12'>" +
                "<label>ColumnHide</label>" +
                "<select ng-model='collectionField.columnHide' class='form-control'>" +
                "<option ng-repeat='hideOption in tableHideOptions' value='{{hideOption.value}}'>{{hideOption.name}}</option>" +
                "</select>" +
                "</li>" +
                //Search Box
                "<li>" +
                "<input type='checkbox' class='search-check' ng-model='collectionField.search'>" +
                "<label class='search-label'>Search</label>" +
                "</li>" +
                "</ul>" +
                "<button class='btn btn-info btn-sm' ng-click='hidePopover()'>Close</button>&nbsp;" +
                "<button class='btn btn-warning btn-sm' ng-click='deleteColumn(collectionField, $index); hidePopover()'>Delete</button>" +
//                "<button class='btn btn-warning btn-sm' ng-click='deleteColumn(collectionField, $index); hidePopover()'>{{collectionField.displayName}} Delete</button>" +
                "</form>" +
                "</div>" +
                "</script>" +
                //End Dropdownlist
                "</th></tr>" +
                " </thead>" +
                //colum Fields
                "<tbody>" +
                "<tr ng-repeat='tablelist in tableData'>" +
                "<td ng-repeat='collectionField in previewTableHeader'>" +
                "<div>{{tablelist[collectionField.fieldName]}}</div>" +
                "</td>" +
                "</tr>" +
                "</tbody>" +
                "</table> " +
                "</div>" +
                "</div>" +
                "<div class='preview-footer'>" +
                "<button data-dismiss='modal' class='btn btn-default' ng-click='closeWidget()'><span class='fa fa-times'></span> Cancel</button>" +
                "<button data-dismiss='modal' class='btn btn-info' ng-click='save()'><span class='fa fa-save'></span> Save</button>" +
                "</div>" +
                "</div>",
        link: function (scope, attrs) {
            scope.tableAggregations = JSON.parse(scope.aggregationsTypes);
            scope.tableGrouping = JSON.parse(scope.groupPriorities);
            scope.tableSorting = JSON.parse(scope.sortingFields);
            scope.tableDisplayFormats = JSON.parse(scope.displayFormats);
            scope.tableAlignments = JSON.parse(scope.displayAlignments);
            scope.tableHideOptions = JSON.parse(scope.hideOptions);
            scope.tableFieldTypes = JSON.parse(scope.fieldTypes)
            scope.widgetTableSave = function () {
                scope.editPreviewTitle = false;
            };
            scope.widgetTableEdit = function ()
            {
                scope.editPreviewTitle = true;
            };

            scope.sortableOptions = {
                start: function (event, ui) {
                    console.log('start1');
//                    console.log(ui);
                },
                stop: function (event, ui) {
                    console.log('stop1');
//                    console.log(ui);
                }
            };

            scope.listColumns = [];
            scope.listColumns = JSON.parse(scope.previewColumns);
            scope.previewWidgetTitle = JSON.parse(scope.previewWidget).widgetTitle;
            var widget = JSON.parse(scope.previewWidget);
            scope.addList = function (list) {
                list.isEdit = true;
                scope.previewTableHeader.push(list);
                $('.defaultTable').dragtable();
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
            };
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
            $http.get(url + 'connectionUrl=' + tableDataSource.dataSourceId.connectionString +
                    "&dataSetId=" + tableDataSource.id +
                    "&driver=" + tableDataSource.dataSourceId.sqlDriver +
                    "&accountId=" + $stateParams.accountId +
                    "&startDate=" + $stateParams.startDate +
                    "&endDate=" + $stateParams.endDate +
                    "&dataSetReportName=" + tableDataSource.reportName +
                    '&username=' + tableDataSource.dataSourceId.userName +
                    '&password=' + dataSourcePassword +
                    '&widgetId=' + (widget.id ? widget.id : '') +
                    '&url=' + tableDataSource.url +
                    '&port=3306&schema=vb&query=' + encodeURI(tableDataSource.query)).success(function (response) {
                scope.tableData = response.data;
                scope.tableList = response.columnDefs;
            });
            scope.deleteColumn = function (collectionField, index) {
                scope.previewTableHeader.splice(index, 1)
            };
            scope.previewTableHeader = JSON.parse(scope.previewColumns);
            scope.save = function () {
                if ($('.query-builder').queryBuilder('getRules')) {
                    scope.jsonRules = JSON.stringify($('.query-builder').queryBuilder('getRules'));
                    scope.queryFilter = $('.query-builder').queryBuilder('getSQL', false, true).sql;
                }
                widget.dateRangeName = $("#dateRangeName").text().trim();
                if (widget.dateRangeName === "Custom") {
                    scope.lastNDays = "";
                    scope.lastNWeeks = "";
                    scope.lastNMonths = "";
                    scope.lastNYears = "";
                }
                try {
                    scope.customStartDate = widget.dateRangeName !== "Select Date Duration" && widget.dateRangeName !== "None" ? moment($('#widgetDateRange').data('daterangepicker').startDate).format('MM/DD/YYYY') : $stateParams.startDate; //$scope.startDate.setDate($scope.startDate.getDate() - 1);
                    scope.customEndDate = widget.dateRangeName !== "Select Date Duration" && widget.dateRangeName !== "None" ? moment($('#widgetDateRange').data('daterangepicker').endDate).format('MM/DD/YYYY') : $stateParams.endDate;
                } catch (e) {

                }
                console.log(widget.dateRangeName);
                console.log(scope.customStartDate);
                console.log(scope.customEndDate);
                var widgetColumnsData = [];
                var saveWidgetColumnList = scope.previewTableHeader;
                angular.forEach(saveWidgetColumnList, function (value, key) {
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
                        fieldType: value.type ? value.type : value.fieldType,
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
                    dateRangeName: widget.dateRangeName,
                    lastNdays: scope.lastNDays,
                    lastNweeks: scope.lastNWeeks,
                    lastNmonths: scope.lastNMonths,
                    lastNyears: scope.lastNYears,
                    customStartDate: scope.customStartDate,
                    customEndDate: scope.customEndDate,
                    content: widget.content,
                    width: widget.width,
                    jsonData: scope.jsonRules ? scope.jsonRules : null,
                    queryFilter: scope.queryFilter ? scope.queryFilter : null
                };
                $http({method: widget.id ? 'PUT' : 'POST', url: 'admin/ui/dbWidget/' + $stateParams.tabId, data: data}).success(function (response) {
                    sessionStorage.clear();
                    $rootScope.goBack();
                    //$state.go("index.dashboard.widget", {productId: $stateParams.productId, accountId: $stateParams.accountId, accountName: $stateParams.accountName, tabId: $stateParams.tabId, startDate: $stateParams.startDate, endDate: $stateParams.endDate});
                });
            };
//            scope.saveQueryFn({savequeryFn: scope.save});
            scope.closeWidget = function () {
                widget = "";
                sessionStorage.clear();
                $rootScope.goBack();
//                $state.go("index.dashboard.widget", {productId: $stateParams.productId, accountId: $stateParams.accountId, accountName: $stateParams.accountName, tabId: $stateParams.tabId, startDate: $stateParams.startDate, endDate: $stateParams.endDate});
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
app.directive('customWidgetDateRange', function ($stateParams, $timeout) {
    return{
        restrict: 'A',
        scope: {
            widgetTableDateRange: '@',
        },
        link: function (scope, element, attr) {
//            $(document).ready(function (e) {
            $(".scheduler-list-style").click(function (e) {
                e.stopPropagation();
            });
            var widget = JSON.parse(scope.widgetTableDateRange);
            var widgetStartDate = widget.customStartDate; //JSON.parse(scope.widgetTableDateRange).customStartDate;
            var widgetEndDate = widget.customEndDate; //JSON.parse(scope.widgetTableDateRange).customEndDate;
            //Date range as a button
            $(element[0]).daterangepicker(
                    {
                        ranges: {
                            'Today': [moment(), moment()],
                            'Yesterday': [moment().subtract(1, 'days'), moment().subtract(1, 'days')],
                            'Last 7 Days': [moment().subtract(6, 'days'), moment()],
                            'Last 14 Days ': [moment().subtract(13, 'days'), moment()],
                            'Last 30 Days': [moment().subtract(29, 'days'), moment()],
                            'This Week (Sun - Today)': [moment().startOf('week'), moment().endOf(new Date())],
//                        'This Week (Mon - Today)': [moment().startOf('week').add(1, 'days'), moment().endOf(new Date())],
                            'Last Week (Sun - Sat)': [moment().subtract(1, 'week').startOf('week'), moment().subtract(1, 'week').endOf('week')],
//                        'Last 2 Weeks (Sun - Sat)': [moment().subtract(2, 'week').startOf('week'), moment().subtract(1, 'week').endOf('week')],
//                        'Last Week (Mon - Sun)': [moment().subtract(1, 'week').startOf('week').add(1, 'days'), moment().subtract(1, 'week').add(1, 'days').endOf('week').add(1, 'days')],
//                        'Last Business Week (Mon - Fri)': [moment().subtract(1, 'week').startOf('week').add(1, 'days'), moment().subtract(1, 'week').add(1, 'days').endOf('week').subtract(1, 'days')],
                            'This Month': [moment().startOf('month'), moment().endOf(new Date())],
                            'Last Month': [moment().subtract(1, 'month').startOf('month'), moment().subtract(1, 'month').endOf('month')],
//                        'Last 2 Months': [moment().subtract(2, 'month').startOf('month'), moment().subtract(1, 'month').endOf('month')],
//                        'Last 3 Months' : [moment().subtract(3, 'month').startOf('month'), moment().subtract(1, 'month').endOf('month')],
                            'This Year': [moment().startOf('year'), moment().endOf(new Date())],
                            'Last Year': [moment().subtract(1, 'year').startOf('year'), moment().subtract(1, 'year').endOf('year')],
//                        'Last 2 Years': [moment().subtract(2, 'year').startOf('year'), moment().subtract(1, 'year').endOf('year')]
//                        'Last 3 Years': [moment().subtract(3, 'year').startOf('year'), moment().subtract(1, 'year').endOf('year')]
                        },
                        startDate: widgetStartDate ? widgetStartDate : moment().subtract(29, 'days'),
                        endDate: widgetEndDate ? widgetEndDate : moment(),
                        maxDate: new Date()
                    },
                    function (startDate, endDate) {
                        $('#widgetDateRange span').html(startDate.format('MM-DD-YYYY') + ' - ' + endDate.format('MM-DD-YYYY'));
                    }
            );
            $(".ranges ul").find("li").addClass("custom-pickers");
            $(".custom-pickers").click(function (e) {
                $(".scheduler-list-style").hide();
                scope.$apply();
            });
            $(".editWidgetDropDown").click(function (e) {
                $(".scheduler-list-style").removeAttr("style");
                $(".scheduler-list-style").css("display", "block");
                $(".daterangepicker").css("display", "none");
//                        e.bind();
            });
            $(".date-range-none").click(function (e) {
                $(".scheduler-list-style").css("display", "none");
            });
            $(document).on("click", function (e) {
                var selectedElement = e.target.className;
                if (selectedElement == "custom-pickers" ||
                        selectedElement == "fa fa-chevron-left glyphicon glyphicon-chevron-left" ||
                        selectedElement == "month" ||
                        selectedElement == "fa fa-chevron-right glyphicon glyphicon-chevron-right" ||
                        selectedElement == "next available" ||
                        selectedElement == "input-mini form-control active" ||
                        selectedElement == "calendar-table" || selectedElement == "table-condensed" ||
                        selectedElement == "daterangepicker_input")
                {
                    $(".scheduler-list-style").css("display", "block");
                } else {
                    $(".scheduler-list-style").css("display", "none");
                }
            });
            $(".applyBtn").click(function (e) {
                try {
                    scope.customStartDate = moment($('#widgetDateRange').data('daterangepicker').startDate).format('MM/DD/YYYY') ? moment($('#widgetDateRange').data('daterangepicker').startDate).format('MM/DD/YYYY') : $stateParams.startDate; //$scope.startDate.setDate($scope.startDate.getDate() - 1);
                    scope.customEndDate = moment($('#widgetDateRange').data('daterangepicker').endDate).format('MM/DD/YYYY') ? moment($('#widgetDateRange').data('daterangepicker').endDate).format('MM/DD/YYYY') : $stateParams.endDate;
                } catch (e) {
                }

                $(".scheduler-list-style").hide(); //                    
            });
//            });
        }
    };
});
app.directive('jqueryQueryBuilder', function ($stateParams, $timeout) {
    return{
        restrict: 'A',
        scope: {
            queryData: '@',
        },
        link: function (scope, element, attr) {
            scope.columns = scope.queryData;
            var jsonFilter = JSON.parse(scope.queryData);
            var columnList = JSON.parse(scope.queryData);
            var filterList = [];
            columnList.columns.forEach(function (value, key) {
                var typeOfValue = value.type ? value.type : value.fieldType;
                if (typeOfValue == 'number') {
                    scope.fieldsType = "integer";
                } else if (typeOfValue == 'string') {
                    scope.fieldsType = "string";
                } else if (typeOfValue == 'Date') {
                    scope.fieldsType = "date";
                } else if (typeOfValue == 'day') {
                    scope.fieldsType = "string";
                } else {
                    scope.fieldsType = value.fieldType;
                }
                filterList.push({id: value.fieldName, label: value.fieldName, type: scope.fieldsType})
            });
            scope.buildQuery = filterList;
            if (jsonFilter.jsonData != null) {
                scope.jsonBuild = JSON.parse(jsonFilter.jsonData);
            }

            scope.buildQuery = filterList;
            if (jsonFilter.jsonData != null) {
                scope.jsonBuild = JSON.parse(jsonFilter.jsonData);
            }

            $(document).ready(function ()
            {
                $(element[0]).queryBuilder(
                        {
                            plugins: ['bt-tooltip-errors'],
                            filters: filterList,
                            rules: scope.jsonBuild ? scope.jsonBuild : null
                        });
                $('#btn-clear').on('click', function () {
                    $(element[0]).queryBuilder('reset');
                });
                $('#btn-reset').on('click', function () {
                    $(element[0]).queryBuilder('setRules', scope.jsonBuild);
                });
                $('#btn-set').on('click', function () {
                    $(element[0]).queryBuilder('setRules', scope.jsonBuild);
                });
            });
        }

    };
});
