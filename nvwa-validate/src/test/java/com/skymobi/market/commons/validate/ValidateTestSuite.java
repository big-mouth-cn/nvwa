package com.skymobi.market.commons.validate;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import com.skymobi.market.commons.validate.factory.AnnotationValidatorFactoryTest;
import com.skymobi.market.commons.validate.internal.BeanValidatorTest;
import com.skymobi.market.commons.validate.internal.CollectionValidatorTest;
import com.skymobi.market.commons.validate.internal.LengthValidatorTest;
import com.skymobi.market.commons.validate.internal.NotGreatThanValidatorTest;
import com.skymobi.market.commons.validate.internal.NotLaterThanValidatorTest;
import com.skymobi.market.commons.validate.internal.NotNullValidatorTest;
import com.skymobi.market.commons.validate.internal.NumericValidatorTest;
import com.skymobi.market.commons.validate.internal.PatternValidatorTest;

@RunWith(Suite.class)
@SuiteClasses( { BeanValidatorTest.class, CollectionValidatorTest.class, LengthValidatorTest.class,
		NotGreatThanValidatorTest.class, NotLaterThanValidatorTest.class,
		NotNullValidatorTest.class, NumericValidatorTest.class, PatternValidatorTest.class,
		AnnotationValidatorFactoryTest.class })
public class ValidateTestSuite {
}
