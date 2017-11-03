app.controller('UiController', function ($scope, $http, $stateParams, $state, $filter, $cookies, $timeout, localStorageService, $rootScope, $translate) {
    $scope.userName = $cookies.getObject("username");
    $scope.permission = localStorageService.get("permission");
    $scope.isAdmin = $cookies.getObject("isAdmin");
    $scope.accountId = $stateParams.accountId;
    $scope.accountName = $stateParams.accountName;
    $scope.userId = $cookies.getObject("userId");
    $scope.tempStartDate = $stateParams.startDate;
    $scope.tempEndDate = $stateParams.endDate;
    $scope.agencyLanguage = $stateParams.lan;
    var lan = $scope.agencyLanguage;
    changeLanguage(lan);

    function changeLanguage(key) {
        $translate.use(key);
    }

    //get Templates
    $scope.selectTemplate = {};
    $scope.getAllTemplate = function () {
        if ($stateParams.templateId == 0) {
            $scope.templateUserId = $scope.userId;
        }
        $http.get('admin/ui/dashboardTemplate/' + $stateParams.productId).success(function (response) {
            $scope.templates = response;
            var template = "";
            if ($stateParams.templateId != 0) {
                template = $filter('filter')(response, {id: $stateParams.templateId})[0];
            }
            $scope.selectTemplate.selected = template;
            var templateUserId = template ? template.userId : "";
            if (!templateUserId) {
                return;
            } else {
                if ($stateParams.templateId != 0) {
                    $scope.templateUserId = template.userId.id;
                }
            }
        });
    };

    $scope.getAllTemplate();

    $scope.getTemplateById = function (template) {
        $stateParams.productId = template.agencyProductId.id;
        $scope.templateId = template.id;
        $scope.accountId = $stateParams.accountId;
        $scope.accountName = $stateParams.accountName;
        $scope.productId = $stateParams.productId;
        $scope.tempObj = template;

        var data = {
            accountId: parseInt($stateParams.accountId),
            productId: $stateParams.productId,
            templateId: template.id
        };

        $http({method: 'POST', url: 'admin/template/productAccountUserTemplate', data: data})
    };

    //product tabs
    $scope.tabs = [];
    $scope.userLogout = function () {
        window.location.href = "login.html";
    };

    $scope.getCurrentPage = function () {
        var url = window.location.href;
        if (url.indexOf("dashboardTemplate") > 0) {
            return "dashboardTemplate";
        }
        if (url.indexOf("widget") > 0) {
            return "dashboard";
        }
    };
    var tabUrl;
    if ($stateParams.templateId > 0) {
        tabUrl = 'admin/ui/templateTabs/' + $stateParams.templateId;
    } else {
        tabUrl = "admin/ui/dbTabs/" + $stateParams.productId + "/" + $stateParams.accountId;
    }
    if ($stateParams.productId) {
        $http.get(tabUrl).success(function (response) {
            var getCurrentUrl;
            var setTabId;
            $scope.loadTab = false;
            if (!response[0].templateId) {
                $scope.tabs = response;
                angular.forEach($scope.tabs, function (value, key) {
                    $scope.dashboardName = value.agencyProductId.productName;
                });

                if (!response) {
                    setTabId = "";
                }
                if (!response[0]) {
                    setTabId = "";
                } else {
                    if ($stateParams.tabId == 0) {
                        setTabId = response[0].id;
                        getCurrentUrl = $scope.getCurrentPage();
                    } else {
                        setTabId = $stateParams.tabId ? $stateParams.tabId : (response[0].id ? response[0].id : 0);
                        getCurrentUrl = $scope.getCurrentPage();
                    }
                }
            } else {
                angular.forEach(response, function (value, key) {
                    $scope.tabs.push(value.tabId);
                    $scope.dashboardName = value.templateId.agencyProductId.productName;
                });

                if (!response) {
                    setTabId = "";
                }
                if (!response[0]) {
                    setTabId = "";
                } else {
                    if ($stateParams.tabId == 0) {
                        setTabId = response[0].tabId.id;
                        getCurrentUrl = $scope.getCurrentPage();
                    } else {
                        setTabId = $stateParams.tabId ? $stateParams.tabId : (response[0].tabId ? response[0].tabId.id : 0);
                        getCurrentUrl = $scope.getCurrentPage();
                    }
                }
            }
            $state.go("index.dashboard.widget", {
                lan: $stateParams.lan,
                accountId: $stateParams.accountId,
                accountName: $stateParams.accountName,
                productId: $stateParams.productId,
                templateId: $stateParams.templateId,
                tabId: setTabId
                        // startDate: $stateParams.startDate,
                        //endDate: $stateParams.endDate
            });
//            }
        });
    }
//
//    $scope.toDate = function (strDate) {
//        if (!strDate) {
//            return new Date();
//        }
//        var from = strDate.split("/");
//        var f = new Date(from[2], from[0] - 1, from[1]);
//        return f;
//    };
//
//    $scope.getDay = function () {
//        var today = new Date();
//        var yesterday = new Date(today);
//        yesterday.setDate(today.getDate() - 30);
//        return yesterday;
//    };
//
//    $scope.getBeforeDay = function () {
//        var today = new Date();
//        var yesterday = new Date(today);
//        yesterday.setDate(today.getDate() - 1);
//        return yesterday;
//    };
//
//    $scope.firstDate = $stateParams.startDate ? $scope.toDate(decodeURIComponent($stateParams.startDate)) : $scope.getDay().toLocaleDateString("en-US");
//    $scope.lastDate = $stateParams.endDate ? $scope.toDate(decodeURIComponent($stateParams.endDate)) : $scope.getBeforeDay().toLocaleDateString("en-US");
//
//    if (!$stateParams.startDate) {
//        $stateParams.startDate = $scope.firstDate;
//    }
//    if (!$stateParams.endDate) {
//        $stateParams.endDate = $scope.lastDate;
//    }
//
//    try {
//        $scope.startDate = moment($('#daterange-btn').data('daterangepicker').startDate).format('MM/DD/YYYY') ? moment($('#daterange-btn').data('daterangepicker').startDate).format('MM/DD/YYYY') : $scope.firstDate;//$scope.startDate.setDate($scope.startDate.getDate() - 1);
//
//        $scope.endDate = moment($('#daterange-btn').data('daterangepicker').endDate).format('MM/DD/YYYY') ? moment($('#daterange-btn').data('daterangepicker').endDate).format('MM/DD/YYYY') : $scope.lastDate;
//    } catch (e) {
//    }

//Edit Dashboard Tab
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

    $http.get('admin/user/sampleDealers').success(function (response) {
        $scope.dealers = response;
    });

    $scope.loadTab = true;

    var dates = $(".pull-right i").text();

    $scope.tabs = [];
    //Add Tab
    $scope.addTab = function (tab) {
        var data = {
            tabName: tab.tabName
        };
        $http({method: 'POST', url: 'admin/ui/dbTabs/' + $stateParams.productId + "/" + $stateParams.accountId + "/" + $stateParams.templateId, data: data}).success(function (response) {
            $stateParams.tabId = "";
            $scope.tabs.push({id: response.id, tabName: tab.tabName, tabClose: true});
            $stateParams.tabId = $scope.tabs[$scope.tabs.length - 1].id;
            $stateParams.startDate = $rootScope.tabStartDate;
            $stateParams.endDate = $rootScope.tabEndDate;
            $state.go("index.dashboard.widget", {
                lan: $stateParams.lan,
                accountId: $stateParams.accountId,
                accountName: $stateParams.accountName,
                templateId: $stateParams.templateId,
                tabId: $stateParams.tabId,
                startDate: $stateParams.startDate,
                endDate: $stateParams.endDate
            });
        });
        $scope.tab = "";
    };
//Delete Tab
    $scope.deleteTab = function (index, tab) {
        $http({method: 'DELETE', url: 'admin/ui/dbTab/' + tab.id}).success(function (response) {
            $http.get("admin/ui/dbTabs/" + $stateParams.productId + "/" + $stateParams.accountId).success(function (response) {
                $scope.loadTab = false;
                $scope.tabs = response;
                $stateParams.tabId = $scope.tabs[$scope.tabs.length - 1].id;
                $stateParams.startDate = $rootScope.tabStartDate;
                $stateParams.endDate = $rootScope.tabEndDate;
                $state.go("index.dashboard.widget", {
                    lan: $stateParams.lan,
                    accountId: $stateParams.accountId,
                    accountName: $stateParams.accountName,
                    templateId: $stateParams.templateId,
                    tabId: $stateParams.tabId,
                    startDate: $stateParams.startDate,
                    endDate: $stateParams.endDate
                });
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
    //Get Reports
    $http.get('static/datas/report.json').success(function (response) {
        $scope.reports = response;
    });
//Drag and Drop in Tab
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
            agencyProductId: tab.agencyProductId,
            modifiedTime: tab.modifiedTime,
            remarks: tab.remarks,
            status: tab.status,
            tabName: tab.tabName,
            tabOrder: tab.tabOrder,
            accountId: tab.accountId,
            userId: tab.userId
        };
        $http({method: 'PUT', url: 'admin/ui/dbTabs/' + $stateParams.productId, data: data});
        tab.editing = false;
        $scope.editedItem = null;
    };
    $scope.templateId = null;

    //save Template
    $scope.saveTemplate = function (template) {
        var tabIds = $scope.tabs.map(function (value, key) {
            if (value) {
                return value.id;
            }
        }).join(',');
        var data = {
            id: template.templateId ? template.templateId : null,
            templateName: template.templateName,
            tabIds: tabIds
        };
        $http({method: 'POST', url: 'admin/ui/saveTemplate/' + $stateParams.productId, data: data}).success(function (response) {
            $scope.getAllTemplate();
        });
        $scope.getAllTemplate();
        $scope.template = "";
        $scope.templateId = "";
    };

    $scope.getUserTemplate = function () {
        $scope.agencyTemplates = [];
        $http.get('admin/ui/getUserTemplate').success(function (response) {
            angular.forEach(response, function (value, key) {
                if (value.shared === "Active") {
                    $scope.sharedUser = 1;
                } else {
                    $scope.sharedUser = 0;
                }
                var data = {
                    id: value.id,
                    templateName: value.templateName,
                    agencyId: value.agencyId,
                    agencyProductId: value.agencyProductId,
                    userId: value.userId,
                    shared: $scope.sharedUser
                };
                $scope.agencyTemplates.push(data);
            });
        });
    };

    $scope.shareUser = function (agencyTemplate) {
        if (agencyTemplate.shared === 1) {
            $scope.shared = "Active";
        } else {
            $scope.shared = "InActive";
        }
        var sharedData = {
            shared: $scope.shared
        };
        $http({method: 'POST', url: 'admin/ui/updateTemplateStatus/' + agencyTemplate.id, data: sharedData}).success(function (response) {

        });
    };
    $scope.deleteUserTemplate = function (agencyTemplate, index) {
        $http({method: 'DELETE', url: 'admin/ui/deleteUserTemplate/' + agencyTemplate.id}).success(function (response) {
            if (agencyTemplate.shared != 1) {
                $scope.agencyTemplates.splice(index, 1);
            }
            $scope.getAllTemplate();
        });
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
