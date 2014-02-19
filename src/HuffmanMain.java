import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Set;
import java.util.TreeMap;

import javax.sound.sampled.Line;
import javax.swing.JFileChooser;

public class HuffmanMain {

	private static Map<Character, Integer> frequencyTable(String text) {
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
	private static PriorityQueue<CharacterTree> singletonsToPriorityQueue(Map<Character, Integer> table) {
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
	private static CharacterTree createTree(PriorityQueue<CharacterTree> queueOfTrees) {
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
	private static Map<Character, String> codeRetreival(CharacterTree codeTree) {
		CharacterTree pathHolder = codeTree; //create a path holder to remember where we are in the tree
		String pathBitString = new String(); //have a string that keeps our path in terms of bits
		Map<Character, String> codeMap = new TreeMap<Character, String>(); //instantiate the map we will return
		while (!pathHolder.isLeaf()) { //while our current path still has children, 
			if (pathHolder.getRight().isLeaf()) { //if our pathHolder's right child is a leaf,
				System.out.println(pathBitString + "1");
				codeMap.put(pathHolder.getRight().data.getKeyCharacter(), pathBitString + "1"); //add the right child's keycharacter and the current path concatenated with "1" since this child is on the right
				System.out.println(codeMap.get(pathHolder.getRight().data.getKeyCharacter()));
				pathHolder = (CharacterTree) pathHolder.getLeft(); //direct the path we keep going on to the left
				pathBitString += "0"; //add "0" to our path bit string since we are going to the left for our next iteration of this loop
			}
			else { //our path continues down the right so the left one is the charcter and the right is more path
				System.out.println(pathBitString + "0");
				codeMap.put(pathHolder.getLeft().data.getKeyCharacter(), pathBitString + "0"); //add the left child and the path + "0" (do everything on the other side)
				System.out.println(codeMap.get(pathHolder.getLeft().data.getKeyCharacter()));
				pathHolder = (CharacterTree) pathHolder.getRight(); //direct the path to the right since the right is not a leaf
				pathBitString += "1"; //add 1 to the path bit string since we are going to the right
			}
		}
		return codeMap; //return our map
	}

	private static Map<Character, String> codeRetreivalRecurs(CharacterTree codeTree, String bitString, Map<Character, String> codeMap) {
		if (codeTree.isLeaf()) {
			codeMap.put(codeTree.data.getKeyCharacter(), bitString);
			System.out.println("Character is " + codeTree.data.getKeyCharacter() + " bit string is " + bitString);
		}
		else {
			codeMap = codeRetreivalRecurs((CharacterTree) codeTree.getLeft(), bitString + "0", codeMap);
			codeMap = codeRetreivalRecurs((CharacterTree) codeTree.getRight(), bitString + "1", codeMap);
		}
		return codeMap;
	}
	
	public static String getFilePath() {
		JFileChooser fc = new JFileChooser("."); // start at current directory

		int returnVal = fc.showOpenDialog(null);
		if(returnVal == JFileChooser.APPROVE_OPTION) {
			File file = fc.getSelectedFile();
			String pathName = file.getAbsolutePath();
			return pathName;
		}
		else
			return "";
	}

	public static void compress(String filename) throws Exception{ //just let us work on this file please
		BufferedReader in = new BufferedReader(new FileReader(filename)); //create a buffered reader to take in the file
		String compressedFilename = filename.concat("compressed"); //add compressed for the compressed file's filename
		BufferedBitWriter bitOutput = new BufferedBitWriter(compressedFilename); //create a new bitwrite to write a file here...
		String fileText = new String(); //allocate a string to hold the file's text
		String line;
		while ((line = in.readLine()) != null) {
			fileText += line; //get the string representation of the file
		}
		System.out.println(fileText);
		in.close(); //close the file cuz we have what we need
		Map<Character, String> characterMap = new TreeMap<Character, String>(); 
		characterMap = codeRetreivalRecurs(createTree(singletonsToPriorityQueue(frequencyTable(fileText))), "", characterMap); //call our methods to get a frequency table then make a priority queue and then create a tree of those and then get the map code of that
		for (int i = 0; i < fileText.length(); i ++) { //for the entire filestring,
			String bitString = characterMap.get(fileText.charAt(i)); //get the bitstring of the character we are at
			for (int j = 0; j < bitString.length(); j ++) { //for the entire bitstring
				if (bitString.charAt(j) == '0') { //if the character is a 0,
					bitOutput.writeBit(0); //write a 0 there
				}
				else { //if the character was a 1
					bitOutput.writeBit(1); //write a 1 there
				}
			}
		}
	}

	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		compress("inputs/USConstitution.txt");


		/*
		 * read in the text to the frequency table
		 * get our map of bit strings
		 * read text again and write each character to bit string from map (using bitwriter)
		 * 	get the string from the map
		 * 	write each character using the bit writer
		 * 	check if char == '0' then write 0
		 * 		else if char == '1' then write literal 1
		 * 			bitwrite(1)
		 * 
		 * e
		 * look at map "1"
		 * is this equal '1'
		 * 	bitwrite write 1
		 */
	}

}
