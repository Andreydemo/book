$(document).ready(function() {
                    var shop = {
                        init : function() {

                            var me = this;

                            this.buttons = {};
                            this.modals = {};
                            this.inputs = {};
                            this.forms = {};

                            // buttons
                            this.buttons.refreshCaptchaBtn = $('#refresh-captcha-btn');
                            this.buttons.submitRegForm = $('#submitRegForm');

                            // forms
                            this.forms.registrationForm = $('#register-form');
                            this.forms.loginForm = $('#login-form');

                            this.inputs.regformInputs = $('.regform-input');
                            this.inputs.logformInputs = $('.logform-input');

                            this.validationRules = {
                                validEmail : [
                                        /^\w+[\w-.]*@\w+((-\w+)|(\w*))\.[a-z]{2,3}$/,
                                        "Please, enter correct email" ],
                                validName : [
                                        /^[A-Z\u0410-\u042f][a-z\u0430-\u044f]{2,16}$/,
                                        "Name must starts with upper case letter and not contain digits or white spaces" ],
                                validPassword : [
                                        /^(?=.*[A-Za-z])(?=.*\d)[A-Za-z\d]{6,}$/,
                                        "Password must contains minimum 6 characters and at least 1 digit" ],
                                validMobile : [
                                        /^\+38 \(0([1-9]{2})\) ([0-9]{3})-([0-9]{2})-([0-9]{2})$/,
                                        "Please, enter correct mobile number" ],

                            };

                            // actions
                            this.buttons.refreshCaptchaBtn.click(function(e) {
                                e.preventDefault();
                                me.refreshCaptcha();
                            });

                            /* this.buttons.submitRegForm.click(function(e) {
                            // var res1 = me.validate(me.inputs.regformInputs,
                            // me.validationRules);
                            // var res2 = me.confirmPassword();
                            // if (!res1 || !res2) {
                            // e.preventDefault();
                            // }
                             });*/

                            $('.paging a').each(function(index, item) {
                                 var regexp = /((\?|\&)page=.*)/g, 
                                 str = location.search, 
                                 match = regexp.exec(str), 
                                 string = match ? str.replace(regexp, '') : str, 
                                 querySymbol = string ? '&' : '?';
                                 
                                 item.setAttribute('href', location.protocol + '//' + location.hostname + ':' + location.port + location.pathname
                                         + string + querySymbol + 'page=' + item.getAttribute('data-page'));
                             });

                        },

                        refreshCaptcha : function() {
                            var id = (new Date()).getTime();
                            $('#captcha').attr('src', 'captcha?id=' + id);
                            $('#captchaKey').attr('value', id);
                            $('#captcha').slideDown('fast');
                        },

                        validate : function(inputs, rules) {
                            var isValid = true;
                            inputs
                                    .each(function() {
                                        var value = $(this).val();

                                        if (!value) {
                                            $(this).prev().html(
                                                    'This field is required');
                                            isValid = false;
                                        } else {
                                            var key = $(this).attr('validate'), rule = rules[key][0], msg = rules[key][1];
                                            if (!rule.test(value)) {
                                                $(this).prev().html(msg);
                                                isValid = false;
                                            } else {
                                                $(this).prev().html("");
                                            }
                                        }
                                    });
                            return isValid;
                        },

                        confirmPassword : function() {
                            if ($('#password').val() != $('#re-password').val()) {
                                $('#re-password').prev().html(
                                        "You have not confirmed you password");
                                return false;
                            }
                            $('#re-password').prev().html("");
                            return true;
                        }

                    }

                    shop.init();

                });