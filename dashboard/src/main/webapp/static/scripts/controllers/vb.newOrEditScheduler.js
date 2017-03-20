app.controller("NewOrEditSchedulerController", function ($scope, $http, $stateParams, $filter) {
    $scope.accountId = $stateParams.accountId;
    $scope.accountName = $stateParams.accountName;
    $scope.startDate = $stateParams.startDate;
    $scope.endDate = $stateParams.endDate;
    $scope.schedulerRepeats = ["Now", "Once", "Daily", "Weekly", "Monthly", "Yearly", "Year Of Week"];
    $scope.weeks = ["Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"];

    $http.get("admin/ui/report").success(function (response) {
        $scope.reports = response;
    });
    $http.get('admin/user/account').success(function (response) {
        $scope.accounts = response;
    });
    $scope.scheduler = {};
    $http.get("admin/scheduler/scheduler/" + $stateParams.schedulerId).success(function (response) {
        if (!response) {
            $scope.scheduler = {
                accountId: {id: $stateParams.accountId, accountName: $stateParams.accountName}
            }
            return;
        }
        $scope.schedulers = response;
        $scope.scheduler = {
            id: response.id,
            schedulerName: response.schedulerName,
            startDate: response.startDate,
            endDate: response.endDate,
            schedulerRepeatType: response.schedulerRepeatType,
            schedulerWeekly: response.schedulerWeekly,
            schedulerTime: response.schedulerTime,
            schedulerMonthly: response.schedulerMonthly,
            schedulerYearly: response.schedulerYearly,
            schedulerYearOfWeek: response.schedulerYearOfWeek,
            reportId: response.reportId,
            schedulerType: response.schedulerType,
            schedulerEmail: response.schedulerEmail.split(','),
            dateRangeName: response.dateRangeName,
            accountId: response.accountId,
            lastNdays: response.lastNdays,
            lastNweeks: response.lastNweeks,
            lastNmonths: response.lastNmonths,
            lastNyears: response.lastNyears,
            customStartDate: response.customStartDate,
            customEndDate: response.customEndDate
        };
    });

    $scope.addNewScheduler = function () {
        $scope.scheduler = "";
    };

    function getWeeks(d) {
        var first = new Date(d.getFullYear(), 0, 1);
        var dayms = 1000 * 60 * 60 * 24;
        var numday = ((d - first) / dayms);
        var weeks = Math.ceil((numday + first.getDay() + 1) / 7);
        return weeks;
    }
    function getLastDayOfYear(datex)
    {
        var year = datex.getFullYear();
        var month = 12;
        var day = 31;
        return month + "/" + day + "/" + year;
    }
    var endOfYearDate = getLastDayOfYear(new Date());
    var endOfYear = getWeeks(new Date(endOfYearDate))
    $scope.totalYearOfWeeks = [];
    for (var i = 1; i <= endOfYear; i++) {
        $scope.totalYearOfWeeks.push(i);
    }

    $scope.selectDuration = function (dateRangeName, scheduler) {
        //scheduler.dateRangeName = dateRangeName;
        console.log(dateRangeName)
        if (dateRangeName == 'Last N Days') {
            if (scheduler.lastNdays) {
                $scope.scheduler.dateRangeName = "Last " + scheduler.lastNdays + " Days";
            } else {
                $scope.scheduler.dateRangeName = "Last 0 Days";
            }
            $scope.scheduler.lastNweeks = "";
            $scope.scheduler.lastNmonths = "";
            $scope.scheduler.lastNyears = "";
        } else if (dateRangeName == 'Last N Weeks') {
            if (scheduler.lastNweeks) {
                $scope.scheduler.dateRangeName = "Last " + scheduler.lastNweeks + " Weeks";
            } else {
                $scope.scheduler.dateRangeName = "Last 0 Weeks";
            }
            $scope.scheduler.lastNdays = "";
            $scope.scheduler.lastNmonths = "";
            $scope.scheduler.lastNyears = "";
        } else if (dateRangeName == 'Last N Months') {
            if (scheduler.lastNmonths) {
                $scope.scheduler.dateRangeName = "Last " + scheduler.lastNmonths + " Months";
            } else {
                $scope.scheduler.dateRangeName = "Last 0 Months";
            }
            $scope.scheduler.lastNdays = "";
            $scope.scheduler.lastNweeks = "";
            $scope.scheduler.lastNyears = "";
        } else if (dateRangeName == 'Last N Years') {
            if (scheduler.lastNyears) {
                $scope.scheduler.dateRangeName = "Last " + scheduler.lastNyears + " Years";
            } else {
                $scope.scheduler.dateRangeName = "Last 0 Years";
            }
            $scope.scheduler.lastNdays = "";
            $scope.scheduler.lastNweeks = "";
            $scope.scheduler.lastNmonths = "";
        } else {
            $scope.scheduler.dateRangeName = dateRangeName;
            $scope.scheduler.lastNdays = "";
            $scope.scheduler.lastNweeks = "";
            $scope.scheduler.lastNmonths = "";
            $scope.scheduler.lastNyears = "";
        }
    }
    $scope.saveScheduler = function (scheduler) {
        try {
            $scope.customStartDate = moment($('#customDateRange').data('daterangepicker').startDate).format('MM/DD/YYYY') ? moment($('#customDateRange').data('daterangepicker').startDate).format('MM/DD/YYYY') : $stateParams.startDate;//$scope.startDate.setDate($scope.startDate.getDate() - 1);

            $scope.customEndDate = moment($('#customDateRange').data('daterangepicker').endDate).format('MM/DD/YYYY') ? moment($('#customDateRange').data('daterangepicker').endDate).format('MM/DD/YYYY') : $stateParams.endDate;
        } catch (e) {

        }
        if (scheduler.schedulerRepeatType === "Now") {
            scheduler.schedulerNow = new Date();
        }

        var emails = scheduler.schedulerEmail.map(function (value, key) {
            if (value) {
                return value;
            }
        }).join(',');

        scheduler.customStartDate = $scope.customStartDate;
        scheduler.customEndDate = $scope.customEndDate;
        scheduler.schedulerEmail = emails;
        console.log($scope.customStartDate);
        console.log($scope.customEndDate);
        console.log(scheduler)
        $http({method: scheduler.id ? 'PUT' : 'POST', url: 'admin/scheduler/scheduler', data: scheduler}).success(function (response) {
        });
        $scope.scheduler = "";
//        var today = new Date();
//        today.toLocaleFormat('%d-%b-%Y');
    };







    $(function () {
        //Initialize Select2 Elements
        $(".select2").select2();
        //Datemask dd/mm/yyyy
        $("#datemask").inputmask("dd/mm/yyyy", {"placeholder": "dd/mm/yyyy"});
        //Datemask2 mm/dd/yyyy
        $("#datemask2").inputmask("mm/dd/yyyy", {"placeholder": "mm/dd/yyyy"});
        //Money Euro
        $("[data-mask]").inputmask();
        //Date range picker
        $('#reservation').daterangepicker();
        //Date range picker with time picker
        $('#reservationtime').daterangepicker({timePicker: true, timePickerIncrement: 30, format: 'MM/DD/YYYY h:mm A'});
        //Date range as a button
        $('#customDateRange').daterangepicker(
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
                    maxDate: new Date()
                },
                function (start, end) {
                    $('#customDateRange span').html(start.format('MM-DD-YYYY') + ' - ' + end.format('MM-DD-YYYY'));
                }
        );
        //Date picker
        $('#datepicker').datepicker({
            autoclose: true
        });
        //iCheck for checkbox and radio inputs
        $('input[type="checkbox"].minimal,  input[type="radio"].minimal').iCheck({
            checkboxClass: 'icheckbox_minimal-blue',
            radioClass: 'iradio_minimal-blue'
        });
        //Red color scheme for iCheck
        $('input[type="checkbox"].minimal-red, input[type="radio"].minimal-red').iCheck({
            checkboxClass: 'icheckbox_minimal-red',
            radioClass: 'iradio_minimal-red'
        });
        //Flat red color scheme for iCheck
        $('input[type="checkbox"].flat-red, input[type="radio"].flat-red').iCheck({
            checkboxClass: 'icheckbox_flat-green',
            radioClass: 'iradio_flat-green'
        });
        //Colorpicker
        $(".my-colorpicker1").colorpicker();
        //color picker with addon
        $(".my-colorpicker2").colorpicker();
        //Timepicker
        $(".timepicker").timepicker({
            showInputs: false
        });

        //$("#config-demo").click(function (e) {       

    });
});
app.directive('jqdatepicker', function () {
    return {
        restrict: 'A',
        require: 'ngModel',
        link: function (scope, element, attrs, ngModelCtrl, ctrl) {
            $(element).datetimepicker({
                format: 'm/d/Y',
                timepicker: false,
                onSelect: function (date) {
                    ctrl.$setViewValue(date);
                    ctrl.$render();
                    scope.$apply();
                }
            });
        }
    };
});
app.directive('jqdatepickerTime', function () {
    return {
        restrict: 'A',
        require: 'ngModel',
        link: function (scope, element, attrs, ngModelCtrl, ctrl) {
            $(element).datetimepicker({
                //format: 'm/d/Y',
                datepicker: false,
                format: 'H:i',
//                step: 5,
                onSelect: function (date) {
                    ctrl.$setViewValue(date);
                    ctrl.$render();
                    scope.$apply();
                }
            });
        }
    };
});
app.directive('jqdatetimepicker', function () {
    return {
        restrict: 'A',
        require: 'ngModel',
        link: function (scope, element, attrs, ngModelCtrl, ctrl) {
            $(element).datetimepicker({
                format: 'm/d/y H:i',
//                step: 5,
                onSelect: function (date) {
                    ctrl.$setViewValue(date);
                    ctrl.$render();
                    scope.$apply();
                }
            });
        }
    };
});