package com.chinawyny.core;

public interface MyHashMap<K, V> {

    interface Entry<K, V> {
        K getKey();
        V getValue()
                ;
    }


    V get(K key);

    boolean delete(K ket);

    boolean insert(K key, V value);
}
