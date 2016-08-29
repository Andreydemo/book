(function() {
    angular.module('skills').component('skillsList', {
            restrict : 'E',
            controller: function() {
            },
            controllerAs: 'skillsListCtrl',
            templateUrl: 'js/skills-list/skills.list.template.html',
            bindings: {
               skills: '<',
            }
    });
})();