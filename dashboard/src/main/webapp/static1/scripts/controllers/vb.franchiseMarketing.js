app.controller('FranchiseMarketingController', function ($scope) {
    $scope.franchisNames = [{name: "All"}];


//    $scope.firstDate = $stateParams.startDate ? $scope.toDate(decodeURIComponent($stateParams.startDate)) : $scope.getDay().toLocaleDateString("en-US");
//    $scope.lastDate = $stateParams.endDate ? $scope.toDate(decodeURIComponent($stateParams.endDate)) : new Date().toLocaleDateString("en-US");
    $(function () {
        //Initialize Select2 Elements
        $(".select2").select2();
        //Datemask dd/mm/yyyy
        $("#datemask").inputmask("dd/mm/yyyy", {"placeholder": "dd/mm/yyyy"});
        //Datemask2 mm/dd/yyyy
        $("#datemask2").inputmask("mm/dd/yyyy", {"placeholder": "mm/dd/yyyy"});
        //Money Euro
        $("[data-mask]").inputmask();
        //Date range picker
        $('#reservation').daterangepicker();
        //Date range picker with time picker
        $('#reservationtime').daterangepicker({timePicker: true, timePickerIncrement: 30, format: 'MM/DD/YYYY h:mm A'});
        //Date range as a button
        $('#daterange-franchisMarketing').daterangepicker(
                {
                    ranges: {
                        'Last Month': [moment().subtract(1, 'month').startOf('month'), moment().subtract(1, 'month').endOf('month')]
                    },
                    startDate: moment().subtract(29, 'days'),
                    endDate: moment()
                },
                function (start, end) {
                    $('#daterange-btn span').html(start.format('MM-DD-YYYY') + ' - ' + end.format('MM-DD-YYYY'));
                }
        );
        //Date picker
        $('#datepicker').datepicker({
            autoclose: true
        });
        //iCheck for checkbox and radio inputs
        $('input[type="checkbox"].minimal,  input[type="radio"].minimal').iCheck({
            checkboxClass: 'icheckbox_minimal-blue',
            radioClass: 'iradio_minimal-blue'
        });
        //Red color scheme for iCheck
        $('input[type="checkbox"].minimal-red, input[type="radio"].minimal-red').iCheck({
            checkboxClass: 'icheckbox_minimal-red',
            radioClass: 'iradio_minimal-red'
        });
        //Flat red color scheme for iCheck
        $('input[type="checkbox"].flat-red, input[type="radio"].flat-red').iCheck({
            checkboxClass: 'icheckbox_flat-green',
            radioClass: 'iradio_flat-green'
        });
        //Colorpicker
        $(".my-colorpicker1").colorpicker();
        //color picker with addon
        $(".my-colorpicker2").colorpicker();
        //Timepicker
        $(".timepicker").timepicker({
            showInputs: false
        });
    });
})
app.directive('franchisBarChart', function ($http, $stateParams) {
    return{
        restrict: 'A',
        scope: {

        },
        link: function (scope, element, attr) {
            var chart = c3.generate({
                bindto: element[0],
                data: {
                    columns: [
                        ['Returning Customers', 30, 200, 100, 400, 150, 250],
                        ['New Customers', 130, 100, 140, 200, 150, 50]
                    ],
                    type: 'bar'
                },
                color: {
                    pattern: ['#62cb31', '#555555']

                },
                bar: {
                    width: {
                        // ratio: 0.5 
                    }
                }
            })
        }
    }
})
app.directive('franchisLineChart', function ($http, $stateParams) {
    return{
        restrict: 'A',
        scope: {

        },
        link: function (scope, element, attr) {
            var chart = c3.generate({
                bindto: element[0],
                data: {
                    columns: [
                        ['Returning Customers', 30, 200, 100, 400, 150, 250],
                        ['New Customers', 130, 100, 140, 200, 150, 50]
                    ],
                    type: 'line'
                },
                color: {
                    pattern: ['#62cb31', '#555555']

                },
                bar: {
                    width: {
                        // ratio: 0.5 
                    }
                }
            })
        }
    }
})
app.directive('franchisTableChart', function ($http, $stateParams) {
    return{
        restrict: 'A',
        template: '<table class="table table-responsive table-hover table-border">' +
                '<thead>' +
                '<th>#</th>' +
                '<th>Franchise Name</th>' +
                '<th>Customers</th>' +
                '</thead>' +
                '<tbody>' +
                '<tr ng-repeat="customer in customers">' +
                '<td>{{$index+1}}</td>' +
                '<td>{{customer.name}}</td>' +
                '<td>{{customer.count}}</td>' +
                '<tr>' +
                '</tbody>' +
                '</table>',
        scope: {

        },
        link: function (scope, element, attr) {
            scope.customers = [{name: 'Eble James', count: 110},
                {name: 'Alfonso', count: 90},
                {name: 'David', count: 56},
                {name: 'Jack', count: 78},
                {name: 'Mike', count: 90}]
        }
    }
})