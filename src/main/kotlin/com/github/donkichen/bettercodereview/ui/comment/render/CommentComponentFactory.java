package com.github.donkichen.bettercodereview.ui.comment.render;

import com.intellij.codeInsight.documentation.DocumentationComponent;
import com.intellij.openapi.editor.DefaultLanguageHighlighterColors;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.colors.EditorColorsScheme;
import com.intellij.openapi.editor.impl.EditorCssFontResolver;
import com.intellij.openapi.editor.markup.TextAttributes;
import com.intellij.ui.ColorUtil;
import com.intellij.ui.scale.JBUIScale;
import com.intellij.util.ui.JBHtmlEditorKit;
import com.intellij.util.ui.JBUI;
import com.intellij.util.ui.UIUtil;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import javax.swing.event.HyperlinkEvent;
import javax.swing.text.EditorKit;
import javax.swing.text.html.HTMLEditorKit;
import javax.swing.text.html.StyleSheet;
import java.awt.*;
import java.awt.font.TextAttribute;
import java.io.IOException;
import java.io.StringReader;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * 大部分代码来自 {@link com.intellij.codeInsight.documentation.render.DocRenderer}
 */
public class CommentComponentFactory {
    private static StyleSheet ourCachedStyleSheet;
    private static String ourCachedStyleSheetLinkColor = "non-existing";

    public static JEditorPane newComponent(@NotNull Editor editor, @Nls @NotNull String text, boolean reusable) {
        JEditorPane pane = new JEditorPane();
        pane.setEditable(false);
        pane.getCaret().setSelectionVisible(!reusable);
        pane.putClientProperty("caretWidth", 0); // do not reserve space for caret (making content one pixel narrower than component)
        pane.setEditorKit(createEditorKit(editor));
        pane.setBorder(JBUI.Borders.empty());
        Map<TextAttribute, Object> fontAttributes = new HashMap<>();
        fontAttributes.put(TextAttribute.SIZE, JBUIScale.scale(DocumentationComponent.getQuickDocFontSize().getSize()));
        // disable kerning for now - laying out all fragments in a file with it takes too much time
        fontAttributes.put(TextAttribute.KERNING, 0);
        pane.setFont(pane.getFont().deriveFont(fontAttributes));
        Color textColor = getTextColor(editor.getColorsScheme());
        pane.setForeground(textColor);
        pane.setSelectedTextColor(textColor);
        pane.setSelectionColor(editor.getSelectionModel().getTextAttributes().getBackgroundColor());
        UIUtil.enableEagerSoftWrapping(pane);
        pane.setText(text);
        pane.addHyperlinkListener(e -> {
            if (e.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {
                //TODO 点击事件
//                activateLink(e);
            }
        });
        return pane;
    }


    private static EditorKit createEditorKit(@NotNull Editor editor) {
        HTMLEditorKit editorKit = new JBHtmlEditorKit();
        editorKit.getStyleSheet().addStyleSheet(getStyleSheet(editor));
        return editorKit;
    }

    private static StyleSheet getStyleSheet(@NotNull Editor editor) {
        EditorColorsScheme colorsScheme = editor.getColorsScheme();
        Color linkColor = colorsScheme.getColor(DefaultLanguageHighlighterColors.DOC_COMMENT_LINK);
        if (linkColor == null) linkColor = getTextColor(colorsScheme);
        String linkColorHex = ColorUtil.toHex(linkColor);
        if (!Objects.equals(linkColorHex, ourCachedStyleSheetLinkColor)) {
            String editorFontNamePlaceHolder = EditorCssFontResolver.EDITOR_FONT_NAME_PLACEHOLDER;
            ourCachedStyleSheet = new StyleSheet();
            try {
                ourCachedStyleSheet.loadRules(new StringReader(
                        "body {overflow-wrap: anywhere}" + // supported by JetBrains Runtime
                                "code {font-family: \"" + editorFontNamePlaceHolder + "\"}" +
                                "pre {font-family: \"" + editorFontNamePlaceHolder + "\";" +
                                "white-space: pre-wrap}" + // supported by JetBrains Runtime
                                "h1, h2, h3, h4, h5, h6 {margin-top: 0; padding-top: 1}" +
                                "a {color: #" + linkColorHex + "; text-decoration: none}" +
                                "p {padding: 7 0 2 0}" +
                                "ol {padding: 0 20 0 0}" +
                                "ul {padding: 0 20 0 0}" +
                                "li {padding: 1 0 2 0}" +
                                "li p {padding-top: 0}" +
                                "table p {padding-bottom: 0}" +
                                "th {text-align: left}" +
                                "td {padding: 2 0 2 0}" +
                                "td p {padding-top: 0}" +
                                ".sections {border-spacing: 0}" +
                                ".section {padding-right: 5; white-space: nowrap}" +
                                ".content {padding: 2 0 2 0}"
                ), null);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            ourCachedStyleSheetLinkColor = linkColorHex;
        }
        return ourCachedStyleSheet;
    }

    private static @NotNull Color getTextColor(@NotNull EditorColorsScheme scheme) {
        TextAttributes attributes = scheme.getAttributes(DefaultLanguageHighlighterColors.DOC_COMMENT);
        Color color = attributes == null ? null : attributes.getForegroundColor();
        return color == null ? scheme.getDefaultForeground() : color;
    }
}
