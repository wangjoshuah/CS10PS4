/**
 * 
 * Character tree that defines individual characters like a Binary Tree of nodes
 * 
 * @author Josh Wang '15
 */
class CharacterTree extends BinaryTree<UniChar> {

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
	
}
