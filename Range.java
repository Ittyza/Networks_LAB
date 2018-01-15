/**
 * Describes a simple range, with a start, an end, and a length
 */
class Range {
    private Long start;
    private Long end;
    public boolean isWritten;
    public boolean inWorker;
    
    public int index;

    Range(Long start, Long end) {
        this.start = start;
        this.end = end;
        this.isWritten = false;
    }

    Long getStart() {
        return start;
    }

    Long getEnd() {
        return end;
    }

    Long getLength() {
        return end - start + 1;
    }
}
