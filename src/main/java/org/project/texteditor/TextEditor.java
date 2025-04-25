/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package org.project.texteditor;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

import com.sun.jna.LastErrorException;
import com.sun.jna.Native;
import com.sun.jna.Pointer;
import com.sun.jna.Structure;
import com.sun.jna.ptr.IntByReference;
import com.sun.jna.win32.StdCallLibrary;




interface Terminal {

    void enableRawMode();

    void disableRawMode();

    WindowSize getWindowSize();
}

class WindowsTerminal implements Terminal {

    private IntByReference inMode;
    private IntByReference outMode;

    @Override
    public void enableRawMode() {
        Pointer inHandle = Kernel32.INSTANCE.GetStdHandle(Kernel32.STD_INPUT_HANDLE);
        inMode = new IntByReference();
        Kernel32.INSTANCE.GetConsoleMode(inHandle, inMode);
        
        int inMode;
        inMode = this.inMode.getValue() & ~(
            Kernel32.ENABLE_ECHO_INPUT
            | Kernel32.ENABLE_LINE_INPUT
            | Kernel32.ENABLE_MOUSE_INPUT
            | Kernel32.ENABLE_WINDOW_INPUT
            | Kernel32.ENABLE_PROCESSED_INPUT
            );
            
        inMode |= Kernel32.ENABLE_VIRTUAL_TERMINAL_INPUT;


        Kernel32.INSTANCE.SetConsoleMode(inHandle, inMode);

        Pointer outHandle = Kernel32.INSTANCE.GetStdHandle(Kernel32.STD_OUTPUT_HANDLE);
        outMode = new IntByReference();
        Kernel32.INSTANCE.GetConsoleMode(outHandle, outMode);

        int outMode = this.outMode.getValue();
        outMode |= Kernel32.ENABLE_VIRTUAL_TERMINAL_PROCESSING;
        outMode |= Kernel32.ENABLE_PROCESSED_OUTPUT;
        Kernel32.INSTANCE.SetConsoleMode(outHandle, outMode);

    }


    @Override
    public void disableRawMode() {
        Pointer inHandle = Kernel32.INSTANCE.GetStdHandle(Kernel32.STD_INPUT_HANDLE);
        Kernel32.INSTANCE.SetConsoleMode(inHandle, inMode.getValue());

        Pointer outHandle = Kernel32.INSTANCE.GetStdHandle(Kernel32.STD_OUTPUT_HANDLE);
        Kernel32.INSTANCE.SetConsoleMode(outHandle, outMode.getValue());
    }


    @Override
    public WindowSize getWindowSize() {
        final Kernel32.CONSOLE_SCREEN_BUFFER_INFO info = new Kernel32.CONSOLE_SCREEN_BUFFER_INFO();
        final Kernel32 instance = Kernel32.INSTANCE;
        final Pointer handle = Kernel32.INSTANCE.GetStdHandle(Kernel32.STD_OUTPUT_HANDLE);
        instance.GetConsoleScreenBufferInfo(handle, info);
        return new WindowSize(info.windowHeight(), info.windowWidth());
    }

    interface Kernel32 extends StdCallLibrary {

        Kernel32 INSTANCE = Native.load("kernel32", Kernel32.class);

        /**
         * The CryptUIDlgSelectCertificateFromStore function displays a dialog box
         * that allows the selection of a certificate from a specified store.
         *
         * @param hCertStore Handle of the certificate store to be searched.
         * @param hwnd Handle of the window for the display. If NULL,
         * defaults to the desktop window.
         * @param pwszTitle String used as the title of the dialog box. If
         * NULL, the default title, "Select Certificate,"
         * is used.
         * @param pwszDisplayString Text statement in the selection dialog box. If
         * NULL, the default phrase, "Select a certificate
         * you want to use," is used.
         * @param dwDontUseColumn Flags that can be combined to exclude columns of
         * the display.
         * @param dwFlags Currently not used and should be set to 0.
         * @param pvReserved Reserved for future use.
         * @return Returns a pointer to the selected certificate context. If no
         * certificate was selected, NULL is returned. When you have
         * finished using the certificate, free the certificate context by
         * calling the CertFreeCertificateContext function.
         */
        public static final int ENABLE_VIRTUAL_TERMINAL_PROCESSING = 0x0004, ENABLE_PROCESSED_OUTPUT = 0x0001;

