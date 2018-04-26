app.service("httpService", function ($http, $q) {
    var httpParams = {};
    this.httpProcess = function (reqMethod, reqUrl, params) {
        if(!reqUrl){
            return;
        }
        var deferred = $q.defer();
        httpParams = {
            method: reqMethod,
            url: reqUrl
        };
        
        if (params) {
            httpParams.data = params;
        }
        return $http(httpParams).then(function (success) {
            deferred.resolve(success.data);
            return deferred.promise;
        }, function (error) {
            deferred.reject(error);
            return deferred.promise;
        });
    };
});


