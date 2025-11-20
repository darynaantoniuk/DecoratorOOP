package ucu.apps;

public class Main {
    public static void main(String[] args) {
        String path = "grogu.png";

        Document smart = new SmartDocument();

        Document cached = new CachedDocument(smart);
        Document timed = new TimedDocument(cached);

        String text1 = timed.parse(path);
        System.out.println("First parse length = " + text1.length());

        String text2 = timed.parse(path);
        System.out.println("Second parse length = " + text2.length());

        if (timed instanceof TimedDocument td) {
            System.out.println("Last parse duration (ms): " + td.getLastDurationMillis());
        }
    }
}
