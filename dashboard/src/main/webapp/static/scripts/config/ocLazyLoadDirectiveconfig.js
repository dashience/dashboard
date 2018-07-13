app.config(function ($ocLazyLoadProvider) {
    $ocLazyLoadProvider.config({
        modules: [
            {
                name: 'areaChart',
                files: ['static/scripts/directives/vb.areaChart.directive.js']
            }, {
                name: 'barChart',
                files: ['static/scripts/directives/vb.barChart.directive.js']
            },{
                name: 'compareTable',
                files: ['static/scripts/directives/vb.compareTable.directive.js']
            },{
                name: 'dataSet',
                files: ['static/scripts/directives/vb.dataSet.directive.js']
            },{
                name: 'derivedDateRange',
                files: ['static/scripts/directives/vb.derivedDateRange.directive.js']
            },{
                name: 'dynamicTable',
                files: ['static/scripts/directives/vb.dynamicTable.directive.js']
            }, {
                name: 'funnelChart',
                files: ['static/scripts/directives/vb.funnelChart.directive.js']
            },{
                name:'gauge',
                files:['static/scripts/directives/vb.gauge.directive.js']
            },{
                name: 'lineChart',
                files: ['static/scripts/directives/vb.lineChart.directive.js']
            },{
                name: 'map',
                files: ['static/scripts/directives/vb.map.directive.js']
            }, {
                name: 'pieChart',
                files: ['static/scripts/directives/vb.pieChart.directive.js']
            }, {
                name: 'scatterChart',
                files: ['static/scripts/directives/vb.scatterChart.directive.js']
            },{
                name: 'sortable',
                files: ['static/scripts/directives/vb.sortable.directive.js']
            }, {
                name: 'stackedBarChart',
                files: ['static/scripts/directives/vb.stackedBarChart.directive.js']
            }, {
                name: 'ticker',
                files: ['static/scripts/directives/vb.ticker.directive.js']
            }
        ]
    });
});

