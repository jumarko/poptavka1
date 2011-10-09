package cz.poptavka.sample.shared.domain;

import com.google.gwt.user.rebind.rpc.ProblemReport.Problem;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

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
    private String primaryMessage;
    private List<String> messages;

    /** for serialization. **/
    public ProblemDetail() {
    }

    public ProblemDetail(ProblemDetail demand) {
        this.updateWholeProblem(demand);
    }

    /**
     * Method created FullDemandDetail from provided Demand domain object.
     * @param problem
     * @return DemandDetail
     */
    public static ProblemDetail createProblemDetail(Problem problem) {
        ProblemDetail detail = new ProblemDetail();

        detail.setPrimaryMessage(problem.getPrimaryMessage());
        List<String> messages = new ArrayList<String>();
        for (Iterator<String> it = problem.getSubMessages().iterator(); it.hasNext();) {
            messages.add(it.next());
        }
        detail.setMessages(messages);

        return detail;
    }

    //---------------------------- GETTERS AND SETTERS --------------------
    public void updateWholeProblem(ProblemDetail problemDetail) {
        primaryMessage = problemDetail.getPrimaryMessage();
        messages = problemDetail.getMessages();
    }

    public List<String> getMessages() {
        return messages;
    }

    public void setMessages(List<String> messages) {
        this.messages = messages;
    }

    public String getPrimaryMessage() {
        return primaryMessage;
    }

    public void setPrimaryMessage(String primaryMessage) {
        this.primaryMessage = primaryMessage;
    }

    @Override
    public String toString() {

        return "\nGlobal Problem Detail Info:"
                + "\n     primaryMessage="
                + primaryMessage + "\n    messages="
                + messages.toString();
    }

//    @Override
//    public boolean equals(Object obj) {
//        if (obj == null) {
//            return false;
//        }
//        if (getClass() != obj.getClass()) {
//            return false;
//        }
//        final ProblemDetail other = (ProblemDetail) obj;
//        if (this.id != other.getId()) {
//            return false;
//        }
//        return true;
//    }
//
//    @Override
//    public int hashCode() {
//        int hash = 7;
//        hash = 11 * hash + (int) (this.id ^ (this.id >>> 32));
//        return hash;
//    }
}
