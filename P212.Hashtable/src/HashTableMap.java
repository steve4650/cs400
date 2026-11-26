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
    this.table = new LinkedList[capacity];
  }

  public HashTableMap() {
    this(8);
  }

  @Override
  public void put(KeyType key, ValueType value) throws IllegalArgumentException {
    int index = Math.abs(key.hashCode()) % this.capacity;
    if(table[index] == null) {
      table[index] = new LinkedList<Pair>();
    }
    table[index].add(new Pair((KeyType) key, (ValueType) value));
  }

  @Override
  public boolean containsKey(KeyType key) {
    int index = Math.abs(key.hashCode()) % this.capacity;
    return table[index] != null;
  }

  @Override
  public ValueType get(KeyType key) throws NoSuchElementException {
    int index = Math.abs(key.hashCode()) % this.capacity;
    if(table[index]== null) {
      throw new NoSuchElementException();
    }
    for(Pair pair: table[index]) {
      if(pair.key.equals(key)) {
        return pair.value;
      }
    }
  }

  @Override
  public ValueType remove(KeyType key) throws NoSuchElementException {
    return null;
  }

  @SuppressWarnings("unchecked")
  @Override
  public void clear() {
    this.table = new LinkedList[this.capacity];
  }

  @Override
  public int getSize() {
    return 0;
  }

  @Override
  public int getCapacity() {
    return this.capacity;
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
