package com.khasra.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.openqa.selenium.By;
import org.openqa.selenium.NoAlertPresentException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.khasra.pojo.Khasra;

public class Util {
	
	private static  Util instance = null;
	
	public static Util getInstance() {
		if(instance==null) {
			instance = new Util();
		}
		return instance;
	}
	
	public void writeDataInFile(String str, String filename, Properties prop ) {
		try {
			BufferedWriter bw = new BufferedWriter(new FileWriter(prop.getProperty("output_file_path") + filename + ".txt", true));
			bw.write(str);
			bw.newLine();
			bw.close();
		} catch (Exception e) {
			System.out.println(e);
		}
	}
	
	public List<Khasra> readFile(Properties prop) {
		List<Khasra> list = new ArrayList<Khasra>();
		BufferedReader br = null;
		try {
			br = new BufferedReader(new FileReader(prop.getProperty("data_file_path")));
			String line = "";
			while ((line = br.readLine()) != null) {
				String data[] = line.split(",");
				if(data.length==1) {
					list.add(new Khasra(data[0],null));
				}
				if(data.length==7) {
					list.add(new Khasra(data[0], data[1], data[2], data[3], data[4], data[5], data[6]));
				}
				if(data.length==8) {
					list.add(new Khasra(data[0], data[1], data[2], data[3], data[4], data[5], data[6],data[7]));
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return list;
	}
	
	public  void handleButtonForm(String str, WebDriver driver) {

		String numToXPath[] = new String[10];
		numToXPath[0] = "//*[@id=\"searchGata\"]/div/div[3]/table/tbody/tr[3]/td[2]/a";
		numToXPath[1] = "//*[@id=\"searchGata\"]/div/div[3]/table/tbody/tr[1]/td[1]/a";
		numToXPath[2] = "//*[@id=\"searchGata\"]/div/div[3]/table/tbody/tr[1]/td[2]/a";
		numToXPath[3] = "//*[@id=\"searchGata\"]/div/div[3]/table/tbody/tr[1]/td[3]/a";
		numToXPath[4] = "//*[@id=\"searchGata\"]/div/div[3]/table/tbody/tr[1]/td[4]/a";
		numToXPath[5] = "//*[@id=\"searchGata\"]/div/div[3]/table/tbody/tr[2]/td[1]/a";
		numToXPath[6] = "//*[@id=\"searchGata\"]/div/div[3]/table/tbody/tr[2]/td[2]/a";
		numToXPath[7] = "//*[@id=\"searchGata\"]/div/div[3]/table/tbody/tr[2]/td[3]/a";
		numToXPath[8] = "//*[@id=\"searchGata\"]/div/div[3]/table/tbody/tr[2]/td[4]/a";
		numToXPath[9] = "//*[@id=\"searchGata\"]/div/div[3]/table/tbody/tr[3]/td[1]/a";

		for (char c : str.toCharArray()) {
			WebElement btn = driver.findElement(By.xpath(numToXPath[c - '0']));
			btn.click();
		}
	}
	
	public void pressClearBtn( WebDriver driver) {
		WebElement btn = driver.findElement(By.xpath("//*[@id=\"searchGata\"]/div/div[3]/table/tbody/tr[3]/td[4]/a"));
		btn.click();
	}
	
	public boolean isAlertPresent(WebDriver driver) 
	{ 
	    try 
	    { 
	        driver.switchTo().alert(); 
	        return true; 
	    }  
	    catch (NoAlertPresentException Ex) 
	    { 
	        return false; 
	    }  
	}
	
	public boolean isOnLoginErrorPage(WebDriver driver) {
		try {
			WebDriverWait wait = new WebDriverWait(driver, Duration.ofMillis(5));
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("/html/body/center/h1")));
			WebElement header = driver.findElement(By.xpath("/html/body/center/h1"));
			if ("Please use correct Login ID or Password".equals(header.getText())
					|| "Some Internal Error".equals(header.getText())) {
				return true;
			}
		} catch (TimeoutException e) {
			return false;
		}
		return false;
	}
	
}
