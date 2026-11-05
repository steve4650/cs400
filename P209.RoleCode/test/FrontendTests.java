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
}
