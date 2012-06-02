package com.eprovement.poptavka.service.register;

import java.util.List;

/**
 * Provides methods for loading registers data.
 * <p>
 *     All registers have typically common structure (at least field code is presented)
 *     and we have following requirements>
 *     <ul>
 *         <li>Have a method for loading all register's values</li>
 *         <li>Have a method for loading one concrete value by given code</li>
 *         <li>Caching of results because registers' data are quite stable</li>
 *     </ul>
 * @author Juraj Martinka
 *         Date: 17.5.11
 */
public interface RegisterService {

    /**
     * Get all values for register identified by class <code>registerClass</code>.
     *
     * @param registerClass class of register, uniquely identifies the concrete register
     * @param <T> generic type - type of register (class)
     * @return all register values (rows in register table)
     */
    <T> List<T> getAllValues(Class<T> registerClass);

    /**
     * Load one unique value uniquely identified by <code>code</code> and <code>registerClass</code>.
     *
     * @param code unique code - unique in context of one register
     * @param registerClass class which uniquely identifies the concrete register (table)
     * @param <T> generic type - type of register (class)
     * @return one register value uniquely identified by code and class or null if no such value exists
     */
    <T> T getValue(String code, Class<T> registerClass);
}
