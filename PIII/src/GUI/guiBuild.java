package GUI;

import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import finalproject.Product;
import finalproject.ProjectV1;

public class guiBuild extends JFrame {

	/**
	 * icons from www.iconfinder.com
	 * are free to use!
	 */
	private static final long serialVersionUID = -6025402466206024977L;

	JMenuBar bar;
	LoginListener logList;
	AdminListener AdmList;
	CustListener CustList;
	SearchListener SearchList;
	buyButtonListener BuyList;
	JMenu startMenu;
	JMenu optionsMenu;
	JLabel welcome;
	JButton searchButton;
	JPanel mainPanel;
	GridBagConstraints MainConstraints;
	Component ProdDisplay;


	ProjectV1 project;
	int sessionID = -1;
	int custID = -1;
	boolean admin = false;
	String userName;
	boolean loggedOut = true;
	
	public ImageIcon ImageIcon2(String rel_url) throws IOException{
		InputStream url = this.getClass().getResourceAsStream(rel_url);
		Image image = ImageIO.read(url);
		return new ImageIcon(image);
	}
	
	ImageIcon img = ImageIcon2("/bg.png");
	ImageIcon img2 = ImageIcon2("/Login-Manager.png");
	ImageIcon img3= ImageIcon2("/option.png");


	public guiBuild() throws IOException {
		super("CSC207 Shopping App");	
		setContentPane(new JLabel( img ));
		setLayout(new FlowLayout());


		//creates new project

		project = new ProjectV1();
		welcome = new JLabel();
		welcome.setFont(new Font ("Sanserif", Font.ITALIC, 10));

		//Builds top bar

		bar = new JMenuBar();
		setJMenuBar(bar);

		//Builds start menu
		
		startMenu = new JMenu("Start");
		startMenu.setIcon(img2);
		bar.add(startMenu);
		logList = new LoginListener(this);

		JMenuItem regItem = new JMenuItem("Register");
		regItem.setMnemonic('R');
		startMenu.add(regItem);
		regItem.addActionListener(logList);

		JMenuItem loginItem = new JMenuItem("Log In");
		loginItem.setMnemonic('I');
		startMenu.add(loginItem);
		loginItem.addActionListener(logList);

		// Adds admins!
		project.addUser("Matthew", "pass", true);
		project.addUser("Gal", "pass", true);
		project.addUser("Conrad", "pass", true);
		project.addUser("Vincent", "pass", true);

		//add main panel with search button and products
		mainPanel = new JPanel(new GridBagLayout());
		MainConstraints = new GridBagConstraints();
		MainConstraints.gridwidth = 1;
		MainConstraints.gridheight = 2;

		searchButton = new JButton("Begin Search!");
		SearchList = new SearchListener(this);
		searchButton.addActionListener(SearchList);

		MainConstraints.fill = GridBagConstraints.HORIZONTAL;
		MainConstraints.gridx = 0;
		MainConstraints.gridy = 0;
		MainConstraints.weighty = 1;
		mainPanel.add(searchButton,MainConstraints);

		MainConstraints.fill = GridBagConstraints.HORIZONTAL;
		MainConstraints.gridx = 0;
		MainConstraints.gridy = 10;
		ProdDisplay = DisplayProducts(project.browseShop(0, 0, "PRICEUp"));
		mainPanel.add(ProdDisplay,MainConstraints);

		getContentPane().add(mainPanel);
		mainPanel.revalidate();
	}

	public void updateProds(List<Product> listOfProds){
		mainPanel.remove(ProdDisplay);
		ProdDisplay = DisplayProducts(listOfProds);
		mainPanel.add(ProdDisplay,MainConstraints);
		mainPanel.revalidate();
	}


