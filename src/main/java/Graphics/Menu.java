package Graphics;

import org.lwjgl.nuklear.NkContext;
import org.lwjgl.nuklear.NkRect;
import org.lwjgl.system.MemoryStack;
import static org.lwjgl.nuklear.Nuklear.*;
import static org.lwjgl.system.MemoryStack.stackPush;

public class Menu {

  public static void render(){
    NkContext ctx = GUI.getInstance().get_context();

    try(MemoryStack stack = stackPush()){
      NkRect rect = NkRect.mallocStack(stack);

      nk_begin(ctx, "Sidebar",nk_rect(0, 0, 230, 250, rect),0);

    }catch (Exception e){


    }finally{

    }
      nk_end(ctx);
    }

}
