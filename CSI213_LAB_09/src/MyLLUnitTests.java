import static org.junit.Assert.*;

import org.junit.Assert;
import org.junit.Test;

public class MyLLUnitTests {
    @Test
    public void findWithEmptyList() throws Exception {
        var ll = new MyLL<String>();
        Assert.assertFalse(ll.Find("hello"));
    }

    @Test
    public void findWithNonEmptyList() throws Exception {
        var ll = new MyLL<String>();
        ll.Append("goodbye");
        Assert.assertFalse(ll.Find("hello"));
    }

    @Test
    public void findWithOneItemList() throws Exception {
        var ll = new MyLL<String>();
        ll.Append("hello");
        Assert.assertTrue(ll.Find("hello"));
    }

    @Test
    public void findWithManyItemList() throws Exception {
        var ll = new MyLL<String>();
        ll.Append("hello");
        ll.Append("there");
        ll.Append("General");
        ll.Append("Kenobi");
        Assert.assertTrue(ll.Find("hello"));
        Assert.assertTrue(ll.Find("Kenobi"));
        Assert.assertFalse(ll.Find("Anakin"));
    }

    @Test
    public void findThenRemoveThenAdd() throws Exception {
        var ll = new MyLL<String>();
        ll.Append("hello");
        ll.Append("there");
        ll.Append("General");
        ll.Append("Kenobi");
        Assert.assertFalse(ll.Find("Anakin"));
        ll.Remove("Kenobi");
        Assert.assertFalse(ll.Find("Kenobi"));
        ll.Insert("General","Anakin");
        Assert.assertTrue(ll.Find("Anakin"));
    }

}
