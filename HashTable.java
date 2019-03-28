//////////////////// ALL ASSIGNMENTS INCLUDE THIS SECTION /////////////////////
//
// Title: Hash Table
// Files: UTF-8
// Course: CS 400, Spring 19
//
// Author: Gerrard Kim
// Email: hkim624@wisc.edu
// Lecturer's Name: Debra Deppeler
//
//////////////////// PAIR PROGRAMMERS COMPLETE THIS SECTION ///////////////////
//
// Partner Name: (name of your pair programming partner)
// Partner Email: (email address of your programming partner)
// Partner Lecturer's Name: (name of your partner's lecturer)
//
// VERIFY THE FOLLOWING BY PLACING AN X NEXT TO EACH TRUE STATEMENT:
// ___ Write-up states that pair programming is allowed for this assignment.
// ___ We have both read and understand the course Pair Programming Policy.
// ___ We have registered our team prior to the team registration deadline.
//
///////////////////////////// CREDIT OUTSIDE HELP /////////////////////////////
//
// Students who get help from sources other than their partner must fully
// acknowledge and credit those sources of help here. Instructors and TAs do
// not need to be credited here, but tutors, friends, relatives, room mates,
// strangers, and others do. If you received no outside help from either type
// of source, then please explicitly indicate NONE.
//
// Persons: NONE
// Online Sources: NONE
//
/////////////////////////////// 80 COLUMNS WIDE ///////////////////////////////
import java.util.ArrayList;

/**
 * Implements hash table with chaining using array of linked nodes.
 * 
 * @author GERRARD
 *
 * @param <K> key
 * @param <V> value
 */
public class HashTable<K extends Comparable<K>, V> implements HashTableADT<K, V> {

    /**
     * An inner class for the implementation of hash table
     * 
     * @author GERRARD
     *
     * @param <K> key
     * @param <V> value
     */
    private class HashNode<K, V> {
        private K key; // unique key for the node
        private V value; // unique value for the node
        private HashNode<K, V> next; // the next node

        /**
         * A constructor that initializes key and value
         * 
         * @param key
         * @param value
         */
        HashNode(K key, V value) {
            this.key = key;
            this.value = value;
        }

        /**
         * An accessor that returns key
         * 
         * @return key
         */
        K getKey() {
            return this.key;
        }

        /**
         * An accessor that returns value
         * 
         * @return value
         */
        V getValue() {
            return this.value;
        }
    }

    private ArrayList<HashNode<K, V>> hashTable; // arraylist that represents a hash table
    private int initialCapacity; // Capacity of the array list
    private double loadFactorThreshold; // the limit that load factor can be
    private int numKeys; // number of keys in the list

    /**
     * A constructor with no argument
     * 
     */
    public HashTable() {
        hashTable = new ArrayList<>();
        this.initialCapacity = 10; // default capacity
        this.loadFactorThreshold = 1.0; // default loadfactor threshold

        for (int i = 0; i < initialCapacity; i++) {
            hashTable.add(null);
        }
    }

    /**
     * A constructor that initializes initialCapacity and loadFactorThreshold
     * 
     * @param initialCapacity
     * @param loadFactorThreshold
     */
    public HashTable(int initialCapacity, double loadFactorThreshold) {
        hashTable = new ArrayList<>();
        if (initialCapacity < 0) {
            return;
        }
        this.initialCapacity = initialCapacity;
        this.loadFactorThreshold = loadFactorThreshold;
        numKeys = 0;

        for (int i = 0; i < initialCapacity; i++) {
            hashTable.add(null);
        }
    }

    /**
     * Inserts a new node to the list
     * 
     * @param key to be inserted
     * @param value to be inserted
     * @exception IllegalNullKeyException if key is null
     * @exception DuplicateKeyException if key is already in data structure
     * 
     */
    @Override
    public void insert(K key, V value) throws IllegalNullKeyException, DuplicateKeyException {
        if (key == null) {
            throw new IllegalNullKeyException();
        }

        int bucketIndex = getIndex(key); // the list index of the key
        HashNode<K, V> current = hashTable.get(bucketIndex);

        while (current != null) {
            if (current.getKey().equals(key)) {
                throw new DuplicateKeyException();
            }
            current = current.next;
        } // checks for duplicate key in the list

        // open addressing for the keys that have same index
        current = hashTable.get(bucketIndex);
        HashNode<K, V> newNode = new HashNode<K, V>(key, value);
        newNode.next = current;
        hashTable.set(bucketIndex, newNode);
        numKeys++; // increment the number of keys


    }

