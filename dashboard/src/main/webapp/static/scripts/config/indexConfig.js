indexApp.config(function ($stateProvider, $ocLazyLoadProvider, $urlRouterProvider) {
    $stateProvider
            .state("main-index", {
                resolve: {
                    
                    loadBaseLib: ['$ocLazyLoad', function ($ocLazyLoad) {
                            console.log("Loader")
                            // you can lazy load files for an existing module
                            return $ocLazyLoad.load(['jquery','font-awesome',
                                'custom_css', 'bootstrap', 
                                'app','lang','serviceUrl','login_factory','ui_routeprovider'
                            ]);
                        }],
                     serviceData: ['$ocLazyLoad', '$injector', function ($ocLazyLoad, $injector) {
                            return $ocLazyLoad.load('http_service')
                        }]
                    
                }

            });

    $urlRouterProvider.otherwise(function ($injector) {
        $injector.get('$state').go('main-index');
    });


});

// indexApp.run(function($ocLazyLoad) {
//        $ocLazyLoad.load( 'ui_routeprovider' );
//    });