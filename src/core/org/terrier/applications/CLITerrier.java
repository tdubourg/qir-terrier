package org.terrier.applications;

import java.awt.Point;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Vector;

import javax.swing.table.TableColumn;
import javax.swing.table.TableModel;

import org.apache.log4j.Logger;
import org.terrier.indexing.BasicIndexer;
import org.terrier.indexing.BasicSinglePassIndexer;
import org.terrier.indexing.BlockIndexer;
import org.terrier.indexing.BlockSinglePassIndexer;
import org.terrier.indexing.Collection;
import org.terrier.indexing.Indexer;
import org.terrier.indexing.SimpleFileCollection;
import org.terrier.matching.ResultSet;
import org.terrier.querying.Manager;
import org.terrier.querying.SearchRequest;
import org.terrier.querying.parser.Query;
import org.terrier.querying.parser.QueryParser;
import org.terrier.structures.Index;
import org.terrier.structures.MetaIndex;
import org.terrier.utility.ApplicationSetup;
import org.terrier.utility.Rounding;


public class CLITerrier {
	protected static final Logger logger = Logger.getLogger(CLITerrier.class);

	private boolean debug = false;
	
	private List<String> folderList;
	private Index diskIndex;
	private Manager queryingManager;
	private String managerName = ApplicationSetup.getProperty("desktop.manager", "Manager");
	private static String mModel = ApplicationSetup.getProperty("desktop.matching","Matching");
	private static String wModel = ApplicationSetup.getProperty("desktop.model", "PL2");
	
	public CLITerrier() {
		//setting properties for the application
		ApplicationSetup.BLOCK_INDEXING = true;
		ApplicationSetup.BLOCK_SIZE = 1;
		//assume some defaults so we can work
		if (( ApplicationSetup.getProperty("querying.allowed.controls", null)) == null)
		{
			ApplicationSetup.setProperty("querying.allowed.controls", "c,start,end,qe");
		}
		if ((ApplicationSetup.getProperty("querying.postprocesses.order", null)) == null)
		{
			ApplicationSetup.setProperty("querying.postprocesses.order", "QueryExpansion");
		}
		if ((ApplicationSetup.getProperty("querying.postprocesses.controls", null)) == null)
		{
			ApplicationSetup.setProperty("querying.postprocesses.controls", "qe:QueryExpansion");
		}
		ApplicationSetup.setProperty("indexer.meta.forward.keys","docno,filename");
		ApplicationSetup.setProperty("indexer.meta.forward.keylens","26,2048");
		ApplicationSetup.setProperty("indexing.max.tokens", "10000");
		ApplicationSetup.setProperty("invertedfile.processterms","25000");
		ApplicationSetup.setProperty("ignore.low.idf.terms","false");
		ApplicationSetup.setProperty("matching.dsms", "BooleanFallback");
		folderList = new LinkedList<String>();
		//TODO get the folders to index by command line arguments
		folderList.add("/home/pierre/tmp/terrier-3.5/doc");
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		CLITerrier cliTerrier = new CLITerrier();		
		
		if (args.length == 1 && args[0].equals("--runindex")) {
			cliTerrier.runIndex();
		} else 
		{
			if (args.length == 1 && args[0].equals("--debug")) {
				cliTerrier.setDebug(true);
			}
			if (args.length == 1 && args[0].equals("--query")) {
				if(cliTerrier.loadIndices()) {
					cliTerrier.runQuery();
				} else {
					System.out.println("Error: Can't load Indices");
				}
			}
		}

	}

	private void setDebug(boolean b) {
		// TODO Auto-generated method stub
		debug = b;
	}

