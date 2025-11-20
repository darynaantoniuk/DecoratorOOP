package ucu.apps;


public abstract class AbstractDocumentDecorator implements Document {
    protected final Document document;

        protected AbstractDocumentDecorator(Document document) {
            this.document = document;
        }

        @Override
        public String parse(String path) {
            return document.parse(path);
        }
    }