var app = angular.module("VizBoardApp", ["ui.router", 'ngRoute', "ui.bootstrap", "ui", "ngCookies", "angularScreenfull", "ngDraggable", "angularUtils.directives.dirPagination", 'smart-table', 'angular.filter', 'ngSanitize', 'ui.select', 'ui.sortable', 'LocalStorageModule', 'nsPopover', 'ng-draggable-widgets', 'ngSanitize', 'ngAnimate', 'smartArea', 'pascalprecht.translate']);
//var app = angular.module("VizBoardApp", ["ui.router", "ui.bootstrap", "ui", "ngCookies", "angularScreenfull", "ngDraggable", "angularUtils.directives.dirPagination", 'smart-table', 'angular.filter', 'ngSanitize', 'ui.select', 'ui.grid', 'ui.grid.grouping','ui.sortable', 'ui.grid.edit', 'ui.grid.selection', 'LocalStorageModule','htmlToPdfSave','nsPopover','angular-query-builder']);



app.factory('utilitiesFactory', function ($filter, orderByFilter) {
    
});
//app.service('chartService', function () {
//    this.ennable = function (boolean) {
//        return {enabled: boolean};
//    };
//    this.title = function (title) {
//        return {text: title};
//    };
//});