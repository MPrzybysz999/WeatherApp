package weatherapp;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class WeatherAppGUI extends JFrame {
    
    private JTextField searchField;
    private JButton searchButton;
    private JLabel cityLabel;
    private JLabel temperatureLabel;
    private JLabel descriptionLabel;
    private JLabel weatherIcon;
    private JProgressBar loadingIndicator;
    private JLabel messageLabel;
    private JList<String> searchHistory;
    private DefaultListModel<String> historyModel;
    
    private WeatherService weatherService;

    public WeatherAppGUI() {
        setupFrame();
        createComponents();
        layoutComponents();
        
        // Initialize WeatherService
        weatherService = new WeatherService();
    }
    
    private void setupFrame() {
        setTitle("Weather App");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);
    }
    
    private void createComponents() {
        // Search components
        searchField = new JTextField(20);
        searchField.setToolTipText("Enter city name");
        // Add Enter key listener
        searchField.addActionListener(e -> handleSearch());
        
        searchButton = new JButton("Search");
        searchButton.addActionListener(e -> handleSearch());
        
        // Weather info components
        cityLabel = new JLabel();
        cityLabel.setFont(new Font("Arial", Font.BOLD, 24));
        
        weatherIcon = new JLabel();
        weatherIcon.setPreferredSize(new Dimension(100, 100));
        
        temperatureLabel = new JLabel();
        temperatureLabel.setFont(new Font("Arial", Font.PLAIN, 36));
        
        descriptionLabel = new JLabel();
        descriptionLabel.setFont(new Font("Arial", Font.PLAIN, 18));
        
        // Loading and message components
        loadingIndicator = new JProgressBar();
        loadingIndicator.setIndeterminate(true);
        loadingIndicator.setVisible(false);
        
        messageLabel = new JLabel();
        messageLabel.setVisible(false);
        
        // Search history
        historyModel = new DefaultListModel<>();
        searchHistory = new JList<>(historyModel);
        searchHistory.setPreferredSize(new Dimension(200, 0));
    }
    
    private void layoutComponents() {
        setLayout(new BorderLayout(10, 10));
        
        // Search panel (top)
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        searchPanel.add(searchField);
        searchPanel.add(searchButton);
        add(searchPanel, BorderLayout.NORTH);
        
        // Weather info panel (center)
        JPanel weatherPanel = new JPanel();
        weatherPanel.setLayout(new BoxLayout(weatherPanel, BoxLayout.Y_AXIS));
        weatherPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Add components to weather panel
        Component[] weatherComponents = {
            cityLabel,
            weatherIcon,
            temperatureLabel,
            descriptionLabel,
            loadingIndicator,
            messageLabel
        };
        
        for (Component c : weatherComponents) {
            if (c instanceof JComponent) {
                ((JComponent) c).setAlignmentX(Component.CENTER_ALIGNMENT);
            }
            weatherPanel.add(c);
            weatherPanel.add(Box.createVerticalStrut(10));
        }
        
        add(weatherPanel, BorderLayout.CENTER);
        
        // Search history panel (right)
        JScrollPane historyScroll = new JScrollPane(searchHistory);
        historyScroll.setBorder(BorderFactory.createTitledBorder("Historia wyszukiwania"));
        add(historyScroll, BorderLayout.EAST);
    }
    
    private void handleSearch() {
        String city = searchField.getText().trim();
        if (!city.isEmpty()) {
            loadingIndicator.setVisible(true);
            messageLabel.setVisible(false);
            historyModel.addElement(city);
            
            // Use a separate thread for API call
            new Thread(() -> {
                try {
                    String weatherInfo = weatherService.getWeatherForCity(city);
                    SwingUtilities.invokeLater(() -> {
                        loadingIndicator.setVisible(false);
                        updateWeatherDisplay(weatherInfo);
                    });
                } catch (Exception e) {
                    SwingUtilities.invokeLater(() -> {
                        loadingIndicator.setVisible(false);
                        messageLabel.setText("Error: Could not fetch weather data");
                        messageLabel.setVisible(true);
                    });
                }
            }).start();
        }
    }
    
    private void updateWeatherDisplay(String weatherInfo) {
        // Parse the weather info string (format: "City: XX°C, description")
        String[] parts = weatherInfo.split(":");
        String[] tempAndDesc = parts[1].split(",");
        
        cityLabel.setText(parts[0]);
        temperatureLabel.setText(tempAndDesc[0].trim());
        descriptionLabel.setText(tempAndDesc[1].trim());
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new WeatherAppGUI().setVisible(true);
        });
    }
}
