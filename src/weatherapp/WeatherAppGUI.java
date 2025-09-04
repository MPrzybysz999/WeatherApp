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
    private JLabel pressureLabel;
    private JLabel humidityLabel;
    private JLabel feelsLikeLabel;
    private JLabel windLabel;
    
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
        setMinimumSize(new Dimension(600, 400));  // Set minimum window size
        setPreferredSize(new Dimension(800, 600));
        setLocationRelativeTo(null);
    }
    
    private void createComponents() {
        // Search components
        searchField = new JTextField(20);
        searchField.setToolTipText("Enter city name");
        // Add Enter key listener
        searchField.addActionListener(e -> handleSearch());
        
        searchButton = new JButton("Wyszukaj");
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
        
        pressureLabel = new JLabel();
        pressureLabel.setFont(new Font("Arial", Font.PLAIN, 18));
        
        humidityLabel = new JLabel();
        humidityLabel.setFont(new Font("Arial", Font.PLAIN, 18));
        
        feelsLikeLabel = new JLabel();
        feelsLikeLabel.setFont(new Font("Arial", Font.PLAIN, 18));
        
        windLabel = new JLabel();
        windLabel.setFont(new Font("Arial", Font.PLAIN, 18));
        
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
        
        // Center align all labels
        cityLabel.setHorizontalAlignment(SwingConstants.CENTER);
        temperatureLabel.setHorizontalAlignment(SwingConstants.CENTER);
        descriptionLabel.setHorizontalAlignment(SwingConstants.CENTER);
        pressureLabel.setHorizontalAlignment(SwingConstants.CENTER);
        humidityLabel.setHorizontalAlignment(SwingConstants.CENTER);
        feelsLikeLabel.setHorizontalAlignment(SwingConstants.CENTER);
        windLabel.setHorizontalAlignment(SwingConstants.CENTER);
        messageLabel.setHorizontalAlignment(SwingConstants.CENTER);
    }
    
    private void layoutComponents() {
        setLayout(new BorderLayout(10, 10));
        
        // Search panel (top)
        JPanel searchPanel = new JPanel();
        searchPanel.setLayout(new BoxLayout(searchPanel, BoxLayout.X_AXIS));
        searchPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        searchField.setMaximumSize(new Dimension(Integer.MAX_VALUE, searchField.getPreferredSize().height));
        searchPanel.add(searchField);
        searchPanel.add(Box.createRigidArea(new Dimension(10, 0)));
        searchPanel.add(searchButton);
        add(searchPanel, BorderLayout.NORTH);
        
        // Weather info panel (center)
        JPanel weatherPanel = new JPanel(new GridBagLayout());
        weatherPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.weightx = 1.0;
        
        // Center align the panel contents
        JPanel centeringPanel = new JPanel(new GridBagLayout());
        GridBagConstraints centerGbc = new GridBagConstraints();
        centerGbc.gridwidth = GridBagConstraints.REMAINDER;
        centerGbc.anchor = GridBagConstraints.CENTER;
        centerGbc.insets = new Insets(5, 5, 5, 5);
        
        Component[] weatherComponents = {
            cityLabel,
            weatherIcon,
            temperatureLabel,
            descriptionLabel,
            pressureLabel,
            humidityLabel,
            feelsLikeLabel,
            windLabel,
            loadingIndicator,
            messageLabel
        };
        
        for (Component c : weatherComponents) {
            centeringPanel.add(c, centerGbc);
        }
        
        // Add the centering panel to the weather panel
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        weatherPanel.add(centeringPanel, gbc);
        
        // Add glue to push components to top
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        weatherPanel.add(Box.createVerticalGlue(), gbc);
        
        // Wrap weatherPanel in a scroll pane
        JScrollPane weatherScrollPane = new JScrollPane(weatherPanel);
        weatherScrollPane.setBorder(null);
        weatherScrollPane.getVerticalScrollBar().setUnitIncrement(16);
        add(weatherScrollPane, BorderLayout.CENTER);
        
        // Search history panel (right)
        JScrollPane historyScroll = new JScrollPane(searchHistory);
        historyScroll.setBorder(BorderFactory.createTitledBorder("Historia wyszukiwania"));
        historyScroll.setPreferredSize(new Dimension(200, 0));
        add(historyScroll, BorderLayout.EAST);

        // Add component listeners for dynamic font scaling
        addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentResized(java.awt.event.ComponentEvent evt) {
                int width = getWidth();
                int height = getHeight();
                float fontScale = Math.min(width / 800f, height / 600f);
                
                cityLabel.setFont(new Font("Arial", Font.BOLD, (int)(24 * fontScale)));
                temperatureLabel.setFont(new Font("Arial", Font.PLAIN, (int)(36 * fontScale)));
                descriptionLabel.setFont(new Font("Arial", Font.PLAIN, (int)(18 * fontScale)));
                pressureLabel.setFont(new Font("Arial", Font.PLAIN, (int)(18 * fontScale)));
                humidityLabel.setFont(new Font("Arial", Font.PLAIN, (int)(18 * fontScale)));
                feelsLikeLabel.setFont(new Font("Arial", Font.PLAIN, (int)(18 * fontScale)));
                windLabel.setFont(new Font("Arial", Font.PLAIN, (int)(18 * fontScale)));
            }
        });

        pack();
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
    
    private String degreesToDirection(String degrees) {
        try {
            int deg = Integer.parseInt(degrees.trim());
            if (deg >= 337.5 || deg < 22.5) return "północ";
            if (deg >= 22.5 && deg < 67.5) return "północny wschód";
            if (deg >= 67.5 && deg < 112.5) return "wschód";
            if (deg >= 112.5 && deg < 157.5) return "południowy wschód";
            if (deg >= 157.5 && deg < 202.5) return "południe";
            if (deg >= 202.5 && deg < 247.5) return "południowy zachód";
            if (deg >= 247.5 && deg < 292.5) return "zachód";
            if (deg >= 292.5 && deg < 337.5) return "północny zachód";
            return "nieznany";
        } catch (NumberFormatException e) {
            return "nieznany";
        }
    }

    private void updateWeatherDisplay(String weatherInfo) {
        if (weatherInfo.startsWith("Error")) {
            messageLabel.setText(weatherInfo);
            messageLabel.setVisible(true);
            return;
        }

        String[] parts = weatherInfo.split("\\|");
        if (parts.length == 8) {
            cityLabel.setText(parts[0]);
            temperatureLabel.setText(String.format("%.1f°C", Double.parseDouble(parts[1])));
            descriptionLabel.setText(parts[2]);
            pressureLabel.setText(String.format("Ciśnienie: %s hPa", parts[3]));
            humidityLabel.setText(String.format("Wilgotność: %s%%", parts[4]));
            feelsLikeLabel.setText(String.format("Odczuwalna: %.1f°C", Double.parseDouble(parts[5])));
            windLabel.setText(String.format("Wiatr: %.1f m/s, Kierunek: %s", 
                Double.parseDouble(parts[6]), degreesToDirection(parts[7])));
        }
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new WeatherAppGUI().setVisible(true);
        });
    }
}
