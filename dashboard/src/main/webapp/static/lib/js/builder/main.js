angular.module('VizBoardApp', [
    'angular-query-builder'
])

//.constant('APP_VERSION', angular_query_builder_version)

.config(['$httpProvider', '$logProvider', 'DemoDataProvider', function ($httpProvider, $logProvider, DemoDataProvider) {

    $logProvider.debugEnabled(true);

    var interceptor = ['$rootScope', '$q', '$location', function ($rootScope, $q, $location) {

        var demoData = DemoDataProvider.$get();

        return {
            'request': function (request) {
                if (request.url.indexOf("https://localhost/aqb/typeahead/object-types/") > -1 ||
                        request.url.indexOf("https://localhost/aqb/typeahead/fma/") > -1) {
                    request.timeout = 1;
                }
                return request;
            },
            'responseError': function (response) {
                if (response.config.url.indexOf("https://localhost/aqb/typeahead/object-types/") > -1) {
                    // Some object types returned by the typeahead
                    response.data = demoData.getObjectTypes();
                    response.status = 200;
                }
                else if (response.config.url.indexOf("https://localhost/aqb/typeahead/fma/") > -1) {
                    // Some FMA terms returned by the typeahead
                    response.data = demoData.getFMATerms();
                    response.status = 200;
                }
                return response;
            }
        };
    }];
    $httpProvider.interceptors.push(interceptor);
}]);

