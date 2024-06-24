import org.apache.spark.{SparkConf, SparkContext}
import org.apache.spark.rdd.RDD

object WordCount {
  def main(args: Array[String]): Unit = {
    val conf = new SparkConf().setAppName("WordCount").setMaster("local[*]")
    val sc = new SparkContext(conf)

    // Path to the text file
    val pathToFile = "words.txt"

    // Read the text file and create an RDD of lines
    val linesRdd = sc.textFile(pathToFile)

    // Split each line into words and flatten the result
    val wordsRdd = linesRdd.flatMap(_.split("\\s+"))

    // Map each word to a tuple (word, 1) for counting
    val wordCountRdd = wordsRdd.map(word => (word, 1))

    // Reduce by key to get the count of each word
    val wordCounts = wordCountRdd.reduceByKey(_ + _)

    // i) Count the number of occurrences of each word
    println("Word counts:")
    wordCounts.collect().foreach(println)

    // ii) Arrange the word count in ascending order based on the key
    val sortedWordCounts = wordCounts.sortByKey()
    println("Word counts sorted by key:")
    sortedWordCounts.collect().foreach(println)

    // iii) Display the words that begin with ‘s’
    val wordsStartingWithS = wordCounts.filter { case (word, count) => word.startsWith("s") }
    println("Words starting with 's':")
    wordsStartingWithS.collect().foreach(println)

    // Stop the SparkContext
    sc.stop()
  }
}
