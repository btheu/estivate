package estivate.core.impl;

import java.lang.reflect.AccessibleObject;

import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import estivate.annotations.Select;
import estivate.core.SelectEvaluater;
import estivate.core.Selecter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DefaultSelecter implements Selecter {

	public Elements select(Document document, Elements elements, AccessibleObject member) {

		Elements results = elements;

		Select aSelect = member.getAnnotation(Select.class);
		if (aSelect == null) {
			log.debug("No Select found, using root element");
		} else {
			results = SelectEvaluater.select(aSelect, elements, member);
		}

		return results;
	}

}
