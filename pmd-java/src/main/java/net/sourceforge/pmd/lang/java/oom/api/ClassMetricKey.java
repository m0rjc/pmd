/**
 * BSD-style license; for more info see http://pmd.sourceforge.net/license.html
 */

package net.sourceforge.pmd.lang.java.oom.api;

import net.sourceforge.pmd.lang.java.ast.ASTAnyTypeDeclaration;
import net.sourceforge.pmd.lang.java.oom.metrics.AtfdMetric.AtfdClassMetric;
import net.sourceforge.pmd.lang.java.oom.metrics.CycloMetric.CycloClassMetric;
import net.sourceforge.pmd.lang.java.oom.metrics.LocMetric.LocClassMetric;
import net.sourceforge.pmd.lang.java.oom.metrics.NcssMetric.NcssClassMetric;
import net.sourceforge.pmd.lang.java.oom.metrics.WmcMetric;

/**
 * Keys identifying standard class metrics.
 */
public enum ClassMetricKey implements MetricKey<ASTAnyTypeDeclaration> {

    /**
     * Access to Foreign Data.
     *
     * @see net.sourceforge.pmd.lang.java.oom.metrics.AtfdMetric
     */
    ATFD(new AtfdClassMetric()),

    /**
     * Weighed Method Count.
     *
     * @see WmcMetric
     */
    WMC(new WmcMetric()),

    /**
     * Cyclomatic complexity.
     *
     * @see net.sourceforge.pmd.lang.java.oom.metrics.CycloMetric
     */
    CYCLO(new CycloClassMetric()),

    /**
     * Non Commenting Source Statements.
     *
     * @see net.sourceforge.pmd.lang.java.oom.metrics.NcssMetric
     */
    NCSS(new NcssClassMetric()),

    /**
     * Lines of Code.
     *
     * @see net.sourceforge.pmd.lang.java.oom.metrics.LocMetric
     */
    LOC(new LocClassMetric());

    private final ClassMetric calculator;


    ClassMetricKey(ClassMetric m) {
        calculator = m;
    }


    @Override
    public ClassMetric getCalculator() {
        return calculator;
    }


    @Override
    public boolean supports(ASTAnyTypeDeclaration node) {
        return calculator.supports(node);
    }


    /**
     * Creates a new metric key holding a metric which can be computed on a class.
     *
     * TODO:cf Generify and move to MetricKey after upgrading compiler to 1.8
     *
     * @param metric The metric to use
     * @param name   The name of the metric
     *
     * @return The metric key
     */
    public static MetricKey<ASTAnyTypeDeclaration> of(final Metric<ASTAnyTypeDeclaration> metric, final String name) {
        return new MetricKey<ASTAnyTypeDeclaration>() {
            @Override
            public String name() {
                return name;
            }


            @Override
            public Metric<ASTAnyTypeDeclaration> getCalculator() {
                return metric;
            }


            @Override
            public boolean supports(ASTAnyTypeDeclaration node) {
                return metric.supports(node);
            }
        };
    }

}
