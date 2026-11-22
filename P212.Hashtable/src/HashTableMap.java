import java.util.List;
import java.util.NoSuchElementException;

public class HashTableMap implements MapADT {

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
    
}
