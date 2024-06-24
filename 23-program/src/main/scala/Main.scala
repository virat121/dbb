import org.apache.spark.{SparkConf, SparkContext}

object RDDFromCollection {
  def main(args: Array[String]): Unit = {
    // Step 1: Create SparkContext
    val conf = new SparkConf().setAppName("RDDFromCollection").setMaster("local[*]")
    val sc = new SparkContext(conf)
    
    // Step 2: Create the collection
    val data = Array(11, 34, 45, 67, 3, 4, 90)
    
    // Step 3: Create RDD from the collection with 3 partitions
    val rdd = sc.parallelize(data, 3)
    
    // Print RDD partitions
    println("RDD partitions:")
    rdd.glom().collect().foreach(arr => println(arr.mkString(", ")))
    
    // Step 4: Use mapPartitionsWithIndex to return the content of each partition along with partition index
    // and apply a function that increments each element by 1
    val indexedRdd = rdd.mapPartitionsWithIndex((index, iter) => {
      iter.map(element => (index, element + 1)).toArray.iterator
    })
    
    // Collect and print the results
    val result = indexedRdd.collect()
    println("Content of each partition with incremented values:")
    result.foreach { case (index, value) =>
      println(s"Partition $index: $value")
    }
    
    // Stop SparkContext
    sc.stop()
  }
}
