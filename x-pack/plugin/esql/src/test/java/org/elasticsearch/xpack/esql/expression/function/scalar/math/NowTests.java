/*
 * Copyright Elasticsearch B.V. and/or licensed to Elasticsearch B.V. under one
 * or more contributor license agreements. Licensed under the Elastic License
 * 2.0; you may not use this file except in compliance with the Elastic License
 * 2.0.
 */

package org.elasticsearch.xpack.esql.expression.function.scalar.math;

import com.carrotsearch.randomizedtesting.annotations.Name;
import com.carrotsearch.randomizedtesting.annotations.ParametersFactory;

import org.elasticsearch.compute.data.Block;
import org.elasticsearch.compute.data.LongBlock;
import org.elasticsearch.xpack.esql.EsqlTestUtils;
import org.elasticsearch.xpack.esql.core.expression.Expression;
import org.elasticsearch.xpack.esql.core.tree.Source;
import org.elasticsearch.xpack.esql.core.type.DataTypes;
import org.elasticsearch.xpack.esql.expression.function.TestCaseSupplier;
import org.elasticsearch.xpack.esql.expression.function.scalar.AbstractConfigurationFunctionTestCase;
import org.elasticsearch.xpack.esql.expression.function.scalar.date.Now;
import org.elasticsearch.xpack.esql.session.EsqlConfiguration;
import org.hamcrest.Matcher;

import java.util.List;
import java.util.function.Supplier;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.matchesPattern;

public class NowTests extends AbstractConfigurationFunctionTestCase {
    public NowTests(@Name("TestCase") Supplier<TestCaseSupplier.TestCase> testCaseSupplier) {
        this.testCase = testCaseSupplier.get();
    }

    @ParametersFactory
    public static Iterable<Object[]> parameters() {
        return parameterSuppliersFromTypedData(
            List.of(
                new TestCaseSupplier(
                    "Now Test",
                    () -> new TestCaseSupplier.TestCase(
                        List.of(),
                        matchesPattern("LiteralsEvaluator\\[lit=.*\\]"),
                        DataTypes.DATETIME,
                        equalTo(EsqlTestUtils.TEST_CFG.now().toInstant().toEpochMilli())
                    )
                )
            )
        );
    }

    @Override
    protected Expression buildWithConfiguration(Source source, List<Expression> args, EsqlConfiguration configuration) {
        return new Now(Source.EMPTY, configuration);
    }

    @Override
    protected void assertSimpleWithNulls(List<Object> data, Block value, int nullBlock) {
        assertThat(((LongBlock) value).asVector().getLong(0), equalTo(EsqlTestUtils.TEST_CFG.now().toInstant().toEpochMilli()));
    }

    @Override
    protected Matcher<Object> allNullsMatcher() {
        return equalTo(EsqlTestUtils.TEST_CFG.now().toInstant().toEpochMilli());
    }

}
