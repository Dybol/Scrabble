import lombok.Getter;
import lombok.Setter;

import java.util.LinkedList;
import java.util.List;

@Getter
public class Player {

	@Setter
	private List<Character> letters;

	private int points = 0;

	private final String name;

	//tylko te glowne, bo z tymi co sie wyraz styka itp juz jest problem z kolejnoscia
	private final List<Word> playersWords = new LinkedList<>();

	public Player(final String name) {
		this.name = name;
	}

	public void addWord(final Word word) {
		this.playersWords.add(word);
	}

	public void clearLetters() {
		this.letters.clear();
	}

	public void increasePoints(final int i) {
		this.points += i;
	}

	@Override
	public String toString() {
		return "Player{" +
				"letters=" + letters +
				", points=" + points +
				'}';
	}
}
