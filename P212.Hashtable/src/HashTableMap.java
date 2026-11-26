import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;

public class HashTableMap<KeyType, ValueType> implements MapADT<KeyType, ValueType> {

  protected LinkedList<Pair>[] table = null;
  protected int capacity = 8;
  protected int size = 0;

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
    this.size++;
    if (this.size / this.capacity > 0.75) {
      rehash();
    }
    int index = Math.abs(key.hashCode()) % this.capacity;
    if (table[index] == null) {
      table[index] = new LinkedList<Pair>();
    }
    table[index].add(new Pair((KeyType) key, (ValueType) value));
  }

  @Override
  public boolean containsKey(KeyType key) {
    try {
      this.get(key);
      return true;
    } catch (NoSuchElementException e) {
      return false;
    }
  }

  @Override
  public ValueType get(KeyType key) throws NoSuchElementException {
    int index = Math.abs(key.hashCode()) % this.capacity;
    if (table[index] == null) {
      throw new NoSuchElementException();
    }
    for (Pair pair : table[index]) {
      if (pair.key.equals(key)) {
        return pair.value;
      }
    }
    throw new NoSuchElementException();
  }

  @Override
  public ValueType remove(KeyType key) throws NoSuchElementException {
    int index = Math.abs(key.hashCode()) % this.capacity;
    if (table[index] == null) {
      throw new NoSuchElementException();
    }
    int i = 0;
    for (Pair pair : table[index]) {
      if (pair.key.equals(key)) {
        table[index].remove(i);
      }
      i++;
    }
    throw new NoSuchElementException();
  }

  @SuppressWarnings("unchecked")
  @Override
  public void clear() {
    this.table = new LinkedList[this.capacity];
  }

  @Override
  public int getSize() {
    return this.size;
  }

  @Override
  public int getCapacity() {
    return this.capacity;
  }

  @Override
  public List<KeyType> getKeys() {
    List<KeyType> result = new LinkedList<KeyType>();
    for (int i = 0; i < this.table.length; i++) {
      if (table[i] != null) {
        for (Pair pair : table[i]) {
          result.add(pair.key);
        }
      }
    }
    return result;
  }

  private void rehash() {
    this.capacity *= 2;
    LinkedList<Pair>[] old_table = this.table;
    this.clear();
    for (int i = 0; i < old_table.length; i++) {
      if (old_table[i] != null) {
        for (Pair pair : old_table[i]) {
          this.put(pair.key, pair.value);
        }
      }
    }
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
