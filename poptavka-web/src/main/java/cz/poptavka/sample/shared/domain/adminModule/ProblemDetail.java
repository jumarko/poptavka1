package cz.poptavka.sample.shared.domain.adminModule;

import cz.poptavka.sample.domain.user.Problem;
import java.io.Serializable;

/**
 * Represents full detail of domain object <b>Problem</b> used in <i>Administration Module</i>.
 * Contains 2 static methods:  1. creating detail object
 *                             2. updating domain object
 *
 * @author Martin Slavkovsky
 *
 */
public class ProblemDetail implements Serializable {

    /**
     * Generated serialVersionUID.
     */
    private static final long serialVersionUID = -530982467233195456L;
    private Long id;
    private String text;

    /** for serialization. **/
    public ProblemDetail() {
    }

    public ProblemDetail(ProblemDetail problemDetail) {
        this.updateWholeProblem(problemDetail);
    }

    //---------------------------- GETTERS AND SETTERS --------------------
    public void updateWholeProblem(ProblemDetail problemDetail) {
        id = problemDetail.getId();
        text = problemDetail.getText();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return "\nGlobal Problem Detail Info:"
                + "\n    ID=" + Long.toString(id)
                + "\n    Text=" + text;
    }
}
