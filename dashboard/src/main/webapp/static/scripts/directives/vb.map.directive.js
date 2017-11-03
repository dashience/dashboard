app.directive("mapDirective", function ($http, $stateParams) {
    return{
        restrict: 'AE',
        template: '<ng-map default-style="true" zoom="5" center="59.339025, 18.065818">' +
                '<info-window id="myInfoWindow">' +
                '<div ng-non-bindable>' +
                '<h4>{{selectedCity.name}}</h4>' +
                '</div>' +
                '</info-window>' +
                '<marker ng-repeat="map in mapCities" position="{{map.pos}}" title="{{map.name}}" id="{{map.id}}" on-click="showCity(event, map)">' +
                '</marker>' +
                '</ng-map>',
        scope: {
            mapCity: "@",
            widgetColumns: "@",
            mapSource: '@',
            widgetObj: '@'
        },
        link: function (scope, element, attr) {

//            console.log(scope.mapCity);
            scope.mapCities = JSON.parse(scope.mapCity);
//            console.log(scope.mapCities);

            //location popover
            scope.showCity = function (event, city) {
                scope.selectedCity = city;
                scope.map.showInfoWindow('myInfoWindow', this);
            };


            var mapDataSource = JSON.parse(scope.mapSource);
            if (scope.mapSource) {
                var url = "admin/proxy/getData?";

                var dataSourcePassword;
                if (mapDataSource.dataSourceId.password) {
                    dataSourcePassword = mapDataSource.dataSourceId.password;
                } else {
                    dataSourcePassword = '';
                }
                var getWidgetObj = JSON.parse(scope.widgetObj);
//                console.log(getWidgetObj);
                var setWidgetAccountId;
                var setProductSegment;
                var setTimeSegment;
                var setNetworkType;

                if (getWidgetObj.productSegment && getWidgetObj.productSegment.type) {
                    setProductSegment = getWidgetObj.productSegment.type;
                } else {
                    setProductSegment = getWidgetObj.productSegment;
                }

                if (getWidgetObj.timeSegment && getWidgetObj.timeSegment.type) {
                    setTimeSegment = getWidgetObj.timeSegment.type;
                } else {
                    setTimeSegment = getWidgetObj.timeSegment;
                }

                if (getWidgetObj.networkType && getWidgetObj.networkType.type) {
                    setNetworkType = getWidgetObj.networkType.type;
                } else {
                    setNetworkType = getWidgetObj.networkType;
                }
                var dashboardFilter;
                if (getWidgetObj.filterUrlParameter) {
                    dashboardFilter = JSON.stringify(getWidgetObj.filterUrlParameter)
                } else {
                    dashboardFilter = ""
                }

            }


            $http.get(url + 'connectionUrl=' + mapDataSource.dataSourceId.connectionString +
                    "&dataSetId=" + mapDataSource.id +
                    "&accountId=" + (getWidgetObj.accountId ? (getWidgetObj.accountId.id ? getWidgetObj.accountId.id : getWidgetObj.accountId) : $stateParams.accountId) +
                    "&userId=" + (mapDataSource.userId ? mapDataSource.userId.id : null) +
                    "&driver=" + mapDataSource.dataSourceId.sqlDriver +
                    "&productSegment=" + setProductSegment +
                    "&timeSegment=" + setTimeSegment +
                    "&networkType=" + setNetworkType +
                    "&dashboardFilter=" + encodeURI(dashboardFilter) +
                    "&startDate=" + $stateParams.startDate +
                    "&endDate=" + $stateParams.endDate +
                    '&username=' + mapDataSource.dataSourceId.userName +
                    "&dataSetReportName=" + mapDataSource.reportName +
                    '&password=' + dataSourcePassword +
                    '&widgetId=' + scope.widgetId +
                    '&url=' + mapDataSource.url +
                    '&port=3306&schema=vb&query=' + encodeURI(mapDataSource.query)).success(function (response) {

                scope.getValues = response.data;

                scope.getLocationValue = [];
                angular.forEach(scope.getValues, function (value, key) {
                    scope.locationValues = value;
                    scope.getLocationValue.push(value);
                });
                console.log(scope.getLocationValue);


                console.log(scope.widgetColumns);
                scope.locationNames = (JSON.parse(scope.widgetColumns));
                var location;
                var latitude;
                var longitude;
                angular.forEach(scope.locationNames, function (value, key) {
//                    console.log(value);
                    if (value.isLocation === 1) {
                        location = value.fieldName;
                    } else {
                         
                    }

                    if (value.isLatitude === 1) {
                        latitude = value.fieldName;
                    } else {

                    }

                    if (value.isLongitude === 1) {
                        longitude = value.fieldName;
                    } else {

                    }


                });

                console.log("Location:", location);
                console.log("Latitude:", latitude);
                console.log("Longitude:", longitude);
                
                var locationName = location;
                var latitudeName = latitude;
                var longitudeName = longitude;
                
                var fieldLocationName;
                var fieldLatitudeValue;
                var fieldLongitudeValue;

//                scope.count=0;
                angular.forEach(scope.getLocationValue, function (value) {
                    fieldLocationName = value[locationName];
                    fieldLatitudeValue = value[latitudeName];
                    fieldLongitudeValue = value[longitudeName];
                    scope.mapCities.push({"name": fieldLocationName, "pos": [fieldLatitudeValue,fieldLongitudeValue]})
                });
                
                console.log(locationName);
                console.log(fieldLatitudeValue);
                console.log(fieldLongitudeValue);
                
               
            });


        }
    };
});