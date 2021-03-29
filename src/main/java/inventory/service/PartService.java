package inventory.service;

import inventory.model.*;
import inventory.repository.PartRepository;
import inventory.validator.ValidationException;
import inventory.validator.Validator;
import javafx.collections.ObservableList;

public class PartService {

    private PartRepository partRepository;

    public PartService(PartRepository partRepository) {
        this.partRepository = partRepository;
    }

    public void addInhousePart(String name, double price, int inStock, int min, int max, int partDynamicValue) {
        InhousePart inhousePart = new InhousePart(partRepository.getAutoPartId(), name, price, inStock, min, max, partDynamicValue);
        partRepository.addPart(inhousePart);
    }

    /**
     *
     * @param name String, can not be empty
     * @param price double, must be greater than 0.
     * @param inStock int, must be greater than 0.
     * @param min int, must be positive, less than the Max value and lower than inStock.
     * @param max int, must be positive, bigger than the min value and bigger than inStock.
     * @param partDynamicValue String, can not be empty
     */
    public void addOutsourcePart(String name, double price, int inStock, int min, int max, String partDynamicValue) {
        String errorMessage = "";
        errorMessage = Validator.isValidPart(name, price, inStock, min, max, errorMessage);
        if(!errorMessage.isEmpty()){
            throw new ValidationException(errorMessage);
        }
        OutsourcedPart outsourcedPart = new OutsourcedPart(partRepository.getAutoPartId(), name, price, inStock, min, max, partDynamicValue);
        partRepository.addPart(outsourcedPart);
    }

    public ObservableList<Part> getAllParts() {
        return partRepository.getAllParts();
    }

    public Part lookupPart(String search) {
        return partRepository.lookupPart(search);
    }

    public void updateInhousePart(int partIndex, int partId, String name, double price, int inStock, int min, int max, int partDynamicValue) {
        InhousePart inhousePart = new InhousePart(partId, name, price, inStock, min, max, partDynamicValue);
        partRepository.updatePart(partIndex, inhousePart);
    }

    public void updateOutsourcedPart(int partIndex, int partId, String name, double price, int inStock, int min, int max, String partDynamicValue) {
        OutsourcedPart outsourcedPart = new OutsourcedPart(partId, name, price, inStock, min, max, partDynamicValue);
        partRepository.updatePart(partIndex, outsourcedPart);
    }

    public void deletePart(Part part) {
        partRepository.deletePart(part);
    }
}
