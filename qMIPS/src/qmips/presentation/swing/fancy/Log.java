package qmips.presentation.swing.fancy;

import java.awt.Color;

import javax.swing.JTextArea;

public class Log {

	public static LogWritter inf;
	public static LogWritter err;
	
	public static void init(JTextArea text){
		inf = new LogWritter(text, Color.BLACK);
		err = new LogWritter(text, Color.RED);
	}
	
	static class LogWritter{
		
		private JTextArea text;
		private Color color;
		
		public LogWritter(JTextArea text, Color color){
			this.text = text;
			this.color = color;
		}
		
		public void println(String msg){
			text.setForeground(color);
			text.append(msg + "\n");
		}
	}
}
