app.controller('DataSetController', function ($scope, $http, $stateParams, $filter, $timeout, localStorageService, $translate, $cookies) {
    $scope.permission = localStorageService.get("permission");
    $scope.dataSetFlag = false;
    $scope.nwStatusFlag = false;
    $scope.timeSegFlag = false;
    $scope.startDate = $stateParams.startDate;
    $scope.endDate = $stateParams.endDate;
    $scope.tab = 1;
    $scope.childTab = 3;
    $scope.setTab = function (newTab) {
        $scope.tab = newTab;
    };
    $scope.setChildTab = function (newTab) {
        $scope.childTab = newTab;
    };

    $scope.isSet = function (tabNum) {
        return $scope.tab === tabNum;
    };
    $scope.isChildSet = function (tabNum) {
        return $scope.childTab === tabNum;
    };

    $scope.selectAggregations = [
        {name: 'None', value: ""},
        {name: 'Sum', value: "sum"},
        {name: 'CTR', value: "ctr"},
        {name: 'CPC', value: "cpc"},
        {name: 'CPCS', value: "cpcs"},
        {name: 'CPS', value: "cps"},
        {name: 'CPA', value: "cpa"},
        {name: 'CPAS', value: "cpas"},
        {name: 'Avg', value: "avg"},
        {name: 'Count', value: "count"},
        {name: 'Min', value: "min"},
        {name: 'Max', value: "max"},
        {name: 'CPL', value: "cpl"},
        {name: 'CPLC', value: "cplc"},
        {name: 'CPComment', value: "cpcomment"},
        {name: 'CPostE', value: "cposte"},
        {name: 'CPageE', value: "cpagee"},
        {name: 'CPP', value: "cpp"},
        {name: 'CPR', value: "cpr"}
    ];
    $scope.fieldTypes = [
        {name: 'None', value: ''},
        {name: 'String', value: 'string'},
        {name: 'Number', value: 'number'},
        {name: 'Date', value: 'date'},
        {name: 'Day', value: 'day'}
    ];
    $scope.formats = [
        {name: "None", value: ''},
        {name: "Currency", value: '$,.2f'},
        {name: "Integer", value: ',.0f'},
        {name: "Percentage", value: ',.2%'},
        {name: "Decimal1", value: ',.1f'},
        {name: "Decimal2", value: ',.2f'},
        {name: "Time", value: 'H:M:S'},
        {name: "Star Rating", value: 'starRating'},
        {name: "Date", value: 'MM-dd-yyyy'}
    ];
    $scope.categories = [
        {name: "Metrics", value: "metrics"},
        {name: "Dimension", value: "dimension"}
    ];

    $scope.deleteField = function (index) {
        $scope.columnsHeaderDefs.splice(index, 1);
    };


    $scope.agencyLanguage = $stateParams.lan;//$cookies.getObject("agencyLanguage");

    var lan = $scope.agencyLanguage;
    changeLanguage(lan);

    function changeLanguage(key) {
        $translate.use(key);
    }

    $scope.joinTypes = [
        {name: 'Left', value: 'left'},
        {name: 'Right', value: 'right'},
        {name: 'Inner', value: 'inner'},
        {name: 'Union', value: 'union'}
    ];
    var url = "admin/proxy/getData?";

    function getPreviewDataSet(dataSet, selectType) {
        console.log(dataSet);
        var dataSourcePassword = '';
        var connectionUrl = null;
        var driver = null;
        var dataSourceType = null;
        var userName = null;
        var dataSourceId = null;
        if (dataSet.dataSourceId != null) {
            if (dataSet.dataSourceId.password) {
                dataSourcePassword = dataSet.dataSourceId.password;
            } else {
                dataSourcePassword = '';
            }

            connectionUrl = dataSet.dataSourceId.connectionString;
            dataSourceId = dataSet.dataSourceId.id;
            driver = dataSet.dataSourceId.dataSourceType;
            dataSourceType = dataSet.dataSourceId.dataSourceType;
            userName = dataSet.dataSourceId.userName;
        }
        var joinDataSetId = null;
        if (dataSet.joinDataSetId != null) {
            joinDataSetId = dataSet.joinDataSetId.id;
        }
        $http.get(url + 'connectionUrl=' + connectionUrl +
                "&dataSourceId=" + dataSourceId +
                "&dataSetId=" + dataSet.id +
                "&joinDataSetId=" + joinDataSetId +
                "&accountId=" + $stateParams.accountId +
                "&dataSetReportName=" + dataSet.reportName +
                "&timeSegment=" + dataSet.timeSegment +
                "&filter=" + dataSet.networkType +
                "&productSegment=" + dataSet.productSegment +
                "&driver=" + dataSourceType +
                "&dataSourceType=" + dataSourceType +
                "&location=" + $stateParams.locationId +
                "&startDate=" + $stateParams.startDate +
                "&endDate=" + $stateParams.endDate +
                '&username=' + userName +
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
        $scope.loadingResultCompleted = false;
        getPreviewDataSet($scope.firstDataSet, "dataSet1");
    };
    $scope.selectSecondDataSet = function (dataSet) {
        $scope.secondDataSet = JSON.parse(dataSet.secondDataSet);
        $scope.secondDataSetName = $scope.secondDataSet.name;
        $scope.dataSetIdSecond = $scope.secondDataSet.id;
        $scope.secondDataSetLoadingCompleted = false;
        $scope.secondDataSetLoading = true;
        $scope.loadingResultCompleted = false;
        getPreviewDataSet($scope.secondDataSet, "dataSet2");
    };
    $scope.selectFirstDataSetColumn = function (dataSetColumn) {
        if ($scope.operationType == 'union' || $scope.operationType == 'intersection') {
            dataSetColumn.columnName = dataSetColumn.conditionFieldFirst;
        }
    };
    $scope.dataSetColumnList = [];
    $scope.joinDataSetList = [];
    $scope.hideCondition = false;
    $scope.selectJoinType = function (joinDataSetColumn) {
        $scope.operationType = joinDataSetColumn.joinType;
        if (joinDataSetColumn.joinType != null) {
            $scope.hideCondition = true;
        }
    };

    $scope.addJoinColumnList = function () {
        $scope.dataSetColumnList.push({});
    };
    $scope.removeJoinDataSetColumn = function (index, conditionId) {
        $scope.dataSetColumnList.splice(index, 1);
        $http({method: 'DELETE', url: 'admin/ui/deleteJoinDataSetCondition/' + conditionId + "/" + joinDataSetId}).success(function (response) {
            $scope.joinDataSetList = response;
        });
    };

    var joinDataSetId = "";

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
            for (i = 0; i < $scope.dataSetColumnList.length; i++) {
                var conditionId = null;
                if (typeof ($scope.joinDataSetList[i]) !== "undefined") {
                    conditionId = $scope.joinDataSetList[i].id;
                }
                var conditionData = {
                    conditionFieldFirst: $scope.dataSetColumnList[i].conditionFieldFirst,
                    conditionFieldSecond: $scope.dataSetColumnList[i].conditionFieldSecond,
                    columnName: $scope.dataSetColumnList[i].columnName,
                    conditionId: conditionId
                };
                $scope.dataSetLists.push(conditionData);
            }
            $scope.dataSetColumnList = $scope.dataSetLists;
        }
        var data = {
            id: joinDataSetId,
            dataSetName: joinDataSetColumn.joinDataSetName,
            dataSetIdFirst: dataSetIdFirst,
            dataSetIdSecond: dataSetIdSecond,
            operationType: joinDataSetColumn.joinType,
            conditionFields: $scope.dataSetColumnList
        };
        $http({method: 'POST', url: 'admin/ui/joinDataSet', data: data}).success(function (response) {
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
            });
        });
    };
    $scope.cancelJoinDataSet = function () {
        $scope.joinDataSetColumn = "";
        joinDataSetId = "";
//        $scope.dataSetColumn = "";
        $scope.dataSetColumnList = [];
        $scope.hideCondition = false;
        $scope.secondDataSetLoadingCompleted = false;
        $scope.firstDataSetLoadingCompleted = false;
        $scope.loadingResultCompleted = false;
        $scope.loadingResult = false;
    };
    $scope.saveToDataSet = function (joinDataSetColumn) {
        var dataSet = JSON.parse(joinDataSetColumn.firstDataSet);
        var dataSourceId = null;
        var joinDataSource = {
            name: "Join DataSource",
            id: null,
            agencyId: dataSet.agencyId,
            userId: dataSet.userId
        };
        $http({method: 'POST', url: 'admin/ui/joinDataSource', data: joinDataSource}).success(function (response) {
            dataSourceId = response.id;

            var joinDataSetList = {
                agencyId: dataSet.agencyId,
                name: joinDataSetColumn.joinDataSetName,
                userId: dataSet.userId,
                joinDataSetId: joinDataSetId,
                dataSourceId: dataSourceId
            };
            var data = joinDataSetList;
            console.log(data);
            $http({method: 'POST', url: 'admin/ui/dataSet', data: data}).success(function (response) {
                $scope.cancelJoinDataSet();
                getItems();
                $scope.setTab(1);
            });
        });
    };
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
        },
        {
            type: 'All',
            name: 'All'
        },
        {
            type: 'none',
            name: 'None'
        }
    ];
    $scope.dataSetFlagValidation = function (dataSource)
    {
        $scope.reportSelected = true;
        $scope.enableMe = true;
        $scope.dataSet.reportName = "";
        if (dataSource === "adwords")
        {
            $scope.report = $scope.adwordsPerformance;
            $scope.dataSetFlag = true;
            $scope.nwStatusFlag = false;
            $scope.timeSegFlag = false;
            $scope.productSegFlag = false;
            $scope.semRushFlag = false;
        } else if (dataSource === "analytics")
        {
            $scope.report = $scope.analyticsPerformance;
            $scope.dataSetFlag = true;
            $scope.nwStatusFlag = false;
            $scope.timeSegFlag = false;
            $scope.productSegFlag = false;
            $scope.semRushFlag = false;
        } else if (dataSource === "facebook")
        {
            $scope.report = $scope.facebookPerformance;
            console.log($scope.report);
            $scope.dataSetFlag = true;
            $scope.nwStatusFlag = false;
            $scope.timeSegFlag = false;
            $scope.productSegFlag = false;
            $scope.semRushFlag = false;
        } else if (dataSource === "pinterest")
        {
            $scope.report = $scope.pinterestPerformance;
            $scope.dataSetFlag = true;
            $scope.nwStatusFlag = false;
            $scope.timeSegFlag = false;
            $scope.productSegFlag = false;
            $scope.semRushFlag = false;
        } else if (dataSource === "instagram")
        {
            $scope.report = $scope.instagramPerformance;
            $scope.dataSetFlag = true;
            $scope.nwStatusFlag = false;
            $scope.timeSegFlag = false;
        } else if (dataSource === "twitter")
        {
            $scope.report = $scope.twitterPerformance;
            $scope.dataSetFlag = true;
            $scope.nwStatusFlag = false;
            $scope.timeSegFlag = false;
        } else if (dataSource === "googlePlus")
        {
            $scope.report = $scope.googlePlusPerformance;
            $scope.dataSetFlag = true;
            $scope.nwStatusFlag = false;
            $scope.timeSegFlag = false;

        } else if (dataSource === "pinterest")
        {
            $scope.report = $scope.pinterestPerformance;
            $scope.dataSetFlag = true;
            $scope.nwStatusFlag = false;
            $scope.timeSegFlag = false;
            $scope.semRushFlag = false;
        } else if (dataSource === "linkedin")
        {
            $scope.report = $scope.linkedinPerformance;
            $scope.dataSetFlag = true;
            $scope.nwStatusFlag = false;
            $scope.timeSegFlag = false;
            $scope.productSegFlag = false;
            $scope.semRushFlag = false;
        } else if (dataSource === "bing")
        {
            $scope.report = $scope.bingPerformance;
            $scope.dataSetFlag = true;
            $scope.nwStatusFlag = false;
            $scope.semRushFlag = false;
        } else if (dataSource === "semRush")
        {
            $scope.report = $scope.semRush;
            console.log($scope.report);
            $scope.productSegFlag = false;
            $scope.dataSetFlag = true;
            $scope.nwStatusFlag = false;
            $scope.timeSegFlag = false;
            $scope.semRushFlag = true;
            $scope.semFreqFlag = false;
        } else {
            $scope.dataSetFlag = false;
            $scope.nwStatusFlag = false;
            $scope.semRushFlag = false;
            $scope.semFreqFlag = true;
        }
    };
    $scope.linkedinPerformance = [
        {
            type: 'companyProfile',
            name: 'Company Profile',
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
            type: 'pagePerformance',
            name: 'Page Performance',
            timeSegments: [
                {
                    type: 'month',
                    name: 'Month'
                }
            ],
            productSegments: [
                {
                    type: 'none',
                    name: 'None'
                }
            ]
        }, {
            type: 'postPerformance',
            name: 'Post Performance',
            timeSegments: [
                {
                    type: 'none',
                    name: 'None'
                }
            ],
            productSegments: [
                {
                    type: 'overall',
                    name: 'Over All'
                }, {
                    type: 'recentPosts',
                    name: 'Recent Posts'
                }, {
                    type: 'postType',
                    name: 'Post Type'
                }
            ]
        }, {
            type: 'pageFollowersPerformance',
            name: 'Page Followers Performance Report',
            timeSegments: [
                {
                    type: 'day',
                    name: 'Day'
                }, {
                    type: 'month',
                    name: 'Month'
                }, {
                    type: 'none',
                    name: 'None'
                }
            ],
            productSegments: [
                {
                    type: 'overall',
                    name: 'Over All'
                }, {
                    type: 'countries',
                    name: 'Countries'
                }, {
                    type: 'regions',
                    name: 'Regions'
                }, {
                    type: 'functions',
                    name: 'Job Function'
                }, {
                    type: 'seniorities',
                    name: 'Seniorities'
                }, {
                    type: 'industries',
                    name: 'Industries'
                }, {
                    type: 'companySizes',
                    name: 'Company Size'
                }, {
                    type: 'employmentStatus',
                    name: 'Employment Status'
                }, {
                    type: 'historicalPageFollowers',
                    name: 'Historical Followers'
                }, {
                    type: 'none',
                    name: 'None'
                }
            ]
        }
    ];

    $scope.twitterPerformance = [
        {
            type: 'pagePerformance',
            name: 'Page Performance',
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
            type: 'screenName',
            name: 'Screen Name',
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
            type: 'userTimeLine',
            name: 'User Performance Metrics',
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
    ];

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
                }
            ]
        }
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
        }
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
                    type: 'ga:hour',
                    name: 'Hour of Day'
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
                    type: 'ga:dayOfWeekName',
                    name: 'Day of Week'
                },
                {
                    type: 'ga:hour',
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

            productSegments: [
                {
                    type: 'none',
                    name: 'None'
                }
            ]
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
    $scope.semRush = [
        {
            type: 'overview',
            name: 'Overview Report',
            timeSegments: [
                {
                    name: "All Databases",
                    type: "domain_ranks"
                }, {
                    name: "One Database",
                    type: "domain_rank"
                }, {
                    name: "History",
                    type: "domain_rank_history"
                }, {
                    name: "Winners and Losers",
                    type: "rank_difference"
                }, {
                    name: "SEMrush Rank",
                    type: "rank"
                }
            ],
            productSegments: [
                {
                    name: "google.com",
                    type: "us"
                },
                {
                    name: "google.co.uk",
                    type: "uk"
                },
                {
                    name: "google.ca",
                    type: "ca"
                }, {
                    name: "google.ru",
                    type: "ru"
                },
                {
                    name: "google.de",
                    type: "de"
                }, {
                    name: "google.fr",
                    type: "fr"
                },
                {
                    name: "google.es",
                    type: "es"
                },
                {
                    name: "google.it",
                    type: "it"
                }, {
                    name: "google.com.br",
                    type: "br"
                },
                {
                    name: "google.com.au",
                    type: "au"
                },
                {
                    name: "bing.com",
                    type: "bing-us"
                },
                {
                    name: "google.com.ar",
                    type: "ar"
                },
                {
                    name: "google.be",
                    type: "be"
                },
                {
                    name: "google.ch",
                    type: "ch"
                },
                {
                    name: "google.dk",
                    type: "dk"
                },
                {
                    name: "google.fi",
                    type: "fi"
                },
                {
                    name: "google.com.hk",
                    type: "hk"
                },
                {
                    name: "google.ie",
                    type: "ie"
                },
                {
                    name: "google.co.il",
                    type: "il"
                },
                {
                    name: "google.com.mx",
                    type: "mx"
                },
                {
                    name: "google.nl",
                    type: "nl"
                },
                {
                    name: "google.no",
                    type: "no"
                }, {
                    name: "google.pl",
                    type: "pl"
                },
                {
                    name: "google.se",
                    type: "se"
                },
                {
                    name: "google.com.sg",
                    type: "sg"
                }, {
                    name: "google.com.tr",
                    type: "tr"
                },
                {
                    name: "google.com.us-mobile",
                    type: "mobile-us"
                },
                {
                    name: "google.co.jp",
                    type: "jp"
                },
                {
                    name: "google.co.in",
                    type: "in"
                },
                {
                    name: "google.hu",
                    type: "hu"
                },
                {
                    name: "google.af",
                    type: "af"
                },
                {
                    name: "google.al",
                    type: "al"
                }, {
                    name: "google.dz",
                    type: "dz"
                },
                {
                    name: "google.ao",
                    type: "ao"
                }, {
                    name: "google.am",
                    type: "am"
                },
                {
                    name: "google.at",
                    type: "at"
                },
                {
                    name: "google.az",
                    type: "az"
                },
                {
                    name: "google.bh",
                    type: "bh"
                },
                {
                    name: "google.bd",
                    type: "bd"
                },
                {
                    name: "google.by",
                    type: "by"
                },
                {
                    name: "google.bz",
                    type: "bz"
                }, {
                    name: "google.bo",
                    type: "bo"
                },
                {
                    name: "google.ba",
                    type: "ba"
                }, {
                    name: "google.bw",
                    type: "bw"
                },
                {
                    name: "google.bn",
                    type: "bn"
                },
                {
                    name: "google.bg",
                    type: "bg"
                },
                {
                    name: "google.cv",
                    type: "cv"
                },
                {
                    name: "google.kh",
                    type: "kh"
                },
                {
                    name: "google.kh",
                    type: "kh"
                },
                {
                    name: "google.cm",
                    type: "cm"
                }, {
                    name: "google.cl",
                    type: "cl"
                },
                {
                    name: "google.co",
                    type: "co"
                }, {
                    name: "google.cr",
                    type: "cr"
                },
                {
                    name: "google.hr",
                    type: "hr"
                },
                {
                    name: "google.cy",
                    type: "cy"
                },
                {
                    name: "google.cz",
                    type: "cz"
                },
                {
                    name: "google.cd",
                    type: "cd"
                },
                {
                    name: "google.do",
                    type: "do"
                },
                {
                    name: "google.ec",
                    type: "ec"
                }, {
                    name: "google.eg",
                    type: "eg"
                }, {
                    name: "google.sv",
                    type: "sv"
                }, {
                    name: "google.bw",
                    type: "bw"
                },
                {
                    name: "google.ee",
                    type: "ee"
                },
                {
                    name: "google.et",
                    type: "et"
                },
                {
                    name: "google.ge",
                    type: "ge"
                },
                {
                    name: "google.gh",
                    type: "gh"
                },
                {
                    name: "google.gr",
                    type: "gr"
                },
                {
                    name: "google.gt",
                    type: "gt"
                }, {
                    name: "google.gt",
                    type: "gt"
                }, {
                    name: "google.gy",
                    type: "gy"
                }, {
                    name: "google.ht",
                    type: "ht"
                },
                {
                    name: "google.hn",
                    type: "hn"
                },
                {
                    name: "google.is",
                    type: "is"
                },
                {
                    name: "google.id",
                    type: "id"
                },
                {
                    name: "google.jm",
                    type: "jm"
                },
                {
                    name: "google.jo",
                    type: "jo"
                },
                {
                    name: "google.kz",
                    type: "kz"
                }, {
                    name: "google.lv",
                    type: "lv"
                }, {
                    name: "google.lb",
                    type: "lb"
                }, {
                    name: "google.lt",
                    type: "lt"
                },
                {
                    name: "google.lu",
                    type: "lu"
                },
                {
                    name: "google.mg",
                    type: "mg"
                },
                {
                    name: "google.my",
                    type: "my"
                },
                {
                    name: "google.mt",
                    type: "mt"
                },
                {
                    name: "google.mu",
                    type: "mu"
                },
                {
                    name: "google.md",
                    type: "md"
                }, {
                    name: "google.mn",
                    type: "mn"
                }, {
                    name: "google.me",
                    type: "me"
                }, {
                    name: "google.ma",
                    type: "ma"
                },
                {
                    name: "google.mz",
                    type: "na"
                },
                {
                    name: "google.np",
                    type: "np"
                },
                {
                    name: "google.nz",
                    type: "nz"
                },
                {
                    name: "google.ni",
                    type: "ni"
                },
                {
                    name: "google.ng",
                    type: "ng"
                },
                {
                    name: "google.om",
                    type: "om"
                }, {
                    name: "google.py",
                    type: "py"
                }, {
                    name: "google.pe",
                    type: "pe"
                }, {
                    name: "google.ph",
                    type: "ph"
                },
                {
                    name: "google.pt",
                    type: "pt"
                },
                {
                    name: "google.ro",
                    type: "ro"
                },
                {
                    name: "google.sa",
                    type: "sa"
                },
                {
                    name: "google.sn",
                    type: "sn"
                },
                {
                    name: "google.rs",
                    type: "rs"
                },
                {
                    name: "google.sk",
                    type: "sk"
                }, {
                    name: "google.si",
                    type: "si"
                },
                {
                    name: "google.za",
                    type: "za"
                },
                {
                    name: "google.kr",
                    type: "kr"
                },
                {
                    name: "google.lk",
                    type: "lk"
                },
                {
                    name: "google.th",
                    type: "th"
                },
                {
                    name: "google.bs",
                    type: "bs"
                }, {
                    name: "google.tt",
                    type: "tt"
                }, {
                    name: "google.tn",
                    type: "tn"
                }, {
                    name: "google.ua",
                    type: "ua"
                },
                {
                    name: "google.ae",
                    type: "ae"
                },
                {
                    name: "google.uy",
                    type: "uy"
                },
                {
                    name: "google.ve",
                    type: "ve"
                },
                {
                    name: "google.vn",
                    type: "vn"
                },
                {
                    name: "google.zm",
                    type: "zm"
                },
                {
                    name: "google.zw",
                    type: "zw"
                }, {
                    name: "google.ly",
                    type: "ly"
                }, {
                    name: "google.com.uk-mobile",
                    type: "mobile-uk"
                }, {
                    name: "google.ca-mobile",
                    type: "mobile-ca"
                },
                {
                    name: "google.de-mobile",
                    type: "mobile-uk"
                },
                {
                    name: "google.fr-mobile",
                    type: "mobile-fr"
                },
                {
                    name: "google.es-mobile",
                    type: "mobile-es"
                },
                {
                    name: "google.it-mobile",
                    type: "mobile-it"
                },
                {
                    name: "google.com.br-mobile",
                    type: "mobile-br"
                },
                {
                    name: "google.com.au-mobile",
                    type: "mobile-au"
                }, {
                    name: "google.dk-mobile",
                    type: "mobile-dk"
                },
                {
                    name: "google.com.mx-mobile",
                    type: "mobile-mx"
                },
                {
                    name: "google.nl",
                    type: "mobile-nl"
                }, {
                    name: "google.se",
                    type: "mobile-se"
                }, {
                    name: "google.com.tr-mobile",
                    type: "mobile-tr"
                },

                {
                    name: "google.co.in",
                    type: "mobile-in"
                }, {
                    name: "google.co-id",
                    type: "mobile-id"
                }, {
                    name: "google.co.il",
                    type: "mobile-il"
                }
            ]
        }, {
            type: 'domain',
            name: 'Domain Report',
            timeSegments: [
                {
                    name: "Domain Organic Search Keywords",
                    type: "domain_organic"
                }, {
                    name: "Domain Paid Search Keywords",
                    type: "domain_adwords"
                }, {
                    name: "Ads Copies",
                    type: "domain_adwords_unique"
                }, {
                    name: "Competitors in Organic Search",
                    type: "domain_organic_organic"
                }, {
                    name: "Competitors in Paid Search",
                    type: "domain_adwords_adwords"
                }, {
                    name: " Domain Ads History",
                    type: "domain_adwords_historical"
                }, {
                    name: "Domain vs. Domain",
                    type: "domain_domains"
                }, {
                    name: "Domain PLA Search Keywords",
                    type: "domain_shopping"
                },
                {
                    name: "PLA Copies",
                    type: "domain_shopping_unique"
                }, {
                    name: "PLA Competitors",
                    type: "domain_shopping_shopping"
                }
            ],
            productSegments: [
                {
                    name: "google.com",
                    type: "us"
                },
                {
                    name: "google.co.uk",
                    type: "uk"
                },
                {
                    name: "google.ca",
                    type: "ca"
                }, {
                    name: "google.ru",
                    type: "ru"
                },
                {
                    name: "google.de",
                    type: "de"
                }, {
                    name: "google.fr",
                    type: "fr"
                },
                {
                    name: "google.es",
                    type: "es"
                },
                {
                    name: "google.it",
                    type: "it"
                }, {
                    name: "google.com.br",
                    type: "br"
                },
                {
                    name: "google.com.au",
                    type: "au"
                },
                {
                    name: "bing.com",
                    type: "bing-us"
                },
                {
                    name: "google.com.ar",
                    type: "ar"
                },
                {
                    name: "google.be",
                    type: "be"
                },
                {
                    name: "google.ch",
                    type: "ch"
                },
                {
                    name: "google.dk",
                    type: "dk"
                },
                {
                    name: "google.fi",
                    type: "fi"
                },
                {
                    name: "google.com.hk",
                    type: "hk"
                },
                {
                    name: "google.ie",
                    type: "ie"
                },
                {
                    name: "google.co.il",
                    type: "il"
                },
                {
                    name: "google.com.mx",
                    type: "mx"
                },
                {
                    name: "google.nl",
                    type: "nl"
                },
                {
                    name: "google.no",
                    type: "no"
                }, {
                    name: "google.pl",
                    type: "pl"
                },
                {
                    name: "google.se",
                    type: "se"
                },
                {
                    name: "google.com.sg",
                    type: "sg"
                }, {
                    name: "google.com.tr",
                    type: "tr"
                },
                {
                    name: "google.com.us-mobile",
                    type: "mobile-us"
                },
                {
                    name: "google.co.jp",
                    type: "jp"
                },
                {
                    name: "google.co.in",
                    type: "in"
                },
                {
                    name: "google.hu",
                    type: "hu"
                },
                {
                    name: "google.af",
                    type: "af"
                },
                {
                    name: "google.al",
                    type: "al"
                }, {
                    name: "google.dz",
                    type: "dz"
                },
                {
                    name: "google.ao",
                    type: "ao"
                }, {
                    name: "google.am",
                    type: "am"
                },
                {
                    name: "google.at",
                    type: "at"
                },
                {
                    name: "google.az",
                    type: "az"
                },
                {
                    name: "google.bh",
                    type: "bh"
                },
                {
                    name: "google.bd",
                    type: "bd"
                },
                {
                    name: "google.by",
                    type: "by"
                },
                {
                    name: "google.bz",
                    type: "bz"
                }, {
                    name: "google.bo",
                    type: "bo"
                },
                {
                    name: "google.ba",
                    type: "ba"
                }, {
                    name: "google.bw",
                    type: "bw"
                },
                {
                    name: "google.bn",
                    type: "bn"
                },
                {
                    name: "google.bg",
                    type: "bg"
                },
                {
                    name: "google.cv",
                    type: "cv"
                },
                {
                    name: "google.kh",
                    type: "kh"
                },
                {
                    name: "google.kh",
                    type: "kh"
                },
                {
                    name: "google.cm",
                    type: "cm"
                }, {
                    name: "google.cl",
                    type: "cl"
                },
                {
                    name: "google.co",
                    type: "co"
                }, {
                    name: "google.cr",
                    type: "cr"
                },
                {
                    name: "google.hr",
                    type: "hr"
                },
                {
                    name: "google.cy",
                    type: "cy"
                },
                {
                    name: "google.cz",
                    type: "cz"
                },
                {
                    name: "google.cd",
                    type: "cd"
                },
                {
                    name: "google.do",
                    type: "do"
                },
                {
                    name: "google.ec",
                    type: "ec"
                }, {
                    name: "google.eg",
                    type: "eg"
                }, {
                    name: "google.sv",
                    type: "sv"
                }, {
                    name: "google.bw",
                    type: "bw"
                },
                {
                    name: "google.ee",
                    type: "ee"
                },
                {
                    name: "google.et",
                    type: "et"
                },
                {
                    name: "google.ge",
                    type: "ge"
                },
                {
                    name: "google.gh",
                    type: "gh"
                },
                {
                    name: "google.gr",
                    type: "gr"
                },
                {
                    name: "google.gt",
                    type: "gt"
                }, {
                    name: "google.gt",
                    type: "gt"
                }, {
                    name: "google.gy",
                    type: "gy"
                }, {
                    name: "google.ht",
                    type: "ht"
                },
                {
                    name: "google.hn",
                    type: "hn"
                },
                {
                    name: "google.is",
                    type: "is"
                },
                {
                    name: "google.id",
                    type: "id"
                },
                {
                    name: "google.jm",
                    type: "jm"
                },
                {
                    name: "google.jo",
                    type: "jo"
                },
                {
                    name: "google.kz",
                    type: "kz"
                }, {
                    name: "google.lv",
                    type: "lv"
                }, {
                    name: "google.lb",
                    type: "lb"
                }, {
                    name: "google.lt",
                    type: "lt"
                },
                {
                    name: "google.lu",
                    type: "lu"
                },
                {
                    name: "google.mg",
                    type: "mg"
                },
                {
                    name: "google.my",
                    type: "my"
                },
                {
                    name: "google.mt",
                    type: "mt"
                },
                {
                    name: "google.mu",
                    type: "mu"
                },
                {
                    name: "google.md",
                    type: "md"
                }, {
                    name: "google.mn",
                    type: "mn"
                }, {
                    name: "google.me",
                    type: "me"
                }, {
                    name: "google.ma",
                    type: "ma"
                },
                {
                    name: "google.mz",
                    type: "na"
                },
                {
                    name: "google.np",
                    type: "np"
                },
                {
                    name: "google.nz",
                    type: "nz"
                },
                {
                    name: "google.ni",
                    type: "ni"
                },
                {
                    name: "google.ng",
                    type: "ng"
                },
                {
                    name: "google.om",
                    type: "om"
                }, {
                    name: "google.py",
                    type: "py"
                }, {
                    name: "google.pe",
                    type: "pe"
                }, {
                    name: "google.ph",
                    type: "ph"
                },
                {
                    name: "google.pt",
                    type: "pt"
                },
                {
                    name: "google.ro",
                    type: "ro"
                },
                {
                    name: "google.sa",
                    type: "sa"
                },
                {
                    name: "google.sn",
                    type: "sn"
                },
                {
                    name: "google.rs",
                    type: "rs"
                },
                {
                    name: "google.sk",
                    type: "sk"
                }, {
                    name: "google.si",
                    type: "si"
                },
                {
                    name: "google.za",
                    type: "za"
                },
                {
                    name: "google.kr",
                    type: "kr"
                },
                {
                    name: "google.lk",
                    type: "lk"
                },
                {
                    name: "google.th",
                    type: "th"
                },
                {
                    name: "google.bs",
                    type: "bs"
                }, {
                    name: "google.tt",
                    type: "tt"
                }, {
                    name: "google.tn",
                    type: "tn"
                }, {
                    name: "google.ua",
                    type: "ua"
                },
                {
                    name: "google.ae",
                    type: "ae"
                },
                {
                    name: "google.uy",
                    type: "uy"
                },
                {
                    name: "google.ve",
                    type: "ve"
                },
                {
                    name: "google.vn",
                    type: "vn"
                },
                {
                    name: "google.zm",
                    type: "zm"
                },
                {
                    name: "google.zw",
                    type: "zw"
                }, {
                    name: "google.ly",
                    type: "ly"
                }, {
                    name: "google.com.uk-mobile",
                    type: "mobile-uk"
                }, {
                    name: "google.ca-mobile",
                    type: "mobile-ca"
                },
                {
                    name: "google.de-mobile",
                    type: "mobile-uk"
                },
                {
                    name: "google.fr-mobile",
                    type: "mobile-fr"
                },
                {
                    name: "google.es-mobile",
                    type: "mobile-es"
                },
                {
                    name: "google.it-mobile",
                    type: "mobile-it"
                },
                {
                    name: "google.com.br-mobile",
                    type: "mobile-br"
                },
                {
                    name: "google.com.au-mobile",
                    type: "mobile-au"
                }, {
                    name: "google.dk-mobile",
                    type: "mobile-dk"
                },
                {
                    name: "google.com.mx-mobile",
                    type: "mobile-mx"
                },
                {
                    name: "google.nl",
                    type: "mobile-nl"
                }, {
                    name: "google.se",
                    type: "mobile-se"
                }, {
                    name: "google.com.tr-mobile",
                    type: "mobile-tr"
                },

                {
                    name: "google.co.in",
                    type: "mobile-in"
                }, {
                    name: "google.co-id",
                    type: "mobile-id"
                }, {
                    name: "google.co.il",
                    type: "mobile-il"
                }
            ]
        }, {
            type: 'keyword',
            name: 'Keyword Report',
            timeSegments: [
                {
                    name: "Keyword Overview - All Databases",
                    type: "phrase_all"
                }, {
                    name: "Keyword Overview - One Database",
                    type: "phrase_this"
                }, {
                    name: " Organic Results",
                    type: "phrase_organic"
                }, {
                    name: "Paid Results",
                    type: "phrase_adwords"
                }, {
                    name: "Related Keywords",
                    type: "phrase_related"
                }, {
                    name: "Keyword Ads History",
                    type: "phrase_adwords_historical"
                }, {
                    name: "Phrase Match Keywords",
                    type: "phrase_fullsearch"
                }, {
                    name: "Keyword Difficulty",
                    type: "phrase_kdi"
                }
            ],
            productSegments: [
                {
                    name: "google.com",
                    type: "us"
                },
                {
                    name: "google.co.uk",
                    type: "uk"
                },
                {
                    name: "google.ca",
                    type: "ca"
                }, {
                    name: "google.ru",
                    type: "ru"
                },
                {
                    name: "google.de",
                    type: "de"
                }, {
                    name: "google.fr",
                    type: "fr"
                },
                {
                    name: "google.es",
                    type: "es"
                },
                {
                    name: "google.it",
                    type: "it"
                }, {
                    name: "google.com.br",
                    type: "br"
                },
                {
                    name: "google.com.au",
                    type: "au"
                },
                {
                    name: "bing.com",
                    type: "bing-us"
                },
                {
                    name: "google.com.ar",
                    type: "ar"
                },
                {
                    name: "google.be",
                    type: "be"
                },
                {
                    name: "google.ch",
                    type: "ch"
                },
                {
                    name: "google.dk",
                    type: "dk"
                },
                {
                    name: "google.fi",
                    type: "fi"
                },
                {
                    name: "google.com.hk",
                    type: "hk"
                },
                {
                    name: "google.ie",
                    type: "ie"
                },
                {
                    name: "google.co.il",
                    type: "il"
                },
                {
                    name: "google.com.mx",
                    type: "mx"
                },
                {
                    name: "google.nl",
                    type: "nl"
                },
                {
                    name: "google.no",
                    type: "no"
                }, {
                    name: "google.pl",
                    type: "pl"
                },
                {
                    name: "google.se",
                    type: "se"
                },
                {
                    name: "google.com.sg",
                    type: "sg"
                }, {
                    name: "google.com.tr",
                    type: "tr"
                },
                {
                    name: "google.com.us-mobile",
                    type: "mobile-us"
                },
                {
                    name: "google.co.jp",
                    type: "jp"
                },
                {
                    name: "google.co.in",
                    type: "in"
                },
                {
                    name: "google.hu",
                    type: "hu"
                },
                {
                    name: "google.af",
                    type: "af"
                },
                {
                    name: "google.al",
                    type: "al"
                }, {
                    name: "google.dz",
                    type: "dz"
                },
                {
                    name: "google.ao",
                    type: "ao"
                }, {
                    name: "google.am",
                    type: "am"
                },
                {
                    name: "google.at",
                    type: "at"
                },
                {
                    name: "google.az",
                    type: "az"
                },
                {
                    name: "google.bh",
                    type: "bh"
                },
                {
                    name: "google.bd",
                    type: "bd"
                },
                {
                    name: "google.by",
                    type: "by"
                },
                {
                    name: "google.bz",
                    type: "bz"
                }, {
                    name: "google.bo",
                    type: "bo"
                },
                {
                    name: "google.ba",
                    type: "ba"
                }, {
                    name: "google.bw",
                    type: "bw"
                },
                {
                    name: "google.bn",
                    type: "bn"
                },
                {
                    name: "google.bg",
                    type: "bg"
                },
                {
                    name: "google.cv",
                    type: "cv"
                },
                {
                    name: "google.kh",
                    type: "kh"
                },
                {
                    name: "google.kh",
                    type: "kh"
                },
                {
                    name: "google.cm",
                    type: "cm"
                }, {
                    name: "google.cl",
                    type: "cl"
                },
                {
                    name: "google.co",
                    type: "co"
                }, {
                    name: "google.cr",
                    type: "cr"
                },
                {
                    name: "google.hr",
                    type: "hr"
                },
                {
                    name: "google.cy",
                    type: "cy"
                },
                {
                    name: "google.cz",
                    type: "cz"
                },
                {
                    name: "google.cd",
                    type: "cd"
                },
                {
                    name: "google.do",
                    type: "do"
                },
                {
                    name: "google.ec",
                    type: "ec"
                }, {
                    name: "google.eg",
                    type: "eg"
                }, {
                    name: "google.sv",
                    type: "sv"
                }, {
                    name: "google.bw",
                    type: "bw"
                },
                {
                    name: "google.ee",
                    type: "ee"
                },
                {
                    name: "google.et",
                    type: "et"
                },
                {
                    name: "google.ge",
                    type: "ge"
                },
                {
                    name: "google.gh",
                    type: "gh"
                },
                {
                    name: "google.gr",
                    type: "gr"
                },
                {
                    name: "google.gt",
                    type: "gt"
                }, {
                    name: "google.gt",
                    type: "gt"
                }, {
                    name: "google.gy",
                    type: "gy"
                }, {
                    name: "google.ht",
                    type: "ht"
                },
                {
                    name: "google.hn",
                    type: "hn"
                },
                {
                    name: "google.is",
                    type: "is"
                },
                {
                    name: "google.id",
                    type: "id"
                },
                {
                    name: "google.jm",
                    type: "jm"
                },
                {
                    name: "google.jo",
                    type: "jo"
                },
                {
                    name: "google.kz",
                    type: "kz"
                }, {
                    name: "google.lv",
                    type: "lv"
                }, {
                    name: "google.lb",
                    type: "lb"
                }, {
                    name: "google.lt",
                    type: "lt"
                },
                {
                    name: "google.lu",
                    type: "lu"
                },
                {
                    name: "google.mg",
                    type: "mg"
                },
                {
                    name: "google.my",
                    type: "my"
                },
                {
                    name: "google.mt",
                    type: "mt"
                },
                {
                    name: "google.mu",
                    type: "mu"
                },
                {
                    name: "google.md",
                    type: "md"
                }, {
                    name: "google.mn",
                    type: "mn"
                }, {
                    name: "google.me",
                    type: "me"
                }, {
                    name: "google.ma",
                    type: "ma"
                },
                {
                    name: "google.mz",
                    type: "na"
                },
                {
                    name: "google.np",
                    type: "np"
                },
                {
                    name: "google.nz",
                    type: "nz"
                },
                {
                    name: "google.ni",
                    type: "ni"
                },
                {
                    name: "google.ng",
                    type: "ng"
                },
                {
                    name: "google.om",
                    type: "om"
                }, {
                    name: "google.py",
                    type: "py"
                }, {
                    name: "google.pe",
                    type: "pe"
                }, {
                    name: "google.ph",
                    type: "ph"
                },
                {
                    name: "google.pt",
                    type: "pt"
                },
                {
                    name: "google.ro",
                    type: "ro"
                },
                {
                    name: "google.sa",
                    type: "sa"
                },
                {
                    name: "google.sn",
                    type: "sn"
                },
                {
                    name: "google.rs",
                    type: "rs"
                },
                {
                    name: "google.sk",
                    type: "sk"
                }, {
                    name: "google.si",
                    type: "si"
                },
                {
                    name: "google.za",
                    type: "za"
                },
                {
                    name: "google.kr",
                    type: "kr"
                },
                {
                    name: "google.lk",
                    type: "lk"
                },
                {
                    name: "google.th",
                    type: "th"
                },
                {
                    name: "google.bs",
                    type: "bs"
                }, {
                    name: "google.tt",
                    type: "tt"
                }, {
                    name: "google.tn",
                    type: "tn"
                }, {
                    name: "google.ua",
                    type: "ua"
                },
                {
                    name: "google.ae",
                    type: "ae"
                },
                {
                    name: "google.uy",
                    type: "uy"
                },
                {
                    name: "google.ve",
                    type: "ve"
                },
                {
                    name: "google.vn",
                    type: "vn"
                },
                {
                    name: "google.zm",
                    type: "zm"
                },
                {
                    name: "google.zw",
                    type: "zw"
                }, {
                    name: "google.ly",
                    type: "ly"
                }, {
                    name: "google.com.uk-mobile",
                    type: "mobile-uk"
                }, {
                    name: "google.ca-mobile",
                    type: "mobile-ca"
                },
                {
                    name: "google.de-mobile",
                    type: "mobile-uk"
                },
                {
                    name: "google.fr-mobile",
                    type: "mobile-fr"
                },
                {
                    name: "google.es-mobile",
                    type: "mobile-es"
                },
                {
                    name: "google.it-mobile",
                    type: "mobile-it"
                },
                {
                    name: "google.com.br-mobile",
                    type: "mobile-br"
                },
                {
                    name: "google.com.au-mobile",
                    type: "mobile-au"
                }, {
                    name: "google.dk-mobile",
                    type: "mobile-dk"
                },
                {
                    name: "google.com.mx-mobile",
                    type: "mobile-mx"
                },
                {
                    name: "google.nl",
                    type: "mobile-nl"
                }, {
                    name: "google.se",
                    type: "mobile-se"
                }, {
                    name: "google.com.tr-mobile",
                    type: "mobile-tr"
                },

                {
                    name: "google.co.in",
                    type: "mobile-in"
                }, {
                    name: "google.co-id",
                    type: "mobile-id"
                }, {
                    name: "google.co.il",
                    type: "mobile-il"
                }
            ]
        }, {
            type: 'url',
            name: 'Url Report',
            timeSegments: [
                {
                    name: "URL Organic Search Keywords",
                    type: "url_organic"
                }, {
                    name: "URL Paid Search Keywords",
                    type: "url_adwords"
                }
            ],
            productSegments: [
                {
                    name: "google.com",
                    type: "us"
                },
                {
                    name: "google.co.uk",
                    type: "uk"
                },
                {
                    name: "google.ca",
                    type: "ca"
                }, {
                    name: "google.ru",
                    type: "ru"
                },
                {
                    name: "google.de",
                    type: "de"
                }, {
                    name: "google.fr",
                    type: "fr"
                },
                {
                    name: "google.es",
                    type: "es"
                },
                {
                    name: "google.it",
                    type: "it"
                }, {
                    name: "google.com.br",
                    type: "br"
                },
                {
                    name: "google.com.au",
                    type: "au"
                },
                {
                    name: "bing.com",
                    type: "bing-us"
                },
                {
                    name: "google.com.ar",
                    type: "ar"
                },
                {
                    name: "google.be",
                    type: "be"
                },
                {
                    name: "google.ch",
                    type: "ch"
                },
                {
                    name: "google.dk",
                    type: "dk"
                },
                {
                    name: "google.fi",
                    type: "fi"
                },
                {
                    name: "google.com.hk",
                    type: "hk"
                },
                {
                    name: "google.ie",
                    type: "ie"
                },
                {
                    name: "google.co.il",
                    type: "il"
                },
                {
                    name: "google.com.mx",
                    type: "mx"
                },
                {
                    name: "google.nl",
                    type: "nl"
                },
                {
                    name: "google.no",
                    type: "no"
                }, {
                    name: "google.pl",
                    type: "pl"
                },
                {
                    name: "google.se",
                    type: "se"
                },
                {
                    name: "google.com.sg",
                    type: "sg"
                }, {
                    name: "google.com.tr",
                    type: "tr"
                },
                {
                    name: "google.com.us-mobile",
                    type: "mobile-us"
                },
                {
                    name: "google.co.jp",
                    type: "jp"
                },
                {
                    name: "google.co.in",
                    type: "in"
                },
                {
                    name: "google.hu",
                    type: "hu"
                },
                {
                    name: "google.af",
                    type: "af"
                },
                {
                    name: "google.al",
                    type: "al"
                }, {
                    name: "google.dz",
                    type: "dz"
                },
                {
                    name: "google.ao",
                    type: "ao"
                }, {
                    name: "google.am",
                    type: "am"
                },
                {
                    name: "google.at",
                    type: "at"
                },
                {
                    name: "google.az",
                    type: "az"
                },
                {
                    name: "google.bh",
                    type: "bh"
                },
                {
                    name: "google.bd",
                    type: "bd"
                },
                {
                    name: "google.by",
                    type: "by"
                },
                {
                    name: "google.bz",
                    type: "bz"
                }, {
                    name: "google.bo",
                    type: "bo"
                },
                {
                    name: "google.ba",
                    type: "ba"
                }, {
                    name: "google.bw",
                    type: "bw"
                },
                {
                    name: "google.bn",
                    type: "bn"
                },
                {
                    name: "google.bg",
                    type: "bg"
                },
                {
                    name: "google.cv",
                    type: "cv"
                },
                {
                    name: "google.kh",
                    type: "kh"
                },
                {
                    name: "google.kh",
                    type: "kh"
                },
                {
                    name: "google.cm",
                    type: "cm"
                }, {
                    name: "google.cl",
                    type: "cl"
                },
                {
                    name: "google.co",
                    type: "co"
                }, {
                    name: "google.cr",
                    type: "cr"
                },
                {
                    name: "google.hr",
                    type: "hr"
                },
                {
                    name: "google.cy",
                    type: "cy"
                },
                {
                    name: "google.cz",
                    type: "cz"
                },
                {
                    name: "google.cd",
                    type: "cd"
                },
                {
                    name: "google.do",
                    type: "do"
                },
                {
                    name: "google.ec",
                    type: "ec"
                }, {
                    name: "google.eg",
                    type: "eg"
                }, {
                    name: "google.sv",
                    type: "sv"
                }, {
                    name: "google.bw",
                    type: "bw"
                },
                {
                    name: "google.ee",
                    type: "ee"
                },
                {
                    name: "google.et",
                    type: "et"
                },
                {
                    name: "google.ge",
                    type: "ge"
                },
                {
                    name: "google.gh",
                    type: "gh"
                },
                {
                    name: "google.gr",
                    type: "gr"
                },
                {
                    name: "google.gt",
                    type: "gt"
                }, {
                    name: "google.gt",
                    type: "gt"
                }, {
                    name: "google.gy",
                    type: "gy"
                }, {
                    name: "google.ht",
                    type: "ht"
                },
                {
                    name: "google.hn",
                    type: "hn"
                },
                {
                    name: "google.is",
                    type: "is"
                },
                {
                    name: "google.id",
                    type: "id"
                },
                {
                    name: "google.jm",
                    type: "jm"
                },
                {
                    name: "google.jo",
                    type: "jo"
                },
                {
                    name: "google.kz",
                    type: "kz"
                }, {
                    name: "google.lv",
                    type: "lv"
                }, {
                    name: "google.lb",
                    type: "lb"
                }, {
                    name: "google.lt",
                    type: "lt"
                },
                {
                    name: "google.lu",
                    type: "lu"
                },
                {
                    name: "google.mg",
                    type: "mg"
                },
                {
                    name: "google.my",
                    type: "my"
                },
                {
                    name: "google.mt",
                    type: "mt"
                },
                {
                    name: "google.mu",
                    type: "mu"
                },
                {
                    name: "google.md",
                    type: "md"
                }, {
                    name: "google.mn",
                    type: "mn"
                }, {
                    name: "google.me",
                    type: "me"
                }, {
                    name: "google.ma",
                    type: "ma"
                },
                {
                    name: "google.mz",
                    type: "na"
                },
                {
                    name: "google.np",
                    type: "np"
                },
                {
                    name: "google.nz",
                    type: "nz"
                },
                {
                    name: "google.ni",
                    type: "ni"
                },
                {
                    name: "google.ng",
                    type: "ng"
                },
                {
                    name: "google.om",
                    type: "om"
                }, {
                    name: "google.py",
                    type: "py"
                }, {
                    name: "google.pe",
                    type: "pe"
                }, {
                    name: "google.ph",
                    type: "ph"
                },
                {
                    name: "google.pt",
                    type: "pt"
                },
                {
                    name: "google.ro",
                    type: "ro"
                },
                {
                    name: "google.sa",
                    type: "sa"
                },
                {
                    name: "google.sn",
                    type: "sn"
                },
                {
                    name: "google.rs",
                    type: "rs"
                },
                {
                    name: "google.sk",
                    type: "sk"
                }, {
                    name: "google.si",
                    type: "si"
                },
                {
                    name: "google.za",
                    type: "za"
                },
                {
                    name: "google.kr",
                    type: "kr"
                },
                {
                    name: "google.lk",
                    type: "lk"
                },
                {
                    name: "google.th",
                    type: "th"
                },
                {
                    name: "google.bs",
                    type: "bs"
                }, {
                    name: "google.tt",
                    type: "tt"
                }, {
                    name: "google.tn",
                    type: "tn"
                }, {
                    name: "google.ua",
                    type: "ua"
                },
                {
                    name: "google.ae",
                    type: "ae"
                },
                {
                    name: "google.uy",
                    type: "uy"
                },
                {
                    name: "google.ve",
                    type: "ve"
                },
                {
                    name: "google.vn",
                    type: "vn"
                },
                {
                    name: "google.zm",
                    type: "zm"
                },
                {
                    name: "google.zw",
                    type: "zw"
                }, {
                    name: "google.ly",
                    type: "ly"
                }, {
                    name: "google.com.uk-mobile",
                    type: "mobile-uk"
                }, {
                    name: "google.ca-mobile",
                    type: "mobile-ca"
                },
                {
                    name: "google.de-mobile",
                    type: "mobile-uk"
                },
                {
                    name: "google.fr-mobile",
                    type: "mobile-fr"
                },
                {
                    name: "google.es-mobile",
                    type: "mobile-es"
                },
                {
                    name: "google.it-mobile",
                    type: "mobile-it"
                },
                {
                    name: "google.com.br-mobile",
                    type: "mobile-br"
                },
                {
                    name: "google.com.au-mobile",
                    type: "mobile-au"
                }, {
                    name: "google.dk-mobile",
                    type: "mobile-dk"
                },
                {
                    name: "google.com.mx-mobile",
                    type: "mobile-mx"
                },
                {
                    name: "google.nl",
                    type: "mobile-nl"
                }, {
                    name: "google.se",
                    type: "mobile-se"
                }, {
                    name: "google.com.tr-mobile",
                    type: "mobile-tr"
                },

                {
                    name: "google.co.in",
                    type: "mobile-in"
                }, {
                    name: "google.co-id",
                    type: "mobile-id"
                }, {
                    name: "google.co.il",
                    type: "mobile-il"
                }
            ]
        }, {
            type: 'backlinks',
            name: 'Backlinks Report',
            timeSegments: [
                {
                    name: " Backlinks Overview",
                    type: "backlinks_overview"
                }, {
                    name: "Backlinks",
                    type: "backlinks"
                }, {
                    name: "Referring Domains",
                    type: "backlinks_refdomains"
                }, {
                    name: "Referring IPs",
                    type: "backlinks_refips"
                }, {
                    name: "TLD Distribution",
                    type: "backlinks_tld"
                }, {
                    name: "Referring Domains by Country",
                    type: "backlinks_geo"
                }, {
                    name: "Anchors",
                    type: "backlinks_anchors"
                }, {
                    name: "Indexed pages",
                    type: "backlinks_pages"
                }, {
                    name: "Comparison by referring domains",
                    type: "backlinks_matrix"
                }
            ],
            productSegments: [
                {
                    name: "Domain",
                    type: "domain"
                }, {
                    name: "Root Domain",
                    type: "root_domain"
                }, {
                    name: "URL",
                    type: "url"
                },
            ]
        }
    ];


    $scope.googlePlusPerformance = [
        {
            type: 'activityPerformance',
            name: 'User Activity Performance',
            timeSegments: [
                {
                    name: "None",
                    type: "none"
                }
            ],
            productSegments: [
                {
                    name: "None",
                    type: "none"
                }
            ]
        },
    ];

    $scope.getTimeSegements = function (dataSet) {
        $scope.reportSelected = false;
        $scope.enableMe = true;
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

        if ($scope.dataSet.dataSourceId.dataSourceType === "twitter") {
            var index = getIndex($scope.dataSet.reportName, $scope.twitterPerformance);
            $scope.timeSegment = $scope.twitterPerformance[index].timeSegments;
            $scope.productSegment = $scope.twitterPerformance[index].productSegments;
            $scope.timeSegFlag = true;
            $scope.productSegFlag = true;
            $scope.nwStatusFlag = false;
            $scope.dataSet.timeSegment = {name: 'None', type: 'none'};
            $scope.dataSet.productSegment = {name: 'None', type: 'none'};
        }

        if ($scope.dataSet.dataSourceId.dataSourceType === "linkedin") {
            var index = getIndex($scope.dataSet.reportName, $scope.linkedinPerformance);
            $scope.timeSegment = $scope.linkedinPerformance[index].timeSegments;
            $scope.productSegment = $scope.linkedinPerformance[index].productSegments;

            $scope.timeSegFlag = true;
            $scope.productSegFlag = true;
            $scope.nwStatusFlag = false;
            $scope.semRushFlag = false;

            if ($scope.dataSet.reportName === 'companyProfile') {
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

            if ($scope.dataSet.reportName === 'pagePerformance') {
                if (!dataSet.timeSegment) {
                    $scope.dataSet.timeSegment = {name: 'Month', type: 'month'};
                } else {
                    getTimeSegment(timeSegmentList, timeSegmentName);
                }
                if (!dataSet.productSegment) {
                    $scope.dataSet.productSegment = {name: 'None', type: 'none'};
                } else {
                    getProductSegment(productList, productSegmentName);
                }
            }

            if ($scope.dataSet.reportName === 'postPerformance') {
                if (!dataSet.timeSegment) {
                    $scope.dataSet.timeSegment = {name: 'None', type: 'none'};
                } else {
                    getTimeSegment(timeSegmentList, timeSegmentName);
                }
                if (!dataSet.productSegment) {
                    $scope.dataSet.productSegment = {name: 'Over All', type: 'overall'};
                } else {
                    getProductSegment(productList, productSegmentName);
                }
            }


            if ($scope.dataSet.reportName === 'pageFollowersPerformance') {
                if (!dataSet.timeSegment) {
                    $scope.dataSet.timeSegment = {name: 'Day', type: 'day'};
                } else {
                    getTimeSegment(timeSegmentList, timeSegmentName);
                }
                if (!dataSet.productSegment) {
                    $scope.dataSet.productSegment = {name: 'Historical Followers', type: 'historicalPageFollowers'};
                } else {
                    getProductSegment(productList, productSegmentName);
                }
            }




        }

        if ($scope.dataSet.dataSourceId.dataSourceType === "facebook") {
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
            if (!dataSet.timeSegment) {
                if ($scope.dataSet.reportName == 'geoPerformance') {
                    $scope.dataSet.timeSegment = {name: 'None', type: 'none'};
                } else {
                    getTimeSegment(timeSegmentList, timeSegmentName);
                }
                if (!dataSet.productSegment) {
                    $scope.dataSet.productSegment = {name: 'City', type: 'city'};
                } else {
                    getProductSegment(productList, productSegmentName);
                }
            } else {
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

            if ($scope.dataSet.reportName == 'placementReport') {
                $scope.dataSet.timeSegment = {name: 'None', type: 'none'};
                $scope.dataSet.productSegment = {name: 'None', type: 'none'};
                $scope.dataSet.networkType = {name: 'None', type: 'none'};
            }

            if ($scope.dataSet.reportName == 'videoPerformance') {
                $scope.dataSet.timeSegment = {name: 'None', type: 'none'};
                $scope.dataSet.productSegment = {name: 'None', type: 'none'};
                $scope.dataSet.networkType = {name: 'None', type: 'none'};
            }

            if ($scope.dataSet.reportName == 'geoPerformance') {
                $scope.dataSet.timeSegment = {name: 'None', type: 'none'};
                $scope.dataSet.productSegment = {name: 'None', type: 'none'};
                $scope.dataSet.networkType = {name: 'None', type: 'none'};
            } else {
                $scope.dataSet.timeSegment = {name: 'None', type: 'none'};
                $scope.dataSet.productSegment = {name: 'None', type: 'none'};
                $scope.dataSet.networkType = {name: 'None', type: 'none'};

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
//        if ($scope.dataSet.dataSourceId.dataSourceType == "linkedin")
//        {
//            var index = getIndex($scope.dataSet.reportName, $scope.linkedinPerformance);
//            $scope.timeSegment = $scope.linkedinPerformance[index].timeSegments;
//            $scope.productSegment = $scope.linkedinPerformance[index].productSegments;
//            $scope.nwStatusFlag = false;
//            $scope.timeSegFlag = false;
//            $scope.productSegFlag = false;
//        }
        if ($scope.dataSet.dataSourceId.dataSourceType == "pinterest")
        {
            var index = getIndex($scope.dataSet.reportName, $scope.pinterestPerformance);
            $scope.timeSegment = $scope.pinterestPerformance[index].timeSegments;
            $scope.productSegment = $scope.pinterestPerformance[index].productSegments;
            $scope.nwStatusFlag = false;
            $scope.timeSegFlag = false;
            $scope.productSegFlag = false;
        }
        if ($scope.dataSet.dataSourceId.dataSourceType == "semRush")
        {
            var index = getIndex($scope.dataSet.reportName, $scope.semRush);
            $scope.timeSegment = $scope.semRush[index].timeSegments;
            $scope.productSegment = $scope.semRush[index].productSegments;

            var productList = $scope.productSegment;
            var productSegmentName = dataSet.productSegment;

            var timeSegmentList = $scope.timeSegment;
            var timeSegmentName = dataSet.timeSegment;

            if ($scope.dataSet.reportName !== "") {
                $scope.nwStatusFlag = false;
                $scope.timeSegFlag = false;
                $scope.productSegFlag = false;
            }

            if ($scope.dataSet.reportName == 'overview') {
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
            if ($scope.dataSet.reportName == 'domain') {
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
            if ($scope.dataSet.reportName == 'keyword') {
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
            if ($scope.dataSet.reportName == 'url') {
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
            if ($scope.dataSet.reportName == 'backlinks') {
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
//        $scope.searchDataSourceItems.unshift({dataSourceId:{name: ''}});
        angular.forEach(response, function (value, key) {
            $scope.searchDataSourceItems.push({dataSourceId: {name: value.name}});
        });
    });
//    $scope.selectedItems = {dataSourceId: {name: ''}};

    $scope.selectXlsSheet = function (dataSource) {
        if (dataSource.dataSourceType == 'xls') {
            var url = "admin/proxy/getSheets?";
            var dataSourceId = dataSource.id;
            $http.get(url + "dataSourceId=" + dataSourceId).success(function (response) {
                $scope.xlsSheetNames = response;
            });
        }
    };

    $scope.columnsHeaderDefs = [];
    $scope.getDataSetColDefs = function (getDataSetColDefs) {
        $scope.columnsHeaderDefs = "";
        $scope.columnsHeaderDefs = getDataSetColDefs;
    };

    $scope.saveDataSet = function () {
        var dataSetList = $scope.dataSet;
        if (dataSetList.timeSegment != null) {
            dataSetList.timeSegment = dataSetList.timeSegment.type;
        } else {
            dataSetList.timeSegment = null;
        }
        if (dataSetList.productSegment != null) {
            dataSetList.productSegment = dataSetList.productSegment.type;
        } else {
            dataSetList.productSegment = null;
        }
        if (dataSetList.networkType != null) {
            dataSetList.networkType = dataSetList.networkType.type;
        } else {
            dataSetList.networkType = null;
        }
        var dataSet = dataSetList;
        if (dataSet.dataSourceId != null) {
            dataSet.dataSourceId = dataSet.dataSourceId.id;
        } else {
            dataSet.dataSourceId = null;
        }
        $scope.nwStatusFlag = true;
        $http({method: dataSet.id ? 'PUT' : 'POST', url: 'admin/ui/dataSet', data: dataSet}).success(function (response) {
//            $scope.columnHeaderByDataSetId = response;
            var getDataSetId = response.id;
            var data = $scope.columnsHeaderDefs;
            console.log(data);
            var gatDataSourceType = dataSet.dataSourceId ? dataSet.dataSourceId.dataSourceType : null;
            if (gatDataSourceType != "sql" && data != null) {
                $http({method: 'POST', url: 'admin/ui/saveDataSetColumnsForDataSet/' + getDataSetId, data: data}).success(function (response) {
                    getItems();
                });
            }
        });
        $scope.dataSet = "";
        $scope.showPreviewChart = false;
        $scope.previewData = null;
        $scope.dataSetFlag = false;
        $scope.columnHeaderByDataSetId = "";
    };

//    $scope.savePublishDataSet = function(dataSet){
//        console.log(dataSet);
//        var data = {
//            id : dataSet.id,
//            publish : dataSet.publish
//        };
//        console.log(data);
//        $http({method: data.id ? 'PUT' : 'POST', url: 'admin/ui/dataSet/enableOrDisable', data: data}).success(function (response) {
//            getItems();
//        });
//    };

    $scope.clearTable = function () {
        $scope.dataSet = "";
        $scope.columnHeaderByDataSetId = "";
    };
    $scope.editDataSet = function (dataSet) {
        $scope.columnHeaderByDataSetId = dataSet;
        $scope.nwStatusFlag = true;
        $scope.childTab = 3;
        var joinDataSetId = null;
        var publish = false;
        if (dataSet.joinDataSetId != null) {
            joinDataSetId = dataSet.joinDataSetId.id;
        }
        if (dataSet.publish != null) {
            if (dataSet.publish == "Active") {
                publish = true;
            }
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
            joinDataSetId: joinDataSetId,
            publish: publish
        };
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
                $scope.semRushFlag = false;
            } else if (dataSet.dataSourceId.dataSourceType === "facebook")
            {
                $scope.report = $scope.facebookPerformance;
                $scope.getTimeSegements(dataSet);
                $scope.dataSetFlag = true;
                $scope.nwStatusFlag = false;
                $scope.semRushFlag = false;
            } else if (dataSet.dataSourceId.dataSourceType === "bing")
            {
                $scope.report = $scope.bingPerformance;
                $scope.getTimeSegements(dataSet);
                $scope.dataSetFlag = true;
                $scope.nwStatusFlag = false;
                $scope.semRushFlag = false;
            } else if (dataSet.dataSourceId.dataSourceType === "pinterest")
            {
                $scope.report = $scope.pinterestPerformance;
                $scope.getTimeSegements(dataSet);
                $scope.dataSetFlag = true;
                $scope.nwStatusFlag = false;
                $scope.semRushFlag = false;
            } else if (dataSet.dataSourceId.dataSourceType === "adwords")
            {
                $scope.report = $scope.adwordsPerformance;
                $scope.getTimeSegements();
                $scope.dataSetFlag = true;
                $scope.nwStatusFlag = true;
                $scope.semRushFlag = false;
            } else if (dataSet.dataSourceId.dataSourceType === "analytics")
            {
                $scope.report = $scope.analyticsPerformance;
                $scope.getTimeSegements(dataSet);
                $scope.dataSetFlag = true;
                $scope.nwStatusFlag = false;
                $scope.semRushFlag = false;
            } else if (dataSet.dataSourceId.dataSourceType === "linkedin")
            {
                $scope.report = $scope.linkedinPerformance;
                $scope.getTimeSegements(dataSet);
                $scope.dataSetFlag = true;
                $scope.nwStatusFlag = false;
                $scope.timeSegFlag = true;
                $scope.productSegFlag = true;
                $scope.semRushFlag = false;
            } else if (dataSet.dataSourceId.dataSourceType === "semRush")
            {
                $scope.report = $scope.semRush;
                $scope.getTimeSegements(dataSet);
                $scope.dataSetFlag = true;
                $scope.nwStatusFlag = false;
                $scope.timeSegFlag = false;
                $scope.semRushFlag = true;
                $scope.productSegFlag = false;
            } else {
                $scope.dataSetFlag = false;
                $scope.nwStatusFlag = false;
                $scope.semRushFlag = false;
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
        $scope.selected = true;
        $scope.reportSelected = true;
        $scope.enableMe = true;
        $scope.dataSet = "";
        $scope.showPreviewChart = false;
        $scope.previewData = null;
        $scope.selectedRow = null;
        $scope.dataSetFlag = false;
        $scope.columnHeaderByDataSetId = "";
        $scope.childTab = 3;
    };
    $scope.deleteDataSet = function (dataSet, index) {
        $http({method: 'DELETE', url: 'admin/ui/dataSet/' + dataSet.id}).success(function (response) {
            $scope.dataSets.splice(index, 1);
            getItems();
        });
        $scope.clearDataSet(dataSet);
        $scope.selectedRow = null;
        $scope.columnHeaderByDataSetId = "";
    };
    $scope.selectedRow = null;
    $scope.setClickedRow = function (index) {
        $scope.selectedRow = index;
        $scope.showPreviewChart = false;
        $scope.previewData = null;
    };

    $scope.saveDataSetFieldSettings = function (columnHeader) {
        var dataSetId = $scope.columnHeaderByDataSetId;
        var data = {
            id: columnHeader.id,
            fieldName: columnHeader.fieldName,
            displayName: columnHeader.displayName,
            dataSetId: dataSetId ? dataSetId.id : '',
            category: columnHeader.category,
            dataFormat: columnHeader.dataFormat,
            displayFormat: columnHeader.displayFormat,
            expression: columnHeader.expression,
            fieldType: columnHeader.fieldType,
            functionName: columnHeader.functionName,
            groupPriority: columnHeader.groupPriority,
            sortOrder: columnHeader.sortOrder,
            sortPriority: columnHeader.sortPriority,
            status: columnHeader.status,
            userId: columnHeader.userId,
            widgetId: columnHeader.widgetId
        };

        $http({method: 'POST', url: "admin/ui/createDataSetColumnByDataSet", data: data}).success(function (response) {
            console.log(response);
        });
    };

//    $scope.getDataSetColDefs = function(dataSetColumn){alert(1)
//        console.log(dataSetColumn);
//    }
//    
});
