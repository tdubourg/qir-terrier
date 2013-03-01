package org.terrier.structures;

import java.util.ArrayList;

public class Vector {

	public class DocumentVectorEntry {
		private int termId;
		private int occurences;

		public DocumentVectorEntry(int termid, int occ) {
			termId = termid;
			occurences = occ;
		}

		public int getTermId() { return termId; }
		public int getOccurences() { return occurences; }
	}
	
	private int i_pointer;
	private ArrayList<DocumentVectorEntry> entries;

    public Vector(){
		entries = new ArrayList<>(100);
	}

	public void reset() {
		i_pointer = -1;
	}

	public boolean endOfVector() {
		return i_pointer >= entries.size();
	}
    
    protected void pushEntry(DocumentVectorEntry dve) {
        entries.add(dve);
    }

	public DocumentVectorEntry getNextEntry() {
		i_pointer++;
		if (!endOfVector()) {
			return entries.get(i_pointer);
		} else {
			return null;
		}
	}
}