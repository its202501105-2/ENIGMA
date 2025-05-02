import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * ローター
 */
public class Rotor {
	/** ローターの入力文字セット */
	private String charSet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
	/** ローターの出力文字セット */
	private String charSetPair;
	/** ローターの位相 */
	private int offset = 0;

	/**
	 * シード値を基に標準的な英字[A-Z]が設定されたローターを生成します。
	 * 生成後に入力文字セットを変更することはできません。
	 * 
	 * @param seed シード値。
	 * 出力文字セットは一意に決定されます。
	 * 負の値が指定された場合はランダムに決定されます。
	 */
	public Rotor(int seed) {
		generateCharSetPair(seed);
	}

	/**
	 * シード値と入力文字セットを基にローターを生成します。
	 * 生成後に入力文字セットを変更することはできません。
	 * 
	 * @param seed シード値。
	 * 出力文字セットは一意に決定されます。
	 * 負の値が指定された場合はランダムに決定されます。
	 * @param charSet 入力文字セット。
	 * 重複した文字が存在しない、長さが1以上の文字列です。
	 * 条件に合致しない文字列が指定された場合は標準的な英字[A-Z]が設定されます。
	 * リフレクターと併用する場合はリフレクターと同じ条件で指定する必要があります。
	 */
	public Rotor(int seed, String charSet) {
		if (charSet != null && charSet.length() > 0 && Utility.isNonDuplicateString(charSet))
			this.charSet = charSet;

		generateCharSetPair(seed);
	}

	// Char set

	/**
	 * 入力文字セットを取得します。
	 * 
	 * @return 入力文字セット。
	 */
	public String getCharSet() {
		return charSet;
	}

	// Char set pair

	/**
	 * 出力文字セットを生成します。
	 * 
	 * @param seed シード値。
	 * 出力文字セットは一意に決定されます。
	 * 負の値が指定された場合はランダムに決定されます。
	 */
	private void generateCharSetPair(int seed) {
		Random random = seed >= 0 ? new Random(seed) : new Random();

		StringBuffer charSetPairBuf = new StringBuffer();
		List<Integer> charSetPairCombineIndexBuf = IntStream.range(0, charSet.length()).boxed()
				.collect(Collectors.toList());

		for (int i = 0; i < charSet.length(); i++)
			charSetPairBuf.append(charSet
					.charAt(charSetPairCombineIndexBuf.remove(random.nextInt(charSetPairCombineIndexBuf.size()))));

		charSetPair = charSetPairBuf.toString();
	}

	/**
	 * 出力文字セットを取得します。
	 * 
	 * @return 出力文字セット。
	 */
	public String getCharSetPair() {
		return charSetPair;
	}

	/**
	 * 出力文字セットを設定します。
	 * 
	 * @param newCharSetPair 新しい出力文字セット。
	 * NULLや入力文字セットと異なる長さの文字列が指定された場合はエラーコード1が返されます。
	 * 入力文字セットに存在しない文字が含まれている場合はエラーコード2が返されます。
	 * @return エラーコード。
	 * 正常に設定された場合は0が返されます。
	 */
	public int setCharSetPair(String newCharSetPair) { // => error code
		// Check

		if (newCharSetPair == null || charSet.length() != newCharSetPair.length())
			return 1;

		for (int i = 0; i < newCharSetPair.length(); i++)
			if (charSet.indexOf(newCharSetPair.charAt(i)) == -1)
				return 2;

		// Set

		charSetPair = newCharSetPair;

		return 0;
	}

	// Offset

	/**
	 * 位相を取得します。
	 * 
	 * @return 位相。
	 */
	public int getOffset() {
		return offset;
	}

	/**
	 * 位相に対応する入力文字セットの文字を取得します。
	 * 
	 * @return 位相に対応する入力文字セットの文字。
	 */
	public char getOffsetChar() {
		return charSet.charAt(offset);
	}

	/**
	 * 位相を設定します。
	 * 
	 * @param newOffset 新しい位相。
	 * 値の正負を問いません。
	 * 値が文字セットの長さを超える場合は周期に収納されます。
	 * @return 設定された位相。
	 */
	public int setOffset(int newOffset) { // => result offset
		offset = Utility.getCycleInt(newOffset, charSet.length());

		return offset;
	}

	/**
	 * 位相を設定します。
	 * 
	 * @param charCode 位相に対応する入力文字セットの文字。
	 * 文字セットに存在しない文字が指定された場合はエラーコード1が返されます。
	 * @return エラーコード。
	 * 正常に設定された場合は0が返されます。
	 */
	public int setOffset(char charCode) { // => error code
		int offsetBuf = charSet.indexOf(charCode);

		if (offsetBuf != -1) {
			offset = offsetBuf;

			return 0;
		} else {
			return 1;
		}
	}

	/**
	 * 位相をインクリメントします。
	 * 位相の周期は文字セットの長さです。
	 * 
	 * @return 一周したら真、一周しなければ偽。
	 */
	public boolean rotate() { // => is full circle
		offset++;

		if (offset >= charSet.length()) {
			offset = 0;

			return true;
		} else {
			return false;
		}
	}

	// Function

	/**
	 * 文字を変換します。
	 * 
	 * @param charCode 変換する文字。
	 * 文字セットに存在しない文字が指定された場合はNULL文字が返されます。
	 * @return 変換された文字。
	 */
	public char convertChar(char charCode) {
		int charSetIndex = charSet.indexOf(charCode);

		if (charSetIndex != -1) {
			int charSetPairIndex = Utility.getCycleInt(charSetIndex + offset, charSetPair.length());

			return charSetPair.charAt(charSetPairIndex);
		} else {
			return 0;
		}
	}

	/**
	 * 文字を逆変換します。
	 * 
	 * @param charCode 変換する文字。
	 * 文字セットに存在しない文字が指定された場合はNULL文字が返されます。
	 * @return 変換された文字。
	 */
	public char convertReverseChar(char charCode) {
		int charSetIndex = charSetPair.indexOf(charCode);

		if (charSetIndex != -1) {
			int charSetPairIndex = Utility.getCycleInt(charSetIndex - offset, charSetPair.length());

			return charSet.charAt(charSetPairIndex);
		} else {
			return 0;
		}
	}
}
