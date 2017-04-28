function setTempPhoto(patientID) {
	var result;
	switch(patientID) {
		case "1000": {
			result = "A123456789";
			break;
		}
		case "1001": {
			result = "B112345678";
			break;
		}
		case "1002": {
			result = "C123456789";
			break;
		}
		case "1003": {
			result = "D112345678";
			break;
		}
		case "1004": {
			result = "E101234567";
			break;
		}
		case "1005": {
			result = "F223456789";
			break;
		}
		case "1006": {
			result = "G212345678";
			break;
		}
		case "1007": {
			result = "H223456789";
			break;
		}
		case "1008": {
			result = "I212345678";
			break;
		}
		case "1009": {
			result = "J201234567";
			break;
		}
		default: {
			result = "default";
			break;
		}
	}
	return result;
}

function setTempPhotoByName(patientName) {
	var result;
	switch(patientName) {
		case "王小明": {
			result = "A123456789";
			break;
		}
		case "王小弟": {
			result = "B112345678";
			break;
		}
		case "李志偉": {
			result = "C123456789";
			break;
		}
		case "陳志明": {
			result = "D112345678";
			break;
		}
		case "沈建宏": {
			result = "E101234567";
			break;
		}
		case "楊美玲": {
			result = "F223456789";
			break;
		}
		case "莊雅婷": {
			result = "G212345678";
			break;
		}
		case "黃淑娟": {
			result = "H223456789";
			break;
		}
		case "謝淑芬": {
			result = "I212345678";
			break;
		}
		case "陳雅惠": {
			result = "J201234567";
			break;
		}
		default: {
			result = "default";
			break;
		}
	}
	return result;
}