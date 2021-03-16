package inventory.controller;

import inventory.service.PartService;
import inventory.service.ProductService;

public interface Controller {
    
    void setService(PartService partService, ProductService productService);
}
