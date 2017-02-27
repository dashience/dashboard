
app.config(function ($stateProvider, $urlRouterProvider) {
    $stateProvider
            .state("index", {
                url: "/index",
                templateUrl: "static/views/vb.index.html"
            })
            .state("index.dashboard", {
                url: "/dashboard/:productId/:locationId",
                templateUrl: "static/views/dashboard/dashboard.html",
            })
            .state("index.dashboard.widget", {
                url: "/widget/:tabId?:startDate/:endDate",
                templateUrl: "static/views/dashboard/widgets.html",
                controller: 'WidgetController'
            })
            .state("index.editWidget", {
                url: "/editWidget/:locationId/:productId/:tabId/:widgetId?:startDate/:endDate",
                templateUrl: "static/views/dashboard/editWidget.html",
                controller: 'EditWidgetController'
            })
            .state("index.report", {
                url: "/reportIndex/:locationId",
                templateUrl: "static/views/reports/reportIndex.html",
                controller: 'ReportIndexController'
            })
            .state("index.report.template", {
                url: "/template/:locationId/:reportId?:startDate/:endDate",
                templateUrl: "static/views/reports/reportTemplate.html",
                controller: 'TemplateController',
                activetab: 'template'
            })
            .state("index.report.reports", {
                url: "/report/locationId?:startDate/:endDate",
                templateUrl: "static/views/reports/reports.html",
                controller: 'ReportController',
                activetab: 'report'
            })
            .state("index.report.newOrEdit", {
                url: "/newOrEdit/:locationId/:reportId?:startDate/:endDate",
                templateUrl: "static/views/reports/newOrEditReports.html",
                controller: 'NewOrEditReportController',
                activetab: 'report'
            })
            .state("index.dataSource", {
                url: "/dataSource/:locationId?:startDate/:endDate",
                templateUrl: "static/views/source/dataSource.html",
                controller: 'DataSourceController'
            })
            .state("index.dataSet", {
                url: "/dataSet/:locationId?:startDate/:endDate",
                templateUrl: "static/views/source/dataSet.html",
                controller: 'DataSetController'
            })
            .state("index.franchiseMarketing", {
                url: "/franchiseMarketing/:locationId?:startDate/:endDate",
                templateUrl: "static/views/franchiseMarketing/franchiseMarketing.html",
                controller: 'FranchiseMarketingController'
            })
            .state("index.schedulerIndex", {
                url: "/schedulerIndex/:locationId",
                templateUrl: "static/views/scheduler/schedulerIndex.html",
//                controller: 'SchedulerController'
            })
            .state("index.schedulerIndex.scheduler", {
                url: "/scheduler/:locationId?:startDate/:endDate",
                templateUrl: "static/views/scheduler/scheduler.html",
                controller: 'SchedulerController'
            })
            .state("index.user", {
                url: "/user/:locationId?:startDate/:endDate",
                templateUrl: "static/views/admin/user.html",
                controller: 'UserController'
            })
            .state("index.accountIndex", {
                url: "/accountIndex/:locationId?:startDate/:endDate",
                templateUrl: "static/views/admin/accountIndex.html",
                controller: 'AccountIndexController'
            })
            .state("index.accountIndex.account", {
                url: "/account/:locationId?:startDate/:endDate",
                templateUrl: "static/views/admin/account.html",
                controller: 'AccountController',
                 activetab: 'account'
            })
            .state("index.accountIndex.property", {
                url: "/property/:locationId?:startDate/:endDate",
                templateUrl: "static/views/admin/property.html",
                controller: 'PropertyController',
                 activetab: 'property'
            })
            .state("index.accountIndex.permission", {
                url: "/permission/:locationId?:startDate/:endDate",
                templateUrl: "static/views/admin/permission.html",
                controller: 'PermissionController',
                 activetab: 'permission'
            });
//            .state("index.schedulerIndex.editOrNewScheduler", {
//                url: "/editOrNewScheduler/:locationId?:startDate/:endDate",
//                templateUrl: "static/views/scheduler/newOrEditScheduler.html",
//                controller: 'NewOrEditSchedulerController'
//            });

    $urlRouterProvider.otherwise(function ($injector) {
        $injector.get('$state').go('index.dashboard', {productId: 2});
    });
//    $urlRouterProvider.otherwise('index/dashboard/1/1');
});
//
//Array.prototype.move = function (from, to) {
//    this.splice(to, 0, this.splice(from, 1)[0]);
//    return this;
//};
