app.directive('previewTable', function ($http, $filter, $stateParams) {
    return{
        restrict: 'A',
        scope: {
            path: '@',
            getDataSetColumns: '&'
//            dataSetId: '@'
                    // widgetColumns: '@',
                    //setTableFn: '&',
                    // tableFooter:'@'
        },
        template: '<div ng-show="loadingTable" class="text-center" style="color: #228995;"><img src="static/img/logos/loader.gif"></div>' +
                '<div ng-if="ajaxLoadingCompleted">' +
                '<div ng-if="tableRows!=null&&dataSetId!=null" class="pull-right">' +
                '<button class="btn btn-warning btn-xs" title="Delete Derived Columns" ng-click="resetDataSetColumn()">Reset</button>' +
                '<button class="btn btn-success btn-xs" title="Add Derived Column" data-toggle="modal" data-target="#dataSet" ng-click="dataSetFieldsClose(dataSetColumn)"><i class="fa fa-plus"></i></button>' +
                '<div id="dataSet" class="modal" role="dialog">' +
                '<div class="modal-dialog modal-lg">' +
                '<div class="modal-content">' +
                '<div class="modal-header">' +
                '<button type="button" class="close" ng-click="dataSetFieldsClose(dataSetColumn)" data-dismiss="modal">&times;</button>' +
                '<h4 class="modal-title">Derived Column</h4>' +
                '</div>' +
                '<div class="modal-body" style="overflow: visible;">' +
                '<form name="dataSetForm" class="form-horizontal">' +
                '<div class="form-group">' +
                '<label class="col-md-3">Field Name</label>' +
                '<div class="col-md-3">' +
                '<input class="form-control" ng-model="dataSetColumn.fieldName"  ng-change="checkFieldName(dataSetColumn.fieldName)" type="text">' +
                '</div>' +
                '<div class="col-md-3">' +
                '<span ng-show="dataSetError" style="color:red">Field Name Already Exists</span>' +
                '</div>' +
//                '<label class="col-md-2">Base Field</label>' +
//                '<div class="col-md-4">' +
//                '<select class="form-control" ng-model="dataSetColumn.baseField">' +
//                '<option ng-if="!dataSetColumn.functionName && !dataSetColumn.expression" ng-repeat="dataSetColumn in dataSetColumns" value={{dataSetColumn.fieldName}}>' +
//                '{{dataSetColumn.fieldName}}' +
//                '</option>' +
//                '</select>' +
//                '</div>' +

                '</div>' +
//                '<div class="form-group">' +
//                '<label class="col-md-3">Function</label>' +
//                '<div class="col-md-3">' +
//                '<select  name="functionName" class="form-control" ng-model="dataSetColumn.functionName" ng-change="functionChange(dataSetColumn)" ng-disabled="dataSetColumn.expression?true:false">' +
//                '<option ng-repeat="functionType in functionTypes" value={{functionType.name}}>' +
//                '{{functionType.name}}' +
//                '</option>' +
//                '</select>' +
//                '</div>' +
//                '<div ng-if="dataSetColumn.functionName===\'Custom\'" class="col-md-2">' +
//                '<div class="dropdown editWidgetDropDown">' +
//                '<button class="drop btn btn-default dropdown-toggle" type="button" data-toggle="dropdown" id="dateRangeName">' +
//                ' <span ng-class="{\'text-danger\':dateErrorMessage==true}">{{dataSetColumn.dateRangeName?dataSetColumn.dateRangeName:"Select Date"}}</span>' +
//                '<span class="caret"></span></button>' +
//                '<ul class="dropdown-menu scheduler-list-style">' +
//                '<li>' +
//                '<div class="col-md-12">' +
//                '<div>' +
//                '<a class="pull-right custom-daterange-box" function-Date-Range ng-click="selectFunctionDateRange(dataSetColumn)" widget-Table-Date-Range="{{dataSetColumn}}" id="widgetDateRange">' +
//                '<span class="date-border">' +
//                '{{dataSetColumn.customStartDate ? dataSetColumn.customStartDate : startDate| date: "MM/dd/yyyy"}} - {{dataSetColumn.customEndDate ? dataSetColumn.customEndDate : endDate| date: "MM/dd/yyyy"}}' +
//                '</span>' +
//                '</a>' +
//                '</div>' +
//                '</div>' +
//                '</li>' +
////                            text values
//                '<li>' +
//                '<a>Last <input type="text"' +
//                'ng-model="dataSetColumn.lastNdays"' +
//                'ng-change="selectFunctionDuration(\'Last N Days\', dataSetColumn)" ' +
//                'class="form-control"' +
//                'ng-model-options="{debounce: 500}"' +
//                'style="width: 60px; display: contents; height: 25px;"> ' +
//                'Days' +
//                '</a>' +
//                '</li>' +
//                '<li>' +
//                '<a>Last <input type="text"' +
//                'ng-model="dataSetColumn.lastNweeks"' +
//                'ng-change="selectFunctionDuration(\'Last N Weeks\', dataSetColumn)"' +
//                'class="form-control" ' +
//                'ng-model-options="{debounce: 500}"' +
//                'style="width: 60px; display: contents; height: 25px;"> ' +
//                'Weeks' +
//                '</a>' +
//                '</li>' +
//                '<li>' +
//                '<a>Last <input type="text"' +
//                'ng-model="dataSetColumn.lastNmonths"' +
//                'ng-change="selectFunctionDuration(\'Last N Months\', dataSetColumn)"' +
//                'class="form-control"' +
//                'ng-model-options="{debounce: 500}"' +
//                'style="width: 60px; display: contents; height: 25px;"> ' +
//                'Months' +
//                '</a>' +
//                '</li>' +
//                ' <li>' +
//                '<a>Last <input type="text" ' +
//                'ng-model="dataSetColumn.lastNyears"' +
//                'ng-change="selectFunctionDuration(\'Last N Years\', dataSetColumn)"' +
//                'class="form-control"' +
//                'ng-model-options="{debounce: 500}"' +
//                'style="width: 60px; display: contents; height: 25px;"> ' +
//                'Years' +
//                '</a>' +
//                '</li>' +
//                '</ul>' +
//                ' </div>' +
//                '</div>' +
//                '<label class="col-md-1">Column</label>' +
//                '<div class="col-md-2">' +
//                '<select class="form-control" ng-disabled="dataSetColumn.expression?true:false" ng-model="dataSetColumn.columnName">' +
//                '<option ng-if="!dataSetColumn.functionName && !dataSetColumn.expression" ng-repeat="dataSetColumn in dataSetColumns" value={{dataSetColumn.fieldName}}>' +
//                '{{dataSetColumn.fieldName}}' +
//                '</option>' +
//                '</select>' +
//                '</div>' +
//                '<div class="col-md-1">' +
//                '<i class="fa fa-minus-circle" style="cursor:pointer" ng-click="clearFunction(dataSetColumn)"></i>' +
//                '</div>' +
//                '</div>' +





                '<div class="form-group">' +
                '<label class="col-md-3">Expression</label>' +
                '<div class="col-md-8">' +
                '<textarea name="expression" ng-trim="false" spellcheck="false" smart-area="config" ' +
                'class="form-control code expression" ng-model="dataSetColumn.expression" ng-disabled="dataSetColumn.functionName?true:false" rows="5"></textarea>' +
                '</div>' +
                '<div class="col-md-1">' +
                '<i class="fa fa-minus-circle" style="cursor:pointer" ng-click="clearExpression(dataSetColumn)"></i>' +
                '</div>' +
                '</div>' +
                '<div class="form-group">' +
                '<label class="col-md-3">Field Type</label>' +
                '<div class="col-md-3">' +
                '<select class="form-control" ng-model="dataSetColumn.fieldType">' +
                '<option ng-repeat="fieldType in fieldTypes" value="{{fieldType.value}}">' +
                '{{fieldType.name}}' +
                '</option>' +
                '</select>' +
                '</div>' +
                '<label class="col-md-2">Format</label>' +
                '<div class="col-md-4">' +
                '<select class="form-control" ng-model="dataSetColumn.displayFormat">' +
                '<option  ng-repeat="formatType in formats" value="{{formatType.value}}">' +
                '{{formatType.name}}' +
                '</option>' +
                '</select>' +
                '</div>' +
                '</div>' +
                '</form>' +
                '</div>' +
                '<div class="modal-footer">' +
                '<button type="button" class="btn btn-success" data-dismiss="modal" ng-disabled="dataSetError||!((dataSetColumn.expression||(dataSetColumn.functionName&&dataSetColumn.columnName))&&dataSetColumn.fieldName&&dataSetColumn.fieldType)" ng-click="saveDataSetColumn(dataSetColumn)">Save</button>' +
                '<button type="button" class="btn btn-default" data-dismiss="modal" ng-click="dataSetFieldsClose(dataSetColumn)" >Close</button>' +
                '</div>' +
                '</div>' +
                '</div>' +
                '</div>' +
                '</div>' +
                '<table class="table table-responsive table-bordered table-l2t">' +
                '<thead><tr>' +
                '<th class="text-capitalize table-bg" ng-repeat="col in dataSetColumns">' +
                '{{col.fieldName}}' +
                //Edit
                '<div>' +
                '<button ng-if="col.functionName != null|| col.expression != null" type="button" class="btn btn-default btn-xs" data-toggle="modal" data-target="#dataSetColumn{{col.id}}" ng-click="editDataset(col)"><i class="fa fa-pencil"></i></button>' +
                '<div id="dataSetColumn{{col.id}}" class="modal" role="dialog">' +
                '<div class="modal-dialog modal-lg">' +
                '<div class="modal-content">' +
                '<div class="modal-header">' +
                '<button type="button" class="close" ng-click="dataSetFieldsClose(dataSetColumn)" data-dismiss="modal">&times;</button>' +
                '<h4 class="modal-title">Derived Column</h4>' +
                '</div>' +
                '<div class="modal-body" style="overflow: visible;">' +
                '<form name="dataSetForm" class="form-horizontal">' +
                '<div class="form-group">' +
                '<label class="col-md-3">Field Name</label>' +
                '<div class="col-md-3">' +
                '<input class="form-control" ng-model="dataSetColumn.fieldName"  ng-change="checkFieldName(dataSetColumn.fieldName)" type="text">' +
                '</div>' +
                '<div class="col-md-3">' +
                '<span ng-show="dataSetError" style="color:red">Field Name Already Exists</span>' +
                '</div>' +
//                '<label class="col-md-2">Base Field</label>' +
//                '<div class="col-md-4">' +
//                '<select class="form-control" ng-model="dataSetColumn.baseField">' +
//                '<option ng-if="!dataSetColumn.functionName && !dataSetColumn.expression" ng-repeat="dataSetColumn in dataSetColumns" value={{dataSetColumn.fieldName}}>' +
//                '{{dataSetColumn.fieldName}}' +
//                '</option>' +
//                '</select>' +
//                '</div>' +

//                '</div>' +
                '</div>' +
//                '<div class="form-group">' +
//                '<label class="col-md-3">Function</label>' +
//                '<div class="col-md-3">' +
//                '<select  name="functionName" class="form-control" ng-model="dataSetColumn.functionName" ng-change="functionChange(dataSetColumn.functionName)" ng-disabled="dataSetColumn.expression?true:false">' +
//                '<option ng-repeat="functionType in functionTypes" value={{functionType.name}}>' +
//                '{{functionType.name}}' +
//                '</option>' +
//                '</select>' +
//                '</div>' +
//                '<div ng-if="dataSetColumn.functionName===\'Custom\'" class="col-md-2">' +
//                '<div class="dropdown editWidgetDropDown">' +
//                '<button class="drop btn btn-default dropdown-toggle" type="button" data-toggle="dropdown" id="dateRangeName">' +
//                ' <span ng-class="{\'text-danger\':dateErrorMessage==true}">{{dataSetColumn.dateRangeName?dataSetColumn.dateRangeName:"Select Date"}}</span>' +
//                '<span class="caret"></span></button>' +
//                '<ul class="dropdown-menu scheduler-list-style">' +
//                '<li>' +
//                '<div class="col-md-12">' +
//                '<div>' +
//                '<a class="pull-right custom-daterange-box" function-Date-Range ng-click="selectFunctionDateRange(dataSetColumn)" widget-Table-Date-Range="{{dataSetColumn}}" id="widgetDateRange">' +
//                '<span class="date-border">' +
//                '{{dataSetColumn.customStartDate ? dataSetColumn.customStartDate : startDate| date: "MM/dd/yyyy"}} - {{dataSetColumn.customEndDate ? dataSetColumn.customEndDate : endDate| date: "MM/dd/yyyy"}}' +
//                '</span>' +
//                '</a>' +
//                '</div>' +
//                '</div>' +
//                '</li>' +
////                            text values
//                '<li>' +
//                '<a>Last <input type="text"' +
//                'ng-model="dataSetColumn.lastNdays"' +
//                'ng-change="selectFunctionDuration(\'Last N Days\', dataSetColumn)" ' +
//                'class="form-control"' +
//                'ng-model-options="{debounce: 500}"' +
//                'style="width: 60px; display: contents; height: 25px;"> ' +
//                'Days' +
//                '</a>' +
//                '</li>' +
//                '<li>' +
//                '<a>Last <input type="text"' +
//                'ng-model="dataSetColumn.lastNweeks"' +
//                'ng-change="selectFunctionDuration(\'Last N Weeks\', dataSetColumn)"' +
//                'class="form-control" ' +
//                'ng-model-options="{debounce: 500}"' +
//                'style="width: 60px; display: contents; height: 25px;"> ' +
//                'Weeks' +
//                '</a>' +
//                '</li>' +
//                '<li>' +
//                '<a>Last <input type="text"' +
//                'ng-model="dataSetColumn.lastNmonths"' +
//                'ng-change="selectFunctionDuration(\'Last N Months\', dataSetColumn)"' +
//                'class="form-control"' +
//                'ng-model-options="{debounce: 500}"' +
//                'style="width: 60px; display: contents; height: 25px;"> ' +
//                'Months' +
//                '</a>' +
//                '</li>' +
//                ' <li>' +
//                '<a>Last <input type="text" ' +
//                'ng-model="dataSetColumn.lastNyears"' +
//                'ng-change="selectFunctionDuration(\'Last N Years\', dataSetColumn)"' +
//                'class="form-control"' +
//                'ng-model-options="{debounce: 500}"' +
//                'style="width: 60px; display: contents; height: 25px;"> ' +
//                'Years' +
//                '</a>' +
//                '</li>' +
//                '</ul>' +
//                ' </div>' +
//                '</div>' +
//                '<label class="col-md-1">Column</label>' +
//                '<div class="col-md-2">' +
//                '<select class="form-control" ng-disabled="dataSetColumn.expression?true:false" ng-model="dataSetColumn.columnName">' +
//                '<option ng-if="!dataSetColumn.functionName && !dataSetColumn.expression" ng-repeat="dataSetColumn in dataSetColumns" value={{dataSetColumn.fieldName}}>' +
//                '{{dataSetColumn.fieldName}}' +
//                '</option>' +
//                '</select>' +
//                '</div>' +
//                '<div class="col-md-1">' +
//                '<i class="fa fa-minus-circle" style="cursor:pointer" ng-click="clearFunction(dataSetColumn)"></i>' +
//                '</div>' +
//                '</div>' +
                '<div class="form-group">' +
                '<label class="col-md-3">Expression</label>' +
                '<div class="col-md-8">' +
                '<textarea name="expression" ng-trim="false" spellcheck="false" smart-area="config" ' +
                'class="form-control code expression" ng-model="dataSetColumn.expression" ng-disabled="dataSetColumn.functionName?true:false" rows="5"></textarea>' +
                '</div>' +
                '<div class="col-md-1">' +
                '<i class="fa fa-minus-circle" style="cursor:pointer" ng-click="clearExpression(dataSetColumn)"></i>' +
                '</div>' +
                '</div>' +
                '<div class="form-group">' +
                '<label class="col-md-3">Field Type</label>' +
                '<div class="col-md-3">' +
                '<select class="form-control" ng-model="dataSetColumn.fieldType">' +
                '<option ng-repeat="fieldType in fieldTypes" value="{{fieldType.value}}">' +
                '{{fieldType.name}}' +
                '</option>' +
                '</select>' +
                '</div>' +
                '<label class="col-md-2">Format</label>' +
                '<div class="col-md-4">' +
                '<select class="form-control" ng-model="dataSetColumn.displayFormat">' +
                '<option  ng-repeat="formatType in formats" value="{{formatType.value}}">' +
                '{{formatType.name}}' +
                '</option>' +
                '</select>' +
                '</div>' +
                '</div>' +
                '</form>' +
                '</div>' +
                '<div class="modal-footer">' +
                '<button type="button" class="btn btn-success" data-dismiss="modal" ng-disabled="dataSetError || !((dataSetColumn.expression || (dataSetColumn.functionName && dataSetColumn.columnName)) && dataSetColumn.fieldName && dataSetColumn.fieldType)" ng-click="saveDataSetColumn(dataSetColumn)">Save</button>' +
                '<button type="button" class="btn btn-default" data-dismiss="modal" ng-click="dataSetFieldsClose(dataSetColumn)">Close</button>' +
                '</div>' +
                '</div>' +
                '</div>' +
                '</div>' +
                '<button ng-if="col.functionName != null|| col.expression != null" type="button" ng-click=deleteDerivedDataset(col) class="btn btn-default btn-xs"><i class="fa fa-trash"></i></button>' +
                '</div>' +
                '</th>' +
                '</tr></thead>' +
                '<tbody ng-repeat="tableRow in tableRows">' +
                '<tr class="text-capitalize">' +
                '<td ng-repeat="col in dataSetColumns">' +
                '<div>{{format(col, tableRow[col.fieldName])}}</div>' +
                '</td>' +
                '</tbody>' +
                '</table>' +
                '</div>',
        link: function (scope, element, attr) {
            scope.startDate = $stateParams.startDate;
            scope.endDate = $stateParams.endDate;
            scope.fieldTypes = [
                {name: 'None', value: ''},
                {name: 'String', value: 'string'},
                {name: 'Number', value: 'number'},
                {name: 'Date', value: 'date'},
                {name: 'Day', value: 'day'}
            ];
            scope.formats = [
                {name: "None", value: ''},
                {name: "Currency", value: '$,.2f'},
                {name: "Integer", value: ',.0f'},
                {name: "Percentage", value: ',.2%'},
                {name: "Decimal1", value: ',.1f'},
                {name: "Decimal2", value: ',.2f'},
                {name: "Time", value: 'H:M:S'}
            ];
            scope.functionTypes = [
                {name: "YOY", value: 'yoy'},
                {name: "MOM", value: 'mom'},
                {name: 'WOW', value: 'wow'},
                {name: 'Custom', value: 'custom'}
            ];
            scope.loadingTable = true;

            var dataSourcePath = JSON.parse(scope.path)
            var url = "admin/proxy/getData?";
            var dataSourcePassword = '';
            if (dataSourcePath.dataSourceId != null) {
                if (dataSourcePath.dataSourceId.dataSourceType == "sql") {
                    url = "admin/proxy/getJson?url=../dbApi/admin/dataSet/getData&";
                }

                if (dataSourcePath.dataSourceId.password) {
                    dataSourcePassword = dataSourcePath.dataSourceId.password;
                } else {
                    dataSourcePassword = '';
                }
            }
            scope.format = function (column, value) {
                if (column.fieldType === "date") {
                    return value;
                }
                if (!value) {
                    return "-";
                }
                if (column.displayFormat) {
                    if (Number.isNaN(value)) {
                        return "-";
                    }
                    if (column.displayFormat.indexOf("%") > -1) {
                        // return d3.format(column.displayFormat)(value / 100);
                    }
                    return d3.format(column.displayFormat)(value);
                }
                return value;
            };
            var setTimeSegment, setProductSegment;
            if (dataSourcePath.timeSegment) {
                setTimeSegment = dataSourcePath.timeSegment.type;
            } else {
                setTimeSegment = 'none';
            }
            if (dataSourcePath.productSegment) {
                setProductSegment = dataSourcePath.productSegment.type;
            } else {
                setProductSegment = 'none';
            }
            var connectionUrl = null;
            var driver = null;
            var dataSourceType = null;
            var userName = null;
            var dataSourceId = null;
            if (dataSourcePath.dataSourceId != null) {
                connectionUrl = dataSourcePath.dataSourceId.connectionString;
                dataSourceId = dataSourcePath.dataSourceId.id;
                driver = dataSourcePath.dataSourceId.sqlDriver;
                dataSourceType = dataSourcePath.dataSourceId.dataSourceType;
                userName = dataSourcePath.dataSourceId.userName;
            }
            scope.dataSetItems = function () {
                $http.get(url + 'connectionUrl=' + connectionUrl +
                        "&dataSourceId=" + dataSourceId +
                        "&dataSetId=" + dataSourcePath.id +
                        "&joinDataSetId=" + dataSourcePath.joinDataSetId +
                        "&accountId=" + $stateParams.accountId +
                        "&dataSetReportName=" + dataSourcePath.reportName +
                        "&timeSegment=" + setTimeSegment +
                        "&filter=" + dataSourcePath.networkType +
                        "&productSegment=" + setProductSegment +
                        "&driver=" + driver +
                        "&dataSourceType=" + dataSourceType +
                        "&location=" + $stateParams.locationId +
                        "&startDate=" + $stateParams.startDate +
                        "&endDate=" + $stateParams.endDate +
                        '&username=' + userName +
                        '&password=' + dataSourcePassword +
                        '&url=' + dataSourcePath.url +
                        '&port=3306&schema=deeta_dashboard&query=' + encodeURI(dataSourcePath.query)).success(function (response) {
                    scope.dataSetColumns = [];
                    if (dataSourcePath.id == null) {
                        scope.ajaxLoadingCompleted = true;
                        scope.loadingTable = false;
                        scope.dataSetColumns = response.columnDefs;
                        scope.getDataSetColumns({dataSetColumn: scope.dataSetColumns});
                    }
                    scope.tableColumns = response.columnDefs;
                    scope.tableRows = response.data.slice(0, 5);
                    function dayOfWeekAsString(dayIndex) {
                        return ["Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"][dayIndex];
                    }
                    scope.columns = [];
                    scope.dataSetId = dataSourcePath.id;
                    if (dataSourcePath.id != null) {
                        $http.get("admin/ui/getDataSetColumnsByDataSetId/" + dataSourcePath.id).success(function (resp) {
                            scope.ajaxLoadingCompleted = true;
                            scope.loadingTable = false;
                            scope.dataSetColumns = [];
                            if (resp == "" || resp == null) {
                                scope.dataSetColumns = scope.tableColumns;
                            } else {
                                angular.forEach(resp, function (value, key) {
                                    angular.forEach(scope.tableColumns, function (val, key) {
                                        if (value.fieldName == val.fieldName) {
                                            var data = {
                                                id: value.id,
                                                fieldName: value.fieldName,
                                                displayName: value.displayName,
                                                fieldType: value.fieldType,
                                                displayFormat: value.displayFormat,
                                                status: value.status,
                                                expression: value.expression,
                                                functionName: value.functionName,
                                                columnName: value.columnName,
                                                baseField: value.baseField,
                                                dateRangeName: value.dateRangeName,
                                                customStartDate: value.customStartDate,
                                                customEndDate: value.customEndDate,
                                                lastNdays: value.lastNdays,
                                                lastNweeks: value.lastNweeks,
                                                lastNmonths: value.lastNmonths,
                                                lastNyears: value.lastNyears,
                                                userId: value.userId,
                                                widget: value.widgetId
                                            };
                                            scope.dataSetColumns.push(data);
                                        }
                                    });
                                });
                            }
                            scope.expressionLessColumn = [];
                            for (var j = 0; j < scope.dataSetColumns.length; j++) {
                                if (scope.dataSetColumns[j].expression === null && scope.dataSetColumns[j].functionName === null) {
                                    scope.expressionLessColumn.push(scope.dataSetColumns[j]);
                                }
                            }
                            for (var i = 0; i < scope.dataSetColumns.length; i++) {
                                var status = null;
                                var expression = null;
                                var functionName = null;
                                var columnName = null;
                                var baseField = null;
                                var dateRangeName = null;
                                var customStartDate = null;
                                var customEndDate = null;
                                var lastNdays = null;
                                var lastNweeks = null;
                                var lastNmonths = null;
                                var lastNyears = null;
                                if (typeof (scope.dataSetColumns[i].status) !== "undefined") {
                                    status = scope.dataSetColumns[i].status;
                                }
                                if (typeof (scope.dataSetColumns[i].expression) !== "undefined") {
                                    expression = scope.dataSetColumns[i].expression;
                                }
                                if (typeof (scope.dataSetColumns[i].functionName) !== "undefined") {
                                    functionName = scope.dataSetColumns[i].functionName;
                                }
                                if (typeof (scope.dataSetColumns[i].columnName) !== "undefined") {
                                    columnName = scope.dataSetColumns[i].columnName;
                                }
                                if (typeof (scope.dataSetColumns[i].baseField) !== "undefined") {
                                    baseField = scope.dataSetColumns[i].baseField;
                                }
                                if (typeof (scope.dataSetColumns[i].dateRangeName) !== "undefined") {
                                    dateRangeName = scope.dataSetColumns[i].dateRangeName;
                                }
                                if (typeof (scope.dataSetColumns[i].customStartDate) !== "undefined") {
                                    customStartDate = scope.dataSetColumns[i].customStartDate;
                                }
                                if (typeof (scope.dataSetColumns[i].customEndDate) !== "undefined") {
                                    customEndDate = scope.dataSetColumns[i].customEndDate;
                                }
                                if (typeof (scope.dataSetColumns[i].lastNdays) !== "undefined") {
                                    lastNdays = scope.dataSetColumns[i].lastNdays;
                                }
                                if (typeof (scope.dataSetColumns[i].lastNweeks) !== "undefined") {
                                    lastNweeks = scope.dataSetColumns[i].lastNweeks;
                                }
                                if (typeof (scope.dataSetColumns[i].lastNmonths) !== "undefined") {
                                    lastNmonths = scope.dataSetColumns[i].lastNmonths;
                                }
                                if (typeof (scope.dataSetColumns[i].lastNYears) !== "undefined") {
                                    lastNyears = scope.dataSetColumns[i].lastNyears;
                                }
                                var columnData = {
                                    id: scope.dataSetColumns[i].id,
                                    fieldName: scope.dataSetColumns[i].fieldName,
                                    displayName: scope.dataSetColumns[i].displayName,
                                    fieldType: scope.dataSetColumns[i].fieldType,
                                    displayFormat: scope.dataSetColumns[i].displayFormat,
                                    status: status,
                                    expression: expression,
                                    functionName: functionName,
                                    columnName: columnName,
                                    baseField: baseField,
                                    dateRangeName: scope.dataSetColumns[i].dateRangeName,
                                    customStartDate: scope.dataSetColumns[i].customStartDate,
                                    customEndDate: scope.dataSetColumns[i].customEndDate,
                                    lastNdays: scope.dataSetColumns[i].lastNdays,
                                    lastNweeks: scope.dataSetColumns[i].lastNweeks,
                                    lastNmonths: scope.dataSetColumns[i].lastNmonths,
                                    lastNyears: scope.dataSetColumns[i].lastNyears,
                                    userId: scope.dataSetColumns[i].userId,
                                    widgetId: scope.dataSetColumns[i].widgetId
                                };
                                scope.columns.push(columnData);
                            }
                        });
                    }
                    var tableColumnsData = {
                        datasetId: dataSourcePath.id,
                        tableColumns: scope.columns
                    };
                    if (scope.columns.length > 0) {
                        $http({method: 'POST', url: 'admin/ui/dataSetColumns', data: JSON.stringify(tableColumnsData)}).success(function (response) {
                        });
                    }
                });
            };
            scope.dataSetItems();
            scope.resetDataSetColumn = function () {
                var dataSetId = dataSourcePath.id;
                $http({method: 'DELETE', url: 'admin/ui/dataSetColumn/' + dataSetId}).success(function () {
                    scope.dataSetItems();
                });
            };
            scope.dataSetError = false;
            function showDataSetError() {
                scope.dataSetError = true;
            }
            scope.checkFieldName = function (fieldName) {
                for (var i = 0; i < scope.tableColumns.length; i++) {
                    if (fieldName == scope.tableColumns[i].fieldName) {
                        showDataSetError();
                        break;
                    } else {
                        scope.dataSetError = false;
                    }
                }
            };
            scope.functionChange = function (dataSetColumn) {
                scope.dateErrorMessage = false;
                if (dataSetColumn.functionName != "Custom") {
                    dataSetColumn.dateRangeName = "";
                }
            };
            scope.selectFunctionDateRange = function (dataSetColumn) {
                scope.dateErrorMessage = false;
                dataSetColumn.dateRangeName = "Custom";
                dataSetColumn.lastNdays = "";
                dataSetColumn.lastNweeks = "";
                dataSetColumn.lastNmonths = "";
                dataSetColumn.lastNyears = "";
                scope.dateErrorMessage = false;
            };
            scope.clearFunction = function (dataSetColumn) {
                dataSetColumn.columnName = "";
                dataSetColumn.functionName = "";
                dataSetColumn.baseField = "";
            };
            scope.clearExpression = function (dataSetColumn) {
                dataSetColumn.expression = "";
            };
            scope.dataSetFieldsClose = function (dataSetColumn) {
                if (!dataSetColumn) {
                    return;
                }
                scope.dataSetColumn = "";
                dataSetColumn.customStartDate = $stateParams.startDate;
                dataSetColumn.customEndDate = $stateParams.endDate;
                scope.dataSetError = false;
            };
            scope.selectFunctionDuration = function (dateRangeName, dataSetColumn) {

                //scheduler.dateRangeName = dateRangeName;
                if (dateRangeName == 'Last N Days') {
                    if (dataSetColumn.lastNdays) {
                        dataSetColumn.dateRangeName = "Last " + dataSetColumn.lastNdays + " Days";
                    } else {
                        dataSetColumn.dateRangeName = "Last 0 Days";
                    }
                    dataSetColumn.lastNweeks = "";
                    dataSetColumn.lastNmonths = "";
                    dataSetColumn.lastNyears = "";
                } else if (dateRangeName == 'Last N Weeks') {
                    if (dataSetColumn.lastNweeks) {
                        dataSetColumn.dateRangeName = "Last " + dataSetColumn.lastNweeks + " Weeks";
                    } else {
                        dataSetColumn.dateRangeName = "Last 0 Weeks";
                    }
                    dataSetColumn.lastNdays = "";
                    dataSetColumn.lastNmonths = "";
                    dataSetColumn.lastNyears = "";
                } else if (dateRangeName == 'Last N Months') {
                    if (dataSetColumn.lastNmonths) {
                        dataSetColumn.dateRangeName = "Last " + dataSetColumn.lastNmonths + " Months";
                    } else {
                        dataSetColumn.dateRangeName = "Last 0 Months";
                    }
                    dataSetColumn.lastNdays = "";
                    dataSetColumn.lastNweeks = "";
                    dataSetColumn.lastNyears = "";
                } else if (dateRangeName == 'Last N Years') {
                    if (dataSetColumn.lastNyears) {
                        dataSetColumn.dateRangeName = "Last " + dataSetColumn.lastNyears + " Years";
                    } else {
                        dataSetColumn.dateRangeName = "Last 0 Years";
                    }
                    dataSetColumn.lastNdays = "";
                    dataSetColumn.lastNweeks = "";
                    dataSetColumn.lastNmonths = "";
                } else {
                    dataSetColumn.dateRangeName = dateRangeName;
                    dataSetColumn.lastNdays = "";
                    dataSetColumn.lastNweeks = "";
                    dataSetColumn.lastNmonths = "";
                    dataSetColumn.lastNyears = "";
                }
                scope.dateErrorMessage = false;
            };
            scope.saveDataSetColumn = function (dataSetColumn) {
                dataSetColumn.dateRangeName = $("#dateRangeName").text().trim();
                if (dataSetColumn.dateRangeName == "Select Date") {
                    dataSetColumn.dateRangeName = ""
                }

                try {
                    scope.customStartDate = moment($('#widgetDateRange').data('daterangepicker').startDate).format('MM/DD/YYYY') //: $stateParams.startDate; //$scope.startDate.setDate($scope.startDate.getDate() - 1);
                    scope.customEndDate = moment($('#widgetDateRange').data('daterangepicker').endDate).format('MM/DD/YYYY')// : $stateParams.endDate;
                } catch (e) {

                }

                if (dataSetColumn.dateRangeName != "Custom") {
                    scope.customStartDate = "";
                    scope.customEndDate = "";
                }
                scope.dataSetId = dataSourcePath.id;
                var data = {
                    dataSetId: dataSourcePath.id,
                    id: dataSetColumn.id,
                    tableColumns: scope.columns,
                    expression: dataSetColumn.expression,
                    fieldName: dataSetColumn.fieldName,
                    displayName: dataSetColumn.fieldName,
                    fieldType: dataSetColumn.fieldType,
                    baseField: dataSetColumn.baseField,
                    displayFormat: dataSetColumn.displayFormat,
                    functionName: dataSetColumn.functionName,
                    columnName: dataSetColumn.columnName,
                    dateRangeName: dataSetColumn.dateRangeName,
                    customStartDate: scope.customStartDate,
                    customEndDate: scope.customEndDate,
                    lastNdays: dataSetColumn.lastNdays,
                    lastNweeks: dataSetColumn.lastNweeks,
                    lastNmonths: dataSetColumn.lastNmonths,
                    lastNyears: dataSetColumn.lastNyears,
                    userId: dataSetColumn.userId ? dataSetColumn.userId : null,
                    widgetId: dataSetColumn.widgetId ? dataSetColumn.widget : null
                };
//                if (!dataSetColumn.dateRangeName && dataSetColumn.functionName == 'Custom') {
//                    scope.dateErrorMessage = true;
//                } else {
//                    scope.dateErrorMessage = false;
//                    $('.modal').modal('hide');
                $http({method: 'POST', url: 'admin/ui/dataSetFormulaColumns', data: JSON.stringify(data)}).success(function (response) {
                    scope.ajaxLoadingCompleted = false;
                    scope.loadingTable = true;
                    scope.dataSetColumn = "";
                    dataSetColumn.customStartDate = $stateParams.startDate;
                    dataSetColumn.customEndDate = $stateParams.endDate;
                    scope.dataSetItems();
                });
            };
//            };

            scope.editDataset = function (dataSetColumn) {
                if (dataSetColumn.customStartDate == "" && dataSetColumn.customStartDate == null && dataSetColumn.customEndDate == "" && dataSetColumn.customEndDate == null) {
                    dataSetColumn.customStartDate = $stateParams.startDate;
                    dataSetColumn.customEndDate = $stateParams.endDate;
                }
                var editData = {
                    id: dataSetColumn.id,
                    expression: dataSetColumn.expression,
                    fieldName: dataSetColumn.fieldName,
                    fieldType: dataSetColumn.fieldType,
                    displayFormat: dataSetColumn.displayFormat,
                    functionName: dataSetColumn.functionName,
                    columnName: dataSetColumn.columnName,
                    dateRangeName: dataSetColumn.dateRangeName,
                    customStartDate: dataSetColumn.customStartDate,
                    customEndDate: dataSetColumn.customEndDate,
                    baseField: dataSetColumn.baseField,
                    lastNdays: dataSetColumn.lastNdays,
                    lastNyears: dataSetColumn.lastNyears,
                    lastNweeks: dataSetColumn.lastNweeks,
                    lastNmonths: dataSetColumn.lastNmonths,
                    userId: dataSetColumn.userId
                };
                scope.dataSetColumn = editData;
            };
            scope.deleteDerivedDataset = function (dataSetColumn) {
                $http({method: "DELETE", url: 'admin/ui/dataSetFormulaColumns/' + dataSetColumn.id}).success(function (response) {
                    scope.ajaxLoadingCompleted = false;
                    scope.loadingTable = true;
                    scope.dataSetItems();
                });
            };

            scope.config = {
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
                            var listData = scope.tableColumns.filter(function (element) {
                                return element.displayName.substr(0, match[1].length).toLowerCase() === match[1].toLowerCase()
                                        && element.displayName.length > match[1].length;
                            }).map(function (element) {
                                return {
                                    display: element.displayName, // This gets displayed in the dropdown
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
        }
    };
}); /* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


