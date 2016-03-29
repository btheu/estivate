package btheu.jsoupmapper;

import java.io.IOException;
import java.io.InputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.AccessibleObject;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import btheu.jsoupmapper.core.ClassUtils;
import btheu.jsoupmapper.core.ConvertorContext;
import btheu.jsoupmapper.core.PrimitiveTypeConvertor;
import btheu.jsoupmapper.core.SelectEvaluator;
import btheu.jsoupmapper.core.StandardTypeConvertor;
import btheu.jsoupmapper.core.TypeConvertor;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class JSoupMapper {

	@Getter
	@Setter
	protected String encoding = "UTF-8";

	@Getter
	@Setter
	protected String baseURI = "/";

	protected static final String PACKAGE_NAME = Select.class.getPackage()
			.getName();

	public Object map(InputStream document, Type type) {
		Document parseDocument = parseDocument(document);
		return map(parseDocument, new Elements(parseDocument), type);
	}

	public <T> List<T> mapToList(InputStream document, Class<T> clazz) {
		Document parseDocument = parseDocument(document);
		return mapToList(parseDocument, new Elements(parseDocument), clazz);
	}

	public <T> T map(InputStream document, Class<T> clazz) {
		Document parseDocument = parseDocument(document);
		return map(parseDocument, new Elements(parseDocument), clazz);
	}

	public static Object map(Document document, Elements elements, Type type) {
		if (type instanceof ParameterizedType) {
			ParameterizedType parameterizedType = (ParameterizedType) type;

			// Handle type parameter class
			Class<?> classArgument = (Class<?>) parameterizedType
					.getActualTypeArguments()[0];

			// Handle type class
			Class<?> rowClass = (Class<?>) parameterizedType.getRawType();

			if (Collection.class.isAssignableFrom(rowClass)) {

				return mapToList(document, elements, classArgument);

			} else {
				log.debug(rowClass.getCanonicalName() + " is not a collection");

				throw new IllegalArgumentException(
						"Parameterized type not handled: "
								+ rowClass.getCanonicalName());
			}
		} else {
			return map(document, elements, (Class<?>) type);
		}
	}

	public static <T> List<T> mapToList(Document document, Elements element,
			Class<T> clazz) {
		List<T> result = new ArrayList<T>();

		Elements elementsCur = element;

		log.debug(element.toString());

		Select selector = clazz.getAnnotation(Select.class);
		if (selector != null) {
			log.debug(selector.value());

			elementsCur = element.select(selector.value());
		}

		for (Element elt : elementsCur) {
			result.add(map(document, new Elements(elt), clazz));
		}

		return result;
	}

	public static <T> T map(Document document, Elements elements,
			Class<T> clazz) {
		try {

			Elements elementsCurr = elements;

			Select aSelect = clazz.getAnnotation(Select.class);
			if (aSelect != null) {
				elementsCurr = SelectEvaluator.select(aSelect, elements, clazz);
			} else {
				log.debug("no Select found, using root element");
			}

			return map(document, elementsCurr, clazz, clazz.newInstance());
		} catch (InstantiationException | IllegalAccessException e) {
			log.error(e.getMessage(), e);

			throw new IllegalArgumentException(
					"Cant create new instance of " + clazz.getCanonicalName(),
					e);

		}
	}

	public static <T> T map(Document document, Elements element, Class<T> clazz,
			T target) {

		List<Field> allFields = getAllFields(target.getClass());
		for (Field field : allFields) {
			map(document, element, field, target);
		}

		Method[] methods = target.getClass().getMethods();
		for (Method method : methods) {
			map(document, element, method, target);
		}

		return target;
	}

	private static <T> void map(Document document, Elements elements,
			AccessibleObject member, T target) {

		if (hasOneAnnotationMapper(member)) {
			Boolean optional = false;

			Optional aOptional = member.getAnnotation(Optional.class);
			if (aOptional != null) {
				optional = aOptional.value();
			}


			Elements elementsCurr = elements;

			Select aSelect = member.getAnnotation(Select.class);
			if (aSelect != null) {
				elementsCurr = SelectEvaluator.select(aSelect, elements, member);
			} else {
				log.debug("no Select found, using root element");
			}

			Object value = reduce(document, elementsCurr, member);

			if (!optional && value == null) {
				throw new IllegalStateException(
						"No value matched and not optional for "
								+ getName(member));
			} else {
				mapFieldOrArgument(document, elementsCurr, target, member,
						value);
			}
		}

	}

	private static boolean hasOneAnnotationMapper(AccessibleObject member) {
		Annotation[] annotations = member.getAnnotations();
		for (Annotation annotation : annotations) {
			if (annotation.annotationType().getCanonicalName()
					.startsWith(PACKAGE_NAME)) {
				return true;
			}
		}
		return false;
	}


	/**
	 * Apply all rules (annotations) of type reduce.
	 * 
	 * @see Attr
	 * @see Text
	 * @see Title
	 * @see TagName
	 * @see Val
	 * 
	 * @param elementSelected
	 * @param member
	 * @return
	 */
	private static Object reduce(Document document, Elements elements,
			AnnotatedElement member) {

		Object value = elements;

		Attr aAttr = member.getAnnotation(Attr.class);
		if (aAttr != null) {

			log.debug("'{}' attr", getName(member));

			log.debug("using attr()", getName(member));

			value = elements.attr(aAttr.value());
		}

		Val aVal = member.getAnnotation(Val.class);
		if (aVal != null) {

			log.debug("'{}' val", getName(member));

			log.debug("using val()", getName(member));

			value = elements.val();
		}

		TagName aTagName = member.getAnnotation(TagName.class);
		if (aTagName != null) {

			log.debug("'{}' tagName", getName(member));

			log.debug("using tagName()", getName(member));

			value = elements.first().tagName();
		}

		Title aTitle = member.getAnnotation(Title.class);
		if (aTitle != null) {

			log.debug("'{}' title", getName(member));

			log.debug("using title()", getName(member));

			value = document.title();
		}

		Text aText = member.getAnnotation(Text.class);
		if (aText != null) {
			log.debug("'{}' text", getName(member));

			Elements currElts = SelectEvaluator.select(aText,elements, member);

			log.trace("text in  '{}'", currElts);
			if (aText.own()) {
				log.debug("using first().owntext()");
				value = currElts.first().ownText();
			} else {
				log.debug("using text()");
				value = currElts.text();
			}

			log.trace("text out  '{}'", value);
		}

		return value;
	}

	protected static Object getName(AnnotatedElement member) {
		return ClassUtils.getName(member);
	}

	protected static void mapFieldOrArgument(Document document,
			Elements elements, Object target, AccessibleObject member,
			Object value) {
		try {
			// Get target Type of
			// - Field
			// - Method arguments
			// Evaluate target type, trigger recursive evaluation if necessary
			// set the value to the target
			if (member instanceof Field) {
				Field field = (Field) member;

				Type fieldType = field.getGenericType();

				Object values = evaluateArguments(document, elements, value,
						fieldType)[0];

				setValue(document, elements, target, field, values);

			} else if (member instanceof Method) {
				Method method = (Method) member;

				Type[] parameterTypes = method.getGenericParameterTypes();

				Object[] values = evaluateArguments(document, elements, value,
						parameterTypes);

				setValue(document, elements, target, method, values);
			}

		} catch (IllegalArgumentException | IllegalAccessException
				| InvocationTargetException e) {
			throw new IllegalArgumentException("Cannot set value [" + value
					+ "|"+value.getClass()+"] for member [" + getName(member) + "]", e);
		}

	}

	public static final List<TypeConvertor> convertors = new ArrayList<>();
	static {
		convertors.add(new PrimitiveTypeConvertor());
		convertors.add(new StandardTypeConvertor());
		convertors.add(new RecursiveMappingTypeConvertor());
	}

	/**
	 * Prepares futures method arguments (or field value) depending of ordered
	 * expected types.
	 * 
	 * @param document
	 * @param elements
	 * @param value
	 *            The current value of the DOM after selects and reduces
	 * @param targetsType
	 * 
	 * @return Arguments ordered giving method signature aka <code>argumentsType
	 *         <code>
	 */
	protected static Object[] evaluateArguments(Document document,
			Elements elements, Object value, Type... targetsType) {
		Object[] arguments = new Object[targetsType.length];

		for (int i = 0; i < targetsType.length; i++) {
			Type targetType = targetsType[i];

			Class<?> targetRawType = ClassUtils.rawType(targetType);

			for (TypeConvertor convertor : convertors) {
				if (convertor instanceof ConvertorContext) {
					ConvertorContext convertorContext = (ConvertorContext) convertor;
					convertorContext.setDocument(document);
					convertorContext.setElements(elements);
					convertorContext.setGenericTargetType(targetType);
				}
				if (convertor.canConvert(targetRawType, value)) {

					arguments[i] = convertor.convert(targetRawType, value);

					break;
				}
			}
		}
		return arguments;
	}

	protected static void setValue(Document document, Elements element,
			Object target, Field field, Object value)
					throws IllegalArgumentException, IllegalAccessException {

		log.debug("set value on field ['{}' => '{}']", value, field.getName());

		// If both types matchs
		if (ClassUtils.isAssignableValue(field.getType(), value)) {

			// TODO call setter first if exists
			field.setAccessible(true);

			field.set(target, value);

		} else {
			log.error("set value is not assignable with field");
			throw new IllegalArgumentException(
					"Cant set " + value.toString() + " to " + field.getName());
		}

	}

	protected static void setValue(Document document, Elements element,
			Object target, Method method, Object... values)
					throws IllegalArgumentException, IllegalAccessException,
					InvocationTargetException {

		log.debug("set value by method [{} ({})]", method.getName(), values);

		method.invoke(target, values);
	}

	public static List<Field> getAllFields(Class<?> clazz) {

		List<Field> res = new ArrayList<Field>();

		Class<?> index = clazz;
		while (index != Object.class) {
			res.addAll(Arrays.asList(index.getDeclaredFields()));

			index = index.getSuperclass();
		}

		return res;
	}

	protected Document parseDocument(InputStream document) {
		try {
			return Jsoup.parse(document, encoding, baseURI);
		} catch (IOException e) {
			log.error(e.getMessage(), e);
		}
		return null;
	}

	public static class RecursiveMappingTypeConvertor
	implements TypeConvertor, ConvertorContext {

		@Setter
		private Document document;
		@Setter
		private Elements elements;
		@Setter
		private Type genericTargetType;

		@Override
		public boolean canConvert(Class<?> targetType, Object value) {
			return true;
		}

		@Override
		public Object convert(Class<?> targetType, Object value) {
			log.debug("evaluate argument by recursive mapping");
			return map(document, elements, genericTargetType);
		}

	}
}
