package cn.navigational.dbfx.controls.editor;

import cn.navigational.dbfx.kit.KeywordHelper;
import cn.navigational.dbfx.kit.enums.Clients;
import impl.org.controlsfx.skin.AutoCompletePopup;
import javafx.beans.value.ChangeListener;
import org.fxmisc.richtext.CodeArea;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * SQL auto complete popup
 *
 * @author yangkui
 * @since 1.0
 */
public class SQLAutoCompletePopup extends AutoCompletePopup<String> {
    private static final char BLACK_SYMBOL = ' ';
    private static final char WRAPPER_SYMBOL = '\n';
    private final CodeArea codeArea;
    private final List<String> keywords;
    private final AtomicInteger end = new AtomicInteger();
    private final AtomicInteger start = new AtomicInteger();
    private final ChangeListener<String> listener = ((observable, oldValue, newValue) -> this.findClosestWord(newValue));


    public SQLAutoCompletePopup(Clients cl, CodeArea codeArea) {
        this.codeArea = codeArea;
        this.keywords = KeywordHelper.getKeyWordSyn(cl);
        this.setOnSuggestion(e -> {
            codeArea.replaceText(start.get(), end.get(), e.getSuggestion());
            this.hide();
        });
        codeArea.textProperty().addListener(this.listener);
    }

    private void findClosestWord(String text) {
        var pos = codeArea.getCaretPosition();
        var str = text.substring(0, pos);
        var a = str.lastIndexOf(BLACK_SYMBOL);
        var b = str.lastIndexOf(WRAPPER_SYMBOL);
        var index = Math.max(a, b);
        if (index == -1) {
            index = 0;
        }
        var keyword = str.substring(index, pos).toUpperCase().trim();
        if ("".equals(keyword)) {
            this.hide();
            return;
        }
        char symbol = str.charAt(index);
        if (symbol == BLACK_SYMBOL || symbol == WRAPPER_SYMBOL) {
            index += 1;
        }
        if (this.containerKey(keyword)) {
            this.end.set(pos);
            this.start.set(index);
            if (!this.isShowing()) {
                this.show(codeArea);
            }
            //fix popup position
        } else {
            this.hide();
        }
    }

    /**
     * Determine whether the current dictionary contains the specified key value
     *
     * @param key Target key
     * @return If container true otherwise false
     */
    public boolean containerKey(String key) {
        final List<String> list = new ArrayList<>();
        for (String keyword : keywords) {
            if (keyword.startsWith(key)) {
                list.add(keyword);
            }
        }
        if (this.getSuggestions().size() > 0) {
            this.getSuggestions().clear();
        }
        boolean res = !list.isEmpty();
        if (res) {
            this.getSuggestions().addAll(list);
        }
        return res;
    }


    public void dispose() {
        this.codeArea.textProperty().removeListener(this.listener);
    }
}
