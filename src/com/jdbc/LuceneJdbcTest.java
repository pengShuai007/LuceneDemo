package com.jdbc;

import java.io.File;
import java.io.IOException;
import java.sql.ResultSet;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.Field.Store;
import org.apache.lucene.document.FieldType;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.IndexableFieldType;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

public class LuceneJdbcTest {

	private static String INDEX_DIR = "F:\\Workspaces\\eclipse_mine\\LuceneDemo\\JdbcIndex";
	private static Analyzer analyzer = null;
	private static Directory directory = null;
	private static IndexWriter indexWriter = null;
	
	public static void createIndex(ResultSet rs){
		try {
			analyzer = new StandardAnalyzer();
			directory = FSDirectory.open(new File(INDEX_DIR).toPath());
			
			File indexFile = new File(INDEX_DIR);
			if (!indexFile.exists()) {
				indexFile.mkdirs();
			}
			IndexWriterConfig config = new IndexWriterConfig(analyzer);
			indexWriter = new IndexWriter(directory, config);
			while(rs.next()){
				Document document = new Document();
				new Field("", "", null);
				FieldType t = new FieldType();
				new Field("", "", t);
				document.add(new TextField("ID", rs.getString(0), Store.YES));
				document.add(new TextField("HOSPITAL_ID", rs.getString(1), Store.YES));
				document.add(new TextField("FUNCTION_ID", rs.getString(2), Store.YES));
				document.add(new TextField("NAME", rs.getString(3), Store.YES));
				indexWriter.addDocument(document);
			}
			indexWriter.commit();
			indexWriter.close();
			System.out.println("-----------索引创建完成------------");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	public static void searchDb(String queryString){
		try {
			directory = FSDirectory.open(new File(INDEX_DIR).toPath());
			analyzer = new StandardAnalyzer();
			DirectoryReader ireader = DirectoryReader.open(directory);
			IndexSearcher isearcher = new IndexSearcher(ireader);

			QueryParser parser = new QueryParser("NAME", analyzer);
			Query query = parser.parse(queryString);
			ScoreDoc[] hits = isearcher.search(query, 1000).scoreDocs;

			for (int i = 0; i < hits.length; i++) {
				Document hitDoc = isearcher.doc(hits[i].doc);
				System.out.println("____________________________");
				System.out.println("ID："+hitDoc.get("ID"));
				System.out.println("医院ID："+hitDoc.get("HOSPITAL_ID"));
				System.out.println("函数ID："+hitDoc.get("FUNCTION_ID"));
				System.out.println("姓名："+hitDoc.get("NAME"));
				System.out.println("____________________________");
			}
			ireader.close();
			directory.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	} 
}
