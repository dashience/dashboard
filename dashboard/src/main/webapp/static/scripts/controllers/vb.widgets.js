function isNumber(n) {
    return !isNaN(parseFloat(n)) && isFinite(n);
}

function formatBySecond(second) {
    var minutes = "0" + Math.floor(second / 60);
    var seconds = "0" + (second - minutes * 60);
    var hours = "0" + Math.floor(minutes / 60);
    return hours.substr(-2) + " : " + minutes.substr(-2) + " : " + seconds.substr(-2);
}

function dashboardFormat(column, value) {
    if (column.fieldType === "date") {
        return value;
    }
    if (column.fieldType === "string") {
        return value;
    }
    if (column.displayFormat == null) {
        return value;
    }
    if (column && column.displayFormat) {
        if (column.displayFormat.indexOf("%") > -1) {
            return d3.format(column.displayFormat)(value / 100);
        } else if (column.displayFormat == 'H:M:S') {
            return formatBySecond(parseInt(value))
        } else {
            return d3.format(column.displayFormat)(value);
        }
    }
}

function dateConvert(fromFormat, toFormat, value) {
    return moment(value, fromFormat).format(toFormat);
    // return value;
}

app.controller('WidgetController', function ($scope, $http, $stateParams, $timeout, $filter, $cookies, localStorageService, $rootScope, $state, $window, $interval) {
    $scope.dispHideBuilder = true;
    $scope.widgets = [];
    $scope.tags = [];
    $scope.collectionFields = [];
    $scope.dragEnabled = true;
    $scope.showFilter = false;
    $scope.showColumnDefs = false;
    $scope.showDateRange = false;
    $scope.permission = localStorageService.get("permission");
    $scope.accountID = $stateParams.accountId;
    $scope.accountName = $stateParams.accountName;
    $scope.productID = $stateParams.productId;
    $scope.widgetTabId = $stateParams.tabId;
    $scope.widgetStartDate = $stateParams.startDate;
    $scope.widgetEndDate = $stateParams.endDate;
    $scope.userId = $cookies.getObject("userId");
    $scope.templateId = $stateParams.templateId;
    $scope.widgetDataSetColumnsDefs = [];

    if ($scope.permission.createReport === true) {
        $scope.showCreateReport = true;
    } else {
        $scope.showCreateReport = false;
    }

    $http.get('admin/ui/dashboardTemplate/' + $stateParams.productId).success(function (response) {
        $scope.templates = response;
        var template = "";
        if ($stateParams.templateId != 0) {
            template = $filter('filter')(response, {id: $stateParams.templateId})[0];
        }
        $scope.selectTemplate.selected = template;
        var templateUserId = template ? template.userId : "";
        if (!templateUserId) {
            return;
        } else {
            $scope.templateUserId = template.userId.id;
        }
    });

    $http.get("admin/report/reportWidget").success(function (response) {
        $scope.reportWidgets = response;
    });

    $http.get('admin/report/getReport').success(function (response) {
        $scope.reportList = response;
    });

    $http.get('admin/ui/dataSource').success(function (response) {
        $scope.dataSources = response;
        console.log($scope.dataSources);
    });

    $http.get('static/datas/imageUrl.json').success(function (response) {       //Popup- Select Chart-Type Json
        $scope.chartTypes = response;
    });

    $http.get('admin/tag').success(function (response) {
        $scope.tags = response;
    });
    $scope.downloadXLSByWidget = function (widget) {
        console.log(widget)
        var fileName;
        var name = widget.widgetTitle;
        if (name) {
            fileName = name.split(' ').join("");
        } else {
            fileName = "Skyzone"
        }
        var url = "admin/proxy/downloadData?";
        var setProductSegment;
        var setTimeSegment;
        var setNetworkType;

        if (widget.productSegment && widget.productSegment.type) {
            setProductSegment = widget.productSegment.type;
        } else {
            setProductSegment = widget.productSegment;
        }

        if (widget.timeSegment && widget.timeSegment.type) {
            setTimeSegment = widget.timeSegment.type;
        } else {
            setTimeSegment = widget.timeSegment;
        }

        if (widget.networkType && widget.networkType.type) {
            setNetworkType = widget.networkType.type;
        } else {
            setNetworkType = widget.networkType;
        }

        var dataSourcePassword;
        if (!widget.dataSetId) {
            return;
        }
        if (widget.dataSetId.dataSourceId.password) {
            dataSourcePassword = widget.dataSetId.dataSourceId.password;
        } else {
            dataSourcePassword = '';
        }

        window.open("admin/proxy/downloadData?connectionUrl=" +
                widget.dataSetId.dataSourceId.connectionString +
                "&dataSetId=" + widget.dataSetId.id +
                "&accountId=" + (widget.accountId ? (widget.accountId.id ? widget.accountId.id : widget.accountId) : $stateParams.accountId) +
                "&userId=" + (widget.dataSetId.userId ? widget.dataSetId.userId.id : null) +
                "&driver=" + widget.dataSourceId.sqlDriver +
                "&productSegment=" + setProductSegment +
                "&timeSegment=" + setTimeSegment +
                "&networkType=" + setNetworkType +
                "&startDate=" + $stateParams.startDate +
                "&endDate=" + $stateParams.endDate +
                '&username=' + widget.dataSetId.dataSourceId.userName +
                "&dataSetReportName=" + widget.dataSetId.reportName +
                '&password=' + dataSourcePassword +
                '&widgetId=' + widget.id +
                '&url=' + widget.dataSetId.url +
                '&port=3306&schema=vb&query=' + encodeURI(widget.dataSetId.query));
    };

    $scope.networkTypes = [
        {
            type: 'SEARCH',
            name: 'Search'
        },
        {
            type: 'CONTENT',
            name: 'Content'
        },
        {
            type: 'YOUTUBE_SEARCH',
            name: 'Youtube Search'
        },
        {
            type: 'YOUTUBE_WATCH',
            name: 'Youtube Watch'
        },
        {
            type: 'ALL',
            name: 'All'
        },
        {
            type: 'none',
            name: 'None'
        }
    ];
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
    ];

    $scope.selectWidgetDuration = function (dateRangeName, widgetObj) {
        if (dateRangeName == 'Last N Days') {
            if (widgetObj.lastNdays) {
                widgetObj.dateRangeName = "Last " + widgetObj.lastNdays + " Days";
            } else {
                widgetObj.dateRangeName = "Last 0 Days";
            }
            widgetObj.lastNweeks = "";
            widgetObj.lastNmonths = "";
            widgetObj.lastNyears = "";
        } else if (dateRangeName == 'Last N Weeks') {
            if (widgetObj.lastNweeks) {
                widgetObj.dateRangeName = "Last " + widgetObj.lastNweeks + " Weeks";
            } else {
                widgetObj.dateRangeName = "Last 0 Weeks";
            }
            widgetObj.lastNdays = "";
            widgetObj.lastNmonths = "";
            widgetObj.lastNyears = "";
        } else if (dateRangeName == 'Last N Months') {
            if (widgetObj.lastNmonths) {
                widgetObj.dateRangeName = "Last " + widgetObj.lastNmonths + " Months";
            } else {
                widgetObj.dateRangeName = "Last 0 Months";
            }
            widgetObj.lastNdays = "";
            widgetObj.lastNweeks = "";
            widgetObj.lastNyears = "";
        } else if (dateRangeName == 'Last N Years') {
            if (widgetObj.lastNyears) {
                widgetObj.dateRangeName = "Last " + widgetObj.lastNyears + " Years";
            } else {
                widgetObj.dateRangeName = "Last 0 Years";
            }
            widgetObj.lastNdays = "";
            widgetObj.lastNweeks = "";
            widgetObj.lastNmonths = "";
        } else {
            widgetObj.dateRangeName = dateRangeName;
            widgetObj.lastNdays = "";
            widgetObj.lastNweeks = "";
            widgetObj.lastNmonths = "";
            widgetObj.lastNyears = "";
        }
    };

    $scope.selectDateRangeName = function (widgetObj) {
        $scope.editChart = widgetObj;
        widgetObj.dateRangeName = "Custom";
        widgetObj.lastNdays = "";
        widgetObj.lastNweeks = "";
        widgetObj.lastNmonths = "";
        widgetObj.lastNyears = "";
    };

    $scope.widgetObj = {allAccount: 1, selectAll: 0};

    $scope.clearChartType = function () {
        $scope.widgetObj = {allAccount: 1, selectAll: 0};
        $scope.selectedChartType = "";
        $scope.chartTypeName = "";
        $scope.dataSetColumn.fieldName = "";
        $scope.dataSetColumn.textExpression = "";
        $scope.dataSetColumn.fieldType = "";
        $scope.dataSetColumn.displayFormat = "";
        $scope.xColumn = "";
        $scope.selectPieChartXAxis = "";
        $scope.selectPieChartYAxis = "";
        $scope.y1Column = "";
        $scope.y2Column = "";
        $scope.tickerItem = "";
        $scope.funnelItem = "";
        $scope.showPreviewChart = false;
        $scope.showColumnDefs = false;
        $scope.showFilter = false;
        $scope.loadingColumnsGif = false;
        $scope.showDateRange = false;
        $scope.showSortBy = false;
    };

    $scope.firstSortableOptions = {
        start: function (event, ui) {
        },
        stop: function (event, ui) {
        }
    };

    $rootScope.getWidgetItem = function () {
        if (!$stateParams.tabId) {
            $stateParams.tabId = 0;
        }
        $http.get("admin/ui/dbWidget/" + $stateParams.tabId + '/' + $stateParams.accountId).success(function (response) {
            var widgetItems = [];
            widgetItems = response;
            if (response) {
                // $scope.productName = response[0].tabId.agencyProductId.productName;
            }
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
            });
            $http.get("admin/ui/getChartColorByUserId").success(function (response) {
                $scope.userChartColors = response;
                var widgetColors;
                if (response.optionValue) {
                    widgetColors = response.optionValue.split(',');
                }
                widgetItems.forEach(function (value, key) {
                    value.chartColors = widgetColors;
                });
                $scope.widgets = widgetItems;
                console.log(widgetItems);
            });
        });
    };
    $rootScope.getWidgetItem();

    function loadInitialWidgetColumnData(columns) {
        var data = [];
        columns.forEach(function (value, key) {
            data.push({
                id: value.id,
                fieldName: value.fieldName,
                displayName: value.displayName,
                agregationFunction: value.agregationFunction,
                expression: value.expression,
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
                columnHide: value.columnHide,
                search: value.search,
                groupField: value.groupField,
                combinationType: value.combinationType,
                derivedId: value.derivedId
            });
        });
        return data;
    }
    $scope.loadingColumnsGif = false;
    var setDefaultChartType;
    var setDefaultWidgetObj = [];

    $scope.setWidgetItems = function (widget) {
        $scope.dispHideBuilder = true;
        firstPreviewAfterEdit = 1;
        widget.targetColors = [];

        $scope.widgetId = widget.id;
        if (widget.chartColorOption) {
            var widgetColors = widget.chartColorOption.split(',');
            for (var i = 0; i <= widgetColors.length; i++) {
                if (widgetColors[i] && widgetColors[i] !== "") {
                    widget.targetColors.push({color: widgetColors[i]});
                }
            }
        }
        setDefaultWidgetObj = [];
        var data = loadInitialWidgetColumnData(widget.columns);
        if ($scope.collectionFields.length == widget.columns.length) {
            widget.selectAll = 1;
        } else {
            widget.selectAll = 0;
        }
        setDefaultWidgetObj.push({
            chartType: widget.chartType,
            id: widget.id,
            columns: data,
            widgetTitle: widget.widgetTitle,
            dataSourceId: widget.dataSourceId,
            dataSetId: widget.dataSetId,
            timeSegment: widget.timeSegment,
            productSegment: widget.productSegment,
            networkType: widget.networkType,
            targetColors: widget.targetColors,
            dateRangeName: widget.dateRangeName,
            customStartDate: widget.customStartDate,
            customEndDate: widget.customEndDate,
            lastNdays: widget.lastNdays,
            lastNweeks: widget.lastNweeks,
            lastNmonths: widget.lastNmonths,
            lastNyears: widget.lastNyears,
            accountId: widget.accountId ? widget.accountId.id : null
        });
        setDefaultChartType = widget.chartType;
        $scope.showDerived = false;
        if (!widget.accountId) {
            widget.allAccount = 1;
        }
        $scope.widgetObj = widget;
        $scope.widgetObj.previewTitle = widget.widgetTitle;
//        $scope.queryBuilderList = widget;
        $scope.widgetObj.columns.forEach(function (val, key) {
            val.columnsButtons = true;
        });
        $scope.widgetObj.previewTitle = widget.widgetTitle;
        var getDataSourceId = widget.dataSourceId;
        $scope.selectWidgetDataSource(getDataSourceId);
        getSegments(widget);
        getNetworkTypebyObj(widget);
        $scope.y1Column = [];
        $scope.y2Column = [];
        $scope.tickerItem = [];
        $scope.groupingFields = [];
        $scope.funnelItem = [];
        angular.forEach(widget.columns, function (val, key) {
            if (val.xAxis == 1) {
                $scope.xColumn = val;
                $scope.selectPieChartXAxis = val;
                $scope.selectX1Axis(widget, val);
            }
            if (val.yAxis == 1) {
                if (val.fieldName) {
                    if (widget.chartType == "pie") {
                        $scope.selectPieChartYAxis = val;
                    }
                    $scope.y1Column.push(val);
                }
            }
            if (val.yAxis == 2) {
                if (val.fieldName) {
                    $scope.y2Column.push(val);
                }
            }
            if (val.groupField) {
                $scope.groupingFields.push(val);
            }
            if (widget.chartType === 'ticker') {
                $scope.tickerItem.push(val);
            }
            if (widget.chartType === 'funnel') {
                $scope.funnelItem.push(val);
            }
        });
        tableDef(widget, $scope.y1Column, $scope.y2Column);
        $timeout(function () {
            $scope.queryBuilderList = widget;
            console.log($scope.queryBuilderList);
            resetQueryBuilder();
        }, 50);
    };
    function getNetworkTypebyObj(widget) {
        var getNetworkType = widget.networkType;
        $scope.networkTypes.forEach(function (val, key) {
            if (val.type === getNetworkType) {
                widget.networkType = val;
            }
        });
    }

    function tableDef(widget, y1Column, y2Column) {
        if (widget.columns) {
            if (widget.dataSetId) {
                columnHeaderDef(widget, y1Column, y2Column);
            }
        } else {
            if (widget.dataSetId) {
                columnHeaderDef(widget, y1Column, y2Column);
            }
        }
    }

    function columnHeaderDef(widget, y1Column, y2Column) {
        $scope.afterLoadWidgetColumns = false;
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
//        if (widget.dataSetId.dataSourceId.dataSourceType == "sql") {
//            url = "admin/proxy/getJson?url=../dbApi/admin/dataSet/getData&";
//        }

        var setProductSegment;
        var setTimeSegment;
        var setNetworkType;
        if (widget.productSegment && widget.productSegment.type) {
            setProductSegment = widget.productSegment.type;
        } else {
            setProductSegment = widget.productSegment;
        }

        if (widget.timeSegment && widget.timeSegment.type) {
            setTimeSegment = widget.timeSegment.type;
        } else {
            setTimeSegment = widget.timeSegment;
        }

        if (widget.networkType && widget.networkType.type) {
            setNetworkType = widget.networkType.type;
        } else {
            setNetworkType = widget.networkType;
        }

        $http.get(url + 'connectionUrl=' + widget.dataSetId.dataSourceId.connectionString +
                "&dataSetId=" + widget.dataSetId.id +
                "&accountId=" + $stateParams.accountId +
                "&widgetId=" + widget.id +
                "&productSegment=" + setProductSegment +
                "&timeSegment=" + setTimeSegment +
                "&networkType=" + setNetworkType +
                "&userId=" + (widget.dataSetId.userId ? widget.dataSetId.userId.id : null) +
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
            $scope.collectionFields = response.columnDefs;
            $scope.widgetDataSetColumnsDefs = response.columnDefs;
            var getWidgetColumns = widget.columns;

            $scope.collectionFields.forEach(function (value, k) {
                var machField = $.grep(getWidgetColumns, function (b) {
                    return b.fieldName === value.fieldName;
                });
                if (machField.length > 0) {
                    value.selectColumnDef = 1;
                } else {
                    value.selectColumnDef = 0;
                }
            });
            $scope.afterLoadWidgetColumns = true;
            $scope.columnY1Axis = [];
            $scope.columnY2Axis = [];

            angular.forEach($scope.collectionFields, function (value, key) {
                $scope.columnY1Axis.push(value);
                $scope.columnY2Axis.push(value);
            });
            angular.forEach(y1Column, function (value, key) {
                var data = $scope.columnY1Axis.find(function (item, i) {
                    if (item.fieldName === value.fieldName) {
                        return i;
                    }
                });
                var index = $scope.columnY1Axis.indexOf(data);
                $scope.columnY1Axis.splice(index, 1);
            });

            angular.forEach(y2Column, function (value, key) {
                var data = $scope.columnY2Axis.find(function (item, i) {
                    if (item.fieldName === value.fieldName) {
                        return i;
                    }
                });
                var index = $scope.columnY2Axis.indexOf(data);
                $scope.columnY2Axis.splice(index, 1);
            });

            angular.forEach(y2Column, function (value, key) {
                var data = $scope.columnY1Axis.find(function (item, i) {
                    if (item.fieldName === value.fieldName) {
                        return i;
                    }
                });
                var index = $scope.columnY1Axis.indexOf(data);
                $scope.columnY1Axis.splice(index, 1);
            });

            angular.forEach(y1Column, function (value, key) {
                var data = $scope.columnY2Axis.find(function (item, i) {
                    if (item.fieldName === value.fieldName) {
                        return i;
                    }
                });
                var index = $scope.columnY2Axis.indexOf(data);
                $scope.columnY2Axis.splice(index, 1);
            });
            resetQueryBuilder();
        });
    }

    $scope.getNewDataSetObj = function (widget, chartTypeName) {
        $scope.hideSelectedColumn = true;
        $scope.dispHideBuilder = true;
        widget.columns = [];
        $scope.collectionFields = [];
        $scope.afterLoadWidgetColumns = false;
        var widgetList = widget;
        var getDataSet = widgetList.dataSetId;
        if (!getDataSet) {
            return;
        }
        $scope.y1Column = "";
        $scope.xColumn = "";
        $scope.selectPieChartXAxis = "";
        $scope.selectPieChartYAxis = "";
        $scope.y2Column = "";
        $scope.tickerItem = "";
        $scope.funnelItem = "";
        getSegments(widget);
        widget.jsonData = null;
        widget.queryFilter = null;
        var url = "admin/proxy/getData?";
//        if (getDataSet.dataSourceId.dataSourceType == "sql") {
//            url = "admin/proxy/getJson?url=../dbApi/admin/dataSet/getData&";
//        }
        var dataSourcePassword;
        if (getDataSet.dataSourceId.password) {
            dataSourcePassword = getDataSet.dataSourceId.password;
        } else {
            dataSourcePassword = '';
        }

        var setTimeSegment = widget.timeSegment ? widget.timeSegment.type : null;
        var setProductSegment = widget.productSegment ? widget.productSegment.type : null;
        var setNetworkType = widget.networkType ? widget.networkType.type : null;
        $http.get(url + 'connectionUrl=' + getDataSet.dataSourceId.connectionString +
                "&dataSetId=" + getDataSet.id +
                "&accountId=" + $stateParams.accountId +
                "&widgetId=" + widget.id +
                "&productSegment=" + setProductSegment +
                "&timeSegment=" + setTimeSegment +
                "&networkType=" + setNetworkType +
                "&userId=" + (getDataSet.userId ? getDataSet.userId.id : null) +
                "&dataSetReportName=" + getDataSet.reportName +
                "&driver=" + getDataSet.dataSourceId.sqlDriver +
                "&startDate=" + $stateParams.startDate +
                "&endDate=" + $stateParams.endDate +
                '&username=' + getDataSet.dataSourceId.userName +
                '&password=' + dataSourcePassword +
                '&url=' + getDataSet.url +
                '&port=3306&schema=vb&query=' + encodeURI(getDataSet.query) +
                "&fieldsOnly=true").success(function (response) {
            $scope.afterLoadWidgetColumns = true;
            $scope.hideSelectedColumn = false;
//            if ((chartTypeName ? chartTypeName : widgetList.chartType) !== 'table') {
//                $scope.collectionFields = response.columnDefs;
//            } else {
            $scope.collectionFields = response.columnDefs;
            $scope.widgetDataSetColumnsDefs = response.columnDefs;
//            }
            $scope.columnY1Axis = [];
            $scope.columnY2Axis = [];
            angular.forEach($scope.collectionFields, function (value, key) {
                $scope.columnY1Axis.push(value);
                $scope.columnY2Axis.push(value);
            });
            resetQueryBuilder();
        });
    };

    $scope.selectWidgetDataSource = function (dataSourceName) {
        if (!dataSourceName) {
            return;
        }
        $scope.y1Column = "";
        $scope.selectPieChartXAxis = "";
        $scope.selectPieChartYAxis = "";
        $scope.y2Column = "";
        $scope.tickerItem = "";
        $scope.funnelItem = "";
        $http.get('admin/ui/dataSet/publishDataSet').success(function (response) {
            $scope.dataSets = [];
            angular.forEach(response, function (value, key) {
                if (!value.dataSourceId) {
                    return;
                }
                if (value.dataSourceId.name === dataSourceName.name) {
                    $scope.dataSets.push(value);
                }
            });
        });
    };

    function getSegments(widget) {
        var timeSegmentType = widget.timeSegment;
        var productSegmentType = widget.productSegment;
        var getDataSourceType = widget.dataSourceId ? widget.dataSourceId.dataSourceType : null;
        if (getDataSourceType === 'csv' || getDataSourceType === 'sql' || getDataSourceType === 'xls' || getDataSourceType === "") {
            return;
        }
        var getReportName = widget.dataSetId ? widget.dataSetId.reportName : null;
        $http.get("static/datas/dataSets/dataSets.json").success(function (response) {
            var getDataSetObjs = response;
            var getDataSetPerformance = getDataSetObjs[getDataSourceType];
            if (!getDataSetPerformance) {
                return;
            }
            getDataSetPerformance.forEach(function (val, key) {
                var getPerformanceType = val.type;
                if (getReportName === getPerformanceType) {
                    $scope.timeSegments = val.timeSegments;
                    $scope.productSegments = val.productSegments;
                    if (!$scope.timeSegments) {
                        return;
                    }
                    $scope.timeSegments.forEach(function (val, key) {
                        if (val.type === timeSegmentType) {
                            $scope.widgetObj.timeSegment = val;
                        }
                    });
                    if (!$scope.productSegments) {
                        return;
                    }
                    $scope.productSegments.forEach(function (val, key) {
                        if (val.type === productSegmentType) {
                            $scope.widgetObj.productSegment = val;
                        }
                    });
                }
            });
        });
    }

    function getNetworkTypebyObj(widget) {
        var getNetworkType = widget.networkType;
        $scope.networkTypes.forEach(function (val, key) {
            if (val.type === getNetworkType) {
                $scope.widgetObj.networkType = val;
            }
        });
    }

    $scope.selectChart = function (chartType, widget) {
        addColor = [];
        $scope.hideSelectedColumn = true;
        $scope.showSortBy = false;
        $scope.showColumnDefs = false;
        $scope.showPreviewChart = false;
        $scope.showFilter = false;
        $scope.showColor = false;
        $scope.selectedChartType = chartType.type;
        $scope.chartTypeName = chartType.type;
        $scope.showDateRange = false;
//        $scope.xColumn = "";
//        $scope.y1Column = "";
//        $scope.selectPieChartXAxis = "";
//        $scope.selectPieChartYAxis = "";
//        $scope.y2Column = "";
//        $scope.tickerItem = "";
//        $scope.funnelItem = "";
//        $scope.widgetObj.dataSourceId = "";
//        $scope.widgetObj.dataSetId = "";
//        $scope.widgetObj.timeSegment = "";
//        $scope.widgetObj.productSegment = "";
//        $scope.widgetObj.networkType = "";
        $scope.widgetObj.chartColorOption = "";
        $scope.widgetObj.targetColors = "";
        if ($scope.chartTypeName) {
            widget.columns = [];
            $scope.collectionFields.forEach(function (value, k) {
                var machField = $.grep(widget.columns, function (b) {
                    return b.fieldName === value.fieldName;
                });
                if (machField.length > 0) {
                    value.selectColumnDef = 1;
                } else {
                    value.selectColumnDef = 0;
                }
            });
        }

        $timeout(function () {
            $scope.hideSelectedColumn = false;
        }, 50);
    };
    $scope.showListOfColumns = function () {
        $scope.showSortBy = false;
        $scope.loadingColumnsGif = true;
        $scope.showFilter = false;
        $scope.showColumnDefs = true;
        $scope.showPreviewChart = false;
        $scope.showDateRange = false;
        $scope.showColor = false;
        $scope.hideSelectedColumn = false;
    };

    $scope.showFilterList = function () {
        $scope.showFilter = true;
        $scope.loadingColumnsGif = true;
        $scope.showColumnDefs = false;
        $scope.showPreviewChart = false;
        $scope.showDateRange = false;
        $scope.showColor = false;
        $scope.showSortBy = false;
    };

    $scope.showEditor = function (chartType, widget) {
        $scope.chartTypeName = chartType ? chartType : widget.chartType;
        $scope.showSortBy = false;
        $scope.showPreviewChart = true;
        $scope.showColor = false;
        $scope.showDateRange = false;
    };

    $scope.showDateDurations = function () {
        $scope.showSortBy = false;
        $scope.showDateRange = true;
        $scope.loadingColumnsGif = false;
        $scope.showFilter = false;
        $scope.showColumnDefs = false;
        $scope.showColor = false;
        $scope.showPreviewChart = false;
    };

    $scope.showSortingColumn = function () {
        $scope.showSortBy = true;
        $scope.showDateRange = false;
        $scope.loadingColumnsGif = false;
        $scope.showFilter = false;
        $scope.showColumnDefs = false;
        $scope.showColor = false;
        $scope.showPreviewChart = false;
    };

    $scope.showColorList = function () {
        $scope.showColor = true;
        $scope.showFilter = false;
        $scope.showSortBy = false;
        $scope.loadingColumnsGif = false;
        $scope.showColumnDefs = false;
        $scope.showPreviewChart = false;
        $scope.showDateRange = false;
    };

    var firstPreviewAfterEdit = 1;
    $scope.showPreview = function (widgetObj, userChartColors) {
        var chartType = $scope.chartTypeName;
        $scope.showPreviewChart = true;
        $scope.showFilter = false;
        $scope.showSortBy = false;
        $scope.showColumnDefs = false;
        $scope.showDateRange = false;
        $scope.showColor = false;
        $scope.chartTypeName = null;
        $timeout(function () {
            $scope.chartTypeName = chartType ? chartType : widgetObj.chartType;
        }, 50);

        var chartColors = userChartColors ? userChartColors.optionValue : null;
        if (firstPreviewAfterEdit == 1) {
            $scope.chartColorOptionsVal = widgetObj.chartColorOption;
        }
        firstPreviewAfterEdit = firstPreviewAfterEdit + 1;
        if (widgetObj.targetColors) {
            var widgetChartColor = widgetObj.targetColors.map(function (val, key) {
                if (val) {
                    return val.color;
                }
            }).join(',');
            widgetObj.chartColorOption = widgetChartColor;
        }
        if (!widgetObj.chartColorOption) {
            widgetObj.chartColorOption = chartColors;
        }
        $scope.displayPreviewChart = widgetObj;
    };

    $scope.sortableOptions = {
        start: function (event, ui) {
        },
        stop: function (event, ui) {
        }
    };


    $scope.selectAllColumns = function (columns, widget) {
        console.log(widget.selectAll);
        $scope.dispHideBuilder = true;
        var exists = false;
        if (widget.selectAll == 1) {
            angular.forEach(columns, function (obj, key) {
                obj.selectColumnDef = 1;
                var checkObj = $.grep(widget.columns, function (val) {
                    if (obj.fieldName === val.fieldName) {
                        exists = true;
                    } else {
                        exists = false;
                    }
                    return exists;
                });
                if (checkObj == false) {
                    var data = {
                        derivedId: obj.id,
                        agregationFunction: obj.agregationFunction,
                        columnsButtons: true,
                        displayFormat: obj.displayFormat,
                        displayName: obj.displayName,
                        expression: obj.expression,
                        fieldName: obj.fieldName,
                        fieldType: obj.fieldType,
                        functionName: obj.functionName,
                        groupPriority: obj.groupPriority,
                        selectColumnDef: obj.selectColumnDef,
                        sortOrder: obj.sortOrder,
                        sortPriority: obj.sortPriority,
                        status: obj.status,
                        type: obj.type,
                        userId: obj.userId,
                        widgetId: obj.widgetId
                    };
                    widget.columns.push(data);
                }
            });
        } else {
            widget.columns = [];
            angular.forEach(columns, function (val, key) {
                val.selectColumnDef = 0;
            });
        }
        $timeout(function () {
            $scope.queryBuilderList = widget;
            resetQueryBuilder();
        }, 50);
    };

    $scope.selectColumnItem = function (obj, widget) {
        $scope.dispHideBuilder = true;
        obj.columnsButtons = true;
        var checkColumnDef = obj.selectColumnDef;
        if (checkColumnDef === 1) {
            var data = {
                derivedId: obj.id,
                agregationFunction: obj.agregationFunction,
                columnsButtons: obj.columnsButtons,
                displayFormat: obj.displayFormat,
                displayName: obj.displayName,
                expression: obj.expression,
                fieldName: obj.fieldName,
                fieldType: obj.fieldType,
                functionName: obj.functionName,
                groupPriority: obj.groupPriority,
                selectColumnDef: obj.selectColumnDef,
                sortOrder: obj.sortOrder,
                sortPriority: obj.sortPriority,
                status: obj.status,
                type: obj.type,
                userId: obj.userId,
                widgetId: obj.widgetId
            };
            widget.columns.push(data);
        } else {
            var index = -1;
            var filteredObj = widget.columns.find(function (item, i) {
                if (item.fieldName === obj.fieldName) {
                    index = i;
                    return i;
                }
            });
            if (index != -1) {
                widget.columns.splice(index, 1);
                widget.selectAll = 0;
            }
        }
        if ($scope.collectionFields.length == widget.columns.length) {
            widget.selectAll = 1;
        }
        $timeout(function () {
            $scope.queryBuilderList = widget;
            resetQueryBuilder();
        }, 50);
    };

    $scope.removeSelectedValue = function (widget, obj, index) {
        widget.selectAll = 0;
        $scope.dispHideBuilder = true;
        widget.columns.splice(index, 1);
        $scope.collectionFields.forEach(function (val, key) {
            if (val.displayName === obj.displayName) {
                val.selectColumnDef = 0;
            }
        });
        $timeout(function () {
            resetQueryBuilder();
        }, 40);
    };

    $scope.targetColors = [];

    $scope.deleteColorOption = function (targetColor, index) {
        $scope.widgetObj.targetColors.splice(index, 1);
    };

    var addColor = [];
    $scope.addColors = function (widget) {
        if (widget.targetColors) {
            widget.targetColors.push({color: "#62cb31"});
        } else {
            var targetColors =
                    {
                        color: "#62cb31"
                    };
            addColor.push(targetColors);
            widget["targetColors"] = addColor;
        }
    };

    $scope.deleteWidget = function (widget, index) {
        $http({method: 'DELETE', url: 'admin/ui/dbWidget/' + widget.id}).success(function (response) {
            $scope.widgets.splice(index, 1);
        });
    };

    $scope.widgetDuplicate = function (widgetData) {
        $http.get("admin/ui/dbWidgetDuplicate/" + widgetData.widgetId + "/" + widgetData.tabId).success(function (response) {
            $http.get("admin/ui/dbDuplicateTag/" + response.id).success(function (dataTag) {
                response["tags"] = dataTag[0];
                $scope.widgets.push(response);
            });
        });
    };

    $scope.pageRefresh = function () {          //Page Refresh
        $rootScope.getWidgetItem();
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
    };

    $scope.goReport = function () {
        $state.go('index.report.reports', {accountId: $stateParams.accountId, accountName: $stateParams.accountName, startDate: $stateParams.startDate, endDate: $stateParams.endDate});
    };

    $scope.onDropComplete = function (index, widget, evt) {
        if (widget !== "" && widget !== null) {
            var otherObj = $scope.widgets[index];
            var otherIndex = $scope.widgets.indexOf(widget);
            $scope.widgets = $scope.moveWidget($scope.widgets, otherIndex, index);
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
    $scope.reportEmptyLogo = "static/img/logos/deeta-logo.png";
    $scope.selectReport = function (reportWidget) {
        $scope.hideReportsTable = true;
//        $scope.loadReportsTable = false;
        $scope.showReportWidgetName = false;
        $scope.reportWidgetTitle = [];
        $scope.reportLogo = reportWidget.logo;
        $scope.reportDescription = reportWidget.description;
        $http.get("admin/report/reportWidget/" + reportWidget.id + "?locationId=" + $stateParams.accountId).success(function (response) {
            $scope.hideReportsTable = false;
            if (response.length > 0) {
                $scope.showReportWidgetName = true;
                $scope.reportWidgetTitle = response;
                $scope.showReportEmptyMessage = false;
            } else {
                $scope.showReportEmptyMessage = true;
                $scope.reportEmptyMessage = "No Data Found";
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
    $scope.setFunnelFn = function (funnelFn) {
        $scope.directiveFunnelFn = funnelFn;
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
            $http({method: widget.id ? 'PUT' : 'POST', url: 'admin/ui/editWidgetSize/' + widget.id + "?width=" + widget.width}).success(function (response) {
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
            $http({method: widget.id ? 'PUT' : 'POST', url: 'admin/ui/editWidgetSize/' + widget.id + "?width=" + widget.width}).success(function (response) {
            });
        }, 50);
    };

    $scope.editedItem = null;

    $scope.startEditing = function (columns) {
        columns.dispName = columns.displayName;
        columns.editing = true;
        columns.columnsButtons = false;
        $scope.editedItem = columns;
    };

    $scope.doneEditing = function (columns) {
        columns.displayName = columns.dispName;
        columns.editing = false;
        columns.columnsButtons = true;
        $scope.editedItem = null;
    };

    $scope.selectX1Axis = function (widgetObj, column) {
        $scope.dispHideBuilder = true;
        var exists = false;
        angular.forEach(widgetObj.columns, function (value, key) {
            if (column.fieldName === value.fieldName) {
                exists = true;
                value.xAxis = 1;
            } else {
                value.xAxis = null;
            }
        });
        if (exists === false) {
            column.xAxis = 1;
            widgetObj.columns.push(column);
        }
        $timeout(function () {
            $scope.queryBuilderList = widgetObj;
            resetQueryBuilder();
        }, 50);
    };
    $scope.selectY1Axis = function (widget, y1data, chartTypeName) {
        $scope.dispHideBuilder = true;
        angular.forEach($scope.columnY2Axis, function (val, key) {
            angular.forEach(y1data, function (value, key) {
                if (val.fieldName === value.fieldName) {
                    var index = $scope.columnY2Axis.indexOf(val);
                    $scope.columnY2Axis.splice(index, 1);
                }
            });
        });
        angular.forEach(y1data, function (value, key) {
            if (!value) {
                return;
            }
            var exists = false;
            angular.forEach(widget.columns, function (val, key) {
                if (val.fieldName === value.fieldName) {
                    exists = true;
                    val.yAxis = 1;
                    val.groupField = y1data.indexOf(value) + 1;  //If Stacked Bar
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
                    value.groupField = y1data.indexOf(value) + 1;
                    widget.columns.push(value);
                }
            }
            $timeout(function () {
                $scope.queryBuilderList = widget;
                resetQueryBuilder();
            }, 50);
        });
    };

    $scope.selectY2Axis = function (widget, y2data) {
        $scope.dispHideBuilder = true;
        angular.forEach($scope.columnY1Axis, function (val, key) {
            angular.forEach(y2data, function (value, key) {
                if (val.fieldName === value.fieldName) {
                    var index = $scope.columnY1Axis.indexOf(val);
                    $scope.columnY1Axis.splice(index, 1);
                }
            });
        });
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
                    if (val.fieldName === y2data.removeItem) {
                        val.yAxis = null;
                    }
                }
            });
            if (exists === false) {
                if (value.displayName) {
                    value.yAxis = 2;
                    widget.columns.push(value);
                }
            }
        });
        $timeout(function () {
            $scope.queryBuilderList = widget;
            resetQueryBuilder();
        }, 50);
    };

    $scope.removedByY1Column = function (widgetObj, column, yAxisItems) {
//        if (yAxisItems.length > 0) {
        $scope.columnY2Axis.push(column);
        var index = $scope.columnY1Axis.indexOf(column);
        if (index == -1) {
            $scope.columnY1Axis.push(column);
        }
        yAxisItems.removeItem = column.fieldName;
        $scope.selectY1Axis(widgetObj, yAxisItems);
//        } else {
        var getIndex = widgetObj.columns.indexOf(column);
        widgetObj.columns.splice(getIndex, 1);
//            angular.forEach(widgetObj.columns, function (val, key) {
//                if (val.fieldName == column.fieldName) {
//                    val.yAxis = null;
//                }
//            });
//        }
    };
    $scope.removedByY2Column = function (widgetObj, column, yAxisItems) {
//        if (yAxisItems.length > 0) {
        $scope.columnY1Axis.push(column);
        var index = $scope.columnY2Axis.indexOf(column);
        if (index == -1) {
            $scope.columnY2Axis.push(column);
        }
        yAxisItems.removeItem = column.fieldName;
        $scope.selectY2Axis(widgetObj, yAxisItems);
//        } else {
        var getIndex = widgetObj.columns.indexOf(column);
        widgetObj.columns.splice(getIndex, 1);
//            angular.forEach(widgetObj.columns, function (val, key) {
//                if (val.fieldName == column.fieldName) {
//                    val.yAxis = null;
//                }
//            });
//        }
    };

    $scope.ticker = function (widgetObj, column) {
        $scope.dispHideBuilder = true;
        var newColumns = [];
        if (column.length == 0) {
            widgetObj.columns = "";
        } else {
            angular.forEach(column, function (value, key) {
                angular.forEach($scope.collectionFields, function (val, header) {
                    if (val.fieldName === value.fieldName) {
                        val.displayFormat = value.displayFormat;
                        newColumns.push(val);
                    }
                });
                widgetObj.columns = newColumns;
            });
        }
        $scope.tickerItem = widgetObj.columns;
        $timeout(function () {
            $scope.queryBuilderList = widgetObj;
            resetQueryBuilder();
        }, 50);
    };

    $scope.removedByTicker = function (widgetObj, column, tickerItem) {
        $scope.ticker(widgetObj, tickerItem);
        //var getIndex = widgetObj.columns.indexOf(column)
        // console.log(getIndex)
        //widgetObj.columns.splice(getIndex, 1)
    };
// Funnel Format
    $scope.funnel = function (widget, column) {
        $scope.dispHideBuilder = true;
        var exists = false;
        if (column.length == 0) {
            widget.columns = "";
        } else {
            angular.forEach(column, function (value, key) {
                var checkObj = $.grep(widget.columns, function (val) {
                    if (value.fieldName === val.fieldName) {
                        exists = true;
                    } else {
                        exists = false;
                    }
                    return exists;
                });
                if (checkObj == false) {
                    widget.columns.push(value);
                }
            });
        }
        $scope.funnelItem = widget.columns;
        $timeout(function () {
            $scope.queryBuilderList = widget;
            resetQueryBuilder();
        }, 50);
    };

    $scope.removedByFunnel = function (widget, removeItem, funnelItem) {
        $scope.dispHideBuilder = true;
        var getIndex = widget.columns.indexOf(removeItem);
        widget.columns.splice(getIndex, 1);
        //$scope.funnel(widget, funnelItem);   
        $timeout(function () {
            $scope.queryBuilderList = widget;
            resetQueryBuilder();
        }, 50);
    };

    $scope.selectPieChartX = function (widget, column) {
        $scope.dispHideBuilder = true;
        if (!column) {
            return;
        }
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
            $timeout(function () {
                $scope.queryBuilderList = widget;
                resetQueryBuilder();
            }, 50);
        }
        $timeout(function () {
            $scope.queryBuilderList = widget;
            resetQueryBuilder();
        }, 50);
    };

    $scope.selectPieChartY = function (widget, column) {
        $scope.dispHideBuilder = true;
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
            $timeout(function () {
                $scope.queryBuilderList = widget;
                resetQueryBuilder();
            }, 50);
        }
        $timeout(function () {
            $scope.queryBuilderList = widget;
            resetQueryBuilder();
        }, 50);
    };

    function resetQueryBuilder() {
        $scope.dispHideBuilder = false;
    }
    ;
    //Derived Column
    $scope.showDerived = false;
    $scope.dataSetColumn = {};
    $scope.addDerived = function () {
        $scope.dataSetColumn = {};
        $scope.showDerived = true;
    };
    $scope.cancelDerivedColumn = function (dataSetColumn) {
        $scope.showDerived = false;
        $scope.dataSetColumn = "";
    };
    //Edit Derived
    $scope.editDerivedColumn = function (collectionField, widgetObj) {
        $scope.showDerived = false;
        $scope.dataSetColumn = {};
        if (collectionField.userId != null) {
            $scope.showDerived = true;
            var data = {
                agregationFunction: collectionField.agregationFunction,
                functionName: collectionField.functionName,
                groupPriority: collectionField.groupPriority,
                id: collectionField.id,
                sortOrder: collectionField.sortOrder,
                sortPriority: collectionField.sortPriority,
                status: collectionField.status,
                textExpression: collectionField.expression,
                fieldType: collectionField.fieldType,
                fieldName: collectionField.fieldName,
                displayName: collectionField.displayName,
                userId: collectionField.userId,
                displayFormat: collectionField.displayFormat,
                widgetId: widgetObj.id
            };
            $scope.dataSetColumn = data;
        }
    };
    //Save DerivedColumn
    $scope.saveDerivedColumn = function (dataSetColumn, widget) {
        $scope.collectionField = {};
        var dataSetColumnData = {
            functionName: dataSetColumn.functionName ? dataSetColumn.functionName : null,
            id: dataSetColumn.id ? dataSetColumn.id : null,
            sortPriority: dataSetColumn.sortPriority ? dataSetColumn.sortPriority : null,
            status: dataSetColumn.status ? dataSetColumn.status : null,
            expression: dataSetColumn.textExpression,
            fieldName: dataSetColumn.fieldName,
            displayName: dataSetColumn.fieldName,
            fieldType: dataSetColumn.fieldType,
            dataSetId: widget.dataSetId.id,
            userId: dataSetColumn.userId ? dataSetColumn.userId : $scope.userId,
            displayFormat: dataSetColumn.displayFormat
        };
        var oldFieldName = "";
        if (!dataSetColumn.id) {
            $scope.collectionFields.push(dataSetColumnData);
            $scope.columnY1Axis.push(dataSetColumnData);
            $scope.columnY2Axis.push(dataSetColumnData);
        } else {
            $scope.collectionFields.forEach(function (val, key) {
                if (val.id === dataSetColumn.id) {
                    oldFieldName = val.fieldName;
                    val.fieldName = dataSetColumnData.fieldName;
                    val.displayName = dataSetColumnData.displayName;
                    val.expression = dataSetColumnData.expression;
                    val.functionName = dataSetColumnData.functionName;
                    val.fieldType = dataSetColumnData.fieldType;
                    val.displayFormat = dataSetColumnData.displayFormat;
                    val.status = dataSetColumnData.status;
                    val.dataSetId = dataSetColumnData.dataSetId;
                    val.userId = dataSetColumnData.userId;
                    val.sortPriority = dataSetColumnData.sortPriority;
                }
            });
            $scope.columnY1Axis.forEach(function (val, key) {
                if (val.id === dataSetColumn.id) {
                    oldFieldName = val.fieldName;
                    val.fieldName = dataSetColumnData.fieldName;
                    val.displayName = dataSetColumnData.displayName;
                    val.expression = dataSetColumnData.expression;
                    val.functionName = dataSetColumnData.functionName;
                    val.fieldType = dataSetColumnData.fieldType;
                    val.displayFormat = dataSetColumnData.displayFormat;
                    val.status = dataSetColumnData.status;
                    val.dataSetId = dataSetColumnData.dataSetId;
                    val.userId = dataSetColumnData.userId;
                    val.sortPriority = dataSetColumnData.sortPriority;
                }
            });
        }
//        widget.columns.forEach(function (val, key) {
//            if (val.derivedId === dataSetColumn.id || val.fieldName === oldFieldName) {
//                val.fieldName = dataSetColumnData.fieldName;
//                val.displayName = dataSetColumnData.displayName;
//                val.expression = dataSetColumnData.expression;
//                val.functionName = dataSetColumnData.functionName;
//                val.fieldType = dataSetColumnData.fieldType;
//                val.displayFormat = dataSetColumnData.displayFormat;
//                val.derivedId = dataSetColumnData.id;
//                val.status = dataSetColumnData.status;
//                val.dataSetId = dataSetColumnData.dataSetId;
//                val.userId = dataSetColumnData.userId;
//                val.sortPriority = dataSetColumnData.sortPriority;
//            }
//        });
        $scope.showDerived = false;
        $scope.dataSetColumn = "";
    };

    //check FieldName
    $scope.dataSetError = false;
    function showDataSetError() {
        $scope.dataSetError = true;
    }
    $scope.checkFieldName = function (fieldName) {
        for (var i = 0; i < $scope.collectionFields.length; i++) {
            if (fieldName == $scope.collectionFields[i].fieldName) {
                showDataSetError();
                break;
            } else {
                $scope.dataSetError = false;
            }
        }
    };
    //Auto Complete
    $scope.config = {
        autocomplete: [
            {
                words: [/[A-Za-z]+[_A-Za-z0-9]/gi],
                cssClass: 'user'
            }
        ],
        dropdown: [
            {
                trigger: /([A-Za-z]+[_A-Za-z0-9]+)/gi,
                list: function (match, callback) {
                    // match is the regexp return, in this case it returns
                    // [0] the full match, [1] the first capture group => username
                    // Prepare the fake data
                    var listData = $scope.collectionFields.filter(function (element) {
                        return element.fieldName.substr(0, match[1].length).toLowerCase() === match[1].toLowerCase()
                                && element.fieldName.length > match[1].length;
                    }).map(function (element) {
                        return {
                            display: element.fieldName, // This gets displayed in the dropdown
                            item: element // This will get passed to onSelect
                        };
                    });
                    callback(listData);
                },
                onSelect: function (item) {
                    return item.display;
                },
                mode: 'replace'
            }
        ]
    };


