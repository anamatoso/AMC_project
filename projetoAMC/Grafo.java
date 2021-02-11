package projetoAMC;

import java.io.Serializable;
import java.util.ArrayList;

public class Grafo implements Serializable {

	private static final long serialVersionUID = 1L;

	int dim;
	ArrayList<ArrayList<Integer>> adj;


	public Grafo(int dim) {
		this.dim = dim;
		this.adj = new ArrayList<ArrayList<Integer>>(dim); //lista de adjacencia dos pais
		for (int i = 0; i < dim; i++)
			adj.add(new ArrayList<Integer>());
	}

	protected int getDim() { //retorna numero de nos
		return dim;
	}

	public String toString() {
		return "GraphList [dim=" + dim + ", adj=" + adj + "]";
	}

	public void addEdge(int o, int d) throws Exception{ // adiciona aresta onde o é a origem e d o destino 
		if (0 <= o && o < getDim() && 0 <= d && d < getDim()) { // se nao estiver ja ligada e se nao criar ciclos
			if (!edgeQ(o,d) && !connected(d,o)) {
				adj.get(d).add(o);
			}
		}
		else
			throw new Exception("Error in addEdge: vertices should be values " + "between 0 and "+(getDim()-1));
	}

	public void removeEdge(int o, int d) throws Exception{ // remove a aresta onde o é a origem e d o destino 
		if (0 <= o && o < getDim() && 0 <= d && d < getDim()) 
			adj.get(d).remove((Integer) o); 
		else
			throw new Exception("Error in removeEdge: vertices should be values " + "between 0 and "+(getDim()-1));
	}

	public void invertedge(int o, int d) throws Exception{ // inverte a aresta onde o é a origem e d o destino 
		if (0 <= o && o < getDim() && 0 <= d && d < getDim()) { // se nao exister aresta d->o e se nao criar ciclos
			if (edgeQ(o,d)) {
				removeEdge(o,d);
				if (!connected(o,d)) adj.get(o).add(d);
				else {
					adj.get(o).add(d);
				}
			}
		}
		else
			throw new Exception("Error in invertedge: vertices should be values " + "between 0 and "+(getDim()-1));
	}

	boolean edgeQ(int o, int d) throws Exception{
		if (0 <= o && o < getDim() && 0 <= d && d < getDim()) 
			return adj.get(d).contains(o);
		else
			throw new Exception("Error in edgeQ: vertices should be values " + "between 0 and "+(getDim()-1));
	}

	public ArrayList<Integer> parents(int d) throws Exception{
		if (0 <= d && d < getDim()) {
			return adj.get(d);
		}
		else
			throw new Exception("Error in parents: vertices should be values " + "between 0 and "+(getDim()-1));
	}


	public boolean connected(int o, int d) throws Exception{
		// Largura: fila de espera
		if (0 <= o && o < getDim() && 0 <= d && d < getDim()) {
			boolean visited[]        = new boolean[getDim()];
			ArrayList<Integer> queue = new ArrayList<Integer>();
			queue.add(d); // adicionar no final
			boolean found = false;
			while (!queue.isEmpty() && !found) {
				int first = queue.remove(0); //retirar do inicio
				if (first == o)
					found = true;
				if (!visited[first]) {
					visited[first] = true;
					for (int x : parents(first)) 
						queue.add(x);
				}
			}
			return found;
		}
		else
			throw new Exception("Error in connected: vertices should be values "+ "between 0 and "+(getDim()-1));
	}

	protected int[][] distlist(int dominios[]) { //retorna as varias combinaçoes possiveis de variaveis com os dominios no vetor "dominio"
		int d=1;
		for (int i : dominios) {d=d*i;}	//d vsi ser o numero de possiveis combinaçoes entre as variaveis
		int res[][]= new int [d][dominios.length];	//inicializa a matriz que em cada linha tem um possibilidade de combinaçao
		for (int i = 0; i < d; i++) {
			int a=i;
			for (int j = 0; j <dominios.length; j++) {
				int x=a;
				a=a%dominios[dominios.length-1-j];
				res[i][dominios.length-1-j]=a;
				a=(x-a)/dominios[dominios.length-1-j];
			}
		}
		return res;
	}


