app.controller('HeaderController', function ($scope, $cookies, $http, $filter, $stateParams, localStorageService, $state, $location, $rootScope) {
    $scope.permission = localStorageService.get("permission");
    $scope.userName = $cookies.getObject("username");
    $scope.isAdmin = $cookies.getObject("isAdmin");
    $scope.agencyId = $cookies.getObject("agencyId");
    $scope.fullName = $cookies.getObject("fullname");
    $scope.productId = $stateParams.productId;
    $scope.selectTabID = $state;

    $scope.setParamsProduct = function (product) {
        var setTabId = 0;
        var productId = product.id;
        var lastTemplateId;
        var setTemplateId = product.templateId ? product.templateId.id : 0;

        if ($stateParams.productId != product.id) {
            $http.get("admin/template/getProductTemplate/" + productId + "/" + $stateParams.accountId).success(function (response) {
                var responseObj = response;
                // if (responseObj) {
                lastTemplateId = responseObj ? responseObj.id : null;
                $stateParams.productId = product.id;
                $stateParams.templateId = lastTemplateId ? lastTemplateId : setTemplateId;//product.templateId ? product.templateId.id : 0;
                // }
                $state.go("index.dashboard.widget", {
                    accountId: $stateParams.accountId,
                    accountName: $stateParams.accountName,
                    productId: $stateParams.productId,
                    templateId: $stateParams.templateId,
                    tabId: setTabId,
                    startDate: $stateParams.startDate,
                    endDate: $stateParams.endDate
                });
            });
        } else {
            return;
        }
    };

    $scope.setParams = function () {
        $scope.accountId = $stateParams.accountId;
        $scope.accountName = $stateParams.accountName;
        $scope.startDate = $stateParams.startDate;
        $scope.endDate = $stateParams.endDate;
    };
    $scope.product = [];
    $scope.selectAccount = {};

    $http.get('admin/ui/userAccountByUser').success(function (response) {
        if (!response[0]) {
            return;
        }
        $scope.accounts = response;
        $stateParams.accountId = $stateParams.accountId ? $stateParams.accountId : response[0].accountId.id;
        $stateParams.accountName = $stateParams.accountName ? $stateParams.accountName : response[0].accountId.accountName;
        angular.forEach($scope.accounts, function (value, key) {
            if (value.accountId.id == $stateParams.accountId) {
                $scope.name = value;
            }
        });
        $scope.selectAccount.selected = {accountName: $scope.name.accountId.accountName};
        $scope.accountLogo = $scope.name.accountId.logo;
        if (!$scope.name.userId.agencyId) {
            $scope.loadNewUrl()
            return;
        }
        getAgencyProduct($scope.name.userId.agencyId.id);
    });

    $scope.getAccountId = function (account) {
        if ($stateParams.accountId != account.accountId.id) {
            $stateParams.tabId = 0;
            $stateParams.templateId = 0;
        }
        if (account.accountId.logo) {
            $scope.accountLogo = account.accountId.logo;
        } else {
            $scope.accountLogo = "";
        }
        $stateParams.accountId = account.accountId.id;
        $scope.selectAccount.selected = {accountName: account.accountId.accountName};
        $stateParams.accountName = account.accountId.accountName;
        $http.get("admin/template/getProductTemplate/" + $stateParams.productId + "/" + $stateParams.accountId).success(function (response) {
            $stateParams.templateId = response.id;
            $scope.loadNewUrl();
        });
    };

    $scope.toDate = function (strDate) {
        if (!strDate) {
            return new Date();
        }
        var from = strDate.split("/");
        var f = new Date(from[2], from[0] - 1, from[1]);
        return f;
    };

    $scope.getDay = function () {
        var today = new Date();
        var yesterday = new Date(today);
        yesterday.setDate(today.getDate() - 30);
        return yesterday;
    };

    $scope.getBeforeDay = function () {
        var today = new Date();
        var yesterday = new Date(today);
        yesterday.setDate(today.getDate() - 1);
        return yesterday;
    };
//alert($stateParams.endDate)
//return
    $scope.firstDate = $stateParams.startDate ? $scope.toDate(decodeURIComponent($stateParams.startDate)) : $scope.getDay().toLocaleDateString("en-US");
    $scope.lastDate = $stateParams.endDate ? $scope.toDate(decodeURIComponent($stateParams.endDate)) : $scope.getBeforeDay().toLocaleDateString("en-US");
    if (!$stateParams.startDate) {
        $stateParams.startDate = $scope.firstDate;
    }
    if (!$stateParams.endDate) {
        $stateParams.endDate = $scope.lastDate;
    }

    function getAgencyProduct(agencyId) {
        $http.get('admin/user/agencyProduct/' + agencyId).success(function (response) {
            $scope.products = response;
            if (!response) {
                return;
            }
            if (!response[0]) {
                return;
            }
            var getTemplateId = response[0].templateId ? response[0].templateId.id : 0;
            $stateParams.productId = $stateParams.productId ? $stateParams.productId : response[0].id;
            var getProductTemplateId = $stateParams.templateId ? $stateParams.templateId : getTemplateId;
//            var templateId = $stateParams.templateId = $stateParams.templateId ? $stateParams.templateId : getTemplateId;


            $http.get("admin/template/getProductTemplate/" + $stateParams.productId + "/" + $stateParams.accountId).success(function (response) {
                var getLastTemplateId = response;
                var templateId = getLastTemplateId ? getLastTemplateId.id : getProductTemplateId;
                $stateParams.templateId = templateId;
                try {
                    var startDate = moment($('#daterange-btn').data('daterangepicker').startDate).format('MM/DD/YYYY') ? moment($('#daterange-btn').data('daterangepicker').startDate).format('MM/DD/YYYY') : $scope.firstDate;//$scope.startDate.setDate($scope.startDate.getDate() - 1);

                    var endDate = moment($('#daterange-btn').data('daterangepicker').endDate).format('MM/DD/YYYY') ? moment($('#daterange-btn').data('daterangepicker').endDate).format('MM/DD/YYYY') : $scope.lastDate;
                } catch (e) {
                }
               // $stateParams.startDate = startDate;
               //$stateParams.endDate = endDate;
               // alert(startDate)
                //alert(endDate)
                defaultCalls()
            });
        });
    }




    function defaultCalls() {
        if ($scope.getCurrentPage() === "dashboard") {
            $state.go("index.dashboard." + $scope.getCurrentTab(), {
                accountId: $stateParams.accountId,
                accountName: $stateParams.accountName,
                productId: $stateParams.productId,
                templateId: $stateParams.templateId,
                tabId: $stateParams.tabId,
                startDate: $stateParams.startDate ? $stateParams.startDate : $scope.startDate,
                endDate: $stateParams.endDate ? $stateParams.endDate : $scope.endDate
            });
        } else if ($scope.getCurrentPage() === "editWidget") {
            $state.go("index.editWidget", {
                accountId: $stateParams.accountId,
                accountName: $stateParams.accountName,
                tabId: $stateParams.tabId,
                widgetId: $stateParams.widgetId,
                startDate: $stateParams.startDate ? $stateParams.startDate : $scope.startDate,
                endDate: $stateParams.startDate ? $stateParams.endDate : $scope.endDate
            });
        } else if ($scope.getCurrentPage() === "reports") {
            $state.go("index.report.reports", {
                accountId: $stateParams.accountId,
                accountName: $stateParams.accountName,
                productId: $stateParams.productId,
                tabId: $stateParams.tabId,
                startDate: $stateParams.startDate ? $stateParams.startDate : $scope.startDate,
                endDate: $stateParams.endDate ? $stateParams.endDate : $scope.endDate
            });
        } else if ($scope.getCurrentPage() === "newOrEdit") {
            $state.go("index.report.newOrEdit", {
                accountId: $stateParams.accountId,
                accountName: $stateParams.accountName,
                reportId: $stateParams.reportId,
                startDate: $stateParams.startDate ? $stateParams.startDate : $scope.startDate,
                endDate: $stateParams.endDate ? $stateParams.endDate : $scope.endDate
            });
        } else if ($scope.getCurrentPage() === "updateReportWidget") {
            $state.go("index.widgetEditByReport", {
                accountId: $stateParams.accountId,
                accountName: $stateParams.accountName,
                reportId: $stateParams.reportId,
                reportWidgetId: $stateParams.reportWidgetId,
                startDate: $stateParams.startDate ? $stateParams.startDate : $scope.startDate,
                endDate: $stateParams.endDate ? $stateParams.endDate : $scope.endDate
            });
        } else if ($scope.getCurrentPage() === "dataSource") {
            $state.go("index.dataSource", {
                accountId: $stateParams.accountId,
                accountName: $stateParams.accountName,
                startDate: $stateParams.startDate ? $stateParams.startDate : $scope.startDate,
                endDate: $stateParams.endDate ? $stateParams.endDate : $scope.endDate
            });
        } else if ($scope.getCurrentPage() === "dataSet") {
            $state.go("index.dataSet", {
                accountId: $stateParams.accountId,
                accountName: $stateParams.accountName,
                startDate: $stateParams.startDate ? $stateParams.startDate : $scope.startDate,
                endDate: $stateParams.endDate ? $stateParams.endDate : $scope.endDate
            });
        } else if ($scope.getCurrentPage() === "scheduler") {
            $state.go("index.schedulerIndex.scheduler", {
                accountId: $stateParams.accountId,
                accountName: $stateParams.accountName,
                startDate: $stateParams.startDate ? $stateParams.startDate : $scope.startDate,
                endDate: $stateParams.endDate ? $stateParams.endDate : $scope.endDate
            });
        } else if ($scope.getCurrentPage() === "editOrNewScheduler") {
            $state.go("index.schedulerIndex.editOrNewScheduler", {
                accountId: $stateParams.accountId,
                accountName: $stateParams.accountName,
                schedulerId: $stateParams.schedulerId,
                startDate: $stateParams.startDate ? $stateParams.startDate : $scope.startDate,
                endDate: $stateParams.endDate ? $stateParams.endDate : $scope.endDate
            });
        } else if ($scope.getCurrentPage() === "user") {
            $state.go("index.user", {
                accountId: $stateParams.accountId,
                accountName: $stateParams.accountName,
                startDate: $stateParams.startDate ? $stateParams.startDate : $scope.startDate,
                endDate: $stateParams.endDate ? $stateParams.endDate : $scope.endDate
            });
        } else if ($scope.getCurrentPage() === "account") {
            $state.go("index.account", {
                accountId: $stateParams.accountId,
                accountName: $stateParams.accountName,
                startDate: $stateParams.startDate ? $stateParams.startDate : $scope.startDate,
                endDate: $stateParams.endDate ? $stateParams.endDate : $scope.endDate
            });
        } else if ($scope.getCurrentPage() === "agency") {
            $state.go("index.agency", {
                accountId: $stateParams.accountId,
                accountName: $stateParams.accountName,
                startDate: $stateParams.startDate ? $stateParams.startDate : $scope.startDate,
                endDate: $stateParams.endDate ? $stateParams.endDate : $scope.endDate
            });
        } else if ($scope.getCurrentPage() === "fieldSettings") {
            $state.go("index.fieldSettings", {
                accountId: $stateParams.accountId,
                accountName: $stateParams.accountName,
                startDate: $stateParams.startDate ? $stateParams.startDate : $scope.startDate,
                endDate: $stateParams.endDate ? $stateParams.endDate : $scope.endDate
            });
        } else if ($scope.getCurrentPage() === "favourites") {
            $state.go("index.favourites", {
                accountId: $stateParams.accountId,
                accountName: $stateParams.accountName,
                startDate: $stateParams.startDate,
                endDate: $stateParams.endDate
            });
        } else if ($scope.getCurrentPage() === "viewFavouritesWidget") {
            $state.go("index.viewFavouritesWidget", {
                accountId: $stateParams.accountId,
                accountName: $stateParams.accountName,
                productId: $stateParams.productId,
                favouriteName: $stateParams.favouriteName,
                startDate: $stateParams.startDate,
                endDate: $stateParams.endDate
            });
        } else if ($scope.getCurrentPage() === "settings") {
            $state.go("index.settings", {
                startDate: $stateParams.startDate,
                endDate: $stateParams.endDate
            });
        } else {
            $location.path("/" + "?startDate=" + $('#startDate').val() + "&endDate=" + $('#endDate').val());
        }
    }






    $scope.setProductByFav = function () {
        $scope.accountId = $stateParams.accountId;
        $scope.accountName = $stateParams.accountName;
        $scope.productId = $stateParams.productId;
        $scope.startDate = $stateParams.startDate;
        $scope.endDate = $stateParams.endDate;
    };

    $scope.loadNewUrl = function () {
        try {
            var startDate = moment($('#daterange-btn').data('daterangepicker').startDate).format('MM/DD/YYYY') ? moment($('#daterange-btn').data('daterangepicker').startDate).format('MM/DD/YYYY') : $scope.firstDate;//$scope.startDate.setDate($scope.startDate.getDate() - 1);

            var endDate = moment($('#daterange-btn').data('daterangepicker').endDate).format('MM/DD/YYYY') ? moment($('#daterange-btn').data('daterangepicker').endDate).format('MM/DD/YYYY') : $scope.lastDate;
        } catch (e) {
        }
        $stateParams.startDate = startDate;
        $stateParams.endDate = endDate;
        if ($scope.getCurrentPage() === "dashboard") {
            $state.go("index.dashboard." + $scope.getCurrentTab(), {
                accountId: $stateParams.accountId,
                accountName: $stateParams.accountName,
                productId: $stateParams.productId,
                templateId: $stateParams.templateId,
                tabId: $stateParams.tabId,
                startDate: $stateParams.startDate,
                endDate: $stateParams.endDate
            });
        } else if ($scope.getCurrentPage() === "editWidget") {
            $state.go("index.editWidget", {
                accountId: $stateParams.accountId,
                accountName: $stateParams.accountName,
                tabId: $stateParams.tabId,
                widgetId: $stateParams.widgetId,
                startDate: $stateParams.startDate,
                endDate: $stateParams.endDate
            });
        } else if ($scope.getCurrentPage() === "reports") {
            $state.go("index.report.reports", {
                accountId: $stateParams.accountId,
                accountName: $stateParams.accountName,
                productId: $stateParams.productId,
                tabId: $stateParams.tabId,
                startDate: $stateParams.startDate,
                endDate: $stateParams.endDate
            });
        } else if ($scope.getCurrentPage() === "newOrEdit") {
            $state.go("index.report.newOrEdit", {
                accountId: $stateParams.accountId,
                accountName: $stateParams.accountName,
                reportId: $stateParams.reportId,
                startDate: $stateParams.startDate,
                endDate: $stateParams.endDate
            });
        } else if ($scope.getCurrentPage() === "updateReportWidget") {
            $state.go("index.widgetEditByReport", {
                accountId: $stateParams.accountId,
                accountName: $stateParams.accountName,
                reportId: $stateParams.reportId,
                reportWidgetId: $stateParams.reportWidgetId,
                startDate: $stateParams.startDate,
                endDate: $stateParams.endDate
            });
        }
//        else if ($scope.getCurrentPage() === "franchiseMarketing") {
//            $state.go("index.franchiseMarketing", {
//                accountId: $stateParams.accountId,
//                accountName: $stateParams.accountName,
//                productId: $stateParams.productId,
//                startDate: $scope.startDate,
//                endDate: $scope.endDate
//            });
//        }
        else if ($scope.getCurrentPage() === "dataSource") {
            $state.go("index.dataSource", {
                accountId: $stateParams.accountId,
                accountName: $stateParams.accountName,
                startDate: $stateParams.startDate,
                endDate: $stateParams.endDate
            });
        } else if ($scope.getCurrentPage() === "dataSet") {
            $state.go("index.dataSet", {
                accountId: $stateParams.accountId,
                accountName: $stateParams.accountName,
                startDate: $stateParams.startDate,
                endDate: $stateParams.endDate
            });
        } else if ($scope.getCurrentPage() === "scheduler") {
            $state.go("index.schedulerIndex.scheduler", {
                accountId: $stateParams.accountId,
                accountName: $stateParams.accountName,
                startDate: $stateParams.startDate,
                endDate: $stateParams.endDate
            });
        } else if ($scope.getCurrentPage() === "editOrNewScheduler") {
            $state.go("index.schedulerIndex.editOrNewScheduler", {
                accountId: $stateParams.accountId,
                accountName: $stateParams.accountName,
                schedulerId: $stateParams.schedulerId,
                startDate: $stateParams.startDate,
                endDate: $stateParams.endDate
            });
        } else if ($scope.getCurrentPage() === "user") {
            $state.go("index.user", {
                accountId: $stateParams.accountId,
                accountName: $stateParams.accountName,
                startDate: $stateParams.startDate,
                endDate: $stateParams.endDate
            });
        } else if ($scope.getCurrentPage() === "account") {
            $state.go("index.account", {
                accountId: $stateParams.accountId,
                accountName: $stateParams.accountName,
                startDate: $stateParams.startDate,
                endDate: $stateParams.endDate
            });
        } else if ($scope.getCurrentPage() === "agency") {
            $state.go("index.agency", {
                accountId: $stateParams.accountId,
                accountName: $stateParams.accountName,
                startDate: $stateParams.startDate,
                endDate: $stateParams.endDate
            });
        } else if ($scope.getCurrentPage() === "fieldSettings") {
            $state.go("index.fieldSettings", {
                accountId: $stateParams.accountId,
                accountName: $stateParams.accountName,
                startDate: $stateParams.startDate,
                endDate: $stateParams.endDate
            });
        }
//        else if ($scope.getCurrentPage() === "tag") {
//            $state.go("index.tag", {
//                accountId: $stateParams.accountId,
//                accountName: $stateParams.accountName,
//                startDate: $stateParams.startDate,
//                endDate: $stateParams.endDate
//            });
//        }
        else if ($scope.getCurrentPage() === "favourites") {
            $state.go("index.favourites", {
                accountId: $stateParams.accountId,
                accountName: $stateParams.accountName,
                startDate: $stateParams.startDate,
                endDate: $stateParams.endDate
            });
        } else if ($scope.getCurrentPage() === "viewFavouritesWidget") {
            $state.go("index.viewFavouritesWidget", {
                accountId: $stateParams.accountId,
                accountName: $stateParams.accountName,
                productId: $stateParams.productId,
                //favouriteId: $stateParams.favouriteId,
                favouriteName: $stateParams.favouriteName,
                startDate: $stateParams.startDate,
                endDate: $stateParams.endDate
            });
        } else if ($scope.getCurrentPage() === "settings") {
            $state.go("index.settings", {
                startDate: $stateParams.startDate,
                endDate: $stateParams.endDate
            });
        } else {
            $location.path("/" + "?startDate=" + $('#startDate').val() + "&endDate=" + $('#endDate').val());
        }
    };
    $scope.userLogout = function () {
        window.location.href = "login.html"
    }
    $scope.getCurrentPage = function () {
        var url = window.location.href;
        if (url.indexOf("widget") > 0) {
            return "dashboard";
        }
        if (url.indexOf("dashboardTemplate") > 0) {
            return "dashboardTemplate";
        }
        if (url.indexOf("editWidget") > 0) {
            return "editWidget";
        }
        if (url.indexOf("newOrEdit") > 0) {
            return "newOrEdit";
        }
        if (url.indexOf("report") > 0) {
            return "reports";
        }
        if (url.indexOf("updateReportWidget") > 0) {
            return "updateReportWidget";
        }
//        if (url.indexOf("franchiseMarketing") > 0) {
//            return "franchiseMarketing";
//        }
        if (url.indexOf("dataSource") > 0) {
            return "dataSource";
        }
        if (url.indexOf("dataSet") > 0) {
            return "dataSet";
        }
        if (url.indexOf("editOrNewScheduler") > 0) {
            return "editOrNewScheduler";
        }
        if (url.indexOf("scheduler") > 0) {
            return "scheduler";
        }
        if (url.indexOf("user") > 0) {
            return "user";
        }
        if (url.indexOf("account") > 0) {
            return "account";
        }
        if (url.indexOf("agency") > 0) {
            return "agency";
        }
        if (url.indexOf("fieldSettings") > 0) {
            return "fieldSettings";
        }
        if (url.indexOf("favourites") > 0) {
            return "favourites";
        }
        if (url.indexOf("viewFavouritesWidget") > 0) {
            return "viewFavouritesWidget";
        }
        if (url.indexOf("settings") > 0) {
            return "settings";
        }
//        if (url.indexOf("tag") > 0) {
//            return "tag";
//        }
        return "dashboard";
    };
    $scope.getCurrentTab = function () {
        var url = window.location.href;
        if (url.indexOf("widget") > 0) {
            return "widget";
        }
        return "widget";
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
        $('#daterange-btn').daterangepicker(
                {
                    ranges: {
                        'Today': [moment(), moment()],
                        'Yesterday': [moment().subtract(1, 'days'), moment().subtract(1, 'days')],
                        'Last 7 Days': [moment().subtract(7, 'days'), moment().subtract(1, 'days')],
                        'Last 14 Days ': [moment().subtract(14, 'days'), moment().subtract(1, 'days')],
                        'Last 30 Days': [moment().subtract(30, 'days'), moment().subtract(1, 'days')],
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
                        'This Month': [moment().startOf('month'), moment().endOf(new Date())],
                        'Last Month': [moment().subtract(1, 'month').startOf('month'), moment().subtract(1, 'month').endOf('month')]
                    },
                    startDate: $stateParams.startDate ? $stateParams.startDate : moment().subtract(30, 'days'),
                    endDate: $stateParams.endDate ? $stateParams.endDate : moment().subtract(1, 'days'),
                    maxDate: new Date()
                },
                function (startDate, endDate) {
                    $('#daterange-btn span').html(startDate.format('MM-DD-YYYY') + ' - ' + endDate.format('MM-DD-YYYY'));
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

        $(document).on('click', '.table-condensed .month', function () {
            var months = ["Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"];
            var selectedMonth = $(this).text();
            var splitMY = selectedMonth.split(" ");
            var monthvalue = $.inArray(splitMY[0], months);
            var FirstDay = new Date(splitMY[1], monthvalue, 1);
            var LastDay = new Date(splitMY[1], monthvalue + 1, 0);

            $("input[name='daterangepicker_start']").daterangepicker({
                singleDatePicker: false,
                startDate: FirstDay
            });

            $("input[name='daterangepicker_end']").daterangepicker({
                singleDatePicker: false,
                startDate: LastDay
            });

        });
    });
});
