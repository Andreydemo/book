(function() {
    angular.module('skills').controller('SkillsController', function() {
        this.skill = {};
        this.skills = skills;

        this.addSkill = function() {
            skills.push(this.skill);
            this.skill = {};
        }
    });
    var skills = [
            {
                name: 'HTML',
                width: 95
            },
            {
                name: 'CSS',
                width: 70
            },
            {
                name: 'jQuery',
                width: 50
            },
            {
                name: 'PHP',
                width: 30
            },
            {
                name: 'Laravel',
                width: 20
            },
        ];
})();