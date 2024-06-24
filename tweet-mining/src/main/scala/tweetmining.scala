import org.apache.spark.{SparkConf, SparkContext}
import com.google.gson.Gson

object tweetmining {

  // Create the Spark configuration and Spark context
  val conf = new SparkConf()
    .setAppName("User mining")
    .setMaster("local[*]")

  val sc = new SparkContext(conf)

  var pathToFile = ""

  def main(args: Array[String]): Unit = {

    if (args.length != 1) {
      println("Usage: tweetmining <path_to_json_file>")
      System.exit(1)
    }

    pathToFile = args(0)

    // Load tweets from JSON file into RDD
    val tweets = sc.textFile(pathToFile).mapPartitions(TweetUtils.parseFromJson(_))

    // Group tweets by user
    val tweetsByUser = tweets.map(tweet => (tweet.user, tweet)).groupByKey()

    // Count number of tweets by each user
    val numTweetsByUser = tweetsByUser.mapValues(_.size)

    // Sort users by the number of tweets in descending order
    val sortedUsersByNumTweets = numTweetsByUser.sortBy(_._2, ascending = false)

    // Print top 10 tweeters
    sortedUsersByNumTweets.take(10).foreach(println)
  }
}