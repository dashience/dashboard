app.controller('EditWidgetController', function ($scope, $http, $stateParams, localStorageService, $timeout, $filter, $state) {
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
    $http.get("admin/ui/dbWidget/" + $stateParams.tabId).success(function (response) {
        $scope.widgets = response;
        if ($stateParams.widgetId != 0) {
            $scope.editWidgetData.push($filter('filter')($scope.widgets, {id: $stateParams.widgetId})[0]);
            angular.forEach($scope.editWidgetData, function (value, key) {
                $scope.editWidget(value)
                $scope.buildQuery = value.queryFilter;
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
        {name: 'yes', value: "Yes"},
        {name: 'No', value: "No"}
    ]
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
        if (widget.dataSetId.dataSourceId.dataSourceType == "csv") {
            url = "admin/csv/getData?";
        }
        if (widget.dataSetId.dataSourceId.dataSourceType == "facebook") {
            url = "admin/proxy/getData?";
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

    $scope.editWidget = function (widget) {    //Edit widget
        tagWidgetId(widget)
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
        $scope.editChartType = null;
        widget.previewUrl = dataSet;
        widget.columns = [];
        var chartType = widget;
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
        console.log(column)
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
        console.log(widget)
        var chartType = widget;
        $timeout(function () {
            $scope.previewChart(chartType, widget)
        }, 50);
    };
    $scope.selectY1Axis = function (widget, y1data) {
        var groupVar = [];
        console.log(y1data);

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
        if (widget.chartType == 'stackedbar') {
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
                        val.groupField = y2data.indexOf(value) + 1;
                        console.log(val.groupField);
                    } else {
                        if (val.fieldName == y2data.removeItem) {
                            val.yAxis = null;
                            val.groupField = null;
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
        }
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
//                $scope.selectY1Axis(widget, column);
            }
            if (column.yAxis == 2) {
                $scope.setFormat(widget, column)
//                $scope.selectY2Axis(widget, column);
            }
        } else if (widget.chartType == 'pie') {
            if (column.xAxis == 1) {
                $scope.setFormat(widget, column)
//                $scope.selectPieChartX(widget, column);
            }
            if (column.yAxis == 1) {
                $scope.setFormat(widget, column)
//                $scope.selectPieChartY(widget, column);
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
            console.log(response)
            $scope.widgetTags = response;
            angular.forEach(response, function (value, key) {
                console.log(value)
                widget.tagName.push(value.tagId.tagName)
            });
        });
    }

//Query Builder
    var data = '{"group": {"operator": "AND","rules": []}}';
    function htmlEntities(str) {
        console.log(str)
        return String(str).replace(/</g, '&lt;').replace(/>/g, '&gt;');
    }
    function computed(group) {
        if (!group)
            return "";
        for (var str = "(", i = 0; i < group.rules.length; i++) {
            i > 0 && (str += " <strong>" + group.operator + "</strong> ");
            str += group.rules[i].group ?
                    computed(group.rules[i].group) :
                    group.rules[i].field + " " + htmlEntities(group.rules[i].condition) + " " + group.rules[i].data;
        }
        return str + ")";
    }
    $scope.json = null;
    $scope.filter = JSON.parse(data);
    $scope.$watch('filter', function (newValue) {
        $scope.json = JSON.stringify(newValue, null, 2);
        $scope.buildQuery = computed(newValue.group);
//        console.log(filterHtmlTag($scope.buildQuery))
    }, true);
    $scope.setFilterType = function (widget) {
        console.log($scope.buildQuery)
        $scope.editChartType = null;
        widget.queryFilter = $scope.buildQuery;
        widget.isFilter = true;
        var chartType = widget;
        $timeout(function () {
            $scope.previewChart(chartType, widget)
//            $scope.directiveQueryFn()
        }, 10);
    }
    $scope.setQueryFn = function (queryFn) {
        $scope.directiveQueryFn = queryFn;
    };
//    function filterHtmlTag(text) {
//        return  text ? String(text).replace(/<[^>]+>/gm, '') : '';
//    }


    $scope.save = function (widget) {
        try {
            $scope.customStartDate = moment($('#widgetDateRange').data('daterangepicker').startDate).format('MM/DD/YYYY') ? moment($('#widgetDateRange').data('daterangepicker').startDate).format('MM/DD/YYYY') : $stateParams.startDate; //$scope.startDate.setDate($scope.startDate.getDate() - 1);

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
            queryFilter: $scope.buildQuery
        };
        $http({method: widget.id ? 'PUT' : 'POST', url: 'admin/ui/dbWidget/' + $stateParams.tabId, data: data}).success(function (response) {
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
            $state.go("index.dashboard.widget", {productId: $stateParams.productId, accountId: $stateParams.accountId, accountName: $stateParams.accountName, tabId: $stateParams.tabId, startDate: $stateParams.startDate, endDate: $stateParams.endDate})
        });
    }
    ;

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

//    $scope.currentLocation = "index.dashboard.widget", {productId: $stateParams.productId, accountId: $stateParams.accountId, accountName: $stateParams.accountName, tabId: $stateParams.tabId, startDate: $stateParams.startDate, endDate: $stateParams.endDate};
    $scope.currentLocation = "\"index.dashboard.widget\", {productId:" + $stateParams.productId + ", accountId:" + $stateParams.accountId + ", accountName:" + $stateParams.accountName + ", tabId:" + $stateParams.tabId + ", startDate:" + $stateParams.startDate + ", endDate:" + $stateParams.endDate + "}";
    $scope.selectDateRangeName = function (widget) {
        $scope.editChart = widget;
        widget.dateRangeName = "Custom";
        widget.lastNdays = "";
        widget.lastNweeks = "";
        widget.lastNmonths = "";
        widget.lastNyears = "";
    };
//    $scope.selectCustomRange = function (isRange) {alert()
//        if (isRange == true) {
//            $scope.editChartType = null;
//            var chartType =$scope.editChart;
//            console.log()
//            console.log(chartType)
//            $timeout(function () {
//                $scope.previewChart(chartType, $scope.editChart)
//            }, 50);
//        }
//    }
//  
});
app.directive('queryBuilder', ['$compile', function ($compile) {
        return {
            restrict: 'E',
            scope: {
                group: '=',
                collection: '@'
            },
            templateUrl: '/queryBuilderDirective.html',
            compile: function (element, attrs) {
                var content, directive;
                content = element.contents().remove();
                return function (scope, element, attrs) {
                    scope.operators = [
                        {name: 'AND'},
                        {name: 'OR'}
                    ];
//                    console.log(scope.collection)
                    scope.columns = scope.collection;
                    var columnList = JSON.parse(scope.collection);
                    var filterList = [];
                    columnList.forEach(function (value, key) {
                        filterList.push({name: value.fieldName, type: value.fieldType})
                    });
//                    console.log(filterList)
                    scope.fields = filterList;
//                    scope.fields = [
//                        {name: 'Firstname', type: ''},
//                        {name: 'Lastname', type: ''},
//                        {name: 'Birthdate', type: ''},
//                        {name: 'City', type: ''},
//                        {name: 'Country', type: ''}
//                    ];

                    scope.conditions = [
                        {name: '='},
                        {name: '<>'},
                        {name: '<'},
                        {name: '<='},
                        {name: '>'},
                        {name: '>='},
                        {name: 'contains'}
                    ];
                    scope.addCondition = function () {
                        scope.group.rules.push({
                            condition: '=',
                            field: filterList[0].name,
                            data: ''
                        });
                    };
//                    console.log(scope.group)
//                    console.log(JSON.parse(scope.collection))
//                    var columnFieldType = JSON.parse(scope.collection);
//                    angular.forEach(columnFieldType, function (value, key) {
//                        angular.forEach(scope.group, function (val, key) {
//                            angular.forEach(val.rules, function (rule, key) {
//                            console.log(value.fieldName)
//                            console.log(value.rules);
//                            if (value.fieldName == val.field)
//                                if (value.fieldType == 'Number') {
//                                    scope.conditions = [
//                                        {name: '='},
//                                        {name: '<>'},
//                                        {name: '<'},
//                                        {name: '<='},
//                                        {name: '>'},
//                                        {name: '>='}
//                                    ];
//                                } else {
//                                    scope.conditions = [
//                                        {name: '='},
//                                        {name: '<>'},
//                                        {name: '<'},
//                                        {name: '<='},
//                                        {name: '>'},
//                                        {name: '>='},
//                                        {name: 'contains'}
//                                    ];
//                                }
//                            });
//                        });
//                    });
                    scope.removeCondition = function (index) {
                        scope.group.rules.splice(index, 1);
                    };
                    scope.addGroup = function () {
                        scope.group.rules.push({
                            group: {
                                operator: 'AND',
                                rules: []
                            }
                        });
                    };
                    scope.removeGroup = function () {
                        "group" in scope.$parent && scope.$parent.group.rules.splice(scope.$parent.$index, 1);
                    };
                    directive || (directive = $compile(content));
                    element.append(directive(scope, function ($compile) {
                        return $compile;
                    }));
                };
            }
        };
    }]);
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

app.directive('widgetPreviewTable', function ($http, $stateParams, $state, orderByFilter) {
    return{
        restrict: 'AE',
        scope: {
            setQueryFn: '&',
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
                "<div class='col-sm-6' ng-click='widgetTableEdit()'><input class='form-control' type='text'  ng-model='previewWidgetTitle'></div>" +
                "<div class='col-sm-4'><a ng-click='widgetTableSave()'><i class='fa fa-save'></i></a>" +
                "<a ng-click='widgetTableSave()'><i class='fa fa-close'></i></a></div>" +
                "</div>" +
                "</div>" +
                "</div>" +
                "</div>" + //End Panel Title
                "</div>" +
                "</div>" +
//                "<pre>{{previewTableHeader | json}}</pre>" +
//                " <pre ng-repeat='header in previewTableHeader'>{{header.displayName}} </pre>" +
                //Table
                "<div class=''>" +
                "<div class='table-responsive tbl-preview'>" +
                "<table  class='defaultTable table table-bordered table-hover'>" +
                "<thead>" +
                "<tr>" +
                "<th id='{{collectionField.displayName}}'  ng-repeat='collectionField in previewTableHeader'>" +
                "<div  ng-hide='collectionField.isEdit'>" +
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
                "<tr><th ng-repeat='collectionField in previewTableHeader'>" +
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
            scope.previewTableHeader = JSON.parse(scope.previewColumns);
            scope.listColumns = [];
            scope.listColumns = JSON.parse(scope.previewColumns);
            scope.previewWidgetTitle = JSON.parse(scope.previewWidget).widgetTitle;
            var widget = JSON.parse(scope.previewWidget);
            console.log(widget);
            console.log(widget.customStartDate);
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
                    "&dataSetReportName" + tableDataSource.reportName +
                    '&username=' + tableDataSource.dataSourceId.userName +
                    '&password=' + dataSourcePassword +
                    '&widgetId=' + widget.id +
                    '&url=' + tableDataSource.url +
                    '&port=3306&schema=vb&query=' + encodeURI(tableDataSource.query)).success(function (response) {
                console.log(response)
                scope.tableData = response.data;
                scope.tableList = response.columnDefs;
            });
            console.log(scope.tableData);
            console.log(scope.previewTableHeader);
            console.log(scope.filterReturnItem);
            $(document).ready(function () {
                $('.defaultTable').dragtable({
                    persistState: function (table) {

                        if (!window.sessionStorage)
                            return;
                        //var ss = window.sessionStorage;
                        table.el.find('th').each(function (i) {
                            if (this.id != '') {
                                table.sortOrder[this.id] = i;
                            }
                        });
                        //ss.setItem('tableorder', JSON.stringify(table.sortOrder));
                        //var object = eval('(' + window.sessionStorage.getItem('tableorder') + ')');
                        scope.mapJson(table.sortOrder);
                    },
                    clickDelay: 200,

                    // restoreState: eval('(' + window.sessionStorage.getItem('tableorder') + ')')
                });
            });
            scope.mapJson = function (object) {
                scope.draggedObject = [];
                scope.newHeaders = [];
                $.each(object, function (key, value) {
                    scope.draggedObject[value] = key;
                });
                console.log(scope.draggedObject);
                angular.forEach(scope.previewTableHeader, function (value, key) {
                    scope.filterReturnItem = orderByFilter(scope.previewTableHeader, function (item) {
                        return scope.draggedObject.indexOf(item.displayName)
                    });
                });
                console.log(scope.filterReturnItem);

//                scope.previewTableHeader = []
//                scope.$apply(function () {
//                    scope.previewTableHeader = scope.filterReturnItem;
//                });
////                scope.$apply();
//                angular.forEach(scope.previewTableHeader, function (value, key) {
//                    console.log("Drag Data")
//                    console.log(value.fieldName)
//                    var indexid = scope.previewTableHeader.indexOf(value)
////                    console.log(indexid)
//                });
//                sessionStorage.clear();
            };
            scope.deleteColumn = function (collectionField, $index) {
                console.log($index);
//                angular.forEach(scope.previewTableHeader, function (value, key) {
//                    console.log("Drag Data")
//                    console.log(value.fieldName)
//                    var indexid = scope.previewTableHeader.indexOf(value)
////                    console.log(indexid)
//                });
//                console.log(scope.previewTableHeader);
//                var index = scope.previewTableHeader.indexOf(collectionField);
//                //scope.previewTableHeader.splice(index, 1);
//             
//                console.log(scope.previewTableHeader);
//                return;
                if (typeof (scope.filterReturnItem) == "undefined")
                {
                    scope.previewTableHeader.splice($index, 1);
                } else {
                    console.log("1----------------------------------");
                    console.log(scope.previewTableHeader);
                    var dragDeletedIndexPos = scope.filterReturnItem.indexOf(collectionField);
                    console.log(dragDeletedIndexPos);
                    scope.filterReturnItem.splice(dragDeletedIndexPos, 1);
                    scope.previewTableHeader = scope.filterReturnItem;

                    console.log(scope.previewTableHeader);
//                    var returnItems = scope.previewTableHeader;
//                    scope.previewTableHeader = ""
//                    scope.previewTableHeader = returnItems;
//                    scope.$apply();
//                    var previewHeaderObject = scope.previewTableHeader[$index];
//                    console.log(previewHeaderObject);
//                    console.log(scope.filterReturnItem);
//                    var dragableHeaderIndex = scope.filterReturnItem.indexOf(previewHeaderObject);
//                    console.log("dragableHeaderIndex="+dragableHeaderIndex);
//                    scope.filterReturnItem.splice(dragableHeaderIndex, 1);
////                    scope.previewTableHeader = [];
//                    console.log(scope.filterReturnItem);
//                    scope.previewTableHeader = scope.filterReturnItem;
//                    console.log(scope.previewTableHeader);
                }
            };
            scope.save = function (column) {
                widget.dateRangeName = $("#dateRangeName").text().trim();
                if (widget.dateRangeName === "Custom") {
                    console.log("---->");
                    scope.lastNDays = "";
                    scope.lastNWeeks = "";
                    scope.lastNMonths = "";
                    scope.lastNYears = "";
                }
                console.log(scope.reloadUrl)
                console.log(scope.lastNDays);
                try {
                    scope.customStartDate = moment($('#widgetDateRange').data('daterangepicker').startDate).format('MM/DD/YYYY') ? moment($('#widgetDateRange').data('daterangepicker').startDate).format('MM/DD/YYYY') : $stateParams.startDate; //$scope.startDate.setDate($scope.startDate.getDate() - 1);

                    scope.customEndDate = moment($('#widgetDateRange').data('daterangepicker').endDate).format('MM/DD/YYYY') ? moment($('#widgetDateRange').data('daterangepicker').endDate).format('MM/DD/YYYY') : $stateParams.endDate;
                } catch (e) {

                }
                var widgetColumnsData = [];
//                console.log(scope.filterReturnItem)
                var saveWidgetColumnList = scope.filterReturnItem ? scope.filterReturnItem : scope.previewTableHeader;
                console.log(saveWidgetColumnList)
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
                    dateRangeName: widget.dateRangeName,
                    lastNdays: scope.lastNDays,
                    lastNweeks: scope.lastNWeeks,
                    lastNmonths: scope.lastNMonths,
                    lastNyears: scope.lastNYears,
                    customStartDate: scope.customStartDate,
                    customEndDate: scope.customEndDate,
                    content: widget.content,
                    width: widget.width,
                    queryFilter: widget.queryFilter
                };
                $http({method: widget.id ? 'PUT' : 'POST', url: 'admin/ui/dbWidget/' + $stateParams.tabId, data: data}).success(function (response) {
                    sessionStorage.clear();
                    $state.go("index.dashboard.widget", {productId: $stateParams.productId, accountId: $stateParams.accountId, accountName: $stateParams.accountName, tabId: $stateParams.tabId, startDate: $stateParams.startDate, endDate: $stateParams.endDate});
                });
            };
            if (widget.isFilter == true) {
                scope.setQueryFn({queryFn: scope.save()});
            }
            scope.closeWidget = function () {
                widget = "";
                sessionStorage.clear();
                $state.go("index.dashboard.widget", {productId: $stateParams.productId, accountId: $stateParams.accountId, accountName: $stateParams.accountName, tabId: $stateParams.tabId, startDate: $stateParams.startDate, endDate: $stateParams.endDate});
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
//            selectCustomRange: "&"
        },
        link: function (scope, element, attr) {
            $(document).ready(function (e) {
                $(".scheduler-list-style").click(function (e) {
                    e.stopPropagation();
                });
                console.log(JSON.parse(scope.widgetTableDateRange).customStartDate);
                console.log(JSON.parse(scope.widgetTableDateRange).customEndDate);
                var widget = JSON.parse(scope.widgetTableDateRange);
                var widgetStartDate = widget.customStartDate;//JSON.parse(scope.widgetTableDateRange).customStartDate;
                var widgetEndDate = widget.customEndDate;//JSON.parse(scope.widgetTableDateRange).customEndDate;
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
                $(".ranges ul").find("li").addClass("custom-picker");
                $(".custom-picker").click(function (e) {
                    angular.forEach(scope.previewTableHeader, function (value, key) {
                        console.log("Drag Data")
                        console.log(value.fieldName)
                        var indexid = scope.previewTableHeader.indexOf(value)
//                    console.log(indexid)
                    });
                    console.log(scope.previewTableHeader);
                    var index = scope.previewTableHeader.indexOf(collectionField);
                    //scope.previewTableHeader.splice(index, 1);
//                alert(index);
                    console.log(scope.previewTableHeader);
                    return;
//                e.bind();
                });
                $(".editWidgetDropDown").click(function (e) {
                    console.log("dropdown click event")
                    $(".scheduler-list-style").removeAttr("style");
                    $(".scheduler-list-style").css("display", "block");
                    $(".daterangepicker").css("display", "none");
//                        e.bind();
                });
                $(document).on("click", function (e) {
                    var selectedElement = e.target.className;
                    console.log(e.target.className);
                    console.log(selectedElement);
                    if (selectedElement == "custom-picker" ||
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
                    console.log("apply button click event triggered");
                    try {
                        scope.customStartDate = moment($('#widgetDateRange').data('daterangepicker').startDate).format('MM/DD/YYYY') ? moment($('#widgetDateRange').data('daterangepicker').startDate).format('MM/DD/YYYY') : $stateParams.startDate;//$scope.startDate.setDate($scope.startDate.getDate() - 1);
                        scope.customEndDate = moment($('#widgetDateRange').data('daterangepicker').endDate).format('MM/DD/YYYY') ? moment($('#widgetDateRange').data('daterangepicker').endDate).format('MM/DD/YYYY') : $stateParams.endDate;
                    } catch (e) {
                    }

                    $(".scheduler-list-style").hide();
//                    console.log(scope.reloadUrl)
                    $state.go("index.dashboard.widget", {productId: $stateParams.productId, accountId: $stateParams.accountId, accountName: $stateParams.accountName, tabId: $stateParams.tabId, startDate: $stateParams.startDate, endDate: $stateParams.endDate});

//                    $timeout(function () {
//                    }, 50);
//                    scope.editChartType = null;
                });


            });
        }//end of link function
    };
});

//                    selectWidgetDuration('Custom', widget);
