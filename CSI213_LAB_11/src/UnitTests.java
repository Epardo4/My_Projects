import org.junit.Assert;
import org.junit.Test;

public class UnitTests {

    @Test
    public void TestEmpty() {
        var t = new BinaryTree<String>();
        Assert.assertEquals(t.toString(),"Empty Tree");
    }

    @Test
    public void TestAdd() {
        var t = new BinaryTree<String>();
        t.add("hello");
        t.add("there");
        t.add("general");
        t.add("kenobi");
        Assert.assertEquals("general, hello, kenobi, there, ", t.toString());
    }

    @Test
    public void TestFind() {
        var t = new BinaryTree<String>();
        t.add("hello");
        t.add("there");
        t.add("general");
        t.add("kenobi");
        Assert.assertTrue(t.find("kenobi"));
        Assert.assertFalse(t.find("anakin"));
    }
}