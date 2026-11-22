import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;

public class HashTableMap<KeyType, ValueType> implements MapADT {

    protected LinkedList<Pair>[] table = null;

    @SuppressWarnings("unchecked")
    public HashTableMap(int capacity) {
        if(capacity < 1) {
            throw new IllegalArgumentException("small small capacity i fear...");
        }
        table = new LinkedList[capacity];
    }

    public HashTableMap() {
        this(8);
    }

    @Override
    public void put(Object key, Object value) throws IllegalArgumentException {
    }

    @Override
    public boolean containsKey(Object key) {
        return false;
    }

    @Override
    public Object get(Object key) throws NoSuchElementException {
        return null;
    }

    @Override
    public Object remove(Object key) throws NoSuchElementException {
        return null;
    }

    @Override
    public void clear() {
    }

    @Override
    public int getSize() {
        return 0;
    }

    @Override
    public int getCapacity() {
        return 0;
    }

    @Override
    public List getKeys() {
        return null;
    }

    protected class Pair {

    public KeyType key;
    public ValueType value;

    public Pair(KeyType key, ValueType value) {
        this.key = key;
        this.value = value;
    }

}

    
}
