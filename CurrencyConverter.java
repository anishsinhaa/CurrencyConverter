package com.Projects.CurrencyConverter;

import netscape.javascript.JSObject;
import org.json.JSONException;
import org.json.JSONObject;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DecimalFormat;

public class CurrencyConverter {
    private JPanel panelMain;
    private JLabel title;
    private JLabel title2;
    private JTextField amount2;
    private JComboBox From;
    private JComboBox To;
    private JButton CONVERTButton;
    private JLabel title3;
    private JTextField amount1;
    private static double amt;
    public static String sendHTTPGETRequest(String fromCode,String toCode,double amount) throws IOException, JSONException {
        DecimalFormat df = new DecimalFormat(".00");
        String GET_URL="http://api.exchangeratesapi.io/v1/latest?access_key=6c96081ece3c8ef53445a02dffca6c02";
        URL url = new URL(GET_URL);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("GET");
        int responseCode= con.getResponseCode();
        if(responseCode==HttpURLConnection.HTTP_OK){
            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();
            while((inputLine = in.readLine())!= null){
                response.append(inputLine);
            }in.close();
            JSONObject obj = new JSONObject(response.toString());
            Double fromRate = obj.getJSONObject("rates").getDouble(fromCode);
            Double toRate = obj.getJSONObject("rates").getDouble(toCode);
            double eur = amount/fromRate;
            double toAmount =(eur * toRate);
            System.out.println(df.format(toAmount));
            return (df.format(toAmount));
        }
        else
            System.out.println("Get Request Failed");
            return "";

    }
    public CurrencyConverter() {
        CONVERTButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(amount2.getText()=="" || To.getSelectedItem().toString()=="SELECT" || From.getSelectedItem().toString()=="SELECT")
                    JOptionPane.showMessageDialog(CONVERTButton,"Please fill the form correctly");
                else {
                    amt = Integer.parseInt(amount1.getText());
                    String to = To.getSelectedItem().toString();
                    String from = From.getSelectedItem().toString();
                    try {
                        String res = sendHTTPGETRequest(from,to,amt);
                        amount2.setText(res);
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    } catch (JSONException ex) {
                        throw new RuntimeException(ex);
                    }

                }
            }
        });
        amount2.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                super.keyTyped(e);
                if(Character.isAlphabetic(e.getKeyChar()))
                    e.consume();

            }
        });
        amount1.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                super.keyTyped(e);
                if(Character.isAlphabetic(e.getKeyChar()))
                    e.consume();

            }
        });
    }

    public static void main(String[] args) {
        CurrencyConverter h = new CurrencyConverter();
        JFrame j = new JFrame();
        j.setContentPane(h.panelMain);
        j.setTitle("Converter");
        j.setSize(600,300);
        j.setVisible(true);
        j.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

}


