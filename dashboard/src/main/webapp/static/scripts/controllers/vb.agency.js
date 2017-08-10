app.controller('AgencyController', function ($scope, $http) {

    // currency-format
//   $scope.getCurrencyDataFromServer = function() {
    $http.get('admin/ui/currencies/').
            success(function (data, status, headers, config) {
                $scope.currencies = data;
                console.log(data)
            }).
            error(function (data, status, headers, config) {
//                alert("error!");
            });
//    };  
//    

//    $scope.getTimezoneDataFromServer = function() {
    $http.get('admin/ui/timezones/').
            success(function (data, status, headers, config) {
                $scope.timezones = data;
                console.log(data)
            }).
            error(function (data, status, headers, config) {
//                alert("error!");
            });
//    }; 


    //currency-format

//    $scope.currencies = [
//        {currencyName: "US", currencyFormat: '$'},
//        {currencyName: "UK", currencyFormat: '£'},
//        {currencyName: "INR", currencyFormat: '₹'},
//        {currencyName: "JPN", currencyFormat: '¥'},
//        {currencyName: "EU", currencyFormat: '€'}
//    ];
//
//    $scope.timezone = [
//        {countrName: "US", timezoneFormat: '$'},
//        {countryName: "UK", timezoneFormat: '£'},
//        {countryName: "INR", timezoneFormat: '₹'},
//        {countryName: "JPN", timezoneFormat: '¥'},
//        {countryName: "EU", timezoneFormat: '€'}
//    ];


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
    //Default Templates

    function getTemplateByAgency(agency) {
        $http.get('admin/ui/getDefaultTemplate/' + agency.id).success(function (response) {
            console.log(response);
            $scope.templates = response;
        });
    }

    $scope.selectDefaultTemplate = function (template) {
        console.log(template);
        $scope.templateId = template.id;
    };

    $scope.saveAgency = function (agency) {
        if ($scope.agency.logo === "static/img/logos/deeta-logo.png") {
            $scope.agency.logo = "";
        }
        var data = {
            id: agency.id,
            agencyName: agency.agencyName,
            agencyDashiencePath: agency.agencyDashiencePath,
            email: agency.email,
            description: agency.description,
            status: agency.status,
            logo: $scope.agency.logo
        };
        $http({method: agency.id ? 'PUT' : 'POST', url: 'admin/user/agency', data: data}).success(function (response) {
            $scope.agencyById = data;
            console.log(response);
            getAgency();
            if (response.status == true) {
                $scope.agency = {logo: "static/img/logos/deeta-logo.png"};
            } else if (response.status == false) {
                var dialog = bootbox.dialog({
                    title: 'Alert',
                    message: response.message
                });
                dialog.init(function () {
                    setTimeout(function () {
                        dialog.modal('hide');
                    }, 2000);
                });
            } else {
                $scope.agency = {logo: "static/img/logos/deeta-logo.png"};
            }
        });

    };
    $scope.selectedRow = null;
    $scope.editAgency = function (agency, index) {
        getAgencyLicence(agency);
        $scope.agencyById = agency;
        getTemplateByAgency(agency);
        var data = {
            id: agency.id,
            agencyName: agency.agencyName,
            agencyDashiencePath: agency.agencyDashiencePath,
            email: agency.email,
            description: agency.description,
            status: agency.status,
            logo: agency.logo ? agency.logo : "static/img/logos/deeta-logo.png"
        };
        $scope.agency = data;
        $scope.selectedRow = index;
        $scope.showAgencyUserForm = false;
        $scope.showAgencyProductForm = false;
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
            angular.forEach(response, function (value, key) {
                $scope.agencyLicence = {id: value.id, maxNoTab: value.maxNoTab, maxNoUser: value.maxNoUser, maxNoClient: value.maxNoClient, maxNoAccount: value.maxNoAccount, expiryDate: value.expiryDate, maxNoWidgetPerTab: value.maxNoWidgetPerTab}
            });
        });

        $http.get('admin/user/agencyUser/' + agency.id).success(function (response) {
            $scope.agencyUsers = response;
        });

        $http.get('admin/user/agencyProduct/' + agency.id).success(function (response) {
            $scope.agencyProducts = response;
            console.log(response)
        });

        $http.get('admin/user/agencySetting/' + agency.id).success(function (response) {
            $scope.agencySetting = {}
            $scope.agencysettings = response;


            $scope.agencySetting.id = response.id;
            $scope.agencySetting.currencyId = response.currencyId;
            $scope.agencySetting.timeZoneId = response.timeZoneId;

        });
    }

    $scope.saveAgencyLicence = function (agencyLicence) {
        if (!$scope.agencyById) {
            var dialog = bootbox.dialog({
                title: 'Alert',
                message: '<p>Select Agency</p>'
            });
            dialog.init(function () {
                setTimeout(function () {
                    dialog.modal('hide');
                }, 2000);
            });
        } else {
            var data = {
                id: agencyLicence.id,
                agencyId: $scope.agencyById.id,
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
        }
    };

    $scope.saveAgencySettings = function (agencySettings) {
        console.log(agencySettings);
        if (!$scope.agencyById) {
            var dialog = bootbox.dialog({
                title: 'Alert',
                message: '<p>Select Agency</p>'
            });
            dialog.init(function () {
                setTimeout(function () {
                    dialog.modal('hide');
                }, 2000);
            });
        } else {
            var data = {
                id: agencySettings.id,
                agencyId: $scope.agencyById.id,
                currencyId: agencySettings.currencyId,
                timeZoneId: agencySettings.timeZoneId
            };

            console.log(data)
            $http({method: agencySettings.id ? 'PUT' : 'POST', url: 'admin/user/agencySetting', data: data}).success(function (response) {
                console.log(data)
                $scope.agencySettings = response;
            });

            $scope.agencySetting = ""
        }
    };

    $scope.addAgencyUser = function () {
        $scope.agencyUser = "";
        $scope.showAgencyUserForm = true;
    };

    $scope.saveAgencyUser = function (agencyUser) {
        if (!$scope.agencyById) {
            var dialog = bootbox.dialog({
                title: 'Alert',
                message: '<p>Select Agency</p>'
            });
            dialog.init(function () {
                setTimeout(function () {
                    dialog.modal('hide');
                }, 2000);
            });
        } else {
            var agencyUserData = {
                id: agencyUser.id,
                firstName: agencyUser.firstName,
                lastName: agencyUser.lastName,
                userName: agencyUser.userName,
                email: agencyUser.email,
                password: agencyUser.password,
                primaryPhone: agencyUser.primaryPhone,
                secondaryPhone: agencyUser.secondaryPhone,
                agencyId: $scope.agencyById.id,
            };
            $http({method: agencyUser.id ? 'PUT' : 'POST', url: 'admin/ui/user', data: agencyUserData}).success(function (response) {
                getAgencyLicence($scope.agencyById);
                if (response.status == true) {
                    $scope.agencyUser = "";
                    $scope.showAgencyUserForm = false;
                } else {
                    var dialog = bootbox.dialog({
                        title: 'Alert',
                        message: response.message
                    });
                    dialog.init(function () {
                        setTimeout(function () {
                            dialog.modal('hide');
                        }, 2000);
                    });
                }
                console.log(response);
            });
        }
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
        var data = {
            id: agencyProduct.id,
            productName: agencyProduct.productName,
            icon: $scope.agencyProduct.icon,
            agencyId: $scope.agencyById.id,
            showProduct: agencyProduct.showProduct,
            templateId: $scope.templateId
        };
        $scope.agencyProduct = data;
        $scope.selectedRows = index;
        $scope.showAgencyProductForm = true;
    };

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
        console.log(agencyProduct.template)
        var agencyProductId = $scope.agencyById;
        if ($scope.agencyProduct.icon === "static/img/logos/deeta-logo.png") {
            $scope.agencyProduct.icon = "";
        }
        if (!$scope.agencyById) {
            var dialog = bootbox.dialog({
                title: 'Alert',
                message: '<p>Select Agency</p>'
            });
            dialog.init(function () {
                setTimeout(function () {
                    dialog.modal('hide');
                }, 2000);
            });
        } else {
            if (!$scope.agencyProduct.icon) {
                $scope.productIconEmptyMessage = "Product Icon Required";

            } else {
                var data = {
                    id: agencyProduct.id,
                    productName: agencyProduct.productName,
                    icon: $scope.agencyProduct.icon,
                    agencyId: $scope.agencyById.id,
                    showProduct: agencyProduct.showProduct,
                    templateId: agencyProduct.templateId
                };
                $http({method: agencyProduct.id ? 'PUT' : 'POST', url: 'admin/user/agencyProduct', data: data}).success(function (response) {
                    getAgencyLicence(agencyProductId);
                    $scope.showAgencyProductForm = false;
                });
                $scope.agencyProduct = {icon: "static/img/logos/deeta-logo.png"};
                $scope.productIconEmptyMessage = "";
            }
        }
    };
    $scope.clearAgencyProduct = function () {
        $scope.showAgencyProductForm = false;
        $scope.agencyProduct = {icon: "static/img/logos/deeta-logo.png"};
        $scope.productIconEmptyMessage = "";
    };
    $scope.selectedAgencyProduct = null;
    $scope.deleteAgencyProduct = function (agencyProduct, index) {
        $http({method: 'Delete', url: 'admin/user/agencyProduct/' + agencyProduct.id}).success(function (response) {
            $scope.agencyProducts.splice(index, 1);
        });
    }
    $scope.setAgencyProductRow = function (agencyProduct, index) {
        $scope.selectedAgencyProduct = index;
    }
    $scope.editAgencyProduct = function (agencyProduct) {
        var data = {
            id: agencyProduct.id,
            productName: agencyProduct.productName,
            icon: agencyProduct.icon,
            agencyId: $scope.agencyById.id,
            showProduct: agencyProduct.showProduct,
            templateId: agencyProduct.templateId
        };
        $scope.agencyProduct = data;
        $scope.showAgencyProductForm = true;
    }
    $scope.changeOrder = function (s) {
        var agencyProductId = $scope.agencyById;
        if (!agencyProductId) {
            return;
        }
        //console.log(s)
//if()
        var order = s.map(function (value, key) {
            if (value) {
                return value.id;
            }
        }).join(',');
        console.log(order)

//        if (order) {
        $http({method: 'GET', url: 'admin/user/productUpdateOrder/' + agencyProductId.id + "?productOrder=" + order});
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