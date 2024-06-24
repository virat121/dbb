import org.apache.spark.{SparkConf, SparkContext}
import org.apache.spark.rdd.RDD

object ItemRDD {
  def main(args: Array[String]): Unit = {
    val conf = new SparkConf().setAppName("ItemRDD").setMaster("local[*]")
    val sc = new SparkContext(conf)

    // Define the Item collection as a Map
    val items = Map(
      "Ball" -> 10,
      "Ribbon" -> 50,
      "Box" -> 20,
      "Pen" -> 5,
      "Book" -> 8,
      "Dairy" -> 4,
      "Pin" -> 20
    )

    // Create an RDD from the Item collection
    val itemsRdd: RDD[(String, Int)] = sc.parallelize(items.toSeq)

    // i. Find how many partitions are created for the collection
    val numPartitions = itemsRdd.getNumPartitions
    println(s"Number of partitions: $numPartitions")

    // ii. Display the content of the RDD
    println("Contents of the RDD:")
    itemsRdd.collect().foreach(println)

    // Display the content of each partition separately
    println("Contents of each partition:")
    itemsRdd.mapPartitionsWithIndex((index, iterator) => {
      iterator.map(item => (index, item))
    }).collect().foreach {
      case (partitionIndex, item) =>
        println(s"Partition $partitionIndex: $item")
    }

    // Stop the SparkContext
    sc.stop()
  }
}
