function showCalendarByTagName(formName, fieldName){
    //var formName_ = getNetuiTagName(formName);
    var fieldName_ = getNetuiTagName(fieldName);
    //var element = document[formName_][fieldName_];
    //popUpCalendar(element, element, DATE_FORMAT_STR);
	displayDatePicker(fieldName_);
}

function showCalendar(formName, inputName){
    //var element = document[formName][inputName];
	//alert(element);
    //popUpCalendar(element, element, DATE_FORMAT_STR);
	displayDatePicker(inputName);
}
function showCalendar(inputName){
	displayDatePicker(inputName);
	//var element;
	//eval("element = document.all."+inputName);
	//popUpCalendar(element, element, DATE_FORMAT_STR);
}

