public class Document {

    private final String studentName;
    private final String documentName;
    private final int numberOfPages;

    public Document(String id, String documentName, int numberOfPages) {
        this.documentName = documentName;
        this.studentName = id;
        this.numberOfPages = numberOfPages;
    }

    public String getStudentName() {
        return studentName;
    }

    public String getDocumentName() {
        return documentName;
    }

    public int getNumberOfPages() {
        return numberOfPages;
    }

    @Override
    public String toString() {
        return "Document{" +
                "studentName='" + studentName + '\'' +
                ", documentName='" + documentName + '\'' +
                ", numberOfPages=" + numberOfPages +
                '}';
    }
}
