var table = document.getElementById("rouletteTab");
if (table != null) {
	for (var i = 0; i < table.rows.length; i++) {
		for (var j = 0; j < table.rows[i].cells.length; j++)
			table.rows[i].cells[j].onclick = function() {
				tableText(this);
			};
	}
}

function tableText(tableCell) {
	var value = tableCell.innerHTML.replace('<span>', '')
			.replace('</span>', '').replace('<span class="vt">', '').replace(/(\r\n|\n|\r)/gm,'');
	if (isNumeric(value))
		addBet('STRIGHTUP', value, '10');
	if (value == '1st')
		addBet('COLUMN1', '', '10');
	if (value == '2nd')
		addBet('COLUMN2', '', '10');
	if (value == '3rd')
		addBet('COLUMN3', '', '10');
	if (value == '1st 12')
		addBet('DOZEN1', '', '10');
	if (value == '2nd 12')
		addBet('DOZEN2', '', '10');
	if (value == '3rd 12')
		addBet('DOZEN3', '', '10');
	if (value == 'EVEN')
		addBet('EVEN', '', '10');
	if (value == 'ODD')
		addBet('ODD', '', '10');
	if (value == '1 to 18')
		addBet('HALF1', '', '10');
	if (value == '19 to 36')
		addBet('HALF2', '', '10');
	if (value == 'RED')
		addBet('RED', '', '10');
	if (value == 'BLACK')
		addBet('BLACK', '', '10');
	
	//alert(value);
	// STRIGHTUP(35, 1), //
	// SPLIT(17, 2), // to calculate
	// STREET(11, 3), //
	// CORNER(8, 4), //
	// FIVE(6, 5), //
	// SIXLINE(5, 6), //
	// COLUMN1(2, 12), COLUMN2(2, 12), COLUMN3(2, 12), //
	// DOZEN1(2, 12), DOZEN2(2, 12), DOZEN3(2, 12), //
	// EVEN(1, 18), ODD(1, 18), //
	// HALF1(1, 18), HALF2(1, 18), //
	// RED(1, 18), BLUE(1, 18);
}

function isNumeric(num) {
	num = "" + num; // coerce num to be a string
	return !isNaN(num) && !isNaN(parseFloat(num));
}