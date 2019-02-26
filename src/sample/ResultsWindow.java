package sample;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.ArrayList;

public class ResultsWindow extends JFrame{

    private JPanel mainPanel;
    private JTable table;
    private JScrollPane scrollPane;

    public ResultsWindow(ArrayList<EbayItem> ebayItems, int itemnumber){
        String col[] = {"Name", "Price", "Shipping", "Condition", "URL"};
        DefaultTableModel tableModel = new DefaultTableModel(col, 0);

        for (int i = 0; i < ebayItems.get(itemnumber).parsedData.size(); i++) {
            String name = ebayItems.get(itemnumber).parsedData.get(i).name;
            String url = ebayItems.get(itemnumber).parsedData.get(i).url;
            String condition = ebayItems.get(itemnumber).parsedData.get(i).condition;
            float price = ebayItems.get(itemnumber).parsedData.get(i).price;
            float shipping = ebayItems.get(itemnumber).parsedData.get(i).shippingPrice;

            Object[] data = {name, price, shipping, condition, url};
            tableModel.addRow(data);
        }

        table.setModel(tableModel);
        getContentPane().add(scrollPane);
        setSize(500,500);

    }
}
