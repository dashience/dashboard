function isNumber(n) {
    return !isNaN(parseFloat(n)) && isFinite(n);
}

function minutesGreaterThanSixty(minutes) {
    var minutesInt = parseInt(minutes) - 60;
    minutes = minutesInt + "";
    if (parseInt(minutes) >= 60) {
        minutes = minutesGreaterThanSixty(minutes);
    }
    return minutes;
}

function formatBySecond(second) {
    var minutes = "0" + Math.floor(second / 60);
    var seconds = "0" + (second - minutes * 60);
    var hours = "0" + Math.floor(minutes / 60);
    if (parseInt(minutes) >= 60) {
        minutes = minutesGreaterThanSixty(minutes);
    }
    return hours.substr(-2) + ":" + minutes.substr(-2) + ":" + seconds.substr(-2);
}

function dashboardFormat(column, value) {
    var strValue = value;
    if (strValue.toString().indexOf(',') !== -1) {
        value = value.replace(/\,/g, '');
    }
    if (column.fieldType === "date") {
        return value;
    }
    if (column.fieldType === "string") {
        return value;
    }
    if (column.displayFormat == null) {
        return value;
    }
    if((column.fieldType === "number") && (typeof(value) === 'string')){
        value = parseFloat(value);
    }
    if (column && column.displayFormat) {
        if (column.displayFormat.indexOf("%") > -1) {
            return d3.format(column.displayFormat)(value / 100);
        } else if (column.displayFormat == 'H:M:S') {
           var newValue = value.toString();
            if(newValue.indexOf("-") > -1){
                value = Math.abs(value);
            }
            return formatBySecond(parseInt(value));
        } else {
            return d3.format(column.displayFormat)(value);
        }
    }
}

function dateConvert(fromFormat, toFormat, value) {
    return moment(value, fromFormat).format(toFormat);
    // return value;
}

app.filter('setDecimal', function () {
    return function (input, places) {
        if (isNaN(input))
            return input;
        var factor = "1" + Array(+(places > 0 && places + 1)).join("0");
        return Math.round(input * factor) / factor;
    };
});
app.filter('gridDisplayFormat', function () {
    return function (input, formatString) {
        if (formatString) {
            if (formatString.indexOf("%") > -1) {
                return d3.format(formatString)(input / 100);
            }
            return d3.format(formatString)(input);
        }
        return input;
    };
});
app.filter('capitalize', function () {
    return function (input) {
        return (!!input) ? input.charAt(0).toUpperCase() + input.substr(1).toLowerCase() : '';
    };
});
app.filter('xAxis', [function () {
        return function (chartXAxis) {
            var xAxis = ['', 'x-1'];
            return xAxis[chartXAxis];
        };
    }]);
app.filter('yAxis', [function () {
        return function (chartYAxis) {
            var yAxis = ['', 'y-1', 'y-2'];
            return yAxis[chartYAxis];
        };
    }]);
app.filter('hideColumn', [function () {
        return function (chartYAxis) {
            var hideColumn = ['No', 'Yes'];
            return hideColumn[chartYAxis];
        };
    }]);

//app.filter('xAxis', [function () {
//        return function (chartXAxis) {
//            var xAxis = ['', 'x-1'];
//            return xAxis[chartXAxis];
//        };
//    }]);
//app.filter('yAxis', [function () {
//        return function (chartYAxis) {
//            var yAxis = ['', 'y-1', 'y-2'];
//            return yAxis[chartYAxis];
//        };
//    }]);
//app.filter('hideColumn', [function () {
//        return function (chartYAxis) {
//            var hideColumn = ['No', 'Yes'];
//            return hideColumn[chartYAxis];
//        };
//    }]);