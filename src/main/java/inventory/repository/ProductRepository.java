package inventory.repository;

import inventory.model.Inventory;
import inventory.model.Part;
import inventory.model.Product;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.*;
import java.util.StringTokenizer;

public class ProductRepository {

    private static String filename = "data/items.txt";
    private Inventory inventory;

    public ProductRepository(Inventory inventory) {
        this.inventory = inventory;
        readProducts();
    }

    public void readProducts() {
        ClassLoader classLoader = PartRepository.class.getClassLoader();
        File file = new File(classLoader.getResource(filename).getFile());

        ObservableList<Product> listP = FXCollections.observableArrayList();
        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(file));
            String line = null;
            while ((line = br.readLine()) != null) {
                Product product = getProductFromString(line);
                if (product != null)
                    listP.add(product);
            }
            br.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        inventory.setProducts(listP);
    }

    public void writeAll() {

        ClassLoader classLoader = PartRepository.class.getClassLoader();
        File file = new File(classLoader.getResource(filename).getFile());

        BufferedWriter bw = null;
        ObservableList<Part> parts = inventory.getAllParts();
        ObservableList<Product> products = inventory.getProducts();

        try {
            bw = new BufferedWriter(new FileWriter(file));
            for (Part p : parts) {
                System.out.println(p.toString());
                bw.write(p.toString());
                bw.newLine();
            }

            for (Product pr : products) {
                String line = pr.toString() + ",";
                ObservableList<Part> list = pr.getAssociatedParts();
                int index = 0;
                while (index < list.size() - 1) {
                    line = line + list.get(index).getPartId() + ":";
                    index++;
                }
                if (index == list.size() - 1)
                    line = line + list.get(index).getPartId();
                bw.write(line);
                bw.newLine();
            }
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void addProduct(Product product) {
        inventory.addProduct(product);
        writeAll();
    }

    public int getAutoProductId() {
        return inventory.getAutoProductId();
    }

    public ObservableList<Product> getAllProducts() {
        return inventory.getProducts();
    }

    public Product lookupProduct(String search) {
        return inventory.lookupProduct(search);
    }

    public void updateProduct(int productIndex, Product product) {
        inventory.updateProduct(productIndex, product);
        writeAll();
    }

    public void deleteProduct(Product product) {
        inventory.removeProduct(product);
        writeAll();
    }
    
    public Inventory getInventory() {
        return inventory;
    }

    public void setInventory(Inventory inventory) {
        this.inventory = inventory;
    }

    private Product getProductFromString(String line) {
        Product product = null;
        if (line == null || line.equals("")) return null;
        StringTokenizer st = new StringTokenizer(line, ",");
        String type = st.nextToken();
        if (type.equals("P")) {
            int id = Integer.parseInt(st.nextToken());
            inventory.setAutoProductId(id);
            String name = st.nextToken();
            double price = Double.parseDouble(st.nextToken());
            int inStock = Integer.parseInt(st.nextToken());
            int minStock = Integer.parseInt(st.nextToken());
            int maxStock = Integer.parseInt(st.nextToken());
            String partIDs = st.nextToken();

            StringTokenizer ids = new StringTokenizer(partIDs, ":");
            ObservableList<Part> list = FXCollections.observableArrayList();
            while (ids.hasMoreTokens()) {
                String idP = ids.nextToken();
                Part part = inventory.lookupPart(idP);
                if (part != null)
                    list.add(part);
            }
            product = new Product(id, name, price, inStock, minStock, maxStock, list);
            product.setAssociatedParts(list);
        }
        return product;
    }
}
