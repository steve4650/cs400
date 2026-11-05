import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class FrontendTests {
   
    @Test
    public void roleTest1(){
        Frontend frontend = new Frontend();
        System.out.println(frontend.createHTMLShortestPathPrompt());
        Assertions.assertTrue(frontend.createHTMLReachableWithinPrompt().contains("start"));
    }
}
