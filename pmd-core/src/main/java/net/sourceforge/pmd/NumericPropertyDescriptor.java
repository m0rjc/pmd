/**
 * BSD-style license; for more info see http://pmd.sourceforge.net/license.html
 */

package net.sourceforge.pmd;

import static net.sourceforge.pmd.PropertyDescriptorField.MAX;
import static net.sourceforge.pmd.PropertyDescriptorField.MIN;

import java.util.Map;

import net.sourceforge.pmd.lang.rule.properties.ExpectedFieldsBuilder;

/**
 * Defines a descriptor type whose instance values are required to lie within
 * specified upper and lower limits.
 *
 * @param <T> type of the property value
 *
 * @author Brian Remedios
 */
public interface NumericPropertyDescriptor<T> extends PropertyDescriptor<T> {

    Map<PropertyDescriptorField, Boolean> NUMBER_FIELD_TYPES_BY_KEY
        = ExpectedFieldsBuilder.instance().put(MIN, true).put(MAX, true).build();


    /**
     * Returns the maximum value that instances of the property can have.
     *
     * @return Number
     */
    Number upperLimit();


    /**
     * Returns the minimum value that instances of the property can have.
     *
     * @return Number
     */
    Number lowerLimit();
}
