app.controller('DashboardController', function ($scope, $http, $stateParams, $state, $filter, $timeout, $rootScope,
        $translate, loginFactory, httpService, $location) {
    var dashboardObj = this;
    dashboardObj.authObj = {};
    var requestUrl = serviceUrl;

    dashboardObj.authObj = loginFactory.getAuthObj();
    dashboardObj.authObj.agencyLanguage = loginFactory.getAgencyLan();

//    dashboardObj.userName = dashboardObj.authObj.username;
//    dashboardObj.permission = dashboardObj.authObj.permission;
//    dashboardObj.isAdmin = dashboardObj.authObj.isAdmin;
    dashboardObj.accountId = $stateParams.accountId;
    dashboardObj.accountName = $stateParams.accountName;
//    dashboardObj.userId = dashboardObj.authObj.id;
    dashboardObj.tempStartDate = $stateParams.startDate;
    dashboardObj.tempEndDate = $stateParams.endDate;
    dashboardObj.agencyLanguage = $stateParams.lan;
    var lan = dashboardObj.agencyLanguage;
    changeLanguage(lan);

    dashboardObj.accounts = $scope.$resolve.resolveObj.getUserAccount;



    function changeLanguage(key) {
        $translate.use(key);
    }

//    dashboardObj.loadStatus = true;
//    $rootScope.$on("loadStatusChanged", function (event, loadStatus) {
//        dashboardObj.loadStatus = "";
//        $timeout(function () {
//            dashboardObj.loadStatus = loadStatus;
//        }, 20);
//    });

    //get Templates
    dashboardObj.selectTemplate = {};
    dashboardObj.getAllTemplate = function () {
        if ($stateParams.templateId == 0) {
            dashboardObj.templateUserId = dashboardObj.userId;
        }
        var templates = httpService.httpProcess('GET', requestUrl.dashboardTemplateUrl + $stateParams.productId);
        templates.then(function (response) {

//            });
//        $http.get('admin/ui/dashboardTemplate/' + $stateParams.productId).success(function (response) {
            dashboardObj.templates = response;
            var template = "";
            if ($stateParams.templateId != 0) {
                template = $filter('filter')(response, {id: $stateParams.templateId})[0];
            }
            dashboardObj.selectTemplate.selected = template;
            var templateUserId = template ? template.userId : "";
            if (!templateUserId) {
                return;
            } else {
                if ($stateParams.templateId != 0) {
                    dashboardObj.templateUserId = template.userId.id;
                }
            }
        });
    };

    dashboardObj.getAllTemplate();

    dashboardObj.setTemplateById = function (template) {
        $stateParams.productId = template.agencyProductId.id;
        dashboardObj.templateId = template.id;
        dashboardObj.accountId = $stateParams.accountId;
        dashboardObj.accountName = $stateParams.accountName;
        dashboardObj.productId = $stateParams.productId;
        dashboardObj.tempObj = template;

        var data = {
            accountId: parseInt($stateParams.accountId),
            productId: $stateParams.productId,
            templateId: template.id
        };

        var productAccUserTemplate = httpService.httpProcess('POST', requestUrl.productAccUserTempUrl, data);
        productAccUserTemplate.then(function (response) {

        });

//        $http({method: 'POST', url: 'admin/template/productAccountUserTemplate', data: data})
    };

    //product tabs
    dashboardObj.tabs = [];
//    dashboardObj.userLogout = function () {
//        window.location.href = "login.html";
//    };

    dashboardObj.getCurrentPage = function () {
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
        tabUrl = requestUrl.tab_templateId + $stateParams.templateId;
    } else {
        tabUrl = requestUrl.tab_id + $stateParams.productId + "/" + $stateParams.accountId;
    }
    if ($stateParams.productId) {
        var tab = httpService.httpProcess('GET', tabUrl);
        tab.then(function (response) {

//        });
//        $http.get(tabUrl).success(function (response) {
            console.log("*********** TEMPLATE TABS URL ***********", tabUrl);
            console.log("********** TEMPLATE TABS **************", response);
            var getCurrentUrl;
            var setTabId;
            dashboardObj.loadTab = false;
            if (!response[0].templateId) {
                dashboardObj.tabs = response;
                console.log('tabs', response)
//                angular.forEach(dashboardObj.tabs, function (value, key) {
                dashboardObj.dashboardName = response[0].agencyProductId.productName;
//                });

                if (!response) {
                    setTabId = "";
                }
                if (!response[0]) {
                    setTabId = "";
                } else {
                    if ($stateParams.tabId == 0) {
                        setTabId = response[0].id;
                        getCurrentUrl = dashboardObj.getCurrentPage();
                    } else {
                        setTabId = $stateParams.tabId ? $stateParams.tabId : (response[0].id ? response[0].id : 0);
                        getCurrentUrl = dashboardObj.getCurrentPage();
                    }
                }
            } else {
                angular.forEach(response, function (value, key) {
                    dashboardObj.tabs.push(value.tabId);
                    dashboardObj.dashboardName = value.templateId.agencyProductId.productName;
                });

                if (!response) {
                    setTabId = "";
                }
                if (!response[0]) {
                    setTabId = "";
                } else {
                    if ($stateParams.tabId == 0) {
                        setTabId = response[0].tabId.id;
                        getCurrentUrl = dashboardObj.getCurrentPage();
                    } else {
                        setTabId = $stateParams.tabId ? $stateParams.tabId : (response[0].tabId ? response[0].tabId.id : 0);
                        getCurrentUrl = dashboardObj.getCurrentPage();
                    }
                }
            }

//            $state.go("index.dashboard.widget", {
//                lan: $stateParams.lan,
//                accountId: $stateParams.accountId,
//                accountName: $stateParams.accountName,
//                productId: $stateParams.productId,
//                templateId: $stateParams.templateId,
//                tabId: setTabId
//                        // startDate: $stateParams.startDate,
//                        //endDate: $stateParams.endDate
//            });          
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


    $http.get('admin/user/sampleDealers').then(function (response) {
        $scope.dealers = response;
    });

    $scope.loadTab = true;

    var dates = $(".pull-right i").text();

    $scope.tabs = [];
    //Add Tab
    $scope.addTab = function (tab) {

        console.log(loginFactory.addTab(tab))

//        $stateParams.tabId = "";
//            $stateParams.tabId = $scope.tabs[$scope.tabs.length - 1].id;
//            $stateParams.startDate = $rootScope.tabStartDate;
//            $stateParams.endDate = $rootScope.tabEndDate;
//            $scope.tabs.push({id: response.id, tabName: tab.tabName, tabClose: true});
        return;



        var data = {
            tabName: tab.tabName
        };

        var addTab = httpService.httpProcess('POST', requestUrl.addTabUrl + $stateParams.productId + "/" + $stateParams.accountId +
                "/" + $stateParams.templateId, data);
        addTab.then(function (response) {

//        });
//        
//        $http({method: 'POST', url: 'admin/ui/dbTabs/' + $stateParams.productId + "/" + $stateParams.accountId +
//                    "/" + $stateParams.templateId, data: data}).then(function (response) {
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
        var deleteTab = httpService.httpProcess('DELETE', requestUrl.delTabUrl + tab.id);
        deleteTab.then(function (response) {

        });
        $http({method: 'DELETE', url: 'admin/ui/dbTab/' + tab.id}).then(function (response) {
            $http.get("admin/ui/dbTabs/" + $stateParams.productId + "/" + $stateParams.accountId).then(function (response) {
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
//    $http.get('static/datas/report.json').then(function (response) {
//        $scope.reports = response;
//    });
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
                var dbTabUpdate = httpService.httpProcess('GET', requestUrl.dbTabUpdateUrl + $stateParams.productId + "?tabOrder=" + tabOrder);
                dbTabUpdate.then(function (response) {

                });
//                $http({method: 'GET', url: 'admin/ui/dbTabUpdateOrder/' + $stateParams.productId + "?tabOrder=" + tabOrder});
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
        var dbTabs = httpService.httpProcess('PUT', requestUrl.dbTabsUrl + $stateParams.productId, data);
        dbTabs.then(function (response) {

        });
//        $http({method: 'PUT', url: 'admin/ui/dbTabs/' + $stateParams.productId, data: data});
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
        $http({method: 'POST', url: 'admin/ui/saveTemplate/' + $stateParams.productId, data: data}).then(function (response) {
            $scope.getAllTemplate();
        });
        $scope.getAllTemplate();
        $scope.template = "";
        $scope.templateId = "";
    };

    $scope.getUserTemplate = function () {
        $scope.agencyTemplates = [];
        $http.get('admin/ui/getUserTemplate').then(function (response) {
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
        $http({method: 'POST', url: 'admin/ui/updateTemplateStatus/' + agencyTemplate.id, data: sharedData}).then(function (response) {

        });
    };
    $scope.deleteUserTemplate = function (agencyTemplate, index) {
        $http({method: 'DELETE', url: 'admin/ui/deleteUserTemplate/' + agencyTemplate.id}).then(function (response) {
            if (agencyTemplate.shared != 1) {
                $scope.agencyTemplates.splice(index, 1);
            }
            $scope.getAllTemplate();
        });
    };


    $scope.getTableType = "compareOff"//tableTypeByDateRange ? tableTypeByDateRange : "compareOff";

    $scope.compareDateRange = {
        startDate: '07/10/2018', //compareStartDate,
        endDate: new Date()//compareEndDate
    };

    function getWidgetItem() {
        var tabId;
        if (!$stateParams.tabId) {
            alert(1)
//            $stateParams.tabId = 0;
            tabId = 312;
        }
        console.log($stateParams.tabId)
        var dbWidget = httpService.httpProcess('GET', requestUrl.widgetUrl + tabId + '/' + $stateParams.accountId);
        dbWidget.then(function (response) {


//        $http.get("admin/ui/dbWidget/" + $stateParams.tabId + '/' + $stateParams.accountId).success(function (response) {
            var widgetItems = [];
            widgetItems = response;
            if (response) {
                // $scope.productName = response[0].tabId.agencyProductId.productName;
            }
            $http.get("admin/tag/getAllFav/").then(function (favResponse) {
                widgetItems.forEach(function (value, key) {
                    var favWidget = $.grep(favResponse, function (b) {
                        return b.id === value.id;
                    });
                    if (favWidget.length > 0) {
                        value.isFav = true;
                    } else {
                        value.isFav = false;
                    }
                });
            });
            $http.get("admin/ui/getChartColorByUserId").then(function (response) {
                $scope.userChartColors = response;
                var widgetColors;
                if (response.optionValue) {
                    widgetColors = response.optionValue.split(',');
                }
                widgetItems.forEach(function (value, key) {
                    value.chartColors = widgetColors;
                    if (value.chartType == 'horizontalBar') {
                        value.isHorizontalBar = true;
                    } else {
                        value.isHorizontalBar = false;
                    }
                });
                dashboardObj.widgets = widgetItems;
                console.log("widgets", $scope.widgets)
            });
        });
    }
    ;
    getWidgetItem();




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


