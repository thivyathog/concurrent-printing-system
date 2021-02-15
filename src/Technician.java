/**
 * Represents the abstract technician class
 */
public abstract class  Technician extends Thread{

    private String techName;
    private LaserPrinter printer;
    private ThreadGroup threadGroupUser;


    public Technician(String name, ThreadGroup threadGroup, Printer printer) {
        super(threadGroup,name);
        this.threadGroupUser = threadGroup;
        this.techName = name;
        this.printer = (LaserPrinter) printer;
    }

    public String getTechName() {
        return techName;
    }

    public LaserPrinter getPrinter() {
        return this.printer;
    }

    public ThreadGroup getThreadGroupUser() {
        return threadGroupUser;
    }
}
