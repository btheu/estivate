package estivate.core;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;

import org.jsoup.helper.StringUtil;
import org.jsoup.select.Elements;

import estivate.annotations.Attr;
import estivate.annotations.Select;
import estivate.annotations.TagName;
import estivate.annotations.Text;
import estivate.annotations.Val;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

/**
 * 
 * @author Benoit Theunissen
 *
 */
@Slf4j
public abstract class SelectEvaluator {

	public static Elements select(SelectBean bean, Elements elementsIn, AnnotatedElement member) {

		log.debug("selecting '{}' with '{}' for '{}'",bean.getSelect(), bean.getName(), ClassUtils.getName(member));
		log.trace("select in '{}'", elementsIn.toString());

		Elements elementsOut = elementsIn.select(bean.getSelect());


		if(bean.isFirst() && bean.isLast()){
			throw new IllegalArgumentException("Select cant be true for first() and last() a the same time");
		}
		if(bean.isFirst()){
			log.trace("select first element");
			elementsOut = new Elements(elementsOut.first());
		}
		if(bean.isLast()){
			log.trace("select last element");
			elementsOut = new Elements(elementsOut.last());
		}
		log.trace("select out '{}'", elementsOut.toString());
		
		return elementsOut;
	}


	public static Elements select(Text annotation, Elements elements, AnnotatedElement member) {
		return select(new SelectBean(annotation),elements, member);
	}
	public static Elements select(Select annotation, Elements elements, AnnotatedElement member) {
		return select(new SelectBean(annotation),elements, member);
	}
	public static Elements select(Attr annotation, Elements elements, AnnotatedElement member) {
		return select(new SelectBean(annotation),elements, member);
	}
	public static Elements select(TagName annotation, Elements elements, AnnotatedElement member) {
		return select(new SelectBean(annotation),elements, member);
	}
	public static Elements select(Val annotation, Elements elements, AnnotatedElement member) {
		return select(new SelectBean(annotation),elements, member);
	}



	@Data
	public static class SelectBean {

		/**
		 * Hold the name of the original annotation
		 */
		protected String name;

		protected String select;

		protected int index;

		protected boolean unique;

		protected boolean first;

		protected boolean last;

		protected boolean optional;

		public SelectBean(Select annotation) {
			setAnnotation(annotation);
			this.select = or(annotation.value(), annotation.select());
			this.index = annotation.index();
			this.unique = annotation.unique();
			this.first = annotation.first();
			this.last = annotation.last();
			this.optional = annotation.optional();
		}

		public SelectBean(Text annotation) {
			setAnnotation(annotation);
			this.select = annotation.select();
			this.index = annotation.index();
			this.unique = annotation.unique();
			this.first = annotation.first();
			this.last = annotation.last();
			this.optional = annotation.optional();
		}

		public SelectBean(Attr annotation) {
			setAnnotation(annotation);
			this.select = annotation.select();
			this.index = annotation.index();
			this.unique = annotation.unique();
			this.first = annotation.first();
			this.last = annotation.last();
			this.optional = annotation.optional();
		}

		public SelectBean(TagName annotation) {
			setAnnotation(annotation);
			this.select = annotation.select();
			this.index = annotation.index();
			this.unique = annotation.unique();
			this.first = annotation.first();
			this.last = annotation.last();
			this.optional = annotation.optional();
		}

		public SelectBean(Val annotation) {
			setAnnotation(annotation);
			this.select = annotation.select();
			this.index = annotation.index();
			this.unique = annotation.unique();
			this.first = annotation.first();
			this.last = annotation.last();
			this.optional = annotation.optional();
		}

		protected void setAnnotation(Annotation annotation) {
			this.name = annotation.annotationType().getSimpleName();
		}

		private String or(String value1, String value2) {
			return StringUtil.isBlank(value1) ? value2 : value1;
		}
	}


}
