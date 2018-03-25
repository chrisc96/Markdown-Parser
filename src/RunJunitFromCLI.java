import org.junit.runner.JUnitCore;
import org.junit.runner.Request;
import org.junit.runner.Result;

public class RunJunitFromCLI {
    public static void main(String... args) throws ClassNotFoundException {
        String[] classAndMethod = args[0].split("#");
        Request request = Request.method(Class.forName(classAndMethod[0]),
                classAndMethod[1]);

        Result result = new JUnitCore().run(request);
        if (result.wasSuccessful()) {
            System.out.println(classAndMethod[1] + "() was successful");
        }
        else {
            System.out.println(classAndMethod[1] + "() failed");
        }
        System.exit(result.wasSuccessful() ? 0 : 1);
    }
}