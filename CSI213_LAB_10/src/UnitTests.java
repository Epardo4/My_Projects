
import org.junit.Assert;
import org.junit.Test;

public class UnitTests {
    private HashyMap<String,String> getBen() {
        var hm = new HashyMap<String,String>();
        hm.put("hello","there");
        hm.put("general","Kenobi");
        return hm;
    }
    
    @Test
    public void testContainsKey() {
        var hm = getBen();
        Assert.assertTrue(hm.containsKey("hello"));
        Assert.assertTrue(hm.containsKey("general"));
        Assert.assertFalse(hm.containsKey("there"));
        Assert.assertFalse(hm.containsKey("Kenobi"));
    }

    @Test
    public void testRemove() {
        var hm = getBen();
        Assert.assertTrue(hm.containsKey("hello"));
        Assert.assertTrue(hm.containsKey("general"));
        var e = hm.remove("general");
        Assert.assertEquals(e,"Kenobi");
        Assert.assertFalse(hm.containsKey("general"));
        Assert.assertFalse(hm.containsValue("Kenobi"));
    }

    @Test
    public void testSize() {
        var hm = getBen();
        Assert.assertEquals(hm.size(), 2);
    }

    @Test
    public void testEmpty() {
        var hm = getBen();
        Assert.assertFalse(hm.isEmpty());
        hm.remove("hello");
        Assert.assertFalse(hm.isEmpty());
        hm.remove("general");
        Assert.assertTrue(hm.isEmpty());
    }

    @Test
    public void testClear() {
        var hm = getBen();
        hm.clear();
        Assert.assertTrue(hm.isEmpty());
        Assert.assertFalse(hm.containsKey("hello"));
        Assert.assertFalse(hm.containsValue("there"));
        Assert.assertFalse(hm.containsKey("general"));
        Assert.assertFalse(hm.containsValue("Kenobi"));
    }

    @Test
    public void testGet() {
        var hm = getBen();
        Assert.assertEquals(hm.get("hello"),"there");
        Assert.assertEquals(hm.get("general"),"Kenobi");
    }

    @Test
    public void testKeys() {
        var hm = getBen();
        var keys = hm.keys();
        Assert.assertTrue(keys.contains("hello"));
        Assert.assertTrue(keys.contains("general"));
    }

    @Test
    public void testValues() {
        var hm = getBen();
        var keys = hm.values();
        Assert.assertTrue(keys.contains("there"));
        Assert.assertTrue(keys.contains("Kenobi"));
    }

    @Test
    public void testEntries() {
        var hm = getBen();
        var entries = hm.entries();
        if (entries.get(0).key.equals("hello")) {
            Assert.assertEquals("there", entries.get(0).value);
            Assert.assertEquals("general", entries.get(1).key);
            Assert.assertEquals("Kenobi", entries.get(1).value);
        }
        else {
            Assert.assertEquals("general", entries.get(0).key);
            Assert.assertEquals("Kenobi", entries.get(0).value);
            Assert.assertEquals("there", entries.get(1).value);

        }
    }
}