	private void runIndex() {
		System.out.println("Number of Documents: ");
		System.out.println("Number of Tokens: ");
		System.out.println("Number of Unique Terms: ");
		System.out.println("Number of Pointers: ");
	
		if (folderList == null || folderList.size() == 0)
		{
			logger.error("No folders specified to index. Aborting indexing process.");
			return;
		}
	
		try {
			//deleting existing files
			if (diskIndex!=null) {
				diskIndex.close();
				diskIndex = null;
				
			}
			queryingManager = null;	

			//remove any existing index
			File indexPath = new File(ApplicationSetup.TERRIER_INDEX_PATH);
			if (indexPath.exists())
				deleteFiles(indexPath);
			else
				if (! indexPath.mkdirs()) //ensure that the index folder exists
				{
					logger.error("ERROR: Could not create the index folders at: "+ indexPath);
					logger.error("Aborting indexing process");
					return;
				}
	
	
			Indexer indexer;
			final boolean useSinglePass = Boolean.parseBoolean(ApplicationSetup.getProperty("desktop.indexing.singlepass", "false"));
			indexer = ApplicationSetup.BLOCK_INDEXING
				? useSinglePass 
					? new BlockSinglePassIndexer(ApplicationSetup.TERRIER_INDEX_PATH, ApplicationSetup.TERRIER_INDEX_PREFIX)  
					: new BlockIndexer(ApplicationSetup.TERRIER_INDEX_PATH, ApplicationSetup.TERRIER_INDEX_PREFIX)
				: useSinglePass
					? new BasicSinglePassIndexer(ApplicationSetup.TERRIER_INDEX_PATH, ApplicationSetup.TERRIER_INDEX_PREFIX)
					: new BasicIndexer(ApplicationSetup.TERRIER_INDEX_PATH, ApplicationSetup.TERRIER_INDEX_PREFIX);
	
			SimpleFileCollection sfc = new SimpleFileCollection(folderList, true);
	
			indexer.index(new Collection[] { sfc });
	
			
			//load in the indexes
			if (loadIndices()) {
				//indices loaded
				/*
				jTabbedPane.setEnabledAt(jTabbedPane.indexOfTab("Search"), true);
				jTabbedPane.setSelectedIndex(jTabbedPane.indexOfTab("Search"));
				getJTextField().requestFocusInWindow(); 
				*/
				System.out.println("Indices loaded");
				
			} else { //indices failed to load, probably because we've not indexed
					 // anything yet
//				jTabbedPane.setEnabledAt(jTabbedPane.indexOfTab("Search"), false);
//				jTabbedPane.setSelectedIndex(jTabbedPane.indexOfTab("Index"));
				System.out.println("Failed to load indices");
			}
		
			if (diskIndex != null)	
			{
				System.out.println("Number of Documents: " + diskIndex.getCollectionStatistics().getNumberOfDocuments());
				System.out.println("Number of Tokens: " + diskIndex.getCollectionStatistics().getNumberOfTokens());
				System.out.println("Number of Unique Terms: " + diskIndex.getCollectionStatistics().getNumberOfUniqueTerms());
				System.out.println("Number of Pointers: " + diskIndex.getCollectionStatistics().getNumberOfPointers());
			}
			else
			{
				System.out.println("Number of Documents: 0");
				System.out.println("Number of Tokens: 0");
				System.out.println("Number of Unique Terms: 0");
				System.out.println("Number of Pointers: 0");
			}
			
		} catch(Exception e) {
			logger.error("An unexpected exception occured while indexing. Indexing has been aborted.",e);
			
		}

	}
	
	/**
	 * Processes the query and returns the results.
	 */
	private void runQuery() {
		//TODO replace this by a command line argument
		String query = "divergence";
		if (query == null || query.length() == 0)
			return;
		try {
			Query q = null;
			try{
				q = QueryParser.parseQuery(query);
			} catch (Exception e) {
				//century kludge!
				//remove everything except character and spaces, and retry
				q = QueryParser.parseQuery(query.replaceAll("[^a-zA-Z0-9 ]", ""));	
			}
			
			if (q == null)
			{
				//give up
				return;
			}
			if (queryingManager == null)
			{
				return;
			}
			System.out.println(q.toString());	
			SearchRequest srq = queryingManager.newSearchRequest();
			srq.setQuery(q);
			srq.addMatchingModel(mModel, wModel);
			srq.setControl("c", "1.0d");
			queryingManager.runPreProcessing(srq);
			queryingManager.runMatching(srq);
			queryingManager.runPostProcessing(srq);
			queryingManager.runPostFilters(srq);
			renderResults(srq.getResultSet());
		} catch (Exception e) {
			logger.error("An exception when running the query: #"+query +"# :",e);
			
		}

	}
	
