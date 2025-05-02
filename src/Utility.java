import java.util.ArrayList;
import java.util.List;

/**
 * 汎用機能
 */
public class Utility {
	private Utility() {
		// インスタンス化を拒否
	}

	/**
	 * 整数値を指定の周期へ収納します。
	 * 
	 * @param n 収納する整数値。
	 * 値の正負を問いません。
	 * @param cycle 周期。
	 * 0の場合は例外になります。
	 * @return 収納された整数値。
	 */
	public static int getCycleInt(int n, int cycle) {
		int res = n % cycle;

		return res >= 0 ? res : cycle + res;
	}

	/**
	 * 文字列に重複した（2つ以上の）文字が存在しないか確認します。
	 * 
	 * @param str 確認する文字列。
	 * NULLは空の文字列として扱われます。
	 * @return 重複していなければ真、重複していれば偽。
	 */
	public static boolean isNonDuplicateString(String str) {
		if (str == null)
			return true;

		for (int i = 0; i < str.length() - 1; i++)
			for (int j = i + 1; j < str.length(); j++)
				if (str.charAt(i) == str.charAt(j))
					return false;

		return true;
	}

	/**
	 * 空白を区切り文字として文字列を配列へ評価します。
	 * クォーテーションで囲まれた文字列を一つの要素として扱います。
	 * 一部のエスケープされた文字をアンエスケープします。
	 * 
	 * @param str 評価する文字列。
	 * NULLの場合はNULLが返されます。
	 * 空の場合は空の配列が返されます。
	 * クォーテーションの終点が存在しない場合は始点から文末までを一つの要素として扱います。
	 * 不正なエスケープシーケンスはエスケープ文字（\）が取り除かれます。
	 * @return 評価された文字列配列。
	 */
	public static String[] evalArguments(String str) {
		if (str == null)
			return null;

		if (str.length() <= 0)
			return new String[0];

		List<String> res = new ArrayList<String>();
		StringBuffer buf = new StringBuffer();

		int targetSymbolIndex = -1;

		for (int i = 0; i < str.length(); i++) {
			char currentChar = str.charAt(i);
			int currentSymbolIndex = "\"'`".indexOf(currentChar);

			if (currentChar == '\\') {
				if (i + 3 < str.length() && str.substring(i + 1, i + 4).matches("[0-7]{3}")) {
					buf.append((char) Integer.parseInt(str.substring(i + 1, i + 4), 8));

					i += 3;
				} else if (i + 5 < str.length() && str.substring(i + 1, i + 6).matches("u[0-f]{4}")) {
					buf.append((char) Integer.parseInt(str.substring(i + 2, i + 6), 16));

					i += 5;
				} else if (i + 1 < str.length()) {
					switch (str.charAt(i + 1)) {
					case 'b' -> buf.append('\b');
					case 'f' -> buf.append('\f');
					case 'n' -> buf.append('\n');
					case 'r' -> buf.append('\r');
					case 's' -> buf.append(' ');
					case 't' -> buf.append('\t');
					case '"' -> buf.append('"');
					case '\'' -> buf.append('\'');
					case '\\' -> buf.append('\\');
					default -> i -= 1;
					}

					i += 1;
				}
			} else if (targetSymbolIndex == -1 && currentSymbolIndex != -1) {
				targetSymbolIndex = currentSymbolIndex;
			} else if (targetSymbolIndex != -1 && currentSymbolIndex == targetSymbolIndex) {
				targetSymbolIndex = -1;

				res.add(buf.toString());
				buf.setLength(0);
			} else if (targetSymbolIndex == -1 && currentChar == ' ') {
				if (buf.length() != 0) {
					res.add(buf.toString());
					buf.setLength(0);
				}
			} else {
				buf.append(currentChar);
			}
		}

		if (buf.length() != 0)
			res.add(buf.toString());

		return res.toArray(new String[res.size()]);
	}
}
