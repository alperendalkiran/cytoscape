package csplugins.test.widgets.test.unitTests.view;

import csplugins.widgets.autocomplete.index.Hit;
import csplugins.widgets.autocomplete.index.TextIndex;
import csplugins.widgets.autocomplete.index.TextIndexFactory;
import csplugins.widgets.autocomplete.view.AutoCompleteDocument;
import csplugins.widgets.autocomplete.view.ComboBoxFactory;
import csplugins.widgets.autocomplete.view.TextIndexComboBox;
import junit.framework.TestCase;

import javax.swing.text.Document;
import javax.swing.text.JTextComponent;
import javax.swing.text.SimpleAttributeSet;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

/**
 * JUnit Tests for the TextIndexComboBox.
 *
 * @author Ethan Cerami.
 */
public class TestTextIndexComboBox extends TestCase {

    /**
     * Tests various user operations on the TextIndexComboBox.
     *
     * @throws Exception All Errors.
     */
    public void testComboBox() throws Exception {
        TextIndex textIndex = createSampleTextIndex();
        TextIndexComboBox comboBox = ComboBoxFactory.
                createTextIndexComboBox(textIndex, 1.0);
        JTextComponent editor = (JTextComponent)
                comboBox.getEditor().getEditorComponent();

        //  Assuming no user input, what do I see in the pull-down?
        assertEquals(TextIndexComboBox.DEFAULT_MAX_HITS_SHOWN,
                comboBox.getItemCount());
        assertEquals("apple", comboBox.getItemAt(0).toString());
        assertEquals("bag", comboBox.getItemAt(1).toString());
        assertEquals("bat", comboBox.getItemAt(2).toString());
        assertEquals("burn", comboBox.getItemAt(9).toString());

        //  Assuming user enters "B", what do I see in the pull-down menu?
        Document doc = editor.getDocument();
        doc.insertString(0, "B", new SimpleAttributeSet());
        assertEquals(12, comboBox.getItemCount());
        assertEquals("bag", comboBox.getItemAt(0).toString());
        assertEquals("bat", comboBox.getItemAt(1).toString());
        assertEquals("beef", comboBox.getItemAt(2).toString());
        assertEquals("bust", comboBox.getItemAt(9).toString());

        //  What do I see in the text box?  should be first item in the list
        assertEquals("bag", editor.getText());

        //  Assuming user now enters "o", what do I see?
        doc.insertString(1, "o", new SimpleAttributeSet());
        assertEquals("bone", editor.getText());
        assertEquals(4, comboBox.getItemCount());
        assertEquals("bone", comboBox.getItemAt(0).toString());
        assertEquals("boney", comboBox.getItemAt(1).toString());
        assertEquals("boom", comboBox.getItemAt(2).toString());
        assertEquals("boon", comboBox.getItemAt(3).toString());

        //  Assuming user now deletes the last "o", possibly via backspace,
        //  what do I see?
        doc.remove(1, 1);
        assertEquals("bag", editor.getText());
        assertEquals(12, comboBox.getItemCount());
        assertEquals("bag", comboBox.getItemAt(0).toString());
        assertEquals("bat", comboBox.getItemAt(1).toString());
        assertEquals("beef", comboBox.getItemAt(2).toString());
        assertEquals("bust", comboBox.getItemAt(9).toString());

        //  Start over;  Assuming user enters "but", what do I see in
        //  the pull-down?
        doc.remove(0, doc.getLength());
        doc.insertString(0, "but", new SimpleAttributeSet());
        assertEquals(2, comboBox.getItemCount());
        assertEquals("butter", comboBox.getItemAt(0).toString());
        assertEquals("button", comboBox.getItemAt(1).toString());

        //  What do I see in the text box;  should be first item in the list
        assertEquals("butter", editor.getText());

        //  Assuming user hits Backspace a few times,
        //  e.g. erases the last 'tter', then what?
        doc.remove(2, 4);
        assertEquals(5, comboBox.getItemCount());
        assertEquals("bug", comboBox.getItemAt(0).toString());
        assertEquals("burn", comboBox.getItemAt(1).toString());
        assertEquals("bust", comboBox.getItemAt(2).toString());
        assertEquals("butter", comboBox.getItemAt(3).toString());
        assertEquals("button", comboBox.getItemAt(4).toString());
    }

