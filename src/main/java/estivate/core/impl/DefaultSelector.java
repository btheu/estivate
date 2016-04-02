package estivate.core.impl;

import java.lang.reflect.AccessibleObject;

import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import estivate.annotations.Select;
import estivate.core.SelectEvaluator;
import estivate.core.Selector;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DefaultSelector implements Selector {

    @Override
    public Elements select(Document document, Elements elements,
            AccessibleObject member) {

        Elements results = elements;

        Select aSelect = member.getAnnotation(Select.class);
        if (aSelect == null) {
            log.debug("No Select found, using root element");
        } else {
            results = SelectEvaluator.select(aSelect, elements, member);
        }

        return results;
    }

}
