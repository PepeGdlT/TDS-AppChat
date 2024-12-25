package persistencia;

/*Esta clase implementa un pool para los adaptadores que lo necesiten*/

import java.util.Hashtable;

public enum PoolDAO {
	INSTANCE;
	private Hashtable<Integer, Object> pool;
	
	private PoolDAO() {
		pool = new Hashtable<Integer, Object>();
	}


	
	public Object getObjeto(int id) {
		return pool.get(id);
    }
	public void addObjeto(int id, Object objeto) {
		pool.put(id, objeto);
	}

	public void removeObjeto(int id) {
		pool.remove(id);
	}
	
	public boolean contiene(int id) {
		return pool.containsKey(id);
	}
}
