package Graphics;
import org.lwjgl.nuklear.*;
import org.lwjgl.system.*;
import java.nio.*;
import java.util.*;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.nuklear.Nuklear.*;
import static org.lwjgl.opengl.GL30C.*;
import static org.lwjgl.system.MemoryStack.*;
import static org.lwjgl.system.MemoryUtil.*;


public class GUI {
  //Singleton
  private static GUI GUI = null;

  private NkContext
  ctx = NkContext.create(); // Create a Nuklear context, it is used everywhere.
  private NkUserFont
  default_font = NkUserFont.create(); // This is the Nuklear font object used for rendering text.

  private NkBuffer
  cmds = NkBuffer.create(); // Stores a list of drawing commands that will be passed to OpenGL to render the interface.
  private NkDrawNullTexture
  null_texture = NkDrawNullTexture.create(); // An empty texture used for drawing.


  private static final int BUFFER_INITIAL_SIZE = 4 * 1024;

  private static final int MAX_VERTEX_BUFFER  = 512 * 1024;
  private static final int MAX_ELEMENT_BUFFER = 128 * 1024;

  private static final NkAllocator ALLOCATOR;

  private static final NkDrawVertexLayoutElement.Buffer VERTEX_LAYOUT;

  static {
    ALLOCATOR = NkAllocator.create()
    .alloc((handle, old, size) -> nmemAllocChecked(size))
    .mfree((handle, ptr) -> nmemFree(ptr));

    VERTEX_LAYOUT = NkDrawVertexLayoutElement.create(4)
    .position(0).attribute(NK_VERTEX_POSITION).format(NK_FORMAT_FLOAT).offset(0)
    .position(1).attribute(NK_VERTEX_TEXCOORD).format(NK_FORMAT_FLOAT).offset(8)
    .position(2).attribute(NK_VERTEX_COLOR).format(NK_FORMAT_R8G8B8A8).offset(16)
    .position(3).attribute(NK_VERTEX_ATTRIBUTE_COUNT).format(NK_FORMAT_COUNT).offset(0)
    .flip();
  }

  private int vbo, vao, ebo;
  private int prog;
  private int vert_shdr;
  private int frag_shdr;
  private int uniform_tex;
  private int uniform_proj;

  public static GUI getInstance() {
    if(GUI == null){
      GUI = new GUI();
    }

    return GUI;
  }

