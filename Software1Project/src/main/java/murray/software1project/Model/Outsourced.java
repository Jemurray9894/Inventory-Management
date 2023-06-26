package murray.software1project.Model;

public class Outsourced extends Part {

    /**
     * Creates Outsourced parts.
     */
    private String companyName;
    public Outsourced(int id, String name, double price, int stock, int min, int max, String companyName) {
        super(id, name, price, stock, min, max);
        this.companyName = companyName;
    }

    /**
     * Company Name
     * @return
     */
    public String getCompanyName() {
        return companyName;
    }

    /**
     * Sets Company Name
     * @param companyName
     */
    public void setCompanyName(String companyName){
        this.companyName = companyName;
    }
}
