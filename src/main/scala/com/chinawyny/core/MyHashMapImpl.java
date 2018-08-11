package com.chinawyny.core;

public class MyHashMapImpl<K, V> implements MyHashMap<K, V> {


    private static final int DEFAULT_LENGTH = 16;

    private static final double DEFAULT_LOAD_FACTOR = 0.75;

    private Node[] tables;

    private int threshold;

    private int size;

    public MyHashMapImpl() {
        tables = new Node[DEFAULT_LENGTH];
        threshold = (int) (DEFAULT_LENGTH * DEFAULT_LOAD_FACTOR);
    }

    static class Node<K, V> {
        final K key;
        V value;
        Node next;

        Node(K key, V value, Node<K, V> next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }

    private int getIndex(Object key) {
        int originHash = key.hashCode();
        return (originHash ^ (originHash >>> 16)) % (tables.length - 1);
    }

    private void resize() {
        if (size >= threshold) {
            Node[] newTables = new Node[tables.length * 2];

            for (int i = 0; i < tables.length; i++) {
                Node<K, V> node = tables[i];
                while (node != null) {
                    insert(node.key, node.value);
                }
            }
            tables = newTables;
        }
    }

    public V get(K key) {
        int index = getIndex(key);
        Node<K,V> node = tables[index];
        if (node == null) {
            return null;
        } else {
            while (!node.key.equals(key)) {
                node = node.next;
            }
            return node == null ? null : node.value;
        }

    }

    public boolean delete(K key) {
        int index = getIndex(key);
        Node node = tables[index];
        if (node == null) {
            return false;
        } else {
            Node pre = node;
            while (!node.key.equals(key)) {
                pre = node;
                node = node.next;
            }
            if (node != null) {
                pre.next = node.next;
                node = null;
                return true;
            }
            else {
                return false;
            }

        }
    }

    public boolean insert(K key, V value) {

        int index = getIndex(key);
        Node node = tables[index];
        if (node == null) {
            tables[index] = new Node<K, V>(key, value, null);
        } else {
            Node pre = node;
            while (!node.key.equals(key)) {
                pre = node;
                node = node.next;
            }
            if (node != null) {
                node.value = value;
            } else {

                Node newNode = new Node<K, V>(key, value, null);
                pre.next = newNode;
                size++;
                resize();
            }

            return true;
        }
        return false;

    }
}
