var app = angular.module("VizBoardApp", ["ui.router", 'ngRoute', "ui.bootstrap", "ui", "ngCookies", "angularScreenfull", "ngDraggable", "angularUtils.directives.dirPagination", 'smart-table', 'angular.filter', 'ngSanitize', 'ui.select', 'ui.sortable', 'LocalStorageModule', 'nsPopover', 'ng-draggable-widgets', 'ngSanitize', 'ngAnimate', 'smartArea', 'pascalprecht.translate']);
//var app = angular.module("VizBoardApp", ["ui.router", "ui.bootstrap", "ui", "ngCookies", "angularScreenfull", "ngDraggable", "angularUtils.directives.dirPagination", 'smart-table', 'angular.filter', 'ngSanitize', 'ui.select', 'ui.grid', 'ui.grid.grouping','ui.sortable', 'ui.grid.edit', 'ui.grid.selection', 'LocalStorageModule','htmlToPdfSave','nsPopover','angular-query-builder']);

app.factory('chartFactory', function () {

    function selectChart(chartType, data) {
        var dataFormat = function (displayValues) {
            console.log("this------------------>",displayValues);
            return displayFormat(data.formatData, displayValues);
        };
        if (chartType == 'line' || chartType == 'area' || chartType == 'combination') {
            return lineChart(data, dataFormat, chartType);
        } else if (chartType == 'pie') {
            return pieChart(data, dataFormat, chartType);
        } else if (chartType == 'gauge') {
            return gaugeChart(data, dataFormat, chartType);
        } else if (chartType == 'funnel') {
            return funnelChart(data, dataFormat, chartType);
        }
        if (chartType == 'bar' || chartType == 'horizontalBar' || chartType == 'stackedbar') {
            if (chartType == 'stackedbar') {
                data.stacked = 'normal';
            }
            return barChart(data, dataFormat, chartType);
        }
    }

    function displayFormat(displayFormats, displayValue) {
        var format;
        angular.forEach(displayFormats, function (value, key) {
            var dispFormat = value.displayFormat;
            if (displayValue.series.name === value.displayName || displayValue.key === value.displayName) {
                if (value.displayFormat) {
                    if (value.displayFormat != 'H:M:S') {
                        if (dispFormat.indexOf("%") > -1) {
                            format = d3.format(dispFormat)(displayValue.y / 100);
                        } else {
                            format = d3.format(dispFormat)(displayValue.y);
                        }
                    } else {
                        format = formatBySecond(parseInt(displayValue.y))
                    }
                } else {
                    format = displayValue.y;
                }
            }
        });
        return format;
    }

    function lineChart(data, dataFormat, chartType) {
        return {
            exporting: {enabled: true},
            chart: {
                renderTo: data.renderTo,
                height: 400,
                margin: 50,
                marginBottom: 50 * 1.75,
                type: chartType == 'combination' ? null : chartType,
                style: {
                    fontFamily: 'Roboto', //'robotoregular',
                }
            },
            credits: {enabled: false},
            title: {text: ''},
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
                title: {enabled: false},
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
                }
            },
            legend: {
                enabled: data.showLegend,
                layout: 'horizontal',
                align: 'center',
                backgroundColor: ((Highcharts.theme && Highcharts.theme.legendBackgroundColor) || '#FFFFFF'),
                shadow: true
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
                },
                area: {
                    dataLabels: {
                        enabled: true,
                        formatter: function () {
                            return dataFormat(this);
                        }
                    }
                },
                column: {
                    stacking: data.stacked,
                    dataLabels: {
                        enabled: true,
                        formatter: function () {
                            return dataFormat(this);
                        }
                    }
                }
            },
            series: data.formattedData
        };

    }
    function barChart(data, dataFormat, chartType) {
        return {
            chart: {
                renderTo: data.renderTo,
                type: chartType == 'bar' || chartType == 'stackedbar' ? 'column' : 'bar'
            },
            title: {
                text: ''
            },
            xAxis: {
                categories: data.xData,
                title: {
                    text: null
                }
            },
            yAxis: {
                min: 0,
                title: {
                    text: '',
                    align: 'high'
                },
                labels: {
                    overflow: 'justify'
                }
            },
            tooltip: {
                valueSuffix: ' millions',
                useHTML: true,
                formatter: function () {
                    var symbol = '●';
                    return this.x + '<br/>' + '<span style="color:' + this.series.color + '">' + symbol + '</span>' + ' ' + this.series.name + ': ' + dataFormat(this);
                },
                style: {
                    zIndex: 100
                }
            },
            plotOptions: {
                bar: {
                    dataLabels: {
                        enabled: true,
                        formatter: function () {
                            return dataFormat(this)
                        }
                    }
                },
                column: {
                    stacking: data.stacked,
                    dataLabels: {
                        enabled: true,
                        formatter: function () {
                            return dataFormat(this);
                        }
                    }
                }
            },
            legend: {
                layout: 'horizontal',
                align: 'center',
                backgroundColor: ((Highcharts.theme && Highcharts.theme.legendBackgroundColor) || '#FFFFFF'),
                shadow: true
            },
            credits: {
                enabled: false
            },
            series: data.formattedData
        };

    }
    function pieChart(data, dataFormat, chartType) {
        return {
            chart: {
                renderTo: data.renderTo,
                plotBackgroundColor: null,
                plotBorderWidth: null,
                plotShadow: false,
                type: 'pie'
            },
            title: {
                text: ''
            },

            plotOptions: {
                pie: {
                    allowPointSelect: true,
                    cursor: 'pointer',
                    dataLabels: {
                        enabled: true,
                        format: '<b>{point.name}</b>: {point.percentage:.1f} %',
                        style: {
                            color: (Highcharts.theme && Highcharts.theme.contrastTextColor) || 'black'
                        }
                    },
                    showInLegend: true
                }
            },
            series: data.formattedData
        };
    }
    function gaugeChart(data, dataFormat, chartType) {
        return{
            chart: {
                renderTo: data.renderTo,
                type: 'solidgauge',
//                backgroundColor: '#fff'
            },
            title: null,
            pane: {
                size: '100%',
                startAngle: -90,
                endAngle: 90,
                background: {
                    backgroundColor: (Highcharts.theme && Highcharts.theme.background2) || '#EEE',
                    innerRadius: '60%',
                    outerRadius: '100%',
                    shape: 'arc'
                }
            },
            tooltip: {
                valueSuffix: ' millions',
                useHTML: true,
                formatter: function () {
                    var symbol = '●';
                    return  '<span style="color:' + this.series.color + '">' + symbol + '</span>' + ' ' + this.series.name + ': ' + dataFormat(this);
                },
                style: {
                    zIndex: 100
                }
            },
            plotOptions: {
                solidgauge: {
                    dataLabels: {
                        enabled: true,
                        formatter: function () {
                            return dataFormat(this)
                        }
                    }
                }
            },
            yAxis: {
                min: 0,
                max: 100,
                stops: [
                    [0.1, '#e74c3c'], // red
                    [0.5, '#f1c40f'], // yellow
                    [0.9, '#2ecc71'] // green
                ],
                minorTickInterval: null,
                tickPixelInterval: 400,
                tickWidth: 0,
                gridLineWidth: 0,
                gridLineColor: 'transparent',
            },

            series: data.formattedData
        }
    }
    function funnelChart(data, dataFormat, chartType) {
        return{
            chart: {
                renderTo: data.renderTo,
                type: 'funnel'
            },
            title: {
                text: ''
            },
            tooltip: {
                valueSuffix: ' millions',
                useHTML: true,
                formatter: function () {
                    var symbol = '●';
                    return  '<span style="color:' + this.series.color + '">' + symbol + '</span>' + ' ' + this.key + ': ' + dataFormat(this);
                },
                style: {
                    zIndex: 100
                }
            },
            plotOptions: {
                series: {
                    dataLabels: {
                        enabled: true,
                        format: '<b>{point.name}</b> ({point.y:,.0f})',
                        color: (Highcharts.theme && Highcharts.theme.contrastTextColor) || 'black',
                        softConnector: true
                    },
                    center: ['40%', '50%'],
                    neckWidth: '30%',
                    neckHeight: '25%',
                    width: '80%'
                }
            },
            legend: {
                enabled: false
            },
            series: [{
                    name: '',
                    data: data.formattedData
                }]
        }
    }
    return{
        selectChart: selectChart
    };
});

//app.service('chartService', function () {
//    this.ennable = function (boolean) {
//        return {enabled: boolean};
//    };
//    this.title = function (title) {
//        return {text: title};
//    };
//});