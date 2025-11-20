import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ucu.apps.CachedDocument;
import ucu.apps.Document;
import ucu.apps.TimedDocument;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

import static org.junit.jupiter.api.Assertions.*;

class DecoratorTest {
    private static class FakeDocument implements Document {
        private int parseCount = 0;
        private final String text;

        FakeDocument(String text) {
            this.text = text;
        }

        @Override
        public String parse(String path) {
            parseCount++;
            return text;
        }

        int getParseCount() {
            return parseCount;
        }
    }

    private static final String DB_URL = "jdbc:sqlite:db";

    @BeforeEach
    void initDb() throws Exception {
        try (Connection conn = DriverManager.getConnection(DB_URL);
             Statement stmt = conn.createStatement()) {

            stmt.executeUpdate("DROP TABLE IF EXISTS cache");
            stmt.executeUpdate("CREATE TABLE cache (" +
                    "path TEXT PRIMARY KEY," +
                    "parsed_string TEXT NOT NULL" +
                    ")");
        }
    }

    @Test
    void cachedDocumentUsesCacheAfterFirstCall() {
        FakeDocument doc = new FakeDocument("Hello cache");
        CachedDocument cached = new CachedDocument(doc);

        String path = "grogu.png";

        String first = cached.parse(path);
        String second = cached.parse(path);

        assertEquals("Hello cache", first);
        assertEquals("Hello cache", second);
        assertEquals(1, doc.getParseCount(),
                "CachedDocument should call wrapped document only once and then use SQLite cache");
    }

    @Test
    void timedDocumentDelegatesAndMeasuresTime() {
        FakeDocument doc = new FakeDocument("Timed");
        TimedDocument timed = new TimedDocument(doc);

        String a = timed.parse("grogu.png");
        String b = timed.parse("grogu.png");

        assertEquals("Timed", a);
        assertEquals("Timed", b);
        assertEquals(2, doc.getParseCount(), "TimedDocument must just delegate to wrapped document");
        assertTrue(timed.getLastDurationMillis() >= 0);
    }
}
