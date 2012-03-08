package cz.poptavka.sample.shared.domain.adminModule;

import cz.poptavka.sample.domain.user.Problem;
import java.io.Serializable;

/**
 * Represents full detail of demandType. Serves for creating new demandType
 * or for call of detail, that supports editing.
 *
 * @author Beho
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

    /**
     * Method created FullDemandDetail from provided Demand domain object.
     * @param problem
     * @return DemandDetail
     */
    public static ProblemDetail createProblemDetail(Problem problem) {
        ProblemDetail detail = new ProblemDetail();

        detail.setId(problem.getId());
        detail.setText(problem.getText());

        return detail;
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
}
