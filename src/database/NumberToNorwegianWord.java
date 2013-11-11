package database;

public class NumberToNorwegianWord {
	
	/**
	 * Inneholder en liste med navn paa hvert tiende tall fra ti til hundre
	 */
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
	
	/**
	 * Inneholder navnet paa tallene fra en til tjue
	 */
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
	 * Inneholder listen over navn for hver tredje null
	 * Kilde: http://no.wikipedia.org/wiki/tall#Store_tall
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
	
	/**
	 * Inneholder navnet paa et tusen og et hundre, siden de bruker et og ikke en
	 */
	private static final String[] specialCases = {
	    "null",
	    "et hundre",
	    "et tusen"
	  };
	
	/**
	 * tar inne nummeret og gir det tilbake som en string med det norske navnet for tallet
	 * @param number
	 * @return
	 */
	public static String numberToWord(long number) {
		
		if (number >= thousandPowerOf(thousands.length+1)){
			return "numberet er for stort for denne funksjonen";
		}
		
		String word = "";
		
		word += remainingNumberLessThanTwoThousand(number);
		if (!word.equals("")) {
			return word;
		}
		
//		if (number < 2000 && number > 999) {
//			word += checkSpecialCases(number);
//			number -= 1000;
//			if (number > 0) {
//				if (!word.equals("")) {
//					word += " ";
//				}
//				word +=numberBelowThousand((int) number, true);
//			}
//			return word;
//		}
		
		for (int i = thousands.length-1; i > -1; i--) {
			
			if (number < 2000) {
				if (!word.equals("")) {
					word += " ";
				}
				word += remainingNumberLessThanTwoThousand(number);
				return word;
			}
			
			int answer = (int) (number/thousandPowerOf(i));
			
			if (answer > 0) {
				if (!word.equals("")) {
					word += " ";
				}
				if (word.equals("")) {
					word +=numberBelowThousand(answer, false) + " " + thousands[i];
				} else if ((!word.equals("")) && (i == 0)) {
					word +=numberBelowThousand(answer, true) + " " + thousands[i];
				} else {
					word +=numberBelowThousand(answer, false) + " " + thousands[i];
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
	
	/**
	 * tar inn et tall mindr enn tusen og returnerer det norske navnet for tallet, hvis tallet er en del av et stoerre tall, vil det under visse forhold sette et og foran det returnerte tallet
	 * @param number
	 * @param partOfBiggerNumber
	 * @return
	 */
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
	
	/**
	 * brukes for tall mindre enn 2000
	 * @param number
	 * @return
	 */
	private static String remainingNumberLessThanTwoThousand(long number) {
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
		}else if (number < 1000){
			if (!word.equals("")) {
				word += " ";
			}
			word +=numberBelowThousand((int) number, true);
		}
		return word;
	}
	
	/**
	 * returner verdien av 1000 opphoeyd i det tilsendte tallet
	 * @param power
	 * @return
	 */
	private static long thousandPowerOf(long power) {
		long returnValue = 1;
		
		for (int i = 0; i < power; i++) {
			returnValue *= 1000;
		}
		
		return returnValue;
	}
	
	/**
	 * sjekker om tallet som skal brukes er et hundre eller et tusen
	 * @param number
	 * @return
	 */
	private static String checkSpecialCases(long number){
		String word = "";
		
		if (number < 200 && number > 99) {
			word += specialCases[1];
		} else if (number < 2000 && number > 999) {
			word += specialCases[2];
		}
		
		return word;
	}
	
	/**
	 * brukt for aa teste koden
	 * @param args
	 */
	public static void main(String[] args) {
		
//		for (long i = 0; i < thousandPowerOf(thousands.length+1); i++) {
		for (long i = thousandPowerOf(6)+3000; i < thousandPowerOf(6)+3198; i++) {
			System.out.println(i + ": " + numberToWord(i));
		}

	}

}
