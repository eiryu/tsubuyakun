package com.eiryu.twitter.tsubuyakun;

import java.awt.Button;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.GraphicsEnvironment;
import java.awt.HeadlessException;
import java.awt.Label;
import java.awt.TextArea;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.TextEvent;
import java.awt.event.TextListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;

/**
 * @author eiryu
 * 
 */
@SuppressWarnings("serial")
public class Tweet extends Frame implements ActionListener, TextListener, WindowListener, KeyListener {


    private TextArea textArea;
    private Label textLength;
    private Button sendButton;
    private int caretPosition;

    public static final int MAXIMUM_LENGTH_OF_TWEET = 140;
    public static final int FLAME_WIDTH = 400;
    public static final int FLAME_HEIGHT = 250;
    
    public static final String LINE_SEPARATOR = System.getProperty("line.separator");

    private static Twitter tw = new TwitterFactory().getInstance();

    /**
     * @throws HeadlessException
     */
    public Tweet() throws HeadlessException {
        super("つぶや君");
        
        GraphicsEnvironment env = GraphicsEnvironment.getLocalGraphicsEnvironment();
        setLocation(env.getMaximumWindowBounds().width - FLAME_WIDTH, env.getMaximumWindowBounds().height
                - FLAME_HEIGHT);
        setSize(FLAME_WIDTH, FLAME_HEIGHT);
        setLayout(new FlowLayout());

        textArea = new TextArea("", 10, 50, TextArea.SCROLLBARS_NONE);
        textLength = new Label(String.valueOf(MAXIMUM_LENGTH_OF_TWEET) + "     ");
        sendButton = new Button("つぶやく");

        add(textArea);
        add(textLength);
        add(sendButton);

        addWindowListener(this);
        textArea.addTextListener(this);
        sendButton.addActionListener(this);

        textArea.addKeyListener(this);
        sendButton.addKeyListener(this);
        setVisible(true);
    }

    /**
	 * @param args
	 */
	public static void main(String[] args) {
	    new Tweet();
	}

	@Override
    public void actionPerformed(ActionEvent e) {
        String contents = textArea.getText().replaceAll(LINE_SEPARATOR, "");
        if (contents.length() > 0) {
            sendButton.setEnabled(false);

            try {
                tw.updateStatus(textArea.getText());
                textArea.setText("");
            } catch (TwitterException ex) {
                textLength.setText("ERROR!");
            } finally {
                sendButton.setEnabled(true);
            }
        }
        textArea.requestFocus();
    }

    @Override
    public void textValueChanged(TextEvent e) {
        String contents = textArea.getText();
        if (contents.replaceAll(LINE_SEPARATOR, " ").length() > MAXIMUM_LENGTH_OF_TWEET) {
            // オーバーした文字数をカウント
            int over = contents.replaceAll(LINE_SEPARATOR, " ").length() - MAXIMUM_LENGTH_OF_TWEET;

            char[] src = contents.toCharArray();
            // 最後に入力されたのが改行コードだったら余分に一文字削る
            if ('\n' == (contents.charAt(contents.length() - 1))) {
                ++over;
            }
            char[] dst = new char[src.length - over];
            for (int i = 0; i < dst.length; i++) {
                dst[i] = src[i];
            }

            textArea.setText(String.valueOf(dst));
            // カーソル位置
            caretPosition -= countLineSeparator(contents);
            textArea.setCaretPosition(caretPosition);
        }

        textLength.setText(String.valueOf(MAXIMUM_LENGTH_OF_TWEET
                - contents.replaceAll(LINE_SEPARATOR, " ").length()));
    }

    private int countLineSeparator(String str) {
        int cnt = 0;
        char[] contents = str.toCharArray();
        for (int i = 0; i < caretPosition; i++) {
            if ('\n' == contents[i]) {
                cnt++;
            }
        }
        return cnt;
    }

    @Override
    public void windowActivated(WindowEvent e) {
    }

    @Override
    public void windowClosed(WindowEvent e) {
    }

    @Override
    public void windowClosing(WindowEvent e) {
        System.exit(0);
    }

    @Override
    public void windowDeactivated(WindowEvent e) {
    }

    @Override
    public void windowDeiconified(WindowEvent e) {
    }

    @Override
    public void windowIconified(WindowEvent e) {
    }

    @Override
    public void windowOpened(WindowEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
        caretPosition = textArea.getCaretPosition();
//        System.out.println(caretPosition);

        // System.out.println(e.getKeyCode());
        // タブキーでフォーカス。うまくいかず
        // if (e.getKeyChar() == 9) {
        // String contents = textArea.getText();
        // textArea.setText(contents.substring(0, contents.length() - 1));
        // sendButton.requestFocus();
        // }

        // shift + enter での処理。うまくいかず
        // if (e.getKeyCode() == 10) {
        // if (e.getKeyCode() == 16) {
        // System.out.println("press TAB key");
        // textArea.setText(textArea.getText().replaceAll("\t", ""));
        // sendButton.requestFocus();
        // }
        // }
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

}