	public Component DisplayProducts(List<Product> Produce) {
		//get products
		if (Produce.isEmpty()){
			JLabel Output = new JLabel();
			Output.setText("No items available!");
			return Output;
		}
		else{

			//build list of products
			Component Output = new JPanel(new GridBagLayout());
			GridBagConstraints outputConstraints = new GridBagConstraints();
			outputConstraints.gridwidth = 1;
			int counter = 0;
			for (Product p : Produce){
				outputConstraints.fill = GridBagConstraints.HORIZONTAL;
				outputConstraints.gridx = 0;
				outputConstraints.gridy = counter;
				counter++;
				JPanel displayJPanel = new JPanel();
				
				JButton buyButton = new JButton("ADD TO CART");
				
				JLabel displayName = new JLabel();
				JLabel displayCat = new JLabel();
				JLabel displayPrice = new JLabel();
				JLabel displayAvail = new JLabel();
				JLabel Name = new JLabel("Name: ");
				JLabel Cat = new JLabel("Category: ");
				JLabel Price = new JLabel("Price: ");
				JLabel Avail = new JLabel("Availability:");
				
				displayJPanel.setBounds(5, 0, 100, 50);
				displayJPanel.setBorder(BorderFactory.createLineBorder(Color.black, 1));
				displayName.setHorizontalTextPosition(SwingConstants.LEFT);
				displayCat.setHorizontalTextPosition(SwingConstants.LEFT);
				displayPrice.setHorizontalTextPosition(SwingConstants.LEFT);
				displayAvail.setHorizontalTextPosition(SwingConstants.LEFT);
				
				displayName.setText(p.getName());
				displayPrice.setText("$" + p.getPrice() );
				displayCat.setText(p.getCategory().getCategoryName());
				System.out.println(p.getID());
				displayAvail.setText(project.availQ(p.getID())+"");
				
				
				Image ProdImage = new ImageIcon(p.getPicture()).getImage().getScaledInstance(60, 60, Image.SCALE_DEFAULT);
				ImageIcon ImageIcon = new ImageIcon(ProdImage);
				
				displayAvail.setIcon(ImageIcon);
				buyButton.setForeground(Color.RED.brighter());
				buyButton.setBackground(Color.YELLOW.brighter());
				BuyList = new buyButtonListener(this,p);
				buyButton.addActionListener(BuyList);
				
				
				displayJPanel.add(buyButton);
				displayJPanel.add(Name);
				displayJPanel.add(displayName);
				displayJPanel.add(Cat);
				displayJPanel.add(displayCat);
				displayJPanel.add(Price);
				displayJPanel.add(displayPrice);
				displayJPanel.add(Avail);
				displayJPanel.add(displayAvail);
				
				
				Name.setForeground(Color.GREEN);
				Cat.setForeground(Color.GREEN);
				Price.setForeground(Color.GREEN);
				Avail.setForeground(Color.GREEN);
				
				System.out.println("looking for: " + p.getPicture());
				((JPanel) Output).add(displayJPanel, outputConstraints);
			}
			return Output;
		}
	}

	public void guiBuildFromUserLogin(String username){
		
		//clear current start menu
		bar.remove(startMenu);
		startMenu = new JMenu("Welcome shopper, " + username + "!  ");
		startMenu.setIcon(img2);
		logList = new LoginListener(this);
		bar.add(startMenu);

		//add logout option to current session
		JMenuItem logoutItem = new JMenuItem("Log Out");
		startMenu.add(logoutItem);
		logoutItem.addActionListener(logList);

		//add customer options
		JMenu optionsMenu = new JMenu("Options");
		optionsMenu.setIcon(img3);
		bar.add(optionsMenu);

		CustList = new CustListener(this);

		JMenuItem cartItem = new JMenuItem("Go to Cart");
		optionsMenu.add(cartItem);
		cartItem.addActionListener(CustList);

		JMenuItem coItem = new JMenuItem("Checkout Cart");
		optionsMenu.add(coItem);
		coItem.addActionListener(CustList);
		bar.revalidate();

		JMenuItem invoiceItem = new JMenuItem("Get Invoices");
		optionsMenu.add(invoiceItem);
		invoiceItem.addActionListener(CustList);

		
	}
	
