import org.apache.spark.SparkConf
import org.apache.spark.streaming.{StreamingContext, Seconds}


object SocketWordCount {
  def main(args: Array[String]): Unit = {
    // Step 1: Setup Spark configuration and StreamingContext
    val conf = new SparkConf().setAppName("SocketWordCount").setMaster("local[*]")
    val ssc = new StreamingContext(conf, Seconds(5)) // Batch interval of 5 seconds
    
    // Step 2: Create a DStream that connects to localhost:4444
    val lines = ssc.socketTextStream("localhost", 4444)
    
    // Step 3: Perform word count
    val wordCounts = lines
      .flatMap(_.split(" "))
      .map(word => (word, 1))
      .reduceByKey(_ + _)
    
    // Step 4: Print the word counts
    wordCounts.print()
    
    // Step 5: Start the streaming context
    ssc.start()
    ssc.awaitTermination()
  }
}
