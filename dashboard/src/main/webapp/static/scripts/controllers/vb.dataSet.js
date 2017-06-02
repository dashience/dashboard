app.controller('DataSetController', function ($scope, $http, $stateParams, $filter, $timeout) {
    $scope.dataSetFlag = false;
    $scope.nwStatusFlag = false;
    $scope.timeSegFlag = false;
    $scope.productSegFlag = false;
//    $scope.dataSet.timeSegment  = 'None'//{name: 'None', type: 'none'}
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
        console.log(dataSource);
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
        } else if (dataSource === "bing")
        {
            $scope.report = $scope.bingPerformance;
            console.log($scope.report);
            $scope.dataSetFlag = true;
            $scope.nwStatusFlag = false;
        } else if (dataSource === "pinterest")
        {
            $scope.report = $scope.pinterestPerformance;
            console.log($scope.report);
            $scope.dataSetFlag = true;
            $scope.nwStatusFlag = false;
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
            name: 'getTopBoards',
        }, {
            type: 'getTopPins',
            name: 'getTopPins',
        }, {
            type: 'getOrganicData',
            name: 'getOrganicData',
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
                    name: 'device'
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
                    name: 'city'
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


//    $scope.dataSet={timeSegment:'none'};


    $scope.getTimeSegements = function () {

        console.log("Data source type-->" + $scope.dataSet.dataSourceId.dataSourceType);
        if ($scope.dataSet.dataSourceId.dataSourceType === "bing")
        {
            var index = getIndex($scope.dataSet.reportName, $scope.bingPerformance);
            $scope.timeSegment = $scope.bingPerformance[index].timeSegments;
            $scope.productSegment = $scope.bingPerformance[index].productSegments;
            $scope.timeSegFlag = true;
            $scope.productSegFlag = true;
            $scope.nwStatusFlag = false;
            if ($scope.dataSet.reportName == 'geoPerformance') {
                $scope.dataSet.timeSegment = {name: 'None', type: 'none'};
                $scope.dataSet.productSegment = {name: 'City', type: 'city'};
            } else {
                $scope.dataSet.timeSegment = {name: 'None', type: 'none'}
                $scope.dataSet.productSegment = {name: 'None', type: 'none'}
            }
        }


        if ($scope.dataSet.dataSourceId.dataSourceType == "instagram")
        {
            var index = getIndex($scope.dataSet.reportName, $scope.instagramPerformance);
            $scope.timeSegment = $scope.instagramPerformance[index].timeSegments;
            $scope.productSegment = $scope.instagramPerformance[index].productSegments;

            $scope.nwStatusFlag = true;
        }
        if ($scope.dataSet.dataSourceId.dataSourceType == "facebook")
        {
            console.log("hello ***");
            console.log("************************************************");
            console.log($scope.dataSet.reportName);
            var index = getIndex($scope.dataSet.reportName, $scope.facebookPerformance);
            $scope.timeSegment = $scope.facebookPerformance[index].timeSegments;
            $scope.productSegment = $scope.facebookPerformance[index].productSegments;
            console.log("************************************************");
            console.log($scope.productSegment);
            if ($scope.dataSet.reportName !== "") {
                $scope.nwStatusFlag = false;
                $scope.timeSegFlag = true;
                $scope.productSegFlag = true;
            }

            if ($scope.dataSet.reportName == 'agePerformance') {
                $scope.dataSet.timeSegment = {name: 'None', type: 'none'}
                $scope.dataSet.productSegment = $scope.productSegment[0]
            }
            if ($scope.dataSet.reportName == 'genderPerformance') {
                $scope.dataSet.timeSegment = {name: 'None', type: 'none'}
                $scope.dataSet.productSegment = $scope.productSegment[0]
            }
            if ($scope.dataSet.reportName == 'pageReactions') {
                $scope.dataSet.timeSegment = $scope.timeSegment[0];
                $scope.dataSet.productSegment = {name: 'None', type: 'none'}
            }
            if ($scope.dataSet.reportName == 'accountPerformance') {
                $scope.dataSet.timeSegment = $scope.timeSegment[3];
                $scope.dataSet.productSegment = $scope.productSegment[1];
            }
            if ($scope.dataSet.reportName == 'campaignPerformance') {
                $scope.dataSet.timeSegment = {name: 'None', type: 'none'};
                $scope.dataSet.productSegment = {name: 'None', type: 'none'};
            }

            if ($scope.dataSet.reportName == 'adPerformance') {
                $scope.dataSet.timeSegment = {name: 'None', type: 'none'};
                $scope.dataSet.productSegment = {name: 'None', type: 'none'};
            }

            if ($scope.dataSet.reportName == 'adSetPerformance') {
                $scope.dataSet.timeSegment = {name: 'None', type: 'none'};
                $scope.dataSet.productSegment = {name: 'None', type: 'none'};
            }

            if ($scope.dataSet.reportName == 'postPerformance') {
                $scope.dataSet.timeSegment = {name: 'None', type: 'none'};
                $scope.dataSet.productSegment = {name: 'None', type: 'none'};
            }

            if ($scope.dataSet.reportName == 'engagements') {
                $scope.dataSet.timeSegment = {name: 'None', type: 'none'};
                $scope.dataSet.productSegment = {name: 'None', type: 'none'};
            }

            if ($scope.dataSet.reportName == 'reach') {
                $scope.dataSet.timeSegment = {name: 'None', type: 'none'};
                $scope.dataSet.productSegment = {name: 'None', type: 'none'};
            }

            if ($scope.dataSet.reportName == 'pageLikes') {
                $scope.dataSet.timeSegment = {name: 'None', type: 'none'};
                $scope.dataSet.productSegment = {name: 'None', type: 'none'};
            }

            if ($scope.dataSet.reportName == 'pageViews') {
                $scope.dataSet.timeSegment = {name: 'None', type: 'none'};
                $scope.dataSet.productSegment = {name: 'None', type: 'none'};
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
        }

        if ($scope.dataSet.dataSourceId.dataSourceType == "analytics")
        {
            var index = getIndex($scope.dataSet.reportName, $scope.analyticsPerformance);
            $scope.timeSegment = $scope.analyticsPerformance[index].timeSegments;
            $scope.productSegment = $scope.analyticsPerformance[index].productSegments;
            $scope.nwStatusFlag = false;
        }
        if ($scope.dataSet.dataSourceId.dataSourceType == "linkedin")
        {
            var index = getIndex($scope.dataSet.reportName, $scope.linkedinPerformance);
            $scope.timeSegment = $scope.linkedinPerformance[index].timeSegments;
            $scope.productSegment = $scope.linkedinPerformance[index].productSegments;
            $scope.timeSegFlag = false;
            $scope.productSegFlag = false;
            $scope.nwStatusFlag = false;
        }
        if ($scope.dataSet.dataSourceId.dataSourceType == "pinterest")
        {
            var index = getIndex($scope.dataSet.reportName, $scope.pinterestPerformance);
//            $scope.timeSegment = $scope.pinterestPerformance[index].timeSegments;
            $scope.productSegment = $scope.pinterestPerformance[index].productSegments;
            $scope.nwStatusFlag = false;
            $scope.timeSegFlag = false;
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

//    $scope.dataSet.timeSegment = 'none'
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
    $scope.selectedItems = {name: "All Data Source", value: '', id: 0}

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
        dataSetList.timeSegment === "" ? 'none' : dataSetList.timeSegment;

        var dataSet = dataSetList;//$scope.dataSet;
        dataSet.dataSourceId = dataSet.dataSourceId.id;
        console.log(dataSet);
//        if (dataSet.networkType !== null && typeof (dataSet.networkType) !== "undefined")
//        {
//            var networkType = dataSet.networkType.map(function (value, key) {
//                if (value) {
//                    return value.type;
//                }
//            }).join(',');
//            dataSet.networkType = networkType;
        $scope.nwStatusFlag = true;
//        } else {
//            $scope.nwStatusFlag = false;
//        }
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
//        if (dataSet.networkType !== null)
//        {
//            dataSet.networkType = dataSet.networkType.split(',').map(function (value, key) {
//                return {
//                    'name': value ? value : ''
//                }
//            });
        $scope.nwStatusFlag = true;
//        } else {
//
//            $scope.nwStatusFlag = false;
//        }
        console.log(dataSet.networkType);
        console.log(dataSet.timeSegment);
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
            userId: dataSet.userId.id

        };
        console.log(data);
        $scope.dataSet = data;
        var dataSource = dataSet.dataSourceId
        var dataSourceType = dataSet.dataSourceId.dataSourceType;
        if (dataSourceType == 'xls') {
            $scope.selectXlsSheet(dataSource);
        }
        if (dataSet.dataSourceId.dataSourceType === "instagram")
        {
            $scope.report = $scope.instagramPerformance;
            $scope.getTimeSegemens();
            $scope.dataSetFlag = true;
            $scope.nwStatusFlag = false;
        } else if (dataSet.dataSourceId.dataSourceType === "facebook")
        {
            $scope.report = $scope.facebookPerformance;
            $scope.getTimeSegements();
            $scope.dataSetFlag = true;
            $scope.nwStatusFlag = false;
        } else if (dataSet.dataSourceId.dataSourceType === "pinterest")
        {
            $scope.report = $scope.pinterestPerformance;
            $scope.getTimeSegements();
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
            $scope.getTimeSegements();
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
            $scope.dataSets.splice(index, 1)
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
        template: '<div ng-show="showErrorMsg"><h5><center>{{errorMsg}}</center></h5></div>' +
                '<div ng-hide="showErrorMsg">' +
                '<div ng-show="loadingTable" class="text-center" style="color: #228995;"><img src="static/img/logos/loader.gif"></div>' +
                '<table ng-if="ajaxLoadingCompleted" class="table table-responsive table-bordered table-l2t">' +
//                '<div class="pull-right">' +
//                '<button class="btn btn-success btn-xs" data-toggle="modal" data-target="#myModal"><i class="fa fa-plus"></i></button>' +
//                '<div id="myModal" class="modal fade" role="dialog">' +
//                '<div class="modal-dialog">' +
//                '<div class="modal-content">' +
//                '<div class="modal-header">' +
//                '<button type="button" class="close" data-dismiss="modal" ng-click="dataSetFieldsClose()">&times;</button>' +
//                '<h4 class="modal-title">Derived Column</h4>' +
//                '</div>' +
//                '<div class="modal-body">' +
//                '<form name="dataSetForm" class="form-horizontal">' +
//                '<div class="form-group">' +
//                '<label class="col-md-3">Field Name</label>' +
//                '<div class="col-md-9">' +
//                '<input class="form-control" ng-model="datasetColumn.fieldName" ng-change="checkFieldName(datasetColumn.fieldName)" type="text">' +
//                '<span ng-show="dataSetError" style="color:red">Field Name Already Exists</span>' +
//                '</div>' +
//                '</div>' +
//                '<div class="form-group">' +
//                '<label class="col-md-3">Field Type</label>' +
//                '<div class="col-md-3">' +
//                '<select class="form-control" ng-model="datasetColumn.fieldType">' +
//                '<option ng-repeat="fieldType in fieldTypes" value="{{fieldType.value}}">' +
//                '{{fieldType.name}}' +
//                '</option>' +
//                '</select>' +
//                '</div>' +
//                '<label class="col-md-2">Format</label>' +
//                '<div class="col-md-4">' +
//                '<select class="form-control" ng-model="datasetColumn.format">' +
//                '<option  ng-repeat="formatType in formats" value="{{formatType.value}}">' +
//                '{{formatType.name}}' +
//                '</option>' +
//                '</select>' +
//                '</div>' +
//                '</div>' +
//                '<div class="form-group">' +
//                '<label class="col-md-3">Expression</label>' +
//                '<div class="col-md-9">' +
//                '<textarea name="expression" class=form-control" ng-model="datasetColumn.expression" ng-disabled="datasetColumn.function?true:false" rows="3" style="width:350px;resize:none"></textarea>' +
//                '<i class="btn btn-md fa fa-minus-circle" ng-click="clearExpression(datasetColumn)"></i>' +
//                '</div>' +
//                '</div>' +
//                '<div class="form-group">' +
//                '<label class="col-md-3">Function</label>' +
//                '<div class="col-md-3">' +
//                '<select  name="functionName" class="form-control" ng-model="datasetColumn.function" ng-disabled="datasetColumn.expression?true:false">' +
//                '<option>' +
//                'YOY' +
//                '</option>' +
//                '<option>' +
//                'MOM' +
//                '</option>' +
//                '</select>' +
//                '</div>' +
//                '<label class="col-md-2">Column</label>' +
//                '<div class="col-md-3">' +
//                '<select class="form-control" ng-disabled="datasetColumn.expression?true:false" ng-model="datasetColumn.columnName">' +
//                '<option ng-repeat="dataSetColumn in expressionLessColumn" value={{dataSetColumn.fieldName}}>' +
//                '{{dataSetColumn.fieldName}}' +
//                '</option>' +
//                '</select>' +
//                '</div>' +
//                '<div class="col-md-1">' +
//                '<i class="btn btn-md fa fa-minus-circle" ng-click="clearFunction(datasetColumn)"></i>' +
//                '</div>' +
//                '</div>' +
//                '</form>' +
//                '</div>' +
//                '<div class="modal-footer">' +
//                '<button type="button" class="btn btn-success"  ng-click="saveDatasetColumn(datasetColumn)" data-dismiss="modal">Save</button>' +
//                '<button type="button" class="btn btn-default"  ng-click="dataSetFieldsClose()" data-dismiss="modal">Close</button>' +
//                '</div>' +
//                '</div>' +
//                '</div>' +
//                '</div>' +
//                '</div>' +               
                '<thead><tr>' +
                '<th class="text-capitalize table-bg" ng-repeat="col in tableColumns">' +
                '{{col.displayName}}' +
                '</th>' +
                '</tr></thead>' +
                '<tbody ng-repeat="tableRow in tableRows">' +
                '<tr class="text-capitalize">' +
                '<td ng-repeat="col in tableColumns">' +
                '<div>{{format(col, tableRow[col.fieldName])}}</div>' +
                '</td>' +
                '</tbody>' +
                '</table>' +
                '</div>',
        link: function (scope, element, attr) {
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
                {name: "Time", value: 'H:M:S'},
                {name: "Star Rating", value: 'starRating'}
            ];
            scope.loadingTable = true;
            var dataSourcePath = JSON.parse(scope.path)
            console.log(dataSourcePath);
//            console.log(dataSourcePath.dataSourceId.userName);
//            console.log(dataSourcePath.dataSourceId.connectionString);
//            console.log(dataSourcePath.dataSourceId.sqlDriver);
//            console.log(dataSourcePath.dataSourceId.password);
            var url = "admin/proxy/getData?";
            if (dataSourcePath.dataSourceId.dataSourceType == "sql") {
                url = "admin/proxy/getJson?url=../dbApi/admin/dataSet/getData&";
            }

            var dataSourcePassword;
            if (dataSourcePath.dataSourceId.password) {
                dataSourcePassword = dataSourcePath.dataSourceId.password;
            } else {
                dataSourcePassword = '';
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
            var setTimeSegment, setProductSegment;

            if (dataSourcePath.timeSegment) {
                setTimeSegment = dataSourcePath.timeSegment.type;
            } else {
                setTimeSegment = 'none'
            }
            if (dataSourcePath.productSegment) {
                setProductSegment = dataSourcePath.productSegment.type;
            } else {
                setProductSegment = 'none'
            }


            $http.get(url + 'connectionUrl=' + dataSourcePath.dataSourceId.connectionString +
                    "&dataSourceId=" + dataSourcePath.dataSourceId.id +
                    "&dataSetId=" + dataSourcePath.id +
                    "&accountId=" + $stateParams.accountId +
                    "&dataSetReportName=" + dataSourcePath.reportName +
                    "&timeSegment=" + setTimeSegment +
                    "&filter=" + dataSourcePath.networkType +
                    "&productSegment=" + setProductSegment +
                    "&driver=" + dataSourcePath.dataSourceId.dataSourceType +
                    "&dataSourceType=" + dataSourcePath.dataSourceId.dataSourceType +
                    "&location=" + $stateParams.locationId +
                    "&startDate=" + $stateParams.startDate +
                    "&endDate=" + $stateParams.endDate +
                    '&username=' + dataSourcePath.dataSourceId.userName +
                    '&password=' + dataSourcePassword +
                    '&url=' + dataSourcePath.url +
                    '&port=3306&schema=deeta_dashboard&query=' + encodeURI(dataSourcePath.query)).success(function (response) {
                scope.ajaxLoadingCompleted = true;
                scope.loadingTable = false;
                console.log(response)
                if (response.data.length == 0) {
                    scope.errorMsg = "No Data Found";
                    scope.showErrorMsg = true;
                } else {
                    scope.showErrorMsg = false;
                    scope.tableColumns = response.columnDefs;
                    scope.tableRows = response.data;
                    console.log(scope.tableColumns);
                }
            });
        }
    };
});
