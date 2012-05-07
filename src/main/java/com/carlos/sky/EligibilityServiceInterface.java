package com.carlos.sky;

public interface EligibilityServiceInterface {

    public abstract boolean isEligible(Long accountNumber) throws TechnicalFailureException, InvalidAccountNumberException;

}