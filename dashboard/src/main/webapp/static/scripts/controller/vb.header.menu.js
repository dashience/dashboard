app.controller('HeaderMenuController', function ($scope, $http, $q, $location, httpService) {
    var authObj = {};
    var headerMenuObj = {selectAccount: {}, product: ''};
    authObj = angular.fromJson(sessionStorage.getItem('auth'));
    authObj.agencyLanguage = sessionStorage.getItem('agencyLanguage');

    headerMenuObj.permission = authObj.permission;
    headerMenuObj.userName = authObj.username;
    headerMenuObj.isAdmin = authObj.isAdmin;
    headerMenuObj.agencyId = authObj.agencyId;
    headerMenuObj.fullName = authObj.fullName;
    headerMenuObj.lan = authObj.agencyLanguage;

    var userAccUrl = 'admin/ui/userAccountByUser';
    var lastUserAccUrl = 'admin/user/getLastUserAccount';
    headerMenuObj.getUserAccount = httpService.httpProcess('GET', userAccUrl);//$http.get('admin/ui/userAccountByUser');
    headerMenuObj.getLastUserAccount = httpService.httpProcess('GET', lastUserAccUrl);//$http.get('admin/user/getLastUserAccount');
    var qRequests = $q.all(headerMenuObj).then(function (response) {
        console.log(response)
        if (!response.getUserAccount || !response.getLastUserAccount) {
            return;
        }
        headerMenuObj.accounts = response.getUserAccount;
        if (response.getLastUserAccount) {
            headerMenuObj.accountId = response.getLastUserAccount.accountId.id;
            headerMenuObj.accountName = response.getLastUserAccount.accountId.accountName;
        } else {
            headerMenuObj.accountId = headerMenuObj.accountId ? headerMenuObj.accountId : response.getUserAccount[0].accountId.id;//response[0].data.accountId.id;
            headerMenuObj.accountName = headerMenuObj.accountName ? headerMenuObj.accountName : response.getUserAccount[0].accountId.accountName;//response[0].data.accountId.accountName;
        }
        angular.forEach(headerMenuObj.accounts, function (value, key) {
            if (value.accountId.id == headerMenuObj.accountId) {
                headerMenuObj.name = value;
            }
        });
        headerMenuObj.selectAccount.selected = {accountName: headerMenuObj.name.accountId.accountName};
        headerMenuObj.accountLogo = headerMenuObj.name.accountId.logo;
        if (!headerMenuObj.name.userId.agencyId) {
//            $scope.loadNewUrl();
            return;
        }
        getAgencyProduct(headerMenuObj.name.userId.agencyId.id);
    });

    function getAgencyProduct(agencyId) {
        var productUrl = 'admin/user/agencyProduct/' + agencyId;
        var product = httpService.httpProcess('GET', productUrl);
        product.then(function (response) {
            headerMenuObj.products = response;
            if (!response) {
                return;
            }
            angular.forEach(headerMenuObj.products, function (val, key) {
                if (key == 0) {
                    headerMenuObj.product = 'Dashboard:' + val.productName;
                }
            });
            var getTemplateId = response[0].templateId ? response[0].templateId.id : 0;
            headerMenuObj.productId = headerMenuObj.productId ? headerMenuObj.productId : response[0].id;
            var getProductTemplateId = headerMenuObj.templateId ? headerMenuObj.templateId : getTemplateId;

            var templateUrl = 'admin/template/getProductTemplate/' + headerMenuObj.productId + "/" + headerMenuObj.accountId;
            var template = httpService.httpProcess('GET', templateUrl);
            template.then(function (response) {

//            $http.get("admin/template/getProductTemplate/" + headerMenuObj.productId + "/" + headerMenuObj.accountId).then(function (response) {
                var getLastTemplateId = response;
                var templateId = getLastTemplateId ? getLastTemplateId.id : getProductTemplateId;
                headerMenuObj.templateId = templateId;
                try {
                    var startDate = moment($('#daterange-btn').data('daterangepicker').startDate).format('MM/DD/YYYY') ? moment($('#daterange-btn').data('daterangepicker').startDate).format('MM/DD/YYYY') : $scope.firstDate;//$scope.startDate.setDate($scope.startDate.getDate() - 1);

                    var endDate = moment($('#daterange-btn').data('daterangepicker').endDate).format('MM/DD/YYYY') ? moment($('#daterange-btn').data('daterangepicker').endDate).format('MM/DD/YYYY') : $scope.lastDate;
                } catch (e) {
                }
//                defaultCalls();
            });
            setQueryParams(headerMenuObj);
        });
    }

    function setQueryParams(paramObj) {
        $location.search({'lan': paramObj.lan, 'accountId': paramObj.accountId, 'accountName': paramObj.accountName,
            'productId': paramObj.productId, 'templateId': paramObj.templateId, 'tabId': paramObj.tabId});
    }

    $scope.setParamsProduct = function (product) {
        headerMenuObj.product = 'Dashboard:' + product.productName;
        var setTabId = 0;
        var productId = product.id;
        var lastTemplateId;
        var setTemplateId = product.templateId ? product.templateId.id : 0;

        if (headerMenuObj.productId != product.id) {
            var setProductUrl = 'admin/template/getProductTemplate/' + +productId + "/" + headerMenuObj.accountId;
            var productUrl = httpService.httpProcess('GET', setProductUrl);
            productUrl.then(function (response) {

//            $http.get("admin/template/getProductTemplate/" + productId + "/" + headerMenuObj.accountId).success(function (response) {
                var responseObj = response;
                lastTemplateId = responseObj ? responseObj.id : null;
                headerMenuObj.productId = product.id;
                headerMenuObj.templateId = lastTemplateId ? lastTemplateId : setTemplateId;  //product.templateId ? product.templateId.id : 0;
                headerMenuObj.tabId = setTabId;

//                setQueryParams(headerMenuObj);


//                            $state.go("index.dashboard.widget", {
//                    accountId: $stateParams.accountId,
//                    accountName: $stateParams.accountName,
//                    productId: $stateParams.productId,
//                    templateId: $stateParams.templateId,
//                    tabId: setTabId,
//                    startDate: $stateParams.startDate,
//                    endDate: $stateParams.endDate,
//                    compareStatus: $scope.selectedTablesType,
//                    compareStartDate: $scope.compare_startDate,
//                    compareEndDate: $scope.compare_endDate
//                });

            });
        } else {
            return;
        }
    };

    $scope.getProduct = function (product) {
        headerMenuObj.product = product;

//        setQueryParams(headerMenuObj)
//        $scope.accountId = $stateParams.accountId;
//        $scope.accountName = $stateParams.accountName;
//        $scope.startDate = $stateParams.startDate;
//        $scope.endDate = $stateParams.endDate;
//        $scope.compareStatus = $scope.selectedTablesType;
//        $scope.compareStartDate = $scope.compare_startDate;
//        $scope.compareEndDate = $scope.compare_endDate;
    };



    $scope.headerMenuObj = headerMenuObj;
});


