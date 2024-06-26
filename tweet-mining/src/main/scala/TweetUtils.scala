import com.google.gson.Gson

object TweetUtils {
  case class Tweet(id: String, user: String, text: String, place: String, country: String)

  def parseFromJson(lines: Iterator[String]): Iterator[Tweet] = {
    val gson = new Gson()
    lines.map(line => gson.fromJson(line, classOf[Tweet]))
  }
}
