//angular.module('MultiLanguageApp', ['pascalprecht.translate'])
app.config(function ($translateProvider) {
    $translateProvider.preferredLanguage('en');
    $translateProvider.translations('cn', {
        Username_Title: '请输入您的用户名',
        Username: '用户名',
        Password_Title: '请输入您的密码',
        Password: '密码',
        Login: '登录',
        'Invalid Login': '登录无效'
    });
    
    $translateProvider.translations('en', {
        Username_Title: 'Please enter your Username',
        Username: 'Username',
        Password_Title: 'Please enter your Password',
        Password: 'Password',
        Login: 'Login',
        'Invalid Login': 'Invalid Login'
    });
});