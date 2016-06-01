package com.epam.cdp.exception;

public class EntityNotFoundException extends RuntimeException {
    private Long entityId;

    public EntityNotFoundException(String message) {
        super(message);
    }

    public EntityNotFoundException(Long entityId) {
        this.entityId = entityId;
    }

    public EntityNotFoundException(String message, Long entityId) {
        super(message);
        this.entityId = entityId;
    }

    public EntityNotFoundException(String message, Throwable cause, Long entityId) {
        super(message, cause);
        this.entityId = entityId;
    }

    public EntityNotFoundException(Throwable cause, Long entityId) {
        super(cause);
        this.entityId = entityId;
    }

    public Long getEntityId() {
        return entityId;
    }
}