	private void renderResults(ResultSet rs) throws Exception {
		Vector<String> HeaderRow = new Vector<String>(4);
		MetaIndex meta = diskIndex.getMetaIndex();
		HeaderRow.add(" ");
		HeaderRow.add("Filename");
		HeaderRow.add("Directory");
		HeaderRow.add("Score");
		System.err.println("INFO: RenderResults "+rs.getExactResultSize()+" "+rs.getResultSize());
		int ResultsSize = rs.getResultSize();
		int[] docids = rs.getDocids();
		double[] scores = rs.getScores();
		Vector<Vector<String>> rows = new Vector<Vector<String>>(ResultsSize);
		System.out.println(HeaderRow);
		for (int i = 0; i < ResultsSize; i++) {
			Vector<String> thisRow = new Vector<String>(4);
			thisRow.add("" + (i + 1));
			String f = meta.getItem("filename", docids[i]);
			System.err.println("INFO: RenderResults "+f);
			//String f = (String) fileList.get(docids[i]);
			if (f == null)
				continue;
			int dotIndex = f.lastIndexOf('.');
			String extension = f.substring(dotIndex+1);
			thisRow.add(new File(f).getName());
			thisRow.add(new File(f).getPath());
			thisRow.add(Rounding.toString(scores[i], 4));
			//rows.add(thisRow);
			System.out.println(thisRow);
		}
		System.err.println("INFO: RenderResults done rendering");		
	}

	private void deleteFiles(File dir)
	{
		String[] files = dir.list();
		for (int i = 0; i < files.length; i++) {
			File f = new File(dir, files[i]);
			if (f.exists()) 
			{
				if (f.isFile())
				{
					logger.info("Deleting: "+f+": "+f.delete());
				}
				else if (f.isDirectory() && ! f.getName().equals("CVS"))
				{
					deleteFiles(f);
					logger.info("Deleting: "+f+": "+f.delete());
				}
			}
		}	
	}
	
	/**
	 * Returns true if loading the index succeeded and the system is ready for
	 * querying. If false, then the collection needs to be indexed first.
	 */
	private boolean loadIndices() {
		if (diskIndex != null)
			try{
				diskIndex.close();
			} catch (IOException ioe) {
				logger.warn("Problem closing old index", ioe);
			}
		diskIndex = null;
		diskIndex = Index.createIndex();
		
		if (diskIndex == null)
		{
			return false;
		}
		System.out.println("Number of Documents: " + diskIndex.getCollectionStatistics().getNumberOfDocuments());
		System.out.println("Number of Tokens: " + diskIndex.getCollectionStatistics().getNumberOfTokens());
		System.out.println("Number of Unique Terms: " + diskIndex.getCollectionStatistics().getNumberOfUniqueTerms());
		System.out.println("Number of Pointers: " + diskIndex.getCollectionStatistics().getNumberOfPointers());
		try{
			if (managerName.indexOf('.') == -1)
				managerName = "org.terrier.querying."+managerName;
			queryingManager = (Manager) (Class.forName(managerName)
				.getConstructor(new Class[]{Index.class})
				.newInstance(new Object[]{diskIndex}));
		} catch (Exception e) {
			logger.warn("Problem loading Manager ("+managerName+"): ",e);
			return false;
		}
		if (queryingManager == null)
			return false;
		return true;
	}

}
