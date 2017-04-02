package ship;

import java.util.Random;


import port.Berth;
import port.Port;
import port.PortException;
import warehouse.Warehouse;

public class Ship implements Runnable {

	private volatile boolean stopThread = false;

	private String name;
	private Port port;
	private Warehouse shipWarehouse;

	public Ship(String name, Port port, int shipWarehouseSize) {
		this.name = name;
		this.port = port;
		shipWarehouse = new Warehouse(shipWarehouseSize);
	}

	/*
	 * public void setContainersToWarehouse(List<Container> containerList) {
	 * shipWarehouse.addContainer(containerList); }
	 */

	public Warehouse getShipWarehouse() {
		return shipWarehouse;
	}

	public String getName() {
		return name;
	}

	public void stopThread() {
		stopThread = true;
	}

	public void run() {
		try {

			while (!stopThread) {
				atSea();
				inPort();
			}
		} catch (InterruptedException e) {
			System.out.println("С кораблем случилась неприятность.");
		} catch (PortException e) {
			System.out.println("С портом случилась неприятность.");
		}
	}

	private void atSea() throws InterruptedException {
		Thread.sleep(1000);
	}

	private void inPort() throws PortException, InterruptedException {

		boolean isLockedBerth = false;
		Berth berth = null;
		try {
			isLockedBerth = port.lockBerth(this);

			if (isLockedBerth) {
				berth = port.getBerth(this);
				System.out.println("Корабль " + name + " пришвартовался к причалу "
						+ berth.getId());
				ShipAction action = getNextAction();
				// #changed
				if ((this.getShipWarehouse().getRealSize() != 0 ) ){
					executeAction(action, berth);
				} else if (action.equals(ShipAction.LOAD_FROM_PORT)){
					executeAction(action, berth); 
				}
				
			} else {
				System.out.println("Кораблю " + name
						+ " отказано в швартовке к причалу ");
			}
		} finally {
			if (isLockedBerth) {
				port.unlockBerth(this);
				System.out.println("Корабль " + name + " отошел от причала "
						+ berth.getId());
			}
		}

	}

	private void executeAction(ShipAction action, Berth berth)
			throws InterruptedException {
		switch (action) {
		case LOAD_TO_PORT:
			loadToPort(berth);
			break;
		case LOAD_FROM_PORT:
			loadFromPort(berth);
			break;
		}
	}

	private boolean loadToPort(Berth berth) throws InterruptedException {

		int containersNumberToMove = containersCount(this.getShipWarehouse().getRealSize());		 // #Changed
		boolean result = false;

		System.out.println("Корабль " + name + " хочет загрузить "
				+ containersNumberToMove + " контейнеров на склад порта.");

		result = berth.add(shipWarehouse, containersNumberToMove);

		if (!result) {
			System.out.println("Недостаточно места на складе порта для выгрузки кораблем "
					+ name + " " + containersNumberToMove + " контейнеров.");
		} else {
			System.out.println("Корабль " + name + " выгрузил "
					+ containersNumberToMove + " контейнеров в порт.");

		}
		return result;
	}

	private boolean loadFromPort(Berth berth) throws InterruptedException {

		int containersNumberToMove = containersCount(port.getPortWarehouse().getRealSize()); 		 // #Changed

		boolean result = false;

		System.out.println("Корабль " + name + " хочет загрузить "
				+ containersNumberToMove + " контейнеров со склада порта.");

		result = berth.get(shipWarehouse, containersNumberToMove);

		if (result) {
			System.out.println("Корабль " + name + " загрузил "
					+ containersNumberToMove + " контейнеров из порта.");
		} else {
			System.out.println("Недостаточно места на на корабле " + name
					+ " для погрузки " + containersNumberToMove
					+ " контейнеров из порта.");
		}

		return result;
	}

	private int containersCount(int count) {		 // #Changed
		Random random = new Random();
		if (count == 1) { 							// #Changed
			return 1;								 // #Changed
		} else if (count == 0) {								 // #Changed
			return 0;								// #Changed
		} else {
			return random.nextInt(count-1)+1;		 // #Changed
		}										 // #Changed
		
	}

	private ShipAction getNextAction() {
		Random random = new Random();
		int value = random.nextInt(4000);
		if (value < 1000) {
			return ShipAction.LOAD_TO_PORT;
		} else if (value < 2000) {
			return ShipAction.LOAD_FROM_PORT;
		}
		return ShipAction.LOAD_TO_PORT;
	}

	enum ShipAction {
		LOAD_TO_PORT, LOAD_FROM_PORT
	}
}
