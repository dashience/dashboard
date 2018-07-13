app.controller('WidgetController', function ($scope, $http, $stateParams, $timeout, $filter, $cookies, localStorageService, $rootScope, $state, $window, $interval, $translate) {
    $scope.dispHideBuilder = true;
    $scope.widgets = [];
    $scope.tags = [];
    $scope.collectionFields = [];
    $scope.dragEnabled = true;
    $scope.showFilter = false;
    $scope.showColumnDefs = false;
    $scope.showDateRange = false;
    $scope.showWidgetDateRange = false;
    $scope.saveBtnIsDisable = true;
    $scope.permission = localStorageService.get("permission");
    $scope.accountID = $stateParams.accountId;
    $scope.accountName = $stateParams.accountName;
    $scope.productID = $stateParams.productId;
    $scope.widgetTabId = $stateParams.tabId;
    $scope.widgetStartDate = $stateParams.startDate;
    $rootScope.tabStartDate = $stateParams.startDate;
    $rootScope.tabEndDate = $stateParams.endDate;
    $scope.userId = $cookies.getObject("userId");
    $scope.widgetEndDate = $stateParams.endDate;
    $scope.userId = localStorageService.get("userId");
    var agency = localStorageService.get("agencyId");
    $scope.agencyId = agency.id;
    $scope.templateId = $stateParams.templateId;
    $scope.widgetDataSetColumnsDefs = [];

    $scope.agencyLanguage = $stateParams.lan;

    var lan = $scope.agencyLanguage ? $scope.agencyLanguage : null;
    changeLanguage(lan);

    function changeLanguage(key) {
        $translate.use(key);
    }

    function getUnMatchedColumns(allColumns, columns) {
        return allColumns.filter(function (value) {
            return isUnmatchedColumn(value, columns);
        })
    }

    function isUnmatchedColumn(val, columns) {
        return columns.every(function (matched) {
            return val.fieldName !== matched.fieldName;
        });
    }

    if ($scope.permission.createReport === true) {
        $scope.showCreateReport = true;
    } else {
        $scope.showCreateReport = false;
    }
    var tableTypeByDateRange = localStorageService.get("selectedTableType") ? localStorageService.get("selectedTableType") : "compareOff";
    if (tableTypeByDateRange == 'compareOn') {
        $scope.selectedTablesType = 'compareOn';
        $scope.compareDateRangeType = true;
    } else {
        $scope.selectedTablesType = 'compareOff';
        $scope.compareDateRangeType = false;
    }
    var compareStartDate = localStorageService.get("comparisonStartDate");
    var compareEndDate = localStorageService.get("comparisonEndDate");
    $scope.compareDateRange = {
        startDate: compareStartDate,
        endDate: compareEndDate
    };
    $scope.getTableType = tableTypeByDateRange ? tableTypeByDateRange : "compareOff";
    //Geo Map
    $scope.cities = [
        {id: 1, name: 'Oslo', pos: [59.923043, 10.752839]},
        {id: 2, name: 'Stockholm', pos: [59.339025, 18.065818]},
        {id: 3, name: 'Copenhagen', pos: [55.675507, 12.574227]},
        {id: 4, name: 'Berlin', pos: [52.521248, 13.399038]},
        {id: 5, name: 'Paris', pos: [48.856127, 2.346525]}
    ];

//    $scope.cities=[{id:1}]
    $http.get('static/datas/tickerIcons.json').success(function (response) {       //Popup- Select Chart-Type Json
        $scope.chartIcons = response;
    });

    $scope.selectIcon = function (widgetObj, selectIcon) {
        widgetObj.icon = selectIcon.icon;
    };

    $scope.findChartIcon = function (iconName) {
        if (!iconName) {
            return;
        }
        var selectedIconName;
        $scope.chartIcons.forEach(function (val, k) {
            if (val.icon == iconName) {
                selectedIconName = val.name;
            }
        });
        return selectedIconName;
    };

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
    });

    $http.get('static/datas/imageUrl.json').success(function (response) {       //Popup- Select Chart-Type Json
        $scope.chartTypes = response;
    });

    $http.get('admin/tag').success(function (response) {
        $scope.tags = response;
    });
    $scope.downloadXLSByWidget = function (widget) {
        var fileName;
        var name = widget.widgetTitle;
        if (name) {
            fileName = name.split(' ').join("");
        } else {
            fileName = "Dashience";
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
    ];
    //Aggregation Type-Popup
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

//   $scope.combinationType="area";
//    $scope.combinationTypeName = {};
//    $scope.combinationTypeName={combinationType:'area'}

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
            widgetObj.customEndDate = "";
            widgetObj.customStartDate = "";
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
        $scope.gaugeItem = "";
        $scope.showPreviewChart = false;
        $scope.showColumnDefs = false;
        $scope.showFilter = false;
        $scope.loadingColumnsGif = false;
        $scope.showDateRange = false;
        $scope.showSortBy = false;
        $scope.saveBtnIsDisable = true;
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
                    var favWidget = $.grep(favResponse, function (b) {
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
                    if (value.chartType == 'horizontalBar') {
                        value.isHorizontalBar = true;
                    } else {
                        value.isHorizontalBar = false;
                    }
                });
                $scope.widgets = widgetItems;
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
//        console.log("******************* EDIT WIDGET ****************");
//        console.log(widget);
//
//        console.log("******************* EDIT WIDGET ****************");


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
        if (widget.dateRangeName == "None") {
            widget.customStartDate = "";
            widget.customEndDate = "";
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
            accountId: widget.accountId ? widget.accountId.id : null,
            icon: widget.icon
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
//        $scope.gaugeItem = [];
        var value1 = "";
        var value2 = "";
        var value3 = "";
        angular.forEach(widget.columns, function (val, key) {
            if (val.xAxis == 1) {
                value1 = val;
                $scope.xColumn = val;
                $scope.selectPieChartXAxis = val;
            }
            if (val.isLocation == 1) {
                value1 = val;
                $scope.location = val;
            }
            if (val.isLatitude == 1) {
                value2 = val;
                $scope.latitude = val;
            }
            if (val.isLongitude == 1) {
                value3 = val;
                $scope.longitude = val;
            }
            if (val.yAxis == 1) {
                if (val.fieldName) {
                    if (widget.chartType == "pie") {
                        $scope.selectPieChartYAxis = val;
                    }
                    $scope.y1Column.push(val);
                }
                value2 = val;
            }
            if (val.yAxis == 2) {
                if (val.fieldName) {
                    $scope.y2Column.push(val);
                }
                value3 = val;
            }
            if (val.groupField) {
                $scope.groupingFields.push(val);
            }
            if (widget.chartType === 'ticker') {
                value1 = val;
                $scope.tickerItem.push(val);
            }
            if (widget.chartType === 'funnel') {
                value1 = val;
                $scope.funnelItem.push(val);
            }
            if (widget.chartType === 'gauge') {
                value1 = val;
                $scope.gaugeItem = val;
            }
            if (widget.chartType === 'table') {
                value1 = val;
            }
        });
        $scope.saveBtnIsDisable = checkValidationBySaveBtn(widget.chartType, value1, value2, value3);
        tableDef(widget, $scope.y1Column, $scope.y2Column);
        console.log("Edit widget object -->", $scope.widgetObj);
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
        $scope.hideSelectedColumn = true;
        $scope.afterLoadWidgetColumns = false;
        $scope.queryBuilderList = "";
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
            console.log("response-------------->", response);
            $scope.collectionFields = [];
            $scope.collectionFields = response.columnDefs;
            if ($scope.collectionFields.length == widget.columns.length) {
                widget.selectAll = 1;
            } else {
                widget.selectAll = 0;
            }
//            $scope.locations = response.data;
            console.log("widget------------->", widget);
            var filterList = {
                columns: response.columnDefs,
                widgetObj: widget
            };
//            $timeout(function () {
            $scope.queryBuilderList = filterList;
            resetQueryBuilder();
//            }, 40);
            $scope.widgetDataSetColumnsDefs = response.columnDefs;
            var getWidgetColumns = widget.columns;

            $scope.afterLoadWidgetColumns = true;
            $scope.columnY1Axis = [];
            $scope.columnY2Axis = [];
            $scope.columnXAxis = [];
            $scope.columnPieXAxis = [];
            $scope.columnPieYAxis = [];
            $scope.tickerAxis = [];


            angular.forEach($scope.collectionFields, function (value, key) {
                $scope.columnY1Axis.push(value);
                $scope.columnY2Axis.push(value);
                $scope.columnXAxis.push(value);
                $scope.columnPieXAxis.push(value);
                $scope.columnPieYAxis.push(value);
                $scope.tickerAxis.push(value);
            });

            $scope.collectionFields.forEach(function (value, k) {
                var machField = $.grep(getWidgetColumns, function (b) {
                    return b.fieldName === value.fieldName;
                });
                if (machField.length > 0) {
                    value.selectColumnDef = 1;
                } else {
                    value.selectColumnDef = 0;
                }
                console.log("machField -->", machField);
                var index = $scope.collectionFields.indexOf(machField);
                console.log($scope.collectionFields[index]);
//                $scope.collectionFields.splice(index, 1);
//                console.log("Remove collection Fields", $scope.collectionFields);
            });

            if ($scope.xColumn) {
                var dataY1 = $scope.columnY1Axis.find(function (item, i) {
                    if (item.fieldName === $scope.xColumn.fieldName) {
                        return item;
                    }
                });
                var dataY2 = $scope.columnY2Axis.find(function (item, i) {
                    if (item.fieldName === $scope.xColumn.fieldName) {
                        return item;
                    }
                });
                var indexY1 = $scope.columnY1Axis.indexOf(dataY1);
                var indexY2 = $scope.columnY2Axis.indexOf(dataY2);
                $scope.columnY1Axis.splice(indexY1, 1);
                $scope.columnY2Axis.splice(indexY2, 1);
            }

            if ($scope.selectPieChartXAxis && $scope.selectPieChartYAxis) {
                var dataX = $scope.columnPieXAxis.find(function (item, i) {
                    if (item.fieldName === $scope.selectPieChartYAxis.fieldName) {
                        return item;
                    }
                });
                var dataY = $scope.columnPieYAxis.find(function (item, i) {
                    if (item.fieldName === $scope.selectPieChartXAxis.fieldName) {
                        return item;
                    }
                });
                var indexX = $scope.columnPieXAxis.indexOf(dataX);
                var indexY = $scope.columnPieYAxis.indexOf(dataY);
                $scope.columnPieXAxis.splice(indexX, 1);
                $scope.columnPieYAxis.splice(indexY, 1);
            }



            if (widget.chartType === 'funnel') {
                $scope.collectionFields = getUnMatchedColumns($scope.collectionFields, getWidgetColumns);
            }




            angular.forEach(y1Column, function (value, key) {
                var dataY1 = $scope.columnY1Axis.find(function (item, i) {
                    if (item.fieldName === value.fieldName) {
                        return item;
                    }
                });
                var dataX = $scope.columnXAxis.find(function (item, i) {
                    if (item.fieldName === value.fieldName) {
                        return item;
                    }
                });
                var indexY1 = $scope.columnY1Axis.indexOf(dataY1);
                var indexX = $scope.columnXAxis.indexOf(dataX);
                $scope.columnY1Axis.splice(indexY1, 1);
                $scope.columnXAxis.splice(indexX, 1);
            });

            angular.forEach(y2Column, function (value, key) {
                var dataY2 = $scope.columnY2Axis.find(function (item, i) {
                    if (item.fieldName === value.fieldName) {
                        return item;
                    }
                });
                var dataX = $scope.columnXAxis.find(function (item, i) {
                    if (item.fieldName === value.fieldName) {
                        return item;
                    }
                });
                var indexY2 = $scope.columnY2Axis.indexOf(dataY2);
                var indexX = $scope.columnXAxis.indexOf(dataX);
                $scope.columnY2Axis.splice(indexY2, 1);
                $scope.columnXAxis.splice(indexX, 1);
            });

            angular.forEach(y2Column, function (value, key) {
                var data = $scope.columnY1Axis.find(function (item, i) {
                    if (item.fieldName === value.fieldName) {
                        return item;
                    }
                });
                var index = $scope.columnY1Axis.indexOf(data);
                $scope.columnY1Axis.splice(index, 1);
            });

            angular.forEach(y1Column, function (value, key) {
                var data = $scope.columnY2Axis.find(function (item, i) {
                    if (item.fieldName === value.fieldName) {
                        return item;
                    }
                });
                var index = $scope.columnY2Axis.indexOf(data);
                $scope.columnY2Axis.splice(index, 1);
            });
            /*
             * for ticker code
             */
            angular.forEach($scope.tickerItem, function (value, key) {
                var data = $scope.tickerAxis.find(function (item, i) {
                    if (item.fieldName === value.fieldName) {
                        return item;
                    }
                });
                var index = $scope.tickerAxis.indexOf(data);
                $scope.tickerAxis.splice(index, 1);
            });



            resetQueryBuilder();
        });

    }

    $scope.resetTimeProduct = function () {
        $scope.timeSegments = [];
        $scope.productSegments = [];
    };

    $scope.getNewDataSetObj = function (widget, chartTypeName) {
        $scope.saveBtnIsDisable = true;
        console.log("&&&&&&&&& widget", widget);
        console.log("chartTypeName --------", chartTypeName);
        $scope.hideSelectedColumn = true;
        $scope.queryBuilderList = "";
        $scope.dispHideBuilder = true;
        widget.columns = [];
        widget.selectAll = "";
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
        $scope.gaugeItem = "";
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
            console.log("resp------------------->", response);
            $scope.afterLoadWidgetColumns = true;
            $scope.hideSelectedColumn = false;
//            if ((chartTypeName ? chartTypeName : widgetList.chartType) !== 'table') {
//                $scope.collectionFields = response.columnDefs;
//            } else {

//            $scope.xAxisValue=response.data;

//            $scope.mapLocation=[];
//            
//            angular.forEach($scope.xAxisValue,function(value,key){
//                $scope.locationValue=value;
//            });



            $scope.collectionFields = response.columnDefs;

            var filterList = {
                columns: response.columnDefs,
                widgetObj: widget
            };
            //$timeout(function () {
            $scope.queryBuilderList = filterList;
            resetQueryBuilder();
            //}, 40);
            $scope.widgetDataSetColumnsDefs = response.columnDefs;
//            }
            $scope.columnY1Axis = [];
            $scope.columnY2Axis = [];
            $scope.columnXAxis = [];
            $scope.columnPieXAxis = [];
            $scope.columnPieYAxis = [];
            $scope.tickerAxis = [];
            angular.forEach($scope.collectionFields, function (value, key) {
                $scope.columnY1Axis.push(value);
                $scope.columnY2Axis.push(value);
                $scope.columnXAxis.push(value);
                $scope.columnPieXAxis.push(value);
                $scope.columnPieYAxis.push(value);
                $scope.tickerAxis.push(value);
            });
            console.log("ticker axis--------->", $scope.tickerAxis);
            resetQueryBuilder();
        });
    };

    $scope.selectWidgetDataSource = function (dataSourceName) {

        if (!dataSourceName) {
            return;
        }

        if (dataSourceName.dataSourceType === "xls" || dataSourceName.dataSourceType === "csv") {
            $scope.showWidgetDateRange = true;
        } else {
            $scope.showWidgetDateRange = false;
        }
        $scope.y1Column = "";
        $scope.selectPieChartXAxis = "";
        $scope.selectPieChartYAxis = "";
        $scope.y2Column = "";
        $scope.tickerItem = "";
        $scope.funnelItem = "";
        $scope.gaugeItem = "";
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

                    $scope.timeSegments.forEach(function (val, key) {
                        if (val.type === timeSegmentType) {
                            $scope.widgetObj.timeSegment = val;
                        }
                    });

                    $scope.productSegments.forEach(function (val, key) {
                        if (val.type === productSegmentType) {
                            $scope.widgetObj.productSegment = val;
                        }
                    });
                }
            });




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

    $scope.selectChart = function (chartType, widgetObj) {
        console.log("**************************** SELECT CHART ******************");
        console.log("chartType -->", chartType);
        console.log("widgetobj -->", widgetObj);
        console.log("**************************** SELECT CHART ******************");
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
        $scope.advanced = false;

        if (chartType.type === 'horizontalBar') {
            widgetObj.isHorizontalBar = true;
        } else {
            widgetObj.isHorizontalBar = false;
        }
        $scope.widgetObj.selectAll = "";
        $scope.widgetObj.chartColorOption = "";
        $scope.widgetObj.targetColors = "";
        if ($scope.chartTypeName) {
            widgetObj.columns = [];
            $scope.collectionFields.forEach(function (value, k) {
                var machField = $.grep(widgetObj.columns, function (b) {
                    return b.fieldName === value.fieldName;
                });
                if (machField.length > 0) {
                    value.selectColumnDef = 1;
                } else {
                    value.selectColumnDef = 0;
                }
            });
        }
        $scope.xColumn = "";
        $scope.selectPieChartXAxis = "";
        $scope.selectPieChartYAxis = "";
        $scope.y1Column = "";
        $scope.y2Column = "";
        $timeout(function () {
            $scope.hideSelectedColumn = false;
        }, 50);

        $scope.saveBtnIsDisable = true;
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
        $scope.advanced = false;
    };

    $scope.showFilterList = function () {
        $scope.showFilter = true;
        $scope.loadingColumnsGif = true;
        $scope.showColumnDefs = false;
        $scope.showPreviewChart = false;
        $scope.showDateRange = false;
        $scope.showColor = false;
        $scope.advanced = false;
        $scope.showSortBy = false;
    };

    $scope.showEditor = function (chartType, widget) {
        $scope.chartTypeName = chartType ? chartType : widget.chartType;
        $scope.showSortBy = false;
        $scope.showPreviewChart = true;
        $scope.showColor = false;
        $scope.showDateRange = false;
        $scope.advanced = false;
    };

    $scope.showDateDurations = function () {
        $scope.showSortBy = false;
        $scope.showDateRange = true;
        $scope.loadingColumnsGif = false;
        $scope.showFilter = false;
        $scope.showColumnDefs = false;
        $scope.showColor = false;
        $scope.showPreviewChart = false;
        $scope.advanced = false;
    };

    $scope.showSortingColumn = function () {
        $scope.showSortBy = true;
        $scope.showDateRange = false;
        $scope.loadingColumnsGif = false;
        $scope.showFilter = false;
        $scope.showColumnDefs = false;
        $scope.showColor = false;
        $scope.showPreviewChart = false;
        $scope.advanced = false;
    };

    $scope.showColorList = function () {
        $scope.showColor = true;
        $scope.showFilter = false;
        $scope.showSortBy = false;
        $scope.loadingColumnsGif = false;
        $scope.showColumnDefs = false;
        $scope.showPreviewChart = false;
        $scope.showDateRange = false;
        $scope.advanced = false;
    };
    $scope.advanced = false;
    $scope.showAdvancedOptions = function () {
        $scope.advanced = true;
        $scope.showColor = false;
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
        $scope.advanced = false;
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
//        $scope.dispHideBuilder = true;
        var value1;
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
                        widgetId: obj.widgetId,
                        category: obj.category
                    };
                    widget.columns.push(data);
                }
            });
            value1 = widget.columns[0].fieldName;
            $scope.saveBtnIsDisable = checkValidationBySaveBtn("table", value1);
        } else {
            widget.columns = [];
            angular.forEach(columns, function (val, key) {
                val.selectColumnDef = 0;
            });
            $scope.saveBtnIsDisable = checkValidationBySaveBtn("table", value1);
        }
