var app = angular.module("DashienceApp", ['ui.router', 'ui.bootstrap', 'ui', 'ngSanitize', 'pascalprecht.translate','ngCookies','LocalStorageModule','oc.lazyLoad']);
//var app = angular.module("DashienceApp", ["ui.router", 'ngRoute', "ui.bootstrap", "ui", "ngCookies", "angularScreenfull", "ngDraggable", "angularUtils.directives.dirPagination", 'smart-table', 'angular.filter', 'ngSanitize', 'ui.select', 'ui.sortable', 'LocalStorageModule', 'nsPopover', 'ng-draggable-widgets', 'ngSanitize', 'ngAnimate', 'smartArea', 'pascalprecht.translate']);

//document.addEventListener("DOMContentLoaded", function() {
//    var fileref = document.createElement('script');
//    fileref.setAttribute("type", "text/javascript");
//    fileref.setAttribute("src", 'static/lib/js/template-admin.js');
//    fileref.setAttribute("src", 'static/lib/js/template-page-index.js');
//    fileref.setAttribute("src", 'static/lib/js/basic-form-element.js');
//    fileref.setAttribute("src", 'static/lib/js/template-demo.js');
////Here I need an event to know that jquery is 
////loaded to run stuff that needs jquery
//});