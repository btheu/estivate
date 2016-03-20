package neomcfly.jsoupmapper;

import java.io.IOException;
import java.io.InputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class JSoupMapper extends JSoupMapperOLD {

    @Getter
    @Setter
    protected String encoding = "UTF-8";

    @Getter
    @Setter
    protected String baseURI = "/";

    public <T> Collection<T> mapToList(InputStream document, Class<T> clazz) {
        return mapToList(parseDocument(document), clazz);
    }

    public <T> T map(InputStream document, Class<T> clazz) {
        return map(parseDocument(document), clazz);
    }

    public <T> Collection<T> mapToList(Element element, Class<T> clazz) {
        Collection<T> result = new ArrayList<T>();

        log.debug(element.toString());

        JSoupListSelect selector = clazz.getAnnotation(JSoupListSelect.class);
        if (selector == null) {
            throw new IllegalStateException("Class " + clazz.getCanonicalName()
                    + " does not have " + JSoupListSelect.class.getSimpleName()
                    + " annotation");
        }

        log.debug(selector.value());

        Elements elements = element.select(selector.value());
        for (Element currElement : elements) {
            result.add(map(currElement, clazz));
        }

        return result;
    }

    @Override
    public <T> T map(Element element, Class<T> clazz) {
        try {
            return map(element, clazz, clazz.newInstance());
        } catch (InstantiationException e) {
            log.error(e.getMessage(), e);
        } catch (IllegalAccessException e) {
            log.error(e.getMessage(), e);
        } catch (IllegalArgumentException e) {
            log.error(e.getMessage(), e);
        }
        return null;
    }

    public <T> T map(Element element, Class<T> clazz, T target) {

        List<Field> allFields = getAllFields(target.getClass());
        for (Field field : allFields) {
            map(element, field, target);
        }

        Method[] methods = target.getClass().getMethods();
        for (Method method : methods) {
            map(element, method, target);
        }

        return target;
    }

    private <T> void map(Element element, AccessibleObject member, T target) {

        if (hasOneAnnotationMapper(member)) {
            Boolean optional = false;

            JSoupOptional aOptional = member.getAnnotation(JSoupOptional.class);
            if (aOptional != null) {
                optional = aOptional.value();
            }

            Elements elementSelected = select(element, member);

            // TODO Handle here member of collection type
            Object value = reduce(elementSelected.first(), member);

            if (!optional && value == null) {
                throw new IllegalStateException(
                        "No value matched and not optional for "
                                + getName(member));
            } else {
                setValue(target, member, value);
            }
        }

    }

    private boolean hasOneAnnotationMapper(AccessibleObject member) {
        Annotation[] annotations = member.getAnnotations();
        for (Annotation annotation : annotations) {
            if (annotation.annotationType().getCanonicalName()
                    .startsWith("neomcfly.jsoupmapper")) {
                return true;
            }
        }
        return false;
    }

    /**
     * Apply all rules (annotations) of type select
     * 
     * @see JSoupSelect
     * 
     * @param element
     * @param member
     * @return
     */
    private Elements select(Element element, AccessibleObject member) {

        JSoupSelect selector = member.getAnnotation(JSoupSelect.class);
        if (selector == null) {
            log.debug("no JSoupSelect found, using root element");
            return new Elements(element);
        }

        return element.select(selector.value());
    }

    /**
     * Apply all rules (annotations) of type reduce.
     * 
     * @see JSoupAttr
     * @see JSoupText
     * @see JSoupTitle
     * @see JSoupTagName
     * @see JSoupVal
     * 
     * @param elementSelected
     * @param member
     * @return
     */
    private Object reduce(Element element, AccessibleObject member) {

        Object value = element;

        JSoupAttr aAttr = member.getAnnotation(JSoupAttr.class);
        if (aAttr != null) {

            log.debug("{} attr", getName(member));

            log.debug("using attr()", getName(member));

            value = element.attr(aAttr.value());
        }

        JSoupText aText = member.getAnnotation(JSoupText.class);
        if (aText != null) {
            log.debug("{} text", getName(member));

            if (aText.value() || aText.own()) {
                log.debug("using owntext()");
                value = element.ownText();
            } else {
                log.debug("using text()");
                value = element.text();
            }
        }

        // TODO throw exeption if not optional and no result
        return value;
    }

    protected Element parseDocument(InputStream document) {
        try {
            return Jsoup.parse(document, encoding, baseURI);
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
        return null;
    }

}
