import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class FrontendTests {

  /*
   * Test that the frontend is building HTML snippets with the expected elements.
   */
  @Test
  public void roleTest1() {
    Frontend frontend = new Frontend();
    Assertions.assertTrue(frontend.createHTMLReachableWithinPrompt().contains("id=\"from\""));
    Assertions.assertTrue(frontend.createHTMLReachableWithinPrompt().contains("id=\"time\""));
    Assertions.assertTrue(frontend.createHTMLReachableWithinPrompt().contains("button"));
  }

  /*
   * Test that a shortest path response between two particular nodes contains HTML elements
   * in the expected format with the correct start, end, and time of travel information.
   */
  @Test
  public void roleTest2() {
    Frontend frontend = new Frontend();
    String result =
        frontend.createHTMLShortestPathResponse("Union South", "Computer Sciences and Statistics");
    Assertions.assertTrue(result.contains("Start Location: Union South"));
    Assertions.assertTrue(result.contains("End Location: Computer Sciences and Statistics"));
    Assertions.assertTrue(result.contains("Total Time: 3.0 minutes"));
  }

  /*
   * Test that a reachable within response between two particular nodes contains HTML elements
   * and mentions the correct locations.
   */
  @Test
  public void roleTest3() {
    Frontend frontend = new Frontend();
    String result = frontend.createHTMLReachableWithinResponse("Union South", 10.0);
    Assertions.assertTrue(result.contains("</p>"));
    Assertions.assertTrue(result.contains("Computer Sciences and Statistics"));
    Assertions.assertTrue(result.contains("Weeks Hall for Geological Sciences"));
  }
}
