package sample;

import com.google.gson.JsonArray;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Objects;

public class Main {

    private JFrame frame;
    private JPanel mainPanel;
    private JButton startButton;
    private JTextField inputTextField;
    private JTable results;
    private JScrollPane scroll;
    private JLabel resultCount;
    private JLabel totalResultsCount;
    private EbayItem ebayItem;
    String input;
    String url;
    String name;
    String condition;
    float price;
    float shipping;
    String key;
    int t;


    public Main() {

        try {
            key = new String(Files.readAllBytes(Paths.get("ebay_key.txt")));
        }
        catch (Exception e) {
            System.out.println("Key file exception : " +e);
        }

        String col[] = {"Name", "Condition", "Price", "Shipping", "URL"};
        DefaultTableModel tableModel = new DefaultTableModel(col,0);
        //tableModel.addColumn(col);
        //tableModel.addRow(col);

        results.setModel(tableModel);
        results.setRowHeight(35);

        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                tableModel.setRowCount(0);
                resultCount.setText("");
                totalResultsCount.setText("");

                input = inputTextField.getText();

                ebayItem = new EbayItem(input, key);

                System.out.println("parsed data in main : size " +ebayItem.parsedData.size());
                System.out.println("total result count : " +ebayItem.parsedData.get(0).totalresults);

                for (int i = 0; i < ebayItem.parsedData.size(); i++) {

                    url = ebayItem.parsedData.get(i).url;
                    name = ebayItem.parsedData.get(i).name;
                    condition = ebayItem.parsedData.get(i).condition;
                    price = ebayItem.parsedData.get(i).price;
                    shipping = ebayItem.parsedData.get(i).shippingPrice;

                    Object[] data = {name, condition, price, shipping, url};
                    tableModel.addRow(data);
                }

                resultCount.setText("Rodomų rezultatų kiekis " + tableModel.getRowCount());
                totalResultsCount.setText("Viso rastų rezultatų kiekis " + ebayItem.parsedData.get(0).totalresults);
            }
        });
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Ebay API");
        frame.setContentPane(new Main().mainPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);

    }
}
