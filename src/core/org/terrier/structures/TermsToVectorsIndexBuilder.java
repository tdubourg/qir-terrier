/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.terrier.structures;

import java.util.Iterator;
import org.terrier.structures.Vector;
import org.terrier.utility.ApplicationSetup;

//* This class is not multi-thread-proof because of internal classes that are not
public class TermsToVectorsIndexBuilder {

    //* This class is not multi-thread-proof
    static protected class CircularFixedSizeBuffer<E> implements Iterable<E> {

        private java.util.concurrent.LinkedBlockingDeque deq;

        public CircularFixedSizeBuffer(int capacity) {
            deq = new java.util.concurrent.LinkedBlockingDeque<E>(capacity);
        }

        public void push(E element) {
            try {
                deq.add(element);
            } catch (IllegalStateException e) {
                deq.pop();
                deq.add(element);
            }
        }

        @Override
        public Iterator<E> iterator() {
            return deq.iterator();
        }
    }
    private CircularFixedSizeBuffer<Integer> buffer;
    private TermsToVectorsIndex ttvi;

    public TermsToVectorsIndexBuilder() {
        buffer = new CircularFixedSizeBuffer<>(ApplicationSetup.WINDOW_SIZE);
        ttvi = new TermsToVectorsIndex();
    }

    private Vector createVectorFromBuffer() {
        Vector dv = new Vector();
        for (Integer termid : buffer) {
            dv.pushNewTerm(termid);
        }
        return dv;
    }

    public void pushTerm(int termid) {
        buffer.push(termid);
        Vector dv = createVectorFromBuffer();
        VectorSet vs = ttvi.get(termid);
        if (null != vs) {
            vs.insert(dv);
        } else {
            ttvi.put(termid, new VectorSet().insert(dv));
        }
    }
    
    public TermsToVectorsIndex getBuiltIndex() {
        return ttvi;
    }
}