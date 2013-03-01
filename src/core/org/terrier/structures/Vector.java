package org.terrier.structures;

import java.util.ArrayList;

public class Vector {

    public class VectorEntry {

        private int termId;
        private int occurences;

        public VectorEntry(int termid, int occ) {
            termId = termid;
            occurences = occ;
        }

        public int getTermId() {
            return termId;
        }

        public int getOccurences() {
            return occurences;
        }

        private void newOccurence() {
            occurences++;
        }
    }
    private int i_pointer;
    private ArrayList<VectorEntry> entries;

    public Vector() {
        entries = new ArrayList<>(100);
    }

    /**
     * Standard indexOf, returns the index of the element whose termId is equal to the one passed as a parameter, or -1
     *
     * @param termId
     * @return -1 is not found, a number between 0 and entries.size()-1 if found
     */
    private int indexOf(int termId) {
        if (entries.isEmpty()) {
            return -1;
        }
        VectorEntry ve = null;
        int max = entries.size();
        for (int i = 0; i < max; i++) {
            if (termId == ve.getTermId()) {
                return i;
            }
        }
        return -1;
    }

    public void reset() {
        i_pointer = -1;
    }

    public boolean endOfVector() {
        return i_pointer >= entries.size();
    }

    protected void pushEntry(VectorEntry dve) {
        entries.add(dve);
    }

    protected void pushNewTerm(int termId) {
        int ind = indexOf(termId);
        if (-1 == ind) {
            VectorEntry v = new VectorEntry(termId, 1);
            entries.add(v);
        } else {
            entries.get(ind).newOccurence();
        }
    }

    public VectorEntry getNextEntry() {
        i_pointer++;
        if (!endOfVector()) {
            return entries.get(i_pointer);
        } else {
            return null;
        }
    }
}