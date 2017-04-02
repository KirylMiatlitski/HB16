package main;

import java.util.ArrayList;
import java.util.List;

import port.Port;
import ship.Ship;
import warehouse.Container;

public class Main {

	public static void main(String[] args) throws InterruptedException {
		int warehousePortSize = 15;
		List<Container> containerList = new ArrayList<Container>(warehousePortSize);
		for (int i=0; i<warehousePortSize; i++){
			containerList.add(new Container(i));
		}
		
		Port port = new Port(2, 90);
		port.getPortWarehouse().addContainer(containerList);
		
		
		containerList = new ArrayList<Container>(warehousePortSize);
		for (int i=0; i<warehousePortSize; i++){
			containerList.add(new Container(i+30));
		}
		Ship ship1 = new Ship("Ship1", port, 90);
		ship1.getShipWarehouse().addContainer(containerList);
		
		containerList = new ArrayList<Container>(warehousePortSize);
		for (int i=0; i<warehousePortSize; i++){
			containerList.add(new Container(i+60));
		}
		Ship ship2 = new Ship("Ship2", port, 90);
		ship2.getShipWarehouse().addContainer(containerList);
		
		containerList = new ArrayList<Container>(warehousePortSize);
		for (int i=0; i<warehousePortSize; i++){
			containerList.add(new Container(i+60));
		}
		Ship ship3 = new Ship("Ship3", port, 90);
		ship3.getShipWarehouse().addContainer(containerList);	
		
		
		new Thread(ship1).start();		
		new Thread(ship2).start();		
		new Thread(ship3).start();
		

		Thread.sleep(9000);
		
//		ship1.interrupt();
	//	ship2.interrupt();
		//ship3.interrupt();
		ship1.stopThread();
		ship2.stopThread();
		ship3.stopThread();
		
/*		Thread.sleep(1000);
		System.out.println("Ship1 содержит "+ship1.getShipWarehouse().getRealSize()+ " контейнеров");
		System.out.println("Ship2 содержит "+ship2.getShipWarehouse().getRealSize()+ " контейнеров");
		System.out.println("Ship3 содержит "+ship3.getShipWarehouse().getRealSize()+ " контейнеров");
		System.out.println("Port содержит "+port.getPortWarehouse().getRealSize()+ " контейнеров");
		System.out.println("Было всего 60 контейнеров на всех объектах,  сейчас  "+(port.getPortWarehouse().getRealSize()+ship1.getShipWarehouse().getRealSize()+ship2.getShipWarehouse().getRealSize()+ship3.getShipWarehouse().getRealSize())+ " контейнеров");
*/	}

}