  public NkContext setupWindow(long win) {
    glfwSetScrollCallback(win, (window, xoffset, yoffset) -> {
      try (MemoryStack stack = stackPush()) {
        NkVec2 scroll = NkVec2.mallocStack(stack)
        .x((float)xoffset)
        .y((float)yoffset);
        nk_input_scroll(ctx, scroll);
      }
    });
    glfwSetCharCallback(win, (window, codepoint) -> nk_input_unicode(ctx, codepoint));
    glfwSetKeyCallback(win, (window, key, scancode, action, mods) -> {
      boolean press = action == GLFW_PRESS;
      switch (key) {
        case GLFW_KEY_ESCAPE:
          glfwSetWindowShouldClose(window, true);
          break;
        case GLFW_KEY_DELETE:
          nk_input_key(ctx, NK_KEY_DEL, press);
          break;
        case GLFW_KEY_ENTER:
          nk_input_key(ctx, NK_KEY_ENTER, press);
          break;
        case GLFW_KEY_TAB:
          nk_input_key(ctx, NK_KEY_TAB, press);
          break;
        case GLFW_KEY_BACKSPACE:
          nk_input_key(ctx, NK_KEY_BACKSPACE, press);
          break;
        case GLFW_KEY_UP:
          nk_input_key(ctx, NK_KEY_UP, press);
          break;
        case GLFW_KEY_DOWN:
          nk_input_key(ctx, NK_KEY_DOWN, press);
          break;
        case GLFW_KEY_HOME:
          nk_input_key(ctx, NK_KEY_TEXT_START, press);
          nk_input_key(ctx, NK_KEY_SCROLL_START, press);
          break;
        case GLFW_KEY_END:
          nk_input_key(ctx, NK_KEY_TEXT_END, press);
          nk_input_key(ctx, NK_KEY_SCROLL_END, press);
          break;
        case GLFW_KEY_PAGE_DOWN:
          nk_input_key(ctx, NK_KEY_SCROLL_DOWN, press);
          break;
        case GLFW_KEY_PAGE_UP:
          nk_input_key(ctx, NK_KEY_SCROLL_UP, press);
          break;
        case GLFW_KEY_LEFT_SHIFT:
        case GLFW_KEY_RIGHT_SHIFT:
          nk_input_key(ctx, NK_KEY_SHIFT, press);
          break;
        case GLFW_KEY_LEFT_CONTROL:
        case GLFW_KEY_RIGHT_CONTROL:
          if (press) {
            nk_input_key(ctx, NK_KEY_COPY, glfwGetKey(window, GLFW_KEY_C) == GLFW_PRESS);
            nk_input_key(ctx, NK_KEY_PASTE, glfwGetKey(window, GLFW_KEY_P) == GLFW_PRESS);
            nk_input_key(ctx, NK_KEY_CUT, glfwGetKey(window, GLFW_KEY_X) == GLFW_PRESS);
            nk_input_key(ctx, NK_KEY_TEXT_UNDO, glfwGetKey(window, GLFW_KEY_Z) == GLFW_PRESS);
            nk_input_key(ctx, NK_KEY_TEXT_REDO, glfwGetKey(window, GLFW_KEY_R) == GLFW_PRESS);
            nk_input_key(ctx, NK_KEY_TEXT_WORD_LEFT, glfwGetKey(window, GLFW_KEY_LEFT) == GLFW_PRESS);
            nk_input_key(ctx, NK_KEY_TEXT_WORD_RIGHT, glfwGetKey(window, GLFW_KEY_RIGHT) == GLFW_PRESS);
            nk_input_key(ctx, NK_KEY_TEXT_LINE_START, glfwGetKey(window, GLFW_KEY_B) == GLFW_PRESS);
            nk_input_key(ctx, NK_KEY_TEXT_LINE_END, glfwGetKey(window, GLFW_KEY_E) == GLFW_PRESS);
          } else {
            nk_input_key(ctx, NK_KEY_LEFT, glfwGetKey(window, GLFW_KEY_LEFT) == GLFW_PRESS);
            nk_input_key(ctx, NK_KEY_RIGHT, glfwGetKey(window, GLFW_KEY_RIGHT) == GLFW_PRESS);
            nk_input_key(ctx, NK_KEY_COPY, false);
            nk_input_key(ctx, NK_KEY_PASTE, false);
            nk_input_key(ctx, NK_KEY_CUT, false);
            nk_input_key(ctx, NK_KEY_SHIFT, false);
          }
          break;
      }
    });
    glfwSetCursorPosCallback(win, (window, xpos, ypos) -> nk_input_motion(ctx, (int)xpos, (int)ypos));
    glfwSetMouseButtonCallback(win, (window, button, action, mods) -> {
      try (MemoryStack stack = stackPush()) {
        DoubleBuffer cx = stack.mallocDouble(1);
        DoubleBuffer cy = stack.mallocDouble(1);

        glfwGetCursorPos(window, cx, cy);

        int x = (int)cx.get(0);
        int y = (int)cy.get(0);

        int nkButton;
        switch (button) {
          case GLFW_MOUSE_BUTTON_RIGHT:
            nkButton = NK_BUTTON_RIGHT;
            break;
          case GLFW_MOUSE_BUTTON_MIDDLE:
            nkButton = NK_BUTTON_MIDDLE;
            break;
          default:
            nkButton = NK_BUTTON_LEFT;
        }
        nk_input_button(ctx, nkButton, x, y, action == GLFW_PRESS);
      }
    });

    nk_init(ctx, ALLOCATOR, null);
    ctx.clip()
    .copy((handle, text, len) -> {
      if (len == 0) {
        return;
      }

      try (MemoryStack stack = stackPush()) {
        ByteBuffer str = stack.malloc(len + 1);
        memCopy(text, memAddress(str), len);
        str.put(len, (byte)0);

        glfwSetClipboardString(win, str);
      }
    })
    .paste((handle, edit) -> {
      long text = nglfwGetClipboardString(win);
      if (text != NULL) {
        nnk_textedit_paste(edit, text, nnk_strlen(text));
      }
    });

    setupContext();
    return ctx;
  }


