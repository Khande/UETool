package me.ele.uetool.base.item;

import me.ele.uetool.base.Element;

public class AddMinusEditItem extends EditTextItem {

    public AddMinusEditItem(String name, Element element, int type, String detail) {
        super(name, element, type, detail);
    }

    public AddMinusEditItem(String name, Element element, int type, String detail, boolean usePxUnit) {
        super(name, element, type, detail, usePxUnit);
    }
}
