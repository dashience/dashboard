app.controller('DataSetController', function ($scope, $http, $stateParams, $filter, $timeout) {
    $scope.dataSetFlag = false;
    $scope.nwStatusFlag = false;
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
        } else if (dataSource === "analytics")
        {
            $scope.report = $scope.analyticsPerformance;
            $scope.dataSetFlag = true;
            $scope.nwStatusFlag = false;
        } else if (dataSource === "facebook")
        {
            $scope.report = $scope.facebookPerformance;
            console.log($scope.report);
            $scope.dataSetFlag = true;
            $scope.nwStatusFlag = false;
        } else if (dataSource === "instagram")
        {
            $scope.report = $scope.instagramPerformance;
            $scope.dataSetFlag = true;
            $scope.nwStatusFlag = false;
        } else if (dataSource === "linkedin")
        {
            $scope.report = $scope.linkedinPerformance;
            $scope.dataSetFlag = true;
            $scope.nwStatusFlag = false;
        } else {
            $scope.dataSetFlag = false;
            $scope.nwStatusFlag = false;
        }
    };

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
                    name: 'Hour Of Week'
                },
                {
                    type: 'ga:dayOfWeekName',
                    name: 'Day Of Week'
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
                    name: 'Channel Grouping'
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
                    name: 'Channel Grouping'
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
                    name: 'Day Of Week'
                },
                {
                    type: 'ga:dateHour',
                    name: 'Hour Of Day'
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
                    name: 'Channel Grouping'
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
                    name: 'Channel Grouping'
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
                    name: 'Day Of Week'
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
                    name: 'event Label'
                },
                {
                    type: 'ga:eventAction',
                    name: 'event Action'
                },
                {
                    type: 'ga:channelGrouping',
                    name: 'Channel Grouping'
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
                    type: 'day',
                    name: 'Day'
                },
                {
                    type: 'week',
                    name: 'Week'
                },
                {
                    type: 'month',
                    name: 'Month'
                },
                {
                    type: 'year',
                    name: 'Year'
                },
                {
                    type: 'quarter',
                    name: 'Quarter'
                },
                {
                    type: 'hourOfTheDay',
                    name: 'Hour Of The Day'
                },
                {
                    type: 'DayOfWeek',
                    name: 'Day Of Week'
                }
            ],
            productSegments: [
                {
                    type: 'device',
                    name: 'Device'
                },
                {
                    type: 'network search partner',
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
                    type: 'day',
                    name: 'Day'
                },
                {
                    type: 'week',
                    name: 'Week'
                },
                {
                    type: 'month',
                    name: 'Month'
                },
                {
                    type: 'year',
                    name: 'Year'
                },
                {
                    type: 'quarter',
                    name: 'Quarter'
                },
                {
                    type: 'hourOfTheDay',
                    name: 'Hour Of The Day'
                },
                {
                    type: 'DayOfWeek',
                    name: 'Day Of Week'
                }
            ],
            productSegments: [
                {
                    type: 'device',
                    name: 'Device'
                },
                {
                    type: 'network search partner',
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
                    type: 'day',
                    name: 'Day'
                },
                {
                    type: 'week',
                    name: 'Week'
                },
                {
                    type: 'month',
                    name: 'Month'
                },
                {
                    type: 'year',
                    name: 'Year'
                },
                {
                    type: 'quarter',
                    name: 'Quarter'
                }
            ],
            productSegments: [
                {
                    type: 'device',
                    name: 'Device'
                },
                {
                    type: 'network search partner',
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
                    type: 'day',
                    name: 'Day'
                },
                {
                    type: 'week',
                    name: 'Week'
                },
                {
                    type: 'month',
                    name: 'Month'
                },
                {
                    type: 'year',
                    name: 'Year'
                },
                {
                    type: 'quarter',
                    name: 'Quarter'
                }
            ],
            productSegments: [
                {
                    type: 'device',
                    name: 'Device'
                },
                {
                    type: 'network search partner',
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
                    type: 'day',
                    name: 'Day'
                },
                {
                    type: 'week',
                    name: 'Week'
                },
                {
                    type: 'month',
                    name: 'Month'
                },
                {
                    type: 'year',
                    name: 'Year'
                },
                {
                    type: 'quarter',
                    name: 'Quarter'
                }
            ],
            productSegments: [
                {
                    type: 'device',
                    name: 'Device'
                },
                {
                    type: 'network search partner',
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
                    type: 'day',
                    name: 'Day'
                },
                {
                    type: 'week',
                    name: 'Week'
                },
                {
                    type: 'month',
                    name: 'Month'
                },
                {
                    type: 'year',
                    name: 'Year'
                },
                {
                    type: 'quarter',
                    name: 'Quarter'
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
                    type: 'day',
                    name: 'Day'
                },
                {
                    type: 'week',
                    name: 'Week'
                },
                {
                    type: 'month',
                    name: 'Month'
                },
                {
                    type: 'year',
                    name: 'Year'
                },
                {
                    type: 'quarter',
                    name: 'Quarter'
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
                    type: 'day',
                    name: 'Day'
                },
                {
                    type: 'week',
                    name: 'Week'
                },
                {
                    type: 'month',
                    name: 'Month'
                },
                {
                    type: 'year',
                    name: 'Year'
                },
                {
                    type: 'quarter',
                    name: 'Quarter'
                }
            ],
        },
        {
            type: 'geoPerformance',
            name: 'Geo',
            timeSegments: [
                {
                    type: 'day',
                    name: 'Day'
                },
                {
                    type: 'week',
                    name: 'Week'
                },
                {
                    type: 'month',
                    name: 'Month'
                },
                {
                    type: 'year',
                    name: 'Year'
                },
                {
                    type: 'DayOfWeek',
                    name: 'Day Of Week'
                },
                {
                    type: 'quarter',
                    name: 'Quarter'
                },
                {
                    type: 'country',
                    name: 'Country'
                },
                {
                    type: 'region',
                    name: 'Region'
                },
                {
                    type: 'city',
                    name: 'City'
                },
                {
                    type: 'metroArea',
                    name: 'Metro Area'
                },
                {
                    type: 'zip',
                    name: 'Zip'
                }
            ],
            productSegments: [
                {
                    type: 'device',
                    name: 'Device'
                },
                {
                    type: 'network search partner',
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
                    type: 'day',
                    name: 'Day'
                },
                {
                    type: 'week',
                    name: 'Week'
                },
                {
                    type: 'month',
                    name: 'Month'
                },
                {
                    type: 'year',
                    name: 'Year'
                },
                {
                    type: 'quarter',
                    name: 'Quarter'
                }
            ],
            productSegments: [
                {
                    type: 'device',
                    name: 'Device'
                },
                {
                    type: 'network search partner',
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
                    type: 'hourOfTheDay',
                    name: 'hourOfTheDay'
                },
                {
                    type: 'DayOfWeek',
                    name: 'DayOfWeek'
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
        }
    ];



    $scope.getTimeSegemens = function () {
        $scope.dataSet.timeSegment = "";
        $scope.dataSet.productSegment = "";

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
        if (dataSet.networkType !== null && typeof (dataSet.networkType) !== "undefined")
        {
            var networkType = dataSet.networkType.map(function (value, key) {
                if (value) {
                    return value.type;
                }
            }).join(',');
            dataSet.networkType = networkType;
            $scope.nwStatusFlag = true;
        } else {
            $scope.nwStatusFlag = false;
        }
        $http({method: dataSet.id ? 'PUT' : 'POST', url: 'admin/ui/dataSet', data: dataSet}).success(function (response) {
            getItems();
        });
        $scope.dataSet = "";
        $scope.showPreviewChart = false;
        $scope.previewData = null;
        $scope.dataSetFlag = false;
    };

    $scope.editDataSet = function (dataSet) {
        console.log(dataSet);
        if (dataSet.networkType !== null)
        {
            dataSet.networkType = dataSet.networkType.split(',').map(function (value, key) {
                return {
                    'name': value ? value : ''
                }
            });
            $scope.nwStatusFlag = true;
        } else {

            $scope.nwStatusFlag = false;
        }
        console.log(dataSet.networkType);
        var data = {
            id: dataSet.id,
            name: dataSet.name,
            query: dataSet.query,
            url: dataSet.url,
            reportName: dataSet.reportName,
            timeSegment: dataSet.timeSegment,
            productSegment: dataSet.productSegment,
            networkType: dataSet.networkType ? dataSet.networkType : '',
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
    };

    $scope.refreshDataSet = function (dataSet) {
        $scope.showPreviewChart = true;
        $scope.previewData = null;
        $timeout(function () {
            $scope.previewData = dataSet;
        }, 50);
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
                '<table ng-if="ajaxLoadingCompleted" class="table table-responsive table-bordered table-l2t">' +
                '<thead><tr>' +
                '<th class="text-capitalize table-bg" ng-repeat="col in tableColumns">' +
                '{{col.displayName}}' +
                '</th>' +
                '</tr></thead>' +
                '<tbody ng-repeat="tableRow in tableRows">' +
                '<tr class="text-capitalize">' +
                '<td ng-repeat="col in tableColumns">' +
                '<div>{{tableRow[col.fieldName]}}</div>' +
                '</td>' +
                '</tbody>' +
                '</table>',
        link: function (scope, element, attr) {
            scope.loadingTable = true;
            var dataSourcePath = JSON.parse(scope.path)
//            console.log(dataSourcePath);
//            console.log(dataSourcePath.dataSourceId.userName);
//            console.log(dataSourcePath.dataSourceId.connectionString);
//            console.log(dataSourcePath.dataSourceId.sqlDriver);
//            console.log(dataSourcePath.dataSourceId.password);
            var url = "admin/proxy/getData?";
            if (dataSourcePath.dataSourceId.dataSourceType == "sql") {
                url = "admin/proxy/getJson?url=../dbApi/admin/dataSet/getData&";
            }
            if (dataSourcePath.dataSourceId.dataSourceType == "csv") {
                url = "admin/csv/getData?";
            }
            if (dataSourcePath.dataSourceId.dataSourceType == "facebook") {
                url = "admin/proxy/getData?";
            }

            var dataSourcePassword;
            if (dataSourcePath.dataSourceId.password) {
                dataSourcePassword = dataSourcePath.dataSourceId.password;
            } else {
                dataSourcePassword = '';
            }
            console.log(dataSourcePath.networkType);
            
            $http.get(url + 'connectionUrl=' + dataSourcePath.dataSourceId.connectionString +
                    "&dataSourceId=" + dataSourcePath.dataSourceId.id +
                    "&accountId=" + $stateParams.accountId +
                    "&dataSetReportName=" + dataSourcePath.reportName +
                    "&timeSegment=" + dataSourcePath.timeSegment +
                    "&filter=" + dataSourcePath.networkType.type +
                    "&productSegment=" + dataSourcePath.productSegment +
                    "&driver=" + dataSourcePath.dataSourceId.dataSourceType +
                    "&dataSourceType=" + dataSourcePath.dataSourceId.dataSourceType +
                    "&location=" + $stateParams.locationId +
                    "&startDate=" + $stateParams.startDate +
                    "&endDate=" + $stateParams.endDate +
                    '&username=' + dataSourcePath.dataSourceId.userName +
                    '&password=' + dataSourcePassword +
                    '&port=3306&schema=deeta_dashboard&query=' + encodeURI(dataSourcePath.query)).success(function (response) {
                scope.ajaxLoadingCompleted = true;
                scope.loadingTable = false;
                scope.tableColumns = response.columnDefs;
                scope.tableRows = response.data;
            });
        }
    };
});
