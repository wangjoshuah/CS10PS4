import java.util.*;

/**
 * 
 * Character tree that defines individual characters like a Binary Tree of nodes
 * 
 * @author Josh Wang '15
 */
class CharacterTree extends BinaryTree<UniChar> implements Comparable<CharacterTree>{

	/**
	 * Constructs a leaf
	 * @param data	the leaf node's data (char and frequency)
	 */
	public CharacterTree(UniChar data) {
		super(data);
	}
	
	/**
	 * Constructs a tree from two lower trees
	 * @param data		total frequency
	 * @param left		left tree
	 * @param right		right tree
	 */
	public CharacterTree(UniChar data, CharacterTree left, CharacterTree right) {
		super(data, left, right);
	}
	
	@Override
	public int compareTo(CharacterTree o) {
		// TODO Auto-generated method stub
		return (int) Math.signum(data.getFrequency() - o.data.getFrequency());
	}
	
	public CharacterTree getLeft() { //do this so we won't have to type cast
		return (CharacterTree) super.getLeft();
	}

	public CharacterTree getRight() {
		return (CharacterTree) super.getRight();
	}
	
}
