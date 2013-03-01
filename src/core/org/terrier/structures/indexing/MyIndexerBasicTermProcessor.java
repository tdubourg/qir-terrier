/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.terrier.structures.indexing;

import org.terrier.structures.TermsToVectorsIndexBuilder;
import org.terrier.indexing.MyIndexer;
import org.terrier.structures.VectorSet;
import org.terrier.terms.TermPipeline;

/**
 * This class implements an end of a TermPipeline that adds the term to the DocumentTree. This TermProcessor does NOT have field support.
 */
public class MyIndexerBasicTermProcessor implements TermPipeline {
    private DocumentPostingList termsInDocument;
    private VectorSet documentVectorsSet;
    private TermsToVectorsIndexBuilder currentDocumentVectorsBuilder;
    private MyIndexer currenIndexer;

    public MyIndexerBasicTermProcessor(DocumentPostingList _termsInDocument, VectorSet _documentVectorSet, TermsToVectorsIndexBuilder _documentVectorBuilder, MyIndexer _currentIndexer) {
        System.out.println("@@ Instanciating a new basic term processor");
        termsInDocument = _termsInDocument;
        currentDocumentVectorsBuilder = _documentVectorBuilder;
        documentVectorsSet = _documentVectorSet;
        currenIndexer = _currentIndexer;
    }

    //term pipeline implementation
    public void processTerm(String term) {
        // System.out.println("Processing a new term " + term);
			/* null means the term has been filtered out (eg stopwords) */
        if (term != null) {
            //add term to thingy tree
            termsInDocument.insert(term);
            int termId;
            termId = termsInDocument.getTermId(term);
            documentVectorsSet.insert(currentDocumentVectorsBuilder.pushTerm(termId));
            currenIndexer.incrementNumOfTokensInDocument();
        }
    }

    public boolean reset() {
        return true;
    }
}