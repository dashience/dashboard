(function () {
angular.module('DashienceApp').controller('HeaderController', function ($scope, $location, httpService, loginFactory) {
        var headerObj = this;
        var requestUrl = serviceUrl;

        headerObj.authObj = {};
        headerObj.selectAccount = {selected: {}};
        headerObj.productName = '';
        var resolveObj = $scope.$resolve.resolveObj;

        getResolveObj();

        headerObj.authObj = loginFactory.getAuthObj();
        headerObj.authObj.agencyLanguage = loginFactory.getAgencyLan();
        
//        headerObj.permission = headerObj.authObj.permission;
//        headerObj.userName = headerObj.authObj.username;
//        headerObj.isAdmin = headerObj.authObj.isAdmin;
//        headerObj.agencyId = headerObj.authObj.agencyId;
//        headerObj.fullName = headerObj.authObj.fullName;
//        headerObj.lan = headerObj.authObj.agencyLanguage;

        function getResolveObj() {
            
            console.log("resolve obj",resolveObj)
            headerObj.properties = resolveObj.base_property;

            if (!resolveObj.getUserAccount || !resolveObj.getLastUserAccount) {
                return;
            }
            headerObj.accounts = resolveObj.getUserAccount;
            if (resolveObj.getLastUserAccount) {
                headerObj.accountId = resolveObj.getLastUserAccount.accountId.id;
                headerObj.accountName = resolveObj.getLastUserAccount.accountId.accountName;
            } else {
                headerObj.accountId = headerObj.accountId ? headerObj.accountId : resolveObj.getUserAccount[0].accountId.id;
                headerObj.accountName = headerObj.accountName ? headerObj.accountName : resolveObj.getUserAccount[0].accountId.accountName;
            }
            angular.forEach(headerObj.accounts, function (value, key) {
                if (value.accountId.id == headerObj.accountId) {
                    headerObj.name = value;
                }
            });
            headerObj.selectAccount.selected = {accountName: headerObj.name.accountId.accountName};
            headerObj.accountLogo = headerObj.name.accountId.logo;
            if (!headerObj.name.userId.agencyId) {
                return;
            }
//            getAgencyProduct(headerObj.name.userId.agencyId.id);
             getAgencyProduct()
        }


        function getAgencyProduct() {
           
//            var product = httpService.httpProcess('GET', requestUrl.productUrl + agencyId);
//            product.then(function (response) {
                headerObj.products = resolveObj.getAgencyProduct;
                if (!resolveObj.getAgencyProduct) {
                    return;
                }
                angular.forEach(headerObj.products, function (val, key) {
                    if (key == 0) {
                        headerObj.productName = 'Dashboard:' + val.productName;
                    }
                });
                var getTemplateId = headerObj.products[0].templateId ? headerObj.products[0].templateId.id : 0;
                headerObj.productId = headerObj.productId ? headerObj.productId : headerObj.products[0].id;
                var getProductTemplateId = headerObj.templateId ? headerObj.templateId : getTemplateId;

                var template = httpService.httpProcess('GET', requestUrl.templateUrl + headerObj.productId + "/" + headerObj.accountId);
                template.then(function (response) {

                    var getLastTemplateId = response;
                    var templateId = getLastTemplateId ? getLastTemplateId.id : getProductTemplateId;
                    headerObj.templateId = templateId;
                    try {
                        var startDate = moment($('#daterange-btn').data('daterangepicker').startDate).format('MM/DD/YYYY') ? moment($('#daterange-btn').data('daterangepicker').startDate).format('MM/DD/YYYY') : $scope.firstDate;//$scope.startDate.setDate($scope.startDate.getDate() - 1);

                        var endDate = moment($('#daterange-btn').data('daterangepicker').endDate).format('MM/DD/YYYY') ? moment($('#daterange-btn').data('daterangepicker').endDate).format('MM/DD/YYYY') : $scope.lastDate;
                    } catch (e) {
                    }
                });
                setQueryParams(headerObj);
//            });
        }

        function setQueryParams(paramObj) {
            $location.search({'lan': paramObj.lan, 'accountId': paramObj.accountId, 'accountName': paramObj.accountName,
                'productId': paramObj.productId, 'templateId': paramObj.templateId, 'tabId': paramObj.tabId});
        }

        headerObj.setParamsProduct = function (product) {
            headerObj.productName = 'Dashboard:' + product.productName;
            var setTabId = 0;
            var productId = product.id;
            var lastTemplateId;
            var setTemplateId = product.templateId ? product.templateId.id : 0;

            if (headerObj.productId != product.id) {
                var productUrl = httpService.httpProcess('GET', requestUrl.templateUrl + productId + "/" + headerObj.accountId);
                productUrl.then(function (response) {
                    var responseObj = response;
                    lastTemplateId = responseObj ? responseObj.id : null;
                    headerObj.productId = product.id;
                    headerObj.templateId = lastTemplateId ? lastTemplateId : setTemplateId;
                    headerObj.tabId = setTabId;

                });
            } else {
                return;
            }
        };

        headerObj.getProduct = function (product) {
            headerObj.productName = product;
        };

//    $scope.headerObj = headerObj;
    });
})();

