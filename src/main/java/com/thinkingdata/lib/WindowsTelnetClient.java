package com.thinkingdata.lib;



import java.io.IOException;  
import java.io.InputStream;  
import java.io.OutputStream;  
import java.net.SocketException;  
  
import org.apache.commons.net.telnet.TelnetClient;

/**
 * @author Fernando Zhu
 * @version 1.0
 * @date 2020/5/21 17:45
 */
public class WindowsTelnetClient {  
    /** Telnet服务器返回的字符集 */  
    private static final String SRC_CHARSET = "ISO8859-1";  
  
    /** 转换后的字符集 */  
    private static final String DEST_CHARSET = "GBK";  
  
    /** 
     * 终端类型。包括以下类型：VT102、VT100、VT220、WYSE50、WYSE60、XTERM、SCOANSI、ANSI、LINUX、 
     * VSHELL几种。经测试，对于Windows的Telnet服务器，只有VT100、ANSI类型会造成中文乱码 
     */  
    private static final String TERM_TYPE = "VT220";  
  
    private TelnetClient client = new TelnetClient(TERM_TYPE);// Telnet客户端  
    private InputStream input; // Telnet输入流，用于获取Telnet服务器的返回信息  
    private OutputStream output; // Telnet输出流，用于向服务器发送命令  
    private String hostname; // IP地址或主机名  
    private int port = 23; // 端口。默认为23  
    private String username; // 用户名  
    private String password; // 密码  
    private String prompt; // 命令提示符，用于判断是否读取到了返回信息的结尾  
  
    /** 
     * 创建Telnet客户端，用于连接Windows的Telnet服务器。使用默认端口：23 
     *  
     * @param hostname 
     *            - IP地址，或主机名 
     * @param username 
     *            - 用户名 
     * @param password 
     *            - 密码 
     */  
    public WindowsTelnetClient(String hostname, String username, String password) {  
        this.hostname = hostname;  
        this.username = username;  
        this.password = password;  
    }  
  
    /** 
     * 创建Telnet客户端，用于连接Windows的Telnet服务器 
     *  
     * @param hostname 
     *            - IP地址，或主机名 
     * @param port 
     *            - 端口 
     * @param username 
     *            - 用户名 
     * @param password 
     *            - 密码 
     */  
    public WindowsTelnetClient(String hostname, int port, String username, String password) {  
        this.hostname = hostname;  
        this.port = port;  
        this.username = username;  
        this.password = password;  
    }  
  
    /** 
     * 连接到Telnet服务器 
     *  
     * @return - Telnet服务器的返回信息。截止到password： 
     * @throws SocketException 
     * @throws IOException 
     */  
    public String connect() throws SocketException, IOException {  
        client.connect(hostname, port);  
        input = client.getInputStream();  
        output = client.getOutputStream();  
        // 因为不知道服务器返回的是Login： 还是 login： ，所以忽略l  
        String loginOutput = readTo("ogin: ");  
        output.write((username + "\r\n").getBytes());  
        output.flush();  
        // 因为不知道服务器返回的是Password： 还是 password： ，所以忽略p  
        String passwordOutput = readTo("assword: ");  
        output.write((password + "\r\n").getBytes());  
        output.flush();  
        String promptOutput = readTo(">");  
        // 取倒数4位字符作为提示符，因为提示符最短为4位，如：C:\>  
        prompt = promptOutput.substring(promptOutput.length() - 4);  
        return loginOutput + passwordOutput + password + promptOutput;  
    }  
  
    /** 
     * 向Telnet服务器发送命令 
     *  
     * @param command 
     *            - 命令 
     * @return - 执行命令后，在命令行输出的信息 
     * @throws IOException 
     */  
    public String sendCommand(String command) throws IOException {  
        output.write(command.getBytes());  
        output.write('\r');  
        output.write('\n');  
        output.flush();  
        return readToPrompt();  
    }  
  
    /** 
     * 断开连接 
     *  
     * @return - 断开连接的命令 
     */  
  
    public String disconnect() {  
        try {  
            input.close();  
            output.close();  
            client.disconnect();  
        } catch (Exception e) {  
        }  
  
        return "exit";  
    }  
  
    /** 
     * 读取后指定的字符处 
     *  
     * @param end 
     *            - 指定的字符 
     * @return - 从上次读取的位置，到<code>end</code>位置的输出内容 
     */  
    private String readTo(String end) {  
        StringBuffer sb = new StringBuffer();  
  
        char endChar = end.charAt(end.length() - 1);  
        char chr;  
        try {  
            while (true) {  
                chr = (char) input.read();  
                sb.append(chr);  
                if (chr == endChar && sb.toString().endsWith(end)) {  
                    return new String(sb.toString().getBytes(SRC_CHARSET), DEST_CHARSET); // 编码转换，解决中文乱码问题  
                }  
            }  
        } catch (IOException e) {  
            e.printStackTrace();  
        }  
  
        return "";  
    }  
  
    /** 
     * 读取后命令提示符 
     *  
     * @return - 从上次读取的位置，到命令提示符的输出内容 
     */  
    private String readToPrompt() {  
        return readTo(prompt);  
    }  
    public static void main(String[] args) throws Exception {  
        String hostname = "127.0.0.1"; 
        int port = 23;  
        String username = "Administrator";  
        String password = "Zw93#Ly93";  
        WindowsTelnetClient client = new WindowsTelnetClient(hostname, port, username, password);  
        System.out.print(client.connect());  
        System.out.print(client.sendCommand("C:\\Users\\Administrator\\Desktop\\startChromeNode.bat")); // 执行批处理脚本  
        System.out.print(client.disconnect());  
    }  
}  
