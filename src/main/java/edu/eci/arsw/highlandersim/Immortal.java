package edu.eci.arsw.highlandersim;

import java.util.List;
import java.util.Random;

public class Immortal extends Thread {

    private ImmortalUpdateReportCallback updateCallback=null;
    
    private int health;
    
    private int defaultDamageValue;

    private final List<Immortal> immortalsPopulation;

    private final String name;

    private final Random r = new Random(System.currentTimeMillis());
    
    private boolean sleep = false;
    private boolean alive = true;
    
    public static final Object tieLock = new Object();
    public static Object monitor = ControlFrame.monitor;
    
    
    public Immortal(String name, List<Immortal> immortalsPopulation, int health, int defaultDamageValue, ImmortalUpdateReportCallback ucb) {
        super(name);
        this.updateCallback=ucb;
        this.name = name;
        this.immortalsPopulation = immortalsPopulation;
        this.health = health;
        this.defaultDamageValue=defaultDamageValue;
    }

    public void run() {

        while (alive) {
            Immortal im;

            int myIndex = immortalsPopulation.indexOf(this);

            int nextFighterIndex = r.nextInt(immortalsPopulation.size());

            //avoid self-fight
            if (nextFighterIndex == myIndex) {
                nextFighterIndex = ((nextFighterIndex + 1) % immortalsPopulation.size());
            }

            im = immortalsPopulation.get(nextFighterIndex);

            this.fight(im);

            try {
            	
            	if(sleep) {
            		synchronized (monitor) {
            			monitor.wait();
            			resumeImmortal();
					}
            	}
            	
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }

    public void die() {
    	this.alive = false;
    }
    
    public void pauseImmortal() {
    	if(!sleep) {
    		sleep = true;
    	}
    }
    
    public void resumeImmortal() {
    	if(sleep) {
    		sleep = false;
    	}
    }
    
    public void transferLife(Immortal i2){
		if (i2.getHealth() > 0) {
            i2.changeHealth(i2.getHealth() - defaultDamageValue);
            this.health += defaultDamageValue;
            updateCallback.processReport("Fight: " + this + " vs " + i2+"\n");
        } else {
        	die();
            updateCallback.processReport(this + " says:" + i2 + " is already dead!\n");
        }
	}
    
    public void fight(Immortal i2) {
    	
		
    	
    	int thisHash = System.identityHashCode(this);
    	int i2Hash = System.identityHashCode(i2);
    	
    	if (thisHash < i2Hash) {
    		synchronized (this) {
    			synchronized (i2) {
    				this.transferLife(i2);
    			}
    		}
    	} else if (thisHash > i2Hash) {
    		synchronized (i2) {
    			synchronized (this) {
    				this.transferLife(i2);
    			}
    		}
    	} else {
    		synchronized (tieLock) {
    			synchronized (this) {
    				synchronized (i2) {
    					this.transferLife(i2);
    				}
    			}
    		}
    	}
    }

    public void changeHealth(int v) {
    	health = v;
    }

    public int getHealth() {
    	return health;
    }

    @Override
    public String toString() {

        return name + "[" + health + "]";
    }

}
