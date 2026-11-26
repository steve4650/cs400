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
      throw new IllegalArgumentException("Capacity must be at least 1.");
    }
    this.capacity = capacity;
    this.size = 0;
    this.table = (LinkedList<Pair>[]) new LinkedList[capacity];
  }

  public HashTableMap() {
    this(8);
  }

  /*
   * Helper method to get the table index of a key
   * according to the hash formula
   */
  int getIndex(KeyType key) {
    return Math.abs(key.hashCode()) % this.capacity;
  }

  @Override
  public void put(KeyType key, ValueType value) throws IllegalArgumentException {
    if (key == null || this.containsKey(key)) {
      throw new IllegalArgumentException();
    }

    if ((double) (this.size + 1) / this.capacity >= 0.75) {
      rehash();
    }
    int index = getIndex(key);
    if (table[index] == null) {
      table[index] = new LinkedList<Pair>();
    }
    table[index].add(new Pair(key, value));
    this.size++;
  }

  @Override
  public boolean containsKey(KeyType key) {
    if (key == null) {
      throw new NullPointerException();
    }
    int index = getIndex(key);
    if (table[index] == null) {
      return false;
    }
    for (Pair pair : table[index]) {
      if (pair.key.equals(key)) {
        return true;
      }
    }
    return false;
  }

  @Override
  public ValueType get(KeyType key) throws NoSuchElementException {
    if (key == null) {
      throw new NullPointerException("Key cannot be null.");
    }
    int index = getIndex(key);
    if (table[index] == null) {
      throw new NoSuchElementException("Key not found.");
    }
    for (Pair pair : table[index]) {
      if (pair.key.equals(key)) {
        return pair.value;
      }
    }
    throw new NoSuchElementException("Key not found.");
  }

  @Override
  public ValueType remove(KeyType key) throws NoSuchElementException {
    if (key == null) {
      throw new NullPointerException();
    }
    int index = getIndex(key);
    if (table[index] == null) {
      throw new NoSuchElementException("Key not found.");
    }
    for (int i = 0; i < table[index].size(); i++) {
      Pair pair = table[index].get(i);
      if (pair.key.equals(key)) {
        table[index].remove(i);
        this.size--;
        return pair.value;
      }
    }
    throw new NoSuchElementException("Key not found.");
  }

  @SuppressWarnings("unchecked")
  @Override
  public void clear() {
    this.table = (LinkedList<Pair>[]) new LinkedList[this.capacity];
    this.size = 0;
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

  /** Helper method to double the capacity and rehash all key-value pairs. */
  @SuppressWarnings("unchecked")
  void rehash() {
    this.capacity *= 2;
    LinkedList<Pair>[] oldTable = this.table;
    this.table = (LinkedList<Pair>[]) new LinkedList[this.capacity];
    for (int i = 0; i < oldTable.length; i++) {
      if (oldTable[i] != null) {
        for (Pair pair : oldTable[i]) {
          int index = getIndex(pair.key);
          if (this.table[index] == null) {
            this.table[index] = new LinkedList<Pair>();
          }
          this.table[index].add(pair);
        }
      }
    }
  }

  /** Inner class to represent key-value pairs stored in the hash table. */
  protected class Pair {
    public KeyType key;
    public ValueType value;

    public Pair(KeyType key, ValueType value) {
      this.key = key;
      this.value = value;
    }
  }
}