    /**
     * Removes a node with the key from the list return true if key is found, false otherwise
     * 
     * @param key to be removed
     * @exception IllegalNullKeyException if key is null
     * 
     */
    @Override
    public boolean remove(K key) throws IllegalNullKeyException {
        if (key == null) {
            throw new IllegalNullKeyException();
        }

        int bucketIndex = getIndex(key); // the list index of the key
        HashNode<K, V> head = hashTable.get(bucketIndex);
        HashNode<K, V> predecessor = null; // predecessor node


        while (head != null) {
            if (head.getKey().equals(key)) {
                break;
            } // if key is found, stop
            predecessor = head; // keep finding if not found
            head = head.next;
        }

        if (head == null) {
            return false;
        }

        numKeys--; // decrement the number of keys
        if (predecessor != null) {
            predecessor.next = head.next;
        } else {
            hashTable.set(bucketIndex, head.next);
        }

        return true;
    }

    /**
     * Gets the value with the specified key
     * 
     * @param key to be accessed
     * @exception IllegalNullKeyException if key is null
     * @exception KeyNotFoundException if key is not found
     * @return the value associated with the specified key
     * 
     */
    @Override
    public V get(K key) throws IllegalNullKeyException, KeyNotFoundException {
        if (key == null) {
            throw new IllegalNullKeyException();
        }

        int bucketIndex = getIndex(key); // the list index of the key
        HashNode<K, V> head = hashTable.get(bucketIndex);

        // Searching the key in the chain
        while (head != null) {
            if (head.getKey().equals(key)) {
                return head.getValue();
            }
            head = head.next;
        }
        throw new KeyNotFoundException(); // if key is not found, throw it
    }

    /**
     * Gets the number of keys
     * 
     * @return the number of key in the data structure
     * 
     */
    @Override
    public int numKeys() {
        return this.numKeys;
    }

    /**
     * Gets the loadFactorThreshold
     * 
     * @return the loadFactorThreshold that was passed into the constructor
     * 
     */
    @Override
    public double getLoadFactorThreshold() {

        return this.loadFactorThreshold;
    }

    /**
     * Gets the load factor for the hash table
     * 
     * @return load factor = number of items / current table size
     * 
     */
    @Override
    public double getLoadFactor() {
        return (double) numKeys / initialCapacity;
    }

    /**
     * Gets the current table size of the hash table If the load factor threshold is reached, the
     * capacity becomes 2*capacity+1
     * 
     * @return the current capacity of the hash table
     * 
     */
    @Override
    public int getCapacity() {
        if ((getLoadFactor()) >= getLoadFactorThreshold()) {
            initialCapacity = 2 * initialCapacity + 1; // new table size
            // Assigning new table
            ArrayList<HashNode<K, V>> temp = hashTable;
            hashTable = new ArrayList<>();
            numKeys = 0;
            for (int i = 0; i < getCapacity(); i++) {
                hashTable.add(null);
            }
            for (HashNode<K, V> head : temp) {
                while (head != null) {
                    try {
                        insert(head.key, head.value);
                    } catch (IllegalNullKeyException | DuplicateKeyException e) {
                    } // handling the exceptions
                    head = head.next;
                }
            }
        }

        return this.initialCapacity;
    }

    /**
     * 5 CHAINED BUCKET: array of linked nodes
     * 
     * @return the collision resolution scheme used for this hash table
     * 
     */
    @Override
    public int getCollisionResolution() {
        return 5;
    }

    /**
     * Implements hash function to find index
     * 
     * @param key
     * @return the absolute value of key.hashCode % current table size
     * 
     */
    private int getIndex(K key) {
        return Math.abs(key.hashCode() % getCapacity());
    }

}
