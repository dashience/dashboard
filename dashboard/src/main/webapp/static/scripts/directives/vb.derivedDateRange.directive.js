app.directive('functionDateRange', function ($stateParams, $timeout) {
    return{
        restrict: 'A',
        scope: {
            widgetTableDateRange: '@',
        },
        link: function (scope, element, attr) {
            $(document).ready(function (e) {
                $(".scheduler-list-style").click(function (e) {
                    e.stopPropagation();
                });
                console.log(scope.widgetTableDateRange)
                var derivedColumn = scope.widgetTableDateRange;
                var columnStartDate = derivedColumn.customStartDate ? derivedColumn.customStartDate : $stateParams.startDate; //JSON.parse(scope.widgetTableDateRange).customStartDate;
                var columnEndDate = derivedColumn.customEndDate ? derivedColumn.customEndDate : $stateParams.endDate;//JSON.parse(scope.widgetTableDateRange).customEndDate;
                //Date range as a button
                $(element[0]).daterangepicker(
                        {
                            ranges: {
                                'Today': [moment(), moment()],
//                                'Yesterday': [moment().subtract(1, 'days'), moment().subtract(1, 'days')],
                                'Last 7 Days': [moment().subtract(7, 'days'), moment().subtract(1, 'days')],
                                'Last 14 Days ': [moment().subtract(14, 'days'), moment().subtract(1, 'days')],
                                'Last 30 Days': [moment().subtract(30, 'days'), moment().subtract(1, 'days')],
//                                'This Week (Sun - Today)': [moment().startOf('week'), moment().endOf(new Date())],
////                        'This Week (Mon - Today)': [moment().startOf('week').add(1, 'days'), moment().endOf(new Date())],
//                                'Last Week (Sun - Sat)': [moment().subtract(1, 'week').startOf('week'), moment().subtract(1, 'week').endOf('week')],
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
                            startDate: columnStartDate ? columnStartDate : moment().subtract(30, 'days'),
                            endDate: columnEndDate ? columnEndDate : moment().subtract(1, 'days'),
                            maxDate: new Date()
                        },
                        function (startDate, endDate) {
                            $('#widgetDateRange span').html(startDate.format('MM-DD-YYYY') + ' - ' + endDate.format('MM-DD-YYYY'));
                        }
                );
                $(".ranges ul").find("li").addClass("custom-picker");
                $(".custom-picker").click(function (e) {
                    $(".scheduler-list-style").hide();
                    scope.$apply();
                });
                $(".editWidgetDropDown").click(function (e) {
                    $(".scheduler-list-style").removeAttr("style");
                    $(".scheduler-list-style").css("display", "block");
                    $(".daterangepicker").css("display", "none");
//                        e.bind();
                });
                $(document).on("click", function (e) {
                    var selectedElement = e.target.className;
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
                    try {
                        scope.customStartDate = moment($('#widgetDateRange').data('daterangepicker').startDate).format('MM/DD/YYYY') ? moment($('#widgetDateRange').data('daterangepicker').startDate).format('MM/DD/YYYY') : widgetStartDate; //$scope.startDate.setDate($scope.startDate.getDate() - 1);
                        scope.customEndDate = moment($('#widgetDateRange').data('daterangepicker').endDate).format('MM/DD/YYYY') ? moment($('#widgetDateRange').data('daterangepicker').endDate).format('MM/DD/YYYY') : widgetEndDate;
                    } catch (e) {
                    }

                    $(".scheduler-list-style").hide(); //                    
                });
            });
        }
    };
});