    /**
     * Test explicit functionality re:  selecting items via cursor keys.
     *
     * @throws Exception All Errors.
     */
    public void testSelectionViaCursorKeys() throws Exception {
        TextIndex textIndex = createSampleTextIndex();
        TextIndexComboBox comboBox = ComboBoxFactory.
                createTextIndexComboBox(textIndex, 1.0);
        JTextComponent editor = (JTextComponent)
                comboBox.getEditor().getEditorComponent();
        AutoCompleteDocument doc = (AutoCompleteDocument) editor.getDocument();

        //  w/o cursor key selection;
        //  before selection: there should be 10 items in the pull-down
        //  after selection:  there should be 1 item in the pull-down
        doc.insertString(0, "B", new SimpleAttributeSet());
        int before = comboBox.getItemCount();
        comboBox.setSelectedItem("beef");
        int after = comboBox.getItemCount();
        assertEquals(12, before);
        assertEquals(1, after);

        //  w/o cursor key selection;
        //  before selection:  there should be 10 items in the pull-down
        //  after selection:  there should still be 10 items in the pull-down
        doc.remove(0, doc.getLength());
        doc.insertString(0, "B", new SimpleAttributeSet());
        before = comboBox.getItemCount();
        doc.setCursorKeyPressed(true);
        comboBox.setSelectedItem("beef");
        after = comboBox.getItemCount();
        assertEquals(12, before);
        assertEquals(12, after);
    }

    /**
     * Tests the FinalSelectionListener Framework.
     *
     * @throws Exception All Errors.
     */
    public void testFinalSelectionListenerFramework() throws Exception {
        TextIndex textIndex = createSampleTextIndex();
        TextIndexComboBox comboBox = ComboBoxFactory.
                createTextIndexComboBox(textIndex, 1.0);
        final ArrayList eventList = new ArrayList();
        comboBox.addFinalSelectionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                eventList.add(e);
                TextIndexComboBox comboBox = (TextIndexComboBox) e.getSource();
                Hit hit = (Hit) comboBox.getSelectedItem();
                assertEquals("apple", hit.getKeyword());
            }
        });
        //  Select an item from the pull-down menu
        comboBox.setSelectedIndex(0);

        //  Verify that event was received my listener
        assertEquals(1, eventList.size());
    }

    /**
     * Creates a Sample Text Index.
     *
     * @return TextIndex Object.
     */
    public static TextIndex createSampleTextIndex() {
        TextIndex textIndex = TextIndexFactory.createDefaultTextIndex();
        textIndex.addToIndex("Apple", new String());
        textIndex.addToIndex("Bat", new String());
        textIndex.addToIndex("Bat", new String());
        textIndex.addToIndex("Bat", new String());
        textIndex.addToIndex("Butter", new String());
        textIndex.addToIndex("Butter", new String());
        textIndex.addToIndex("Butter", new String());
        textIndex.addToIndex("Butter", new String());
        textIndex.addToIndex("Butter", new String());
        textIndex.addToIndex("Button", new String());
        textIndex.addToIndex("Button", new String());
        textIndex.addToIndex("Button", new String());
        textIndex.addToIndex("Button", new String());
        textIndex.addToIndex("Button", new String());
        textIndex.addToIndex("Button", new String());
        textIndex.addToIndex("Button", new String());
        textIndex.addToIndex("Button", new String());
        textIndex.addToIndex("Button", new String());
        textIndex.addToIndex("Button", new String());
        textIndex.addToIndex("Button", new String());
        textIndex.addToIndex("Button", new String());
        textIndex.addToIndex("Button", new String());
        textIndex.addToIndex("Button", new String());
        textIndex.addToIndex("Button", new String());
        textIndex.addToIndex("Button", new String());
        textIndex.addToIndex("Bug", new String());
        textIndex.addToIndex("Bag", new String());
        textIndex.addToIndex("Bust", new String());
        textIndex.addToIndex("Burn", new String());
        textIndex.addToIndex("Beef", new String());
        textIndex.addToIndex("Bone", new String());
        textIndex.addToIndex("Boon", new String());
        textIndex.addToIndex("Boney", new String());
        textIndex.addToIndex("Boom", new String());
        textIndex.addToIndex("Jello", new String());
        textIndex.addToIndex("Kramer", new String());
        textIndex.addToIndex("Lion", new String());
        textIndex.addToIndex("Mary", new String());
        textIndex.addToIndex("Nancy", new String());
        textIndex.addToIndex("Oprah", new String());
        textIndex.addToIndex("Queen", new String());
        textIndex.addToIndex("Round", new String());
        textIndex.addToIndex("Sam", new String());
        textIndex.addToIndex("Sweet", new String());
        textIndex.addToIndex("Swoon", new String());
        textIndex.addToIndex("Swimmer", new String());
        textIndex.addToIndex("Swish", new String());
        textIndex.addToIndex("Swig", new String());
        textIndex.addToIndex("Sammy", new String());
        textIndex.addToIndex("Tomorrow", new String());
        textIndex.addToIndex("Underground", new String());
        textIndex.addToIndex("Vector", new String());
        textIndex.addToIndex("World", new String());
        textIndex.addToIndex("Xerox", new String());
        textIndex.addToIndex("Yoyo", new String());
        textIndex.addToIndex("Zoo", new String());
        textIndex.addToIndex("Zoo_REALLY_REALLY_REALLY_REALLY_REALY_"
                + "LONG NAME", new String());
        return textIndex;
    }
}
