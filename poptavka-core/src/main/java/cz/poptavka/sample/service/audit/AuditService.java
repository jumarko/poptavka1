package cz.poptavka.sample.service.audit;

import org.hibernate.envers.exception.NotAuditedException;
import org.hibernate.envers.exception.RevisionDoesNotExistException;

import java.util.Date;
import java.util.List;

/**
 * @author Juraj Martinka
 *         Date: 8.1.11
 */
public interface AuditService {

    /**
     * Find an entity by primary key at the given revision.
     *
     * @param cls        Class of the entity.
     * @param primaryKey Primary key of the entity.
     * @param revision   Revision in which to get the entity.
     * @return The found entity instance at the given revision (its properties may be partially filled
     *         if not all properties are audited) or null, if an entity with that id didn't exist at that
     *         revision.
     * @throws IllegalArgumentException If cls or primaryKey is null or revision is less or equal to 0.
     * @throws org.hibernate.envers.exception.NotAuditedException
     *                                  When entities of the given class are not audited.
     * @throws IllegalStateException    If the associated entity manager is closed.
     */
    <T> T find(Class<T> cls, Object primaryKey, Number revision) throws
            IllegalArgumentException, NotAuditedException, IllegalStateException;

    /**
     * Find an entity by primary key at the given revision with the specified entityName.
     *
     * @param cls        Class of the entity.
     * @param entityName EntityName to find.
     * @param primaryKey Primary key of the entity.
     * @param revision   Revision in which to get the entity.
     * @return The found entity instance at the given revision (its properties may be partially filled
     *         if not all properties are audited) or null, if an entity with that id didn't exist at that
     *         revision.
     * @throws IllegalArgumentException If cls or primaryKey is null or revision is less or equal to 0.
     * @throws NotAuditedException      When entities of the given class are not audited.
     * @throws IllegalStateException    If the associated entity manager is closed.
     */
    <T> T find(Class<T> cls, String entityName, Object primaryKey,
               Number revision) throws IllegalArgumentException,
            NotAuditedException, IllegalStateException;

    /**
     * Get a list of revision numbers, at which an entity was modified.
     *
     * @param cls        Class of the entity.
     * @param primaryKey Primary key of the entity.
     * @return A list of revision numbers, at which the entity was modified, sorted in ascending order (so older
     *         revisions come first).
     * @throws NotAuditedException      When entities of the given class are not audited.
     * @throws IllegalArgumentException If cls or primaryKey is null.
     * @throws IllegalStateException    If the associated entity manager is closed.
     */
    List<Number> getRevisions(Class<?> cls, Object primaryKey)
        throws IllegalArgumentException, NotAuditedException, IllegalStateException;

    /**
     * Get a list of revision numbers, at which an entity was modified, looking by entityName.
     *
     * @param cls        Class of the entity.
     * @param entityName EntityName to find.
     * @param primaryKey Primary key of the entity.
     * @return A list of revision numbers, at which the entity was modified, sorted in ascending order (so older
     *         revisions come first).
     * @throws NotAuditedException      When entities of the given class are not audited.
     * @throws IllegalArgumentException If cls or primaryKey is null.
     * @throws IllegalStateException    If the associated entity manager is closed.
     */
    List<Number> getRevisions(Class<?> cls, String entityName, Object primaryKey)
        throws IllegalArgumentException, NotAuditedException, IllegalStateException;

    /**
     * Get the date, at which a revision was created.
     *
     * @param revision Number of the revision for which to get the date.
     * @return Date of commiting the given revision.
     * @throws IllegalArgumentException If revision is less or equal to 0.
     * @throws org.hibernate.envers.exception.RevisionDoesNotExistException
     *                                  If the revision does not exist.
     * @throws IllegalStateException    If the associated entity manager is closed.
     */
    Date getRevisionDate(Number revision) throws IllegalArgumentException, RevisionDoesNotExistException,
            IllegalStateException;

    /**
     * Gets the revision number, that corresponds to the given date. More precisely, returns
     * the number of the highest revision, which was created on or before the given date. So:
     * <code>getRevisionDate(getRevisionNumberForDate(date)) <= date</code> and
     * <code>getRevisionDate(getRevisionNumberForDate(date)+1) > date</code>.
     *
     * @param date Date for which to get the revision.
     * @return Revision number corresponding to the given date.
     * @throws IllegalStateException         If the associated entity manager is closed.
     * @throws RevisionDoesNotExistException If the given date is before the first revision.
     * @throws IllegalArgumentException      If <code>date</code> is <code>null</code>.
     */
    Number getRevisionNumberForDate(Date date) throws IllegalStateException, RevisionDoesNotExistException,
            IllegalArgumentException;

    /**
     * A helper method; should be used only if a custom revision entity is used.
     * See also {@link org.hibernate.envers.RevisionEntity}.
     *
     * @param revisionEntityClass Class of the revision entity.
     *                            Should be annotated with {@link org.hibernate.envers.RevisionEntity}.
     * @param revision            Number of the revision for which to get the data.
     * @return Entity containing data for the given revision.
     * @throws IllegalArgumentException      If revision is less or equal to 0 or if the class of the revision entity
     *                                       is invalid.
     * @throws RevisionDoesNotExistException If the revision does not exist.
     * @throws IllegalStateException         If the associated entity manager is closed.
     */
    <T> T findRevision(Class<T> revisionEntityClass, Number revision) throws IllegalArgumentException,
            RevisionDoesNotExistException, IllegalStateException;

    /**
     * Gets an instance of the current revision entity, to which any entries in the audit tables will be bound.
     * Please note the if {@code persist} is {@code false}, and no audited entities are modified in this session,
     * then the obtained revision entity instance won't be persisted. If {@code persist} is {@code true}, the revision
     * entity instance will always be persisted, regardless of whether audited entities are changed or not.
     *
     * @param revisionEntityClass Class of the revision entity. Should be annotated with
     *                            {@link org.hibernate.envers.RevisionEntity}.
     * @param persist             If the revision entity is not yet persisted, should it become persisted.
     *                            This way, the primary identifier (id) will be filled (if it's assigned by the DB)
     *                            and available, but the revision entity will be persisted even if there are no changes
     *                            to audited entities. Otherwise, the revision number (id) can be {@code null}.
     * @return The current revision entity, to which any entries in the audit tables will be bound.
     */
    <T> T getCurrentRevision(Class<T> revisionEntityClass, boolean persist);

    /**
     *
     * @return A query creator, associated with this AuditReader instance, with which queries can be
     * created and later executed. Shouldn't be used after the associated Session or EntityManager
     * is closed.
     */
    // TODO: make up your mind
//       AuditQueryCreator createQuery();

    /**
     * Checks if the entityClass was configured to be audited.
     *
     * @param entityClass Class of the entity asking for audit support
     * @return true if the entityClass is audited.
     */
    boolean isEntityClassAudited(Class<?> entityClass);

}
