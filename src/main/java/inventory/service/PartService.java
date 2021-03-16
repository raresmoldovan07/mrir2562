package inventory.service;

import inventory.model.*;
import inventory.repository.PartRepository;
import javafx.collections.ObservableList;

public class PartService {

    private PartRepository partRepository;

    public PartService(PartRepository partRepository){
        this.partRepository = partRepository;
    }

    public void addInhousePart(String name, double price, int inStock, int min, int  max, int partDynamicValue){
        InhousePart inhousePart = new InhousePart(partRepository.getAutoPartId(), name, price, inStock, min, max, partDynamicValue);
        partRepository.addPart(inhousePart);
    }

    public void addOutsourcePart(String name, double price, int inStock, int min, int  max, String partDynamicValue){
        OutsourcedPart outsourcedPart = new OutsourcedPart(partRepository.getAutoPartId(), name, price, inStock, min, max, partDynamicValue);
        partRepository.addPart(outsourcedPart);
    }

    public ObservableList<Part> getAllParts() {
        return partRepository.getAllParts();
    }

    public Part lookupPart(String search) {
        return partRepository.lookupPart(search);
    }

    public void updateInhousePart(int partIndex, int partId, String name, double price, int inStock, int min, int max, int partDynamicValue){
        InhousePart inhousePart = new InhousePart(partId, name, price, inStock, min, max, partDynamicValue);
        partRepository.updatePart(partIndex, inhousePart);
    }

    public void updateOutsourcedPart(int partIndex, int partId, String name, double price, int inStock, int min, int max, String partDynamicValue){
        OutsourcedPart outsourcedPart = new OutsourcedPart(partId, name, price, inStock, min, max, partDynamicValue);
        partRepository.updatePart(partIndex, outsourcedPart);
    }

    public void deletePart(Part part){
        partRepository.deletePart(part);
    }
}
