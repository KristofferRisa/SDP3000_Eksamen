package controller;

import gui.Maingui;

public class Controller {
	
	static Maingui maingui;
	
	public static void init(Maingui maingui) {
		Controller.maingui = maingui;
	}

	public static void stop() {
		System.exit(0);		
	}

	public static void open() {	
		maingui.open();		
	}

	public static void save() {
		maingui.save();		
	}

	public static void paste() {
		maingui.paste();		
	}

	public static void testhtml() {
		maingui.testhtml();		
	}

	public static void toggleHtmlView(boolean toggle) {
		maingui.toggleHtmlView(toggle);
	}

}
