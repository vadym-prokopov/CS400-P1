// --== CS400 File Header Information ==--
// Name: Vadym Prokopov
// Email: prokopov@wisc.edu
// Team: EE
// TA: Keren Chen
// Lecturer: Florian Heimerl
// Notes to Grader: HashTableMap Implementation

import java.util.LinkedList;
import java.util.NoSuchElementException;

public class HashTableMap<KeyType, ValueType> {

	/**
	 * Supplementary Node class for holding key-value pairs
	 * 
	 * @param key   : Key of the key-value pair
	 * @param value : Value of the key-value pair
	 */
	public class Node<KeyType, ValueType> {

		KeyType key;
		ValueType value;

		/**
		 * Class constructor for Node class
		 * 
		 * @param key   : Key of the key-value pair
		 * @param value : Value of the key-value pair
		 */
		public Node(KeyType key, ValueType value) {
			this.key = key;
			this.value = value;
		}
	}

	// Class Variables

	private int capacity;
	private static double loadFactor = 0.8;
	private int size;
	private LinkedList<Node<KeyType, ValueType>>[] table;
	private int threshold;

	// Class Constructors
	/**
	 * Class Constructor specifying the capacity of the HashTableMap
	 * 
	 * @param : capacity number of buckets in the HashTableMap
	 */
	public HashTableMap(int capacity) {
		/* Handle capacity being under <=0 */
		if (capacity <= 0) {
			throw new IllegalArgumentException("Capacity can't be less than or equal to 0. capacity = " + capacity);
		}
		this.capacity = capacity;
		size = 0;
		table = new LinkedList[capacity];
		threshold = (int) (capacity * loadFactor);
	}

	/**
	 * Class Constructor (defaults to a capacity of 10)
	 */
	public HashTableMap() {
		this(10);
	}

	// Class Methods

	/**
	 * Helper method for calculating and returning the bucketIndex for a given hash
	 * key and capacity Formula for index calculation: index = | key.hashCode() | %
	 * capacity
	 * 
	 * @param key      : the key (of the key value, pair) to be converted into an
	 *                 index.
	 * @param capacity : the capacity of the HashTableMap
	 * @return : the index of the bucket in which the given key-value pair should be
	 *         in.
	 */
	private int getIndex(KeyType key, int capacity) {
		return Math.abs(key.hashCode()) % capacity;
	}

	/**
	 * Helper method without specifying capacity (defaults to HashTableMap object's
	 * current capacity parameter)
	 * 
	 * @param : key the key (of the key value, pair) to be converted into an index.
	 * @return : the index of the bucket in which the given key-value pair should be
	 *         in.
	 */
	private int getIndex(KeyType key) {
		return getIndex(key, capacity);
	}

	/**
	 * Private helper method for resizing the table and rehashing all of the
	 * existing Nodes (key-value pairs).
	 * 
	 * @param : None
	 * @return : void
	 */
	private void resize() {

		LinkedList<Node<KeyType, ValueType>>[] newTable = new LinkedList[2 * capacity];
		for (int i = 0; i < capacity; i++) {
			LinkedList<Node<KeyType, ValueType>> oldBucket = table[i];
			if (oldBucket != null) {
				for (int j = 0; j < oldBucket.size(); j++) {
					Node<KeyType, ValueType> oldNode = oldBucket.get(j);
					int newBucketIndex = getIndex(oldNode.key, 2 * capacity);
					if (newTable[newBucketIndex] == null) {
						newTable[newBucketIndex] = new LinkedList<Node<KeyType, ValueType>>();
					}
					LinkedList<Node<KeyType, ValueType>> newBucket = newTable[newBucketIndex];
					Node<KeyType, ValueType> newNode = new Node(oldNode.key, oldNode.value);
					newBucket.addFirst(newNode);
				}
			}
		}
		this.capacity = 2 * capacity;
		this.table = newTable;
		this.threshold = (int) (this.capacity * loadFactor);
	}

	/**
	 * Method for putting a new key-value pair (Node object) into the HashTableMap
	 * 
	 * @param key   : Key of the key-value pair
	 * @param value : Value of the key-value pair
	 * @return : boolean value with the result of the operation. True, if successful
	 *         key-value pair insertion. False, if key already exists in the
	 *         HashTableMap
	 */
	public boolean put(KeyType key, ValueType value) {
		int bucketIndex = getIndex(key);
		Node<KeyType, ValueType> n = new Node(key, value);
		if (containsKey(key) == true) {
			return false;
		}
		if (table[bucketIndex] == null) {
			table[bucketIndex] = new LinkedList<Node<KeyType, ValueType>>();
		}
		LinkedList<Node<KeyType, ValueType>> bucket = table[bucketIndex];
		bucket.addFirst(n);
		size++;
		if (size >= threshold) {
			resize();
		}
		return true;

	}

	/**
	 * Method for checking if a key exists in the HashTableMap
	 * 
	 * @param key : Key of the key-value pair
	 * @return : boolean value with the result of the operation. True, if key exists
	 *         in HashTableMap. False, if key doesn't exist in the HashTableMap
	 */
	public boolean containsKey(KeyType key) {
		int bucketIndex = getIndex(key);
		// If no LinkedList at index, key doesn't exist
		if (table[bucketIndex] == null) {
			return false;
		}
		LinkedList<Node<KeyType, ValueType>> bucket = table[bucketIndex];
		for (int i = 0; i < bucket.size(); i++) {
			if (key.equals((bucket.get(i)).key)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Method for retrieving the value by key from the HashTableMap
	 * 
	 * @param key : Key of the key-value pair
	 * @return : ValueType value with the corresponding value of the key (taken from
	 *         the Node). If key doesn't exist in HashTableMap, throw
	 *         NoSuchElementException.
	 */
	public ValueType get(KeyType key) throws NoSuchElementException {
		int bucketIndex = getIndex(key);
		LinkedList<Node<KeyType, ValueType>> bucket = table[bucketIndex];
		if (bucket == null) {
			throw new NoSuchElementException("No element exists for key: " + key);
		}
		for (int i = 0; i < bucket.size(); i++) {
			if (key.equals((bucket.get(i)).key)) {
				return bucket.get(i).value;
			}
		}
		throw new NoSuchElementException("No element exists for key: " + key);
	}

	/**
	 * Method for retrieving the current size of the HashTableMap
	 * 
	 * @return : integer value of the current size of the HashTableMap
	 */
	public int size() {
		return size;
	}

	/**
	 * Method for removing a key-value pair by the key
	 * 
	 * @param key : Key of the key-value pair
	 * @return : ValueType value with the corresponding value of the deleted key
	 *         value pair (taken from the Node). If key doesn't exist in
	 *         HashTableMap return null.
	 */
	public ValueType remove(KeyType key) {
		int bucketIndex = getIndex(key);
		LinkedList<Node<KeyType, ValueType>> bucket = table[bucketIndex];
		for (int i = 0; i < bucket.size(); i++) {
			if (key.equals(((Node) bucket.get(i)).key)) {
				ValueType value = bucket.get(i).value;
				bucket.remove(i);
				size--;
				return value;
			}
		}
		return null;
	}

	/**
	 * Method for deleting/clearing all key-value pairs in HashTableMap
	 */
	public void clear() {
		size = 0;
		table = new LinkedList[capacity];
	}
}