app.controller('DataSourceConfigurationController', function ($scope, $http) {

    //Tabs
    $scope.tab = 1;

    $scope.setTab = function (newTab) {
        $scope.tab = newTab;
    };

    $scope.isSet = function (tabNum) {
        return $scope.tab === tabNum;
    };

     //DataSource
    $http.get('admin/ui/dataSource').success(function (response) {
        $scope.dataSources = response;
    });

    $scope.dataSets = [];
    $scope.addDataSet = function () {
        $scope.dataSets.push({isEdit: true});
    };


    $scope.levels = [];
    $scope.addLevel = function () {
        $scope.levels.push({isEdit: true});
    };

    $scope.segments = [];
    $scope.addSegment = function () {
        $scope.segments.push({isEdit: true});
    };

    $scope.frequencys = [];
    $scope.addFrequency = function () {
        $scope.frequencys.push({isEdit: true});
    };


    // Data Set Name display
    $scope.dataSourceSelect = function (dataSource) {
        var dataSourceId = dataSource.id;
        $scope.selectedDataSourceId = dataSourceId;

        getDataSetList(dataSourceId);
    };


    // Select Data Set displaying Level,Segment,Frequency
    $scope.dataSetSelect = function (dataSetConfig) {
        var dataSetConfigId = dataSetConfig.id;
        $scope.selectedDataSetId = dataSetConfigId;

        getLevelsByDataSet(dataSetConfigId);
        getSegmentsByDataSet(dataSetConfigId);
        getFrequencysByDataSet(dataSetConfigId);
    };


    // Getting Levels
    function getLevelsByDataSet(getDataSetConfigId) {
        var id = getDataSetConfigId;
        $http.get('admin/dataSourceConfigDataSet/dataSourceConfigLevel/' + id).success(function (response) {
            $scope.levels = response;
        });
    }

    // Getting Segments
    function getSegmentsByDataSet(getDataSetConfigId) {
        var id = getDataSetConfigId;
        $http.get('admin/dataSourceConfigDataSet/dataSourceConfigSegment/' + id).success(function (response) {
            $scope.segments = response;
        });

    }

    //Getting Frequency
    function getFrequencysByDataSet(getDataSetConfigId) {
        var id = getDataSetConfigId;
        $http.get('admin/dataSourceConfigDataSet/dataSourceConfigFrequency/' + id).success(function (response) {
            $scope.frequencys = response;
        });

    }

    // Getting Data Set
    function getDataSetList(dataSourceId) {
        var id = dataSourceId;
        $http.get('admin/dataSourceConfigDataSet/dataSourceDataSet/' + id).success(function (response) {
            $scope.dataSets = response;
        });

    }

    $scope.saveLevel = function (level) {
        var dataSetConfigId = $scope.selectedDataSetId;

        var data = {
            id: level.id,
            dataSetConfigId: dataSetConfigId,
            metrics: level.metrics,
            alias: level.alias,
            orderBy: level.orderBy
        };

        $http({method: level.id ? "PUT" : "POST", url: 'admin/dataSourceConfigDataSet/dataSourceConfigLevel', data: data}).success(function (response) {
            getLevelsByDataSet(dataSetConfigId);
        });
    };

    $scope.deleteLevel = function (level) {
        var dataSetConfigId = $scope.selectedDataSetId;
 
        $http({method: "DELETE", url: 'admin/dataSourceConfigDataSet/dataSourceConfigLevel/' + level.id}).success(function (response) {
            getLevelsByDataSet(dataSetConfigId);
        });
    };


    $scope.deleteFieldLevel = function (index) {
        $scope.levels.splice(index, 1);
    };

    $scope.saveSegment = function (segment) {
        var dataSetConfigId = $scope.selectedDataSetId;

        var data = {
            id: segment.id,
            dataSetConfigId: dataSetConfigId,
            metrics: segment.metrics,
            alias: segment.alias,
            orderBy: segment.orderBy
        };

        $http({method: segment.id ? "PUT" : "POST", url: 'admin/dataSourceConfigDataSet/dataSourceConfigSegment', data: data}).success(function (response) {
            getSegmentsByDataSet(dataSetConfigId);
        });
    };


    $scope.deleteSegment = function (segment) {
        var dataSetConfigId = $scope.selectedDataSetId;

        $http({method: "DELETE", url: 'admin/dataSourceConfigDataSet/dataSourceConfigSegment/' + segment.id}).success(function (response) {
            getSegmentsByDataSet(dataSetConfigId);
        });
    };


    $scope.deleteFieldSegment = function (index) {
        $scope.segments.splice(index, 1);
    };


    $scope.saveFrequency = function (frequency) {
        var dataSetConfigId = $scope.selectedDataSetId;

        var data = {
            id: frequency.id,
            dataSetConfigId: dataSetConfigId,
            metrics: frequency.metrics,
            alias: frequency.alias,
            orderBy: frequency.orderBy
        };

        $http({method: frequency.id ? "PUT" : "POST", url: 'admin/dataSourceConfigDataSet/dataSourceConfigFrequency', data: data}).success(function (response) {
            getFrequencysByDataSet(dataSetConfigId);
        });
    };

    $scope.deleteFrequency = function (frequency) {
        var dataSetConfigId = $scope.selectedDataSetId;
        
        $http({method: "DELETE", url: 'admin/dataSourceConfigDataSet/dataSourceConfigFrequency/' + frequency.id}).success(function (response) {
            getFrequencysByDataSet(dataSetConfigId);
        });
    };


    $scope.deleteFieldSegment = function (index) {
        $scope.segments.splice(index, 1);
    };

    $scope.saveDataSet = function (dataSet) {
        var dataSourceId = $scope.selectedDataSourceId;
        console.log(dataSet);
        var data = {
            id: dataSet.id,
            dataSetName: dataSet.dataSetName,
            dataSourceId: dataSourceId
        };

        $http({method: dataSet.id ? "PUT" : "POST", url: 'admin/dataSourceConfigDataSet/dataSourceDataSet', data: data}).success(function (response) {
            getDataSetList(dataSourceId);
        });
    };

    $scope.deleteDataSet = function (dataSet) {
        var dataSourceId = $scope.selectedDataSourceId;

        $http({method: "DELETE", url: 'admin/dataSourceConfigDataSet/dataSourceDataSet/' + dataSet.id}).success(function (response) {
            getDataSetList(dataSourceId);
        });
    };

    $scope.deleteFieldDataSet = function (index) {
        $scope.dataSets.splice(index, 1);
    };
    

});