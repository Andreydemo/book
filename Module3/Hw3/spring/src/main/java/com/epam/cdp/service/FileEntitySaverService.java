package com.epam.cdp.service;

/**
 * Saves entities to storage from file.
 */
public interface FileEntitySaverService {
    /**
     * Save entities to storage from file.
     * @param filePath path to file.
     */
    void save(String filePath);
}
