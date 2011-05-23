package cz.poptavka.sample.service.user;

/**
 * Encapsulates various criteria for searching users.
 * <p/>
 * Immutable, uses builder pattern for constructing object.
 *
 * @author Juraj Martinka
 *         Date: 8.1.11
 */
public final class UserSearchCriteria {

    private String name;

    private String surName;

    private String companyName;

    private String email;

    private UserSearchCriteria() {
        // use builder instead of constructor
    }

    public String getName() {
        return name;
    }

    public String getSurName() {
        return surName;
    }

    public String getCompanyName() {
        return companyName;
    }

    public String getEmail() {
        return email;
    }

    //----------------------------------  builder ----------------------------------------------------------------------
    public static final class Builder {
        private UserSearchCriteria userSearchCriteria;

        private Builder() {
            userSearchCriteria = new UserSearchCriteria();
        }

        public Builder withName(String name) {
            userSearchCriteria.name = name;
            return this;
        }

        public Builder withSurName(String surName) {
            userSearchCriteria.surName = surName;
            return this;
        }

        public Builder withCompanyName(String companyName) {
            userSearchCriteria.companyName = companyName;
            return this;
        }

        public Builder withEmail(String email) {
            userSearchCriteria.email = email;
            return this;
        }

        public static Builder userSearchCriteria() {
            return new Builder();
        }

        public UserSearchCriteria build() {
            return userSearchCriteria;
        }
    }
}
