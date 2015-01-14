package hpi.sparkml;


import hpi.sparkml.objects.Document;
import hpi.sparkml.objects.LabeledDocument;

import java.util.List;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.ml.Pipeline;
import org.apache.spark.ml.PipelineModel;
import org.apache.spark.ml.PipelineStage;
import org.apache.spark.ml.classification.LogisticRegression;
import org.apache.spark.ml.feature.HashingTF;
import org.apache.spark.ml.feature.Tokenizer;
import org.apache.spark.sql.api.java.Row;
import org.apache.spark.sql.api.java.JavaSQLContext;
import org.apache.spark.sql.api.java.JavaSchemaRDD;

import com.google.common.collect.Lists;

/**
 * Hello world!
 *{@link Document}
 */
public class App 
{
    @SuppressWarnings("deprecation")
	public static void main( String[] args )
    {
    	SparkConf sparkConf = new SparkConf().setAppName("JavaLR").setMaster("local");
    	JavaSparkContext sc = new JavaSparkContext(sparkConf);
    	JavaSQLContext jsql = new JavaSQLContext(sc);
    	
    	List<LabeledDocument> localTraining = Lists.newArrayList(
    		      new LabeledDocument(0L, "a b c d e spark", 1.0),
    		      new LabeledDocument(1L, "b d", 0.0),
    		      new LabeledDocument(2L, "spark f g h", 1.0),
    		      new LabeledDocument(3L, "hadoop mapreduce", 0.0));
    	
    	JavaSchemaRDD training =
    		      jsql.applySchema(sc.parallelize(localTraining), LabeledDocument.class);
    
    	Tokenizer tokenizer = new Tokenizer()
        .setInputCol("text")
        .setOutputCol("words");
      HashingTF hashingTF = new HashingTF()
        .setNumFeatures(1000)
        .setInputCol(tokenizer.getOutputCol())
        .setOutputCol("features");
      LogisticRegression lr = new LogisticRegression()      //<----logistic regression. 
        .setMaxIter(10)
        .setRegParam(0.01);
      Pipeline pipeline = new Pipeline()
        .setStages(new PipelineStage[] {tokenizer, hashingTF, lr});
      
      PipelineModel model = pipeline.fit(training);
      
      System.out.println("train done...");
      
      List<Document> localTest = Lists.newArrayList(
    	      new Document(4L, "spark i j k"),
    	      new Document(5L, "l m n"),
    	      new Document(6L, "mapreduce spark"),
    	      new Document(7L, "apache hadoop"));
    	    JavaSchemaRDD test = jsql.applySchema(sc.parallelize(localTest), Document.class);
    	    
    	  
    	 // Make predictions on test documents.
    	    model.transform(test).registerAsTable("prediction");
    	    JavaSchemaRDD predictions = jsql.sql("SELECT id, text, score, prediction FROM prediction");
    	    for (Row r: predictions.collect()) {
    	      System.out.println("(" + r.get(0) + ", " + r.get(1) + ") --> score=" + r.get(2)
    	          + ", prediction=" + r.get(3));
    	    }
    	
    	
    	System.out.println("system finished...");
    }
    
    
}