//    $scope.saveDerivedColumn = function (dataSetColumn, widget) {
//        $scope.tableColumns = [];
//        $http.get("admin/ui/getDataSetColumnsByDataSetId/" + widget.dataSetId.id).success(function (resp) {
//            if (resp == "" || resp == null) {
//                angular.forEach($scope.collectionFields, function (val, key) {
//                    var data = {
//                        agregationFunction: val.agregationFunction,
//                        functionName: val.functionName,
//                        groupPriority: val.groupPriority,
//                        id: null,
//                        sortOrder: val.sortOrder,
//                        sortPriority: val.sortPriority,
//                        status: val.status,
//                        expression: val.expression,
//                        fieldType: val.type,
//                        fieldName: val.fieldName,
//                        displayName: val.displayName,
//                        userId: val.userId,
//                        widgetId:val.widgetId,
////            baseField: dataSetColumn.baseField,
//                        displayFormat: val.displayFormat
////            columnName: dataSetColumn.columnName,
////            dateRangeName: dataSetColumn.dateRangeName,
////            customStartDate: scope.customStartDate,
////            customEndDate: scope.customEndDate,
////            lastNdays: dataSetColumn.lastNdays,
////            lastNweeks: dataSetColumn.lastNweeks,
////            lastNmonths: dataSetColumn.lastNmonths,
////            lastNyears: dataSetColumn.lastNyears
//                    };
//                    $scope.tableColumns.push(data);
//
//                });
//            } else {
//                angular.forEach(resp, function (value, key) {
//                    angular.forEach($scope.collectionFields, function (val, key) {
//                        if (value.fieldName === val.fieldName) {
//                            var data = {
//                                agregationFunction: value.agregationFunction,
//                                functionName: value.functionName,
//                                groupPriority: value.groupPriority,
//                                id: value.id,
//                                sortOrder: value.sortOrder,
//                                sortPriority: value.sortPriority,
//                                status: value.status,
//                                expression: value.expression,
//                                fieldType: value.fieldType,
//                                fieldName: value.fieldName,
//                                displayName: value.displayName,
//                                userId: value.userId,
//                                widgetId: value.widgetId,
////            baseField: dataSetColumn.baseField,
//                                displayFormat: value.displayFormat
////            columnName: dataSetColumn.columnName,
////            dateRangeName: dataSetColumn.dateRangeName,
////            customStartDate: scope.customStartDate,
////            customEndDate: scope.customEndDate,
////            lastNdays: dataSetColumn.lastNdays,
////            lastNweeks: dataSetColumn.lastNweeks,
////            lastNmonths: dataSetColumn.lastNmonths,
////            lastNyears: dataSetColumn.lastNyears
//                            };
//                            $scope.tableColumns.push(data);
//                        }
//                    });
//
//                });
//            }
//            ;
//
//            var dataSetColumnData = {
//                functionName: dataSetColumn.functionName ? dataSetColumn.functionName : null,
//                id: dataSetColumn.id ? dataSetColumn.id : null,
//                sortPriority: dataSetColumn.sortPriority ? dataSetColumn.sortPriority : null,
//                status: dataSetColumn.status ? dataSetColumn.status : null,
//                expression: dataSetColumn.expression,
//                fieldName: dataSetColumn.fieldName,
//                displayName: dataSetColumn.fieldName,
//                fieldType: dataSetColumn.fieldType,
//                tableColumns: $scope.tableColumns,
//                dataSetId: widget.dataSetId.id,
//                widgetId: widget.id,
////            baseField: dataSetColumn.baseField,
//                displayFormat: dataSetColumn.displayFormat
////            columnName: dataSetColumn.columnName,
////            dateRangeName: dataSetColumn.dateRangeName,
////            customStartDate: scope.customStartDate,
////            customEndDate: scope.customEndDate,
////            lastNdays: dataSetColumn.lastNdays,
////            lastNweeks: dataSetColumn.lastNweeks,
////            lastNmonths: dataSetColumn.lastNmonths,
////            lastNyears: dataSetColumn.lastNyears
//            };
//
//
////            $http({method: 'POST', url: 'admin/ui/createWidgetColumn', data: dataSetColumnData}).success(function (response) {
////                var count = 0;
////                angular.forEach($scope.collectionFields, function (value, key) {
////                    if (value.id === response.id) {
//////                        columnHeaderDef
////                        count = 1;
////                        if (value.id == dataSetColumnData.id) {
////                            value.fieldName = dataSetColumn.fieldName;
//////                           value.displayName = dataSetColumn.displayName;
////                        }
////                    }
////                });
////                $scope.collectionFields;
////                if (count == 0) {
////                    $scope.collectionFields.push(response);
////                }
////                $scope.tableColumns.push(response);
//                $scope.showDerived = false;
//                $scope.dataSetColumn = "";
//            });
//            $scope.dataSetColumn = "";
//            // $scope.collectionField.fieldName = dataSetColumnData.fieldName;
//
//        });
//    };

    function clearEditAllWidgetData() {
        $scope.widgetObj.id = "";
        $scope.widgetObj.previewTitle = "";
        $scope.widgetObj.chartType = "";
        $scope.selectedChartType = "";
        $scope.widgetObj.dataSourceId = "";
        $scope.widgetObj.dataSetId = "";
        $scope.widgetObj.timeSegment = "";
        $scope.widgetObj.productSegment = "";
        $scope.widgetObj.networkType = "";
        $scope.chartTypeName = "";
        $scope.dataSetColumn.fieldName = "";
        $scope.dataSetColumn.textExpression = "";
        $scope.dataSetColumn.fieldType = "";
        $scope.dataSetColumn.displayFormat = "";
        $scope.widgetObj.lastNdays = "";
        $scope.widgetObj.lastNweeks = "";
        $scope.widgetObj.lastNmonths = "";
        $scope.widgetObj.lastNyears = "";
        $scope.widgetObj.customStartDate = "";
        $scope.widgetObj.customStartDate = "";
        $scope.widgetObj.allAccount = "";
        $scope.widgetObj.selectAll = "";
        $scope.widgetObj.chartColorOption = "";
        $scope.widgetObj.targetColors = "";
        $scope.showPreviewChart = false;
        $scope.showColumnDefs = false;
        $scope.showFilter = false;
        $scope.loadingColumnsGif = false;
        $scope.showDateRange = false;
        $scope.showSortBy = false;
        $scope.showColor = false;
    }

    $scope.save = function (widget) {
        addColor = [];
        $scope.jsonData = "";
        $scope.queryFilter = "";
        var widgetColor = "";
        if (widget.targetColors) {
            widgetColor = widget.targetColors.map(function (value, key) {
                if (value) {
                    return value.color;
                }
            }).join(',');
        }

        if (widget.chartType != 'text') {
            try {
                if ($('.query-builder').queryBuilder('getRules')) {
                    $scope.jsonData = JSON.stringify($('.query-builder').queryBuilder('getRules'));
                    $scope.queryFilter = $('.query-builder').queryBuilder('getSQL', false, true).sql;
                }
            } catch (e) {

            }
        }
        try {
            $scope.customStartDate = widget.dateRangeName == "Custom" ? moment($('#widgetDateRange').data('daterangepicker').startDate).format('MM/DD/YYYY') : $stateParams.startDate; //$scope.startDate.setDate($scope.startDate.getDate() - 1);
            $scope.customEndDate = widget.dateRangeName == "Custom" ? moment($('#widgetDateRange').data('daterangepicker').endDate).format('MM/DD/YYYY') : $stateParams.endDate;
        } catch (e) {

        }
        if (widget.dateRangeName != "Custom") {
            $scope.customStartDate = "";
            $scope.customEndDate = "";
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
                expression: value.expression,
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
                combinationType: value.combinationType,
                derivedId: value.derivedId
            };
            widgetColumnsData.push(columnData);
        });
        var dataSourceTypeId;
        var dataSetTypeId;
        var dataSourceObj;
        var dataSetObj;
        if (widget.chartType != 'text') {
            dataSourceTypeId = widget.dataSourceId ? widget.dataSourceId.id : null;
            dataSourceObj = widget.dataSourceId;
            dataSetTypeId = widget.dataSetId ? widget.dataSetId.id : null;
            dataSetObj = widget.dataSetId;
        } else {
            dataSourceTypeId = 0;
            dataSetTypeId = 0;
        }

        if (widget.allAccount === 1) {
            widget.accountId = null;
        } else {
            widget.accountId = parseInt($stateParams.accountId);
        }
        var data = {
            id: widget.id,
            chartType: $scope.chartTypeName ? $scope.chartTypeName : widget.chartType,
            widgetTitle: widget.previewTitle,
            widgetColumns: widgetColumnsData,
            dataSourceId: dataSourceTypeId,
            dataSetId: dataSetTypeId,
            tableFooter: widget.tableFooter,
            zeroSuppression: widget.zeroSuppression,
            maxRecord: widget.maxRecord,
            dateDuration: widget.dateDuration,
            content: widget.content,
            width: widget.width ? widget.width : 12,
            dateRangeName: widget.dateRangeName,
            lastNdays: widget.lastNdays,
            lastNweeks: widget.lastNweeks,
            lastNmonths: widget.lastNmonths,
            lastNyears: widget.lastNyears,
            isGridLine: widget.isGridLine,
            customStartDate: $scope.customStartDate ? $scope.customStartDate : widget.customStartDate,
            customEndDate: $scope.customEndDate ? $scope.customEndDate : widget.customEndDate,
            jsonData: $scope.jsonData ? $scope.jsonData : null,
            queryFilter: $scope.queryFilter ? $scope.queryFilter : null,
            accountId: widget.accountId,
            timeSegment: widget.timeSegment ? widget.timeSegment.type : null,
            productSegment: widget.productSegment ? widget.productSegment.type : null,
            networkType: widget.networkType ? widget.networkType.type : null,
            createdBy: widget.createdBy,
            chartColorOption: widgetColor
        };
        clearEditAllWidgetData();
        $http({method: widget.id ? 'PUT' : 'POST', url: 'admin/ui/dbWidget/' + $stateParams.tabId, data: data}).success(function (response) {
            var widgetColors;
            var newWidgetResponse = response;
            if ($scope.userChartColors.optionValue) {
                widgetColors = $scope.userChartColors.optionValue.split(',');
            }
            response.chartColors = widgetColors;
            // if()
            if (!data.id) {
                $scope.columnHeaderColuction = [];
                $scope.widgetDataSetColumnsDefs.forEach(function (val, key) {
                    var collectionFieldDefs = {
                        id: val.id,
                        functionName: val.functionName,
                        dataFormat: val.dataFormat,
                        displayFormat: val.displayFormat,
                        sortPriority: val.sortPriority,
                        sortOrder: val.sortOrder,
                        agregationFunction: val.agregationFunction,
                        groupPriority: val.groupPriority,
                        widgetId: val.widgetId,
                        expression: val.expression,
                        userId: val.userId,
                        fieldName: val.fieldName,
                        status: val.status,
                        fieldType: val.fieldType,
                        displayName: val.displayName
                    };
                    $scope.columnHeaderColuction.push(collectionFieldDefs);
                });
                $http({method: 'POST', url: 'admin/ui/saveDataSetColumnsForWidget/' + response.id, data: $scope.columnHeaderColuction}).success(function (data) {
                });
            }
            $scope.derivedColumns = [];
            $scope.collectionFields.forEach(function (value, key) {
                var columnData = {
                    id: value.id,
                    expression: value.expression,
                    fieldName: value.fieldName,
                    displayName: value.displayName,
                    displayFormat: value.displayFormat,
                    status: value.status,
                    functionName: value.functionName,
                    columnName: value.columnName,
                    baseField: value.baseField,
                    dateRangeName: value.dateRangeName,
                    customStartDate: value.customStartDate,
                    customEndDate: value.customEndDate,
                    lastNdays: value.lastNdays,
                    lastNmonths: value.lastNmonths,
                    lastNweeks: value.lastNweeks,
                    lastNyears: value.lastNyears,
                    fieldType: value.fieldType,
                    sortPriority: value.sortPriority,
                    userId: value.userId, //value.userId,
                    dataSetId: data.dataSetId ? data.dataSetId.id : null
                };
                $scope.derivedColumns.push(columnData);
            });
            var colData = {
                tableColumns: $scope.derivedColumns
            };
            widget.chartType = "";
            $http({method: 'POST', url: 'admin/ui/createWidgetColumn/' + response.id, data: colData}).success(function (response) {
                $scope.chartTypeName = "";
                widget.id = data.id;
                widget.chartType = data.chartType;
                widget.chartColorOption = data.chartColorOption;
                widget.widgetTitle = data.widgetTitle;
                widget.dataSetId = dataSetObj;
                widget.dataSourceId = dataSourceObj;
                widget.timeSegment = data.timeSegment;
                widget.productSegment = data.productSegment;
                widget.networkType = data.networkType;
                widget.columns = data.widgetColumns;
                widget.dateRangeName = data.dateRangeName;
                widget.customStartDate = data.customStartDate;
                widget.customEndDate = data.customEndDate;
                widget.lastNdays = data.lastNdays;
                widget.lastNweeks = data.lastNweeks;
                widget.lastNmonths = data.lastNmonths;
                widget.lastNyears = data.lastNyears;
                widget.allAccount = data.accountId;
                widget.jsonData = data.jsonData;
                widget.queryFilter = data.queryFilter;
                data.dataSourceId = dataSourceObj;
                data.dataSetId = dataSetObj;
                widget.chartColors = widgetColors;
                widget = data;
                if (!data.id) {
                    $scope.widgets.unshift(newWidgetResponse);
                }
            });
//            if (!response.id) {
//                $('.showEditWidget').modal('hide');
//                return;
//            }
            $('.showEditWidget').modal('hide');
        });
    };
    var tempTargetColors = [];
    $scope.cancel = function (widgetObj) {
        resetQueryBuilder();
        addColor = [];
        $('.showEditWidget').modal('hide');
        angular.forEach(setDefaultWidgetObj, function (val, key) {
            $scope.widgetObj.id = val.id;
            $scope.widgetObj.previewTitle = val.widgetTitle;
            $scope.widgetObj.chartType = val.chartType;
            $scope.widgetObj.dataSourceId = val.dataSourceId;
            $scope.widgetObj.dataSetId = val.dataSetId;
            $scope.widgetObj.timeSegment = val.timeSegment;
            $scope.widgetObj.productSegment = val.productSegment;
            $scope.widgetObj.networkType = val.networkType;
            $scope.widgetObj.columns = val.columns;
            $scope.widgetObj.dateRangeName = val.dateRangeName;
            $scope.widgetObj.customStartDate = val.customStartDate;
            $scope.widgetObj.customEndDate = val.customEndDate;
            $scope.widgetObj.lastNdays = val.lastNdays;
            $scope.widgetObj.lastNweeks = val.lastNweeks;
            $scope.widgetObj.lastNmonths = val.lastNmonths;
            $scope.widgetObj.lastNyears = val.lastNyears;
            $scope.widgetObj.allAccount = val.accountId;
            $scope.widgetObj.selectAll = val.selectAll;
        });
        if ($scope.widgetObj.dataSourceId && $scope.widgetObj.dataSetId) {
            $scope.collectionFields = [];
            $scope.columnY1Axis = [];
            $scope.columnY2Axis = [];
        }
        $scope.chartTypeName = "";
        $scope.xColumn = "";
        $scope.tickerItem = "";
        $scope.funnelItem = "";
        $scope.y1Column = "";
        $scope.y2Column = "";
        $scope.selectPieChartYAxis = "";
        $scope.selectPieChartXAxis = "";
        $scope.dataSetColumn.fieldName = "";
        $scope.dataSetColumn.expression = "";
        $scope.dataSetColumn.fieldType = "";
        $scope.dataSetColumn.displayFormat = "";
        $scope.showSortBy = false;
        $scope.selectedChartType = "";
        $scope.showPreviewChart = false;
        $scope.showColumnDefs = false;
        $scope.showFilter = false;
        $scope.loadingColumnsGif = false;
        $scope.showColor = false;
        $scope.showDateRange = false;
        $scope.widgetObj.chartColorOption = "";
        $scope.widgetObj.targetColors = "";
        if ($scope.chartColorOptionsVal) {
            $scope.widgetObj.chartColorOption = $scope.chartColorOptionsVal;
            var widgetTargetColors = $scope.chartColorOptionsVal.split(",");
            for (var i = 0; i < widgetTargetColors.length; i++) {
                var targetColors = {
                    color: widgetTargetColors[i]
                };
                tempTargetColors.push(targetColors);
            }
            $scope.widgetObj["targetColors"] = tempTargetColors;
        }
    };
});

