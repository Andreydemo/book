$(document).ready(function () {
	var slider = document.getElementById('price-range');

	var minInput = $("#minprice"),
		maxInput = $("#maxprice"),
		currentMinVal = minInput.val(),
		currentMaxVal = maxInput.val(),
		minVal = minInput.attr('min'),
		maxVal = minInput.attr('max'),
		resizeInput = function () {
			$(this).attr('size', $(this).val().length);
		},
		resizeAll = function () {
			resizeInput.call(maxInput);
			resizeInput.call(minInput);
		};


		minInput.val(currentMinVal ? currentMinVal : minVal);
		maxInput.val(currentMaxVal ? currentMaxVal : maxVal);
		resizeAll();
		
	noUiSlider.create(slider, {
		start: [currentMinVal ? currentMinVal : minVal,
				currentMaxVal ? currentMaxVal : maxVal],
		connect: true,
		step: 100,
		range: {
			'min': parseInt(minVal),
			'max': parseInt(maxVal),
		}
	});
	slider.noUiSlider.on('slide', function(){
		var values = slider.noUiSlider.get(),
			minVal = parseInt(values[0]),
			maxVal = parseInt(values[1]);

		minInput.val(minVal);
		maxInput.val(maxVal);
		resizeAll();

	});

});