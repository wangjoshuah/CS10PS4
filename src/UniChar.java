/**
 * The class that holds the data for each node of the Character Tree. It has a key variable that is the character and a value variable that is the frequency of the character
 * @author Josh Wang '15
 */

class UniChar implements Comparable<UniChar>{
	//set up our instance variables
	private char keyCharacter;
	private int frequency;

	/**
	 * Constructors
	 */
	
	/**
	 * Constructor for leaf
	 * @param key		the special character we are looking at
	 * @param value		the frequency of the charaacter
	 */
	public UniChar(char key, int value) {
		keyCharacter = key; //assign the key value
		frequency = value; //assign our frequency
	}
	
	/**
	 * Constructor for a branch given total frequency
	 * @param value 	total frequency for the tree
	 */
	public UniChar(int value) {
		frequency = value; //assign our frequency
	}
	
	/**
	 * Constructor for a branch given individual frequencies of its left and right child
	 * @param value1	left child's frequency
	 * @param value2	right child's frequency
	 */
	public UniChar(int value1, int value2) {
		frequency = value1 + value2; //assign our frequency as the sum of children
	}
	
	/**
	 * Getter Methods for our UniChar
	 */
	
	/**
	 * getter method for the key character
	 * @return the character we are encoding
	 */
	public char getKeyCharacter() {
		return keyCharacter;
	}
	
	/**
	 * getter for our frequency
	 * @return the total frequency
	 */
	public int getFrequency() {
		return frequency;
	}
	

	@Override
	public int compareTo(UniChar o) {
		// TODO Auto-generated method stub
		return (int) Math.signum(frequency - o.frequency);
	}
	
	
	
}
