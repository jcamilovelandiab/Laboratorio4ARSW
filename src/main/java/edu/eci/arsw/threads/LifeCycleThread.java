package edu.eci.arsw.threads;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;
import java.util.logging.Logger;

import edu.eci.arsw.blacklistvalidator.HostBlackListsValidator;
import edu.eci.arsw.spamkeywordsdatasource.HostBlacklistsDataSourceFacade;

public class LifeCycleThread extends Thread {

	private int inf, sup, checkedListsCount;
	private String ipaddress;
	private List<Integer> idServer;
	public static int cantidadFound = 0;
	public AtomicInteger atomicInt;

	public LifeCycleThread(int inf, int sup, String ipaddress, AtomicInteger atomicInt) {
		this.inf = inf;
		this.sup = sup;
		this.ipaddress = ipaddress;
		this.atomicInt = atomicInt;
		checkedListsCount = 0;
		idServer = new ArrayList<Integer>();
		start();
	}

	public int getNServers() {
		return idServer.size();
	}

	public List<Integer> getIdServer() {
		return idServer;
	}

	public int getcheckedListsCount() {
		return checkedListsCount;
	}

	@Override
	public void run() {
		HostBlacklistsDataSourceFacade skds = HostBlacklistsDataSourceFacade.getInstance();
		for (int i = inf; i <= sup; i++) {
			checkedListsCount++;
			if (skds.isInBlackListServer(i, ipaddress)) {
				atomicInt.getAndIncrement();
				idServer.add(i);
			} 
			if(atomicInt.get() == HostBlackListsValidator.BLACK_LIST_ALARM_COUNT){				
					break;
			}
		}
	}
}
