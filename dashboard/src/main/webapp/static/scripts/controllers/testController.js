app.controller('TestController', function ($scope, $http) {

//    var chart = c3.generate({
//        bindto: '#chart',
//        data: {
//            columns: [
//                ['data1', 30, 200, 100, 400, 150, 250],
//                ['data2', 50, 20, 10, 40, 15, 25]
//            ],
//            axes: {
//                data2: 'y2' // ADD
//            }
//        },
//        axis: {
//            y2: {
//                show: true // ADD
//            }
//        }
//    });

//    d3.json("../../../static/datas/sample.json", function (data) {
//        var modData = [];
//        console.log(data);
//        data.results.forEach(function (d, i) {
//            var item = ["param-" + d.param];
//            d.val.forEach(function (j) {
//                item.push(j);
//            });
//            modData.push(item);
//        });
//        var chart = c3.generate({
//            data: {
//                columns: modData
//            }
//        });
//    });

    d3.json("../../../static/datas/sample.json", function (data) {
        var modData = [];
        console.log(data);
        data.results.forEach(function (d, i) {
            var item = ["param-" + d.param];
            d.val.forEach(function (j) {
                item.push(j);
            });
            modData.push(item);
        });
        var chart = c3.generate({
            bindto: '#barChart',
            data: {
                columns: modData,
                type: 'bar'
            }
        });
    });



    var margin = {
        top: 20,
        right: 20,
        bottom: 30,
        left: 40
    },
            width = 960 - margin.left - margin.right,
            height = 500 - margin.top - margin.bottom;

    var x = d3.scale.ordinal()
            .rangeRoundBands([0, width], .1);

    var y = d3.scale.linear()
            .range([height, 0]);

    var xAxis = d3.svg.axis()
            .scale(x)
            .orient("bottom");

    var yAxis = d3.svg.axis()
            .scale(y)
            .orient("left")
            .ticks(10, "");

    var svg = d3.select("#chart").append("svg")
            .attr("width", width + margin.left + margin.right)
            .attr("height", height + margin.top + margin.bottom)
            .append("g")
            .attr("transform", "translate(" + margin.left + "," + margin.top + ")");

    var data = [{
            "id": "2",
            "name": "wombat",
            "total": "98000",
            "record_date": "2016-01-21 00:00:00"
        }, {
            "id": "5",
            "name": "wombat",
            "total": "96000",
            "record_date": "2016-02-21 00:00:00"
        }, {
            "id": "8",
            "name": "wombat",
            "total": "93000",
            "record_date": "2016-03-21 00:00:00"
        }, {
            "id": "11",
            "name": "wombat",
            "total": "91000",
            "record_date": "2016-04-21 00:00:00"
        }, {
            "id": "14",
            "name": "wombat",
            "total": "92000",
            "record_date": "2016-05-21 00:00:00"
        }, {
            "id": "17",
            "name": "wombat",
            "total": "83000",
            "record_date": "2016-06-21 00:00:00"
        }, {
            "id": "20",
            "name": "wombat",
            "total": "81000",
            "record_date": "2016-07-21 00:00:00"
        }];

    var k = [];
    data.forEach(function (d) {
        d.record_date = d.record_date;
        d.total = +d.total;
        k.push(d.record_date)
    });

    x.domain(data.map(function (d) {
        return d.record_date;
    }));
    y.domain([0, d3.max(data, function (d) {
            return d.total;
        })]);

    svg.append("g")
            .attr("class", "x axis")
            .attr("transform", "translate(0," + height + ")")
            .call(xAxis);

    svg.append("g")
            .attr("class", "y axis")
            .call(yAxis)
            .append("text")
            .attr("transform", "rotate(-90)")
            .attr("y", 6)
            .attr("dy", ".71em")
            .style("text-anchor", "end")
            .text("Count");

    svg.selectAll(".bar")
            .data(data)
            .enter().append("rect")
            .attr("class", "bar")
            .attr("x", function (d) {
                return x(d.record_date);
            })
            .attr("width", x.rangeBand())
            .attr("y", function (d) {
                return y(d.total);
            })
            .attr("height", function (d) {
                return height - y(d.total);
            });


    function type(d) {
        d.total = +d.total;
        return d;
    }

















    $http.get("../../../static/datas/2.json").success(function (response) {
        //$scope.tableItems = response;
    });
})