package ucu.apps;

public class TimedDocument extends AbstractDocumentDecorator {

    private long lastDurationMillis = -1L;

    public TimedDocument(Document delegate) {
        super(delegate);
    }

    @Override
    public String parse(String path) {
        long start = System.nanoTime();
        String result = super.parse(path);
        long end = System.nanoTime();

        lastDurationMillis = (end - start) / 1_000_000L;
        System.out.println("TimedDocument.parse() took " + lastDurationMillis + " ms");

        return result;
    }

    public long getLastDurationMillis() {
        return lastDurationMillis;
    }
}
