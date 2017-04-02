package port;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;


import ship.Ship;
import warehouse.Warehouse;

public class Port {
	
	private BlockingQueue<Berth> berthList; // очередь причалов
	
	private Warehouse portWarehouse; // хранилище порта
	
	private Map<Ship, Berth> usedBerths; // какой корабль у какого причала стоит

	public Port(int berthSize, int warehouseSize) {
		portWarehouse = new Warehouse(warehouseSize); // создаем пустое хранилище
		berthList = new ArrayBlockingQueue<Berth>(berthSize); // создаем очередь причалов
		for (int i = 0; i < berthSize; i++) { // заполняем очередь причалов непосредственно самими причалами
			berthList.add(new Berth(i, portWarehouse));
		}
		usedBerths = new HashMap<Ship, Berth>(); // создаем объект, который будет
		// хранить связь между кораблем и причалом
		System.out.println("Порт создан.");
	}
	
	/*public void setContainersToWarehouse(List<Container> containerList){
		portWarehouse.addContainer(containerList);
	}*/
	
	public Warehouse getPortWarehouse(){
		return portWarehouse;
	}

	public boolean lockBerth(Ship ship) {
		Berth berth;
		try {
			berth = berthList.take();
			synchronized (usedBerths) { // #Changed
				usedBerths.put(ship, berth);
			} // #Changed
		} catch (InterruptedException e) {
			System.out.println("Кораблю " + ship.getName() + " отказано в швартовке.");
			return false;
		}		
		return true;
	}
	
	/*public Berth lockBerth(Ship ship) {
		Berth berth = null;
		try {
			berth = berthList.take();
			usedBerths.put(ship, berth);
		} catch (InterruptedException e) {
			System.out.println("Кораблю " + ship.getName() + " отказано в швартовке.");
			return null;
		}		
		return berth;
	}*/
	
	
	public boolean unlockBerth(Ship ship) {
		Berth berth = usedBerths.get(ship);
		
		try {
			berthList.put(berth);
			synchronized (usedBerths) { 	// #Changed
				usedBerths.remove(ship);
			}								// #Changed
		} catch (InterruptedException e) {
			System.out.println("Корабль " + ship.getName() + " не смог отшвартоваться.");
			return false;
		}		
		return true;
	}
	
	public Berth getBerth(Ship ship) throws PortException {
		
		Berth berth = usedBerths.get(ship);
		if (berth == null){
			throw new PortException("Try to use Berth without blocking.");
		}
		return berth;		
	}
}
