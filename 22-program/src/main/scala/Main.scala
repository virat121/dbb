import org.apache.spark.sql.SparkSession

object AverageCalculator {
  def main(args: Array[String]): Unit = {
    // Step 1: Create SparkSession
    val spark = SparkSession.builder()
      .appName("AverageCalculator")
      .master("local[*]")
      .getOrCreate()

    // Step 2: Read text file into an RDD
    val textFilePath = "input.csv" // Replace with your actual CSV file path
    val rdd = spark.sparkContext.textFile(textFilePath)
      .flatMap(line => line.split(",")) // Split each line by comma to get individual numbers
      .map(_.trim.toInt) // Convert each string to integer

    // Step 3: Calculate average using Spark aggregation functions
    val count = rdd.count()
    val sum = rdd.reduce(_ + _)
    val average = sum.toDouble / count

    // Step 4: Display the result
    println(s"Average of the items: $average")

    // Stop SparkSession
    spark.stop()
  }
}
