import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;

public class HashTableMap<KeyType, ValueType> implements MapADT {

  protected LinkedList<Pair>[] table = null;
  protected int capacity  = 8;

  @SuppressWarnings("unchecked")
  public HashTableMap(int capacity) {
    if (capacity < 1) {
      throw new IllegalArgumentException("small small capacity i fear...");
    }
    this.capacity = capacity;
    table = new LinkedList[capacity];
  }

  public HashTableMap() {
    this(8);
  }

  @Override
  public void put(Object key, Object value) throws IllegalArgumentException {
    int index = key.hashCode() % this.capacity;
    if(table[index] == null) {
      table[index] = new LinkedList<Pair>();
    }
    table[index].add(new Pair((KeyType) key, (ValueType) value));
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
  public void clear() {}

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
