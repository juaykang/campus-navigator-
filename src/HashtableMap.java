// == CS400 Fall 2024 File Header Information ==
// Name: Jaden Tan
// Email: <jtan94@wisc.edu email address>
// Group: <your group's id: P2.1804>
// Lecturer: Gary Dahl
// Notes to Grader: <optional extra notes>


import java.util.LinkedList;
import java.util.NoSuchElementException;


import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class HashtableMap<KeyType, ValueType> implements MapADT<KeyType, ValueType> {

  protected class Pair {

    public KeyType key;
    public ValueType value;

    public Pair(KeyType key, ValueType value) {
      this.key = key;
      this.value = value;
    }

  }

  protected LinkedList<Pair>[] table = null;
  // current size of the hashmap
  private int size;
  // current capacity of the hashmap
  private int capacity;

  public HashtableMap(int capacity) {
    this.capacity = capacity;
    // throw exception if the capacity of the hashmap is less than or equal to zero
    if (capacity <= 0) {
      throw new IllegalArgumentException("capacity cannot be less than zero");
    }
    // initialize the hashmap to the capacity passed in the arguemnt
    table = new LinkedList[capacity];
  }

  // default capacity for the hashmap
  public HashtableMap() {
    this(64);
  } // with default capacity = 64

  /**
   * This method inserts the key and value into the hashmap
   *
   * @param key   the key of the key,value pair
   * @param value the value that key maps to
   * @throws IllegalArgumentException if the key is already in the hashmap, or the key
   *                                  passed is null
   */
  @Override
  public void put(KeyType key, ValueType value) throws IllegalArgumentException {
    // creating a object Pair for the argument that is passed
    Pair pair = new Pair(key, value);
    // if the key is null, throw NullPointerException
    if (key == null) {
      throw new NullPointerException("key cannot be null");
    }
    // if the key is already in the hashmap, throw IllegalArgumentException
    if (this.containsKey(key)) {
      throw new IllegalArgumentException("key already exists in the hashmap");
    }

    int hashIndex = Math.abs(key.hashCode() % this.getCapacity());
    if (table[hashIndex] == null) {
      table[hashIndex] = new LinkedList<>();
    }
    // add the pair to the linkedList
    table[hashIndex].add(pair);
    // increment the size of the hashmap
    size++;
    // load factor resize
    if ((double) size / getCapacity() >= 0.8) {
      resize();
    }
  }

  /**
   * This method resizes the hashmap by creating a new array of linkedLists
   */
  private void resize() {
    // doubles the initial capacity
    int newCapacity = getCapacity() * 2;
    LinkedList<Pair>[] newTable = new LinkedList[newCapacity];

    for (LinkedList<Pair> item : table) {
      if (item != null) {
        for (Pair pair : item) {
          // rehashing for the new array
          int newIndex = Math.abs(pair.key.hashCode()) % newCapacity;
          if (newTable[newIndex] == null) {
            newTable[newIndex] = new LinkedList<>();
          }
          newTable[newIndex].add(pair);
        }
      }
    }

    this.table = newTable;
    this.capacity = newCapacity;
  }

  /**
   * This method returns true if the key passed in is in the hashmap, false otherwise
   *
   * @param key the key to check
   * @return true or false
   */
  @Override
  public boolean containsKey(KeyType key) {
    int index = Math.abs(key.hashCode()) % capacity;
    if (key == null) {
      return false;
    }
    LinkedList<Pair> item = table[index];
    if (item != null) {
      for (Pair pair : table[index]) {
        if (pair.key.equals(key)) {
          return true;
        }
      }
    }
    return false;
  }

  /**
   * This method returns the value of the key that is passed in
   *
   * @param key the key to look up
   * @return the value of the key that is passed in
   * @throws NoSuchElementException if the key is not found in the hashmap
   */
  @Override
  public ValueType get(KeyType key) throws NoSuchElementException {
    int index = Math.abs(key.hashCode()) % capacity;
    if (table[index] == null) {
      throw new NoSuchElementException("key is not in the hashmap");
    }
    // returns the value if the key is found
    for (Pair pair : table[index]) {
      if (pair.key.equals(key)) {
        return pair.value;
      }
    }
    throw new NoSuchElementException("Key is not in the hashmap");
  }

  /**
   * This method removes the key passed in from the hashmap
   *
   * @param key the key whose mapping to remove
   * @return the value of the key that is removed
   * @throws NoSuchElementException if the key is not found in the hashmap
   */
  @Override
  public ValueType remove(KeyType key) throws NoSuchElementException {
    if (key == null) {
      throw new NoSuchElementException("key cannot be null.");
    }

    int index = Math.abs(key.hashCode()) % capacity;
    LinkedList<Pair> item = table[index];
    if (item != null) {
      for (Pair pair : item) {
        if (pair.key.equals(key)) {
          ValueType value = pair.value;
          item.remove(pair);
          size--;
          return value;
        }
      }
    }
    throw new NoSuchElementException("key not found");
  }

  /**
   * This method clears all the values in the hashmap
   */
  @Override
  public void clear() {
    this.table = new LinkedList[capacity];
    this.size = 0;

  }

  /**
   * This method returns the size of the hashmap
   *
   * @return size of hashmap
   */
  @Override
  public int getSize() {
    return size;
  }

  /**
   * This method returns the capacity of the hashmap
   *
   * @return capacity of the hashmap
   */
  @Override
  public int getCapacity() {
    return capacity;
  }

  public LinkedList<KeyType> getKeys() {
    LinkedList<KeyType> key = new LinkedList<>();

    for (LinkedList<Pair> item: table) {
      if (item != null) {
        for (Pair pair : item) {
          key.add(pair.key);
        }
      }
    }
    return key;
  }

  // test for the put and getSize method
  @Test
  public void test1() {
    // creates a hashmap
    HashtableMap<String, Integer> hashmap = new HashtableMap<>();
    // inserts keytype "hi" and value "2"
    hashmap.put("hi", 2);
    IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class, () -> hashmap.put("hi", 3),
            "expected exception for duplicate key"
    );
    assertEquals("key already exists in the hashmap", exception.getMessage(),
            "wrong exception message");
    assertEquals(1, hashmap.getSize(), "wrong size");
  }

  // test for the get method
  @Test
  public void test2() {
    HashtableMap<String, Integer> hashmap = new HashtableMap<>();
    hashmap.put("testKey", 3);
    hashmap.put("testKey2", 5);
    NoSuchElementException exception = assertThrows(NoSuchElementException.class,
            () -> hashmap.get("key"), "NoSuchElement exception expected");
    assertEquals(3, hashmap.get("testKey"), "wrong value");
  }

  // test for the remove and contains method
  @Test
  public void test3() {
    HashtableMap<String, Integer> hashmap = new HashtableMap<>();
    hashmap.put("testKey", 3);
    hashmap.put("testKey2", 5);
    hashmap.remove("testKey");
    NoSuchElementException exception = assertThrows(NoSuchElementException.class,
            () -> hashmap.get("key"), "NoSuchElement exception expected");

    assertEquals(1, hashmap.getSize(), "wrong size");

  }

  // tests the clear method
  @Test
  public void test4() {
    HashtableMap<String, Integer> hashmap = new HashtableMap<>();
    hashmap.put("testKey", 3);
    hashmap.put("testKey2", 5);
    hashmap.clear();
    assertEquals(0, hashmap.getSize(), "size should be 0");
  }

  // test for the getCapacity method
  @Test
  public void test5() {
    HashtableMap<String, Integer> hashmap = new HashtableMap<>();
    hashmap.put("testKey", 3);
    hashmap.put("testKey2", 5);
    assertEquals(64, hashmap.getCapacity(), "wrong capacity");

    HashtableMap<String, String> customMap = new HashtableMap<>(128);
    assertEquals(128, customMap.getCapacity());
  }


}

