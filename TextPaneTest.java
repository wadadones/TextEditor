import javax.swing.*;
import javax.swing.text.*;
import javax.swing.event.*;
import java.awt.event.*;
import java.awt.*;

//import java.awt.Color;

public class TextPaneTest extends JFrame implements ActionListener, CaretListener{
	protected JTextPane textPane;

	protected DefaultStyledDocument doc;
	protected StyleContext sc;

	protected JToolBar toolBar;

	protected JComboBox<String> comboFonts;
	protected JComboBox<String> comboSizes;
	protected JToggleButton toggleB;
	protected JToggleButton toggleI;
	protected JToggleButton toggleU;
	protected JToggleButton toggleS;

	protected String currentFontName = "";
	protected int currentFontSize = 0;
	protected boolean flag = false;

	public static void main(String[] args){
		TextPaneTest test = new TextPaneTest();
		
		test.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		test.setVisible(true);
	}

	TextPaneTest(){
		setTitle("TextPaneTest Test");
		setBounds(10,10,500,200);

		initToolBar();
		getContentPane().add(toolBar, BorderLayout.NORTH);

		textPane = new JTextPane();
		JScrollPane scroll = new JScrollPane(textPane,
			JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
			JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		getContentPane().add(scroll, BorderLayout.CENTER);

		sc = new StyleContext();
		doc = new DefaultStyledDocument(sc);

		textPane.setDocument(doc);

		//set CaretListener
		textPane.addCaretListener(this);

		//read initial documents
		initDocument(doc, sc);

		//change style
		//changeStyle(doc);

	}

	protected void initDocument(DefaultStyledDocument doc, StyleContext sc){
		StringBuffer sb = new StringBuffer();
		sb.append("スタイル付きのテキストサンプルです。\n");
		sb.append("スタイルを変えて表示しています。");

		try{
			//insert documents
			doc.insertString(0, new String(sb),
				sc.getStyle(StyleContext.DEFAULT_STYLE));
		}catch(BadLocationException ble){
			System.err.println("初期文書の読み込みに失敗しました。");
		}
	}

	protected void initToolBar(){
		toolBar = new JToolBar();
		toolBar.setLayout(new FlowLayout(FlowLayout.LEFT));

		GraphicsEnvironment ge =
			GraphicsEnvironment.getLocalGraphicsEnvironment();
		String familyName[] = ge.getAvailableFontFamilyNames();

		comboFonts = new JComboBox<String>(familyName);
		comboFonts.setMaximumSize(comboFonts.getPreferredSize());
		comboFonts.addActionListener(this);
		comboFonts.setActionCommand("comboFonts");
		toolBar.add(comboFonts);

		
		comboSizes = new JComboBox<String>(new String[] {"8", "9", "10", 
    		"11", "12", "14", "16", "18", "20", "22", "24", "26", 
    		"28", "36", "48", "72"});

		comboSizes.setMaximumSize(comboSizes.getPreferredSize());
		comboSizes.addActionListener(this);
		comboSizes.setActionCommand("comboSizes");
		toolBar.add(comboSizes);

		toolBar.addSeparator();
	}

	public void actionPerformed(ActionEvent e){

		if(flag) return;

		String actionCommand = e.getActionCommand();
		MutableAttributeSet attr = new SimpleAttributeSet();

		if(actionCommand.equals("comboFonts")){
			String fontName = comboFonts.getSelectedItem().toString();
			StyleConstants.setFontFamily(attr, fontName);
		}else if(actionCommand.equals("comboSizes")){
			int fontSize = 0;
			try{
				fontSize = Integer.parseInt(comboSizes.
					getSelectedItem().toString());
			}catch(NumberFormatException ex){
				return;
			}
			StyleConstants.setFontSize(attr, fontSize);
		}else{
			return;
		}
		setAttributeSet(attr);
		textPane.requestFocusInWindow();

	}

	protected void setAttributeSet(AttributeSet attr){
		int start = textPane.getSelectionStart();
		int end = textPane.getSelectionEnd();
		doc.setCharacterAttributes(start, end - start, attr, false);
	}


	public void caretUpdate(CaretEvent e){
		flag = true;

		int p = textPane.getSelectionStart();
		AttributeSet a = doc.getCharacterElement(p).getAttributes();

		String name = StyleConstants.getFontFamily(a);

		//if the font name equals before the change, this is ignored.
		if(!currentFontName.equals(name)){
			currentFontName = name;
			comboFonts.setSelectedItem(name);
		}

		int size = StyleConstants.getFontSize(a);

		//if the font size equals before the change, this is ignored.
		if(currentFontSize != size){
			currentFontSize = size;
			comboSizes.setSelectedItem(Integer.toString(size));
		}

		flag = false;
	}

	/*protected void changeStyle(DefaultStyledDocument doc){
		
		//change style of 8 characters from the 4th to BOLD
		MutableAttributeSet attr1 = new SimpleAttributeSet();
    	StyleConstants.setBold(attr1, true);
    	doc.setCharacterAttributes(4, 8, attr1, false);

    	//change style of 4 characters from the 6th to ITALIC
    	MutableAttributeSet attr2 = new SimpleAttributeSet();
    	StyleConstants.setItalic(attr2, true);
    	doc.setCharacterAttributes(6, 4, attr2, false);

    	//change color of 4 characters from the 2th
    	MutableAttributeSet attr3 = new SimpleAttributeSet();
    	StyleConstants.setForeground(attr3, Color.red);
		StyleConstants.setBackground(attr3, Color.black);
		doc.setCharacterAttributes(2, 4, attr3, false);

    	MutableAttributeSet attr4 = new SimpleAttributeSet();
    	StyleConstants.setFontFamily(attr4, "HGP行書体");
    	StyleConstants.setFontSize(attr4, 24);
    	doc.setCharacterAttributes(20, 6, attr4, false);

    	MutableAttributeSet attr5 = new SimpleAttributeSet();
    	StyleConstants.setUnderline(attr5, true);
    	doc.setCharacterAttributes(8, 5, attr5, false);

    	MutableAttributeSet attr6 = new SimpleAttributeSet();
    	StyleConstants.setStrikeThrough(attr6, true);
    	doc.setCharacterAttributes(27, 5, attr6, false);
	}*/
}