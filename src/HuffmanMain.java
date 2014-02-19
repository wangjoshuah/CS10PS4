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

	/**
	 * Creates a priority queue filled with singleton leaf trees of the data from the frequency table we receive
	 * @param table		frequency table mapping character values to their frequencies
	 * @return
	 */
	public PriorityQueue<CharacterTree> singletonsToPriorityQueue(Map<Character, Integer> table) {
		PriorityQueue<CharacterTree> singletonQueue = new PriorityQueue<CharacterTree>(); //create a priority queue to hold all the singleton trees
		Set<Character> charSet = table.keySet(); //get the set of values for the frequency table
		for (Character letter : charSet) { //for each key in the map,
			UniChar data = new UniChar(letter.charValue(), table.get(letter).intValue()); //create data with the letter value and frequency
			CharacterTree singletonTree = new CharacterTree(data); //create a leaf node to hold that data
			singletonQueue.add(singletonTree); //add the leaf node to the priority queue
		}
		return singletonQueue; //return our priority queue
	}
	
	/**
	 * Returns a fully constructed tree that will be our character binary tree mapping to binary bits
	 * @param queueOfTrees 		priority queue of singleton trees with data
	 * @return
	 */
	public CharacterTree createTree(PriorityQueue<CharacterTree> queueOfTrees) {
		while (queueOfTrees.size() > 1) { //while we have multiple trees in our priority queue
			CharacterTree lowestTree = queueOfTrees.remove(); //take the tree with the lowest frequency from the queue
			CharacterTree secondLowestTree = queueOfTrees.remove(); //also take the second lowest tree from the queue
			UniChar parentData = new UniChar(lowestTree.data.getFrequency(), secondLowestTree.data.getFrequency()); //create new data--the total frequency from the two lower nodes' data
			CharacterTree parentTree = new CharacterTree(parentData, lowestTree, secondLowestTree); //create a parent tree holding the two lower trees
			queueOfTrees.add(parentTree); //add the parent back into the priority queue to keep creating this tree
		}
		return queueOfTrees.remove(); //return the last tree remaining
	}
	
	/**
	 * gets a map of the bitstring values for each character in the code tree
	 * @param codeTree		the binary tree with unique paths to each character
	 * @return
	 */
	public Map<Character, String> codeRetreival(CharacterTree codeTree) {
		for (int i = 0, i < codeTree.)
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
