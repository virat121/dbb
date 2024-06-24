import org.apache.spark.{SparkConf, SparkContext}
import org.apache.spark.rdd.RDD

object CombineByKeyExample {
  def main(args: Array[String]): Unit = {
    val conf = new SparkConf().setAppName("CombineByKeyExample").setMaster("local[*]")
    val sc = new SparkContext(conf)

    // Define the initial collection
    val items = Seq(
      ("coffee", 2),
      ("cappuccino", 5),
      ("tea", 3),
      ("coffee", 10),
      ("cappuccino", 15)
    )

    // Create an RDD from the collection
    val itemsRdd: RDD[(String, Int)] = sc.parallelize(items)

    // Use combineByKey to combine values with the same key
    val combinedRdd = itemsRdd.combineByKey(
      (value: Int) => value,       // createCombiner
      (acc: Int, value: Int) => acc + value,  // mergeValue
      (acc1: Int, acc2: Int) => acc1 + acc2   // mergeCombiners
    )

    // Collect and print the results
    val combinedResults = combinedRdd.collect()
    combinedResults.foreach(println)

    // Stop the SparkContext
    sc.stop()
  }
}
