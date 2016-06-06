package org.bigmouth.nvwa.validate.factory;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.bigmouth.nvwa.utils.ReflectUtils;
import org.bigmouth.nvwa.utils.ReflectUtils.FieldFilter;
import org.bigmouth.nvwa.validate.Validator;
import org.bigmouth.nvwa.validate.annotation.ValidateCollection;
import org.bigmouth.nvwa.validate.annotation.ValidateCustom;
import org.bigmouth.nvwa.validate.annotation.ValidateLength;
import org.bigmouth.nvwa.validate.annotation.ValidateNotGreatThan;
import org.bigmouth.nvwa.validate.annotation.ValidateNotLaterThan;
import org.bigmouth.nvwa.validate.annotation.ValidateNotNull;
import org.bigmouth.nvwa.validate.annotation.ValidateNumeric;
import org.bigmouth.nvwa.validate.annotation.ValidatePattern;
import org.bigmouth.nvwa.validate.internal.BeanValidator;
import org.bigmouth.nvwa.validate.internal.BeanValidatorWrapper;
import org.bigmouth.nvwa.validate.internal.CollectionValidator;
import org.bigmouth.nvwa.validate.internal.CustomValidator;
import org.bigmouth.nvwa.validate.internal.LengthValidator;
import org.bigmouth.nvwa.validate.internal.NotGreatThanValidator;
import org.bigmouth.nvwa.validate.internal.NotLaterThanValidator;
import org.bigmouth.nvwa.validate.internal.NotNullValidator;
import org.bigmouth.nvwa.validate.internal.NumericValidator;
import org.bigmouth.nvwa.validate.internal.PatternValidator;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

public class AnnotationValidatorFactory implements ValidatorFactory {

	private final Map<Class<? extends Annotation>, ValidatorMetadata> validatorMetadatas = Maps
			.newHashMap();

	public AnnotationValidatorFactory() {
		initValidatorMetadatas();
	}

