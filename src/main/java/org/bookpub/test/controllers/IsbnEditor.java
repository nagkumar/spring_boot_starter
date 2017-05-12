package org.bookpub.test.controllers;

import org.springframework.util.StringUtils;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;

import java.beans.PropertyEditorSupport;

/**
 * Created by reddaiah on 5/12/2017.
 */
public class IsbnEditor extends PropertyEditorSupport {
    @Override
    public void setAsText(String text) throws IllegalArgumentException {
        if (StringUtils.hasText(text)) {
            setValue(new Isbn(text.trim()));
        }
        else {
            setValue(null);
        }
    }
    @Override
    public String getAsText() {
        Isbn isbn = (Isbn) getValue();
        if (isbn != null) {
            return isbn.getIsbn();
        }
        else {
            return "";
        }
    }

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.registerCustomEditor(Isbn.class, new IsbnEditor());
    }
}