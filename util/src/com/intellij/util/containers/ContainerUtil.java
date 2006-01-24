/*
 * Copyright 2000-2005 JetBrains s.r.o.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.intellij.util.containers;

import com.intellij.openapi.util.Condition;
import com.intellij.util.Function;

import java.util.*;
import java.lang.reflect.Array;

public class ContainerUtil {
  public static List mergeSortedLists(List list1, List list2, Comparator comparator, boolean mergeEqualItems){
    ArrayList result = new ArrayList();

    int index1 = 0;
    int index2 = 0;
    while(index1 < list1.size() || index2 < list2.size()){
      if (index1 >= list1.size()){
        result.add(list2.get(index2++));
      }
      else if (index2 >= list2.size()){
        result.add(list1.get(index1++));
      }
      else{
        Object element1 = list1.get(index1);
        Object element2 = list2.get(index2);
        int c = comparator.compare(element1,  element2);
        if (c < 0){
          result.add(element1);
          index1++;
        }
        else if (c > 0){
          result.add(element2);
          index2++;
        }
        else{
          result.add(element1);
          if (!mergeEqualItems){
            result.add(element2);
          }
          index1++;
          index2++;
        }
      }
    }

    return result;
  }

  public static <T> void addAll(Collection<T> collection, Iterator<T> iterator) {
    while (iterator.hasNext()) {
      T o = iterator.next();
      collection.add(o);
    }
  }

  public static <T> ArrayList<T> collect(Iterator<T> iterator) {
    ArrayList<T> list = new ArrayList<T>();
    addAll(list, iterator);
    return list;
  }

  public static <T> HashSet<T> collectSet(Iterator<T> iterator) {
    HashSet<T> hashSet = new HashSet<T>();
    addAll(hashSet, iterator);
    return hashSet;
  }

  public static <K,V> HashMap<K, V> assignKeys(Iterator<V> iterator, Convertor<V, K> keyConvertor) {
    HashMap<K, V> hashMap = new HashMap<K, V>();
    while (iterator.hasNext()) {
      V value = iterator.next();
      hashMap.put(keyConvertor.convert(value), value);
    }
    return hashMap;
  }

  public static <K, V> HashMap<K, V> assignValues(Iterator<K> iterator, Convertor<K, V> valueConvertor) {
    HashMap<K, V> hashMap = new HashMap<K, V>();
    while (iterator.hasNext()) {
      K key = iterator.next();
      hashMap.put(key, valueConvertor.convert(key));
    }
    return hashMap;
  }

  public static <T> Iterator<T> emptyIterator() {
    return new Iterator<T>() {
      public boolean hasNext() { return false; }
      public T next() { throw new NoSuchElementException(); }
      public void remove() { throw new IllegalStateException(); }
    };
  }

  public static <T> T find(Object[] array, Condition<T> condition) {
    for (Object anArray : array) {
      T element = (T)anArray;
      if (condition.value(element)) return element;
    }
    return null;
  }

  public static <T> T find(Iterable<? extends T> iterable, Condition<T> condition) {
    return find(iterable.iterator(), condition);
  }

  public static <T> T find(Iterator<? extends T> iterator, Condition<T> condition) {
    while (iterator.hasNext()) {
      T value = iterator.next();
      if (condition.value(value)) return value;
    }
    return null;
  }

  public static <T,V> List<V> map2List(Collection<? extends T> collection, Function<T,V> mapper) {
    final ArrayList<V> list = new ArrayList<V>(collection.size());
    for (final T t : collection) {
      list.add(mapper.fun(t));
    }
    return list;
  }

  public static <T,V> V[] map2Array(Collection<? extends T> collection, Class<V> aClass, Function<T,V> mapper) {
    final List<V> list = map2List(collection, mapper);
    return list.toArray((V[])Array.newInstance(aClass, list.size()));
  }

  public static <T,V> V[] map2Array(Collection<? extends T> collection, V[] to, Function<T,V> mapper) {
    return map2List(collection, mapper).toArray(to);
  }

  public static <T> List<T> findAll(Collection<? extends T> collection, Condition<T> condition) {
    final ArrayList<T> result = new ArrayList<T>();
    for (final T t : collection) {
      if (condition.value(t)) {
        result.add(t);
      }
    }
    return result;
  }

  public static <T> void removeDuplicates(Collection<T> collection) {
    Set<T> collected = new HashSet<T>();
    for (Iterator<T> iterator = collection.iterator(); iterator.hasNext();) {
      T t = iterator.next();
      if (!collected.contains(t)) {
        collected.add(t);
      } else {
        iterator.remove();
      }
    }
  }

  public static <T> Iterator<T> iterate(T[] arrays) {
    return Arrays.asList(arrays).iterator();
  }

  public static <T> Iterator<T> iterate(final Enumeration<T> enumeration) {
    return new Iterator<T>() {
      public boolean hasNext() {
        return enumeration.hasMoreElements();
      }

      public T next() {
        return enumeration.nextElement();
      }

      public void remove() {
        throw new UnsupportedOperationException();
      }
    };
  }

  public static <E> void swapElements(final List<E> list, final int index1, final int index2) {
    E e1 = list.get(index1);
    E e2 = list.get(index2);
    list.set(index1, e2);
    list.set(index2, e1);
  }

  public static <T> ArrayList<T> collect(Iterator iterator, FilteringIterator.InstanceOf<T> instanceOf) {
    return collect(FilteringIterator.create(iterator, instanceOf));
  }

  public static <T> void addAll(Collection<T> collection, Enumeration<T> enumeration) {
    while (enumeration.hasMoreElements()) {
      T element = enumeration.nextElement();
      collection.add(element);
    }
  }

  public static <T, U extends T> T findInstance(Iterator<T> iterator, Class<U> aClass) {
    // uncomment for 1.5
    //return (U)find(iterator, new FilteringIterator.InstanceOf<U>(aClass));
    return (T)find(iterator, new FilteringIterator.InstanceOf<T>((Class<T>)aClass));
  }

  public static <T,V> List<T> concat(Iterable<V> list, Function<V,List<T>> fun) {
    final ArrayList<T> result = new ArrayList<T>();
    for (final V v : list) {
      result.addAll(fun.fun(v));
    }
    return result;
  }

}
