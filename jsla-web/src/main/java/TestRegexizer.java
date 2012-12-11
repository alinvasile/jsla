import org.jsla.web.context.Regexizer;


public class TestRegexizer {

    /**
     * @param args
     */
    public static void main(String[] args) {
        String url = "/reports/*";
        System.out.println(Regexizer.escape(url));

    }

}
