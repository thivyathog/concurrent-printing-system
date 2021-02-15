import utils.Utilities;

import static utils.Utilities.ProcessLogger.*;

/**
 * Represents the composite printing system, that cotains concurrent printer(resource), students, and technicians threads
 */
public class PrintingSystem {

    public static void main(String[] args) {

        //create Thread Groups
        Utilities.printLogs(PRINTING_SYSTEM, "Creating Thread Groups ", Utilities.ProcessLogger.INFO);
        ThreadGroup studentGroup = new ThreadGroup("Student");
        Utilities.printLogs(PRINTING_SYSTEM, " Student Thread Group Created", Utilities.ProcessLogger.INFO);
        ThreadGroup technicianGroup = new ThreadGroup("Technician");
        Utilities.printLogs(PRINTING_SYSTEM, " Technician Thread Group Created", Utilities.ProcessLogger.INFO);

        //create Printer Resource
        LaserPrinter printer = new LaserPrinter("LaserPrinter1120J", studentGroup);
        Utilities.printLogs(PRINTING_SYSTEM, "Initialised PRINTER Resource", Utilities.ProcessLogger.INFO);

        //create 4 student Threads
        Student student1 = new Student("James", studentGroup, printer);
        Utilities.printLogs(PRINTING_SYSTEM, "Initialised STUDENT" + student1.getName(),
                Utilities.ProcessLogger.INFO);
        Student student2 = new Student("Harry", studentGroup, printer);
        Utilities.printLogs(PRINTING_SYSTEM, "Initialised STUDENT" + student2.getName(),
                Utilities.ProcessLogger.INFO);
        Student student3 = new Student("Richie", studentGroup, printer);
        Utilities.printLogs(PRINTING_SYSTEM, "Initialised STUDENT" + student3.getName(),
                Utilities.ProcessLogger.INFO);
        Student student4 = new Student("Shantha", studentGroup, printer);
        Utilities.printLogs(PRINTING_SYSTEM, "Initialised STUDENT" + student4.getName(),
                Utilities.ProcessLogger.INFO);

        //create technician threads
        Technician paperTechnician = new PaperTechnician("Jack", technicianGroup, printer);
        Utilities.printLogs(PRINTING_SYSTEM, "Initialised " +
                PAPER_TECHNICIAN.toString().replace("_", ""), Utilities.ProcessLogger.INFO);
        Technician tonerTechnician = new TonerTechnician("Tom", technicianGroup, printer);
        Utilities.printLogs(PRINTING_SYSTEM, "Initialised " +
                TONER_TECHNICIAN.toString().replace("_", ""), Utilities.ProcessLogger.INFO);

        // Start all the threads
        Utilities.printLogs(PRINTING_SYSTEM, "Starting all initalised Threads", Utilities.ProcessLogger.INFO);

        student1.start();
        Utilities.printLogs(PRINTING_SYSTEM, "Started " + student1.getName() + " STUDENT Thread",
                Utilities.ProcessLogger.INFO);

        student2.start();
        Utilities.printLogs(PRINTING_SYSTEM, "Started " + student2.getName() + " STUDENT Thread",
                Utilities.ProcessLogger.INFO);

        student3.start();
        Utilities.printLogs(PRINTING_SYSTEM, "Started " + student3.getName() + " STUDENT Thread",
                Utilities.ProcessLogger.INFO);

        student4.start();
        Utilities.printLogs(PRINTING_SYSTEM, "Started " + student4.getName() + " STUDENT Thread",
                Utilities.ProcessLogger.INFO);

        paperTechnician.start();
        Utilities.printLogs(PRINTING_SYSTEM, "Started PAPER TECHNICIAN Thread", Utilities.ProcessLogger.INFO);

        Utilities.printLogs(PRINTING_SYSTEM, "Started TONER TECHNICIAN Thread", Utilities.ProcessLogger.INFO);
        tonerTechnician.start();

        try {

            student1.join();
            Utilities.printLogs(PRINTING_SYSTEM, "EXECUTION completed by : " + student1.getName(),
                    Utilities.ProcessLogger.INFO);
            student2.join();
            Utilities.printLogs(PRINTING_SYSTEM, "EXECUTION completed by : " + student2.getName(),
                    Utilities.ProcessLogger.INFO);
            student3.join();
            Utilities.printLogs(PRINTING_SYSTEM, "EXECUTION completed by : " + student3.getName(),
                    Utilities.ProcessLogger.INFO);
            student4.join();
            Utilities.printLogs(PRINTING_SYSTEM, "EXECUTION completed by : " + student4.getName(),
                    Utilities.ProcessLogger.INFO);
            paperTechnician.join();
            Utilities.printLogs(PRINTING_SYSTEM, "EXECUTION completed by : Paper Technician " +
                    paperTechnician.getTechName(), Utilities.ProcessLogger.INFO);
            tonerTechnician.join();
            Utilities.printLogs(PRINTING_SYSTEM, "EXECUTION completed by : Toner Technician " +
                    tonerTechnician.getTechName(), Utilities.ProcessLogger.INFO);

            Utilities.printLogs(PRINTING_SYSTEM, "All Tasks successfully Completed ,Total Number" +
                            " of pages printed : "+ printer.getPrintedPages()+", Final Status of " + printer.toString(),
                    Utilities.ProcessLogger.INFO);


        } catch (InterruptedException e) {
            Utilities.printLogs(Utilities.ProcessLogger.PRINTER, e.toString(), Utilities.ProcessLogger.ERROR);
        }


    }

}
