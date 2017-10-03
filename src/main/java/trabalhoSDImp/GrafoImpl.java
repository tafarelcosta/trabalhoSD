package trabalhoSDImp;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import org.apache.thrift.TException;
import trabalhoSD.Aresta;
import trabalhoSD.Grafo;
import trabalhoSD.GrafoInterface;
import trabalhoSD.IdentifiqID;
import trabalhoSD.Vertice;
import trabalhoSD.notFaulException;

/**
 * 
 * Implementacao dos metodos criado na interface GrafoInterface, no arquivo Thrift
 * 
 * 
 * @author Tafarel,Augusto
 *
 */

public class GrafoImpl implements GrafoInterface.Iface {

	ConcurrentHashMap<Integer, Vertice> V = new ConcurrentHashMap<>();
	ConcurrentHashMap<IdentifiqID, Aresta> A = new ConcurrentHashMap<>();

	Grafo grafo = new Grafo(V, A);

	@Override
	public boolean removeVertice(int idVertice) throws TException {
        Vertice vt = V.computeIfPresent(idVertice, (a, b) -> {
   
            ArrayList<IdentifiqID> arRemovidas = new ArrayList<>();

            Set<Map.Entry<IdentifiqID, Aresta>> arestas = A.entrySet();
            for (Iterator<Entry<IdentifiqID, Aresta>> i = arestas.iterator(); i.hasNext();) {
                Map.Entry<IdentifiqID, Aresta> ar;
                ar = (Map.Entry<IdentifiqID, Aresta>) i.next();
                Aresta valor = ar.getValue();

                if (valor.getV1().getNome() == idVertice || valor.getV2().getNome() == idVertice) {
                    arRemovidas.add(ar.getKey());
                }
            }

            // Remove as chaves
            for (IdentifiqID i : arRemovidas) {
                A.remove(i);
            }
            V.remove(idVertice);
            
            return b;
        });
        return vt != null;  
	}

	@Override
	public List<Vertice> listaVertices() throws notFaulException, TException {
	       synchronized (V.values()) {
	            if (!V.isEmpty()) {
	                return new ArrayList<Vertice>(V.values());
	            }
	        }
	        throw new notFaulException();
	}

	@Override
	public Aresta buscaAresta(int vertice1, int vertice2) throws notFaulException, TException {

		IdentifiqID vai = new IdentifiqID(vertice1, vertice2, true);
		IdentifiqID vai1 = new IdentifiqID(vertice1, vertice2, false);
		IdentifiqID vai2 = new IdentifiqID(vertice2, vertice1, false);

		Aresta aresta1 = A.computeIfPresent(vai, (IdentifiqID a, Aresta b) -> {
			return b;
		});
		if (aresta1 != null)
			return aresta1;

		Aresta aresta2 = A.computeIfPresent(vai1, (IdentifiqID a, Aresta b) -> {
			return b;
		});
		if (aresta2 != null)
			return aresta2;

		Aresta aresta3 = A.computeIfPresent(vai2, (IdentifiqID a, Aresta b) -> {
			return b;
		});
		if (aresta3 != null)
			return aresta3;

		throw new notFaulException();

	}

	@Override
	public Vertice buscaVertice(int nomedoVertice) throws notFaulException, TException {
		Vertice v = V.computeIfPresent(nomedoVertice, (a, b) -> {
			return b;
		});
		if (v != null)
			return v;

		throw new notFaulException();
	}

	@Override
	public boolean addVertice(Vertice vertice) throws TException {

		if (vertice.getNome() >= 0 && vertice.getPeso() >= 0) {
			if (V.putIfAbsent(vertice.nome, vertice) == null) {
				return true;
			}
		}
		return false;

	}