app.directive('uiColorpicker', function () {
    return {
        restrict: 'AE',
        require: 'ngModel',
        scope: false,
        replace: true,
        template: "<span><input class='input-small' /></span>",
        link: function (scope, element, attrs, ngModel) {
            var input = element.find('input');
            var options = angular.extend({
                color: ngModel.$viewValue,
                change: function (color) {
                    scope.$apply(function () {
                        ngModel.$setViewValue(color.toHexString());
                    });
                }
            }, scope.$eval(attrs.options));
            ngModel.$render = function () {
                input.spectrum('set', ngModel.$viewValue || '');
            };
            input.spectrum(options);
        }
    };
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
                return "Total :"
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
                        scope.hideAll(value.data, false)
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
                    return dashboardFormat(column, value);
                }
                return value;
            };

            scope.getStars = function (rating) {
                var val = parseFloat(rating);
                var size = val / 5 * 100;
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
            tickerTitleName: '@',
            widgetObj: '@'
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
                tickerName.push({fieldName: value.fieldName, displayName: value.displayName, displayFormat: value.displayFormat});
            });

            var format = function (column, value) {
                if (!value) {
                    return "-";
                }
                if (column.displayFormat) {
//                    if (isNaN(value)) {
//                        return "-";
//                    }
                    return dashboardFormat(column, value);
                }
                return value;
            };

            var setData = [];
            var data = [];
            var tickerDataSource = JSON.parse(scope.tickerSource);
            var url = "admin/proxy/getData?";
