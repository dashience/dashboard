/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
function chart(){alert("Chart")
$.get('static/datas/defaultPanel.json', function (response, status) {
    
    angular.forEach(response, function (value, key) {
        console.log(value);
        console.log($('#'+value.chartId));
        if (value.chartType === "line") {
            lineChart(value);
        } else if (value.chartType === "area") {
            areaChart(value);
        } else if (value.chartType === "bar") {
            barChart(value);
        } else if (value.chartType === "groupedbar") {
            groupedBarChart(value);
        } else if (value.chartType === "pie") {
            pieChart(value);
        } else if (value.chartType === "donut") {
            donutChart(value);
        } else if (value.chartType === "table") {
            dashboardTable(value);
        }
        else {
            alert("No Charts Available");
        }
    });
});

function dashboardTable(value){
    //$( "#myDiv" ).css( "border", "3px solid red" );
}

function lineChart(value) {

}
//areaChart()
function areaChart(value) {

}

function barChart(value) {
   
}

function groupedBarChart(value) {
    var margin = {
        top: 20,
        right: 20,
        bottom: 30,
        left: 40
    },
    width = 410 - margin.left - margin.right,
            height = 260 - margin.top - margin.bottom;
    var x0 = d3.scale.ordinal()
            .rangeRoundBands([0, width], .1);
    var x1 = d3.scale.ordinal();
    var y = d3.scale.linear()
            .range([height, 0]);
    var color = d3.scale.ordinal()
            .range(["#98abc5", "#6b486b", "#ff8c00"]);
    var xAxis = d3.svg.axis()
            .scale(x0)
            .orient("bottom");
    var yAxis = d3.svg.axis()
            .scale(y)
            .orient("left")
            .tickFormat(d3.format(".2s"));
    var svg = d3.select("#" + value.dispChart + value.chartId).append("svg")
            .attr("width", width + margin.left + margin.right)
            .attr("height", height + margin.top + margin.bottom)
            .append("g")
            .attr("transform", "translate(" + margin.left + "," + margin.top + ")");
    d3.json(value.url, function (error, json) {
        var data;
        if (!json)
            json = error; //not sure why error seems to contain the data!...
        if (json[0] && json[0].summary) {
            data = json[0].summary;
        } else {
            data = json;
        }
        var burgers = [];
        var dates = [];
        data.forEach(function (d) {
            var i = burgers.indexOf(d.name);
            if ((i < 0)) {
                burgers.push(d.name)
            }
            d.date = d.trend.years + "-" + d.trend.months;
            i = dates.indexOf(d.date);
            if ((i < 0)) {
                dates.push(d.date)
            }
        });
        var newData = [];
        burgers.forEach(function (burger) {
            var val = [];
            data.forEach(function (d) {
                if (burger == d.name) {
                    val.push(d);
                }
            });
            newData.push({name: burger, value: val});
        });
        console.log(newData)
        x0.domain(newData.map(function (d) {
            return d.name;
        }));
        x1.domain(dates).rangeRoundBands([0, x0.rangeBand()]);
        y.domain([0, d3.max(data, function (d) {
                return d.volume;
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
                .text("Volume");
        var state = svg.selectAll(".state")
                .data(newData)
                .enter().append("g")
                .attr("class", "g")
                .attr("transform", function (d) {
                    return "translate(" + x0(d.name) + ",0)";
                });
        state.selectAll("rect")
                .data(function (d) {
                    return d.value;
                })
                .enter()
                .append("rect")
                .attr("width", x1.rangeBand())
                .attr("x", function (d) {
                    return x1(d.date);
                })
                .attr("y", function (d) {
                    return y(d.volume);
                })
                .attr("height", function (d) {
                    return height - y(d.volume);
                })
                .style("fill", function (d) {
                    return color(d.date);
                });
        var legend = svg.selectAll(".legend")
                .data(dates.slice().reverse())
                .enter().append("g")
                .attr("class", "legend")
                .attr("transform", function (d, i) {
                    return "translate(0," + i * 20 + ")";
                });
        legend.append("rect")
                .attr("x", width - 18)
                .attr("width", 18)
                .attr("height", 18)
                .style("fill", color);
        legend.append("text")
                .attr("x", width - 24)
                .attr("y", 9)
                .attr("dy", ".35em")
                .style("text-anchor", "end")
                .text(function (d) {
                    return d;
                });
    })
}
function pieChart(value) {

}

function donutChart(value) {

}


function table(value) {

    
    d3.json(value.url, function (error,data) {
        var columns = [];
        var columnsObject = [];
        angular.forEach(data, function(child, childKey){
        console.log("child : "+child+" ----- "+child.TableName);
        angular.forEach(child.childItems, function(value, key){
            columnsObject.push(value);
            angular.forEach(value, function(object, header){
                        console.log("Header : "+header)
                for (property in header) {
                    if (columns.indexOf(header) === -1) {
                        columns.push(header);
                        console.log(columns);
                    };
                };            
            });
           
        });
})
  function tabulate(data, columns) {
		var table = d3.select("#" + value.dispChart + value.chartId).append('table')
		var thead = table.append('thead')
		var tbody = table.append('tbody');

		// append the header row
		thead.append('tr')
		  .selectAll('th')
		  .data(columns).enter()
		  .append('th')
		    .text(function (column) { return column; });

		// create a row for each object in the data
		var rows = tbody.selectAll('tr')
		  .data(data)
		  .enter()
		  .append('tr');

		// create a cell in each row for each column
		var cells = rows.selectAll('td')
		  .data(function (row) {
		    return columns.map(function (column) {
		      return {column: column, value: row[column]};
		    });
		  })
		  .enter()
		  .append('td')
		    .text(function (d) { return d.value; });

	  return table;
	}
	// render the table(s)
	tabulate(columnsObject, columns); // 2 column table

});
}
}