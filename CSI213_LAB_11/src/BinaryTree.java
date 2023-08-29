public class BinaryTree<T extends Comparable<T>> {
	private Node root;
    public class Node {
	// Add members here
    	public T data;
    	public Node left;
    	public Node right;
        Node(T newOne) { // set the value of the node here
        	data = newOne;
        }

	// in-order traversal : 
	// Concatenate: 
	// 	call toString on the left branch, if there is one.
	// 	call toString on "this".
	// 	the literal string ", "
	// 	call toString on the right branch if there is one.
	// return that concatenated string
        @Override
        public String toString() { 
        	String info = "";
        	if(left != null)
        		info += left.toString();
        	info += this.data + ", ";
        	if(right != null)
        		info += right.toString();
        	return info;
        }
    }
    
    // make a new node. Set it to root if root is null
    // otherwise traverse the tree (using .compareTo()) looking for the right place to add
    // (that is, a null branch that doesn't violate the rules of a binary tree)
    public void add(T item) {
    	Node toAdd = new Node(item);
    	if(root == null) {
    		root = toAdd;
    		return;
    	}
    	Node check = root;
    	while(true) {
    		if(check.data.compareTo(item) > 0)
    			if(check.left == null) {
    				check.left = toAdd;
    				return;
    			}
    			else
    				check = check.left;
    		else 
    			if(check.right == null) {
    				check.right = toAdd;
    				return;
    			}
    			else
    				check = check.right;
    	}
    }

    // traverse the tree looking for the item (using compareTo()). 
    // Return true if you find it.
    public boolean find(T item) {
    	Node check = root;
    	while(check != null) {
    		if(check.data.compareTo(item) < 0)
    			check = check.right;
    		else if(check.data.compareTo(item) > 0)
    			check = check.left;
    		else
    			return true;
    	}
    	return false;
    }

    // This is correct as is.
    @Override
    public String toString() {
        if (root == null) return "Empty Tree";
        return root.toString();
    }
}