        int ENABLE_LINE_INPUT = 0x0002;
        int ENABLE_PROCESSED_INPUT = 0x0001;
        int ENABLE_ECHO_INPUT = 0x0004;
        int ENABLE_MOUSE_INPUT = 0x0010;
        int ENABLE_WINDOW_INPUT = 0x0008;
        int ENABLE_QUICK_EDIT_MODE = 0x0040;
        int ENABLE_INSERT_MODE = 0x0020;

        int ENABLE_EXTENDED_FLAGS = 0x0080;

        int ENABLE_VIRTUAL_TERMINAL_INPUT = 0x0200;


        int STD_OUTPUT_HANDLE = -11;
        int STD_INPUT_HANDLE = -10;
        int DISABLE_NEWLINE_AUTO_RETURN = 0x0008;

        // BOOL WINAPI GetConsoleScreenBufferInfo(
        // _In_   HANDLE hConsoleOutput,
        // _Out_  PCONSOLE_SCREEN_BUFFER_INFO lpConsoleScreenBufferInfo);
        void GetConsoleScreenBufferInfo(
                Pointer in_hConsoleOutput,
                CONSOLE_SCREEN_BUFFER_INFO out_lpConsoleScreenBufferInfo)
                throws LastErrorException;

        void GetConsoleMode(
                Pointer in_hConsoleOutput,
                IntByReference out_lpMode)
                throws LastErrorException;

        void SetConsoleMode(
                Pointer in_hConsoleOutput,
                int in_dwMode) throws LastErrorException;

        Pointer GetStdHandle(int nStdHandle);

        // typedef struct _CONSOLE_SCREEN_BUFFER_INFO {
        //   COORD      dwSize;
        //   COORD      dwCursorPosition;
        //   WORD       wAttributes;
        //   SMALL_RECT srWindow;
        //   COORD      dwMaximumWindowSize;
        // } CONSOLE_SCREEN_BUFFER_INFO;
        class CONSOLE_SCREEN_BUFFER_INFO extends Structure {


            public COORD dwSize;
            public COORD dwCursorPosition;
            public short wAttributes;
            public SMALL_RECT srWindow;
            public COORD dwMaximumWindowSize;

            private static String[] fieldOrder = {"dwSize", "dwCursorPosition", "wAttributes", "srWindow", "dwMaximumWindowSize"};

            @Override
            protected java.util.List<String> getFieldOrder() {
                return java.util.Arrays.asList(fieldOrder);
            }

            public int windowWidth() {
                return this.srWindow.width() + 1;
            }

            public int windowHeight() {
                return this.srWindow.height() + 1;
            }
        }

        // typedef struct _COORD {
        //    SHORT X;
        //    SHORT Y;
        //  } COORD, *PCOORD;
        class COORD extends Structure implements Structure.ByValue {
            public COORD() {
            }

            public COORD(short X, short Y) {
                this.X = X;
                this.Y = Y;
            }

            public short X;
            public short Y;

            private static String[] fieldOrder = {"X", "Y"};

            @Override
            protected java.util.List<String> getFieldOrder() {
                return java.util.Arrays.asList(fieldOrder);
            }
        }

        // typedef struct _SMALL_RECT {
        //    SHORT Left;
        //    SHORT Top;
        //    SHORT Right;
        //    SHORT Bottom;
        //  } SMALL_RECT;
        class SMALL_RECT extends Structure {
            public SMALL_RECT() {
            }

            public SMALL_RECT(SMALL_RECT org) {
                this(org.Top, org.Left, org.Bottom, org.Right);
            }

            public SMALL_RECT(short Top, short Left, short Bottom, short Right) {
                this.Top = Top;
                this.Left = Left;
                this.Bottom = Bottom;
                this.Right = Right;
            }

            public short Left;
            public short Top;
            public short Right;
            public short Bottom;

            private static String[] fieldOrder = {"Left", "Top", "Right", "Bottom"};

            @Override
            protected java.util.List<String> getFieldOrder() {
                return java.util.Arrays.asList(fieldOrder);
            }

            public short width() {
                return (short) (this.Right - this.Left);
            }

            public short height() {
                return (short) (this.Bottom - this.Top);
            }

        }


    }
}



public class TextEditor {
    private static final Terminal terminal = new WindowsTerminal();
    private static int rows = 10;
    private static int columns = 10;
    private static int cursorX = 0, offsetX = 0, cursorY = 0, offsetY = 0;
    //private static List<String> content = new ArrayList<>();
    private static Rope root = null;
    private static File my_file;
    private static final int ARROW_UP = 1000, ARROW_DOWN = 1001, ARROW_LEFT = 1002,
            ARROW_RIGHT = 1003,
            HOME = 1004,
            END = 1005,
            PAGE_UP = 1006,
            PAGE_DOWN = 1007,
            DEL = 1008,
            BACKSPACE = 127,
            ESC = 1027;

