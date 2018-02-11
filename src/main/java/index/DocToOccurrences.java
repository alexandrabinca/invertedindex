package index;

import java.util.concurrent.atomic.AtomicLong;

class DocToOccurrences implements Comparable<DocToOccurrences>{
    private long documentId;
    private AtomicLong wordOccurrences;

    public DocToOccurrences(long documentId, long wordOccurrences) {
        this.documentId = documentId;
        this.wordOccurrences = new AtomicLong(wordOccurrences);
    }

    @Override
    public int compareTo(DocToOccurrences docToOccurrences) {
        return Long.compare(docToOccurrences.wordOccurrences.get(), this.wordOccurrences.get());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        DocToOccurrences docToOccurrences = (DocToOccurrences) o;

        return documentId == docToOccurrences.documentId;
    }

    @Override
    public int hashCode() {
        return Long.hashCode(documentId);
    }

    public void addToCount(long delta) {
        this.wordOccurrences.addAndGet(delta);
    }

    public long getDocumentId() {
        return documentId;
    }
}