	public void logUserIn(String username, String passWord){
		sessionID = project.login(username,passWord);
		userName = username;
		custID = project.getCustIDFromUserName(username);
		loggedOut = false;
		project.getShoppingCart(userName); //refresh shopping cart
		project.clearLoginOutofStockProducts(sessionID);//remove items sold in shop
		String lostItems = project.getLoginOutofStockProducts(sessionID);//get objects lost by above
		int firstDigit = Integer.parseInt(Integer.toString(sessionID).substring(0, 1));
		admin = firstDigit == 1;
		if(!admin){
			JOptionPane.showConfirmDialog(null,lostItems + " lost from cart!","The following items are no longer available and were removed from the cart!", JOptionPane.OK_CANCEL_OPTION);
			System.out.println("worked?");

		}
		updateProds(project.browseShop(0, 0, "PRICEUp")); //refresh displayed items
		System.out.println("User: "+ userName+ " logged in successfully!");
		System.out.println("Customer ID: "+custID);
		System.out.println("session ID: "+sessionID);
		System.out.println("Admin Status?" + admin);
	}

	public void guiBuildFromAdminLogin(String username){

		//clear current start menu
		admin = true;
		loggedOut = false;
		bar.remove(startMenu);
		startMenu = new JMenu("Welcome admin, " + username + "!   ");
		startMenu.setIcon(img2);
		logList = new LoginListener(this);
		bar.add(startMenu);

		//add logout option to current session
		JMenuItem logoutItem = new JMenuItem("Log Out");
		startMenu.add(logoutItem);
		logoutItem.addActionListener(logList);

		//add Admin options
		optionsMenu = new JMenu("Options");
		optionsMenu.setIcon(img3);
		bar.add(optionsMenu);
		AdmList = new AdminListener(this);
		optionsMenu.addActionListener(AdmList);

		JMenuItem catItem = new JMenuItem("Add Category");
		JMenuItem prodItem = new JMenuItem("Add Product");
		JMenuItem wareItem = new JMenuItem("Add A Warehouse");
		JMenuItem mainItem = new JMenuItem("Maintain Product Quantities");
		JMenuItem graphItem = new JMenuItem("Maintain Shipping Graph");
		JMenuItem reportItem = new JMenuItem("Produce Sales Report");


		optionsMenu.add(catItem);
		catItem.addActionListener(AdmList);
		optionsMenu.add(prodItem);
		prodItem.addActionListener(AdmList);
		optionsMenu.add(wareItem);
		wareItem.addActionListener(AdmList);
		optionsMenu.add(mainItem);
		mainItem.addActionListener(AdmList);
		optionsMenu.add(graphItem);
		graphItem.addActionListener(AdmList);
		optionsMenu.add(reportItem);
		reportItem.addActionListener(AdmList);

		optionsMenu.addActionListener(AdmList);
		bar.revalidate();


	}


	public void guiBuildFromLogout() {

		//clear current start menu
		bar.remove(startMenu);
		loggedOut = true;
		admin = false;

		//Builds top bar

		bar = new JMenuBar();
		setJMenuBar(bar);

		//Builds start menu

		startMenu = new JMenu("Start");
		startMenu.setIcon(img2);
		bar.add(startMenu);
		logList = new LoginListener(this);

		JMenuItem regItem = new JMenuItem("Register");
		regItem.setMnemonic('R');
		startMenu.add(regItem);
		regItem.addActionListener(logList);

		JMenuItem loginItem = new JMenuItem("Log In");
		loginItem.setMnemonic('I');
		startMenu.add(loginItem);
		loginItem.addActionListener(logList);
		bar.revalidate();
		updateProds(project.browseShop(0, 0, "PRICEUp"));
		
		

	}


	public boolean hasLoggedOut() {
		return loggedOut;
	}
}





