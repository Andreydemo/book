$(document).ready(function(){	
	$('.spinner-mask').css({"opacity": "1", "animation-iteration-count": "1"});
	$('.education-list').css({"opacity": "0"});
	var myFirebaseRef = new Firebase("https://cdp-db.firebaseio.com/");
		
	function createEducationItem(element) {
		var li = document.createElement("li");
		$(li).addClass('education-item');
		var date = document.createElement("div");
		$(date).addClass('education-date');
		var span = document.createElement("span");
		$(span).text(element.date);
		$(span).appendTo(date);
		$(date).appendTo(li);
		var info = document.createElement("div");
		$(info).addClass('education-info');
		var h3 = document.createElement("h3");
		$(h3).text(element.title);
		var p = document.createElement("p");
		$(p).text(element.someText);
		var div = document.createElement("div");
		$(div).addClass('arrow-left');
		$(h3).appendTo(info);
		$(p).appendTo(info);
		$(div).appendTo(info);
		$(info).appendTo(li);
			
		$(".education-list").append(li);
	}	
	
	function renderList(element, from, to) {
		for (i = from; i < to; i++) {
			createEducationItem(element[i]);
		}
	}
		
	function initialUpload(){
	  $('.spinner-mask').css({"opacity": "0"});
	  $('.education-list').css({"opacity": "1"});
				 
	  myFirebaseRef.on("child_added", function(snapshot) {
			renderList(snapshot.val(), 0, 5);
		}, 
		function (errorObject) {
			console.log("The read failed: " + errorObject.code);
        });
	};
	setTimeout(initialUpload, 2000);
	
	$(".education-list").scroll(function() {
	   if($(this).scrollTop() + $(this).innerHeight() >= $(this)[0].scrollHeight) {
			myFirebaseRef.on("child_added", function(snapshot) {
			var elements = snapshot.val();
			var numItems = $('.education-item').length;
			renderList(elements, numItems, elements.length);
		}, 
		function (errorObject) {
			console.log("The read failed: " + errorObject.code);
        });
	   }
	});

	var $grid = $('.grid').isotope({
	  itemSelector: '.grid-item',
	});
	
	$(".code-link").click(function(event){
		event.preventDefault();
		$grid.isotope({ filter: '.code' });
	});
	
	$(".ui-link").click(function(){
		event.preventDefault();
		$grid.isotope({ filter: '.ui' });
	});
	
	$(".all-link").click(function(){
		event.preventDefault();
		$grid.isotope({ filter: '*' });
	});
	
	$('.add-skill-form').submit(function(){
		var text = $('.input-skill input[type="text"]').val();
		var width = $('.input-skill input[type="number"]').val() * 9.6;
		var element = document.createElement("li");
		$(element).text(text);
		$(element).addClass('skill');
		$(element).css({"width":width + "%"});
		$(".skill-list").append(element);
		return false;
	});
	
	$('a.aside-text').on('click', function(e) {
		$('a.aside-text').removeClass('active');
        var id = $(this).attr('href').slice(1),
            content_title = $('#'+id);
		var el = e.target;
		$(this).addClass('active');
		
        $('html, body').animate(
            {
                scrollTop: $($.attr(this, 'href')).offset().top
            },
            {
                duration: 750,
            }
        );
	});
	
	$("a.button-up").click(function() {
		$("html, body").animate({ scrollTop: 0 }, 
			{
                duration: 750,
            });
		return false;
	});
});

$(document).scroll(function(){
    var scrollPos = $(document).scrollTop();
        $('.aside-item a').each(function () {
			var target = $(this.hash), targetTop = target.position().top;
            if (targetTop - scrollPos <= 60) {
                $('.aside-item .active').removeClass("active");
				$(this).addClass("active");
            }
        })
});