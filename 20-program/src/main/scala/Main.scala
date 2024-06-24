import org.apache.spark.sql.SparkSession

object AverageMarks {
  def main(args: Array[String]): Unit = {
    val spark = SparkSession.builder()
      .appName("AverageMarks")
      .master("local[*]")
      .getOrCreate()

    // Sample input data
    val data = Array(
      ("Joe", "Maths", 83),
      ("Joe", "Physics", 74),
      ("Joe", "Chemistry", 91),
      ("Joe", "Biology", 82),
      ("Nik", "Maths", 69),
      ("Nik", "Physics", 62),
      ("Nik", "Chemistry", 97),
      ("Nik", "Biology", 80)
    )

    // Convert data to PairRDD (student, (subject, marks))
    val pairRDD = spark.sparkContext.parallelize(data).map { case (student, subject, marks) =>
      (student, (subject, marks))
    }

    // Use combineByKey to calculate sum and count
    val sumCount = pairRDD.combineByKey(
      // createCombiner: initialize the accumulator (marks, count) for each key
      (subjectMarks: (String, Int)) => (subjectMarks._2, 1),

      // mergeValue: merge a new value (marks) into the accumulator
      (acc: (Int, Int), subjectMarks: (String, Int)) => (acc._1 + subjectMarks._2, acc._2 + 1),

      // mergeCombiners: merge accumulators from different partitions
      (acc1: (Int, Int), acc2: (Int, Int)) => (acc1._1 + acc2._1, acc1._2 + acc2._2)
    )

    // Calculate average
    val averageMarks = sumCount.mapValues { case (sum, count) =>
      sum.toDouble / count
    }

    // Collect the results and print
    val result = averageMarks.collect()
    result.foreach { case (student, average) =>
      println(s"Student: $student, Average Marks: $average")
    }

    spark.stop()
  }
}
