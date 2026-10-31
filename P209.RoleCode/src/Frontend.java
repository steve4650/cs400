public class Frontend implements FrontendInterface {

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
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException(
        "Unimplemented method 'createHTMLShortestPathResponse'");
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
