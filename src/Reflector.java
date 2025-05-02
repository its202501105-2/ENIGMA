import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * リフレクター
 */
public class Reflector {
	/** リフレクターの入力文字セット */
	private String charSet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
	/** リフレクターの出力文字セット */
	private String charSetPair;

	/**
	 * シード値を基に標準的な英字[A-Z]が設定されたリフレクターを生成します。
	 * 生成後に入力文字セットを変更することはできません。
	 * 
	 * @param seed シード値。
	 * 出力文字セットは一意に決定されます。
	 * 負の値が指定された場合はランダムに決定されます。
	 */
	public Reflector(int seed) {
		generateCharSetPair(seed);
	}

	/**
	 * シード値と入力文字セットを基にリフレクターを生成します。
	 * 生成後に入力文字セットを変更することはできません。
	 * 
	 * @param seed シード値。
	 * 出力文字セットは一意に決定されます。
	 * 負の値が指定された場合はランダムに決定されます。
	 * @param charSet 入力文字セット。
	 * 重複した文字が存在しない、長さが2以上かつ偶数の文字列です。
	 * 条件に合致しない文字列が指定された場合は標準的な英字[A-Z]が設定されます。
	 */
	public Reflector(int seed, String charSet) {
		if (charSet != null && charSet.length() > 0 && charSet.length() % 2 == 0
				&& Utility.isNonDuplicateString(charSet))
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

		char[] charSetPairBuf = new char[charSet.length()];
		List<Integer> charSetPairCombineIndexBuf = IntStream.range(0, charSet.length()).boxed()
				.collect(Collectors.toList());

		for (int i = 0; i < charSet.length(); i++) {
			int targetIndex = charSetPairCombineIndexBuf.indexOf(i);

			if (targetIndex != -1) {
				charSetPairCombineIndexBuf.remove(targetIndex);
			} else {
				continue;
			}

			int pickupIndex = charSetPairCombineIndexBuf.remove(random.nextInt(charSetPairCombineIndexBuf.size()));

			charSetPairBuf[i] = charSet.charAt(pickupIndex);
			charSetPairBuf[pickupIndex] = charSet.charAt(i);
		}

		charSetPair = new String(charSetPairBuf);
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
	 * 対応する文字が一対一の関係ではない場合はエラーコード3が返されます。
	 * @return エラーコード。
	 * 正常に設定された場合は0が返されます。
	 */
	public int setCharSetPair(String newCharSetPair) { // => error code
		// Check

		if (newCharSetPair == null || charSet.length() != newCharSetPair.length())
			return 1;

		for (int i = 0; i < newCharSetPair.length(); i++) {
			int targetIndex = charSet.indexOf(newCharSetPair.charAt(i));

			if (targetIndex == -1)
				return 2;

			if (newCharSetPair.charAt(targetIndex) != charSet.charAt(i))
				return 3;
		}

		// Set

		charSetPair = newCharSetPair;

		return 0;
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
			return charSetPair.charAt(charSetIndex);
		} else {
			return 0;
		}
	}
}
