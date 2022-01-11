import java.util.concurrent.Callable;

public class API implements Callable<String> {

    @Override
    public String call() {
        return response;
    }
    
    private static final String response = "This is the call response";
}
