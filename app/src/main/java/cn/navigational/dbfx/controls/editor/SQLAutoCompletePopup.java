package cn.navigational.dbfx.controls.editor;

import cn.navigational.dbfx.kit.KeywordHelper;
import cn.navigational.dbfx.kit.enums.Clients;
import impl.org.controlsfx.skin.AutoCompletePopup;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.geometry.Bounds;
import org.fxmisc.richtext.CodeArea;
import org.fxmisc.richtext.GenericStyledArea;
import org.fxmisc.richtext.model.Paragraph;
import org.fxmisc.richtext.model.StyleSpans;
import org.fxmisc.richtext.model.StyleSpansBuilder;
import org.reactfx.EventStream;
import org.reactfx.EventStreams;
import org.reactfx.collection.ListModification;
import org.reactfx.value.Var;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static org.reactfx.EventStreams.nonNullValuesOf;

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
    private final Var<Boolean> showWhenItemOutsideViewport = Var.newSimpleVar(true);
    private final Pattern PATTERN;
    private static final String PAREN_PATTERN = "\\(|\\)";
    private static final String BRACE_PATTERN = "\\{|\\}";
    private static final String BRACKET_PATTERN = "\\[|\\]";
    private static final String SEMICOLON_PATTERN = "\\;";
    private static final String STRING_PATTERN = "\"([^\"\\\\]|\\\\.)*\"";
    private static final String COMMENT_PATTERN = "//[^\n]*" + "|" + "/\\*(.|\\R)*?\\*/" + "|" + "/\\*[^\\v]*" + "|" + "^\\h*\\*([^\\v]*|/)";


    public final EventStream<Boolean> outsideViewportValues() {
        return showWhenItemOutsideViewport.values();
    }


    public SQLAutoCompletePopup(Clients cl, CodeArea codeArea) {
        this.prefHeight(200);
        this.setAutoHide(false);
        this.codeArea = codeArea;
        this.keywords = KeywordHelper.getKeyWordSyn(cl);
        final List<String> lowKeyword = this.keywords.stream()
                .map(String::toLowerCase).collect(Collectors.toList());
        final String keywordPattern = "\\b(" + String.join("|", keywords) +'|'+
                String.join("|", lowKeyword) + ")\\b";
        this.PATTERN = Pattern.compile(
                "(?<KEYWORD>" + keywordPattern + ")"
                        + "|(?<PAREN>" + PAREN_PATTERN + ")"
                        + "|(?<BRACE>" + BRACE_PATTERN + ")"
                        + "|(?<BRACKET>" + BRACKET_PATTERN + ")"
                        + "|(?<SEMICOLON>" + SEMICOLON_PATTERN + ")"
                        + "|(?<STRING>" + STRING_PATTERN + ")"
                        + "|(?<COMMENT>" + COMMENT_PATTERN + ")"
        );
        codeArea.getVisibleParagraphs().addModificationObserver
                (
                        new VisibleParagraphStyler<>(codeArea, this::computeHighlighting)
                );
        this.setOnSuggestion(e -> {
            codeArea.replaceText(start.get(), end.get(), e.getSuggestion());
            this.hide();
        });
        EventStream<Optional<Bounds>> caretBounds = nonNullValuesOf(codeArea.caretBoundsProperty());
        EventStreams.combine(caretBounds, outsideViewportValues()).subscribe(tuple3 -> {
            Optional<Bounds> opt = tuple3._1;
            if (opt.isEmpty()) {
                return;
            }
            Bounds b = opt.get();
            this.setX(b.getMaxX());
            this.setY(b.getMaxY());
        });
        codeArea.textProperty().addListener(this.listener);
    }

    private StyleSpans<Collection<String>> computeHighlighting(String text) {
        Matcher matcher = PATTERN.matcher(text);
        int lastKwEnd = 0;
        StyleSpansBuilder<Collection<String>> spansBuilder
                = new StyleSpansBuilder<>();
        while (matcher.find()) {
            String styleClass =
                    matcher.group("KEYWORD") != null ? "keyword" :
                            matcher.group("PAREN") != null ? "paren" :
                                    matcher.group("BRACE") != null ? "brace" :
                                            matcher.group("BRACKET") != null ? "bracket" :
                                                    matcher.group("SEMICOLON") != null ? "semicolon" :
                                                            matcher.group("STRING") != null ? "string" :
                                                                    matcher.group("COMMENT") != null ? "comment" :
                                                                            null; /* never happens */
            assert styleClass != null;
            spansBuilder.add(Collections.emptyList(), matcher.start() - lastKwEnd);
            spansBuilder.add(Collections.singleton(styleClass), matcher.end() - matcher.start());
            lastKwEnd = matcher.end();
        }
        spansBuilder.add(Collections.emptyList(), text.length() - lastKwEnd);
        return spansBuilder.create();
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
            if (!keyword.equals(key) && keyword.startsWith(key)) {
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


    private class VisibleParagraphStyler<PS, SEG, S> implements Consumer<ListModification<? extends Paragraph<PS, SEG, S>>> {
        private final GenericStyledArea<PS, SEG, S> area;
        private final Function<String, StyleSpans<S>> computeStyles;
        private int prevParagraph, prevTextLength;

        public VisibleParagraphStyler(GenericStyledArea<PS, SEG, S> area, Function<String, StyleSpans<S>> computeStyles) {
            this.computeStyles = computeStyles;
            this.area = area;
        }

        @Override
        public void accept(ListModification<? extends Paragraph<PS, SEG, S>> lm) {
            if (lm.getAddedSize() > 0) {
                int paragraph = Math.min(area.firstVisibleParToAllParIndex() + lm.getFrom(), area.getParagraphs().size() - 1);
                String text = area.getText(paragraph, 0, paragraph, area.getParagraphLength(paragraph));

                if (paragraph != prevParagraph || text.length() != prevTextLength) {
                    int startPos = area.getAbsolutePosition(paragraph, 0);
                    Platform.runLater(() -> area.setStyleSpans(startPos, computeStyles.apply(text)));
                    prevTextLength = text.length();
                    prevParagraph = paragraph;
                }
            }
        }
    }

    public void dispose() {
        this.codeArea.textProperty().removeListener(this.listener);
    }
}
