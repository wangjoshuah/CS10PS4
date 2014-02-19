import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Set;
import java.util.TreeMap;

import javax.sound.sampled.Line;
import javax.swing.JFileChooser;

public class HuffmanMain {
	private Map<Character, String> characterMap; //keep our character map
	private CharacterTree codeTree;
	public String pathName;

	public HuffmanMain(String pathName) {
		characterMap = new TreeMap<Character, String>();
		this.pathName = pathName;
	}

	private Map<Character, Integer> frequencyTable() throws Exception {
		BufferedReader input = new BufferedReader(new FileReader(this.pathName));  //create a buffered reader to take in the file
		Map<Character, Integer> ftable = new TreeMap<Character, Integer>(); //Create new Map that has a key as a character and an integer representing the count a character appears as its value
		int nextChar; //have an int hold the current character
		while ((nextChar = input.read()) != -1) { //will stop reading if the char is -1
			if (ftable.containsKey((char)nextChar)) { //if the frequency table contains a given character already
				int temp = ftable.get((char)nextChar) + 1; //create a new variable equal to that character's value plus one
				ftable.put((char)nextChar, temp); //replace that given character with a value that is one greater than before
			} else { //if the frequency table does not contain a given character
				ftable.put((char)nextChar, 1); //put that character into the map with a frequency of one
			}		
		}
		input.close(); //end reading
		return ftable;
	}

	/**
	 * Creates a priority queue filled with singleton leaf trees of the data from the frequency table we receive
	 * @param table		frequency table mapping character values to their frequencies
	 * @return
	 */
	private PriorityQueue<CharacterTree> singletonsToPriorityQueue(Map<Character, Integer> table) {
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
	private CharacterTree createTree(PriorityQueue<CharacterTree> queueOfTrees) {
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
	private Map<Character, String> codeRetreivalRecurs(CharacterTree codeTree, String bitString, Map<Character, String> codeMap) { //recurses through it
		if (codeTree.isLeaf()) { //if the node is a leaf
			codeMap.put(codeTree.data.getKeyCharacter(), bitString); //add its data to the map
		}
		else { //if the node is not a leaf,
			codeMap = codeRetreivalRecurs( codeTree.getLeft(), bitString + "0", codeMap); //recurs on its left child
			codeMap = codeRetreivalRecurs( codeTree.getRight(), bitString + "1", codeMap); //recurse on its right child
		}
		return codeMap; //return the code map
	}

	public void compress() throws Exception { //just let us work on this file please
		
		codeTree = createTree(singletonsToPriorityQueue(frequencyTable())); //get our code tree
		characterMap = codeRetreivalRecurs(codeTree, "", characterMap); //call our methods to get a frequency table then make a priority queue and then create a tree of those and then get the map code of that

		BufferedReader input = new BufferedReader(new FileReader(this.pathName));  //create a buffered reader to take in the file
		BufferedBitWriter bitOutput = new BufferedBitWriter(this.pathName + "compressed.txt"); //create a new bitwrite to write a file here...
		
		int nextChar = 0; //hold the next character from the reader
		System.out.println("enter while loop");
		while ((nextChar = input.read()) != -1) { //will stop reading if the char is -1
			String bitString = characterMap.get((char) nextChar); //get the bitstring of the character we are at
			for (int j = 0; j < bitString.length(); j ++) { //for the entire bitstring
				if (bitString.charAt(j) == '0') { //if the character is a 0,
					bitOutput.writeBit(0); //write a 0 there
				}
				else { //if the character was a 1
					bitOutput.writeBit(1); //write a 1 there
				}
			}
		}
		System.out.println("exit while loop");
		input.close(); //close the file cuz we have what we need		
		bitOutput.close(); //end writing
	}

	public void decompress() throws Exception {
		BufferedBitReader bitInput = new BufferedBitReader(this.pathName + "compressed.txt"); //created bit reader to read each bit of the compressed file
		int bit = 0; //create a local variable to store the bit
		CharacterTree node = codeTree;
		BufferedWriter output = new BufferedWriter(new FileWriter(this.pathName + "decompressed.txt")); 
		while (bit != -1) { //stop reading when the bit is returned is -1 aka end
			bit = bitInput.readBit(); 
			if (node.isLeaf()) { //if our node is a leaf
				output.write(node.getData().getKeyCharacter()); //write out that character
				node = codeTree; //reset our node to go back to the top
			}
			if (bit == 1) { //if bit is 1 we go to the right
				node = node.getRight(); //go to the right
			}
			else { //we go to the left
				node = node.getLeft(); //push our node to the left
			}
		}
		bitInput.close(); //end reading bits
		output.close(); //end writing
	}

	/**
	 * Puts up a fileChooser and gets path name for file to be opened.
	 * Returns an empty string if the user clicks "cancel".
	 * @return path name of the file chosen	
	 */
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

	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		//		compress("inputs/USConstitution.txt");
		//		decompress("inputs/USConstitution.txtcompressed");
		//		
		//		compress("inputs/testcase.txt");
		//		decompress("inputs/testcase.txtcompressed");
		//
		////		compress("inputs/WarAndPeace.txt");
		////		decompress("inputs/WarAndPeace.txtcompressed");

		String filePath = getFilePath();
		HuffmanMain huffConvert = new HuffmanMain(filePath);
		huffConvert.compress();
		huffConvert.decompress();

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
