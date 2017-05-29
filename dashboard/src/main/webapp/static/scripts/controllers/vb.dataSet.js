app.controller('DataSetController', function ($scope, $http, $stateParams, $filter, $timeout) {
    $scope.dataSetFlag = false;
    $scope.nwStatusFlag = false;
    $scope.timeSegFlag = false;
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
            $scope.dataSetFlag = false;
            $scope.nwStatusFlag = false;
            $scope.timeSegFlag = false;
        } else {
            $scope.dataSetFlag = false;
            $scope.nwStatusFlag = false;
        }
    };
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
                    type: 'year',
                    name: 'year'
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
                    type: 'network search partner',
                    name: 'network search partner'
                },
                {
                    type: 'none',
                    name: 'none'
                }
            ]
        },
        {
            type: 'campaignPerformance',
            name: 'campaignPerformance',
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
                    name: 'none'
                }
            ]
        },
        {
            type: 'adPerformance',
            name: 'adPerformance',
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
                    name: 'none'
                }
            ]
        },
        {
            type: 'devicePerformance',
            name: 'devicePerformance',
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
                    name: 'none'
                }
            ]
        }, {
            type: 'agePerformance',
            name: 'agePerformance',
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
                    name: 'none'
                }
            ]
        }, {
            type: 'genderPerformance',
            name: 'genderPerformance',
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
                    name: 'none'
                }
            ]
        }, {
            type: 'postPerformance',
            name: 'postPerformance',
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
                    name: 'none'
                }
            ]
        }, {
            type: 'postSummary',
            name: 'postSummary',
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
                    name: 'none'
                }
            ]
        }, {
            type: 'recentPostPerformance',
            name: 'recentPostPerformance',
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
                    name: 'none'
                }
            ]
        }, {
            type: 'totalOrganicLikes',
            name: 'totalOrganicLikes',
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
                    name: 'none'
                }
            ]
        }, {
            type: 'totalPageViews',
            name: 'totalPageViews',
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
                    name: 'none'
                }
            ]
        }, {
            type: 'totalEngagements',
            name: 'totalEngagements',
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
                    name: 'none'
                }
            ]
        }, {
            type: 'totalReach',
            name: 'totalReach',
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
                    name: 'none'
                }
            ]
        }, {
            type: 'pageLikesByCity',
            name: 'pageLikesByCity',
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
                    name: 'none'
                }
            ]
        }
    ];
