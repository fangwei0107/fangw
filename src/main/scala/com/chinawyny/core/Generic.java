package com.chinawyny.core;

public class Generic<T> {
    private T key;
    public Generic(T key) {
        this.key = key;
    }

    T getKey() {
        return key;
    }

    public static void main(String[] args) {
        Generic<Integer> generic = new Generic<Integer>(1);
        Generic<Number> numberGeneric = new Generic<Number>(123);
        showKeyValue1(generic);
    }

    public static void showKeyValue1(Generic<?> obj){
        System.out.println("泛型测试key value is " + obj.getKey());
    }
    public <K> K genericMethod(Class<K> tClass)throws InstantiationException ,
            IllegalAccessException{
        K instance = tClass.newInstance();
        return instance;
    }
}
