import org.apache.spark.{SparkConf, SparkContext}

object WordCount {
  def main(args: Array[String]): Unit = {
    // Step 1: Create SparkContext
    val conf = new SparkConf().setAppName("WordCount").setMaster("local[*]")
    val sc = new SparkContext(conf)
    
    try {
      // Step 2: Path to the text file
      val pathToFile = "text.txt"
      
      // Step 3: Read the text file and create an RDD of lines
      val linesRdd = sc.textFile(pathToFile)
      
      // Step 4: Split each line into words and flatten the result
      val wordsRdd = linesRdd.flatMap(_.split("\\s+"))
      
      // Step 5: Map each word to a tuple (word, 1) for counting
      val wordCountRdd = wordsRdd.map(word => (word, 1))
      
      // Step 6: Reduce by key to get the count of each word
      val wordCounts = wordCountRdd.reduceByKey(_ + _)
      
      // Step 7: Print the word counts
      wordCounts.foreach(println)
    } finally {
      // Step 8: Stop SparkContext
      sc.stop()
    }
  }
}
