/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package cz.poptavka.sample.domain.mail;

import cz.poptavka.sample.domain.common.DomainObject;
import javax.persistence.MappedSuperclass;

/**
 *
 * @author Vojtech Hubr
 *         Date 12.4.11
 * Makes it possible to organize messages in a tree-like structure
 * as in builletin boards.
 */

@MappedSuperclass
public class MessageTreeItem extends DomainObject {
    /* the first message in the thread, i.e. the original question */
    Message threadRoot;
    /* immedate parent of this message - to what this message is a reply */
    Message parent;
    /* the first child of this message - the first reply to rhis message */
    Message firstBorn;
    /* the next reply to this message's parent */
    Message nextSibling;

    public Message getFirstBorn() {
        return firstBorn;
    }

    public void setFirstBorn(Message firstBorn) {
        this.firstBorn = firstBorn;
    }

    public Message getNextSibling() {
        return nextSibling;
    }

    public void setNextSibling(Message nextSibling) {
        this.nextSibling = nextSibling;
    }

    public Message getParent() {
        return parent;
    }

    public void setParent(Message parent) {
        this.parent = parent;
    }

    public Message getThreadRoot() {
        return threadRoot;
    }

    public void setThreadRoot(Message threadRoot) {
        this.threadRoot = threadRoot;
    }

}
