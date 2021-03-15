package inventory.service;

import inventory.model.Part;
import inventory.model.Product;
import inventory.repository.ProductRepository;
import javafx.collections.ObservableList;

public class ProductService {

    private ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public void addProduct(String name, double price, int inStock, int min, int max, ObservableList<Part> addParts) {
        Product product = new Product(productRepository.getAutoProductId(), name, price, inStock, min, max, addParts);
        productRepository.addProduct(product);
    }

    public Product lookupProduct(String search) {
        return productRepository.lookupProduct(search);
    }

    public ObservableList<Product> getAllProducts() {
        return productRepository.getAllProducts();
    }

    public void updateProduct(int productIndex, int productId, String name, double price, int inStock, int min, int max, ObservableList<Part> addParts) {
        Product product = new Product(productId, name, price, inStock, min, max, addParts);
        productRepository.updateProduct(productIndex, product);
    }

    public void deleteProduct(Product product) {
        productRepository.deleteProduct(product);
    }
}
