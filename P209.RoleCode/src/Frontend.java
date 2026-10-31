import java.util.List;

public class Frontend implements FrontendInterface {

  private BackendInterface backend;

  public Frontend() {
    this.backend = new Backend_Placeholder(new Graph_Placeholder());
  }

  @Override
  public String createHTMLShortestPathPrompt() {
    return """
        <input type="text" id="start" placeholder="Start Location">
        <input type="text" id="end" placeholder="End Location">
        <input type="button" value="Find Shortest Path" id="shortestPathButton">
    """;
  }

  @Override
  public String createHTMLShortestPathResponse(String start, String end) {
    String result =
        "<p id=\"start-label\">Start Location: "
            + start
            + "</p><p id=\"end-label\">End Location: "
            + end
            + "</p>";
    List<String> path = this.backend.findShortestPathLocations(start, end);
    if (path.size() == 0) {
      result += "<p>No such path was found by our humble backend... you are searching for impossible paths.</p>";
      return result;
    }
    result += "<ol>";
    for (String location : path) {
      result += "<li class=\"intermediate-location\">" + location + "</li>";
    }
    result += "</ol>";
    List<Double> times = this.backend.findShortestPathTimes(start, end);
    Double totalTime = 0.0;
    for (Double time : times) {
      totalTime += time;
    }
    result += "<p id=\"total-time-label\">Total Time: " + totalTime + " minutes</p>";
    return result;
  }

  @Override
  public String createHTMLReachableWithinPrompt() {
    return """
        <input type="text" id="from" placeholder="From Location">
        <input type="text" id="time" placeholder="Time radius (minutes)">
        <input type="button" value="Reachable From Within" id="reachableWithinButton">
    """;
  }

  @Override
  public String createHTMLReachableWithinResponse(String start, double maxTime) {
    String result = "<p id=\"start-label\">From Location: " + start + "</p><p id=\"time-label\">Time radius: " + maxTime + " minutes</p>";
    List<String> reachable = this.backend.getReachableWithin(start, maxTime);
    if(reachable.size() == 0) {
      result += "<p>None! You need to broaden your search.</p>";
      return result; 
    }
    result += "<ol>";
    for (String location : reachable) {
      result += "<li class=\"intermediate-location\">" + location + "</li>";
    }
    result += "</ol>";
    return result;
  }
}
