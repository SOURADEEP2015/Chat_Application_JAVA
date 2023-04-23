
import java.io.BufferedReader;
import java.io.PrintWriter;
import java.net.*;
import java.io.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class Client {
    Socket socket;
    BufferedReader br;
    PrintWriter out;

    private JLabel heading = new JLabel("Client Area");
    private JTextArea message = new JTextArea();
    private JTextField messageInput = new JTextField();
    private Font font = new Font("Roboto", Font.PLAIN, 20);

    public Client() {
        try {

            System.out.println("Sending Req to server");
            socket = new Socket("127.0.0.1", 7777);
            System.out.println("Connection done");
            br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream());
            createGUI();
            handleEvent();
            startReading();
            // startWriting();

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

    }

    private void handleEvent() {
        messageInput.addKeyListener(new KeyListener() {

            @Override
            public void keyTyped(KeyEvent e) {
                // TODO Auto-generated method stub
                // throw new UnsupportedOperationException("Unimplemented method 'keyTyped'");
            }

            @Override
            public void keyPressed(KeyEvent e) {
                // TODO Auto-generated method stub
                // throw new UnsupportedOperationException("Unimplemented method 'keyPressed'");
            }

            @Override
            public void keyReleased(KeyEvent e) {
                // TODO Auto-generated method stub
                System.out.println("Key Released" + e.getKeyChar());
                if (e.getKeyCode() == 10) {
                    String contentToSend = messageInput.getText();
                    message.append("Me: " + contentToSend + "\n");
                    out.println(contentToSend);
                    out.flush();
                    messageInput.setText("");
                }
                // throw new UnsupportedOperationException("Unimplemented method
                // 'keyReleased'");
            }

        });
    }

    private void createGUI() {
        try {
            JFrame frame = new JFrame();
            frame.setVisible(true);
            frame.setTitle("Client Messanger");
            frame.setSize(500, 500);
            frame.setLocationRelativeTo(null);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            heading.setFont(font);
            message.setFont(font);
            messageInput.setFont(font);
            heading.setHorizontalAlignment(SwingConstants.CENTER);
            heading.setHorizontalTextPosition(SwingConstants.CENTER);
            heading.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
            messageInput.setHorizontalAlignment(SwingConstants.CENTER);

            // creating Layout
            frame.setLayout(new BorderLayout());
            frame.add(heading, BorderLayout.NORTH);
            frame.add(message, BorderLayout.CENTER);
            frame.add(messageInput, BorderLayout.SOUTH);

        } catch (Exception e) {
            System.out.println(e.getLocalizedMessage());
        }
    }

    public void startReading() {
        // thread - to raed data
        Runnable r1 = () -> {
            System.out.println("Reader Start");
            while (true) {
                try {
                    String msg = br.readLine();
                    if (msg.equals("exit")) {
                        JOptionPane.showMessageDialog(heading, "Server terminated the chat!!!");
                        messageInput.setEnabled(false);
                        System.out.println("Server terminated the chat");
                        socket.close();
                        break;
                    }
                    System.out.println("Server : " + msg);
                    message.append("Server: " + msg + "\n");
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
            }
        };
        new Thread(r1).start();
    }

    public void startWriting() {
        // thread - get data from user and then send it to client\\
        Runnable r2 = () -> {
            System.out.println("Writer Started");
            while (true) {
                try {
                    BufferedReader br1 = new BufferedReader(new InputStreamReader((System.in)));
                    String content = br1.readLine();
                    out.println(content);
                    out.flush();
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
            }
        };

        // Start the Thrad..
        new Thread(r2).start();
    }

    public static void main(String[] args) {
        System.out.println("This is Client");
        new Client();
    }
}
