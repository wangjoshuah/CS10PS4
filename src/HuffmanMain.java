import java.io.IOException;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Set;
import java.util.TreeMap;

public class HuffmanMain {
	public Map<Character, Integer> FrequencyTable(String text) {
		Map<Character, Integer> ftable = new TreeMap<Character, Integer>(); //Create new Map that has a key as a character and an integer representing the count a character appears as its value
		for (int i = 0; i < text.length(); i++) { //loop over each character in the string
			char getChar = text.charAt(i); //get the character at a given i
			if (ftable.containsKey(getChar)) { //if the frequency table contains a given character already
				int temp = ftable.get(getChar) + 1; //create a new variable equal to that character's value plus one
				ftable.put(getChar, temp); //replace that given character with a value that is one greater than before
			} else { //if the frequency table does not contain a given character
				ftable.put(getChar, 1); //put that character into the map with a frequency of one
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
		CharacterTree pathHolder = codeTree; //create a path holder to remember where we are in the tree
		String pathBitString = new String(); //have a string that keeps our path in terms of bits
		Map<Character, String> codeMap = new TreeMap<Character, String>(); //instantiate the map we will return
		while (!pathHolder.isLeaf()) { //while our current path still has children, 
			if (pathHolder.getRight().isLeaf()) { //if our pathHolder's right child is a leaf,
				codeMap.put(pathHolder.getRight().data.getKeyCharacter(), pathBitString + "1"); //add the right child's keycharacter and the current path concatenated with "1" since this child is on the right
				pathHolder = (CharacterTree) pathHolder.getLeft(); //direct the path we keep going on to the left
				pathBitString += "0"; //add "0" to our path bit string since we are going to the left for our next iteration of this loop
			}
			else { //our path continues down the right so the left one is the charcter and the right is more path
				codeMap.put(pathHolder.getLeft().data.getKeyCharacter(), pathBitString + "0"); //add the left child and the path + "0" (do everything on the other side)
				pathHolder = (CharacterTree) pathHolder.getRight(); //direct the path to the right since the right is not a leaf
				pathBitString += "1"; //add 1 to the path bit string since we are going to the right
			}
		}
		return codeMap; //return our map
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
