package murray.software1project.Model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * Houses the inventories for parts and products.
 */
public class Inventory {
    /**
     * Keeps track of the last ID assigned so that a new, unique one is generated for the next part or product added.
     */

    private static ObservableList<Part> allParts = FXCollections.observableArrayList();
    private static ObservableList<Product> allProducts = FXCollections.observableArrayList();

    /**
     * Adds the parts to the inventory. Displays them on the Parts table on the main form.
     * @param newPart
     */
    public static void addPart(Part newPart) {
        allParts.add(newPart);

    }

    /**
     * Adds product to Inventory, displays it on the Products table on the main form.
     * @param newProduct
     */
    public static void addProduct(Product newProduct) {
        allProducts.add(newProduct);

    }

    /**
     * Takes integer assigned as PartID and returns a part that matches said ID. Returns null when no matching ID is found.
     * @param PartID
     * @return
     */
    public static Part lookupPart(int PartID) {
        if (!allParts.isEmpty()) {
            for (int i = 0; i < allParts.size(); i++) {
                if (allParts.get(i).getId() == PartID) return allParts.get(i);
            }
        }
        return null;
    }

    /**
     * Takes integer assigned as Product ID and returns the product that matches that ID. Returns null when matching ID isn't found.
     * @param productID
     * @return
     */
    public static Product lookupProduct(int productID) {
        if (!allProducts.isEmpty()) {
            for (int i = 0; i < allProducts.size(); i++) {
                if (allProducts.get(i).getId() == productID) return allProducts.get(i);
            }
        }
        return null;
    }

    /**
     * Allows you to search for a part in the table using either the name or ID.
     * @param partNameOrId
     * @return
     */
    public static ObservableList<Part> lookupPart(String partNameOrId) {
        ObservableList<Part> matchingParts = FXCollections.observableArrayList();
        for (Part part : allParts) {
            if (part.getName().contains(partNameOrId) || Integer.toString(part.getId()).equals(partNameOrId)) {
                matchingParts.add(part);
            }
        }
        return matchingParts;
    }


    /**
     * Allows you to search for a product in the table using either the name or ID.
     * @param productNameOrId
     * @return
     */
    public static ObservableList<Product> lookupProduct(String productNameOrId) {
        ObservableList<Product> matchingProducts = FXCollections.observableArrayList();
        for (Product product : allProducts) {
            if (product.getName().contains(productNameOrId) || Integer.toString(product.getId()).equals(productNameOrId)) {
                matchingProducts.add(product);
            }
        }
        return matchingProducts;
    }

    /**
     * Allows Products on table to be modified.
     * @param index
     * @param selectedProducts
     */
    public static void updateProduct(int index, Product selectedProducts) {
        allProducts.set(index, selectedProducts);
    }

    /**
     * Allows parts on table to be modified.
     * @param index
     * @param selectedPart
     */
    public static void updatePart(int index, Part selectedPart) {
        allParts.set(index, selectedPart);
    }

    /**
     * Allows products to be deleted from table.
     * @param selectedProducts
     * @return
     */
    public static boolean deleteProduct(Product selectedProducts) {
        return allProducts.remove(selectedProducts);
    }

    /**
     * Allows for a part to be deleted from the table.
     * @param selectedParts
     * @return
     */
    public static boolean deletePart(Part selectedParts) {
        return allParts.remove(selectedParts);
    }


    public static ObservableList<Part> getallParts() {
        return allParts;
    }

    public static ObservableList<Product> getallProducts() {
        return allProducts;
    }
}