    public static void main(String[] args) throws IOException, InterruptedException {
        openFile(args);
        initEditor();
        while (true) {
            scroll();
            refreshScreen();
            int key = readKey();
            handleKey(key);
        }
       

    }
   
    private static void scroll() {
        if (cursorY >= rows + offsetY) {
            offsetY = cursorY - rows + 1;
        } else if (cursorY < offsetY) {
            offsetY = cursorY;
        }

        if (cursorX >= columns + offsetX) {
            offsetX = cursorX - columns + 1;
        } else if (cursorX < offsetX) {
            offsetX = cursorX;
        }
    }
    private static void openFile(String[] args) {
        if (args.length == 1) {
                try {
                    my_file = new File(args[0]);
                    my_file.createNewFile();
                    try (BufferedReader reader = new BufferedReader(new FileReader(my_file))) {
                        String line;
                        int index = 0;
                        while ((line = reader.readLine()) != null) {
                            Rope lineRoot = new Rope();         // nodo che rappresenta la riga
                            Rope.create(lineRoot, line.length(), line);
                            lineRoot.weight = index;                // rappresenta 1 riga
                            
                            if (root == null) {
                                root = lineRoot;
                            } else {
                                Rope newRoot = new Rope();
                                newRoot.left = root;
                                newRoot.right = lineRoot;
                                newRoot.weight = lineRoot.weight;
                                root = newRoot;
                            }
                            index++;
                        }
                        if (Files.size(my_file.toPath()) == 0) {
                            root = new Rope();
                            root = Rope.appendEmptyLine(root);
                            cursorY++;
                        }
                        //content = stream.collect(Collectors.toCollection(ArrayList::new ));
                    } catch (IOException e) { System.out.println(e.getMessage());}
                } catch (IOException e) {
                    System.err.println(e.getMessage());
                }
            

        }
    }
    private static void initEditor() {
        terminal.enableRawMode();
        WindowSize windowSize = terminal.getWindowSize();
        columns = windowSize.columns();
        rows = windowSize.rows() - 1;
    }
    private static void refreshScreen() throws IOException {
        StringBuilder builder = new StringBuilder();
        moveCursorToTopLeft(builder);
        drawContent(builder);
        drawStatusBar(builder);
        drawCursor(builder);
        System.out.print(builder);
    }

    private static void moveCursorToTopLeft(StringBuilder builder) {
        builder.append("\033[H");
    }
   
    private static void drawCursor(StringBuilder builder) {
        builder.append(String.format("\033[%d;%dH", cursorY - offsetY + 1, cursorX - offsetX + 1));
    }

    private static void drawStatusBar(StringBuilder builder) {
        String statusMessage = "Rows: " + rows + "X:" + cursorX + " Y: " + cursorY;
        builder.append("\033[7m")
                .append(statusMessage)
                .append(" ".repeat(Math.max(0, columns - statusMessage.length())))
                .append("\033[0m");
    }

    private static void drawContent(StringBuilder builder) {
        for (int i = 0; i < rows; i++) {
            int fileI = offsetY + i;
            if (fileI >= root.weight+1) {  //fileI >= content.size()
                builder.append("~");
                //root = new Rope();
                //root = Rope.appendEmptyLine(root);
            } else {
                String line = Rope.getLine(root, fileI);  //content.get(fileI)
                int lengthToDraw = line.length() - offsetX;

                if (lengthToDraw < 0) {
                    lengthToDraw = 0;
                }
                if (lengthToDraw > columns) {
                    lengthToDraw = columns;
                }

                if (lengthToDraw > 0) {
                    builder.append(line, offsetX, offsetX + lengthToDraw);
                }


            }
            builder.append("\033[K\r\n");
        }
    }

    private static int controlKey (int key) {
        return key & 0x1f;
    }

