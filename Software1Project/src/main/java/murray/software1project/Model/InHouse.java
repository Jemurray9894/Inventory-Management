package murray.software1project.Model;

public class InHouse extends Part {

    /**
     * Creates the In-house parts.
     */
    private int machineID;
    public InHouse(int id, String name, double price, int stock, int min, int max, int machineID) {
        super(id, name, price, stock, min, max);
        this.machineID = machineID;
    }

    /**
     * Machine ID
     * @return
     */
    public int getMachineID() {
        return machineID;
    }

    /**
     * Sets Machine ID
     * @param machineID
     */
    public void setMachineID(int machineID) {
        this.machineID = machineID;
    }

}