	private void initValidatorMetadatas() {
		registerValidatorMetadata(new ValidatorMetadata(ValidateCollection.class) {

			@Override
			public Validator create(Field f, Annotation anno) {
				ValidateCollection an = (ValidateCollection) anno;
				Class<? extends Validator> clazz = an.elementValidator();
				try {
					return new CollectionValidator(f, clazz.newInstance(), an.allowEmpty(), an
							.minSize(), an.maxSize());
				} catch (InstantiationException e) {
					throw new ValidatorCreateException("CollectionValidator create:", e);
				} catch (IllegalAccessException e) {
					throw new ValidatorCreateException("CollectionValidator create:", e);
				}
			}

			@Override
			public void match(Class<?> clazz) {
				if (!(List.class.isAssignableFrom(clazz) || clazz.isArray()))
					throw new ValidateAnnotationMisuseException(getAnnoClass(),
							" expect List or Array,but " + clazz);
			}
		});
		registerValidatorMetadata(new ValidatorMetadata(ValidateLength.class) {

			@Override
			public Validator create(Field f, Annotation anno) {
				ValidateLength an = (ValidateLength) anno;
				return new LengthValidator(f, an.min(), an.max());
			}

			@Override
			public void match(Class<?> clazz) {
				if (String.class != clazz)
					throw new ValidateAnnotationMisuseException(getAnnoClass(),
							" expect String,but " + clazz);
			}
		});
		registerValidatorMetadata(new ValidatorMetadata(ValidateNotGreatThan.class) {

			@Override
			public Validator create(Field f, Annotation anno) {
				ValidateNotGreatThan an = (ValidateNotGreatThan) anno;
				String cfn = an.compareField();
				Field cf;
				try {
					cf = f.getDeclaringClass().getDeclaredField(cfn);
				} catch (SecurityException e) {
					throw new ValidatorCreateException("NotGreatThanValidator create:", e);
				} catch (NoSuchFieldException e) {
					throw new ValidatorCreateException("NotGreatThanValidator create:", e);
				}
				// TODO: if cf is not Numeric
				return new NotGreatThanValidator(f, cf);
			}

			@Override
			public void match(Class<?> clazz) {
				if (!(Byte.class == clazz || byte.class == clazz || Short.class == clazz
						|| short.class == clazz || Integer.class == clazz || int.class == clazz
						|| Long.class == clazz || long.class == clazz)) {
					throw new ValidateAnnotationMisuseException(getAnnoClass(),
							" expect Numeric,but " + clazz);
				}
			}
		});
		registerValidatorMetadata(new ValidatorMetadata(ValidateNotLaterThan.class) {

			@Override
			public Validator create(Field f, Annotation anno) {
				ValidateNotLaterThan an = (ValidateNotLaterThan) anno;
				String cfn = an.compareField();
				Field cf;
				try {
					cf = f.getDeclaringClass().getField(cfn);
				} catch (SecurityException e) {
					throw new ValidatorCreateException("NotLaterThanValidator create:", e);
				} catch (NoSuchFieldException e) {
					throw new ValidatorCreateException("NotLaterThanValidator create:", e);
				}
				// TODO: if cf is not Date
				return new NotLaterThanValidator(f, cf);
			}

			@Override
			public void match(Class<?> clazz) {
				if (!(Date.class.isAssignableFrom(clazz)))
					throw new ValidateAnnotationMisuseException(getAnnoClass(), " expect Date,but "
							+ clazz);
			}
		});
		registerValidatorMetadata(new ValidatorMetadata(ValidateNotNull.class) {

			@Override
			public Validator create(Field f, Annotation anno) {
				return new NotNullValidator(f);
			}

			@Override
			public void match(Class<?> clazz) {
				if (clazz.isPrimitive())
					throw new ValidateAnnotationMisuseException(getAnnoClass(),
							" expect Object,but " + clazz);
			}
		});
		registerValidatorMetadata(new ValidatorMetadata(ValidateNumeric.class) {

			@Override
			public Validator create(Field f, Annotation anno) {
				ValidateNumeric an = (ValidateNumeric) anno;
				return new NumericValidator(f, an.min(), an.max());
			}

			@Override
			public void match(Class<?> clazz) {
				if (!(Byte.class == clazz || byte.class == clazz || Short.class == clazz
						|| short.class == clazz || Integer.class == clazz || int.class == clazz
						|| Long.class == clazz || long.class == clazz || String.class == clazz)) {
					throw new ValidateAnnotationMisuseException(getAnnoClass(),
							" expect Numeric,but " + clazz);
				}
			}
		});
		registerValidatorMetadata(new ValidatorMetadata(ValidatePattern.class) {

			@Override
			public Validator create(Field f, Annotation anno) {
				ValidatePattern an = (ValidatePattern) anno;
				return new PatternValidator(f, an.value());
			}

			@Override
			public void match(Class<?> clazz) {
				if (String.class != clazz)
					throw new ValidateAnnotationMisuseException(getAnnoClass(),
							" expect String,but " + clazz);
			}
		});
		registerValidatorMetadata(new ValidatorMetadata(ValidateCustom.class) {

			@Override
			public Validator create(Field f, Annotation anno) {
				ValidateCustom an = (ValidateCustom) anno;
				Class<? extends Validator> clazz = an.validator();
				try {
					return new CustomValidator(f, clazz.newInstance());
				} catch (InstantiationException e) {
					throw new ValidatorCreateException("CustomValidator create:", e);
				} catch (IllegalAccessException e) {
					throw new ValidatorCreateException("CustomValidator create:", e);
				}
			}

			@Override
			public void match(Class<?> clazz) {
				// Empty Handler
			}
		});
	}

	@Override
	public Validator create(Class<?> beanClass) {
		final List<Validator> childValidators = Lists.newArrayList();
		ReflectUtils.findFields(beanClass, new FieldFilter() {

			@Override
			public boolean accept(Field _f) {
				List<Validator> vs = create(_f);
				childValidators.addAll(vs);
				return false;
			}
		});

		if (childValidators.isEmpty())
			return null;
		final BeanValidator beanValidator = new BeanValidator();
		beanValidator.register(childValidators);
		return beanValidator;
	}

	private List<Validator> create(Field f) {
		List<Validator> ret = Lists.newArrayList();
		Class<?> clazz = f.getType();

		// annotations
		Annotation[] annos = f.getAnnotations();
		for (Annotation anno : annos) {
			ValidatorMetadata vm = validatorMetadatas.get(anno.annotationType());
			if (null == vm)
				continue;
			vm.match(clazz);
			Validator validator = vm.create(f, anno);
			ret.add(validator);
		}

		// child object
		final List<Validator> childValidators = Lists.newArrayList();
		if (clazz.isPrimitive() || (String.class == clazz))
			return ret;
		ReflectUtils.findFields(clazz, new FieldFilter() {

			@Override
			public boolean accept(Field _f) {
				List<Validator> vs = create(_f);
				childValidators.addAll(vs);
				return false;
			}
		});
		if (childValidators.size() > 0) {
			BeanValidator beanValidator = new BeanValidator();
			for (Validator childValidator : childValidators) {
				beanValidator.register(childValidator);
			}

			ret.add(new BeanValidatorWrapper(f, beanValidator));
		}
		return ret;
	}

	public void registerValidatorMetadata(ValidatorMetadata metadata) {
		validatorMetadatas.put(metadata.getAnnoClass(), metadata);
	}
}
