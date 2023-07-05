package com.khasra;

import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.sql.Timestamp;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.Select;

import com.khasra.constants.Constants;
import com.khasra.pojo.Khasra;
import com.khasra.util.Util;

public class MainApplication {
	private static Properties prop = new Properties();
	private static int i = 0;
	private static List<Khasra> list;
	private static String  filename;

	static {
		FileInputStream ip;
		try {
			ip = new FileInputStream(Constants.CONFIG_FILE);
			prop.load(ip);
		} catch (Exception e) {
			e.printStackTrace();
		}
		list = Util.getInstance().readFile(prop);
	}

	@SuppressWarnings("deprecation")
	public static void main(String[] args) {

		System.setProperty(Constants.WEBDRIVER_PROPERTY, prop.getProperty(Constants.CHROME_WEBDRIVER_PATH));
		WebDriver driver = new ChromeDriver();

		driver.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);
		driver.manage().window().maximize();

		login(driver);

		while (Util.getInstance().isOnLoginErrorPage(driver)) {
			login(driver);
		}

		handleFormAfterLogin(driver);
		
		WebElement link = null;
		if(prop.getProperty(Constants.IS_DELETE).equals("1")) {
			link = driver.findElement(By.xpath("/html/body/div[2]/div/div/div[4]/a"));
		}
		else {
			link = driver.findElement(By.xpath("//*[@id='link2']/a"));
		}
		
		link.click();

		Timestamp timestamp = new Timestamp(System.currentTimeMillis());
		filename = String.valueOf(timestamp.getTime());

		while ( i < list.size()) {
			Khasra khasra = list.get(i);
			
			if(khasra.isDelete()) {
				performDeleteLogic(driver, khasra);
				i++;
			}
			else if (khasra.isRiktBhare()) {
				performRiktBhareLogic(driver, khasra);
				i++;
			} else if (!khasra.isMultiple()) {
				performMainLogicForSingleFasal(driver, khasra);
				i++;
			} else {
				int size = khasra.getSize();
				String[] areaArr = new String[size];
				String[] fasalArr = new String[size];
				String[] techArr = new String[size];

				for (int j = 0; j < size; j++) {
					khasra = list.get(i);
					areaArr[j] = khasra.getArea();
					fasalArr[j] = khasra.getFasal_name();
					techArr[j] = khasra.getAgriTech();
					i++;
				}
				performMainLogicForMultipleFasal(driver, khasra, areaArr, fasalArr, techArr,size);
			}
		}
		
		wait(Constants.TIME_500_MS);

