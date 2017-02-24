app.controller('UiController', function ($scope, $http, $stateParams, $state, $filter, $cookies, $timeout, localStorageService, $rootScope) {
    $scope.permission = localStorageService.get("permission");
    console.log($stateParams.productId)

//    $scope.selectTabID = $state;
    $http.get("admin/ui/dashboard").success(function (response) {
        console.log(response)
        $scope.filterDashboard = [];
        angular.forEach(response, function (value, key) {
            $scope.filterDashboard.push({id: value.id, name: value.dashboardTitle, dasahoardProductId: value.productId.id})
            $scope.findDashboardId = $filter('filter')($scope.filterDashboard, {dasahoardProductId: $stateParams.productId})[0];
            console.log($scope.findDashboardId)
        });
        console.log($scope.findDashboardId.name);
        $http.get("admin/ui/dbTabs/" + $scope.findDashboardId.id).success(function (response) {
            console.log(response)
            $scope.loadTab = false;
            $scope.tabs = response;
            angular.forEach(response, function (value, key) {
                $scope.dashboardName = value.dashboardId.dashboardTitle;
                console.log(value.dashboardId.dashboardTitle)
            });
            if (!response) {
                return;
            }
            if (!$stateParams.tabId) {
                $state.go("index.dashboard.widget", {locationId: $stateParams.locationId, tabId: response[0].id, startDate: $stateParams.startDate, endDate: $stateParams.endDate});
            } else {
                $state.go("index.dashboard.widget", {locationId: $stateParams.locationId, tabId: $stateParams.tabId, startDate: $stateParams.startDate, endDate: $stateParams.endDate});
            }
        });
    })

    $scope.userName = $cookies.getObject("username");
    $scope.productId = $stateParams.productId;
    $scope.tabId = $stateParams.tabId;
    $scope.dataCheck = function () {
        console.log($stateParams.startDate + " - " + $stateParams.endDate);
    }

    $scope.toDate = function (strDate) {
        if (!strDate) {
            return new Date();
        }
        var from = strDate.split("/");
        var f = new Date(from[2], from[0] - 1, from[1]);
        return f;
    };

    $scope.getDay = function () {
        var today = new Date();
        var yesterday = new Date(today);
        yesterday.setDate(today.getDate() - 29);
        return yesterday;
    };

    $scope.firstDate = $stateParams.startDate ? $scope.toDate(decodeURIComponent($stateParams.startDate)) : $scope.getDay().toLocaleDateString("en-US");
    $scope.lastDate = $stateParams.endDate ? $scope.toDate(decodeURIComponent($stateParams.endDate)) : new Date().toLocaleDateString("en-US");

    if (!$stateParams.startDate) {
        $stateParams.startDate = $scope.firstDate;
    }
    if (!$stateParams.endDate) {
        $stateParams.endDate = $scope.lastDate;
    }

    try {
        $scope.startDate = moment($('#daterange-btn').data('daterangepicker').startDate).format('MM/DD/YYYY') ? moment($('#daterange-btn').data('daterangepicker').startDate).format('MM/DD/YYYY') : $scope.firstDate;//$scope.startDate.setDate($scope.startDate.getDate() - 1);

        $scope.endDate = moment($('#daterange-btn').data('daterangepicker').endDate).format('MM/DD/YYYY') ? moment($('#daterange-btn').data('daterangepicker').endDate).format('MM/DD/YYYY') : $scope.lastDate;
    } catch (e) {
    }


    $scope.editDashboardTab = function (tab) {
        var data = {
            tabNames: tab.tabName
        };
        $scope.tab = data;
        $timeout(function () {
            $('#editTab' + tab.id).modal('show');
        }, 100);
    };

    $scope.selectProductName = "Select Product";
    $scope.changeProduct = function (product) {
        $scope.selectProductName = product.productName;
        $scope.productId = product.id;
    };
    $http.get('admin/ui/product').success(function (response) {
        $scope.products = response;
    });

    $http.get('admin/user/sampleDealers').success(function (response) {
        $scope.dealers = response;
    });

    $scope.loadTab = true;

    var dates = $(".pull-right i").text();

    $scope.tabs = [];
    $scope.addTab = function (tab) {
        console.log($scope.findDashboardId)
        console.log($scope.findDashboardId.id)
        var data = {
            tabName: tab.tabName
        };
        $http({method: 'POST', url: 'admin/ui/dbTabs/' + $scope.findDashboardId.id, data: data}).success(function (response) {
            $scope.tabs.push({id: response.id, tabName: tab.tabName, tabClose: true});
        });
        $scope.tab = "";
    };

    $scope.deleteTab = function (index, tab) {
        console.log($scope.findDashboardId.id)
        $http({method: 'DELETE', url: 'admin/ui/dbTab/' + tab.id}).success(function (response) {
            $http.get("admin/ui/dbTabs/" + $scope.findDashboardId.id).success(function (response) {
                $scope.loadTab = false;
                $scope.tabs = response;
                angular.forEach(response, function (value, key) {
                    $scope.dashboardName = value.dashboardId.dashboardTitle;
                });
                //$scope.startId = response[0].id ? response[0].id : 0;
                $state.go("index.dashboard.widget", {locationId: $stateParams.locationId, tabId: response[0].id, startDate: $stateParams.startDate, endDate: $stateParams.endDate});
            });
        });
    };

    $scope.reports = [];
    $scope.addParent = function () {
        $scope.reports.push({isEdit: true, childItems: []});
    };
    $scope.addChild = function (report) {
        report.childItems.push({isEdit: true});
    };

    $http.get('static/datas/report.json').success(function (response) {
        $scope.reports = response;
    });

    $scope.moveItem = function (list, from, to) {
        list.splice(to, 0, list.splice(from, 1)[0]);
        return list;
    };

    $scope.onDropTabComplete = function (index, tab, evt) {
        if (tab !== "" && tab !== null) {
            var otherObj = $scope.tabs[index];
            var otherIndex = $scope.tabs.indexOf(tab);

            $scope.tabs = $scope.moveItem($scope.tabs, otherIndex, index);
            var tabOrder = $scope.tabs.map(function (value, key) {
                if (value) {
                    return value.id;
                }
            }).join(',');
            if (tabOrder) {
                $http({method: 'GET', url: 'admin/ui/dbTabUpdateOrder/' + $stateParams.productId + "?tabOrder=" + tabOrder});
            }
        }
    };

    $scope.editedItem = null;
    $scope.startEditing = function (tab) {
        tab.editing = true;
        $scope.editedItem = tab;
    };

    $scope.doneEditing = function (tab) {
        var data = {
            id: tab.id,
            createdTime: tab.createdTime,
            dashboardId: tab.dashboardId,
            modifiedTime: tab.modifiedTime,
            remarks: tab.remarks,
            status: tab.status,
            tabName: tab.tabName,
            tabOrder: tab.tabOrder
        };
        $http({method: 'PUT', url: 'admin/ui/dbTabs/' + $stateParams.productId, data: data})
        tab.editing = false;
        $scope.editedItem = null;
    };
})
        .directive('ngBlur', function () {
            return function (scope, elem, attrs) {
                elem.bind('blur', function () {
                    scope.$apply(attrs.ngBlur);
                });
            };
        })

        .directive('ngFocus', function ($timeout) {
            return function (scope, elem, attrs) {
                scope.$watch(attrs.ngFocus, function (newval) {
                    if (newval) {
                        $timeout(function () {
                            elem[0].focus();
                        }, 0, false);
                    }
                });
            };
        });
