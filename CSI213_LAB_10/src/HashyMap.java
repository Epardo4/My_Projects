import java.util.LinkedList;

public class HashyMap<K extends Comparable<K>,V extends Comparable<V>>  {
    public class Entry {
        public Entry(K k, V v) {
            key = k;
            value = v;
        }
        public K key;
        public V value;
    }

    LinkedList<Entry>[] data; // an array of linked lists of Entries

    public HashyMap () { // use a default size of 11
    	data = new LinkedList[11];
    	for(int i = 0; i < data.length; i++)
    		data[i] = new LinkedList<Entry>();
    }

    public HashyMap (int size) { // make data equal to size
    	data = new LinkedList[size];
    	for(int i = 0; i < data.length; i++)
    		data[i] = new LinkedList<Entry>();
    }

    private int hash(K key) { // calculate the hash value using hashCode
    	int answer = key.hashCode();
    	return Math.abs(answer);
    }

    @SuppressWarnings("unchecked")
    public int size() { // get the number of elements in the hash map
    	int count = 0;
    	for(LinkedList list : data)
    		if(list.size() > 0) count += list.size();
    	return count;
    }

    @SuppressWarnings("unchecked")
    public boolean isEmpty() { // are there any entries in the hash map?
    	for(int i = 0; i < data.length; i++)
    		if(!data[i].isEmpty()) return false;
    	return true;
    }

    public boolean containsKey(K key) { // does this key exist?
    	int hash = hash(key) % data.length;
    	if(data[hash].isEmpty())
    		return false;
    	if(!data[hash].get(0).key.equals(key))
    		return false;
    	return true;
    }

    @SuppressWarnings("unchecked")
    public boolean containsValue(V value) { // does this value exist? This is SLOW.
    	for(int i = 0; i < data.length; i++)
    		if(data[i] != null)
    			for(int j = 0; j < data[i].size(); j++)
    				if(data[i].get(j) != null && data[i].get(j).value.equals(value))
    					return true;
    	return false;
    }

    @SuppressWarnings("unchecked")
    public V get(K key) { // get the value for this key
    	int hash = hash(key) % data.length;
    	return data[hash].get(0).value;
    }

    @SuppressWarnings("unchecked")
    public void put(K key, V value) { // create an entry and put it in the hashymap. Don't worry about duplicates
    	Entry input = new Entry(key, value);
    	int hash = hash(input.key) % data.length;
    	data[hash].add(input);
    	
    }

    @SuppressWarnings("unchecked")
    public V remove(K key) { // remove the entry matching this key
    	int hash = hash(key) % data.length;
    	V toRemove = data[hash].get(0).value;
    	data[hash].clear();
    	return toRemove;
    }

    @SuppressWarnings("unchecked")
    public void clear() { // empty the whole hashymap
    	for(int i = 0; i < data.length; i++)
    		data[i].clear();
    }

    @SuppressWarnings("unchecked")
    public LinkedList<K> keys() { // get all of the keys; make a new linked list
    	LinkedList<K> keys = new LinkedList<K>();
    	for(int i = 0; i < data.length; i++)
    		if(!data[i].isEmpty())
    			keys.add(data[i].get(0).key);
    	return keys;
    }

    @SuppressWarnings("unchecked")
    public LinkedList<V> values() { // get all of the values; make a new linked list
    	LinkedList<V> values = new LinkedList<V>();
    	for(int i = 0; i < data.length; i++)
    		if(!data[i].isEmpty())
    			values.add(data[i].get(0).value);
    	return values;
    }

    @SuppressWarnings("unchecked")
    public LinkedList<Entry> entries() { // get all of the entries; make a new linked list
    	LinkedList<Entry> entries = new LinkedList<Entry>();
    	for(int i = 0; i < data.length; i++)
    		if(!data[i].isEmpty())
    			entries.add(data[i].get(0));
    	return entries;
    }
}