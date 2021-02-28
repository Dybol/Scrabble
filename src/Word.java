import lombok.Getter;

@Getter
public class Word {

	private final String theWord;
	private final int points;
	private final int multipliers;
	private final boolean sevenLettersBonus;

	public Word(final String theWord, final int points, final int multipliers, final boolean sevenLettersBonus) {
		this.theWord = theWord;
		this.points = points;
		this.multipliers = multipliers;
		this.sevenLettersBonus = sevenLettersBonus;
	}

	public Word(final String theWord, final int points, final int multipliers) {
		this.theWord = theWord;
		this.points = points;
		this.multipliers = multipliers;
		this.sevenLettersBonus = false;
	}

	public int calculatePoints() {
		return (this.points * this.multipliers) + (this.sevenLettersBonus ? 50 : 0);
	}

	@Override
	public String toString() {
		return "Word{" +
				"theWord='" + theWord + '\'' +
				", points=" + points +
				", multipliers=" + multipliers +
				'}';
	}
}
