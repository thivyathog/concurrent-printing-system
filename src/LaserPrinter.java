import utils.Utilities;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import static utils.Utilities.ProcessLogger.*;

public class LaserPrinter implements ServicePrinter {

    private String printerID;
    private int paper_level;
    private int toner_level;
    private int printedDocuments;
    private int printedPages;
    private int minToner;
    private ThreadGroup studentThreadGroup;
    // reentrant Lock
    private Lock resourceLock = new ReentrantLock(true); //fairness Enabled
    private Condition printerCondition;

    @Override
    public String toString() {
        return "Printer{" +
                "printerID='" + printerID + '\'' +
                ", paper_level=" + paper_level +
                ", toner_level=" + toner_level +
                ", printedDocuments=" + printedDocuments +
                '}';
    }

    public LaserPrinter(String printerID, ThreadGroup threadGroup) {
        this.printerID = printerID;
        this.toner_level = Full_Toner_Level;
        this.paper_level = Full_Paper_Tray;
        this.printedDocuments = 0;
        this.studentThreadGroup = threadGroup;
        this.minToner = Minimum_Toner_Level;
        this.printerCondition = resourceLock.newCondition();
        this.printedPages = 0;

    }

    public void printDocument(Document document) {
        this.resourceLock.lock();

        try {
            Utilities.printLogs(Utilities.ProcessLogger.PRINTER, this.toString(), Utilities.ProcessLogger.INFO);

            int numberOfPages = document.getNumberOfPages();

            while (paper_level < numberOfPages || toner_level < numberOfPages) { //avoid deadlock
                String message = "";
                if (toner_level < numberOfPages)
                    message += "toner not available";
                if (paper_level < numberOfPages) {
                    if (message.isEmpty()) {
                        message += "paper not available";
                    } else {
                        message += " and paper not available";
                    }

                }

                if (!message.isEmpty()) {
                    message += ",Waiting until it has been replaced";
                    Utilities.printLogs(Utilities.ProcessLogger.PRINTER, message, Utilities.ProcessLogger.WARN);
                }
                printerCondition.await(); // Insufficient resources. Wait till it's refilled.

            }
            // Print document if resources are sufficient
            if (paper_level > numberOfPages && toner_level > numberOfPages) { //avoid deadlock

                this.paper_level -= numberOfPages;
                this.toner_level -= numberOfPages;
                this.printedPages += numberOfPages;
                printedDocuments++;
                Utilities.printLogs(Utilities.ProcessLogger.PRINTER, "Printed Document : " + document,
                        Utilities.ProcessLogger.INFO);
                Utilities.printLogs(Utilities.ProcessLogger.PRINTER, this.toString(), Utilities.ProcessLogger.INFO);
            }

            this.printerCondition.signalAll();


        } catch (InterruptedException e) {
            Utilities.printLogs(Utilities.ProcessLogger.PRINTER, e.toString(), Utilities.ProcessLogger.ERROR);
        } finally {
            Utilities.printLogs(Utilities.ProcessLogger.STUDENT, "Printer current status: " + this.toString(),
                    Utilities.ProcessLogger.INFO);
            //Utilities.printLogs(PRINTER, "Printer is released by [" + document.getStudentName() + "]",
            // Utilities.ProcessLogger.INFO);
            this.resourceLock.unlock();
        }

    }

    public void refillPaper() {

        this.resourceLock.lock();
        //Utilities.printLogs(PRINTER, "Printer is acquired by [" + PAPER_TECHNICIAN + "]", Utilities.ProcessLogger.INFO);

        try {
            while (willPaperOverflow()) { //paper level + sheets per pack> full paper tray - not overflow

                // Check if printer has finished serving all the threads in students Thread Group
                if (studentThreadGroup.activeCount() > 0) {

                    String loggingMessage = "Paper level has not reached the minimum level to be refilled. " +
                            "Waiting to check again in 5 seconds";

                    Utilities.printLogs(Utilities.ProcessLogger.PRINTER, this.toString(), INFO);
                    Utilities.printLogs(Utilities.ProcessLogger.PRINTER, loggingMessage, INFO);

                    printerCondition.await(5, TimeUnit.SECONDS);

                } else {

                    Utilities.printLogs(Utilities.ProcessLogger.PRINTER, "Usage of the Printer has been concluded" +
                            ", no need to replace Paper", Utilities.ProcessLogger.INFO);
                    break;
                }
            }

            if (paper_level + SheetsPerPack <= Full_Paper_Tray) {
                Utilities.printLogs(Utilities.ProcessLogger.PRINTER, "Paper level has reduced below the specified" +
                        " Level", WARN);
                this.paper_level += SheetsPerPack;
                Utilities.printLogs(Utilities.ProcessLogger.PRINTER, "Refilled with a pack of papers : " +
                        this.toString(), Utilities.ProcessLogger.INFO);

            }

            this.printerCondition.signalAll();

        } catch (InterruptedException e) {
            Utilities.printLogs(Utilities.ProcessLogger.PRINTER, e.toString(), Utilities.ProcessLogger.ERROR);
        } finally {
            Utilities.printLogs(PAPER_TECHNICIAN, "Printer current status: " + this.toString(),
                    Utilities.ProcessLogger.INFO);
            //Utilities.printLogs(PRINTER, "Printer is released by [" + PAPER_TECHNICIAN + "]", Utilities.ProcessLogger.INFO);
            this.resourceLock.unlock();
        }
    }

    public void replaceTonerCartridge() {
        this.resourceLock.lock();
        //Utilities.printLogs(PRINTER, "Printer is acquired by [" + TONER_TECHNICIAN + "]", Utilities.ProcessLogger.INFO);
        try {
            while (toner_level > minToner) {

                // Check if printer has finished serving all the threads in students Thread Group
                if (studentThreadGroup.activeCount() > 0) {

                    String loggingMessage = "Toner has not reached minimum level. Waiting to check again in 5 seconds";

                    Utilities.printLogs(Utilities.ProcessLogger.PRINTER, this.toString(), INFO);

                    Utilities.printLogs(Utilities.ProcessLogger.PRINTER, loggingMessage, INFO);

                    printerCondition.await(5, TimeUnit.SECONDS);
                } else {

                    Utilities.printLogs(Utilities.ProcessLogger.PRINTER, "Usage of the Printer has been " +
                            "concluded, no need to replace TONER", Utilities.ProcessLogger.INFO);
                    break;
                }
            }


            if (toner_level < minToner) {
                Utilities.printLogs(Utilities.ProcessLogger.PRINTER, "Toner has reached below the minimum level", WARN);
                this.toner_level = Full_Toner_Level;
                Utilities.printLogs(Utilities.ProcessLogger.PRINTER, "Toner has been replaced : " + this.toString(),
                        Utilities.ProcessLogger.INFO);

            }

            this.printerCondition.signalAll();

        } catch (InterruptedException e) {
            Utilities.printLogs(Utilities.ProcessLogger.PRINTER, e.toString(), Utilities.ProcessLogger.ERROR);
        } finally {
            Utilities.printLogs(TONER_TECHNICIAN, "Printer status: " + this.toString(), Utilities.ProcessLogger.INFO);
            //Utilities.printLogs(PRINTER, "Printer is released by [" + TONER_TECHNICIAN + "]", Utilities.ProcessLogger.INFO);
            this.resourceLock.unlock();
        }
    }


    private boolean willPaperOverflow() {
        return this.paper_level + SheetsPerPack > Full_Paper_Tray;
    }

    public int getPrintedPages() {
        return printedPages;
    }

}

