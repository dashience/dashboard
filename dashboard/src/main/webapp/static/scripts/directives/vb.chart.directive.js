app.directive('chartDirective', function ($http, $filter, $stateParams, orderByFilter, chartFactory, httpService) {
    return {
        restrict: 'E',
        template: '<div ng-show="loadingLine" class="text-center"><img src="static/img/logos/loader.gif" width="40"></div>' +
                '<div ng-show="hideEmptyLine" class="text-center">{{lineEmptyMessage}}</div>',
        scope: {
            setLineChartFn: '&',
            widgetObj: '@',
            defaultChartColor: '@',
            compareDateRange: '@',
            urlType: '@'
        },
        link: function (scope, element, attr) {

        }
    };
});