  private void setupContext() {
    String NK_SHADER_VERSION = Platform.get() == Platform.MACOSX ? "#version 150\n" : "#version 300 es\n";
    String vertex_shader =
    NK_SHADER_VERSION +
    "uniform mat4 ProjMtx;\n" +
    "in vec2 Position;\n" +
    "in vec2 TexCoord;\n" +
    "in vec4 Color;\n" +
    "out vec2 Frag_UV;\n" +
    "out vec4 Frag_Color;\n" +
    "void main() {\n" +
    "   Frag_UV = TexCoord;\n" +
    "   Frag_Color = Color;\n" +
    "   gl_Position = ProjMtx * vec4(Position.xy, 0, 1);\n" +
    "}\n";
    String fragment_shader =
    NK_SHADER_VERSION +
    "precision mediump float;\n" +
    "uniform sampler2D Texture;\n" +
    "in vec2 Frag_UV;\n" +
    "in vec4 Frag_Color;\n" +
    "out vec4 Out_Color;\n" +
    "void main(){\n" +
    "   Out_Color = Frag_Color * texture(Texture, Frag_UV.st);\n" +
    "}\n";

    nk_buffer_init(cmds, ALLOCATOR, BUFFER_INITIAL_SIZE);
    prog = glCreateProgram();
    vert_shdr = glCreateShader(GL_VERTEX_SHADER);
    frag_shdr = glCreateShader(GL_FRAGMENT_SHADER);
    glShaderSource(vert_shdr, vertex_shader);
    glShaderSource(frag_shdr, fragment_shader);
    glCompileShader(vert_shdr);
    glCompileShader(frag_shdr);
    if (glGetShaderi(vert_shdr, GL_COMPILE_STATUS) != GL_TRUE) {
      throw new IllegalStateException();
    }
    if (glGetShaderi(frag_shdr, GL_COMPILE_STATUS) != GL_TRUE) {
      throw new IllegalStateException();
    }
    glAttachShader(prog, vert_shdr);
    glAttachShader(prog, frag_shdr);
    glLinkProgram(prog);
    if (glGetProgrami(prog, GL_LINK_STATUS) != GL_TRUE) {
      throw new IllegalStateException();
    }

    uniform_tex = glGetUniformLocation(prog, "Texture");
    uniform_proj = glGetUniformLocation(prog, "ProjMtx");
    int attrib_pos = glGetAttribLocation(prog, "Position");
    int attrib_uv  = glGetAttribLocation(prog, "TexCoord");
    int attrib_col = glGetAttribLocation(prog, "Color");

    {
      // buffer setup
      vbo = glGenBuffers();
      ebo = glGenBuffers();
      vao = glGenVertexArrays();

      glBindVertexArray(vao);
      glBindBuffer(GL_ARRAY_BUFFER, vbo);
      glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, ebo);

      glEnableVertexAttribArray(attrib_pos);
      glEnableVertexAttribArray(attrib_uv);
      glEnableVertexAttribArray(attrib_col);

      glVertexAttribPointer(attrib_pos, 2, GL_FLOAT, false, 20, 0);
      glVertexAttribPointer(attrib_uv, 2, GL_FLOAT, false, 20, 8);
      glVertexAttribPointer(attrib_col, 4, GL_UNSIGNED_BYTE, true, 20, 16);
    }

