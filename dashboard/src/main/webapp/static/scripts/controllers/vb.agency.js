app.controller('AgencyController', function ($scope, $http) {

//Tabs
    $scope.tab = 1;
    $scope.setTabAgency = function (newTab) {
        $scope.tab = newTab;
    };
    $scope.isSetAgency = function (tabNum) {
        return $scope.tab === tabNum;
    };
    $scope.addAgency = function () {
        $scope.agency = {logo: "static/img/logos/deeta-logo.png"};
    };
    $scope.agencies = [];
    function getAgency() {
        $http.get('admin/user/agency').success(function (response) {
            $scope.agencies = response;
            //  $scope.editAgency(response[0], 0);
        });
    }
    getAgency();
    $scope.agency = {logo: "static/img/logos/deeta-logo.png"}; //Logo Upload
    $scope.imageUpload = function (event) {
        var files = event.target.files;
        for (var i = 0; i < files.length; i++) {
            var file = files[i];
            var reader = new FileReader();
            reader.onload = $scope.imageIsLoaded;
            reader.readAsDataURL(file);
        }
    };
    $scope.imageIsLoaded = function (e) {
        $scope.$apply(function () {
            $scope.agency.logo = e.target.result;
        });
    };
    $scope.saveAgency = function (agency) {
        if ($scope.agency.logo === "static/img/logos/deeta-logo.png") {
            $scope.agency.logo = "";
        }
        var data = {
            id: agency.id,
            agencyName: agency.agencyName,
            email: agency.email,
            description: agency.description,
            status: agency.status,
            logo: $scope.agency.logo
        };
        $http({method: agency.id ? 'PUT' : 'POST', url: 'admin/user/agency', data: data}).success(function (response) {
            getAgency();
        });
        $scope.agency = {logo: "static/img/logos/deeta-logo.png"};
    };
    $scope.selectedRow = null;
    $scope.editAgency = function (agency, index) {
        getAgencyLicence(agency)
        $scope.agencyLicenceId = agency;
        var data = {
            id: agency.id,
            agencyName: agency.agencyName,
            email: agency.email,
            description: agency.description,
            status: agency.status,
            logo: agency.logo ? agency.logo : "static/img/logos/deeta-logo.png"
        };
        $scope.agency = data;
        $scope.selectedRow = index;
    };
    $scope.clearAgency = function () {
        $scope.agency = {logo: "static/img/logos/deeta-logo.png"};
    };
    $scope.deleteAgency = function (agency, index) {
        $http({method: 'DELETE', url: 'admin/user/agency/' + agency.id}).success(function (response) {
            $scope.agencies.splice(index, 1);
        });
    };
    $scope.searchAgencyDetails = function (agencyList) {
        $scope.searchAgencyItem = agencyList;
    }
    $scope.agencyProducts = [];
    function getAgencyLicence(agency) {
        $http.get('admin/user/agencyLicence/' + agency.id).success(function (response) {
            $scope.agencyLicence = {}
            $scope.agencyLicences = response;
            console.log($scope.agencyLicences)
            angular.forEach(response, function (value, key) {
                $scope.agencyLicence = {id: value.id, maxNoTab: value.maxNoTab, maxNoUser: value.maxNoUser, maxNoClient: value.maxNoClient, maxNoAccount: value.maxNoAccount, expiryDate: value.expiryDate, maxNoWidgetPerTab: value.maxNoWidgetPerTab}
                console.log($scope.agencyLicence)
            });
        });

        $http.get('admin/user/agencyUser/' + agency.id).success(function (response) {
            $scope.agencyUsers = response;
        });

        $http.get('admin/user/agencyProduct/' + agency.id).success(function (response) {
            $scope.agencyProducts = response;
        });
    }

    $scope.saveAgencyLicence = function (agencyLicence) {
        console.log($scope.agencyLicenceId)
        var data = {
            id: agencyLicence.id,
            agencyId: $scope.agencyLicenceId.id,
            maxNoTab: agencyLicence.maxNoTab,
            maxNoUser: agencyLicence.maxNoUser,
            maxNoClient: agencyLicence.maxNoClient,
            maxNoAccount: agencyLicence.maxNoAccount,
            expiryDate: new Date(agencyLicence.expiryDate),
            maxNoWidgetPerTab: agencyLicence.maxNoWidgetPerTab
        };
        $http({method: agencyLicence.id ? 'PUT' : 'POST', url: 'admin/user/agencyLicence', data: data}).success(function (response) {
            $scope.agencyLicences = response;
        });
    };

    $scope.addAgencyUser = function () {
        $scope.agencyUser = "";
        $scope.showAgencyUserForm = true;
    };

    $scope.saveAgencyUser = function (agencyUser) {
        var agencyUserData = {
            id: agencyUser.id,
            firstName: agencyUser.firstName,
            lastName: agencyUser.lastName,
            userName: agencyUser.userName,
            email: agencyUser.email,
            password: agencyUser.password,
            primaryPhone: agencyUser.primaryPhone,
            secondaryPhone: agencyUser.secondaryPhone,
            agencyId: $scope.agencyLicenceId.id,
        };

        $http({method: agencyUser.id ? 'PUT' : 'POST', url: 'admin/ui/user', data: agencyUserData}).success(function (response) {
            getAgencyLicence($scope.agencyLicenceId);
        });
        $scope.agencyUser = "";
        $scope.showAgencyUserForm = false;
    };

    $scope.clearAgencyUser = function () {
        $scope.showAgencyUserForm = false;
        $scope.agencyUser = "";
    };

    $scope.addAgencyProduct = function () {
        $scope.agencyProduct = {icon: "static/img/logos/deeta-logo.png"};
        $scope.showAgencyProductForm = true;
    }
    $scope.selectedRows = null;
    $scope.setAccountUserRow = function (agencyProduct, index) {
        console.log(index);
        var data = {
            id: agencyProduct.id,
            productName: agencyProduct.productName,
            icon: $scope.agencyProduct.icon,
            agencyId: $scope.agencyLicenceId.id,
            showProduct: agencyProduct.showProduct
        }
        $scope.agencyProduct = data;
        $scope.selectedRows = index;
        $scope.showAgencyProductForm = true;
//        console.log($index.productName);
    }
    $scope.agencyProduct = {icon: "static/img/logos/deeta-logo.png"};
    $scope.productIcon = function (event) {
        var files = event.target.files;
        for (var i = 0; i < files.length; i++) {
            var file = files[i];
            var reader = new FileReader();
            reader.onload = $scope.imageLoadingIcon;
            reader.readAsDataURL(file);
        }
    };
    $scope.imageLoadingIcon = function (e) {
        $scope.$apply(function () {
            $scope.agencyProduct.icon = e.target.result;
        });
    };
    $scope.saveAgencyProduct = function (agencyProduct) {
        var agencyProductId = $scope.agencyLicenceId;
        console.log(agencyProductId)
        if ($scope.agencyProduct.icon === "static/img/logos/deeta-logo.png") {
            $scope.agencyProduct.icon = "";
        }
        var data = {
            id: agencyProduct.id,
            productName: agencyProduct.productName,
            icon: $scope.agencyProduct.icon,
            agencyId: $scope.agencyLicenceId.id,
            showProduct: agencyProduct.showProduct
        };
        $http({method: agencyProduct.id ? 'PUT' : 'POST', url: 'admin/user/agencyProduct', data: data}).success(function (response) {
            getAgencyLicence(agencyProductId);
            $scope.showAgencyProductForm = false;
        });
        $scope.agencyProduct = {icon: "static/img/logos/deeta-logo.png"};
    };
    $scope.clearAgencyProduct = function () {
        $scope.showAgencyProductForm = false;
        $scope.agencyProduct = {icon: "static/img/logos/deeta-logo.png"};
    };
    $scope.selectedAgencyProduct = null;
    $scope.setAgencyUserRow = function (agencyProduct, index) {
        var data = {
            id: agencyProduct.id,
            productName: agencyProduct.productName,
            icon: agencyProduct.icon,
            agencyId: $scope.agencyLicenceId.id,
            showProduct: agencyProduct.showProduct
        }
        $scope.agencyProduct = data;
        $scope.selectedAgencyProduct = index;
        $scope.showAgencyProductForm = true;     
    }
    $scope.changeOrder = function (s) {
        var agencyProductId = $scope.agencyLicenceId;
//        console.log(s)
//if()
        var order = s.map(function (value, key) {
            if (value) {
                return value.id;
            }
        }).join(',');
        console.log(order)

//        if (order) {
//            $http({method: 'GET', url: 'admin/user/productUpdateOrder/' + agencyProductId.id + "?productOrder=" + order});
//        }
    };
});
app.directive("datePicker", function () {
    return {
        restrict: "A",
        link: function (scope, el, attr) {
            el.datepicker({
                dateFormat: 'yy-mm-dd'
            });
        }
    };
});
app.directive('customOnChange', function () {
    return {
        restrict: 'A',
        link: function (scope, element, attrs) {
            var onChangeFunc = scope.$eval(attrs.customOnChange);
            element.bind('change', onChangeFunc);
        }
    };
});