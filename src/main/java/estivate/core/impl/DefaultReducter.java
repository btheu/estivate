package estivate.core.impl;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.AnnotatedElement;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import estivate.annotations.Attr;
import estivate.annotations.TagName;
import estivate.annotations.Text;
import estivate.annotations.Title;
import estivate.annotations.Val;
import estivate.core.ClassUtils;
import estivate.core.Reducter;
import estivate.core.SelectEvaluater;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Deprecated
public class DefaultReducter implements Reducter {

	/*
	 * (non-Javadoc)
	 * 
	 * @see estivate.core.Reducter#reduce(org.jsoup.nodes.Document,
	 * org.jsoup.select.Elements, java.lang.reflect.AccessibleObject, boolean)
	 */
	public Object reduce(Document document, Elements elementsIn, AccessibleObject member, boolean isTargetList) {

		Object value = elementsIn;

		Attr aAttr = member.getAnnotation(Attr.class);
		if (aAttr != null) {

			Elements elementsOut = SelectEvaluater.select(aAttr, elementsIn, member);
			if (elementsOut.isEmpty()) {
				return null;
			}

			log.debug("'{}' attr", getName(member));

			if (isTargetList) {
				List<String> list = new ArrayList<String>();

				for (Element element : elementsOut) {
					list.add(element.attr(aAttr.value()));
				}

				value = list;
			} else {
				if (elementsOut.size() > 1) {
					log.warn(
							"'{}' attr using first element. Consider fixing the select expression to get only one element.",
							getName(member));
				}
				log.trace("attr in  '{}'", elementsOut);

				log.debug("using attr()", getName(member));

				StringBuilder sb = new StringBuilder(50);
				for (Element element : elementsOut) {
					if (sb.length() != 0)
						sb.append(" ");
					sb.append(element.attr(aAttr.value()));
				}

				value = sb.toString();
			}
			log.trace("attr out  '{}'", value);
		}

		Val aVal = member.getAnnotation(Val.class);
		if (aVal != null) {

			Elements elementsOut = SelectEvaluater.select(aVal, elementsIn, member);
			if (elementsOut.isEmpty()) {
				return null;
			}

			log.debug("'{}' val", getName(member));

			if (isTargetList) {
				List<String> list = new ArrayList<String>();

				for (Element element : elementsOut) {
					list.add(element.val());
				}

				value = list;
			} else {
				if (elementsOut.size() > 1) {
					log.warn(
							"'{}' val using first element. Consider fixing the select expression to get only one element.",
							getName(member));
				}
				log.trace("val in  '{}'", elementsOut);

				log.debug("using val()", getName(member));

				StringBuilder sb = new StringBuilder(50);
				for (Element element : elementsOut) {
					if (sb.length() != 0)
						sb.append(" ");
					sb.append(element.val());
				}

				value = sb.toString();
			}
			log.trace("val out  '{}'", value);
		}

		TagName aTagName = member.getAnnotation(TagName.class);
		if (aTagName != null) {

			Elements elementsOut = SelectEvaluater.select(aTagName, elementsIn, member);
			if (elementsOut.isEmpty()) {
				return null;
			}

			log.debug("'{}' tagName", getName(member));

			if (isTargetList) {
				List<String> list = new ArrayList<String>();

				for (Element element : elementsOut) {
					list.add(element.tagName());
				}

				value = list;
			} else {
				if (elementsOut.size() > 1) {
					log.warn(
							"'{}' tagName using first element. Consider fixing the select expression to get only one element.",
							getName(member));
				}
				log.trace("tagName in  '{}'", elementsOut);

				log.debug("using tagName()", getName(member));

				StringBuilder sb = new StringBuilder(50);
				for (Element element : elementsOut) {
					if (sb.length() != 0)
						sb.append(" ");
					sb.append(element.tagName());
				}

				value = sb.toString();
			}
			log.trace("tagName out  '{}'", value);
		}

		Title aTitle = member.getAnnotation(Title.class);
		if (aTitle != null) {

			log.debug("'{}' title", getName(member));

			log.debug("using title()", getName(member));

			value = document.title();
		}

		Text aText = member.getAnnotation(Text.class);
		if (aText != null) {

			Elements elementsOut = SelectEvaluater.select(aText, elementsIn, member);
			if (elementsOut.isEmpty()) {
				return null;
			}

			log.debug("'{}' text", getName(member));

			if (isTargetList) {
				List<String> list = new ArrayList<String>();
				if (aText.own()) {
					for (Element element : elementsOut) {
						list.add(element.ownText());
					}
				} else {
					for (Element element : elementsOut) {
						list.add(element.text());
					}
				}
				value = list;
			} else {
				if (elementsOut.size() > 1) {
					log.warn(
							"'{}' text using first element. Consider fixing the select expression to get only one element.",
							getName(member));
				}
				log.trace("text in  '{}'", elementsOut);
				if (aText.own()) {
					log.debug("using simple owntext()");

					StringBuilder sb = new StringBuilder(50);
					for (Element element : elementsOut) {
						if (sb.length() != 0)
							sb.append(" ");
						sb.append(element.ownText());
					}

					value = sb.toString();
				} else {
					log.debug("using simple text()");
					value = elementsOut.text();
				}
			}

			log.trace("text out  '{}'", value);
		}

		return value;
	}

	protected static Object getName(AnnotatedElement member) {
		return ClassUtils.getName(member);
	}

}
