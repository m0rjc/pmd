/**
 * BSD-style license; for more info see http://pmd.sourceforge.net/license.html
 */

package net.sourceforge.pmd.lang.java.oom.rule;

import java.util.HashMap;
import java.util.Map;

import net.sourceforge.pmd.lang.java.ast.ASTAnyTypeDeclaration;
import net.sourceforge.pmd.lang.java.ast.ASTCompilationUnit;
import net.sourceforge.pmd.lang.java.ast.ASTMethodDeclaration;
import net.sourceforge.pmd.lang.java.ast.ASTMethodOrConstructorDeclaration;
import net.sourceforge.pmd.lang.java.oom.Metrics;
import net.sourceforge.pmd.lang.java.oom.api.ClassMetricKey;
import net.sourceforge.pmd.lang.java.oom.api.Metric;
import net.sourceforge.pmd.lang.java.oom.api.MetricVersion;
import net.sourceforge.pmd.lang.java.oom.api.OperationMetricKey;
import net.sourceforge.pmd.lang.java.oom.api.ResultOption;
import net.sourceforge.pmd.lang.java.oom.metrics.NcssMetric.NcssVersion;
import net.sourceforge.pmd.lang.java.rule.AbstractJavaMetricsRule;
import net.sourceforge.pmd.lang.rule.properties.EnumeratedProperty;
import net.sourceforge.pmd.lang.rule.properties.IntegerProperty;

/**
 * Simple rule for Ncss. Maybe to be enriched with type specific thresholds.
 *
 * @author Clément Fournier
 */
public final class NcssCountRule extends AbstractJavaMetricsRule {


    private static final IntegerProperty METHOD_REPORT_LEVEL_DESCRIPTOR = new IntegerProperty(
        "methodReportLevel", "Metric reporting threshold for methods", 1, 60, 12, 1.0f);

    private static final IntegerProperty CLASS_REPORT_LEVEL_DESCRIPTOR = new IntegerProperty(
        "classReportLevel", "Metric reporting threshold for classes", 1, 1000, 250, 1.0f);

    private static final Map<String, MetricVersion> VERSION_MAP;

    static {
        VERSION_MAP = new HashMap<>();
        VERSION_MAP.put("standard", Metric.Version.STANDARD);
        VERSION_MAP.put("javaNcss", NcssVersion.JAVANCSS);
    }

    private static final EnumeratedProperty<MetricVersion> NCSS_VERSION_DESCRIPTOR = new EnumeratedProperty<>(
        "ncssVersion", "Choose a variant of Ncss or the standard",
        VERSION_MAP, Metric.Version.STANDARD, MetricVersion.class, 3.0f);


    private int methodReportLevel;
    private int classReportLevel;
    private MetricVersion ncssVersion = Metric.Version.STANDARD;


    public NcssCountRule() {
        definePropertyDescriptor(METHOD_REPORT_LEVEL_DESCRIPTOR);
        definePropertyDescriptor(CLASS_REPORT_LEVEL_DESCRIPTOR);
        definePropertyDescriptor(NCSS_VERSION_DESCRIPTOR);
    }


    @Override
    public Object visit(ASTCompilationUnit node, Object data) {
        methodReportLevel = getProperty(METHOD_REPORT_LEVEL_DESCRIPTOR);
        classReportLevel = getProperty(CLASS_REPORT_LEVEL_DESCRIPTOR);
        ncssVersion = getProperty(NCSS_VERSION_DESCRIPTOR);

        super.visit(node, data);
        return data;
    }


    @Override
    public Object visit(ASTAnyTypeDeclaration node, Object data) {

        super.visit(node, data);

        if (ClassMetricKey.NCSS.supports(node)) {
            int classSize = (int) Metrics.get(ClassMetricKey.NCSS, node, ncssVersion);
            int classHighest = (int) Metrics.get(OperationMetricKey.NCSS, node, ncssVersion, ResultOption.HIGHEST);

            if (classSize >= classReportLevel) {
                String[] messageParams = {node.getTypeKind().name().toLowerCase(),
                                          node.getImage(),
                                          classSize + " (Highest = " + classHighest + ")", };

                addViolation(data, node, messageParams);
            }
        }
        return data;
    }


    @Override
    public Object visit(ASTMethodOrConstructorDeclaration node, Object data) {

        int methodSize = (int) Metrics.get(OperationMetricKey.NCSS, node, ncssVersion);
        if (methodSize >= methodReportLevel) {
            addViolation(data, node, new String[] {node instanceof ASTMethodDeclaration ? "method" : "constructor",
                                                   node.getQualifiedName().getOperation(), "" + methodSize, });
        }

        return data;
    }

}
