/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.terrier.structures.indexing;

import org.terrier.indexing.DocumentVectorsBuilder;
import org.terrier.indexing.MyIndexer;
import org.terrier.structures.DocumentVectorSet;
import org.terrier.terms.TermPipeline;

/**
 * This class implements an end of a TermPipeline that adds the term to the DocumentTree. This TermProcessor does NOT have field support.
 */
public class MyIndexerBasicTermProcessor implements TermPipeline {
    private DocumentPostingList termsInDocument;
    private DocumentVectorSet documentVectorsSet;
    private DocumentVectorsBuilder currentDocumentVectorsBuilder;
    private MyIndexer currenIndexer;

    public MyIndexerBasicTermProcessor(DocumentPostingList _termsInDocument, DocumentVectorSet _documentVectorSet, DocumentVectorsBuilder _documentVectorBuilder, MyIndexer _currentIndexer) {
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
            documentVectorsSet.insert(currentDocumentVectorsBuilder.pushTerm(term));
            currenIndexer.incrementNumOfTokensInDocument();
        }
    }

    public boolean reset() {
        return true;
    }
}