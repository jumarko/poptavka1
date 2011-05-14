package cz.poptavka.sample.shared.domain;

import java.io.Serializable;

/**
 * Low-bandwitdh representation of Locality designed for direct use on frontend.
 *
 * @author Beho
 *
 */
public class LocalityDetail implements Serializable {

    public static final int REGION = 0;
    public static final int DISTRICT = 1;
    public static final int CITY = 2;

    /**
     * Generated serialVersionUID.
     */
    private static final long serialVersionUID = 8480517227278818048L;

    private String name;
    private String code;

    public LocalityDetail() {
    }

    public LocalityDetail(String name, String code) {
        this.name = name;
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

}
