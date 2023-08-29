public class MyLL<T> {
	private class Node<T> {
		public Node(T val) { data = val; }
		public Node(T val, Node<T> next) { data = val; this.next = next;}
		public T data;
		public Node<T> next;
	}
	private Node<T> head;
	private Node<T>[] listArray;
	MyLL() {
		listArray = new Node[0];
	}

	public void Insert(T previous, T value) throws Exception{
		Node<T>[] otherList = new Node[listArray.length + 1];
		int index = 0;
		for(int i = 0; i <= getIndexOfHead(); i++)
			if(listArray[i].data.equals(previous))
				index = i;
		for(int i = 0; i <= index; i++)
			otherList[i] = listArray[i];
		int indOfHead = getIndexOfHead();
		for(int i = indOfHead; i > index; i--) {
			otherList[i + 1] = listArray[i];
		}
		listArray = otherList;
		listArray[index + 1] = new Node(value);
		head = listArray[indOfHead + 1];
		// insert a new node with value "value" after the node with value "previous". 
	}
	public void Append(T value) {
		if(listArray.length == 0) {
			listArray = new Node[5];
			head = new Node(value);
			listArray[0] = head;
		}
		else if((getIndexOfHead() < listArray.length - 1)) {
			listArray[getIndexOfHead() + 1] = new Node(value);
			head = listArray[getIndexOfHead() + 1];
		}
		else {
			Node<T>[] otherList = new Node[listArray.length + 5];
			for(int i = 0; i < listArray.length; i++)
				otherList[i] = listArray[i];
			listArray = otherList;
			listArray[getIndexOfHead() + 1] = new Node(value);
			head = listArray[getIndexOfHead() + 1];
		}
		// add a new node with value "value" at the end of the list. Head is a special case...
	}

	public boolean Find(T value) {
		for(int i = 0; i <= getIndexOfHead(); i++) {
			if(listArray[i].data.equals(value))
				return true;
		}
		return false;
		// return true if "value" is in the list, false if not
	}

	public boolean Remove(T value) {
		if(Find(value) == false)
			return false;
		int index = 0;
		Node<T> temporary;
		for(int i = 0; i < getIndexOfHead(); i++)
			if(listArray[i].data.equals(value))
				index = i;
		if(value.equals(listArray[getIndexOfHead()].data)) {
			head = listArray[getIndexOfHead() - 1];
			listArray[getIndexOfHead() + 1] = null;
		}
		for(int i = index; i <= getIndexOfHead(); i++) {
			temporary = listArray[i];
			listArray[i] = listArray[i + 1];
			listArray[i + 1] = temporary;
		}
		return true;
		// remove "value" from the list. return true if it was found and removed, false otherwise
	}
	public int getIndexOfHead() {
		for(int i = 0; i < listArray.length; i++) {
			if(listArray[i].data.equals(head.data))
				return i;
		}
		return -1;
	}
}