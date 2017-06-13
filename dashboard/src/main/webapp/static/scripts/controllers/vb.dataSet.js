app.controller('DataSetController', function ($scope, $http, $stateParams, $filter, $timeout) {
    $scope.dataSetFlag = false;
    $scope.nwStatusFlag = false;
    $scope.timeSegFlag = false;
    $scope.startDate = $stateParams.startDate;
    $scope.endDate = $stateParams.endDate;
    $scope.tab = 1;
    $scope.setTab = function (newTab) {
        $scope.tab = newTab;
    };
    $scope.isSet = function (tabNum) {
        return $scope.tab === tabNum;
    };
    $scope.joinTypes = [
        {name: 'Left', value: 'left'},
        {name: 'Right', value: 'right'},
        {name: 'Inner', value: 'inner'},
        {name: 'Union', value: 'union'},
        {name: 'Intersection', value: 'intersection'}
    ];

    var url = "admin/proxy/getData?";

    function getPreviewDataSet(dataSet, selectType) {
        var dataSourcePassword;
        if (dataSet.dataSourceId.password) {
            dataSourcePassword = dataSet.dataSourceId.password;
        } else {
            dataSourcePassword = '';
        }
        $http.get(url + 'connectionUrl=' + dataSet.dataSourceId.connectionString +
                "&dataSourceId=" + dataSet.dataSourceId.id +
                "&dataSetId=" + dataSet.id +
                "&accountId=" + $stateParams.accountId +
                "&dataSetReportName=" + dataSet.reportName +
                "&timeSegment=" + dataSet.timeSegment +
                "&filter=" + dataSet.networkType +
                "&productSegment=" + dataSet.productSegment +
                "&driver=" + dataSet.dataSourceId.dataSourceType +
                "&dataSourceType=" + dataSet.dataSourceId.dataSourceType +
                "&location=" + $stateParams.locationId +
                "&startDate=" + $stateParams.startDate +
                "&endDate=" + $stateParams.endDate +
                '&username=' + dataSet.dataSourceId.userName +
                '&password=' + dataSourcePassword +
                '&url=' + dataSet.url +
                '&port=3306&schema=deeta_dashboard&query=' + encodeURI(dataSet.query)).success(function (response) {
            console.log(response);
            if (selectType == "dataSet1") {
                $scope.firstDataSetLoading = false;
                $scope.firstDataSetLoadingCompleted = true;
                $scope.firstDataSetColumns = response.columnDefs;
                $scope.firstDataSetRows = response.data;
            } else if (selectType == "dataSet2") {
                $scope.secondDataSetLoading = false;
                $scope.secondDataSetLoadingCompleted = true;
                $scope.secondDataSetColumns = response.columnDefs;
                $scope.secondDataSetRows = response.data;
            } else {
                return; //response;
            }
        });


    }
    $scope.selectFirstDataSet = function (dataSet) {

        $scope.firstDataSet = JSON.parse(dataSet.firstDataSet);
        $scope.firstDataSetName = $scope.firstDataSet.name;
        $scope.dataSetIdFirst = $scope.firstDataSet.id;
        $scope.firstDataSetLoadingCompleted = false;
        $scope.firstDataSetLoading = true;
        getPreviewDataSet($scope.firstDataSet, "dataSet1");

    };
    $scope.selectSecondDataSet = function (dataSet) {
        $scope.secondDataSet = JSON.parse(dataSet.secondDataSet);
        $scope.secondDataSetName = $scope.secondDataSet.name;
        $scope.dataSetIdSecond = $scope.secondDataSet.id;
        $scope.secondDataSetLoadingCompleted = false;
        $scope.secondDataSetLoading = true;
        getPreviewDataSet($scope.secondDataSet, "dataSet2");
    };
    $scope.dataSetColumnList = [];
    $scope.joinDataSetList = [];
    $scope.hideCondition = false;
    $scope.selectJoinType = function (joinDataSetColumn) {
        $scope.operationType = joinDataSetColumn.joinType;
        console.log($scope.operationType);
        if (joinDataSetColumn.joinType != null) {
            $scope.hideCondition = true;
        }
    };

    $scope.addJoinColumnList = function () {
        $scope.dataSetColumnList.push({});
    };
    $scope.removeJoinDataSetColumn = function (index, conditionId) {
        console.log(conditionId)
        $scope.dataSetColumnList.splice(index, 1);

        $http({method: 'DELETE', url: 'admin/ui/deleteJoinDataSetCondition/' + conditionId + "/" + joinDataSetId}).success(function (response) {
            console.log(response)
            $scope.joinDataSetList = response;
        });
        console.log($scope.joinDataSetList)
        console.log($scope.dataSetColumnList)
    }
    var joinDataSetId = null;


    $scope.loadingResultCompleted = false;
    $scope.loadingResult = false;
    $scope.saveJoinDataSet = function (joinDataSetColumn) {
        $scope.dataSetLists = [];
        $scope.loadingResultCompleted = false;
        $scope.loadingResult = true;
        $scope.errorHide = false;
        var dataSetIdFirst = JSON.parse(joinDataSetColumn.firstDataSet).id;
        var dataSetIdSecond = JSON.parse(joinDataSetColumn.secondDataSet).id;
        if (joinDataSetId != null) {
            console.log($scope.joinDataSetList);
            for (i = 0; i < $scope.dataSetColumnList.length; i++) {
                var conditionId = null;
                if (typeof ($scope.joinDataSetList[i]) !== "undefined") {
                    conditionId = $scope.joinDataSetList[i].id;
                }
                console.log(conditionId)
                var conditionData = {
                    conditionFieldFirst: $scope.dataSetColumnList[i].conditionFieldFirst,
                    conditionFieldSecond: $scope.dataSetColumnList[i].conditionFieldSecond,
                    columnName: $scope.dataSetColumnList[i].columnName,
                    conditionId: conditionId
                };
                $scope.dataSetLists.push(conditionData);
                console.log($scope.dataSetLists)
            }
            $scope.dataSetColumnList = $scope.dataSetLists;
            console.log($scope.dataSetColumnList)
        }
        var data = {
            id: joinDataSetId,
            dataSetName: joinDataSetColumn.joinDataSetName,
            dataSetIdFirst: dataSetIdFirst,
            dataSetIdSecond: dataSetIdSecond,
            operationType: joinDataSetColumn.joinType,
            conditionFields: $scope.dataSetColumnList
        };
        console.log(data);
        $http({method: 'POST', url: 'admin/ui/joinDataSet', data: data}).success(function (response) {
            console.log(response);
            $scope.joinDataSetList = response;
            joinDataSetId = response[0].joinDataSetId.id;
            $scope.joinDataSetNewName = response[0].joinDataSetId.dataSetName;
            $http.get(url + 'joinDataSetId=' + response[0].joinDataSetId.id +
                    "&accountId=" + $stateParams.accountId +
                    "&location=" + $stateParams.locationId +
                    "&startDate=" + $stateParams.startDate +
                    "&endDate=" + $stateParams.endDate).success(function (response) {
                $scope.loadingResult = false;
                $scope.loadingResultCompleted = true;

                $scope.joinColumns = response.columnDefs;
                $scope.joinRows = response.data;
                if (response.columnDefs == "" || response.data == "") {
                    $scope.errorHide = true;
                    $scope.errorMessage = "No Data Found";
                }
                console.log(response.columnDefs);
                console.log(response.data);
            });
        });
    };
    $scope.cancelJoinDataSet = function (joinDataSetColumn) {
        $scope.joinDataSetColumn = "";
//        $scope.dataSetColumn = "";
        $scope.dataSetColumnList = [];
        $scope.hideCondition = false;
        $scope.secondDataSetLoadingCompleted = false;
        $scope.firstDataSetLoadingCompleted = false;
        $scope.loadingResultCompleted = false;
        $scope.loadingResult = false;
    }
    $scope.saveToDataSet = function (joinDataSetColumn) {
        var dataSet = JSON.parse(joinDataSetColumn.firstDataSet);
        var joinDataSetList = {
            agencyId: dataSet.agencyId,
            name: joinDataSetColumn.joinDataSetName,
            userId: dataSet.userId,
            joinDataSetId: joinDataSetId
        };
        var data = joinDataSetList;
        $http({method: 'POST', url: 'admin/ui/dataSet', data: data}).success(function (response) {
            getItems();
        });
    };
    /*
     * 
     * All
     search
     unknown
     content
     youtubeSearch
     youtubeWatch
     
     */
    $scope.networkTypes = [
        {
            type: 'SEARCH',
            name: 'Search'
        },
        {
            type: 'CONTENT',
            name: 'Content'
        },
        {
            type: 'YOUTUBE_SEARCH',
            name: 'Youtube Search'
        },
        {
            type: 'YOUTUBE_WATCH',
            name: 'Youtube Watch'
        }
    ];
    $scope.dataSetFlagValidation = function (dataSource)
    {
        if (dataSource === "adwords")
        {
            $scope.report = $scope.adwordsPerformance;
            $scope.dataSetFlag = true;
            $scope.nwStatusFlag = true;
            $scope.timeSegFlag = true;
        } else if (dataSource === "analytics")
        {
            $scope.report = $scope.analyticsPerformance;
            $scope.dataSetFlag = true;
            $scope.nwStatusFlag = false;
            $scope.timeSegFlag = true;
        } else if (dataSource === "facebook")
        {
            $scope.report = $scope.facebookPerformance;
            console.log($scope.report);
            $scope.dataSetFlag = true;
            $scope.nwStatusFlag = false;
        } else if (dataSource === "pinterest")
        {
            $scope.report = $scope.pinterestPerformance;
            console.log($scope.report);
            $scope.dataSetFlag = true;
            $scope.nwStatusFlag = false;
            $scope.timeSegFlag = false;
            $scope.productSegFlag = false;

        } else if (dataSource === "instagram")
        {
            $scope.report = $scope.instagramPerformance;
            $scope.dataSetFlag = true;
            $scope.nwStatusFlag = false;
            $scope.timeSegFlag = false;
        } else if (dataSource === "pinterest")
        {
            $scope.report = $scope.pinterestPerformance;
            $scope.dataSetFlag = true;
            $scope.nwStatusFlag = false;
            $scope.timeSegFlag = false;
        } else if (dataSource === "linkedin")
        {
            $scope.report = $scope.linkedinPerformance;
            $scope.dataSetFlag = true;
            $scope.nwStatusFlag = false;
            $scope.timeSegFlag = false;
            $scope.productSegFlag = false;
        } else if (dataSource === "bing")
        {
            $scope.report = $scope.bingPerformance;
            $scope.dataSetFlag = true;
            $scope.nwStatusFlag = false;
        } else {
            $scope.dataSetFlag = false;
            $scope.nwStatusFlag = false;
        }
    };
    $scope.linkedinPerformance = [
        {
            type: 'organic',
            name: 'Organic Report',
            timeSegments: [],
            productSegments: []
        }
    ];
    $scope.pinterestPerformance = [
        {
            type: 'getTopBoards',
            name: 'getTopBoards'
        }, {
            type: 'getTopPins',
            name: 'getTopPins'
        }, {
            type: 'getOrganicData',
            name: 'getOrganicData'
        }
    ]

    $scope.bingPerformance = [
        {
            type: 'accountPerformance',
            name: 'Account Performance',
            timeSegments: [
                {
                    type: 'daily',
                    name: 'Daily'
                },
                {
                    type: 'monthly',
                    name: 'Monthly'
                },
                {
                    type: 'weekly',
                    name: 'Weekly'
                },
                {
                    type: 'dayOfWeek',
                    name: 'Day Of Week'
                },
                {
                    type: 'hourOfDay',
                    name: 'Hour Of Day'
                },
                {
                    type: 'none',
                    name: 'None'
                }
            ],
            productSegments: [
                {
                    type: 'device',
                    name: 'Device'
                },
                {
                    type: 'none',
                    name: 'None'
                }
            ]
        },
        {
            type: 'campaignPerformance',
            name: 'Campaign Performance',
            timeSegments: [
                {
                    type: 'daily',
                    name: 'Daily'
                },
                {
                    type: 'monthly',
                    name: 'Monthly'
                },
                {
                    type: 'weekly',
                    name: 'Weekly'
                },

                {
                    type: 'none',
                    name: 'None'
                }
            ],
            productSegments: [
                {
                    type: 'device',
                    name: 'Device'
                },
                {
                    type: 'none',
                    name: 'None'
                }
            ]
        },
        {
            type: 'adGroupPerformance',
            name: 'AdGroup Performance',
            timeSegments: [
                {
                    type: 'daily',
                    name: 'Daily'
                },
                {
                    type: 'monthly',
                    name: 'Monthly'
                },
                {
                    type: 'weekly',
                    name: 'Weekly'
                },
                {
                    type: 'none',
                    name: 'None'
                }
            ],
            productSegments: [
                {
                    type: 'none',
                    name: 'None'
                }
            ]
        },
        {
            type: 'adPerformance',
            name: 'Ad Performance',
            timeSegments: [
                {
                    type: 'daily',
                    name: 'Daily'
                },
                {
                    type: 'monthly',
                    name: 'Monthly'
                },
                {
                    type: 'weekly',
                    name: 'Weekly'
                },
                {
                    type: 'none',
                    name: 'None'
                }
            ],
            productSegments: [
                {
                    type: 'none',
                    name: 'None'
                }
            ]
        },
        {
            type: 'geoPerformance',
            name: 'Geo Performance',
            timeSegments: [
                {
                    type: 'none',
                    name: 'None'
                }
            ],
            productSegments: [

                {
                    type: 'zip',
                    name: 'Zip'
                },
                {
                    type: 'city',
                    name: 'City'
                },
            ]
        },
    ];
    $scope.facebookPerformance = [
        {
            type: 'accountPerformance',
            name: 'accountPerformance',
            timeSegments: [
                {
                    type: 'day',
                    name: 'day'
                },
                {
                    type: 'week',
                    name: 'week'
                },
                {
                    type: 'month',
                    name: 'month'
                },
                {
                    type: 'none',
                    name: 'None'
                }
            ],
            productSegments: [
                {
                    type: 'device',
                    name: 'Device'
                },
                {
                    type: 'none',
                    name: 'None'
                }
            ]
        },
        {
            type: 'campaignPerformance',
            name: 'campaignPerformance',
            timeSegments: [
                {
                    type: 'none',
                    name: 'None'
                }
            ],
            productSegments: [
                {
                    type: 'device',
                    name: 'device'
                },
                {
                    type: 'none',
                    name: 'None'
                }
            ]
        },
        {
            type: 'adPerformance',
            name: 'adPerformance',
            timeSegments: [
                {
                    type: 'none',
                    name: 'None'
                }
            ],
            productSegments: [
                {
                    type: 'none',
                    name: 'None'
                }
            ]
        },
        {
            type: 'adSetPerformance',
            name: 'adSetPerformance',
            timeSegments: [
                {
                    type: 'none',
                    name: 'None'
                }
            ],
            productSegments: [
                {
                    type: 'none',
                    name: 'None'
                }
            ]
        },
        {
            type: 'agePerformance',
            name: 'agePerformance',
            timeSegments: [
                {
                    type: 'none',
                    name: 'None'
                }
            ],
            productSegments: [
                {
                    type: 'age',
                    name: 'age'
                },
                {
                    type: 'none',
                    name: 'None'
                }
            ]
        }, {
            type: 'genderPerformance',
            name: 'genderPerformance',
            timeSegments: [
                {
                    type: 'none',
                    name: 'None'
                }
            ],
            productSegments: [
                {
                    type: 'gender',
                    name: 'gender'
                },
                {
                    type: 'none',
                    name: 'None'
                }
            ]
        }, {
            type: 'postPerformance',
            name: 'postPerformance',
            timeSegments: [
                {
                    type: 'none',
                    name: 'None'
                }
            ],
            productSegments: [
                {
                    type: 'none',
                    name: 'None'
                }
            ]
        },
        {
            type: 'engagements',
            name: 'Engagements',
            timeSegments: [
                {
                    type: 'none',
                    name: 'None'
                }
            ],
            productSegments: [
                {
                    type: 'none',
                    name: 'None'
                }
            ]
        }, {
            type: 'reach',
            name: 'Reach',
            timeSegments: [
                {
                    type: 'none',
                    name: 'None'
                }
            ],
            productSegments: [
                {
                    type: 'none',
                    name: 'None'
                }
            ]
        }, {
            type: 'pageLikes',
            name: 'pageLikes',
            timeSegments: [
                {
                    type: 'none',
                    name: 'None'
                }
            ],
            productSegments: [
                {
                    type: 'city',
                    name: 'City'
                },
                {
                    type: 'none',
                    name: 'None'
                }
            ]
        }, {
            type: 'pageViews',
            name: 'pageViews',
            timeSegments: [
                {
                    type: 'day',
                    name: 'day'
                },
                {
                    type: 'none',
                    name: 'None'
                }
            ],
            productSegments: [
                {
                    type: 'device',
                    name: 'device'
                },
                {
                    type: 'gender',
                    name: 'gender'
                },
                {
                    type: 'none',
                    name: 'None'
                }
            ]
        }, {
            type: 'pageReactions',
            name: 'pageReactions',
            timeSegments: [
                {
                    type: 'day',
                    name: 'day'
                },
                {
                    type: 'none',
                    name: 'None'
                }
            ],
            productSegments: [
                {
                    type: 'none',
                    name: 'None'
                }
            ]
        },
    ];
    $scope.instagramPerformance = [
        {
            type: 'instagramPerformance',
            name: 'instagramPerformance',
            timeSegments: [
                {
                    type: 'day',
                    name: 'day'
                },
                {
                    type: 'week',
                    name: 'week'
                },
                {
                    type: 'month',
                    name: 'month'
                },
                {
                    type: 'year',
                    name: 'year'
                }
            ],
            productSegments: [
                {
                    type: 'device',
                    name: 'device'
                },
                {
                    type: 'none',
                    name: 'none'
                }
            ]
        }
    ];
//accountPerformance
    $scope.analyticsPerformance = [
        {
            type: 'overallPerformance',
            name: 'Overall Performance',
            timeSegments: [
                {
                    type: 'ga:date',
                    name: 'Day'
                },
                {
                    type: 'ga:isoYearIsoWeek',
                    name: 'Week'
                },
                {
                    type: 'ga:yearMonth',
                    name: 'Month'
                },
                {
                    type: 'ga:year',
                    name: 'Year'
                },
                {
                    type: 'ga:dateHour',
                    name: 'Hour of Week'
                },
                {
                    type: 'ga:dayOfWeekName',
                    name: 'Day of Week'
                },
                {
                    type: 'none',
                    name: 'None'
                }
            ],
            productSegments: [
                {
                    type: 'ga:browser',
                    name: 'Browser'
                },
                {
                    type: 'ga:operatingSystem',
                    name: 'OS'
                },
                {
                    type: 'ga:deviceCategory',
                    name: 'Device'
                },
                {
                    type: 'ga:source',
                    name: 'Source'
                },
                {
                    type: 'ga:previousPagePath',
                    name: 'Previous Page Path'
                },
                {
                    type: 'ga:secondPagePath',
                    name: 'Next Page Path'
                },
                {
                    type: 'ga:city',
                    name: 'City'
                },
                {
                    type: 'ga:country',
                    name: 'Country'
                },
                {
                    type: 'ga:metro',
                    name: 'Metro'
                },
                {
                    type: 'ga:channelGrouping',
                    name: 'ChannelGrouping'
                },
                {
                    type: 'none',
                    name: 'None'
                }
            ]
        },
        {
            type: 'referralReport',
            name: 'Referral Report',
            timeSegments: [
                {
                    type: 'ga:date',
                    name: 'Day'
                },
                {
                    type: 'ga:isoYearIsoWeek',
                    name: 'Week'
                },
                {
                    type: 'ga:yearMonth',
                    name: 'Month'
                },
                {
                    type: 'ga:year',
                    name: 'Year'
                },
                {
                    type: 'none',
                    name: 'None'
                }
            ],
            productSegments: [
                {
                    type: 'ga:browser',
                    name: 'Browser'
                },
                {
                    type: 'ga:operatingSystem',
                    name: 'OS'
                },
                {
                    type: 'ga:deviceCategory',
                    name: 'Device'
                },
                {
                    type: 'ga:source',
                    name: 'Source'
                },
                {
                    type: 'ga:channelGrouping',
                    name: 'ChannelGrouping'
                },
                {
                    type: 'none',
                    name: 'None'
                }
            ]
        },
        {
            type: 'pagePerformance',
            name: 'Page Performance',
            timeSegments: [
                {
                    type: 'ga:date',
                    name: 'Day'
                },
                {
                    type: 'ga:isoYearIsoWeek',
                    name: 'Week'
                },
                {
                    type: 'ga:yearMonth',
                    name: 'Month'
                },
                {
                    type: 'ga:year',
                    name: 'Year'
                },
                {
                    type: 'none',
                    name: 'None'
                }
            ],
            productSegments: [
                {
                    type: 'ga:browser',
                    name: 'Browser'
                },
                {
                    type: 'ga:operatingSystem',
                    name: 'OS'
                },
                {
                    type: 'ga:deviceCategory',
                    name: 'Device'
                },
                {
                    type: 'ga:source',
                    name: 'Source'
                },
                {
                    type: 'ga:city',
                    name: 'City'
                },
                {
                    type: 'ga:country',
                    name: 'Country'
                },
                {
                    type: 'ga:region',
                    name: 'Region'
                },
                {
                    type: 'ga:metro',
                    name: 'Metro'
                },
                {
                    type: 'ga:channelGrouping',
                    name: 'ChannelGrouping'
                },
                {
                    type: 'none',
                    name: 'None'
                }
            ]
        },
        {
            type: 'visitorsTypeReport',
            name: 'Visitors Type Report',
            timeSegments: [
                {
                    type: 'ga:date',
                    name: 'Day'
                },
                {
                    type: 'ga:isoYearIsoWeek',
                    name: 'Week'
                },
                {
                    type: 'ga:yearMonth',
                    name: 'Month'
                },
                {
                    type: 'ga:year',
                    name: 'Year'
                },
                {
                    type: 'none',
                    name: 'None'
                }
            ],
            productSegments: [
                {
                    type: 'none',
                    name: 'None'
                }
            ]
        },
        {
            type: 'frequencyReport',
            name: 'Frequency Report',
            timeSegments: [
                {
                    type: 'ga:date',
                    name: 'Day'
                },
                {
                    type: 'ga:isoYearIsoWeek',
                    name: 'Week'
                },
                {
                    type: 'ga:yearMonth',
                    name: 'Month'
                },
                {
                    type: 'ga:year',
                    name: 'Year'
                },
                {
                    type: 'none',
                    name: 'None'
                }
            ],
            productSegments: [
                {
                    type: 'none',
                    name: 'None'
                }
            ]
        },
        {
            type: 'goalsReport',
            name: 'Goals Report',
            timeSegments: [
                {
                    type: 'ga:date',
                    name: 'Day'
                },
                {
                    type: 'ga:isoYearIsoWeek',
                    name: 'Week'
                },
                {
                    type: 'ga:YearMonth',
                    name: 'Month'
                },
                {
                    type: 'ga:year',
                    name: 'Year'
                },
                {
                    type: 'ga:dayOfWeek',
                    name: 'Day of Week'
                },
                {
                    type: 'ga:dateHour',
                    name: 'Hour of Day'
                },
                {
                    type: 'none',
                    name: 'None'
                }
            ],
            productSegments: [
                {
                    type: 'ga:browser',
                    name: 'Browser'
                },
                {
                    type: 'ga:operatingSystem',
                    name: 'OS'
                },
                {
                    type: 'ga:deviceCategory',
                    name: 'Device'
                },
                {
                    type: 'ga:source',
                    name: 'Source'
                },
                {
                    type: 'ga:city',
                    name: 'City'
                },
                {
                    type: 'ga:country',
                    name: 'Country'
                },
                {
                    type: 'ga:region',
                    name: 'Region'
                },
                {
                    type: 'ga:metro',
                    name: 'Metro'
                },
                {
                    type: 'ga:channelGrouping',
                    name: 'ChannelGrouping'
                },
                {
                    type: 'none',
                    name: 'None'
                }
            ]
        },
        {
            type: 'pageGoalsReport',
            name: 'Page Goal Report',
            timeSegments: [
                {
                    type: 'ga:date',
                    name: 'Day'
                },
                {
                    type: 'ga:isoYearIsoWeek',
                    name: 'Week'
                },
                {
                    type: 'ga:YearMonth',
                    name: 'Month'
                },
                {
                    type: 'ga:year',
                    name: 'Year'
                },
                {
                    type: 'none',
                    name: 'None'
                }
            ],
            productSegments: [
                {
                    type: 'ga:browser',
                    name: 'Browser'
                },
                {
                    type: 'ga:operatingSystem',
                    name: 'OS'
                },
                {
                    type: 'ga:deviceCategory',
                    name: 'Device'
                },
                {
                    type: 'ga:source',
                    name: 'Source'
                },
                {
                    type: 'ga:channelGrouping',
                    name: 'ChannelGrouping'
                },
                {
                    type: 'none',
                    name: 'None'
                }
            ]
        },
        {
            type: 'eventsReport',
            name: 'Events Report',
            timeSegments: [
                {
                    type: 'ga:date',
                    name: 'Day'
                },
                {
                    type: 'ga:isoYearIsoWeek',
                    name: 'Week'
                },
                {
                    type: 'ga:YearMonth',
                    name: 'Month'
                },
                {
                    type: 'ga:year',
                    name: 'Year'
                },
                {
                    type: 'ga:dayOfWeek',
                    name: 'Day of Week'
                },
                {
                    type: 'none',
                    name: 'None'
                }
            ],
            productSegments: [
                {
                    type: 'ga:browser',
                    name: 'Browser'
                },
                {
                    type: 'ga:operatingSystem',
                    name: 'OS'
                },
                {
                    type: 'ga:deviceCategory',
                    name: 'Device'
                },
                {
                    type: 'ga:source',
                    name: 'Source'
                },
                {
                    type: 'ga:eventLabel',
                    name: 'EventLabel'
                },
                {
                    type: 'ga:eventAction',
                    name: 'EventAction'
                },
                {
                    type: 'ga:channelGrouping',
                    name: 'ChannelGrouping'
                },
                {
                    type: 'none',
                    name: 'None'
                }
            ]
        }
    ];
    $scope.adwordsPerformance = [
        {
            type: 'accountPerformance',
            name: 'Account',
            timeSegments: [
                {
                    type: 'Date',
                    name: 'Day'
                },
                {
                    type: 'Week',
                    name: 'Week'
                },
                {
                    type: 'Month',
                    name: 'Month'
                },
                {
                    type: 'Year',
                    name: 'Year'
                },
                {
                    type: 'Quarter',
                    name: 'Quarter'
                },
                {
                    type: 'HourOfDay',
                    name: 'Hour Of The Day'
                },
                {
                    type: 'DayOfWeek',
                    name: 'Day Of Week'
                },
                {
                    type: 'none',
                    name: 'None'
                }
            ],
            productSegments: [
                {
                    type: 'Device',
                    name: 'Device'
                },
                {
                    type: 'AdNetworkType1',
                    name: 'Network Search Partner'
                },
                {
                    type: 'none',
                    name: 'None'
                }
            ]
        },
        {
            type: 'campaignPerformance',
            name: 'Campaign',
            timeSegments: [
                {
                    type: 'Date',
                    name: 'Day'
                },
                {
                    type: 'Week',
                    name: 'Week'
                },
                {
                    type: 'Month',
                    name: 'Month'
                },
                {
                    type: 'Year',
                    name: 'Year'
                },
                {
                    type: 'Quarter',
                    name: 'Quarter'
                },
                {
                    type: 'HourOfDay',
                    name: 'Hour Of The Day'
                },
                {
                    type: 'DayOfWeek',
                    name: 'Day Of Week'
                },
                {
                    type: 'none',
                    name: 'None'
                }
            ],
            productSegments: [
                {
                    type: 'Device',
                    name: 'Device'
                },
                {
                    type: 'AdNetworkType1',
                    name: 'Network Search Partner'
                },
                {
                    type: 'none',
                    name: 'None'
                }
            ]
        },
        {
            type: 'adGroupPerformance',
            name: 'AdGroup',
            timeSegments: [
                {
                    type: 'Date',
                    name: 'Day'
                },
                {
                    type: 'Week',
                    name: 'Week'
                },
                {
                    type: 'Month',
                    name: 'Month'
                },
                {
                    type: 'Year',
                    name: 'Year'
                },
                {
                    type: 'Quarter',
                    name: 'Quarter'
                },
                {
                    type: 'none',
                    name: 'None'
                }
            ],
            productSegments: [
                {
                    type: 'Device',
                    name: 'Device'
                },
                {
                    type: 'AdNetworkType1',
                    name: 'Network Search Partner'
                },
                {
                    type: 'none',
                    name: 'None'
                }
            ]
        },
        {
            type: 'keywordPerformance',
            name: 'Keyword',
            timeSegments: [
                {
                    type: 'Date',
                    name: 'Day'
                },
                {
                    type: 'Week',
                    name: 'Week'
                },
                {
                    type: 'Month',
                    name: 'Month'
                },
                {
                    type: 'Year',
                    name: 'Year'
                },
                {
                    type: 'Quarter',
                    name: 'Quarter'
                },
                {
                    type: 'none',
                    name: 'None'
                }
            ],
            productSegments: [
                {
                    type: 'Device',
                    name: 'Device'
                },
                {
                    type: 'AdNetworkType1',
                    name: 'Network Search Partner'
                },
                {
                    type: 'none',
                    name: 'None'
                }
            ]
        },
        {
            type: 'adPerformance',
            name: 'AdCopy',
            timeSegments: [
                {
                    type: 'Date',
                    name: 'Day'
                },
                {
                    type: 'Week',
                    name: 'Week'
                },
                {
                    type: 'Month',
                    name: 'Month'
                },
                {
                    type: 'Year',
                    name: 'Year'
                },
                {
                    type: 'Quarter',
                    name: 'Quarter'
                },
                {
                    type: 'none',
                    name: 'None'
                }
            ],
            productSegments: [
                {
                    type: 'Device',
                    name: 'Device'
                },
                {
                    type: 'AdNetworkType1',
                    name: 'Network Search Partner'
                },
                {
                    type: 'none',
                    name: 'None'
                }
            ]
        },
        {
            type: 'searchQueryReport',
            name: 'Search Query Report',
            timeSegments: [
                {
                    type: 'Date',
                    name: 'Day'
                },
                {
                    type: 'Week',
                    name: 'Week'
                },
                {
                    type: 'Month',
                    name: 'Month'
                },
                {
                    type: 'Year',
                    name: 'Year'
                },
                {
                    type: 'Quarter',
                    name: 'Quarter'
                },
                {
                    type: 'none',
                    name: 'None'
                }
            ],
            productSegments: [
                {
                    type: 'none',
                    name: 'None'
                }
            ]
        },
        {
            type: 'finalUrlReport',
            name: 'Final URL Report',
            timeSegments: [
                {
                    type: 'Day',
                    name: 'Day'
                },
                {
                    type: 'Week',
                    name: 'Week'
                },
                {
                    type: 'Month',
                    name: 'Month'
                },
                {
                    type: 'Year',
                    name: 'Year'
                },
                {
                    type: 'Quarter',
                    name: 'Quarter'
                },
                {
                    type: 'none',
                    name: 'None'
                }
            ],
            productSegments: [
                {
                    type: 'none',
                    name: 'None'
                }
            ]
        },
        {
            type: 'placementReport',
            name: 'Placement Report',
            timeSegments: [
                {
                    type: 'Date',
                    name: 'Day'
                },
                {
                    type: 'Week',
                    name: 'Week'
                },
                {
                    type: 'Month',
                    name: 'Month'
                },
                {
                    type: 'Year',
                    name: 'Year'
                },
                {
                    type: 'Quarter',
                    name: 'Quarter'
                },
                {
                    type: 'none',
                    name: 'None'
                }
            ],
        },
        {
            type: 'geoPerformance',
            name: 'Geo',
            timeSegments: [
                {
                    type: 'Day',
                    name: 'Day'
                },
                {
                    type: 'Week',
                    name: 'Week'
                },
                {
                    type: 'Month',
                    name: 'Month'
                },
                {
                    type: 'Year',
                    name: 'Year'
                },
                {
                    type: 'DayOfWeek',
                    name: 'Day Of Week'
                },
                {
                    type: 'Quarter',
                    name: 'Quarter'
                },
                {
                    type: 'RegionCriteriaId',
                    name: 'Region'
                },
                {
                    type: 'CityCriteriaId,RegionCriteriaId',
                    name: 'City'
                },
                {
                    type: 'MetroCriteriaId,RegionCriteriaId,CityCriteriaId',
                    name: 'Metro Area'
                },
                {
                    type: 'CityCriteriaId,RegionCriteriaId,MostSpecificCriteriaId',
                    name: 'Zip'
                },
                {
                    type: 'none',
                    name: 'None'
                }
            ],
            productSegments: [
                {
                    type: 'Device',
                    name: 'Device'
                },
                {
                    type: 'AdNetworkType1',
                    name: 'Network Search Partner'
                },
                {
                    type: 'none',
                    name: 'None'
                }
            ]
        }, {
            type: 'videoPerformance',
            name: 'Video',
            timeSegments: [
                {
                    type: 'Date',
                    name: 'Day'
                },
                {
                    type: 'Week',
                    name: 'Week'
                },
                {
                    type: 'Month',
                    name: 'Month'
                },
                {
                    type: 'Year',
                    name: 'Year'
                },
                {
                    type: 'Quarter',
                    name: 'Quarter'
                },
                {
                    type: 'none',
                    name: 'None'
                }
            ],
            productSegments: [
                {
                    type: 'Device',
                    name: 'Device'
                },
                {
                    type: 'AdNetworkType1',
                    name: 'Network Search Partner'
                },
                {
                    type: 'none',
                    name: 'None'
                }
            ]
        }
    ];
    $scope.getTimeSegements = function (dataSet) {

        if ($scope.dataSet.dataSourceId.dataSourceType == "instagram")
        {
            var index = getIndex($scope.dataSet.reportName, $scope.instagramPerformance);
            $scope.timeSegment = $scope.instagramPerformance[index].timeSegments;
            $scope.productSegment = $scope.instagramPerformance[index].productSegments;
            $scope.nwStatusFlag = true;
        }
//        if ($scope.dataSet.dataSourceId.dataSourceType == "facebook")
//        {
//            var index = getIndex($scope.dataSet.reportName, $scope.facebookPerformance);
//            $scope.timeSegment = $scope.facebookPerformance[index].timeSegments;
//            $scope.productSegment = $scope.facebookPerformance[index].productSegments;
//            $scope.nwStatusFlag = false;
//        }

        if ($scope.dataSet.dataSourceId.dataSourceType == "facebook")
        {
            var index = getIndex($scope.dataSet.reportName, $scope.facebookPerformance);
            $scope.timeSegment = $scope.facebookPerformance[index].timeSegments;
            $scope.productSegment = $scope.facebookPerformance[index].productSegments;

            var productList = $scope.productSegment;
            var productSegmentName = dataSet.productSegment;

            var timeSegmentList = $scope.timeSegment;
            var timeSegmentName = dataSet.timeSegment;

            if ($scope.dataSet.reportName !== "") {
                $scope.nwStatusFlag = false;
                $scope.timeSegFlag = true;
                $scope.productSegFlag = true;
            }

            if ($scope.dataSet.reportName == 'agePerformance') {
                if (!dataSet.timeSegment) {
                    $scope.dataSet.timeSegment = {name: 'None', type: 'none'};
                } else {
                    getTimeSegment(timeSegmentList, timeSegmentName);
                }
                if (!dataSet.productSegment) {
                    $scope.dataSet.productSegment = {name: 'None', type: 'none'};
                } else {
                    getProductSegment(productList, productSegmentName);
                }
            }
            if ($scope.dataSet.reportName == 'genderPerformance') {
                if (!dataSet.timeSegment) {
                    $scope.dataSet.timeSegment = {name: 'None', type: 'none'};
                } else {
                    getTimeSegment(timeSegmentList, timeSegmentName);
                }
                if (!dataSet.productSegment) {
                    $scope.dataSet.productSegment = {name: 'None', type: 'none'};
                } else {
                    getProductSegment(productList, productSegmentName);
                }
            }
            if ($scope.dataSet.reportName == 'pageReactions') {
                if (!dataSet.timeSegment) {
                    $scope.dataSet.timeSegment = {name: 'None', type: 'none'};
                } else {
                    getTimeSegment(timeSegmentList, timeSegmentName);
                }
                if (!dataSet.productSegment) {
                    $scope.dataSet.productSegment = {name: 'None', type: 'none'};
                } else {
                    getProductSegment(productList, productSegmentName);
                }
            }
            if ($scope.dataSet.reportName == 'accountPerformance') {
                if (!dataSet.timeSegment) {
                    $scope.dataSet.timeSegment = {name: 'None', type: 'none'};
                } else {
                    getTimeSegment(timeSegmentList, timeSegmentName);
                }
                if (!dataSet.productSegment) {
                    $scope.dataSet.productSegment = {name: 'None', type: 'none'};
                } else {
                    getProductSegment(productList, productSegmentName);
                }
            }
            if ($scope.dataSet.reportName == 'campaignPerformance') {
                if (!dataSet.timeSegment) {
                    $scope.dataSet.timeSegment = {name: 'None', type: 'none'};
                } else {
                    getTimeSegment(timeSegmentList, timeSegmentName);
                }
                if (!dataSet.productSegment) {
                    $scope.dataSet.productSegment = {name: 'None', type: 'none'};
                } else {
                    getProductSegment(productList, productSegmentName);
                }
            }

            if ($scope.dataSet.reportName == 'adPerformance') {
                if (!dataSet.timeSegment) {
                    $scope.dataSet.timeSegment = {name: 'None', type: 'none'};
                } else {
                    getTimeSegment(timeSegmentList, timeSegmentName);
                }
                if (!dataSet.productSegment) {
                    $scope.dataSet.productSegment = {name: 'None', type: 'none'};
                } else {
                    getProductSegment(productList, productSegmentName);
                }
            }

            if ($scope.dataSet.reportName == 'adSetPerformance') {
                if (!dataSet.timeSegment) {
                    $scope.dataSet.timeSegment = {name: 'None', type: 'none'};
                } else {
                    getTimeSegment(timeSegmentList, timeSegmentName);
                }
                if (!dataSet.productSegment) {
                    $scope.dataSet.productSegment = {name: 'None', type: 'none'};
                } else {
                    getProductSegment(productList, productSegmentName);
                }
            }

            if ($scope.dataSet.reportName == 'postPerformance') {
                if (!dataSet.timeSegment) {
                    $scope.dataSet.timeSegment = {name: 'None', type: 'none'};
                } else {
                    getTimeSegment(timeSegmentList, timeSegmentName);
                }
                if (!dataSet.productSegment) {
                    $scope.dataSet.productSegment = {name: 'None', type: 'none'};
                } else {
                    getProductSegment(productList, productSegmentName);
                }
            }

            if ($scope.dataSet.reportName == 'engagements') {
                if (!dataSet.timeSegment) {
                    $scope.dataSet.timeSegment = {name: 'None', type: 'none'};
                } else {
                    getTimeSegment(timeSegmentList, timeSegmentName);
                }
                if (!dataSet.productSegment) {
                    $scope.dataSet.productSegment = {name: 'None', type: 'none'};
                } else {
                    getProductSegment(productList, productSegmentName);
                }
            }

            if ($scope.dataSet.reportName == 'reach') {
                if (!dataSet.timeSegment) {
                    $scope.dataSet.timeSegment = {name: 'None', type: 'none'};
                } else {
                    getTimeSegment(timeSegmentList, timeSegmentName);
                }
                if (!dataSet.productSegment) {
                    $scope.dataSet.productSegment = {name: 'None', type: 'none'};
                } else {
                    getProductSegment(productList, productSegmentName);
                }
            }

            if ($scope.dataSet.reportName == 'pageLikes') {
                if (!dataSet.timeSegment) {
                    $scope.dataSet.timeSegment = {name: 'None', type: 'none'};
                } else {
                    getTimeSegment(timeSegmentList, timeSegmentName);
                }
                if (!dataSet.productSegment) {
                    $scope.dataSet.productSegment = {name: 'None', type: 'none'};
                } else {
                    getProductSegment(productList, productSegmentName);
                }
            }

            if ($scope.dataSet.reportName == 'pageViews') {
                if (!dataSet.timeSegment) {
                    $scope.dataSet.timeSegment = {name: 'None', type: 'none'};
                } else {
                    getTimeSegment(timeSegmentList, timeSegmentName);
                }
                if (!dataSet.productSegment) {
                    $scope.dataSet.productSegment = {name: 'None', type: 'none'};
                } else {
                    getProductSegment(productList, productSegmentName);
                }
            }
        }
        if ($scope.dataSet.dataSourceId.dataSourceType === "bing")
        {
            var index = getIndex($scope.dataSet.reportName, $scope.bingPerformance);
            $scope.timeSegment = $scope.bingPerformance[index].timeSegments;
            $scope.productSegment = $scope.bingPerformance[index].productSegments;

            var timeSegmentList = $scope.timeSegment;
            var timeSegmentName = dataSet.timeSegment;
            var productList = $scope.productSegment;
            var productSegmentName = dataSet.productSegment;
            $scope.dataSetFlag = true;
            $scope.timeSegFlag = true;
            $scope.productSegFlag = true;
            $scope.nwStatusFlag = false;
            if ($scope.dataSet.reportName == 'geoPerformance') {
                if (!dataSet.timeSegment) {
                    $scope.dataSet.timeSegment = {name: 'None', type: 'none'};
                } else {
                    getTimeSegment(timeSegmentList, timeSegmentName)
                }
                if (!dataSet.productSegment) {
                    $scope.dataSet.productSegment = {name: 'City', type: 'city'};
                } else {
                    getProductSegment(productList, productSegmentName)
                }
            } else {
                if (!dataSet.timeSegment) {
                    $scope.dataSet.timeSegment = {name: 'None', type: 'none'};
                } else {
                    getTimeSegment(timeSegmentList, timeSegmentName)
                }
                if (!dataSet.productSegment) {
                    $scope.dataSet.productSegment = {name: 'None', type: 'none'};
                } else {
                    getProductSegment(productList, productSegmentName)
                }
            }
        }
        if ($scope.dataSet.dataSourceId.dataSourceType == "pinterest")
        {
            var index = getIndex($scope.dataSet.reportName, $scope.pinterestPerformance);
            $scope.timeSegment = $scope.pinterestPerformance[index].timeSegments;
            $scope.productSegment = $scope.pinterestPerformance[index].productSegments;
            $scope.nwStatusFlag = false;
        }
        if ($scope.dataSet.dataSourceId.dataSourceType == "adwords")
        {
            var index = getIndex($scope.dataSet.reportName, $scope.adwordsPerformance);
            $scope.timeSegment = $scope.adwordsPerformance[index].timeSegments;
            $scope.productSegment = $scope.adwordsPerformance[index].productSegments;
            $scope.nwStatusFlag = true;
            $scope.timeSegFlag = true;
            $scope.productSegFlag = true;
            if ($scope.dataSet.reportName == 'geoPerformance') {
                $scope.dataSet.timeSegment = {name: 'None', type: 'none'};
                $scope.dataSet.productSegment = {name: 'City', type: 'city'};
            } else {
                $scope.dataSet.timeSegment = {name: 'None', type: 'none'};
                $scope.dataSet.productSegment = {name: 'None', type: 'none'};
            }
        }

        if ($scope.dataSet.dataSourceId.dataSourceType == "analytics")
        {
            var index = getIndex($scope.dataSet.reportName, $scope.analyticsPerformance);
            $scope.timeSegment = $scope.analyticsPerformance[index].timeSegments;
            $scope.productSegment = $scope.analyticsPerformance[index].productSegments;

            var productList = $scope.productSegment;
            var productSegmentName = dataSet.productSegment;

            var timeSegmentList = $scope.timeSegment;
            var timeSegmentName = dataSet.timeSegment;
            $scope.timeSegFlag = true;
            $scope.productSegFlag = true;
            $scope.nwStatusFlag = false;
            if (!dataSet.timeSegment) {
                $scope.dataSet.timeSegment = {name: 'None', type: 'none'};
            } else {
                getTimeSegment(timeSegmentList, timeSegmentName)
            }
            if (!dataSet.productSegment) {
                $scope.dataSet.productSegment = {name: 'None', type: 'none'};
            } else {
                getProductSegment(productList, productSegmentName)
            }
        }
        if ($scope.dataSet.dataSourceId.dataSourceType == "linkedin")
        {
            var index = getIndex($scope.dataSet.reportName, $scope.linkedinPerformance);
            $scope.timeSegment = $scope.linkedinPerformance[index].timeSegments;
            $scope.productSegment = $scope.linkedinPerformance[index].productSegments;
            $scope.nwStatusFlag = false;
            $scope.timeSegFlag = false;
            $scope.productSegFlag = false;
        }
        if ($scope.dataSet.dataSourceId.dataSourceType == "pinterest")
        {
            var index = getIndex($scope.dataSet.reportName, $scope.pinterestPerformance);
            $scope.timeSegment = $scope.pinterestPerformance[index].timeSegments;
            $scope.productSegment = $scope.pinterestPerformance[index].productSegments;
            $scope.nwStatusFlag = false;
            $scope.timeSegFlag = false;
            $scope.productSegFlag = false;
        }

        function getIndex(data, object)
        {
            for (var i = 0; i < object.length; i++)
            {
                if (object[i].type == data)
                {
                    return i;
                }
            }
        }
    };



    function getProductSegment(productList, productSegmentName) {
        productList.forEach(function (val, key) {
            if (productSegmentName == val.type) {
                $scope.dataSet.productSegment = val;
            }
        });
    }

    function getTimeSegment(timeSegmentList, timeSegmentName) {
        timeSegmentList.forEach(function (val, key) {
            if (timeSegmentName == val.type) {
                $scope.dataSet.timeSegment = val;
            }
        });
    }



    $scope.accountID = $stateParams.accountId;
    $scope.accountName = $stateParams.accountName;
    $scope.startDate = $stateParams.startDate;
    $scope.endDate = $stateParams.endDate;
    function getItems() {
        $http.get('admin/ui/dataSet').success(function (response) {
            $scope.dataSets = response;
        });
    }
    getItems();
    $http.get('admin/ui/dataSource').success(function (response) {
        $scope.searchDataSourceItems = [];
        $scope.dataSources = response;
        $scope.searchDataSourceItems.unshift({name: 'All Data Source', value: '', id: 0});
        angular.forEach(response, function (value, key) {
            $scope.searchDataSourceItems.push({name: value.name, value: value.name, id: value.id});
        });
    });
    $scope.selectedItems = {name: "All Data Source", value: '', id: 0};

    $scope.selectXlsSheet = function (dataSource) {
        if (dataSource.dataSourceType == 'xls') {
            var url = "admin/proxy/getSheets?";
            var dataSourceId = dataSource.id;
            $http.get(url + "dataSourceId=" + dataSourceId).success(function (response) {
                $scope.xlsSheetNames = response;
            });
        }
    };
    $scope.saveDataSet = function () {
        var dataSetList = $scope.dataSet;
        console.log(dataSetList);
        console.log(dataSetList.timeSegment.type);
        dataSetList.timeSegment = dataSetList.timeSegment.type;
        dataSetList.productSegment = dataSetList.productSegment.type;
        console.log(dataSetList.timeSegment);

        var dataSet = dataSetList;
        dataSet.dataSourceId = dataSet.dataSourceId.id;
        $scope.nwStatusFlag = true;
        $http({method: dataSet.id ? 'PUT' : 'POST', url: 'admin/ui/dataSet', data: dataSet}).success(function (response) {
            getItems();
        });
        $scope.dataSet = "";
        $scope.showPreviewChart = false;
        $scope.previewData = null;
        $scope.dataSetFlag = false;
    };
    $scope.clearTable = function () {
        $scope.dataSet = "";
    };
    $scope.editDataSet = function (dataSet) {
        $scope.nwStatusFlag = true;
        console.log(dataSet.networkType);
        console.log(dataSet);
        var joinDataSetId = null;
        if (dataSet.joinDataSetId != null) {
            joinDataSetId = dataSet.joinDataSetId.id;
        }
        var data = {
            id: dataSet.id,
            name: dataSet.name,
            query: dataSet.query,
            url: dataSet.url,
            reportName: dataSet.reportName,
            timeSegment: dataSet.timeSegment,
            productSegment: dataSet.productSegment,
            networkType: dataSet.networkType,
            dataSourceId: dataSet.dataSourceId,
            agencyId: dataSet.agencyId.id,
            userId: dataSet.userId.id,
            joinDataSetId: joinDataSetId
        };
        console.log(data);
        $scope.dataSet = data;
        var dataSource = null;
        var dataSourceType = null;
        if (dataSet.dataSourceId != null) {
            dataSource = dataSet.dataSourceId;
            dataSourceType = dataSet.dataSourceId.dataSourceType;
            if (dataSourceType == 'xls') {
                $scope.selectXlsSheet(dataSource);
            }
            if (dataSet.dataSourceId.dataSourceType === "instagram")
            {
                $scope.report = $scope.instagramPerformance;
                $scope.getTimeSegements();
                $scope.dataSetFlag = true;
                $scope.nwStatusFlag = false;
            } else if (dataSet.dataSourceId.dataSourceType === "facebook")
            {
                $scope.report = $scope.facebookPerformance;
                $scope.getTimeSegements(dataSet);
                $scope.dataSetFlag = true;
                $scope.nwStatusFlag = false;
            } else if (dataSet.dataSourceId.dataSourceType === "bing")
            {
                $scope.report = $scope.bingPerformance;
                $scope.getTimeSegements(dataSet);
                $scope.dataSetFlag = true;
                $scope.nwStatusFlag = false;
            } else if (dataSet.dataSourceId.dataSourceType === "pinterest")
            {
                $scope.report = $scope.pinterestPerformance;
                $scope.getTimeSegements(dataSet);
                $scope.dataSetFlag = true;
                $scope.nwStatusFlag = false;
            } else if (dataSet.dataSourceId.dataSourceType === "adwords")
            {
                $scope.report = $scope.adwordsPerformance;
                $scope.getTimeSegements();
                $scope.dataSetFlag = true;
                $scope.nwStatusFlag = true;
            } else if (dataSet.dataSourceId.dataSourceType === "analytics")
            {
                $scope.report = $scope.analyticsPerformance;
                $scope.getTimeSegements(dataSet);
                $scope.dataSetFlag = true;
                $scope.nwStatusFlag = false;
            } else if (dataSet.dataSourceId.dataSourceType === "linkedin")
            {
                $scope.report = $scope.linkedinPerformance;
                $scope.getTimeSegements();
                $scope.dataSetFlag = true;
                $scope.nwStatusFlag = false;
            } else {
                $scope.dataSetFlag = false;
                $scope.nwStatusFlag = false;
            }
        }

//        if (dataSet.dataSourceId.dataSourceType === "adwords" || dataSet.dataSourceId.dataSourceType === "analytics" || dataSet.dataSourceId.dataSourceType === "facebook" || dataSet.dataSourceId.dataSourceType === "instagram")
//        {
//            if (dataSet.dataSourceId.dataSourceType === "instagram")
//            {
//                $scope.report = $scope.instagramPerformance;
//            } else {
//
//                $scope.report = $scope.reportPerformance;
//            }
//            $scope.getTimeSegemens();
//            $scope.dataSetFlag = true;
//        } else {
//            $scope.dataSetFlag = false;
//        }
//        console.log(data);
    };
    $scope.resetPreview = function (dataSet) {
        $scope.previewData = null;
    };
    $scope.previewDataSet = function (dataSet) {
        $scope.showPreviewChart = true;
        $scope.previewData = dataSet;
        $scope.previewData = null;
        $timeout(function () {
            $scope.previewData = dataSet;
        }, 50);
    };
    $scope.refreshDataSet = function (dataSet) {
//        var tmpDataSet = dataSet
//        dataSet.timeSegment = "";
//        dataSet.productSegment = "";
        $scope.showPreviewChart = true;
        $scope.previewData = null;
        $timeout(function () {
            $scope.previewData = dataSet;
        }, 50);
    };
    $scope.clearSeg = function (dataSet) {
        dataSet.timeSegment = '';
        dataSet.productSegment = '';
    };
    $scope.clearDataSet = function (dataSet) {
        $scope.dataSet = "";
        $scope.showPreviewChart = false;
        $scope.previewData = null;
        $scope.selectedRow = null;
        $scope.dataSetFlag = false;
    };
    $scope.deleteDataSet = function (dataSet, index) {
        $http({method: 'DELETE', url: 'admin/ui/dataSet/' + dataSet.id}).success(function (response) {
            $scope.dataSets.splice(index, 1);
        });
    };
    $scope.selectedRow = null;
    $scope.setClickedRow = function (index) {
        $scope.selectedRow = index;
        $scope.showPreviewChart = false;
        $scope.previewData = null;
    };
});
app.directive('previewTable', function ($http, $filter, $stateParams) {
    return{
        restrict: 'A',
        scope: {
            path: '@'
//            dataSetId: '@'
                    // widgetColumns: '@',
                    //setTableFn: '&',
                    // tableFooter:'@'
        },
        template: '<div ng-show="loadingTable" class="text-center" style="color: #228995;"><img src="static/img/logos/loader.gif"></div>' +
                '<div ng-if="ajaxLoadingCompleted">' +
                '<div ng-if="tableRows!=null&&dataSetId!=null" class="pull-right">' +
                '<button class="btn btn-warning btn-xs" title="Delete Derived Columns" ng-click="resetDataSetColumn()">Reset</button>' +
                '<button class="btn btn-success btn-xs" title="Add Derived Column" data-toggle="modal" data-target="#dataSet" ng-click="dataSetFieldsClose(dataSetColumn)"><i class="fa fa-plus"></i></button>' +
                '<div id="dataSet" class="modal" role="dialog">' +
                '<div class="modal-dialog modal-lg">' +
                '<div class="modal-content">' +
                '<div class="modal-header">' +
                '<button type="button" class="close" ng-click="dataSetFieldsClose(dataSetColumn)" data-dismiss="modal">&times;</button>' +
                '<h4 class="modal-title">Derived Column</h4>' +
                '</div>' +
                '<div class="modal-body" style="overflow: visible;">' +
                '<form name="dataSetForm" class="form-horizontal">' +
                '<div class="form-group">' +
                '<label class="col-md-3">Field Name</label>' +
                '<div class="col-md-3">' +
                '<input class="form-control" ng-model="dataSetColumn.fieldName"  ng-change="checkFieldName(dataSetColumn.fieldName)" type="text">' +
                '</div>' +
                '<div class="col-md-3">' +
                '<span ng-show="dataSetError" style="color:red">Field Name Already Exists</span>' +
                '</div>' +
//                '<label class="col-md-2">Base Field</label>' +
//                '<div class="col-md-4">' +
//                '<select class="form-control" ng-model="dataSetColumn.baseField">' +
//                '<option ng-if="!dataSetColumn.functionName && !dataSetColumn.expression" ng-repeat="dataSetColumn in dataSetColumns" value={{dataSetColumn.fieldName}}>' +
//                '{{dataSetColumn.fieldName}}' +
//                '</option>' +
//                '</select>' +
//                '</div>' +

                '</div>' +
//                '<div class="form-group">' +
//                '<label class="col-md-3">Function</label>' +
//                '<div class="col-md-3">' +
//                '<select  name="functionName" class="form-control" ng-model="dataSetColumn.functionName" ng-change="functionChange(dataSetColumn)" ng-disabled="dataSetColumn.expression?true:false">' +
//                '<option ng-repeat="functionType in functionTypes" value={{functionType.name}}>' +
//                '{{functionType.name}}' +
//                '</option>' +
//                '</select>' +
//                '</div>' +
//                '<div ng-if="dataSetColumn.functionName===\'Custom\'" class="col-md-2">' +
//                '<div class="dropdown editWidgetDropDown">' +
//                '<button class="drop btn btn-default dropdown-toggle" type="button" data-toggle="dropdown" id="dateRangeName">' +
//                ' <span ng-class="{\'text-danger\':dateErrorMessage==true}">{{dataSetColumn.dateRangeName?dataSetColumn.dateRangeName:"Select Date"}}</span>' +
//                '<span class="caret"></span></button>' +
//                '<ul class="dropdown-menu scheduler-list-style">' +
//                '<li>' +
//                '<div class="col-md-12">' +
//                '<div>' +
//                '<a class="pull-right custom-daterange-box" function-Date-Range ng-click="selectFunctionDateRange(dataSetColumn)" widget-Table-Date-Range="{{dataSetColumn}}" id="widgetDateRange">' +
//                '<span class="date-border">' +
//                '{{dataSetColumn.customStartDate ? dataSetColumn.customStartDate : startDate| date: "MM/dd/yyyy"}} - {{dataSetColumn.customEndDate ? dataSetColumn.customEndDate : endDate| date: "MM/dd/yyyy"}}' +
//                '</span>' +
//                '</a>' +
//                '</div>' +
//                '</div>' +
//                '</li>' +
////                            text values
//                '<li>' +
//                '<a>Last <input type="text"' +
//                'ng-model="dataSetColumn.lastNdays"' +
//                'ng-change="selectFunctionDuration(\'Last N Days\', dataSetColumn)" ' +
//                'class="form-control"' +
//                'ng-model-options="{debounce: 500}"' +
//                'style="width: 60px; display: contents; height: 25px;"> ' +
//                'Days' +
//                '</a>' +
//                '</li>' +
//                '<li>' +
//                '<a>Last <input type="text"' +
//                'ng-model="dataSetColumn.lastNweeks"' +
//                'ng-change="selectFunctionDuration(\'Last N Weeks\', dataSetColumn)"' +
//                'class="form-control" ' +
//                'ng-model-options="{debounce: 500}"' +
//                'style="width: 60px; display: contents; height: 25px;"> ' +
//                'Weeks' +
//                '</a>' +
//                '</li>' +
//                '<li>' +
//                '<a>Last <input type="text"' +
//                'ng-model="dataSetColumn.lastNmonths"' +
//                'ng-change="selectFunctionDuration(\'Last N Months\', dataSetColumn)"' +
//                'class="form-control"' +
//                'ng-model-options="{debounce: 500}"' +
//                'style="width: 60px; display: contents; height: 25px;"> ' +
//                'Months' +
//                '</a>' +
//                '</li>' +
//                ' <li>' +
//                '<a>Last <input type="text" ' +
//                'ng-model="dataSetColumn.lastNyears"' +
//                'ng-change="selectFunctionDuration(\'Last N Years\', dataSetColumn)"' +
//                'class="form-control"' +
//                'ng-model-options="{debounce: 500}"' +
//                'style="width: 60px; display: contents; height: 25px;"> ' +
//                'Years' +
//                '</a>' +
//                '</li>' +
//                '</ul>' +
//                ' </div>' +
//                '</div>' +
//                '<label class="col-md-1">Column</label>' +
//                '<div class="col-md-2">' +
//                '<select class="form-control" ng-disabled="dataSetColumn.expression?true:false" ng-model="dataSetColumn.columnName">' +
//                '<option ng-if="!dataSetColumn.functionName && !dataSetColumn.expression" ng-repeat="dataSetColumn in dataSetColumns" value={{dataSetColumn.fieldName}}>' +
//                '{{dataSetColumn.fieldName}}' +
//                '</option>' +
//                '</select>' +
//                '</div>' +
//                '<div class="col-md-1">' +
//                '<i class="fa fa-minus-circle" style="cursor:pointer" ng-click="clearFunction(dataSetColumn)"></i>' +
//                '</div>' +
//                '</div>' +





                '<div class="form-group">' +
                '<label class="col-md-3">Expression</label>' +
                '<div class="col-md-8">' +
                '<textarea name="expression" ng-trim="false" spellcheck="false" smart-area="config" ' +
                'class="form-control code expression" ng-model="dataSetColumn.expression" ng-disabled="dataSetColumn.functionName?true:false" rows="5"></textarea>' +
                '</div>' +
                '<div class="col-md-1">' +
                '<i class="fa fa-minus-circle" style="cursor:pointer" ng-click="clearExpression(dataSetColumn)"></i>' +
                '</div>' +
                '</div>' +
                '<div class="form-group">' +
                '<label class="col-md-3">Field Type</label>' +
                '<div class="col-md-3">' +
                '<select class="form-control" ng-model="dataSetColumn.fieldType">' +
                '<option ng-repeat="fieldType in fieldTypes" value="{{fieldType.value}}">' +
                '{{fieldType.name}}' +
                '</option>' +
                '</select>' +
                '</div>' +
                '<label class="col-md-2">Format</label>' +
                '<div class="col-md-4">' +
                '<select class="form-control" ng-model="dataSetColumn.displayFormat">' +
                '<option  ng-repeat="formatType in formats" value="{{formatType.value}}">' +
                '{{formatType.name}}' +
                '</option>' +
                '</select>' +
                '</div>' +
                '</div>' +
                '</form>' +
                '</div>' +
                '<div class="modal-footer">' +
                '<button type="button" class="btn btn-success" data-dismiss="modal" ng-disabled="dataSetError||!((dataSetColumn.expression||(dataSetColumn.functionName&&dataSetColumn.columnName))&&dataSetColumn.fieldName&&dataSetColumn.fieldType)" ng-click="saveDataSetColumn(dataSetColumn)">Save</button>' +
                '<button type="button" class="btn btn-default" data-dismiss="modal" ng-click="dataSetFieldsClose(dataSetColumn)" >Close</button>' +
                '</div>' +
                '</div>' +
                '</div>' +
                '</div>' +
                '</div>' +
                '<table class="table table-responsive table-bordered table-l2t">' +
                '<thead><tr>' +
                '<th class="text-capitalize table-bg" ng-repeat="col in dataSetColumns">' +
                '{{col.fieldName}}' +
                //Edit
                '<div>' +
                '<button ng-if="col.functionName != null|| col.expression != null" type="button" class="btn btn-default btn-xs" data-toggle="modal" data-target="#dataSetColumn{{col.id}}" ng-click="editDataset(col)"><i class="fa fa-pencil"></i></button>' +
                '<div id="dataSetColumn{{col.id}}" class="modal" role="dialog">' +
                '<div class="modal-dialog modal-lg">' +
                '<div class="modal-content">' +
                '<div class="modal-header">' +
                '<button type="button" class="close" ng-click="dataSetFieldsClose(dataSetColumn)" data-dismiss="modal">&times;</button>' +
                '<h4 class="modal-title">Derived Column</h4>' +
                '</div>' +
                '<div class="modal-body" style="overflow: visible;">' +
                '<form name="dataSetForm" class="form-horizontal">' +
                '<div class="form-group">' +
                '<label class="col-md-3">Field Name</label>' +
                '<div class="col-md-3">' +
                '<input class="form-control" ng-model="dataSetColumn.fieldName"  ng-change="checkFieldName(dataSetColumn.fieldName)" type="text">' +
                '</div>' +
                '<div class="col-md-3">' +
                '<span ng-show="dataSetError" style="color:red">Field Name Already Exists</span>' +
                '</div>' +
//                '<label class="col-md-2">Base Field</label>' +
//                '<div class="col-md-4">' +
//                '<select class="form-control" ng-model="dataSetColumn.baseField">' +
//                '<option ng-if="!dataSetColumn.functionName && !dataSetColumn.expression" ng-repeat="dataSetColumn in dataSetColumns" value={{dataSetColumn.fieldName}}>' +
//                '{{dataSetColumn.fieldName}}' +
//                '</option>' +
//                '</select>' +
//                '</div>' +

                '</div>' +
                '</div>' +
                '<div class="form-group">' +
                '<label class="col-md-3">Function</label>' +
                '<div class="col-md-3">' +
                '<select  name="functionName" class="form-control" ng-model="dataSetColumn.functionName" ng-change="functionChange(dataSetColumn.functionName)" ng-disabled="dataSetColumn.expression?true:false">' +
                '<option ng-repeat="functionType in functionTypes" value={{functionType.name}}>' +
                '{{functionType.name}}' +
                '</option>' +
                '</select>' +
                '</div>' +
                '<div ng-if="dataSetColumn.functionName===\'Custom\'" class="col-md-2">' +
                '<div class="dropdown editWidgetDropDown">' +
                '<button class="drop btn btn-default dropdown-toggle" type="button" data-toggle="dropdown" id="dateRangeName">' +
                ' <span ng-class="{\'text-danger\':dateErrorMessage==true}">{{dataSetColumn.dateRangeName?dataSetColumn.dateRangeName:"Select Date"}}</span>' +
                '<span class="caret"></span></button>' +
                '<ul class="dropdown-menu scheduler-list-style">' +
                '<li>' +
                '<div class="col-md-12">' +
                '<div>' +
                '<a class="pull-right custom-daterange-box" function-Date-Range ng-click="selectFunctionDateRange(dataSetColumn)" widget-Table-Date-Range="{{dataSetColumn}}" id="widgetDateRange">' +
                '<span class="date-border">' +
                '{{dataSetColumn.customStartDate ? dataSetColumn.customStartDate : startDate| date: "MM/dd/yyyy"}} - {{dataSetColumn.customEndDate ? dataSetColumn.customEndDate : endDate| date: "MM/dd/yyyy"}}' +
                '</span>' +
                '</a>' +
                '</div>' +
                '</div>' +
                '</li>' +
//                            text values
                '<li>' +
                '<a>Last <input type="text"' +
                'ng-model="dataSetColumn.lastNdays"' +
                'ng-change="selectFunctionDuration(\'Last N Days\', dataSetColumn)" ' +
                'class="form-control"' +
                'ng-model-options="{debounce: 500}"' +
                'style="width: 60px; display: contents; height: 25px;"> ' +
                'Days' +
                '</a>' +
                '</li>' +
                '<li>' +
                '<a>Last <input type="text"' +
                'ng-model="dataSetColumn.lastNweeks"' +
                'ng-change="selectFunctionDuration(\'Last N Weeks\', dataSetColumn)"' +
                'class="form-control" ' +
                'ng-model-options="{debounce: 500}"' +
                'style="width: 60px; display: contents; height: 25px;"> ' +
                'Weeks' +
                '</a>' +
                '</li>' +
                '<li>' +
                '<a>Last <input type="text"' +
                'ng-model="dataSetColumn.lastNmonths"' +
                'ng-change="selectFunctionDuration(\'Last N Months\', dataSetColumn)"' +
                'class="form-control"' +
                'ng-model-options="{debounce: 500}"' +
                'style="width: 60px; display: contents; height: 25px;"> ' +
                'Months' +
                '</a>' +
                '</li>' +
                ' <li>' +
                '<a>Last <input type="text" ' +
                'ng-model="dataSetColumn.lastNyears"' +
                'ng-change="selectFunctionDuration(\'Last N Years\', dataSetColumn)"' +
                'class="form-control"' +
                'ng-model-options="{debounce: 500}"' +
                'style="width: 60px; display: contents; height: 25px;"> ' +
                'Years' +
                '</a>' +
                '</li>' +
                '</ul>' +
                ' </div>' +
                '</div>' +
                '<label class="col-md-1">Column</label>' +
                '<div class="col-md-2">' +
                '<select class="form-control" ng-disabled="dataSetColumn.expression?true:false" ng-model="dataSetColumn.columnName">' +
                '<option ng-if="!dataSetColumn.functionName && !dataSetColumn.expression" ng-repeat="dataSetColumn in dataSetColumns" value={{dataSetColumn.fieldName}}>' +
                '{{dataSetColumn.fieldName}}' +
                '</option>' +
                '</select>' +
                '</div>' +
                '<div class="col-md-1">' +
                '<i class="fa fa-minus-circle" style="cursor:pointer" ng-click="clearFunction(dataSetColumn)"></i>' +
                '</div>' +
                '</div>' +
                '<div class="form-group">' +
                '<label class="col-md-3">Expression</label>' +
                '<div class="col-md-8">' +
                '<textarea name="expression" ng-trim="false" spellcheck="false" smart-area="config" ' +
                'class="form-control code expression" ng-model="dataSetColumn.expression" ng-disabled="dataSetColumn.functionName?true:false" rows="5"></textarea>' +
                '</div>' +
                '<div class="col-md-1">' +
                '<i class="fa fa-minus-circle" style="cursor:pointer" ng-click="clearExpression(dataSetColumn)"></i>' +
                '</div>' +
                '</div>' +
                '<div class="form-group">' +
                '<label class="col-md-3">Field Type</label>' +
                '<div class="col-md-3">' +
                '<select class="form-control" ng-model="dataSetColumn.fieldType">' +
                '<option ng-repeat="fieldType in fieldTypes" value="{{fieldType.value}}">' +
                '{{fieldType.name}}' +
                '</option>' +
                '</select>' +
                '</div>' +
                '<label class="col-md-2">Format</label>' +
                '<div class="col-md-4">' +
                '<select class="form-control" ng-model="dataSetColumn.displayFormat">' +
                '<option  ng-repeat="formatType in formats" value="{{formatType.value}}">' +
                '{{formatType.name}}' +
                '</option>' +
                '</select>' +
                '</div>' +
                '</div>' +
                '</form>' +
                '</div>' +
                '<div class="modal-footer">' +
                '<button type="button" class="btn btn-success" data-dismiss="modal" ng-disabled="dataSetError || !((dataSetColumn.expression || (dataSetColumn.functionName && dataSetColumn.columnName)) && dataSetColumn.fieldName && dataSetColumn.fieldType)" ng-click="saveDataSetColumn(dataSetColumn)">Save</button>' +
                '<button type="button" class="btn btn-default" data-dismiss="modal" ng-click="dataSetFieldsClose(dataSetColumn)">Close</button>' +
                '</div>' +
                '</div>' +
                '</div>' +
                '</div>' +
                '<button ng-if="col.functionName != null|| col.expression != null" type="button" ng-click=deleteDataset(col) class="btn btn-default btn-xs"><i class="fa fa-trash"></i></button>' +
                '</div>' +
                '</th>' +
                '</tr></thead>' +
                '<tbody ng-repeat="tableRow in tableRows">' +
                '<tr class="text-capitalize">' +
                '<td ng-repeat="col in dataSetColumns">' +
                '<div>{{format(col, tableRow[col.fieldName])}}</div>' +
                '</td>' +
                '</tbody>' +
                '</table>' +
                '</div>',
        link: function (scope, element, attr) {
            scope.startDate = $stateParams.startDate;
            scope.endDate = $stateParams.endDate;
            scope.fieldTypes = [
                {name: 'None', value: ''},
                {name: 'String', value: 'string'},
                {name: 'Number', value: 'number'},
                {name: 'Date', value: 'date'},
                {name: 'Day', value: 'day'}
            ];
            scope.formats = [
                {name: "None", value: ''},
                {name: "Currency", value: '$,.2f'},
                {name: "Integer", value: ',.0f'},
                {name: "Percentage", value: ',.2%'},
                {name: "Decimal1", value: ',.1f'},
                {name: "Decimal2", value: ',.2f'},
                {name: "Time", value: 'H:M:S'}
            ];
            scope.functionTypes = [
                {name: "YOY", value: 'yoy'},
                {name: "MOM", value: 'mom'},
                {name: 'WOW', value: 'wow'},
                {name: 'Custom', value: 'custom'}
            ];
            scope.loadingTable = true;
            var dataSourcePath = JSON.parse(scope.path)
            console.log(dataSourcePath);
//            console.log(dataSourcePath.dataSourceId.userName);
//            console.log(dataSourcePath.dataSourceId.connectionString);
//            console.log(dataSourcePath.dataSourceId.sqlDriver);
//            console.log(dataSourcePath.dataSourceId.password);
            var url = "admin/proxy/getData?";
            var dataSourcePassword = '';
            if (dataSourcePath.dataSourceId != null) {
                if (dataSourcePath.dataSourceId.dataSourceType == "sql") {
                    url = "admin/proxy/getJson?url=../dbApi/admin/dataSet/getData&";
                }

                if (dataSourcePath.dataSourceId.password) {
                    dataSourcePassword = dataSourcePath.dataSourceId.password;
                } else {
                    dataSourcePassword = '';
                }
            }
            console.log(dataSourcePath.networkType);
            scope.format = function (column, value) {
                if (!value) {
                    return "-";
                }
                if (column.displayFormat) {
                    if (Number.isNaN(value)) {
                        return "-";
                    }
                    if (column.displayFormat.indexOf("%") > -1) {
                        // return d3.format(column.displayFormat)(value / 100);
                    }
                    return d3.format(column.displayFormat)(value);
                }
                return value;
            };
            console.log(dataSourcePath);
            var setTimeSegment, setProductSegment;

            if (dataSourcePath.timeSegment) {
                setTimeSegment = dataSourcePath.timeSegment.type;
            } else {
                setTimeSegment = 'none';
            }
            if (dataSourcePath.productSegment) {
                setProductSegment = dataSourcePath.productSegment.type;
            } else {
                setProductSegment = 'none';
            }
            var connectionUrl = null;
            var dataSourceId = null;
            var driver = null;
            var dataSourceType = null;
            var userName = null;
            if (dataSourcePath.dataSourceId != null) {
                connectionUrl = dataSourcePath.dataSourceId.connectionString;
                dataSourceId = dataSourcePath.dataSourceId.id;
                driver = dataSourcePath.dataSourceId.dataSourceType;
                dataSourceType = dataSourcePath.dataSourceId.dataSourceType;
                userName = dataSourcePath.dataSourceId.userName;
            }
            scope.dataSetItems = function () {
                $http.get(url + 'connectionUrl=' + connectionUrl +
                        "&dataSourceId=" + dataSourceId +
                        "&dataSetId=" + dataSourcePath.id +
                        "&joinDataSetId=" + dataSourcePath.joinDataSetId +
                        "&accountId=" + $stateParams.accountId +
                        "&dataSetReportName=" + dataSourcePath.reportName +
                        "&timeSegment=" + setTimeSegment +
                        "&filter=" + dataSourcePath.networkType +
                        "&productSegment=" + setProductSegment +
                        "&driver=" + dataSourceType +
                        "&dataSourceType=" + dataSourceType +
                        "&location=" + $stateParams.locationId +
                        "&startDate=" + $stateParams.startDate +
                        "&endDate=" + $stateParams.endDate +
                        '&username=' + userName +
                        '&password=' + dataSourcePassword +
                        '&url=' + dataSourcePath.url +
                        '&port=3306&schema=deeta_dashboard&query=' + encodeURI(dataSourcePath.query)).success(function (response) {
                    scope.dataSetColumns = [];
                    if (dataSourcePath.id == null) {
                        scope.ajaxLoadingCompleted = true;
                        scope.loadingTable = false;
                        scope.dataSetColumns = response.columnDefs;
                    }
                    scope.tableColumns = response.columnDefs;
//                    scope.tableRows = response.data;

                    if (setTimeSegment == "dayOfWeek") {
                        scope.dayOfWeekDataSet = [];
                        angular.forEach(response.data, function (valueObj, key) {
                            var dayOfWeekObj = {
                                accountId: valueObj.accountId,
                                accountName: valueObj.accountName,
                                averageCpc: valueObj.averageCpc,
                                averagePosition: valueObj.averagePosition,
                                clicks: valueObj.clicks,
                                conversionRate: valueObj.conversionRate,
                                conversions: valueObj.conversions,
                                costPerConversion: valueObj.costPerConversion,
                                ctr: valueObj.ctr,
                                dayOfWeek: dayOfWeekAsString(valueObj.dayOfWeek - 1),
                                gregorianDate: valueObj.gregorianDate,
                                hourOfDay: valueObj.hourOfDay,
                                impressionLostToBudgetPercent: valueObj.impressionLostToBudgetPercent,
                                impressionLostToRankPercent: valueObj.impressionLostToRankPercent,
                                impressionSharePercent: valueObj.impressionSharePercent,
                                impressions: valueObj.impressions,
                                month: valueObj.month,
                                phoneCalls: valueObj.phoneCalls,
                                qualityScore: valueObj.qualityScore,
                                spend: valueObj.spend,
                                week: valueObj.week
                            };
                            scope.dayOfWeekDataSet.push(dayOfWeekObj);
                        });
                        scope.tableRows = scope.dayOfWeekDataSet;
                    } else {
                        scope.tableRows = response.data;
                    }
                    function dayOfWeekAsString(dayIndex) {
                        return ["Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"][dayIndex];
                    }





                    scope.columns = [];
                    scope.dataSetId = dataSourcePath.id;
                    if (dataSourcePath.id != null) {
                        $http.get("admin/ui/getDatasetColumnByDatasetId/" + dataSourcePath.id).success(function (resp) {
                            scope.ajaxLoadingCompleted = true;
                            scope.loadingTable = false;
                            console.log(resp)
                            scope.dataSetColumns = [];
                            if (resp == "" || resp == null) {
                                scope.dataSetColumns = scope.tableColumns;
                                console.log(scope.dataSetColumns);
                            } else {
                                angular.forEach(resp, function (value, key) {
                                    angular.forEach(scope.tableColumns, function (val, key) {
                                        if (value.fieldName == val.fieldName) {
                                            var data = {
                                                id: value.id,
                                                fieldName: value.fieldName,
                                                displayName: value.displayName,
                                                fieldType: value.fieldType,
                                                displayFormat: value.displayFormat,
                                                status: value.status,
                                                expression: value.expression,
                                                functionName: value.functionName,
                                                columnName: value.columnName,
                                                baseField: value.baseField,
                                                dateRangeName: value.dateRangeName,
                                                customStartDate: value.customStartDate,
                                                customEndDate: value.customEndDate,
                                                lastNdays: value.lastNdays,
                                                lastNweeks: value.lastNweeks,
                                                lastNmonths: value.lastNmonths,
                                                lastNyears: value.lastNyears
                                            };
                                            console.log(data);
                                            scope.dataSetColumns.push(data);
                                        }
                                    });
                                });
                            }
                            console.log(scope.dataSetColumns);
                            scope.expressionLessColumn = [];
                            for (var j = 0; j < scope.dataSetColumns.length; j++) {
                                if (scope.dataSetColumns[j].expression === null && scope.dataSetColumns[j].functionName === null) {
                                    scope.expressionLessColumn.push(scope.dataSetColumns[j]);
                                }
                            }
                            console.log(scope.dataSetColumns.length);
                            for (var i = 0; i < scope.dataSetColumns.length; i++) {
                                console.log(scope.dataSetColumns[i]);
                                var status = null;
                                var expression = null;
                                var functionName = null;
                                var columnName = null;
                                var baseField = null;
                                var dateRangeName = null;
                                var customStartDate = null;
                                var customEndDate = null;
                                var lastNdays = null;
                                var lastNweeks = null;
                                var lastNmonths = null;
                                var lastNyears = null;
                                if (typeof (scope.dataSetColumns[i].status) !== "undefined") {
                                    status = scope.dataSetColumns[i].status;
                                }
                                if (typeof (scope.dataSetColumns[i].expression) !== "undefined") {
                                    expression = scope.dataSetColumns[i].expression;
                                }
                                if (typeof (scope.dataSetColumns[i].functionName) !== "undefined") {
                                    functionName = scope.dataSetColumns[i].functionName;
                                }
                                if (typeof (scope.dataSetColumns[i].columnName) !== "undefined") {
                                    columnName = scope.dataSetColumns[i].columnName;
                                }
                                if (typeof (scope.dataSetColumns[i].baseField) !== "undefined") {
                                    baseField = scope.dataSetColumns[i].baseField;
                                }
                                if (typeof (scope.dataSetColumns[i].dateRangeName) !== "undefined") {
                                    dateRangeName = scope.dataSetColumns[i].dateRangeName;
                                }
                                if (typeof (scope.dataSetColumns[i].customStartDate) !== "undefined") {
                                    customStartDate = scope.dataSetColumns[i].customStartDate;
                                }
                                if (typeof (scope.dataSetColumns[i].customEndDate) !== "undefined") {
                                    customEndDate = scope.dataSetColumns[i].customEndDate;
                                }
                                if (typeof (scope.dataSetColumns[i].lastNdays) !== "undefined") {
                                    lastNdays = scope.dataSetColumns[i].lastNdays;
                                }
                                if (typeof (scope.dataSetColumns[i].lastNweeks) !== "undefined") {
                                    lastNweeks = scope.dataSetColumns[i].lastNweeks;
                                }
                                if (typeof (scope.dataSetColumns[i].lastNmonths) !== "undefined") {
                                    lastNmonths = scope.dataSetColumns[i].lastNmonths;
                                }
                                if (typeof (scope.dataSetColumns[i].lastNYears) !== "undefined") {
                                    lastNyears = scope.dataSetColumns[i].lastNyears;
                                }
                                var columnData = {
                                    id: scope.dataSetColumns[i].id,
                                    fieldName: scope.dataSetColumns[i].fieldName,
                                    displayName: scope.dataSetColumns[i].displayName,
                                    fieldType: scope.dataSetColumns[i].fieldType,
                                    displayFormat: scope.dataSetColumns[i].displayFormat,
                                    status: status,
                                    expression: expression,
                                    functionName: functionName,
                                    columnName: columnName,
                                    baseField: baseField,
                                    dateRangeName: scope.dataSetColumns[i].dateRangeName,
                                    customStartDate: scope.dataSetColumns[i].customStartDate,
                                    customEndDate: scope.dataSetColumns[i].customEndDate,
                                    lastNdays: scope.dataSetColumns[i].lastNdays,
                                    lastNweeks: scope.dataSetColumns[i].lastNweeks,
                                    lastNmonths: scope.dataSetColumns[i].lastNmonths,
                                    lastNyears: scope.dataSetColumns[i].lastNyears
                                };
                                scope.columns.push(columnData);
                            }
                        });
                    }
//                    var tableColumnsData = {
//                        datasetId: dataSourcePath.id,
//                        tableColumns: scope.columns,
//                    };
//                    console.log(tableColumnsData);
//                    $http({method: 'POST', url: 'admin/ui/dataSetColumns', data: JSON.stringify(tableColumnsData)}).success(function (response) {
//                        console.log(response);
//                    });
                });

            };
            scope.dataSetItems();


            scope.resetDataSetColumn = function () {
                var dataSetId = dataSourcePath.id;
                $http({method: 'DELETE', url: 'admin/ui/dataSetColumn/' + dataSetId}).success(function () {
                    scope.dataSetItems();
                })
            }


            scope.dataSetError = false;
            function showDataSetError() {
                scope.dataSetError = true;
            }
            scope.checkFieldName = function (fieldName) {
                for (var i = 0; i < scope.tableColumns.length; i++) {
                    if (fieldName == scope.tableColumns[i].fieldName) {
                        showDataSetError()
                        break;
                    } else {
                        scope.dataSetError = false;
                    }
                }
            };
            scope.functionChange = function (dataSetColumn) {
                scope.dateErrorMessage = false;
                if (dataSetColumn.functionName != "Custom") {
                    dataSetColumn.dateRangeName = "";
                }
            }
            scope.dataSetColumn = {}
            scope.selectFunctionDateRange = function (dataSetColumn) {
                scope.dateErrorMessage = false;
                dataSetColumn.dateRangeName = "Custom";
                dataSetColumn.lastNdays = "";
                dataSetColumn.lastNweeks = "";
                dataSetColumn.lastNmonths = "";
                dataSetColumn.lastNyears = "";
                scope.dateErrorMessage = false;
            }
            scope.clearFunction = function (dataSetColumn) {
                dataSetColumn.columnName = "";
                dataSetColumn.functionName = "";
                dataSetColumn.baseField = "";
            }
            scope.clearExpression = function (dataSetColumn) {
                dataSetColumn.expression = "";
            }
            scope.dataSetFieldsClose = function (dataSetColumn) {
                dataSetColumn.expression = "";
                dataSetColumn.fieldName = "";
                dataSetColumn.fieldType = "";
                dataSetColumn.displayFormat = "";
                dataSetColumn.functionName = "";
                dataSetColumn.columnName = "";
                dataSetColumn.baseField = "";
                dataSetColumn.dateRangeName = "";
                dataSetColumn.customStartDate = $stateParams.startDate;
                dataSetColumn.customEndDate = $stateParams.endDate;
                dataSetColumn.lastNdays = "";
                dataSetColumn.lastNyears = "";
                dataSetColumn.lastNweeks = "";
                dataSetColumn.lastNmonths = "";
                scope.dataSetError = false;
            };
            scope.selectFunctionDuration = function (dateRangeName, dataSetColumn) {

                //scheduler.dateRangeName = dateRangeName;
                if (dateRangeName == 'Last N Days') {
                    if (dataSetColumn.lastNdays) {
                        dataSetColumn.dateRangeName = "Last " + dataSetColumn.lastNdays + " Days";
                    } else {
                        dataSetColumn.dateRangeName = "Last 0 Days";
                    }
                    dataSetColumn.lastNweeks = "";
                    dataSetColumn.lastNmonths = "";
                    dataSetColumn.lastNyears = "";
                } else if (dateRangeName == 'Last N Weeks') {
                    if (dataSetColumn.lastNweeks) {
                        dataSetColumn.dateRangeName = "Last " + dataSetColumn.lastNweeks + " Weeks";
                    } else {
                        dataSetColumn.dateRangeName = "Last 0 Weeks";
                    }
                    dataSetColumn.lastNdays = "";
                    dataSetColumn.lastNmonths = "";
                    dataSetColumn.lastNyears = "";
                } else if (dateRangeName == 'Last N Months') {
                    if (dataSetColumn.lastNmonths) {
                        dataSetColumn.dateRangeName = "Last " + dataSetColumn.lastNmonths + " Months";
                    } else {
                        dataSetColumn.dateRangeName = "Last 0 Months";
                    }
                    dataSetColumn.lastNdays = "";
                    dataSetColumn.lastNweeks = "";
                    dataSetColumn.lastNyears = "";
                } else if (dateRangeName == 'Last N Years') {
                    if (dataSetColumn.lastNyears) {
                        dataSetColumn.dateRangeName = "Last " + dataSetColumn.lastNyears + " Years";
                    } else {
                        dataSetColumn.dateRangeName = "Last 0 Years";
                    }
                    dataSetColumn.lastNdays = "";
                    dataSetColumn.lastNweeks = "";
                    dataSetColumn.lastNmonths = "";
                } else {
                    dataSetColumn.dateRangeName = dateRangeName;
                    dataSetColumn.lastNdays = "";
                    dataSetColumn.lastNweeks = "";
                    dataSetColumn.lastNmonths = "";
                    dataSetColumn.lastNyears = "";
                }
                scope.dateErrorMessage = false;
            };

            scope.saveDataSetColumn = function (dataSetColumn) {
                console.log(dataSetColumn);
                dataSetColumn.dateRangeName = $("#dateRangeName").text().trim();
                console.log(dataSetColumn.dateRangeName);

                if (dataSetColumn.dateRangeName == "Select Date") {
                    dataSetColumn.dateRangeName = ""
                }

                try {
                    scope.customStartDate = moment($('#widgetDateRange').data('daterangepicker').startDate).format('MM/DD/YYYY') //: $stateParams.startDate; //$scope.startDate.setDate($scope.startDate.getDate() - 1);
                    scope.customEndDate = moment($('#widgetDateRange').data('daterangepicker').endDate).format('MM/DD/YYYY')// : $stateParams.endDate;
                } catch (e) {

                }

                if (dataSetColumn.dateRangeName != "Custom") {
                    scope.customStartDate = "";
                    scope.customEndDate = "";
                }
                scope.datasetId = dataSourcePath.id;
                var data = {
                    datasetId: dataSourcePath.id,
                    id: dataSetColumn.id,
                    tableColumns: scope.columns,
                    expression: dataSetColumn.expression,
                    fieldName: dataSetColumn.fieldName,
                    displayName: dataSetColumn.fieldName,
                    fieldType: dataSetColumn.fieldType,
                    baseField: dataSetColumn.baseField,
                    displayFormat: dataSetColumn.displayFormat,
                    functionName: dataSetColumn.functionName,
                    columnName: dataSetColumn.columnName,
                    dateRangeName: dataSetColumn.dateRangeName,
                    customStartDate: scope.customStartDate,
                    customEndDate: scope.customEndDate,
                    lastNdays: dataSetColumn.lastNdays,
                    lastNweeks: dataSetColumn.lastNweeks,
                    lastNmonths: dataSetColumn.lastNmonths,
                    lastNyears: dataSetColumn.lastNyears
                };
                console.log(data);
//                if (!dataSetColumn.dateRangeName && dataSetColumn.functionName == 'Custom') {
//                    scope.dateErrorMessage = true;
//                } else {
//                    scope.dateErrorMessage = false;
//                    $('.modal').modal('hide');
                $http({method: 'POST', url: 'admin/ui/dataSetFormulaColumns', data: JSON.stringify(data)}).success(function (response) {
                    console.log(response);
                    scope.ajaxLoadingCompleted = false;
                    scope.loadingTable = true;
                    dataSetColumn.id = "";
                    dataSetColumn.expression = "";
                    dataSetColumn.fieldName = "";
                    dataSetColumn.fieldType = "";
                    dataSetColumn.displayFormat = "";
                    dataSetColumn.functionName = "";
                    dataSetColumn.columnName = "";
                    dataSetColumn.dateRangeName = "";
                    dataSetColumn.customStartDate = $stateParams.startDate;
                    dataSetColumn.customEndDate = $stateParams.endDate;
                    dataSetColumn.baseField = "";
                    dataSetColumn.lastNdays = "";
                    dataSetColumn.lastNyears = "";
                    dataSetColumn.lastNweeks = "";
                    dataSetColumn.lastNmonths = "";
                    scope.dataSetItems();
                });
            }
//            };

            scope.editDataset = function (dataSetColumn) {
                console.log(dataSetColumn)
                if (dataSetColumn.customStartDate == "" && dataSetColumn.customStartDate == null && dataSetColumn.customEndDate == "" && dataSetColumn.customEndDate == null) {
                    dataSetColumn.customStartDate = $stateParams.startDate;
                    dataSetColumn.customEndDate = $stateParams.endDate;
                }
                var editData = {
                    id: dataSetColumn.id,
                    expression: dataSetColumn.expression,
                    fieldName: dataSetColumn.fieldName,
                    fieldType: dataSetColumn.fieldType,
                    displayFormat: dataSetColumn.displayFormat,
                    functionName: dataSetColumn.functionName,
                    columnName: dataSetColumn.columnName,
                    dateRangeName: dataSetColumn.dateRangeName,
                    customStartDate: dataSetColumn.customStartDate,
                    customEndDate: dataSetColumn.customEndDate,
                    baseField: dataSetColumn.baseField,
                    lastNdays: dataSetColumn.lastNdays,
                    lastNyears: dataSetColumn.lastNyears,
                    lastNweeks: dataSetColumn.lastNweeks,
                    lastNmonths: dataSetColumn.lastNmonths
                };
                console.log(editData);
                scope.dataSetColumn = editData;
            };

            scope.deleteDataset = function (dataSetColumn) {
                $http({method: "DELETE", url: 'admin/ui/dataSetFormulaColumns/' + dataSetColumn.id}).success(function (response) {
                    scope.ajaxLoadingCompleted = false;
                    scope.loadingTable = true;
                    scope.dataSetItems();
                });
            }

            scope.config = {
                autocomplete: [
                    {
                        words: [/[A-Za-z]+[_A-Za-z0-9]/gi],
                        cssClass: 'user'
                    }
                ],
                dropdown: [
                    {
                        trigger: /([A-Za-z]+[_A-Za-z0-9]+)/gi,
                        list: function (match, callback) {
                            // match is the regexp return, in this case it returns
                            // [0] the full match, [1] the first capture group => username
                            // Prepare the fake data
                            var listData = scope.tableColumns.filter(function (element) {
                                return element.displayName.substr(0, match[1].length).toLowerCase() === match[1].toLowerCase()
                                        && element.displayName.length > match[1].length;
                            }).map(function (element) {
                                return {
                                    display: element.displayName, // This gets displayed in the dropdown
                                    item: element // This will get passed to onSelect
                                };
                            });
                            callback(listData);
                        },
                        onSelect: function (item) {
                            return item.display;
                        },
                        mode: 'replace'
                    }
                ]
            };
        }
    };
});
app.directive('functionDateRange', function ($stateParams, $timeout) {
    return{
        restrict: 'A',
        scope: {
            widgetTableDateRange: '@',
        },
        link: function (scope, element, attr) {
            $(document).ready(function (e) {
                $(".scheduler-list-style").click(function (e) {
                    e.stopPropagation();
                });
                console.log(scope.widgetTableDateRange)
                var derivedColumn = scope.widgetTableDateRange;
                var columnStartDate = derivedColumn.customStartDate ? derivedColumn.customStartDate : $stateParams.startDate; //JSON.parse(scope.widgetTableDateRange).customStartDate;
                var columnEndDate = derivedColumn.customEndDate ? derivedColumn.customEndDate : $stateParams.endDate;
                ; //JSON.parse(scope.widgetTableDateRange).customEndDate;
                //Date range as a button
                $(element[0]).daterangepicker(
                        {
                            ranges: {
                                'Today': [moment(), moment()],
//                                'Yesterday': [moment().subtract(1, 'days'), moment().subtract(1, 'days')],
                                'Last 7 Days': [moment().subtract(7, 'days'), moment().subtract(1, 'days')],
                                'Last 14 Days ': [moment().subtract(14, 'days'), moment().subtract(1, 'days')],
                                'Last 30 Days': [moment().subtract(30, 'days'), moment().subtract(1, 'days')],
//                                'This Week (Sun - Today)': [moment().startOf('week'), moment().endOf(new Date())],
////                        'This Week (Mon - Today)': [moment().startOf('week').add(1, 'days'), moment().endOf(new Date())],
//                                'Last Week (Sun - Sat)': [moment().subtract(1, 'week').startOf('week'), moment().subtract(1, 'week').endOf('week')],
//                        'Last 2 Weeks (Sun - Sat)': [moment().subtract(2, 'week').startOf('week'), moment().subtract(1, 'week').endOf('week')],
//                        'Last Week (Mon - Sun)': [moment().subtract(1, 'week').startOf('week').add(1, 'days'), moment().subtract(1, 'week').add(1, 'days').endOf('week').add(1, 'days')],
//                        'Last Business Week (Mon - Fri)': [moment().subtract(1, 'week').startOf('week').add(1, 'days'), moment().subtract(1, 'week').add(1, 'days').endOf('week').subtract(1, 'days')],
                                'This Month': [moment().startOf('month'), moment().endOf(new Date())],
                                'Last Month': [moment().subtract(1, 'month').startOf('month'), moment().subtract(1, 'month').endOf('month')],
//                        'Last 2 Months': [moment().subtract(2, 'month').startOf('month'), moment().subtract(1, 'month').endOf('month')],
//                        'Last 3 Months' : [moment().subtract(3, 'month').startOf('month'), moment().subtract(1, 'month').endOf('month')],
                                'This Year': [moment().startOf('year'), moment().endOf(new Date())],
                                'Last Year': [moment().subtract(1, 'year').startOf('year'), moment().subtract(1, 'year').endOf('year')],
//                        'Last 2 Years': [moment().subtract(2, 'year').startOf('year'), moment().subtract(1, 'year').endOf('year')]
//                        'Last 3 Years': [moment().subtract(3, 'year').startOf('year'), moment().subtract(1, 'year').endOf('year')]
                            },
                            startDate: columnStartDate ? columnStartDate : moment().subtract(30, 'days'),
                            endDate: columnEndDate ? columnEndDate : moment().subtract(1, 'days'),
                            maxDate: new Date()
                        },
                        function (startDate, endDate) {
                            $('#widgetDateRange span').html(startDate.format('MM-DD-YYYY') + ' - ' + endDate.format('MM-DD-YYYY'));
                        }
                );
                $(".ranges ul").find("li").addClass("custom-picker");
                $(".custom-picker").click(function (e) {
                    $(".scheduler-list-style").hide();
                    scope.$apply();
                });
                $(".editWidgetDropDown").click(function (e) {
                    $(".scheduler-list-style").removeAttr("style");
                    $(".scheduler-list-style").css("display", "block");
                    $(".daterangepicker").css("display", "none");
//                        e.bind();
                });
                $(document).on("click", function (e) {
                    var selectedElement = e.target.className;
                    if (selectedElement == "custom-picker" ||
                            selectedElement == "fa fa-chevron-left glyphicon glyphicon-chevron-left" ||
                            selectedElement == "month" ||
                            selectedElement == "fa fa-chevron-right glyphicon glyphicon-chevron-right" ||
                            selectedElement == "next available" ||
                            selectedElement == "input-mini form-control active" ||
                            selectedElement == "calendar-table" || selectedElement == "table-condensed" ||
                            selectedElement == "daterangepicker_input")
                    {
                        $(".scheduler-list-style").css("display", "block");
                    } else {
                        $(".scheduler-list-style").css("display", "none");
                    }
                });
                $(".applyBtn").click(function (e) {
                    try {
                        scope.customStartDate = moment($('#widgetDateRange').data('daterangepicker').startDate).format('MM/DD/YYYY') ? moment($('#widgetDateRange').data('daterangepicker').startDate).format('MM/DD/YYYY') : widgetStartDate; //$scope.startDate.setDate($scope.startDate.getDate() - 1);
                        scope.customEndDate = moment($('#widgetDateRange').data('daterangepicker').endDate).format('MM/DD/YYYY') ? moment($('#widgetDateRange').data('daterangepicker').endDate).format('MM/DD/YYYY') : widgetEndDate;
                    } catch (e) {
                    }

                    $(".scheduler-list-style").hide(); //                    
                });
            });
        }
    };
});