//        $timeout(function () {
//            $scope.queryBuilderList = widget;
//            resetQueryBuilder();
//        }, 50);
    };

    $scope.selectColumnItem = function (obj, widget) {
        var value1;
        console.log("**************************8 TABLE ********************************");
        console.log("object", obj);
        console.log("widget", widget);
//        $scope.dispHideBuilder = true;
        obj.columnsButtons = true;
        var checkColumnDef = obj.selectColumnDef;
        if (checkColumnDef === 1) {
            var data = {
                derivedColumnId: obj.derivedColumnId,
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
                widgetId: obj.widgetId,
                category: obj.category
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

        if (widget.columns.length > 0 || widget.selectAll === 1) {
            value1 = widget.columns[0].fieldName;
            $scope.saveBtnIsDisable = checkValidationBySaveBtn("table", value1);
        } else {
            $scope.saveBtnIsDisable = checkValidationBySaveBtn("table", value1);
        }
//        $timeout(function () {
//            $scope.queryBuilderList = widget;
//            resetQueryBuilder();
//        }, 50);
    };

    $scope.removeSelectedValue = function (widget, obj, index) {
        var value1;
        widget.selectAll = 0;
//        $scope.dispHideBuilder = true;
        widget.columns.splice(index, 1);
        $scope.collectionFields.forEach(function (val, key) {
            if (val.displayName === obj.displayName) {
                val.selectColumnDef = 0;
            }
        });
        $timeout(function () {
            resetQueryBuilder();
        }, 40);

        if (widget.columns.length > 0 || widget.selectAll === 1) {
            value1 = widget.columns;
            $scope.saveBtnIsDisable = checkValidationBySaveBtn("table", value1);
        } else {
            $scope.saveBtnIsDisable = checkValidationBySaveBtn("table", value1);
        }
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
        $scope.duplicateLoading = true;
//        var dialog = bootbox.dialog({
//            title: '<h4>Duplicate Widget</h4>',
//            message: '<center><img src="static/img/logos/loader.gif" width="50"></center>',
//            closeButton: false,
//            buttons: {
//                ok: {
//                    label: "OK",
//                    className: 'btn-success btn-xs',
//                    callback: function () {
//                        dialog.modal('hide');
//                    }
//                }
//            }});

        $http.get("admin/ui/dbWidgetDuplicate/" + widgetData.widgetId + "/" + widgetData.tabId).success(function (response) {
            $http.get("admin/ui/dbDuplicateTag/" + response.id).success(function (dataTag) {
                $scope.duplicateLoading = false;
                response["tags"] = dataTag[0];
                if (response.chartType === "horizontalBar") {
                    response.isHorizontalBar = true;
                } else {
                    response.isHorizontalBar = true;
                }
                $scope.widgets.push(response);
//                dialog.init(function () {
//                    setTimeout(function () {
//                        dialog.find('.bootbox-body').html('<center>Duplicate Widget Added Bottom of the Page</center>');
//                    }, 3000);
//                });
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
    $scope.setGaugeFn = function (gaugeFn) {
        $scope.directiveGaugeFn = gaugeFn;
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
    $scope.setCustomDatePickerFn = function (customDateFn) {
        $scope.directiveDateFn = customDateFn;
    };
    $scope.setMapFn = function (mapFn) {
        $scope.directiveMapFn = mapFn;
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
            $http({method: widget.id ? 'PUT' : 'POST', url: 'admin/ui/editWidgetSize/' + widget.id + "?width=" + widget.width}).success(function (response) { });
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
        console.log("********* SELECT X1AXIS ******************");
        console.log(" widgetObj --->", widgetObj);
        console.log("columns -->", column);
        console.log("********* SELECT X1AXIS ******************");
        var value1;
        var value2;
        if (!column) {
            return;
        }
        var exists = false;

        angular.forEach($scope.columnY1Axis, function (val, key) {
            if (val.fieldName === column.fieldName) {
                var index = $scope.columnY1Axis.indexOf(val);
                $scope.columnY1Axis.splice(index, 1);
            }
        });
        angular.forEach($scope.columnY2Axis, function (val, key) {
            if (val.fieldName === column.fieldName) {
                var index = $scope.columnY2Axis.indexOf(val);
                $scope.columnY2Axis.splice(index, 1);
            }
        });
        angular.forEach(widgetObj.columns, function (value, key) {


            if (value.xAxis == 1 && value.yAxis != 1 && value.yAxis != 2) {
                var index = widgetObj.columns.indexOf(value);
                widgetObj.columns.splice(index, 1);
                value.xAxis = "";

                $scope.columnY1Axis.push(value);
                $scope.columnY2Axis.push(value);
            }
            if (!(value.xAxis == 1 && value.yAxis == 1 && value.yAxis == 2)) {
                value = "";
            }
            if (column.fieldName === value.fieldName) {
                exists = true;
                value.xAxis = 1;
                value.yAxis = "";
            } else {
                value.xAxis = null;
            }
        });
        if (exists === false) {
            column.xAxis = 1;
            widgetObj.columns.push(column);
        }
        $scope.xColumn = column;
        widgetObj.columns.filter(function (val) {
            if (val.xAxis == 1) {
                value1 = val;
            }
            if (val.yAxis == 1) {
                value2 = val;
            }
        });
        $scope.saveBtnIsDisable = checkValidationBySaveBtn(widgetObj.chartType, value1, value2);
    };


    $scope.selectLocation = function (widgetObj, column) {
        var value1;
        var value2;
        var value3;
        var exists = false;
        angular.forEach(widgetObj.columns, function (value, key) {
            if (column.fieldName === value.fieldName) {
                exists = true;
                value.isLocation = 1;
            } else {
                value.isLocation = null;
            }
        });
        if (exists === false) {
            column.isLocation = 1;
            widgetObj.columns.push(column);
        }
        widgetObj.columns.filter(function (val) {
            if (val.isLocation == 1) {
                value1 = val;
            }
            if (val.isLatitude == 1) {
                value2 = val;
            }
            if (val.isLongitude == 1) {
                value3 = val;
            }
        });
        $scope.saveBtnIsDisable = checkValidationBySaveBtn(widgetObj.chartType, value1, value2, value3);
    };


    $scope.selectLatitude = function (widgetObj, column) {
        var value1;
        var value2;
        var value3;
        var exists = false;
        angular.forEach(widgetObj.columns, function (value, key) {
            if (column.fieldName === value.fieldName) {
                exists = true;
                value.isLatitude = 1;
            } else {
                value.isLatitude = null;
            }
        });
        if (exists === false) {
            column.isLatitude = 1;
            widgetObj.columns.push(column);
        }
        widgetObj.columns.filter(function (val) {
            if (val.isLocation == 1) {
                value1 = val;
            }
            if (val.isLatitude == 1) {
                value2 = val;
            }
            if (val.isLongitude == 1) {
                value3 = val;
            }
        });
        $scope.saveBtnIsDisable = checkValidationBySaveBtn(widgetObj.chartType, value1, value2, value3);
    };


    $scope.selectLongitude = function (widgetObj, column) {
        var exists = false;
        var value1;
        var value2;
        var value3;
        angular.forEach(widgetObj.columns, function (value, key) {
            if (column.fieldName === value.fieldName) {
                exists = true;
                value.isLongitude = 1;
            } else {
                value.isLongitude = null;
            }

        });
        if (exists === false) {
            column.isLongitude = 1;
            widgetObj.columns.push(column);
        }
        widgetObj.columns.filter(function (val) {
            if (val.isLocation == 1) {
                value1 = val;
            }
            if (val.isLatitude == 1) {
                value2 = val;
            }
            if (val.isLongitude == 1) {
                value3 = val;
            }
        });
        $scope.saveBtnIsDisable = checkValidationBySaveBtn(widgetObj.chartType, value1, value2, value3);
    };

    $scope.y1Column = [];
    $scope.selectY1Axis = function (widget, y1data, chartTypeName) {
        var value1;
        var value2;
        if (chartTypeName === "combination" || widget.chartType === "combination") {
            angular.forEach(y1data, function (value, key) {
                value.combinationType = 'area';
            });
        }
        angular.forEach($scope.columnY2Axis, function (val, key) {
            angular.forEach(y1data, function (value, key) {
                if (val.fieldName === value.fieldName) {
                    var index = $scope.columnY2Axis.indexOf(val);
                    $scope.columnY2Axis.splice(index, 1);
                }
            });
        });
        angular.forEach($scope.columnXAxis, function (val, key) {
            angular.forEach(y1data, function (value, key) {
                if (val.fieldName === value.fieldName) {
                    var index = $scope.columnXAxis.indexOf(val);
                    $scope.columnXAxis.splice(index, 1);
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
            $scope.y1Column = y1data;
        });
        widget.columns.filter(function (val) {
            if (val.xAxis == 1) {
                value1 = val;
            }
            if (val.yAxis == 1) {
                value2 = val;
            }
        });

        console.log("Y1 axis -->", widget.columns);
        console.log("chartType Name -->", chartTypeName);
        $scope.saveBtnIsDisable = checkValidationBySaveBtn(widget.chartType, value1, value2);
    };
    $scope.y2Column = [];
    $scope.selectY2Axis = function (widget, y2data, chartTypeName) {
        var value1;
        var value2;
        if (chartTypeName === "combination" || widget.chartType === "combination") {
            angular.forEach(y2data, function (value, key) {
                value.combinationType = 'line';
            });
        }
        angular.forEach($scope.columnY1Axis, function (val, key) {
            angular.forEach(y2data, function (value, key) {
                if (val.fieldName === value.fieldName) {
                    var index = $scope.columnY1Axis.indexOf(val);
                    $scope.columnY1Axis.splice(index, 1);
                }
            });
        });
        angular.forEach($scope.columnXAxis, function (val, key) {
            angular.forEach(y2data, function (value, key) {
                if (val.fieldName === value.fieldName) {
                    var index = $scope.columnXAxis.indexOf(val);
                    $scope.columnXAxis.splice(index, 1);
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
        $scope.y2Column = y2data;
        widget.columns.filter(function (val) {
            if (val.xAxis == 1) {
                value1 = val;
            }
            if (val.yAxis == 1) {
                value2 = val;
            }
        });
        $scope.saveBtnIsDisable = checkValidationBySaveBtn(widget.chartType, value1, value2);
    };


    $scope.removedByY1Column = function (widgetObj, column, yAxisItems) {
        //        if (yAxisItems.length > 0) {
        var value1, value2;
        $scope.columnY2Axis.push(column);
        var index = $scope.columnY1Axis.indexOf(column);
        var indexX = $scope.columnXAxis.indexOf(column);
        if (index == -1) {
            $scope.columnY1Axis.push(column);
        }
        if (indexX == -1) {
            $scope.columnXAxis.push(column);
        }

        yAxisItems.removeItem = column.fieldName;
        $scope.selectY1Axis(widgetObj, yAxisItems, widgetObj.chartType);
//        } else {
        var getIndex = widgetObj.columns.indexOf(column);
        widgetObj.columns.splice(getIndex, 1);

        widgetObj.columns.filter(function (val) {
            if (val.xAxis == 1) {
                value1 = val;
            }
            if (val.yAxis == 1) {
                value2 = val;
            }
        });
        $scope.saveBtnIsDisable = checkValidationBySaveBtn(widgetObj.chartType, value1, value2);

        //            angular.forEach(widgetObj.columns, function (val, key) {
//                if (val.fieldName == column.fieldName) {
        //                    val.yAxis = null;
//                }
        //            });
//        }
    };
    $scope.removedByY2Column = function (widgetObj, column, yAxisItems) {
        //        if (yAxisItems.length > 0) {
        var value1, value2, value3;
        $scope.columnY1Axis.push(column);
        var index = $scope.columnY2Axis.indexOf(column);
        var indexX = $scope.columnXAxis.indexOf(column);
        if (index == -1) {
            $scope.columnY2Axis.push(column);
        }
        if (indexX == -1) {
            $scope.columnXAxis.push(column);
        }
        yAxisItems.removeItem = column.fieldName;
        $scope.selectY2Axis(widgetObj, yAxisItems, widgetObj.chartType);
//        } else {
        var getIndex = widgetObj.columns.indexOf(column);
        widgetObj.columns.splice(getIndex, 1);

        widgetObj.columns.filter(function (val) {
            if (val.xAxis == 1) {
                value1 = val;
            }
            if (val.yAxis == 1) {
                value2 = val;
            }
            if (val.yAxis == 2) {
                value3 = val;
            }
        });

        $scope.saveBtnIsDisable = checkValidationBySaveBtn(widgetObj.chartType, value1, value2);


        //            angular.forEach(widgetObj.columns, function (val, key) {
//                if (val.fieldName == column.fieldName) {
//                    val.yAxis = null;
//                }
        //            });
//        }
    };

    $scope.ticker = function (widgetObj, column) {

        console.log("ticker widgetobj -->", widgetObj);
        console.log("ticker widgetobj column -->", column);

        var value1;
        //        $scope.dispHideBuilder = true;
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
        value1 = widgetObj.columns;
        $scope.tickerItem = widgetObj.columns;
        if (widgetObj.columns.length > 0) {
            value1 = 1;
        }
        $scope.saveBtnIsDisable = checkValidationBySaveBtn(widgetObj.chartType, value1);
        console.log("ticker widget columns -->", $scope.collectionFields);
    };
    /*
     * 
     * @param {type} widgetObj
     * @param {type} text
     * @returns {undefined}
     */
    $scope.setTextContent = function (text) {
        var value1;
        console.log("****************** TEXT ********************");
        console.log("text -->", text);
        console.log("****************** TEXT ********************");
        if (text !== "") {
            value1 = text;
        }
        $scope.ckContent = text;
        $scope.saveBtnIsDisable = checkValidationBySaveBtn("text", value1);
    };

    $scope.gauge = function (widgetObj, gaugeItem) {

        var value1;
        $scope.gaugeItem = gaugeItem;
        $scope.dispHideBuilder = true;
        var newColumns = [];
        if (!gaugeItem) {
            return;
        }
        if (gaugeItem.length === 0) {
            widgetObj.columns = "";
        } else {
            angular.forEach($scope.collectionFields, function (val, header) {
                if (val.fieldName === gaugeItem.fieldName) {
                    val.displayFormat = gaugeItem.displayFormat;
                    newColumns.push(val);
                }
            });
            widgetObj.columns = newColumns;
            value1 = gaugeItem.fieldName;
        }
        $scope.saveBtnIsDisable = checkValidationBySaveBtn("gauge", value1);
    };

    $scope.removedByTicker = function (widgetObj, column, tickerItem) {
        var value1;
        $scope.ticker(widgetObj, tickerItem);

        var index = $scope.tickerAxis.indexOf(column);
        if (index == -1) {
            $scope.tickerAxis.push(column);
        }

        if (widgetObj.columns.length > 0) {
            widgetObj.columns.filter(function (val) {
                if (val.fieldName !== "") {
                    value1 = val.fieldName;
                }
            });
        }
        $scope.saveBtnIsDisable = checkValidationBySaveBtn("ticker", value1);
    };
    // Funnel Format
    $scope.funnel = function (widget, column) {
        var value1;
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
        value1 = $scope.funnelItem.length;
//        console.log(widget)
        $scope.saveBtnIsDisable = checkValidationBySaveBtn(widget.chartType, value1);
    };
    $scope.removedByFunnel = function (widget, removeItem, funnelItem) {
        var value1;
        var getIndex = widget.columns.indexOf(removeItem);
        widget.columns.splice(getIndex, 1);

        if (widget.columns.length > 0) {
            widget.columns.filter(function (val) {
                if (val.fieldName !== "") {
                    value1 = val.fieldName;
                }
            });
        }
        console.log("widget columns length -->", widget.columns.length)
        console.log("widget columns  -->", widget.columns)
        $scope.saveBtnIsDisable = checkValidationBySaveBtn("funnel", value1);

    };
    $scope.selectPieChartX = function (widget, column) {
        console.log("************ PIE CHART **********");
        var value1 = "";
        var value2 = "";
        $scope.selectPieChartXAxis = column;
        if (!column) {
            return;
        }
        if (widget.columns) {
            var index = widget.columns.findIndex(x => x.xAxis == 1);
            widget.columns.filter(function (val, k) {
                if (index != -1 && val.xAxis == 1) {
                    widget.columns.splice(index, 1);
                }
            });

        }
        var exists = false;
        angular.forEach($scope.columnPieYAxis, function (val, key) {
            if (val.fieldName === column.fieldName) {
                var index = $scope.columnPieYAxis.indexOf(val);
                $scope.columnPieYAxis.splice(index, 1);
            }
        });
        angular.forEach(widget.columns, function (value, key) {
            if (value.xAxis == 1 && value.yAxis != 1 && value.yAxis != 2) {
                value.xAxis = "";
                $scope.columnPieYAxis.push(value);
            }
            if (!(value.xAxis == 1 && value.yAxis == 1 && value.yAxis == 2)) {
                value = "";
            }
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
        widget.columns.filter(function (val) {
            if (val.xAxis == 1) {
                value1 = val;//widget.columns[0]
            }
            if (val.yAxis == 1) {
                value2 = val;
            }
        })
        $scope.saveBtnIsDisable = checkValidationBySaveBtn(widget.chartType, value1, value2);
    };
    $scope.selectPieChartY = function (widget, column) {
        var value1 = "";
        var value2 = "";
        $scope.selectPieChartYAxis = column;
        if (!column) {
            return;
        }
        if (!widget.columns) {
            widget.columns = [];
        }
        if (widget.columns) {
            var index = widget.columns.findIndex(x => x.yAxis == 1);
            if (index != -1) {
                widget.columns.splice(index, 1);
            }
        }

        var exists = false;
        angular.forEach($scope.columnPieXAxis, function (val, key) {
            if (val.fieldName === column.fieldName) {
                var index = $scope.columnPieXAxis.indexOf(val);
                $scope.columnPieXAxis.splice(index, 1);
            }
        });
        angular.forEach(widget.columns, function (value, key) {
            if (value.yAxis == 1 && value.xAxis != 1) {
                value.yAxis = "";
                $scope.columnPieXAxis.push(value);
            }
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
        widget.columns.filter(function (val) {
            if (val.xAxis == 1) {
                value1 = val//widget.columns[0]
            }
            if (val.yAxis == 1) {
                value2 = val
            }
        })
        $scope.saveBtnIsDisable = checkValidationBySaveBtn(widget.chartType, value1, value2);
    };
    function resetQueryBuilder() {
        $scope.dispHideBuilder = false;
    }
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
                widgetId: widgetObj.id,
                derivedColumnId: collectionField.derivedColumnId
            };
            $scope.dataSetColumn = data;
        }
    };
    var deleteColumns = [];
    $scope.removeDerivedColumn = function (obj, widgetObj) {

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
        }
        var index = $scope.collectionFields.indexOf(obj);
        $scope.collectionFields.splice(index, 1);
        if (data) {
            var getIndex = widgetObj.columns.findIndex(x => x.fieldName == data.fieldName);
            if (getIndex != -1) {
                widgetObj.columns.splice(getIndex, 1);
            }
        }
        deleteColumns.push({id: obj.id, fieldName: obj.fieldName});
    };
    function getObject(collection, column, searchData) {
        var object = $filter('filter')(collection, function (field) {
            return field.derivedColumnId === searchData;
        })[0];
        return object;
    }
    function getRandomNumber() {
        return Math.floor(Math.random() * 1000000000);
    }
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
            if (typeof (dataSetColumn.derivedColumnId) === "undefined") {
                dataSetColumnData.derivedColumnId = getRandomNumber();
                $scope.collectionFields.push(dataSetColumnData);
                $scope.columnY1Axis.push(dataSetColumnData);
                $scope.columnY2Axis.push(dataSetColumnData);
            } else {
                var derivedColumnId = dataSetColumn.derivedColumnId;
                dataSetColumnData.derivedColumnId = derivedColumnId;

                $scope.collectionFields.forEach(function (val, key) {
                    if (val.derivedColumnId === derivedColumnId) {
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

                $scope.columnXAxis.forEach(function (val, key) {
                    if (val.derivedColumnId === derivedColumnId) {
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

                $scope.columnPieXAxis.forEach(function (val, key) {
                    if (val.derivedColumnId === derivedColumnId) {
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

                $scope.columnPieYAxis.forEach(function (val, key) {
                    if (val.derivedColumnId === derivedColumnId) {
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
                    if (val.derivedColumnId === derivedColumnId) {
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

                $scope.columnY2Axis.forEach(function (val, key) {
                    if (val.derivedColumnId === derivedColumnId) {
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

                $scope.widgetObj.columns.forEach(function (val, key) {
                    if (val.derivedColumnId === derivedColumnId) {
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
    function clearEditAllWidgetData() {
        $scope.widgetObj.id = "";
        $scope.widgetObj.previewTitle = "";
        $scope.widgetObj.chartType = "";
        $scope.selectedChartType = "";
        $scope.widgetObj.content = "";
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
        $scope.widgetObj.dateRangeName = "";
        $scope.widgetObj.lastNdays = "";
        $scope.widgetObj.lastNweeks = "";
        $scope.widgetObj.lastNmonths = "";
        $scope.widgetObj.lastNyears = "";
        $scope.widgetObj.customStartDate = "";
        $scope.widgetObj.customEndDate = "";
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
        $scope.dispHideBuilder = true;
        $scope.queryBuilderList = "";
        $scope.advanced = false;
    }
    $scope.deleteColumn = function (widget, chartTypeName, index) {
        /*
         * 
         * @type @var;chartTypeName
         * The below two lines are used for edit widget. Chartype will be undefined..So while editing we are checking
         */
        var chartType = chartTypeName ? chartTypeName : widget.chartType;
        chartTypeName = chartType;
        widget.selectAll = 0;
//        console.log("************************ ADVANCED COLUMNS DELETE************");
//        console.log("widget -->", widget);
//        console.log("chartTypeName -->", chartTypeName);
//        console.log("index -->", index);
//        console.log("y1 column -->", $scope.y1Column);


        var value1, value2, value3;

        var widgetObj = widget.columns[index];
        if (widget.chartType === 'table' || chartTypeName === 'table') {
            $scope.collectionFields.forEach(function (val, key) {
                if (val.displayName === widgetObj.displayName) {
                    val.selectColumnDef = 0;
                }
            });
        } else {
            if (widget.chartType === 'pie' || chartTypeName === 'pie') {
                $scope.hideSelectedColumn = true;
                if ($scope.selectPieChartXAxis.displayName === widgetObj.displayName) {
                    $scope.selectPieChartXAxis = "";
                    $scope.columnPieYAxis.push(widgetObj);
                    setTimeout(function () {
                        $scope.hideSelectedColumn = false
                    }, 1000);
                }
                if ($scope.selectPieChartYAxis.displayName === widgetObj.displayName) {
                    $scope.columnPieXAxis.push(widgetObj);
                    $scope.selectPieChartYAxis = "";
                    setTimeout(function () {
                        $scope.hideSelectedColumn = false
                    }, 1000);
                }

            } else if (widget.chartType === 'gauge' || chartTypeName === 'gauge') {
                if ($scope.gaugeItem.displayName === widgetObj.displayName) {
                    $scope.gaugeItem = "";
                }
            } else if (widget.chartType === 'ticker' || chartTypeName === 'ticker') {
                $scope.tickerItem.forEach(function (val, key) {
                    $scope.hideSelectedColumn = true;
                    if (val.displayName === widgetObj.displayName) {
                        /*
                         *  Removed because of issue in create widget
                         */

                        var chartIndex = $scope.tickerItem.indexOf(val);
                        $scope.tickerItem.splice(chartIndex, 1);
                        var index = $scope.tickerAxis.indexOf(widgetObj);
                        if (index == -1) {
                            $scope.tickerAxis.push(widgetObj);
                        }
//                        setTimeout(function () {
//                            $scope.hideSelectedColumn = false;
//                        }, 1000);

                    }
                });
            } else if (widget.chartType === 'funnel' || chartTypeName === 'funnel') {
                $scope.funnelItem.forEach(function (val, key) {
                    $scope.hideSelectedColumn = true
                    if (val.displayName === widgetObj.displayName) {
                        var chartIndex = $scope.funnelItem.indexOf(val);
                    }
                });
            } else {
                widget.columns.forEach(function (value, key) {
                    if (parseInt(value.yAxis) === 1) {
                        $scope.y1Column.forEach(function (val, key) {
                            if (val.displayName === widgetObj.displayName) {
                                var chartIndex = $scope.y1Column.indexOf(val);
                                $scope.y1Column.splice(chartIndex, 1);
                                $scope.columnY2Axis.push(val);
                                $scope.columnXAxis.push(val);
                            }
                        });
                        /*
                         * 
                         * @type type
                         * The below code is use for edit widget
                         * In edit widget if we remove y1axis in advanced menu, its removed from yaxis but not pushing in the list
                         */
                        var index = $scope.columnY1Axis.indexOf(widgetObj);
                        if (index === -1) {
                            $scope.columnY1Axis.push(widgetObj);
                        }

                    } else if (parseInt(value.yAxis) === 2) {
                        $scope.y2Column.forEach(function (val, key) {
                            if (val.displayName === widgetObj.displayName) {
                                var chartIndex = $scope.y2Column.indexOf(val);
                                $scope.y2Column.splice(chartIndex, 1);
                                $scope.columnY1Axis.push(val);
                                $scope.columnXAxis.push(val);
                            }
                        });
                        /*
                         * 
                         * @type type
                         * The below code is use for edit widget
                         * In edit widget if we remove y1axis in advanced menu, its removed from yaxis but not pushing in the list
                         */
                        var index = $scope.columnY2Axis.indexOf(widgetObj);
                        if (index === -1) {
                            $scope.columnY2Axis.push(widgetObj);
                        }
                    } else if (parseInt(value.xAxis) === 1) {
                        if ($scope.xColumn.displayName === widgetObj.displayName) {
                            $scope.hideSelectedColumn = true;
                            $scope.xColumn = "";
                            setTimeout(function () {
                                $scope.hideSelectedColumn = false;
                            }, 1000);
                        }
                    }

                });
            }
        }

        widget.columns.splice(index, 1);
        if (chartTypeName === "table" || chartTypeName === "ticker" || chartTypeName === "funnel") {
            if (widget.columns.length > 0 || widget.selectAll === 1) {
                value1 = widget.columns;
                $scope.saveBtnIsDisable = checkValidationBySaveBtn(chartTypeName, value1);
            } else {
                $scope.saveBtnIsDisable = checkValidationBySaveBtn(chartTypeName, value1);
            }
        } else {
            widget.columns.filter(function (val) {
                if (val.xAxis == 1) {
                    value1 = val;
                }
                if (val.yAxis == 1) {
                    value2 = val;
                }
                if (val.yAxis == 2) {
                    value3 = val;
                }
            });
        }
//        console.log("***************** DELETE COLUMNS ADVANCED MENU ***********************");
//        console.log("Widget Columns -->", widget.columns);
//        console.log("chart Type -->", chartTypeName);
//        console.log("XColumn -->", value1);
//        console.log("y1column -->", value2);
//        console.log("y2column -->", value3);
//        console.log("***************** DELETE COLUMNS ADVANCED MENU ***********************");
        $scope.saveBtnIsDisable = checkValidationBySaveBtn(chartTypeName, value1, value2, value3);
//        console.log("Save Btn Status -->", $scope.saveBtnIsDisable);
    };
    $scope.undo = function (obj, status) {
        var widget = obj//$scope.undoWidget;
        if (status == true) {
            $scope.hideSelectedColumn = true;
            $scope.setWidgetItems(widget);
            setTimeout(function () {
                $scope.hideSelectedColumn = false;
            }, 1000);

        } else {
            var undoWidgetObj = angular.copy(widget)
            $scope.undoWidget = undoWidgetObj;
        }
    }
    $scope.checkValidate = function (title) {
        $scope.widgetObj.previewTitle = title;
        var returnStatus = $scope.checkPreview();
        if (returnStatus === false) {
            $scope.saveBtnIsDisable = false;
        } else {
            $scope.saveBtnIsDisable = true;
        }
    };
    $scope.checkPreview = function () {
        if ($scope.returnStatus === false && typeof $scope.widgetObj.previewTitle !== 'undefined' && $scope.widgetObj.previewTitle !== '' && $scope.widgetObj.previewTitle !== null) {
            return false;
        } else {
            return true;
        }
    };
    function checkValidationBySaveBtn(chartType, value1, value2, value3) {
        var selectedChart = chartType ? chartType : $scope.chartTypeName;
        var returnStatus = false;
//        console.log('chartType', selectedChart);
//        console.log('xColumn', value1);
//        console.log('selectPieChartXAxis', value1);
//        console.log('selectPieChartYAxis', value2);
//        console.log('y1Column', value2);
//        console.log('y2Column', value3);
//        console.log('location', value1);
//        console.log('latitude', value2);
//        console.log('longitude', value3);
//        console.log('gaugeItem', value1);
//        console.log('tickerItem', value1);
//        console.log('funnelItem', value1);
        if ((selectedChart === "area" || selectedChart === "line" || selectedChart === "bar" ||
                selectedChart === "horizontalBar" || selectedChart === "combination" || selectedChart === "stackedbar" ||
                selectedChart === "scatter") && value1) {
            if (!value2 && value3) {
                returnStatus = true;
            } else if (value2 && !value3) {
                returnStatus = false;
            } else if (!value2 && !value3) {
                returnStatus = true;
            }
        } else if (selectedChart === "pie" && (value1 && value2)) {
            returnStatus = false;
        } else if ((selectedChart == 'ticker' || selectedChart === 'funnel' ||
                selectedChart === 'gauge' || selectedChart === 'table' || selectedChart === 'text') && value1) {
            returnStatus = false;
        } else {
            returnStatus = true;
        }
        $scope.returnStatus = returnStatus;
        returnStatus = $scope.checkPreview();
        return returnStatus;
    }


    $scope.save = function (widget) {
        console.log("********** SAVE WIDGET INTITAL *", widget);
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
                alignment: value.alignment,
                agregationFunction: value.agregationFunction,
                expression: value.expression,
                groupPriority: isNaN(value.groupPriority) ? null : value.groupPriority,
                xAxis: isNaN(value.xAxis) ? null : value.xAxis,
                yAxis: isNaN(value.yAxis) ? null : value.yAxis,
                sortOrder: value.sortOrder,
                displayFormat: value.displayFormat,
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
                derivedId: value.derivedId,
                category: value.category,
                isLocation: value.isLocation,
                isLatitude: value.isLatitude,
                isLongitude: value.isLongitude
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
        console.log("All count -->", widget.allAccount);

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
            content: $scope.ckContent,
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
            chartColorOption: widgetColor,
            icon: widget.icon

        };
        console.log("******************* SAVE WIDGET ACCIYBT UD *******", data);
        clearEditAllWidgetData();
        var deleteColumnDef = deleteColumns.map(function (value, key) {
            if (value) {
                return value.fieldName;
            }
        }).join(',');
        if (data.id) {
            $http({method: 'DELETE', url: 'admin/ui/deleteDerivedColumn/' + data.id + '?deleteColumns=' + deleteColumnDef}).success(function (response) {
            });
        }
        deleteColumns = [];
        var postUrl;

        postUrl = 'admin/ui/dbWidget/' + $stateParams.tabId;
        $http({method: data.id ? 'PUT' : 'POST', url: postUrl, data: data}).success(function (response) {
            $scope.saveBtnIsDisable = true;
            var widgetColors;
            var newWidgetResponse = response;
            if ($scope.userChartColors.optionValue) {
                widgetColors = $scope.userChartColors.optionValue.split(',');
            }
            response.chartColors = widgetColors;
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
                        displayName: val.displayName,
                        icon: val.icon
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
                    category: value.category,
                    userId: value.userId, //value.userId,
                    dataSetId: data.dataSetId ? data.dataSetId.id : null
                };
                $scope.derivedColumns.push(columnData);
            });
            var colData = {
                tableColumns: $scope.derivedColumns
            };
            console.log("datasetId----------->", dataSetObj);
            widget.chartType = "";
            $http({method: 'POST', url: 'admin/ui/createWidgetColumn/' + response.id, data: colData}).success(function (response) {
                $scope.chartTypeName = "";
                widget.id = data.id;
                widget.content = data.content;
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
                widget.icon = data.icon;

                widget = data;
                if (widget.chartType === 'horizontalBar') {
                    newWidgetResponse.isHorizontalBar = true;
                } else {
                    newWidgetResponse.isHorizontalBar = false;
                }
                if (!data.id) {
                    $scope.widgets.unshift(newWidgetResponse);
                }
            });
            $('.showEditWidget').modal('hide');
        });
    };
    var tempTargetColors = [];
    $scope.cancel = function (widgetObj) {
        console.log("widgetObj-----------", widgetObj);
        $scope.saveBtnIsDisable = true;
        $scope.dispHideBuilder = true;
        $scope.queryBuilderList = "";
        //        resetQueryBuilder();
        addColor = [];
        deleteColumns = [];
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
            $scope.columnXAxis = [];
            $scope.columnPieXAxis = [];
            $scope.columnPieYAxis = [];
        }


        $scope.chartTypeName = "";
        $scope.xColumn = "";
        $scope.tickerItem = "";
        $scope.funnelItem = "";
        $scope.gaugeItem = "";
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
        $scope.advanced = false;
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
                conversionsColumn.putAggregationValue();
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

app.directive('ckEditor', function () {
    return {
        require: '?ngModel',
        link: function (scope, elm, attr, ngModel) {
            var ck = CKEDITOR.replace(elm[0], {
                removeButtons: 'About'
            });
            if (!ngModel)
                return;
               console.log("ckeditor-------------------->",ngModel);
            ck.on('instanceReady', function () {
                ck.setData(ngModel.$viewValue);
            });
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
            var jsonFilter = JSON.parse(scope.queryData).widgetObj;
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
                filterList.push({id: value.fieldName, label: value.displayName, type: scope.fieldsType});
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
