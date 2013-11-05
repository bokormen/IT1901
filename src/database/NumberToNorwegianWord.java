package database;

public class NumberToNorwegianWord {
	
	private static final String[] tens = {
	    "",
	    "ti",
	    "tjue",
	    "tretti",
	    "førti",
	    "femti",
	    "seksti",
	    "sytti",
	    "åtti",
	    "nitti",
	    "hundre"
	  };
	
	private static final String[] oneToTwenty = {
	    "",
	    "en",
	    "to",
	    "tre",
	    "fire",
	    "fem",
	    "seks",
	    "sju",
	    "åtte",
	    "ni",
	    "ti",
	    "elve",
	    "tolv",
	    "tretten",
	    "fjorten",
	    "femten",
	    "seksten",
	    "sytten",
	    "atten",
	    "nitten"
	  };
	
	/**
	 * 
	 * Kilde: http://no.wikipedia.org/wiki/number#Store_number
	 */
	private static final String[] thousands = {
	    "",
	    "tusen",
	    "million",
	    "milliard",
	    "billion",
	    "billiard",
	    "trillion",
	    "trilliard",
	    "kvadrillion"
	  };
	
	private static final String[] specialCases = {
	    "null",
	    "et hundre",
	    "et tusen"
	  };
	
	public static String numberToWord(long number) {
		
		if (number >= thousandPowerOf(thousands.length+1)){
			return "numberet er for stort for denne funksjonen";
		}
		
		String word = "";
		
		if (number < 2000 && number > 999) {
			word += checkSpecialCases(number);
			number -= 1000;
			if (number > 0) {
				if (!word.equals("")) {
					word += " ";
				}
				word +=numberBelowThousand((int) number, true);
			}
			return word;
		}
		
		for (int i = thousands.length-1; i > -1; i--) {
			
			int answer = (int) (number/thousandPowerOf(i));
			
			if (answer > 0) {
				if (!word.equals("")) {
					word += " ";
				}
				if (word.equals("")) {
					word +=numberBelowThousand(answer, false) + " " + thousands[i];
				} else {
					word +=numberBelowThousand(answer, true) + " " + thousands[i];
				}
				
				if (answer > 1 && i > 1) {
					word += "er";
				}
			}
			
			number -= answer * thousandPowerOf(i);
		}
		
		if (word.equals("")) {
			word += specialCases[0];
		}
		
		return word;
	}
	
	private static String numberBelowThousand(int number, boolean partOfBiggerNumber) {
		String word = "";
		
		boolean hundred = false;
		
		int answer = (number/100);
		
		if (answer == 1) {
			word += specialCases[1];
			hundred = true;
		} else if (answer > 1) {
			word += oneToTwenty[answer] + " " + tens[10];
			hundred = true;
		}
		
		number -= answer * 100;
		
		if (number > 0 && partOfBiggerNumber && (!hundred)){
			word += "og ";
		} else if (number > 0 && hundred) {
			word += " og ";
		}
		
		if (number < 20) {
			word += oneToTwenty[number];
		} else if (number != 0) {
			answer = (int) (number/10);
			number -= answer*10;
			word += tens[answer];
			if (number > 0) {
				word += " " + oneToTwenty[number];
			}
		}
		
		return word;
	}
	
	private static long thousandPowerOf(long power) {
		long returnValue = 1;
		
		for (int i = 0; i < power; i++) {
			returnValue *= 1000;
		}
		
		return returnValue;
	}
	
	private static String checkSpecialCases(long number){
		String word = "";
		
		if (number < 200 && number > 99) {
			word += specialCases[1];
		} else if (number < 2000 && number > 999) {
			word += specialCases[2];
		}
		
		return word;
	}

	public static void main(String[] args) {
		
//		for (long i = 0; i < thousandPowerOf(thousands.length+1); i++) {
		for (long i = thousandPowerOf(thousands.length-3)+10000000; i < thousandPowerOf(thousands.length-3)+10000010; i++) {
			System.out.println(i + ": " + numberToWord(i));
		}

	}

}
