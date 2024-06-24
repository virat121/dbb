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

    // Define partitions as lists of tuples
    val partition1 = Seq(("Ball", 10), ("Ribbon", 50), ("Box", 20))
    val partition2 = Seq(("Pen", 5), ("Book", 8))
    val partition3 = Seq(("Dairy", 4), ("Pin", 20))

    // Create an RDD with the specified partitioning
    val itemsRdd: RDD[(String, Int)] = sc.parallelize(partition1 ++ partition2 ++ partition3, 3)

    // i. Find how many partitions are created for the collection
    val numPartitions = itemsRdd.getNumPartitions
    println(s"Number of partitions: $numPartitions")

    // ii. Display the content of the RDD
    println("Contents of the RDD:")
    itemsRdd.collect().foreach(println)

    // iii. Display the content of each partition separately
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
