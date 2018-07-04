package me.ele.uetool;

import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.util.Pair;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import me.ele.uetool.base.Element;
import me.ele.uetool.base.IAttrs;
import me.ele.uetool.base.item.AddMinusEditItem;
import me.ele.uetool.base.item.BitmapItem;
import me.ele.uetool.base.item.EditTextItem;
import me.ele.uetool.base.item.Item;
import me.ele.uetool.base.item.SwitchItem;
import me.ele.uetool.base.item.TextItem;
import me.ele.uetool.base.item.TitleItem;

import static me.ele.uetool.base.DimenUtil.px2dip;
import static me.ele.uetool.base.DimenUtil.px2sp;

public class UETCore implements IAttrs {

    @Override
    public List<Item> getAttrs(Element element, boolean usePxUnit) {
        List<Item> items = new ArrayList<>();

        View view = element.getView();

        items.add(new SwitchItem("px/dp", element, SwitchItem.Type.TYPE_PX_OR_DP, !usePxUnit));
        items.add(new SwitchItem("Move", element, SwitchItem.Type.TYPE_MOVE));
        items.add(new SwitchItem("ValidViews", element, SwitchItem.Type.TYPE_SHOW_VALID_VIEWS));

        IAttrs iAttrs = AttrsManager.createAttrs(view);
        if (iAttrs != null) {
            items.addAll(iAttrs.getAttrs(element, usePxUnit));
        }

        items.add(new TitleItem("COMMON"));
        items.add(new TextItem("Class", view.getClass().getName()));
        items.add(new TextItem("Id", Util.getResId(view)));
        items.add(new TextItem("ResName", Util.getResourceName(view.getId())));
        items.add(new TextItem("Clickable", Boolean.toString(view.isClickable()).toUpperCase()));
        items.add(new TextItem("Focused", Boolean.toString(view.isFocused()).toUpperCase()));

        Pair<String, String> sizeLabelAndText = makeSizeLabelAndText("Width", view.getWidth(), usePxUnit);
        items.add(new AddMinusEditItem(sizeLabelAndText.first, element, EditTextItem.Type.TYPE_WIDTH,
                sizeLabelAndText.second));

        sizeLabelAndText = makeSizeLabelAndText("Height", view.getHeight(), usePxUnit);
        items.add(new AddMinusEditItem(sizeLabelAndText.first, element, EditTextItem.Type.TYPE_HEIGHT,
                sizeLabelAndText.second));
        items.add(new TextItem("Alpha", String.valueOf(view.getAlpha())));
        Object background = Util.getBackground(view);
        if (background instanceof String) {
            items.add(new TextItem("Background", (String) background));
        } else if (background instanceof Bitmap) {
            items.add(new BitmapItem("Background", (Bitmap) background));
        }
        sizeLabelAndText = makeSizeLabelAndText("PaddingLeft", view.getPaddingLeft(), usePxUnit);
        items.add(new AddMinusEditItem(sizeLabelAndText.first, element, EditTextItem.Type.TYPE_PADDING_LEFT,
                sizeLabelAndText.second));

        sizeLabelAndText = makeSizeLabelAndText("PaddingTop", view.getPaddingTop(), usePxUnit);
        items.add(new AddMinusEditItem(sizeLabelAndText.first, element, EditTextItem.Type.TYPE_PADDING_TOP,
                sizeLabelAndText.second));

        sizeLabelAndText = makeSizeLabelAndText("PaddingRight", view.getPaddingRight(), usePxUnit);
        items.add(new AddMinusEditItem(sizeLabelAndText.first, element, EditTextItem.Type.TYPE_PADDING_RIGHT,
                sizeLabelAndText.second));

        sizeLabelAndText = makeSizeLabelAndText("PaddingBottom", view.getPaddingBottom(), usePxUnit);
        items.add(new AddMinusEditItem(sizeLabelAndText.first, element, EditTextItem.Type.TYPE_PADDING_BOTTOM,
                sizeLabelAndText.second));

        return items;
    }


    private static Pair<String, String> makeSizeLabelAndText(@NonNull final String prefixLabelName, final int sizeInPx,
                                                             final boolean usePxUnit) {
        String sizeLabel = prefixLabelName + (usePxUnit ? " (px) " : " (dp) ");
        String sizeText = usePxUnit ? String.valueOf(sizeInPx) : px2dip(sizeInPx);
        return new Pair<>(sizeLabel, sizeText);
    }


    static class AttrsManager {

        public static IAttrs createAttrs(View view) {
            if (view instanceof TextView) {
                return new UETTextView();
            } else if (view instanceof ImageView) {
                return new UETImageView();
            }
            return null;
        }
    }

    private static class UETTextView implements IAttrs {

        @Override
        public List<Item> getAttrs(Element element, boolean usePxUnit) {
            List<Item> items = new ArrayList<>();
            TextView textView = ((TextView) element.getView());
            items.add(new TitleItem("TextView"));
            items.add(new EditTextItem("Text", element, EditTextItem.Type.TYPE_TEXT, textView.getText().toString()));
            float textSizeInPx = textView.getTextSize();
            String textSizeText = usePxUnit ? String.valueOf((int)textSizeInPx) : px2sp(textSizeInPx);
            String textSizeLabel = usePxUnit ? "TextSize (px) " : "TextSize（sp）";
            items.add(new AddMinusEditItem(textSizeLabel, element, EditTextItem.Type.TYPE_TEXT_SIZE, textSizeText));
            items.add(new EditTextItem("TextColor", element, EditTextItem.Type.TYPE_TEXT_COLOR, Util.intToHexColor(textView.getCurrentTextColor())));
            List<Pair<String, Bitmap>> pairs = Util.getTextViewBitmap(textView);
            for (Pair<String, Bitmap> pair : pairs) {
                items.add(new BitmapItem(pair.first, pair.second));
            }
            items.add(new SwitchItem("IsBold", element, SwitchItem.Type.TYPE_IS_BOLD, textView.getTypeface() != null && textView.getTypeface().isBold()));
            return items;
        }
    }

    private static class UETImageView implements IAttrs {

        @Override
        public List<Item> getAttrs(Element element, boolean usePxUnit) {
            ImageView imageView = ((ImageView) element.getView());
            List<Item> items = new ArrayList<>();
            items.add(new TitleItem("ImageView"));
            items.add(new BitmapItem("Bitmap", Util.getImageViewBitmap(imageView)));
            items.add(new TextItem("ScaleType", Util.getImageViewScaleType(imageView)));
            return items;
        }
    }
}