	@Override
	public boolean addAresta(Aresta aresta) throws TException {

		Aresta ar;

		if (V.containsKey(aresta.getV1().getNome()) && V.containsKey(aresta.getV2().getNome())
				&& aresta.getV1().getNome() != aresta.getV2().getNome()) {

			synchronized (aresta) {
				try {
					ar = buscaAresta(aresta.getV1().getNome(), aresta.getV2().getNome());
					return false;
				} catch (notFaulException ex) {
					IdentifiqID ida = new IdentifiqID(aresta.getV1().nome, aresta.getV2().nome, aresta.flag);
					if (A.putIfAbsent(ida, aresta) == null)
						return true;
				}
			}
		}
		return false;
	}

	@Override
	public boolean removeAresta(int vertice1, int vertice2) throws TException {
		IdentifiqID vai = new IdentifiqID(vertice1, vertice2, true);
		IdentifiqID ida1 = new IdentifiqID(vertice1, vertice2, false);
		IdentifiqID ida2 = new IdentifiqID(vertice2, vertice1, false);

		Aresta ar1 = A.computeIfPresent(vai, (IdentifiqID a, Aresta b) -> {
			A.remove(vai);
			return b;
		});
		if (ar1 != null)
			return true;

		Aresta ar2 = A.computeIfPresent(ida1, (IdentifiqID a, Aresta b) -> {
			A.remove(ida1);
			return b;
		});
		if (ar2 != null)
			return true;

		Aresta ar3 = A.computeIfPresent(ida2, (IdentifiqID a, Aresta b) -> {
			A.remove(ida2);
			return b;
		});
		if (ar3 != null)
			return true;

		return false;
	}

	@Override
	public boolean atualizarAresta(Aresta aresta) throws TException {
		IdentifiqID ida = new IdentifiqID(aresta.getV1().nome, aresta.getV2().nome, aresta.flag);
		synchronized (aresta) {
			try {
				Aresta ar = buscaAresta(aresta.getV1().nome, aresta.getV2().nome);

				A.replace(ida, ar, aresta);
				return true;
			} catch (notFaulException ex) {
				return false;
			}
		}
	}

	@Override
	public boolean atualizarVertice(Vertice vertice) throws TException {
		Vertice vtice = V.computeIfPresent(vertice.getNome(), (Integer a, Vertice b) -> {
			V.replace(vertice.getNome(), b, vertice);
			return b;
		});

		return vtice != null;
	}

	@Override
	public List<Aresta> arestasVertice(Vertice v) throws TException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Aresta> arestasDoVertice(int n) throws TException {

		ArrayList<Aresta> resp = new ArrayList<>();

		synchronized (A.values()) {
			Set<Map.Entry<IdentifiqID, Aresta>> arestas = A.entrySet();
			for (Iterator i = arestas.iterator(); i.hasNext();) {
				Map.Entry<IdentifiqID, Aresta> ar;
				ar = (Map.Entry<IdentifiqID, Aresta>) i.next();
				Aresta valor = ar.getValue();

				// Adiciona no arraylist as chaves a serem removidas
				if (!valor.flag) {
					if (valor.getV1().getNome() == n || valor.getV2().getNome() == n) {
						resp.add(valor);
					}
				} else if (valor.getV1().getNome() == n) {
					resp.add(valor);
				}

			}
			if (!resp.isEmpty()) {
				return resp;
			}
		}

		throw new notFaulException();
	}

	@Override
	public List<Aresta> listaArestas() throws notFaulException, TException {
		synchronized (A.values()) {
			if (!A.isEmpty()) {
				return new ArrayList(A.values());
			}
		}
		throw new notFaulException();
	}

	@Override
	public List<Vertice> verticesVizinhos(int n) throws notFaulException, TException {

		try {
			ArrayList<Aresta> arestas = (ArrayList<Aresta>) arestasDoVertice(n);
			synchronized (arestas) {
				ArrayList<Vertice> resp = new ArrayList<>();
				for (Aresta arest : arestas) {
					if (arest.getV1().getNome() != n) {
						resp.add(arest.getV1());
					} else {
						resp.add(arest.getV2());
					}
				}
				return resp;
			}
		} catch (notFaulException ex) {
			throw new notFaulException();
		}

	}

}