	protected  double infomut(int pos, Amostrad a) throws Exception{
		if (0 <= pos && pos < getDim()) {
			int np=a.element(0).length;
			int v[]= {pos};
			int c[]={np-1};
			ArrayList<Integer> pais=parents(pos); 	//posiçoes dos pais exceto classe num vetor
			if (pais.contains(np-1)) {
				int pc=pais.indexOf(np-1);
				pais.remove(pc);
			}

			int doms[]=new int[2+pais.size()];	//constroi vetor de dominios
			doms[0]=a.domain(v);
			doms[doms.length-1]=a.domain(c);
			for (int i = 1; i < doms.length-1; i++) {
				int p[]= {pais.get(i-1)};
				doms[i]=a.domain(p);
			}	

			int posi[]=new int[2+pais.size()];	//constroi vetor de posiçao de X, pais e classe
			posi[0]=pos;
			posi[posi.length-1]=np-1;
			for (int i = 1; i < posi.length-1; i++) {
				posi[i]=pais.get(i-1);
			}
			pais.add(np-1);
			int posixc[]= {pos,np-1};	//vetor de posiçoes do x e da classe

			int posipc[]= new int[posi.length-1];
			for (int i = 0; i < posipc.length; i++) {posipc[i]=posi[i+1];}  	//vetor de posiçoes dos pais e da classe

			int posic[]= {np-1};

			int dist[][]=distlist(doms);
			double sum=0;
			for (int[] poss : dist) {
				int cval[]= {poss[poss.length-1]};	//vetor de valor da classe
				int xcval[]= {poss[0], poss[poss.length-1]};	//vetor de valor do x e da classe
				int pcval[]= new int[poss.length-1];
				for (int i = 0; i < pcval.length; i++) {pcval[i]=poss[i+1];} // vetor de valores dos pais e da classe

				if (a.count(posi, poss)*a.count(posic, cval)!=0) {
					sum= sum + (a.count(posi, poss))*(Math.log(((a.count(posi, poss))*(a.count(posic, cval)))/
							((a.count(posixc, xcval))*(a.count(posipc, pcval))))
							/Math.log(2));


				}
			}
			return sum/a.length();
		}
		else
			throw new Exception("Error no infomut: posiçao out of bounds");
	}

	protected double theta(Amostrad a) throws Exception {
		int np = a.element(0).length;
		int c[]= {np-1};
		int dc = a.domain(c);
		int doms[]=new int[np-1];
		for (int i=0; i<doms.length; i++) {
			int x[]={i};
			doms[i]=a.domain(x);	//dom de todas as var menos a classe
		}
		double sum=0;
		for (int i=0; i<doms.length; i++) {
			try {
				ArrayList<Integer> pais=parents(i); 	//arraylist posiçoes dos pais sem classe
				if (pais.contains(np-1)) {
					int pc=pais.indexOf(np-1);
					pais.remove(pc);
				}
				int paispos[]=pais.stream().mapToInt(Integer::intValue).toArray(); // pais em vetor sem classe
				sum=sum+((doms[i]-1)*a.domain(paispos)*dc);
				pais.add(np-1);
			} catch (Exception e) {e.printStackTrace();}
		}

		return sum+dc-1;
	}

	public double MDL (Amostrad a) throws Exception {
		int np=a.element(0).length;
		double sum=0;
		for (int i=0; i<np-1; i++) {
			try {
				sum= sum+infomut(i,a);

			} catch (Exception e) {e.printStackTrace();}
		}
		return (Math.log10(a.length())/Math.log10(2)*theta(a)/2)-(a.length())*sum;
	}


	protected ArrayList<Integer[]> EdgeList() {
		ArrayList<Integer[]> list = new ArrayList<Integer[]>();
		try {
			for (int d=0; d<this.dim;d++) {
				int paispos[]=parents(d).stream().mapToInt(Integer::intValue).toArray();
				for (int p=0; p<paispos.length; p++) {
					Integer[] edge={paispos[p],d};
					list.add(edge);}
			}
		}
		catch (Exception e) {e.printStackTrace();}							
		return list;
	}

	public double MDLdelta (Amostrad a, int o, int d, int op ) throws Exception {
		Grafo galt= new Grafo (this.dim);		
		try {
			if (op==0 || op==1 || op==2) {
				ArrayList<Integer[]> el=this.EdgeList();
				for (int i=0; i<el.size(); i++) {
					Integer[] edge=el.get(i);  		
					galt.addEdge(edge[0],edge[1]); 
				}			
				if (op==0) galt.removeEdge(o, d);
				if (op==1) galt.invertedge(o,d);
				if (op==2) galt.addEdge(o, d);
			}
			else 
				throw new Exception("Error in MDLdelta: int nao corresponde operaçao");

		} catch (Exception e) {e.printStackTrace();}	

		double mdlalt=galt.MDL(a);
		double mdl=this.MDL(a);
		double delta= mdlalt-mdl; //isto tem de ser <0 para o grafo ser mlr
		//		return delta;
		if (delta<0) {this.adj=galt.adj;}
		return delta;
	}




	public static void main(String[] args) {

	}
}
