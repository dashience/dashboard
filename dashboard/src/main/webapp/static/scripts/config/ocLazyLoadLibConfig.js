indexApp.config(function ($ocLazyLoadProvider) {
    $ocLazyLoadProvider.config({
        debug: true,
        serie: true,
        modules: [
            {
                name: 'angular',
                files: [
                    'static/lib/js/angular/angular.min.js',
                    'static/lib/js/angular/angular-sanitize.min.js',
                    'static/lib/js/angular/angular-ui-router.min.js',
                    'static/lib/js/angular/angular-ui.min.js',
                    'static/lib/js/angular/angular-cookie.min.js',
                    'static/lib/js/ocLazyLoad.require.js',
                    'static/lib/js/ocLazyload.js'
                ]
            }, {
                name: 'jquery',
                files: [
                    'static/lib/js/jquery-2.2.3.min.js',
                    'static/lib/plugins/jquery-slimscroll/jquery.slimscroll.js',
                    'static/lib/plugins/node-waves/waves.js',
                    'static/lib/plugins/jquery-countto/jquery.countTo.js',
                    'static/lib/js/template-admin.js',
                    'static/lib/js/template-demo.js'
                ],
                serie: true
            }, {
                name: 'bootstrap',
                files: [
                    'static/lib/plugins/bootstrap/js/bootstrap.js',
                    'static/lib/plugins/bootstrap/css/bootstrap.css',
                    'static/lib/plugins/bootstrap-select/css/bootstrap-select.css',
                    'static/lib/plugins/bootstrap-select/js/bootstrap-select.js'
                ],
                serie: true
            }, {
                name: 'font-awesome',
                files: [
                    'static/lib/css/font-awesome.min.css'
                ],
                serie: true
            }, {
                name: 'custom_js',
                files: [
                    
                    'static/lib/plugins/jquery-slimscroll/jquery.slimscroll.js',
                    'static/lib/plugins/node-waves/waves.js',
                    'static/lib/plugins/jquery-countto/jquery.countTo.js',
                    'static/lib/js/template-admin.js',
                    'static/lib/js/template-demo.js'
                ],
                serie: true
            }, {
                name: 'custom_css',
                files: [
                    'static/lib/plugins/node-waves/waves.css',
                    'static/lib/plugins/animate-css/animate.css',
                    'static/lib/css/raleway.css',
                    'static/lib/css/materialicons.css',
                    'static/lib/css/style.css'
                ]
            }, {
                name: 'app',
                files: [
                    'static/scripts/app.js'
                ],
                serie: true
            }, {
                name: 'ui_routeprovider',
                files: [
                    'static/scripts/config/ui-routeprovider.js'
                ],
                serie: true
            }, {
                name: 'http_service',
                files: [
                    'static/scripts/service/vb.service.js'
                ],
                serie: true
            }, {
                name: 'login_factory',
                files: [
                    'static/scripts/factory/vb.factory.js'
                ],
                serie: true
            }, {
                name: 'lang',
                files: [
                    'static/scripts/config/lang.js'
                ],
                serie: true
            }, {
                name: 'serviceUrl',
                files: [
                    'static/scripts/controller/vb.serviceUrl.js'
                ],
                serie: true
            }]
    });
});

