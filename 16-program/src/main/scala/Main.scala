import org.apache.spark.SparkConf
import org.apache.spark.SparkContext

object WordCount {
  def main(args: Array[String]): Unit = {
    val pathToFile = "text.txt"
    val conf = new SparkConf().setAppName("WordCount").setMaster("local[*]")
    val sc = new SparkContext(conf)

    try {
      // Step 1: Read text file and split into words
      val wordsRdd = sc.textFile(pathToFile)
                       .flatMap(line => line.split("\\s+")) // Split by any whitespace

      // Step 2: Map each word to (word, 1) for counting
      val wordCountInitRdd = wordsRdd.map(word => (word, 1))

      // Step 3: Reduce by key to get word counts
      val wordCountRdd = wordCountInitRdd.reduceByKey(_ + _)

      // Step 4: Filter words with count > 4
      val highFreqWords = wordCountRdd.filter(_._2 > 4)

      // Step 5: Save results to a file
      highFreqWords.saveAsTextFile("output")

    } finally {
      // Step 6: Stop SparkContext
      sc.stop()
    }
  }
}
