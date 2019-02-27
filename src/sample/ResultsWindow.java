package sample;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileWriter;
import java.net.URI;
import java.util.ArrayList;

public class ResultsWindow extends JFrame{

    private JPanel mainPanel;
    private JTable table;
    private JScrollPane scrollPane;
    private JButton exportButton;

    public ResultsWindow(ArrayList<EbayItem> ebayItems, int itemnumber){
        String col[] = {"Name", "Price", "Shipping", "Condition", "URL"};
        DefaultTableModel tableModel = new DefaultTableModel(col, 0){
            @Override
            public Class<?> getColumnClass(int col) {

                Class retVal = Object.class;

                if(getRowCount() > 0)
                    retVal =  getValueAt(0, col).getClass();

                return retVal;
            }
            @Override
            public boolean isCellEditable(int row, int column){
                return false;
            }
        };
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

        exportButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                if (fileChooser.showSaveDialog(ResultsWindow.this) == JFileChooser.APPROVE_OPTION){
                    String fileName = fileChooser.getSelectedFile().getName();
                    String filePath = fileChooser.getSelectedFile().getParentFile().getPath();
                    int fileLength = fileName.length();
                    String extension = "";
                    String file = "";

                    if (fileLength > 4) extension = fileName.substring(fileLength-5, fileLength);

                    if (extension.equals(".xls")){
                        file = filePath + "\\" + fileName;
                    }
                    else {
                        file = filePath + "\\" + fileName + ".xls";
                    }
                    export(table, new File(file));
                }
            }
        });

        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    if (table.getSelectedColumn() == 4) {
                        URI link = URI.create(table.getValueAt(table.getSelectedRow(),4).toString());
                        openLink(link);
                    }

                }
            }
        });

        getContentPane().add(mainPanel);
        getContentPane().revalidate();
        getContentPane().repaint();
        setSize(500,500);
    }

    public void export(JTable table, File file){
        try{
            TableModel model = table.getModel();
            FileWriter writer = new FileWriter(file);

            for (int i = 0; i < model.getColumnCount(); i++){
                writer.write(model.getColumnName(i) +  "\t");
            }
            writer.write("\n");
            for (int i = 0; i < model.getRowCount(); i++){
                for(int j = 0; j < model.getColumnCount(); j++) {
                    writer.write(model.getValueAt(i,j).toString() + "\t");
                }
                writer.write("\n");
            }
            writer.close();
        }
        catch (Exception e){
            System.out.println("File export error : " +e);
        }
    }
    public void openLink(URI uri){
        if (Desktop.isDesktopSupported()){
            try {
                Desktop.getDesktop().browse(uri);
            }
            catch (Exception e) {
                System.out.println("Browse error : " +e);
            }
        }
    }
}
