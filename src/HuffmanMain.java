import java.io.IOException;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Set;
import java.util.TreeMap;

public class HuffmanMain {

	public Map<Character, Integer> FrequencyTable(String text) {

		Map<Character, Integer> ftable = new TreeMap<Character, Integer>();

		for (int i = 0; i < text.length(); i++) {
			char getChar = text.charAt(i);
			if (ftable.containsKey(getChar)) {
				int temp = ftable.get(getChar) + 1;
				ftable.put(getChar, temp);
			} else {
				ftable.put(getChar, 1);
			}
		}

		return ftable;
	}

	public PriorityQueue<CharacterTree> singletonsToPriorityQueue(Map<Character, Integer> table) {
		PriorityQueue<CharacterTree> singletonQueue = new PriorityQueue<CharacterTree>();
		Set<Character> charSet = table.keySet();
		for (Character letter : charSet) {
			UniChar data = new UniChar(letter.charValue(), table.get(letter).intValue());
			CharacterTree singletonTree = new CharacterTree(data);
			singletonQueue.add(singletonTree);
		}
		return singletonQueue;
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
