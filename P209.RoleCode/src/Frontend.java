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
    List<String> path = this.backend.findShortestPathLocations(start, end);
    if (path.size() == 0) {
      return "No such path was found by our humble backend...";
    }
    String result =
        "<p id=\"start-label\">Start Location: "
            + start
            + "</p><p id=\"end-label\">End Location: "
            + end
            + "</p><ol>";
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
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException(
        "Unimplemented method 'createHTMLReachableWithinPrompt'");
  }

  @Override
  public String createHTMLReachableWithinResponse(String start, double maxTime) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException(
        "Unimplemented method 'createHTMLReachableWithinResponse'");
  }
}
