
app.config(function ($stateProvider, $urlRouterProvider, $routeProvider) {
    $stateProvider
            .state("index", {
                url: "/index",
                templateUrl: "static/views/vb.index.html",
                //controller: "IndexController"
            })
            .state("index.dashboard", {
                url: "/dashboard/:accountId/:accountName/:productId",
                templateUrl: "static/views/dashboard/dashboard.html",
            })
            .state("index.dashboard.widget", {
                url: "/widget/:tabId?:startDate/:endDate",
                templateUrl: "static/views/dashboard/widgets.html",
                controller: 'WidgetController'
            })
            .state("index.editWidget", {
                url: "/editWidget/:accountId/:accountName/:productId/:tabId/:widgetId?:startDate/:endDate",
                templateUrl: "static/views/dashboard/editWidget.html",
                controller: 'EditWidgetController'
            })
            .state("index.report", {
                url: "/reportIndex/:accountId/:accountName",
                templateUrl: "static/views/reports/reportIndex.html",
                controller: 'ReportIndexController'
            })
            .state("index.report.reports", {
                url: "/report?:startDate/:endDate",
                templateUrl: "static/views/reports/reports.html",
                controller: 'ReportController',
                activetab: 'report'
            })
            .state("index.report.newOrEdit", {
                url: "/newOrEdit/:reportId?:startDate/:endDate",
                templateUrl: "static/views/reports/newOrEditReports.html",
                controller: 'NewOrEditReportController',
                activetab: 'report'
            })
            .state("index.widgetEditByReport", {
                url: "/updateReportWidget/:accountId/:accountName/:reportId/:reportWidgetId?:startDate/:endDate",
                templateUrl: "static/views/reports/editReportWidget.html",
                controller: 'WidgetEditReportController'
            })
//            .state("index.report.template", {
//                url: "/template/:accountId/:accountName/:reportId?:startDate/:endDate",
//                templateUrl: "static/views/reports/reportTemplate.html",
//                controller: 'TemplateController',
//                activetab: 'template'
//            })
            .state("index.dataSource", {
                url: "/dataSource/:accountId/:accountName?:startDate/:endDate",
                templateUrl: "static/views/source/dataSource.html",
                controller: 'DataSourceController'
            })
            .state("index.dataSet", {
                url: "/dataSet/:accountId/:accountName?:startDate/:endDate",
                templateUrl: "static/views/source/dataSet.html",
                controller: 'DataSetController'
            })
//            .state("index.franchiseMarketing", {
//                url: "/franchiseMarketing/:locationId?:startDate/:endDate",
//                templateUrl: "static/views/franchiseMarketing/franchiseMarketing.html",
//                controller: 'FranchiseMarketingController'
//            })
            .state("index.schedulerIndex", {
                url: "/schedulerIndex/:accountId/:accountName",
                templateUrl: "static/views/scheduler/schedulerIndex.html",
//                controller: 'SchedulerController'
            })
            .state("index.schedulerIndex.scheduler", {
                url: "/scheduler?:startDate/:endDate",
                templateUrl: "static/views/scheduler/scheduler.html",
                controller: 'SchedulerController'
            })
            .state("index.schedulerIndex.editOrNewScheduler", {
                url: "/editOrNewScheduler/:schedulerId?:startDate/:endDate",
                templateUrl: "static/views/scheduler/newOrEditScheduler.html",
                controller: 'NewOrEditSchedulerController'
            })
            .state("index.user", {
                url: "/user/:accountId/:accountName?:startDate/:endDate",
                templateUrl: "static/views/admin/user.html",
                controller: 'UserController'
            })
            .state("index.account", {
                url: "/account/:accountId/:accountName?:startDate/:endDate",
                templateUrl: "static/views/admin/account.html",
                controller: 'AccountController'
            })
            .state("index.agency", {
                url: "/agency/:accountId/:accountName?:startDate/:endDate",
                templateUrl: "static/views/admin/agency.html",
                controller: 'AgencyController'
            })
            .state("index.fieldSettings", {
                url: "/fieldSettings/:accountId/:accountName?:startDate/:endDate",
                templateUrl: "static/views/fieldSettings/fieldSettings.html",
                controller: 'FieldSettingsController'
            })
//            .state("index.favourites", {
//                url: "/favourites/:accountId/:accountName?:startDate/:endDate",
//                templateUrl: "static/views/admin/favourites.html",
//                controller: 'FavouritesController'
//            })
            .state("index.viewFavouritesWidget", {
                url: "/viewFavouritesWidget/:accountId/:accountName/:productId/:favouriteName?:startDate/:endDate",
                templateUrl: "static/views/admin/viewFavouritesWidget.html",
                controller: 'ViewFavouritesWidgetController'
            })
            .state("index.settings", {
                url: "/settings?:startDate/:endDate",
                templateUrl: "static/views/admin/settings.html",
                controller: 'SettingsController'
            })
            .state("viewPdf", {
                url: "/viewPdf/:accountId/:accountName/:productId/:productName/:tabId?:startDate/:endDate",
                templateUrl: "static/views/pdf/vb.pdf.html",
                controller:'PdfController'
            })
            .state("viewReportPdf", {
                url: "/viewReportPdf/:accountId/:reportId?:startDate/:endDate",
                templateUrl: "static/views/pdf/vb.reportPdf.html",
                controller:'ReportPdfController'
            })
            .state("viewFavouritesPdf", {
                url: "/viewFavouritesPdf/:accountId/:favouriteName?:startDate/:endDate",
                templateUrl: "static/views/pdf/vb.favouritesPdf.html",
                controller:'FavouritesPdfController'
            });
//            .state("index.viewFavouritesWidget", {
//                url: "/viewFavouritesWidget/:accountId/:accountName/:favouriteId/:favouriteName?:startDate/:endDate",
//                templateUrl: "static/views/admin/viewFavouritesWidget.html",
//                controller: 'ViewFavouritesWidgetController'
//            });


    $urlRouterProvider.otherwise(function ($injector) {
        $injector.get('$state').go('index.dashboard');
    });


//    $routeProvider.when('/viewPdf', {
//        url: '/viewPdf/:accountId/:accountName/:tabId',
//        templateUrl: 'static/views/pdf/vb.pdf.html'});
//    $urlRouterProvider.otherwise('index/dashboard/1/1');
});
app.run(['$window', '$rootScope', '$stateParams',
    function ($window, $rootScope, $stateParams) {
        console.log($stateParams)
        //$rootScope.accountNameByPdf = $stateParams.accountName; 

        $rootScope.goBack = function () {
            $window.history.back();
        }
    }])