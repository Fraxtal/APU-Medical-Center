package Customer.model;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Abstract base class demonstrating OOP concepts: Inheritance and Abstraction
 * Provides common functionality for all entities in the system
 * Implements Serializable for data persistence
 */
public abstract class BaseEntity implements Serializable {
    
    protected int id;
    protected LocalDateTime createdAt;
    protected LocalDateTime updatedAt;
    
    /**
     * Default constructor
     */
    public BaseEntity() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }
    
    /**
     * Constructor with ID
     * @param id The entity ID
     */
    public BaseEntity(int id) {
        this.id = id;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }
    
    /**
     * Get the entity ID
     * @return The entity ID
     */
    public int getId() {
        return id;
    }
    
    /**
     * Set the entity ID
     * @param id The entity ID
     */
    public void setId(int id) {
        this.id = id;
        this.updatedAt = LocalDateTime.now();
    }
    
    /**
     * Get creation timestamp
     * @return Creation timestamp
     */
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    
    /**
     * Get last update timestamp
     * @return Last update timestamp
     */
    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
    
    /**
     * Update the last modified timestamp
     */
    protected void updateTimestamp() {
        this.updatedAt = LocalDateTime.now();
    }
    
    /**
     * Abstract method to validate entity data
     * Must be implemented by subclasses - demonstrates abstraction
     * @return true if valid, false otherwise
     */
    public abstract boolean isValid();
    
    /**
     * Abstract method to get entity summary
     * Must be implemented by subclasses - demonstrates polymorphism
     * @return Entity summary string
     */
    public abstract String getSummary();
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        BaseEntity that = (BaseEntity) obj;
        return id == that.id;
    }
    
    @Override
    public int hashCode() {
        return Integer.hashCode(id);
    }
}
