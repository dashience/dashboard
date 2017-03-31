
app.config(function ($stateProvider, $urlRouterProvider) {
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
            });

    $urlRouterProvider.otherwise(function ($injector) {
        $injector.get('$state').go('index.dashboard');
    });
//    $urlRouterProvider.otherwise('index/dashboard/1/1');
});
//
//Array.prototype.move = function (from, to) {
//    this.splice(to, 0, this.splice(from, 1)[0]);
//    return this;
//};
