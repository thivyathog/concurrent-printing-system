import utils.Utilities;

import java.util.Random;

public class Student extends Thread{
    protected String studentName;
    protected Printer printer;
    protected String documentName;
    protected ThreadGroup threadGroup;

    public Student(String name, ThreadGroup threadGroup, Printer printer) {
               super(threadGroup, name);
               this.printer = printer;
               this.studentName=name;
               this.threadGroup=threadGroup;
           }

           @Override
           public void run() {
               for(int i=1 ; i<6 ;i++){ //5times
                   documentName = "doc_id_" + i ;

                   Document document= new Document(this.studentName,documentName,generateRandomPageCount());
                   Utilities.printLogs(Utilities.ProcessLogger.STUDENT, this.getName() + " generated Document to" +
                           " print: " + document, Utilities.ProcessLogger.INFO);

                   Utilities.printLogs(Utilities.ProcessLogger.STUDENT,"[" + this.getName() + "] has requested to" +
                           " print the document: " + document, Utilities.ProcessLogger.INFO);

                   if(document.getNumberOfPages()!=0)
                   printer.printDocument(document);
                   try {
                       sleep((int) (Math.random() * 1000));
                   } catch (InterruptedException e) {

                       Utilities.printLogs(Utilities.ProcessLogger.STUDENT,e.toString(),Utilities.ProcessLogger.ERROR);
                   }
       }

    }

    /**
     * Generates a random page count between 1 to 20
     * @return Random page count
     */
    private int generateRandomPageCount() {
        return new Random().nextInt(19) + 1;
    }
}