		driver.close();
	}

	private static void performDeleteLogic(WebDriver driver, Khasra khasra) {
		printStart();
		
		Util.getInstance().handleButtonForm(khasra.getNum(), driver);
		System.out.println("Khasra number : " + khasra.getNum());
		Util.getInstance().writeDataInFile("Khasra number : " + khasra.getNum(), filename, prop);
		
		WebElement btn = driver.findElement(By.xpath("//*[@id='sgw']/button"));
		btn.click();
		
		if (handleMultipleOption(driver, khasra)) {
			WebElement btn2 = driver.findElement(By.xpath("//*[@id='case_frm']/button"));
			btn2.click();
			
			try {
				WebElement submitBtn = driver.findElement(By.xpath("//*[@id='tab-3']/form/input[5]"));
				submitBtn.submit();
			} catch (Exception e) {
				goBack(driver);
			}

			System.out.println(khasra.getNum()+" Deleted");
			writeDataInFile(khasra.getNum()+" Deleted", filename);

			wait(Constants.TIME_500_MS);
			
		}
	
		printEnd();
	}

	private static void performRiktBhareLogic(WebDriver driver, Khasra khasra) {

		printStart();

		Util.getInstance().handleButtonForm(khasra.getNum(), driver);
		System.out.println("Khasra number : " + khasra.getNum());
		Util.getInstance().writeDataInFile("Khasra number : " + khasra.getNum(), filename, prop);

		WebElement btn = driver.findElement(By.xpath("//*[@id='sgw']/button"));
		btn.click();

		if (handleMultipleOption(driver, khasra)) {

			WebElement btn2 = driver.findElement(By.xpath("//*[@id='case_frm']/button[2]"));
			btn2.click();
			
			WebElement agriArea = driver.findElement(By.xpath("//*[@id='agriArea']"));
			String agriAreaValue = agriArea.getAttribute("value");
			System.out.println("Agri Value : " + agriAreaValue);
			Util.getInstance().writeDataInFile("Agri Value : " + agriAreaValue, filename, prop);

			WebElement fasalName = driver.findElement(By.xpath("//*[@id='fasal_name']"));
			Select fasalNameSelect = new Select(fasalName);
			fasalNameSelect.selectByValue(khasra.getFasal_name());
			System.out.println("Fasal Name : " + fasalNameSelect.getFirstSelectedOption().getText());
			writeDataInFile("Fasal Name : " + fasalNameSelect.getFirstSelectedOption().getText(), filename);
			
			agriArea = driver.findElement(By.xpath("//*[@id='agriArea']"));
			agriArea.clear();
			agriArea.sendKeys(agriAreaValue);

			WebElement submitBtn = driver.findElement(By.xpath("//*[@id='tab-3']/form/p/table[3]/tbody/tr/td[1]/input[5]"));
			submitBtn.submit();

			System.out.println("Bhag2 submitted");
			writeDataInFile("Bhag2 submitted", filename);

			wait(Constants.TIME_500_MS);

			goBack(driver);
		}
		printEnd();
	}

	private static void performMainLogicForSingleFasal(WebDriver driver, Khasra khasra) {

		printStart();

		Util.getInstance().handleButtonForm(khasra.getNum(), driver);
		System.out.println("Khasra number : " + khasra.getNum());
		Util.getInstance().writeDataInFile("Khasra number : " + khasra.getNum(), filename, prop);

		WebElement btn = driver.findElement(By.xpath("//*[@id='sgw']/button"));
		btn.click();

		if (handleMultipleOption(driver, khasra)) {

			WebElement btn2 = driver.findElement(By.xpath("//*[@id='case_frm']/button[2]"));
			btn2.click();

			WebElement fasalName = driver.findElement(By.xpath("//*[@id='fasal_name']"));
			Select fasalNameSelect = new Select(fasalName);
			fasalNameSelect.selectByValue(khasra.getFasal_name());
			System.out.println("Fasal Name : " + fasalNameSelect.getFirstSelectedOption().getText());
			Util.getInstance().writeDataInFile("Fasal Name : " + fasalNameSelect.getFirstSelectedOption().getText(), filename, prop);

			WebElement agriTech = driver.findElement(By.xpath("//*[@id='agriTech']"));
			Select agriTechSelect = new Select(agriTech);
			agriTechSelect.selectByValue(khasra.getAgriTech());
			System.out.println("Agri Tech Name : " + agriTechSelect.getFirstSelectedOption().getText());
			Util.getInstance().writeDataInFile("Agri Tech Name : " + agriTechSelect.getFirstSelectedOption().getText(), filename, prop);

			WebElement agriArea = driver.findElement(By.xpath("//*[@id='agriArea']"));
			String agriAreaValue = agriArea.getAttribute("value");
			System.out.println("Agri Value : " + agriAreaValue);
			Util.getInstance().writeDataInFile("Agri Value : " + agriAreaValue, filename, prop);

			WebElement sichitArea = driver.findElement(By.xpath("//*[@id='sichitArea']"));
			sichitArea.clear();
			sichitArea.sendKeys(agriAreaValue);

			WebElement submitBtn = driver.findElement(By.xpath("//*[@id='tab-3']/form/p/table[3]/tbody/tr/td[1]/input[5]"));
			submitBtn.submit();

			System.out.println("Bhag2 submitted");
			writeDataInFile("Bhag2 submitted", filename);

			wait(Constants.TIME_500_MS);

			if (khasra.getRiktBhareCode() != 2) {
				performBhag3(driver);
			}

			goBack(driver);
		}
		printEnd();
	}

	private static void performMainLogicForMultipleFasal(WebDriver driver, Khasra khasra, String[] areaArr, String[] fasalArr, String[] techArr, int size) {

		printStart();

		Util.getInstance().handleButtonForm(khasra.getNum(), driver);
		System.out.println("Khasra number : " + khasra.getNum());
		writeDataInFile("Khasra number : " + khasra.getNum(), filename);

		WebElement btn = driver.findElement(By.xpath("//*[@id='sgw']/button"));
		btn.click();

		if (handleMultipleOption(driver, khasra)) {

			WebElement btn2 = driver.findElement(By.xpath("//*[@id='case_frm']/button[2]"));
			btn2.click();

			for (int j = 0; j < size; j++) {

				WebElement fasalName = driver.findElements(By.xpath("//*[@id='fasal_name']")).get(j);
				Select fasalNameSelect = new Select(fasalName);
				fasalNameSelect.selectByValue(fasalArr[j]);
				System.out.println("Fasal Name : " + fasalNameSelect.getFirstSelectedOption().getText());
				writeDataInFile("Fasal Name : " + fasalNameSelect.getFirstSelectedOption().getText(), filename);

				WebElement agriTech = driver.findElements(By.xpath("//*[@id='agriTech']")).get(j);
				Select agriTechSelect = new Select(agriTech);
				agriTechSelect.selectByValue(techArr[j]);
				System.out.println("Agri Tech Name : " + agriTechSelect.getFirstSelectedOption().getText());
				writeDataInFile("Agri Tech Name : " + agriTechSelect.getFirstSelectedOption().getText(), filename);

				WebElement agriArea = driver.findElements(By.xpath("//*[@id='agriArea']")).get(j);
				String agriAreaValue = areaArr[j];
				agriArea.clear();
				agriArea.sendKeys(agriAreaValue);
				System.out.println("Agri Value : " + agriAreaValue);
				writeDataInFile("Agri Value : " + agriAreaValue, filename);

				WebElement sichitArea = driver.findElements(By.xpath("//*[@id='sichitArea']")).get(j);
				sichitArea.clear();
				sichitArea.sendKeys(agriAreaValue);

				WebElement asichitArea = driver.findElements(By.xpath("//*[@id='asichitArea']")).get(j);
				asichitArea.clear();
				asichitArea.sendKeys("0");

				if (j < (size - 1)) {
					WebElement fasalJodeBtn = driver.findElement(By.xpath("//*[@id='tab-3']/form/p/table[3]/tbody/tr/td[1]/input[4]"));
					fasalJodeBtn.click();
				}
			}

			WebElement submitBtn = driver.findElement(By.xpath("//*[@id='tab-3']/form/p/table[3]/tbody/tr/td[1]/input[5]"));
			submitBtn.submit();

			System.out.println("Bhag2 submitted");
			writeDataInFile("Bhag2 submitted", filename);

			wait(Constants.TIME_500_MS);
			
			if (khasra.getRiktBhareCode() != 2) {
				performBhag3(driver);
			}
			
			goBack(driver);
		}
		printEnd();
	}
	
	private static void wait(int time) {
		try {
			Thread.sleep(time);
		} catch (InterruptedException e) {
			e.printStackTrace();
			 Thread.currentThread().interrupt();
		}
	}

	private static void login(WebDriver driver) {
		driver.get(prop.getProperty(Constants.HOME_PAGE_URL));

		WebElement link = driver.findElement(By.xpath("/html/body/center/main/div/div/ul/li[4]/a"));
		link.click();

		WebElement district = driver.findElement(By.xpath("//*[@id='up_district']"));
		Select districtSelect = new Select(district);
		districtSelect.selectByValue(prop.getProperty(Constants.PROP_DISTRICT));

		WebElement tehsil = driver.findElement(By.xpath("//*[@id='up_tehsil']"));
		Select tehsilSelect = new Select(tehsil);
		tehsilSelect.selectByValue(prop.getProperty(Constants.PROP_TEHSIL));

		WebElement halka = driver.findElement(By.xpath("//*[@id='up_halka']"));
		Select halkaSelect = new Select(halka);
		halkaSelect.selectByValue(prop.getProperty(Constants.PROP_HALKA));

		WebElement password = driver.findElement(By.xpath("//*[@id='password']"));
		password.sendKeys(prop.getProperty(Constants.PROP_PASSWORD));

		WebElement captchaDiv = driver.findElement(By.xpath("//*[@id='CaptchaDiv']"));
		String captcha = captchaDiv.getText();
		System.out.println("Captcha code : " + captcha);

		WebElement captchaInput = driver.findElement(By.xpath("//*[@id='CaptchaInput']"));
		captchaInput.sendKeys(captcha);

		wait(Constants.TIME_50_MS);

		WebElement btn = driver.findElement(By.xpath("/html/body/div/div/div/form/div[8]/button"));
		btn.click();
	}

	private static void handleFormAfterLogin(WebDriver driver) {
		WebElement gram = driver.findElement(By.xpath("//*[@id='gram_name']"));
		Select gramSelect = new Select(gram);
		gramSelect.selectByValue(prop.getProperty(Constants.PROP_GRAM)); 

		WebElement fasalYear = driver.findElement(By.xpath("//*[@id='fasalYear']"));
		Select fasalYearSelect = new Select(fasalYear);
		fasalYearSelect.selectByValue(prop.getProperty(Constants.PROP_FASAL_YEAR));

		WebElement fasal = driver.findElement(By.xpath("//*[@id='fasal']"));
		Select fasalSelect = new Select(fasal);
		fasalSelect.selectByValue(prop.getProperty(Constants.PROP_FASAL));

		if(Util.getInstance().isAlertPresent(driver) ) {
			driver.switchTo().alert().accept();
		}
		
		WebElement btn = driver.findElement(By.xpath("/html/body/div/div/div/form/div[4]/button"));
		btn.click();
	}

	private static void performBhag3(WebDriver driver ) {
		WebElement bhag3Btn = driver.findElement(By.xpath("//*[@id='tab-3']/form/input[1]"));
		bhag3Btn.click();

		wait(Constants.TIME_500_MS);

		WebElement bhag3SubmitBtn = driver.findElement(By.xpath("//*[@id='tab-10']/form/p/table[2]/tbody/tr/td[1]/input[5]"));
		bhag3SubmitBtn.submit();

		System.out.println("Bhag3 submitted");
		writeDataInFile("Bhag3 submitted", filename);

		wait(Constants.TIME_500_MS);
	}
	
	public static void writeDataInFile(String str, String filename) {
		try {
			BufferedWriter bw = new BufferedWriter(new FileWriter(prop.getProperty("output_file_path") + filename + ".txt", true));
			bw.write(str);
			bw.newLine();
			bw.close();
		} catch (Exception e) {
			System.out.println(e);
		}
	}
	
	private static boolean handleMultipleOption(WebDriver driver, Khasra khasra) {
		List<WebElement> allOptions = driver.findElements(By.xpath("//*[@id=\"searchGata\"]/div/div[1]/div/div[2]/ul/li"));
		boolean flagGotIt = false;
		if (allOptions.size() == 1) {
			WebElement radioBtn = driver.findElement(By.xpath("//*[@id='ksn-0']"));
			radioBtn.click();
			flagGotIt = true;
		} else if(khasra.getGataUniqueCode()!=null){
			for (int i = 0; i < allOptions.size(); i++) {
				WebElement option = allOptions.get(i);
				WebElement label = option.findElement(By.xpath("//*[@id=\"searchGata\"]/div/div[1]/div/div[2]/ul/li[" + (i + 1) + "]/label"));
				System.out.println("Label Text : " + label.getText());
				if (label.getText().endsWith(khasra.getGataUniqueCode())) {
					WebElement radioBtn = option.findElement(By.xpath("//*[@id='ksn-" + (i) + "']"));
					radioBtn.click();
					flagGotIt = true;
				}
			}
		}
		else {
			writeDataInFile("Multiple options available for khasra "+(khasra.getNum()), filename);
			for (int i = 0; i < allOptions.size(); i++) {
				WebElement option = allOptions.get(i);
				WebElement label = option.findElement(By.xpath("//*[@id=\"searchGata\"]/div/div[1]/div/div[2]/ul/li[" + (i + 1) + "]/label"));
				writeDataInFile("Label Text : " + label.getText(), filename);
			}
		}

		if (!flagGotIt ) {
			String str = "Failed to find " + (khasra.getNum() + ": " + khasra.getGataUniqueCode());
			System.out.println(str);
			writeDataInFile(str, filename);
			Util.getInstance().pressClearBtn(driver);
		}
		
		return flagGotIt;
	}
	
	private static void goBack(WebDriver driver) {
		WebElement back = driver.findElement(By.xpath("//*[@id='content']/center/header/div/div[7]/div/a"));
		back.click();
	}
	
	private static void printStart(){
		System.out.println("====================Start=========================");
		writeDataInFile("====================Start=========================", filename);
	}
	
	private static void printEnd() {
		System.out.println("====================end=========================");
		writeDataInFile("====================end=========================", filename);
	}
}