    {
      // null texture setup
      int nullTexID = glGenTextures();

      null_texture.texture().id(nullTexID);
      null_texture.uv().set(0.5f, 0.5f);

      glBindTexture(GL_TEXTURE_2D, nullTexID);
      try (MemoryStack stack = stackPush()) {
        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA8, 1, 1, 0, GL_RGBA, GL_UNSIGNED_INT_8_8_8_8_REV, stack.ints(0xFFFFFFFF));
      }
      glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
      glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
    }

    glBindTexture(GL_TEXTURE_2D, 0);
    glBindBuffer(GL_ARRAY_BUFFER, 0);
    glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0);
    glBindVertexArray(0);
  }


  public void render() {
    try (MemoryStack stack = stackPush()) {
      // setup global state
      glEnable(GL_BLEND);
      glBlendEquation(GL_FUNC_ADD);
      glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
      glDisable(GL_CULL_FACE);
      glDisable(GL_DEPTH_TEST);
      glEnable(GL_SCISSOR_TEST);
      glActiveTexture(GL_TEXTURE0);

      // setup program
      glUseProgram(prog);
      glUniform1i(uniform_tex, 0);
      glUniformMatrix4fv(uniform_proj, false, stack.floats(
      2.0f / Window.getInstance().getWidth(), 0.0f, 0.0f, 0.0f,
      0.0f, -2.0f / Window.getInstance().getHeight(), 0.0f, 0.0f,
      0.0f, 0.0f, -1.0f, 0.0f,
      -1.0f, 1.0f, 0.0f, 1.0f
      ));
      glViewport(0, 0, Window.getInstance().getDisplay_Width(), Window.getInstance()
      .getDisplay_Height());
    }

    {
      // convert from command queue into draw list and draw to screen

      // allocate vertex and element buffer
      glBindVertexArray(vao);
      glBindBuffer(GL_ARRAY_BUFFER, vbo);
      glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, ebo);

      glBufferData(GL_ARRAY_BUFFER, MAX_VERTEX_BUFFER, GL_STREAM_DRAW);
      glBufferData(GL_ELEMENT_ARRAY_BUFFER, MAX_ELEMENT_BUFFER, GL_STREAM_DRAW);

      // load draw vertices & elements directly into vertex + element buffer
      ByteBuffer vertices = Objects.requireNonNull(glMapBuffer(GL_ARRAY_BUFFER, GL_WRITE_ONLY, MAX_VERTEX_BUFFER, null));
      ByteBuffer elements = Objects.requireNonNull(glMapBuffer(GL_ELEMENT_ARRAY_BUFFER, GL_WRITE_ONLY, MAX_ELEMENT_BUFFER, null));
      try (MemoryStack stack = stackPush()) {
        // fill convert configuration
        NkConvertConfig config = NkConvertConfig.callocStack(stack)
        .vertex_layout(VERTEX_LAYOUT)
        .vertex_size(20)
        .vertex_alignment(4)
        .null_texture(null_texture)
        .circle_segment_count(22)
        .curve_segment_count(22)
        .arc_segment_count(22)
        .global_alpha(1.0f)
        .shape_AA(NK_ANTI_ALIASING_ON)
        .line_AA(NK_ANTI_ALIASING_ON);

        // setup buffers to load vertices and elements
        NkBuffer vbuf = NkBuffer.mallocStack(stack);
        NkBuffer ebuf = NkBuffer.mallocStack(stack);

        nk_buffer_init_fixed(vbuf, vertices/*, max_vertex_buffer*/);
        nk_buffer_init_fixed(ebuf, elements/*, max_element_buffer*/);
        nk_convert(ctx, cmds, vbuf, ebuf, config);
      }
      glUnmapBuffer(GL_ELEMENT_ARRAY_BUFFER);
      glUnmapBuffer(GL_ARRAY_BUFFER);

      // iterate over and execute each draw command
      float fb_scale_x =
      (float)Window.getInstance().getDisplay_Width() / (float)Window.getInstance().getDisplay_Width();
      float fb_scale_y =
      (float)Window.getInstance().getHeight() / (float)Window.getInstance().getDisplay_Height();

      long offset = NULL;
      for (NkDrawCommand cmd = nk__draw_begin(ctx, cmds); cmd != null; cmd = nk__draw_next(cmd, cmds, ctx)) {
        if (cmd.elem_count() == 0) {
          continue;
        }
        glBindTexture(GL_TEXTURE_2D, cmd.texture().id());
        glScissor(
        (int)(cmd.clip_rect().x() * fb_scale_x),
        (int)((Window.getInstance().getHeight()  - (int)(cmd.clip_rect().y() + cmd.clip_rect().h())) * fb_scale_y),
        (int)(cmd.clip_rect().w() * fb_scale_x),
        (int)(cmd.clip_rect().h() * fb_scale_y)
        );
        glDrawElements(GL_TRIANGLES, cmd.elem_count(), GL_UNSIGNED_SHORT, offset);
        offset += cmd.elem_count() * 2;
      }
      nk_clear(ctx);
      nk_buffer_clear(cmds);
    }

    // default OpenGL state
    glUseProgram(0);
    glBindBuffer(GL_ARRAY_BUFFER, 0);
    glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0);
    glBindVertexArray(0);
    glDisable(GL_BLEND);
    glDisable(GL_SCISSOR_TEST);
  }

  public void input(long win){
    nk_input_begin(ctx);

    glfwPollEvents();

    NkMouse mouse = ctx.input().mouse();
    if (mouse.grab()) {
      glfwSetInputMode(win, GLFW_CURSOR, GLFW_CURSOR_HIDDEN);
    } else if (mouse.grabbed()) {
      float prevX = mouse.prev().x();
      float prevY = mouse.prev().y();
      glfwSetCursorPos(win, prevX, prevY);
      mouse.pos().x(prevX);
      mouse.pos().y(prevY);
    } else if (mouse.ungrab()) {
      glfwSetInputMode(win, GLFW_CURSOR, GLFW_CURSOR_NORMAL);
    }

    nk_input_end(ctx);

  }

  public NkContext get_context(){
    return ctx;
  }

}