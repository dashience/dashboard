var app = angular.module("VizBoardApp", ["ui.router", 'ngRoute', "ui.bootstrap", "ui", "ngCookies", "angularScreenfull", "ngDraggable", "angularUtils.directives.dirPagination", 'smart-table', 'angular.filter', 'ngSanitize', 'ui.select', 'ui.sortable', 'LocalStorageModule', 'nsPopover', 'ng-draggable-widgets', 'ngSanitize', 'ngAnimate', 'smartArea', 'pascalprecht.translate']);
//var app = angular.module("VizBoardApp", ["ui.router", "ui.bootstrap", "ui", "ngCookies", "angularScreenfull", "ngDraggable", "angularUtils.directives.dirPagination", 'smart-table', 'angular.filter', 'ngSanitize', 'ui.select', 'ui.grid', 'ui.grid.grouping','ui.sortable', 'ui.grid.edit', 'ui.grid.selection', 'LocalStorageModule','htmlToPdfSave','nsPopover','angular-query-builder']);


app.factory('chartFactory', function (chartService) {

    function selectChart(chartType, data) {
        if (data.isCompare) {
            var dataFormat = function (val) {
                var format;
                angular.forEach(data.compareFormat, function (value, key) {
                    var dispFormat = value.displayFormat;
                    if (val.series.name === value.displayName) {
                        if (value.displayFormat) {
                            if (value.displayFormat && value.displayFormat != 'H:M:S') {
                                if (dispFormat.indexOf("%") > -1) {
                                    format = d3.format(dispFormat)(val.y / 100);
                                } else {
                                    format = d3.format(dispFormat)(val.y);
                                }
                            } else {
                                format = formatBySecond(parseInt(val.y))
                            }                                  
                        } else {
                            format = val.y;
                        }
                    }
                });
                return format;
            }
        } else {
            var dataFormat = function (val) {
                var format;
                angular.forEach(data.displayFormat, function (value, key) {
                    var displayName = value.displayName;
                    var dispFormat = value.displayFormat;
                    if (val.series.name === displayName) {
                        if (value.displayFormat) {
                            if (value.displayFormat && value.displayFormat != 'H:M:S') {
                                if (dispFormat.indexOf("%") > -1) {
                                    format = d3.format(dispFormat)(val.y / 100);
                                } else {
                                    format = d3.format(dispFormat)(val.y);
                                }
                            } else {
                                format = formatBySecond(parseInt(val.y))
                            }
//                                   
                        } else {
                            format = val.y;
                        }

                    }

                });
                return format;
            };
        }

        if (chartType == 'line' || chartType == 'area') {
            return lineChart(data, dataFormat , chartType);
        }
    }
    function lineChart(data, dataFormat , chartType) {
        return {
            exporting: chartService.ennable(true),
            chart: {
                renderTo: data.renderTo,
                height: 400,
                margin: 50,
                marginBottom: 50 * 1.75,
                type: chartType,
                style: {
                    fontFamily: 'Roboto', //'robotoregular',
                }
            },
            credits: chartService.ennable(false),
            title: chartService.title(''),
            xAxis: {
                categories: data.xData,
                tickWidth: 0,
                labels: {
                    rotation: 0,
                    style: {
                        color: '#989797',
                        fontSize: 12
                    }
                }
            },
            yAxis: {
                title: chartService.ennable(false),
                labels: {
                    style: {
                        color: '#989797',
                        fontSize: 12
                    }
                }
            },
            tooltip: {
                useHTML: true,
                formatter: function () {
                    var symbol = '●';
                    return this.x + '<br/>' + '<span style="color:' + this.series.color + '">' + symbol + '</span>' + ' ' + this.series.name + ': ' + dataFormat(this);

                },
            },
            legend: {
                enabled: data.showLegend,
                layout: 'horizontal',
                align: 'center',
                backgroundColor: ((Highcharts.theme && Highcharts.theme.legendBackgroundColor) || '#FFFFFF'),
                shadow: true
//                verticalAlign: 'bottom',
//                itemWidth: 150,
//                x: 50 / 2,
//                y: 0,
//                floating: false,
//                backgroundColor: 'white',
//                symbolPadding: 10,
//                symbolWidth: 0,
//                itemStyle: {
//                    color: '#989797',
//                    fontSize: 12,
//                    fontWeight: 'Regular'
//                }
            },
            plotOptions: {
                series: {
                    marker: {
                        fillColor: 'white',
                        lineWidth: 2,
                        lineColor: null
                    }
                },
                line: {
                    dataLabels: {
                        enabled: true,
                        formatter: function () {
                            return dataFormat(this);
                        }
                    }
                }
            },
            series: data.formattedData
        }

    }
    return{
        selectChart: selectChart
    }
});

app.service('chartService', function () {
    this.ennable = function (boolean) {
        return {enabled: boolean};
    };
    this.title = function (title) {
        return {text: title};
    };
});