import lombok.experimental.UtilityClass;

import java.awt.*;
import java.util.List;
import java.util.*;

@UtilityClass
public class Utils {

	@UtilityClass
	public class Constants {
		public Color MID_COLOR = new Color(200, 50, 0);
		public Color X3_WORD = new Color(128, 0, 0);
		public Color X2_WORD = new Color(255, 69, 0);
		public Color X3_LETTER = new Color(0, 0, 205);
		public Color X2_LETTER = new Color(135, 206, 250);
		public Color DEFAULT = new Color(0, 100, 0);
	}

	public List<Character> drawLetters(final int amount, final Player player) {
		final List<Character> letters = new ArrayList<>();

		final Random generator = new Random();
		final Object[] values = ScrabbleMainClass.getLetters().keySet().toArray();

		for (int i = 0; i < amount; i++) {
			final char actLetter = (Character) values[generator.nextInt(values.length)];
			final int amountOfLetter = ScrabbleMainClass.letters.get(actLetter);

			//to znaczy, ze nie ma juz zadnych liter w puli, czyli nie bedzie zadnego dobierania!
			if (ScrabbleMainClass.getLetterCount() <= 0) {
				System.out.println(player.getLetters() + "<---- Litery gracza " + player.getName());
				if (player.getLetters().size() == 0) {
					System.out.println("KONIEC GRY!");
					break;
				}
				break;
				//continue;
			}
			//koniec danej litery
			if (amountOfLetter == 0) {
				i--;
				continue;
			}

			//koniec liter wszystkich

			letters.add(actLetter);
			ScrabbleMainClass.letters.put(actLetter, ScrabbleMainClass.getLetters().get(actLetter) - 1);
			ScrabbleMainClass.decreaseLetters();

		}
		return letters;
	}


	//one time use
	public Map<Character, Integer> fillLettersCount() {
		final Map<Character, Integer> letters = new HashMap<>();
		letters.put('a', 9);
		letters.put('ą', 1);
		letters.put('b', 2);
		letters.put('c', 3);
		letters.put('ć', 1);
		letters.put('d', 3);
		letters.put('e', 7);
		letters.put('ę', 1);
		letters.put('f', 1);
		letters.put('g', 2);
		letters.put('h', 2);
		letters.put('i', 8);
		letters.put('j', 2);
		letters.put('k', 3);
		letters.put('l', 3);
		letters.put('ł', 2);
		letters.put('m', 3);
		letters.put('n', 5);
		letters.put('ń', 1);
		letters.put('o', 6);
		letters.put('ó', 1);
		letters.put('p', 3);
		letters.put('r', 4);
		letters.put('s', 4);
		letters.put('ś', 1);
		letters.put('t', 3);
		letters.put('u', 2);
		letters.put('w', 4);
		letters.put('y', 4);
		letters.put('z', 5);
		letters.put('ź', 1);
		letters.put('ż', 1);
		return letters;
	}
}
