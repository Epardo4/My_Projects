import org.junit.Assert;

public class Practice {
	public static void main(String []  args) {
		var t = new BinaryTree<String>();
        t.add("hello");
        System.out.println(t.toString());
        t.add("there");
        System.out.println(t.toString());
        t.add("general");
        System.out.println(t.toString());
        t.add("kenobi");
        System.out.println(t.toString());
	}
}
