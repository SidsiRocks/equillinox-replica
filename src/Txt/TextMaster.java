package Txt;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fontMeshCreator.FontType;
import fontMeshCreator.GUIText;
import fontMeshCreator.TextMeshData;
import fontRendering.FontRenderer;
import renderEngine.Loader;

public class TextMaster {
    private static Loader loader;
    private static Map<FontType, List<GUIText>> texts = new HashMap<FontType, List<GUIText>>();
    private static FontRenderer renderer;
     
    public static void init(Loader theLoader){
        renderer = new FontRenderer();
        loader = theLoader;
    }
    
    public static void loadText(GUIText text) {
    	FontType font = text.getFont();
    	
    	TextMeshData data = font.loadText(text);
    	int vao = loader.loadToVao(data.getVertexPositions(),data.getTextureCoords());
    	text.setMeshInfo(vao,data.getVertexCount());
    	List<GUIText> textBatch = texts.get(font);
    	
    	if(textBatch == null) {
    		textBatch = new ArrayList<GUIText>();
    		texts.put(font,textBatch);
    	}
    	
    	textBatch.add(text);
    }
     
    public static void render() {
    	renderer.render(texts);
    }
    
    public static void removeGUItext(GUIText txt) {
    	List<GUIText> textBatch = texts.get(txt.getFont());
    	textBatch.remove(txt);
    	if(textBatch.isEmpty()) {
    		texts.remove(txt.getFont(),textBatch);
    	}
    }
    public static void cleanUp(){
        renderer.cleanUp();
    }
}