//            if (tickerDataSource.dataSourceId.dataSourceType == "sql") {
//                url = "admin/proxy/getJson?url=../dbApi/admin/dataSet/getData&";
//            }
            var dataSourcePassword;
            if (tickerDataSource.dataSourceId.password) {
                dataSourcePassword = tickerDataSource.dataSourceId.password;
            } else {
                dataSourcePassword = '';
            }

            var getWidgetObj = JSON.parse(scope.widgetObj);
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
                        "&startDate=" + $stateParams.startDate +
                        "&endDate=" + $stateParams.endDate +
                        "&productSegment=" + setProductSegment +
                        "&timeSegment=" + setTimeSegment +
                        "&networkType=" + setNetworkType +
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
            };
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
            widgetObj: '@',
            defaultChartColor: '@'
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

            if (!scope.widgetColumns) {
                return;
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
                            if (format.indexOf("%") > -1) {
                                return d3.format(format)(value / 100);
                            }
                            return d3.format(format)(value);
                        };
                    } else {
                        labels["format"][displayName] = function (value) {
                            return formatBySecond(parseInt(value))
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
            var lineChartDataSource = JSON.parse(scope.lineChartSource);
            if (scope.lineChartSource) {

                var url = "admin/proxy/getData?";
//                if (lineChartDataSource.dataSourceId.dataSourceType == "sql") {
//                    url = "admin/proxy/getJson?url=../dbApi/admin/dataSet/getData&";
//                }
                var dataSourcePassword;
                if (lineChartDataSource.dataSourceId.password) {
                    dataSourcePassword = lineChartDataSource.dataSourceId.password;
                } else {
                    dataSourcePassword = '';
                }
                var getWidgetObj = JSON.parse(scope.widgetObj);
                var defaultColors = scope.defaultChartColor ? JSON.parse(scope.defaultChartColor) : "";
                //var defaultColors = ['#59B7DE', '#D7EA2B', '#FF3300', '#E7A13D', '#3F7577', '#7BAE16'];

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
                scope.refreshLineChart = function () {
                    $http.get(url + 'connectionUrl=' + lineChartDataSource.dataSourceId.connectionString +
                            "&dataSetId=" + lineChartDataSource.id +
                            "&accountId=" + (getWidgetObj.accountId ? (getWidgetObj.accountId.id ? getWidgetObj.accountId.id : getWidgetObj.accountId) : $stateParams.accountId) +
                            "&userId=" + (lineChartDataSource.userId ? lineChartDataSource.userId.id : null) +
                            "&driver=" + lineChartDataSource.dataSourceId.sqlDriver +
                            "&startDate=" + $stateParams.startDate +
                            "&endDate=" + $stateParams.endDate +
                            "&productSegment=" + setProductSegment +
                            "&timeSegment=" + setTimeSegment +
                            "&networkType=" + setNetworkType +
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
                            var chartMaxRecord = JSON.parse(scope.widgetObj);
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
                                });
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
                            //yaxis mapping data
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
                                padding: {
                                    top: 10,
                                    right: 50,
                                    bottom: 10,
                                    left: 50,
                                },
                                bindto: element[0],
                                data: {
                                    x: xAxis.fieldName,
                                    columns: columns,
                                    labels: labels,
                                    axes: axes,
                                    types: chartCombinationtypes
                                },
                                color: {
                                    pattern: chartColors ? chartColors : defaultColors
                                },
                                tooltip: {show: false},
                                axis: {
                                    x: {
                                        tick: {
                                            format: function (x) {
                                                return xData[x];
                                            },
                                            culling: false
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
                };
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
            widgetObj: '@',
            defaultChartColor: '@'
        },
        link: function (scope, element, attr) {
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
            if (!scope.widgetColumns) {
                return;
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
                            if (format.indexOf("%") > -1) {
                                return d3.format(format)(value / 100);
                            }
                            return d3.format(format)(value);
                        };
                    } else {
                        labels["format"][displayName] = function (value) {
                            return formatBySecond(parseInt(value))
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

            var barChartDataSource = JSON.parse(scope.barChartSource);
            if (scope.barChartSource) {

                var getWidgetObj = JSON.parse(scope.widgetObj);
                var url = "admin/proxy/getData?";
//                if (barChartDataSource.dataSourceId.dataSourceType == "sql") {
//                    url = "admin/proxy/getJson?url=../dbApi/admin/dataSet/getData&";
//                }
                var dataSourcePassword;
                if (barChartDataSource.dataSourceId.password) {
                    dataSourcePassword = barChartDataSource.dataSourceId.password;
                } else {
                    dataSourcePassword = '';
                }
                var defaultColors = scope.defaultChartColor ? JSON.parse(scope.defaultChartColor) : "";
//                var defaultColors = ['#59B7DE', '#D7EA2B', '#FF3300', '#E7A13D', '#3F7577', '#7BAE16'];
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

                scope.refreshBarChart = function () {
                    $http.get(url + 'connectionUrl=' + barChartDataSource.dataSourceId.connectionString +
                            "&dataSetId=" + barChartDataSource.id +
                            "&accountId=" + (getWidgetObj.accountId ? (getWidgetObj.accountId.id ? getWidgetObj.accountId.id : getWidgetObj.accountId) : $stateParams.accountId) +
                            "&userId=" + (barChartDataSource.userId ? barChartDataSource.userId.id : null) +
                            "&dataSetReportName=" + barChartDataSource.reportName +
                            "&driver=" + barChartDataSource.dataSourceId.sqlDriver +
                            "&location=" + $stateParams.locationId +
                            "&startDate=" + $stateParams.startDate +
                            "&endDate=" + $stateParams.endDate +
                            "&productSegment=" + setProductSegment +
                            "&timeSegment=" + setTimeSegment +
                            "&networkType=" + setNetworkType +
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
                            var gridData = JSON.parse(scope.widgetObj);
                            var chartMaxRecord = JSON.parse(scope.widgetObj);
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
                                });
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
                                padding: {
                                    top: 10,
                                    right: 50,
                                    bottom: 10,
                                    left: 50,
                                },
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
                                    pattern: chartColors ? chartColors : defaultColors
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
                };
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
            widgetObj: '@',
            defaultChartColor: '@'
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
            if (!scope.widgetColumns) {
                return;
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
                            if (format.indexOf("%") > -1) {
                                return d3.format(format)(value / 100);
                            }
                            return d3.format(format)(value);
                        };
                    } else {
                        labels["format"][displayName] = function (value) {
                            return formatBySecond(parseInt(value))
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
                    xAxis = {fieldName: value.fieldName, displayName: value.displayName, displayFormat: value.displayFormat};
                }
                if (value.yAxis) {
                    yAxis.push({fieldName: value.fieldName, displayName: value.displayName, displayFormat: value.displayFormat});
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
//                if (pieChartDataSource.dataSourceId.dataSourceType == "sql") {
//                    url = "admin/proxy/getJson?url=../dbApi/admin/dataSet/getData&";
//                }

                var dataSourcePassword;
                if (pieChartDataSource.dataSourceId.password) {
                    dataSourcePassword = pieChartDataSource.dataSourceId.password;
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

                scope.refreshPieChart = function () {
                    $http.get(url + 'connectionUrl=' + pieChartDataSource.dataSourceId.connectionString +
                            "&dataSetId=" + pieChartDataSource.id +
                            "&accountId=" + (getWidgetObj.accountId ? (getWidgetObj.accountId.id ? getWidgetObj.accountId.id : getWidgetObj.accountId) : $stateParams.accountId) +
                            "&userId=" + (pieChartDataSource.userId ? pieChartDataSource.userId.id : null) +
                            "&dataSetReportName=" + pieChartDataSource.reportName +
                            "&driver=" + pieChartDataSource.dataSourceId.sqlDriver +
                            "&startDate=" + $stateParams.startDate +
                            "&endDate=" + $stateParams.endDate +
                            "&productSegment=" + setProductSegment +
                            "&timeSegment=" + setTimeSegment +
                            "&networkType=" + setNetworkType +
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
                                });
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
                            var data = {};
                            var legends = [];
                            var yAxisField = yAxis[0];
                            chartData.forEach(function (e) {
                                legends.push(e[xAxis.fieldName]);
                                data[e[xAxis.fieldName]] = data[e[xAxis.fieldName]] ? data[e[xAxis.fieldName]] : 0 + e[yAxisField.fieldName] ? e[yAxisField.fieldName] : 0;
                            });
                            var chart = c3.generate({
                                padding: {
                                    top: 10,
                                    right: 50,
                                    bottom: 10,
                                    left: 50
                                },
                                bindto: element[0],
                                data: {
                                    json: [data],
                                    keys: {
                                        value: xData,
                                    },
                                    type: 'pie'
                                },
                                pie: {
                                    label: {
                                        format: function (value, ratio, id) {
                                            var percentage = d3.format("%.2f")(ratio);
                                            return  percentage + ", \n" + dashboardFormat(yAxisField, value);
                                        }
                                    }
                                },
                                color: {
                                    pattern: chartColors ? chartColors : defaultColors
                                },
                                tooltip: {
                                    show: true,
                                    format: {
                                        value: function (value, ratio, id) {
                                            var percentage = d3.format("%.2f")(ratio);
                                            return  percentage + ", \n" + dashboardFormat(yAxisField, value);
                                        }
                                    }
                                },
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
            widgetObj: '@',
            defaultChartColor: '@'
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
            if (!scope.widgetColumns) {
                return;
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
                            if (format.indexOf("%") > -1) {
                                return d3.format(format)(value / 100);
                            }
                            return d3.format(format)(value);
                        };
                    } else {
                        labels["format"][displayName] = function (value) {
                            return formatBySecond(parseInt(value))
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
            var areaChartDataSource = JSON.parse(scope.areaChartSource);
            if (scope.areaChartSource) {
                var url = "admin/proxy/getData?";
//                if (areaChartDataSource.dataSourceId.dataSourceType == "sql") {
//                    url = "admin/proxy/getJson?url=../dbApi/admin/dataSet/getData&";
//                }

                var dataSourcePassword;
                if (areaChartDataSource.dataSourceId.password) {
                    dataSourcePassword = areaChartDataSource.dataSourceId.password;
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

                scope.refreshAreaChart = function () {
                    $http.get(url + 'connectionUrl=' + areaChartDataSource.dataSourceId.connectionString +
                            "&dataSetId=" + areaChartDataSource.id +
                            "&accountId=" + (getWidgetObj.accountId ? (getWidgetObj.accountId.id ? getWidgetObj.accountId.id : getWidgetObj.accountId) : $stateParams.accountId) +
                            "&userId=" + (areaChartDataSource.userId ? areaChartDataSource.userId.id : null) +
                            "&dataSetReportName=" + areaChartDataSource.reportName +
                            "&driver=" + areaChartDataSource.dataSourceId.sqlDriver +
//                            "&location=" + $stateParams.locationId +
                            "&startDate=" + $stateParams.startDate +
                            "&endDate=" + $stateParams.endDate +
                            "&productSegment=" + setProductSegment +
                            "&timeSegment=" + setTimeSegment +
                            "&networkType=" + setNetworkType +
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
                                });
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
                                padding: {
                                    top: 10,
                                    right: 50,
                                    bottom: 10,
                                    left: 50,
                                },
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
                                    pattern: chartColors ? chartColors : defaultColors
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
            widgetObj: '@',
            defaultChartColor: '@'
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
            if (!scope.widgetColumns) {
                return;
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
                            if (format.indexOf("%") > -1) {
                                return d3.format(format)(value / 100);
                            }
                            return d3.format(format)(value);
                        };
                    } else {
                        labels["format"][displayName] = function (value) {
                            return formatBySecond(parseInt(value))
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
                    groupingFields.push({fieldName: value.fieldName, groupField: value.groupField, fieldType: value.fieldType, displayName: value.displayName});
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
                                return -1 * parseFloat(a[value.fieldName]);
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

            var stackedBarChartDataSource = JSON.parse(scope.stackedBarChartSource);
            if (scope.stackedBarChartSource) {
                var url = "admin/proxy/getData?";
//                if (stackedBarChartDataSource.dataSourceId.dataSourceType == "sql") {
//                    url = "admin/proxy/getJson?url=../dbApi/admin/dataSet/getData&";
//                }

                var dataSourcePassword;
                if (stackedBarChartDataSource.dataSourceId.password) {
                    dataSourcePassword = stackedBarChartDataSource.dataSourceId.password;
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

                scope.refreshStackedBarChart = function () {
                    $http.get(url + 'connectionUrl=' + stackedBarChartDataSource.dataSourceId.connectionString +
                            "&dataSetId=" + stackedBarChartDataSource.id +
                            "&accountId=" + (getWidgetObj.accountId ? (getWidgetObj.accountId.id ? getWidgetObj.accountId.id : getWidgetObj.accountId) : $stateParams.accountId) +
                            "&userId=" + (stackedBarChartDataSource.userId ? stackedBarChartDataSource.userId.id : null) +
                            "&dataSetReportName=" + stackedBarChartDataSource.reportName +
                            "&driver=" + stackedBarChartDataSource.dataSourceId.sqlDriver +
                            "&location=" + $stateParams.locationId +
                            "&startDate=" + $stateParams.startDate +
                            "&endDate=" + $stateParams.endDate +
                            "&productSegment=" + setProductSegment +
                            "&timeSegment=" + setTimeSegment +
                            "&networkType=" + setNetworkType +
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
                            var chartMaxRecord = JSON.parse(scope.widgetObj);
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
                                console.log("ySeriesData");

                                console.log(ySeriesData);
                                columns.push(ySeriesData);
                            });
                            console.log("groupNames");
                            console.log("new Columns");
                            console.log(columns);
                            console.log("len-->" + columns.length);

                            var groupingNames = [];
                            console.log("grouping Fields");
                            console.log(groupingFields);
                            angular.forEach(groupingFields, function (value, key) {
                                groupingNames.push(value.displayName);
                            });
                            angular.forEach(combinationTypes, function (value, key) {
                                chartCombinationtypes[[value.fieldName]] = value.combinationType;
                            });
                            console.log("grouping names");
                            console.log(groupingNames);
                            console.log("columns");
                            console.log(columns);
//                            document.write(columns);
                            console.log("xaxis");
                            console.log(xAxis);
                            console.log("xTicks");
                            console.log(xTicks);
                            var gridLine = false;
                            if (gridData.isGridLine == 'Yes') {
                                gridLine = true;
                            } else {
                                gridLine = false;
                            }

                            var chart = c3.generate({
                                padding: {
                                    top: 10,
                                    right: 50,
                                    bottom: 10,
                                    left: 50,
                                },
                                bindto: element[0],
                                data: {
                                    //visits,0,Sessions,61101,New Users,42251,% New Sessions,69.14944108934388,Exit Rate,51.01156431344717
//                                    columns: [
//                                       [ "Sessions", 61101 ],
//                                       [ "New Users", 42251 ],
//                                       [ "% New Sessions", 69.14944108934388 ],
//                                       [ "Exit Rate", 51.01156431344717 ]
//                                    ],
                                    x: xAxis.fieldName,
                                    labels: labels,
                                    axes: axes,
                                    types: chartCombinationtypes,
                                    columns: columns,
                                    type: 'bar',
                                    groups: [
                                        groupingNames
                                    ]
                                },
                                color: {
                                    pattern: chartColors ? chartColors : defaultColors
                                },
                                tooltip: {show: false},
                                axis: {
                                    x: {
                                        tick: {
                                            format: function (x) {
                                                return xData[x];
                                            },
                                            culling: false
                                        },

                                    },
                                    y2: y2
                                },
                                grid: {
                                    y: {
                                        lines: [{value: 0}]
                                    }
                                }
                            });
//                            var chart = c3.generate({
//                                padding: {
//                                    top: 10,
//                                    right: 50,
//                                    bottom: 10,
//                                    left: 50,
//                                },
//                                bindto: element[0],
//                                data: {
//                                    x: xAxis.fieldName,
//                                    columns: columns,
//                                    labels: labels,
//                                    type: 'bar',
//                                    groups: [groupingNames],
//                                    axes: axes,
//                                    types: chartCombinationtypes
//                                },
//                                color: {
//                                    pattern: chartColors ? chartColors : defaultColors
//                                },
//                                tooltip: {show: true},
//                                axis: {
//                                    x: {
//                                        tick: {
//                                            format: function (x) {
//                                                return xData[x];
//                                            }
//                                        }
//                                    },
//                                    y2: y2
//                                },
//                                grid: {
//                                    x: {
//                                        show: gridLine
//                                    },
//                                    y: {
//                                        show: gridLine
//                                    }
//                                }
//                            });
                        }
                    });
                }
                scope.setStackedBarChartFn({stackedBarChartFn: scope.refreshStackedBarChart});
                scope.refreshStackedBarChart();
            }
        }
    };
});

app.directive('scatterChartDirective', function ($http, $filter, $stateParams, orderByFilter, $timeout) {
    return{
        restrict: 'A',
        template: '<div ng-show="loadingScatter" class="text-center"><img src="static/img/logos/loader.gif" width="40"></div>' +
                '<div ng-show="hideEmptyScatter" class="text-center">{{scatterEmptyMessage}}</div>',
        scope: {
            setScatterChartFn: '&',
            getScatterWidgetObj: '&',
            scatterChartSource: '@',
            widgetId: '@',
            widgetColumns: '@',
            scatterChartId: '@',
            widgetObj: '@',
            defaultChartColor: '@'
        },
        link: function (scope, element, attr) {
            var labels = {format: {}};
            scope.loadingScatter = true;
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
            if (!scope.widgetColumns) {
                return;
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
                            if (format.indexOf("%") > -1) {
                                return d3.format(format)(value / 100);
                            }
                            return d3.format(format)(value);
                        };
                    } else {
                        labels["format"][displayName] = function (value) {
                            return formatBySecond(parseInt(value))
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
                                return -1 * parseFloat(a[value.fieldName]);
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
            var scatterChartDataSource = JSON.parse(scope.scatterChartSource);
            if (scope.scatterChartSource) {

                var url = "admin/proxy/getData?";
//                if (lineChartDataSource.dataSourceId.dataSourceType == "sql") {
//                    url = "admin/proxy/getJson?url=../dbApi/admin/dataSet/getData&";
//                }
                var dataSourcePassword;
                if (scatterChartDataSource.dataSourceId.password) {
                    dataSourcePassword = scatterChartDataSource.dataSourceId.password;
                } else {
                    dataSourcePassword = '';
                }

                var getWidgetObj = JSON.parse(scope.widgetObj);
                var defaultColors = scope.defaultChartColor ? JSON.parse(scope.defaultChartColor) : "";
                //var defaultColors = ['#59B7DE', '#D7EA2B', '#FF3300', '#E7A13D', '#3F7577', '#7BAE16'];

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

                scope.refreshScatterChart = function () {
                    $http.get(url + 'connectionUrl=' + scatterChartDataSource.dataSourceId.connectionString +
                            "&dataSetId=" + scatterChartDataSource.id +
                            "&accountId=" + (getWidgetObj.accountId ? (getWidgetObj.accountId.id ? getWidgetObj.accountId.id : getWidgetObj.accountId) : $stateParams.accountId) +
                            "&userId=" + (scatterChartDataSource.userId ? scatterChartDataSource.userId.id : null) +
                            "&driver=" + scatterChartDataSource.dataSourceId.sqlDriver +
                            "&startDate=" + $stateParams.startDate +
                            "&endDate=" + $stateParams.endDate +
                            "&productSegment=" + setProductSegment +
                            "&timeSegment=" + setTimeSegment +
                            "&networkType=" + setNetworkType +
                            '&username=' + scatterChartDataSource.dataSourceId.userName +
                            '&password=' + dataSourcePassword +
                            "&dataSetReportName=" + scatterChartDataSource.reportName +
                            '&widgetId=' + scope.widgetId +
                            '&url=' + scatterChartDataSource.url +
                            '&port=3306&schema=vb&query=' + encodeURI(scatterChartDataSource.query)).success(function (response) {
                        scope.loadingScatter = false;
                        if (!response.data) {
                            return;
                        }
                        scope.getScatterWidgetObj({obj: response.data})
                        if (response.data.length === 0) {
                            scope.scatterEmptyMessage = "No Data Found";
                            scope.hideEmptyScatter = true;
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
                                        var dateOrders = ["Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"];
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
                            var gridScatter = false;
                            if (gridData.isGridLine == 'Yes') {
                                gridScatter = true;
                            } else {
                                gridScatter = false;
                            }

                            var chart = c3.generate({
                                padding: {
                                    top: 10,
                                    right: 50,
                                    bottom: 10,
                                    left: 50,
                                },
                                bindto: element[0],
                                data: {
                                    x: xAxis.fieldName,
                                    columns: columns,
                                    labels: labels,
                                    axes: axes,
                                    type: 'scatter'
                                },
                                color: {
                                    pattern: chartColors ? chartColors : defaultColors
                                },
                                tooltip: {show: false},
                                axis: {
                                    x: {
                                        tick: {
                                            format: function (x) {
                                                return xData[x];
                                            },
                                            fit: false
                                        }
                                    },
                                    y2: y2
                                },

                                grid: {
                                    x: {
                                        show: gridScatter
                                    },
                                    y: {
                                        show: gridScatter
                                    }
                                }
                            });
                        }
                    });
                };
                scope.setScatterChartFn({scatterFn: scope.refreshScatterChart});
                scope.refreshScatterChart();
            }
        }
    };
});

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
                                scope.funnels.push({funnelTitle: value.displayName, totalValue: format(value, total), dataValue: total});
                            });
                        }
                        var data = scope.funnels;
                        scope.funnelCharts = [];
                        scope.funnelFiltered = $filter('orderBy')(scope.funnels, 'totalValue');
                        scope.fName = [];
                        scope.fValue = [];
                        scope.dValue = [];
                        angular.forEach(scope.funnels, function (value, key) {
                            var funnelFieldName = value.funnelTitle;
                            var funnelValue = value.totalValue;
                            var dataValue = value.dataValue;
                            scope.fName.push(funnelFieldName);
                            scope.fValue.push(funnelValue);
                            scope.dValue.push(dataValue);
                        });
                        var funnelData = filterFunnelByValue(scope.fName, scope.fValue, scope.dValue);
                        scope.funnelCharts = funnelData;
                        function filterFunnelByValue(name, value, dataValue) {
                            var len = name.length;
                            var temp, temp1 = 0, temp2 = 0;
                            for (var i = 0; i < len; i++) {
                                for (var j = i + 1; j < len; j++) {
                                    if (dataValue[i] < dataValue[j]) {
                                        temp = value[i];
                                        value[i] = value[j];
                                        value[j] = temp;
                                        temp1 = name[i];
                                        name[i] = name[j];
                                        name[j] = temp1;
                                        temp2 = dataValue[i];
                                        dataValue[i] = dataValue[j];
                                        dataValue[j] = temp2;
                                    }
                                }
                            }
                            return funnelArrayObjects(name, value);
                        }

                        function funnelArrayObjects(name, value) {
                            var funnelObject = [];
                            var funnelColor = chartColors ? chartColors : defaultColors;
                            var len = name.length;
                            for (var i = 0; i < len; i++) {
                                funnelObject.push([name[i], value[i], funnelColor[i]]);
                            }
                            return funnelObject;
                        }

                        /*Filter*/
                        function drawChart() {
                            var width = $(element[0]).width();
                            var height = 315;
                            var options = {
                                width: width,
                                height: height,
                                bottomPinch: 1, // How many sections to pinch
                                hoverEffects: true  // Whether the funnel has effects on hover
                            };
                            var funnel = new D3Funnel(scope.funnelCharts, options);
                            funnel.draw(element[0]);
                        }
                        drawChart();
                        $(window).on("resize", function () {
                            drawChart();
                        });
                    });
                }
                ;
                scope.setFunnelFn({funnelFn: scope.refreshFunnel});
                scope.refreshFunnel();
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
            };
        })
        .filter('capitalize', function () {
            return function (input) {
                return (!!input) ? input.charAt(0).toUpperCase() + input.substr(1).toLowerCase() : '';
            };
        })
        .filter('xAxis', [function () {
                return function (chartXAxis) {
                    var xAxis = ['', 'x-1'];
                    return xAxis[chartXAxis];
                };
            }])
        .filter('yAxis', [function () {
                return function (chartYAxis) {
                    var yAxis = ['', 'y-1', 'y-2'];
                    return yAxis[chartYAxis];
                };
            }])
        .filter('hideColumn', [function () {
                return function (chartYAxis) {
                    var hideColumn = ['No', 'Yes'];
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
        }
    };
    return service;
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
            widgetTableDateRange: '@'
        },
        link: function (scope, element, attr) {
//            $(document).ready(function (e) {
            $(".scheduler-list-style").click(function (e) {
                e.stopPropagation();
            });
            var widget = JSON.parse(scope.widgetTableDateRange);
            var widgetStartDate = widget.customStartDate ? widget.customStartDate : $stateParams.startDate; //JSON.parse(scope.widgetTableDateRange).customStartDate;
            var widgetEndDate = widget.customEndDate ? widget.customEndDate : $stateParams.endDate; //JSON.parse(scope.widgetTableDateRange).customEndDate;
            //Date range as a button
            $(element[0]).daterangepicker(
                    {
                        ranges: {
                            'Today': [moment(), moment()],
                            'Yesterday': [moment().subtract(1, 'days'), moment().subtract(1, 'days')],
                            'Last 7 Days': [moment().subtract(7, 'days'), moment().subtract(1, 'days')],
                            'Last 14 Days ': [moment().subtract(14, 'days'), moment().subtract(1, 'days')],
                            'Last 30 Days': [moment().subtract(30, 'days'), moment().subtract(1, 'days')],
                            'This Week (Mon - Today)': [moment().startOf('week').add(1, 'days'), moment().endOf(new Date())],
//                        'This Week (Mon - Today)': [moment().startOf('week').add(1, 'days'), moment().endOf(new Date())],
                            'Last Week (Mon - Sun)': [moment().subtract(1, 'week').startOf('week').add(1, 'days'), moment().startOf('week')],
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
                        startDate: widgetStartDate ? widgetStartDate : moment().subtract(30, 'days'),
                        endDate: widgetEndDate ? widgetEndDate : moment().subtract(1, 'days'),
                        maxDate: new Date(),
                        drops: "down" //down
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
            queryData: '@'
        },
        link: function (scope, element, attr) {
            scope.columns = scope.queryData;
            if (!scope.queryData) {
                return;
            }
            var jsonFilter = JSON.parse(scope.queryData);
            var columnList = JSON.parse(scope.queryData);
            var filterList = [];
            columnList.columns.forEach(function (value, key) {
                console.log(value);
                console.log(value.fieldType);
                console.log(value.fieldName);
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
                filterList.push({id: value.fieldName, label: value.fieldName, type: scope.fieldsType});
            });
            scope.buildQuery = filterList;
            if (jsonFilter.jsonData != null) {
                scope.jsonBuild = JSON.parse(jsonFilter.jsonData);
            }
//            scope.buildQuery = filterList;
//            if (jsonFilter.jsonData != null) {
//                scope.jsonBuild = JSON.parse(jsonFilter.jsonData);
//            }

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
