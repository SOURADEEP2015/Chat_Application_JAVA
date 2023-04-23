import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.*;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

class Server {
    // Declaring Variable with type...
    ServerSocket server;
    Socket socket;
    // to read data
    BufferedReader br;
    PrintWriter out;
    private JLabel heading = new JLabel("Server Area");
    private JTextArea message = new JTextArea();
    private JTextField messageInput = new JTextField();
    private Font font = new Font("Roboto", Font.PLAIN, 20);

    // Constructor...
    public Server() {
        try {
            // initiate server
            this.server = new ServerSocket(7777);
            createGUI();
            handleEvent();
            System.out.println("Server is ready to accept connection");
            System.out.println("Waiting...");
            // accepting client req
            socket = server.accept();

            br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream());
           
            startReading();
            //startWriting();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private void handleEvent() {
        messageInput.addKeyListener(new KeyListener() {

            @Override
            public void keyTyped(KeyEvent e) {
                // TODO Auto-generated method stub
            }

            @Override
            public void keyPressed(KeyEvent e) {
                // TODO Auto-generated method stub
                // throw new UnsupportedOperationException("Unimplemented method 'keyPressed'");
            }

            @Override
            public void keyReleased(KeyEvent e) {
                // TODO Auto-generated method stub
                //System.out.println("Key Released" + e.getKeyChar());
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
        JFrame frame = new JFrame();
        frame.setVisible(true);
        frame.setTitle("Server Messanger");
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
    }

    public void startReading() {
        // thread - to raed data
        Runnable r1 = () -> {
            System.out.println("Reader Start");
            while (true) {
                try {
                    String msg = br.readLine();
                    if (msg.equals("exit")) {
                        JOptionPane.showMessageDialog(heading, "CLient terminated the chat!!!");
                        messageInput.setEnabled(false);
                        System.out.println("Client terminated the chat");
                        break;
                    }
                    System.out.println("Client : " + msg);
                    message.append("Client : " + msg);
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
            }
        };
        new Thread(r1).start();
    }

    public void startWriting() {
        // thread - get data from user and then send it to client\
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
        System.out.println("This is Server.. Going to start Server");
        new Server();
    }
}