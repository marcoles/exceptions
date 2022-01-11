import java.util.concurrent.Callable;
import java.util.function.Consumer;
import io.grpc.StatusRuntimeException;

public class Main {
    private static final int MAX_REVERIFY = 10;
    private static final int TIMEOUT = 20;
    public static void main (String[] args) {
    }
    
    private static <ResponseType> ResponseType callAndVerify(
        Callable<ResponseType> supplier, Consumer<ResponseType> verifier) throws Exception {
        for (int i = 1; i <= MAX_REVERIFY; i++) {
            try {
                ResponseType response = supplier.call();
                verifier.accept(response);
                return response;
            } catch (StatusRuntimeException exception) {
                if (exception.getMessage().contains("INTEGRATION_FILTER_KEY")) {
                    throw exception;
                } else {
                    if (MAX_REVERIFY == i) {
                        throw new Exception();
                    } else {
                        Thread.sleep(TIMEOUT * 1000);
                    }
                }
            } catch (AssertionError exception) {
                if (MAX_REVERIFY == i) {
                    throw exception;
                } else {
                    Thread.sleep(TIMEOUT * 1000);
                }
            }
        }
        throw new RuntimeException("There is an error in the algorithm");
    }
}
