app.config(function ($ocLazyLoadProvider) {
    $ocLazyLoadProvider.config({
        debug: true,
        serie: true,
        modules: [{
                name: 'headerController',
                files: ['static/scripts/controller/vb.header.js'],
                serie: true
            }, {
                name: 'dashboardController',
                files: ['static/scripts/controller/vb.dashboardTab.js'],
                serie: true
            }, {
                name: 'reportIndex',
                files: [
                    'static/scripts/controller/vb.reportIndex.js'
                ],
                serie: true
            }, {
                name: 'report',
                files: [
                    'static/scripts/controller/vb.reports.js'
                ],
                serie: true
            }]
    });
});

