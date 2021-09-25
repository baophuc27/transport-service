package com.reeco.service;

import com.reeco.exception.IncorrectParameterException;

import java.util.Map;

public class Validator {

    /**
     * This method validate <code>EntityId</code> entity id. If entity id is invalid than throw
     * <code>IncorrectParameterException</code> exception
     *
     * @param entityId     the entityId
     * @param errorMessage the error message for exception
     */
    public static boolean validateEntityId(Long entityId) {
        return entityId != null && entityId > 0L;
    }

    public static void validateContainFieldPayload(String field, Map<String, Object> payload, String errorMessage){
        if (!payload.containsKey(field)){
            throw new IncorrectParameterException(errorMessage);
        }
    }
    /**
     * This method validate <code>String</code> string. If string is invalid than throw
     * <code>IncorrectParameterException</code> exception
     *
     * @param val          the val
     * @param errorMessage the error message for exception
     */
    public static void validateString(String val, String errorMessage) {
        if (val == null || val.isEmpty()) {
            throw new IncorrectParameterException(errorMessage);
        }
    }


    /**
     * This method validate <code>String</code> string. If string is invalid than throw
     * <code>IncorrectParameterException</code> exception
     *
     * @param val          the val
     * @param errorMessage the error message for exception
     */
    public static void validatePositiveNumber(long val, String errorMessage) {
        if (val <= 0) {
            throw new IncorrectParameterException(errorMessage);
        }
    }

}