//    $scope.pinterestPerformance = [
//        {
//            type: 'getTopBoards',
//            name: 'getTopBoards',
////            timeSegments: [
////                {
////                    type: 'day',
////                    name: 'day'
////                },
////                {
////                    type: 'week',
////                    name: 'week'
////                },
////                {
////                    type: 'month',
////                    name: 'month'
////                },
////                {
////                    type: 'year',
////                    name: 'year'
////                }
////            ]
//        }, {
//            type: 'getTopPins',
//            name: 'getTopPins',
////            timeSegments: [
////                {
////                    type: 'day',
////                    name: 'day'
////                },
////                {
////                    type: 'week',
////                    name: 'week'
////                },
////                {
////                    type: 'month',
////                    name: 'month'
////                },
////                {
////                    type: 'year',
////                    name: 'year'
////                }
////            ]
//        }, {
//            type: 'getFollowingsCount',
//            name: 'getFollowingsCount',
////            timeSegments: [
////                {
////                    type: 'day',
////                    name: 'day'
////                },
////                {
////                    type: 'week',
////                    name: 'week'
////                },
////                {
////                    type: 'month',
////                    name: 'month'
////                },
////                {
////                    type: 'year',
////                    name: 'year'
////                }
////            ]
//        }, {
//            type: 'getPinsLikeCount',
//            name: 'getPinsLikeCount',
////            timeSegments: [
////                {
////                    type: 'day',
////                    name: 'day'
////                },
////                {
////                    type: 'week',
////                    name: 'week'
////                },
////                {
////                    type: 'month',
////                    name: 'month'
////                },
////                {
////                    type: 'year',
////                    name: 'year'
////                }
////            ]
//        }, {
//            type: 'getTotalBoards',
//            name: 'getTotalBoards',
////            timeSegments: [
////                {
////                    type: 'day',
////                    name: 'day'
////                },
////                {
////                    type: 'week',
////                    name: 'week'
////                },
////                {
////                    type: 'month',
////                    name: 'month'
////                },
////                {
////                    type: 'year',
////                    name: 'year'
////                }
////            ]
//        }, {
//            type: 'getTotalPins',
//            name: 'getTotalPins',
////            timeSegments: [
////                {
////                    type: 'day',
////                    name: 'day'
////                },
////                {
////                    type: 'week',
////                    name: 'week'
////                },
////                {
////                    type: 'month',
////                    name: 'month'
////                },
////                {
////                    type: 'year',
////                    name: 'year'
////                }
////            ]
//        }
//    ]
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
    $scope.getTimeSegemens = function () {

        if ($scope.dataSet.dataSourceId.dataSourceType == "instagram")
        {
            var index = getIndex($scope.dataSet.reportName, $scope.instagramPerformance);
            $scope.timeSegment = $scope.instagramPerformance[index].timeSegments;
            $scope.productSegment = $scope.instagramPerformance[index].productSegments;
            $scope.nwStatusFlag = true;
        }
        if ($scope.dataSet.dataSourceId.dataSourceType == "facebook")
        {
            var index = getIndex($scope.dataSet.reportName, $scope.facebookPerformance);
            $scope.timeSegment = $scope.facebookPerformance[index].timeSegments;
            $scope.productSegment = $scope.facebookPerformance[index].productSegments;
            $scope.nwStatusFlag = false;
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
        var dataSet = $scope.dataSet;
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
            $scope.getTimeSegemens();
            $scope.dataSetFlag = true;
            $scope.nwStatusFlag = false;
        } else if (dataSet.dataSourceId.dataSourceType === "pinterest")
        {
            $scope.report = $scope.pinterestPerformance;
            $scope.getTimeSegemens();
            $scope.dataSetFlag = true;
            $scope.nwStatusFlag = false;
        } else if (dataSet.dataSourceId.dataSourceType === "adwords")
        {
            $scope.report = $scope.adwordsPerformance;
            $scope.getTimeSegemens();
            $scope.dataSetFlag = true;
            $scope.nwStatusFlag = true;
        } else if (dataSet.dataSourceId.dataSourceType === "analytics")
        {
            $scope.report = $scope.analyticsPerformance;
            $scope.getTimeSegemens();
            $scope.dataSetFlag = true;
            $scope.nwStatusFlag = false;
        } else if (dataSet.dataSourceId.dataSourceType === "linkedin")
        {
            $scope.report = $scope.linkedinPerformance;
            $scope.getTimeSegemens();
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
        template: '<div ng-show="loadingTable" class="text-center" style="color: #228995;"><img src="static/img/logos/loader.gif"></div>' +
                '<div ng-if="ajaxLoadingCompleted">' +
                '<div class="pull-right">' +
                '<button ng-show="tableRows != null" class="btn btn-success btn-xs" data-toggle="modal" data-target="#dataset"><i class="fa fa-plus"></i></button>' +
                '<div id="dataset" class="modal fade" role="dialog">' +
                '<div class="modal-dialog">' +
                '<div class="modal-content">' +
                '<div class="modal-header">' +
                '<button type="button" class="close" ng-click="dataSetFieldsClose(datasetColumn)" data-dismiss="modal">&times;</button>' +
                '<h4 class="modal-title">Derived Column</h4>' +
                '</div>' +
                '<div class="modal-body">' +
                '<form name="dataSetForm" class="form-horizontal">' +
                '<div class="form-group">' +
                '<label class="col-md-3">Field Name</label>' +
                '<div class="col-md-9">' +
                '<input class="form-control" ng-model="datasetColumn.fieldName" ng-change="checkFieldName(datasetColumn.fieldName)" type="text">' +
                '<span ng-show="dataSetError" style="color:red">Field Name Already Exists</span>' +
                '</div>' +
                '</div>' +
                '<div class="form-group">' +
                '<label class="col-md-3">Field Type</label>' +
                '<div class="col-md-3">' +
                '<select class="form-control" ng-model="datasetColumn.fieldType">' +
                '<option ng-repeat="fieldType in fieldTypes" value="{{fieldType.value}}">' +
                '{{fieldType.name}}' +
                '</option>' +
                '</select>' +
                '</div>' +
                '<label class="col-md-2">Format</label>' +
                '<div class="col-md-4">' +
                '<select class="form-control" ng-model="datasetColumn.format">' +
                '<option  ng-repeat="formatType in formats" value="{{formatType.value}}">' +
                '{{formatType.name}}' +
                '</option>' +
                '</select>' +
                '</div>' +
                '</div>' +
                '<div class="form-group">' +
                '<label class="col-md-3">Expression</label>' +
                '<div class="col-md-8">' +
                '<textarea name="expression" ng-trim="false" spellcheck="false" smart-area="config" ' +
                'class="form-control code expression" ng-model="datasetColumn.expression" ng-disabled="datasetColumn.functionName?true:false" rows="5"></textarea>' +
                '</div>' +
                '<div class="col-md-1">' +
                '<i class="fa fa-minus-circle" style="cursor:pointer" ng-click="clearExpression(datasetColumn)"></i>' +
                '</div>' +
                '</div>' +
                '<div class="form-group">' +
                '<label class="col-md-3">Function</label>' +
                '<div class="col-md-3">' +
                '<select  name="functionName" class="form-control" ng-model="datasetColumn.functionName" ng-disabled="datasetColumn.expression?true:false">' +
                '<option ng-repeat="functionType in functionTypes" value={{functionType.name}}>' +
                '{{functionType.name}}' +
                '</option>' +
                '</select>' +
                '</div>' +
                '<label class="col-md-2">Column</label>' +
                '<div class="col-md-3">' +
                '<select class="form-control" ng-disabled="datasetColumn.expression?true:false" ng-model="datasetColumn.columnName">' +
                '<option ng-if="dataSetColumn.functionName == null && dataSetColumn.expression == null" ng-repeat="dataSetColumn in tableColumns" value={{dataSetColumn.fieldName}}>' +
                '{{dataSetColumn.fieldName}}' +
                '</option>' +
                '</select>' +
                '</div>' +
                '<div class="col-md-1">' +
                '<i class="fa fa-minus-circle" style="cursor:pointer" ng-click="clearFunction(datasetColumn)"></i>' +
                '</div>' +
                '</div>' +
                '</form>' +
                '</div>' +
                '<div class="modal-footer">' +
                '<button type="button" class="btn btn-success"  ng-click="saveDatasetColumn(datasetColumn)" ng-disabled="dataSetError||!((datasetColumn.expression||(datasetColumn.functionName&&datasetColumn.columnName))&&datasetColumn.fieldName&&datasetColumn.fieldType&&datasetColumn.format)||!(datasetColumn)" data-dismiss="modal">Save</button>' +
                '<button type="button" class="btn btn-default" ng-click="dataSetFieldsClose(datasetColumn)" data-dismiss="modal">Close</button>' +
                '</div>' +
                '</div>' +
                '</div>' +
                '</div>' +
                '</div>' +
                '<table class="table table-responsive table-bordered table-l2t">' +
                '<thead><tr>' +
                '<th class="text-capitalize table-bg" ng-repeat="col in tableColumns">' +
                '{{col.fieldName}}' +
                //Edit
                '<div>' +
                '<button ng-if="col.functionName != null|| col.expression != null" type="button" class="btn btn-default btn-xs" data-toggle="modal" data-target="#dataSetColumn{{col.fieldName}}" ng-click="editDataset(col)"><i class="fa fa-pencil"></i></button>' +
                '<div id="dataSetColumn{{col.fieldName}}" class="modal fade" role="dialog">' +
                '<div class="modal-dialog">' +
                '<div class="modal-content">' +
                '<div class="modal-header">' +
                '<button type="button" class="close" ng-click="dataSetFieldsClose(datasetColumn)" data-dismiss="modal">&times;</button>' +
                '<h4 class="modal-title">Derived Column</h4>' +
                '</div>' +
                '<div class="modal-body">' +
                '<form name="dataSetForm" class="form-horizontal">' +
                '<div class="form-group">' +
                '<label class="col-md-3">Field Name</label>' +
                '<div class="col-md-9">' +
                '<input class="form-control" ng-model="datasetColumn.fieldName" ng-change="checkFieldName(datasetColumn.fieldName)" type="text">' +
                '<span ng-show="dataSetError" style="color:red">Field Name Already Exists</span>' +
                '</div>' +
                '</div>' +
                '<div class="form-group">' +
                '<label class="col-md-3">Field Type</label>' +
                '<div class="col-md-3">' +
                '<select class="form-control" ng-model="datasetColumn.fieldType">' +
                '<option ng-repeat="fieldType in fieldTypes" value="{{fieldType.value}}">' +
                '{{fieldType.name}}' +
                '</option>' +
                '</select>' +
                '</div>' +
                '<label class="col-md-2">Format</label>' +
                '<div class="col-md-4">' +
                '<select class="form-control" ng-model="datasetColumn.format">' +
                '<option  ng-repeat="formatType in formats" value="{{formatType.value}}">' +
                '{{formatType.name}}' +
                '</option>' +
                '</select>' +
                '</div>' +
                '</div>' +
                '<div class="form-group">' +
                '<label class="col-md-3">Expression</label>' +
                '<div class="col-md-8">' +
                '<textarea name="expression" ng-trim="false" spellcheck="false" smart-area="config" class="form-control code expression" ng-model="datasetColumn.expression" ng-trim="false" ng-disabled="datasetColumn.functionName?true:false" rows="5"></textarea>' +
                '</div>' +
                '<div class="col-md-1">' +
                '<i class="fa fa-minus-circle" style="cursor:pointer" ng-click="clearExpression(datasetColumn)"></i>' +
                '</div>' +
                '</div>' +
                '<div class="form-group">' +
                '<label class="col-md-3">Function</label>' +
                '<div class="col-md-3">' +
                '<select  name="functionName" class="form-control" ng-model="datasetColumn.functionName" ng-disabled="datasetColumn.expression?true:false">' +
                '<option ng-repeat="functionType in functionTypes" value={{functionType.name}}>' +
                '{{functionType.name}}' +
                '</option>' +
                '</select>' +
                '</div>' +
                '<label class="col-md-2">Column</label>' +
                '<div class="col-md-3">' +
                '<select class="form-control" ng-disabled="datasetColumn.expression?true:false" ng-model="datasetColumn.columnName">' +
                '<option ng-if="dataSetColumn.functionName == null && dataSetColumn.expression == null" ng-repeat="dataSetColumn in tableColumns" value={{dataSetColumn.fieldName}}>' +
                '{{dataSetColumn.fieldName}}' +
                '</option>' +
                '</select>' +
                '</div>' +
                '<div class="col-md-1">' +
                '<i class="fa fa-minus-circle" style="cursor:pointer" ng-click="clearFunction(datasetColumn)"></i>' +
                '</div>' +
                '</div>' +
                '</form>' +
                '</div>' +
                '<div class="modal-footer">' +
                '<button type="button" class="btn btn-success"  ng-click="saveDatasetColumn(datasetColumn)" data-dismiss="modal">Save</button>' +
                '<button type="button" class="btn btn-default" ng-click="dataSetFieldsClose(datasetColumn)" data-dismiss="modal">Close</button>' +
                '</div>' +
                '</div>' +
                '</div>' +
                '</div>' +
                '</div>' +
                '<button ng-if="col.functionName != null|| col.expression != null" type="button" ng-click=deleteDataset(col) class="btn btn-default btn-xs"><i class="fa fa-trash"></i></button>' +
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
            scope.functionTypes = [
                {name: "YOY", value: 'yoy'},
                {name: "MOM", value: 'mom'},
                {name: 'WOW', value: 'wow'}
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
            scope.dataSetItems = function () {
                $http.get(url + 'connectionUrl=' + dataSourcePath.dataSourceId.connectionString +
                        "&dataSourceId=" + dataSourcePath.dataSourceId.id +
                        "&dataSetId=" + dataSourcePath.id +
                        "&accountId=" + $stateParams.accountId +
                        "&dataSetReportName=" + dataSourcePath.reportName +
                        "&timeSegment=" + dataSourcePath.timeSegment +
                        "&filter=" + dataSourcePath.networkType +
                        "&productSegment=" + dataSourcePath.productSegment +
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
                    scope.tableColumns = response.columnDefs;
                    scope.tableRows = response.data;
                    console.log(scope.tableColumns);
//                console.log(scope.tableRows)

                    scope.expressionLessColumn = [];
                    for (var j = 0; j < scope.tableColumns.length; j++) {
                        if (scope.tableColumns[j].expression === null && scope.tableColumns[j].functionName === null) {
                            scope.expressionLessColumn.push(scope.tableColumns[j]);
                        }
                    }
                    scope.columns = [];
                    console.log(scope.tableColumns.length);
                    for (var i = 0; i < scope.tableColumns.length; i++) {
                        console.log(scope.tableColumns[i]);
                        var status = null;
                        var expression = null;
                        var functionName = null;
                        if (typeof (scope.tableColumns[i].status) !== undefined) {
                            status = scope.tableColumns[i].status;
                        }
                        if (typeof (scope.tableColumns[i].expression) !== undefined) {
                            expression = scope.tableColumns[i].expression;
                        }
                        if (typeof (scope.tableColumns[i].functionName) !== undefined) {
                            functionName = scope.tableColumns[i].functionName;
                        }
                        var columnData = {
                            id: scope.tableColumns[i].id,
                            fieldName: scope.tableColumns[i].fieldName,
                            displayName: scope.tableColumns[i].displayName,
                            fieldType: scope.tableColumns[i].type,
                            displayFormat: scope.tableColumns[i].displayFormat,
                            status: status,
                            expression: expression,
                            functionName: functionName
                        }
                        scope.columns.push(columnData);
                    }
                    console.log(scope.columns);
//                    var tableColumnsData = {
//                        datasetId: dataSourcePath.id,
//                        tableColumns: scope.columns,
//                    };
//                    console.log(tableColumnsData);
//                    $http({method: 'POST', url: 'admin/ui/dataSetColumns', data: JSON.stringify(tableColumnsData)}).success(function (response) {
//                        console.log(response);
//                    });
                });
            }
            scope.dataSetItems();

            scope.checkFieldName = function (fieldName) {

                angular.forEach(scope.tableColumns, function (value, key) {

                    console.log(typeof (fieldName));
                    console.log(typeof (value.fieldName));
                    if (value.expression == null && value.functionName == null) {
                        console.log(fieldName + " " + value.fieldName);
                        if (value.fieldName === fieldName) {
                            scope.dataSetError = true;
                            angular.$apply();
                            console.log(scope.dataSetError);
                        } else {
                            scope.dataSetError = false;
                        }
                    }
                });
            };
//            scope.functiondisabled = false;
//            scope.expressiondisabled = false;
            scope.clearFunction = function (datasetColumn) {
                datasetColumn.columnName = "";
                datasetColumn.functionName = "";
//                scope.expressiondisabled = false;
//                scope.functiondisabled = false;
            }
            scope.clearExpression = function (datasetColumn) {
                datasetColumn.expression = "";
//                scope.functiondisabled = false;
//                scope.expressiondisabled = false;
            }
            scope.dataSetFieldsClose = function (datasetColumn) {
                console.log("function called close");
                datasetColumn.expression = "";
                datasetColumn.fieldName = "";
                datasetColumn.fieldType = "";
                datasetColumn.format = "";
                datasetColumn.functionName = "";
                datasetColumn.columnName = "";
                scope.dataSetError = false;
            };
            scope.saveDatasetColumn = function (datasetColumn) {
//                scope.datasetColumns=[];
//                scope.datasetColumns.push(dataSetFields);
//                console.log(scope.datasetColumns)
                console.log(datasetColumn);
                console.log(typeof (datasetColumn.functionName) + " " + typeof (datasetColumn.columnName));

                var functionName = null;
                if (typeof (datasetColumn.functionName) !== "undefined" && datasetColumn.functionName !== null && datasetColumn.functionName !== "" && datasetColumn.columnName !== null && datasetColumn.columnName !== "" && typeof (datasetColumn.columnName) !== "undefined") {
                    functionName = datasetColumn.functionName + "(" + datasetColumn.columnName + ")";
                }
                console.log(functionName);
                var data = {
                    datasetId: dataSourcePath.id,
                    id: datasetColumn.id,
                    tableColumns: scope.columns,
                    expression: datasetColumn.expression,
                    fieldName: datasetColumn.fieldName,
                    displayName: datasetColumn.fieldName,
                    fieldType: datasetColumn.fieldType,
                    displayFormat: datasetColumn.format,
                    functionName: functionName
                };
                console.log(data);
                $http({method: 'POST', url: 'admin/ui/dataSetFormulaColumns', data: JSON.stringify(data)}).success(function (response) {
                    console.log(response)
                    scope.dataSetItems();
                    datasetColumn.expression = "";
                    datasetColumn.fieldName = "";
                    datasetColumn.fieldType = "";
                    datasetColumn.format = "";
                    datasetColumn.functionName = "";
                    datasetColumn.columnName = "";
                });
            }
            scope.editDataset = function (datasetFields) {
                console.log(datasetFields);
                var functionName = null;
                var columnName = null;
                if (datasetFields.functionName != null) {
                    var str = datasetFields.functionName;
                    var findIndex = str.indexOf("(");
                    functionName = str.slice(0, findIndex);
                    columnName = str.slice(findIndex + 1, str.length - 1);
                }

                var editData = {
                    id: datasetFields.id,
                    expression: datasetFields.expression,
                    fieldName: datasetFields.fieldName,
                    fieldType: datasetFields.type,
                    format: datasetFields.displayFormat,
                    functionName: functionName,
                    columnName: columnName
                };
                scope.datasetColumn = editData;
                console.log(editData);
            }
            scope.deleteDataset = function (datasetColumn) {
                $http({method: "DELETE", url: 'admin/ui/dataSetFormulaColumns/' + datasetColumn.id}).success(function (response) {
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
            }
            ;


        }
    };
});
