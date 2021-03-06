package qmips.presentation.swing.fancy;

import java.awt.Color;

import javax.swing.JTextPane;
import javax.swing.SwingUtilities;
import javax.swing.text.AttributeSet;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;

public class Log {

	public static LogWritter inf;
	public static LogWritter err;

	public static void init(JTextPane text) {
		inf = new LogWritter(text, Color.BLACK);
		err = new LogWritter(text, Color.RED);
	}

	public static class LogWritter {

		private JTextPane text;
		private Color color;

		public LogWritter(JTextPane text, Color color) {
			this.text = text;
			this.color = color;
		}

		public void println(String msg) {
			SwingUtilities.invokeLater(new RunnableMessage(msg));
		}
		
		private class RunnableMessage implements Runnable{
			
			private String msg;
			
			protected RunnableMessage(String msg){
				this.msg = msg;
			}
			
			@Override
			public void run() {
				StyleContext sc = StyleContext.getDefaultStyleContext();
				AttributeSet aset = sc.addAttribute(SimpleAttributeSet.EMPTY,
						StyleConstants.Foreground, color);
				int len = text.getDocument().getLength();
				text.setCaretPosition(len);
				text.setCharacterAttributes(aset, false);
				text.replaceSelection(this.msg + "\n");
			}
			
		}
	}
}