    private static int readKey() throws IOException {
        int key = System.in.read();
        if (key != '\033') {
            return key;
        }
        if (key == 127) {
            return BACKSPACE;
        }
        
        int nextKey = System.in.read();
        if (nextKey != '[' && nextKey != 'O') {
            return nextKey;
        }

        int yetAnotherKey = System.in.read();

        if (nextKey == '[') {
            return switch (yetAnotherKey) {
                case 'A' -> ARROW_UP;  // e.g. esc[A == arrow_up
                case 'B' -> ARROW_DOWN;
                case 'C' -> ARROW_RIGHT;
                case 'D' -> ARROW_LEFT;
                case 'H' -> HOME;
                case 'F' -> END;
                case '0', '1', '2', '3', '4', '5', '6', '7', '8', '9' -> {  // e.g: esc[5~ == page_up
                    int yetYetAnotherChar = System.in.read();
                    if (yetYetAnotherChar != '~') {
                        yield yetYetAnotherChar;
                    }
                    switch (yetAnotherKey) {
                        case '1':
                        case '7':
                            yield HOME;
                        case '3':
                            yield DEL;
                        case '4':
                        case '8':
                            yield END;
                        case '5':
                            yield PAGE_UP;
                        case '6':
                            yield PAGE_DOWN;
                        default:
                            yield yetAnotherKey;
                    }
                }
                default -> yetAnotherKey;
            };
        } else { //if (nextKey == 'O') {  e.g. escpOH == HOME
            return switch (yetAnotherKey) {
                case 'H' -> HOME;
                case 'F' -> END;
                default -> yetAnotherKey;
            };
        }
    }

    private static void handleKey(int key) {
        if (key == controlKey('q')) {
            exit();
        } else if (List.of(ARROW_UP, ARROW_DOWN, ARROW_LEFT, ARROW_RIGHT).contains(key)) {
            moveCursor(key);
        } else if (key == '\r') {
            insertNewLine();
        } else if (List.of(BACKSPACE).contains(key)) {
            delete();
        } else if (key == 's') {
            save();
        } else {
            insert((char)key);
        }

    }

    private static void exit() {
        System.out.print("\033[2J");
        System.out.print("\033[H");
        save();
        terminal.disableRawMode();
        System.exit(0);
    }


    private static void moveCursor(int key) {
        String line = currentLine();
        switch (key) {
            case ARROW_UP -> {
                if (cursorY > 0) {
                    cursorY--;
                }
            }
            case ARROW_DOWN -> {
                if (cursorY < root.weight+1) { //content.size()
                    cursorY++;
                }
            }
            case ARROW_LEFT -> {
                if (cursorX > 0) {
                    cursorX--;
                }
            }
            case ARROW_RIGHT -> {
                if (line != null && cursorX < line.length()) {
                    cursorX++;
                }
            }
            case PAGE_UP, PAGE_DOWN -> {

                if (key == PAGE_UP) {
                    moveCursorToTopOffScreen();
                } else if (key == PAGE_DOWN) {
                    moveCursorToBottomOffScreen();
                }

                for (int i = 0; i < rows; i++) {
                    moveCursor(key == PAGE_UP ? ARROW_UP : ARROW_DOWN);
                }

            }
            case HOME -> cursorX = 0;
            case END -> {
                if (line != null) {
                    cursorX = line.length();
                }
            }
        }

        String newLine = currentLine();
        if (newLine != null && cursorX > newLine.length()) {
            cursorX = newLine.length();
        }
    }

    private static String currentLine() {
        return cursorY < root.weight+1 ? Rope.getLine(root, cursorY) : null;  //content.size() ? content.get(cursorY) : null
    }

    private static void moveCursorToTopOffScreen() {
        cursorY = offsetY;
    }

    private static void moveCursorToBottomOffScreen() {
        cursorY = offsetY + rows - 1;
        if (cursorY > root.weight+1) cursorY = root.weight+1;  //content.size()) cursorY = content.size()
    }
    private static void insert (char key) {
        String newLine = currentLine();
        //String line = content.get(cursorY);
        if (newLine != null) {
            String editedLine = new StringBuilder(newLine).insert(cursorX, key).toString();
            Rope.setLine(root, cursorY, editedLine);  //content.set(cursorY, editedLine)
            cursorX++;
        }

    }



    private static void save() {
        String[] allLinesTraverse = Rope.allLines(root);
        try(FileWriter myWriter = new FileWriter(my_file.toPath().toString())){
            for (String line : allLinesTraverse) {
                myWriter.write(line + "\n");
            }
            myWriter.close();
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }
    
    private static void insertNewLine() {
        root = Rope.appendEmptyLine(root);        //content.add("");
        cursorY++;
        cursorX = 0;
    }
    
    private static void delete() {
        String line = currentLine();
        String editedLine = new StringBuilder(line).deleteCharAt(cursorX-1).toString();
        Rope.setLine(root, cursorY, editedLine);               //content.set(cursorY, editedLine);
        cursorX--;

    }
}

record WindowSize(int rows, int columns) {

}

