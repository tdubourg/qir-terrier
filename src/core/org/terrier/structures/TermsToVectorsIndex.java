/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.terrier.structures;

import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author troll
 */
public class TermsToVectorsIndex extends Index implements Iterable<Map.Entry<Integer, VectorSet>>, Map<Integer, VectorSet> {
    protected Map<Integer, VectorSet> entries;
    
    // ----------- Iterable Implementation ----------
    @Override
    public Iterator iterator() {
        return entries.entrySet().iterator();
    }

    // ----------- Map Interface Implementation ----------
    @Override
    public int size() {
        return entries.size();
    }

    @Override
    public boolean isEmpty() {
        return entries.isEmpty();
    }

    @Override
    public boolean containsKey(Object key) {
        return entries.containsKey(key);
    }

    @Override
    public boolean containsValue(Object value) {
        return entries.containsValue(value);
    }

    @Override
    public VectorSet get(Object key) {
        return entries.get(key);
    }

    @Override
    public VectorSet put(Integer key, VectorSet value) {
        return entries.put(key, value);
    }

    @Override
    public VectorSet remove(Object key) {
        return entries.remove(key);
    }

    @Override
    public void putAll(Map<? extends Integer, ? extends VectorSet> m) {
        entries.putAll(m);
    }

    @Override
    public void clear() {
        entries.clear();
    }

    @Override
    public Set<Integer> keySet() {
        return entries.keySet();
    }

    @Override
    public Collection<VectorSet> values() {
        return entries.values();
    }

    @Override
    public Set<Entry<Integer, VectorSet>> entrySet() {
        return entries.entrySet();
    }
    
    // ----------- /Map Interface Implementation ----------
    
    @Override
    public boolean equals(Object o) {
        if (o.getClass() != this.getClass()) {
            return false;
        }
        TermsToVectorsIndex other = (TermsToVectorsIndex)o;
        return this.entries.equals(other.entries);
    }
}
