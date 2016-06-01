package com.epam.cdp.model;

import java.math.BigDecimal;

public interface UserAccount {
    /**
     * User Id. UNIQUE.
     *
     * @return User Id.
     */
    long getId();

    void setId(long id);

    BigDecimal getBalance();

    void setBalance(BigDecimal balance);

    long getUserId();

    void setUserId(long userId);
}
