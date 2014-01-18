package com.eprovement.poptavka.rest.externalsource;

/**
 * Simplified version of {@link com.eprovement.poptavka.rest.common.dto.CategoryDto}
 * containing only {@code id} and {@code name}.
 */
public class SimpleCategoryDto {
    private Long id;
    private String name;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "SimpleCategoryDto{"
                + "id=" + id
                + ", name='" + name + '\''
                + '}';
    }
}
